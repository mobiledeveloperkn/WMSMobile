package wms.mobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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

import org.json.JSONArray;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bl.mRoleBL;
import bl.tUserLoginBL;
import library.common.mRoleData;
import library.dal.clsHardCode;

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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonLogin :
                txtPass = etTxtPass.getText().toString();
                AsyncCallLogin task = new AsyncCallLogin();
                task.execute();
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
}
