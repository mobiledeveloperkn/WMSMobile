package library.dal;

import android.os.Environment;

import java.io.File;

/**
 * Created by ASUS ZE on 15/11/2016.
 */

public class clsHardCode  {
    public String txtPathApp= Environment.getExternalStorageDirectory()+ File.separator+"Android"+File.separator+"data"+File.separator+"WMSMobile"+File.separator+"app_database"+File.separator;
    public String txtPathUserData = Environment.getExternalStorageDirectory() + File.separator + "Android" + File.separator + "data" + File.separator + "WMSMobile" + File.separator + "user_data" + File.separator;
    public String txtDatabaseName=txtPathApp+"WMSMobile";
    public String txtTable_mConfig = "mconfig";
    public String txtTable_mSystemConfig = "mSystemConfig";
    public String txtTable_mUser = "tUserLogin";
    public String txtTable_tTimerLog = "tTimerLog";
    public String txtTable_mUserRole = "mUserRole";
    public String txtTable_mSPMHeader = "mSPMHeader";
    public String txtTable_mSPMDetail = "mSPMDetail";
    public String txtTokenAPI = "129195202189197196195202189175";
    public String txtMessCancelRequest = "Canceled Request Data";
    public String txtMessNetworkOffline = "Please Check Your Network!!";
    public String txtMessNoData = "Data Not Found..";
    public String txtMessGetUserRole = "Getting User Role..";
    public String txtMessWrongPass = "Password is Incorect...";
    public String txtMessLogin = "Login..";
    public String txtServerHubName = "hubAPI";
    public String txtMethodServerGetRole = "getRoleByUsername";
    public String txtMethodServerGetVersionName = "getDataLastVersion";
    public String txtMethodServerLogin = "login";
    public String txtMethodServerLogout = "logout";
    public String txtMethodServerGetNoSPM = "getDataSPM";
    public String txtMethodServerRefreshDataSTAR = "RefreshDataSTAR";
    public String txtMethodServerCheckWaitingDataSTAR = "checkWaitingDataSTAR";
    public String txtMethodServerConfirmSPMDetail = "confirmSPMDetail";
    public String txtMethodServerUndoCancelSPMDetail = "revertCancelSPMDetail";
    public String txtMethodServerConfirmSPMHeader = "confirmSPMHeader";
    public String txtMethodServerRefreshSPMHeader = "refreshSPMHeader";
    public String txtMethodPushData = "pushDataOffline";
    public String txtMethodServerCancelSPMDetail = "cancelSPMDetail";
    public String txtTable_tDisplayPicture = "tDisplayPicture";
    public String txtTable_tDeviceInfoUser = "tDeviceInfoUser";
    public String txtUpdateConnectionId = "updateConnectionId";
    public String txtGetLatestSTAR = "getLatestSTAR";
}
