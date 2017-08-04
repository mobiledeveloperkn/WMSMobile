package wms.mobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import addon.ConnectivityReceiver;
import addon.MyApplication;
import bl.SignalRBL;
import bl.clsMainBL;
import bl.mSPMDetailBL;
import bl.mSPMHeaderBL;
import bl.tDeviceInfoUserBL;
import bl.tUserLoginBL;
import library.common.enumConfigData;
import library.common.mSPMDetailData;
import library.common.mSPMHeaderData;
import library.common.mconfigData;
import library.common.tUserLoginData;
import library.dal.clsHardCode;
import library.dal.mconfigDA;
import service.WMSMobileService;

import static junit.framework.Assert.assertEquals;
import static wms.mobile.R.id.txtLoginEmail;

/**
 * Created by ASUS ZE on 15/11/2016.
 */

public class Login extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener, ConnectivityReceiver.ConnectivityReceiverListener, WMSMobileService.mHubConnectionSevice, WMSMobileService.mHubConnectionSlow {

    Button btn_login, btn_ping, btn_exit;
    TextInputEditText etTxtPass;
    EditText etTxtEmail;
    Spinner spnRole;
    String txtEmail, txtPass, selectedRole;
    private List<String> arrrole, arrNodata;
    private HashMap<String, String> HMRole = new HashMap<>();
    private CoordinatorLayout coordinatorLayout;
    ConnectivityManager conMan;
    TextInputLayout tilEmail, tilPass;
    TextView txtInfo, txtVersion;

    private String versionName = "";
    private tUserLoginData dataLogin;
    private ProgressDialog progressDialog;
    private LinearLayout llContentWarning;
    private LinearLayout llContent;
    private Button btnCheckVersion;
//    private long time = 15000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        btn_login = (Button) findViewById(R.id.buttonLogin);
        btn_ping = (Button) findViewById(R.id.buttonPing);
        btn_exit = (Button) findViewById(R.id.buttonExit);
        etTxtEmail = (EditText) findViewById(R.id.txtLoginEmail);
        etTxtPass = (TextInputEditText) findViewById(R.id.editTextPass);
        spnRole = (Spinner) findViewById(R.id.spnRole);
        tilEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        tilPass = (TextInputLayout) findViewById(R.id.input_layout_pass);
        txtInfo = (TextView) findViewById(R.id.txtVersionLogin);
        txtVersion = (TextView) findViewById(R.id.txtVersionApp);

        llContentWarning = (LinearLayout) findViewById(R.id.llContentWarning);
        llContent = (LinearLayout) findViewById(R.id.llContent);
        btnCheckVersion = (Button) findViewById(R.id.btnCheckVersion);

        etTxtEmail.setFocusableInTouchMode(true);
        etTxtPass.setFocusableInTouchMode(true);

        ImageView imgBanner = (ImageView) findViewById(R.id.header);
        imgBanner.setAdjustViewBounds(true);
        imgBanner.setScaleType(ImageView.ScaleType.CENTER_CROP);

        etTxtEmail.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        SQLiteDatabase db = new clsMainBL().getDb();
        mconfigDA _mconfigDA = new mconfigDA(db);

        mconfigData dataAPI = _mconfigDA.getData(db, enumConfigData.ApiKalbe.getidConfigData());
        txtInfo.setText(dataAPI.get_txtValue());

        final PackageInfo pInfo = new clsMainActivity().getPinfo(this);

        if (pInfo != null) {
            versionName = pInfo.versionName;
            txtVersion.setText(pInfo.versionName);
        }

        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Loading... Please Wait");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);

        try {
            if (pInfo != null) {
                llContentWarning.setVisibility(View.VISIBLE);
                llContent.setVisibility(View.GONE);
                btnCheckVersion.setVisibility(View.VISIBLE);
                requestCheckVersion(pInfo.versionName);
            }
        } catch (Exception ex) {
            llContentWarning.setVisibility(View.VISIBLE);
            btnCheckVersion.setVisibility(View.VISIBLE);
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }

        btnCheckVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(Login.this);
                progressDialog.setMessage("Loading... Please Wait");
                progressDialog.setIndeterminate(false);
                progressDialog.setCancelable(false);
                try {
                    if (pInfo != null) {
                        requestCheckVersion(pInfo.versionName);
                    }
                } catch (Exception ex) {
                    llContentWarning.setVisibility(View.VISIBLE);
                    btnCheckVersion.setVisibility(View.VISIBLE);
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            }
        });

        new tDeviceInfoUserBL().SaveInfoDevice("", "", versionName);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayoutLogin);

        arrNodata = new ArrayList<>();
        arrNodata.add("-");

        btn_ping.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        etTxtEmail.setOnKeyListener(this);
        etTxtPass.setOnKeyListener(this);

        spnRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedRole = spnRole.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dataLogin = new tUserLoginData();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLogin:
                txtPass = etTxtPass.getText().toString();
                sendRequestLogin();
                break;
            case R.id.buttonPing:
                sendRequestPing();
                break;
            case R.id.buttonExit:
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Confirm");
                alertDialog.setMessage("Are you sure to Exit?");
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
                break;
        }
    }

    private void sendRequestPing() {
        String strUrl = new mconfigDA(new clsMainBL().getDb()).getData(new clsMainBL().getDb(), enumConfigData.ApiKalbe.getidConfigData()).get_txtValue();

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL url = new URL(strUrl);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.connect();

            assertEquals(HttpURLConnection.HTTP_OK, urlConn.getResponseCode());
            new clsMainActivity().showCustomToast(Login.this, "Connected", true);

        } catch (IOException e) {
            new clsMainActivity().showCustomToast(Login.this, "Not connected", false);
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        switch (view.getId()) {
            case txtLoginEmail:
                // If the event is a key-down event on the "enter" button
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i == KeyEvent.KEYCODE_ENTER)) {
                    txtEmail = etTxtEmail.getText().toString();
                    if (txtEmail.length() > 0) {
                        sendRequestGetRole();
                    } else {
                        etTxtEmail.clearFocus();
                        etTxtPass.clearFocus();
                        etTxtEmail.requestFocus();
                        tilEmail.setError("Username cannot empty");
                        arrrole = new ArrayList<>();
                        arrrole = arrNodata;
                        spnRole.setAdapter(new MyAdapter(getApplicationContext(), R.layout.custom_spinner, arrrole));
                    }
                    return true;
                }
            case R.id.editTextPass:
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i == KeyEvent.KEYCODE_ENTER)) {
                    txtPass = etTxtPass.getText().toString();
                    if (txtPass.length() > 0) {
                        btn_login.performClick();
                    } else {
                        etTxtPass.requestFocus();
                        tilPass.requestFocus();
                        tilPass.setError("Password Cannot empty");
                    }
                    return true;
                }
        }
        return true;
    }

    private void requestCheckVersion(String versionName) {
        boolean status;
        if (versionName != null) {
            progressDialog.show();
            status = new WMSMobileService().getDataLastVersion(versionName);
            if (!status) {
                new clsMainActivity().checkConnection(coordinatorLayout, conMan);
                progressDialog.dismiss();
                llContentWarning.setVisibility(View.VISIBLE);
                btnCheckVersion.setVisibility(View.VISIBLE);
            }
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    }

    public void sendRequestGetRole() {
        boolean status;
        if (txtEmail.length() > 0) {
            progressDialog.show();
//            new clsMainActivity().timerDelayRemoveDialog(time, progressDialog);
            status = new WMSMobileService().getRole(txtEmail, versionName);
            if (!status) {
                progressDialog.dismiss();
                new clsMainActivity().checkConnection(coordinatorLayout, conMan);
                etTxtEmail.requestFocus();
            }
        } else {
            etTxtEmail.clearFocus();
            etTxtPass.clearFocus();
            etTxtEmail.requestFocus();
            new clsMainActivity().showSnackbar(coordinatorLayout, "Username cannot empty", false, "");
        }
    }

    private void sendRequestLogin() {
        String nameRole = selectedRole;
        boolean status = false;

        if (txtPass.length() > 0) {
            progressDialog.show();
//            new clsMainActivity().timerDelayRemoveDialog(time, progressDialog);
            try {
                status = new WMSMobileService().login(txtEmail, txtPass, HMRole.get(nameRole), versionName, dataLogin.getIntUserId());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!status) {
                progressDialog.dismiss();
                boolean report = new SignalRBL().buildingConnection();
                etTxtPass.requestFocus();
                Toast.makeText(Login.this, String.valueOf(report), Toast.LENGTH_SHORT).show();
            }
        } else {
            etTxtPass.requestFocus();
            new clsMainActivity().showSnackbar(coordinatorLayout, "Password cannot empty", false, "");
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        new clsMainActivity().checkConnection(coordinatorLayout, conMan);
    }

    @Override
    public void onReceiveMessageHub(final JSONObject jsonObject) {
        Login.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String strMethodName;
                arrrole = new ArrayList<>();
                arrrole = arrNodata;

                try {
                    strMethodName = jsonObject.get("strMethodName").toString();

                    if (strMethodName.equalsIgnoreCase("GetRoleByUsername")) {
                        initSpinnerRole(jsonObject);
                    } else if (strMethodName.equalsIgnoreCase("Login")) {
                        executeLogin(jsonObject);
                    } else if (strMethodName.equalsIgnoreCase("getDataLastVersion")) {
                        initMethodCheckinVersion(jsonObject);
                    } else if (strMethodName.equalsIgnoreCase("BroadcastMessage")) {
                        initMethodBroadcastMessage(jsonObject);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initMethodCheckinVersion(JSONObject jsonObject) {
        String strMethodName, boolValid, txtLink, strMessage ="";
        arrrole = new ArrayList<>();
        arrrole = arrNodata;

        try {
            boolValid = jsonObject.get("boolValid").toString();
            strMessage = jsonObject.get("strMessage").toString();
            strMethodName = jsonObject.get("strMethodName").toString();

            if (strMethodName.equalsIgnoreCase("getDataLastVersion")) {

                if (boolValid.equalsIgnoreCase("false")) {
                    JSONObject jsonObjectInner = jsonObject.getJSONObject("listOfmDataVesion");

                    txtLink = jsonObjectInner.get("LINK_FILE").toString();
                    // instantiate it within the onCreate method
                    mProgressDialog = new ProgressDialog(Login.this);
                    mProgressDialog.setMessage("Please Wait For Downloading File....");
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.setCancelable(false);

                    // execute this when the downloader must be fired
                    final DownloadTask downloadTask = new DownloadTask(Login.this);
                    downloadTask.execute(txtLink);

                    mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            downloadTask.cancel(true);
                        }
                    });
                } else {
                        llContent.setVisibility(View.VISIBLE);
                        btnCheckVersion.setVisibility(View.GONE);
                        llContentWarning.setVisibility(View.GONE);
                        new clsMainActivity().showToast(getApplicationContext(), strMessage);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initMethodBroadcastMessage(JSONObject jsonObject) {
        String strMessage;
        arrrole = new ArrayList<>();
        arrrole = arrNodata;

        try {
            strMessage = jsonObject.get("strMessage").toString();

            AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(this);
            builder1.setTitle("Inforamation");
            builder1.setMessage(strMessage);
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Close",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initSpinnerRole(JSONObject jsonObject) {

        String strMethodName, strMessage, boolValid, intRoleId, txtRoleName, intUserId;
        arrrole = new ArrayList<>();
        arrrole = arrNodata;

        try {
            boolValid = jsonObject.get("boolValid").toString();
            strMessage = jsonObject.get("strMessage").toString();
            strMethodName = jsonObject.get("strMethodName").toString();

            if (strMethodName.equalsIgnoreCase("GetRoleByUsername")) {

                if (boolValid.equalsIgnoreCase("true")) {
                    JSONArray jsonArrayInner = jsonObject.getJSONArray("listOfmUserRole");

                    arrrole = new ArrayList<>();

                    for (int i = 0; i < jsonArrayInner.length(); i++) {
                        String jsonInner = jsonArrayInner.get(i).toString();
                        jsonObject = new JSONObject(jsonInner);

                        intRoleId = jsonObject.get("ROLE_ID").toString();
                        txtRoleName = jsonObject.get("ROLE_NAME").toString();
                        intUserId = jsonObject.get("USER_ID").toString();
                        dataLogin.setIntUserId(intUserId);
                        arrrole.add(txtRoleName);

                        HMRole.put(txtRoleName, intRoleId);
                    }
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    tilEmail.setError(null);
                    etTxtPass.requestFocus();
                } else {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    tilEmail.setError(strMessage);
                    spnRole.setAdapter(new MyAdapter(getApplicationContext(), R.layout.custom_spinner, arrrole));
                    etTxtEmail.requestFocus();
                }
                spnRole.setAdapter(new MyAdapter(getApplicationContext(), R.layout.custom_spinner, arrrole));
                spnRole.setEnabled(true);
            } else {
                spnRole.setAdapter(new MyAdapter(getApplicationContext(), R.layout.custom_spinner, arrrole));
                spnRole.setEnabled(false);
                etTxtEmail.requestFocus();
            }
            progressDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }
    }

    private void executeLogin(JSONObject jsonObject) {

        String strMessage, boolValid;

        try {
            boolValid = jsonObject.get("boolValid").toString();
            strMessage = jsonObject.get("strMessage").toString();
//            strMethodName = jsonObject.get("strMethodName").toString();

            if (boolValid.equalsIgnoreCase("true")) {
                JSONObject jsonObjectLogin = jsonObject.getJSONObject("listOftUserLogin");

                tUserLoginData _tUserLoginData = new tUserLoginData();

                _tUserLoginData.setIntUserId(jsonObjectLogin.get("USER_ID").toString());
                _tUserLoginData.setTxtUserName(jsonObjectLogin.get("USER_NAME").toString());
                _tUserLoginData.setIntUserRole(jsonObjectLogin.get("ROLE_ID").toString());
                _tUserLoginData.setTxtRoleName(jsonObjectLogin.get("ROLE_NAME").toString());
                _tUserLoginData.setDtLastLogin(jsonObjectLogin.get("CREATION_DATE").toString());
                _tUserLoginData.setTxtDataId(jsonObjectLogin.getString("DATA_ID"));

                if (!jsonObject.isNull("listOfmSPMHeader")) {
                    JSONObject jsonObjectSPMHeader = jsonObject.getJSONObject("listOfmSPMHeader");

                    String status = jsonObjectSPMHeader.get("STATUS").toString();
                    String sync = jsonObjectSPMHeader.get("SYNC").toString();

                    mSPMHeaderData _mSPMHeaderData = new mSPMHeaderData();

                    if (status.equals("1") && sync.equals("0")) {
                        _mSPMHeaderData.setIntSPMId(jsonObjectSPMHeader.get("SPM_HEADER_ID").toString());
                        _mSPMHeaderData.setTxtNoSPM(jsonObjectSPMHeader.get("SPM_NO").toString());
                        _mSPMHeaderData.setTxtBranchCode(jsonObjectSPMHeader.get("BRANCH_CODE").toString());
                        _mSPMHeaderData.setTxtSalesOrder(jsonObjectSPMHeader.get("SALES_ORDER").toString());
                        _mSPMHeaderData.setIntUserId(jsonObjectSPMHeader.get("USER_ID").toString());
                        _mSPMHeaderData.setBitStatus("0");
                        _mSPMHeaderData.setBitSync(jsonObjectSPMHeader.get("SYNC").toString());
                        _mSPMHeaderData.setBitStart("0");

                        new mSPMHeaderBL().saveData(_mSPMHeaderData);

                        JSONArray jsonArraySPMDetail = jsonObject.getJSONArray("listOfmSPMDetail");

//                        List<mSPMDetailData> _mSPMDetailData = new ArrayList<>();

                        for (int i = 0; i < jsonArraySPMDetail.length(); i++) {
                            String jsonInner = jsonArraySPMDetail.get(i).toString();
                            jsonObject = new JSONObject(jsonInner);

                            mSPMDetailData data = new mSPMDetailData();

                            data.setIntSPMDetailId(jsonObject.get("SPM_DETAIL_ID").toString());
                            data.setTxtNoSPM(jsonObject.get("SPM_NO").toString());
                            data.setTxtLocator(jsonObject.get("LOCATOR").toString());
                            data.setTxtItemCode(jsonObject.get("ITEM_CODE").toString());
                            data.setTxtItemName(jsonObject.get("ITEM_NAME").toString());
                            data.setIntQty(jsonObject.get("QUANTITY").toString());
                            data.setBitStatus(jsonObject.get("STATUS").toString());
                            data.setBitSync(jsonObject.get("SYNC").toString());

                            new mSPMDetailBL().insert(data);
                        }
                    }
                }
                new tUserLoginBL().saveData(_tUserLoginData);

                finish();
                Intent myIntent = new Intent(Login.this, Home.class);
                startActivity(myIntent);
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                etTxtPass.requestFocus();
                tilPass.requestFocus();
                tilPass.setError(strMessage);
            }
            progressDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }
    }

    @Override
    public void onConnectionSlow(boolean info) {

    }

    private class MyAdapter extends ArrayAdapter<String> {

        List<String> arrObject;
        Context context;

        MyAdapter(Context context, int textViewResourceId, List<String> objects) {
            super(context, textViewResourceId, objects);
            arrObject = new ArrayList<>();
            arrObject = objects;
            this.context = context;
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            return getCustomView(position, parent);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            return getCustomView(position, parent);
        }

        View getCustomView(int position, ViewGroup parent) {
            LayoutInflater inflater = Login.this.getLayoutInflater();
            View row = inflater.inflate(R.layout.custom_spinner, parent, false);
            TextView label = (TextView) row.findViewById(R.id.tvTitle);
            label.setText(arrObject.get(position));
            TextView sub = (TextView) row.findViewById(R.id.tvDesc);
            sub.setVisibility(View.INVISIBLE);
            sub.setVisibility(View.GONE);
            row.setBackgroundColor(Color.TRANSPARENT);
            return row;
        }
    }

    public void setHubConnection(WMSMobileService.mHubConnectionSevice hubConnection) {
        WMSMobileService.mHubConnectionSevice = hubConnection;
    }

    public void HubConnectionSlow(WMSMobileService.mHubConnectionSlow mHubConnectionSlow) {
        WMSMobileService.mHubConnectionSlow = mHubConnectionSlow;
    }

    ProgressDialog mProgressDialog;

    private class DownloadTask extends AsyncTask<String, Integer, String> {
        private Context context;
        private PowerManager.WakeLock mWakeLock;

        DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                String txtPath = new clsHardCode().txtPathUserData;
                File mediaStorageDir = new File(txtPath);
                // Create the storage directory if it does not exist
                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        return null;
                    }
                }
                output = new FileOutputStream(txtPath + "Android_WMS Mobile_AND.2017.002.apk");

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

//        int intProcesscancel = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.dismiss();
            if (result != null)
                new clsMainActivity().showToast(context, "Download error: " + result);
            else {
                new clsMainActivity().showToast(context, "File downloaded");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String txtPath = new clsHardCode().txtPathUserData + "Android_WMS Mobile_AND.2017.002.apk";
                intent.setDataAndType(Uri.fromFile(new File(txtPath)), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onStart() {
        setHubConnection(this);
        HubConnectionSlow(this);
        super.onStart();
    }

    @Override
    protected void onResume() {
        setHubConnection(this);
        MyApplication.getInstance().setConnectivityListener(this);
        new clsMainActivity().checkConnection(coordinatorLayout, conMan);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
