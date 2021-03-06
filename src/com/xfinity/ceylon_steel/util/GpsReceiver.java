/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2014, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : April 5, 2014, 01:38:42 PM
 */
package com.xfinity.ceylon_steel.util;

import android.app.AlarmManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;

import java.util.List;

public class GpsReceiver extends Service {


	private static final long MINIMUM_DISTANCE_CHANGE = 0;
	private static final long MINIMUM_TIME_DIFFERENCE = 0;
	protected static LocationManager locationManager;
	private volatile static Location lastKnownLocation;
	private volatile static GpsReceiver gpsReceiver;

	private GpsReceiver(Context context) {
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

		//look for fastest location fix
		Criteria criteria = new Criteria();
		String bestProvider = locationManager.getBestProvider(criteria, true);
		lastKnownLocation = locationManager.getLastKnownLocation(bestProvider);

		List<String> matchingProviders = locationManager.getAllProviders();
		for (String provider : matchingProviders) {
			Location location = locationManager.getLastKnownLocation(provider);
			lastKnownLocation = (lastKnownLocation != null && location != null && location.getAccuracy() < lastKnownLocation.getAccuracy()) ? location : lastKnownLocation;
			locationManager.requestLocationUpdates(provider, MINIMUM_TIME_DIFFERENCE, MINIMUM_DISTANCE_CHANGE, LocationListenerImpl.getInstance(), Looper.getMainLooper());
		}
	}

	public synchronized static GpsReceiver getGpsReceiver(Context applicationContext) {
		return (gpsReceiver == null) ? gpsReceiver = new GpsReceiver(applicationContext) : gpsReceiver;
	}

	public synchronized Location getHighAccurateLocation() {
		lastKnownLocation = null;
		do {
			if (lastKnownLocation != null) {
				if (lastKnownLocation.getLatitude() == 0 && lastKnownLocation.getLongitude() == 0) {
					return lastKnownLocation = null;
				}
				long time = lastKnownLocation.getTime();
				long currentTimeMillis = System.currentTimeMillis();
				long timeDifference = Math.abs(time - currentTimeMillis);
				if (timeDifference > AlarmManager.INTERVAL_HALF_HOUR) {
					return lastKnownLocation = null;
				}
			}
		} while (lastKnownLocation == null || (lastKnownLocation != null && (lastKnownLocation.getLatitude() == 0 || lastKnownLocation.getLongitude() == 0)));
		return lastKnownLocation;
	}

	public synchronized Location getLastKnownLocation() {
		if (lastKnownLocation != null) {
			if (lastKnownLocation.getLatitude() == 0 && lastKnownLocation.getLongitude() == 0) {
				return lastKnownLocation = null;
			}
			long time = lastKnownLocation.getTime();
			long currentTimeMillis = System.currentTimeMillis();
			long timeDifference = Math.abs(time - currentTimeMillis);
			if (timeDifference > AlarmManager.INTERVAL_HALF_HOUR) {
				return lastKnownLocation = null;
			}
		}
		return lastKnownLocation;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private static class LocationListenerImpl implements LocationListener {
		private static LocationListenerImpl locationListener;

		private LocationListenerImpl() {
		}

		public static LocationListenerImpl getInstance() {
			return (locationListener == null) ? locationListener = new LocationListenerImpl() : locationListener;
		}

		@Override
		public void onLocationChanged(Location location) {
			lastKnownLocation = location;
			BusProvider.getInstance().post(location);
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
