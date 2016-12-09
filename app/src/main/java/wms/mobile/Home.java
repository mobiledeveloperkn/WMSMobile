package wms.mobile;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import bl.clsMainBL;
import bl.mSPMHeaderBL;
import jim.h.common.android.lib.zxing.config.ZXingLibConfig;
import jim.h.common.android.lib.zxing.integrator.IntentIntegrator;
import jim.h.common.android.lib.zxing.integrator.IntentResult;
import library.common.mSPMHeaderData;
import library.dal.mSPMDetailDA;
import library.dal.mSPMHeaderDA;
import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.MessageReceivedHandler;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.LongPollingTransport;
import service.SignalRService;

public class Home extends AppCompatActivity implements View.OnClickListener {

    Button btnOutstandingTask, btnScan;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ZXingLibConfig zxingLibConfig;

    mSPMHeaderData _mSPMHeaderData;

    private final Context mContext = this;
    private SignalRService mService;
    private boolean mBound = false;

    // private fields
    HubConnection connection;
    HubProxy hub;
    ClientTransport transport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnOutstandingTask = (Button) findViewById(R.id.btn_outstanding);
        btnScan = (Button) findViewById(R.id.btn_scan);

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setElevation(0);

        Logger logger = new Logger() {
            @Override
            public void log(String message, LogLevel logLevel) {
                Log.e("SignalR", message);
            }
        };

        Platform.loadPlatformComponent(new AndroidPlatformComponent());
        connection = new HubConnection("http://10.171.10.8:8082");
        hub = connection.createHubProxy("chatHub"); // case insensitivity

        /* ****new codes here**** */
        hub.subscribe(this);

        transport = new LongPollingTransport(connection.getLogger());

        /* ****new codes here**** */
        connection.start(transport);

        /* ****new codes here**** */
        /* ****seems useless but should be here!**** */
        hub.subscribe(new Object() {
            @SuppressWarnings("unused")
            public void newMessage(final String message, final String messageId, final String chatId,
                                   final String senderUserId, final String fileUrl, final String replyToMessageId) {


            }
        });


        /* ********************** */
        /* ****new codes here**** */
        /* **** the main method that I fetch data from server**** */
        connection.received(new MessageReceivedHandler() {
            @Override
            public void onMessageReceived(final JsonElement json) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        JsonObject jsonObject = json.getAsJsonObject();
                        Log.e("<Debug>", "response = " + jsonObject.toString());
                        Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        hub.invoke("broadcastMessage", "Halo");

        btnOutstandingTask.setOnClickListener(this);
        btnScan.setOnClickListener(this);

        _mSPMHeaderData = new mSPMHeaderData();
        _mSPMHeaderData = new mSPMHeaderBL().GetAllData();

        if (_mSPMHeaderData.getIntSPMId()==null){
            btnOutstandingTask.setEnabled(false);
            btnScan.setEnabled(true);
        } else if (_mSPMHeaderData.getIntSPMId()!=null){
            btnOutstandingTask.setEnabled(true);
            btnScan.setEnabled(false);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_outstanding :
                startActivity(new Intent(Home.this, OutstandingTask.class));
                break;
            case  R.id.btn_scan :
                IntentIntegrator.initiateScan(this, zxingLibConfig);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SQLiteDatabase db = new clsMainBL().getDb();

        if(requestCode==IntentIntegrator.REQUEST_CODE){
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scanResult.getContents() == null && scanResult.getFormatName()==null) {
                return;
            }
            final String result = scanResult.getContents();
            new clsMainActivity().showCustomToast(getApplicationContext(), result, true);

//            if (mBound) {
                // Call a method from the SignalRService.
                // However, if this call were something that might hang, then this request should
                // occur in a separate thread to avoid slowing down the activity performance.
                if (result != null) {
//                    String receiver = "Tes";
//                    String message = result;
//                    mService.sendMessage_To(receiver, message, message);
                    new clsMainActivity().showCustomToast(getApplicationContext(),result,true);
                    mSPMHeaderDA _mSPMHeaderDA = new mSPMHeaderDA(db);
                    mSPMDetailDA _mSPMDetailDA = new mSPMDetailDA(db);
                    int sumdata_mSPMHeaderDA = _mSPMHeaderDA.getContactsCount(db);
		            if (sumdata_mSPMHeaderDA == 0) {
			        _mSPMHeaderDA.InsertDefaultSPMHeader(db);
                    _mSPMDetailDA.InsertDefaultmSPMDetail(db);
                    startActivity(new Intent(Home.this, OutstandingTask.class));
                    }
                }
//            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent();
        intent.setClass(mContext, SignalRService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        _mSPMHeaderData = new mSPMHeaderData();
        _mSPMHeaderData = new mSPMHeaderBL().GetAllData();

        if (_mSPMHeaderData.getIntSPMId()==null){
            btnOutstandingTask.setEnabled(false);
            btnScan.setEnabled(true);
        } else if (_mSPMHeaderData.getIntSPMId()!=null){
            btnOutstandingTask.setEnabled(true);
            btnScan.setEnabled(false);
        }
    }

    @Override
    protected void onStop() {
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
        super.onStop();
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
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}
