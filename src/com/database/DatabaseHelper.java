package com.database;

import java.io.File;
import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	// destination path (location) of our database on device
	private static String DB_PATH = "";
	public static final String DB_NAME = "TELECOM_DATABASE_NEW";
//	public static final String DB_NAME = "TELECOM_DATABASE_01";
	// public static final String DB_NAME = "TELECOM_DATABASE1";
	// public static final String DB_NAME = "TELECOM_DATABASE2";

	// public static final String DB_NAME = "TELECOM_DATABASEREQ";
	private SQLiteDatabase mDataBase;
	public static final int DATABASE_VERSION = 1;
	// public static final int DATABASE_VERSION = 2;
	// TABLES
	public static final String TABLE_USER_REGISTRATION = "UserRegistration";
	public static final String TABLE_SERVICEPLANS = "ServicePlans";
	public static final String TABLE_SERVICEREQUESTS = "ServiceRequests";
	public static final String TABLE_BILLPAY = "BillPayment";

	// table1
	public static final String USER_ID = "Userid";
	public static final String NAME = "Name";
	public static final String EMAIL_ID = "Emailid";
	public static final String PASSWORD = "Password";
	public static final String PHONE = "Phone";
	public static final String BROADBAND_NO = "NetcardNo";
	public static final String LANDLINE_NO = "Landline";
	public static final String ADDRESS = "User_address";
	// table2
	public static final String SERVICEPLAN_ID = "PlanId";
	public static final String SERVICE_PLANS = "ServicePlans";
	public static final String MODE_TYPE = "Type";// either
													// cellular,landline,broadband

	// TABLE3-
	// public static final String USER_ID = "Userid";
	// public static final String SERVICEPLAN_ID = "PlanId";
	// public static final String MODE_TYPE = "Type";
	public static final String USER_PLAN = "Plan";
	public static final String USER_ACCOUNT = "Account_no";
	public static final String USER_ACTIVATION = "Activation";// boolean--0(false),1(true)

	// table-4
	// public static final String NAME = "Name";
	// public static final String PHONE = "Phone";
	// public static final String BROADBAND_NO = "NetcardNo";
	// public static final String LANDLINE_NO = "Landline";
	public static final String AMOUNT = "Amount";
	public static final String BILL_DATE = "Billdate";

	public DatabaseHelper db = null;
	public SQLiteDatabase sqlite = null;

	public DatabaseHelper(Context context) {
		// TODO Auto-generated constructor stub
		super(context, DB_NAME, null, DATABASE_VERSION);
		DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
		Log.d("DB *****---", "db helper constructor called");
	}

	public void createDataBase() throws IOException {
		// If database not exists copy it from the assets

		boolean mDataBaseExist = checkDataBase();
		Log.d("DB", "" + mDataBaseExist);
		if (!mDataBaseExist) {
			this.getReadableDatabase();
			this.close();
			// try {
			// // Copy the database from assests
			// copyDataBase();
			// Log.e(TAG, "createDatabase database created");
			// } catch (IOException mIOException) {
			// throw new Error("ErrorCopyingDataBase");
			// }
		}
	}

	// Check that the database exists here: /data/data/your package/databases/Da
	// Name
	private boolean checkDataBase() {
		File dbFile = new File(DB_PATH + DB_NAME);
		// Log.v("dbFile", dbFile + "   "+ dbFile.exists());
		return dbFile.exists();
	}

	// Open the database, so we can query it
	public boolean openDataBase() throws SQLException {
		String mPath = DB_PATH + DB_NAME;
		// Log.v("mPath", mPath);
		mDataBase = SQLiteDatabase.openDatabase(mPath, null,
				SQLiteDatabase.CREATE_IF_NECESSARY);
		// mDataBase = SQLiteDatabase.openDatabase(mPath, null,
		// SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		return mDataBase != null;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// TODO Auto-generated method stub
		// db.execSQL("CREATE TABLE IF NOT EXISTS"
		// +TABLE_NAME+"(Firstname TEXT,Lastname TEXT,Emailid TEXT PRIMARY KEY,Password TEXT,Gender TEXT,DateOfBirth NUMERIC,Phone NUMERIC,Pincode INTEGER);");

		// db.execSQL("create table if not exists " + TABLE_NAME + "( "
		// + FIRST_NAME + " text not null, " + LAST_NAME + " text, "
		// + EMAIL_ID + " text primary key, " + PASSWORD + " text,"
		// + GENDER + " text," + DOB + " integer," + PHONE + " text, "
		// + PIN_CODE + " integer);");

		db.execSQL("create table if not exists " + TABLE_USER_REGISTRATION
				+ "( " + USER_ID + " integer ," + NAME + " text not null, "
				+ EMAIL_ID + " text primary key, " + PASSWORD + " text,"
				+ PHONE + " text," + BROADBAND_NO + " text," + LANDLINE_NO
				+ " text," + ADDRESS + " text);");

		db.execSQL("create table if not exists " + TABLE_SERVICEPLANS + "( "
				+ SERVICEPLAN_ID + " integer primary key , " + SERVICE_PLANS
				+ " text not null ," + MODE_TYPE + " text);");

		// db.execSQL("create table if not exists " + TABLE_SERVICEREQUESTS +
		// "( "
		// + USER_ID + " integer, " + SERVICEPLAN_ID + " integer, "
		// + MODE_TYPE + " text" + USER_ACTIVATION + " integer);");

		db.execSQL("create table if not exists " + TABLE_SERVICEREQUESTS + "( "
				+ EMAIL_ID + " text  primarykey, " + USER_ACCOUNT
				+ " text not null, " + USER_PLAN + " text not null, "
				+ MODE_TYPE + " text not null," + USER_ACTIVATION
				+ " integer);");

		db.execSQL("create table if not exists " + TABLE_BILLPAY + "( "
				+ EMAIL_ID + " text not null , " + USER_ACCOUNT
				+ " text not null," + MODE_TYPE + " text not null," + USER_PLAN
				+ " text not null, " + AMOUNT + " text not null ," + BILL_DATE
				+ " text not null);");

	}

	private Cursor rawQuery(String string, String[] strings) {
		return null;
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
