package library.dal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import library.common.enumConfigData;
import library.common.mconfigData;

public class mconfigDA {

    public mconfigDA(SQLiteDatabase db) {
        mconfigData dt = new mconfigData();
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_CONTACTS + "(" + dt.Property_intId
                + " INTEGER PRIMARY KEY," + dt.Property_txtName
                + " TEXT NOT NULL," + dt.Property_txtValue + " TEXT NOT NULL,"
                + dt.Property_txtDefaultValue + " TEXT NOT NULL,"
                + dt.Property_intEditAdmin + " TEXT NOT NULL" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // All Static variables

    // Contacts table name
    private static final String TABLE_CONTACTS = new clsHardCode().txtTable_mConfig;

    // Upgrading database
    public void DropTable(SQLiteDatabase db) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void SaveDataMConfig(SQLiteDatabase db, mconfigData data) {
        mconfigData dt = new mconfigData();
        db.execSQL("INSERT OR REPLACE into " + TABLE_CONTACTS + " ("
                + dt.Property_intId + "," + dt.Property_txtName + ","
                + dt.Property_txtValue + "," + dt.Property_txtDefaultValue
                + "," + dt.Property_intEditAdmin + ") " + "values('"
                + String.valueOf(data.get_intId()) + "','"
                + String.valueOf(data.get_txtName()) + "','"
                + String.valueOf(data.get_txtValue()) + "','"
                + String.valueOf(data.get_txtDefaultValue()) + "',"
                + String.valueOf(data.get_intEditAdmin()) + ")");
        // db.insert(TABLE_CONTACTS, null, values);
        // db.close(); // Closing database connection
    }

    public void DeleteAllDataMConfig(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + TABLE_CONTACTS);
        // db.insert(TABLE_CONTACTS, null, values);
        // db.close(); // Closing database connection
    }

