package library.dal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import library.common.tUserLoginData;

/**
 * Created by ASUS ZE on 16/11/2016.
 */

public class tUserLoginDA {
    public tUserLoginDA(SQLiteDatabase db) {
        tUserLoginData dt = new tUserLoginData();
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_CONTACTS + "("
                + dt.Property_IntUserId + " TEXT PRIMARY KEY,"
                + dt.Property_txtUserName + " TEXT NULL,"
                + dt.Property_txtPassword + " TEXT NULL,"
                + dt.Property_IntUserRole + " TEXT NULL,"
                + dt.Property_txtRoleName + " TEXT NULL,"
                + dt.Property_dtLastLogin + " TEXT  NULL)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // All Static variables

    // Contacts table name
    private static final String TABLE_CONTACTS = new clsHardCode().txtTable_mUser;

    // Upgrading database
    public void DropTable(SQLiteDatabase db) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void SaveDataMConfig(SQLiteDatabase db, tUserLoginData data) {
        tUserLoginData dt = new tUserLoginData();
        db.execSQL("INSERT OR REPLACE into " + TABLE_CONTACTS + " ("
                + dt.Property_IntUserId
                + "," + dt.Property_txtUserName
                + ","+ dt.Property_txtPassword
                + ","+ dt.Property_IntUserRole
                + ","+ dt.Property_dtLastLogin
                + ","+ dt.Property_txtRoleName
                + ") " + "values('"
                + String.valueOf(data.getIntUserId()) + "','"
                + String.valueOf(data.getTxtUserName()) + "','"
                + String.valueOf(data.getTxtPassword()) + "','"
                + String.valueOf(data.getIntUserRole()) + "','"
                + String.valueOf(data.getTxtRoleName()) + "','"
                + String.valueOf(data.getDtLastLogin()) + "')");
        // db.insert(TABLE_CONTACTS, null, values);
        // db.close(); // Closing database connection
    }
    public void DeleteAllDataMConfig(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + TABLE_CONTACTS );
        // db.insert(TABLE_CONTACTS, null, values);
        // db.close(); // Closing database connection
    }
    // Getting single contact
    public tUserLoginData getData(SQLiteDatabase db, int id) {
        tUserLoginData dt = new tUserLoginData();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] {
                        dt.Property_IntUserId
                        , dt.Property_txtUserName
                        , dt.Property_txtPassword
                        , dt.Property_IntUserRole
                        , dt.Property_txtRoleName
                        , dt.Property_dtLastLogin},
                dt.Property_IntUserId + "=?", new String[] { String.valueOf(id) },
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        tUserLoginData contact = new tUserLoginData();
        if (cursor.getCount() > 0) {
            contact.setIntUserId(cursor.getString(0));
            contact.setTxtUserName(cursor.getString(1));
            contact.setTxtPassword(cursor.getString(2));
            contact.setIntUserRole(cursor.getString(3));
            contact.setTxtRoleName(cursor.getString(4));
            contact.setDtLastLogin(cursor.getString(5));
            // return contact
        } else {
            contact = null;
        }
        cursor.close();
        return contact;
    }


    // Getting All Contacts
    public List<tUserLoginData> getAllData(SQLiteDatabase db) {
        List<tUserLoginData> contactList = new ArrayList<tUserLoginData>();
        // Select All Query
        tUserLoginData dt = new tUserLoginData();
        String selectQuery = "SELECT  " + dt.Property_All + " FROM "
                + TABLE_CONTACTS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                tUserLoginData contact = new tUserLoginData();
                contact.setIntUserId(cursor.getString(0));
                contact.setTxtUserName(cursor.getString(1));
                contact.setTxtPassword(cursor.getString(2));
                contact.setIntUserRole(cursor.getString(3));
                contact.setTxtRoleName(cursor.getString(4));
                contact.setDtLastLogin(cursor.getString(5));
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
        tUserLoginData dt = new tUserLoginData();
        db.delete(TABLE_CONTACTS, dt.Property_IntUserId + " = ?",
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

    public void InsertDefaultMUser(SQLiteDatabase db) {
        String txtQuery = "insert into tUserLogin(IntUserId,txtUserName,txtPassword,IntUserRole,txtRoleName,dtLastLogin )"
                + "select  '114','rheza.tesar','sanghiang','100','Picker','2016-11-21';";
        db.execSQL(txtQuery);
    }

    public boolean CheckLoginNow(SQLiteDatabase db) throws ParseException {
        // Select All Query
        tUserLoginData dt=new tUserLoginData();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        String selectQuery = "SELECT  strftime('%Y-%m-%d',"+dt.Property_dtLastLogin+") as "+dt.Property_dtLastLogin+" FROM " + TABLE_CONTACTS +"  LIMIT 1";

        Cursor cursor = db.rawQuery(selectQuery, null);
        boolean result=false;
        if (cursor.moveToFirst()) {
            do {
                String datetime = cursor.getString(0);
                String a = dateFormat.format(cal.getTime());
                if(datetime!=null){
                    if(dateFormat.format(cal.getTime()).compareTo(datetime)==0){
                        result=true;
                        break;
                    }
                }
            } while (cursor.moveToNext());
        }


        cursor.close();
        // return contact list
        return result;
    }

//    public List<tUserLoginData> GetInUserRole(SQLiteDatabase db, String data) {
//        List<tUserLoginData> contactList = null;
//        // Select All Query
//        tUserLoginData dt=new tUserLoginData();
//        String selectQuery = "SELECT  "+dt.Property_All+" FROM " + TABLE_CONTACTS +" WHERE "+dt.Property_IntUserRole +"='"+data+"'";
//
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            contactList=new ArrayList<tUserLoginData>();
//            do {
//                tUserLoginData contact = new tUserLoginData();
//                contact.setIntUserId(cursor.getString(0));
//                contact.setTxtUserName(cursor.getString(1));
//                contact.setTxtPassword(cursor.getString(2));
//                contact.setIntUserRole(cursor.getString(3));
//                contact.setTxtRoleName(cursor.getString(4));
//                // Adding contact to list
//                contactList.add(contact);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        // return contact list
//        return contactList;
//    }

//    public List<tUserLoginData> GetBranchByRegion(SQLiteDatabase db, String region) {
//        List<tUserLoginData> contactList = null;
//        // Select All Query
//        mBranchData dt=new mBranchData();
//        String selectQuery = "SELECT  "+dt.Property_All+" FROM " + TABLE_CONTACTS +" WHERE "+dt.Property_txtRegion +"='"+region+"'";
//
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            contactList=new ArrayList<mBranchData>();
//            do {
//                mBranchData contact = new mBranchData();
//                contact.set_uuId(cursor.getString(0));
//                contact.set_txtBranchID(cursor.getString(1));
//                contact.set_txtBranchName(cursor.getString(2));
//                contact.set_txtRegion(cursor.getString(3));
//                // Adding contact to list
//                contactList.add(contact);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        // return contact list
//        return contactList;
//    }
}
