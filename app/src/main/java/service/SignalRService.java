package service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
    public static final String BROADCAST = "MY_ACTION";
    String DATA_PASSED = "DATA_PASSED";
    private HubConnection mHubConnection;
    private HubProxy mHubProxy;
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
        startSignalR();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mHubConnection.stop();
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
        mHubProxy.invoke(METHOD_SERVER, jsonObject.toString());
    }

    private void startSignalR() {
        Platform.loadPlatformComponent(new AndroidPlatformComponent());

        String serverUrl = "http://10.171.10.13/wmsonline";
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
            intent.setAction(BROADCAST);
            intent.putExtra(DATA_PASSED, e.toString());
            sendBroadcast(intent);
            return;
        }

        mHubConnection.received(new MessageReceivedHandler() {
            @Override
            public void onMessageReceived(JsonElement jsonElement) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
//                jsonObject = jsonObject.getAsJsonObject("A");
                Intent intent = new Intent();
                intent.setAction(BROADCAST);
                intent.putExtra(DATA_PASSED, jsonObject.toString());
                sendBroadcast(intent);
            }
        });
    }
}
