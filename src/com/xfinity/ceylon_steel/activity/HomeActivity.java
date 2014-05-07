/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 8, 2014, 7:49:44 PM
 */
package com.xfinity.ceylon_steel.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.xfinity.ceylon_steel.R;
import com.xfinity.ceylon_steel.activity.attendence.AttendanceActivity;
import com.xfinity.ceylon_steel.activity.make_sales_order.MakeSalesOrderActivity;
import com.xfinity.ceylon_steel.activity.unproductive_call.UnProductiveCallActivity;
import com.xfinity.ceylon_steel.activity.view_sales_order.ViewSalesOrderActivity;
import com.xfinity.ceylon_steel.controller.UserController;
import com.xfinity.ceylon_steel.db.SQLiteDatabaseHelper;
import com.xfinity.ceylon_steel.service.Tracker;

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
	private Button btnReloadData;
	private Button btnLogout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_page);
		initialize();

		//starts Rep tracking function
		Intent tracker = new Intent(this, Tracker.class);
		startService(tracker);
	}

	@Override
	public void onBackPressed() {
		System.exit(0);
	}

	// <editor-fold defaultstate="collapsed" desc="Initialize">
	private void initialize() {
		btnMakeSalesOrder = (Button) findViewById(R.id.btnMakeSalesOrder);
		btnViewSalesOrder = (Button) findViewById(R.id.btnViewSalesOrder);
		btnAttendence = (Button) findViewById(R.id.btnAttendence);
		btnUnProductiveCall = (Button) findViewById(R.id.btnUnProductiveCall);
		btnReloadData = (Button) findViewById(R.id.btnReloadData);
		btnLogout = (Button) findViewById(R.id.btnLogout);

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
		btnReloadData.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnReloadDataClicked(view);
			}
		});
		btnLogout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnLogoutClicked(view);
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

	private void btnReloadDataClicked(View view) {
		UserController.loadDataFromServer(this);
	}

	private void btnLogoutClicked(View view) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(com.xfinity.ceylon_steel.R.string.message_title);
		alert.setMessage("You're about to sign-out from sales pad.\nIf you sign-out your un synchronized data will be deleted.\nAre you sure you want to continue?");
		alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				Intent tracker = new Intent(HomeActivity.this, Tracker.class);
				HomeActivity.this.stopService(tracker);
				boolean clearAuthentication = UserController.clearAuthentication(HomeActivity.this);
				if (clearAuthentication) {
					SQLiteDatabaseHelper.dropDatabase(HomeActivity.this);
					Intent loginActivity = new Intent(HomeActivity.this, LoginActivity.class);
					startActivity(loginActivity);
					finish();
				}
			}
		});
		alert.setNegativeButton("No", null);
		alert.show();
	}

}
