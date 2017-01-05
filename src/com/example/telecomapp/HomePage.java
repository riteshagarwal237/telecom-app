package com.example.telecomapp;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.json.JSONException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.database.TestAdapter;
import com.example.telecomapp.AdminServiceRequestsActivity.ServiceRequests_adapter;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

public class HomePage extends Activity implements OnCheckedChangeListener,
		OnClickListener {

	private static final String CONFIG_ENVIRONMENT = PaymentActivity.ENVIRONMENT_SANDBOX;

	// note that these credentials will differ between live & sandbox
	// environments.
	// private static final String CONFIG_CLIENT_ID =
	// "credential from developer.paypal.com";

	private static final String CONFIG_CLIENT_ID = "ASQoUhDEb8-hv5RoQMULwd2bOLxFJ5Sz7jPZSQLx-3gKJJ6OX3xiD89METjg";
	// when testing in sandbox, this is likely the -facilitator email address.
	// private static final String CONFIG_RECEIVER_EMAIL =
	// "matching paypal email address";
	private static final String CONFIG_RECEIVER_EMAIL = "divya.vatnala-facilitator@gmail.com";

	SegmentedRadioGroup segmentText;
	String segment_type, accntdetails_plantype, bill_accnt_no, accnt_no/*
																		 * to
																		 * insert
																		 * in
																		 * request
																		 * table
																		 */;
	ListView billHistory_listview;
	private SharedPreferences home_preferences;
	Editor home_editor;
	private String cellular_phone, land_no, broadband_no;
	TextView /* addAccount_TV */requestSentTV, billpay_nameTV,
			billpay_mobilenoTV, billpay_landlinenoTV, billpay_broadbandnoTV,
			billpay_planTV, billHistory_nameTV, billHistory_accntNoTV,
			billHistory_planTV, billHistory_planTypeTV, billHistory_billdateTV,
			billHistory_billAmountTV;
	LinearLayout accountDetails_layout, billPayment_layout, billHistory_layout,
			paymentHistoryDetails_layout, billpay_mobileLayout,
			billpay_landlineLayout, billpay_broadbandLayout,
			accntdetails_mobileLayout, accntdetails_landlineLayout,
			accntdetails_broadbandLayout;
	Button addAccount_button, accntdetails_addButton, billpay_button,
			paymentButton, historyButton;
	EditText accntdetailss_mobilenoEditText, accntdetailss_landlinenoEditText,
			accntdetailss_broadbandnoEditText, /*
												 * billpay_nameEditText,
												 * billpay_mobilenoEditText,
												 * billpay_landlinenoEditText,
												 * billpay_broadbandnoEditText,
												 */billpay_dateEditText,
			billpay_amountEditText;
	TestAdapter db_testAdapter;
	int accnt_activation, accnt_update;
	boolean billinsertion_val, accntdetails_insertion, accntdetails_retrieve,
			accntdetails_insert /*
								 * addAccnt_click_phone = true,
								 * addAccnt_click_lande = true,
								 * addAccnt_click_broadband = true
								 */; /*
									 * to make ext gone or dull once adding
									 * accnt
									 */;
	Spinner accntdetails_planSpinner;
	Accntdetails_serviceplanAdapter adapter;

	ArrayList<String> accntdetails_planslist = new ArrayList<String>();
	ArrayList<RequestsBean> billHistory_list = new ArrayList<RequestsBean>();
	BillHistory_adapter history_adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_segment);
		home_preferences = PreferenceManager.getDefaultSharedPreferences(this
				.getApplicationContext());
		segment_type = "Cellular";
		segmentText = (SegmentedRadioGroup) findViewById(R.id.segment_text);
		// addAccount_TV = (TextView)
		// findViewById(R.id.home_addaccountTextview);
		requestSentTV = (TextView) findViewById(R.id.home_requestsentTV);
		addAccount_button = (Button) findViewById(R.id.home_addaccountButton);
		accountDetails_layout = (LinearLayout) findViewById(R.id.home_userAccountdetails);
		billPayment_layout = (LinearLayout) findViewById(R.id.home_billpayment);
		billHistory_layout = (LinearLayout) findViewById(R.id.home_billhistory);
		paymentHistoryDetails_layout = (LinearLayout) findViewById(R.id.home_paymenthistory);
		paymentButton = (Button) findViewById(R.id.home_paymentbutton);
		historyButton = (Button) findViewById(R.id.home_accntHistorybutton);
		// account details
		accntdetailss_mobilenoEditText = (EditText) findViewById(R.id.accoundetails_mobilenoEditText);
		accntdetailss_landlinenoEditText = (EditText) findViewById(R.id.accoundetails_landlinenoEditText);
		accntdetailss_broadbandnoEditText = (EditText) findViewById(R.id.accoundetails_broadbandnoEditText);
		accntdetails_planSpinner = (Spinner) findViewById(R.id.accntdetails_plantypespinner);
		accntdetails_addButton = (Button) findViewById(R.id.accntdetails_addbutton);
		accntdetails_mobileLayout = (LinearLayout) findViewById(R.id.accoundetails_mobileLayout);
		accntdetails_landlineLayout = (LinearLayout) findViewById(R.id.accoundetails_landlineLayout);
		accntdetails_broadbandLayout = (LinearLayout) findViewById(R.id.accoundetails_broadbandLayout);

		accntdetails_addButton.setOnClickListener(this);
		adapter = new Accntdetails_serviceplanAdapter();
		accntdetails_planSpinner.setAdapter(adapter);
		// bill payment

		// billpay_nameEditText = (EditText)
		// findViewById(R.id.bill_nameEditText);
		// billpay_mobilenoEditText = (EditText)
		// findViewById(R.id.bill_mobilenoEditText);
		// billpay_landlinenoEditText = (EditText)
		// findViewById(R.id.bill_landlinenoEditText);
		// billpay_broadbandnoEditText = (EditText)
		// findViewById(R.id.bill_broadbandnoEditText);
		billpay_nameTV = (TextView) findViewById(R.id.billpay_nametextview);
		billpay_mobilenoTV = (TextView) findViewById(R.id.billpay_mobilenoTextview);
		billpay_landlinenoTV = (TextView) findViewById(R.id.billpay_landlinenoTextview);
		billpay_broadbandnoTV = (TextView) findViewById(R.id.billpay_broadbandnoTextview);
		billpay_planTV = (TextView) findViewById(R.id.billpay_userplanTextview);
		billpay_dateEditText = (EditText) findViewById(R.id.bill_dateEditText);
		billpay_amountEditText = (EditText) findViewById(R.id.bill_amountEditText);
		billpay_button = (Button) findViewById(R.id.bill_paybutton);
		billpay_mobileLayout = (LinearLayout) findViewById(R.id.billpay_mobilelayout);
		billpay_landlineLayout = (LinearLayout) findViewById(R.id.billpay_landlinelayout);
		billpay_broadbandLayout = (LinearLayout) findViewById(R.id.billpay_broadbandlayout);
		billpay_button.setOnClickListener(this);

		segmentText.setOnCheckedChangeListener(this);
		addAccount_button.setOnClickListener(this);
		paymentButton.setOnClickListener(this);
		historyButton.setOnClickListener(this);

		// bill history

		billHistory_listview = (ListView) findViewById(R.id.billhistory_listview);
		billHistory_nameTV = (TextView) findViewById(R.id.history_nametextview);
		billHistory_accntNoTV = (TextView) findViewById(R.id.history_accntnotextview);
		billHistory_planTV = (TextView) findViewById(R.id.history_plantetextview);
		billHistory_planTypeTV = (TextView) findViewById(R.id.history_typetextview);
		billHistory_billdateTV = (TextView) findViewById(R.id.history_billdatetextview);
		billHistory_billAmountTV = (TextView) findViewById(R.id.history_amountTextview);

		accntdetails_planSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {

						accntdetails_plantype = accntdetails_planslist
								.get(position);
						Log.d("selected planType---", accntdetails_plantype);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		// segment checked code
		// regarding accnt activation
		db_testAdapter = new TestAdapter(HomePage.this);
		db_testAdapter.createDatabase();
		db_testAdapter.open();
		// accnt_activation = db_testAdapter
		// .activeaccnt_retrieval(home_preferences.getString(
		// "username", ""));
		accnt_activation = db_testAdapter.activeaccnt_retrieval(
				home_preferences.getString("usermailid", ""), segment_type);
		db_testAdapter.close();

		Log.e("accnt  activation*****", "" + accnt_activation + " TYPE--"
				+ home_preferences.getString("accntType", ""));
		if (accnt_activation == 1
				&& home_preferences.getString("accntType", "")
						.equalsIgnoreCase("Cellular")) {
			// if (accnt_activation == 1) {
			Toast.makeText(HomePage.this, "Account  activated",
					Toast.LENGTH_LONG).show();

			// update user table
			db_testAdapter = new TestAdapter(HomePage.this);
			db_testAdapter.createDatabase();
			db_testAdapter.open();
			accnt_update = db_testAdapter.userAccount_configure(
					home_preferences.getString("accntNo", ""),
					home_preferences.getString("accntType", ""));
			db_testAdapter.close();
			//
			if (accnt_update == 1) {
				Toast.makeText(HomePage.this, "Account updated successfully",
						Toast.LENGTH_LONG).show();
				// billPayment_layout.setVisibility(View.VISIBLE);
				paymentHistoryDetails_layout.setVisibility(View.VISIBLE);
				addAccount_button.setVisibility(View.GONE);

			} else {
				// ACCNT NOT ACTIVATED
				// billPayment_layout.setVisibility(View.GONE);
				// paymentHistoryDetails_layout.setVisibility(View.GONE);

				addAccount_button.setVisibility(View.GONE);
				requestSentTV.setVisibility(View.VISIBLE);
				accountDetails_layout.setVisibility(View.GONE);
				billPayment_layout.setVisibility(View.GONE);
				billHistory_layout.setVisibility(View.GONE);
				paymentHistoryDetails_layout.setVisibility(View.GONE);
			}
		} else if (accnt_activation == 0) {
			// acnt not activated
			// ACNT NOT INSERTED

			if (accnt_update == 1) {
				// activated
				Toast.makeText(HomePage.this, "Account updated successfully",
						Toast.LENGTH_LONG).show();
				// billPayment_layout.setVisibility(View.VISIBLE);
				paymentHistoryDetails_layout.setVisibility(View.VISIBLE);
				addAccount_button.setVisibility(View.GONE);

			} else {
				// ACCNT NOT ACTIVATED
				// billPayment_layout.setVisibility(View.GONE);
				// paymentHistoryDetails_layout.setVisibility(View.GONE);

				addAccount_button.setVisibility(View.GONE);
				requestSentTV.setVisibility(View.VISIBLE);
				accountDetails_layout.setVisibility(View.GONE);
				billPayment_layout.setVisibility(View.GONE);
				billHistory_layout.setVisibility(View.GONE);
				paymentHistoryDetails_layout.setVisibility(View.GONE);
			}

			// addAccount_button.setVisibility(View.GONE);
			// requestSentTV.setVisibility(View.VISIBLE);
			// } else {
			//
			// if (addAccnt_click_phone) {
			// addAccount_button.setVisibility(View.GONE);
			// requestSentTV.setVisibility(View.VISIBLE);
			// accountDetails_layout.setVisibility(View.GONE);
			// billPayment_layout.setVisibility(View.GONE);
			// billHistory_layout.setVisibility(View.GONE);
			// paymentHistoryDetails_layout.setVisibility(View.GONE);
			// }
			// else if (home_preferences.getBoolean("acntinsertion", false) ==
			// true
			// || addAccnt_click_phone == false) {
			// // request sent and not activated and when user navigating in
			// // segments after clicking add acnt button in 1 segmnt
			// addAccount_button.setVisibility(View.GONE);
			// requestSentTV.setVisibility(View.VISIBLE);
			//
			// }
			// else {
			// addAccount_button.setVisibility(View.VISIBLE);
			// requestSentTV.setVisibility(View.GONE);
			// }

			// else {
			// addAccount_button.setVisibility(View.GONE);
			// requestSentTV.setVisibility(View.VISIBLE);
			// }
			Toast.makeText(HomePage.this,
					"Account not activated ..Please wait", Toast.LENGTH_LONG)
					.show();
			// }// END OF ELSE

		}

		else {

		}
		// service plans insertion default
		// insert plans into DB
		Log.d("inserting----", "plans detault");

		db_testAdapter = new TestAdapter(this);
		db_testAdapter.createDatabase();
		db_testAdapter.open();
		db_testAdapter.serviceplans_insertion();
		db_testAdapter.close();

		Intent intent = new Intent(this, PayPalService.class);

		intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT,
				CONFIG_ENVIRONMENT);
		intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, CONFIG_CLIENT_ID);
		intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL,
				CONFIG_RECEIVER_EMAIL);

		startService(intent);

		generateNotification(HomePage.this, "Telecom App");
	}

	public void generateNotification(Context context, String message) {

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher,
				"Telecom SelfService", System.currentTimeMillis());
		// Hide the notification after its selected
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		Intent intent;

		intent = new Intent(context, MainActivity.class);
		// intent.putExtra("pushNoti", "pushNoti");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, "Telecom App", message,
				pendingIntent);

		notification.defaults = Notification.DEFAULT_SOUND;
		notificationManager.notify(0, notification);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_details, menu);

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// MenuItem register = menu.findItem(R.id.menu_login);
		// Log.d("on prepareMenu ******fb session", "" + session_var);
		// if (!session_var && !mTwitter.isTwitterLoggedInAlready()) {
		// register.setVisible(true);
		// } else {
		//
		// register.setVisible(false);
		//
		// }
		// MenuItem register1 = menu.findItem(R.id.menu_logout);
		//
		//
		// if (session_var || mTwitter.isTwitterLoggedInAlready()) {
		// register1.setVisible(true);
		//
		// } else {
		// register1.setVisible(false);
		// }

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_logout:

			if (MainActivity.login_retrieval) {
				home_editor = home_preferences.edit();
				home_editor.putString("usermailid", "");
				home_editor.putString("userpwd", "");
				home_editor.commit();
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
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (group == segmentText) {

			if (checkedId == R.id.button_cellular) {

				segment_type = "Cellular";
				// addAccnt_click_phone = true;
				// if (addAccnt_click) {
				//
				// addAccount_TV.setVisibility(View.VISIBLE);
				// } else {
				// addAccount_TV.setVisibility(View.GONE);
				// }

				// regarding accnt activation
				db_testAdapter = new TestAdapter(HomePage.this);
				db_testAdapter.createDatabase();
				db_testAdapter.open();
				// accnt_activation = db_testAdapter
				// .activeaccnt_retrieval(home_preferences.getString(
				// "username", ""));
				accnt_activation = db_testAdapter.activeaccnt_retrieval(
						home_preferences.getString("usermailid", ""),
						segment_type);
				db_testAdapter.close();

				Log.e("accnt  activation*****",
						"" + accnt_activation + " TYPE--"
								+ home_preferences.getString("accntType", ""));
				if (accnt_activation == 1
						&& home_preferences.getString("accntType", "")
								.equalsIgnoreCase("Cellular")) {
					Toast.makeText(HomePage.this, "Account  activated",
							Toast.LENGTH_LONG).show();

					// update user table

					// update user table
					db_testAdapter = new TestAdapter(HomePage.this);
					db_testAdapter.createDatabase();
					db_testAdapter.open();
					accnt_update = db_testAdapter.userAccount_configure(
							home_preferences.getString("accntNo", ""),
							home_preferences.getString("accntType", ""));
					db_testAdapter.close();
					//
					if (accnt_update == 1) {
						Toast.makeText(HomePage.this,
								"Account updated successfully",
								Toast.LENGTH_LONG).show();
						paymentHistoryDetails_layout
								.setVisibility(View.VISIBLE);
						addAccount_button.setVisibility(View.GONE);

					} else {
						// ACCNT NOT ACTIVATED
						// paymentHistoryDetails_layout.setVisibility(View.GONE);
						// addAccount_button.setVisibility(View.VISIBLE);

						addAccount_button.setVisibility(View.GONE);
						requestSentTV.setVisibility(View.VISIBLE);
						accountDetails_layout.setVisibility(View.GONE);
						billPayment_layout.setVisibility(View.GONE);
						billHistory_layout.setVisibility(View.GONE);
						paymentHistoryDetails_layout.setVisibility(View.GONE);
					}
				} else if (accnt_activation == 0) {
					// ACNT NOT INSERTED

					if (accnt_update == 1) {
						// activated
						Toast.makeText(HomePage.this,
								"Account updated successfully",
								Toast.LENGTH_LONG).show();
						// billPayment_layout.setVisibility(View.VISIBLE);
						paymentHistoryDetails_layout
								.setVisibility(View.VISIBLE);
						addAccount_button.setVisibility(View.GONE);

					} else {
						// ACCNT NOT ACTIVATED
						// billPayment_layout.setVisibility(View.GONE);
						// paymentHistoryDetails_layout.setVisibility(View.GONE);

						addAccount_button.setVisibility(View.GONE);
						requestSentTV.setVisibility(View.VISIBLE);
						accountDetails_layout.setVisibility(View.GONE);
						billPayment_layout.setVisibility(View.GONE);
						billHistory_layout.setVisibility(View.GONE);
						paymentHistoryDetails_layout.setVisibility(View.GONE);
					}

					// addAccount_button.setVisibility(View.GONE);
					// requestSentTV.setVisibility(View.VISIBLE);
					// } else {

					// if (addAccnt_click_phone) {

					// addAccount_button.setVisibility(View.VISIBLE);
					// requestSentTV.setVisibility(View.GONE);
					// accountDetails_layout.setVisibility(View.GONE);
					// billPayment_layout.setVisibility(View.GONE);
					// billHistory_layout.setVisibility(View.GONE);
					// paymentHistoryDetails_layout.setVisibility(View.GONE);

					// }
					// else if (home_preferences.getBoolean("acntinsertion",
					// false) == true || addAccnt_click_phone == false) {
					// addAccount_button.setVisibility(View.GONE);
					// requestSentTV.setVisibility(View.VISIBLE);
					// } else {
					// addAccount_button.setVisibility(View.VISIBLE);
					// requestSentTV.setVisibility(View.GONE);
					// }

					// else {
					// addAccount_button.setVisibility(View.GONE);
					// requestSentTV.setVisibility(View.VISIBLE);
					// }
					Toast.makeText(HomePage.this,
							"Account not activated ..Please wait",
							Toast.LENGTH_LONG).show();
					// }//END OF ELSE
				}

			} else if (checkedId == R.id.button_landline) {
				segment_type = "Landline";
				// addAccnt_click_lande = true;
				// regarding accnt activation
				db_testAdapter = new TestAdapter(HomePage.this);
				db_testAdapter.createDatabase();
				db_testAdapter.open();
				accnt_activation = db_testAdapter.activeaccnt_retrieval(
						home_preferences.getString("usermailid", ""),
						segment_type);
				db_testAdapter.close();

				Log.e("accnt  activation*****",
						"" + accnt_activation + " TYPE--"
								+ home_preferences.getString("accntType", ""));
				if (accnt_activation == 1
						&& home_preferences.getString("accntType", "")
								.equalsIgnoreCase("Landline")) {
					Toast.makeText(HomePage.this, "Account  activated",
							Toast.LENGTH_LONG).show();

					// update user table
					db_testAdapter = new TestAdapter(HomePage.this);
					db_testAdapter.createDatabase();
					db_testAdapter.open();
					accnt_update = db_testAdapter.userAccount_configure(
							home_preferences.getString("accntNo", ""),
							home_preferences.getString("accntType", ""));
					db_testAdapter.close();
					//
					if (accnt_update == 1) {
						Toast.makeText(HomePage.this,
								"Account updated successfully",
								Toast.LENGTH_LONG).show();
						paymentHistoryDetails_layout
								.setVisibility(View.VISIBLE);
						addAccount_button.setVisibility(View.GONE);
					} else {
						// ACCNT NOT ACTIVATED

						// paymentHistoryDetails_layout.setVisibility(View.GONE);
						// addAccount_button.setVisibility(View.VISIBLE);

						addAccount_button.setVisibility(View.GONE);
						requestSentTV.setVisibility(View.VISIBLE);
						accountDetails_layout.setVisibility(View.GONE);
						billPayment_layout.setVisibility(View.GONE);
						billHistory_layout.setVisibility(View.GONE);
						paymentHistoryDetails_layout.setVisibility(View.GONE);
					}
				} else if (accnt_activation == 0) {
					// ACNT NOT INSERTED

					if (accnt_update == 1) {
						// activated
						Toast.makeText(HomePage.this,
								"Account updated successfully",
								Toast.LENGTH_LONG).show();
						// billPayment_layout.setVisibility(View.VISIBLE);
						paymentHistoryDetails_layout
								.setVisibility(View.VISIBLE);
						addAccount_button.setVisibility(View.GONE);

					} else {
						// ACCNT NOT ACTIVATED
						// billPayment_layout.setVisibility(View.GONE);
						// paymentHistoryDetails_layout.setVisibility(View.GONE);

						addAccount_button.setVisibility(View.GONE);
						requestSentTV.setVisibility(View.VISIBLE);
						accountDetails_layout.setVisibility(View.GONE);
						billPayment_layout.setVisibility(View.GONE);
						billHistory_layout.setVisibility(View.GONE);
						paymentHistoryDetails_layout.setVisibility(View.GONE);
					}
					// addAccount_button.setVisibility(View.GONE);
					// requestSentTV.setVisibility(View.VISIBLE);
					// } else {

					// if (addAccnt_click_lande) {

					// addAccount_button.setVisibility(View.VISIBLE);
					// requestSentTV.setVisibility(View.GONE);
					// accountDetails_layout.setVisibility(View.GONE);
					// billPayment_layout.setVisibility(View.GONE);
					// billHistory_layout.setVisibility(View.GONE);
					// paymentHistoryDetails_layout.setVisibility(View.GONE);
					// }
					// else if (home_preferences.getBoolean("acntinsertion",
					// false) == true || addAccnt_click_lande == false) {
					// addAccount_button.setVisibility(View.GONE);
					// requestSentTV.setVisibility(View.VISIBLE);
					// } else {
					// addAccount_button.setVisibility(View.VISIBLE);
					// requestSentTV.setVisibility(View.GONE);
					// }

					// else {
					// addAccount_button.setVisibility(View.GONE);
					// requestSentTV.setVisibility(View.VISIBLE);
					// }

					Toast.makeText(HomePage.this,
							"Account not activated ..Please wait",
							Toast.LENGTH_LONG).show();
					// }//END OF ELSE

				}// /END IF ELSE IF

			} else if (checkedId == R.id.button_broadband) {
				segment_type = "Broadband";
				// addAccnt_click_broadband = true;
				// regarding accnt activation
				db_testAdapter = new TestAdapter(HomePage.this);
				db_testAdapter.createDatabase();
				db_testAdapter.open();
				// accnt_activation = db_testAdapter
				// .activeaccnt_retrieval(home_preferences.getString(
				// "username", ""));
				accnt_activation = db_testAdapter.activeaccnt_retrieval(
						home_preferences.getString("usermailid", ""),
						segment_type);
				db_testAdapter.close();

				Log.e("accnt  activation*****",
						"" + accnt_activation + " TYPE--"
								+ home_preferences.getString("accntType", ""));
				if (accnt_activation == 1
						&& home_preferences.getString("accntType", "")
								.equalsIgnoreCase("Broadband")) {
					Toast.makeText(HomePage.this, "Account  activated",
							Toast.LENGTH_LONG).show();

					// update user table
					db_testAdapter = new TestAdapter(HomePage.this);
					db_testAdapter.createDatabase();
					db_testAdapter.open();
					accnt_update = db_testAdapter.userAccount_configure(
							home_preferences.getString("accntNo", ""),
							home_preferences.getString("accntType", ""));
					db_testAdapter.close();
					//
					if (accnt_update == 1) {
						Toast.makeText(HomePage.this,
								"Account updated successfully",
								Toast.LENGTH_LONG).show();
						paymentHistoryDetails_layout
								.setVisibility(View.VISIBLE);
						addAccount_button.setVisibility(View.GONE);
					} else {
						// ACCNT NOT ACTIVATED

						// paymentHistoryDetails_layout.setVisibility(View.GONE);
						// addAccount_button.setVisibility(View.VISIBLE);

						addAccount_button.setVisibility(View.GONE);
						requestSentTV.setVisibility(View.VISIBLE);
						accountDetails_layout.setVisibility(View.GONE);
						billPayment_layout.setVisibility(View.GONE);
						billHistory_layout.setVisibility(View.GONE);
						paymentHistoryDetails_layout.setVisibility(View.GONE);
					}
				}

				else if (accnt_activation == 0) {
					// ACNT NOT INSERTED

					if (accnt_update == 1) {
						// activated
						Toast.makeText(HomePage.this,
								"Account updated successfully",
								Toast.LENGTH_LONG).show();
						// billPayment_layout.setVisibility(View.VISIBLE);
						paymentHistoryDetails_layout
								.setVisibility(View.VISIBLE);
						addAccount_button.setVisibility(View.GONE);

					} else {
						// ACCNT NOT ACTIVATED
						// billPayment_layout.setVisibility(View.GONE);
						// paymentHistoryDetails_layout.setVisibility(View.GONE);

						addAccount_button.setVisibility(View.GONE);
						requestSentTV.setVisibility(View.VISIBLE);
						accountDetails_layout.setVisibility(View.GONE);
						billPayment_layout.setVisibility(View.GONE);
						billHistory_layout.setVisibility(View.GONE);
						paymentHistoryDetails_layout.setVisibility(View.GONE);
					}

					// addAccount_button.setVisibility(View.GONE);
					// requestSentTV.setVisibility(View.VISIBLE);
					// } else {

					// if (addAccnt_click_broadband) {

					// addAccount_button.setVisibility(View.VISIBLE);
					// accountDetails_layout.setVisibility(View.GONE);
					// requestSentTV.setVisibility(View.GONE);
					// billPayment_layout.setVisibility(View.GONE);
					// billHistory_layout.setVisibility(View.GONE);
					// paymentHistoryDetails_layout.setVisibility(View.GONE);
					// }

					// else if (home_preferences.getBoolean("acntinsertion",
					// false) == true || addAccnt_click_broadband == false) {
					// addAccount_button.setVisibility(View.GONE);
					// requestSentTV.setVisibility(View.VISIBLE);
					// } else {
					// addAccount_button.setVisibility(View.VISIBLE);
					// requestSentTV.setVisibility(View.GONE);
					// }

					// else {
					// addAccount_button.setVisibility(View.GONE);
					// requestSentTV.setVisibility(View.VISIBLE);
					// }
					Toast.makeText(HomePage.this,
							"Account not activated ..Please wait",
							Toast.LENGTH_LONG).show();
					// }

				}
			}
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.home_addaccountButton:

			addAccount_button.setVisibility(View.GONE);
			accountDetails_layout.setVisibility(View.VISIBLE);
			accntdetails_planslist.clear();
			// retrieve service plan

			db_testAdapter = new TestAdapter(HomePage.this);
			db_testAdapter.createDatabase();
			db_testAdapter.open();
			accntdetails_planslist = db_testAdapter
					.serviceplans_retrieval(segment_type);
			db_testAdapter.close();
			Log.d("Home--accntdetails_retrieve update *****", ""
					+ accntdetails_planslist);

			if (accntdetails_planslist.size() != 0) {
				Log.d("retrieve success*****", "");
				accntdetails_planSpinner.setAdapter(adapter);

			} else {
				Log.d("retrieve fail*****", "");

			}

			// make respective a/c layout visible
			if (segment_type.equalsIgnoreCase("Cellular")) {

				accntdetails_mobileLayout.setVisibility(View.VISIBLE);
				accntdetails_landlineLayout.setVisibility(View.GONE);
				accntdetails_broadbandLayout.setVisibility(View.GONE);
				accnt_no = accntdetailss_mobilenoEditText.getText().toString();
			} else if (segment_type.equalsIgnoreCase("Landline")) {
				accntdetails_mobileLayout.setVisibility(View.GONE);
				accntdetails_landlineLayout.setVisibility(View.VISIBLE);
				accntdetails_broadbandLayout.setVisibility(View.GONE);
				accnt_no = accntdetailss_landlinenoEditText.getText()
						.toString();
			} else if (segment_type.equalsIgnoreCase("Broadband")) {
				accntdetails_mobileLayout.setVisibility(View.GONE);
				accntdetails_landlineLayout.setVisibility(View.GONE);
				accntdetails_broadbandLayout.setVisibility(View.VISIBLE);
				accnt_no = accntdetailss_broadbandnoEditText.getText()
						.toString();
			}

			break;
		case R.id.accntdetails_addbutton:
			// add user info to DB and make payment visible-INSERT STMT
			addAccount_button.setVisibility(View.GONE);
			accountDetails_layout.setVisibility(View.GONE);
			//
			Log.d("add button click  *****", "");
			db_testAdapter = new TestAdapter(HomePage.this);
			db_testAdapter.createDatabase();
			db_testAdapter.open();
			// accntdetails_insertion = db_testAdapter.userAccount_configure(
			// accntdetailss_mobilenoEditText.getText().toString(),
			// accntdetailss_landlinenoEditText.getText().toString(),
			// accntdetailss_broadbandnoEditText.getText().toString(),accntdetails_plantype);
			accntdetails_insertion = db_testAdapter
					.userAccount_insertIntoRequests(
							home_preferences.getString("usermailid", ""),
							accnt_no, segment_type, accntdetails_plantype);
			db_testAdapter.close();
			Log.d("Home--accntdetails_insertionupdate update *****", ""
					+ accntdetails_insertion);

			if (accntdetails_insertion) {

				// if (segment_type.equalsIgnoreCase("Cellular")) {
				//
				// addAccnt_click_phone = false;
				// } else if (segment_type.equalsIgnoreCase("Landline")) {
				//
				// addAccnt_click_lande = false;
				// } else if (segment_type.equalsIgnoreCase("Broadband")) {
				// addAccnt_click_broadband = false;
				//
				// }
				// addAccnt_click = false;
				// accntdetails_insert=true;
				// home_editor = home_preferences.edit();
				// home_editor.putBoolean("acntinsertion", true);
				// home_editor.commit();
				requestSentTV.setVisibility(View.VISIBLE);
				Toast.makeText(this, "Your request sent successfully****",
						Toast.LENGTH_LONG).show();
			} else {
				// addAccnt_click = true;
				// if (segment_type.equalsIgnoreCase("Cellular")) {
				//
				// addAccnt_click_phone = true;
				// } else if (segment_type.equalsIgnoreCase("Landline")) {
				//
				// addAccnt_click_lande = true;
				// } else if (segment_type.equalsIgnoreCase("Broadband")) {
				// addAccnt_click_broadband = true;
				//
				// }
				home_editor = home_preferences.edit();
				home_editor.putBoolean("acntinsertion", false);
				home_editor.commit();
				requestSentTV.setVisibility(View.GONE);
				Toast.makeText(this, "Your request not sent successfully****",
						Toast.LENGTH_LONG).show();
			}

			// add beow code after accnt ativated by admin
			// if (accntdetails_insertion) {
			// Log.d("update success*****", "");
			// billPayment_layout.setVisibility(View.VISIBLE);
			// paymentHistoryDetails_layout.setVisibility(View.VISIBLE);
			//
			// billpay_nameTV.setText(home_preferences.getString("username",
			// ""));
			// // a/c retrieval
			// db_testAdapter = new TestAdapter(HomePage.this);
			// db_testAdapter.createDatabase();
			// db_testAdapter.open();
			// accntdetails_retrieve = db_testAdapter.userAccount_retrieval(
			// home_preferences.getString("usermailid", ""),
			// home_preferences.getString("userpwd", ""));
			// db_testAdapter.close();
			// Log.d("Home--accntdetails_insertionupdate update *****", ""
			// + accntdetails_insertion);
			//
			// // make respective a/c layout visible
			// if (segment_type.equalsIgnoreCase("Cellular")) {
			// billpay_mobileLayout.setVisibility(View.VISIBLE);
			// billpay_landlineLayout.setVisibility(View.GONE);
			// billpay_broadbandLayout.setVisibility(View.GONE);
			// if (accntdetails_retrieve) {
			// billpay_mobilenoTV.setText(home_preferences.getString(
			// "userphone", ""));
			// }
			// } else if (segment_type.equalsIgnoreCase("Landline")) {
			// billpay_mobileLayout.setVisibility(View.GONE);
			// billpay_landlineLayout.setVisibility(View.VISIBLE);
			// billpay_broadbandLayout.setVisibility(View.GONE);
			// if (accntdetails_retrieve) {
			// billpay_landlinenoTV.setText(home_preferences
			// .getString("userlandline", ""));
			// }
			// } else if (segment_type.equalsIgnoreCase("Broadband")) {
			// billpay_mobileLayout.setVisibility(View.GONE);
			// billpay_landlineLayout.setVisibility(View.GONE);
			// billpay_broadbandLayout.setVisibility(View.VISIBLE);
			// if (accntdetails_retrieve) {
			// billpay_broadbandnoTV.setText(home_preferences
			// .getString("userbroadband", ""));
			// }
			// }
			//
			// } else {
			//
			// }

			break;
		case R.id.home_paymentbutton:
			// make view visible-- insert bill
			billPayment_layout.setVisibility(View.VISIBLE);
			paymentHistoryDetails_layout.setVisibility(View.GONE);

			billpay_nameTV.setText(home_preferences.getString("username", ""));
			billpay_planTV.setText(home_preferences.getString("userplan", ""));
			// make respective a/c layout visible
			if (segment_type.equalsIgnoreCase("Cellular")) {

				billpay_mobileLayout.setVisibility(View.VISIBLE);
				billpay_landlineLayout.setVisibility(View.GONE);
				billpay_broadbandLayout.setVisibility(View.GONE);
				billpay_mobilenoTV.setText(home_preferences.getString(
						"accntNo", ""));
				bill_accnt_no = billpay_mobilenoTV.getText().toString();
			} else if (segment_type.equalsIgnoreCase("Landline")) {
				billpay_mobileLayout.setVisibility(View.GONE);
				billpay_landlineLayout.setVisibility(View.VISIBLE);
				billpay_broadbandLayout.setVisibility(View.GONE);
				billpay_landlinenoTV.setText(home_preferences.getString(
						"accntNo", ""));
				bill_accnt_no = billpay_mobilenoTV.getText().toString();
			} else if (segment_type.equalsIgnoreCase("Broadband")) {
				billpay_mobileLayout.setVisibility(View.GONE);
				billpay_landlineLayout.setVisibility(View.GONE);
				billpay_broadbandLayout.setVisibility(View.VISIBLE);
				billpay_broadbandnoTV.setText(home_preferences.getString(
						"accntNo", ""));
				bill_accnt_no = billpay_mobilenoTV.getText().toString();
			}

			break;
		case R.id.home_accntHistorybutton:

			Log.d("history click*********", "");
			billHistory_layout.setVisibility(View.VISIBLE);
			paymentHistoryDetails_layout.setVisibility(View.GONE);
			// retrieve bill
			db_testAdapter = new TestAdapter(HomePage.this);
			db_testAdapter.createDatabase();
			db_testAdapter.open();
			billHistory_list = db_testAdapter.bill_history(home_preferences
					.getString("usermailid", ""));
			db_testAdapter.close();
			Log.d("Home--billHistory_list  *****", "" + billHistory_list.size());

			history_adapter = new BillHistory_adapter();
			billHistory_listview.setAdapter(history_adapter);

			break;
		case R.id.bill_paybutton:
			// bill payment
			Log.d("bill-pay button click  *****", "");

			// insert bill
			db_testAdapter = new TestAdapter(HomePage.this);
			db_testAdapter.createDatabase();
			db_testAdapter.open();
			billinsertion_val = db_testAdapter.bill_insertion(
					home_preferences.getString("usermailid", ""),
					bill_accnt_no, segment_type,
					home_preferences.getString("userplan", ""),
					billpay_amountEditText.getText().toString(),
					billpay_dateEditText.getText().toString());
			db_testAdapter.close();
			Log.d("Home--billdetails_insertionupdate update *****", ""
					+ billinsertion_val);

			if (billinsertion_val) {

				Toast.makeText(this, "Bill payment successfull", 2000).show();
				Log.d("home---bill-payment successfull********",
						"bill-information saved");
				billPayment_layout.setVisibility(View.GONE);
				paymentHistoryDetails_layout.setVisibility(View.VISIBLE);
			} else {
				billPayment_layout.setVisibility(View.VISIBLE);
				paymentHistoryDetails_layout.setVisibility(View.GONE);
			}
			// Paypal related

			if (isConnectingToInternet()) {

				PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(
						"1.00"), "USD", "Pizza Order");

				Intent activityintent = new Intent(HomePage.this,
						PaymentActivity.class);

				activityintent.putExtra(
						PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT,
						CONFIG_ENVIRONMENT);
				activityintent.putExtra(PaymentActivity.EXTRA_CLIENT_ID,
						CONFIG_CLIENT_ID);
				activityintent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL,
						CONFIG_RECEIVER_EMAIL);

				// It's important to repeat the clientId here so that // the //
				// SDK // has it if // Android restarts your // app midway
				// through
				// the payment UI flow.
				activityintent.putExtra(PaymentActivity.EXTRA_CLIENT_ID,
						CONFIG_CLIENT_ID);
				activityintent.putExtra(PaymentActivity.EXTRA_PAYER_ID,
						CONFIG_RECEIVER_EMAIL);
				activityintent.putExtra(PaymentActivity.EXTRA_PAYMENT,
						thingToBuy);

				startActivityForResult(activityintent, 0);
			} else {

				Toast.makeText(
						HomePage.this,
						"Couldnot connect to network.Please check your internet connection.",
						3000).show();
			}
			break;

		default:
			break;
		}

	}

	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) HomePage.this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						Log.d("Network",
								"NETWORKnAME: " + info[i].getTypeName());
						return true;
					}

		}
		return false;

	}

	// account details--service plans
	public class Accntdetails_serviceplanAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return accntdetails_planslist.size();
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
			convertView = LayoutInflater.from(HomePage.this).inflate(
					R.layout.spinner_text, null);
			TextView text1 = (TextView) convertView
					.findViewById(R.id.spinnertext1);

			text1.setText(accntdetails_planslist.get(position));
			Log.d("servicePlan--spinner type", "Data : " + text1.toString());
			return convertView;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("OnActivity Result", "OnActivity Result");
		if (resultCode == Activity.RESULT_OK) {
			PaymentConfirmation confirm = data
					.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
			if (confirm != null) {
				try {
					Log.i("paymentExample", confirm.toJSONObject().toString(4));

					// TODO: send 'confirm' to your server for verification.
					// see
					// https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
					// for more details.
					Log.i("Payment Done ", "Insert the order in database");

					// dbobject.createDatabase();
					// dbobject.open();
					// dbobject.insertNewPizzaOrder(orderType, orderAddress,
					// totalPrice);
					// dbobject.close();

					Toast.makeText(HomePage.this, "Payment successfull", 2000)
							.show();
					// buyItBtn.setText("You order has been placed. Thank you..!!");
					// buyItBtn.setClickable(false);
				} catch (JSONException e) {
					Log.e("paymentExample",
							"an extremely unlikely failure occurred: ", e);
				}
			}
		} else if (resultCode == Activity.RESULT_CANCELED) {
			Log.i("paymentExample", "The user canceled.");
		} else if (resultCode == PaymentActivity.RESULT_PAYMENT_INVALID) {
			Log.i("paymentExample",
					"An invalid payment was submitted. Please see the docs.");
		}
	}

	public class BillHistory_adapter extends BaseAdapter {

		@Override
		public int getCount() {

			return billHistory_list.size();
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

			convertView = LayoutInflater.from(HomePage.this).inflate(
					R.layout.billhistory_customlayout, null);
			TextView nameTV = (TextView) convertView
					.findViewById(R.id.history_nametextview);
			TextView accntNoTV = (TextView) convertView
					.findViewById(R.id.history_accntnotextview);
			TextView planTV = (TextView) convertView
					.findViewById(R.id.history_plantetextview);
			TextView typeTV = (TextView) convertView
					.findViewById(R.id.history_typetextview);
			TextView billDateTV = (TextView) convertView
					.findViewById(R.id.history_billdatetextview);
			TextView billAmntTV = (TextView) convertView
					.findViewById(R.id.history_amountTextview);

			nameTV.setText(billHistory_list.get(position).getName());
			accntNoTV.setText(billHistory_list.get(position).getAccnt_no());
			typeTV.setText(billHistory_list.get(position).getType());
			planTV.setText(billHistory_list.get(position).getPlan());
			billDateTV.setText(billHistory_list.get(position).getBillDate());
			billAmntTV.setText(billHistory_list.get(position).getBillAmount());
			// convertView.setId(position);
			return convertView;
		}

	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	//
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	//
	// // if ((addAccnt_click_phone == false || addAccnt_click_lande ==
	// // false || addAccnt_click_broadband == false)
	// // && accnt_update == 1) {
	// if (accnt_update == 1) {
	// Toast.makeText(HomePage.this, "Account updated successfully",
	// Toast.LENGTH_LONG).show();
	// paymentHistoryDetails_layout.setVisibility(View.VISIBLE);
	// addAccount_button.setVisibility(View.GONE);
	// } else {
	// paymentHistoryDetails_layout.setVisibility(View.GONE);
	// addAccount_button.setVisibility(View.VISIBLE);
	//
	// // Ask the user if they want to quit
	// new AlertDialog.Builder(this)
	// .setIcon(android.R.drawable.ic_dialog_alert)
	// .setTitle("Confirm")
	// .setMessage("Do you want to exit from the application?")
	// .setPositiveButton("yes",
	// new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog,
	// int which) {
	//
	// // Stop the activity
	// // SharedPreferences.Editor editor =
	// // mPrefs
	// // .edit();
	// // editor.putString("deviceselection",
	// // "null");
	// // editor.commit();
	//
	// // HomePage.this.finish();
	// // MainActivity.this.finish();
	// finish();
	// }
	//
	// }).setNegativeButton("No", null).show();
	// }
	// return true;
	// } else {
	// return super.onKeyDown(keyCode, event);
	// }
	// }

	// @Override
	// public void onBackPressed() {
	// super.onBackPressed();
	//
	// // if ((addAccnt_click_phone == false || addAccnt_click_lande == false
	// // || addAccnt_click_broadband == false)
	// // && accnt_update == 1) {
	// if (accnt_update == 1) {
	// Toast.makeText(HomePage.this, "Account updated successfully",
	// Toast.LENGTH_LONG).show();
	// paymentHistoryDetails_layout.setVisibility(View.VISIBLE);
	// addAccount_button.setVisibility(View.GONE);
	// } else {
	// paymentHistoryDetails_layout.setVisibility(View.GONE);
	// addAccount_button.setVisibility(View.VISIBLE);
	//
	// // Ask the user if they want to quit
	//
	// }
	//
	// }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// Handle the back button
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// Ask the user if they want to quit
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Confirm")
					.setMessage("Do you want to exit from the application?")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									// Stop the activity
									// SharedPreferences.Editor editor = mPrefs
									// .edit();
									// editor.putString("deviceselection",
									// "null");
									// editor.commit();

									HomePage.this.finish();
								}

							}).setNegativeButton("No", null).show();

			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

}
