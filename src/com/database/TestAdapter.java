package com.database;

import java.io.IOException;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.telecomapp.HomePage;
import com.example.telecomapp.MainActivity;
import com.example.telecomapp.RegistrationActivity;
import com.example.telecomapp.RequestsBean;

public class TestAdapter {
	protected static final String TAG = "DataAdapter";
	ArrayList<String> vod_dates = new ArrayList<String>();
	private final Context mContext;
	private SQLiteDatabase mDb;
	private DatabaseHelper mDbHelper;
	public String[] mobile_plans = { "plan-99:pulse rate for all calls 60 sec",
			"plan-225:pulse rate for all calls 1 sec",
			"plan-325:pulse rate for all calss 15 sec",
			"plan-525:pulse rate for all calss 15 sec" };

	public String[] landline_plans = {
			"UL-350:plan charges per month 350/-,pulse:60 sec,from bsnl-landline to bsnl mobile-0.60",
			"UL-500:plan charges per month 500/-,pulse:60 sec,from bsnl-landline to bsnl mobile-no charge",
			"TV-250:plan charges per month 250/-,pulse:60 sec,from bsnl-landline to bsnl mobile-1.00" };

	public String[] broadband_plans = {
			"plan-99:pulse rate for all calls 60 sec",
			"plan-225:pulse rate for all calls 1 sec",
			"plan-325:pulse rate for all calss 15 sec",
			"plan-525:pulse rate for all calss 15 sec" };

	public String user_mail, user_name, user_pwd, serviceplan, user_mobileno,
			user_landlineno, user_broadbandno;
	int accnt_active = 0;
	Editor login_editor;
	private SharedPreferences main_preferences;

	public TestAdapter(Context context) {
		this.mContext = context;
		mDbHelper = new DatabaseHelper(mContext);

		main_preferences = PreferenceManager
				.getDefaultSharedPreferences(mContext.getApplicationContext());
	}

	public TestAdapter createDatabase() throws SQLException {
		try {
			mDbHelper.createDataBase();
		} catch (IOException mIOException) {
			Log.e(TAG, mIOException.toString() + " UnableToCreateDatabase");
			throw new Error("UnableToCreateDatabase");
		}
		return this;
	}

	public TestAdapter open() throws SQLException {
		try {
			mDbHelper.openDataBase();
			// mDbHelper.close();
			mDb = mDbHelper.getReadableDatabase();
		} catch (SQLException mSQLException) {
			Log.e(TAG, "open >>" + mSQLException.toString());
			throw mSQLException;
		}
		return this;
	}

	public void close() {
		mDb.close();
		mDbHelper.close();
	}

	public void serviceplans_insertion() {

		for (int i = 0; i < mobile_plans.length; i++) {
			ContentValues cv = new ContentValues();
			// cv.put(DatabaseHelper.SERVICEPLAN_ID, i);
			cv.put(DatabaseHelper.SERVICE_PLANS, mobile_plans[i]);
			cv.put(DatabaseHelper.MODE_TYPE, "Cellular");
			if ((mDb.insert(DatabaseHelper.SERVICE_PLANS, null, cv)) != -1) {
				Log.d("Saving mobie plans  Data", "information saved");
			} else {
				Log.d("saving  mobile-plans  Data", "information not saved");
			}
		}

		for (int i = mobile_plans.length; i < landline_plans.length; i++) {
			ContentValues cv = new ContentValues();
			// cv.put(DatabaseHelper.SERVICEPLAN_ID, i);
			cv.put(DatabaseHelper.SERVICE_PLANS, landline_plans[i]);
			cv.put(DatabaseHelper.MODE_TYPE, "Landline");
			if ((mDb.insert(DatabaseHelper.SERVICE_PLANS, null, cv)) != -1) {
				Log.d("Saving Landline plans  Data", "information saved");
			} else {
				Log.d("saving  Landline-plans  Data", "information not saved");
			}
		}

		for (int i = landline_plans.length; i < broadband_plans.length; i++) {
			ContentValues cv = new ContentValues();
			// cv.put(DatabaseHelper.SERVICEPLAN_ID, i);
			cv.put(DatabaseHelper.SERVICE_PLANS, broadband_plans[i]);
			cv.put(DatabaseHelper.MODE_TYPE, "Broadband");
			if ((mDb.insert(DatabaseHelper.SERVICE_PLANS, null, cv)) != -1) {
				Log.d("Saving Landline plans  Data", "information saved");
			} else {
				Log.d("saving  Landline-plans  Data", "information not saved");
			}
		}

	}

