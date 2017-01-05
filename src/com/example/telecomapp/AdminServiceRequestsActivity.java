package com.example.telecomapp;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.database.TestAdapter;

public class AdminServiceRequestsActivity extends Activity {

	private ListView req_listview;
	ServiceRequests_adapter adapter;
	TestAdapter db_testAdapter;

	ArrayList<RequestsBean> servicerequests_list = new ArrayList<RequestsBean>();
	int req_activation;
	String type;// to activate particular accnt

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_service_requests);
		req_listview = (ListView) findViewById(R.id.servicereq_listview);

		// retrieve from DB
		db_testAdapter = new TestAdapter(this);
		db_testAdapter.createDatabase();
		db_testAdapter.open();
		servicerequests_list = db_testAdapter.serviceReqs_retrieval();
		db_testAdapter.close();
		adapter = new ServiceRequests_adapter();
		req_listview.setAdapter(adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_service_requests, menu);
		return true;
	}

	public class ServiceRequests_adapter extends BaseAdapter {

		@Override
		public int getCount() {

			return servicerequests_list.size();
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

			convertView = LayoutInflater
					.from(AdminServiceRequestsActivity.this).inflate(
							R.layout.servicereq_customlistview, null);
			TextView nameTV = (TextView) convertView
					.findViewById(R.id.servicereq_nametextview);
			TextView accntNoTV = (TextView) convertView
					.findViewById(R.id.servicereq_accntnotextview);
			TextView planTV = (TextView) convertView
					.findViewById(R.id.servicereq_plantetextview);
			TextView typeTV = (TextView) convertView
					.findViewById(R.id.servicereq_typetextview);
			final Button activateButton = (Button) convertView
					.findViewById(R.id.activatebutton);
			activateButton.setId(position);
			activateButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.e("activate button click********",
							"activate button click********");

					type = servicerequests_list.get(v.getId()).getType();
					Log.d("ACCNT TYPE--pos", type + " accnt no--"
							+ servicerequests_list.get(v.getId()).getAccnt_no());
					// update request table
					db_testAdapter = new TestAdapter(
							AdminServiceRequestsActivity.this);
					db_testAdapter.createDatabase();
					db_testAdapter.open();
					req_activation = db_testAdapter.serviceReqs_activate(type);
					db_testAdapter.close();

					if (req_activation == 1) {
						activateButton.setClickable(false);
						activateButton.setText("Activated");
						Toast.makeText(
								AdminServiceRequestsActivity.this,
								"Your request has activated for account***"
										+ servicerequests_list.get(v.getId())
												.getAccnt_no(), Toast.LENGTH_LONG)
								.show();

					} else {

						Toast.makeText(AdminServiceRequestsActivity.this,
								"Your request not activated", Toast.LENGTH_LONG)
								.show();
					}
				}
			});
			//
			nameTV.setText(servicerequests_list.get(position).getName());
			accntNoTV.setText(servicerequests_list.get(position).getAccnt_no());
			planTV.setText(servicerequests_list.get(position).getPlan());
			typeTV.setText(servicerequests_list.get(position).getType());
			// convertView.setId(position);
			return convertView;
		}

	}


}
