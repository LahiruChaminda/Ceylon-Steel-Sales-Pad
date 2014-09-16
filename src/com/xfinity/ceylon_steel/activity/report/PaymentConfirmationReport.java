/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2014, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Sep 09, 2014, 11:30 AM
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
public class PaymentConfirmationReport extends Activity {

	private Button btnReturnToHome;
	private ListView listOfPayments;
	private BaseAdapter adapter;
	private ArrayList<JSONObject> paymentConfirmationDetails = new ArrayList<JSONObject>();
	private NumberFormat currencyFormat;
	private EditText inputFromDate;
	private EditText inputToDate;
	private Button btnSearch;
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
						progressDialog = ProgressDialog.show(PaymentConfirmationReport.this, null, "Downloading Data...");
					}
				});
				JSONArray confirmationDetails = OutletController.getPaymentConfirmationDetails(PaymentConfirmationReport.this, from, to);
				paymentConfirmationDetails.clear();
				for (int i = 0; i < confirmationDetails.length(); i++) {
					paymentConfirmationDetails.add(confirmationDetails.getJSONObject(i));
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
	private Calendar calendar = Calendar.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payment_confirmation_report);
		currencyFormat = NumberFormat.getNumberInstance();
		currencyFormat.setMinimumFractionDigits(2);
		currencyFormat.setMaximumFractionDigits(2);
		currencyFormat.setGroupingUsed(true);
		initialize();
	}

	@Override
	public void onBackPressed() {
		Intent homeActivity = new Intent(this, HomeActivity.class);
		startActivity(homeActivity);
		finish();
	}

	private void initialize() {
		btnReturnToHome = (Button) findViewById(R.id.btnReturnToHome);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		inputFromDate = (EditText) findViewById(R.id.inputFromDate);
		inputToDate = (EditText) findViewById(R.id.inputToDate);

		btnReturnToHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		inputFromDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				inputFromDateClicked(v);
			}
		});
		inputToDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				inputToDateClicked(v);
			}
		});
		btnSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btnSearchClicked(v);
			}
		});
		listOfPayments = (ListView) findViewById(R.id.listOfPayments);
		adapter = new BaseAdapter() {
			@Override
			public int getCount() {
				return paymentConfirmationDetails.size();
			}

			@Override
			public JSONObject getItem(int position) {
				return paymentConfirmationDetails.get(position);
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
					convertView = layoutInflater.inflate(R.layout.payment_confirmation_item, null);
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
					viewHolder.txtAcknowledgement = (TextView) convertView.findViewById(R.id.txtAcknowledgement);
				} else {
					viewHolder = (ViewHolder) convertView.getTag();
				}
				JSONObject json = getItem(position);
				try {
					String status = "";
					switch (json.getInt("acknowladgement")) {
						case 0:
							status = "ACCEPTED";
							convertView.setBackgroundColor(Color.parseColor("#b2ffb2"));
							break;
						case 1:
							status = "NOT ACCEPTED";
							convertView.setBackgroundColor(Color.parseColor("#ffb2b2"));
							break;
						case 2:
							status = "PENDING";
							convertView.setBackgroundColor(Color.parseColor("#ffffb2"));
							break;
					}
					viewHolder.txtRetailer.setText(json.isNull("outletName") ? "" : json.getString("outletName"));
					viewHolder.txtInvoiceNo.setText(json.isNull("distributorCode") ? "" : json.getString("distributorCode"));
					viewHolder.txtInvoiceDate.setText(json.isNull("salesOrderDate") ? "" : json.getString("salesOrderDate"));
					viewHolder.txtPaymentMode.setText(json.isNull("paymentMethod") ? "" : json.getString("paymentMethod"));
					viewHolder.txtPaymentDate.setText(json.isNull("date") ? "" : json.getString("date"));
					viewHolder.txtChequeNo.setText(json.isNull("chequeNo") ? "" : json.getString("chequeNo"));
					viewHolder.txtBank.setText(json.isNull("bank") ? "" : json.getString("bank"));
					viewHolder.txtBankingDate.setText(json.isNull("bankingDate") ? "" : json.getString("bankingDate"));
					viewHolder.txtAmount.setText("Rs " + currencyFormat.format(json.isNull("amount") ? 0 : Double.parseDouble(json.getString("amount"))));
					viewHolder.txtAcknowledgement.setText(status);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return convertView;
			}
		};
		listOfPayments.setAdapter(adapter);
	}

	private void inputToDateClicked(View view) {
		new DatePickerDialog(PaymentConfirmationReport.this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				inputToDate.setText(to = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
	}

	private void btnSearchClicked(View view) {
		new Thread(runnable).start();
	}

	private void inputFromDateClicked(View view) {
		new DatePickerDialog(PaymentConfirmationReport.this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				inputFromDate.setText(from = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
	}

	@Override
	protected void onPostResume() {
		super.onPostResume();
		btnSearch.callOnClick();
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
		TextView txtAcknowledgement;
	}
}
