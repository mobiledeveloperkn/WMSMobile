package library.common;

public class tDeviceInfoUserData {
	public synchronized String get_txtDeviceId() {
		return _txtDeviceId;
	}
	public synchronized void set_txtDeviceId(String _txtDeviceId) {
		this._txtDeviceId = _txtDeviceId;
	}
	public tDeviceInfoUserData() {
		super();
		// TODO Auto-generated constructor stub
	}
	public tDeviceInfoUserData(int _intId, String _txtVersion, String _txtDevice,
							   String _txtModel, String _txtUserId, String _txtDeviceId, String versionName) {
		this._intId = _intId;
		this._txtVersion = _txtVersion;
		this._txtDevice = _txtDevice;
		this._txtDeviceId = _txtDeviceId;
		this._txtModel = _txtModel;
		this._txtUserId = _txtUserId;
		this._txtVersionName = versionName;
	}
	public synchronized int get_intId() {
		return _intId;
	}
	public synchronized void set_intId(int _intId) {
		this._intId = _intId;
	}
	public synchronized String get_txtVersion() {
		return _txtVersion;
	}
	public synchronized void set_txtVersion(String _txtVersion) {
		this._txtVersion = _txtVersion;
	}
	public synchronized String get_txtDevice() {
		return _txtDevice;
	}
	public synchronized void set_txtDevice(String _txtDevice) {
		this._txtDevice = _txtDevice;
	}
	public synchronized String get_txtModel() {
		return _txtModel;
	}
	public synchronized void set_txtModel(String _txtModel) {
		this._txtModel = _txtModel;
	}
	public synchronized String get_txtUserId() {
		return _txtUserId;
	}
	public synchronized void set_txtUserId(String _txtUserId) {
		this._txtUserId = _txtUserId;
	}
	public String get_txtVersionName() {
		return _txtVersionName;
	}

	public void set_txtVersionName(String _txtVersionName) {
		this._txtVersionName = _txtVersionName;
	}
	private int _intId;
	private String _txtVersion;
	private String _txtDevice;
	private String _txtDeviceId;
	private String _txtModel;
	private String _txtUserId;
	private String _txtVersionName;
	
	public String Property_intId="intId";
	public String Property_txtVersion="txtVersion";
	public String Property_txtDevice="txtDevice";
	public String Property_txtModel="txtModel";
	public String Property_txtUserId="txtUserId";
	public String Property_txtDeviceId="TxtDeviceId";
	public String Property_txtVersionName="_txtVersionName";
	public String Property_ListOftDeviceInfoUserData="ListOftDeviceInfoUserData";
	public String Property_All=Property_intId+","+
			Property_txtVersion+","+
			Property_txtDevice+","+
			Property_txtDeviceId+","+
			Property_txtModel+","+
			Property_txtUserId+","+
			Property_txtVersionName;
}
