package bl;

import android.database.sqlite.SQLiteDatabase;

import library.common.mSPMHeaderData;
import library.dal.mSPMHeaderDA;

/**
 * Created by ASUS ZE on 18/11/2016.
 */

public class mSPMHeaderBL extends clsMainBL {

    public mSPMHeaderData GetAllData(){
        SQLiteDatabase db=getDb();
        mSPMHeaderDA _mSPMHeaderDA=new mSPMHeaderDA(db);
        mSPMHeaderData data=_mSPMHeaderDA.getAllData(db);
        db.close();
        return data;
    }

    public mSPMHeaderData GetDataByStatus(){
        SQLiteDatabase db=getDb();
        mSPMHeaderDA _mSPMHeaderDA=new mSPMHeaderDA(db);
        mSPMHeaderData data=_mSPMHeaderDA.GetDataByStatus(db);
        db.close();
        return data;
    }

    public mSPMHeaderData GetDataById(String id){
        SQLiteDatabase db=getDb();
        mSPMHeaderDA _mSPMHeaderDA=new mSPMHeaderDA(db);
        mSPMHeaderData data=_mSPMHeaderDA.GetDataById(db, id);
        db.close();
        return data;
    }

    public mSPMHeaderData getAllDataPushData(){
        SQLiteDatabase db=getDb();
        mSPMHeaderDA _mSPMHeaderDA=new mSPMHeaderDA(db);
        mSPMHeaderData data=_mSPMHeaderDA.getAllDataPushData(db);
        db.close();
        return data;
    }

    public void saveData (mSPMHeaderData _mSPMHeaderData){
        SQLiteDatabase db=getDb();
        mSPMHeaderDA _mSPMHeaderDA=new mSPMHeaderDA(db);
        _mSPMHeaderDA.SaveData(db, _mSPMHeaderData);
    }
    public void updateDataValueById(String id){
        SQLiteDatabase _db=getDb();
        mSPMHeaderDA _mSPMHeaderDA=new mSPMHeaderDA(_db);
        _mSPMHeaderDA.updateDataById(_db, id);
    }

    public void updateDataValueByIdOffline(String id){
        SQLiteDatabase _db=getDb();
        mSPMHeaderDA _mSPMHeaderDA=new mSPMHeaderDA(_db);
        _mSPMHeaderDA.updateDataByIdOffline(_db, id);
    }

    public void saveDataPush(String id){
        SQLiteDatabase _db=getDb();
        mSPMHeaderDA _mSPMHeaderDA=new mSPMHeaderDA(_db);
        _mSPMHeaderDA.saveDataPush(_db, id);
    }

    public void updateDataSPMStartById(String id, String dTime){
        SQLiteDatabase _db=getDb();
        mSPMHeaderDA _mSPMHeaderDA=new mSPMHeaderDA(_db);
        _mSPMHeaderDA.updateDataBitStartById(_db, id, dTime);
    }

    public void updateDtEndById(String id, String dTime){
        SQLiteDatabase _db=getDb();
        mSPMHeaderDA _mSPMHeaderDA=new mSPMHeaderDA(_db);
        _mSPMHeaderDA.updateDtEndById(_db, id, dTime);
    }
}
