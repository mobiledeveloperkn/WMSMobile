package wms.mobile;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import addon.ConnectivityReceiver;
import addon.MyApplication;
import addon.SwipeListAdapter;
import bl.SignalRBL;
import bl.mSPMDetailBL;
import bl.mSPMHeaderBL;
import bl.mSystemConfigBL;
import bl.tUserLoginBL;
import fragment.TaskCancelFragment;
import fragment.TaskOnProgressFragment;
import fragment.TaskSuccessFragment;
import library.common.mSPMDetailData;
import library.common.mSPMHeaderData;
import library.common.mSystemConfigData;
import library.common.tUserLoginData;
import service.WMSMobileService;

import static fragment.TaskOnProgressFragment.adapterProgress;
import static fragment.TaskSuccessFragment.adapterSuccess;

/**
 * Created by ASUS ZE on 16/11/2016.
 */

public class TabsTaskHeader extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, WMSMobileService.mHubConnectionSevice, SwipeListAdapter.triggerOnOfflineConnection, SwipeListAdapter.triggerProgressDialog {

    @SuppressLint("StaticFieldLeak")
    private static TabLayout tabLayout;
    private tUserLoginData dataLogin;

    private boolean refresher;

    mSPMHeaderData _mSPMHeaderData;
    private List<mSPMDetailData> mSPMDetailDataListConfirm;
    private List<mSPMDetailData> mSPMDetailData;

    @SuppressLint("StaticFieldLeak")
    private static CoordinatorLayout coordinatorLayout;
    private static ConnectivityManager conMan;

    private String txtNoSPM;
    private String versionName = "";
    private String tab;

