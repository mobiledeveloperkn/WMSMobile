package bl;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import library.common.mSPMDetailData;
import library.dal.mSPMDetailDA;

/**
 * Created by arick.anjasmara on 18/11/2016.
 */

public class mSPMDetailBL extends clsMainBL {

    public mSPMDetailData getData(int id) {
        SQLiteDatabase db = getDb();
        mSPMDetailDA _mSPMDetailDA = new mSPMDetailDA(db);
        mSPMDetailData data = _mSPMDetailDA.getData(db, id);
        db.close();
        return data;
    }

    public void insert(mSPMDetailData data) {
        SQLiteDatabase db = getDb();
        mSPMDetailDA _mSPMDetailDA = new mSPMDetailDA(db);
        _mSPMDetailDA.SaveDataMConfig(db, data);
        db.close();
    }

    public List<mSPMDetailData> getAllData() {
        SQLiteDatabase db = getDb();
        mSPMDetailDA _mSPMDetailDA = new mSPMDetailDA(db);
        List<mSPMDetailData> data = _mSPMDetailDA.getAllData(db);
        db.close();
        return data;
    }

    public List<mSPMDetailData> getAllDataById(String id) {
        SQLiteDatabase db = getDb();
        mSPMDetailDA _mSPMDetailDA = new mSPMDetailDA(db);
        List<mSPMDetailData> data = _mSPMDetailDA.getAllDataById(db, id);
        db.close();
        return data;
    }

    public List<mSPMDetailData> getAllDataTaskPending(String id) {
        SQLiteDatabase db = getDb();
        mSPMDetailDA _mSPMDetailDA = new mSPMDetailDA(db);
        List<mSPMDetailData> data = _mSPMDetailDA.getAllDataTaskPending(db, id);
        db.close();
        return data;
    }

    public List<mSPMDetailData> getAllDataPushData() {
        SQLiteDatabase db = getDb();
        mSPMDetailDA _mSPMDetailDA = new mSPMDetailDA(db);
        List<mSPMDetailData> data = _mSPMDetailDA.getAllDataPushData(db);
        db.close();
        return data;
    }

    public List<mSPMDetailData> getAllDataTaskConfirm(String id) {
        SQLiteDatabase db = getDb();
        mSPMDetailDA _mSPMDetailDA = new mSPMDetailDA(db);
        List<mSPMDetailData> data = _mSPMDetailDA.getAllDataTaskConfirm(db, id);
        db.close();
        return data;
    }

    public List<mSPMDetailData> getAllDataTaskCancel(String id) {
        SQLiteDatabase db = getDb();
        mSPMDetailDA _mSPMDetailDA = new mSPMDetailDA(db);
        List<mSPMDetailData> data = _mSPMDetailDA.getAllDataTaskCancel(db, id);
        db.close();
        return data;
    }

    public void saveDataList(List<mSPMDetailData> data){

    }

    public void updateDataValueById(String idSPMDetail, String intUserId){
        SQLiteDatabase _db=getDb();
        mSPMDetailDA _mSPMDetailDA = new mSPMDetailDA(_db);
        _mSPMDetailDA.updateDataById(_db, idSPMDetail, intUserId);
    }

    public void updateDataValueByIdOffline(String idSPMDetail, String intUserId){
        SQLiteDatabase _db=getDb();
        mSPMDetailDA _mSPMDetailDA = new mSPMDetailDA(_db);
        _mSPMDetailDA.updateDataByIdOffline(_db, idSPMDetail, intUserId);
    }

    public void updateDataSPMCancelById(String id, String _intUserId, String reason){
        SQLiteDatabase _db=getDb();
        mSPMDetailDA _mSPMDetailDA = new mSPMDetailDA(_db);
        _mSPMDetailDA.updateDataSPMCancelById(_db, id, _intUserId, reason);
    }

    public void updateDataSPMCancelByIdOffline(String id, String _intUserId, String reason){
        SQLiteDatabase _db=getDb();
        mSPMDetailDA _mSPMDetailDA = new mSPMDetailDA(_db);
        _mSPMDetailDA.updateDataSPMCancelByIdOffline(_db, id, _intUserId, reason);
    }

    public void saveFromPushData(String id, String status, String sync){
        SQLiteDatabase _db=getDb();
        mSPMDetailDA _mSPMDetailDA = new mSPMDetailDA(_db);
        _mSPMDetailDA.saveDataPush(_db, id, status, sync);
    }
}
