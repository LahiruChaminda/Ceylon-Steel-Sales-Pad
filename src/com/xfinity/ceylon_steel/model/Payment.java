/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2014, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Jun 19, 2014, 8:34:54 PM
 */
package com.xfinity.ceylon_steel.model;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class Payment implements Serializable {

	public static final String CASH_PAYMENT = "CASH";
	public static final String CHEQUE_PAYMENT = "CHEQUE";

	private long salesOrderId;
	private double paidValue;
	private String paidDate;
	private String paymentMethod;
	private String bank;
	private String chequeNo;
	private String realizationDate;
	private boolean synced;


	public Payment(long salesOrderId, double paidValue, String paidDate, boolean synced) {
		this.salesOrderId = salesOrderId;
		this.paidValue = paidValue;
		this.paidDate = paidDate;
		this.paymentMethod = CASH_PAYMENT;
		this.synced = synced;
	}

	public Payment(long salesOrderId, double paidValue, String paidDate, String bank, String chequeNo, String realizationDate, boolean synced) {
		this.salesOrderId = salesOrderId;
		this.paidValue = paidValue;
		this.paidDate = paidDate;
		this.paymentMethod = CHEQUE_PAYMENT;
		this.chequeNo = chequeNo;
		this.bank = bank;
		this.realizationDate = realizationDate;
		this.synced = synced;
	}

	public double getPaidValue() {
		return paidValue;
	}

	public void setPaidValue(double paidValue) {
		this.paidValue = paidValue;
	}

	public String getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(String paidDate) {
		this.paidDate = paidDate;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getChequeNo() {
		return chequeNo;
	}

	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	public long getSalesOrderId() {
		return salesOrderId;
	}

	public void setSalesOrderId(long salesOrderId) {
		this.salesOrderId = salesOrderId;
	}

	public String getRealizationDate() {
		return realizationDate;
	}

	public void setRealizationDate(String realizationDate) {
		this.realizationDate = realizationDate;
	}

	public boolean isSynced() {
		return synced;
	}

	public void setSynced(boolean synced) {
		this.synced = synced;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public JSONObject getPaymentAsJson() {
		HashMap<String, Object> jsonParams = new HashMap<String, Object>();
		jsonParams.put("salesOrderId", salesOrderId);
		jsonParams.put("paidValue", paidValue);
		jsonParams.put("paidDate", paidDate);
		jsonParams.put("paymentMethod", paymentMethod);
		jsonParams.put("bank", (bank == null) ? "" : bank);
		jsonParams.put("chequeNo", (chequeNo == null) ? "" : chequeNo);
		jsonParams.put("realizationDate", (realizationDate == null) ? "" : realizationDate);
		return new JSONObject(jsonParams);
	}
}
