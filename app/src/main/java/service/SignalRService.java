package service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
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

public class SignalRService extends Service {
    public static final String BROADCAST_GETROLE = "BROADCAST_GETROLE";
    public static final String BROADCAST_LOGIN = "BROADCAST_LOGIN";
    public static final String BROADCAST_GETDATASPM = "BROADCAST_GETDATASPM";
    public static final String BROADCAST_CONFIRMDATASPMDETAIL = "BROADCAST_CONFIRMDATASPMDETAIL";
    public static final String BROADCAST_CONFIRMDATASPMHEADER = "BROADCAST_CONFIRMDATASPMHEADER";
    public static final String BROADCAST_ERROR = "BROADCAST_ERROR";
    String DATA_PASSED_GETROLE = "DATA_PASSED_GETROLE";
    String DATA_PASSED_LOGIN = "DATA_PASSED_LOGIN";
    String DATA_PASSED_GETDATASPM = "DATA_PASSED_GETDATASPM";
    String DATA_PASSED_CONFIRMDATASPMDETAIL = "DATA_PASSED_CONFIRMDATASPMDETAIL";
    String DATA_PASSED_CONFIRMDATASPMHEADER = "DATA_PASSED_CONFIRMDATASPMHEADER";
    String DATA_PASSED_ERROR = "DATA_PASSED_ERROR";
    private static HubConnection mHubConnection;
    private static HubProxy mHubProxy;
    private static boolean mBound = false;
    private Handler mHandler; // to display Toast message
    private final IBinder mBinder = new LocalBinder(); // Binder given to clients
    ResultReceiver resultReceiver;

