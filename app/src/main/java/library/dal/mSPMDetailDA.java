package library.dal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import library.common.mSPMDetailData;

/**
 * Created by ASUS ZE on 16/11/2016.
 */

public class mSPMDetailDA {
    public mSPMDetailDA(SQLiteDatabase db) {
        mSPMDetailData dt = new mSPMDetailData();
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_CONTACTS + "("
                + dt.Property_intSPMDetailId + " TEXT PRIMARY KEY,"
                + dt.Property_txtNoSPM + " TEXT NULL,"
                + dt.Property_txtLocator + " TEXT NULL,"
                + dt.Property_txtItemCode + " TEXT NULL,"
                + dt.Property_txtItemName + " TEXT NULL,"
                + dt.Property_intQty + " TEXT NULL,"
                + dt.Property_bitStatus + " TEXT NULL,"
                + dt.Property_bitSync + " TEXT  NULL)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // All Static variables

    // Contacts table name
    private static final String TABLE_CONTACTS = new clsHardCode().txtTable_mSPMDetail;

    // Upgrading database
    public void DropTable(SQLiteDatabase db) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void SaveDataMConfig(SQLiteDatabase db, mSPMDetailData data) {
        mSPMDetailData dt = new mSPMDetailData();
        db.execSQL("INSERT OR REPLACE into " + TABLE_CONTACTS + " ("
                + dt.Property_intSPMDetailId
                + "," + dt.Property_txtNoSPM
                + "," + dt.Property_txtLocator
                + "," + dt.Property_txtItemCode
                + "," + dt.Property_txtItemName
                + "," + dt.Property_intQty
                + "," + dt.Property_bitStatus
                + "," + dt.Property_bitSync

                + ") " + "values('"
                + String.valueOf(data.getIntSPMDetailId()) + "','"
                + String.valueOf(data.getTxtNoSPM()) + "','"
                + String.valueOf(data.getTxtLocator()) + "','"
                + String.valueOf(data.getTxtItemCode()) + "','"
                + String.valueOf(data.getTxtItemName()) + "','"
                + String.valueOf(data.getIntQty()) + "','"
                + String.valueOf(data.getBitStatus()) + "','"
                + String.valueOf(data.getBitSync()) + "')");
        // db.insert(TABLE_CONTACTS, null, values);
        // db.close(); // Closing database connection
    }
    public void DeleteAllDataMConfig(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + TABLE_CONTACTS );
        // db.insert(TABLE_CONTACTS, null, values);
        // db.close(); // Closing database connection
    }
    // Getting single contact
    public mSPMDetailData getData(SQLiteDatabase db, int id) {
        mSPMDetailData dt = new mSPMDetailData();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] {
                        dt.Property_intSPMDetailId
                        , dt.Property_txtNoSPM
                        , dt.Property_txtLocator
                        , dt.Property_txtItemCode
                        , dt.Property_txtItemName
                        , dt.Property_intQty
                        , dt.Property_bitStatus
                        , dt.Property_bitSync},
                dt.Property_intSPMDetailId + "=?", new String[] { String.valueOf(id) },
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        mSPMDetailData contact = new mSPMDetailData();
        if (cursor.getCount() > 0) {
            contact.setIntSPMDetailId(cursor.getString(0));
            contact.setTxtNoSPM(cursor.getString(1));
            contact.setTxtLocator(cursor.getString(2));
            contact.setTxtItemCode(cursor.getString(3));
            contact.setTxtItemName(cursor.getString(4));
            contact.setIntQty(cursor.getString(5));
            contact.setBitStatus(cursor.getString(6));
            contact.setBitSync(cursor.getString(7));
            // return contact
        } else {
            contact = null;
        }
        cursor.close();
        return contact;
    }


    // Getting All Contacts
    public List<mSPMDetailData> getAllData(SQLiteDatabase db) {
        List<mSPMDetailData> contactList = new ArrayList<mSPMDetailData>();
        // Select All Query
        mSPMDetailData dt = new mSPMDetailData();
        String selectQuery = "SELECT  " + dt.Property_All + " FROM "
                + TABLE_CONTACTS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                mSPMDetailData contact = new mSPMDetailData();
                contact.setIntSPMDetailId(cursor.getString(0));
                contact.setTxtNoSPM(cursor.getString(1));
                contact.setTxtLocator(cursor.getString(2));
                contact.setTxtItemCode(cursor.getString(3));
                contact.setTxtItemName(cursor.getString(4));
                contact.setIntQty(cursor.getString(5));
                contact.setBitStatus(cursor.getString(6));
                contact.setBitSync(cursor.getString(7));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return contact list
        return contactList;
    }

    // Deleting single contact
    public void deleteContact(SQLiteDatabase db, int id) {
        mSPMDetailData dt = new mSPMDetailData();
        db.delete(TABLE_CONTACTS, dt.Property_intSPMDetailId + " = ?",
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
    public void InsertDefaultmSPMDetail(SQLiteDatabase db) {
        String txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '1','839382','A-001','NBBAS','NUTRIVE BENECOL NO ADDED SUGAR BLACKCURRANT 100 ML','50','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '2','839382','A-002','QWERT','DIABETASOL','25','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '3','839383','A-001','NBBAS','NUTRIVE BENECOL NO ADDED SUGAR BLACKCURRANT 100 ML','50','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '4','839383','A-002','QWERT','DIABETASOL','60','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '5','839384','A-001','NBBAS','NUTRIVE BENECOL NO ADDED SUGAR BLACKCURRANT 100 ML','50','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '6','839384','A-002','QWERT','DIABETASOL','60','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '7','839385','A-001','NBBAS','NUTRIVE BENECOL NO ADDED SUGAR BLACKCURRANT 100 ML','50','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '8','839385','A-002','QWERT','DIABETASOL','60','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '9','839386','A-001','NBBAS','NUTRIVE BENECOL NO ADDED SUGAR BLACKCURRANT 100 ML','50','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '10','839386','A-002','QWERT','DIABETASOL','60','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '11','839387','A-001','NBBAS','NUTRIVE BENECOL NO ADDED SUGAR BLACKCURRANT 100 ML','50','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '12','839387','A-002','QWERT','DIABETASOL','60','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '13','839388','A-001','NBBAS','NUTRIVE BENECOL NO ADDED SUGAR BLACKCURRANT 100 ML','50','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '14','839388','A-002','QWERT','DIABETASOL','60','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '15','839389','A-001','NBBAS','NUTRIVE BENECOL NO ADDED SUGAR BLACKCURRANT 100 ML','50','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '16','839389','A-002','QWERT','DIABETASOL','60','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '17','839390','A-001','NBBAS','NUTRIVE BENECOL NO ADDED SUGAR BLACKCURRANT 100 ML','50','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '18','839390','A-002','QWERT','DIABETASOL','60','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '19','839391','A-001','NBBAS','NUTRIVE BENECOL NO ADDED SUGAR BLACKCURRANT 100 ML','50','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '20','839391','A-002','QWERT','DIABETASOL','60','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '21','839392','A-001','NBBAS','NUTRIVE BENECOL NO ADDED SUGAR BLACKCURRANT 100 ML','50','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '22','839392','A-002','QWERT','DIABETASOL','60','0','0';";
        db.execSQL(txtQuery);
    }
}
