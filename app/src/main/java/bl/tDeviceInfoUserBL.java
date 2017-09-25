package bl;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import library.common.tDeviceInfoUserData;
import library.dal.tDeviceInfoUserDA;

public class tDeviceInfoUserBL extends clsMainBL {
	public void SaveInfoDevice(String txtUserId, String txtDeviceId, String versionName){
		SQLiteDatabase db=getDb();
		tDeviceInfoUserDA _tDeviceInfoUserDA=new tDeviceInfoUserDA(db);
		tDeviceInfoUserData data= new tDeviceInfoUserData();
		int num=1;
		data.set_intId(num);
		data.set_txtUserId(txtUserId);
		data.set_txtDeviceId(txtDeviceId);
		data.set_txtDevice(android.os.Build.DEVICE);
		data.set_txtModel(android.os.Build.MANUFACTURER+" "+android.os.Build.MODEL);
		data.set_txtVersion(System.getProperty("os.version"));
		data.set_txtVersionName(versionName);
        _tDeviceInfoUserDA.SaveDatatDeviceInfoUserData(db, data);
	}
	public List<tDeviceInfoUserData> getData(int id){
		List<tDeviceInfoUserData> listData=new ArrayList<tDeviceInfoUserData>();
		tDeviceInfoUserDA _tDeviceInfoUserDA=new tDeviceInfoUserDA(db);
		if(id == 0){
			listData=_tDeviceInfoUserDA.getAllData(db);
		}else{
			tDeviceInfoUserData data=new tDeviceInfoUserData();
			data=_tDeviceInfoUserDA.getData(db, id);
			listData.add(data);
		}
		return listData;
	}
}
