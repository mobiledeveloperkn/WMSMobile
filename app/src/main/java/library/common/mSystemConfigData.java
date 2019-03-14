package library.common;

public class mSystemConfigData {

    public mSystemConfigData(int _intId, String _txtName, String _txtValue, String _txtDefaultValue) {
        this._intId = _intId;
        this._txtName = _txtName;
        this._txtValue = _txtValue;
        this._txtDefaultValue = _txtDefaultValue;
    }

    public mSystemConfigData() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String get_txtDefaultValue() {
        return _txtDefaultValue;
    }

    public void set_txtDefaultValue(String _txtDefaultValue) {
        this._txtDefaultValue = _txtDefaultValue;
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

    private int _intId;
    private String _txtName;
    private String _txtValue;
    private String _txtDefaultValue;

    public String Property_intId = "intId";
    public String Property_txtName = "txtName";
    public String Property_txtValue = "txtValue";
    public String Property_txtDefaultValue = "txtDefaultValue";
    public String Property_ListOfMSystemConfig = "ListOfMconfig";
    public String Property_All = Property_intId + "," +
            Property_txtName + "," +
            Property_txtValue + "," +
            Property_txtDefaultValue;
}
