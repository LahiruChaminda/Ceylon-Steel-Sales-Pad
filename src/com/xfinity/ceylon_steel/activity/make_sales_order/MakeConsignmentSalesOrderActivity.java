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
import com.xfinity.ceylon_steel.controller.DriverController;
import com.xfinity.ceylon_steel.controller.OutletController;
import com.xfinity.ceylon_steel.controller.UserController;
import com.xfinity.ceylon_steel.controller.VehicleController;
import com.xfinity.ceylon_steel.model.*;
import com.xfinity.ceylon_steel.service.BatteryService;
import com.xfinity.ceylon_steel.service.GpsReceiver;

import java.util.ArrayList;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class MakeConsignmentSalesOrderActivity extends Activity {

	private Button btnMakeConsignmentOrderNext;
	private AutoCompleteTextView makeConsignmentOrderOutletAuto;
	private AutoCompleteTextView makeConsignmentOrderDistributorAuto;
	private AutoCompleteTextView makeConsignmentOrderVehicleAuto;
	private AutoCompleteTextView makeConsignmentOrderDriverAuto;
	private EditText makeConsignmentOrderDriverNIC;
	private DatePicker makeConsignmentOrderDeliveryDate;
	private EditText makeConsignmentOrderRemarks;

	private Outlet outlet;
	private User distributor;

	private Location lastKnownLocation;
	private Thread GPS_CHECKER;
	private GpsReceiver gpsReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.make_consignment_order_page);
		initialize();

		ArrayList<Outlet> outlets = OutletController.getOutlets(this);
		ArrayAdapter<Outlet> outletAdapter = new ArrayAdapter<Outlet>(this, android.R.layout.simple_dropdown_item_1line, outlets);
		makeConsignmentOrderOutletAuto.setAdapter(outletAdapter);

		ArrayList<User> distributors = UserController.getDistributors(this);
		ArrayAdapter<User> distributorAdapter = new ArrayAdapter<User>(this, android.R.layout.simple_dropdown_item_1line, distributors);
		makeConsignmentOrderDistributorAuto.setAdapter(distributorAdapter);

		ArrayList<Vehicle> vehicles = VehicleController.getVehicles(this);
		ArrayAdapter<Vehicle> vehicleAdapter = new ArrayAdapter<Vehicle>(this, android.R.layout.simple_dropdown_item_1line, vehicles);
		makeConsignmentOrderVehicleAuto.setAdapter(vehicleAdapter);

		ArrayList<Driver> drivers = DriverController.getDrivers(this);
		ArrayAdapter<Driver> driverAdapter = new ArrayAdapter<Driver>(this, android.R.layout.simple_dropdown_item_1line, drivers);
		makeConsignmentOrderDriverAuto.setAdapter(driverAdapter);

		gpsReceiver = GpsReceiver.getGpsReceiver(getApplicationContext());
		GPS_CHECKER = new Thread() {
			private Handler handler = new Handler();
			private ProgressDialog progressDialog;

			@Override
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						progressDialog = new ProgressDialog(MakeConsignmentSalesOrderActivity.this);
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
						btnMakeConsignmentOrderNext.setEnabled(true);
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
		makeConsignmentOrderOutletAuto = (AutoCompleteTextView) findViewById(R.id.makeConsignmentOrderOutletAuto);
		makeConsignmentOrderDistributorAuto = (AutoCompleteTextView) findViewById(R.id.makeConsignmentOrderDistributorAuto);
		makeConsignmentOrderVehicleAuto = (AutoCompleteTextView) findViewById(R.id.makeConsignmentOrderVehicleAuto);
		makeConsignmentOrderDriverAuto = (AutoCompleteTextView) findViewById(R.id.makeConsignmentOrderDriverAuto);
		makeConsignmentOrderDriverNIC = (EditText) findViewById(R.id.makeConsignmentOrderDriverNIC);
		makeConsignmentOrderDeliveryDate = (DatePicker) findViewById(R.id.makeConsignmentOrderDeliveryDate);
		makeConsignmentOrderRemarks = (EditText) findViewById(R.id.makeConsignmentOrderRemarks);
		btnMakeConsignmentOrderNext = (Button) findViewById(R.id.btnMakeConsignmentOrderNext);
		btnMakeConsignmentOrderNext.setEnabled(false);

		btnMakeConsignmentOrderNext.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnMakeConsignmentOrderNextClicked(view);
			}
		});
		makeConsignmentOrderOutletAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				makeConsignmentOrderOutletAutoItemSelected(adapterView, view, position, id);
			}
		});
		makeConsignmentOrderDistributorAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				makeConsignmentOrderDistributorAutoItemSelected(adapterView, view, position, id);
			}
		});
		makeConsignmentOrderDriverAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				makeConsignmentOrderDriverAutoItemSelected(adapterView, view, position, id);
			}
		});
	}
