/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 9, 2014, 5:20:10 PM
 */
package com.xfinity.ceylon_steel.model;

import android.content.Context;
import com.xfinity.ceylon_steel.controller.OutletController;

import java.util.ArrayList;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class Outlet {

	private int outletId;
	private String outletName;
	private ArrayList<Invoice> pendingInvoices;

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
	 * @param outletId the outletId to set
	 */
	public void setOutletId(int outletId) {
		this.outletId = outletId;
	}

	/**
	 * @return the outletName
	 */
	public String getOutletName() {
		return outletName;
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

	public ArrayList<Invoice> getPendingInvoices(Context context) {
		if (pendingInvoices == null) {
			this.pendingInvoices = OutletController.getPendingInvoices(this.outletId, context);
		}
		return this.pendingInvoices;
	}
}