    public SignalRService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        resultReceiver = intent.getParcelableExtra("receiver");
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
//        startSignalR();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent("service.SignalRService");
        sendBroadcast(broadcastIntent);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // Return the communication channel to the service.
        startSignalR();
        return mBinder;
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public SignalRService getService() {
            // Return this instance of SignalRService so clients can call public methods
            return SignalRService.this;
        }
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
        if(mBound){
            if(mHubConnection.getConnectionId()!=null){
                mHubProxy.invoke(METHOD_SERVER, jsonObject.toString());
                status = true;
            } else {
                status = false;
            }
        } else {
             status = false;
        }
        return status;
    }

    public boolean login(String txtEmail, String pass, String roleId) {
        boolean status = false;
        String METHOD_SERVER = new clsHardCode().txtMethodServerLogin;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("txtUsername", txtEmail);
            jsonObject.put("txtPassword", pass);
            jsonObject.put("txtRoleId", roleId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mBound){
            if(mHubConnection.getConnectionId()!=null){
                mHubProxy.invoke(METHOD_SERVER, jsonObject.toString());
                status = true;
            } else {
                status = false;
            }
        } else {
            status = false;
        }
        return status;
    }

    public boolean getDataSPM(String result, String intUserId) {
        boolean status = false;
        String METHOD_SERVER = new clsHardCode().txtMethodServerGetNoSPM;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("txtBarcode", result);
            jsonObject.put("intUserId", intUserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(mBound){
            if(mHubConnection.getConnectionId()!=null){
                mHubProxy.invoke(METHOD_SERVER, jsonObject.toString());
                status = true;
            } else {
                status = false;
            }
        } else {
            status = false;
        }
        return status;
    }

    public boolean confirmSPMDetail(String intSPMDetailId, String intUserId) {
        boolean status = false;
        String METHOD_SERVER = new clsHardCode().txtMethodServerConfirmSPMDetail;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("intSPMDetailId", intSPMDetailId);
            jsonObject.put("intUserId", intUserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(mBound){
            if(mHubConnection.getConnectionId()!=null){
                mHubProxy.invoke(METHOD_SERVER, jsonObject.toString());
                status = true;
            } else {
                status = false;
            }
        } else {
            status = false;
        }
        return status;
    }

    public boolean confirmSPMHeader(String intSPMHeaderId) {
        boolean status = false;
        String METHOD_SERVER = new clsHardCode().txtMethodServerConfirmSPMHeader;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("intSPMHeaderId", intSPMHeaderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(mBound){
            if(mHubConnection.getConnectionId()!=null){
                mHubProxy.invoke(METHOD_SERVER, jsonObject.toString());
                status = true;
            } else {
                status = false;
            }
        } else {
            status = false;
        }
        return status;
    }

    public String startSignalR() {
        String status = null;
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
            status = String.valueOf(mHubConnection.getLogger());
        } catch (InterruptedException | ExecutionException e) {
//            Log.d("SimpleSignalR", e.toString());
//            Intent intent = new Intent();
//            intent.setAction(BROADCAST_ERROR);
//            intent.putExtra(DATA_PASSED_ERROR, e.toString());
//            sendBroadcast(intent);
            status = e.toString();
        }

        mHubConnection.connected(new Runnable() {
            @Override
            public void run() {
                mBound = true;
            }
        });

        mHubConnection.received(new MessageReceivedHandler() {
            @Override
            public void onMessageReceived(JsonElement jsonElement) {
                JSONObject json = null;
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String strMethodName, strMessage, boolValid, intRoleId, txtRoleName, dtInsert, dtUpdated;

                String jsonString = jsonObject.toString();

//                tUserLoginData _tUserLoginData = new tUserLoginData();
//                _tUserLoginData.setTxtUserName(jsonString);
//                new tUserLoginBL().saveData(_tUserLoginData);

                try {
                    json = new JSONObject(jsonString);

                    JSONArray jsonArray = json.getJSONArray("A");

                    String jsonArrayString = jsonArray.get(0).toString();

//                    Toast.makeText(getApplicationContext(), jsonArrayString, Toast.LENGTH_SHORT).show();

                    JSONObject jsonObjectFinal = new JSONObject(jsonArrayString);

                    boolValid = jsonObjectFinal.get("boolValid").toString();
                    strMessage = jsonObjectFinal.get("strMessage").toString();
                    strMethodName = jsonObjectFinal.get("strMethodName").toString();

                    if(boolValid.equalsIgnoreCase("true")){
                        if(strMethodName.equalsIgnoreCase("GetRoleByUsername")){
                            Intent intent = new Intent();
                            intent.setAction(BROADCAST_GETROLE);
                            intent.putExtra(DATA_PASSED_GETROLE, jsonObjectFinal.toString());
                            sendBroadcast(intent);
                        } else if (strMethodName.equalsIgnoreCase("Login")){
                            Intent intent = new Intent();
                            intent.setAction(BROADCAST_LOGIN);
                            intent.putExtra(DATA_PASSED_LOGIN, jsonObjectFinal.toString());
                            sendBroadcast(intent);
                        } else if (strMethodName.equalsIgnoreCase("GetDataSPM")){
                            Intent intent = new Intent();
                            intent.setAction(BROADCAST_GETDATASPM);
                            intent.putExtra(DATA_PASSED_GETDATASPM, jsonObjectFinal.toString());
                            sendBroadcast(intent);
                        } else if (strMethodName.equalsIgnoreCase("confirmSPMDetail")){
                            Intent intent = new Intent();
                            intent.setAction(BROADCAST_CONFIRMDATASPMDETAIL);
                            intent.putExtra(DATA_PASSED_CONFIRMDATASPMDETAIL, jsonObjectFinal.toString());
                            sendBroadcast(intent);
                        } else if (strMethodName.equalsIgnoreCase("confirmSPMHeader")){
                            Intent intent = new Intent();
                            intent.setAction(BROADCAST_CONFIRMDATASPMHEADER);
                            intent.putExtra(DATA_PASSED_CONFIRMDATASPMHEADER, jsonObjectFinal.toString());
                            sendBroadcast(intent);
                        }
                    } else if (boolValid.equalsIgnoreCase("false")){
                        if (strMethodName.equalsIgnoreCase("GetDataSPM")){
                            Intent intent = new Intent();
                            intent.setAction(BROADCAST_GETDATASPM);
                            intent.putExtra(DATA_PASSED_GETDATASPM, jsonObjectFinal.toString());
                            sendBroadcast(intent);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
//                    Intent intent = new Intent();
//                    intent.setAction(BROADCAST_ERROR);
//                    intent.putExtra(DATA_PASSED_ERROR, e.toString());
//                    sendBroadcast(intent);
                }


            }
        });
        return status;
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to SignalRService, cast the IBinder and get SignalRService instance
            SignalRService.LocalBinder binder = (SignalRService.LocalBinder) service;
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            Toast.makeText(getApplicationContext(), "dis", Toast.LENGTH_SHORT).show();
        }
    };
}
