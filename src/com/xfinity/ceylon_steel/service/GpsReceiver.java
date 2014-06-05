/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2014, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : April 5, 2014, 01:38:42 PM
 */
package com.xfinity.ceylon_steel.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GpsReceiver extends Service {

	private static final long MINIMUM_DISTANCE_CHANGE = 0;
	private static final long MINIMUM_TIME_DIFFERENCE = 0;
	protected static LocationManager locationManager;
	private volatile static Location lastKnownLocation;
	private volatile static GpsReceiver gpsReceiver;

	private GpsReceiver(Context applicationContext) {
		locationManager = (LocationManager) applicationContext.getSystemService(Context.LOCATION_SERVICE);
		lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_DIFFERENCE, MINIMUM_DISTANCE_CHANGE, new LocationListenerImpl());
	}

	public synchronized static GpsReceiver getGpsReceiver(Context applicationContext) {
		if (gpsReceiver == null) {
			gpsReceiver = new GpsReceiver(applicationContext);
		}
		return gpsReceiver;
	}

	public synchronized Location getHighAccurateLocation() {
		lastKnownLocation = null;
		while (lastKnownLocation == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
				Logger.getLogger(GpsReceiver.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		if (lastKnownLocation != null && lastKnownLocation.getLatitude() != 0 && lastKnownLocation.getLongitude() != 0 && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			long time = lastKnownLocation.getTime();
			long currentTimeMillis = System.currentTimeMillis();
			long timeDifference = Math.abs(time - currentTimeMillis);
			if (timeDifference > 30 * 60 * 1000) {
				return null;
			}
		}
		return lastKnownLocation;
	}

	public synchronized Location getLastKnownLocation() {
		if (lastKnownLocation != null && lastKnownLocation.getLatitude() != 0 && lastKnownLocation.getLongitude() != 0 && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			long time = lastKnownLocation.getTime();
			long currentTimeMillis = System.currentTimeMillis();
			long timeDifference = Math.abs(time - currentTimeMillis);
			if (timeDifference > 30 * 60 * 1000) {
				return null;
			}
		}
		return lastKnownLocation;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private static class LocationListenerImpl implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			lastKnownLocation = location;
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

}
