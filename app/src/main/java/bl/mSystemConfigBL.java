package bl;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import library.common.mSPMDetailData;
import library.common.mSystemConfigData;
import library.dal.mSPMDetailDA;
import library.dal.mSystemConfigDA;

/**
 * Created by arick.anjasmara on 18/11/2016.
 */

public class mSystemConfigBL extends clsMainBL {

    public mSystemConfigData getData(int id) {
        SQLiteDatabase db = getDb();
        mSystemConfigDA _mSystemConfigDA = new mSystemConfigDA(db);
        mSystemConfigData data = _mSystemConfigDA.getData(db, id);
        db.close();
        return data;
    }

    public void updateOrderPicking(int id) {
        SQLiteDatabase db = getDb();
        mSystemConfigDA _mSystemConfigDA = new mSystemConfigDA(db);
        _mSystemConfigDA.UpdateOrderPicking(db, id);
        db.close();
    }

    public void UpdateFilterPicking(String filter) {
        SQLiteDatabase db = getDb();
        mSystemConfigDA _mSystemConfigDA = new mSystemConfigDA(db);
        _mSystemConfigDA.UpdateFilterPicking(db, filter);
        db.close();
    }
}
