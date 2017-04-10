package libraryORM.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by ASUS ZE on 02/03/2017.
 */

@DatabaseTable(tableName = "clsSPMHeader")
public class clsSPMHeader implements Serializable {
    @DatabaseField(id = true, unique = true, columnName = "intSPMId")
    public String intSPMId;

    @DatabaseField(columnName = "txtNoSPM")
    public String txtNoSPM;

    @DatabaseField(columnName = "txtBranchCode")
    public String txtBranchCode;

    @DatabaseField(columnName = "txtBranchName")
    public String txtBranchName;

    @DatabaseField(columnName = "txtSalesOrder")
    public String txtSalesOrder;

    @DatabaseField(columnName = "intUserId")
    public String intUserId;

    @DatabaseField(columnName = "bitStatus")
    public String bitStatus;

    @DatabaseField(columnName = "bitSync")
    public String bitSync;

    @DatabaseField(columnName = "dtStart")
    public String dtStart;

    @DatabaseField(columnName = "dtEnd")
    public String dtEnd;

    @DatabaseField(columnName = "bitStart")
    public  String bitStart;

    public clsSPMHeader(){
        super();
    }

    public clsSPMHeader(String intSPMId, String txtNoSPM, String txtBranchCode, String txtBranchName, String txtSalesOrder, String intUserId, String bitStatus, String bitSync, String dtStart, String dtEnd){
        this.intSPMId = intSPMId;
        this.txtNoSPM = txtNoSPM;
        this.txtBranchCode = txtBranchCode;
        this.txtBranchName = txtBranchName;
        this.txtSalesOrder = txtSalesOrder;
        this.intUserId = intUserId;
        this.bitStatus = bitStatus;
        this.bitSync = bitSync;
        this.dtStart = dtStart;
        this.dtEnd = dtEnd;
    }
}
