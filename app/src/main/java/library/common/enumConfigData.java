package library.common;

public enum enumConfigData {
	AndroidVersionName  (1),
	ApiKalbe	(2),
	TypeMobile   (3),
	DomainKalbe   (4),
	ApplicationName   (5),
	TextFooter   (6),
	LIVE   (7),
	BackGroundServiceOnline   (8)
	;
	enumConfigData(int idConfigData) {
		this.idConfigData = idConfigData;
	}
	public int getidConfigData() {
		return this.idConfigData;
	}
	private  final int idConfigData;
}
