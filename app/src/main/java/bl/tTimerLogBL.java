package bl;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import library.common.tTimerLogData;
import library.dal.tTimerLogDA;

/**
 * Created by Arick Anjasmara on 03/05/2017.
 */

public class tTimerLogBL extends clsMainBL {

    public void insertData(tTimerLogData dt){
        SQLiteDatabase _db=getDb();
        tTimerLogDA _tTimerLogDA =new tTimerLogDA(_db);
        _tTimerLogDA.SaveData(_db, dt);
    }

    public List<tTimerLogData> getAllData(){
        SQLiteDatabase _db=getDb();
        tTimerLogDA _tTimerLogDA =new tTimerLogDA(_db);
        List<tTimerLogData> listData = _tTimerLogDA.getAllData(_db);
        return listData;
    }

    public void deleteDataCompleteWhileError(){
        SQLiteDatabase _db=getDb();
        tTimerLogDA _tTimerLogDA =new tTimerLogDA(_db);
        _tTimerLogDA.DeleteCompleteWhileErrorPush(_db);
    }
}
