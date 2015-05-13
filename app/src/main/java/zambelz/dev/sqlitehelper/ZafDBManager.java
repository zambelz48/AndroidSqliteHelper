package zambelz.dev.sqlitehelper;

/**
 * Database Manager
 * @author Nanda . J . A 
 * @version 03.05.2014 build 14.06
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class ZafDBManager extends ZafDBAdapter {

	private String dbPath;
	private int dbVersion;
	private String dbName;

	private Context context;
	private SQLiteOpenHelper sHelper;
	private SQLiteDatabase db;

	public ZafDBManager(Context context, SQLiteOpenHelper sHelper, SQLiteDatabase db, String dbPath, String dbName) {
		super(db, dbPath + dbName);
		
		this.context = context;
		this.sHelper = sHelper;
		this.db		 = db;
	}

	public void setDBProfile(String dbPath, int dbVersion, String dbName) {
		this.dbPath = dbPath;
		this.dbVersion = dbVersion;
		this.dbName = dbName;
	}
	
	public SQLiteDatabase getDB() {
		return db;
	}
	
	public String getDBPath() {
		return dbPath;
	}
	
	public int getDBVersion() {
		return dbVersion;
	}
	
	public String getDBName() {
		return dbName;
	}

	public void registerDB() throws IOException {
		if(!isDBExists()) {
			copyDatabase();
		}
	}
	
	private boolean isDBExists() {
		try {
			File DBFile = new File(dbPath + dbName);
			return DBFile.exists();
		} catch (Exception e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
		}

		return false;
	}

	public void copyDatabase() throws IOException {
		sHelper.getReadableDatabase();

		InputStream input 	= context.getAssets().open(dbName);
		OutputStream output = new FileOutputStream(dbPath + dbName);
		byte[] buffer 		= new byte[1024];
		int length;
		
		while((length = input.read(buffer)) > 0) {
			output.write(buffer, 0, length);
		}

		output.flush();
		output.close();
		input.close();
	}
	
}
