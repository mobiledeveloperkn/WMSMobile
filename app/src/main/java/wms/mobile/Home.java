package wms.mobile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import addon.ConnectivityReceiver;
import addon.CropDisplayPicture;
import addon.MyApplication;
import bl.SignalRBL;
import bl.clsMainBL;
import bl.mSPMDetailBL;
import bl.mSPMHeaderBL;
import bl.tDisplayPictureBL;
import bl.tTimerLogBL;
import bl.tUserLoginBL;
import de.hdodenhof.circleimageview.CircleImageView;
import jim.h.common.android.lib.zxing.config.ZXingLibConfig;
import jim.h.common.android.lib.zxing.integrator.IntentIntegrator;
import jim.h.common.android.lib.zxing.integrator.IntentResult;
import library.common.clsHelper;
import library.common.mSPMDetailData;
import library.common.mSPMHeaderData;
import library.common.tDisplayPictureData;
import library.common.tTimerLogData;
import library.common.tUserLoginData;
import service.WMSMobileService;

public class Home extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener, WMSMobileService.mHubConnectionSevice {

    Button btnOutstandingTask, btnScan, btnLogout;
    TextView tv_userName, tv_Branch, tv_Email;
    private ZXingLibConfig zxingLibConfig;
    mSPMHeaderData _mSPMHeaderData;
    private boolean validReceiver = false;
    private CoordinatorLayout coordinatorLayout;
    ConnectivityManager conMan;
    private tUserLoginData dataLogin;

    private PackageInfo pInfo = null;
    private String versionName = "";

    private ProgressDialog progressDialog;
    private long time = 15000;

    public static updateSnackbar updateSnackbar;

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Confirm");
        alertDialog.setMessage("Are you sure to exit?");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_home);

        btnOutstandingTask = (Button) findViewById(R.id.btn_outstanding);
        btnScan = (Button) findViewById(R.id.btn_scan);
        btnLogout = (Button) findViewById(R.id.btn_logout);
        tv_userName = (TextView) findViewById(R.id.tvUsername);
        tv_Branch = (TextView) findViewById(R.id.tvBranch);
        tv_Email = (TextView) findViewById(R.id.tvEmail);
        CircleImageView ivProfile = (CircleImageView) findViewById(R.id.profile_image);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayoutHome);

        conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        tDisplayPictureData tDisplayPictureData = new tDisplayPictureBL().getData();

        btnOutstandingTask.setOnClickListener(this);
        btnScan.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        pInfo = new clsMainActivity().getPinfo(this);

        if(pInfo!=null){
            versionName = pInfo.versionName;
        }

//        SQLiteDatabase db = new clsMainBL().getDb();
//        new mSPMHeaderDA(db).InsertDefaultSPMHeader(db);
//        new mSPMDetailDA(db).InsertDefaultmSPMDetail(db);

        _mSPMHeaderData = new mSPMHeaderData();
        _mSPMHeaderData = new mSPMHeaderBL().GetAllData();
        dataLogin = new tUserLoginBL().getUserActive();

        tv_userName.setText(dataLogin.getTxtUserName());
        tv_Branch.setText(dataLogin.getTxtRoleName());

        String dateTime = dataLogin.getDtLastLogin();
        String dtTime = new clsMainActivity().splitDateTime(dateTime);
        tv_Email.setText("Last Login : " + dtTime);

        if (tDisplayPictureData.get_image() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(tDisplayPictureData.get_image(), 0, tDisplayPictureData.get_image().length);
            ivProfile.setImageBitmap(bitmap);
        } else {
            ivProfile.setImageBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.profile));
        }
        ivProfile.setOnClickListener(this);
        new clsMainActivity().checkConnection(coordinatorLayout, conMan);

        //bikin progres dialognya
        progressDialog = new ProgressDialog(Home.this);
        progressDialog.setMessage("Loading... Please Wait");
        progressDialog.setIndeterminate(false); //ukur berapa persen, false maka not do
        progressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_outstanding:
