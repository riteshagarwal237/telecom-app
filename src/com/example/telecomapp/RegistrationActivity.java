package com.example.telecomapp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.database.DatabaseHelper;
import com.database.TestAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends Activity implements OnClickListener {

	EditText registration_nameText, registration_emailText,
			registration_pwdText, registration_confirmpwdText;
	Button registerButton;
	public boolean emailcheck, reg_check;
	public String name, emailId, password;
	private String dob;
	AlertDialog show;
	TestAdapter db_testAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);

		registration_nameText = (EditText) findViewById(R.id.signup__nameEditText);
		registration_emailText = (EditText) findViewById(R.id.signup__emailEditText);
		registration_pwdText = (EditText) findViewById(R.id.signup_passwordeditText);
		registration_confirmpwdText = (EditText) findViewById(R.id.signup_confirmpasswordeditText);
		registerButton = (Button) findViewById(R.id.signup_registerButton);
		registerButton.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration, menu);
		return true;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.signup_registerButton:

			// insert values into DB

			name = ((EditText) findViewById(R.id.signup__nameEditText))
					.getText().toString();
			emailId = ((EditText) findViewById(R.id.signup__emailEditText))
					.getText().toString();
			password = ((EditText) findViewById(R.id.signup_passwordeditText))
					.getText().toString();

			if (name.length() == 0 || emailId.length() == 0
					|| password.length() == 0) {

				// Toast.makeText(this, "All Fields Required.",
				// Toast.LENGTH_SHORT).show();
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setCancelable(false)
						.setMessage("Please fill all fields")
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// All of the fun happens inside the
										// CustomListener now.
										// I had to move it to enable data
										// validation.
									}
								});
				AlertDialog alertDialog = builder.create();
				alertDialog.show();

			}

			else if (checkemail(emailId) != true) {

				// Toast.makeText(this, "Please enter valid email address.",
				show = new AlertDialog.Builder(this).setTitle("Oops..!")
						.setMessage("enter valid email addresss")
						.setPositiveButton("OK", null).show();
			}

			else if (password.length() < 4 || password.length() > 15) {
				// Toast.makeText(this, "password length must be 4 characters.",
				// Toast.LENGTH_SHORT).show();
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setCancelable(false)
						.setMessage("password length must be 4-15")
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// All of the fun happens inside the
										// CustomListener now.
										// I had to move it to enable data
										// validation.
									}
								});
				AlertDialog alertDialog = builder.create();
				alertDialog.show();
			} else {
				// DatabaseHelper db = new DatabaseHelper(this);
				// SQLiteDatabase sq = db.getWritableDatabase();
				// ContentValues cv = new ContentValues();
				// cv.put(DatabaseHelper.NAME, name);
				// cv.put(DatabaseHelper.EMAIL_ID, emailId);
				// cv.put(DatabaseHelper.PASSWORD, password);
				// cv.put(DatabaseHelper.PHONE, "");
				// cv.put(DatabaseHelper.BROADBAND_NO, "");
				// cv.put(DatabaseHelper.LANDLINE_NO, "");
				// if ((sq.insert(DatabaseHelper.TABLE_USER_REGISTRATION, null,
				// cv)) != -1) {
				// Toast.makeText(this, "Registration successfull", 2000)
				// .show();
				// Intent intent = new Intent(this, MainActivity.class);
				// startActivity(intent);
				// finish();
				// }
				//
				// else
				// Toast.makeText(this, "Insertion Error", 2000).show();
				// db.close();

				// updated
				db_testAdapter = new TestAdapter(this);
				db_testAdapter.createDatabase();
				db_testAdapter.open();
				reg_check = db_testAdapter.user_registration(name, emailId,
						password);
				db_testAdapter.close();

				if (reg_check) {
					Toast.makeText(RegistrationActivity.this,
							"Registration successfull", 2000).show();
					Intent intent = new Intent(RegistrationActivity.this,
							MainActivity.class);
					startActivity(intent);
					finish();
				}

			}

			break;

		default:
			break;
		}
	}

	public boolean checkemail(String emailId) {
		Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher matcher = pattern.matcher(emailId);
		emailcheck = matcher.matches();
		return emailcheck;
	}

}
