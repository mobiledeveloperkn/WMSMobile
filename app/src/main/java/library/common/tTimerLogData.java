package library.common;

/**
 * Created by ASUS ZE on 16/11/2016.
 */

public class tTimerLogData {

    private String txtTimerLogId;
    private String txtTimerStatus;
    private String txtTimerType;
    private String txtStarNo;
    private String bitActive;
    private String dtExecute;

    public tTimerLogData() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String Property_txtTimerLogId = "txtTimerLogId";
    public String Property_txtTimerStatus = "txtTimerStatus";
    public String Property_txtTimerType = "txtTimerType";
    public String Property_txtStarNo = "txtStarNo";
    public String Property_bitActive = "bitActive";
    public String Property_dtExecute = "dtExecute";

    public String Property_All=Property_txtTimerLogId+","+Property_txtTimerStatus+","+Property_txtTimerType+","+Property_txtStarNo+","+Property_bitActive+","+Property_dtExecute;

    public String getTxtTimerLogId() {
        return txtTimerLogId;
    }

    public void setTxtTimerLogId(String txtTimerLogId) {
        this.txtTimerLogId = txtTimerLogId;
    }

    public String getTxtTimerStatus() {
        return txtTimerStatus;
    }

    public void setTxtTimerStatus(String txtTimerStatus) {
        this.txtTimerStatus = txtTimerStatus;
    }

    public String getTxtTimerType() {
        return txtTimerType;
    }

    public void setTxtTimerType(String txtTimerType) {
        this.txtTimerType = txtTimerType;
    }

    public String getBitActive() {
        return bitActive;
    }

    public void setBitActive(String bitActive) {
        this.bitActive = bitActive;
    }

    public String getDtExecute() {
        return dtExecute;
    }

    public void setDtExecute(String dtExecute) {
        this.dtExecute = dtExecute;
    }

    public String getTxtStarNo() {
        return txtStarNo;
    }

    public void setTxtStarNo(String txtStarNo) {
        this.txtStarNo = txtStarNo;
    }
}
