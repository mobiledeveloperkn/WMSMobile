package bl;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import library.common.tDisplayPictureData;
import library.dal.tDisplayPictureDA;

/**
 * Created by ASUS ZE on 23/08/2016.
 */
public class tDisplayPictureBL extends clsMainBL {

    public void saveData(List<tDisplayPictureData> Listdata){
        SQLiteDatabase db=getDb();
        tDisplayPictureDA _tDisplayPictureDA = new tDisplayPictureDA(db);
        for(tDisplayPictureData data:Listdata){
            _tDisplayPictureDA.SaveData(db, data);
        }
    }

    public tDisplayPictureData getData(){
        SQLiteDatabase db=getDb();
        tDisplayPictureDA _tDisplayPictureDA = new tDisplayPictureDA(db);
        tDisplayPictureData data=_tDisplayPictureDA.getData(db);
        return data;
    }
}
