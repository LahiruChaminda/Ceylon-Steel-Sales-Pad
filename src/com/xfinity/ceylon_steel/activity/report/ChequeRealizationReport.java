/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2014, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Sep 09, 2014, 11:32 AM
 */
package com.xfinity.ceylon_steel.activity.report;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.xfinity.ceylon_steel.R;
import com.xfinity.ceylon_steel.activity.HomeActivity;
import com.xfinity.ceylon_steel.controller.OutletController;
import com.xfinity.ceylon_steel.controller.UserController;
import com.xfinity.ceylon_steel.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class ChequeRealizationReport extends Activity {

	private Button btnReturnToHome;
	private ListView listOfCheques;
	private Button btnSearch;
	private EditText inputFromDate;
	private EditText inputToDate;
	private AutoCompleteTextView distributorAuto;
	private ArrayList<JSONObject> chequeRealizationStatusCollection = new ArrayList<JSONObject>();
	private BaseAdapter adapter;
	private String from = "";
	private String to = "";
	private Runnable runnable = new Runnable() {

		private Handler handler = new Handler();
		private ProgressDialog progressDialog;

		@Override
		public void run() {
			try {
				handler.post(new Runnable() {
					@Override
					public void run() {
						progressDialog = ProgressDialog.show(ChequeRealizationReport.this, null, "Downloading Data...");
					}
				});
				JSONArray confirmationDetails = OutletController.getChequeRealizationDetails(ChequeRealizationReport.this, from, to);
				chequeRealizationStatusCollection.clear();
				for (int i = 0; i < confirmationDetails.length(); i++) {
					chequeRealizationStatusCollection.add(confirmationDetails.getJSONObject(i));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				handler.post(new Runnable() {
					@Override
					public void run() {
						if (progressDialog != null && progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
						adapter.notifyDataSetChanged();
					}
				});
			}
		}
	};
	private NumberFormat currencyFormat;
	private Calendar calendar = Calendar.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cheque_realization_report);

		currencyFormat = NumberFormat.getNumberInstance();
		currencyFormat.setMinimumFractionDigits(2);
		currencyFormat.setMaximumFractionDigits(2);
		currencyFormat.setGroupingUsed(true);

		initialize();
	}

	private void initialize() {
		btnReturnToHome = (Button) findViewById(R.id.btnReturnToHome);
		distributorAuto = (AutoCompleteTextView) findViewById(R.id.distributorAuto);

		ArrayList<User> distributors = UserController.getDistributors(this);
		ArrayAdapter<User> distributorAdapter = new ArrayAdapter<User>(this, android.R.layout.simple_dropdown_item_1line, distributors);
		distributorAuto.setAdapter(distributorAdapter);

		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btnSearchClicked(v);
			}
		});
		inputFromDate = (EditText) findViewById(R.id.inputFromDate);
		inputFromDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				inputFromDateClicked(v);
			}
		});
		inputToDate = (EditText) findViewById(R.id.inputToDate);
		inputToDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				inputToDateClicked(v);
			}
		});
		btnReturnToHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		adapter = new BaseAdapter() {
			@Override
			public int getCount() {
				return chequeRealizationStatusCollection.size();
			}

			@Override
			public JSONObject getItem(int position) {
				return chequeRealizationStatusCollection.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder viewHolder;
				if (convertView == null) {
					LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
					convertView = layoutInflater.inflate(R.layout.cheque_realization_item, null);
					convertView.setTag(viewHolder = new ViewHolder());
					viewHolder.txtRetailer = (TextView) convertView.findViewById(R.id.txtRetailer);
					viewHolder.txtInvoiceNo = (TextView) convertView.findViewById(R.id.txtInvoiceNo);
					viewHolder.txtInvoiceDate = (TextView) convertView.findViewById(R.id.txtInvoiceDate);
					viewHolder.txtPaymentMode = (TextView) convertView.findViewById(R.id.txtPaymentMode);
					viewHolder.txtPaymentDate = (TextView) convertView.findViewById(R.id.txtPaymentDate);
					viewHolder.txtChequeNo = (TextView) convertView.findViewById(R.id.txtChequeNo);
					viewHolder.txtBank = (TextView) convertView.findViewById(R.id.txtBank);
					viewHolder.txtBankingDate = (TextView) convertView.findViewById(R.id.txtBankingDate);
					viewHolder.txtAmount = (TextView) convertView.findViewById(R.id.txtAmount);
					viewHolder.txtChequeStatus = (TextView) convertView.findViewById(R.id.txtChequeStatus);
				} else {
					viewHolder = (ViewHolder) convertView.getTag();
				}
				JSONObject json = getItem(position);
				try {
					String status = "";
					switch (json.getInt("cheque_status")) {
						case 0:
							status = "HONOR";
							convertView.setBackgroundColor(Color.parseColor("#b2ffb2"));
							break;
						case 1:
							status = "DISHONOR";
							convertView.setBackgroundColor(Color.parseColor("#ffb2b2"));
							break;
						case 2:
							status = "PENDING";
							convertView.setBackgroundColor(Color.parseColor("#ffffb2"));
							break;
					}
					viewHolder.txtRetailer.setText(json.isNull("outletName") ? "" : json.getString("outletName"));
					viewHolder.txtInvoiceNo.setText(json.isNull("distributorCode") ? "" : json.getString("distributorCode"));
					viewHolder.txtInvoiceDate.setText(json.isNull("indate") ? "" : json.getString("indate"));
					viewHolder.txtPaymentMode.setText(json.isNull("paymentMethod") ? "" : json.getString("paymentMethod"));
					viewHolder.txtPaymentDate.setText(json.isNull("date") ? "" : json.getString("date"));
					viewHolder.txtChequeNo.setText(json.isNull("chequeNo") ? "" : json.getString("chequeNo"));
					viewHolder.txtBank.setText(json.isNull("bank") ? "" : json.getString("bank"));
					viewHolder.txtBankingDate.setText(json.isNull("bankingDate") ? "" : json.getString("bankingDate"));
					viewHolder.txtAmount.setText("Rs " + currencyFormat.format(json.isNull("amount") ? 0 : Double.parseDouble(json.getString("amount"))));
					viewHolder.txtChequeStatus.setText(status);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return convertView;
			}
		};
		listOfCheques = (ListView) findViewById(R.id.listOfCheques);
		listOfCheques.setAdapter(adapter);
	}

	private void btnSearchClicked(View view) {
		new Thread(runnable).start();
	}

	private void inputToDateClicked(View view) {
		new DatePickerDialog(ChequeRealizationReport.this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				inputFromDate.setText(to = year + "-" + monthOfYear + "-" + dayOfMonth);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
	}

	private void inputFromDateClicked(View view) {
		new DatePickerDialog(ChequeRealizationReport.this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				inputFromDate.setText(from = year + "-" + monthOfYear + "-" + dayOfMonth);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
	}

	@Override
	public void onBackPressed() {
		Intent homeActivity = new Intent(ChequeRealizationReport.this, HomeActivity.class);
		startActivity(homeActivity);
		finish();
	}

	@Override
	protected void onPostResume() {
		super.onPostResume();
		btnSearch.performClick();
	}

	private class ViewHolder {
		TextView txtRetailer;
		TextView txtInvoiceNo;
		TextView txtInvoiceDate;
		TextView txtPaymentMode;
		TextView txtPaymentDate;
		TextView txtChequeNo;
		TextView txtBank;
		TextView txtBankingDate;
		TextView txtAmount;
		TextView txtChequeStatus;
	}
}
