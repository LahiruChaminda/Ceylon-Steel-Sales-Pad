/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 15, 2014, 11:48:28 AM
 */
package com.xfinity.ceylon_steel.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class InternetObserver {

	private static InternetObserver instance;
	private final ConnectivityManager connectivityManager;

	private InternetObserver(Context context) {
		connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	public static synchronized boolean isConnectedToInternet(Context context) {
		if (instance == null) {
			instance = new InternetObserver(context);
		}
		NetworkInfo networkInfo = instance.connectivityManager.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnected();
	}

}
