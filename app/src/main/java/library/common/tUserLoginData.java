package library.common;

/**
 * Created by ASUS ZE on 16/11/2016.
 */

public class tUserLoginData {

    public tUserLoginData(String IntUserId, String txtUserName, String txtPassword, String IntUserRole, String txtRoleName) {
        this.IntUserId = IntUserId;
        this.txtUserName = txtUserName;
        this.txtPassword = txtPassword;
        this.IntUserRole = IntUserRole;
        this.txtRoleName = txtRoleName;
    }

    public tUserLoginData() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getTxtUserName() {
        return txtUserName;
    }

    public void setTxtUserName(String txtUserName) {
        this.txtUserName = txtUserName;
    }

    public String getTxtRoleName() {
        return txtRoleName;
    }

    public void setTxtRoleName(String txtRoleName) {
        this.txtRoleName = txtRoleName;
    }

    public String getTxtPassword() {
        return txtPassword;
    }

    public void setTxtPassword(String txtPassword) {
        this.txtPassword = txtPassword;
    }

    public String getIntUserRole() {
        return IntUserRole;
    }

    public void setIntUserRole(String intUserRole) {
        IntUserRole = intUserRole;
    }

    public String getIntUserId() {
        return IntUserId;
    }

    public void setIntUserId(String intUserId) {
        IntUserId = intUserId;
    }


    public String getDtLastLogin() {
        return dtLastLogin;
    }

    public void setDtLastLogin(String dtLastLogin) {
        this.dtLastLogin = dtLastLogin;
    }

    private String IntUserId;
    private String txtUserName;
    private String txtPassword;
    private String IntUserRole;
    private String txtRoleName;
    private  String dtLastLogin;

    public String Property_IntUserId = "IntUserId";
    public String Property_txtUserName = "txtUserName";
    public String Property_txtPassword = "txtPassword";
    public String Property_IntUserRole = "IntUserRole";
    public String Property_txtRoleName = "txtRoleName";
    public String Property_dtLastLogin = "dtLastLogin";

    public String Property_All=Property_IntUserId+","+Property_txtUserName+","+Property_txtPassword+","+Property_IntUserRole+","+Property_txtRoleName+","+Property_dtLastLogin;
}
