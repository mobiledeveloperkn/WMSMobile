package service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
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
    public static final String BROADCAST_ERROR = "BROADCAST_ERROR";
    String DATA_PASSED_GETROLE = "DATA_PASSED_GETROLE";
    String DATA_PASSED_LOGIN = "DATA_PASSED_LOGIN";
    String DATA_PASSED_GETDATASPM = "DATA_PASSED_GETDATASPM";
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
//
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent("service.SignalRService");
        sendBroadcast(broadcastIntent);
        super.onDestroy();
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

    public void getRole(String txtEmail, String pInfo) {
        String METHOD_SERVER = "getRoleByUsername";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("txtUsername", txtEmail);
            jsonObject.put("pInfo", pInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(mBound){
            mHubProxy.invoke(METHOD_SERVER, jsonObject.toString());
        } else {
            startSignalR();
        }
    }

    public void login(String txtEmail, String pass, String roleId) {
        String METHOD_SERVER = "login";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("txtUsername", txtEmail);
            jsonObject.put("txtPassword", pass);
            jsonObject.put("txtRoleId", roleId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mBound){
            mHubProxy.invoke(METHOD_SERVER, jsonObject.toString());
        } else {
            startSignalR();
        }
    }

    public void getDataSPM(String result, String intUserId) {
        String METHOD_SERVER = "getDataSPM";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("txtBarcode", result);
            jsonObject.put("intUserId", intUserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(mBound){
            mHubProxy.invoke(METHOD_SERVER, jsonObject.toString());
        } else {
            startSignalR();
        }
    }

    private void startSignalR() {
        Platform.loadPlatformComponent(new AndroidPlatformComponent());

        String serverUrl = "http://10.171.10.8/wmsonline";
        mHubConnection = new HubConnection(serverUrl);
        String SERVER_HUB_CHAT = "hubAPI";
        mHubProxy = mHubConnection.createHubProxy(SERVER_HUB_CHAT);
        ClientTransport clientTransport = new ServerSentEventsTransport(mHubConnection.getLogger());
        SignalRFuture<Void> signalRFuture = mHubConnection.start(clientTransport);

        try {
            signalRFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d("SimpleSignalR", e.toString());
            Intent intent = new Intent();
            intent.setAction(BROADCAST_ERROR);
            intent.putExtra(DATA_PASSED_ERROR, e.toString());
            sendBroadcast(intent);
            return;
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

                try {
                    json = new JSONObject(jsonString);

                    JSONArray jsonArray = json.getJSONArray("A");

                    String jsonArrayString = jsonArray.get(0).toString();

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
                    Intent intent = new Intent();
                    intent.setAction(BROADCAST_ERROR);
                    intent.putExtra(DATA_PASSED_ERROR, e.toString());
                    sendBroadcast(intent);
                }


            }
        });
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
