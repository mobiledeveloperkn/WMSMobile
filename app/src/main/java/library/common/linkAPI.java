package library.common;

public class linkAPI {
	
	public synchronized String get_txtVesion() {
		return _txtVesion;
	}
	public synchronized void set_txtVesion(String _txtVesion) {
		this._txtVesion = _txtVesion;
	}
	public linkAPI() {
		super();
		// TODO Auto-generated constructor stub
	}
	public linkAPI(String _txtMethod, String _txtParam, String _txtToken) {
		super();
		this._txtMethod = _txtMethod;
		this._txtParam = _txtParam;
		this._txtToken = _txtToken;
	}
	public String QueryString(String url){
		return url+"&me="+this.get_txtMethod()+"&param="+this.get_txtParam()+"&tkn="+this.get_txtToken()+"&v="+get_txtVesion();
	}
	public synchronized String get_txtMethod() {
		return _txtMethod;
	}
	public synchronized void set_txtMethod(String _txtMethod) {
		this._txtMethod = _txtMethod;
	}
	public synchronized String get_txtParam() {
		return _txtParam;
	}
	public synchronized void set_txtParam(String _txtParam) {
		this._txtParam = _txtParam;
	}
	public synchronized String get_txtToken() {
		
		return _txtToken;
	}
	public synchronized void set_txtToken(String _txtToken) {
		this._txtToken = _txtToken;
	}
	private String _txtMethod;
	private String _txtParam;
	private String _txtToken;
	private String _txtVesion="";
	
}
