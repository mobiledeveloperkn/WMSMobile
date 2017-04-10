package libraryORM.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by ASUS ZE on 27/02/2017.
 */
@DatabaseTable(tableName = "clsUserLogin")
public class clsUserLogin implements Serializable{

    @DatabaseField(columnName = "intUserId", unique = true)
    public String intUserId;

    @DatabaseField(columnName = "txtUserName")
    public String txtUserName;

    @DatabaseField(columnName = "intUserRole")
    public String intUserRole;

    @DatabaseField(columnName = "txtRoleName")
    public String txtRoleName;

    @DatabaseField(columnName = "dtLastLogin")
    public String dtLastLogin;

    @DatabaseField(columnName = "txtDataId")
    public String txtDataId;

    public clsUserLogin(){
        super();
    }

    // Default constructor is needed for the SQLite, so make sure you also have it
    public clsUserLogin(String intUserId, String txtUserName, String intUserRole, String txtRoleName, String dtLastLogin, String txtDataId){
        this.intUserId = intUserId;
        this.txtUserName = txtUserName;
        this.intUserRole = intUserRole;
        this.txtRoleName = txtRoleName;
        this.dtLastLogin = dtLastLogin;
        this.txtDataId = txtDataId;

    }

    public String getDtLastLogin() {
        return dtLastLogin;
    }

    public void setDtLastLogin(String dtLastLogin) {
        this.dtLastLogin = dtLastLogin;
    }

    public String getIntUserId() {
        return intUserId;
    }

    public void setIntUserId(String intUserId) {
        this.intUserId = intUserId;
    }

    public String getIntUserRole() {
        return intUserRole;
    }

    public void setIntUserRole(String intUserRole) {
        this.intUserRole = intUserRole;
    }

    public String getTxtDataId() {
        return txtDataId;
    }

    public void setTxtDataId(String txtDataId) {
        this.txtDataId = txtDataId;
    }

    public String getTxtRoleName() {
        return txtRoleName;
    }

    public void setTxtRoleName(String txtRoleName) {
        this.txtRoleName = txtRoleName;
    }

    public String getTxtUserName() {
        return txtUserName;
    }

    public void setTxtUserName(String txtUserName) {
        this.txtUserName = txtUserName;
    }

}
