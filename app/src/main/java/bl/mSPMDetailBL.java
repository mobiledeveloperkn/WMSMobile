package bl;

import android.database.sqlite.SQLiteDatabase;

import library.common.mSPMDetailData;
import library.common.mSPMHeaderData;
import library.dal.mSPMDetailDA;
import library.dal.mSPMHeaderDA;

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
}
