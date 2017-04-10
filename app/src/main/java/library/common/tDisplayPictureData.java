package library.common;

/**
 * Created by ASUS ZE on 23/08/2016.
 */
public class tDisplayPictureData {

    public synchronized void set_image(byte[] _image) {
        this._image = _image;
    }
    public synchronized byte[] get_image() {
        return _image;
    }

    public synchronized void set_intPush(String _intPush) {
        this._intPush = _intPush;
    }
    public synchronized String get_intPush() {
        return _intPush;
    }

    public synchronized void set_intID(String _intID) {
        this._intID = _intID;
    }
    public synchronized String get_intID() {
        return _intID;
    }

//    public tDisplayPictureData(String _intID, byte[] _image, String _intPush) {
//        this._intID = _intID;
//        this._image = _image;
//        this._intPush = _intPush;
//    }

    private byte[] _image;
    private String _intPush;
    private String _intID;

    public String Property_image = "image";
    public String Property_intPush = "intPush";
    public String Property__intID = "intID";

    public String Property_All=Property__intID+","+Property_image+","+Property_intPush;
}
