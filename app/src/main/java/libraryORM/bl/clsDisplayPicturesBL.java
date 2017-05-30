package libraryORM.bl;

import android.content.Context;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;

import libraryORM.data.clsDisplayPictures;
import libraryORM.helper.DatabaseHelper;

/**
 * Created by ASUS ZE on 02/03/2017.
 */

public class clsDisplayPicturesBL extends DatabaseHelper {
    public clsDisplayPicturesBL(Context context) {
        super(context);
        this.getWritableDatabase();
    }
    public void saveDataImage(clsDisplayPictures clsDisplayPictures) {
        try {
            UpdateBuilder<clsDisplayPictures, Integer> updateBuilder =
                    getDisplayPicturesDao().updateBuilder();
            updateBuilder.updateColumnValue("id", clsDisplayPictures.id);
            updateBuilder.updateColumnValue("image", clsDisplayPictures.image);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
    }
    public clsDisplayPictures getData(){
        clsDisplayPictures clsDisplayPictures = null;
        try {
            QueryBuilder<clsDisplayPictures, Integer> queryBuilder = getDisplayPicturesDao().queryBuilder();
            Where<clsDisplayPictures, Integer> where = queryBuilder.where();
            SelectArg selectArg = new SelectArg();
// define our query as 'name = ?'
            where.eq("id", 1);
// prepare it so it is ready for later query or iterator calls
            PreparedQuery<clsDisplayPictures> preparedQuery = queryBuilder.prepare();
            clsDisplayPictures = getDisplayPicturesDao().queryForFirst(preparedQuery);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clsDisplayPictures;
    }
}
