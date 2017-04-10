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
import android.os.Environment;
import android.os.PowerManager;
import android.os.StrictMode;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
    private HashMap<String, String> HMRole = new HashMap<String, String>();
    private CoordinatorLayout coordinatorLayout;
    ConnectivityManager conMan;
    TextInputLayout tilEmail, tilPass;
    TextView txtInfo;

    private PackageInfo pInfo = null;
    private String versionName = "";
    private tUserLoginData dataLogin;
    private ProgressDialog progressDialog;
    private long time = 15000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayoutLogin);

//        List<clsUserLogin> clsUserLoginList = new clsUserLoginBL(getApplicationContext()).getAllData();

        btn_login = (Button) findViewById(R.id.buttonLogin);
        btn_ping = (Button) findViewById(R.id.buttonPing);
        btn_exit = (Button) findViewById(R.id.buttonExit);
        etTxtEmail = (EditText) findViewById(R.id.txtLoginEmail);
        etTxtPass = (TextInputEditText) findViewById(R.id.editTextPass);
        spnRole = (Spinner) findViewById(R.id.spnRole);
        tilEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        tilPass = (TextInputLayout) findViewById(R.id.input_layout_pass);
        txtInfo = (TextView) findViewById(R.id.txtVersionLogin);

        ImageView imgBanner = (ImageView) findViewById(R.id.header);
        imgBanner.setAdjustViewBounds(true);
        imgBanner.setScaleType(ImageView.ScaleType.CENTER_CROP);

        etTxtEmail.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        SQLiteDatabase db = new clsMainBL().getDb();
        mconfigDA _mconfigDA = new mconfigDA(db);

        mconfigData dataAPI = _mconfigDA.getData(db, enumConfigData.ApiKalbe.getidConfigData());
        txtInfo.setText(dataAPI.get_txtValue());

        pInfo= new clsMainActivity().getPinfo(this);

        if(pInfo!=null){
            versionName = pInfo.versionName;
        }

        requestCheckVersion(pInfo.versionName);
        new tDeviceInfoUserBL().SaveInfoDevice("","", versionName);

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

        //bikin progres dialognya
        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Loading... Please Wait");
        progressDialog.setIndeterminate(false); //ukur berapa persen, false maka not do
        progressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLogin:
                txtPass = etTxtPass.getText().toString();
                sendRequestLogin(view);
                break;
            case R.id.buttonPing:
                sendRequestPing();
                break;
            case R.id.buttonExit:
