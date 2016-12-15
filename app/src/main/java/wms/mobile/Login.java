package wms.mobile;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bl.mRoleBL;
import bl.tUserLoginBL;
import library.common.mRoleData;
import library.dal.clsHardCode;
import service.SignalRService;

import static wms.mobile.R.id.txtLoginEmail;

/**
 * Created by ASUS ZE on 15/11/2016.
 */

public class Login extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    Button btn_login;
    Intent intent;
    EditText etTxtEmail, etTxtPass;
    Spinner spnRole;
    String txtEmail, txtPass, selectedRole;
    private List<String> arrrole, arrNodata;
    private HashMap<String, String> HMRole = new HashMap<String, String>();
    private final Context mContext = this;
    private SignalRService mService;
    private boolean mBound = false;
    MyReceiver myReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        btn_login = (Button) findViewById(R.id.buttonLogin);
        etTxtEmail = (EditText) findViewById(txtLoginEmail);
        etTxtPass = (EditText) findViewById(R.id.editTextPass);
        spnRole = (Spinner) findViewById(R.id.spnRole);

        arrNodata = new ArrayList<>();
        arrNodata.add("-");

        btn_login.setOnClickListener(this);
        ImageView imgBanner = (ImageView) findViewById(R.id.header);
        imgBanner.setAdjustViewBounds(true);
        imgBanner.setScaleType(ImageView.ScaleType.CENTER_CROP);

        etTxtEmail.setOnKeyListener(this);

        spnRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedRole = spnRole.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLogin:
                txtPass = etTxtPass.getText().toString();
                sendMessage(view);
                break;

        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        switch (view.getId()) {
            case R.id.txtLoginEmail:
                // If the event is a key-down event on the "enter" button
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i == KeyEvent.KEYCODE_ENTER)) {
                    txtEmail = etTxtEmail.getText().toString();
                    sendMessage(view);
                    return true;
                }
        }
        return false;
    }

    private class AsyncCallRole extends AsyncTask<List<mRoleData>, Void, List<mRoleData>> {

        @Override
        protected List<mRoleData> doInBackground(List<mRoleData>... lists) {
            List<mRoleData> roledata = new ArrayList<>();
            try {
                roledata = new mRoleBL().getRole(txtEmail);

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return roledata;
        }

        private ProgressDialog Dialog = new ProgressDialog(Login.this);

        @Override
        protected void onCancelled() {
            Dialog.dismiss();
            new clsMainActivity().showCustomToast(Login.this, new clsHardCode().txtMessCancelRequest, false);
        }

        @Override
        protected void onPostExecute(List<mRoleData> mRoleDatas) {
            arrrole = new ArrayList<>();
            arrrole = arrNodata;
            if (mRoleDatas != null) {
                if (mRoleDatas.size() > 0) {
                    if (mRoleDatas.get(0).getIntRoleId().equals("0") && mRoleDatas.get(0).getIntRoleId().equals("0") && mRoleDatas.get(0).getIntRoleId().equals("0")) {
                        new clsMainActivity().showCustomToast(Login.this, mRoleDatas.get(0).getTxtRoleName(), false);
                        arrrole = arrNodata;
                        spnRole.setAdapter(new MyAdapter(Login.this, R.layout.custom_spinner, arrrole));
                        spnRole.setEnabled(false);
                        etTxtEmail.requestFocus();
                    } else {
                        arrrole = new ArrayList<String>();
                        for (mRoleData dt : mRoleDatas) {
                            arrrole.add(dt.getTxtRoleName());
                            HMRole.put(dt.getTxtRoleName(), dt.getIntRoleId());
                        }
                        spnRole.setAdapter(new MyAdapter(Login.this, R.layout.custom_spinner, arrrole));
                        spnRole.setEnabled(true);
                    }
                } else {
                    new clsMainActivity().showCustomToast(Login.this, new clsHardCode().txtMessNetworkOffline, false);
                    spnRole.setAdapter(new MyAdapter(Login.this, R.layout.custom_spinner, arrrole));
                    etTxtEmail.requestFocus();
                }

            } else if (mRoleDatas == null) {
                new clsMainActivity().showCustomToast(Login.this, new clsHardCode().txtMessNoData, false);
                spnRole.setAdapter(new MyAdapter(Login.this, R.layout.custom_spinner, arrrole));
                etTxtEmail.requestFocus();
            } else {
                new clsMainActivity().showCustomToast(Login.this, new clsHardCode().txtMessNetworkOffline, false);
                spnRole.setAdapter(new MyAdapter(Login.this, R.layout.custom_spinner, arrrole));
                etTxtEmail.requestFocus();
            }
            Dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            Dialog.setMessage(new clsHardCode().txtMessGetUserRole);
            Dialog.setCancelable(false);
            Dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    etTxtEmail.requestFocus();
                }
            });
            Dialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Dialog.dismiss();
        }
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

    private class AsyncCallLogin extends AsyncTask<JSONArray, Void, JSONArray> {
        String dummy;

        @Override
        protected JSONArray doInBackground(JSONArray... jsonArrays) {
            JSONArray Json = null;
            String nameRole = selectedRole;
            String intUserID = "114";
            try {
                dummy = new tUserLoginBL().Login(String.valueOf(txtEmail), String.valueOf(txtPass), HMRole.get(nameRole), intUserID);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return Json;
        }

        private ProgressDialog Dialog = new ProgressDialog(Login.this);

        @Override
        protected void onCancelled() {
            Dialog.dismiss();
            new clsMainActivity().showCustomToast(Login.this, new clsHardCode().txtMessCancelRequest, false);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if (dummy.equals("1")) {
                intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                finish();
            } else {
                new clsMainActivity().showCustomToast(getApplicationContext(), new clsHardCode().txtMessWrongPass, false);
                etTxtPass.requestFocus();
                Dialog.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            Dialog.setMessage(new clsHardCode().txtMessLogin);
            Dialog.setCancelable(false);
            Dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    etTxtPass.requestFocus();
                }
            });
            Dialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Dialog.dismiss();
        }
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

    @Override
    protected void onStart() {

        //Register BroadcastReceiver
        //to receive event from our service
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SignalRService.BROADCAST);
        registerReceiver(myReceiver, intentFilter);

        Intent intent = new Intent();
        intent.setClass(mContext, SignalRService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

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

    public void sendMessage(View view) {
        String nameRole = selectedRole;
        Context context = getApplicationContext(); // or activity.getApplicationContext()
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();

        String myVersionName = "not available"; // initialize String

        try {
            myVersionName = packageManager.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (mBound) {
            // Call a method from the SignalRService.
            // However, if this call were something that might hang, then this request should
            // occur in a separate thread to avoid slowing down the activity performance.
            if (txtEmail.length() > 0) {
                mService.getRole(txtEmail, myVersionName);
            }
        } else {
            Intent intent = new Intent();
            intent.setClass(mContext, SignalRService.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            JSONObject jsonObject = null;
            JSONArray jsonArray = null;
            String strMethodName, strMessage, boolValid, intRoleId, txtRoleName, dtInsert, dtUpdated;
            arrrole = new ArrayList<>();
            arrrole = arrNodata;

            String datapassed = intent.getStringExtra("DATA_PASSED");
            Toast.makeText(Login.this, String.valueOf(datapassed), Toast.LENGTH_SHORT).show();

            try {
                jsonObject = new JSONObject(datapassed);

                jsonArray = jsonObject.getJSONArray("A");

                boolValid = (String) jsonArray.get(0);
                strMessage = (String) jsonArray.get(1);
                strMethodName = (String) jsonArray.get(2);

                String array = jsonArray.toString().replace("\\","");

                jsonObject = new JSONObject(array);

                JSONArray jArray= jsonObject.getJSONArray("A");
//                Iterator iterator = jArray.iterator();

//                while(iterator.hasNext()){
//                    JSONObject innerObj = (JSONObject) iterator.next();
//                    Long result=(Long) innerObj.get("boolValid");
//                    strMethodName=(String) innerObj.get("strMethodName");
//                    strMessage=(String) innerObj.get("strMessage");
//
//                    if(result==1){
//                        JSONObject arrayData = innerObj.getJSONObject("listOfmUserRole");
//                        intRoleId = arrayData.getString("intRoleId");
//                        txtRoleName = arrayData.getString("txtRoleName");
//                    }
//                }

                for(int i = 0 ; i<jArray.length();i++){
                    JSONObject status = jsonArray.getJSONObject(i);

                    boolValid = status.getString("boolValid");
                    strMessage = status.getString("strMessage");
                    strMethodName = status.getString("strMethodName");

                    JSONObject arrayData = status.getJSONObject("listOfmUserRole");
                    intRoleId = arrayData.getString("intRoleId");
                    txtRoleName = arrayData.getString("txtRoleName");

                    arrrole.add(txtRoleName);
                    HMRole.put(txtRoleName,intRoleId);

                }
                spnRole.setAdapter(new MyAdapter(Login.this, R.layout.custom_spinner, arrrole));
                spnRole.setEnabled(true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
