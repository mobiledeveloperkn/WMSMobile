package libraryORM.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by ASUS ZE on 02/03/2017.
 */
@DatabaseTable(tableName = "clsSPMDetail")
public class clsSPMDetail implements Serializable{
    @DatabaseField(id = true, unique = true, columnName = "intSPMDetailId")
    public String intSPMDetailId;

    @DatabaseField(columnName = "txtNoSPM")
    public String txtNoSPM;

    @DatabaseField(columnName = "txtLocator")
    public String txtLocator;

    @DatabaseField(columnName = "txtItemCode")
    public String txtItemCode;

    @DatabaseField(columnName = "txtItemName")
    public String txtItemName;

    @DatabaseField(columnName = "intQty")
    public String intQty;

    @DatabaseField(columnName = "bitStatus")
    public String bitStatus;

    @DatabaseField(columnName = "bitSync")
    public String bitSync;

    @DatabaseField(columnName = "txtReason")
    public String txtReason;

    @DatabaseField(columnName = "intUserId")
    public String intUserId;

    @DatabaseField(columnName = "intFlag")
    public String intFlag;

    public clsSPMDetail(){
        super();
    }

    public clsSPMDetail(String intSPMDetailId, String txtNoSPM, String txtLocator, String txtItemCode, String txtItemName, String intQty, String bitStatus, String bitSync, String txtReason, String intUserId, String intFlag){
        this.intSPMDetailId = intSPMDetailId;
        this.txtNoSPM = txtNoSPM;
        this.txtLocator = txtLocator;
        this.txtItemCode = txtItemCode;
        this.txtItemName = txtItemName;
        this.intQty = intQty;
        this.bitStatus = bitStatus;
        this.bitSync = bitSync;
        this.txtReason = txtReason;
        this.intUserId = intUserId;
        this.intFlag = intFlag;
    }
}
