package library.dal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import library.common.tDisplayPictureData;

/**
 * Created by ASUS ZE on 23/08/2016.
 */
public class tDisplayPictureDA {

    // Contacts table name
    private static final String TABLE_CONTACTS = new clsHardCode().txtTable_tDisplayPicture;

    public tDisplayPictureDA(SQLiteDatabase db) {
        tDisplayPictureData dt = new tDisplayPictureData();
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_CONTACTS + "("
                + dt.Property__intID + " TEXT PRIMARY KEY,"
                + dt.Property_image + " BLOB NULL,"
                + dt.Property_intPush + " TEXT NULL"
                +")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    public void DropTable(SQLiteDatabase db) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        // Create tables again
    }

    // Adding new contact
    public void SaveData(SQLiteDatabase db,tDisplayPictureData data) {
        tDisplayPictureData dt=new tDisplayPictureData();
        ContentValues cv = new ContentValues();

        cv.put(dt.Property__intID, data.get_intID());
        cv.put(dt.Property_image, data.get_image());
        cv.put(dt.Property_intPush, data.get_intPush());

            db.replace(TABLE_CONTACTS, null, cv);
    }

    // Getting single contact
    public tDisplayPictureData getData(SQLiteDatabase db) {
        tDisplayPictureData contact = new tDisplayPictureData();
        // Select All Query
        tDisplayPictureData dt=new tDisplayPictureData();
        String selectQuery = "SELECT  "+dt.Property_All+" FROM " + TABLE_CONTACTS +" WHERE "+dt.Property_intPush+"=1";

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                contact.set_intID(cursor.getString(0));
                contact.set_image(cursor.getBlob(1));
                contact.set_intPush(cursor.getString(2));
                // Adding contact to list
//						contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return contact list
        return contact;
    }

    public void DeleteAllDAta(SQLiteDatabase db) {
        // Drop older table if existed
        db.execSQL("DELETE FROM " + TABLE_CONTACTS);
    }
    // Getting contacts Count
    public int getContactsCount(SQLiteDatabase db) {
        String countQuery = "SELECT * FROM " + TABLE_CONTACTS;
        Cursor cursor = db.rawQuery(countQuery, null);
        int num =cursor.getCount();
        cursor.close();

        // return count
        return num;
    }
}
