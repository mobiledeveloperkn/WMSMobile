package wms.mobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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

import addon.ConnectivityReceiver;
import addon.MyApplication;
import bl.SignalRBL;
import bl.clsMainBL;
import bl.mSPMDetailBL;
import bl.mSPMHeaderBL;
import bl.tTimerLogBL;
import bl.tUserLoginBL;
import library.common.mSPMDetailData;
import library.common.mSPMHeaderData;
import library.common.tTimerLogData;
import library.common.tUserLoginData;
import library.dal.mSPMDetailDA;
import library.dal.mSPMHeaderDA;
import library.dal.tTimerLogDA;
import service.WMSMobileService;

/**
 * Created by ASUS ZE on 16/11/2016.
 */

public class OutstandingTask extends AppCompatActivity implements View.OnClickListener, WMSMobileService.mHubConnectionSevice, ConnectivityReceiver.ConnectivityReceiverListener {

    private Toolbar toolbar;
    Button btnTaskView, btnRefresh, btnComplete, btnBreak;
    TextView tvNoSPM, tvShopTo, tvSO;
    private mSPMHeaderData _mSPMHeaderData;

    private CoordinatorLayout coordinatorLayout;
    private ConnectivityManager conMan;

    private List<mSPMDetailData> mSPMDetailDataListPending;
    private List<mSPMDetailData> mSPMDetailDataListSuccess;
    private List<mSPMDetailData> mSPMDetailDataListCancel;

    TableLayout tReport;

    CircleTextView tvTotalPending, tvTotalConfirm, tvTotalCancel;

    private PackageInfo pInfo = null;
    private String versionName = "";
    private tUserLoginData dataLogin;

    private ProgressDialog progressDialog;
    private long time = 15000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_outstanding_task);
        btnTaskView = (Button) findViewById(R.id.btn_taskview);
        btnRefresh = (Button) findViewById(R.id.btn_refresh);
        btnComplete = (Button) findViewById(R.id.btn_complete);
        btnBreak = (Button) findViewById(R.id.btn_break);
        tvNoSPM = (TextView) findViewById(R.id.tv_noSPM);
        tvShopTo = (TextView) findViewById(R.id.tv_shipTo);
        tvSO = (TextView) findViewById(R.id.tv_SO);
        tvTotalPending = (CircleTextView) findViewById(R.id.tvTotalPending);
        tvTotalConfirm = (CircleTextView) findViewById(R.id.tvTotalConfirm);
        tvTotalCancel = (CircleTextView) findViewById(R.id.tvTotalCancel);
//        tReport = (TableLayout) findViewById(R.id.tl_data_report);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coorOutstandingTask);
        conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        pInfo = new clsMainActivity().getPinfo(this);
        if (pInfo != null) {
            versionName = pInfo.versionName;
        }
        dataLogin = new tUserLoginBL().getUserActive();
        //bikin progres dialognya
        progressDialog = new ProgressDialog(OutstandingTask.this);
        progressDialog.setMessage("Loading... Please Wait");
        progressDialog.setIndeterminate(false); //ukur berapa persen, false maka not do
        progressDialog.setCancelable(false);
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

        tvNoSPM.setText("NO. STAR : " + _mSPMHeaderData.getTxtNoSPM());
