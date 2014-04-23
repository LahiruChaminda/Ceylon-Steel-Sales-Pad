/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 10, 2014, 12:26:28 AM
 */
package com.xfinity.ceylon_steel.activity.unproductive_call;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import com.xfinity.ceylon_steel.R;
import com.xfinity.ceylon_steel.activity.make_sales_order.MakeConsignmentSalesOrderActivity;
import com.xfinity.ceylon_steel.controller.OutletController;
import com.xfinity.ceylon_steel.controller.UnProductiveCallController;
import com.xfinity.ceylon_steel.controller.UserController;
import com.xfinity.ceylon_steel.model.Outlet;
import com.xfinity.ceylon_steel.model.UnProductiveCall;
import com.xfinity.ceylon_steel.service.BatteryService;
import com.xfinity.ceylon_steel.service.GpsReceiver;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class MakeUnProductiveCallActivity extends Activity {

	private AutoCompleteTextView unProductiveCallOutletAuto;
	private EditText txtMakeUnProductiveCallReason;
	private Button btnUnProductiveCallSubmit;
	private int outletId;

	private Location lastKnownLocation;
	private GpsReceiver gpsReceiver;

	private final AsyncTask<Void, Void, Void> GPS_CHECKER = new AsyncTask<Void, Void, Void>() {
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(MakeUnProductiveCallActivity.this);
			progressDialog.setMessage("Waiting for GPS Location...");
            progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			do {
				lastKnownLocation = gpsReceiver.getLastKnownLocation();
				try {
					Thread.sleep(1000);//delay 1 sec
				} catch (InterruptedException ex) {
					Logger.getLogger(MakeConsignmentSalesOrderActivity.class.getName()).log(Level.SEVERE, null, ex);
				}
			} while (lastKnownLocation == null);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			btnUnProductiveCallSubmit.setEnabled(true);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gpsReceiver = GpsReceiver.getGpsReceiver(getApplicationContext());
		setContentView(R.layout.make_unproductive_call_page);
		initialize();
	}

	@Override
	public void onBackPressed() {
		Intent unProductiveCall = new Intent(this, UnProductiveCallActivity.class);
		startActivity(unProductiveCall);
		finish();
	}

	// <editor-fold defaultstate="collapsed" desc="Initialize">
	private void initialize() {
		unProductiveCallOutletAuto = (AutoCompleteTextView) findViewById(R.id.unProductiveCallOutletAuto);
		txtMakeUnProductiveCallReason = (EditText) findViewById(R.id.txtMakeUnProductiveCallReason);
		btnUnProductiveCallSubmit = (Button) findViewById(R.id.btnUnProductiveCallSubmit);
		btnUnProductiveCallSubmit.setEnabled(false);

		unProductiveCallOutletAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				unProductiveCallOutletAutoItemSelected(adapterView, view, position, id);
			}
		});

		ArrayList<Outlet> outlets = OutletController.getOutlets(this);
		ArrayAdapter<Outlet> outletAdapter = new ArrayAdapter<Outlet>(this, android.R.layout.simple_dropdown_item_1line, outlets);
		unProductiveCallOutletAuto.setAdapter(outletAdapter);

		btnUnProductiveCallSubmit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnUnProductiveCallSubmitClicked(view);
			}
		});
		GPS_CHECKER.execute();
	}
	// </editor-fold>

	private void unProductiveCallOutletAutoItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
		Outlet outlet = (Outlet) adapterView.getAdapter().getItem(position);
		outletId = outlet.getOutletId();
	}

	private void btnUnProductiveCallSubmitClicked(View view) {
		UnProductiveCall unProductiveCall = new UnProductiveCall(
				outletId,
				txtMakeUnProductiveCallReason.getText().toString(),
				lastKnownLocation.getTime(),
				lastKnownLocation.getLongitude(),
				lastKnownLocation.getLatitude(),
				BatteryService.getBatteryLevel(this),
				UserController.getAuthorizedUser(this).getUserId()
		);
		UnProductiveCallController.makeUnProductiveCall(unProductiveCall, this);
	}
}
