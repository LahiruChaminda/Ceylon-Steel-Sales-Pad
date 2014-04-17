/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Xfinity and/or its affiliates. All rights reserved.
 * Created on : Mar 9, 2014, 12:29:04 PM
 */
package com.xfinity.ceylon_steel.activity.attendence;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import com.xfinity.ceylon_steel.R;
import com.xfinity.ceylon_steel.activity.HomeActivity;
import com.xfinity.ceylon_steel.controller.UserController;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class AttendanceActivity extends Activity {

	private Button btnCheckIn;
	private Button btnCheckOut;

	/**
	 * Called when the activity is first created.
	 *
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attendence_page);
		initialize();
	}

	@Override
	public void onBackPressed() {
		Intent homeActivity = new Intent(this, HomeActivity.class);
		startActivity(homeActivity);
		finish();
	}

	// <editor-fold defaultstate="collapsed" desc="Initialize">
	private void initialize() {
		btnCheckIn = (Button) findViewById(R.id.btnCheckIn);
		btnCheckOut = (Button) findViewById(R.id.btnCheckOut);
		btnCheckIn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnCheckInClicked(view);
			}
		});
		btnCheckOut.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnCheckOutClicked(view);
			}
		});
	}
	// </editor-fold>

	private void btnCheckInClicked(View view) {
		AlertDialog.Builder checkInConfirmationDialog = new AlertDialog.Builder(this);
		checkInConfirmationDialog.setTitle("Please confirm your action");
		checkInConfirmationDialog.setMessage("Are you sure?");
		checkInConfirmationDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				checkIn(arg0, arg1);
			}
		});
		checkInConfirmationDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				showHomePage(arg0, arg1);
			}
		});
		checkInConfirmationDialog.show();
	}

	private void btnCheckOutClicked(View view) {
		AlertDialog.Builder checkOutConfirmationDialog = new AlertDialog.Builder(this);
		checkOutConfirmationDialog.setTitle("Please confirm your action");
		checkOutConfirmationDialog.setMessage("Are you sure?");
		checkOutConfirmationDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				checkOut(arg0, arg1);
			}
		});
		checkOutConfirmationDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				showHomePage(arg0, arg1);
			}
		});
		checkOutConfirmationDialog.show();
	}

	private void checkIn(DialogInterface arg0, int arg1) {
		UserController.checkIn(this);
	}

	private void checkOut(DialogInterface arg0, int arg1) {
		UserController.checkOut(this);
	}

	public void showHomePage(DialogInterface arg0, int arg1) {
		Intent homeActivity = new Intent(this, HomeActivity.class);
		startActivity(homeActivity);
		finish();
	}

	public void showSettingsPage(DialogInterface arg0, int arg1) {
		Intent settingsActivity = new Intent(Settings.ACTION_DATE_SETTINGS);
		startActivity(settingsActivity);
	}
}