//                backupDatabase("a");
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
                        etTxtEmail.requestFocus();
                        tilEmail.requestFocus();
                        tilEmail.setError("Username cannot empty");
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
        return false;
    }

    private void requestCheckVersion(String versionName) {
//        progressDialog.show();
        boolean status = false;
        if (versionName != null) {
            status = new WMSMobileService().getDataLastVersion(versionName);
            if (!status) {
                new clsMainActivity().checkConnection(coordinatorLayout, conMan);
            }
        }
    }

    public void sendRequestGetRole() {
        boolean status;
        if (txtEmail.length() > 0) {
            progressDialog.show();
            new clsMainActivity().timerDelayRemoveDialog(time, progressDialog);
            status = new WMSMobileService().getRole(txtEmail, versionName);
            if (!status) {
                progressDialog.dismiss();
                new clsMainActivity().checkConnection(coordinatorLayout, conMan);
                etTxtEmail.requestFocus();
            }
        } else {
            etTxtEmail.requestFocus();
            new clsMainActivity().showSnackbar(coordinatorLayout, "Username cannot empty", false, "");
        }
    }

    private void sendRequestLogin(View view) {
        String nameRole = selectedRole;
        boolean status;

        if (txtPass.length() > 0) {
            progressDialog.show();
            new clsMainActivity().timerDelayRemoveDialog(time, progressDialog);
            status = new WMSMobileService().login(txtEmail, txtPass, HMRole.get(nameRole), versionName, dataLogin.getIntUserId());
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
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initMethodCheckinVersion(JSONObject jsonObject) {
        String strMethodName, strMessage, boolValid, intRoleId, txtRoleName, dtInsert, dtUpdated, txtLink;
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
                    new clsMainActivity().showCustomToast(getApplicationContext(), strMessage, true);
                    File dir = new File(new clsHardCode().txtPathUserData);
                    if (dir.isDirectory())
                    {
                        String[] children = dir.list();
                        for (int i = 0; i < children.length; i++)
                        {
                            new File(dir, children[i]).delete();
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initSpinnerRole(JSONObject jsonObject) {

        String strMethodName, strMessage, boolValid, intRoleId, txtRoleName, dtInsert, dtUpdated, intUserId;
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
                    tilEmail.setError(null);
                    tilPass.setError(null);
                } else {
                    tilEmail.setError("Username not Valid");
                    spnRole.setAdapter(new MyAdapter(getApplicationContext(), R.layout.custom_spinner, arrrole));
                    etTxtEmail.requestFocus();
                }
                spnRole.setAdapter(new MyAdapter(getApplicationContext(), R.layout.custom_spinner, arrrole));
                spnRole.setEnabled(true);
                etTxtPass.requestFocus();
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

        String strMethodName, strMessage, boolValid, intRoleId, txtRoleName, dtInsert, dtUpdated;

        try {
            boolValid = jsonObject.get("boolValid").toString();
            strMessage = jsonObject.get("strMessage").toString();
            strMethodName = jsonObject.get("strMethodName").toString();

            if (boolValid.equalsIgnoreCase("true")) {
                JSONObject jsonObjectLogin = jsonObject.getJSONObject("listOftUserLogin");

                tUserLoginData _tUserLoginData = new tUserLoginData();

                _tUserLoginData.setIntUserId(jsonObjectLogin.get("USER_ID").toString());
                _tUserLoginData.setTxtUserName(jsonObjectLogin.get("USER_NAME").toString());
                _tUserLoginData.setIntUserRole(jsonObjectLogin.get("ROLE_ID").toString());
                _tUserLoginData.setTxtRoleName(jsonObjectLogin.get("ROLE_NAME").toString());
                _tUserLoginData.setDtLastLogin(jsonObjectLogin.get("CREATION_DATE").toString());
                _tUserLoginData.setTxtDataId(jsonObjectLogin.getString("DATA_ID").toString());

                if (jsonObject.isNull("listOfmSPMHeader")) {
                } else {
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
                        _mSPMHeaderData.setBitStart("1");

                        new mSPMHeaderBL().saveData(_mSPMHeaderData);

                        JSONArray jsonArraySPMDetail = jsonObject.getJSONArray("listOfmSPMDetail");

                        List<mSPMDetailData> _mSPMDetailData = new ArrayList<>();

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
//                clsUserLogin login = new clsUserLogin(_tUserLoginData.getIntUserId(),_tUserLoginData.getTxtUserName(),_tUserLoginData.getIntUserRole(),_tUserLoginData.getTxtRoleName(),_tUserLoginData.getDtLastLogin(),_tUserLoginData.getTxtDataId());
//                new clsUserLoginBL(getApplicationContext()).saveDataLogin(login);

                finish();
                Intent myIntent = new Intent(Login.this, Home.class);
                startActivity(myIntent);
            } else {
                etTxtPass.requestFocus();
                tilPass.requestFocus();
                tilPass.setError(strMessage);
            }
            progressDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }
//        catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onConnectionSlow(boolean info) {

    }

    public class MyAdapter extends ArrayAdapter<String> {

        List<String> arrObject;
        Context context;

        public MyAdapter(Context context, int textViewResourceId, List<String> objects) {
            super(context, textViewResourceId, objects);
            arrObject = new ArrayList<>();
            arrObject = objects;
            this.context = context;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = Login.this.getLayoutInflater();
            View row = inflater.inflate(R.layout.custom_spinner, parent, false);
            TextView label = (TextView) row.findViewById(R.id.tvTitle);
            label.setText(arrObject.get(position));
            TextView sub = (TextView) row.findViewById(R.id.tvDesc);
            sub.setVisibility(View.INVISIBLE);
            sub.setVisibility(View.GONE);
            row.setBackgroundColor(new Color().TRANSPARENT);
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

        public DownloadTask(Context context) {
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
                output = new FileOutputStream(txtPath + "Android_WMSMobile_2.0.apk");

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

        int intProcesscancel = 0;

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
                String txtPath = new clsHardCode().txtPathUserData + "Android_WMSMobile_2.0.apk";
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
    public void backupDatabase(String databaseName) {
        try {
            File sd = new File(Environment.getExternalStorageDirectory()+File.separator+"wmsMobile"+File.separator);
            File data = Environment.getDataDirectory();

            if(!sd.exists()&&!sd.isDirectory()){
                sd.mkdirs();
            }
            if (sd.canWrite()) {
                String currentDBPath = "//data//"+getPackageName()+"//databases//"+"wmsmobile.db"+"";
                String dateTime = DateFormat.getDateTimeInstance().format(new Date());
                String uuid = UUID.randomUUID().toString();
                String backupDBPath = uuid;
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Toast.makeText(getApplicationContext(), "backup created", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
