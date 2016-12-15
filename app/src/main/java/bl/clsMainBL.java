package bl;

import android.database.sqlite.SQLiteDatabase;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.text.ParseException;
import java.util.UUID;

import library.common.clsHelper;
import library.common.clsStatusMenuStart;
import library.dal.clsHardCode;
import library.dal.enumStatusMenuStart;
import library.dal.mconfigDA;
import library.dal.tUserLoginDA;

public class clsMainBL {
	SQLiteDatabase db;
	public clsMainBL() {
		super();
		this.db = getDb();
	}
	
	public SQLiteDatabase getDb() {
		clsHardCode _clsHardCode;
		clsHelper _clsHelper=new clsHelper();
		_clsHardCode =new clsHardCode();
		_clsHelper.createFolderApp();
		String rootDB = _clsHardCode.txtDatabaseName;
		db=SQLiteDatabase.openOrCreateDatabase(rootDB, null);
		return db;
	}
	public String getLinkAPI(){
		this.db = getDb();
		String txtLinkAPI=new mconfigDA(db).getLinkAPIData(db);
		this.db.close();
		return txtLinkAPI;
	}
	public String getTypeMobile(){
		this.db = getDb();
		String txtLinkAPI=new mconfigDA(db).getTypeMobile(db);
		this.db.close();
		return txtLinkAPI;
	}
	public String getLIVE(){
		this.db = getDb();
		String txtLinkAPI=new mconfigDA(db).getLIVE(db);
		this.db.close();
		return txtLinkAPI;
	}
	public String getBackGroundServiceOnline(){
		this.db = getDb();
		String valueBackGroundServiceOnline=new mconfigDA(db).getBackGroundServiceOnlineData(db);
		this.db.close();
		return valueBackGroundServiceOnline;
	}

	public clsStatusMenuStart checkUserActive() throws ParseException {
		this.db = getDb();
		boolean result = false;
		tUserLoginDA _tUserLoginDA=new tUserLoginDA(db);
		clsStatusMenuStart _clsStatusMenuStart =new clsStatusMenuStart();
		if(_tUserLoginDA.CheckLoginNow(db)){
			_clsStatusMenuStart.set_intStatus(enumStatusMenuStart.UserActiveLogin);
		} else{
			Boolean dvalid=false;
//			List<tSalesProductHeaderData> listDataPush= _tSalesProductHeaderDA.getAllDataToPushData(db);
//			List<tActivityData> listtActivityDataPush= _tActivityDA.getAllDataToPushData(db);
//			List<tAbsenUserData> listtAbsenUserDataPush= _tAbsenUserDA.getAllDataToPushData(db);
//			List<tLeaveMobileData> listTLeave= _tLeaveMobileDA.getAllDataPushData(db);
//			if(listDataPush != null && dvalid==false){
//				dvalid=true;
//			}
//			if(listtActivityDataPush != null && dvalid==false){
//				dvalid=true;
//			}
//			if(listtAbsenUserDataPush != null && dvalid==false){
//				dvalid=true;
//			}
//			if(listTLeave != null && dvalid==false){
//				dvalid=true;
//			}
//			if(listDataPush != null && dvalid==false){
//				dvalid=true;
//			}
			if(dvalid){
//				mMenuData listMenuData= _mMenuDA.getDataByName(db, "mnUploadDataMobile");
//				_clsStatusMenuStart.set_intStatus(enumStatusMenuStart.PushDataSPGMobile);
//				_clsStatusMenuStart.set_mMenuData(listMenuData);
			}else{
				new clsHelper().DeleteAllDB(db);
				_clsStatusMenuStart.set_intStatus(enumStatusMenuStart.FormLogin);
			}
		}
		return _clsStatusMenuStart;
	}
//	public tUserLoginData getUserActive() {
//		this.db = getDb();
//		tUserLoginDA _tUserLoginDA=new tUserLoginDA(db);
//		List<tUserLoginData> listData=_tUserLoginDA.getUserLoginNow(db);
//		db.close();
//		return listData.get(0);
//	}
	public String GenerateGuid(){
		 UUID uuid = UUID.randomUUID();
		 String randomUUIDString = uuid.toString();
		 return randomUUIDString;
	 }

