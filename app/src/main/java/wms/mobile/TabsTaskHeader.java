package wms.mobile;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import addon.ConnectivityReceiver;
import addon.MyApplication;
import addon.SwipeListAdapter;
import bl.mSPMDetailBL;
import bl.mSPMHeaderBL;
import bl.tUserLoginBL;
import fragment.TaskCancelFragment;
import fragment.TaskOnProgressFragment;
import fragment.TaskSuccessFragment;
import library.common.mSPMDetailData;
import library.common.mSPMHeaderData;
import library.common.tUserLoginData;
import service.WMSMobileService;

import static fragment.TaskOnProgressFragment.adapterProgress;
import static fragment.TaskSuccessFragment.adapterSuccess;

/**
 * Created by ASUS ZE on 16/11/2016.
 */

public class TabsTaskHeader extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, WMSMobileService.mHubConnectionSevice, SwipeListAdapter.triggerOnOfflineConnection, SwipeListAdapter.triggerProgressDialog {

    private static TabLayout tabLayout;
    private tUserLoginData dataLogin;

    mSPMHeaderData _mSPMHeaderData;
    private List<mSPMDetailData> mSPMDetailDataListPending;
    private List<mSPMDetailData> mSPMDetailDataListConfirm;
    private List<mSPMDetailData> mSPMDetailDataListCancel;
    private List<mSPMDetailData> mSPMDetailData;

    private static CoordinatorLayout coordinatorLayout;
    private static ConnectivityManager conMan;

    private Menu menu;
    private PackageInfo pInfo = null;
    private String versionName = "";
    private String txtNoSPM;
    private String tab;

