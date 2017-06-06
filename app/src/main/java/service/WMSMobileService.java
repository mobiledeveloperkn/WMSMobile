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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import bl.clsMainBL;
import library.common.enumConfigData;
import library.common.mconfigData;
import library.dal.clsHardCode;
import library.dal.mconfigDA;
import microsoft.aspnet.signalr.client.MessageReceivedHandler;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;

/**
 * Created by ASUS ZE on 26/12/2016.
 */

public class WMSMobileService extends Service {

    public static HubConnection mHubConnection;
    private static HubProxy mHubProxy;
    private static boolean mBound = false;
    private Handler mHandler; // to display Toast message
    private final IBinder mBinder = new LocalBinder(); // Binder given to clients
    ResultReceiver resultReceiver;

    public static mHubConnectionSevice mHubConnectionSevice;
    public static mHubConnectionSlow mHubConnectionSlow;
    public static updateSnackbar updateSnackbar;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("EXIT", "onCreate--------------------------------------------------------------------------!");
        mHandler = new Handler(Looper.getMainLooper());
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

    public boolean checkinConnHub(){
        boolean valid = false;
        if(mHubConnection.getConnectionId()!=null){
            valid = true;
        }
        return valid;
    }

    public boolean getDataLastVersion(String versionName){
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
        } else {
            status = false;
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
            } else {
                status = false;
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
                status = true;
            } else {
                status = false;
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
        } else {
            status = false;
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
            } else {
                status = false;
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
        } else {
            status = false;
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
            } else {
                status = false;
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
            } else {
                status = false;
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
        } else {
            status = false;
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
        } else {
            status = false;
        }
        return status;
    }

    public  boolean pushFromOfflineAct(String json, String versionName){
        boolean status = false;
        String METHOD_SERVER = new clsHardCode().txtMethodPushData;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("dataJson", json);
//            jsonObject.put("pInfo", versionName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mHubConnection.getConnectionId() != null) {
            mHubProxy.invoke(METHOD_SERVER, json);
            status = true;
        } else {
            status = false;
        }
        return status;
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

        mHubConnection.received(new MessageReceivedHandler() {
            @Override
            public void onMessageReceived(JsonElement jsonElement) {
                JSONObject json = null;
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String jsonString = jsonObject.toString();

                try {
                    json = new JSONObject(jsonString);
                    JSONArray jsonArray = json.getJSONArray("A");
                    String jsonArrayString = jsonArray.get(0).toString();
                    JSONObject jsonObjectFinal = new JSONObject(jsonArrayString);
//                    String strMethodName = jsonObject.get("strMethodName").toString();

                    if (mHubConnectionSevice != null) {
                        service.WMSMobileService.mHubConnectionSevice.onReceiveMessageHub(jsonObjectFinal);
                    }
//                    if(updateSnackbar != null && strMethodName.equalsIgnoreCase("pushDataOffline")){
//                        WMSMobileService.updateSnackbar.onUpdateSnackBar(true);
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return status;
    }

    public interface mHubConnectionSevice {
        void onReceiveMessageHub(JSONObject jsonObject);
    }

    public interface updateSnackbar {
        void onUpdateSnackBar(boolean info);
    }

    public interface mHubConnectionSlow {
        void onConnectionSlow(boolean info);
    }
}
