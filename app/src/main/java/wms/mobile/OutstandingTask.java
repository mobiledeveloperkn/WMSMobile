package wms.mobile;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.owater.library.CircleTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import addon.ConnectivityReceiver;
import addon.MyApplication;
import bl.SignalRBL;
import bl.clsMainBL;
import bl.mSPMDetailBL;
import bl.mSPMHeaderBL;
import bl.mSystemConfigBL;
import bl.tTimerLogBL;
import bl.tUserLoginBL;
import library.common.mSPMDetailData;
import library.common.mSPMHeaderData;
import library.common.tTimerLogData;
import library.common.tUserLoginData;
import library.dal.mSPMDetailDA;
import library.dal.tTimerLogDA;
import service.WMSMobileService;

/**
 * Created by ASUS ZE on 16/11/2016.
 */

public class OutstandingTask extends AppCompatActivity implements View.OnClickListener, WMSMobileService.mHubConnectionSevice, ConnectivityReceiver.ConnectivityReceiverListener {

    Button btnTaskView, btnRefresh, btnComplete, btnBreak;
    TextView tvNoSPM, tvShopTo, tvSO;
    private mSPMHeaderData _mSPMHeaderData;

    private CoordinatorLayout coordinatorLayout;
    private ConnectivityManager conMan;

    private List<mSPMDetailData> mSPMDetailDataListPending;
    private List<mSPMDetailData> mSPMDetailDataListSuccess;
    private List<mSPMDetailData> mSPMDetailDataListCancel;

    CircleTextView tvTotalPending, tvTotalConfirm, tvTotalCancel;

