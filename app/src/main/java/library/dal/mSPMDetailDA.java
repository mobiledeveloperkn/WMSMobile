package library.dal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import library.common.mSPMDetailData;
import library.common.mSystemConfigData;

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
                + dt.Property_bitSync + " TEXT NULL,"
                + dt.Property_txtReason + " TEXT NULL,"
                + dt.Property_intUserId + " TEXT NULL,"
                + dt.Property_intFlag + " TEXT NULL,"
                + dt.Property_txtLotNumber + " TEXT NULL,"
                + dt.Property_txtUOM + " TEXT  NULL)";
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
                + "," + dt.Property_txtReason
                + "," + dt.Property_intUserId
                + "," + dt.Property_intFlag
                + "," + dt.Property_txtLotNumber
                + "," + dt.Property_txtUOM

                + ") " + "values('"
                + String.valueOf(data.getIntSPMDetailId()) + "','"
                + String.valueOf(data.getTxtNoSPM()) + "','"
                + String.valueOf(data.getTxtLocator()) + "','"
                + String.valueOf(data.getTxtItemCode()) + "','"
                + String.valueOf(data.getTxtItemName()) + "','"
                + String.valueOf(data.getIntQty()) + "','"
                + String.valueOf(data.getBitStatus()) + "','"
                + String.valueOf(data.getBitSync()) + "','"
                + String.valueOf(data.getTxtReason()) + "','"
                + String.valueOf(data.getIntUserId()) + "','"
                + String.valueOf(data.getIntFlag()) + "','"
                + String.valueOf(data.getTxtLotNumber()) + "','"
                + String.valueOf(data.getTxtUOM()) + "')");
        // db.insert(TABLE_CONTACTS, null, values);
        // db.close(); // Closing database connection
    }

    public void DeleteAllDataMConfig(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + TABLE_CONTACTS);
        // db.insert(TABLE_CONTACTS, null, values);
        // db.close(); // Closing database connection
    }

    // Getting single contact
    public mSPMDetailData getData(SQLiteDatabase db, int id) {
        mSPMDetailData dt = new mSPMDetailData();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{
                        dt.Property_intSPMDetailId
                        , dt.Property_txtNoSPM
                        , dt.Property_txtLocator
                        , dt.Property_txtItemCode
                        , dt.Property_txtItemName
                        , dt.Property_intQty
                        , dt.Property_bitStatus
                        , dt.Property_bitSync
                        , dt.Property_txtReason
                        , dt.Property_intUserId
                        , dt.Property_intFlag
                        , dt.Property_txtLotNumber
                        , dt.Property_txtUOM},
                dt.Property_intSPMDetailId + "=?", new String[]{String.valueOf(id)},
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
            contact.setTxtReason(cursor.getString(8));
            contact.setIntUserId(cursor.getString(9));
            contact.setIntFlag(cursor.getString(10));
            contact.setTxtLotNumber(cursor.getString(11));
            contact.setTxtUOM(cursor.getString(12));
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
        String selectQuery = "SELECT  " + dt.Property_All + " FROM " + TABLE_CONTACTS;
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
                contact.setTxtReason(cursor.getString(8));
                contact.setIntUserId(cursor.getString(9));
                contact.setIntFlag(cursor.getString(10));
                contact.setTxtLotNumber(cursor.getString(11));
                contact.setTxtUOM(cursor.getString(12));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return contact list
        return contactList;
    }

    public List<mSPMDetailData> getAllDataById(SQLiteDatabase db, String id) {
        List<mSPMDetailData> contactList = new ArrayList<mSPMDetailData>();
        // Select All Query
        mSPMDetailData dt = new mSPMDetailData();
        String selectQuery = "SELECT  " + dt.Property_All + " FROM "
                + TABLE_CONTACTS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        int i = 0;

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
                contact.setTxtReason(cursor.getString(8));
                contact.setIntUserId(cursor.getString(9));
                contact.setIntFlag(cursor.getString(10));
                contact.setTxtLotNumber(cursor.getString(11));
                contact.setTxtUOM(cursor.getString(12));
                // Adding contact to list
                contactList.add(contact);
                Log.i("Anjas-dt" + i++, cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return contact list
        return contactList;
    }

    public List<mSPMDetailData> getAllDataPushData(SQLiteDatabase db) {
        List<mSPMDetailData> contactList = null;
        // Select All Query
        mSPMDetailData dt = new mSPMDetailData();
        String selectQuery = "SELECT  " + dt.Property_All + " FROM "
                + TABLE_CONTACTS + " WHERE " + dt.Property_bitStatus + " in (1,2) and " + dt.Property_bitSync + "=0";
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            contactList = new ArrayList<mSPMDetailData>();
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
                contact.setTxtReason(cursor.getString(8));
                contact.setIntUserId(cursor.getString(9));
                contact.setIntFlag(cursor.getString(10));
                contact.setTxtLotNumber(cursor.getString(11));
                contact.setTxtUOM(cursor.getString(12));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return contact list
        return contactList;
    }

    public List<mSPMDetailData> getAllDataTaskPendingNoFilter(SQLiteDatabase db, String id) {
        List<mSPMDetailData> contactList = new ArrayList<mSPMDetailData>();
        // Select All Query

        mSystemConfigData cnf = new mSystemConfigDA(db).getData(db, 1);

        mSPMDetailData dt = new mSPMDetailData();
        String selectQuery = "SELECT  " + dt.Property_All + " FROM "
                + TABLE_CONTACTS + " WHERE " + dt.Property_bitSync + "=0 And " + dt.Property_bitStatus + "=0 And " + dt.Property_txtNoSPM + "='" + id + "' order by intSPMDetailId " + cnf.get_txtValue();
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
                contact.setTxtReason(cursor.getString(8));
                contact.setIntUserId(cursor.getString(9));
                contact.setIntFlag(cursor.getString(10));
                contact.setTxtLotNumber(cursor.getString(11));
                contact.setTxtUOM(cursor.getString(12));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return contact list
        return contactList;
    }

    public List<mSPMDetailData> getAllDataTaskPending(SQLiteDatabase db, String id) {
        List<mSPMDetailData> contactList = new ArrayList<mSPMDetailData>();
        // Select All Query

        mSystemConfigData cnf = new mSystemConfigDA(db).getData(db, 1);
        mSystemConfigData cnf2 = new mSystemConfigDA(db).getData(db, 2);

        mSPMDetailData dt = new mSPMDetailData();
        String selectQuery = "";
        if (cnf2!=null&&!cnf2.get_txtValue().equals("")){
            List<String> listSegment2 = new ArrayList<>();
            if (cnf2.get_txtValue().length()>0){
                String[] words = cnf2.get_txtValue().split(",");
                Collections.addAll(listSegment2, words);
            }
            StringBuilder bd = new StringBuilder();
            for (String segment :listSegment2){
                if (bd.length()==0){
                    bd.append("'"+ segment + "'");
                }else {
                    bd.append(", ");
                    bd.append("'"+ segment + "'");
                }
            }
            selectQuery = "SELECT  " + dt.Property_All + " FROM "
                    + TABLE_CONTACTS + " WHERE "
                    + "(substr(substr(txtLocator, instr(txtLocator, '.')+1), 1, instr(substr(txtLocator, instr(txtLocator, '.')+1), '.')-1) IN (" + bd.toString()
                    + ")) And "
                    + dt.Property_bitSync + "=0 And " + dt.Property_bitStatus + "=0 And "
                    + dt.Property_txtNoSPM + "='" + id
                    + "' order by intSPMDetailId " + cnf.get_txtValue();
        }else {
            selectQuery = "SELECT  " + dt.Property_All + " FROM "
                    + TABLE_CONTACTS + " WHERE " + dt.Property_bitSync + "=0 And " + dt.Property_bitStatus
                    + "=0 And " + dt.Property_txtNoSPM + "='" + id
                    + "' order by intSPMDetailId " + cnf.get_txtValue();
        }

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
                contact.setTxtReason(cursor.getString(8));
                contact.setIntUserId(cursor.getString(9));
                contact.setIntFlag(cursor.getString(10));
                contact.setTxtLotNumber(cursor.getString(11));
                contact.setTxtUOM(cursor.getString(12));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return contact list
        return contactList;
    }



    public List<String> getAllSegment2(SQLiteDatabase db, String id) {
        List<String> contactList = new ArrayList<String>();
        // Select All Query
        mSPMDetailData dt = new mSPMDetailData();
        String selectQuery = "SELECT  "
                + "substr(substr(txtLocator, instr(txtLocator, '.')+1), 1, instr(substr(txtLocator, instr(txtLocator, '.')+1), '.')-1)"
                + " FROM "
                + TABLE_CONTACTS + " WHERE "
                + dt.Property_txtNoSPM + "='" + id
                +"' GROUP BY substr(substr(txtLocator, instr(txtLocator, '.')+1), 1, instr(substr(txtLocator, instr(txtLocator, '.')+1), '.')-1)"
                + " order by substr(substr(txtLocator, instr(txtLocator, '.')+1), 1, instr(substr(txtLocator, instr(txtLocator, '.')+1), '.')-1)";
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                String segment = cursor.getString(0);
                // Adding contact to list
                contactList.add(segment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return contact list
        return contactList;
    }

    public List<mSPMDetailData> getAllDataTaskConfirm(SQLiteDatabase db, String id) {
        List<mSPMDetailData> contactList = new ArrayList<mSPMDetailData>();
        // Select All Query
        mSystemConfigData cnf = new mSystemConfigDA(db).getData(db, 1);
        mSystemConfigData cnf2 = new mSystemConfigDA(db).getData(db, 2);

        mSPMDetailData dt = new mSPMDetailData();
        String selectQuery = "";
        if (cnf2!=null&&!cnf2.get_txtValue().equals("")){
            List<String> listSegment2 = new ArrayList<>();
            if (cnf2.get_txtValue().length()>0){
                String[] words = cnf2.get_txtValue().split(",");
                Collections.addAll(listSegment2, words);
            }
            StringBuilder bd = new StringBuilder();
            for (String segment :listSegment2){
                if (bd.length()==0){
                    bd.append("'"+ segment + "'");
                }else {
                    bd.append(", ");
                    bd.append("'"+ segment + "'");
                }
            }
            selectQuery = "SELECT  " + dt.Property_All + " FROM " + TABLE_CONTACTS + " WHERE "
                    + "(substr(substr(txtLocator, instr(txtLocator, '.')+1), 1, instr(substr(txtLocator, instr(txtLocator, '.')+1), '.')-1) IN (" + bd.toString()
                    + ")) And "
                    + dt.Property_bitStatus + "=1 And " + dt.Property_txtNoSPM + "='" + id + "' ORDER BY intSPMDetailId " + cnf.get_txtValue();
        }else {
            selectQuery = "SELECT  " + dt.Property_All + " FROM " + TABLE_CONTACTS + " WHERE " + dt.Property_bitStatus + "=1 And " + dt.Property_txtNoSPM + "='" + id + "' ORDER BY intSPMDetailId " + cnf.get_txtValue();
        }
//        String selectQuery = "SELECT  " + dt.Property_All + " FROM " + TABLE_CONTACTS + " WHERE " + dt.Property_bitStatus + "=1 And " + dt.Property_txtNoSPM + "='" + id + "' ORDER BY intSPMDetailId " + cnf.get_txtValue();
        //+dt.Property_bitSync+"=1 And "
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        int i = 0;
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
                contact.setTxtReason(cursor.getString(8));
                contact.setIntUserId(cursor.getString(9));
                contact.setIntFlag(cursor.getString(10));
                contact.setTxtLotNumber(cursor.getString(11));
                contact.setTxtUOM(cursor.getString(12));
                // Adding contact to list
                contactList.add(contact);

            } while (cursor.moveToNext());
        }
        cursor.close();
        // return contact list
        return contactList;
    }

    public List<mSPMDetailData> getAllDataTaskCancel(SQLiteDatabase db, String id) {
        mSystemConfigData cnf = new mSystemConfigDA(db).getData(db, 1);
        mSystemConfigData cnf2 = new mSystemConfigDA(db).getData(db, 2);
        List<mSPMDetailData> contactList = new ArrayList<mSPMDetailData>();
        // Select All Query
        mSPMDetailData dt = new mSPMDetailData();
        String selectQuery = "";
        if (cnf2!=null&&!cnf2.get_txtValue().equals("")){
            List<String> listSegment2 = new ArrayList<>();
            if (cnf2.get_txtValue().length()>0){
                String[] words = cnf2.get_txtValue().split(",");
                Collections.addAll(listSegment2, words);
            }
            StringBuilder bd = new StringBuilder();
            for (String segment :listSegment2){
                if (bd.length()==0){
                    bd.append("'"+ segment + "'");
                }else {
                    bd.append(", ");
                    bd.append("'"+ segment + "'");
                }
            }
            selectQuery = "SELECT  " + dt.Property_All + " FROM "
                    + TABLE_CONTACTS + " WHERE "
                    + "(substr(substr(txtLocator, instr(txtLocator, '.')+1), 1, instr(substr(txtLocator, instr(txtLocator, '.')+1), '.')-1) IN (" + bd.toString()
                    + ")) And "
                    + dt.Property_bitStatus + "=2 And " + dt.Property_txtNoSPM + "='" + id + "' ORDER BY intSPMDetailId " + cnf.get_txtValue();
        }else {
            selectQuery = "SELECT  " + dt.Property_All + " FROM "
                    + TABLE_CONTACTS + " WHERE " + dt.Property_bitStatus + "=2 And " + dt.Property_txtNoSPM + "='" + id + "' ORDER BY intSPMDetailId " + cnf.get_txtValue();
        }
//        String selectQuery = "SELECT  " + dt.Property_All + " FROM "
//                + TABLE_CONTACTS + " WHERE " + dt.Property_bitStatus + "=2 And " + dt.Property_txtNoSPM + "='" + id + "' ORDER BY intSPMDetailId " + cnf.get_txtValue();
        //+dt.Property_bitSync+"=1 And "
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
                contact.setTxtReason(cursor.getString(8));
                contact.setIntUserId(cursor.getString(9));
                contact.setIntFlag(cursor.getString(10));
                contact.setTxtLotNumber(cursor.getString(11));
                contact.setTxtUOM(cursor.getString(12));
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
                new String[]{String.valueOf(id)});
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

    public void updateDataById(SQLiteDatabase db, String id, String intUserId) {
        mSPMDetailData dt = new mSPMDetailData();

        ContentValues values = new ContentValues();
        values.put(dt.Property_bitStatus, "1");
        values.put(dt.Property_bitSync, "1");
        values.put(dt.Property_intUserId, intUserId);

        // updating row
        db.update(TABLE_CONTACTS, values, dt.Property_intSPMDetailId + " = ? ",
                new String[]{String.valueOf(id)});
    }

    public int updateDataRevertById(SQLiteDatabase db, String id, String intUserId) {
        mSPMDetailData dt = new mSPMDetailData();

        ContentValues values = new ContentValues();
        values.put(dt.Property_bitStatus, "0");
        values.put(dt.Property_bitSync, "0");
        values.put(dt.Property_intUserId, intUserId);

        // updating row
        return db.update(TABLE_CONTACTS, values, dt.Property_intSPMDetailId + " = ? ",
                new String[]{String.valueOf(id)});
    }

    public int saveDataPush(SQLiteDatabase db, String id, String status, String sync) {
        mSPMDetailData dt = new mSPMDetailData();

        ContentValues values = new ContentValues();
        values.put(dt.Property_bitSync, sync);
        values.put(dt.Property_bitStatus, status);

        // updating row
        return db.update(TABLE_CONTACTS, values, dt.Property_intSPMDetailId + " = ? ",
                new String[]{String.valueOf(id)});
    }

    public int updateDataByIdOffline(SQLiteDatabase db, String id, String intUserId) {
        mSPMDetailData dt = new mSPMDetailData();

        ContentValues values = new ContentValues();
        values.put(dt.Property_bitStatus, "1");
        values.put(dt.Property_bitSync, "0");
        values.put(dt.Property_intUserId, intUserId);

        // updating row
        return db.update(TABLE_CONTACTS, values, dt.Property_intSPMDetailId + " = ? ",
                new String[]{String.valueOf(id)});
    }

    public int updateDataSPMCancelById(SQLiteDatabase db, String id, String _intUserId, String reason) {
        mSPMDetailData dt = new mSPMDetailData();

        ContentValues values = new ContentValues();
        values.put(dt.Property_bitStatus, "2");
        values.put(dt.Property_bitSync, "1");
        values.put(dt.Property_txtReason, reason);
        values.put(dt.Property_intUserId, _intUserId);

        // updating row
        return db.update(TABLE_CONTACTS, values, dt.Property_intSPMDetailId + " = ? ",
                new String[]{String.valueOf(id)});
    }

    public int updateDataSPMCancelByIdOffline(SQLiteDatabase db, String id, String _intUserId, String reason) {
        mSPMDetailData dt = new mSPMDetailData();

        ContentValues values = new ContentValues();
        values.put(dt.Property_bitStatus, "2");
        values.put(dt.Property_bitSync, "0");
        values.put(dt.Property_txtReason, reason);
        values.put(dt.Property_intUserId, _intUserId);

        // updating row
        return db.update(TABLE_CONTACTS, values, dt.Property_intSPMDetailId + " = ? ",
                new String[]{String.valueOf(id)});
    }

    public void InsertDefaultmSPMDetail(SQLiteDatabase db) {
        String txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '1','839382','A-001','NBBAS','NUTRIVE BENECOL NO ADDED SUGAR BLACKCURRANT 100 ML','50','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '2','839382','A-002','QWERT','DIABETASOL','25','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '3','839382','A-001','NBBAS','NUTRIVE BENECOL NO ADDED SUGAR BLACKCURRANT 100 ML','50','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '4','839382','A-002','QWERT','DIABETASOL','60','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '5','839382','A-001','NBBAS','NUTRIVE BENECOL NO ADDED SUGAR BLACKCURRANT 100 ML','50','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '6','839382','A-002','QWERT','DIABETASOL','60','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '7','839382','A-001','NBBAS','NUTRIVE BENECOL NO ADDED SUGAR BLACKCURRANT 100 ML','50','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '8','839382','A-002','QWERT','DIABETASOL','60','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '9','839382','A-001','NBBAS','NUTRIVE BENECOL NO ADDED SUGAR BLACKCURRANT 100 ML','50','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '10','839382','A-002','QWERT','DIABETASOL','60','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '11','839382','A-001','NBBAS','NUTRIVE BENECOL NO ADDED SUGAR BLACKCURRANT 100 ML','50','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '12','839382','A-002','QWERT','DIABETASOL','60','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '13','839382','A-001','NBBAS','NUTRIVE BENECOL NO ADDED SUGAR BLACKCURRANT 100 ML','50','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '14','839382','A-002','QWERT','DIABETASOL','60','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '15','839382','A-001','NBBAS','NUTRIVE BENECOL NO ADDED SUGAR BLACKCURRANT 100 ML','50','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '16','839382','A-002','QWERT','DIABETASOL','60','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '17','839382','A-001','NBBAS','NUTRIVE BENECOL NO ADDED SUGAR BLACKCURRANT 100 ML','50','0','0';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mSPMDetail(intSPMDetailId,txtNoSPM,txtLocator,txtItemCode,txtItemName,intQty,bitStatus,bitSync )"
                + "select  '18','839382','A-002','QWERT','DIABETASOL','60','0','0';";
        db.execSQL(txtQuery);
    }
}
