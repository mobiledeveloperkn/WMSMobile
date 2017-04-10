package libraryORM.bl;

import android.content.Context;

import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

import libraryORM.data.clsUserLogin;
import libraryORM.helper.DatabaseHelper;

/**
 * Created by ASUS ZE on 27/02/2017.
 */

public class clsUserLoginBL extends DatabaseHelper{

    public clsUserLoginBL(Context context) {
        super(context);
        this.getWritableDatabase();

    }

    public void saveDataLogin(clsUserLogin clsUserLogin) throws SQLException{
            getUserLoginDao().create(clsUserLogin);
        close();
    }

    public List<clsUserLogin> getAllData(){
        List<clsUserLogin> clsUserLoginList = null;
        try {

            clsUserLoginList = getUserLoginDao().queryForAll();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        close();
        return clsUserLoginList;
    }

    public void deleteAllData() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), clsUserLogin.class);
    }
}