//        tvShopTo.setText(_mSPMHeaderData.getTxtBranchCode());
//        tvSO.setText(_mSPMHeaderData.getTxtSalesOrder());

        btnTaskView.setOnClickListener(this);
        btnComplete.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        btnBreak.setOnClickListener(this);

        if(_mSPMHeaderData!=null){
            if (_mSPMHeaderData.getBitStart().equals("0")) {
                initMethodMappingButton();
            } else {
                showPopupStartButton();
            }
        }

        setCircleReport();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

            tvTotalPending.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(_mSPMHeaderData.getBitSync().equals("0")){
                        Intent intent = new Intent(OutstandingTask.this, TabsTaskHeader.class);
                        intent.putExtra("txtNoSPM", _mSPMHeaderData.getTxtNoSPM());
                        intent.putExtra("tab","0");
                        startActivity(intent);
                    }
                }
            });
        tvTotalConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_mSPMHeaderData.getBitSync().equals("0")){
                    Intent intent = new Intent(OutstandingTask.this, TabsTaskHeader.class);
                    intent.putExtra("txtNoSPM", _mSPMHeaderData.getTxtNoSPM());
                    intent.putExtra("tab","1");
                    startActivity(intent);
                }
            }
        });
        tvTotalCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_mSPMHeaderData.getBitSync().equals("0")){
                    Intent intent = new Intent(OutstandingTask.this, TabsTaskHeader.class);
                    intent.putExtra("txtNoSPM", _mSPMHeaderData.getTxtNoSPM());
                    intent.putExtra("tab","2");
                    startActivity(intent);
                }
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
                } else if (mSPMDetailDataListPending.size() == 0) {
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
            } else if (mSPMDetailDataListPending.size() == 0) {
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
        final View promptView = layoutInflater.inflate(R.layout.activity_start, null);
        final Button btnStart = (Button) promptView.findViewById(R.id.btn_start);
        final Button btnBack = (Button) promptView.findViewById(R.id.btn_back);
        final LinearLayout ll = (LinearLayout) promptView.findViewById(R.id.ll_popup);

//        ll.setBackgroundColor(Color.TRANSPARENT);
//        promptView.setBackgroundColor(Color.TRANSPARENT);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder
                .setCancelable(false);

        final AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String dt = "";
                if(_mSPMHeaderData.getDtStart().length() == 0 || _mSPMHeaderData.getDtStart().toString().equals("NULL")) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Calendar cal = Calendar.getInstance();
                    dt = dateFormat.format(cal.getTime());
                }
                else{
                    dt = _mSPMHeaderData.getDtStart();
                }

                new mSPMHeaderBL().updateDataSPMStartById(_mSPMHeaderData.getIntSPMId(), dt);

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                onBackPressed();
            }
        });
    }

    private void initMethodTableReport(Context applicationContext) {
        tReport.removeAllViews();

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f);
        params.setMargins(1, 1, 1, 1);

        TableRow tr = new TableRow(applicationContext);

        String[] colTextHeader = {"Locator", "Item Code", "Qty", "Status"};

        for (String text : colTextHeader) {
            TextView tv = new TextView(applicationContext);

            tv.setTextSize(14);
            tv.setPadding(10, 10, 10, 10);
            tv.setText(text);
            tv.setGravity(Gravity.CENTER);
            tv.setBackgroundColor(Color.parseColor("#4CAF50"));
            tv.setTextColor(Color.WHITE);
            tv.setLayoutParams(params);

            tr.addView(tv);
        }
        tReport.addView(tr);

        List<mSPMDetailData> dtListReportData = new ArrayList<>();
        dtListReportData = new mSPMDetailBL().getAllData();

        if (dtListReportData != null) {
            int index = 1;
            for (mSPMDetailData dat : dtListReportData) {
                tr = new TableRow(applicationContext);

                TextView locator = new TextView(applicationContext);
                locator.setTextSize(12);
                locator.setPadding(10, 10, 10, 10);
//                locator.setBackgroundColor(dat.get_txtGroupKN().contains("TOTAL") ? Color.parseColor("#d2fc47") : Color.parseColor("#f0f0f0"));
                locator.setTextColor(Color.BLACK);
                locator.setGravity(Gravity.CENTER);
                locator.setText(dat.getTxtLocator());
                locator.setLayoutParams(params);

                tr.addView(locator);

                TextView itemName = new TextView(applicationContext);
                itemName.setTextSize(12);
                itemName.setPadding(10, 10, 10, 10);
//                brand.setBackgroundColor(dat.get_txtGroupKN().contains("TOTAL") ? Color.parseColor("#d2fc47") : Color.parseColor("#f0f0f0"));
                itemName.setTextColor(Color.BLACK);
                itemName.setGravity(Gravity.CENTER);
                itemName.setText(dat.getTxtItemName());
                itemName.setLayoutParams(params);

                tr.addView(itemName);

                TextView qty = new TextView(applicationContext);
                qty.setTextSize(12);
                qty.setPadding(10, 10, 10, 10);
//                ach_mtd.setBackgroundColor(dat.get_txtGroupKN().contains("TOTAL") ? Color.parseColor("#d2fc47") : Color.parseColor("#f0f0f0"));
                qty.setTextColor(Color.BLACK);
                qty.setGravity(Gravity.CENTER);
                qty.setText(dat.getIntQty());
                qty.setLayoutParams(params);

                tr.addView(qty);

                TextView status = new TextView(applicationContext);
                String statusText = "pending";
                if (dat.getBitStatus().equals("1") && dat.getBitSync().equals("1")) {
                    statusText = "Sync";
                } else if (dat.getBitStatus().equals("2") && dat.getBitSync().equals("1")) {
                    statusText = "Cancel";
                }
                status.setTextSize(12);
                status.setPadding(10, 10, 10, 10);
//                ach_ytd.setBackgroundColor(dat.get_txtGroupKN().contains("TOTAL") ? Color.parseColor("#d2fc47") : Color.parseColor("#f0f0f0"));
                status.setTextColor(Color.BLACK);
                status.setGravity(Gravity.CENTER);
                status.setText(statusText);
                status.setLayoutParams(params);

                tr.addView(status);

                tReport.addView(tr, index++);
            }
        }
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
                alertDialogCancel.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        refreshSPMHeader();
                    }
                });
                alertDialogCancel.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

                alertDialogCancel.show();
                break;
            case R.id.btn_complete:
                final AlertDialog.Builder alertDialogComplete = new AlertDialog.Builder(this);
                alertDialogComplete.setTitle("Confirm");
                alertDialogComplete.setMessage("Are you sure to Complete?");
                alertDialogComplete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmTaskHeader();
                    }
                });
                alertDialogComplete.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

                alertDialogComplete.show();
                break;
            case R.id.btn_break:
                final AlertDialog.Builder alertDialogBreak = new AlertDialog.Builder(this);
                alertDialogBreak.setTitle("Confirm");
                alertDialogBreak.setMessage("Are you sure to Break?");
                alertDialogBreak.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
                    }
                });
                alertDialogBreak.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

                alertDialogBreak.show();
                break;
        }
    }

    private void refreshSPMHeader() {
        final String result = new mSPMHeaderBL().GetAllData().getTxtNoSPM();
        Boolean status;

        if (result != null) {
            progressDialog.show();
//            new clsMainActivity().timerDelayRemoveDialog(time, progressDialog);
            tUserLoginData _tUserLoginData = new tUserLoginBL().getUserActive();
            status = new WMSMobileService().refreshDataSTAR(result, _tUserLoginData.getIntUserId(), versionName);
            if (!status) {
                progressDialog.dismiss();
                boolean report = new SignalRBL().buildingConnection();
                new clsMainActivity().showCustomToast(getApplicationContext(), "Please Check Your Connection...", false);
            }
        }
    }

    public void confirmTaskHeader() {
        progressDialog.show();
//        new clsMainActivity().timerDelayRemoveDialog(time, progressDialog);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String dt = dateFormat.format(cal.getTime());
        boolean status = false;
        String _intSPMId = _mSPMHeaderData.getIntSPMId();

        tTimerLogData dtTimerLogComplete = new tTimerLogData();
        dtTimerLogComplete.setDtExecute(dt);
        dtTimerLogComplete.setBitActive("1");
        dtTimerLogComplete.setTxtTimerType("Complete");
        dtTimerLogComplete.setTxtTimerStatus("Close");
        dtTimerLogComplete.setTxtStarNo(_mSPMHeaderData.getTxtNoSPM());
        dtTimerLogComplete.setTxtTimerLogId(new clsMainBL().GenerateGuid());

        new tTimerLogBL().insertData(dtTimerLogComplete);

        List<tTimerLogData> ltTimerLogData = new ArrayList<>();
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

        status = new WMSMobileService().confirmSPMHeader(_intSPMId, versionName, dataLogin.getIntUserId(),resJson.toString());
        if (status) {
            boolean report = new SignalRBL().buildingConnection();
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
            new mSPMHeaderDA(db).DeleteAllDataMConfig(db);
            new mSPMDetailDA(db).DeleteAllDataMConfig(db);
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
        OutstandingTask.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONArray jsonArray = null;
                String strMethodName, strMessage, boolValid, intRoleId, txtRoleName, dtInsert, dtUpdated;

                try {
                    boolValid = jsonObject.get("boolValid").toString();
                    strMessage = jsonObject.get("strMessage").toString();
                    strMethodName = jsonObject.get("strMethodName").toString();

                    if (strMethodName.equalsIgnoreCase("ConfirmSPMHeader")) {
                        initMethodConfirmSPMHeader(jsonObject);
                    } else if (strMethodName.equalsIgnoreCase("pushDataOffline")) {
                        updateFromPushDataOffline(jsonObject);
                    } else if (strMethodName.equalsIgnoreCase("RefreshDataSTAR")) {
                        initMethodSPM(jsonObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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

                if (status.equals("1") && sync.equals("0")) {

                    new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, true);

                    JSONArray jsonArrayInner = jsonObject.getJSONArray("listOfmSPMDetail");

                    List<mSPMDetailData> _mSPMDetailData = new ArrayList<>();

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
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
            } else {
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, false);
            }
            progressDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }
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
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, true);
                initMethodMappingButton();
            } else {
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initMethodCancelSPMHeader(JSONObject jsonObject) {
        try {
            String boolValid = jsonObject.get("boolValid").toString();
            String strMessage = jsonObject.get("strMessage").toString();
            String strMethodName = jsonObject.get("strMethodName").toString();

            if (boolValid.equalsIgnoreCase("true")) {
                JSONObject jsonObjectHeader = jsonObject.getJSONObject("listOfmSPMHeader");

                String status = jsonObjectHeader.get("STATUS").toString();
                String sync = jsonObjectHeader.get("SYNC").toString();
                String _intSPMHeaderId = jsonObjectHeader.get("SPM_HEADER_ID").toString();

                new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, true);

                new mSPMHeaderBL().updateDataValueById(_intSPMHeaderId);
//                    adapterSuccess.notifyDataSetChanged();
//                    adapterProgress.notifyDataSetChanged();
//                    new TaskOnProgressFragment().fetchData(OutstandingTask.this);
//                    new TaskSuccessFragment().fetchData(OutstandingTask.this);
                btnRefresh.setEnabled(false);
                btnTaskView.setEnabled(false);
                btnComplete.setEnabled(false);
                btnTaskView.setBackgroundResource(R.drawable.btn_innermenu_gray);
                btnComplete.setBackgroundResource(R.drawable.btn_innermenu_gray);
                btnRefresh.setBackgroundResource(R.drawable.btn_innermenu_gray);
//                    SQLiteDatabase db = new clsMainBL().getDb();
//                    new mSPMHeaderDA(db).DropTable(db);
//                    new mSPMDetailDA(db).DropTable(db);
                finish();
                onBackPressed();
//                    Intent myIntent = new Intent(OutstandingTask.this, Home.class);
//                    startActivity(myIntent);
            } else {
                Toast.makeText(OutstandingTask.this, String.valueOf(strMessage), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
//                    Toast.makeText(OutstandingTask.this, String.valueOf(datapassed), Toast.LENGTH_SHORT).show();
        }
    }

    private void initMethodConfirmSPMHeader(JSONObject jsonObject) {

        try {
            String boolValid = jsonObject.get("boolValid").toString();
            String strMessage = jsonObject.get("strMessage").toString();
            String strMethodName = jsonObject.get("strMethodName").toString();

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
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
            } else {
                if(progressDialog!=null){
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
            progressDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
//                    Toast.makeText(OutstandingTask.this, String.valueOf(datapassed), Toast.LENGTH_SHORT).show();
        }
    }

    public void setHubConnection(WMSMobileService.mHubConnectionSevice hubConnection) {
        WMSMobileService.mHubConnectionSevice = hubConnection;
    }

//    public void unSetHubConnection(WMSMobileService.mHubConnectionSevice hubConnection) {
//        WMSMobileService.mHubConnectionSevice = null;
//    }
}
