package wms.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import bl.mSPMHeaderBL;
import library.common.mSPMHeaderData;

/**
 * Created by ASUS ZE on 16/11/2016.
 */

public class OutstandingTask extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Button btnTaskView;
    TextView tvNoSPM, tvShopTo, tvSO;
    mSPMHeaderData _mSPMHeaderData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outstanding_task);
        btnTaskView = (Button) findViewById(R.id.btn_taskview);
        tvNoSPM = (TextView) findViewById(R.id.tv_noSPM);
        tvShopTo = (TextView) findViewById(R.id.tv_shipTo);
        tvSO = (TextView) findViewById(R.id.tv_SO);

        _mSPMHeaderData = new mSPMHeaderData();
        _mSPMHeaderData = new mSPMHeaderBL().GetAllData();

        tvNoSPM.setText(_mSPMHeaderData.getTxtNoSPM());
        tvShopTo.setText(_mSPMHeaderData.getTxtBranchCode());
        tvSO.setText(_mSPMHeaderData.getTxtSalesOrder());

        btnTaskView.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        setupViewPager(viewPager);

//        tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_taskview :
                startActivity(new Intent(OutstandingTask.this, TabsTaskHeader.class));
                break;
        }
    }
}
