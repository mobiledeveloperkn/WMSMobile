package library.common;

/**
 * Created by ASUS ZE on 16/11/2016.
 */

public class mRoleData {
    public mRoleData(String intRoleId, String txtRoleName) {
        this.intRoleId = intRoleId;
        this.txtRoleName = txtRoleName;
    }

    public mRoleData() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getTxtRoleName() {
        return txtRoleName;
    }

    public void setTxtRoleName(String txtRoleName) {
        this.txtRoleName = txtRoleName;
    }

    public String getIntRoleId() {
        return intRoleId;
    }

    public void setIntRoleId(String intRoleId) {
        this.intRoleId = intRoleId;
    }

    private String intRoleId;
    private String txtRoleName;

    public String Property_intRoleId = "intRoleId";
    public String Property_txtRoleName = "txtRoleName";

    public String Property_All=Property_intRoleId+","+Property_txtRoleName;

}
