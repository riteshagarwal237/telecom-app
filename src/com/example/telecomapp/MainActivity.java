package com.example.telecomapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.database.DatabaseHelper;
import com.database.TestAdapter;

public class MainActivity extends Activity implements OnClickListener {

	EditText userText, pwdText;
	private String userName, pwd;
	// public static String user_mail, user_name, user_pwd;
	AlertDialog show;
	private SharedPreferences main_preferences;
	Editor login_editor;
	TestAdapter db_testAdapter;
	public static boolean login_retrieval;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		main_preferences = PreferenceManager.getDefaultSharedPreferences(this
				.getApplicationContext());
		userText = (EditText) findViewById(R.id.email_editText);
		pwdText = (EditText) findViewById(R.id.pwd_editText);
		Button loginButton = (Button) findViewById(R.id.loginbutton);
		loginButton.setOnClickListener(this);
		Button newUserButton = (Button) findViewById(R.id.newuserbutton);
		newUserButton.setOnClickListener(this);

		// db
		// db = new DatabaseHelper(this);
		// sq = db.getWritableDatabase();

		// db_testAdapter = new TestAdapter(MainActivity.this);
		// db_testAdapter.createDatabase();
		// db_testAdapter.open();
		// db_testAdapter.serviceplans_insertion();
		// db_testAdapter.close();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (!main_preferences.getString("usermailid", "").isEmpty()) {
			// startActivity(new Intent(this, HomePage.class));
			startActivity(new Intent(this, HomePageTest.class));
			finish();
		} else {

		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.loginbutton:
			// check and reditect to home page

			userName = userText.getText().toString();
			pwd = pwdText.getText().toString();
			Log.d("user-name*****", userName);
			Log.d("user-pwd*****", pwd);

			if (userName.equalsIgnoreCase("") || pwd.equalsIgnoreCase("")) {
				Toast.makeText(this, "All Fields Required.", Toast.LENGTH_SHORT)
						.show();
			} else if (userName.equalsIgnoreCase("admin")
					&& pwd.equalsIgnoreCase("admin")) {
				// redirct to admin page..show list of service requests and
				// adding services by admin
				startActivity(new Intent(this, AdminPage.class));
				finish();

			} else {
				// retrieveData(userName, pwd);
				db_testAdapter = new TestAdapter(MainActivity.this);
				db_testAdapter.createDatabase();
				db_testAdapter.open();
				login_retrieval = db_testAdapter.user_login(userName, pwd);
				db_testAdapter.close();

				if (login_retrieval) {
					// startActivity(new Intent(this, HomePage.class));
					startActivity(new Intent(this, HomePageTest.class));
					finish();
				} else {
					show = new AlertDialog.Builder(this).setTitle("Oops..!")
							.setMessage("invalid login details")
							.setPositiveButton("OK", null).show();
				}

			}

			// retrieveData(userName, pwd);
			// startActivity(new Intent(this, HomePage.class));

			break;

		case R.id.newuserbutton:
			// registration page..insert values into DB
			startActivity(new Intent(this, RegistrationActivity.class));

			break;

		default:
			break;
		}

	}

}
