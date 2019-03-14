package wms.mobile;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;

import bl.mSPMDetailBL;
import library.common.mSPMDetailData;
import library.dal.clsHardCode;

public class TaskDetail extends AppCompatActivity implements View.OnClickListener {

    Button btnConfirm;
    TextView tvLocator, tvItemCode, tvItemName;
    mSPMDetailData _mSPMDetailData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        btnConfirm = findViewById(R.id.btn_confirm);
        tvLocator = findViewById(R.id.tv_locator);
        tvItemCode = findViewById(R.id.tv_itemcode);
        tvItemName = findViewById(R.id.tv_itemname);

        btnConfirm.setOnClickListener(this);

        _mSPMDetailData = new mSPMDetailBL().getData(1);
        tvLocator.setText(_mSPMDetailData.getTxtLocator());
        tvItemCode.setText(_mSPMDetailData.getTxtItemCode());
        tvItemName.setText(_mSPMDetailData.getTxtItemName());

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_confirm:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure want to confirm this?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        AsyncCallUpdateDetail task = new AsyncCallUpdateDetail();
                        task.execute();
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

//                AlertDialog alert = builder.create();
                builder.show();
                break;

            default:
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncCallUpdateDetail extends AsyncTask<JSONArray, Void, JSONArray> {
        String dummy;
        @Override
        protected JSONArray doInBackground(JSONArray... jsonArrays) {
            JSONArray Json=null;

            if(_mSPMDetailData.getBitStatus().equals("1") && _mSPMDetailData.getBitSync().equals("1")){
                dummy = "0";
            }
            else{
                dummy = "1";
            }

            return  Json;
        }
        private ProgressDialog Dialog = new ProgressDialog(TaskDetail.this);

        @Override
        protected void onCancelled() {
            Dialog.dismiss();
            new clsMainActivity().showCustomToast(TaskDetail.this, new clsHardCode().txtMessCancelRequest, false);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if(dummy.equals("1")){
                _mSPMDetailData.setBitStatus("1");
                _mSPMDetailData.setBitSync("1");
                new mSPMDetailBL().insert(_mSPMDetailData);
                new clsMainActivity().showCustomToast(getApplicationContext(), "Saved", true);
            } else {
                new clsMainActivity().showCustomToast(getApplicationContext(), "Not saved", false);
            }
            Dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            Dialog.setMessage(new clsHardCode().txtMessLogin);
            Dialog.setCancelable(false);
            Dialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Dialog.dismiss();
        }
    }
}
