package library.dal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import library.common.enumConfigData;
import library.common.mSystemConfigData;
import library.common.mconfigData;

public class mSystemConfigDA {

    private static final String TABLE_CONTACTS = new clsHardCode().txtTable_mSystemConfig;

    public void InsertDefaultMsystemConfig(SQLiteDatabase db) {
        String txtQuery = "insert or replace into msystemconfig(intId,txtName,txtValue,txtDefaultValue)"
                + "select  1,'Ordering Picking order','asc','asc'";
        db.execSQL(txtQuery);
    }

    public mSystemConfigDA(SQLiteDatabase db) {
        mSystemConfigData dt = new mSystemConfigData();
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_CONTACTS + "(" + dt.Property_intId
                + " INTEGER PRIMARY KEY," + dt.Property_txtName
                + " TEXT NOT NULL," + dt.Property_txtValue + " TEXT NOT NULL,"
                + dt.Property_txtDefaultValue + " TEXT NOT NULL" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        if (getContactsCount(db) == 0) {
            InsertDefaultMsystemConfig(db);
        }
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

    public void SaveDataMConfig(SQLiteDatabase db, mconfigData data) {
        mSystemConfigData dt = new mSystemConfigData();
        db.execSQL("INSERT OR REPLACE into " + TABLE_CONTACTS + " ("
                + dt.Property_intId + "," + dt.Property_txtName + ","
                + dt.Property_txtValue + "," + dt.Property_txtDefaultValue + ") " +
                "values('"
                + String.valueOf(data.get_intId()) + "','"
                + String.valueOf(data.get_txtName()) + "','"
                + String.valueOf(data.get_txtValue()) + "','"
                + String.valueOf(data.get_txtDefaultValue()) + ")");
    }

    public void UpdateOrderPicking(SQLiteDatabase db, int selected_order) {
        String order = selected_order == 0 ? "asc" : "desc";

        mSystemConfigData dt = new mSystemConfigData();

        String query = "INSERT OR REPLACE into " + TABLE_CONTACTS + " ("
                + dt.Property_intId + "," + dt.Property_txtName + ","
                + dt.Property_txtValue + "," + dt.Property_txtDefaultValue + ") " +
                "values('"
                + String.valueOf(1) + "','"
                + String.valueOf("Ordering Picking order") + "','"
                + String.valueOf(order) + "','"
                + String.valueOf(order) + "')";

        db.execSQL(query);
    }

    // Getting single contact
    public mSystemConfigData getData(SQLiteDatabase db, int id) {
        mSystemConfigData dt = new mSystemConfigData();
        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{
                        dt.Property_intId, dt.Property_txtName, dt.Property_txtValue,
                        dt.Property_txtDefaultValue},
                dt.Property_intId + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        mSystemConfigData contact = new mSystemConfigData();
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                contact = new mSystemConfigData(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3));
            } else {
                contact = null;
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return contact;
    }
}
