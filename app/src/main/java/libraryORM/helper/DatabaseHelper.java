package libraryORM.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import libraryORM.data.clsDeviceInfo;
import libraryORM.data.clsDisplayPictures;
import libraryORM.data.clsMConfig;
import libraryORM.data.clsSPMDetail;
import libraryORM.data.clsSPMHeader;
import libraryORM.data.clsUserLogin;
import libraryORM.data.clsUserRole;

/**
 * Database helper which creates and upgrades the database and provides the DAOs for the app.
 * 
 * 
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	/************************************************
	 * Suggested Copy/Paste code. Everything from here to the done block.
	 ************************************************/

	public static final String DATABASE_NAME = "wmsmobile.db";
	private static final int DATABASE_VERSION = 4;

	private Dao<clsUserLogin, Integer> userLoginDao;
	private Dao<clsDisplayPictures, Integer> displayPicturesDao;
	private Dao<clsSPMHeader, String> SPMHeaderDao;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
//		getWritableDatabase();
}

	/************************************************
	 * Suggested Copy/Paste Done
	 ************************************************/

	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
		// Create tables. This onCreate() method will be invoked only once of the application life time i.e. the first time when the application starts.

		try {
			TableUtils.createTableIfNotExists(connectionSource, clsUserLogin.class);
			TableUtils.createTableIfNotExists(connectionSource, clsDisplayPictures.class);
			TableUtils.createTableIfNotExists(connectionSource, clsDeviceInfo.class);
			TableUtils.createTableIfNotExists(connectionSource, clsMConfig.class);
			TableUtils.createTableIfNotExists(connectionSource, clsSPMDetail.class);
			TableUtils.createTableIfNotExists(connectionSource, clsSPMHeader.class);
			TableUtils.createTableIfNotExists(connectionSource, clsUserRole.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {

		// In case of change in database of next version of application, please increase the value of DATABASE_VERSION variable, then this method will be invoked
		//automatically. Developer needs to handle the upgrade logic here, i.e. create a new table or a new column to an existing table, take the backups of the
		// existing database etc.

		if(oldVer<4){
			try {
				getSPMHeaderDao().executeRaw("ALTER TABLE `clsSPMHeader` ADD COLUMN bitStart VARCHAR;");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		onCreate(sqliteDatabase, connectionSource);

	}
	
	// Create the getDao methods of all database tables to access those from android code.
	// Insert, delete, read, update everything will be happened through DAOs

	public Dao<clsUserLogin, Integer> getUserLoginDao() throws SQLException {
		if (userLoginDao == null) {
			userLoginDao = getDao(clsUserLogin.class);
		}
		return userLoginDao;
	}
	public Dao<clsDisplayPictures, Integer> getDisplayPicturesDao() {
		if (displayPicturesDao == null) {
			try {
				displayPicturesDao = getDao(clsDisplayPictures.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return displayPicturesDao;
	}

	public Dao<clsSPMHeader, String> getSPMHeaderDao() {
		if (SPMHeaderDao == null) {
			try {
				SPMHeaderDao = getDao(clsSPMHeader.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return SPMHeaderDao;
	}
}
