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
	public static final byte ACCEPTED_PAYMENT = 0;
	public static final byte REJECTED_PAYMENT = 1;
	public static final byte PENDING_PAYMENT = 2;
	public static final byte AGED_PAYMENT = 3;
	public static final byte FRESH_PAYMENT = 4;

	private long salesOrderId;
	private double paidValue;
	private String paidDate;
	private String paymentMethod;
	private String bank;
	private String chequeNo;
	private String realizationDate;
	private byte status;


	public Payment(long salesOrderId, double paidValue, String paidDate, byte status) {
		this.salesOrderId = salesOrderId;
		this.paidValue = paidValue;
		this.paidDate = paidDate;
		this.paymentMethod = CASH_PAYMENT;
		this.status = status;
	}

	public Payment(long salesOrderId, double paidValue, String paidDate, String bank, String chequeNo, String realizationDate, byte status) {
		this.salesOrderId = salesOrderId;
		this.paidValue = paidValue;
		this.paidDate = paidDate;
		this.paymentMethod = CHEQUE_PAYMENT;
		this.chequeNo = chequeNo;
		this.bank = bank;
		this.realizationDate = realizationDate;
		this.status = status;
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

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
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
