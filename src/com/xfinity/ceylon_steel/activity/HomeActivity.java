/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 8, 2014, 7:49:44 PM
 */
package com.xfinity.ceylon_steel.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.xfinity.ceylon_steel.R;
import com.xfinity.ceylon_steel.activity.attendence.AttendanceActivity;
import com.xfinity.ceylon_steel.activity.make_sales_order.MakeSalesOrderActivity;
import com.xfinity.ceylon_steel.activity.unproductive_call.UnProductiveCallActivity;
import com.xfinity.ceylon_steel.activity.view_sales_order.ViewSalesOrderActivity;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class HomeActivity extends Activity {

	private Button btnMakeSalesOrder;
	private Button btnViewSalesOrder;
	private Button btnAttendence;
	private Button btnUnProductiveCall;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_page);
		initialize();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	// <editor-fold defaultstate="collapsed" desc="Initialize">
	private void initialize() {
		btnMakeSalesOrder = (Button) findViewById(R.id.btnMakeSalesOrder);
		btnViewSalesOrder = (Button) findViewById(R.id.btnViewSalesOrder);
		btnAttendence = (Button) findViewById(R.id.btnAttendence);
		btnUnProductiveCall = (Button) findViewById(R.id.btnUnProductiveCall);
		btnMakeSalesOrder.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnMakeSalesOrderClicked(view);
			}
		});
		btnViewSalesOrder.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnViewSalesOrderClicked(view);
			}
		});
		btnAttendence.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnAttendenceClicked(view);
			}
		});
		btnUnProductiveCall.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnUnProductiveCallClicked(view);
			}
		});
	}
	// </editor-fold>

	private void btnMakeSalesOrderClicked(View view) {
		Intent makeSalesOrderActivity = new Intent(this, MakeSalesOrderActivity.class);
		startActivity(makeSalesOrderActivity);
		finish();
	}

	private void btnViewSalesOrderClicked(View view) {
		Intent viewSalesOrderActivity = new Intent(this, ViewSalesOrderActivity.class);
		startActivity(viewSalesOrderActivity);
		finish();
	}

	private void btnAttendenceClicked(View view) {
		Intent attendenceActivity = new Intent(this, AttendanceActivity.class);
		startActivity(attendenceActivity);
		finish();
	}

	private void btnUnProductiveCallClicked(View view) {
		Intent unProductiveCall = new Intent(this, UnProductiveCallActivity.class);
		startActivity(unProductiveCall);
		finish();
	}

}
