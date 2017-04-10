package libraryORM.data;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by ASUS ZE on 01/03/2017.
 */

@DatabaseTable(tableName = "tDisplayPicture")
public class clsDisplayPictures implements Serializable{
    @DatabaseField(columnName = "id")
    public int id;

    @DatabaseField(columnName = "image", dataType = DataType.BYTE_ARRAY)
    public byte[] image;

    public clsDisplayPictures(int id, byte[] image){
        this.id = id;
        this.image = image;
    }

    public clsDisplayPictures(){

    }
}