// </editor-fold>

	private void makeConsignmentOrderOutletAutoItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
		outlet = (Outlet) adapterView.getAdapter().getItem(position);
	}

	private void makeConsignmentOrderDistributorAutoItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
		distributor = (User) adapterView.getAdapter().getItem(position);
	}

	private void makeConsignmentOrderDriverAutoItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
		Driver driver = (Driver) adapterView.getAdapter().getItem(position);
		makeConsignmentOrderDriverNIC.setText(driver.toString());
	}

	private void btnMakeConsignmentOrderNextClicked(View view) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		if (outlet == null) {
			dialogBuilder.setTitle(R.string.message_title);
			dialogBuilder.setMessage(R.string.no_outlet_found);
			dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface arg0, int arg1) {
					makeConsignmentOrderOutletAuto.requestFocus();
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
					makeConsignmentOrderDistributorAuto.requestFocus();
				}
			});
			dialogBuilder.show();
			return;
		}
		if (makeConsignmentOrderVehicleAuto.getText().toString().isEmpty()) {
			dialogBuilder.setTitle(R.string.message_title);
			dialogBuilder.setMessage(R.string.no_vehicle_found);
			dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface arg0, int arg1) {
					makeConsignmentOrderVehicleAuto.requestFocus();
				}
			});
			dialogBuilder.show();
			return;
		}
		if (makeConsignmentOrderDriverAuto.getText().toString().isEmpty()) {
			dialogBuilder.setTitle(R.string.message_title);
			dialogBuilder.setMessage(R.string.no_driver_found);
			dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface arg0, int arg1) {
					makeConsignmentOrderDriverAuto.requestFocus();
				}
			});
			dialogBuilder.show();
			return;
		}
		if (makeConsignmentOrderDriverNIC.getText().toString().isEmpty()) {
			dialogBuilder.setTitle(R.string.message_title);
			dialogBuilder.setMessage(R.string.no_driver_nic_found);
			dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface arg0, int arg1) {
					makeConsignmentOrderDriverNIC.requestFocus();
				}
			});
			dialogBuilder.show();
			return;
		}
		Order order = new Order();
		long dateInMilliSeconds = makeConsignmentOrderDeliveryDate.getCalendarView().getDate();
		order.setOrderType(Order.CONSIGNMENT);
		do {
			lastKnownLocation = gpsReceiver.getLastKnownLocation();
		} while (lastKnownLocation == null);
		order.setOrderMadeTimeStamp(lastKnownLocation.getTime());
		order.setLatitude(lastKnownLocation.getLatitude());
		order.setLongitude(lastKnownLocation.getLongitude());
		order.setDeliveryDate(dateInMilliSeconds);
		order.setOutletId(outlet.getOutletId());
		order.setDistributorId(distributor.getUserId());
		order.setVehicle(makeConsignmentOrderVehicleAuto.getText().toString());
		order.setDriver(makeConsignmentOrderDriverAuto.getText().toString());
		order.setDriverNIC(makeConsignmentOrderDriverNIC.getText().toString());
		order.setRemarks(makeConsignmentOrderRemarks.getText().toString());
		order.setRepId(UserController.getAuthorizedUser(this).getUserId());
		order.setBatteryLevel(BatteryService.getBatteryLevel(this));
		Intent categoriesAndItems = new Intent(this, SelectCategoryActivity.class);
		categoriesAndItems.putExtra("order", order);
		startActivity(categoriesAndItems);
		finish();
	}
}
