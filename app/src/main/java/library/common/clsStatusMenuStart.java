package library.common;

import library.dal.enumStatusMenuStart;

public class clsStatusMenuStart {
	private enumStatusMenuStart _intStatus;
	private String _txtDeskripsi;
//	private mMenuData _mMenuData;
	public enumStatusMenuStart get_intStatus() {
		return _intStatus;
	}
	public void set_intStatus(enumStatusMenuStart _intStatus) {
		this._intStatus = _intStatus;
	}
	public String get_txtDeskripsi() {
		return _txtDeskripsi;
	}
	public void set_txtDeskripsi(String _txtDeskripsi) {
		this._txtDeskripsi = _txtDeskripsi;
	}
//	public mMenuData get_mMenuData() {
//		return _mMenuData;
//	}
//	public void set_mMenuData(mMenuData _mMenuData) {
//		this._mMenuData = _mMenuData;
//	}
}
