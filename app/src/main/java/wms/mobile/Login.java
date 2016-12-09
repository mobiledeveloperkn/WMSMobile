package wms.mobile;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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

import static wms.mobile.R.id.txtLoginEmail;

/**
 * Created by ASUS ZE on 15/11/2016.
 */

public class Login extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener{

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

    // private fields
    HubConnection connection;
    HubProxy hub;
    ClientTransport transport;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Logger logger = new Logger() {
            @Override
            public void log(String message, LogLevel logLevel) {
                Log.e("SignalR", message);
            }
        };

        Platform.loadPlatformComponent(new AndroidPlatformComponent());
        connection = new HubConnection("http://10.171.11.47/wms%20online");
        hub = connection.createHubProxy("hubAPI"); // case insensitivity

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
                        if (jsonObject!=null){
                            try {
                                JSONObject jsonObj = new JSONObject(String.valueOf(jsonObject));

                                // Getting JSON Array node
                                JSONArray contacts = jsonObj.getJSONArray("A");

                                String validation = (String) contacts.get(0);
                                String message = (String) contacts.get(1);

                                if(validation.equals("0")){
                                    new clsMainActivity().showCustomToast(getApplicationContext(), message, false);

                                } else if (validation.equals("1")){
                                    intent = new Intent(getApplicationContext(), Home.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    new clsMainActivity().showCustomToast(getApplicationContext(), new clsHardCode().txtMessNoData, false);
                                }
//
//                                listItems = new ArrayList<String>();
//
//                                listItems.add(name + " : " + message);
//                                itemsAdapter.notifyDataSetChanged();
//                                mListView.invalidateViews();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

//                        String message
                        Log.e("<Debug>", "response = " + jsonObject.toString());
//                        Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_LONG).show();

                    }
                });
            }
        });

        btn_login = (Button) findViewById(R.id.buttonLogin);
        etTxtEmail = (EditText) findViewById(txtLoginEmail);
        etTxtPass = (EditText) findViewById(R.id.editTextPass);
        spnRole = (Spinner) findViewById(R.id.spnRole);

        arrNodata = new ArrayList<>();
        arrNodata.add("-");

        btn_login.setOnClickListener(this);
        ImageView imgBanner = (ImageView) findViewById(R.id.header) ;
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

        Intent intent = new Intent();
        intent.setClass(mContext, SignalRService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonLogin :
                txtPass = etTxtPass.getText().toString();
                sendMessage(view);
//                AsyncCallLogin task = new AsyncCallLogin();
//                task.execute();
                break;

        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        switch (view.getId()){
            case  R.id.txtLoginEmail :
                // If the event is a key-down event on the "enter" button
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i == KeyEvent.KEYCODE_ENTER)) {
//                    intProcesscancel = 0;
                    txtEmail = etTxtEmail.getText().toString();
                    AsyncCallRole task = new AsyncCallRole();
                    task.execute();
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
            if (mRoleDatas!=null) {
                if(mRoleDatas.size()>0){
                    if(mRoleDatas.get(0).getIntRoleId().equals("0")&&mRoleDatas.get(0).getIntRoleId().equals("0")&&mRoleDatas.get(0).getIntRoleId().equals("0")){
                        new clsMainActivity().showCustomToast(Login.this, mRoleDatas.get(0).getTxtRoleName(), false);
                        arrrole=arrNodata;
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

            } else if (mRoleDatas==null){
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

    private class AsyncCallLogin extends AsyncTask<JSONArray, Void, JSONArray>{
        String dummy;
        @Override
        protected JSONArray doInBackground(JSONArray... jsonArrays) {
            JSONArray Json=null;
            String nameRole = selectedRole;
            String intUserID = "114";
            try {
                dummy = new tUserLoginBL().Login(String.valueOf(txtEmail), String.valueOf(txtPass), HMRole.get(nameRole), intUserID);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return  Json;
        }
        private ProgressDialog Dialog = new ProgressDialog(Login.this);

        @Override
        protected void onCancelled() {
            Dialog.dismiss();
            new clsMainActivity().showCustomToast(Login.this, new clsHardCode().txtMessCancelRequest, false);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if(dummy.equals("1")){
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

    public void sendMessage(View view) {
        String nameRole = selectedRole;
        if (mBound) {
            // Call a method from the SignalRService.
            // However, if this call were something that might hang, then this request should
            // occur in a separate thread to avoid slowing down the activity performance.
            if (txtEmail.length()>0 && txtPass.length()>0) {
                mService.login(txtEmail, txtPass);
            }
        }
    }
}
