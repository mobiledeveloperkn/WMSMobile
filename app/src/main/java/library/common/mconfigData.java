package library.common;

public class mconfigData {

	public mconfigData(int _intId, String _txtName, String _txtValue,
					   String _txtDefaultValue, int _intEditAdmin) {
		this._intId = _intId;
		this._txtName = _txtName;
		this._txtValue = _txtValue;
		this._txtDefaultValue = _txtDefaultValue;
		this._intEditAdmin = _intEditAdmin;
	}

	public String get_txtDefaultValue() {
		return _txtDefaultValue;
	}

	public void set_txtDefaultValue(String _txtDefaultValue) {
		this._txtDefaultValue = _txtDefaultValue;
	}

	public mconfigData() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int get_intId() {
		return _intId;
	}

	public void set_intId(int _intId) {
		this._intId = _intId;
	}

	public String get_txtName() {
		return _txtName;
	}

	public void set_txtName(String _txtName) {
		this._txtName = _txtName;
	}

	public String get_txtValue() {
		return _txtValue;
	}

	public void set_txtValue(String _txtValue) {
		this._txtValue = _txtValue;
	}

	public int get_intEditAdmin() {
		return _intEditAdmin;
	}

	public void set_intEditAdmin(int _intEditAdmin) {
		this._intEditAdmin = _intEditAdmin;
	}
	
	private int _intId;
	private String _txtName;
	private String _txtValue;
	private String _txtDefaultValue;
	private int _intEditAdmin;
	
	public String Property_intId="intId";
	public String Property_txtName="txtName";
	public String Property_intEditAdmin="intEditAdmin";
	public String Property_txtValue="txtValue";
	public String Property_txtDefaultValue="txtDefaultValue";
	public String Property_ListOfMconfig="ListOfMconfig";
	public String Property_All=Property_intId + "," +
			Property_txtName + "," +
			Property_txtValue + "," +
			Property_txtDefaultValue + "," +
			Property_intEditAdmin ;
}
