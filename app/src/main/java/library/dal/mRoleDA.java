package library.dal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import library.common.mRoleData;

/**
 * Created by ASUS ZE on 16/11/2016.
 */

public class mRoleDA {
    public mRoleDA(SQLiteDatabase db) {
        mRoleData dt = new mRoleData();
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_CONTACTS + "("
                + dt.Property_intRoleId + " TEXT PRIMARY KEY,"
                + dt.Property_txtRoleName + " TEXT  NULL)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // All Static variables

    // Contacts table name
    private static final String TABLE_CONTACTS = new clsHardCode().txtTable_mUserRole;

    // Upgrading database
    public void DropTable(SQLiteDatabase db) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void SaveDataMConfig(SQLiteDatabase db, mRoleData data) {
        mRoleData dt = new mRoleData();
        db.execSQL("INSERT OR REPLACE into " + TABLE_CONTACTS + " ("
                + dt.Property_intRoleId
                + "," + dt.Property_txtRoleName
                + ") " + "values('"
                + String.valueOf(data.getIntRoleId()) + "','"
                + String.valueOf(data.getTxtRoleName()) + "')");
        // db.insert(TABLE_CONTACTS, null, values);
        // db.close(); // Closing database connection
    }
    public void DeleteAllDataMConfig(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + TABLE_CONTACTS );
        // db.insert(TABLE_CONTACTS, null, values);
        // db.close(); // Closing database connection
    }
    // Getting single contact
    public mRoleData getData(SQLiteDatabase db, int id) {
        mRoleData dt = new mRoleData();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[] {
                        dt.Property_intRoleId
                        , dt.Property_txtRoleName},
                dt.Property_intRoleId + "=?", new String[] { String.valueOf(id) },
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        mRoleData contact = new mRoleData();
        if (cursor.getCount() > 0) {
            contact.setIntRoleId(cursor.getString(0));
            contact.setTxtRoleName(cursor.getString(1));
            // return contact
        } else {
            contact = null;
        }
        cursor.close();
        return contact;
    }


    // Getting All Contacts
    public List<mRoleData> getAllData(SQLiteDatabase db) {
        List<mRoleData> contactList = new ArrayList<mRoleData>();
        // Select All Query
        mRoleData dt = new mRoleData();
        String selectQuery = "SELECT  " + dt.Property_All + " FROM "
                + TABLE_CONTACTS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                mRoleData contact = new mRoleData();
                contact.setIntRoleId(cursor.getString(0));
                contact.setTxtRoleName(cursor.getString(1));
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
        mRoleData dt = new mRoleData();
        db.delete(TABLE_CONTACTS, dt.Property_intRoleId + " = ?",
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
    public void InsertDefaultMRole(SQLiteDatabase db) {
        String txtQuery = "insert into mUserRole(intRoleId,txtRoleName )"
                + "select  '100','Picker';";
        db.execSQL(txtQuery);
        txtQuery = "insert into mUserRole(intRoleId,txtRoleName )"
                + "select  '101','Put Away';";
        db.execSQL(txtQuery);
    }
}
