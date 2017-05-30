package bl;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import library.common.clsPushData;
import library.common.dataJson;
import library.common.mSPMDetailData;
import library.common.mSPMHeaderData;
import library.common.tDeviceInfoUserData;
import library.common.tUserLoginData;
import library.dal.mSPMDetailDA;
import library.dal.mSPMHeaderDA;
import library.dal.tDeviceInfoUserDA;
import library.dal.tUserLoginDA;

/**
 * Created by ASUS ZE on 02/02/2017.
 */

public class clsHelperBL extends clsMainBL {
    public clsPushData pushData() {
        clsPushData dtclsPushData = new clsPushData();
        dataJson dtPush = new dataJson();
        SQLiteDatabase db = getDb();
        tUserLoginDA _tUserLoginDA = new tUserLoginDA(db);
        tDeviceInfoUserDA _tDeviceInfoUserDA = new tDeviceInfoUserDA(db);
        String versionName = "";
        if (_tUserLoginDA.getContactsCount(db) > 0) {
            List<tUserLoginData> _tUserLoginData = _tUserLoginDA.getAllData(db);
            List<tDeviceInfoUserData> _tDeviceInfoUserData = _tDeviceInfoUserDA.getAllData(db);
            dtPush.set_txtUserId(_tUserLoginData.get(0).getIntUserId());
            dtPush.set_txtVersionName(_tDeviceInfoUserData.get(0).get_txtVersionName());
//            dtPush.set_txtSessionLoginId(_tUserLoginData.get_txtDataId());
            try {
//                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//                Calendar cal = Calendar.getInstance();
//                mCounterNumberDA _mCounterNumberDA = new mCounterNumberDA(db);
//                mCounterNumberData _data = new mCounterNumberData();
//                _data.set_intId(enumCounterData.MonitorSchedule.getidCounterData());
//                _data.set_txtDeskripsi("value menunjukan waktu terakhir menjalankan services");
//                _data.set_txtName("Monitor Service");
//                _data.set_txtValue(dateFormat.format(cal.getTime()));
//                _mCounterNumberDA.SaveDataMConfig(db, _data);

                //new clsInit().PushData(db,versionName);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            mSPMHeaderDA _mSPMHeaderDA = new mSPMHeaderDA(db);
            mSPMDetailDA _mSPMDetailDA = new mSPMDetailDA(db);

            mSPMHeaderData _mSPMHeaderData = _mSPMHeaderDA.getAllDataPushData(db);
            List<mSPMDetailData> ListOfmSPMDetailData = _mSPMDetailDA.getAllDataPushData(db);

            if (_mSPMHeaderData.getTxtNoSPM() != null) {
                dtPush.setmSPMHeaderData(_mSPMHeaderData);
            }
            if (ListOfmSPMDetailData != null) {
                dtPush.set_ListOfmSPMDetailData(ListOfmSPMDetailData);
            } if (_mSPMHeaderData.getTxtNoSPM()==null&&ListOfmSPMDetailData==null){
                dtPush = null;
            }

        } else {
            dtPush = null;
        }
        db.close();
        dtclsPushData.setDtdataJson(dtPush);
        return dtclsPushData;
    }
}