    private static ProgressDialog progressDialog;
    private static long time = 15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeGreen);
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_tabs_task_header);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayoutTabsRaskHeader);
        conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            txtNoSPM = bundle.getString("txtNoSPM");
            tab = bundle.getString("tab");
        }

        dataLogin = new tUserLoginBL().getUserActive();

        initToolbar();
        initViewPagerAndTabs();

        List<mSPMDetailData> _mSPMDetailData = new ArrayList<mSPMDetailData>();
        _mSPMDetailData = new mSPMDetailBL().getAllDataTaskPending(txtNoSPM);

        if(tab!=null){
            switchTabDinamis(Integer.parseInt(tab));
        } else if (_mSPMDetailData.size() == 0) {
            switchTab();
        }
        pInfo = new clsMainActivity().getPinfo(this);

        if (pInfo != null) {
            versionName = pInfo.versionName;
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

//    @Override
//    protected void onDestroy() {
////        Intent broadcastIntent = new Intent("service.MyRebootReceiver");
////        sendBroadcast(broadcastIntent);
////        if(!new clsMainActivity().isMyServiceRunning(getApplicationContext())){
////            Intent broadcastIntent = new Intent("service.MyRebootReceiver");
////            sendBroadcast(broadcastIntent);
////        } else {
////            boolean valid = new WMSMobileService().checkinConnHub();
////            if(!valid){
////                new WMSMobileService().startSignalR();
////            }
////        }
//        coordinatorLayout = null;
//        conMan = null;
//        tabLayout = null;
//        super.onDestroy();
//    }

    private void initToolbar() {
        if(!txtNoSPM.equals("")){
            _mSPMHeaderData = new mSPMHeaderBL().GetDataById(txtNoSPM);
        }

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("NO. STAR : " + txtNoSPM);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }

    public void initViewPagerAndTabs() {
        mSPMDetailDataListPending = new ArrayList<>();
        mSPMDetailDataListPending = new mSPMDetailBL().getAllDataTaskPending(txtNoSPM);
        mSPMDetailDataListConfirm = new ArrayList<>();
        mSPMDetailDataListConfirm = new mSPMDetailBL().getAllDataTaskConfirm(txtNoSPM);
        mSPMDetailDataListCancel = new ArrayList<>();
        mSPMDetailDataListCancel = new mSPMDetailBL().getAllDataTaskCancel(txtNoSPM);
        mSPMDetailData = new ArrayList<>();
        mSPMDetailData = new mSPMDetailBL().getAllData();
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new TaskOnProgressFragment(), "OnProgress(" + String.valueOf(mSPMDetailDataListPending.size()) + ")");
        pagerAdapter.addFragment(new TaskSuccessFragment(), "Confirm(" + String.valueOf(mSPMDetailDataListConfirm.size()) + ")");
        pagerAdapter.addFragment(new TaskCancelFragment(), "Cancel(" + String.valueOf(mSPMDetailDataListCancel.size()) + ")");
        viewPager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        new clsMainActivity().checkConnection(coordinatorLayout, conMan);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.title_total_spm, menu);
        MenuItem menuItem = menu.findItem(R.id.title);
        mSPMDetailDataListConfirm = new mSPMDetailBL().getAllDataTaskConfirm(txtNoSPM);
        mSPMDetailData = new mSPMDetailBL().getAllDataById(txtNoSPM);
        SpannableString spanString = new SpannableString(mSPMDetailDataListConfirm.size() + "/" + mSPMDetailData.size());
        int end = spanString.length();
        spanString.setSpan(new RelativeSizeSpan(1.4f), 0, end, Spannable.SPAN_COMPOSING);
        menuItem.setTitle(spanString);

        return true;
    }

    @Override
    public void onReceiveMessageHub(final JSONObject jsonObject) {
        TabsTaskHeader.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONArray jsonArray = null;
                String strMethodName, strMessage, boolValid, intRoleId, txtRoleName, dtInsert, dtUpdated;

                try {
                    boolValid = jsonObject.get("boolValid").toString();
                    strMessage = jsonObject.get("strMessage").toString();
                    strMethodName = jsonObject.get("strMethodName").toString();

                    if (strMethodName.equalsIgnoreCase("ConfirmSPMDetail")) {
                        initMethodConfirmSPMDetail(jsonObject);
                    } else if (strMethodName.equalsIgnoreCase("cancelSPMDetail")) {
                        initMethodCancelSPMDetail(jsonObject);
                    } else if(strMethodName.equalsIgnoreCase("revertCancelSPMDetail")){
                        initMethodRevertSPMDetail(jsonObject);
                    } else if (strMethodName.equalsIgnoreCase("pushDataOffline")) {
//                        updateListView();
                        updateFromPushDataOffline(jsonObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initMethodRevertSPMDetail(JSONObject jsonObject) {
        String strMethodName, strMessage, boolValid, intRoleId, txtRoleName, dtInsert, dtUpdated;
        String txtNoSPM;

        try {
            boolValid = jsonObject.get("boolValid").toString();
            strMessage = jsonObject.get("strMessage").toString();
            strMethodName = jsonObject.get("strMethodName").toString();

            if (boolValid.equalsIgnoreCase("true")) {
                JSONObject jsonObjectHeader = jsonObject.getJSONObject("listOfmSPMDetail");

                String status = jsonObjectHeader.get("STATUS").toString();
                String sync = jsonObjectHeader.get("SYNC").toString();
                String _intSPMDetailId = jsonObjectHeader.get("SPM_DETAIL_ID").toString();
                String txtLocator = jsonObjectHeader.get("LOCATOR").toString();
                txtNoSPM = jsonObjectHeader.getString("SPM_NO").toString();

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
                    menu.clear();
                    onCreateOptionsMenu(menu);
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
        String strMethodName, strMessage, boolValid, intRoleId, txtRoleName, dtInsert, dtUpdated;
        String txtNoSPM = "";

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

                    List<mSPMDetailData> _mSPMDetailData = new ArrayList<>();

                    for (int i = 0; i < jsonArraySPMDetail.length(); i++) {
                        String jsonInner = jsonArraySPMDetail.get(i).toString();
                        jsonObject = new JSONObject(jsonInner);
                        txtNoSPM = jsonObject.getString("SPM_NO").toString();
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
                adapterSuccess.notifyDataSetChanged();
                adapterProgress.notifyDataSetChanged();
//                    adapterCancel.notifyDataSetChanged();
                new TaskOnProgressFragment().fetchData(TabsTaskHeader.this);
                new TaskSuccessFragment().fetchData(TabsTaskHeader.this);
//                    new TaskCancelFragment().fetchData(TabsTaskHeader.this);
                List<mSPMDetailData> _mSPMDetailData = new ArrayList<mSPMDetailData>();
                _mSPMDetailData = new mSPMDetailBL().getAllDataTaskPending(txtNoSPM);

                if (_mSPMDetailData.size() == 0) {
//                        switchTab();
                    finish();
                    onBackPressed();
                }
                menu.clear();
                onCreateOptionsMenu(menu);
                new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, true);
            } else {
                new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initMethodCancelSPMDetail(JSONObject jsonObject) {
        String strMethodName, strMessage, boolValid, intRoleId, txtRoleName, dtInsert, dtUpdated;
        String txtNoSPM;

        try {
            boolValid = jsonObject.get("boolValid").toString();
            strMessage = jsonObject.get("strMessage").toString();
            strMethodName = jsonObject.get("strMethodName").toString();

            if (boolValid.equalsIgnoreCase("true")) {
                JSONObject jsonObjectHeader = jsonObject.getJSONObject("listOfmSPMDetail");

                String status = jsonObjectHeader.get("STATUS").toString();
                String sync = jsonObjectHeader.get("SYNC").toString();
                String _intSPMDetailId = jsonObjectHeader.get("SPM_DETAIL_ID").toString();
                String _reason = jsonObjectHeader.get("REASON").toString();
                txtNoSPM = jsonObjectHeader.getString("SPM_NO").toString();

                if (status.equals("2") && sync.equals("1")) {

                    new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, true);

                    new mSPMDetailBL().updateDataSPMCancelById(_intSPMDetailId, dataLogin.getIntUserId(), _reason);
                    adapterSuccess.notifyDataSetChanged();
                    adapterProgress.notifyDataSetChanged();
//                    adapterCancel.notifyDataSetChanged();
                    new TaskOnProgressFragment().fetchData(TabsTaskHeader.this);
                    new TaskSuccessFragment().fetchData(TabsTaskHeader.this);
//                    new TaskCancelFragment().fetchData(TabsTaskHeader.this);
                    List<mSPMDetailData> _mSPMDetailData = new ArrayList<mSPMDetailData>();
                    _mSPMDetailData = new mSPMDetailBL().getAllDataTaskPending(txtNoSPM);

                    if (_mSPMDetailData.size() == 0) {
//                        switchTab();
                        finish();
                        onBackPressed();
                    }
                }
            } else {
                new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
    }

    private void initMethodConfirmSPMDetail(JSONObject jsonObject) {
        String strMethodName, strMessage, boolValid, intRoleId, txtRoleName, dtInsert, dtUpdated;
        String txtNoSPM;

        try {
            boolValid = jsonObject.get("boolValid").toString();
            strMessage = jsonObject.get("strMessage").toString();
            strMethodName = jsonObject.get("strMethodName").toString();

            if (boolValid.equalsIgnoreCase("true")) {
                JSONObject jsonObjectHeader = jsonObject.getJSONObject("listOfmSPMDetail");

                String status = jsonObjectHeader.get("STATUS").toString();
                String sync = jsonObjectHeader.get("SYNC").toString();
                String _intSPMDetailId = jsonObjectHeader.get("SPM_DETAIL_ID").toString();
                String txtLocator = jsonObjectHeader.get("LOCATOR").toString();
                txtNoSPM = jsonObjectHeader.getString("SPM_NO").toString();

                if (status.equals("1") && sync.equals("1")) {

                    new clsMainActivity().saveTimerLog("Open", "Done: " + txtLocator, txtNoSPM);

                    new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, true);

                    new mSPMDetailBL().updateDataValueById(_intSPMDetailId, dataLogin.getIntUserId());
                    adapterSuccess.notifyDataSetChanged();
                    adapterProgress.notifyDataSetChanged();
//                    adapterCancel.notifyDataSetChanged();
                    new TaskOnProgressFragment().fetchData(TabsTaskHeader.this);
                    new TaskSuccessFragment().fetchData(TabsTaskHeader.this);
//                    new TaskCancelFragment().fetchData(TabsTaskHeader.this);
                    List<mSPMDetailData> _mSPMDetailData = new ArrayList<mSPMDetailData>();
                    _mSPMDetailData = new mSPMDetailBL().getAllDataTaskPending(txtNoSPM);

                    if (_mSPMDetailData.size() == 0) {
//                        switchTab();
                        finish();
                        onBackPressed();
                    }
                    menu.clear();
                    onCreateOptionsMenu(menu);
                }
            } else {
                new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        TabsTaskHeader.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONArray jsonArray = null;
                String spmId, userId, method;

                try {
                    method = jsonObject.get("strMethodName").toString();

                    if (method.equals("confirmDetail")) {
                        initMethodConfirmSPMDetailOffline(jsonObject);
                    } else if (method.equals("cancelDetail")) {
                        initMethodCancelSPMDetailOffline(jsonObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initMethodCancelSPMDetailOffline(JSONObject jsonObject) {
        String _intSPMDetailId, _intUserId, message, reason, txtLocator;
        String txtNoSPM;

        try {
            _intSPMDetailId = jsonObject.get("_intSPMDetailId").toString();
            _intUserId = jsonObject.get("_intUserId").toString();
            txtNoSPM = jsonObject.getString("txtNoSPM".toString());
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
            List<mSPMDetailData> _mSPMDetailData = new ArrayList<mSPMDetailData>();
            _mSPMDetailData = new mSPMDetailBL().getAllDataTaskPending(txtNoSPM);

            if (_mSPMDetailData.size() == 0) {
//                        switchTab();
                finish();
                onBackPressed();
            }
            menu.clear();
            onCreateOptionsMenu(menu);
            progressDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
    }

    private void initMethodConfirmSPMDetailOffline(JSONObject jsonObject) {
        String _intSPMDetailId, _intUserId, message;
        String txtNoSPM;

        try {
            _intSPMDetailId = jsonObject.get("_intSPMDetailId").toString();
            _intUserId = jsonObject.get("_intUserId").toString();
            txtNoSPM = jsonObject.getString("txtNoSPM".toString());
            message = jsonObject.get("message").toString();

            new clsMainActivity().showCustomToast(getApplicationContext(), message, true);

            new mSPMDetailBL().updateDataValueByIdOffline(_intSPMDetailId, dataLogin.getIntUserId());
            adapterSuccess.notifyDataSetChanged();
            adapterProgress.notifyDataSetChanged();
//                    adapterCancel.notifyDataSetChanged();
            new TaskOnProgressFragment().fetchData(TabsTaskHeader.this);
            new TaskSuccessFragment().fetchData(TabsTaskHeader.this);
//                    new TaskCancelFragment().fetchData(TabsTaskHeader.this);
            List<mSPMDetailData> _mSPMDetailData = new ArrayList<mSPMDetailData>();
            _mSPMDetailData = new mSPMDetailBL().getAllDataTaskPending(txtNoSPM);

            if (_mSPMDetailData.size() == 0) {
//                        switchTab();
                finish();
                onBackPressed();
            }
            menu.clear();
            onCreateOptionsMenu(menu);
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

    public class PagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void addFragment(Fragment fragment, String title) {
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
            tabLayout.getTabAt(i).setText(data.get(i));
        }
    }

    public void switchTab() {
        tabLayout.getTabAt(1).select();
    }

    public void switchTabDinamis(int position) {
        tabLayout.getTabAt(position).select();
    }

    public void switchTabCancel() {
        tabLayout.getTabAt(2).select();
    }

    public void updateListView() {
        adapterSuccess.notifyDataSetChanged();
        adapterProgress.notifyDataSetChanged();
//                    adapterCancel.notifyDataSetChanged();
        new TaskOnProgressFragment().fetchData(TabsTaskHeader.this);
        new TaskSuccessFragment().fetchData(TabsTaskHeader.this);
    }
}