    private static ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeGreen);
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_tabs_task_header);
        coordinatorLayout = findViewById(R.id.coordinatorLayoutTabsRaskHeader);
        conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        PackageInfo pInfo = new clsMainActivity().getPinfo(this);
        if (pInfo != null) {
            versionName = pInfo.versionName;
        }
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            txtNoSPM = bundle.getString("txtNoSPM");
            tab = bundle.getString("tab");
        }

        dataLogin = new tUserLoginBL().getUserActive();

        initToolbar();
        initViewPagerAndTabs();

        List<mSPMDetailData> _mSPMDetailData;
        _mSPMDetailData = new mSPMDetailBL().getAllDataTaskPending(txtNoSPM);

        if(tab!=null){
            switchTabDinamis(Integer.parseInt(tab));
        } else if (_mSPMDetailData.size() == 0) {
            switchTab();
        }
        //bikin progres dialognya
        progressDialog = new ProgressDialog(TabsTaskHeader.this);
        progressDialog.setMessage("Loading... Please Wait");
        progressDialog.setIndeterminate(false); //ukur berapa persen, false maka not do
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onStart() {
        setTriggerProgress(this);
        setConnectionOffline(this);
        setHubConnection(this);
        super.onStart();
    }

    @Override
    protected void onResume() {
        MyApplication.getInstance().setConnectivityListener(this);
        super.onResume();
    }

    private void initToolbar() {
        if(!txtNoSPM.equals("")){
            _mSPMHeaderData = new mSPMHeaderBL().GetDataById(txtNoSPM);
        }

        Toolbar mToolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(mToolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("NO. STAR : " + txtNoSPM);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }

    public void initViewPagerAndTabs() {
        List<library.common.mSPMDetailData> mSPMDetailDataListPending;
        mSPMDetailDataListPending = new mSPMDetailBL().getAllDataTaskPending(txtNoSPM);
        mSPMDetailDataListConfirm = new ArrayList<>();
        mSPMDetailDataListConfirm = new mSPMDetailBL().getAllDataTaskConfirm(txtNoSPM);
        List<library.common.mSPMDetailData> mSPMDetailDataListCancel;
        mSPMDetailDataListCancel = new mSPMDetailBL().getAllDataTaskCancel(txtNoSPM);
        mSPMDetailData = new ArrayList<>();
        mSPMDetailData = new mSPMDetailBL().getAllData();
        ViewPager viewPager = findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new TaskOnProgressFragment(), "OnProgress(" + mSPMDetailDataListPending.size() + ")");
        pagerAdapter.addFragment(new TaskSuccessFragment(), "Confirm(" + mSPMDetailDataListConfirm.size() + ")");
        pagerAdapter.addFragment(new TaskCancelFragment(), "Cancel(" + mSPMDetailDataListCancel.size() + ")");
        viewPager.setAdapter(pagerAdapter);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        new clsMainActivity().checkConnection(coordinatorLayout, conMan);
        refresher = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.title_total_spm, menu);
        MenuItem menuItem = menu.findItem(R.id.title);
        MenuItem menuItem2 = menu.findItem(R.id.icon);
        mSPMDetailDataListConfirm = new mSPMDetailBL().getAllDataTaskConfirm(txtNoSPM);
        mSPMDetailData = new mSPMDetailBL().getAllDataById(txtNoSPM);
        mSystemConfigData dtcnf = new mSystemConfigBL().getData(1);

        if(!refresher){
            SpannableString spanString = new SpannableString(mSPMDetailDataListConfirm.size() + "/" + mSPMDetailData.size() + " - ORDER BY "+dtcnf.get_txtValue().toUpperCase());
            int end = spanString.length();
            spanString.setSpan(new RelativeSizeSpan(1.4f), 0, end, Spannable.SPAN_COMPOSING);
            menuItem.setTitle(spanString);

            menuItem.setOnMenuItemClickListener(item -> {
                CharSequence[] charSequence = new CharSequence[] {"Ascending","Descending"};
                mSystemConfigData dt = new mSystemConfigBL().getData(1);
                int selected_id = dt.get_txtValue().equals("asc") ? 0 : 1;

                new AlertDialog.Builder(TabsTaskHeader.this)
                        .setTitle("List Order Picking")
                        .setSingleChoiceItems(charSequence, selected_id, (dialog, which) -> {
                            new mSystemConfigBL().updateOrderPicking(which);
                            dialog.dismiss();

                            new clsMainActivity().showCustomToast(getApplicationContext(), "ordering changed", true);
                            recreate();
                        })
                        .show();
                return false;
            });

            menuItem2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int position = tabLayout.getSelectedTabPosition();
                    List<String> segment2List =  new mSPMDetailBL().getAllSegment2(txtNoSPM);
                    String[] charSequence = segment2List.toArray(new String[segment2List.size()]);
                    mSystemConfigData dt = new mSystemConfigBL().getData(2);
                    int count = charSequence.length;
                    boolean[] is_checked = new boolean[count];
                    String listSegment = "";
                    if (dt!=null){
                        listSegment = dt.get_txtValue();
                    }
                    List<String> listSegment2 = new ArrayList<>();
                    if (listSegment.length()>0){
                        String[] words = listSegment.split(",");
                        Collections.addAll(listSegment2, words);
                    }

                    for (int i = 0; i < segment2List.size(); i++){
                        for (String sgm : listSegment2){
                            if (segment2List.get(i).equals(sgm)){
                                is_checked[i] = true;
                            }
                        }

                    }
                    new AlertDialog.Builder(TabsTaskHeader.this)
                            .setTitle("Select Segments")
                            .setMultiChoiceItems(charSequence, is_checked, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    boolean valid = true;
                                    if (isChecked){
                                        for (String segment :listSegment2){
                                            if (segment.equals(charSequence[which])){
                                                valid = false;
                                            }
                                        }
                                        if (valid){
                                            listSegment2.add(charSequence[which]);
                                        }
                                    }else {
                                        for (String segment :listSegment2){
                                            if (segment.equals(charSequence[which])){
                                                valid = false;
                                            }
                                        }
                                        if (!valid){
                                            listSegment2.remove(charSequence[which]);
                                        }
                                    }
                                }
                            })
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    StringBuilder bd = new StringBuilder();
                                    for (String segment :listSegment2){
                                        if (bd.length()==0){
                                            bd.append(segment);
                                        }else {
                                            bd.append(",");
                                            bd.append(segment);
                                        }
                                    }
                                    new mSystemConfigBL().UpdateFilterPicking(bd.toString());
                                    dialog.dismiss();
                                    new clsMainActivity().showCustomToast(getApplicationContext(), "Filtering changed", true);
                                    recreate();
