package library.common;

import java.util.HashMap;

public class clsPushData {
	private dataJson dtdataJson;
	private HashMap<String, byte[]> FileUpload;
	public dataJson getDtdataJson() {
		return dtdataJson;
	}
	public void setDtdataJson(dataJson dtdataJson) {
		this.dtdataJson = dtdataJson;
	}
	public HashMap<String, byte[]> getFileUpload() {
		return FileUpload;
	}
	public void setFileUpload(HashMap<String, byte[]> fileUpload) {
		FileUpload = fileUpload;
	}
}
