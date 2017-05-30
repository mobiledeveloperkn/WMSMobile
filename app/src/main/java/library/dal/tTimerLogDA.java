package library.dal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import library.common.tTimerLogData;

/**
 * Created by Arick Anajasmara on 03/05/2017.
 */

public class tTimerLogDA {

    private static final String TABLE_CONTACTS = new clsHardCode().txtTable_tTimerLog;

    public tTimerLogDA(SQLiteDatabase db) {
        tTimerLogData dt = new tTimerLogData();
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_CONTACTS + "("
                + dt.Property_txtTimerLogId + " TEXT PRIMARY KEY,"
                + dt.Property_txtTimerStatus + " TEXT NULL,"
                + dt.Property_txtTimerType + " TEXT NULL,"
                + dt.Property_txtStarNo + " TEXT NULL,"
                + dt.Property_bitActive + " TEXT NULL,"
                + dt.Property_dtExecute + " TEXT NULL)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    public void DropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
    }

    public void SaveData(SQLiteDatabase db, tTimerLogData data) {
        tTimerLogData dt = new tTimerLogData();

        db.execSQL("Update " + TABLE_CONTACTS + " SET " + dt.Property_bitActive + "='0' WHERE " + dt.Property_bitActive + "='1'");

        db.execSQL("INSERT OR REPLACE into " + TABLE_CONTACTS + " ("
                + dt.Property_txtTimerLogId
                + "," + dt.Property_txtTimerStatus
                + "," + dt.Property_txtTimerType
                + "," + dt.Property_txtStarNo
                + "," + dt.Property_bitActive
                + "," + dt.Property_dtExecute
                +") " + "values('"
                + String.valueOf(data.getTxtTimerLogId()) + "','"
                + String.valueOf(data.getTxtTimerStatus()) + "','"
                + String.valueOf(data.getTxtTimerType()) + "','"
                + String.valueOf(data.getTxtStarNo()) + "','"
                + String.valueOf(data.getBitActive()) + "','"
                + String.valueOf(data.getDtExecute()) + "')");

        db.close();
    }

    public List<tTimerLogData> getAllData(SQLiteDatabase db) {
        List<tTimerLogData> contactList = new ArrayList<>();

        tTimerLogData dt = new tTimerLogData();
        String selectQuery = "SELECT  " + dt.Property_All + " FROM " + TABLE_CONTACTS;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                tTimerLogData contact = new tTimerLogData();
                contact.setTxtTimerLogId(cursor.getString(0));
                contact.setTxtTimerStatus(cursor.getString(1));
                contact.setTxtTimerType(cursor.getString(2));
                contact.setTxtStarNo(cursor.getString(3));
                contact.setBitActive(cursor.getString(4));
                contact.setDtExecute(cursor.getString(5));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return contactList;
    }

    public void DeleteCompleteWhileErrorPush(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + TABLE_CONTACTS + " WHERE txtTimerType='Complete'");
    }

}
