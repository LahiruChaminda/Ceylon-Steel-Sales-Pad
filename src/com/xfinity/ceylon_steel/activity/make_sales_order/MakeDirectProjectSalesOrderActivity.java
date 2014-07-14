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
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;
import com.xfinity.ceylon_steel.R;
import com.xfinity.ceylon_steel.controller.CustomerController;
import com.xfinity.ceylon_steel.controller.DriverController;
import com.xfinity.ceylon_steel.controller.VehicleController;
import com.xfinity.ceylon_steel.model.Customer;
import com.xfinity.ceylon_steel.model.Driver;
import com.xfinity.ceylon_steel.model.Order;
import com.xfinity.ceylon_steel.model.Vehicle;
import com.xfinity.ceylon_steel.service.BatteryService;
import com.xfinity.ceylon_steel.service.GpsReceiver;

import java.util.ArrayList;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class MakeDirectProjectSalesOrderActivity extends Activity {

	private AutoCompleteTextView makeDirectProjectOrderCustomerAuto;
	private AutoCompleteTextView makeDirectProjectOrderVehicleAuto;
	private AutoCompleteTextView makeDirectProjectOrderDriverAuto;
	private EditText makeDirectProjectOrderDriverNIC;
	private DatePicker makeDirectProjectOrderDeliveryDate;
	private EditText makeDirectProjectOrderRemarks;
	private Button btnMakeDirectProjectOrderNext;

	private Location lastKnownLocation;
	private Thread GPS_CHECKER;
	private GpsReceiver gpsReceiver;
	private Customer customer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.make_direct_project_order_page);
		initialize();

		ArrayList<Customer> customers = CustomerController.getCustomers(this);
		ArrayAdapter<Customer> outletAdapter = new ArrayAdapter<Customer>(this, android.R.layout.simple_dropdown_item_1line, customers);
		makeDirectProjectOrderCustomerAuto.setAdapter(outletAdapter);

		ArrayList<Vehicle> vehicles = VehicleController.getVehicles(this);
		ArrayAdapter<Vehicle> vehicleAdapter = new ArrayAdapter<Vehicle>(this, android.R.layout.simple_dropdown_item_1line, vehicles);
		makeDirectProjectOrderVehicleAuto.setAdapter(vehicleAdapter);

		ArrayList<Driver> drivers = DriverController.getDrivers(this);
		ArrayAdapter<Driver> driverAdapter = new ArrayAdapter<Driver>(this, android.R.layout.simple_dropdown_item_1line, drivers);
		makeDirectProjectOrderDriverAuto.setAdapter(driverAdapter);

		gpsReceiver = GpsReceiver.getGpsReceiver(getApplicationContext());
		GPS_CHECKER = new Thread() {
			private Handler handler = new Handler();
			private ProgressDialog progressDialog;

			@Override
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						progressDialog = new ProgressDialog(MakeDirectProjectSalesOrderActivity.this);
						progressDialog.setMessage("Waiting for GPS Location...");
						progressDialog.setCanceledOnTouchOutside(false);
						progressDialog.show();
					}
				});
				do {
					lastKnownLocation = gpsReceiver.getLastKnownLocation();
				} while (lastKnownLocation == null);
				handler.post(new Runnable() {
					@Override
					public void run() {
						if (progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
						btnMakeDirectProjectOrderNext.setEnabled(true);
					}
				});
			}
		};
		GPS_CHECKER.start();
	}

	@Override
	public void onBackPressed() {
		Intent makeSalesOrderActivity = new Intent(this, MakeSalesOrderActivity.class);
		startActivity(makeSalesOrderActivity);
		finish();
	}

	// <editor-fold defaultstate="collapsed" desc="Initialize">
	private void initialize() {
		makeDirectProjectOrderCustomerAuto = (AutoCompleteTextView) findViewById(R.id.makeDirectProjectOrderCustomerAuto);
		makeDirectProjectOrderVehicleAuto = (AutoCompleteTextView) findViewById(R.id.makeDirectProjectOrderVehicleAuto);
		makeDirectProjectOrderDriverAuto = (AutoCompleteTextView) findViewById(R.id.makeDirectProjectOrderDriverAuto);
		makeDirectProjectOrderDriverNIC = (EditText) findViewById(R.id.makeDirectProjectOrderDriverNIC);
		makeDirectProjectOrderDeliveryDate = (DatePicker) findViewById(R.id.makeDirectProjectOrderDeliveryDate);
		makeDirectProjectOrderRemarks = (EditText) findViewById(R.id.makeDirectProjectOrderRemarks);
		btnMakeDirectProjectOrderNext = (Button) findViewById(R.id.btnMakeDirectProjectOrderNext);
		btnMakeDirectProjectOrderNext.setEnabled(true);

		btnMakeDirectProjectOrderNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnMakeDirectProjectOrderNextClicked(view);
			}
		});
		makeDirectProjectOrderCustomerAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				makeDirectProjectOrderCustomerAutoItemSelected(adapterView, view, position, id);
			}
		});
		makeDirectProjectOrderDriverAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				makeDirectProjectOrderDriverAutoItemSelected(adapterView, view, position, id);
			}
		});
	}
	// </editor-fold>

	private void btnMakeDirectProjectOrderNextClicked(View view) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		if (customer == null) {
			dialogBuilder.setTitle(R.string.message_title);
			dialogBuilder.setMessage(R.string.no_customer_found);
			dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
					makeDirectProjectOrderCustomerAuto.requestFocus();
				}
			});
			dialogBuilder.show();
			return;
		}
		if (makeDirectProjectOrderVehicleAuto.getText().toString().isEmpty()) {
			dialogBuilder.setTitle(R.string.message_title);
			dialogBuilder.setMessage(R.string.no_vehicle_found);
			dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
					makeDirectProjectOrderVehicleAuto.requestFocus();
				}
			});
			dialogBuilder.show();
			return;
		}
		if (makeDirectProjectOrderDriverAuto.getText().toString().isEmpty()) {
			dialogBuilder.setTitle(R.string.message_title);
			dialogBuilder.setMessage(R.string.no_driver_found);
			dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface arg0, int arg1) {
					makeDirectProjectOrderDriverAuto.requestFocus();
				}
			});
			dialogBuilder.show();
			return;
		}
		if (makeDirectProjectOrderDriverNIC.getText().toString().isEmpty()) {
			dialogBuilder.setTitle(R.string.message_title);
			dialogBuilder.setMessage(R.string.no_driver_nic_found);
			dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface arg0, int arg1) {
					makeDirectProjectOrderDriverNIC.requestFocus();
				}
			});
			dialogBuilder.show();
			return;
		}
		Order order = new Order();
		long dateInMilliSeconds = makeDirectProjectOrderDeliveryDate.getCalendarView().getDate();
		order.setOrderType(Order.DIRECT_PROJECT);
		do {
			lastKnownLocation = gpsReceiver.getLastKnownLocation();
		} while (lastKnownLocation == null);
		order.setOrderMadeTimeStamp(lastKnownLocation.getTime());
		order.setCustomerId(customer.getCustomerId());
		order.setLatitude(lastKnownLocation.getLatitude());
		order.setLongitude(lastKnownLocation.getLongitude());
		order.setDeliveryDate(dateInMilliSeconds);
		order.setOutletId(customer.getCustomerId());
		order.setVehicle(makeDirectProjectOrderVehicleAuto.getText().toString());
		order.setDriver(makeDirectProjectOrderDriverAuto.getText().toString());
		order.setDriverNIC(makeDirectProjectOrderDriverNIC.getText().toString());
		order.setRemarks(makeDirectProjectOrderRemarks.getText().toString());
		order.setBatteryLevel(BatteryService.getBatteryLevel(this));
		Intent categoriesAndItems = new Intent(this, SelectCategoryActivity.class);
		categoriesAndItems.putExtra("order", order);
		startActivity(categoriesAndItems);
		finish();
	}

	private void makeDirectProjectOrderCustomerAutoItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
		customer = (Customer) adapterView.getAdapter().getItem(position);
	}

	private void makeDirectProjectOrderDriverAutoItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
		Driver driver = (Driver) adapterView.getAdapter().getItem(position);
		makeDirectProjectOrderDriverNIC.setText(driver.toString());
	}
}
