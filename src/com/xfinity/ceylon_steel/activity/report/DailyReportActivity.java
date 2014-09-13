/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2014, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Jul 08, 2014, 2:37 PM
 */
package com.xfinity.ceylon_steel.activity.report;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.xfinity.ceylon_steel.R;
import com.xfinity.ceylon_steel.activity.HomeActivity;
import com.xfinity.ceylon_steel.controller.OutletController;
import com.xfinity.ceylon_steel.controller.UserController;
import com.xfinity.ceylon_steel.model.Invoice;
import com.xfinity.ceylon_steel.model.User;
import com.xfinity.ceylon_steel.widget.FilterableBaseAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class DailyReportActivity extends Activity {

	private Button btnReturnToHome;
	private ListView listInvoices;
	private Button btnSearch;
	private AutoCompleteTextView distributorAuto;
	private EditText inputFromDate;
	private EditText inputToDate;
	private Calendar calendar = Calendar.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.daily_report);
		initialize();
	}

	private void initialize() {
		listInvoices = (ListView) findViewById(R.id.listInvoices);
		distributorAuto = (AutoCompleteTextView) findViewById(R.id.distributorAuto);

		ArrayList<User> distributors = UserController.getDistributors(this);
		ArrayAdapter<User> distributorAdapter = new ArrayAdapter<User>(this, android.R.layout.simple_dropdown_item_1line, distributors);
		distributorAuto.setAdapter(distributorAdapter);

		inputFromDate = (EditText) findViewById(R.id.inputFromDate);
		inputToDate = (EditText) findViewById(R.id.inputToDate);
		btnSearch = (Button) findViewById(R.id.btnSearch);

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
		final ArrayList<Invoice> invoices = OutletController.getPendingInvoices(DailyReportActivity.this);
		final NumberFormat currencyFormat = NumberFormat.getNumberInstance();
		currencyFormat.setMinimumFractionDigits(2);
		currencyFormat.setMaximumFractionDigits(2);
		currencyFormat.setGroupingUsed(true);
		listInvoices.setAdapter(new FilterableBaseAdapter() {
			@Override
			public int getCount() {
				return invoices.size();
			}

			@Override
			public Invoice getItem(int position) {
				return invoices.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View view, ViewGroup parent) {
				ViewHolder viewHolder;
				if (view == null) {
					LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
					viewHolder = new ViewHolder();
					view = inflater.inflate(R.layout.daily_report_item, null);
					viewHolder.txtOutletName = (TextView) view.findViewById(R.id.txtOutletName);
					viewHolder.txtDistributorCode = (TextView) view.findViewById(R.id.txtDistributorCode);
					viewHolder.txtDateOfInvoice = (TextView) view.findViewById(R.id.txtDateOfInvoice);
					viewHolder.txtDeliveryDate = (TextView) view.findViewById(R.id.txtDeliveryDate);
					viewHolder.txtAmount = (TextView) view.findViewById(R.id.txtAmount);
					view.setTag(viewHolder);
				} else {
					viewHolder = (ViewHolder) view.getTag();
				}
				Invoice invoice = getItem(position);
				viewHolder.txtOutletName.setText(invoice.getOutletName());
				viewHolder.txtDistributorCode.setText(invoice.getDistributorCode());
				viewHolder.txtDateOfInvoice.setText(invoice.getDate());
				viewHolder.txtDeliveryDate.setText(invoice.getDeliveryDate());
				viewHolder.txtAmount.setText("Rs " + currencyFormat.format(invoice.getPendingAmount()));
				return view;
			}

			@Override
			public Filter getFilter() {
				return super.getFilter();
			}
		});
		btnReturnToHome = (Button) findViewById(R.id.btnReturnToHome);
		btnReturnToHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				btnReturnToHomeClicked(view);
			}
		});
	}

	private void btnSearchClicked(View view) {

	}

	private void inputToDateClicked(View view) {
		new DatePickerDialog(DailyReportActivity.this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				inputToDate.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
	}

	private void inputFromDateClicked(View view) {
		new DatePickerDialog(DailyReportActivity.this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				inputFromDate.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
	}

	private void btnReturnToHomeClicked(View view) {
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		Intent homeActivity = new Intent(DailyReportActivity.this, HomeActivity.class);
		startActivity(homeActivity);
		finish();
	}

	private static class ViewHolder {
		TextView txtOutletName;
		TextView txtDistributorCode;
		TextView txtDateOfInvoice;
		TextView txtDeliveryDate;
		TextView txtAmount;
	}
}
