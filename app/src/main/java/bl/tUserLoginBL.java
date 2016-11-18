package bl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.List;

import library.common.tUserLoginData;
import library.dal.tUserLoginDA;

/**
 * Created by ASUS ZE on 17/11/2016.
 */

public class tUserLoginBL extends clsMainBL {
    public tUserLoginData getUserActive() {
        this.db = getDb();
        tUserLoginDA _tUserLoginDA =new tUserLoginDA(db);
        List<tUserLoginData> listData= _tUserLoginDA.getAllData(db);
        db.close();
        return listData.get(0);
    }
    public String Login(String txtUserName, String txtPass, String intRoleId, String userID) throws ParseException {
        String pass = "sanghiang";
//        SQLiteDatabase _db = getDb();
//        mconfigDA _mconfigDA = new mconfigDA(_db);
//
//        String strVal2;
//        mconfigData dataAPI = _mconfigDA.getData(db, enumConfigData.ApiKalbe.getidConfigData());
//        strVal2 = dataAPI.get_txtValue();
//        if (dataAPI.get_txtValue().equals("")) {
//            strVal2 = dataAPI.get_txtDefaultValue();
//        }
//        clsHelper _help = new clsHelper();
//        linkAPI dtlinkAPI = new linkAPI();
//        String txtMethod = "GetDataMWebUserWithActiveDirectory";
//        dtlinkAPI.set_txtMethod(txtMethod);
//        dtlinkAPI.set_txtParam(new clsHardCode().txtMethod_Kalbefood + "|" + txtUserName + "|" + txtPass);
//        dtlinkAPI.set_txtToken(new clsHardCode().txtTokenAPI);
//        String strLinkAPI = dtlinkAPI.QueryString(strVal2);
//
//        String JsonData;
        JSONArray JsonArray = null;
        JSONObject jsonObject = null;

        if(txtPass.equalsIgnoreCase(pass)){
            tUserLoginDA _tUserLoginDA = new tUserLoginDA(db);
            int sumdata_mUserDA = _tUserLoginDA.getContactsCount(db);
            if (sumdata_mUserDA == 0) {
                _tUserLoginDA.InsertDefaultMUser(db);
                pass = "1";
            }
        }
//
//        try {
//            JsonData = _help.ResultJsonData(_help.getHTML(strLinkAPI));
//            JsonArray = _help.ResultJsonArray(JsonData);
//
////			return JsonArray;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        _help = new clsHelper();
//        dtlinkAPI = new linkAPI();
//        txtMethod = "GetListCabangByUserAndRole";
//        dtlinkAPI.set_txtMethod(txtMethod);
//        dtlinkAPI.set_txtParam(userID + "|" + intRoleId);
//        dtlinkAPI.set_txtToken(new clsHardCode().txtTokenAPI);
//        strLinkAPI = dtlinkAPI.QueryString(strVal2);

//        String JsonDataCabang;
//        List<mUserRoleData> Listdata = new ArrayList<>();
//
//        try {
//            JsonDataCabang = _help.ResultJsonData(_help.getHTML(strLinkAPI));
//            org.json.simple.JSONArray JsonArrayCabang = _help.ResultJsonArray(JsonDataCabang);
//
//            Iterator i = JsonArrayCabang.iterator();
//
//            SQLiteDatabase db = getDb();
//            mBranchDA _mBranchDA = new mBranchDA(db);
//            _mBranchDA.DeleteAllDataMConfig(db);
//            clsMainActivity _clsMainActivity = new clsMainActivity();
//
//            while (i.hasNext()) {
//
////				UUID uuid = UUID.randomUUID();
////				String randomUUIDString = uuid.toString();
//
//                org.json.simple.JSONObject innerObj = (org.json.simple.JSONObject) i.next();
//
//                mBranchData _data = new mBranchData();
//
//                int index = _mBranchDA.getContactsCount(db) + 1;
//                _data.set_uuId(String.valueOf(index));
//                _data.set_txtBranchID(String.valueOf(innerObj.get("TxtCabangID")));
//                _data.set_txtBranchName(String.valueOf(innerObj.get("TxtName")));
//                _mBranchDA.SaveDataMConfig(db, _data);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return pass;
    }
}
