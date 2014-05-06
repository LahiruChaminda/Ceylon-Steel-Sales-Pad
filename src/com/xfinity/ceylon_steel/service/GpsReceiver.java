/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2014, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : April 5, 2014, 01:38:42 PM
 */
package com.xfinity.ceylon_steel.service;

import android.app.Service;
import android.content.Context;
import static android.content.Context.LOCATION_SERVICE;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GpsReceiver extends Service {

	private volatile static Location lastKnownLocation;
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
	private static final long MIN_TIME_BW_UPDATES = 0;
	protected static LocationManager locationManager;
	private volatile static GpsReceiver gpsReceiver;

	public synchronized static GpsReceiver getGpsReceiver(Context applicationContext) {
		if (gpsReceiver == null) {
			gpsReceiver = new GpsReceiver(applicationContext);
		}
		return gpsReceiver;
	}

	private GpsReceiver(Context applicationContext) {
		locationManager = (LocationManager) applicationContext.getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, LocationListenerImpl.getLocationListener(), Looper.getMainLooper());
	}

	public static void stopUsingGPS() {
		if (locationManager != null) {
			locationManager.removeUpdates(LocationListenerImpl.getLocationListener());
		}
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

	private static class LocationListenerImpl implements LocationListener {

		private static LocationListenerImpl locationListener;

		private LocationListenerImpl() {
		}

		public static LocationListenerImpl getLocationListener() {
			return (locationListener != null) ? locationListener : new LocationListenerImpl();
		}

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

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
