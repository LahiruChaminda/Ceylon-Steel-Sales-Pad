/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 9, 2014, 5:15:09 PM
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
public class OrderDetail implements Serializable {

	private int itemId;
	private double quantity;
	private double unitPrice;
	private double eachDiscount;
	private String itemDescription;

	public OrderDetail() {
	}

	public OrderDetail(int itemId, double quantity, double unitPrice, double eachDiscount) {
		this.itemId = itemId;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.eachDiscount = eachDiscount;
	}

	public OrderDetail(int itemId, double quantity, double unitPrice, double eachDiscount, String itemDescription) {
		this.itemId = itemId;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.eachDiscount = eachDiscount;
		this.itemDescription = itemDescription;
	}

	/**
	 * @return the itemId
	 */
	public int getItemId() {
		return itemId;
	}

	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	/**
	 * @return the quantity
	 */
	public double getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the unitPrice
	 */
	public double getUnitPrice() {
		return unitPrice;
	}

	/**
	 * @param unitPrice the unitPrice to set
	 */
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	/**
	 * @return the eachDiscount
	 */
	public double getEachDiscount() {
		return eachDiscount;
	}

	/**
	 * @param eachDiscount the eachDiscount to set
	 */
	public void setEachDiscount(double eachDiscount) {
		this.eachDiscount = eachDiscount;
	}

	/**
	 * @return the itemDescription
	 */
	public String getItemDescription() {
		return itemDescription;
	}

	/**
	 * @param itemDescription the itemDescription to set
	 */
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	@Override
	public String toString() {
		return getItemDescription();
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + this.itemId;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final OrderDetail other = (OrderDetail) obj;
		return this.itemId == other.itemId;
	}

	public JSONObject getOrderDetailAsJSON() {
		HashMap<String, Object> orderDetailJson = new HashMap<String, Object>();
		orderDetailJson.put("itemId", itemId);
		orderDetailJson.put("quantity", quantity);
		orderDetailJson.put("unitPrice", unitPrice);
		orderDetailJson.put("eachDiscount", eachDiscount);
		return new JSONObject(orderDetailJson);
	}
}
