package bl;

import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import library.common.clsHelper;
import library.common.enumConfigData;
import library.common.linkAPI;
import library.common.mRoleData;
import library.common.tUserLoginData;
import library.common.mconfigData;
import library.dal.clsHardCode;
import library.dal.mRoleDA;
import library.dal.mconfigDA;

/**
 * Created by ASUS ZE on 17/11/2016.
 */

public class mRoleBL extends clsMainBL {
    public List<mRoleData> getRole(String username) throws ParseException {
        SQLiteDatabase _db = getDb();
        mconfigDA _mconfigDA = new mconfigDA(_db);
        String username_hc = "rheza.tesar";

        String strVal2;
        mconfigData dataAPI = _mconfigDA.getData(db, enumConfigData.ApiKalbe.getidConfigData());
        strVal2 = dataAPI.get_txtValue();
        if (dataAPI.get_txtValue().equals("")) {
            strVal2 = dataAPI.get_txtDefaultValue();
        }
        clsHelper _help = new clsHelper();
        linkAPI dtlinkAPI = new linkAPI();
        String txtMethod = "GetAllMWebUserRoleByUserName";
        dtlinkAPI.set_txtMethod(txtMethod);
        dtlinkAPI.set_txtParam(username);
        dtlinkAPI.set_txtToken(new clsHardCode().txtTokenAPI);
        String strLinkAPI = dtlinkAPI.QueryString(strVal2);

        String JsonData;
        List<mRoleData> Listdata = new ArrayList<>();
        mRoleDA _mRoleDA = new mRoleDA(db);
        tUserLoginData _tUserLoginData = new tUserLoginData();
//        _tUserLoginData = new tUserLoginBL().getUserActive();
        if (username_hc.equalsIgnoreCase(username)){
            Listdata = _mRoleDA.getAllData(db);
        } else {
            Listdata = null;
        }

//        try {
//            JsonData = _help.ResultJsonData(_help.getHTML(strLinkAPI));
//            org.json.simple.JSONArray JsonArray = _help.ResultJsonArray(JsonData);
//
//            Iterator i = JsonArray.iterator();
//
//            SQLiteDatabase db = getDb();
//            mUserRoleDA _mUserRoleDA = new mUserRoleDA(db);
//            _mUserRoleDA.DeleteAllDataMConfig(db);
//
//            while (i.hasNext()) {
//                org.json.simple.JSONObject innerObj = (org.json.simple.JSONObject) i.next();
//                Long IntResult = (Long) innerObj.get("_pboolValid");
//
//                if(IntResult==1){
//
//                    mUserRoleData _data = new mUserRoleData();
//                    int index = _mUserRoleDA.getContactsCount(db) + 1;
//                    _data.set_intId(String.valueOf(index));
//                    _data.set_intRoleId(String.valueOf(innerObj.get("IntRoleID")));
//                    _data.set_txtUserId(String.valueOf(innerObj.get("IntUserID")));
//                    _data.set_txtRoleName(String.valueOf(innerObj.get("TxtRoleName")));
//                    _mUserRoleDA.SaveDataMConfig(db, _data);
//                    Listdata.add(_data);
//                } else {
//                    mUserRoleData _data = new mUserRoleData();
//                    _data.set_intId(String.valueOf(innerObj.get("_pboolValid")));
//                    _data.set_intRoleId(String.valueOf(innerObj.get("IntRoleID")));
//                    _data.set_txtUserId(String.valueOf(innerObj.get("IntUserID")));
//                    _data.set_txtRoleName(String.valueOf(innerObj.get("_pstrMessage")));
//                    Listdata.add(_data);
//                }
//
//            }
//            return Listdata;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return Listdata;
    }

}
