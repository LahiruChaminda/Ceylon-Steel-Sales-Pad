/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2014, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Apr 19, 2014, 8:57:24 PM
 */
package com.xfinity.ceylon_steel.controller;

/**
 * @author Supun Lakshan
 */
abstract class WebServiceURL {

	private static final String webServiceURL = "http://gateway.ceylonlinux.com/Ceylon_Steel/native/";

	protected WebServiceURL() {
	}

	protected static final class CategoryURL {

		public static final String getItemsAndCategories = webServiceURL + "getItemsAndCategories";
	}

	protected static final class CustomerURL {

		public static final String getCustomersOfUser = webServiceURL + "getCustomersOfUser";
	}

	protected static final class DriverURL {

		public static final String getDriversOfUser = webServiceURL + "getDriversOfUser";
	}

	protected static final class OrderURL {

		public static final String placeSalesOrder = webServiceURL + "placeSalesOrder";
	}

	protected static final class OutletURL {

		public static final String getOutletsOfUser = webServiceURL + "getOutletsOfUser";
		public static final String syncPayments = webServiceURL + "syncPayments";
	}

	protected static final class UnProductiveCallURL {

		public static final String recordUnProductiveCall = webServiceURL + "recordUnProductiveCall";
	}

	protected static final class UserURL {

		public static final String getUserDetails = webServiceURL + "getUserDetails";
		public static final String checkInCheckOut = webServiceURL + "checkInCheckOut";
		public static final String getDistributorsOfUser = webServiceURL + "getDistributorsOfUser";
		public static final String markRepLocations = webServiceURL + "markRepLocations";
		public static final String getAttendanceHistory = webServiceURL + "getAttendanceHistory";
	}

	protected static final class VehicleURL {

		public static final String getVehiclesOfUser = webServiceURL + "getVehiclesOfUser";
	}
}
