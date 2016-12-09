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

    public void insertOrReplace(mSPMDetailData data) {
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

    public List<mSPMDetailData> getAllDataTaskPending() {
        SQLiteDatabase db = getDb();
        mSPMDetailDA _mSPMDetailDA = new mSPMDetailDA(db);
        List<mSPMDetailData> data = _mSPMDetailDA.getAllDataTaskPending(db);
        db.close();
        return data;
    }

    public List<mSPMDetailData> getAllDataTaskSuccess() {
        SQLiteDatabase db = getDb();
        mSPMDetailDA _mSPMDetailDA = new mSPMDetailDA(db);
        List<mSPMDetailData> data = _mSPMDetailDA.getAllDataTaskSuccess(db);
        db.close();
        return data;
    }
}
