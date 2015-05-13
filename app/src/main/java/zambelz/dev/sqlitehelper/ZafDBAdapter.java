package zambelz.dev.sqlitehelper;

/**
 * SQL Query Mapper for simplify database operation
 * @author Nanda . J . A 
 * @version 03.05.2014 build 14.06 
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class ZafDBAdapter {

	private SQLiteDatabase db;
	private String dbPath;
	

	protected ZafDBAdapter(SQLiteDatabase db, String dbPath) {
		this.db = db;
		this.dbPath = dbPath;
	}

	public void open() throws SQLException {
		db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
	}

	public synchronized void close() {
		if(db != null) {
			db.close();
		}
	}
	
	public void beginTransacation() {
		db.beginTransaction();
	}
	
	public void endTransaction() {
		db.endTransaction();
	}
	
	public void setTransactionSuccessful() {
		db.setTransactionSuccessful();
	}
	
	public SQLiteStatement SQLStatement(String sql) {
		return db.compileStatement(sql);
	}
	

	public long insert(String dbTable, ContentValues cVal) {
		if(db.insert(dbTable, null, cVal) > 0) {
			cVal.clear();
			return 1;
		}
		
		return 0;
	}

	public boolean update(String dbTable, ContentValues cVal, String whereClause) {
		if(db.update(dbTable, cVal, whereClause, null) > 0) {
			cVal.clear();
			return true;
		}
		
		return false;
	}

	public boolean delete(String dbTable, String whereClause) {
		return db.delete(dbTable, whereClause, null) > 0;
	}
	
	public void deleteAndReindex(String table) {
		execSQL("DELETE FROM "+ table);
		execSQL("REINDEX "+ table);
		execSQL("VACUUM");
	}
	
	public void dropAndReindex(String table) {
		execSQL("DROP TABLE "+ table);
		execSQL("REINDEX "+ table);
		execSQL("VACUUM");
	}

	public Cursor fetch(String dbTable, String[] columns, String whereClause) {
		return db.query(dbTable, columns, whereClause, null, null, null, null);
	}

	public Cursor fetch(String dbTable, String whereClause) {
		return rawQuery("SELECT * FROM "+ dbTable +" WHERE "+ whereClause);
	}

	public Cursor fetch(String dbTable) {
		return rawQuery("SELECT * FROM "+ dbTable);
	}

	public boolean isDataExists(String query) {
		Cursor csr = null;
		
		try {
			csr = db.rawQuery(query, null);
			
			if(csr.moveToFirst()) {
				if(csr.getCount() > 0) {
					return true;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(csr != null) {
				csr.close();
			}
		}
		
		return false;
	}
	
	public void execSQL(String query) {
		db.execSQL(query);
	}

	public Cursor rawQuery(String query) {
		return db.rawQuery(query, null);
	}

	public Cursor rawQuery(String query, String[] selectionArgs) {
		return db.rawQuery(query, selectionArgs);
	}

	public int getCount(String query) {
		return rawQuery(query).getCount();
	}

	public int getColumnCount(String query) {
		return rawQuery(query).getColumnCount();
	}
	
	public int getIntColumnsValue(String query, String refColumn) {
		Cursor csr = null;
		int column = 0;
		
		try {
			csr = rawQuery(query);
			
			if(csr.moveToFirst()) {
				if(csr.getCount() > 0) {
					column = csr.getInt(csr.getColumnIndex(refColumn));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			csr.close();
		}
		
		return column;
	}
	
	public String getStringColumnsValue(String query, String refColumn) {
		Cursor csr = null;
		String column = null;
		
		try {
			csr = rawQuery(query);
			
			if(csr.moveToFirst()) {
				if(csr.getCount() > 0) {
					column = csr.getString(csr.getColumnIndex(refColumn));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			csr.close();
		}
		
		return column;
	}
	
}