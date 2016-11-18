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
}