//                                Toast.makeText(getApplicationContext(), bd.toString(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Reset", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new mSystemConfigBL().UpdateFilterPicking("");
                                    dialog.dismiss();
                                    new clsMainActivity().showCustomToast(getApplicationContext(), "Filtering changed", true);
                                    recreate();
                                }
                            })
                            .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();

                    return false;
                }
            });
            refresher = true;
        }

        return true;
    }

//    @Override
//    public void onReceiveMessageHub(final JSONObject jsonObject) {
//
//    }

    private void initMethodRevertSPMDetail(JSONObject jsonObject) {
        String strMessage, boolValid;

        try {
            boolValid = jsonObject.get("boolValid").toString();
            strMessage = jsonObject.get("strMessage").toString();

            if (boolValid.equalsIgnoreCase("true")) {
                JSONObject jsonObjectHeader = jsonObject.getJSONObject("listOfmSPMDetail");

                String status = jsonObjectHeader.get("STATUS").toString();
                String sync = jsonObjectHeader.get("SYNC").toString();
                String _intSPMDetailId = jsonObjectHeader.get("SPM_DETAIL_ID").toString();
                txtNoSPM = jsonObjectHeader.getString("SPM_NO");

                if (status.equals("0") && sync.equals("0")) {

//                    new clsMainActivity().saveTimerLog("Open", "Done: " + txtLocator, txtNoSPM);

                    new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, true);

                    new mSPMDetailBL().updateDataRevertById(_intSPMDetailId, dataLogin.getIntUserId());
                    adapterSuccess.notifyDataSetChanged();
                    adapterProgress.notifyDataSetChanged();
//                    adapterCancel.notifyDataSetChanged();
                    new TaskOnProgressFragment().fetchData(TabsTaskHeader.this);
                    new TaskSuccessFragment().fetchData(TabsTaskHeader.this);
                    new TaskCancelFragment().fetchData(TabsTaskHeader.this);
//                    menu.clear();
//                    onCreateOptionsMenu(menu);
                }
            } else {
                new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
    }

    private void updateFromPushDataOffline(JSONObject jsonObject) {
        String strMessage, boolValid;

        try {
            boolValid = jsonObject.get("boolValid").toString();
            strMessage = jsonObject.get("strMessage").toString();

            if (boolValid.equalsIgnoreCase("true")) {

                String txtNoSPM = "";
                if (!jsonObject.isNull("listOfmSPMHeader")) {
                    JSONObject jsonObjectSPMHeader = jsonObject.getJSONObject("listOfmSPMHeader");

                    String status = jsonObjectSPMHeader.get("STATUS").toString();
                    String sync = jsonObjectSPMHeader.get("SYNC").toString();

                    mSPMHeaderData _mSPMHeaderData = new mSPMHeaderData();

                    if (status.equals("1") && sync.equals("1")) {
                        txtNoSPM = jsonObjectSPMHeader.get("SPM_NO").toString();
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
                        txtNoSPM = jsonObject.getString("SPM_NO");
                        String statusDetail = jsonObject.get("STATUS").toString();
                        String syncDetail = jsonObject.get("SYNC").toString();
                        String id = jsonObject.get("SPM_DETAIL_ID").toString();

                        new mSPMDetailBL().saveFromPushData(id, statusDetail, syncDetail);
                    }
                }

                if(!refresher){
                    adapterSuccess.notifyDataSetChanged();
                    adapterProgress.notifyDataSetChanged();
//                    adapterCancel.notifyDataSetChanged();
                    new TaskOnProgressFragment().fetchData(TabsTaskHeader.this);
                    new TaskSuccessFragment().fetchData(TabsTaskHeader.this);

                    refresher = false;
                }

                List<mSPMDetailData> _mSPMDetailData;
                _mSPMDetailData = new mSPMDetailBL().getAllDataTaskPendingNoFilter(txtNoSPM);
                List<mSPMDetailData> _mSPMDetailDataFilter = new mSPMDetailBL().getAllDataTaskPending(txtNoSPM);

                if (_mSPMDetailData.size() == 0) {
                    finish();
                    onBackPressed();
                }else if (_mSPMDetailDataFilter.size()==0){
                    new mSystemConfigBL().UpdateFilterPicking("");
                }
                new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, true);
                recreate();
            } else {
                new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initMethodCancelSPMDetail(JSONObject jsonObject) {
        String strMessage, boolValid;
        String txtNoSPM;

        try {
            boolValid = jsonObject.get("boolValid").toString();
            strMessage = jsonObject.get("strMessage").toString();

            if (boolValid.equalsIgnoreCase("true")) {
                JSONObject jsonObjectHeader = jsonObject.getJSONObject("listOfmSPMDetail");

                String status = jsonObjectHeader.get("STATUS").toString();
                String sync = jsonObjectHeader.get("SYNC").toString();
                String _intSPMDetailId = jsonObjectHeader.get("SPM_DETAIL_ID").toString();
                String _reason = jsonObjectHeader.get("REASON").toString();
                txtNoSPM = jsonObjectHeader.getString("SPM_NO");

                if (status.equals("2") && sync.equals("1")) {

                    new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, true);

                    new mSPMDetailBL().updateDataSPMCancelById(_intSPMDetailId, dataLogin.getIntUserId(), _reason);
                    adapterSuccess.notifyDataSetChanged();
                    adapterProgress.notifyDataSetChanged();
//                    adapterCancel.notifyDataSetChanged();
                    new TaskOnProgressFragment().fetchData(TabsTaskHeader.this);
                    new TaskSuccessFragment().fetchData(TabsTaskHeader.this);
//                    new TaskCancelFragment().fetchData(TabsTaskHeader.this);
                    List<mSPMDetailData> _mSPMDetailData;
                    _mSPMDetailData = new mSPMDetailBL().getAllDataTaskPendingNoFilter(txtNoSPM);
                    List<mSPMDetailData> _mSPMDetailDataFilter = new mSPMDetailBL().getAllDataTaskPending(txtNoSPM);

                    if (_mSPMDetailData.size() == 0) {
//                        switchTab();
                        finish();
                        onBackPressed();
                    }else if (_mSPMDetailDataFilter.size() == 0){
                        new mSystemConfigBL().UpdateFilterPicking("");
                    }
                }
            } else {
                new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        recreate();
        progressDialog.dismiss();
    }

    private void initMethodConfirmSPMDetail(JSONObject jsonObject) {
        String strMessage, boolValid;
        String txtNoSPM;

        try {
            boolValid = jsonObject.get("boolValid").toString();
            strMessage = jsonObject.get("strMessage").toString();

            if (boolValid.equalsIgnoreCase("true")) {
                JSONObject jsonObjectHeader = jsonObject.getJSONObject("listOfmSPMDetail");

                String status = jsonObjectHeader.get("STATUS").toString();
                String sync = jsonObjectHeader.get("SYNC").toString();
                String _intSPMDetailId = jsonObjectHeader.get("SPM_DETAIL_ID").toString();
                String txtLocator = jsonObjectHeader.get("LOCATOR").toString();
                txtNoSPM = jsonObjectHeader.getString("SPM_NO");

                mSPMDetailData dtDetail = new mSPMDetailBL().getData(Integer.parseInt(_intSPMDetailId));

                if (status.equals("1") && sync.equals("1")) {

                    new clsMainActivity().saveTimerLog("Open", "Done: " + txtLocator, txtNoSPM);

                    if(!dtDetail.getBitStatus().equals("1") && !dtDetail.getBitSync().equals("1")){
                        new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, true);
                    }

                    if(!refresher || (!dtDetail.getBitStatus().equals("1") && !dtDetail.getBitSync().equals("1"))){
//                    new mSPMDetailBL().updateDataValueById(_intSPMDetailId, dataLogin.getIntUserId());
                        adapterSuccess.notifyDataSetChanged();
                        adapterProgress.notifyDataSetChanged();
//                    adapterCancel.notifyDataSetChanged();
                        new TaskOnProgressFragment().fetchData(TabsTaskHeader.this);
                        new TaskSuccessFragment().fetchData(TabsTaskHeader.this);
//                    new TaskCancelFragment().fetchData(TabsTaskHeader.this);

                        refresher = false;
//                        menu.clear();
//                        onCreateOptionsMenu(menu);
                    }

                    List<mSPMDetailData> _mSPMDetailData;
                    _mSPMDetailData = new mSPMDetailBL().getAllDataTaskPendingNoFilter(txtNoSPM);
                    List<mSPMDetailData> _mSPMDetailDataFilter = new mSPMDetailBL().getAllDataTaskPending(txtNoSPM);

                    if (_mSPMDetailData.size() == 0) {
//                        switchTab();
                        finish();
                        onBackPressed();
                    }else if (_mSPMDetailDataFilter.size() == 0){
                        new mSystemConfigBL().UpdateFilterPicking("");
                    }
                }
            } else {
                new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        recreate();
        progressDialog.dismiss();
    }

    public void setHubConnection(WMSMobileService.mHubConnectionSevice hubConnection) {
        WMSMobileService.mHubConnectionSevice = hubConnection;
    }

    public void setConnectionOffline(SwipeListAdapter.triggerOnOfflineConnection connectionOffline) {
        SwipeListAdapter.triggerOnOfflineConnection = connectionOffline;
    }

    public void setTriggerProgress(SwipeListAdapter.triggerProgressDialog progressDialog) {
        SwipeListAdapter.triggerProgressDialog = progressDialog;
    }

    @Override
    public void onOfflineConnection(final JSONObject jsonObject) {
        TabsTaskHeader.this.runOnUiThread(() -> {
            String method;

            try {
                method = jsonObject.get("strMethodName").toString();

                if (method.equals("confirmDetail")) {
                    initMethodConfirmSPMDetailOffline(jsonObject);
                } else if (method.equals("cancelDetail")) {
                    initMethodCancelSPMDetailOffline(jsonObject);
                }
                recreate();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void initMethodCancelSPMDetailOffline(JSONObject jsonObject) {
        String _intSPMDetailId, message, reason, txtLocator;
        String txtNoSPM;

        try {
            _intSPMDetailId = jsonObject.get("_intSPMDetailId").toString();
            txtNoSPM = jsonObject.getString("txtNoSPM");
            message = jsonObject.get("message").toString();
            reason = jsonObject.get("reason").toString();
            txtLocator = jsonObject.get("LOCATOR").toString();

            new clsMainActivity().saveTimerLog("Open", "Cancel: " + txtLocator, txtNoSPM);

            new clsMainActivity().showCustomToast(getApplicationContext(), message, true);

            new mSPMDetailBL().updateDataSPMCancelByIdOffline(_intSPMDetailId, dataLogin.getIntUserId(), reason);
            adapterSuccess.notifyDataSetChanged();
            adapterProgress.notifyDataSetChanged();
//                    adapterCancel.notifyDataSetChanged();
            new TaskOnProgressFragment().fetchData(TabsTaskHeader.this);
            new TaskSuccessFragment().fetchData(TabsTaskHeader.this);
//                    new TaskCancelFragment().fetchData(TabsTaskHeader.this);
            List<mSPMDetailData> _mSPMDetailData;
            _mSPMDetailData = new mSPMDetailBL().getAllDataTaskPendingNoFilter(txtNoSPM);
            List<mSPMDetailData> _mSPMDetailDataFilter = new mSPMDetailBL().getAllDataTaskPending(txtNoSPM);

            if (_mSPMDetailData.size() == 0) {
//                        switchTab();
                finish();
                onBackPressed();
            }else  if (_mSPMDetailDataFilter.size() == 0) {
                new mSystemConfigBL().UpdateFilterPicking("");
            }
//            menu.clear();
//            onCreateOptionsMenu(menu);
            recreate();
            progressDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
    }

    private void initMethodConfirmSPMDetailOffline(JSONObject jsonObject) {
        String _intSPMDetailId, message;
        String txtNoSPM;

        try {
            _intSPMDetailId = jsonObject.get("_intSPMDetailId").toString();
            txtNoSPM = jsonObject.getString("txtNoSPM");
            message = jsonObject.get("message").toString();

            new clsMainActivity().showCustomToast(getApplicationContext(), message, true);

            new mSPMDetailBL().updateDataValueByIdOffline(_intSPMDetailId, dataLogin.getIntUserId());
            adapterSuccess.notifyDataSetChanged();
            adapterProgress.notifyDataSetChanged();
//                    adapterCancel.notifyDataSetChanged();
            new TaskOnProgressFragment().fetchData(TabsTaskHeader.this);
            new TaskSuccessFragment().fetchData(TabsTaskHeader.this);
//                    new TaskCancelFragment().fetchData(TabsTaskHeader.this);
            List<mSPMDetailData> _mSPMDetailData;
            _mSPMDetailData = new mSPMDetailBL().getAllDataTaskPendingNoFilter(txtNoSPM);
            List<mSPMDetailData> _mSPMDetailDataFilter = new mSPMDetailBL().getAllDataTaskPending(txtNoSPM);

            if (_mSPMDetailData.size() == 0) {
//                        switchTab();
                finish();
                onBackPressed();
            }else if (_mSPMDetailDataFilter.size() == 0){
                new mSystemConfigBL().UpdateFilterPicking("");
            }
//            menu.clear();
//            onCreateOptionsMenu(menu);
            recreate();
            progressDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
    }

    @Override
    public void showProgressDialog(boolean valid) {
        if(valid){
            progressDialog.show();
//            new clsMainActivity().timerDelayRemoveDialog(time, progressDialog);
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

    @Override
    public void onReceiveMessageHub(JSONObject jsonObject, boolean isCatch) {
        TabsTaskHeader.this.runOnUiThread(() -> {
            if (isCatch){
                progressDialog.dismiss();
                new AlertDialog.Builder(TabsTaskHeader.this)
                        .setTitle("Alert")
                        .setCancelable(false)
                        .setMessage("Failed getting data, please refresh...")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                refreshSPMHeader();
                                dialog.dismiss();
                            }
                        })
                        .show();
            }else {
                String strMethodName;

                try {
                    Log.i("Anjas-ReceiveHub", String.valueOf(refresher));

                    strMethodName = jsonObject.get("strMethodName").toString();
                    Log.i("HIT", strMethodName);

                    if (strMethodName.equalsIgnoreCase("ConfirmSPMDetail")) {
                        initMethodConfirmSPMDetail(jsonObject);
                    } else if (strMethodName.equalsIgnoreCase("cancelSPMDetail")) {
                        initMethodCancelSPMDetail(jsonObject);
                    } else if(strMethodName.equalsIgnoreCase("revertCancelSPMDetail")){
                        initMethodRevertSPMDetail(jsonObject);
                    } else if (strMethodName.equalsIgnoreCase("pushDataOffline")) {
                        updateFromPushDataOffline(jsonObject);
                    }
                    else if(strMethodName.equalsIgnoreCase("getLatestSTAR")){
                        try{
                            progressDialog.dismiss();
                            finish();

                            Intent intent = new Intent(TabsTaskHeader.this, TabsTaskHeader.class);
                            intent.putExtra("txtNoSPM", _mSPMHeaderData.getTxtNoSPM());
                            startActivity(intent);
                        }
                        catch (Exception ex){
                            Log.i("HIT-Exception", ex.toString());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        });
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

    public void updateTitleTabs(ArrayList<String> data) {
        for (int i = 0; i < data.size(); i++) {
            Objects.requireNonNull(tabLayout.getTabAt(i)).setText(data.get(i));
        }
    }

    public void switchTab() {
        Objects.requireNonNull(tabLayout.getTabAt(1)).select();
    }

    public void switchTabDinamis(int position) {
        Objects.requireNonNull(tabLayout.getTabAt(position)).select();
    }

    public void updateListView() {
        recreate();
//        adapterSuccess.notifyDataSetChanged();
//        adapterProgress.notifyDataSetChanged();
//        adapterCancel.notifyDataSetChanged();
//        new TaskOnProgressFragment().fetchData(TabsTaskHeader.this);
//        new TaskSuccessFragment().fetchData(TabsTaskHeader.this);
    }
}
