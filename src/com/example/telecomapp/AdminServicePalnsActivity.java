package com.example.telecomapp;

import com.database.TestAdapter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class AdminServicePalnsActivity extends Activity implements OnClickListener {

	EditText plandetails_editText;
	Spinner plantype_spinner;
	Button addplanButton;
	String[] servicePlantypes = { "Cellular", "Landline", "Broadband" };

	Service_plantypeAdapter adapter;
	String plantype;
	TestAdapter db_testAdapter;
	private SharedPreferences admin_preferences;
	Editor admin_editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_service_palns);
		admin_preferences = PreferenceManager.getDefaultSharedPreferences(this
				.getApplicationContext());
		plandetails_editText = (EditText) findViewById(R.id.admin_plandetails);
		plantype_spinner = (Spinner) findViewById(R.id.admin_plantypeSpinner);
		addplanButton = (Button) findViewById(R.id.admin_addplanbutton);

		addplanButton.setOnClickListener(this);
		adapter = new Service_plantypeAdapter();
		plantype_spinner.setAdapter(adapter);

		plantype_spinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						plantype = servicePlantypes[position].toString();
						Log.d("selected planType---", plantype);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}

				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_service_palns, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.admin_addplanbutton:
			// insert plans into DB
			Log.d("inserting----", plandetails_editText.getText().toString()
					+ "type--" + plantype);

			db_testAdapter = new TestAdapter(this);
			db_testAdapter.createDatabase();
			db_testAdapter.open();
			db_testAdapter.serviceplans_insertion1(plandetails_editText
					.getText().toString(), plantype);
			db_testAdapter.close();
			
			plandetails_editText.setText("");
			break;

		default:
			break;
		}

	}
	public class Service_plantypeAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return servicePlantypes.length;
		}

		@Override
		public Object getItem(int position) {

			return position;
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			convertView = LayoutInflater.from(AdminServicePalnsActivity.this).inflate(
					R.layout.spinner_text, null);
			TextView text1 = (TextView) convertView
					.findViewById(R.id.spinnertext1);

			text1.setText(servicePlantypes[position]);
			Log.d("servicePlan--spinner type", "Data : " + text1.toString());
			return convertView;
		}

	}
}
