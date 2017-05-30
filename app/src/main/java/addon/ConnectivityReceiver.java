package addon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ASUS ZE on 26/12/2016.
 */

public class ConnectivityReceiver
        extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;
    public static SignalRReceiverListener signalRReceiverListener;

    public ConnectivityReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent arg1) {

        arg1.getFlags();

        String broadcastName = arg1.getAction().substring(0);

        if(broadcastName.equalsIgnoreCase("android.net.conn.CONNECTIVITY_CHANGE")){
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null
                    && activeNetwork.isConnectedOrConnecting();

            if (connectivityReceiverListener != null) {
                connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
            }
        } else if (broadcastName.equalsIgnoreCase("BROADCAST_GETROLE")){

            String datapassed = arg1.getStringExtra("DATA_PASSED_GETROLE");
            boolean valid = datapassed!=null;
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(datapassed);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (signalRReceiverListener != null&&valid) {
                signalRReceiverListener.onMessageReceive(jsonObject);
            }
        } else if (broadcastName.equalsIgnoreCase("BROADCAST_LOGIN")){
            String datapassed = arg1.getStringExtra("DATA_PASSED_LOGIN");
            boolean valid = datapassed!=null;
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(datapassed);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (signalRReceiverListener != null&&valid) {
                signalRReceiverListener.onMessageReceive(jsonObject);
            }
        }
    }

    public static boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) MyApplication.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }

    public interface SignalRReceiverListener {
        void onMessageReceive(JSONObject jsonObject);
    }
}
