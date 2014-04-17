/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 8, 2014, 8:58:14 PM
 */
package com.xfinity.ceylon_steel.controller;

import android.content.Context;
import com.xfinity.ceylon_steel.service.InternetObserver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
abstract class AbstractController {

	//protected static final String webServiceURL = "http://192.168.1.181/Ceylon_Steel/native/";
	protected static final String webServiceURL = "http://gateway.ceylonlinux.com/Ceylon_Steel/native/";

	protected static final class CategoryURL {

		public static final String getItemsAndCategories = webServiceURL + "getItemsAndCategories";
	}

	protected static final class CustomerURL {

		public static final String getCustomersOfUser = webServiceURL + "getCustomersOfUser";
	}

	protected static final class DriverURL {

		public static final String getDriversOfUser = webServiceURL + "getDriversOfUser";
	}

	protected static final class OrderURL {

		public static final String placeSalesOrder = webServiceURL + "placeSalesOrder";
	}

	protected static final class OutletURL {

		public static final String getOutletsOfUser = webServiceURL + "getOutletsOfUser";
	}

	protected static final class UnProductiveCallURL {

		public static final String recordUnProductiveCall = webServiceURL + "recordUnProductiveCall";
	}

	protected static final class UserURL {

		public static final String getUserDetails = webServiceURL + "getUserDetails";
		public static final String checkInCheckOut = webServiceURL + "checkInCheckOut";
		public static final String getDistributorsOfUser = webServiceURL + "getDistributorsOfUser";
		public static final String markRepLocations = webServiceURL + "markRepLocations";
	}

	protected static final class VehicleURL {

		public static final String getVehiclesOfUser = webServiceURL + "getVehiclesOfUser";
	}

	protected final static JSONObject getJsonObject(String url, HashMap<String, Object> parameters, Context context) throws IOException, JSONException {
		if (InternetObserver.isConnectedToInternet(context)) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost postRequest = new HttpPost(url);
			ArrayList<NameValuePair> httpPostParameters = new ArrayList<NameValuePair>();
			if (parameters != null) {
				for (String parameter : parameters.keySet()) {
					httpPostParameters.add(new BasicNameValuePair(parameter, parameters.get(parameter).toString()));
				}
				postRequest.setEntity(new UrlEncodedFormEntity(httpPostParameters));
			}
			HttpResponse response = httpClient.execute(postRequest);
			BufferedReader bufferedReader = null;
			String lineSeparator = System.getProperty("line.separator");
			String responseString = "";
			try {
				bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String currentLine;
				while ((currentLine = bufferedReader.readLine()) != null) {
					responseString = responseString + currentLine + lineSeparator;
				}
				return new JSONObject(responseString);
			} finally {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			}
		}
		return null;
	}

	protected final static JSONArray getJsonArray(String url, HashMap<String, Object> parameters, Context context) throws IOException, JSONException {
		if (InternetObserver.isConnectedToInternet(context)) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost postRequest = new HttpPost(url);
			ArrayList<NameValuePair> httpPostParameters = new ArrayList<NameValuePair>();
			if (parameters != null) {
				for (String parameter : parameters.keySet()) {
					httpPostParameters.add(new BasicNameValuePair(parameter, parameters.get(parameter).toString()));
				}
				postRequest.setEntity(new UrlEncodedFormEntity(httpPostParameters));
			}
			HttpResponse response = httpClient.execute(postRequest);
			BufferedReader bufferedReader = null;
			String lineSeparator = System.getProperty("line.separator");
			String responseString = "";
			try {
				bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String currentLine;
				while ((currentLine = bufferedReader.readLine()) != null) {
					responseString = responseString + currentLine + lineSeparator;
				}
				return new JSONArray(responseString);
			} finally {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			}
		}
		return null;
	}

}
