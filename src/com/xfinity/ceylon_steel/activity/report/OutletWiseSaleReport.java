/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2014, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Sep 09, 2014, 12:37 PM
 */
package com.xfinity.ceylon_steel.activity.report;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
public class OutletWiseSaleReport extends Activity {

	private Button btnReturnToHome;
	private ListView listOfSales;
	private ArrayList<JSONObject> outletWiseSales = new ArrayList<JSONObject>();
	private BaseAdapter adapter;
	private NumberFormat currencyFormat;
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
						progressDialog = ProgressDialog.show(OutletWiseSaleReport.this, null, "Downloading Data...");
					}
				});
				JSONArray confirmationDetails = OutletController.getDistributorOutletWiseSaleDetails(OutletWiseSaleReport.this, from, to);
				outletWiseSales.clear();
				for (int i = 0; i < confirmationDetails.length(); i++) {
					outletWiseSales.add(confirmationDetails.getJSONObject(i));
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
	private EditText inputFromDate;
	private EditText inputToDate;
	private Button btnSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.outlet_wise_sale_report);

		initialize();
	}

	private void initialize() {
		listOfSales = (ListView) findViewById(R.id.listOfSales);
		btnReturnToHome = (Button) findViewById(R.id.btnReturnToHome);
		inputFromDate = (EditText) findViewById(R.id.inputFromDate);
		inputToDate = (EditText) findViewById(R.id.inputToDate);
		btnSearch = (Button) findViewById(R.id.btnSearch);

		adapter = new BaseAdapter() {
			@Override
			public int getCount() {
				return outletWiseSales.size();
			}

			@Override
			public JSONObject getItem(int position) {
				return outletWiseSales.get(position);
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
					convertView = layoutInflater.inflate(R.layout.outlet_wise_sale_item, null);
					convertView.setTag(viewHolder = new ViewHolder());
					viewHolder.txtOutletName = (TextView) convertView.findViewById(R.id.txtOutletName);
					viewHolder.txtDistributorCode = (TextView) convertView.findViewById(R.id.txtDistributorCode);
					viewHolder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
					viewHolder.txtItemName = (TextView) convertView.findViewById(R.id.txtItemName);
					viewHolder.txtQuantity = (TextView) convertView.findViewById(R.id.txtQuantity);
					viewHolder.txtUnit = (TextView) convertView.findViewById(R.id.txtUnit);
					viewHolder.txtAmount = (TextView) convertView.findViewById(R.id.txtAmount);
				} else {
					viewHolder = (ViewHolder) convertView.getTag();
				}
				JSONObject json = getItem(position);
				try {
					viewHolder.txtOutletName.setText(json.isNull("outletName") ? "" : json.getString("outletName"));
					viewHolder.txtDistributorCode.setText(json.isNull("distributorCode") ? "" : json.getString("distributorCode"));
					viewHolder.txtDate.setText(json.isNull("date") ? "" : json.getString("date"));
					viewHolder.txtItemName.setText(json.isNull("description") ? "" : json.getString("description"));
					viewHolder.txtQuantity.setText(json.isNull("quantity") ? "" : json.getString("quantity"));
					viewHolder.txtUnit.setText(json.isNull("unitName") ? "" : json.getString("unitName"));
					viewHolder.txtAmount.setText("Rs " + currencyFormat.format(json.isNull("amount") ? 0 : Double.parseDouble(json.getString("amount"))));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return convertView;
			}
		};
		listOfSales.setAdapter(adapter);

		currencyFormat = NumberFormat.getNumberInstance();
		currencyFormat.setMinimumFractionDigits(2);
		currencyFormat.setMaximumFractionDigits(2);
		currencyFormat.setGroupingUsed(true);

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
	}

	private void btnSearchClicked(View view) {
		new Thread(runnable).start();
	}

	private void inputToDateClicked(View view) {
		new DatePickerDialog(OutletWiseSaleReport.this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				inputToDate.setText(to = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
	}

	private void inputFromDateClicked(View view) {
		new DatePickerDialog(OutletWiseSaleReport.this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				inputFromDate.setText(from = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
	}

	@Override
	public void onBackPressed() {
		Intent homeActivity = new Intent(OutletWiseSaleReport.this, HomeActivity.class);
		startActivity(homeActivity);
		finish();
	}

	@Override
	protected void onPostResume() {
		super.onPostResume();
		btnSearch.callOnClick();
	}

	private class ViewHolder {
		TextView txtOutletName;
		TextView txtDistributorCode;
		TextView txtDate;
		TextView txtItemName;
		TextView txtQuantity;
		TextView txtUnit;
		TextView txtAmount;
	}
}