	public void serviceplans_insertion1(String planDetails, String planType) {

		ContentValues cv = new ContentValues();
		// cv.put(DatabaseHelper.SERVICEPLAN_ID, i);
		cv.put(DatabaseHelper.SERVICE_PLANS, planDetails);
		cv.put(DatabaseHelper.MODE_TYPE, planType);
		if ((mDb.insert(DatabaseHelper.SERVICE_PLANS, null, cv)) != -1) {
			Log.d("Saving mobie plans  Data", "information saved");
		} else {
			Log.d("saving  mobile-plans  Data", "information not saved");
		}

	}

	public ArrayList<String> serviceplans_retrieval(String type) {
		ArrayList<String> labels2 = new ArrayList<String>();
		Log.d(" ****", "Service-plans  retreive ");

		// Cursor cur1 = mDb.rawQuery("select * from "
		// + DatabaseHelper.TABLE_USER_REGISTRATION + "  where emailid ='"
		// + userName + "' and password = '" + pwd + "'", null);

		String selectQuery = "SELECT  * FROM  "
				+ DatabaseHelper.TABLE_SERVICEPLANS + " WHERE Type = ? ";

		String[] args = new String[] { type };

		Cursor cur1 = mDb.rawQuery(selectQuery, args);
		Log.d("plans-info -----------------" + "select * from "
				+ DatabaseHelper.TABLE_SERVICEPLANS + " WHERE Type = ? ",
				":----------" + cur1.getCount());

		// looping through all rows and adding to list
		if (cur1.moveToFirst()) {
			do {
				serviceplan = cur1.getString(cur1
						.getColumnIndex("ServicePlans"));
				labels2.add(serviceplan);

				// System.out.println(a);
			} while (cur1.moveToNext());

			Log.d("Service-plans list", labels2.toString());
			// return labels2;
		} else {

			// return labels2;
		}

		cur1.close();
		return labels2;
	}

