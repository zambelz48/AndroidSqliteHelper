# Overview
Android sqlite helper libraries

# Sample Usage

* Add Gradle dependency:
```gradle
dependencies {
   compile 'com.github.zambelz48:AndroidSqliteHelper:1.0.0'
}
```
Or [Download from Bintray] (http://jcenter.bintray.com/com/github/zambelz48/AndroidSqliteHelper/1.0.0/AndroidSqliteHelper-1.0.0.aar)

* Put the database file on 'Assets' folder on your project

* Create base(parent) class that extends SQLiteOpenHelper
```groovy
public class DBAdapter extends SQLiteOpenHelper {

	// Database configuration
	private static final String APP_PACKAGE 	= "your.package.name/";
	private static final String DB_PATH 		= "/data/data/"+ APP_PACKAGE +"databases/";
	private static final String DB_NAME 		= "YOUR_DATABASE_NAME.db";
	private static final int DB_VERSION 		= 1;
	
	protected ZafDBManager db;
	private SQLiteDatabase sqlDB;

	protected DBAdapter(Context context) throws IOException {
		super(context, DB_NAME, null, DB_VERSION);
		
		// create instance
		db = new ZafDBManager(context, this, sqlDB, DB_PATH, DB_NAME);
		
		// setup database profile
		db.setDBProfile(DB_PATH, Config.DB_VERSION, DB_NAME);
		
		// do register 
		db.registerDB();
	}

	public void open() throws SQLException {
		db.open();
	}
	
	@Override
	public synchronized void close() {
		db.close();
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

}
```

* Sample implementations, extending base(parent) class above

```groovy
public class SampleDB extends DBAdapter {

	private static final String TABLE_NAME = "YOUR_TABLE_NAME";

	public SampleDB(Context context) throws IOException {
		super(context);
	}
	
	public long insert(String[] values) {
		ContentValues val = new ContentValues();
		
		val.put("FIELD_1", 	values[0]);
		val.put("FIELD_2",	values[1]);
		val.put("FIELD_3", 	values[2]);
		
		return db.insert(TABLE_NAME, val);
	}
	
	public boolean update(String[] values) {
		ContentValues val = new ContentValues();
		
		val.put("FIELD_1", 	values[0]);
		val.put("FIELD_2",	values[1]);
		val.put("FIELD_3", 	values[2]);
		
		return db.update(TABLE_NAME, val, "KEY_FIELD = " + values[0]);
	}
	
	public Cursor getRowsData() {
		String whereClause = null;
		
		return db.fetch(TABLE_NAME, 
						new String[] {"FIELD_1", "FIELD_2", "FIELD_3"}, 
						whereClause);
	}
	
	public boolean delete(String keyId) {
		return db.delete(TABLE_NAME, "KEY_FIELD = "+ keyId);
	}
}
```

* Sample usage operation
```groovy
public class SampleActivity extends Activity {
	
	private SampleDB db;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sample_layout);
		
		// create instance
		try {
			db = new SampleDB(this);
		} catch(IOException e) {
			e.printStackTrace();
		}

		// sample operation
		String[] values = {
			"values_1",
			"values_2",
			"values_3"
		};

		db.open();
		db.insert(values);
		db.close();
	}
	
}
```
