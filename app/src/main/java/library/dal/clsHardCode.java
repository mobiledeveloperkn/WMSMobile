package library.dal;

import android.os.Environment;

import java.io.File;

/**
 * Created by ASUS ZE on 15/11/2016.
 */

public class clsHardCode  {
    public String txtPathApp= Environment.getExternalStorageDirectory()+ File.separator+"Android"+File.separator+"data"+File.separator+"WMSMoblie"+File.separator+"app_database"+File.separator;
    public String txtDatabaseName=txtPathApp+"WMSMobile";
    public String txtTable_mConfig = "mconfig";
    public String txtTable_mUser = "tUserLogin";
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


}
