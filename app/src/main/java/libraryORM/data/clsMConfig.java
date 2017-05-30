package libraryORM.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by ASUS ZE on 02/03/2017.
 */

@DatabaseTable(tableName = "clsMConfig")
public class clsMConfig implements Serializable{

    @DatabaseField(columnName = "intId", id = true, unique = true)
    private int intId;

    @DatabaseField(columnName = "txtName")
    private String txtName;

    @DatabaseField(columnName = "txtValue")
    private String txtValue;

    @DatabaseField(columnName = "txtDefaultValue")
    private String txtDefaultValue;

    @DatabaseField(columnName = "intEditAdmin")
    private int intEditAdmin;

    public clsMConfig(){
        super();
    }

    public clsMConfig(int intId, String txtName, String txtValue, String txtDefaultValue, int intEditAdmin){
        this.intId = intId;
        this.txtName = txtName;
        this.txtValue = txtValue;
        this.txtDefaultValue = txtDefaultValue;
        this.intEditAdmin = intEditAdmin;

    }
}
