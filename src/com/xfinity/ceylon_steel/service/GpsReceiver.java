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
import android.provider.Settings;
import java.util.Date;

public class GpsReceiver extends Service {

	private boolean isGPSEnabled = false;
	private static Location lastKnownLocation;
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
	private static final long MIN_TIME_BW_UPDATES = 500;
	protected static LocationManager locationManager;
	private static GpsReceiver gpsReceiver;

	public static GpsReceiver getGpsReceiver(Context applicationContext) {
		if (gpsReceiver == null) {
			gpsReceiver = new GpsReceiver(applicationContext);
		}
		return gpsReceiver;
	}

	private GpsReceiver(Context applicationContext) {
		locationManager = (LocationManager) applicationContext.getSystemService(LOCATION_SERVICE);
		isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (isGPSEnabled) {
			if (lastKnownLocation == null) {
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, LocationListenerImpl.getLocationListener(), Looper.getMainLooper());
				if (locationManager != null) {
					lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				}
			}
		} else {
			Intent gpsSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			applicationContext.startActivity(gpsSettings);
		}
	}

	public static void stopUsingGPS() {
		if (locationManager != null) {
			locationManager.removeUpdates(LocationListenerImpl.getLocationListener());
		}
	}

	public Location getHighAccurateLocation() {
		lastKnownLocation = null;
		while (lastKnownLocation == null) {
		}
		if (lastKnownLocation != null && lastKnownLocation.getLatitude() != 0 && lastKnownLocation.getLongitude() != 0) {
			long time = lastKnownLocation.getTime();
			Date date = new Date();
			long timeDifference = Math.abs(time - date.getTime());
			if (timeDifference > 30 * 60 * 1000) {
				return null;
			}
		}
		return lastKnownLocation;
	}
    public Location getLastKnownLocation() {
        if (lastKnownLocation != null && lastKnownLocation.getLatitude() != 0 && lastKnownLocation.getLongitude() != 0) {
            long time = lastKnownLocation.getTime();
            Date date = new Date();
            long timeDifference = Math.abs(time - date.getTime());
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
