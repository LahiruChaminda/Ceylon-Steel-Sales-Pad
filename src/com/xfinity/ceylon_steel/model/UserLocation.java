/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 9, 2014, 5:16:36 PM
 */
package com.xfinity.ceylon_steel.model;

import java.sql.Timestamp;
import java.util.HashMap;
import org.json.JSONObject;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class UserLocation {

	private int repLocationId;
	private int repId;
	private int batteryLevel;
	private double longitude;
	private double latitude;
	private long timestamp;

	public UserLocation() {
	}

	public UserLocation(double longitude, double latitude, long timestamp, int batteryLevel) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.timestamp = timestamp;
		this.batteryLevel = batteryLevel;
	}

	public UserLocation(double longitude, double latitude, long timestamp, int repId, int repLocationId, int batteryLevel) {
		this.repLocationId = repLocationId;
		this.repId = repId;
		this.longitude = longitude;
		this.latitude = latitude;
		this.timestamp = timestamp;
		this.batteryLevel = batteryLevel;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the repLocationId
	 */
	public int getRepLocationId() {
		return repLocationId;
	}

	/**
	 * @param repLocationId the repLocationId to set
	 */
	public void setRepLocationId(int repLocationId) {
		this.repLocationId = repLocationId;
	}

	/**
	 * @return the repId
	 */
	public int getRepId() {
		return repId;
	}

	/**
	 * @param repId the repId to set
	 */
	public void setRepId(int repId) {
		this.repId = repId;
	}

	public JSONObject getUserLocationJson() {
		HashMap<String, Object> userLocationJson = new HashMap<String, Object>();
		userLocationJson.put("repId", repId);
		userLocationJson.put("longitude", longitude);
		userLocationJson.put("latitude", latitude);
		userLocationJson.put("timestamp", new Timestamp(timestamp).toString());
		userLocationJson.put("batteryLevel", batteryLevel);
		return new JSONObject(userLocationJson);
	}

	public int getBatteryLevel() {
		return batteryLevel;
	}

	public void setBatteryLevel(int batteryLevel) {
		this.batteryLevel = batteryLevel;
	}
}
