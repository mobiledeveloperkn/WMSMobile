package library.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class dataJson {

	public synchronized mSPMHeaderData getmSPMHeaderData() {
		return _mSPMHeaderData;
	}
	public synchronized void setmSPMHeaderData(mSPMHeaderData dtmSPMHeaderData) {
		_mSPMHeaderData = dtmSPMHeaderData;
	}

	public String getTxtMethod() {
		return txtMethod;
	}
	public void setTxtMethod(String txtMethod) {
		this.txtMethod = txtMethod;
	}

	public synchronized String getTxtValue() {
		return txtValue;
	}
	public synchronized void setTxtValue(String txtValue) {
		this.txtValue = txtValue;
	}

	private String _txtUserId;
	private String _txtVersionName;
	public String get_txtUserId() {
		return _txtUserId;
	}
	public void set_txtUserId(String _txtUserId) {
		this._txtUserId = _txtUserId;
	}
	public String get_txtVersionName() {
		return _txtVersionName;
	}
	public void set_txtVersionName(String _txtVersionName) {
		this._txtVersionName = _txtVersionName;
	}

	public JSONObject txtJSON() throws JSONException {
		JSONObject resJson = new JSONObject();
		JSONObject jObjectHeader = new JSONObject();
		Collection<JSONObject> itemsListJquey = new ArrayList<JSONObject>();
		try
		{
			if(this.getmSPMHeaderData() != null){
				mSPMHeaderData _mSPMHeaderData=new mSPMHeaderData();
				_mSPMHeaderData = this.getmSPMHeaderData();
					JSONObject item1 = new JSONObject();
				jObjectHeader.put(_mSPMHeaderData.Property_intSPMId, String.valueOf(_mSPMHeaderData.getIntSPMId()));
				jObjectHeader.put(_mSPMHeaderData.Property_txtNoSPM, String.valueOf(_mSPMHeaderData.getTxtNoSPM()));
				jObjectHeader.put(_mSPMHeaderData.Property_txtBranchCode, String.valueOf(_mSPMHeaderData.getTxtBranchCode()));
				jObjectHeader.put(_mSPMHeaderData.Property_txtBranchName, String.valueOf(_mSPMHeaderData.getTxtBranchName()));
				jObjectHeader.put(_mSPMHeaderData.Property_txtSalesOrder, String.valueOf(_mSPMHeaderData.getTxtSalesOrder()));
				jObjectHeader.put(_mSPMHeaderData.Property_intUserId, String.valueOf(_mSPMHeaderData.getIntUserId()));
				jObjectHeader.put(_mSPMHeaderData.Property_bitStatus, String.valueOf(_mSPMHeaderData.getBitStatus()));
					itemsListJquey.add(item1);
				resJson.put(_mSPMHeaderData.Property_mSPMHeaderData, new JSONObject(String.valueOf(jObjectHeader)));
			}

			if(this.get_ListOfmSPMDetailData()!=null){
				itemsListJquey = new ArrayList<JSONObject>();
				for (mSPMDetailData data : this.get_ListOfmSPMDetailData()) {
					JSONObject item1 = new JSONObject();
					item1.put(data.Property_intSPMDetailId , String.valueOf(data.getIntSPMDetailId()));
					item1.put(data.Property_txtNoSPM , String.valueOf(data.getTxtNoSPM()));
					item1.put(data.Property_txtLocator , String.valueOf(data.getTxtLocator()));
					item1.put(data.Property_txtItemCode , String.valueOf(data.getTxtItemCode()));
					item1.put(data.Property_txtItemName , String.valueOf(data.getTxtItemName()));
					item1.put(data.Property_intQty , String.valueOf(data.getIntQty()));
					item1.put(data.Property_bitStatus , String.valueOf(data.getBitStatus()));
					item1.put(data.Property_txtReason , String.valueOf(data.getTxtReason()));
					itemsListJquey.add(item1);
				}
				resJson.put("ListOfmSPMDetailData", new JSONArray(itemsListJquey));
			}
		}
		catch(Exception ex)
		{
		
		}
		resJson.put(Property_intResult, getIntResult());
		resJson.put(Property_txtDescription, getTxtDescription());
		resJson.put(Property_txtMessage, getTxtMessage());
		resJson.put(Property_txtMethod, getTxtMethod());
		resJson.put(Property_txtValue, getTxtValue());
//		resJson.put(Property_txtSessionLoginId, get_txtSessionLoginId());
		resJson.put(Property_txtUserId, get_txtUserId());
		resJson.put(Property_txtVersionName, get_txtVersionName());
		return resJson;
	}
//	public JSONObject GenerateJsontSalesProductDetailData(tSalesProductDetailData data) throws JSONException {
//		JSONObject item1 = new JSONObject();
//		item1.put(data.Property_dtDate, String.valueOf(data.get_dtDate()));
//		item1.put(data.Property_intActive, String.valueOf(data.get_intActive()));
//		item1.put(data.Property_intId, String.valueOf(data.get_intId()));
//		item1.put(data.Property_intPrice, String.valueOf(data.get_intPrice()));
//		item1.put(data.Property_intQty, String.valueOf(data.get_intQty()));
//		item1.put(data.Property_intTotal, String.valueOf(data.get_intTotal()));
//		item1.put(data.Property_txtCodeProduct, String.valueOf(data.get_txtCodeProduct()));
//		item1.put(data.Property_txtKeterangan, String.valueOf(data.get_txtKeterangan()));
//		item1.put(data.Property_txtNameProduct, String.valueOf(data.get_txtNameProduct()));
//		item1.put(data.Property_txtNIK, String.valueOf(data.get_txtNIK()));
//		item1.put(data.Property_txtNoSo, String.valueOf(data.get_txtNoSo()));
//		return item1;
//	}
//	public JSONObject GenerateJsontSalesProductHeaderData(tSalesProductHeaderData data) throws JSONException {
//		JSONObject item1 = new JSONObject();
//		item1.put(data.Property_intId, String.valueOf(data.get_intId()));
//		item1.put(data.Property_txtKeterangan, String.valueOf(data.get_txtKeterangan()));
//		item1.put(data.Property_txtNIK, String.valueOf(data.get_txtNIK()));
//		item1.put(data.Property_txtBranchCode, String.valueOf(data.get_txtBranchCode()));
//		item1.put(data.Property_txtBranchName, String.valueOf(data.get_txtBranchName()));
//		item1.put(data.Property_txtDate, String.valueOf(data.get_dtDate()));
//		item1.put(data.Property_intIdAbsenUser, String.valueOf(data.get_intIdAbsenUser()));
//		item1.put(data.Property_intSumAmount, String.valueOf(data.get_intSumAmount()));
//		item1.put(data.Property_intSumItem, String.valueOf(data.get_intSumItem()));
//		item1.put(data.Property_OutletCode, String.valueOf(data.get_OutletCode()));
//		item1.put(data.Property_OutletName, String.valueOf(data.get_OutletName()));
//		item1.put(data.Property_UserId, String.valueOf(data.get_UserId()));
//		return item1;
//	}
//
//	public JSONObject GenerateJsontInventorySPGDetail_mobile(tInventorySPGDetail_mobileData data) throws JSONException {
//		JSONObject item1 = new JSONObject();
//		item1.put(data.Property_dtDate, String.valueOf(data.get_dtDate()));
//		item1.put(data.Property_intActive, String.valueOf(data.get_intActive()));
//		item1.put(data.Property_intId, String.valueOf(data.get_intId()));
//		item1.put(data.Property_intQtyDisplay, String.valueOf(data.get_intQtyDisplay()));
//		item1.put(data.Property_intQtyGudang, String.valueOf(data.get_intQtyGudang()));
//		item1.put(data.Property_intTotal, String.valueOf(data.get_intTotal()));
//		item1.put(data.Property_txtCodeProduct, String.valueOf(data.get_txtCodeProduct()));
//		item1.put(data.Property_txtKeterangan, String.valueOf(data.get_txtKeterangan()));
//		item1.put(data.Property_txtNameProduct, String.valueOf(data.get_txtNameProduct()));
//		item1.put(data.Property_txtNIK, String.valueOf(data.get_txtNIK()));
//		item1.put(data.Property_txtNoInv, String.valueOf(data.get_txtNoInv()));
//		return item1;
//	}
//	public JSONObject GenerateJsontInventorySPGHeader_mobile(tInventorySPGHeader_mobileData data) throws JSONException {
//		JSONObject item1 = new JSONObject();
//		item1.put(data.Property_intId, String.valueOf(data.get_intId()));
//		item1.put(data.Property_txtKeterangan, String.valueOf(data.get_txtKeterangan()));
//		item1.put(data.Property_txtNIK, String.valueOf(data.get_txtNIK()));
//		item1.put(data.Property_txtBranchCode, String.valueOf(data.get_txtBranchCode()));
//		item1.put(data.Property_txtBranchName, String.valueOf(data.get_txtBranchName()));
//		item1.put(data.Property_txtDate, String.valueOf(data.get_dtDate()));
//		item1.put(data.Property_intIdAbsenUser, String.valueOf(data.get_intIdAbsenUser()));
//		item1.put(data.Property_intSumAmount, String.valueOf(data.get_intSumAmount()));
//		item1.put(data.Property_intSumItem, String.valueOf(data.get_intSumItem()));
//		item1.put(data.Property_OutletCode, String.valueOf(data.get_OutletCode()));
//		item1.put(data.Property_OutletName, String.valueOf(data.get_OutletName()));
//		item1.put(data.Property_UserId, String.valueOf(data.get_UserId()));
//		return item1;
//	}
//
//
//
//	public JSONObject GenerateJsontCustomerBaseDetailData(tCustomerBaseDetailData data) throws JSONException {
//		JSONObject item1 = new JSONObject();
//		item1.put(data.Property_intCustomerId, String.valueOf(data.get_intCustomerId()));
//		item1.put(data.Property_intQty, String.valueOf(data.get_intQty()));
//		item1.put(data.Property_txtProductBrandCode, String.valueOf(data.get_txtProductBrandCode()));
//		item1.put(data.Property_txtProductBrandName, String.valueOf(data.get_txtProductBrandName()));
//		return item1;
//	}
//
//	public JSONObject GenerateJsontCustomerBaseData(tCustomerBaseData data) throws JSONException {
//		JSONObject item1 = new JSONObject();
//		item1.put(data.Property_bitActive, String.valueOf(data.get_bitActive()));
//		item1.put(data.Property_dtDate, String.valueOf(data.get_dtDate()));
//		item1.put(data.Property_intCustomerId, String.valueOf(data.get_intCustomerId()));
//		item1.put(data.Property_intCustomerIdSync, String.valueOf(data.get_intCustomerIdSync()));
//		item1.put(data.Property_intSubmit, String.valueOf(data.get_intSubmit()));
//		item1.put(data.Property_txtAlamat, String.valueOf(data.get_txtAlamat()));
//		item1.put(data.Property_txtBranchId, String.valueOf(data.get_txtBranchId()));
//		item1.put(data.Property_txtDeviceId, String.valueOf(data.get_txtDeviceId()));
//		item1.put(data.Property_txtNama, String.valueOf(data.get_txtNama()));
//		item1.put(data.Property_txtOutletId, String.valueOf(data.get_txtOutletId()));
//		item1.put(data.Property_txtProductBrandCode, String.valueOf(data.get_txtProductBrandCode()));
//		item1.put(data.Property_txtSex, String.valueOf(data.get_txtSex()));
//		item1.put(data.Property_txtTelp, String.valueOf(data.get_txtTelp()));
//		item1.put(data.Property_txtUserId, String.valueOf(data.get_txtUserId()));
//		return item1;
//	}
	
	public String getIntResult() {
		return intResult;
	}
	public void setIntResult(String intResult) {
		this.intResult = intResult;
	}
	public String getTxtMessage() {
		return txtMessage;
	}
	public void setTxtMessage(String txtMessage) {
		this.txtMessage = txtMessage;
	}
	public String getTxtDescription() {
		return txtDescription;
	}
	public void setTxtDescription(String txtDescription) {
		this.txtDescription = txtDescription;
	}
	public List<mconfigData> getListDatamConfig() {
		return ListDatamConfig;
	}
	public void setListDatamConfig(List<mconfigData> listDatamConfig) {
		ListDatamConfig = listDatamConfig;
	}
	public dataJson() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	private String intResult;
	private String txtMessage;
	private String txtMethod;
	private String txtValue;
	private String txtDescription;
	private List<mconfigData> ListDatamConfig;
	private List<tUserLoginData> ListDatatUserLogin;
	private List<tDeviceInfoUserData> ListDatatDeviceInfoUser;
	private mSPMHeaderData _mSPMHeaderData;
	private List<mSPMDetailData> ListOfmSPMDetailData;

	public List<mSPMDetailData> get_ListOfmSPMDetailData() {
		return ListOfmSPMDetailData;
	}
	public void set_ListOfmSPMDetailData(List<mSPMDetailData> _ListOfmSPMDetailData) {
		this.ListOfmSPMDetailData = _ListOfmSPMDetailData;
	}

	public String Property_txtUserId="txtUserId";
	public String Property_txtSessionLoginId="txtSessionLoginId";
	public String Property_intResult="intResult";
	public String Property_txtMessage="txtMessage";
	public String Property_txtDescription="txtDescription";
	public String Property_txtMethod="txtMethod";
	public String Property_txtValue="txtValue";
	public String Property_txtVersionName="txtVersionName";
}
