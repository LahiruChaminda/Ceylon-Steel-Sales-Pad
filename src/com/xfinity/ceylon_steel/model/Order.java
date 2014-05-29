/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 9, 2014, 5:14:56 PM
 */
package com.xfinity.ceylon_steel.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class Order implements Serializable {

	private long orderId;
	private int repId;
	private int distributorId;
	private String distributorName;
	private int customerId;
	private String customerName;
	private int outletId;
	private String outletName;
	private String driver;
	private String driverNIC;
	private String vehicle;
	private long orderMadeTimeStamp;
	private long deliveryDate;
	private String orderType;
	private int batteryLevel;
	private double longitude;
	private double latitude;
	private String remarks;
	private ArrayList<OrderDetail> orderDetails;
	private double total;
	public static final String CONSIGNMENT = "CONSIGNMENT";
	public static final String DIRECT = "DIRECT";
	public static final String DIRECT_PROJECT = "DIRECT_PROJECT";
	public static final String PROJECT = "PROJECT";

	public Order() {
	}

	public Order(int repId, int distributorId, int customerId, int outletId, String driver, String driverNIC, String vehicle, long orderMadeTimeStamp, long deliveryDate, String orderType, int batteryLevel, double longitude, double latitude, String remarks, ArrayList<OrderDetail> orderDetails) {
		this.repId = repId;
		this.distributorId = distributorId;
		this.customerId = customerId;
		this.outletId = outletId;
		this.driver = driver;
		this.driverNIC = driverNIC;
		this.vehicle = vehicle;
		this.orderMadeTimeStamp = orderMadeTimeStamp;
		this.deliveryDate = deliveryDate;
		this.orderType = orderType;
		this.batteryLevel = batteryLevel;
		this.longitude = longitude;
		this.latitude = latitude;
		this.remarks = remarks;
		this.orderDetails = orderDetails;
	}

	/**
	 * @return the orderId
	 */
	public long getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(long orderId) {
		this.orderId = orderId;
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

	/**
	 * @return the distributorId
	 */
	public int getDistributorId() {
		return distributorId;
	}

	/**
	 * @param distributorId the distributorId to set
	 */
	public void setDistributorId(int distributorId) {
		this.distributorId = distributorId;
	}

	/**
	 * @return the customerId
	 */
	public int getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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

	/**
	 * @return the driver
	 */
	public String getDriver() {
		return driver;
	}

	/**
	 * @param driver the driver to set
	 */
	public void setDriver(String driver) {
		this.driver = driver;
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

	/**
	 * @return the vehicle
	 */
	public String getVehicle() {
		return vehicle;
	}

	/**
	 * @param vehicle the vehicle to set
	 */
	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}

	/**
	 * @return the orderMadeTimeStamp
	 */
	public long getOrderMadeTimeStamp() {
		return orderMadeTimeStamp;
	}

	/**
	 * @param orderMadeTimeStamp the orderMadeTimeStamp to set
	 */
	public void setOrderMadeTimeStamp(long orderMadeTimeStamp) {
		this.orderMadeTimeStamp = orderMadeTimeStamp;
	}

	/**
	 * @return the delivetyDate
	 */
	public long getDeliveryDate() {
		return deliveryDate;
	}

	/**
	 * @param delivetyDate the delivetyDate to set
	 */
	public void setDeliveryDate(long deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	/**
	 * @return the orderType
	 */
	public String getOrderType() {
		return orderType;
	}

	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	/**
	 * @return the batteryLevel
	 */
	public int getBatteryLevel() {
		return batteryLevel;
	}

	/**
	 * @param batteryLevel the batteryLevel to set
	 */
	public void setBatteryLevel(int batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * @return the orderDetails
	 */
	public ArrayList<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	/**
	 * @param orderDetails the orderDetails to set
	 */
	public void setOrderDetails(ArrayList<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	/**
	 * @return the total
	 */
	public double getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(double total) {
		this.total = total;
	}

	/**
	 * @return the distributorName
	 */
	public String getDistributorName() {
		return distributorName;
	}

	/**
	 * @param distributorName the distributorName to set
	 */
	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}

	public JSONObject getOrderAsJSON() {
		HashMap<String, Object> orderJson = new HashMap<String, Object>();
		orderJson.put("repId", repId);
		orderJson.put("distributorId", distributorId);
		orderJson.put("customerId", customerId);
		orderJson.put("outletId", outletId);
		orderJson.put("driver", driver);
		orderJson.put("driverNIC", driverNIC);
		orderJson.put("vehicle", vehicle);
		orderJson.put("orderDate", new Timestamp(orderMadeTimeStamp).toString());
		orderJson.put("deliveryDate", new Timestamp(deliveryDate).toString());
		orderJson.put("orderType", orderType);
		orderJson.put("batteryLevel", batteryLevel);
		orderJson.put("longitude", longitude);
		orderJson.put("latitude", latitude);
		orderJson.put("total", total);
		orderJson.put("remarks", remarks);
		JSONArray orderDetailsJsonArray = new JSONArray();
		for (OrderDetail orderDetail : orderDetails) {
			orderDetailsJsonArray.put(orderDetail.getOrderDetailAsJSON());
		}
		orderJson.put("items", orderDetailsJsonArray);
		return new JSONObject(orderJson);
	}

	@Override
	public String toString() {
		if (orderType.equals(CONSIGNMENT) || orderType.equals(DIRECT)) {
			return outletName;
		} else {
			return customerName;
		}
	}
}
