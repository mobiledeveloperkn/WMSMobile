package wms.mobile;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bl.clsMainBL;
import bl.mSPMHeaderBL;
import bl.tUserLoginBL;
import jim.h.common.android.lib.zxing.config.ZXingLibConfig;
import jim.h.common.android.lib.zxing.integrator.IntentIntegrator;
import jim.h.common.android.lib.zxing.integrator.IntentResult;
import library.common.mSPMDetailData;
import library.common.mSPMHeaderData;
import library.common.tUserLoginData;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
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

    MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnOutstandingTask = (Button) findViewById(R.id.btn_outstanding);
        btnScan = (Button) findViewById(R.id.btn_scan);

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

                    tUserLoginData _tUserLoginData = new tUserLoginData();
                    _tUserLoginData = new tUserLoginBL().getUserActive();
                    new SignalRService().getDataSPM(result, _tUserLoginData.getIntUserId());
                    new clsMainActivity().showCustomToast(getApplicationContext(),result,true);
//                    mSPMHeaderDA _mSPMHeaderDA = new mSPMHeaderDA(db);
//                    mSPMDetailDA _mSPMDetailDA = new mSPMDetailDA(db);
//                    int sumdata_mSPMHeaderDA = _mSPMHeaderDA.getContactsCount(db);
//		            if (sumdata_mSPMHeaderDA == 0) {
//			        _mSPMHeaderDA.InsertDefaultSPMHeader(db);
//                    _mSPMDetailDA.InsertDefaultmSPMDetail(db);
//                    startActivity(new Intent(Home.this, OutstandingTask.class));
//                    }
                }
//            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Intent intent = new Intent();
//        intent.setClass(mContext, SignalRService.class);
//        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

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
    protected void onStart() {
        //Register BroadcastReceiver
        //to receive event from our service
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SignalRService.BROADCAST_ERROR);
        intentFilter.addAction(SignalRService.BROADCAST_GETDATASPM);
        registerReceiver(myReceiver, intentFilter);

//        Intent intent = new Intent();
//        intent.setClass(mContext, SignalRService.class);
//        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        super.onStart();
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

    @Override
    protected void onDestroy() {
        Intent service = new Intent(Home.this, SignalRService.class);
        startService(service);
        super.onDestroy();
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

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            JSONObject jsonObject = null;
            JSONArray jsonArray = null;

            String broadcastName = intent.getAction().substring(0);

            if (broadcastName.equalsIgnoreCase("BROADCAST_GETDATASPM")) {
                String datapassed = intent.getStringExtra("DATA_PASSED_GETDATASPM");

                try {
//                    jsonObject = new JSONObject(datapassed);
//
//                    jsonArray = jsonObject.getJSONArray("A");
//
//                    String jsonString = (String) jsonArray.get(0);

                    jsonObject = new JSONObject(datapassed);

                    String boolValid = jsonObject.get("boolValid").toString();
                    String strMessage = jsonObject.get("strMessage").toString();
                    String strMethodName = jsonObject.get("strMethodName").toString();

                    if (boolValid.equalsIgnoreCase("true")) {
                        JSONObject jsonObjectHeader = jsonObject.getJSONObject("listOfmSPMHeader");

                        String status = jsonObjectHeader.get("bitStatus").toString();
                        String sync = jsonObjectHeader.get("bitSync").toString();

                        mSPMHeaderData _mSPMHeaderData = new mSPMHeaderData();

                        if(status.equals("1")&&sync.equals("0")){
                            _mSPMHeaderData.setIntSPMId(jsonObjectHeader.get("txtUserId").toString());
                            _mSPMHeaderData.setTxtNoSPM(jsonObjectHeader.get("txtNoSPM").toString());
                            _mSPMHeaderData.setTxtBranchCode(jsonObjectHeader.get("txtBranchCode").toString());
                            _mSPMHeaderData.setTxtSalesOrder(jsonObjectHeader.get("txtSalesOrder").toString());
                            _mSPMHeaderData.setIntUserId(jsonObjectHeader.get("intUserId").toString());
                            _mSPMHeaderData.setBitStatus(jsonObjectHeader.get("bitStatus").toString());
                            _mSPMHeaderData.setBitSync(jsonObjectHeader.get("bitSync").toString());

                            new mSPMHeaderBL().saveData(_mSPMHeaderData);

                            JSONArray jsonArrayInner = jsonObject.getJSONArray("listOfmSPMDetail");

                            List<mSPMDetailData> _mSPMDetailData = new ArrayList<>();

                            for (int i = 0; i < jsonArrayInner.length(); i++) {
                                String jsonInner = jsonArrayInner.get(i).toString();
                                jsonObject = new JSONObject(jsonInner);

                                mSPMDetailData data = new mSPMDetailData();

                                data.setIntSPMDetailId(jsonObject.get("intSPMDetailId").toString());
                                data.setTxtNoSPM(jsonObject.get("txtNoSPM").toString());
                                data.setTxtLocator(jsonObject.get("txtLocator").toString());
                                data.setTxtItemCode(jsonObject.get("txtItemCode").toString());
                                data.setTxtItemName(jsonObject.get("txtItemName").toString());
                                data.setIntQty(jsonObject.get("intQty").toString());
                                data.setBitStatus(jsonObject.get("bitStatus").toString());
                                data.setBitSync(jsonObject.get("bitSync").toString());

                                _mSPMDetailData.add(data);
                            }
//                            new mSPMDetailBL().saveData(_mSPMDetailData);
                        }
                    } else {
                        Toast.makeText(Home.this, String.valueOf(datapassed), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Home.this, String.valueOf(datapassed), Toast.LENGTH_SHORT).show();
                }

            }  else {
                String datapassed = intent.getStringExtra("DATA_PASSED_ERROR");

                try {
//                    jsonObject = new JSONObject(datapassed);
//
//                    jsonArray = jsonObject.getJSONArray("A");
//
//                    String jsonString = (String) jsonArray.get(0);

                    jsonObject = new JSONObject(datapassed);

//                    boolValid = jsonObject.get("boolValid").toString();
//                    strMessage = jsonObject.get("strMessage").toString();
//                    strMethodName = jsonObject.get("strMethodName").toString();
//
//                    if (boolValid.equalsIgnoreCase("true")) {
//                        JSONArray jsonArrayInner = jsonObject.getJSONArray("listOfmUserRole");
//
//                        arrrole = new ArrayList<>();
//
//                        for (int i = 0; i < jsonArrayInner.length(); i++) {
//                            String jsonInner = jsonArrayInner.get(i).toString();
//                            jsonObject = new JSONObject(jsonInner);
//
//                            intRoleId = jsonObject.get("intRoleId").toString();
//                            txtRoleName = jsonObject.get("txtRoleName").toString();
//                            arrrole.add(txtRoleName);
//
//                            HMRole.put(txtRoleName, intRoleId);
//                        }
//
//                    } else {
//                        Toast.makeText(Home.this, String.valueOf(datapassed), Toast.LENGTH_SHORT).show();
//                        spnRole.setAdapter(new Login.MyAdapter(Home.this, R.layout.custom_spinner, arrrole));
//                        etTxtEmail.requestFocus();
//                    }
//
//                    spnRole.setAdapter(new Login.MyAdapter(Home.this, R.layout.custom_spinner, arrrole));
//                    spnRole.setEnabled(true);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Home.this, String.valueOf(datapassed), Toast.LENGTH_SHORT).show();
                }

            }


        }
    }
}
