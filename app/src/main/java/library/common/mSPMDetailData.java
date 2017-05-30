package library.common;

/**
 * Created by ASUS ZE on 16/11/2016.
 */

public class mSPMDetailData {
    public mSPMDetailData(String intSPMDetailId, String txtNoSPM, String txtLocator, String txtItemCode, String txtItemName, String intQty, String bitStatus, String bitSync, String intUserId, String intFlag) {
        this.intSPMDetailId = intSPMDetailId;
        this.txtNoSPM = txtNoSPM;
        this.txtLocator = txtLocator;
        this.txtItemCode = txtItemCode;
        this.txtItemName = txtItemName;
        this.intQty = intQty;
        this.bitStatus = bitStatus;
        this.bitSync = bitSync;
        this.intUserId = intUserId;
        this.intFlag = intFlag;
    }

    public mSPMDetailData() {
        super();
        // TODO Auto-generated constructor stub
    }

    private String intSPMDetailId;
    private String txtNoSPM;
    private String txtLocator;
    private String txtItemCode;
    private String txtItemName;
    private String intQty;
    private String bitStatus;
    private String bitSync;
    private String txtReason;
    private String intUserId;
    private String intFlag;

    public String Property_intSPMDetailId = "intSPMDetailId";
    public String Property_txtNoSPM = "txtNoSPM";
    public String Property_txtLocator = "txtLocator";
    public String Property_txtItemCode = "txtItemCode";
    public String Property_txtItemName = "txtItemName";
    public String Property_intQty = "intQty";
    public String Property_bitStatus = "bitStatus";
    public String Property_bitSync = "bitSync";
    public String Property_txtReason = "txtReason";
    public String Property_intUserId = "intUserId";
    public String Property_intFlag = "intFlag";

    public String Property_All=Property_intSPMDetailId+","+Property_txtNoSPM+","+Property_txtLocator+","+Property_txtItemCode+","+Property_txtItemName+","+Property_intQty+","+Property_bitStatus+","+Property_bitSync+","+Property_txtReason+","+Property_intUserId+","+Property_intFlag;

    public String getTxtNoSPM() {
        return txtNoSPM;
    }

    public void setTxtNoSPM(String txtNoSPM) {
        this.txtNoSPM = txtNoSPM;
    }

    public String getTxtLocator() {
        return txtLocator;
    }

    public void setTxtLocator(String txtLocator) {
        this.txtLocator = txtLocator;
    }

    public String getTxtItemName() {
        return txtItemName;
    }

    public void setTxtItemName(String txtItemName) {
        this.txtItemName = txtItemName;
    }

    public String getTxtItemCode() {
        return txtItemCode;
    }

    public void setTxtItemCode(String txtItemCode) {
        this.txtItemCode = txtItemCode;
    }

    public String getIntSPMDetailId() {
        return intSPMDetailId;
    }

    public void setIntSPMDetailId(String intSPMDetailId) {
        this.intSPMDetailId = intSPMDetailId;
    }

    public String getIntQty() {
        return intQty;
    }

    public void setIntQty(String intQty) {
        this.intQty = intQty;
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
    public String getTxtReason() {
        return txtReason;
    }

    public void setTxtReason(String txtReason) {
        this.txtReason = txtReason;
    }

    public String getIntUserId() {
        return intUserId;
    }

    public void setIntUserId(String intUserId) {
        this.intUserId = intUserId;
    }

    public String getIntFlag() {
        return intFlag;
    }

    public void setIntFlag(String intFlag) {
        this.intFlag = intFlag;
    }
}
