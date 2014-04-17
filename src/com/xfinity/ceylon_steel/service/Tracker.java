/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 16, 2014, 12:11:56 PM
 */
package com.xfinity.ceylon_steel.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import com.xfinity.ceylon_steel.controller.UserController;
import com.xfinity.ceylon_steel.model.UserLocation;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class Tracker extends Service {

	private Timer timer;
	private GpsReceiver gpsReceiver;

	@Override
	public void onCreate() {
		super.onCreate();
		gpsReceiver = GpsReceiver.getGpsReceiver(getApplicationContext());
		timer = new Timer("Tracker");
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Location lastKnownLocation = null;
				do {
					lastKnownLocation = gpsReceiver.getLastKnownLocation();
				} while (lastKnownLocation == null);
				UserLocation userLocation = new UserLocation(
						lastKnownLocation.getLongitude(),
						lastKnownLocation.getLatitude(),
						lastKnownLocation.getTime(),
						BatteryService.getBatteryLevel(Tracker.this)
				);
				UserController.markRepLocation(getApplicationContext(), userLocation);
				UserController.syncRepLocations(getApplicationContext());
			}
		}, 0, 15 * 60 * 1000);//15*60*1000 is the millisecond for 15 minutes
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}
