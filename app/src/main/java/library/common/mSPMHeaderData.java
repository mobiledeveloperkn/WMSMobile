package library.common;

/**
 * Created by ASUS ZE on 16/11/2016.
 */

public class mSPMHeaderData {
    public mSPMHeaderData(String intSPMId, String txtNoSPM, String txtBranchCode, String txtBranchName, String txtSalesOrder, String intUserId, String bitStatus, String bitSync, String dtStart, String dtEnd) {
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

    public mSPMHeaderData() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getTxtSalesOrder() {
        return txtSalesOrder;
    }

    public void setTxtSalesOrder(String txtSalesOrder) {
        this.txtSalesOrder = txtSalesOrder;
    }

    public String getTxtNoSPM() {
        return txtNoSPM;
    }

    public void setTxtNoSPM(String txtNoSPM) {
        this.txtNoSPM = txtNoSPM;
    }

    public String getTxtBranchName() {
        return txtBranchName;
    }

    public void setTxtBranchName(String txtBranchName) {
        this.txtBranchName = txtBranchName;
    }

    public String getTxtBranchCode() {
        return txtBranchCode;
    }

    public void setTxtBranchCode(String txtBranchCode) {
        this.txtBranchCode = txtBranchCode;
    }

    public String getIntUserId() {
        return intUserId;
    }

    public void setIntUserId(String intUserId) {
        this.intUserId = intUserId;
    }

    public String getIntSPMId() {
        return intSPMId;
    }

    public void setIntSPMId(String intSPMId) {
        this.intSPMId = intSPMId;
    }

    public String getBitSync() {
        return bitSync;
    }

    public void setBitSync(String bitSync) {
        this.bitSync = bitSync;
    }

    public String getBitStatus() {
        return bitStatus;
    }

    public void setBitStatus(String bitStatus) {
        this.bitStatus = bitStatus;
    }

    public String getBitStart() {
        return bitStart;
    }

    public void setBitStart(String bitStart) {
        this.bitStart = bitStart;
    }

    public String getDtEnd() {
        return dtEnd;
    }

    public void setDtEnd(String dtEnd) {
        this.dtEnd = dtEnd;
    }

    public String getDtStart() {
        return dtStart;
    }

    public void setDtStart(String dtStart) {
        this.dtStart = dtStart;
    }

    private String intSPMId;
    private String txtNoSPM;
    private String txtBranchCode;
    private String txtBranchName;
    private String txtSalesOrder;
    private String intUserId;
    private String bitStatus;
    private String bitSync;
    private String bitStart;
    private String dtStart;
    private String dtEnd;

    public String Property_intSPMId = "intSPMId";
    public String Property_txtNoSPM = "txtNoSPM";
    public String Property_txtBranchCode = "txtBranchCode";
    public String Property_txtBranchName = "txtBranchName";
    public String Property_txtSalesOrder = "txtSalesOrder";
    public String Property_intUserId = "intUserId";
    public String Property_bitStatus = "bitStatus";
    public String Property_bitSync = "bitSync";
    public String Property_bitStart = "bitStart";
    public String Property_dtStart = "dtStart";
    public String Property_dtEnd = "dtEnd";
    public String Property_mSPMHeaderData = "mSPMHeaderData";

    public String Property_All=Property_intSPMId+","+Property_txtNoSPM+","+Property_txtBranchCode+","+Property_txtBranchName+","+Property_txtSalesOrder+","+Property_intUserId+","+Property_bitStatus+","+Property_bitSync+","+Property_bitStart+","+Property_dtStart+","+Property_dtEnd;

}
