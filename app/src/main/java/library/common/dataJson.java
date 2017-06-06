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
        try {
            if (this.getmSPMHeaderData() != null) {
                mSPMHeaderData _mSPMHeaderData = new mSPMHeaderData();
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

            if (this.get_ListOfmSPMDetailData() != null) {
                itemsListJquey = new ArrayList<JSONObject>();
                for (mSPMDetailData data : this.get_ListOfmSPMDetailData()) {
                    JSONObject item1 = new JSONObject();
                    item1.put(data.Property_intSPMDetailId, String.valueOf(data.getIntSPMDetailId()));
                    item1.put(data.Property_txtNoSPM, String.valueOf(data.getTxtNoSPM()));
                    item1.put(data.Property_txtLocator, String.valueOf(data.getTxtLocator()));
                    item1.put(data.Property_txtItemCode, String.valueOf(data.getTxtItemCode()));
                    item1.put(data.Property_txtItemName, String.valueOf(data.getTxtItemName()));
                    item1.put(data.Property_intQty, String.valueOf(data.getIntQty()));
                    item1.put(data.Property_bitStatus, String.valueOf(data.getBitStatus()));
                    item1.put(data.Property_txtReason, String.valueOf(data.getTxtReason()));
                    itemsListJquey.add(item1);
                }
                resJson.put("ListOfmSPMDetailData", new JSONArray(itemsListJquey));
            }

            if(this.getListOftTimrLogData() != null){
                itemsListJquey = new ArrayList<JSONObject>();
                for (tTimerLogData data : this.getListOftTimrLogData()) {
                    JSONObject item1 = new JSONObject();
                    item1.put(data.Property_txtTimerType, String.valueOf(data.getTxtTimerType()));
                    item1.put(data.Property_txtTimerStatus, String.valueOf(data.getTxtTimerStatus()));
                    item1.put(data.Property_txtStarNo, String.valueOf(data.getTxtStarNo()));
                    item1.put(data.Property_dtExecute, String.valueOf(data.getDtExecute()));
                    item1.put(data.Property_bitActive, String.valueOf(data.getBitActive()));
                    item1.put(data.Property_txtTimerLogId, String.valueOf(data.getTxtTimerLogId()));
                    itemsListJquey.add(item1);
                }
                resJson.put("ListOftTimrLogData", new JSONArray(itemsListJquey));
            }

        } catch (Exception ex) {

        }
        resJson.put(Property_intResult, getIntResult());
        resJson.put(Property_txtDescription, getTxtDescription());
        resJson.put(Property_txtMessage, getTxtMessage());
        resJson.put(Property_txtMethod, getTxtMethod());
        resJson.put(Property_txtValue, getTxtValue());
        resJson.put(Property_txtUserId, get_txtUserId());
        resJson.put(Property_txtVersionName, get_txtVersionName());
        return resJson;
    }

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
    private List<tTimerLogData> ListOftTimrLogData;

    public List<mSPMDetailData> get_ListOfmSPMDetailData() {
        return ListOfmSPMDetailData;
    }

    public void set_ListOfmSPMDetailData(List<mSPMDetailData> _ListOfmSPMDetailData) {
        this.ListOfmSPMDetailData = _ListOfmSPMDetailData;
    }

    public String Property_txtUserId = "txtUserId";
    public String Property_txtSessionLoginId = "txtSessionLoginId";
    public String Property_intResult = "intResult";
    public String Property_txtMessage = "txtMessage";
    public String Property_txtDescription = "txtDescription";
    public String Property_txtMethod = "txtMethod";
    public String Property_txtValue = "txtValue";
    public String Property_txtVersionName = "txtVersionName";

    public List<tTimerLogData> getListOftTimrLogData() {
        return ListOftTimrLogData;
    }

    public void setListOftTimrLogData(List<tTimerLogData> listOftTimrLogData) {
        ListOftTimrLogData = listOftTimrLogData;
    }
}
