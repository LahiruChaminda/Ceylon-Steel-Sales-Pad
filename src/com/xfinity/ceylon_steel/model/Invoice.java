/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2014, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Jun 19, 2014, 8:24:57 PM
 */
package com.xfinity.ceylon_steel.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class Invoice implements Serializable {
	private String date;
	private String distributorCode;
	private double pendingAmount;
	private long salesOrderId;
	private ArrayList<Payment> payments;
	private ArrayList<Payment> newPayments;

	public Invoice(String date, String distributorCode, double pendingAmount, long salesOrderId, ArrayList<Payment> payments) {
		this.date = date;
		this.distributorCode = distributorCode;
		this.pendingAmount = pendingAmount;
		this.salesOrderId = salesOrderId;
		this.payments = payments;
	}

	public Invoice(String date, String distributorCode, double pendingAmount, long salesOrderId, ArrayList<Payment> payments, ArrayList<Payment> newPayments) {
		this.date = date;
		this.distributorCode = distributorCode;
		this.pendingAmount = pendingAmount;
		this.salesOrderId = salesOrderId;
		this.payments = payments;
		this.newPayments = newPayments;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDistributorCode() {
		return distributorCode;
	}

	public void setDistributorCode(String distributorCode) {
		this.distributorCode = distributorCode;
	}

	public double getPendingAmount() {
		return pendingAmount;
	}

	public void setPendingAmount(double pendingAmount) {
		this.pendingAmount = pendingAmount;
	}

	public ArrayList<Payment> getPayments() {
		return payments;
	}

	public void setPayments(ArrayList<Payment> payments) {
		this.payments = payments;
	}

	public long getSalesOrderId() {
		return salesOrderId;
	}

	public void setSalesOrderId(long salesOrderId) {
		this.salesOrderId = salesOrderId;
	}

	public ArrayList<Payment> getNewPayments() {
		return newPayments;
	}

	public void setNewPayments(ArrayList<Payment> newPayments) {
		this.newPayments = newPayments;
	}
}
