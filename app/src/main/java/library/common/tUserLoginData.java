package library.common;

/**
 * Created by ASUS ZE on 16/11/2016.
 */

public class tUserLoginData {

    public tUserLoginData(String IntUserId, String txtUserName, String txtPassword, String IntUserRole, String txtRoleName, String txtInstance) {
        this.IntUserId = IntUserId;
        this.txtUserName = txtUserName;
        this.txtPassword = txtPassword;
        this.IntUserRole = IntUserRole;
        this.txtRoleName = txtRoleName;
        this.txtInstance = txtInstance;
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

    public String getTxtDataId() {
        return txtDataId;
    }

    public void setTxtDataId(String txtDataId) {
        this.txtDataId = txtDataId;
    }

    private String IntUserId;
    private String txtUserName;
    private String txtPassword;
    private String IntUserRole;
    private String txtRoleName;
    private String dtLastLogin;
    private String txtDataId;
    private String txtInstance;

    public String Property_IntUserId = "IntUserId";
    public String Property_txtUserName = "txtUserName";
    public String Property_txtPassword = "txtPassword";
    public String Property_IntUserRole = "IntUserRole";
    public String Property_txtRoleName = "txtRoleName";
    public String Property_dtLastLogin = "dtLastLogin";
    public String Property_txtDataId = "txtDataId";
    public String Property_txtInstance = "txtInstance";

    public String Property_All = Property_IntUserId + "," + Property_txtUserName + "," + Property_txtPassword + "," + Property_IntUserRole + "," + Property_txtRoleName + "," + Property_dtLastLogin + "," + Property_txtDataId + "," + Property_txtInstance;

    public String getTxtInstance() {
        return txtInstance;
    }

    public void setTxtInstance(String txtInstance) {
        this.txtInstance = txtInstance;
    }
}