	public JsonArray ResultJsonArray(String dt) throws ParseException{
		JsonParser jsonParser = new JsonParser();
		Object obj = jsonParser.parse(dt);
		JsonArray lang= (JsonArray) obj;
		return lang;
	}


//	public JSONArray callPushDataReturnJson(String versionName, String strJson, HashMap<String, byte[]> ListOfDataFile) throws Exception {
//		SQLiteDatabase _db = getDb();
//		Boolean flag = true;
//		String ErrorMess = "";
//		String txtMethod = "PushDataSPGMobile";
//		linkAPI dtlinkAPI = new linkAPI();
//		clsHelper _help = new clsHelper();
//		dtlinkAPI = new linkAPI();
//		dtlinkAPI.set_txtMethod(txtMethod);
//		tUserLoginDA _tUserLoginDA = new tUserLoginDA(_db);
//		tUserLoginData _dataUserLogin = _tUserLoginDA.getData(_db, 1);
////		dtlinkAPI.set_txtParam(_dataUserLogin.get_txtUserId() + "|||");
//		dtlinkAPI.set_txtToken(new clsHardCode().txtTokenAPI);
//		dtlinkAPI.set_txtVesion(versionName);
//		String strVal2 = "";
//		mconfigDA _mconfigDA = new mconfigDA(_db);
//		mconfigData dataAPI = _mconfigDA.getData(db, enumConfigData.ApiKalbe.getidConfigData());
//		strVal2 = dataAPI.get_txtValue();
//		if (dataAPI.get_txtValue() == "") {
//			strVal2 = dataAPI.get_txtDefaultValue();
//		}
//		dataAPI = _mconfigDA.getData(_db, enumConfigData.BackGroundServiceOnline.getidConfigData());
//		String TimeOut = dataAPI.get_txtValue();
//		String strLinkAPI = dtlinkAPI.QueryString(strVal2);
////		String JsonData = _help.PushDataWithFile(strLinkAPI, strJson, Integer.valueOf(TimeOut), ListOfDataFile);
//		//String JsonData= _help.ResultJsonData(_help.getHTML(strLinkAPI));
////		JSONArray JsonArray = _help.ResultJsonArray(JsonData);
////		APIData dtAPIDATA = new APIData();
////		Iterator i = JsonArray.iterator();
////		mCounterNumberDA _mCounterNumberDA = new mCounterNumberDA(_db);
////		while (i.hasNext()) {
////			org.json.simple.JSONObject innerObj = (org.json.simple.JSONObject) i.next();
////			int boolValid = Integer.valueOf(String.valueOf(innerObj.get(dtAPIDATA.boolValid)));
////			if (boolValid == Integer.valueOf(new clsHardCode().intSuccess)) {
////				mCounterNumberData _data = new mCounterNumberData();
////				_data.set_intId(enumCounterData.dtPushKBN.getidCounterData());
////				_data.set_txtDeskripsi((String) innerObj.get("_pstrMethodRequest"));
////				_data.set_txtName((String) innerObj.get("_pstrMethodRequest"));
////				_data.set_txtValue((String) innerObj.get("_pstrArgument"));
////				_mCounterNumberDA.SaveDataMConfig(_db, _data);
////			} else {
////				flag = false;
////				ErrorMess = (String) innerObj.get(dtAPIDATA.strMessage);
////				break;
////			}
////		}
////		_db.close();
//		return JsonArray;
//	}

//	public String GenerateGuid(Context context){
//		DeviceUuidFactory _DeviceUuidFactory=new DeviceUuidFactory(context);
//		return _DeviceUuidFactory.getDeviceUuid().toString();
//	 }
//	public clsStatusMenuStart checkUserActive() throws ParseException{
//		this.db = getDb();
//		tSalesProductHeaderDA _tSalesProductHeaderDA=new tSalesProductHeaderDA(db);
//		tUserLoginDA _tUserLoginDA=new tUserLoginDA(db);
//    	tActivityDA _tActivityDA=new tActivityDA(db);
//    	tAbsenUserDA _tAbsenUserDA=new tAbsenUserDA(db);
//    	tLeaveMobileDA _tLeaveMobileDA=new tLeaveMobileDA(db);
//    	mMenuDA _mMenuDA=new mMenuDA(db);
//    	clsStatusMenuStart _clsStatusMenuStart =new clsStatusMenuStart();
//    	if(_tUserLoginDA.CheckLoginNow(db)){
//    		List<tUserLoginData> listData=_tUserLoginDA.getUserLoginNow(db);
//    		_clsStatusMenuStart.set_intStatus(enumStatusMenuStart.UserActiveLogin);
//    	}else{
//    		Boolean dvalid=false;
//    		List<tSalesProductHeaderData> listDataPush= _tSalesProductHeaderDA.getAllDataToPushData(db);
//    		List<tActivityData> listtActivityDataPush= _tActivityDA.getAllDataToPushData(db);
//    		List<tAbsenUserData> listtAbsenUserDataPush= _tAbsenUserDA.getAllDataToPushData(db);
//    		List<tLeaveMobileData> listTLeave= _tLeaveMobileDA.getAllDataPushData(db);
//    		if(listDataPush != null && dvalid==false){
//    			dvalid=true;
//    		}
//    		if(listtActivityDataPush != null && dvalid==false){
//    			dvalid=true;
//    		}
//    		if(listtAbsenUserDataPush != null && dvalid==false){
//    			dvalid=true;
//    		}
//    		if(listTLeave != null && dvalid==false){
//    			dvalid=true;
//    		}
//    		if(listDataPush != null && dvalid==false){
//    			dvalid=true;
//    		}
//    		if(dvalid){
//    			mMenuData listMenuData= _mMenuDA.getDataByName(db, "mnUploadDataMobile");
//    			_clsStatusMenuStart.set_intStatus(enumStatusMenuStart.PushDataSPGMobile);
//    			_clsStatusMenuStart.set_mMenuData(listMenuData);
//    		}else{
//        		new clsHelper().DeleteAllDB(db);
//        		_clsStatusMenuStart.set_intStatus(enumStatusMenuStart.FormLogin);
//    		}
//    	}
//    	this.db.close();
//    	return _clsStatusMenuStart;
//	}
//	public dataJson GetAllPushData(String VersionName) throws Exception{
//		SQLiteDatabase db=getDb();
//		dataJson dtJson=new dataJson();
//		tAbsenUserDA _tAbsenUserDA=new tAbsenUserDA(db);
//		tLeaveMobileDA _tLeaveMobileDA=new tLeaveMobileDA(db);
//
//		db.close();
//		return dtJson;
//	}
//	public void PushData(String VersionName) throws Exception{
//		SQLiteDatabase _db=getDb();
//		mconfigDA _mconfigDA =new mconfigDA(_db);
//		String _StrLINKAPI="";
//		mconfigData dataAPI = _mconfigDA.getData(_db,enumConfigData.ApiKalbe.getidConfigData());
//		_StrLINKAPI = dataAPI.get_txtValue();
//		if (dataAPI.get_txtValue() == "") {
//			_StrLINKAPI = dataAPI.get_txtDefaultValue();
//		}
//		clsHelper _help =new clsHelper();
//		String root = new clsHardCode().txtPathUserData;
//		File myDir = new File(root);
//		myDir.mkdirs();
//		linkAPI dtlinkAPI=new linkAPI();
//		String txtMethod="PushDataTabsenUser";
//		dtlinkAPI.set_txtMethod(txtMethod);
//		dtlinkAPI.set_txtParam("");
//		dtlinkAPI.set_txtToken(new clsHardCode().txtTokenAPI);
//		dtlinkAPI.set_txtVesion(VersionName);
//		String strLinkAPI= dtlinkAPI.QueryString(_StrLINKAPI);
//
//		dataAPI = _mconfigDA.getData(_db,enumConfigData.BackGroundServiceOnline.getidConfigData());
//		String TimeOut = dataAPI.get_txtValue();
//		if (dataAPI.get_txtValue() == "") {
//			TimeOut = dataAPI.get_txtDefaultValue();
//		}
//
//		tAbsenUserDA _tAbsenUserDA=new tAbsenUserDA(_db);
//		List<tAbsenUserData> listDataAbsen= _tAbsenUserDA.getAllDataToPushData(_db);
//		if(listDataAbsen != null){
//			for (tAbsenUserData dataAbsen : listDataAbsen) {
//				List<tAbsenUserData> tmplistDataAbsen=new ArrayList<tAbsenUserData>();
//				tmplistDataAbsen.add(dataAbsen);
//				dataJson Json= new dataJson();
//				Json.setIntResult("1");
//				Json.setListOftAbsenUserData(tmplistDataAbsen);
//				//String Html= new clsHelper().pushtData(strLinkAPI,Json.txtJSON().toString(),Integer.valueOf(TimeOut));
//				String Html= new clsHelper().PushDataWithFile(strLinkAPI,Json.txtJSON().toString(),Integer.valueOf(TimeOut),String.valueOf(dataAbsen.get_txtImg1()),String.valueOf(dataAbsen.get_txtImg2()));
//				org.json.simple.JSONArray JsonArray= _help.ResultJsonArray(Html);
//				Iterator i = JsonArray.iterator();
//				while (i.hasNext()) {
//					APIData dtAPIDATA=new APIData();
//					org.json.simple.JSONObject innerObj = (org.json.simple.JSONObject) i.next();
//					int boolValid= Integer.valueOf(String.valueOf( innerObj.get(dtAPIDATA.boolValid)));
//					if(boolValid == Integer.valueOf(new clsHardCode().intSuccess)){
//						dataAbsen.set_intSync("1");
//						dataAbsen.set_txtAbsen(String.valueOf(innerObj.get(dtAPIDATA.strArgument)));
//						_tAbsenUserDA.SaveDatatAbsenUserData(_db, dataAbsen);
//					}
//				}
//			}
//		}
//
//		dtlinkAPI=new linkAPI();
//		txtMethod="PushDataTHeaderSales";
//		dtlinkAPI.set_txtMethod(txtMethod);
//		dtlinkAPI.set_txtParam("");
//		dtlinkAPI.set_txtToken(new clsHardCode().txtTokenAPI);
//		dtlinkAPI.set_txtVesion(VersionName);
//		strLinkAPI= dtlinkAPI.QueryString(_StrLINKAPI);
//
//		tSalesProductHeaderDA _tSalesProductHeaderDA=new tSalesProductHeaderDA(_db);
//		List<tSalesProductHeaderData> ListDataTsalesHeader= _tSalesProductHeaderDA.getAllDataToPushData(_db);
//		if(ListDataTsalesHeader!=null){
//			for (tSalesProductHeaderData dataheader : ListDataTsalesHeader) {
//				dataJson Json= new dataJson();
//				List<tSalesProductHeaderData> tmplistDatatSalesProductHeaderData=new ArrayList<tSalesProductHeaderData>();
//				tmplistDatatSalesProductHeaderData.add(dataheader);
//				tAbsenUserData _tmpdataabsen= _tAbsenUserDA.getData(_db, Integer.valueOf(dataheader.get_intIdAbsenUser()));
//				List<tAbsenUserData> tmpListDataUserAbsen=new ArrayList<tAbsenUserData>();
//				tmpListDataUserAbsen.add(_tmpdataabsen);
//				tSalesProductDetailDA _tSalesProductDetailDA=new tSalesProductDetailDA(_db);
//				List<tSalesProductDetailData> tmpListSalesProductDetail= _tSalesProductDetailDA.getSalesProductDetailByHeaderId(_db, dataheader.get_intId());
//				if(tmpListSalesProductDetail!=null){
//					Json.setListOftSalesProductDetailData(tmpListSalesProductDetail);
//				}
//				Json.setListOftAbsenUserData(tmpListDataUserAbsen);
//				Json.setListOftSalesProductHeaderData(tmplistDatatSalesProductHeaderData);
//				String Html= new clsHelper().pushtData(strLinkAPI,Json.txtJSON().toString(),Integer.valueOf(TimeOut));
//				org.json.simple.JSONArray JsonArray= _help.ResultJsonArray(Html);
//				Iterator i = JsonArray.iterator();
//				while (i.hasNext()) {
//					APIData dtAPIDATA=new APIData();
//					org.json.simple.JSONObject innerObj = (org.json.simple.JSONObject) i.next();
//					int boolValid= Integer.valueOf(String.valueOf( innerObj.get(dtAPIDATA.boolValid)));
//					if(boolValid == Integer.valueOf(new clsHardCode().intSuccess)){
//						dataheader.set_intSync("1");
//						_tSalesProductHeaderDA.SaveDatatSalesProductHeaderData(_db, dataheader);
//					}
//				}
//			}
//		}
//
//		dtlinkAPI=new linkAPI();
//		txtMethod="SaveDataTActivityMobile";
//		dtlinkAPI.set_txtMethod(txtMethod);
//		dtlinkAPI.set_txtParam("");
//		dtlinkAPI.set_txtToken(new clsHardCode().txtTokenAPI);
//		dtlinkAPI.set_txtVesion(VersionName);
//		strLinkAPI= dtlinkAPI.QueryString(_StrLINKAPI);
//
//		tActivityDA _tActivityDA=new tActivityDA(_db);
//		List<tActivityData> ListDatatActivityData= _tActivityDA.getAllDataToPushData(_db);
//		if(ListDatatActivityData!=null){
//			for (tActivityData dataActivity : ListDatatActivityData) {
//				List<tActivityData> tmpListDatatActivityData=new ArrayList<tActivityData>();
//				tmpListDatatActivityData.add(dataActivity);
//				dataJson Json= new dataJson();
//				Json.setIntResult("1");
//				Json.setListOftActivityData(tmpListDatatActivityData);
//				//
//				String Html= new clsHelper().PushDataWithFile(strLinkAPI,Json.txtJSON().toString(),Integer.valueOf(TimeOut),String.valueOf(dataActivity.get_txtImg1()),String.valueOf(dataActivity.get_txtImg2()));
//				org.json.simple.JSONArray JsonArray= _help.ResultJsonArray(Html);
//				Iterator i = JsonArray.iterator();
//				while (i.hasNext()) {
//					APIData dtAPIDATA=new APIData();
//					org.json.simple.JSONObject innerObj = (org.json.simple.JSONObject) i.next();
//					int boolValid= Integer.valueOf(String.valueOf( innerObj.get(dtAPIDATA.boolValid)));
//					if(boolValid == Integer.valueOf(new clsHardCode().intSuccess)){
//						dataActivity.set_intIdSyn(String.valueOf(innerObj.get(dtAPIDATA.strArgument)));
//						_tActivityDA.SaveDatatActivityData(_db, dataActivity);
//					}
//				}
//			}
//		}
//
//		dtlinkAPI=new linkAPI();
//		txtMethod="APISaveDatatCustomerBase";
//		dtlinkAPI.set_txtMethod(txtMethod);
//		dtlinkAPI.set_txtParam("");
//		dtlinkAPI.set_txtToken(new clsHardCode().txtTokenAPI);
//		dtlinkAPI.set_txtVesion(VersionName);
//		strLinkAPI= dtlinkAPI.QueryString(_StrLINKAPI);
//
//		dtlinkAPI=new linkAPI();
//		txtMethod="SaveDatatLeaveMobile";
//		dtlinkAPI.set_txtMethod(txtMethod);
//		dtlinkAPI.set_txtParam("");
//		dtlinkAPI.set_txtToken(new clsHardCode().txtTokenAPI);
//		dtlinkAPI.set_txtVesion(VersionName);
//		strLinkAPI= dtlinkAPI.QueryString(_StrLINKAPI);
//
//		tLeaveMobileDA _tLeaveMobileDA=new tLeaveMobileDA(_db);
//		List<tLeaveMobileData> listoftLeaveMobileData= _tLeaveMobileDA.getAllDataPushData(_db);
//		if(listoftLeaveMobileData!=null){
//			for (tLeaveMobileData dttLeaveMobileData : listoftLeaveMobileData) {
//				//_tLeaveMobileDA.deleteContact(_db, dttLeaveMobileData.get_intLeaveIdSync());
//				List<tLeaveMobileData> tmpListDatatLeaveMobileData=new ArrayList<tLeaveMobileData>();
//				tmpListDatatLeaveMobileData.add(dttLeaveMobileData);
//				dataJson Json= new dataJson();
//				Json.setIntResult("1");
//				Json.setListOftLeaveMobileData(tmpListDatatLeaveMobileData);
//				String Html= new clsHelper().pushtData(strLinkAPI,Json.txtJSON().toString(),Integer.valueOf(TimeOut));
//				org.json.simple.JSONArray JsonArray= _help.ResultJsonArray(Html);
//				Iterator i = JsonArray.iterator();
//				while (i.hasNext()) {
//					APIData dtAPIDATA=new APIData();
//					org.json.simple.JSONObject innerObj = (org.json.simple.JSONObject) i.next();
//					int boolValid= Integer.valueOf(String.valueOf( innerObj.get(dtAPIDATA.boolValid)));
//					if(boolValid == Integer.valueOf(new clsHardCode().intSuccess)){
//						dttLeaveMobileData.set_intLeaveIdSync(String.valueOf(innerObj.get(dtAPIDATA.strArgument)));
//						_tLeaveMobileDA.SaveDataMConfig(_db, dttLeaveMobileData);
//					}
//				}
//			}
//		}
//		_db.close();
//	}
//
//	public JSONArray getAllRegion() throws org.json.simple.parser.ParseException {
//		SQLiteDatabase _db = getDb();
//		mconfigDA _mconfigDA = new mconfigDA(_db);
//
//		String strVal2;
//		mconfigData dataAPI = _mconfigDA.getData(db, enumConfigData.ApiKalbe.getidConfigData());
//		strVal2 = dataAPI.get_txtValue();
//		if (dataAPI.get_txtValue().equals("")) {
//			strVal2 = dataAPI.get_txtDefaultValue();
//		}
//		clsHelper _help = new clsHelper();
//		linkAPI dtlinkAPI = new linkAPI();
//		String txtMethod = "GetListOfDataRegion";
//		dtlinkAPI.set_txtMethod(txtMethod);
//		dtlinkAPI.set_txtParam("");
//		dtlinkAPI.set_txtToken(new clsHardCode().txtTokenAPI);
//		String strLinkAPI = dtlinkAPI.QueryString(strVal2);
//
//		String JsonData;
//		JSONArray JsonArray = null;
//
//		try {
//			JsonData = _help.ResultJsonData(_help.getHTML(strLinkAPI));
//			JsonArray = _help.ResultJsonArray(JsonData);
//
//			return JsonArray;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return JsonArray;
//	}
//
//	public JSONArray getBranchByRegion(String region) throws org.json.simple.parser.ParseException {
//		SQLiteDatabase _db = getDb();
//		mconfigDA _mconfigDA = new mconfigDA(_db);
//
//		String strVal2;
//		mconfigData dataAPI = _mconfigDA.getData(db, enumConfigData.ApiKalbe.getidConfigData());
//		strVal2 = dataAPI.get_txtValue();
//		if (dataAPI.get_txtValue().equals("")) {
//			strVal2 = dataAPI.get_txtDefaultValue();
//		}
//		clsHelper _help = new clsHelper();
//		linkAPI dtlinkAPI = new linkAPI();
//		String txtMethod = "GetListCabangByRegion";
//		dtlinkAPI.set_txtMethod(txtMethod);
//		dtlinkAPI.set_txtParam(region.replaceAll(" ", "%20"));
//		dtlinkAPI.set_txtToken(new clsHardCode().txtTokenAPI);
//		String strLinkAPI = dtlinkAPI.QueryString(strVal2);
//
//		String JsonData;
//		JSONArray JsonArray = null;
//
//		try {
//			JsonData = _help.ResultJsonData(_help.getHTML(strLinkAPI));
//			JsonArray = _help.ResultJsonArray(JsonData);
//
//			return JsonArray;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return JsonArray;
//	}
//
//	public JSONArray getTypeOutletByBranch(String branch) throws org.json.simple.parser.ParseException {
//		SQLiteDatabase _db = getDb();
//		mconfigDA _mconfigDA = new mconfigDA(_db);
//
//		String strVal2;
//		mconfigData dataAPI = _mconfigDA.getData(db, enumConfigData.ApiKalbe.getidConfigData());
//		strVal2 = dataAPI.get_txtValue();
//		if (dataAPI.get_txtValue().equals("")) {
//			strVal2 = dataAPI.get_txtDefaultValue();
//		}
//		clsHelper _help = new clsHelper();
//		linkAPI dtlinkAPI = new linkAPI();
//		String txtMethod = "GetDataTipeSumberDataforWebDashboard";
//		dtlinkAPI.set_txtMethod(txtMethod);
//		dtlinkAPI.set_txtParam(branch.replaceAll(" ", "%20") + "|");
//		dtlinkAPI.set_txtToken(new clsHardCode().txtTokenAPI);
//		String strLinkAPI = dtlinkAPI.QueryString(strVal2);
//
//		String JsonData;
//		JSONArray JsonArray = null;
//
//		try {
//			JsonData = _help.ResultJsonData(_help.getHTML(strLinkAPI));
//			JsonArray = _help.ResultJsonArray(JsonData);
//
//			return JsonArray;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return JsonArray;
//	}
//
//	public JSONArray getRewardUserByDateAndCabang(String periode, String branch) throws org.json.simple.parser.ParseException {
//		SQLiteDatabase _db = getDb();
//		mconfigDA _mconfigDA = new mconfigDA(_db);
//
//		String strVal2;
//		mconfigData dataAPI = _mconfigDA.getData(db, enumConfigData.ApiKalbe.getidConfigData());
//		strVal2 = dataAPI.get_txtValue();
//		if (dataAPI.get_txtValue().equals("")) {
//			strVal2 = dataAPI.get_txtDefaultValue();
//		}
//		clsHelper _help = new clsHelper();
//		linkAPI dtlinkAPI = new linkAPI();
//		String txtMethod = "GetDTofReportWebDashboardOfTheMonthByTxtPeriodAndCabang";
//		dtlinkAPI.set_txtMethod(txtMethod);
//		dtlinkAPI.set_txtParam(periode + "|" + branch.replaceAll(" ", "%20"));
//		dtlinkAPI.set_txtToken(new clsHardCode().txtTokenAPI);
//		String strLinkAPI = dtlinkAPI.QueryString(strVal2);
//
//		String JsonData;
//		JSONArray JsonArray = null;
//
//		try {
//			JsonData = _help.ResultJsonData(_help.getHTML(strLinkAPI));
//			JsonArray = _help.ResultJsonArray(JsonData);
//
//			return JsonArray;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return JsonArray;
//	}
//
//
//	public JSONArray getDataSumberDataforWebDashboard(String branch, String typeOutlet, CharSequence keyword) throws org.json.simple.parser.ParseException {
//		SQLiteDatabase _db = getDb();
//		mconfigDA _mconfigDA = new mconfigDA(_db);
//
//		String strVal2;
//		mconfigData dataAPI = _mconfigDA.getData(db, enumConfigData.ApiKalbe.getidConfigData());
//		strVal2 = dataAPI.get_txtValue();
//		if (dataAPI.get_txtValue().equals("")) {
//			strVal2 = dataAPI.get_txtDefaultValue();
//		}
//		clsHelper _help = new clsHelper();
//		linkAPI dtlinkAPI = new linkAPI();
//		String txtMethod = "GetDataSumberDataforWebDashboard";
//		dtlinkAPI.set_txtMethod(txtMethod);
//
//		if (branch.equals("")||branch==null){
//			dtlinkAPI.set_txtParam(keyword + "|" + typeOutlet.replaceAll(" ", "%20") + "|" + "|");
//		} else {
//			dtlinkAPI.set_txtParam("|" + branch.replaceAll(" ", "%20") + "|" + typeOutlet.replaceAll(" ", "%20") + "|" + keyword);
//		}
//		dtlinkAPI.set_txtToken(new clsHardCode().txtTokenAPI);
//		String strLinkAPI = dtlinkAPI.QueryString(strVal2);
//
//		String JsonData;
//		JSONArray JsonArray = null;
//
//		try {
//			JsonData = _help.ResultJsonData(_help.getHTML(strLinkAPI));
//			JsonArray = _help.ResultJsonArray(JsonData);
//
//			return JsonArray;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return JsonArray;
//	}
//
//	public JSONArray getDataExistsInmGeolocationOutlet(String outletID, String branch) throws org.json.simple.parser.ParseException {
//		SQLiteDatabase _db = getDb();
//		mconfigDA _mconfigDA = new mconfigDA(_db);
//
//		String strVal2;
//		mconfigData dataAPI = _mconfigDA.getData(db, enumConfigData.ApiKalbe.getidConfigData());
//		strVal2 = dataAPI.get_txtValue();
//		if (dataAPI.get_txtValue().equals("")) {
//			strVal2 = dataAPI.get_txtDefaultValue();
//		}
//		clsHelper _help = new clsHelper();
//		linkAPI dtlinkAPI = new linkAPI();
//		String txtMethod = "CheckDataExistsInmGeolocationOutlet";
//		dtlinkAPI.set_txtMethod(txtMethod);
//		dtlinkAPI.set_txtParam( outletID +"|" + branch.replaceAll(" ", "%20"));
//		dtlinkAPI.set_txtToken(new clsHardCode().txtTokenAPI);
//		String strLinkAPI = dtlinkAPI.QueryString(strVal2);
//
//		String JsonData;
//		JSONArray JsonArray = null;
//
//		try {
//			JsonData = _help.ResultJsonData(_help.getHTML(strLinkAPI));
//			JsonArray = _help.ResultJsonArray(JsonData);
//
//			return JsonArray;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return JsonArray;
//	}
//
//	public JSONArray getDataCheckInWithPeriode(String userID, String periode) throws org.json.simple.parser.ParseException {
//		SQLiteDatabase _db = getDb();
//		mconfigDA _mconfigDA = new mconfigDA(_db);
//
//		String strVal2;
//		mconfigData dataAPI = _mconfigDA.getData(db, enumConfigData.ApiKalbe.getidConfigData());
//		strVal2 = dataAPI.get_txtValue();
//		if (dataAPI.get_txtValue().equals("")) {
//			strVal2 = dataAPI.get_txtDefaultValue();
//		}
//		clsHelper _help = new clsHelper();
//		linkAPI dtlinkAPI = new linkAPI();
//		String txtMethod = "GetDataCheckInWithPeriode";
//		dtlinkAPI.set_txtMethod(txtMethod);
//		dtlinkAPI.set_txtParam( "|" + "|" + "|" + userID + "|" + periode);
//		dtlinkAPI.set_txtToken(new clsHardCode().txtTokenAPI);
//		String strLinkAPI = dtlinkAPI.QueryString(strVal2);
//
//		String JsonData;
//		JSONArray JsonArray = null;
//
//		try {
//			JsonData = _help.ResultJsonData(_help.getHTML(strLinkAPI));
//			JsonArray = _help.ResultJsonArray(JsonData);
//
//			return JsonArray;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return JsonArray;
//	}
//	public JSONArray getReportSalesAPIByTxtCabang(String branch, String roleId) throws org.json.simple.parser.ParseException {
//		SQLiteDatabase _db = getDb();
//		mconfigDA _mconfigDA = new mconfigDA(_db);
//
//		String strVal2;
//		mconfigData dataAPI = _mconfigDA.getData(db, enumConfigData.ApiKalbe.getidConfigData());
//		strVal2 = dataAPI.get_txtValue();
//		if (dataAPI.get_txtValue().equals("")) {
//			strVal2 = dataAPI.get_txtDefaultValue();
//		}
//		clsHelper _help = new clsHelper();
//		linkAPI dtlinkAPI = new linkAPI();
//		String txtMethod = "GetAllReportSalesAPIByTxtCabang";
//		dtlinkAPI.set_txtMethod(txtMethod);
//		dtlinkAPI.set_txtParam( branch.replaceAll(" ", "%20") + "|" + roleId);
//		dtlinkAPI.set_txtToken(new clsHardCode().txtTokenAPI);
//		String strLinkAPI = dtlinkAPI.QueryString(strVal2);
//
//		String JsonData;
//		JSONArray JsonArray = null;
//
//		try {
//			JsonData = _help.ResultJsonData(_help.getHTML(strLinkAPI));
//			JsonArray = _help.ResultJsonArray(JsonData);
//
//			return JsonArray;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return JsonArray;
//	}
//
//	public JSONArray getReportOutstandingAdvance(String branch, String roleId) throws org.json.simple.parser.ParseException {
//		SQLiteDatabase _db = getDb();
//		mconfigDA _mconfigDA = new mconfigDA(_db);
//
//		String strVal2;
//		mconfigData dataAPI = _mconfigDA.getData(db, enumConfigData.ApiKalbe.getidConfigData());
//		strVal2 = dataAPI.get_txtValue();
//		if (dataAPI.get_txtValue().equals("")) {
//			strVal2 = dataAPI.get_txtDefaultValue();
//		}
//		clsHelper _help = new clsHelper();
//		linkAPI dtlinkAPI = new linkAPI();
//		String txtMethod = "GetReportOutstandingAdvance";
//		dtlinkAPI.set_txtMethod(txtMethod);
//		dtlinkAPI.set_txtParam( branch.replaceAll(" ", "%20") + "|" + roleId);
//		dtlinkAPI.set_txtToken(new clsHardCode().txtTokenAPI);
//		String strLinkAPI = dtlinkAPI.QueryString(strVal2);
//
//		String JsonData;
//		JSONArray JsonArray = null;
//
//		try {
//			JsonData = _help.ResultJsonData(_help.getHTML(strLinkAPI));
//			JsonArray = _help.ResultJsonArray(JsonData);
//
//			return JsonArray;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return JsonArray;
//	}
//
//	public JSONArray getBanner(String userId, String roleId, String value) throws org.json.simple.parser.ParseException {
//		SQLiteDatabase _db = getDb();
//		mconfigDA _mconfigDA = new mconfigDA(_db);
//
//		String strVal2;
//		mconfigData dataAPI = _mconfigDA.getData(db, enumConfigData.ApiKalbe.getidConfigData());
//		strVal2 = dataAPI.get_txtValue();
//		if (dataAPI.get_txtValue().equals("")) {
//			strVal2 = dataAPI.get_txtDefaultValue();
//		}
//		clsHelper _help = new clsHelper();
//		linkAPI dtlinkAPI = new linkAPI();
//		String txtMethod = "GetAllMWebBannerForMobile";
//		dtlinkAPI.set_txtMethod(txtMethod);
//		dtlinkAPI.set_txtParam( userId+"|"+roleId+"|"+value);
//		dtlinkAPI.set_txtToken(new clsHardCode().txtTokenAPI);
//		String strLinkAPI = dtlinkAPI.QueryString(strVal2);
//
//		String JsonData;
//		JSONArray JsonArray = null;
//
//		try {
//			JsonData = _help.ResultJsonData(_help.getHTML(strLinkAPI));
//			JsonArray = _help.ResultJsonArray(JsonData);
//
//			return JsonArray;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return JsonArray;
//	}
//	public JSONArray saveDataCheckInFromMobile(String txtOutletId, String txtUserID, String txtBranchName, String txtLong, String txtLat, String txtGType, String userId, String flag, String txtAcc) throws org.json.simple.parser.ParseException {
//		SQLiteDatabase _db = getDb();
//		mconfigDA _mconfigDA = new mconfigDA(_db);
//
//		String strVal2;
//		mconfigData dataAPI = _mconfigDA.getData(db, enumConfigData.ApiKalbe.getidConfigData());
//		strVal2 = dataAPI.get_txtValue();
//		if (dataAPI.get_txtValue().equals("")) {
//			strVal2 = dataAPI.get_txtDefaultValue();
//		}
//		clsHelper _help = new clsHelper();
//		linkAPI dtlinkAPI = new linkAPI();
//		String txtMethod = "SaveDataCheckInFromMobile";
//		dtlinkAPI.set_txtMethod(txtMethod);
//		dtlinkAPI.set_txtParam(txtOutletId + "|" + txtUserID + "|" + txtBranchName.replaceAll(" ", "%20") + "|" + txtLong + "|" + txtLat + "|" + txtGType + "|" + txtUserID + "|" + flag + "|" + txtAcc);
//		dtlinkAPI.set_txtToken(new clsHardCode().txtTokenAPI);
//		String strLinkAPI = dtlinkAPI.QueryString(strVal2);
//
//		String JsonData;
//		JSONArray JsonArray = null;
//
//		try {
//			JsonData = _help.ResultJsonData(_help.getHTML(strLinkAPI));
//			JsonArray = _help.ResultJsonArray(JsonData);
//
//			return JsonArray;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return JsonArray;
//	}
}