    private String versionName = "";
    private tUserLoginData dataLogin;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_outstanding_task);
        btnTaskView = findViewById(R.id.btn_taskview);
        btnRefresh = findViewById(R.id.btn_refresh);
        btnComplete = findViewById(R.id.btn_complete);
        btnBreak = findViewById(R.id.btn_break);
        tvNoSPM = findViewById(R.id.tv_noSPM);
        tvShopTo = findViewById(R.id.tv_shipTo);
        tvSO = findViewById(R.id.tv_SO);
        tvTotalPending = findViewById(R.id.tvTotalPending);
        tvTotalConfirm = findViewById(R.id.tvTotalConfirm);
        tvTotalCancel = findViewById(R.id.tvTotalCancel);
        coordinatorLayout = findViewById(R.id.coorOutstandingTask);
        conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        PackageInfo pInfo = new clsMainActivity().getPinfo(this);
        if (pInfo != null) {
            versionName = pInfo.versionName;
        }
        dataLogin = new tUserLoginData();
        dataLogin = new tUserLoginBL().getUserActive();
        //bikin progres dialognya
        progressDialog = new ProgressDialog(OutstandingTask.this);
        progressDialog.setMessage("Loading... Please Wait");
        progressDialog.setIndeterminate(false); //ukur berapa persen, false maka not do
        progressDialog.setCancelable(false);
        new mSystemConfigBL().UpdateFilterPicking("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(new ConnectivityReceiver(),
                    new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    @Override
    protected void onStart() {
        setHubConnection(this);
        super.onStart();
    }

    @Override
    protected void onResume() {
        MyApplication.getInstance().setConnectivityListener(this);
        _mSPMHeaderData = new mSPMHeaderData();
        _mSPMHeaderData = new mSPMHeaderBL().GetDataByStatus();

        tvNoSPM.setText(String.format("NO. STAR : %s", _mSPMHeaderData.getTxtNoSPM()));

        btnTaskView.setOnClickListener(this);
        btnComplete.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        btnBreak.setOnClickListener(this);

        if (_mSPMHeaderData != null) {
            if (_mSPMHeaderData.getBitStart().equals("0")) {
                initMethodMappingButton();
            } else {
                showPopupStartButton();
            }
        }

        setCircleReport();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        super.onResume();
    }

    private void setCircleReport() {
        mSPMDetailDataListPending = new ArrayList<>();
        mSPMDetailDataListPending = new mSPMDetailBL().getAllDataTaskPending(_mSPMHeaderData.getTxtNoSPM());
        mSPMDetailDataListSuccess = new ArrayList<>();
        mSPMDetailDataListSuccess = new mSPMDetailBL().getAllDataTaskConfirm(_mSPMHeaderData.getTxtNoSPM());
        mSPMDetailDataListCancel = new ArrayList<>();
        mSPMDetailDataListCancel = new mSPMDetailBL().getAllDataTaskCancel(_mSPMHeaderData.getTxtNoSPM());

        tvTotalPending.setText(mSPMDetailDataListPending != null ? String.valueOf(mSPMDetailDataListPending.size()) : "0");
        tvTotalConfirm.setText(mSPMDetailDataListSuccess != null ? String.valueOf(mSPMDetailDataListSuccess.size()) : "0");
        tvTotalCancel.setText(mSPMDetailDataListCancel != null ? String.valueOf(mSPMDetailDataListCancel.size()) : "0");

        tvTotalPending.setOnClickListener(view -> {
            if (_mSPMHeaderData.getBitSync().equals("0")) {
                Intent intent = new Intent(OutstandingTask.this, TabsTaskHeader.class);
                intent.putExtra("txtNoSPM", _mSPMHeaderData.getTxtNoSPM());
                intent.putExtra("tab", "0");
                startActivity(intent);
            }
        });
        tvTotalConfirm.setOnClickListener(view -> {
            if (_mSPMHeaderData.getBitSync().equals("0")) {
                Intent intent = new Intent(OutstandingTask.this, TabsTaskHeader.class);
                intent.putExtra("txtNoSPM", _mSPMHeaderData.getTxtNoSPM());
                intent.putExtra("tab", "1");
                startActivity(intent);
            }
        });
        tvTotalCancel.setOnClickListener(view -> {
            if (_mSPMHeaderData.getBitSync().equals("0")) {
                Intent intent = new Intent(OutstandingTask.this, TabsTaskHeader.class);
                intent.putExtra("txtNoSPM", _mSPMHeaderData.getTxtNoSPM());
                intent.putExtra("tab", "2");
                startActivity(intent);
            }
        });
    }

    private void initMethodMappingButton() {
        mSPMDetailDataListPending = new ArrayList<>();
        mSPMDetailDataListPending = new mSPMDetailBL().getAllDataTaskPending(_mSPMHeaderData.getTxtNoSPM());
        mSPMDetailDataListSuccess = new ArrayList<>();
        mSPMDetailDataListSuccess = new mSPMDetailBL().getAllDataTaskConfirm(_mSPMHeaderData.getTxtNoSPM());
        mSPMDetailDataListCancel = new ArrayList<>();
        mSPMDetailDataListCancel = new mSPMDetailBL().getAllDataTaskCancel(_mSPMHeaderData.getTxtNoSPM());
        _mSPMHeaderData = new mSPMHeaderBL().GetDataByStatus();

        if (_mSPMHeaderData.getTxtNoSPM() == null) {
            _mSPMHeaderData = new mSPMHeaderBL().getAllDataPushData();
            if (_mSPMHeaderData.getTxtNoSPM() == null) {
                btnRefresh.setEnabled(false);
                btnTaskView.setEnabled(false);
                btnComplete.setEnabled(false);
                btnTaskView.setBackgroundResource(R.drawable.btn_innermenu_gray);
                btnComplete.setBackgroundResource(R.drawable.btn_innermenu_gray);
                btnRefresh.setBackgroundResource(R.drawable.btn_innermenu_gray);
            } else {

                if (mSPMDetailDataListPending.size() > 0) {
                    if (mSPMDetailDataListSuccess.size() > 0 || mSPMDetailDataListCancel.size() > 0) {
                        btnRefresh.setEnabled(false);
                        btnTaskView.setEnabled(true);
                        btnComplete.setEnabled(false);
                        btnComplete.setBackgroundResource(R.drawable.btn_innermenu_gray);
                        btnRefresh.setBackgroundResource(R.drawable.btn_innermenu_gray);
                    } else {
                        btnRefresh.setEnabled(true);
                        btnTaskView.setEnabled(true);
                        btnComplete.setEnabled(false);
                        btnComplete.setBackgroundResource(R.drawable.btn_innermenu_gray);
                    }
                } else {
                    mSPMDetailDataListPending.size();
                    if (_mSPMHeaderData.getBitStatus().equals("0")) {
                        btnRefresh.setEnabled(false);
                        btnTaskView.setEnabled(true);
                        btnComplete.setEnabled(true);
                        btnComplete.setBackgroundResource(R.drawable.btn_innermenu);
                        btnRefresh.setBackgroundResource(R.drawable.btn_innermenu_gray);
                    } else {
                        btnRefresh.setEnabled(false);
                        btnTaskView.setEnabled(true);
                        btnComplete.setEnabled(false);
                        btnComplete.setBackgroundResource(R.drawable.btn_innermenu_gray);
                        btnRefresh.setBackgroundResource(R.drawable.btn_innermenu_gray);
                    }
                }
            }
        } else {
            if (mSPMDetailDataListPending.size() > 0) {
                if (mSPMDetailDataListSuccess.size() > 0 || mSPMDetailDataListCancel.size() > 0) {
                    btnRefresh.setEnabled(true);
                    btnTaskView.setEnabled(true);
                    btnComplete.setEnabled(false);
                    btnComplete.setBackgroundResource(R.drawable.btn_innermenu_gray);
                    btnRefresh.setBackgroundResource(R.drawable.btn_innermenu);
                } else {
                    btnRefresh.setEnabled(true);
                    btnTaskView.setEnabled(true);
                    btnComplete.setEnabled(false);
                    btnComplete.setBackgroundResource(R.drawable.btn_innermenu_gray);
                }
            } else {
                mSPMDetailDataListPending.size();
                if (_mSPMHeaderData.getBitStatus().equals("0")) {
                    btnRefresh.setEnabled(false);
                    btnTaskView.setEnabled(true);
                    btnComplete.setEnabled(true);
                    btnComplete.setBackgroundResource(R.drawable.btn_innermenu);
                    btnRefresh.setBackgroundResource(R.drawable.btn_innermenu_gray);
                } else {
                    btnRefresh.setEnabled(false);
                    btnTaskView.setEnabled(true);
                    btnComplete.setEnabled(false);
                    btnComplete.setBackgroundResource(R.drawable.btn_innermenu_gray);
                    btnRefresh.setBackgroundResource(R.drawable.btn_innermenu_gray);
                }
            }
        }
    }

    private void showPopupStartButton() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams") final View promptView = layoutInflater.inflate(R.layout.activity_start, null);
        final Button btnStart = promptView.findViewById(R.id.btn_start);
        final Button btnBack = promptView.findViewById(R.id.btn_back);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder
                .setCancelable(false);

        final AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();

        btnStart.setOnClickListener(view -> {

            String dt;
            if (_mSPMHeaderData.getDtStart().length() == 0 || _mSPMHeaderData.getDtStart().equals("NULL")) {
                @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar cal = Calendar.getInstance();
                dt = dateFormat.format(cal.getTime());
            } else {
                dt = _mSPMHeaderData.getDtStart();
            }

            new mSPMHeaderBL().updateDataSPMStartById(_mSPMHeaderData.getIntSPMId(), dt);

            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            String dtExecute = dateFormat.format(cal.getTime());

            tTimerLogData dataTimerLog = new tTimerLogData();
            dataTimerLog.setBitActive("1");
            dataTimerLog.setDtExecute(dtExecute);
            dataTimerLog.setTxtStarNo(_mSPMHeaderData.getTxtNoSPM());
            dataTimerLog.setTxtTimerLogId(new clsMainActivity().GenerateGuid());
            dataTimerLog.setTxtTimerStatus("Open");
            dataTimerLog.setTxtTimerType("Start Awal");
            new tTimerLogBL().insertData(dataTimerLog);

//                new mSPMHeaderBL().updateStatusStart(_mSPMHeaderData.getIntSPMId(), "0");

            initMethodMappingButton();
            alertD.dismiss();
        });

        btnBack.setOnClickListener(view -> {
            finish();
            onBackPressed();
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_taskview:
                Intent intent = new Intent(OutstandingTask.this, TabsTaskHeader.class);
                intent.putExtra("txtNoSPM", _mSPMHeaderData.getTxtNoSPM());
                startActivity(intent);
//                finish();
                break;
            case R.id.btn_refresh:
                final AlertDialog.Builder alertDialogCancel = new AlertDialog.Builder(this);
                alertDialogCancel.setTitle("Confirm");
                alertDialogCancel.setMessage("Are you sure want to refresh STAR?");
                alertDialogCancel.setPositiveButton("OK", (dialog, which) -> refreshSPMHeader());
                alertDialogCancel.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());

                alertDialogCancel.show();
                break;
            case R.id.btn_complete:
                final AlertDialog.Builder alertDialogComplete = new AlertDialog.Builder(this);
                alertDialogComplete.setTitle("Confirm");
                alertDialogComplete.setMessage("Are you sure to Complete?");
                alertDialogComplete.setPositiveButton("OK", (dialog, which) -> confirmTaskHeader());
                alertDialogComplete.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());

                alertDialogComplete.show();
                break;
            case R.id.btn_break:
                final AlertDialog.Builder alertDialogBreak = new AlertDialog.Builder(this);
                alertDialogBreak.setTitle("Confirm");
                alertDialogBreak.setMessage("Are you sure to Break?");
                alertDialogBreak.setPositiveButton("OK", (dialog, which) -> {
                    @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Calendar cal = Calendar.getInstance();
                    String dt = dateFormat.format(cal.getTime());

                    tTimerLogData dataTimerLog = new tTimerLogData();
                    dataTimerLog.setBitActive("1");
                    dataTimerLog.setDtExecute(dt);
                    dataTimerLog.setTxtStarNo(_mSPMHeaderData.getTxtNoSPM());
                    dataTimerLog.setTxtTimerLogId(new clsMainActivity().GenerateGuid());
                    dataTimerLog.setTxtTimerStatus("Close");
                    dataTimerLog.setTxtTimerType("Break");
                    new tTimerLogBL().insertData(dataTimerLog);
                    new mSPMHeaderBL().updateStatusStart(_mSPMHeaderData.getIntSPMId(), "");

                    finish();
                    onBackPressed();
                });
                alertDialogBreak.setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());

                alertDialogBreak.show();
                break;
        }
    }

    private void refreshSPMHeader() {
        final String result = new mSPMHeaderBL().GetAllData().getTxtNoSPM();
        boolean status;

        if (result != null) {
            progressDialog.show();
            tUserLoginData _tUserLoginData = new tUserLoginBL().getUserActive();
            status = new WMSMobileService().refreshDataSTAR(result, _tUserLoginData.getIntUserId(), versionName);
            if (!status) {
                progressDialog.dismiss();
                new SignalRBL().buildingConnection();
                new clsMainActivity().showCustomToast(getApplicationContext(), "Please Check Your Connection...", false);
            }
        }
    }

    public void confirmTaskHeader() {
        progressDialog.show();
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String dt = dateFormat.format(cal.getTime());
        boolean status;
        String _intSPMId = _mSPMHeaderData.getIntSPMId();

        tTimerLogData dtTimerLogComplete = new tTimerLogData();
        dtTimerLogComplete.setDtExecute(dt);
        dtTimerLogComplete.setBitActive("1");
        dtTimerLogComplete.setTxtTimerType("Complete");
        dtTimerLogComplete.setTxtTimerStatus("Close");
        dtTimerLogComplete.setTxtStarNo(_mSPMHeaderData.getTxtNoSPM());
        dtTimerLogComplete.setTxtTimerLogId(new clsMainBL().GenerateGuid());

        new tTimerLogBL().insertData(dtTimerLogComplete);

        List<tTimerLogData> ltTimerLogData;
        ltTimerLogData = new tTimerLogBL().getAllData();

        JSONObject resJson = new JSONObject();
        tTimerLogData _tTimerLogData = new tTimerLogData();
        Collection<JSONObject> itemsListJquey = new ArrayList<>();
        for (tTimerLogData data : ltTimerLogData) {
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

        status = new WMSMobileService().confirmSPMHeader(_intSPMId, versionName, dataLogin.getIntUserId(), resJson.toString());
        if (status) {
            new SignalRBL().buildingConnection();
            new clsMainActivity().showCustomToast(getApplicationContext(), "Success", true);

            new mSPMHeaderBL().updateDataValueByIdOffline(_intSPMId);
//                    adapterSuccess.notifyDataSetChanged();
//                    adapterProgress.notifyDataSetChanged();
//                    new TaskOnProgressFragment().fetchData(OutstandingTask.this);
//                    new TaskSuccessFragment().fetchData(OutstandingTask.this);
//            btnTaskView.setEnabled(false);
//            btnComplete.setEnabled(false);
//            btnTaskView.setBackgroundResource(R.drawable.btn_innermenu_gray);
//            btnComplete.setBackgroundResource(R.drawable.btn_innermenu_gray);

            new mSPMHeaderBL().updateDtEndById(_mSPMHeaderData.getIntSPMId(), dt);

            SQLiteDatabase db = new clsMainBL().getDb();
//            new mSPMHeaderDA(db).DeleteAllDataMConfig(db);
//            new mSPMDetailDA(db).DeleteAllDataMConfig(db);
            new tTimerLogDA(db).DropTable(db);
            progressDialog.dismiss();
//            finish();
//            onBackPressed();
//            Intent myIntent = new Intent(OutstandingTask.this, Home.class);
//            startActivity(myIntent);

        } else {
            new clsMainActivity().showCustomToast(getApplicationContext(), "Error Connection", false);
            progressDialog.dismiss();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        new clsMainActivity().checkConnection(coordinatorLayout, conMan);
    }

    @Override
    public void onReceiveMessageHub(final JSONObject jsonObject) {
        OutstandingTask.this.runOnUiThread(() -> {
            String strMethodName;

            try {
                strMethodName = jsonObject.get("strMethodName").toString();

                if (strMethodName.equalsIgnoreCase("ConfirmSPMHeader")) {
                    initMethodConfirmSPMHeader(jsonObject);
                } else if (strMethodName.equalsIgnoreCase("RefreshDataSTAR")) {
                    initMethodSPM(jsonObject);
                } else if (strMethodName.equalsIgnoreCase("pushDataOffline") || strMethodName.equalsIgnoreCase("ConfirmSPMDetail") || strMethodName.equalsIgnoreCase("cancelSPMDetail") || strMethodName.equalsIgnoreCase("revertCancelSPMDetail")) {
                    setCircleReport();
                } else if (strMethodName.equalsIgnoreCase("getLatestSTAR")){
                    setCircleReport();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void initMethodSPM(JSONObject jsonObject) {
        String strMessage, boolValid;

        try {
            boolValid = jsonObject.get("boolValid").toString();
            strMessage = jsonObject.get("strMessage").toString();

            if (boolValid.equalsIgnoreCase("true")) {
                JSONObject jsonObjectHeader = jsonObject.getJSONObject("listOfmSPMHeader");
                String status = jsonObjectHeader.get("STATUS").toString();
                String sync = jsonObjectHeader.get("SYNC").toString();

                if (status.equals("1") && sync.equals("0")) {

                    new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, true);

                    JSONArray jsonArrayInner = jsonObject.getJSONArray("listOfmSPMDetail");

                    SQLiteDatabase db = new clsMainBL().getDb();
                    new mSPMDetailDA(db).DeleteAllDataMConfig(db);

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
                        data.setTxtLotNumber(jsonObject.get("LOT_NUM").toString());
                        data.setTxtUOM(jsonObject.get("UOM").toString());
                        data.setIntUserId(dataLogin.getIntUserId());

                        new mSPMDetailBL().insert(data);
                    }

                    setCircleReport();

                } else if (status.equals("1") && sync.equals("1")) {
                    new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, false);
                }
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, false);
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }
    }

    private void initMethodConfirmSPMHeader(JSONObject jsonObject) {

        try {
            String boolValid = jsonObject.get("boolValid").toString();
            String strMessage = jsonObject.get("strMessage").toString();

            if (boolValid.equalsIgnoreCase("true")) {

                JSONObject jsonObjectHeader = jsonObject.getJSONObject("listOfmSPMHeader");

                String status = jsonObjectHeader.get("STATUS").toString();
                String sync = jsonObjectHeader.get("SYNC").toString();
                String _intSPMHeaderId = jsonObjectHeader.get("SPM_HEADER_ID").toString();

                if (status.equals("1") && sync.equals("1")) {
                    new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, true);

                    new mSPMHeaderBL().updateDataValueById(_intSPMHeaderId);
//                    adapterSuccess.notifyDataSetChanged();
//                    adapterProgress.notifyDataSetChanged();
//                    new TaskOnProgressFragment().fetchData(OutstandingTask.this);
//                    new TaskSuccessFragment().fetchData(OutstandingTask.this);
                    btnTaskView.setEnabled(false);
                    btnComplete.setEnabled(false);
                    btnTaskView.setBackgroundResource(R.drawable.btn_innermenu_gray);
                    btnComplete.setBackgroundResource(R.drawable.btn_innermenu_gray);
//                    SQLiteDatabase db = new clsMainBL().getDb();
//                    new mSPMHeaderDA(db).DropTable(db);
//                    new mSPMDetailDA(db).DropTable(db);
                    finish();
                    onBackPressed();
//                    Intent myIntent = new Intent(OutstandingTask.this, Home.class);
//                    startActivity(myIntent);
                }
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
//                Toast.makeText(OutstandingTask.this, String.valueOf(strMessage), Toast.LENGTH_SHORT).show();
                new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, false);
                new tTimerLogBL().deleteDataCompleteWhileError();

//                JSONObject jsonObjectHeader = jsonObject.getJSONObject("listOfmSPMHeader");
//
//                String status = jsonObjectHeader.get("STATUS").toString();
//                String sync = jsonObjectHeader.get("SYNC").toString();
//
//                _mSPMHeaderData = new mSPMHeaderData();
//
//                    _mSPMHeaderData.setIntSPMId(jsonObjectHeader.get("SPM_HEADER_ID").toString());
//                    _mSPMHeaderData.setTxtNoSPM(jsonObjectHeader.get("SPM_NO").toString());
//                    _mSPMHeaderData.setTxtBranchCode(jsonObjectHeader.get("BRANCH_CODE").toString());
//                    _mSPMHeaderData.setTxtSalesOrder(jsonObjectHeader.get("SALES_ORDER").toString());
//                    _mSPMHeaderData.setIntUserId(jsonObjectHeader.get("USER_ID").toString());
//                    _mSPMHeaderData.setBitStatus("0");
//                    _mSPMHeaderData.setBitSync("0");
//                    _mSPMHeaderData.setBitStart("1");
//                    _mSPMHeaderData.setIntUserId(dataLogin.getIntUserId());
//
//                    new mSPMHeaderBL().saveData(_mSPMHeaderData);
//
//                    JSONArray jsonArrayInner = jsonObject.getJSONArray("listOfmSPMDetail");
//
//                    List<mSPMDetailData> _mSPMDetailData = new ArrayList<>();
//
//                    for (int i = 0; i < jsonArrayInner.length(); i++) {
//
//                        jsonObject = jsonArrayInner.getJSONObject(i);
//
//                        mSPMDetailData data = new mSPMDetailData();
//
//                        data.setIntSPMDetailId(jsonObject.get("SPM_DETAIL_ID").toString());
//                        data.setTxtNoSPM(jsonObject.get("SPM_NO").toString());
//                        data.setTxtLocator(jsonObject.get("LOCATOR").toString());
//                        data.setTxtItemCode(jsonObject.get("ITEM_CODE").toString());
//                        data.setTxtItemName(jsonObject.get("ITEM_NAME").toString());
//                        data.setIntQty(jsonObject.get("QUANTITY").toString());
//                        data.setBitStatus(jsonObject.get("STATUS").toString());
//                        data.setBitSync(jsonObject.get("SYNC").toString());
//                        data.setTxtUOM(jsonObject.get("UOM").toString());
//                        data.setTxtLotNumber(jsonObject.get("LOT_NUM").toString());
//                        data.setIntUserId(dataLogin.getIntUserId());
//                        new mSPMDetailBL().insert(data);
//                    }
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
//                    Toast.makeText(OutstandingTask.this, String.valueOf(datapassed), Toast.LENGTH_SHORT).show();
        }
    }

    public void setHubConnection(WMSMobileService.mHubConnectionSevice hubConnection) {
        WMSMobileService.mHubConnectionSevice = hubConnection;
    }

}
