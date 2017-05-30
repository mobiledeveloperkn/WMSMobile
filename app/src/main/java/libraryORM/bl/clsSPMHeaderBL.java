package libraryORM.bl;

import android.content.Context;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import libraryORM.data.clsSPMHeader;
import libraryORM.helper.DatabaseHelper;

/**
 * Created by ASUS ZE on 03/03/2017.
 */

public class clsSPMHeaderBL extends DatabaseHelper {
    public clsSPMHeaderBL(Context context) {
        super(context);
        this.getWritableDatabase();
    }

    public List<clsSPMHeader> getData(){
        List<clsSPMHeader> spmHeaderList = new ArrayList<>();
        try {
            spmHeaderList = getSPMHeaderDao().query(getSPMHeaderDao().queryBuilder().where().eq("bitSync", "0").prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();

        return spmHeaderList;
    }

    public List<clsSPMHeader> getDataPushData(){
        List<clsSPMHeader> spmHeaderList = new ArrayList<>();
        try {
            spmHeaderList = getSPMHeaderDao().query(getSPMHeaderDao().queryBuilder().where().eq("bitSync", "0").and().eq("bitStatus","1").prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();

        return spmHeaderList;
    }

    public void saveDataSPMHeader(clsSPMHeader clsSPMHeader) throws SQLException{
        getSPMHeaderDao().create(clsSPMHeader);
        close();
    }
}
