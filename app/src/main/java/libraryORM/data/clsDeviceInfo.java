package libraryORM.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by ASUS ZE on 02/03/2017.
 */

@DatabaseTable(tableName = "clsDeviceInfo")
public class clsDeviceInfo implements Serializable{

    @DatabaseField(id = true, unique = true, columnName = ("intId"))
    private int intId;

    @DatabaseField(columnName = "txtVersion")
    private String txtVersion;

    @DatabaseField(columnName = "txtDevice")
    private String txtDevice;

    @DatabaseField(columnName = "txtDeviceId")
    private String txtDeviceId;

    @DatabaseField(columnName = "txtModel")
    private String txtModel;

    @DatabaseField(columnName = "txtUserId")
    private String txtUserId;

    @DatabaseField(columnName = "txtVersionName")
    private String txtVersionName;

    public clsDeviceInfo(){
        super();
    }

    public clsDeviceInfo(int intId, String txtVersion, String txtDevice, String txtDeviceId, String txtModel, String txtUserId, String txtVersionName){
        this.intId = intId;
        this.txtVersion = txtVersion;
        this.txtDevice = txtDevice;
        this.txtDeviceId = txtDeviceId;
        this.txtModel = txtModel;
        this.txtUserId = txtUserId;
        this.txtVersionName = txtVersionName;
    }
}
