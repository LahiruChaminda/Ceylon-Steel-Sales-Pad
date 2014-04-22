/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 10, 2014, 10:38:24 AM
 */
package com.xfinity.ceylon_steel.activity.make_sales_order;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import com.xfinity.ceylon_steel.R;
import com.xfinity.ceylon_steel.controller.DriverController;
import com.xfinity.ceylon_steel.controller.OutletController;
import com.xfinity.ceylon_steel.controller.VehicleController;
import com.xfinity.ceylon_steel.model.Driver;
import com.xfinity.ceylon_steel.model.Order;
import com.xfinity.ceylon_steel.model.Outlet;
import com.xfinity.ceylon_steel.model.Vehicle;
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
public class MakeDirectSalesOrderActivity extends Activity {

	private AutoCompleteTextView makeDirectOrderOutletAuto;
	private AutoCompleteTextView makeDirectOrderVehicleAuto;
	private AutoCompleteTextView makeDirectOrderDriverAuto;
	private EditText makeDirectOrderDriverNIC;
	private DatePicker makeDirectOrderDeliveryDate;
	private EditText makeDirectOrderRemarks;
	private Button btnMakeDirectOrderNext;

	private Location lastKnownLocation;
	private GpsReceiver gpsReceiver;

	private final AsyncTask<Void, Void, Void> GPS_CHECKER = new AsyncTask<Void, Void, Void>() {
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(MakeDirectSalesOrderActivity.this);
			progressDialog.setMessage("Waiting for GPS Location...");
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
			btnMakeDirectOrderNext.setEnabled(true);
		}
	};

	private Outlet outlet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.make_direct_order_page);
		gpsReceiver = GpsReceiver.getGpsReceiver(getApplicationContext());
		initialize();
	}

	@Override
	public void onBackPressed() {
		Intent makeSalesOrderActivity = new Intent(this, MakeSalesOrderActivity.class);
		startActivity(makeSalesOrderActivity);
		finish();
	}

	// <editor-fold defaultstate="collapsed" desc="Initialize">
	private void initialize() {
		makeDirectOrderOutletAuto = (AutoCompleteTextView) findViewById(R.id.makeDirectOrderOutletAuto);
		makeDirectOrderVehicleAuto = (AutoCompleteTextView) findViewById(R.id.makeDirectOrderVehicleAuto);
		makeDirectOrderDriverAuto = (AutoCompleteTextView) findViewById(R.id.makeDirectOrderDriverAuto);
		makeDirectOrderDriverNIC = (EditText) findViewById(R.id.makeDirectOrderDriverNIC);
		makeDirectOrderDeliveryDate = (DatePicker) findViewById(R.id.makeDirectOrderDeliveryDate);
		makeDirectOrderRemarks = (EditText) findViewById(R.id.makeDirectOrderRemarks);
		btnMakeDirectOrderNext = (Button) findViewById(R.id.btnMakeDirectOrderNext);
		btnMakeDirectOrderNext.setEnabled(true);

		btnMakeDirectOrderNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnMakeDirectOrderNextClicked(view);
			}
		});
		makeDirectOrderOutletAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				makeDirectOrderOutletAutoItemSelected(adapterView, view, position, id);
			}
		});
		makeDirectOrderDriverAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				makeDirectOrderDriverAutoItemSelected(adapterView, view, position, id);
			}
		});

		ArrayList<Outlet> outlets = OutletController.getOutlets(this);
		ArrayAdapter<Outlet> outletAdapter = new ArrayAdapter<Outlet>(this, android.R.layout.simple_dropdown_item_1line, outlets);
		makeDirectOrderOutletAuto.setAdapter(outletAdapter);

		ArrayList<Vehicle> vehicles = VehicleController.getVehicles(this);
		ArrayAdapter<Vehicle> vehicleAdapter = new ArrayAdapter<Vehicle>(this, android.R.layout.simple_dropdown_item_1line, vehicles);
		makeDirectOrderVehicleAuto.setAdapter(vehicleAdapter);

		ArrayList<Driver> drivers = DriverController.getDrivers(this);
		ArrayAdapter<Driver> driverAdapter = new ArrayAdapter<Driver>(this, android.R.layout.simple_dropdown_item_1line, drivers);
		makeDirectOrderDriverAuto.setAdapter(driverAdapter);

		GPS_CHECKER.execute();
	}
	// </editor-fold>

	private void btnMakeDirectOrderNextClicked(View view) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		if (outlet == null) {
			dialogBuilder.setTitle(R.string.message_title);
			dialogBuilder.setMessage(R.string.no_outlet_found);
			dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface arg0, int arg1) {
					makeDirectOrderOutletAuto.requestFocus();
				}
			});
			dialogBuilder.show();
			return;
		}
		if (makeDirectOrderVehicleAuto.getText().toString().isEmpty()) {
			dialogBuilder.setTitle(R.string.message_title);
			dialogBuilder.setMessage(R.string.no_vehicle_found);
			dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface arg0, int arg1) {
					makeDirectOrderVehicleAuto.requestFocus();
				}
			});
			dialogBuilder.show();
			return;
		}
		if (makeDirectOrderDriverAuto.getText().toString().isEmpty()) {
			dialogBuilder.setTitle(R.string.message_title);
			dialogBuilder.setMessage(R.string.no_driver_found);
			dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface arg0, int arg1) {
					makeDirectOrderDriverAuto.requestFocus();
				}
			});
			dialogBuilder.show();
			return;
		}
		if (makeDirectOrderDriverNIC.getText().toString().isEmpty()) {
			dialogBuilder.setTitle(R.string.message_title);
			dialogBuilder.setMessage(R.string.no_driver_nic_found);
			dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface arg0, int arg1) {
					makeDirectOrderDriverNIC.requestFocus();
				}
			});
			dialogBuilder.show();
			return;
		}
		Order order = new Order();
		long dateInMilliSeconds = makeDirectOrderDeliveryDate.getCalendarView().getDate();
		order.setOrderType(Order.DIRECT);
		order.setOrderMadeTimeStamp(lastKnownLocation.getTime());
		order.setLatitude(lastKnownLocation.getLatitude());
		order.setLongitude(lastKnownLocation.getLongitude());
		order.setDeliveryDate(dateInMilliSeconds);
		order.setOutletId(outlet.getOutletId());
		order.setVehicle(makeDirectOrderVehicleAuto.getText().toString());
		order.setDriver(makeDirectOrderDriverAuto.getText().toString());
		order.setDriverNIC(makeDirectOrderDriverNIC.getText().toString());
		order.setRemarks(makeDirectOrderRemarks.getText().toString());
		order.setBatteryLevel(BatteryService.getBatteryLevel(this));
		Intent categoriesAndItems = new Intent(this, SelectCategoryActivity.class);
		categoriesAndItems.putExtra("order", order);
		startActivity(categoriesAndItems);
		finish();
	}

	private void makeDirectOrderOutletAutoItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
		outlet = (Outlet) adapterView.getAdapter().getItem(position);
	}

	private void makeDirectOrderDriverAutoItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
		Driver driver = (Driver) adapterView.getAdapter().getItem(position);
		makeDirectOrderDriverNIC.setText(driver.toString());
	}
}
