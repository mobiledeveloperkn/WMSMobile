package libraryORM.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by ASUS ZE on 02/03/2017.
 */

@DatabaseTable(tableName = "clsUserRole")
public class clsUserRole implements Serializable{

    @DatabaseField(columnName = "intRoleId", id = true, unique = true)
    public String intRoleId;

    @DatabaseField(columnName = "txtRoleName")
    public String txtRoleName;

    public clsUserRole(){
        super();
    }

    public clsUserRole(String intRoleId, String txtRoleName){
        this.intRoleId = intRoleId;
        this.txtRoleName = txtRoleName;
    }
}
