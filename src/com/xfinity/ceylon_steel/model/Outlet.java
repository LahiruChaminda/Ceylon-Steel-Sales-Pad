/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 9, 2014, 5:20:10 PM
 */
package com.xfinity.ceylon_steel.model;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class Outlet {

	private int outletId;
	private String outletName;

	public Outlet() {
	}

	public Outlet(int outletId, String outletName) {
		this.outletId = outletId;
		this.outletName = outletName;
	}

	/**
	 * @return the outletId
	 */
	public int getOutletId() {
		return outletId;
	}

	/**
	 * @return the outletName
	 */
	public String getOutletName() {
		return outletName;
	}

	/**
	 * @param outletId the outletId to set
	 */
	public void setOutletId(int outletId) {
		this.outletId = outletId;
	}

	/**
	 * @param outletName the outletName to set
	 */
	public void setOutletName(String outletName) {
		this.outletName = outletName;
	}

	@Override
	public String toString() {
		return outletName;
	}

}
