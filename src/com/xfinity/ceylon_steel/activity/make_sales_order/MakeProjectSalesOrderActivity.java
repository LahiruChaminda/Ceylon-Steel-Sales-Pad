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
import com.xfinity.ceylon_steel.controller.CustomerController;
import com.xfinity.ceylon_steel.controller.DriverController;
import com.xfinity.ceylon_steel.controller.UserController;
import com.xfinity.ceylon_steel.controller.VehicleController;
import com.xfinity.ceylon_steel.model.Customer;
import com.xfinity.ceylon_steel.model.Driver;
import com.xfinity.ceylon_steel.model.Order;
import com.xfinity.ceylon_steel.model.User;
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
public class MakeProjectSalesOrderActivity extends Activity {

	private AutoCompleteTextView makeProjectOrderCustomerAuto;
	private AutoCompleteTextView makeProjectOrderDistributorAuto;
	private AutoCompleteTextView makeProjectOrderVehicleAuto;
	private AutoCompleteTextView makeProjectOrderDriverAuto;
	private EditText makeProjectOrderDriverNIC;
	private DatePicker makeProjectOrderDeliveryDate;
	private EditText makeProjectOrderRemarks;
	private Button btnMakeProjectOrderNext;

	private Customer customer;
	private User distributor;

	private Location lastKnownLocation;
	private GpsReceiver gpsReceiver;

	private final AsyncTask<Void, Void, Void> GPS_CHECKER = new AsyncTask<Void, Void, Void>() {
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(MakeProjectSalesOrderActivity.this);
			progressDialog.setCanceledOnTouchOutside(false);
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
			btnMakeProjectOrderNext.setEnabled(true);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.make_project_order_page);
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
		makeProjectOrderCustomerAuto = (AutoCompleteTextView) findViewById(R.id.makeProjectOrderCustomerAuto);
		makeProjectOrderDistributorAuto = (AutoCompleteTextView) findViewById(R.id.makeProjectOrderDistributorAuto);
		makeProjectOrderVehicleAuto = (AutoCompleteTextView) findViewById(R.id.makeProjectOrderVehicleAuto);
		makeProjectOrderDriverAuto = (AutoCompleteTextView) findViewById(R.id.makeProjectOrderDriverAuto);
		makeProjectOrderDriverNIC = (EditText) findViewById(R.id.makeProjectOrderDriverNIC);
		makeProjectOrderDeliveryDate = (DatePicker) findViewById(R.id.makeProjectOrderDeliveryDate);
		makeProjectOrderRemarks = (EditText) findViewById(R.id.makeProjectOrderRemarks);
		btnMakeProjectOrderNext = (Button) findViewById(R.id.btnMakeProjectOrderNext);
		btnMakeProjectOrderNext.setEnabled(true);

		btnMakeProjectOrderNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnMakeProjectOrderNextClicked(view);
			}
		});
		makeProjectOrderCustomerAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				makeProjectOrderCustomerAutoItemSelected(adapterView, view, position, id);
			}
		});
		makeProjectOrderDistributorAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				makeProjectOrderDistributorAutoItemSelected(adapterView, view, position, id);
			}
		});
		makeProjectOrderDriverAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				makeProjectOrderDriverAutoItemSelected(adapterView, view, position, id);
			}
		});

		ArrayList<Customer> customers = CustomerController.getCustomers(this);
		ArrayAdapter<Customer> customerAdapter = new ArrayAdapter<Customer>(this, android.R.layout.simple_dropdown_item_1line, customers);
		makeProjectOrderCustomerAuto.setAdapter(customerAdapter);

		ArrayList<User> distributors = UserController.getDistributors(this);
		ArrayAdapter<User> distributorAdapter = new ArrayAdapter<User>(this, android.R.layout.simple_dropdown_item_1line, distributors);
		makeProjectOrderDistributorAuto.setAdapter(distributorAdapter);

		ArrayList<Vehicle> vehicles = VehicleController.getVehicles(this);
		ArrayAdapter<Vehicle> vehicleAdapter = new ArrayAdapter<Vehicle>(this, android.R.layout.simple_dropdown_item_1line, vehicles);
		makeProjectOrderVehicleAuto.setAdapter(vehicleAdapter);

		ArrayList<Driver> drivers = DriverController.getDrivers(this);
		ArrayAdapter<Driver> driverAdapter = new ArrayAdapter<Driver>(this, android.R.layout.simple_dropdown_item_1line, drivers);
		makeProjectOrderDriverAuto.setAdapter(driverAdapter);

		GPS_CHECKER.execute();
	}
	// </editor-fold>

	private void makeProjectOrderCustomerAutoItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
		customer = (Customer) adapterView.getAdapter().getItem(position);
	}

	private void makeProjectOrderDistributorAutoItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
		distributor = (User) adapterView.getAdapter().getItem(position);
	}

	private void makeProjectOrderDriverAutoItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
		Driver driver = (Driver) adapterView.getAdapter().getItem(position);
		makeProjectOrderDriverNIC.setText(driver.toString());
	}

	private void btnMakeProjectOrderNextClicked(View view) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		if (customer == null) {
			dialogBuilder.setTitle(R.string.message_title);
			dialogBuilder.setMessage(R.string.no_customer_found);
			dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
					makeProjectOrderCustomerAuto.requestFocus();
				}
			});
			dialogBuilder.show();
			return;
		}
		if (distributor == null) {
			dialogBuilder.setTitle(R.string.message_title);
			dialogBuilder.setMessage(R.string.no_distributor_found);
			dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface arg0, int arg1) {
					makeProjectOrderDistributorAuto.requestFocus();
				}
			});
			dialogBuilder.show();
			return;
		}
		if (makeProjectOrderVehicleAuto.getText().toString().isEmpty()) {
			dialogBuilder.setTitle(R.string.message_title);
			dialogBuilder.setMessage(R.string.no_vehicle_found);
			dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface arg0, int arg1) {
					makeProjectOrderVehicleAuto.requestFocus();
				}
			});
			dialogBuilder.show();
			return;
		}
		if (makeProjectOrderDriverAuto.getText().toString().isEmpty()) {
			dialogBuilder.setTitle(R.string.message_title);
			dialogBuilder.setMessage(R.string.no_driver_found);
			dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface arg0, int arg1) {
					makeProjectOrderDriverAuto.requestFocus();
				}
			});
			dialogBuilder.show();
			return;
		}
		if (makeProjectOrderDriverNIC.getText().toString().isEmpty()) {
			dialogBuilder.setTitle(R.string.message_title);
			dialogBuilder.setMessage(R.string.no_driver_nic_found);
			dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface arg0, int arg1) {
					makeProjectOrderDriverNIC.requestFocus();
				}
			});
			dialogBuilder.show();
			return;
		}
		Order order = new Order();
		long dateInMilliSeconds = makeProjectOrderDeliveryDate.getCalendarView().getDate();
		order.setOrderType(Order.PROJECT);
		do {
			lastKnownLocation = gpsReceiver.getLastKnownLocation();
		} while (lastKnownLocation == null);
		order.setOrderMadeTimeStamp(lastKnownLocation.getTime());
		order.setLatitude(lastKnownLocation.getLatitude());
		order.setLongitude(lastKnownLocation.getLongitude());
		order.setDeliveryDate(dateInMilliSeconds);
		order.setCustomerId(customer.getCustomerId());
		order.setDistributorId(distributor.getUserId());
		order.setVehicle(makeProjectOrderVehicleAuto.getText().toString());
		order.setDriver(makeProjectOrderDistributorAuto.getText().toString());
		order.setDriverNIC(makeProjectOrderDriverNIC.getText().toString());
		order.setRemarks(makeProjectOrderRemarks.getText().toString());
		order.setBatteryLevel(BatteryService.getBatteryLevel(this));
		Intent categoriesAndItems = new Intent(this, SelectCategoryActivity.class);
		categoriesAndItems.putExtra("order", order);
		startActivity(categoriesAndItems);
		finish();
	}

}
