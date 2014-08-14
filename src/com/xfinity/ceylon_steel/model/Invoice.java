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
	private double invoiceAmount;
	private long salesOrderId;
	private ArrayList<Payment> payments;
	private ArrayList<Payment> unSyncedPayments;
	private ArrayList<Payment> pendingPayments;
	private String outletName;
	private String deliveryDate;

	public Invoice(String date, String distributorCode, double pendingAmount, long salesOrderId, ArrayList<Payment> payments, ArrayList<Payment> unSyncedPayments, ArrayList<Payment> pendingPayments, String outletName, String deliveryDate, double invoiceAmount) {
		this.date = date;
		this.distributorCode = distributorCode;
		this.pendingAmount = pendingAmount;
		this.salesOrderId = salesOrderId;
		this.payments = payments;
		this.unSyncedPayments = unSyncedPayments;
		this.pendingPayments = pendingPayments;
		this.outletName = outletName;
		this.deliveryDate = deliveryDate;
		this.invoiceAmount = invoiceAmount;
	}

	public double getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
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

	public ArrayList<Payment> getUnSyncedPayments() {
		return unSyncedPayments;
	}

	public void setUnSyncedPayments(ArrayList<Payment> unSyncedPayments) {
		this.unSyncedPayments = unSyncedPayments;
	}

	public ArrayList<Payment> getPendingPayments() {
		return pendingPayments;
	}

	public void setPendingPayments(ArrayList<Payment> pendingPayments) {
		this.pendingPayments = pendingPayments;
	}

	public String getOutletName() {
		return outletName;
	}

	public void setOutletName(String outletName) {
		this.outletName = outletName;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
}
