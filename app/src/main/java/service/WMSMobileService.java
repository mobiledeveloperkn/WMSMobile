package service;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
//import com.microsoft.signalr.HubConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import addon.MyApplication;
import bl.clsMainBL;
import bl.mSPMDetailBL;
import bl.mSPMHeaderBL;
import bl.tUserLoginBL;
import library.common.clsHelper;
import library.common.clsStatusMenuStart;
import library.common.enumConfigData;
import library.common.mSPMDetailData;
import library.common.mSPMHeaderData;
import library.common.mconfigData;
import library.common.tUserLoginData;
import library.dal.clsHardCode;
import library.dal.enumStatusMenuStart;
import library.dal.mconfigDA;

import microsoft.aspnet.signalr.client.MessageReceivedHandler;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;
import wms.mobile.OutstandingTask;
import wms.mobile.TabsTaskHeader;

//import microsoft.aspnet.signalr.client.MessageReceivedHandler;
//import microsoft.aspnet.signalr.client.Platform;
//import microsoft.aspnet.signalr.client.SignalRFuture;
//import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
//import microsoft.aspnet.signalr.client.hubs.HubConnection;
//import microsoft.aspnet.signalr.client.transport.ClientTransport;
//import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;

/**
 * Created by ASUS ZE on 26/12/2016.
 */

public class WMSMobileService extends Service {

    public static HubConnection mHubConnection;
    private static HubProxy mHubProxy;
    private final IBinder mBinder = new LocalBinder(); // Binder given to clients
    ResultReceiver resultReceiver;

