package library.dal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import library.common.mSPMHeaderData;

/**
 * Created by ASUS ZE on 16/11/2016.
 */

public class mSPMHeaderDA {
    public mSPMHeaderDA(SQLiteDatabase db) {
        mSPMHeaderData dt = new mSPMHeaderData();
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_CONTACTS + "("
                + dt.Property_intSPMId + " TEXT PRIMARY KEY,"
                + dt.Property_txtNoSPM + " TEXT NULL,"
                + dt.Property_txtBranchCode + " TEXT NULL,"
                + dt.Property_txtBranchName + " TEXT NULL,"
                + dt.Property_txtSalesOrder + " TEXT NULL,"
                + dt.Property_intUserId + " TEXT NULL,"
                + dt.Property_bitStatus + " TEXT NULL,"
                + dt.Property_bitSync + " TEXT NULL,"
                + dt.Property_bitStart + " TEXT NULL,"
                + dt.Property_dtStart + " TEXT NULL,"
                + dt.Property_dtEnd + " TEXT  NULL)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // All Static variables

    // Contacts table name
    private static final String TABLE_CONTACTS = new clsHardCode().txtTable_mSPMHeader;

    // Upgrading database
    public void DropTable(SQLiteDatabase db) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void SaveDataMConfig(SQLiteDatabase db, mSPMHeaderData data) {
        mSPMHeaderData dt = new mSPMHeaderData();
        db.execSQL("INSERT OR REPLACE into " + TABLE_CONTACTS + " ("
                + dt.Property_intSPMId
                + "," + dt.Property_txtNoSPM
                + "," + dt.Property_txtBranchCode
                + "," + dt.Property_txtBranchName
                + "," + dt.Property_txtSalesOrder
                + "," + dt.Property_intUserId
                + "," + dt.Property_bitStatus
                + "," + dt.Property_bitSync
                + "," + dt.Property_bitStart
                + "," + dt.Property_dtStart
                + "," + dt.Property_dtEnd
                + ") " + "values('"
                + String.valueOf(data.getIntSPMId()) + "','"
                + String.valueOf(data.getTxtNoSPM()) + "','"
                + String.valueOf(data.getTxtBranchCode()) + "','"
                + String.valueOf(data.getTxtBranchName()) + "','"
                + String.valueOf(data.getTxtSalesOrder()) + "','"
                + String.valueOf(data.getIntUserId()) + "','"
                + String.valueOf(data.getBitStatus()) + "','"
                + String.valueOf(data.getBitSync()) + "','"
                + String.valueOf(data.getBitStart()) + "','"
                + String.valueOf(data.getDtStart()) + "','"
                + String.valueOf(data.getDtEnd()) + "')");
        // db.insert(TABLE_CONTACTS, null, values);
        // db.close(); // Closing database connection
    }
    public void DeleteAllDataMConfig(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + TABLE_CONTACTS );
        // db.insert(TABLE_CONTACTS, null, values);
        // db.close(); // Closing database connection
    }
    // Getting single contact
    public mSPMHeaderData getData(SQLiteDatabase db, int id) {
        mSPMHeaderData dt = new mSPMHeaderData();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] {
                        dt.Property_intSPMId
                        , dt.Property_txtNoSPM
                        , dt.Property_txtBranchCode
                        , dt.Property_txtBranchName
                        , dt.Property_txtSalesOrder
                        , dt.Property_intUserId
                        , dt.Property_bitStatus
                        , dt.Property_bitSync
                        , dt.Property_bitStart
                        , dt.Property_dtStart
                        , dt.Property_dtEnd},
                dt.Property_intSPMId + "=?", new String[] { String.valueOf(id) },
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        mSPMHeaderData contact = new mSPMHeaderData();
        if (cursor.getCount() > 0) {
            contact.setIntSPMId(cursor.getString(0));
            contact.setTxtNoSPM(cursor.getString(1));
            contact.setTxtBranchCode(cursor.getString(2));
            contact.setTxtBranchName(cursor.getString(3));
            contact.setTxtSalesOrder(cursor.getString(4));
            contact.setIntUserId(cursor.getString(5));
            contact.setBitStatus(cursor.getString(6));
            contact.setBitSync(cursor.getString(7));
            contact.setBitStart(cursor.getString(8));
            contact.setDtStart(cursor.getString(9));
            contact.setDtEnd(cursor.getString(10));
            // return contact
        } else {
            contact = null;
        }
        cursor.close();
        return contact;
    }

    // Adding new contact
    public void SaveData(SQLiteDatabase db,mSPMHeaderData data) {
        mSPMHeaderData dt=new mSPMHeaderData();
        db.execSQL("INSERT OR REPLACE into "+TABLE_CONTACTS+" ("
                +dt.Property_intSPMId+","
                +dt.Property_txtNoSPM+","
                +dt.Property_txtBranchCode+","
                +dt.Property_txtBranchName+","
                +dt.Property_txtSalesOrder+","
                +dt.Property_intUserId+","
                +dt.Property_bitStatus+","
                +dt.Property_bitSync+","
                +dt.Property_bitStart+","
                +dt.Property_dtStart+","
                +dt.Property_dtEnd+") "+
                "values(" +String.valueOf(data.getIntSPMId())+",'"
                +String.valueOf(data.getTxtNoSPM())+"','"
                +String.valueOf(data.getTxtBranchCode())+"','"
                +String.valueOf(data.getTxtBranchCode())+"','"
                +String.valueOf(data.getTxtSalesOrder())+"','"
                +String.valueOf(data.getIntUserId())+"','"
                +String.valueOf(data.getBitStatus())+"','"
                +String.valueOf(data.getBitSync())+"','"
                +String.valueOf(data.getBitStart())+"','"
                +String.valueOf(data.getDtStart())+"','"
                +String.valueOf(data.getDtEnd())+"')");
    }

    // Getting All Contacts
    public mSPMHeaderData getAllData(SQLiteDatabase db) {
        mSPMHeaderData contact = new mSPMHeaderData();
        // Select All Query
        mSPMHeaderData dt = new mSPMHeaderData();
        String selectQuery = "SELECT  " + dt.Property_All + " FROM "
                + TABLE_CONTACTS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                contact.setIntSPMId(cursor.getString(0));
                contact.setTxtNoSPM(cursor.getString(1));
                contact.setTxtBranchCode(cursor.getString(2));
                contact.setTxtBranchName(cursor.getString(3));
                contact.setTxtSalesOrder(cursor.getString(4));
                contact.setIntUserId(cursor.getString(5));
                contact.setBitStatus(cursor.getString(6));
                contact.setBitSync(cursor.getString(7));
                contact.setBitStart(cursor.getString(8));
                contact.setDtStart(cursor.getString(9));
                contact.setDtEnd(cursor.getString(10));
                // Adding contact to list
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return contact list
        return contact;
    }

    public mSPMHeaderData GetDataByStatus(SQLiteDatabase db) {
        mSPMHeaderData contact = new mSPMHeaderData();
        // Select All Query
        mSPMHeaderData dt = new mSPMHeaderData();
        String selectQuery = "SELECT  " + dt.Property_All + " FROM "
                + TABLE_CONTACTS +" WHERE "+dt.Property_bitSync+"=0";
        //+dt.Property_bitSync+"=1 And "
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                contact.setIntSPMId(cursor.getString(0));
                contact.setTxtNoSPM(cursor.getString(1));
                contact.setTxtBranchCode(cursor.getString(2));
                contact.setTxtBranchName(cursor.getString(3));
                contact.setTxtSalesOrder(cursor.getString(4));
                contact.setIntUserId(cursor.getString(5));
                contact.setBitStatus(cursor.getString(6));
                contact.setBitSync(cursor.getString(7));
                contact.setBitStart(cursor.getString(8));
                contact.setDtStart(cursor.getString(9));
                contact.setDtEnd(cursor.getString(10));
                // Adding contact to list
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return contact list
        return contact;
    }

    public mSPMHeaderData GetDataById(SQLiteDatabase db, String id) {
        mSPMHeaderData contact = new mSPMHeaderData();
        // Select All Query
        mSPMHeaderData dt = new mSPMHeaderData();
        String selectQuery = "SELECT  " + dt.Property_All + " FROM "
                + TABLE_CONTACTS +" WHERE "+dt.Property_txtNoSPM+"='"+id+"'";
        //+dt.Property_bitSync+"=1 And "
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                contact.setIntSPMId(cursor.getString(0));
                contact.setTxtNoSPM(cursor.getString(1));
                contact.setTxtBranchCode(cursor.getString(2));
                contact.setTxtBranchName(cursor.getString(3));
                contact.setTxtSalesOrder(cursor.getString(4));
                contact.setIntUserId(cursor.getString(5));
                contact.setBitStatus(cursor.getString(6));
                contact.setBitSync(cursor.getString(7));
                contact.setBitStart(cursor.getString(8));
                contact.setDtStart(cursor.getString(9));
                contact.setDtEnd(cursor.getString(10));
                // Adding contact to list
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return contact list
        return contact;
    }

    public mSPMHeaderData getAllDataPushData(SQLiteDatabase db) {
        mSPMHeaderData contact = new mSPMHeaderData();
        // Select All Query
        mSPMHeaderData dt = new mSPMHeaderData();
        String selectQuery = "SELECT  " + dt.Property_All + " FROM "
                + TABLE_CONTACTS +" WHERE "+dt.Property_bitSync+"=0 And "+dt.Property_bitStatus+"=1";
        //+dt.Property_bitSync+"=1 And "
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                contact.setIntSPMId(cursor.getString(0));
                contact.setTxtNoSPM(cursor.getString(1));
                contact.setTxtBranchCode(cursor.getString(2));
                contact.setTxtBranchName(cursor.getString(3));
                contact.setTxtSalesOrder(cursor.getString(4));
                contact.setIntUserId(cursor.getString(5));
                contact.setBitStatus(cursor.getString(6));
                contact.setBitSync(cursor.getString(7));
                contact.setBitStart(cursor.getString(8));
                contact.setDtStart(cursor.getString(9));
                contact.setDtEnd(cursor.getString(10));
                // Adding contact to list
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return contact list
        return contact;
    }

    public int updateDataById(SQLiteDatabase db, String id) {
        mSPMHeaderData dt = new mSPMHeaderData();

        ContentValues values = new ContentValues();
        values.put(dt.Property_bitStatus, "1");
        values.put(dt.Property_bitSync, "1");

        // updating row
        return db.update(TABLE_CONTACTS, values, dt.Property_intSPMId + " = ? ",
                new String[] { String.valueOf(id) });
    }

    public int updateDataByIdOffline(SQLiteDatabase db, String id) {
        mSPMHeaderData dt = new mSPMHeaderData();

        ContentValues values = new ContentValues();
        values.put(dt.Property_bitStatus, "1");
        values.put(dt.Property_bitSync, "0");

        // updating row
        return db.update(TABLE_CONTACTS, values, dt.Property_intSPMId + " = ? ",
                new String[] { String.valueOf(id) });
    }

    public int saveDataPush(SQLiteDatabase db, String id) {
        mSPMHeaderData dt = new mSPMHeaderData();

        ContentValues values = new ContentValues();
        values.put(dt.Property_bitSync, "1");

        // updating row
        return db.update(TABLE_CONTACTS, values, dt.Property_intSPMId + " = ? ",
                new String[] { String.valueOf(id) });
    }

    public int updateDataBitStartById(SQLiteDatabase db, String id, String dTime) {
        mSPMHeaderData dt = new mSPMHeaderData();

        ContentValues values = new ContentValues();
        values.put(dt.Property_bitStart, "0");
        values.put(dt.Property_dtStart, dTime);
        // updating row
        return db.update(TABLE_CONTACTS, values, dt.Property_intSPMId + " = ? ",
                new String[] { String.valueOf(id) });
    }

    public int updateDtEndById(SQLiteDatabase db, String id, String dTime) {
        mSPMHeaderData dt = new mSPMHeaderData();

        ContentValues values = new ContentValues();
//        values.put(dt.Property_bitStart, "0");
        values.put(dt.Property_dtEnd, dTime);
        // updating row
        return db.update(TABLE_CONTACTS, values, dt.Property_intSPMId + " = ? ",
                new String[] { String.valueOf(id) });
    }

    // Deleting single contact
    public void deleteContact(SQLiteDatabase db, int id) {
        mSPMHeaderData dt = new mSPMHeaderData();
        db.delete(TABLE_CONTACTS, dt.Property_intSPMId + " = ?",
                new String[] { String.valueOf(id) });
        // db.close();
    }

    // Getting contacts Count
    public int getContactsCount(SQLiteDatabase db) {
        String countQuery = "SELECT * FROM " + TABLE_CONTACTS;
        Cursor cursor = db.rawQuery(countQuery, null);
        int countData = cursor.getCount();
        cursor.close();
        // return count
        return countData;
    }

    public void InsertDefaultSPMHeader(SQLiteDatabase db) {
        String txtQuery = "insert into mSPMHeader(intSPMId,txtNoSPM,txtBranchCode,txtBranchName,txtSalesOrder,intUserId,bitStatus,bitSync,bitStart )"
                + "select  '1','839382','JKT','Jakarta','379483922','114','1','0','1';";
        db.execSQL(txtQuery);
//        txtQuery = "insert into mSPMHeader(intSPMId,txtNoSPM,txtBranchCode,txtBranchName,txtSalesOrder,intUserId,bitStatus,bitSync )"
//                + "select  '2','839383','JKT','Jakarta','379483922','114','0','0';";
//        db.execSQL(txtQuery);
//        txtQuery = "insert into mSPMHeader(intSPMId,txtNoSPM,txtBranchCode,txtBranchName,txtSalesOrder,intUserId,bitStatus,bitSync )"
//                + "select  '3','839384','JKT','Jakarta','379483922','114','0','0';";
//        db.execSQL(txtQuery);
//        txtQuery = "insert into mSPMHeader(intSPMId,txtNoSPM,txtBranchCode,txtBranchName,txtSalesOrder,intUserId,bitStatus,bitSync )"
//                + "select  '4','839385','JKT','Jakarta','379483922','114','0','0';";
//        db.execSQL(txtQuery);
//        txtQuery = "insert into mSPMHeader(intSPMId,txtNoSPM,txtBranchCode,txtBranchName,txtSalesOrder,intUserId,bitStatus,bitSync )"
//                + "select  '5','839386','JKT','Jakarta','379483922','114','0','0';";
//        db.execSQL(txtQuery);
//        txtQuery = "insert into mSPMHeader(intSPMId,txtNoSPM,txtBranchCode,txtBranchName,txtSalesOrder,intUserId,bitStatus,bitSync )"
//                + "select  '6','839387','JKT','Jakarta','379483922','114','0','0';";
//        db.execSQL(txtQuery);
//        txtQuery = "insert into mSPMHeader(intSPMId,txtNoSPM,txtBranchCode,txtBranchName,txtSalesOrder,intUserId,bitStatus,bitSync )"
//                + "select  '7','839388','JKT','Jakarta','379483922','114','0','0';";
//        db.execSQL(txtQuery);
//        txtQuery = "insert into mSPMHeader(intSPMId,txtNoSPM,txtBranchCode,txtBranchName,txtSalesOrder,intUserId,bitStatus,bitSync )"
//                + "select  '8','839389','JKT','Jakarta','379483922','114','0','0';";
//        db.execSQL(txtQuery);
//        txtQuery = "insert into mSPMHeader(intSPMId,txtNoSPM,txtBranchCode,txtBranchName,txtSalesOrder,intUserId,bitStatus,bitSync )"
//                + "select  '9','839390','JKT','Jakarta','379483922','114','0','0';";
//        db.execSQL(txtQuery);
//        txtQuery = "insert into mSPMHeader(intSPMId,txtNoSPM,txtBranchCode,txtBranchName,txtSalesOrder,intUserId,bitStatus,bitSync )"
//                + "select  '10','839391','JKT','Jakarta','379483922','114','0','0';";
//        db.execSQL(txtQuery);
//        txtQuery = "insert into mSPMHeader(intSPMId,txtNoSPM,txtBranchCode,txtBranchName,txtSalesOrder,intUserId,bitStatus,bitSync )"
//                + "select  '11','839392','JKT','Jakarta','379483922','114','0','0';";
//        db.execSQL(txtQuery);
    }
}
