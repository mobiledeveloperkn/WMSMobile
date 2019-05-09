package addon;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by ASUS ZE on 26/12/2016.
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;
    private boolean isFinishInsert;
    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public boolean isFinishInsert() {
        return isFinishInsert;
    }

    public void setFinishInsert(boolean finishInsert) {
        isFinishInsert = finishInsert;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
    public void unsetConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = null;
    }
    public void setSignalRListener(ConnectivityReceiver.SignalRReceiverListener listener) {
        ConnectivityReceiver.signalRReceiverListener = listener;
    }
}