	public ArrayList<RequestsBean> serviceReqs_retrieval() {

		ArrayList<RequestsBean> req_list = new ArrayList<RequestsBean>();
		String selectQuery = "SELECT  * FROM  "
				+ DatabaseHelper.TABLE_SERVICEREQUESTS;
		// SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = mDb.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				RequestsBean req_bean = new RequestsBean();
				String name = cursor
						.getString(cursor.getColumnIndex("Emailid"));
				String acnt_no = cursor.getString(cursor
						.getColumnIndex("Account_no"));
				String plan = cursor.getString(cursor.getColumnIndex("Plan"));
				String type = cursor.getString(cursor.getColumnIndex("Type"));
				req_bean.setName(name);
				req_bean.setAccnt_no(acnt_no);
				req_bean.setPlan(plan);
				req_bean.setType(type);
				req_list.add(req_bean);
				// System.out.println(a);
			} while (cursor.moveToNext());
		}

		// closing connection
		cursor.close();
		return req_list;

	}

	public int serviceReqs_activate(String type) {
		Log.d("updating requests table*****", "");
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.USER_ACTIVATION, 1);

		// updating row
		return mDb.update(DatabaseHelper.TABLE_SERVICEREQUESTS, values,
				DatabaseHelper.MODE_TYPE + " = ?",
				new String[] { String.valueOf(type) });

	}

	public int activeaccnt_retrieval(String emailid, String type) {

		int accnt_inserted;
		Log.d(" ****", "database retreive ");
		// check with mailid and type
		String selectQuery = "SELECT  * FROM  "
				+ DatabaseHelper.TABLE_SERVICEREQUESTS
				+ " WHERE Emailid = ?  and Type = ?";

		String[] args = new String[] { emailid, type };

		Cursor cur1 = mDb.rawQuery(selectQuery, args);
		Log.e("active accnt retrieve -----------------" + "select * from "
				+ DatabaseHelper.TABLE_SERVICEREQUESTS
				+ " WHERE Emailid = ?  and Type = ?",
				":----------" + cur1.getCount());
		if (cur1.moveToNext()) {

			Log.d("retreive --", "user exists");
			accnt_active = cur1.getInt(cur1.getColumnIndex("Activation"));
			String accnt_no = cur1.getString(cur1.getColumnIndex("Account_no"));
			String accnt_type = cur1.getString(cur1.getColumnIndex("Type"));
			String plan = cur1.getString(cur1.getColumnIndex("Plan"));

			Log.d("accnt-activation details retrieval*****", "activation--"
					+ accnt_active);

			login_editor = main_preferences.edit();
			login_editor.putString("accntType", accnt_type);
			login_editor.putString("accntNo", accnt_no);
			login_editor.putString("userplan", plan);
			login_editor.putInt("activation", accnt_active);
			login_editor.commit();
			accnt_inserted=1;
		} else {
			accnt_inserted=0;
//			accnt_active = 0;
		}
		cur1.close();
//		return accnt_active;
		return accnt_inserted;

	}

	public boolean user_registration(String name, String emailid, String pwd) {
		ContentValues cv = new ContentValues();
		cv.put(DatabaseHelper.NAME, name);
		cv.put(DatabaseHelper.EMAIL_ID, emailid);
		cv.put(DatabaseHelper.PASSWORD, pwd);
		cv.put(DatabaseHelper.PHONE, "");
		cv.put(DatabaseHelper.BROADBAND_NO, "");
		cv.put(DatabaseHelper.LANDLINE_NO, "");
		if ((mDb.insert(DatabaseHelper.TABLE_USER_REGISTRATION, null, cv)) != -1) {
			Toast.makeText(mContext, "Registration successfull", 2000).show();
			Log.d("Registration successfull********", "information saved");
			return true;
		}

		else {
			Toast.makeText(mContext, "Insertion Error", 2000).show();
			return false;
		}

	}

	public boolean user_login(String userName, String pwd) {
		boolean a = false;
		Log.d(" ****", "database retreive ");

		// Cursor cur1 = mDb.rawQuery("select * from "
		// + DatabaseHelper.TABLE_USER_REGISTRATION + "  where emailid ='"
		// + userName + "' and password = '" + pwd + "'", null);

		String selectQuery = "SELECT  * FROM  "
				+ DatabaseHelper.TABLE_USER_REGISTRATION
				+ " WHERE emailid = ?  and password = ?";

		String[] args = new String[] { userName, pwd };

		Cursor cur1 = mDb.rawQuery(selectQuery, args);
		Log.d("Count -----------------" + "select * from "
				+ DatabaseHelper.TABLE_USER_REGISTRATION + "  where emailid ='"
				+ userName + "' and password = '" + pwd + "'", ":----------"
				+ cur1.getCount());
		if (cur1.moveToNext()) {

			Log.d("retreive --", "user exists");
			user_mail = cur1.getString(cur1.getColumnIndex("Emailid"));
			user_name = cur1.getString(cur1.getColumnIndex("Name"));
			user_pwd = cur1.getString(cur1.getColumnIndex("Password"));

			Log.d("user details retrieval*****", "name--" + user_name
					+ "mail id--" + user_mail + "pwd--" + user_pwd);

			login_editor = main_preferences.edit();
			login_editor.putString("usermailid", user_mail);
			login_editor.putString("username", user_name);
			login_editor.putString("userpwd", user_pwd);
			login_editor.commit();
			a = true;
			// return true;
		} else {
			a = false;
			// cur1.close();
			// return false;
		}
		cur1.close();
		return a;
	}

	public int userAccount_configure(String accnt_no, String type) {

		Log.d("updating user table*****", "");
		ContentValues values = new ContentValues();
		if (type.equalsIgnoreCase("Cellular")) {
			values.put(DatabaseHelper.PHONE, accnt_no);
		} else if (type.equalsIgnoreCase("Landline")) {
			values.put(DatabaseHelper.LANDLINE_NO, accnt_no);
		} else if (type.equalsIgnoreCase("Broadband")) {
			values.put(DatabaseHelper.BROADBAND_NO, accnt_no);
		}

		// updating row
		return mDb.update(DatabaseHelper.TABLE_USER_REGISTRATION, values,
				DatabaseHelper.EMAIL_ID + " = ?",
				new String[] { String.valueOf(main_preferences.getString(
						"usermailid", "")) });

	}

	public boolean userAccount_insertIntoRequests(String mailid,
			String accntNo, String type, String plan) {
		ContentValues cv = new ContentValues();
		cv.put(DatabaseHelper.EMAIL_ID, mailid);
		cv.put(DatabaseHelper.USER_ACCOUNT, accntNo);
		cv.put(DatabaseHelper.MODE_TYPE, type);
		cv.put(DatabaseHelper.USER_PLAN, plan);
		cv.put(DatabaseHelper.USER_ACTIVATION, 0);
		if ((mDb.insert(DatabaseHelper.TABLE_SERVICEREQUESTS, null, cv)) != -1) {
			Toast.makeText(mContext, "requests successfull", 2000).show();
			Log.d("requests successfull********", "information saved");
			return true;
		}

		else {
			Toast.makeText(mContext, "Insertion Error", 2000).show();
			return false;
		}

	}

	public boolean userAccount_retrieval(String userName, String pwd) {

		boolean a = false;
		Log.d(" ****", "database retreival ");

		// Cursor cur1 = mDb.rawQuery("select * from "
		// + DatabaseHelper.TABLE_USER_REGISTRATION + "  where emailid ='"
		// + userName + "' and password = '" + pwd + "'", null);

		String selectQuery = "SELECT  * FROM  "
				+ DatabaseHelper.TABLE_USER_REGISTRATION
				+ " WHERE emailid = ?  and password = ?";

		String[] args = new String[] { userName, pwd };

		Cursor cur1 = mDb.rawQuery(selectQuery, args);
		Log.d("Count -----------------" + "select * from "
				+ DatabaseHelper.TABLE_USER_REGISTRATION + "  where emailid ='"
				+ userName + "' and password = '" + pwd + "'", ":----------"
				+ cur1.getCount());
		if (cur1.moveToNext()) {

			Log.d("retreive --", "user exists");
			user_mobileno = cur1.getString(cur1.getColumnIndex("Phone"));
			user_landlineno = cur1.getString(cur1.getColumnIndex("Landline"));
			user_broadbandno = cur1.getString(cur1.getColumnIndex("NetcardNo"));

			Log.d("user details retrieval*****", "name--" + user_name
					+ "mail id--" + user_mail + "pwd--" + user_pwd);

			login_editor = main_preferences.edit();
			login_editor.putString("userphone", user_mobileno);
			login_editor.putString("userlandline", user_landlineno);
			login_editor.putString("userbroadband", user_broadbandno);
			login_editor.commit();
			a = true;
			// return true;
		} else {
			a = false;
			// cur1.close();
			// return false;
		}
		cur1.close();
		return a;

	}

	public boolean bill_insertion(String mailid, String accntNo,
			String accntType,String plan, String amnt, String billdate) {
		ContentValues cv = new ContentValues();
		cv.put(DatabaseHelper.EMAIL_ID, mailid);
		cv.put(DatabaseHelper.USER_ACCOUNT, accntNo);
		cv.put(DatabaseHelper.MODE_TYPE, accntType);
		cv.put(DatabaseHelper.USER_PLAN, plan);
		cv.put(DatabaseHelper.AMOUNT, amnt);
		cv.put(DatabaseHelper.BILL_DATE, billdate);
		if ((mDb.insert(DatabaseHelper.TABLE_BILLPAY, null, cv)) != -1) {
			Toast.makeText(mContext, "Bill payment successfull", 2000).show();
			Log.d("testadapter--bill payment successfull********",
					"bill-information saved");
			return true;
		}

		else {
			Toast.makeText(mContext, "bill-pay:Insertion Error", 2000).show();
			return false;
		}
	}

	public ArrayList<RequestsBean> bill_history(String mailid) {

		ArrayList<RequestsBean> req_list = new ArrayList<RequestsBean>();
		String selectQuery = "SELECT  * FROM  " + DatabaseHelper.TABLE_BILLPAY
				+ " WHERE emailid = ?";

		String[] args = new String[] { mailid };
		// SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = mDb.rawQuery(selectQuery, args);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				RequestsBean req_bean = new RequestsBean();
				String name = cursor
						.getString(cursor.getColumnIndex("Emailid"));
				String acnt_no = cursor.getString(cursor
						.getColumnIndex("Account_no"));
				String type = cursor.getString(cursor.getColumnIndex("Type"));
				String plan = cursor.getString(cursor.getColumnIndex("Plan"));
				String bill_amount = cursor.getString(cursor
						.getColumnIndex("Amount"));
				String bill_date = cursor.getString(cursor
						.getColumnIndex("Billdate"));
				

				req_bean.setName(name);
				req_bean.setAccnt_no(acnt_no);
				req_bean.setType(type);
				req_bean.setPlan(plan);
				req_bean.setBillAmount(bill_amount);
				req_bean.setBillDate(bill_date);
				req_list.add(req_bean);

				// System.out.println(a);
			} while (cursor.moveToNext());
		}

		// closing connection
		cursor.close();
		return req_list;

	}

	public void delete() {
		// mDb.execSQL("delete from LANGUAGES_TABLE");
		// mDb.execSQL("delete from Genres_Table");
		// mDb.execSQL("delete from VOD_Dates_Table");
		Log.d("*********************************", "Deleted all the tables");

	}

}
