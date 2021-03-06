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
import com.xfinity.ceylon_steel.activity.attendance.AttendanceActivity;
import com.xfinity.ceylon_steel.activity.attendance.CheckInCheckOutHistory;
import com.xfinity.ceylon_steel.activity.make_sales_order.MakeSalesOrderActivity;
import com.xfinity.ceylon_steel.activity.payment.PaymentActivity;
import com.xfinity.ceylon_steel.activity.report.ChequeRealizationReport;
import com.xfinity.ceylon_steel.activity.report.OutletWiseSaleReport;
import com.xfinity.ceylon_steel.activity.report.PaymentConfirmationReport;
import com.xfinity.ceylon_steel.activity.unproductive_call.UnProductiveCallActivity;
import com.xfinity.ceylon_steel.activity.view_sales_order.ViewSalesOrderActivity;
import com.xfinity.ceylon_steel.controller.UserController;
import com.xfinity.ceylon_steel.db.SQLiteDatabaseHelper;
import com.xfinity.ceylon_steel.util.Tracker;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class HomeActivity extends Activity {

	private Button btnMakeSalesOrder;
	private Button btnViewSalesOrder;
	private Button btnAttendance;
	private Button btnAttendanceHistory;
	private Button btnUnProductiveCall;
	private Button btnPayment;
	//	private Button btnPendingInvoices;
	private Button btnReloadData;
	private Button btnLogout;
	private Button btnPaymentConfirmationReport;
	private Button btnChequeStatusReport;
	private Button btnOutletWiseSaleReport;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_page);
		initialize();
	}

	@Override
	protected void onResume() {
		super.onResume();
		startRepTracking();
	}

	@Override
	public void onBackPressed() {
		System.exit(0);
	}

	// <editor-fold defaultstate="collapsed" desc="Initialize">
	private void initialize() {
		btnMakeSalesOrder = (Button) findViewById(R.id.btnMakeSalesOrder);
		btnViewSalesOrder = (Button) findViewById(R.id.btnViewSalesOrder);
		btnAttendance = (Button) findViewById(R.id.btnAttendance);
		btnUnProductiveCall = (Button) findViewById(R.id.btnUnProductiveCall);
		btnReloadData = (Button) findViewById(R.id.btnReloadData);
		btnLogout = (Button) findViewById(R.id.btnLogout);
		btnPayment = (Button) findViewById(R.id.btnPayment);
//		btnPendingInvoices = (Button) findViewById(R.id.btnPendingInvoices);
		btnAttendanceHistory = (Button) findViewById(R.id.btnCheckInCheckoutHistory);
		btnPaymentConfirmationReport = (Button) findViewById(R.id.btnPaymentConfirmationReport);
		btnChequeStatusReport = (Button) findViewById(R.id.btnChequeStatusReport);
		btnOutletWiseSaleReport = (Button) findViewById(R.id.btnOutletWiseSaleReport);

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
		btnAttendance.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnAttendanceClicked(view);
			}
		});
		btnUnProductiveCall.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnUnProductiveCallClicked(view);
			}
		});
		btnAttendanceHistory.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnAttendanceHistoryClicked(view);
			}
		});
		btnReloadData.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnReloadDataClicked(view);
			}
		});
		btnPayment.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnPaymentClicked(view);
			}
		});
//		btnPendingInvoices.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View view) {
//				btnPendingInvoicesClicked(view);
//			}
//		});
		btnLogout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnLogoutClicked(view);
			}
		});

		btnPaymentConfirmationReport.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnPaymentConfirmationReportClicked(view);
			}
		});
		btnChequeStatusReport.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnChequeStatusReportClicked(view);
			}
		});
		btnOutletWiseSaleReport.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnOutletWiseSaleReportClicked(view);
			}
		});
	}
	// </editor-fold>

	private void btnAttendanceHistoryClicked(View view) {
		Intent checkInCheckOutHistoryActivity = new Intent(this, CheckInCheckOutHistory.class);
		startActivity(checkInCheckOutHistoryActivity);
		finish();
	}

//	private void btnPendingInvoicesClicked(View view) {
//		Intent dailyReportActivity = new Intent(this, DailyReportActivity.class);
//		startActivity(dailyReportActivity);
//		finish();
//	}

	private void btnPaymentClicked(View view) {
		Intent paymentActivity = new Intent(this, PaymentActivity.class);
		startActivity(paymentActivity);
		finish();
	}

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

	private void btnAttendanceClicked(View view) {
		Intent attendanceActivity = new Intent(this, AttendanceActivity.class);
		startActivity(attendanceActivity);
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
				Tracker.stopTracking();
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

	private void startRepTracking() {
		//starts Rep tracking function
		Intent tracker = new Intent(this, Tracker.class);
		startService(tracker);
	}

	private void btnPaymentConfirmationReportClicked(View view) {
		Intent intent = new Intent(HomeActivity.this, PaymentConfirmationReport.class);
		startActivity(intent);
		finish();
	}

	private void btnChequeStatusReportClicked(View view) {
		Intent intent = new Intent(HomeActivity.this, ChequeRealizationReport.class);
		startActivity(intent);
		finish();
	}

	private void btnOutletWiseSaleReportClicked(View view) {
		Intent intent = new Intent(HomeActivity.this, OutletWiseSaleReport.class);
		startActivity(intent);
		finish();
	}
}
