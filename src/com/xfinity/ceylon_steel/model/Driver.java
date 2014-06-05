/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 11, 2014, 8:57:12 PM
 */
package com.xfinity.ceylon_steel.model;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class Driver {

	private String driverName;
	private String driverNIC;

	public Driver() {
	}

	public Driver(String driverName, String driverNIC) {
		this.driverName = driverName;
		this.driverNIC = driverNIC;
	}

	/**
	 * @return the driverName
	 */
	public String getDriverName() {
		return driverName;
	}

	/**
	 * @param driverName the driverName to set
	 */
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	/**
	 * @return the driverNIC
	 */
	public String getDriverNIC() {
		return driverNIC;
	}

	/**
	 * @param driverNIC the driverNIC to set
	 */
	public void setDriverNIC(String driverNIC) {
		this.driverNIC = driverNIC;
	}

	@Override
	public String toString() {
		return driverName;
	}

}
