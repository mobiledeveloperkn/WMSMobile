package wms.mobile;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import bl.clsMainBL;
import bl.mSPMHeaderBL;
import jim.h.common.android.lib.zxing.config.ZXingLibConfig;
import jim.h.common.android.lib.zxing.integrator.IntentIntegrator;
import jim.h.common.android.lib.zxing.integrator.IntentResult;
import library.common.mSPMHeaderData;
import library.dal.mSPMDetailDA;
import library.dal.mSPMHeaderDA;

public class Home extends AppCompatActivity implements View.OnClickListener {

    Button btnOutstandingTask, btnScan;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ZXingLibConfig zxingLibConfig;

    mSPMHeaderData _mSPMHeaderData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnOutstandingTask = (Button) findViewById(R.id.btn_outstanding);
        btnScan = (Button) findViewById(R.id.btn_scan);

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
        getSupportActionBar().setElevation(0);

        btnOutstandingTask.setOnClickListener(this);
        btnScan.setOnClickListener(this);

        _mSPMHeaderData = new mSPMHeaderData();
        _mSPMHeaderData = new mSPMHeaderBL().GetAllData();

        if (_mSPMHeaderData.getIntSPMId()==null){
            btnOutstandingTask.setEnabled(false);
            btnScan.setEnabled(true);
        } else if (_mSPMHeaderData.getIntSPMId()!=null){
            btnOutstandingTask.setEnabled(true);
            btnScan.setEnabled(false);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_outstanding :
                startActivity(new Intent(Home.this, OutstandingTask.class));
                break;
            case  R.id.btn_scan :
                IntentIntegrator.initiateScan(this, zxingLibConfig);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SQLiteDatabase db = new clsMainBL().getDb();

        if(requestCode==IntentIntegrator.REQUEST_CODE){
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scanResult == null) {
                return;
            }
            final String result = scanResult.getContents();
            new clsMainActivity().showCustomToast(getApplicationContext(), result, true);
            mSPMHeaderDA _mSPMHeaderDA = new mSPMHeaderDA(db);
            mSPMDetailDA _mSPMDetailDA = new mSPMDetailDA(db);
            int sumdata_mSPMHeaderDA = _mSPMHeaderDA.getContactsCount(db);
		    if (sumdata_mSPMHeaderDA == 0) {
			    _mSPMHeaderDA.InsertDefaultSPMHeader(db);
                _mSPMDetailDA.InsertDefaultmSPMDetail(db);
                startActivity(new Intent(Home.this, OutstandingTask.class));
		}
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        _mSPMHeaderData = new mSPMHeaderData();
        _mSPMHeaderData = new mSPMHeaderBL().GetAllData();

        if (_mSPMHeaderData.getIntSPMId()==null){
            btnOutstandingTask.setEnabled(false);
            btnScan.setEnabled(true);
        } else if (_mSPMHeaderData.getIntSPMId()!=null){
            btnOutstandingTask.setEnabled(true);
            btnScan.setEnabled(false);
        }
    }
}