//                finish();
                startActivity(new Intent(Home.this, OutstandingTask.class));
                break;
            case R.id.btn_scan:
                IntentIntegrator.initiateScan(this, zxingLibConfig);
                break;
            case R.id.btn_logout:

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Confirm");
                alertDialog.setMessage("Are you sure to Logout?");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestLogout();
                    }
                });
                alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

                alertDialog.show();

                break;
            case R.id.profile_image:
                pickImage();
                break;

        }
    }

    private void requestLogout() {
        progressDialog.show();
        new clsMainActivity().timerDelayRemoveDialog(time, progressDialog);
        dataLogin = new tUserLoginData();
        dataLogin = new tUserLoginBL().getUserActive();
        boolean status = false;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String dt = dateFormat.format(cal.getTime());

        List<tTimerLogData> ltTimerLogData = new ArrayList<>();

        tTimerLogData dtTimerLogLogout = new tTimerLogData();

        dtTimerLogLogout.setDtExecute(dt);
        dtTimerLogLogout.setBitActive("1");
        dtTimerLogLogout.setTxtTimerType("Logout");
        dtTimerLogLogout.setTxtTimerStatus("Close");
        dtTimerLogLogout.setTxtStarNo(_mSPMHeaderData.getTxtNoSPM());
        dtTimerLogLogout.setTxtTimerLogId(new clsMainBL().GenerateGuid());

        new tTimerLogBL().insertData(dtTimerLogLogout);

        ltTimerLogData = new tTimerLogBL().getAllData();

        JSONObject resJson = new JSONObject();
        tTimerLogData _tTimerLogData = new tTimerLogData();
        Collection<JSONObject> itemsListJquey = new ArrayList<>();
        for (tTimerLogData data : ltTimerLogData){
            JSONObject item1 = new JSONObject();
            try {
                item1.put(_tTimerLogData.Property_txtTimerLogId, String.valueOf(data.getTxtTimerLogId()));
                item1.put(_tTimerLogData.Property_bitActive, String.valueOf(data.getBitActive()));
                item1.put(_tTimerLogData.Property_dtExecute, String.valueOf(data.getDtExecute()));
                item1.put(_tTimerLogData.Property_txtStarNo, String.valueOf(data.getTxtStarNo()));
                item1.put(_tTimerLogData.Property_txtTimerStatus, String.valueOf(data.getTxtTimerStatus()));
                item1.put(_tTimerLogData.Property_txtTimerType, String.valueOf(data.getTxtTimerType()));
                itemsListJquey.add(item1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            resJson.put("ListOfTimerLog", new JSONArray(itemsListJquey));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        status = new WMSMobileService().logout(dataLogin.getTxtDataId(), versionName, dataLogin.getIntUserId(), resJson.toString());

        if (!status) {
            progressDialog.dismiss();
            boolean report = new SignalRBL().buildingConnection();
            new clsMainActivity().showCustomToast(getApplicationContext(), "Please Check Your Connection...", false);
        }
    }

    public void pickImage() {
        CropImage.startPickImageActivity(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SQLiteDatabase db = new clsMainBL().getDb();
        boolean status = false;

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                Intent intent = new Intent(this, CropDisplayPicture.class);
                String strName = imageUri.toString();
                intent.putExtra("uriPicture", strName);
                startActivity(intent);
                finish();
            }
        } else if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scanResult.getContents() == null && scanResult.getFormatName() == null) {
                return;
            }
            final String result = scanResult.getContents();
            if (result != null) {
                progressDialog.show();
                new clsMainActivity().timerDelayRemoveDialog(time, progressDialog);
                tUserLoginData _tUserLoginData = new tUserLoginData();
                _tUserLoginData = new tUserLoginBL().getUserActive();
                status = new WMSMobileService().getDataSPM(result, _tUserLoginData.getIntUserId(), versionName);
                validReceiver = false;
                if (!status) {
                    progressDialog.dismiss();
                    boolean report = new SignalRBL().buildingConnection();
                    new clsMainActivity().showCustomToast(getApplicationContext(), "Please Check Your Connection...", false);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        MyApplication.getInstance().setConnectivityListener(this);
//        new clsMainActivity().checkConnection(coordinatorLayout, conMan);

        initMethodMappingButton();
        super.onResume();
    }

    private void initMethodMappingButton() {
        _mSPMHeaderData = new mSPMHeaderData();
        _mSPMHeaderData = new mSPMHeaderBL().GetDataByStatus();

        if (_mSPMHeaderData.getTxtNoSPM()==null) {
            _mSPMHeaderData = new mSPMHeaderBL().getAllDataPushData();
            if(_mSPMHeaderData.getTxtNoSPM()==null){
                btnOutstandingTask.setEnabled(false);
                btnScan.setEnabled(true);
                btnScan.setBackgroundResource(R.drawable.bg_inner_normal);
                btnOutstandingTask.setBackgroundResource(R.drawable.bg_inner_normal_gray);
            } else {
                btnOutstandingTask.setEnabled(true);
                btnScan.setEnabled(false);
                btnScan.setBackgroundResource(R.drawable.bg_inner_normal_gray);
                btnOutstandingTask.setBackgroundResource(R.drawable.bg_inner_normal);
            }
        } else {
            btnOutstandingTask.setEnabled(true);
            btnScan.setEnabled(false);
            btnScan.setBackgroundResource(R.drawable.bg_inner_normal_gray);
            btnOutstandingTask.setBackgroundResource(R.drawable.bg_inner_normal);
        }
    }

    @Override
    protected void onStart() {
        setHubConnection(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
//        Intent broadcastIntent = new Intent("service.MyRebootReceiver");
//        sendBroadcast(broadcastIntent);
//        unSetHubConnection(this);
//        if(!new clsMainActivity().isMyServiceRunning(getApplicationContext())){
//            Intent broadcastIntent = new Intent("service.MyRebootReceiver");
//            sendBroadcast(broadcastIntent);
//        } else {
//            boolean valid = new WMSMobileService().checkinConnHub();
//            if(!valid){
//                new WMSMobileService().startSignalR();
//            }
//        }
//        MyApplication.getInstance().unsetConnectivityListener(this);
        super.onDestroy();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        new clsMainActivity().checkConnection(coordinatorLayout, conMan);
    }

    @Override
    public void onReceiveMessageHub(final JSONObject jsonObject) {
        Home.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONArray jsonArray = null;
                String strMethodName, strMessage, boolValid, intRoleId, txtRoleName, dtInsert, dtUpdated;

                try {
                    boolValid = jsonObject.get("boolValid").toString();
                    strMessage = jsonObject.get("strMessage").toString();
                    strMethodName = jsonObject.get("strMethodName").toString();

                    if (strMethodName.equalsIgnoreCase("GetDataSPM")) {
                        initMethodSPM(jsonObject);
                    } else if (strMethodName.equalsIgnoreCase("Logout")){
                        initMethodLogout(jsonObject);
                    } else if (strMethodName.equalsIgnoreCase("pushDataOffline")) {
//                        updateListView();
                        if(updateSnackbar != null){
                            WMSMobileService.updateSnackbar.onUpdateSnackBar(true);
                        }
                        updateFromPushDataOffline(jsonObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateFromPushDataOffline(JSONObject jsonObject) {
        String strMethodName, strMessage, boolValid, intRoleId, txtRoleName, dtInsert, dtUpdated;

        try {
            boolValid = jsonObject.get("boolValid").toString();
            strMessage = jsonObject.get("strMessage").toString();
            strMethodName = jsonObject.get("strMethodName").toString();

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

                    List<mSPMDetailData> _mSPMDetailData = new ArrayList<>();

                    for (int i = 0; i < jsonArraySPMDetail.length(); i++) {
                        String jsonInner = jsonArraySPMDetail.get(i).toString();
                        jsonObject = new JSONObject(jsonInner);
                        String statusDetail = jsonObject.get("STATUS").toString();
                        String syncDetail = jsonObject.get("SYNC").toString();
                        String id = jsonObject.get("SPM_DETAIL_ID").toString();

//                        mSPMDetailData data = new mSPMDetailData();

//                            data.setIntSPMDetailId(jsonObject.get("SPM_DETAIL_ID").toString());
//                            data.setTxtNoSPM(jsonObject.get("SPM_NO").toString());
//                            data.setTxtLocator(jsonObject.get("LOCATOR").toString());
//                            data.setTxtItemCode(jsonObject.get("ITEM_CODE").toString());
//                            data.setTxtItemName(jsonObject.get("ITEM_NAME").toString());
//                            data.setIntQty(jsonObject.get("QUANTITY").toString());
//                            data.setBitStatus(jsonObject.get("STATUS").toString());
//                            data.setBitSync(jsonObject.get("SYNC").toString());
//                            data.setBitSync(jsonObject.get("REASON").toString());

                        new mSPMDetailBL().saveFromPushData(id, statusDetail, syncDetail);
                    }
                }
                new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, true);
                initMethodMappingButton();
            } else {
                new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initMethodLogout(JSONObject jsonObject) {
        String strMethodName, strMessage, boolValid, intRoleId, txtRoleName, dtInsert, dtUpdated;

        try {
            boolValid = jsonObject.get("boolValid").toString();
            strMessage = jsonObject.get("strMessage").toString();
            strMethodName = jsonObject.get("strMethodName").toString();

            if (boolValid.equalsIgnoreCase("true")) {
                SQLiteDatabase db = new clsMainBL().getDb();
                new clsHelper().DeleteAllDB(db);
                finish();
                Intent myIntent = new Intent(Home.this, Login.class);
                startActivity(myIntent);
                finish();
            } else {
                new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, false);
            }
            progressDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }
    }

    private void initMethodSPM(JSONObject jsonObject) {
        String strMethodName, strMessage, boolValid, intRoleId, txtRoleName, dtInsert, dtUpdated;

        try {
            boolValid = jsonObject.get("boolValid").toString();
            strMessage = jsonObject.get("strMessage").toString();
            strMethodName = jsonObject.get("strMethodName").toString();

            if (boolValid.equalsIgnoreCase("true")) {
                JSONObject jsonObjectHeader = jsonObject.getJSONObject("listOfmSPMHeader");

                String status = jsonObjectHeader.get("STATUS").toString();
                String sync = jsonObjectHeader.get("SYNC").toString();

                _mSPMHeaderData = new mSPMHeaderData();

                if (status.equals("1") && sync.equals("0")) {

                    new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, true);

                    _mSPMHeaderData.setIntSPMId(jsonObjectHeader.get("SPM_HEADER_ID").toString());
                    _mSPMHeaderData.setTxtNoSPM(jsonObjectHeader.get("SPM_NO").toString());
                    _mSPMHeaderData.setTxtBranchCode(jsonObjectHeader.get("BRANCH_CODE").toString());
                    _mSPMHeaderData.setTxtSalesOrder(jsonObjectHeader.get("SALES_ORDER").toString());
                    _mSPMHeaderData.setIntUserId(jsonObjectHeader.get("USER_ID").toString());
                    _mSPMHeaderData.setBitStatus("0");
                    _mSPMHeaderData.setBitSync("0");
                    _mSPMHeaderData.setBitStart("1");
                    _mSPMHeaderData.setIntUserId(dataLogin.getIntUserId());

                    new mSPMHeaderBL().saveData(_mSPMHeaderData);

                    JSONArray jsonArrayInner = jsonObject.getJSONArray("listOfmSPMDetail");

                    List<mSPMDetailData> _mSPMDetailData = new ArrayList<>();

                    for (int i = 0; i < jsonArrayInner.length(); i++) {

                        jsonObject = jsonArrayInner.getJSONObject(i);

                        mSPMDetailData data = new mSPMDetailData();

                        data.setIntSPMDetailId(jsonObject.get("SPM_DETAIL_ID").toString());
                        data.setTxtNoSPM(jsonObject.get("SPM_NO").toString());
                        data.setTxtLocator(jsonObject.get("LOCATOR").toString());
                        data.setTxtItemCode(jsonObject.get("ITEM_CODE").toString());
                        data.setTxtItemName(jsonObject.get("ITEM_NAME").toString());
                        data.setIntQty(jsonObject.get("QUANTITY").toString());
                        data.setBitStatus(jsonObject.get("STATUS").toString());
                        data.setBitSync(jsonObject.get("SYNC").toString());
                        data.setIntUserId(dataLogin.getIntUserId());

                        new mSPMDetailBL().insert(data);
                    }
                    btnScan.setEnabled(false);
                    btnScan.setBackgroundResource(R.drawable.btn_innermenu_gray);
                    btnOutstandingTask.setEnabled(true);
                    btnOutstandingTask.setBackgroundResource(R.drawable.btn_innermenu);
                } else if (status.equals("1") && sync.equals("1")) {

                    new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, false);
                    btnScan.setEnabled(true);
                    btnScan.setBackgroundResource(R.drawable.btn_innermenu);
                    btnOutstandingTask.setEnabled(false);
                    btnOutstandingTask.setBackgroundResource(R.drawable.btn_innermenu_gray);
                }
            } else {
                new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, false);
            }
            progressDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }
    }

    public void setHubConnection(WMSMobileService.mHubConnectionSevice hubConnection) {
        WMSMobileService.mHubConnectionSevice = hubConnection;
    }
    public void unSetHubConnection(WMSMobileService.mHubConnectionSevice hubConnection) {
        WMSMobileService.mHubConnectionSevice = null;
    }
    public interface updateSnackbar {
        void onUpdateSnackBar(boolean info);
    }
}
