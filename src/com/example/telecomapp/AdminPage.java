package com.example.telecomapp;

import com.database.TestAdapter;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AdminPage extends Activity implements OnClickListener {

	Button serviceplan_button, servicerequests_button;
	private SharedPreferences admin_preferences;
	Editor admin_editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_page);
		admin_preferences = PreferenceManager.getDefaultSharedPreferences(this
				.getApplicationContext());
		serviceplan_button = (Button) findViewById(R.id.admin_serviceplanbutton);
		servicerequests_button = (Button) findViewById(R.id.admin_servicerequestbutton);

		serviceplan_button.setOnClickListener(this);
		servicerequests_button.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_logout:

			if (MainActivity.login_retrieval) {
				admin_editor = admin_preferences.edit();
				admin_editor.putString("usermailid", "");
				admin_editor.putString("userpwd", "");
				admin_editor.commit();
				Toast.makeText(this, "You have succesfully Logged Out",
						Toast.LENGTH_SHORT).show();

				startActivity(new Intent(this, MainActivity.class));
				finish();
			}
			return true;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.admin_serviceplanbutton:
			startActivity(new Intent(this, AdminServicePalnsActivity.class));
			break;

		case R.id.admin_servicerequestbutton:
			startActivity(new Intent(this, AdminServiceRequestsActivity.class));
			break;
		default:
			break;
		}

	}

}
