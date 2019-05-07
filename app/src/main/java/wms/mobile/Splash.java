package wms.mobile;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;

import addon.ConnectivityReceiver;
import bl.clsMainBL;
import library.common.clsHelper;
import library.common.clsStatusMenuStart;
import library.dal.enumStatusMenuStart;
import service.WMSMobileService;

public class Splash extends AppCompatActivity {

    private TextView version;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    long delay = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.anim_layout);
        version = (TextView) findViewById(R.id.tv_version);

        try {
            version.setText(getPackageManager().getPackageInfo(getPackageName(), 0).versionName + " \u00a9 KN-IT");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            version.setText(e.toString());
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            IntentFilter filter = new IntentFilter();
//            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//            filter.addCategory("register");
//            registerReceiver(new ConnectivityReceiver(), filter);
////            valid = false;
//        }
//        else {
//            valid = true;
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        int hasWriteExternalStoragePermission =
                ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int hasReadExternalStoragePermission =
                ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int hasAccessFineLocationPermission =
                ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasAccessCameraPermission =
                ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.CAMERA);

        if (Build.VERSION.SDK_INT >= 23
                && hasWriteExternalStoragePermission != PackageManager.PERMISSION_GRANTED
                && hasReadExternalStoragePermission != PackageManager.PERMISSION_GRANTED
                && hasAccessFineLocationPermission != PackageManager.PERMISSION_GRANTED
                && hasAccessCameraPermission != PackageManager.PERMISSION_GRANTED
                ) {
            boolean checkPermission = checkPermission();

        } else if (Build.VERSION.SDK_INT >= 23
                && hasWriteExternalStoragePermission == PackageManager.PERMISSION_GRANTED
                && hasReadExternalStoragePermission == PackageManager.PERMISSION_GRANTED
                && hasAccessFineLocationPermission == PackageManager.PERMISSION_GRANTED
                && hasAccessCameraPermission != PackageManager.PERMISSION_GRANTED
                ){
            StartAnimations();
            taskIntent();

        }  else {
            StartAnimations();
            taskIntent();
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            IntentFilter filter = new IntentFilter();
//            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//            filter.addCategory("register");
//            registerReceiver(new ConnectivityReceiver(), filter);
////            valid = false;
//        }
    }

    private boolean checkPermission() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
        builder.setMessage("You need to allow access. . .");
        builder.setCancelable(false);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(Splash.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        && !ActivityCompat.shouldShowRequestPermissionRationale(Splash.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        &&!ActivityCompat.shouldShowRequestPermissionRationale(Splash.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        &&!ActivityCompat.shouldShowRequestPermissionRationale(Splash.this,
                        Manifest.permission.CAMERA)){
                    ActivityCompat.requestPermissions(Splash.this,
                            new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA},
                            REQUEST_CODE_ASK_PERMISSIONS);
                    dialog.dismiss();

                }
                ActivityCompat.requestPermissions(Splash.this,
                        new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA},
                        REQUEST_CODE_ASK_PERMISSIONS);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

        return true;
    }

    private void StartAnimations() {

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        TextView iv = (TextView) findViewById(R.id.iv_anim);
//        iv.setBackgroundResource(R.mipmap.ic_kalbe_phonegap);
        iv.clearAnimation();
        iv.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        anim.reset();
        ImageView iv2 = (ImageView) findViewById(R.id.imageView2);
//        iv.setBackgroundResource(R.mipmap.ic_kalbe_phonegap);
        iv2.clearAnimation();
        iv2.startAnimation(anim);
    }
    private void taskIntent() {


        Timer runProgress = new Timer();
        TimerTask viewTask = new TimerTask() {

            public void run() {
                Intent myIntent = new Intent(Splash.this, Login.class);
                clsHelper _clsHelper = new clsHelper();
                _clsHelper.createFolderApp();
                try {
                    _clsHelper.createDb();
                    clsStatusMenuStart _clsStatusMenuStart = new clsMainBL().checkUserActive();
                    if (_clsStatusMenuStart.get_intStatus() == enumStatusMenuStart.FormLogin) {
                        myIntent = new Intent(Splash.this, Login.class);
                    }  else if (_clsStatusMenuStart.get_intStatus() == enumStatusMenuStart.UserActiveLogin){
                        myIntent = new Intent(Splash.this, Home.class);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                finish();
                Intent service = new Intent(Splash.this, WMSMobileService.class);
                startService(service);
                startActivity(myIntent);
            }
        };
        runProgress.schedule(viewTask, delay);
    }
}
