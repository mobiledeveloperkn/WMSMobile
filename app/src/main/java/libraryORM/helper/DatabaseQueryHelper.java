package libraryORM.helper;

import android.content.Context;

import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import libraryORM.data.clsUserLogin;

/**
 * Created by ASUS ZE on 01/03/2017.
 */

public class DatabaseQueryHelper extends  DatabaseHelper {
    public DatabaseQueryHelper(Context context) {
        super(context);
        this.getWritableDatabase();
    }

    public void DeleteAllData() throws SQLException{
        TableUtils.clearTable(getConnectionSource(), clsUserLogin.class);
    }
}
