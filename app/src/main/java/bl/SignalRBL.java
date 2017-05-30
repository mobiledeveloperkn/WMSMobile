package bl;

import service.WMSMobileService;

/**
 * Created by ASUS ZE on 19/12/2016.
 */

public class SignalRBL extends clsMainBL {

    public boolean buildingConnection() {
//        Intent intent = new Intent();
//        intent.setClass(context, className);
//        context.bindService(intent, connection, context.BIND_AUTO_CREATE);
        boolean report;
        report = new WMSMobileService().startSignalR();
        return report;
    }
}
