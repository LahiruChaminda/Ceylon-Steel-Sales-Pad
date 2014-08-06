/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 9, 2014, 5:13:09 PM
 */
package com.xfinity.ceylon_steel.controller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import com.xfinity.ceylon_steel.activity.HomeActivity;
import com.xfinity.ceylon_steel.db.SQLiteDatabaseHelper;
import com.xfinity.ceylon_steel.model.Order;
import com.xfinity.ceylon_steel.model.OrderDetail;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.xfinity.ceylon_steel.controller.WebServiceURL.OrderURL.placeSalesOrder;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class OrderController extends AbstractController {

	public static long placeConsignmentOrder(Context context, Order order) {
		SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
		SQLiteDatabase writableDatabase = databaseInstance.getWritableDatabase();
		SQLiteStatement orderStatement = writableDatabase.compileStatement("insert into tbl_order(distributorId,outletId,orderDate,deliveryDate,batteryLevel,longitude,latitude,type,remarks,driverName,driverNIC,vehicleNo,total) values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
		SQLiteStatement orderDetailStatement = writableDatabase.compileStatement("insert into tbl_order_detail(orderId,itemId,price,quantity,discount) values (?,?,?,?,?)");
		long orderId = -1;
		try {
			writableDatabase.beginTransaction();
			String orderParamaters[] = {
				Integer.toString(order.getDistributorId()),
				Integer.toString(order.getOutletId()),
				Long.toString(order.getOrderMadeTimeStamp()),
				Long.toString(order.getDeliveryDate()),
				Integer.toString(order.getBatteryLevel()),
				Double.toString(order.getLongitude()),
				Double.toString(order.getLatitude()),
				order.getOrderType(),
				order.getRemarks(),
				order.getDriver(),
				order.getDriverNIC(),
				order.getVehicle(),
				Double.toString(order.getTotal())
			};
			orderStatement.bindAllArgsAsStrings(orderParamaters);
			orderId = orderStatement.executeInsert();
			if (orderId == -1) {
				return -1;
			}
			for (OrderDetail orderDetail : order.getOrderDetails()) {
				String orderDetailParamaters[] = {
					Long.toString(orderId),
					Integer.toString(orderDetail.getItemId()),
					Double.toString(orderDetail.getUnitPrice()),
					Double.toString(orderDetail.getQuantity()),
					Double.toString(orderDetail.getEachDiscount())
				};
				orderDetailStatement.bindAllArgsAsStrings(orderDetailParamaters);
				long orderDetailId = orderDetailStatement.executeInsert();
				if (orderDetailId == -1) {
					return -1;
				}
			}
			writableDatabase.setTransactionSuccessful();
		} finally {
			writableDatabase.endTransaction();
			databaseInstance.close();
		}
		return orderId;
	}

	public static long placeDirectOrder(Context context, Order order) {
		SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
		SQLiteDatabase writableDatabase = databaseInstance.getWritableDatabase();
		SQLiteStatement orderStatement = writableDatabase.compileStatement("insert into tbl_order(outletId,orderDate,deliveryDate,batteryLevel,longitude,latitude,type,remarks,driverName,driverNIC,vehicleNo,total) values (?,?,?,?,?,?,?,?,?,?,?,?)");
		SQLiteStatement orderDetailStatement = writableDatabase.compileStatement("insert into tbl_order_detail(orderId,itemId,price,quantity,discount) values (?,?,?,?,?)");
		long orderId = -1;
		try {
			writableDatabase.beginTransaction();
			String orderParamaters[] = {
				Integer.toString(order.getOutletId()),
				Long.toString(order.getOrderMadeTimeStamp()),
				Long.toString(order.getDeliveryDate()),
				Integer.toString(order.getBatteryLevel()),
				Double.toString(order.getLongitude()),
				Double.toString(order.getLatitude()),
				order.getOrderType(),
				order.getRemarks(),
				order.getDriver(),
				order.getDriverNIC(),
				order.getVehicle(),
				Double.toString(order.getTotal())
			};
			orderStatement.bindAllArgsAsStrings(orderParamaters);
			orderId = orderStatement.executeInsert();
			if (orderId == -1) {
				return -1;
			}
			for (OrderDetail orderDetail : order.getOrderDetails()) {
				String orderDetailParamaters[] = {
					Long.toString(orderId),
					Integer.toString(orderDetail.getItemId()),
					Double.toString(orderDetail.getUnitPrice()),
					Double.toString(orderDetail.getQuantity()),
					Double.toString(orderDetail.getEachDiscount())
				};
				orderDetailStatement.bindAllArgsAsStrings(orderDetailParamaters);
				long orderDetailId = orderDetailStatement.executeInsert();
				if (orderDetailId == -1) {
					return -1;
				}
			}
			writableDatabase.setTransactionSuccessful();
		} finally {
			writableDatabase.endTransaction();
			databaseInstance.close();
		}
		return orderId;
	}

	public static long placeProjectOrder(Context context, Order order) {
		SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
		SQLiteDatabase writableDatabase = databaseInstance.getWritableDatabase();
		String sql;
		if (order.getOrderType().equalsIgnoreCase(Order.PROJECT)) {
			sql = "insert into tbl_order(distributorId,customerId,orderDate,deliveryDate,batteryLevel,longitude,latitude,type,remarks,driverName,driverNIC,vehicleNo,total) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		} else {
			sql = "insert into tbl_order(customerId,orderDate,deliveryDate,batteryLevel,longitude,latitude,type,remarks,driverName,driverNIC,vehicleNo,total) values (?,?,?,?,?,?,?,?,?,?,?,?)";
		}
		SQLiteStatement orderStatement = writableDatabase.compileStatement(sql);
		SQLiteStatement orderDetailStatement = writableDatabase.compileStatement("insert into tbl_order_detail(orderId,itemId,price,quantity,discount) values (?,?,?,?,?)");
		long orderId = -1;
		try {
			writableDatabase.beginTransaction();
			String[] orderParamaters;
			if (order.getOrderType().equalsIgnoreCase(Order.PROJECT)) {
				orderParamaters = new String[]{
					Integer.toString(order.getDistributorId()),
					Integer.toString(order.getCustomerId()),
					Long.toString(order.getOrderMadeTimeStamp()),
					Long.toString(order.getDeliveryDate()),
					Integer.toString(order.getBatteryLevel()),
					Double.toString(order.getLongitude()),
					Double.toString(order.getLatitude()),
					order.getOrderType(),
					order.getRemarks(),
					order.getDriver(),
					order.getDriverNIC(),
					order.getVehicle(),
					Double.toString(order.getTotal())
				};
			} else {
				orderParamaters = new String[]{
					Integer.toString(order.getCustomerId()),
					Long.toString(order.getOrderMadeTimeStamp()),
					Long.toString(order.getDeliveryDate()),
					Integer.toString(order.getBatteryLevel()),
					Double.toString(order.getLongitude()),
					Double.toString(order.getLatitude()),
					order.getOrderType(),
					order.getRemarks(),
					order.getDriver(),
					order.getDriverNIC(),
					order.getVehicle(),
					Double.toString(order.getTotal())
				};
			}
			for (String a : orderParamaters) {
				System.out.println(a);
			}
			orderStatement.bindAllArgsAsStrings(orderParamaters);
			orderId = orderStatement.executeInsert();
			if (orderId == -1) {
				return -1;
			}
			for (OrderDetail orderDetail : order.getOrderDetails()) {
				String orderDetailParamaters[] = {
					Long.toString(orderId),
					Integer.toString(orderDetail.getItemId()),
					Double.toString(orderDetail.getUnitPrice()),
					Double.toString(orderDetail.getQuantity()),
					Double.toString(orderDetail.getEachDiscount())
				};
				orderDetailStatement.bindAllArgsAsStrings(orderDetailParamaters);
				long orderDetailId = orderDetailStatement.executeInsert();
				if (orderDetailId == -1) {
					return -1;
				}
			}
			writableDatabase.setTransactionSuccessful();
		} finally {
			writableDatabase.endTransaction();
			databaseInstance.close();
		}
		return orderId;
	}

	public static ArrayList<Order> getConsignmentOrders(Context context) {
		ArrayList<Order> orders = new ArrayList<Order>();
		SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
		SQLiteDatabase database = databaseInstance.getWritableDatabase();
		int repId = UserController.getAuthorizedUser(context).getUserId();
		Cursor orderCursor = database.rawQuery("select orderId, tbl_order.distributorId, tbl_distributor.distributorName, tbl_order.outletId, orderDate, deliveryDate, driverName, driverNIC, vehicleNo, total, batteryLevel, longitude, latitude, type, remarks, outletName, syncStatus from tbl_order, tbl_outlet, tbl_distributor where tbl_distributor.distributorId=tbl_order.distributorId and tbl_order.outletId=tbl_outlet.outletId and type='CONSIGNMENT'", null);
		int orderIdIndex = orderCursor.getColumnIndex("orderId");
		int distributorIdIndex = orderCursor.getColumnIndex("distributorId");
		int distributorNameIndex = orderCursor.getColumnIndex("distributorName");
		int outletIdIndex = orderCursor.getColumnIndex("outletId");
		int orderDateIndex = orderCursor.getColumnIndex("orderDate");
		int deliveryDateIndex = orderCursor.getColumnIndex("deliveryDate");
		int driverNameIndex = orderCursor.getColumnIndex("driverName");
		int driverNICIndex = orderCursor.getColumnIndex("driverNIC");
		int vehicleNoIndex = orderCursor.getColumnIndex("vehicleNo");
		int totalIndex = orderCursor.getColumnIndex("total");
		int batteryLevelIndex = orderCursor.getColumnIndex("batteryLevel");
		int longitudeIndex = orderCursor.getColumnIndex("longitude");
		int latitudeIndex = orderCursor.getColumnIndex("latitude");
		int remarksIndex = orderCursor.getColumnIndex("remarks");
		int outletNameIndex = orderCursor.getColumnIndex("outletName");
		int syncStatusIndex = orderCursor.getColumnIndex("syncStatus");
		for (orderCursor.moveToFirst(); !orderCursor.isAfterLast(); orderCursor.moveToNext()) {
			long orderId = orderCursor.getLong(orderIdIndex);
			Order order = new Order();
			order.setOrderId(orderId);
			order.setRepId(repId);
			order.setDistributorId(orderCursor.getInt(distributorIdIndex));
			order.setDistributorName(orderCursor.getString(distributorNameIndex));
			order.setOutletId(orderCursor.getInt(outletIdIndex));
			order.setOutletName(orderCursor.getString(outletNameIndex));
			order.setDriver(orderCursor.getString(driverNameIndex));
			order.setDriverNIC(orderCursor.getString(driverNICIndex));
			order.setVehicle(orderCursor.getString(vehicleNoIndex));
			order.setOrderMadeTimeStamp(orderCursor.getLong(orderDateIndex));
			order.setDeliveryDate(orderCursor.getLong(deliveryDateIndex));
			order.setLongitude(orderCursor.getLong(longitudeIndex));
			order.setLatitude(orderCursor.getLong(latitudeIndex));
			order.setBatteryLevel(orderCursor.getInt(batteryLevelIndex));
			order.setOrderType(Order.CONSIGNMENT);
			order.setRemarks(orderCursor.getString(remarksIndex));
			order.setOrderDetails(getOrderDetails(database, orderId));
			order.setTotal(orderCursor.getDouble(totalIndex));
			order.setSyncStatus(orderCursor.getInt(syncStatusIndex) == 1);
			orders.add(order);
		}
		orderCursor.close();
		databaseInstance.close();
		return orders;
	}

	public static ArrayList<Order> getDirectOrders(Context context) {
		ArrayList<Order> orders = new ArrayList<Order>();
		SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
		SQLiteDatabase database = databaseInstance.getWritableDatabase();
		int repId = UserController.getAuthorizedUser(context).getUserId();
		Cursor orderCursor = database.rawQuery("select orderId, tbl_order.outletId, orderDate, deliveryDate, driverName, driverNIC, vehicleNo, total, batteryLevel, longitude, latitude, type, remarks, outletName, syncStatus from tbl_order, tbl_outlet where tbl_order.outletId=tbl_outlet.outletId and type='DIRECT'", null);
		int orderIdIndex = orderCursor.getColumnIndex("orderId");
		int outletIdIndex = orderCursor.getColumnIndex("outletId");
		int orderDateIndex = orderCursor.getColumnIndex("orderDate");
		int deliveryDateIndex = orderCursor.getColumnIndex("deliveryDate");
		int driverNameIndex = orderCursor.getColumnIndex("driverName");
		int driverNICIndex = orderCursor.getColumnIndex("driverNIC");
		int vehicleNoIndex = orderCursor.getColumnIndex("vehicleNo");
		int totalIndex = orderCursor.getColumnIndex("total");
		int batteryLevelIndex = orderCursor.getColumnIndex("batteryLevel");
		int longitudeIndex = orderCursor.getColumnIndex("longitude");
		int latitudeIndex = orderCursor.getColumnIndex("latitude");
		int remarksIndex = orderCursor.getColumnIndex("remarks");
		int outletNameIndex = orderCursor.getColumnIndex("outletName");
		int syncStatusIndex = orderCursor.getColumnIndex("syncStatus");
		for (orderCursor.moveToFirst(); !orderCursor.isAfterLast(); orderCursor.moveToNext()) {
			long orderId = orderCursor.getLong(orderIdIndex);
			Order order = new Order();
			order.setOrderId(orderId);
			order.setRepId(repId);
			order.setOutletId(orderCursor.getInt(outletIdIndex));
			order.setOutletName(orderCursor.getString(outletNameIndex));
			order.setDriver(orderCursor.getString(driverNameIndex));
			order.setDriverNIC(orderCursor.getString(driverNICIndex));
			order.setVehicle(orderCursor.getString(vehicleNoIndex));
			order.setOrderMadeTimeStamp(orderCursor.getLong(orderDateIndex));
			order.setDeliveryDate(orderCursor.getLong(deliveryDateIndex));
			order.setLongitude(orderCursor.getLong(longitudeIndex));
			order.setLatitude(orderCursor.getLong(latitudeIndex));
			order.setBatteryLevel(orderCursor.getInt(batteryLevelIndex));
			order.setOrderType(Order.DIRECT);
			order.setRemarks(orderCursor.getString(remarksIndex));
			order.setOrderDetails(getOrderDetails(database, orderId));
			order.setTotal(orderCursor.getDouble(totalIndex));
			order.setSyncStatus(orderCursor.getInt(syncStatusIndex) == 1);
			orders.add(order);
		}
		orderCursor.close();
		databaseInstance.close();
		return orders;
	}

	public static ArrayList<Order> getProjectOrders(Context context) {
		ArrayList<Order> orders = new ArrayList<Order>();
		SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
		SQLiteDatabase database = databaseInstance.getWritableDatabase();
		int repId = UserController.getAuthorizedUser(context).getUserId();
		Cursor orderCursor = database.rawQuery("select orderId, tbl_order.distributorId, tbl_distributor.distributorName, tbl_order.customerId, customerName, orderDate, deliveryDate, driverName, driverNIC, vehicleNo, total, batteryLevel, longitude, latitude, type, remarks, syncStatus from tbl_order, tbl_customer, tbl_distributor where tbl_order.distributorId-tbl_distributor.distributorId and tbl_order.customerId=tbl_customer.customerId and type='PROJECT'", null);
		int orderIdIndex = orderCursor.getColumnIndex("orderId");
		int distributorIdIndex = orderCursor.getColumnIndex("distributorId");
		int distributorNameIndex = orderCursor.getColumnIndex("distributorName");
		int customerIdIndex = orderCursor.getColumnIndex("customerId");
		int orderDateIndex = orderCursor.getColumnIndex("orderDate");
		int deliveryDateIndex = orderCursor.getColumnIndex("deliveryDate");
		int driverNameIndex = orderCursor.getColumnIndex("driverName");
		int driverNICIndex = orderCursor.getColumnIndex("driverNIC");
		int vehicleNoIndex = orderCursor.getColumnIndex("vehicleNo");
		int totalIndex = orderCursor.getColumnIndex("total");
		int batteryLevelIndex = orderCursor.getColumnIndex("batteryLevel");
		int longitudeIndex = orderCursor.getColumnIndex("longitude");
		int latitudeIndex = orderCursor.getColumnIndex("latitude");
		int remarksIndex = orderCursor.getColumnIndex("remarks");
		int customerNameIndex = orderCursor.getColumnIndex("customerName");
		int syncStatusIndex = orderCursor.getColumnIndex("syncStatus");
		for (orderCursor.moveToFirst(); !orderCursor.isAfterLast(); orderCursor.moveToNext()) {
			long orderId = orderCursor.getLong(orderIdIndex);
			Order order = new Order();
			order.setOrderId(orderId);
			order.setRepId(repId);
			order.setDistributorId(orderCursor.getInt(distributorIdIndex));
			order.setDistributorName(orderCursor.getString(distributorNameIndex));
			order.setCustomerId((orderCursor.getInt(customerIdIndex)));
			order.setCustomerName(orderCursor.getString(customerNameIndex));
			order.setDriver(orderCursor.getString(driverNameIndex));
			order.setDriverNIC(orderCursor.getString(driverNICIndex));
			order.setVehicle(orderCursor.getString(vehicleNoIndex));
			order.setOrderMadeTimeStamp(orderCursor.getLong(orderDateIndex));
			order.setDeliveryDate(orderCursor.getLong(deliveryDateIndex));
			order.setLongitude(orderCursor.getLong(longitudeIndex));
			order.setLatitude(orderCursor.getLong(latitudeIndex));
			order.setBatteryLevel(orderCursor.getInt(batteryLevelIndex));
			order.setOrderType(Order.PROJECT);
			order.setRemarks(orderCursor.getString(remarksIndex));
			order.setOrderDetails(getOrderDetails(database, orderId));
			order.setTotal(orderCursor.getDouble(totalIndex));
			order.setSyncStatus(orderCursor.getInt(syncStatusIndex) == 1);
			orders.add(order);
		}
		orderCursor.close();
		databaseInstance.close();
		return orders;
	}

	private static ArrayList<OrderDetail> getOrderDetails(SQLiteDatabase database, long orderId) {
		Cursor orderDetailCursor = database.rawQuery("select tbl_order_detail.itemId, quantity, tbl_order_detail.price as unitPrice, discount as eachDiscount, itemDescription from tbl_order_detail, tbl_item where tbl_item.itemId=tbl_order_detail.itemId and tbl_order_detail.orderId=?", new String[]{Long.toString(orderId)});
		ArrayList<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
		for (orderDetailCursor.moveToFirst(); !orderDetailCursor.isAfterLast(); orderDetailCursor.moveToNext()) {
			OrderDetail orderDetail = new OrderDetail(
				orderDetailCursor.getInt(0),
				orderDetailCursor.getDouble(1),
				orderDetailCursor.getDouble(2),
				orderDetailCursor.getDouble(3),
				orderDetailCursor.getString(4));
			orderDetails.add(orderDetail);
		}
		orderDetailCursor.close();
		return orderDetails;
	}

	public static void syncOrder(final Context context, final Order order) {
		new AsyncTask<Order, Void, Boolean>() {
			private ProgressDialog progressDialog;

			@Override
			protected void onPreExecute() {
				progressDialog = new ProgressDialog(context);
				progressDialog.setMessage("Syncking Order");
				progressDialog.show();
			}

			@Override
			protected Boolean doInBackground(Order... orders) {
				try {
					Order order = orders[0];
					HashMap<String, Object> parameters = new HashMap<String, Object>();
					parameters.put("data", order.getOrderAsJSON());
					JSONObject responseJson = getJsonObject(placeSalesOrder, parameters, context);
					return responseJson.getBoolean("response");
				} catch (IOException ex) {
					Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
				} catch (JSONException ex) {
					Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
				}
				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle(com.xfinity.ceylon_steel.R.string.message_title);
				if (result) {
					SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
					SQLiteDatabase database = databaseInstance.getWritableDatabase();
					SQLiteStatement orderSyncStatusChangeSql = database.compileStatement("update tbl_order set syncStatus=1 where orderId=?");
					String[] parameter = new String[]{Long.toString(order.getOrderId())};
					orderSyncStatusChangeSql.bindAllArgsAsStrings(parameter);
					orderSyncStatusChangeSql.executeUpdateDelete();
					databaseInstance.close();
					builder.setMessage("Sales Order synced successfully");
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							Intent homeActivity = new Intent(context, HomeActivity.class);
							context.startActivity(homeActivity);
						}
					});
				} else {
					builder.setMessage("Unable to sync order");
					builder.setPositiveButton("OK", null);
				}
				builder.show();
			}

		}.execute(order);
	}

	public static long updateConsignmentOrder(Context context, Order order) {
		SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
		SQLiteDatabase writableDatabase = databaseInstance.getWritableDatabase();
		SQLiteStatement orderDeleteStatement = writableDatabase.compileStatement("delete from tbl_order where orderId=?");
		SQLiteStatement orderStatement = writableDatabase.compileStatement("insert into tbl_order(distributorId,outletId,orderDate,deliveryDate,batteryLevel,longitude,latitude,type,remarks,driverName,driverNIC,vehicleNo,total) values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
		SQLiteStatement orderDetailStatement = writableDatabase.compileStatement("insert into tbl_order_detail(orderId,itemId,price,quantity,discount) values (?,?,?,?,?)");
		long orderId = -1;
		try {
			writableDatabase.beginTransaction();
			orderDeleteStatement.bindAllArgsAsStrings(new String[]{
				Long.toString(order.getOrderId())
			});
			orderDeleteStatement.executeUpdateDelete();
			String orderParamaters[] = {
				Integer.toString(order.getDistributorId()),
				Integer.toString(order.getOutletId()),
				Long.toString(order.getOrderMadeTimeStamp()),
				Long.toString(order.getDeliveryDate()),
				Integer.toString(order.getBatteryLevel()),
				Double.toString(order.getLongitude()),
				Double.toString(order.getLatitude()),
				order.getOrderType(),
				order.getRemarks(),
				order.getDriver(),
				order.getDriverNIC(),
				order.getVehicle(),
				Double.toString(order.getTotal())
			};
			orderStatement.bindAllArgsAsStrings(orderParamaters);
			orderId = orderStatement.executeInsert();
			if (orderId == -1) {
				return -1;
			}
			for (OrderDetail orderDetail : order.getOrderDetails()) {
				String orderDetailParamaters[] = {
					Long.toString(orderId),
					Integer.toString(orderDetail.getItemId()),
					Double.toString(orderDetail.getUnitPrice()),
					Double.toString(orderDetail.getQuantity()),
					Double.toString(orderDetail.getEachDiscount())
				};
				orderDetailStatement.bindAllArgsAsStrings(orderDetailParamaters);
				long orderDetailId = orderDetailStatement.executeInsert();
				if (orderDetailId == -1) {
					return -1;
				}
			}
			writableDatabase.setTransactionSuccessful();
		} finally {
			writableDatabase.endTransaction();
			databaseInstance.close();
		}
		return orderId;
	}

	public static long updateDirectOrder(Context context, Order order) {
		SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
		SQLiteDatabase writableDatabase = databaseInstance.getWritableDatabase();
		SQLiteStatement orderDeleteStatement = writableDatabase.compileStatement("delete from tbl_order where orderId=?");
		SQLiteStatement orderStatement = writableDatabase.compileStatement("insert into tbl_order(outletId,orderDate,deliveryDate,batteryLevel,longitude,latitude,type,remarks,driverName,driverNIC,vehicleNo,total) values (?,?,?,?,?,?,?,?,?,?,?,?)");
		SQLiteStatement orderDetailStatement = writableDatabase.compileStatement("insert into tbl_order_detail(orderId,itemId,price,quantity,discount) values (?,?,?,?,?)");
		long orderId = -1;
		try {
			writableDatabase.beginTransaction();
			orderDeleteStatement.bindAllArgsAsStrings(new String[]{
				Long.toString(order.getOrderId())
			});
			orderDeleteStatement.executeUpdateDelete();
			String orderParamaters[] = {
				Integer.toString(order.getOutletId()),
				Long.toString(order.getOrderMadeTimeStamp()),
				Long.toString(order.getDeliveryDate()),
				Integer.toString(order.getBatteryLevel()),
				Double.toString(order.getLongitude()),
				Double.toString(order.getLatitude()),
				order.getOrderType(),
				order.getRemarks(),
				order.getDriver(),
				order.getDriverNIC(),
				order.getVehicle(),
				Double.toString(order.getTotal())
			};
			orderStatement.bindAllArgsAsStrings(orderParamaters);
			orderId = orderStatement.executeInsert();
			if (orderId == -1) {
				return -1;
			}
			for (OrderDetail orderDetail : order.getOrderDetails()) {
				String orderDetailParamaters[] = {
					Long.toString(orderId),
					Integer.toString(orderDetail.getItemId()),
					Double.toString(orderDetail.getUnitPrice()),
					Double.toString(orderDetail.getQuantity()),
					Double.toString(orderDetail.getEachDiscount())
				};
				orderDetailStatement.bindAllArgsAsStrings(orderDetailParamaters);
				long orderDetailId = orderDetailStatement.executeInsert();
				if (orderDetailId == -1) {
					return -1;
				}
			}
			writableDatabase.setTransactionSuccessful();
		} finally {
			writableDatabase.endTransaction();
			databaseInstance.close();
		}
		return orderId;
	}

	public static long updateProjectOrder(Context context, Order order) {
		SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
		SQLiteDatabase writableDatabase = databaseInstance.getWritableDatabase();
		SQLiteStatement orderDeleteStatement = writableDatabase.compileStatement("delete from tbl_order where orderId=?");
		SQLiteStatement orderStatement = writableDatabase.compileStatement("insert into tbl_order(distributorId,customerId,orderDate,deliveryDate,batteryLevel,longitude,latitude,type,remarks,driverName,driverNIC,vehicleNo) values (?,?,?,?,?,?,?,?,?,?,?,?)");
		SQLiteStatement orderDetailStatement = writableDatabase.compileStatement("insert into tbl_order_detail(orderId,itemId,price,quantity,discount) values (?,?,?,?,?)");
		long orderId = -1;
		try {
			writableDatabase.beginTransaction();
			orderDeleteStatement.bindAllArgsAsStrings(new String[]{
				Long.toString(order.getOrderId())
			});
			orderDeleteStatement.executeUpdateDelete();
			String orderParamaters[] = {
				Integer.toString(order.getDistributorId()),
				Integer.toString(order.getCustomerId()),
				Long.toString(order.getOrderMadeTimeStamp()),
				Long.toString(order.getDeliveryDate()),
				Integer.toString(order.getBatteryLevel()),
				Double.toString(order.getLongitude()),
				Double.toString(order.getLatitude()),
				order.getOrderType(),
				order.getRemarks(),
				order.getDriver(),
				order.getDriverNIC(),
				order.getVehicle(),
				Double.toString(order.getTotal())
			};
			orderStatement.bindAllArgsAsStrings(orderParamaters);
			orderId = orderStatement.executeInsert();
			if (orderId == -1) {
				return -1;
			}
			for (OrderDetail orderDetail : order.getOrderDetails()) {
				String orderDetailParamaters[] = {
					Long.toString(orderId),
					Integer.toString(orderDetail.getItemId()),
					Double.toString(orderDetail.getUnitPrice()),
					Double.toString(orderDetail.getQuantity()),
					Double.toString(orderDetail.getEachDiscount())
				};
				orderDetailStatement.bindAllArgsAsStrings(orderDetailParamaters);
				long orderDetailId = orderDetailStatement.executeInsert();
				if (orderDetailId == -1) {
					return -1;
				}
			}
			writableDatabase.setTransactionSuccessful();
		} finally {
			writableDatabase.endTransaction();
			databaseInstance.close();
		}
		return orderId;
	}

}