    public static mHubConnectionSevice mHubConnectionSevice;
    public static mHubConnectionSlow mHubConnectionSlow;
    public static updateSnackbar updateSnackbar;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("EXIT", "onCreate--------------------------------------------------------------------------!");
        // to display Toast message
        Handler mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        resultReceiver = intent.getParcelableExtra("receiver");
        Log.i("EXIT", "onStartCommand--------------------------------------------------------------------------!");
//        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        startSignalR();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("EXIT", "ondestroy--------------------------------------------------------------------------!");
        Intent broadcastIntent = new Intent("service.MyRebootReceiver");
        sendBroadcast(broadcastIntent);
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent broadcastIntent = new Intent("service.MyRebootReceiver");
        sendBroadcast(broadcastIntent);
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Return the communication channel to the service.
        Log.i("EXIT", "onBind--------------------------------------------------------------------------!");
//        startSignalR();
        return null;
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public WMSMobileService getService() {
            // Return this instance of SignalRService so clients can call public methods
            return WMSMobileService.this;
        }
    }

    public boolean checkinConnHub() {
        boolean valid = false;
        if (mHubConnection.getConnectionId() != null) {
            valid = true;
        }
        return valid;
    }

    public boolean getDataLastVersion(String versionName) {
        String METHOD_SERVER = new clsHardCode().txtMethodServerGetVersionName;
        boolean status = false;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pInfo", versionName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mHubConnection.getConnectionId() != null) {
            mHubProxy.invoke(METHOD_SERVER, jsonObject.toString());
            status = true;
        }
        return status;
    }

    public boolean getRole(String txtEmail, String pInfo) {
        String METHOD_SERVER = new clsHardCode().txtMethodServerGetRole;
        boolean status = false;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("txtUsername", txtEmail);
            jsonObject.put("pInfo", pInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mHubConnection.getConnectionId() != null) {
            mHubProxy.invoke(METHOD_SERVER, jsonObject.toString());
            status = true;
        }
        return status;
    }

    public boolean login(String txtEmail, String pass, String roleId, String pInfo, String intUserId) {
        boolean status = false;
        String METHOD_SERVER = new clsHardCode().txtMethodServerLogin;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("txtUsername", txtEmail);
            jsonObject.put("txtPassword", pass);
            jsonObject.put("txtRoleId", roleId);
            jsonObject.put("pInfo", pInfo);
            jsonObject.put("intUserId", intUserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mHubConnection.getConnectionId() != null) {
            mHubProxy.invoke(METHOD_SERVER, jsonObject.toString());
//                mHubProxy.wait(4000);
            status = true;
        }
        return status;
    }

    public boolean logout(String txtDataId, String versionName, String intUserId, String json) {
        boolean status = false;
        String METHOD_SERVER = new clsHardCode().txtMethodServerLogout;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("txtDataId", txtDataId);
            jsonObject.put("pInfo", versionName);
            jsonObject.put("intUserId", intUserId);
            jsonObject.put("dataTimer", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mHubConnection.getConnectionId() != null) {
            mHubProxy.invoke(METHOD_SERVER, jsonObject.toString());
            status = true;
        }
        return status;
    }

    public boolean getDataSPM(String result, String intUserId, String versionName) {
        boolean status = false;
        String METHOD_SERVER = new clsHardCode().txtMethodServerGetNoSPM;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("txtBarcode", result);
            jsonObject.put("intUserId", intUserId);
            jsonObject.put("pInfo", versionName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mHubConnection.getConnectionId() != null) {
            mHubProxy.invoke(METHOD_SERVER, jsonObject.toString());
            status = true;
        }
        return status;
    }

    public boolean refreshDataSTAR(String result, String intUserId, String versionName) {
        boolean status = false;
        String METHOD_SERVER = new clsHardCode().txtMethodServerRefreshDataSTAR;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("txtBarcode", result);
            jsonObject.put("intUserId", intUserId);
            jsonObject.put("pInfo", versionName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mHubConnection.getConnectionId() != null) {
            mHubProxy.invoke(METHOD_SERVER, jsonObject.toString());
            status = true;
        }
        return status;
    }

    public boolean checkWaitingDataSTAR(String result, String intUserId, String versionName) {
        boolean status = false;
        String METHOD_SERVER = new clsHardCode().txtMethodServerCheckWaitingDataSTAR;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("intSPMHeaderId", result);
            jsonObject.put("intUserId", intUserId);
            jsonObject.put("pInfo", versionName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mHubConnection.getConnectionId() != null) {
            mHubProxy.invoke(METHOD_SERVER, jsonObject.toString());
            status = true;
        }
        return status;
    }

    public boolean confirmSPMDetail(String intSPMDetailId, String intUserId, String versionName) {
        boolean status = false;
        String METHOD_SERVER = new clsHardCode().txtMethodServerConfirmSPMDetail;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("intSPMDetailId", intSPMDetailId);
            jsonObject.put("intUserId", intUserId);
            jsonObject.put("pInfo", versionName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mHubConnection.getConnectionId() != null) {
            mHubProxy.invoke(METHOD_SERVER, jsonObject.toString());
            status = true;
        }
        return status;
    }

    public boolean undoCancelSPMDetail(String intSPMDetailId, String intUserId, String versionName) {
        boolean status = false;
        String METHOD_SERVER = new clsHardCode().txtMethodServerUndoCancelSPMDetail;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("intSPMDetailId", intSPMDetailId);
            jsonObject.put("intUserId", intUserId);
            jsonObject.put("pInfo", versionName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mHubConnection.getConnectionId() != null) {
            mHubProxy.invoke(METHOD_SERVER, jsonObject.toString());
            status = true;
        }
        return status;
    }

    public boolean confirmSPMHeader(String intSPMHeaderId, String versionName, String intUserId, String json) {
        boolean status = false;
        String METHOD_SERVER = new clsHardCode().txtMethodServerConfirmSPMHeader;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("intSPMHeaderId", intSPMHeaderId);
            jsonObject.put("pInfo", versionName);
            jsonObject.put("intUserId", intUserId);
            jsonObject.put("dataTimer", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mHubConnection.getConnectionId() != null) {
            mHubProxy.invoke(METHOD_SERVER, jsonObject.toString());
            status = true;
        }
        return status;
    }

    public boolean cancelSPMDetail(String intSPMDetailId, String intUserId, String reason, String versionName) {
        boolean status = false;
        String METHOD_SERVER = new clsHardCode().txtMethodServerCancelSPMDetail;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("intSPMDetailId", intSPMDetailId);
            jsonObject.put("intUserId", intUserId);
            jsonObject.put("txtReason", reason);
            jsonObject.put("pInfo", versionName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mHubConnection.getConnectionId() != null) {
            mHubProxy.invoke(METHOD_SERVER, jsonObject.toString());
            status = true;
        }
        return status;
    }

    public boolean refreshSPMHeader(String intSPMHeaderId, String versionName) {
        boolean status = false;
        String METHOD_SERVER = new clsHardCode().txtMethodServerRefreshSPMHeader;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("intSPMHeaderId", intSPMHeaderId);
            jsonObject.put("pInfo", versionName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mHubConnection.getConnectionId() != null) {
            mHubProxy.invoke(METHOD_SERVER, jsonObject.toString());
            status = true;
        }
        return status;
    }

    public boolean pushFromOfflineAct(String json, String versionName) {
        boolean status = false;
        String METHOD_SERVER = new clsHardCode().txtMethodPushData;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("dataJson", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mHubConnection.getConnectionId() != null) {
            mHubProxy.invoke(METHOD_SERVER, json);
            status = true;
        }
        return status;
    }

    public void updateConnectionId(String intUserId) {
        String METHOD_SERVER = new clsHardCode().txtUpdateConnectionId;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("intUserId", intUserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mHubConnection.getConnectionId() != null) {
            mHubProxy.invoke(METHOD_SERVER, jsonObject.toString());
        }
    }

    public void getLatestSTAR(String txtSTARNumber) {
        String METHOD_SERVER = new clsHardCode().txtGetLatestSTAR;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("txtSTARNumber", txtSTARNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mHubConnection.getConnectionId() != null) {
            mHubProxy.invoke(METHOD_SERVER, jsonObject.toString());
        }
    }

    public static boolean startSignalR() {
        boolean status;
        Platform.loadPlatformComponent(new AndroidPlatformComponent());

        SQLiteDatabase db = new clsMainBL().getDb();
        mconfigDA _mconfigDA = new mconfigDA(db);

        int sumdata = _mconfigDA.getContactsCount(db);
        if (sumdata == 0) {
            _mconfigDA.InsertDefaultMconfig(db);
        }

        String serverUrl;
        mconfigData dataAPI = _mconfigDA.getData(db, enumConfigData.ApiKalbe.getidConfigData());
        serverUrl = dataAPI.get_txtValue();
        if (dataAPI.get_txtValue().equals("")) {
            serverUrl = dataAPI.get_txtDefaultValue();
        }

        mHubConnection = new HubConnection(serverUrl);
        String SERVER_HUB_CHAT = new clsHardCode().txtServerHubName;
        mHubProxy = mHubConnection.createHubProxy(SERVER_HUB_CHAT);

        ClientTransport clientTransport = new ServerSentEventsTransport(mHubConnection.getLogger());
        SignalRFuture<Void> signalRFuture = mHubConnection.start(clientTransport);

        try {
            signalRFuture.get();
            status = true;
        } catch (InterruptedException | ExecutionException e) {
            status = false;
        }

        mHubConnection.received(jsonElement -> {
            JSONObject json;
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String jsonString = jsonObject.toString();
            Log.i("Buat Dewi", "masuk di onreceived");
            boolean valid = false;
            try {
                json = new JSONObject(jsonString);
//                JSONObject jsonArray2 = json.getJSONObject("H");
                if (json.getString("H").equals("HubAPI")){
                    valid = true;
                }
                JSONArray jsonArray = json.getJSONArray("A");
                String jsonArrayString = jsonArray.get(0).toString();
                JSONObject jsonObjectFinal = new JSONObject(jsonArrayString);
                String strMethodName = jsonObjectFinal.get("strMethodName").toString();

                if (strMethodName.equalsIgnoreCase("ConfirmSPMDetail")) {
                    initMethodConfirmSPMDetail(jsonObjectFinal);
                } else if (strMethodName.equalsIgnoreCase("cancelSPMDetail")) {
                    initMethodCancelSPMDetail(jsonObjectFinal);
                } else if(strMethodName.equalsIgnoreCase("revertCancelSPMDetail")){
                    initMethodRevertSPMDetail(jsonObjectFinal);
                } else if (strMethodName.equalsIgnoreCase("pushDataOffline")) {
                    updateFromPushDataOffline(jsonObjectFinal);
                }

                if (strMethodName.equals("getLatestSTAR")) {
                    JSONObject jsonObjectHeader = jsonObjectFinal.getJSONObject("listOfmSPMHeader");

                    String status1 = jsonObjectHeader.get("STATUS").toString();
                    String sync = jsonObjectHeader.get("SYNC").toString();

                    mSPMHeaderData _mSPMHeaderData = new mSPMHeaderData();

                    tUserLoginData dataLogin;
                    dataLogin = new tUserLoginBL().getUserActive();
                    MyApplication.getInstance().setFinishInsert(false);
                    if (status1.equals("1") && sync.equals("0")) {

                        SQLiteDatabase db1 = new clsMainBL().getDb();
                        new clsHelper().DeleteHeaderDetailStar(db1);

                        _mSPMHeaderData.setIntSPMId(jsonObjectHeader.get("SPM_HEADER_ID").toString());
                        _mSPMHeaderData.setTxtNoSPM(jsonObjectHeader.get("SPM_NO").toString());
                        _mSPMHeaderData.setTxtBranchCode(jsonObjectHeader.get("BRANCH_CODE").toString());
                        _mSPMHeaderData.setTxtSalesOrder(jsonObjectHeader.get("SALES_ORDER").toString());
                        _mSPMHeaderData.setIntUserId(jsonObjectHeader.get("USER_ID").toString());
                        _mSPMHeaderData.setBitStatus(jsonObjectHeader.get("SYNC").toString());
                        _mSPMHeaderData.setBitSync(jsonObjectHeader.get("SYNC").toString());
                        _mSPMHeaderData.setBitStart("0");
                        _mSPMHeaderData.setIntUserId(dataLogin.getIntUserId());

                        new mSPMHeaderBL().saveData(_mSPMHeaderData);

                        JSONArray jsonArrayInner = jsonObjectFinal.getJSONArray("listOfmSPMDetail");

                        JSONObject jsonObjectNew;

                        for (int i = 0; i < jsonArrayInner.length(); i++) {

                            jsonObjectNew = jsonArrayInner.getJSONObject(i);

                            mSPMDetailData data = new mSPMDetailData();

                            data.setIntSPMDetailId(jsonObjectNew.get("SPM_DETAIL_ID").toString());
                            data.setTxtNoSPM(jsonObjectNew.get("SPM_NO").toString());
                            data.setTxtLocator(jsonObjectNew.get("LOCATOR").toString());
                            data.setTxtItemCode(jsonObjectNew.get("ITEM_CODE").toString());
                            data.setTxtItemName(jsonObjectNew.get("ITEM_NAME").toString());
                            data.setIntQty(jsonObjectNew.get("QUANTITY").toString());
                            data.setBitStatus(jsonObjectNew.get("STATUS").toString());
                            data.setBitSync(jsonObjectNew.get("SYNC").toString());
                            data.setTxtUOM(jsonObjectNew.get("UOM").toString());
                            data.setTxtLotNumber(jsonObjectNew.get("LOT_NUM").toString());
                            data.setIntUserId(dataLogin.getIntUserId());
                            new mSPMDetailBL().insert(data);
                        }
                        MyApplication.getInstance().setFinishInsert(true);
//                        if (mHubConnectionSevice != null) {
//                            WMSMobileService.mHubConnectionSevice.onReceiveMessageHub(jsonObjectFinal, false);
//                        }
//                        try{
//                            new TabsTaskHeader().updateListView();
//                            new OutstandingTask().setCircleReport();
//                        }
//                        catch (Exception ignored){
//
//                        }
                    }
                }

                if (mHubConnectionSevice != null) {
                    WMSMobileService.mHubConnectionSevice.onReceiveMessageHub(jsonObjectFinal, false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                if (mHubConnectionSevice != null && valid) {
                    JSONObject jsonObjectFinal = new JSONObject();
                    WMSMobileService.mHubConnectionSevice.onReceiveMessageHub(jsonObjectFinal, true);
                }
                Log.i("Buat Dewi", e.getMessage());
            }
        });

        clsStatusMenuStart _clsStatusMenuStart = new clsStatusMenuStart();
        try {
            _clsStatusMenuStart = new clsMainBL().checkUserActive();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (_clsStatusMenuStart.get_intStatus() == enumStatusMenuStart.UserActiveLogin) {
            tUserLoginData _tUserLoginData;
            _tUserLoginData = new tUserLoginBL().getUserActive();

            new WMSMobileService().updateConnectionId(_tUserLoginData.getIntUserId());
        }

        return status;
    }

    private static void initMethodRevertSPMDetail(JSONObject jsonObject) {
        String strMethodName, strMessage, boolValid, intRoleId, txtRoleName, dtInsert, dtUpdated;
        String txtNoSPM;

        try {
            boolValid = jsonObject.get("boolValid").toString();
            strMessage = jsonObject.get("strMessage").toString();
            strMethodName = jsonObject.get("strMethodName").toString();

            if (boolValid.equalsIgnoreCase("true")) {
                JSONObject jsonObjectHeader = jsonObject.getJSONObject("listOfmSPMDetail");

                String status = jsonObjectHeader.get("STATUS").toString();
                String sync = jsonObjectHeader.get("SYNC").toString();
                String _intSPMDetailId = jsonObjectHeader.get("SPM_DETAIL_ID").toString();
                String txtLocator = jsonObjectHeader.get("LOCATOR").toString();
                txtNoSPM = jsonObjectHeader.getString("SPM_NO").toString();

                if (status.equals("0") && sync.equals("0")) {
                    tUserLoginData dataLogin = new tUserLoginBL().getUserActive();

                    new mSPMDetailBL().updateDataRevertById(_intSPMDetailId, dataLogin.getIntUserId());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private static void initMethodCancelSPMDetail(JSONObject jsonObject) {
        String strMethodName, strMessage, boolValid, intRoleId, txtRoleName, dtInsert, dtUpdated;
        String txtNoSPM;

        try {
            boolValid = jsonObject.get("boolValid").toString();
            strMessage = jsonObject.get("strMessage").toString();
            strMethodName = jsonObject.get("strMethodName").toString();

            if (boolValid.equalsIgnoreCase("true")) {
                JSONObject jsonObjectHeader = jsonObject.getJSONObject("listOfmSPMDetail");

                String status = jsonObjectHeader.get("STATUS").toString();
                String sync = jsonObjectHeader.get("SYNC").toString();
                String _intSPMDetailId = jsonObjectHeader.get("SPM_DETAIL_ID").toString();
                String _reason = jsonObjectHeader.get("REASON").toString();
                txtNoSPM = jsonObjectHeader.getString("SPM_NO").toString();

                if (status.equals("2") && sync.equals("1")) {
                    tUserLoginData dataLogin = new tUserLoginBL().getUserActive();
                    new mSPMDetailBL().updateDataSPMCancelById(_intSPMDetailId, dataLogin.getIntUserId(), _reason);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void initMethodConfirmSPMDetail(JSONObject jsonObject) {
        String strMethodName, strMessage, boolValid, intRoleId, txtRoleName, dtInsert, dtUpdated;
        String txtNoSPM;

        try {
            boolValid = jsonObject.get("boolValid").toString();
            strMessage = jsonObject.get("strMessage").toString();
            strMethodName = jsonObject.get("strMethodName").toString();

            if (boolValid.equalsIgnoreCase("true")) {
                JSONObject jsonObjectHeader = jsonObject.getJSONObject("listOfmSPMDetail");

                String status = jsonObjectHeader.getString("STATUS");
                String sync = jsonObjectHeader.getString("SYNC");
                String _intSPMDetailId = jsonObjectHeader.getString("SPM_DETAIL_ID");
                String txtLocator = jsonObjectHeader.getString("LOCATOR");
                txtNoSPM = jsonObjectHeader.getString("SPM_NO");

                if (status.equals("1") && sync.equals("1")) {

                    tUserLoginData dataLogin = new tUserLoginBL().getUserActive();

                    new mSPMDetailBL().updateDataValueById(_intSPMDetailId, dataLogin.getIntUserId());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void updateFromPushDataOffline(JSONObject jsonObject) {
        String strMethodName, strMessage, boolValid, intRoleId, txtRoleName, dtInsert, dtUpdated;
        String txtNoSPM = "";

        try {
            boolValid = jsonObject.get("boolValid").toString();

            if (boolValid.equalsIgnoreCase("true")) {

                if (!jsonObject.isNull("listOfmSPMHeader")) {
                    JSONObject jsonObjectSPMHeader = jsonObject.getJSONObject("listOfmSPMHeader");

                    String status = jsonObjectSPMHeader.get("STATUS").toString();
                    String sync = jsonObjectSPMHeader.get("SYNC").toString();

                    mSPMHeaderData _mSPMHeaderData = new mSPMHeaderData();

                    if (status.equals("1") && sync.equals("1")) {
                        _mSPMHeaderData.setIntSPMId(jsonObjectSPMHeader.get("SPM_HEADER_ID").toString());
                        _mSPMHeaderData.setTxtNoSPM(jsonObjectSPMHeader.get("SPM_NO").toString());
                        _mSPMHeaderData.setTxtBranchCode(jsonObjectSPMHeader.get("BRANCH_CODE").toString());
                        _mSPMHeaderData.setTxtSalesOrder(jsonObjectSPMHeader.get("SALES_ORDER").toString());
                        _mSPMHeaderData.setIntUserId(jsonObjectSPMHeader.get("USER_ID").toString());
                        _mSPMHeaderData.setBitStatus(jsonObjectSPMHeader.get("STATUS").toString());
                        _mSPMHeaderData.setBitSync(jsonObjectSPMHeader.get("SYNC").toString());

                        new mSPMHeaderBL().saveDataPush(_mSPMHeaderData.getIntSPMId());
                    }
                }

                if (!jsonObject.isNull("listOfmSPMDetail")) {
                    JSONArray jsonArraySPMDetail = jsonObject.getJSONArray("listOfmSPMDetail");

                    for (int i = 0; i < jsonArraySPMDetail.length(); i++) {
                        String jsonInner = jsonArraySPMDetail.get(i).toString();
                        jsonObject = new JSONObject(jsonInner);
                        String statusDetail = jsonObject.get("STATUS").toString();
                        String syncDetail = jsonObject.get("SYNC").toString();
                        String id = jsonObject.get("SPM_DETAIL_ID").toString();

                        new mSPMDetailBL().saveFromPushData(id, statusDetail, syncDetail);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface mHubConnectionSevice {
        void onReceiveMessageHub(JSONObject jsonObject, boolean isCatch);
    }

    public interface updateSnackbar {
        void onUpdateSnackBar(boolean info);
    }

    public interface mHubConnectionSlow {
        void onConnectionSlow(boolean info);
    }
}