    // Getting single contact
    public mconfigData getData(SQLiteDatabase db, int id) {
        mconfigData dt = new mconfigData();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{
                        dt.Property_intId, dt.Property_txtName, dt.Property_txtValue,
                        dt.Property_txtDefaultValue, dt.Property_intEditAdmin},
                dt.Property_intId + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        mconfigData contact = new mconfigData();
        if (cursor.getCount() > 0) {
            contact = new mconfigData(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), Integer.parseInt(cursor.getString(4)));

            // return contact
        } else {
            contact = null;
        }
        cursor.close();
        return contact;
    }

    // Getting All Contacts
    public List<mconfigData> getAllData(SQLiteDatabase db) {
        List<mconfigData> contactList = new ArrayList<mconfigData>();
        // Select All Query
        mconfigData dt = new mconfigData();
        String selectQuery = "SELECT  " + dt.Property_All + " FROM "
                + TABLE_CONTACTS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                mconfigData contact = new mconfigData();
                contact.set_intId(Integer.parseInt(cursor.getString(0)));
                contact.set_txtName(cursor.getString(1));
                contact.set_txtValue(cursor.getString(2));
                contact.set_txtDefaultValue(cursor.getString(3));
                contact.set_intEditAdmin(Integer.parseInt(cursor.getString(4)));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return contact list
        return contactList;
    }

    // Updating single contact
    public int updateContact(SQLiteDatabase db, mconfigData contact) {
        mconfigData dt = new mconfigData();
        ContentValues values = new ContentValues();
        values.put(dt.Property_txtName, contact.get_txtName());
        values.put(dt.Property_txtValue, contact.get_txtValue());
        values.put(dt.Property_intEditAdmin,
                String.valueOf(contact.get_intEditAdmin()));

        // updating row
        return db.update(TABLE_CONTACTS, values, dt.Property_intId + " = ?",
                new String[]{String.valueOf(contact.get_intId())});
    }

    // Deleting single contact
    public void deleteContact(SQLiteDatabase db, int id) {
        mconfigData dt = new mconfigData();
        db.delete(TABLE_CONTACTS, dt.Property_intId + " = ?",
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

    public void InsertDefaultMconfig(SQLiteDatabase db) {
        String txtQuery = "insert into mconfig(intId,txtName,txtValue,txtDefaultValue,intEditAdmin )"
                + "select  1,'android:versionCode','5','5',1;";
        db.execSQL(txtQuery);
        txtQuery = "insert into mconfig(intId,txtName,txtValue,txtDefaultValue,intEditAdmin )"
                + "select 2,'API','http://10.171.10.9/wmsonline','http://10.171.10.13/wmsonline',1;";
        db.execSQL(txtQuery);
    }

    public String getTypeMobile(SQLiteDatabase db) {
        String txtLinkAPI = "";
        mconfigData dt = new mconfigData();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{
                        dt.Property_intId, dt.Property_txtName, dt.Property_txtValue,
                        dt.Property_txtDefaultValue, dt.Property_intEditAdmin},
                dt.Property_intId + "=?", new String[]{String.valueOf(enumConfigData.TypeMobile.getidConfigData())},
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        mconfigData data = new mconfigData();
        if (cursor.getCount() > 0) {
            data = new mconfigData(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), Integer.parseInt(cursor.getString(4)));

            if (data.get_txtValue().equals("")) {
                txtLinkAPI = data.get_txtDefaultValue();
            } else {
                txtLinkAPI = data.get_txtValue();
            }
            // return contact
        } else {
            data = null;
        }
        cursor.close();
        // return contact list
        return txtLinkAPI;
    }

    public String getLIVE(SQLiteDatabase db) {
        String txtLinkAPI = "";
        mconfigData dt = new mconfigData();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{
                        dt.Property_intId, dt.Property_txtName, dt.Property_txtValue,
                        dt.Property_txtDefaultValue, dt.Property_intEditAdmin},
                dt.Property_intId + "=?", new String[]{String.valueOf(enumConfigData.LIVE.getidConfigData())},
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        mconfigData data = new mconfigData();
        if (cursor.getCount() > 0) {
            data = new mconfigData(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), Integer.parseInt(cursor.getString(4)));

            if (data.get_txtValue().equals("")) {
                txtLinkAPI = data.get_txtDefaultValue();
            } else {
                txtLinkAPI = data.get_txtValue();
            }
            // return contact
        } else {
            data = null;
        }
        cursor.close();
        // return contact list
        return txtLinkAPI;
    }

    public String getBackGroundServiceOnlineData(SQLiteDatabase db) {
        String txtLinkAPI = "";
        mconfigData dt = new mconfigData();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{
                        dt.Property_intId, dt.Property_txtName, dt.Property_txtValue,
                        dt.Property_txtDefaultValue, dt.Property_intEditAdmin},
                dt.Property_intId + "=?", new String[]{String.valueOf(enumConfigData.BackGroundServiceOnline.getidConfigData())},
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        mconfigData data = new mconfigData();
        if (cursor.getCount() > 0) {
            data = new mconfigData(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), Integer.parseInt(cursor.getString(4)));

            if (data.get_txtValue().equals("")) {
                txtLinkAPI = data.get_txtDefaultValue();
            } else {
                txtLinkAPI = data.get_txtValue();
            }
            // return contact
        } else {
            data = null;
        }
        cursor.close();
        // return contact list
        return txtLinkAPI;
    }

    public String getDomainKalbeData(SQLiteDatabase db) {
        String txtLinkAPI = "";
        mconfigData dt = new mconfigData();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{
                        dt.Property_intId, dt.Property_txtName, dt.Property_txtValue,
                        dt.Property_txtDefaultValue, dt.Property_intEditAdmin},
                dt.Property_intId + "=?", new String[]{String.valueOf(enumConfigData.DomainKalbe.getidConfigData())},
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        mconfigData data = new mconfigData();
        if (cursor.getCount() > 0) {
            data = new mconfigData(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), Integer.parseInt(cursor.getString(4)));

            if (data.get_txtValue().equals("")) {
                txtLinkAPI = data.get_txtDefaultValue();
            } else {
                txtLinkAPI = data.get_txtValue();
            }
            // return contact
        } else {
            data = null;
        }
        cursor.close();
        // return contact list
        return txtLinkAPI;
    }

    public String getLinkAPIData(SQLiteDatabase db) {
        String txtLinkAPI = "";
        mconfigData dt = new mconfigData();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{
                        dt.Property_intId, dt.Property_txtName, dt.Property_txtValue,
                        dt.Property_txtDefaultValue, dt.Property_intEditAdmin},
                dt.Property_intId + "=?", new String[]{String.valueOf(enumConfigData.ApiKalbe.getidConfigData())},
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        mconfigData data = new mconfigData();
        if (cursor.getCount() > 0) {
            data = new mconfigData(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), Integer.parseInt(cursor.getString(4)));

            if (data.get_txtValue().equals("")) {
                txtLinkAPI = data.get_txtDefaultValue();
            } else {
                txtLinkAPI = data.get_txtValue();
            }
            // return contact
        } else {
            data = null;
        }
        cursor.close();
        // return contact list
        return txtLinkAPI;
    }
}
