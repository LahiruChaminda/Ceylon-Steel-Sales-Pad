/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 9, 2014, 6:04:58 PM
 */
package com.xfinity.ceylon_steel.controller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.widget.Toast;
import com.xfinity.ceylon_steel.R;
import com.xfinity.ceylon_steel.db.SQLiteDatabaseHelper;
import com.xfinity.ceylon_steel.model.Invoice;
import com.xfinity.ceylon_steel.model.Outlet;
import com.xfinity.ceylon_steel.model.Payment;
import com.xfinity.ceylon_steel.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.xfinity.ceylon_steel.controller.WebServiceURL.OutletURL.getOutletsOfUser;
import static com.xfinity.ceylon_steel.controller.WebServiceURL.OutletURL.syncPayments;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class OutletController extends AbstractController {

	public static ArrayList<Outlet> getOutlets(Context context) {
		return readFromDatabase(context, false);
	}

	public static ArrayList<Outlet> getOutletsWithInvoices(Context context) {
		return readFromDatabase(context, true);
	}

	private static ArrayList<Outlet> readFromDatabase(Context context, boolean requestInvoices) {
		SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
		SQLiteDatabase writableDatabase = databaseInstance.getWritableDatabase();
		Cursor cursor = writableDatabase.rawQuery("select distinct * from tbl_outlet", null);
		int outletIdIndex = cursor.getColumnIndex("outletId");
		int outletNameIndex = cursor.getColumnIndex("outletName");
		ArrayList<Outlet> outlets = new ArrayList<Outlet>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			int outletId = cursor.getInt(outletIdIndex);
			String outletName = cursor.getString(outletNameIndex);
			if (requestInvoices) {
				ArrayList<Invoice> invoices = new ArrayList<Invoice>();
				Cursor invoiceCursor = writableDatabase.rawQuery("select distinct salesOrderId, date, distributorCode, pendingAmount from tbl_invoice where outletId=?", new String[]{Integer.toString(outletId)});
				for (invoiceCursor.moveToFirst(); !invoiceCursor.isAfterLast(); invoiceCursor.moveToNext()) {
					ArrayList<Payment> payments = new ArrayList<Payment>();
					long salesOrderId = invoiceCursor.getLong(0);
					String date = invoiceCursor.getString(1);
					String distributorCode = invoiceCursor.getString(2);
					double pendingAmount = invoiceCursor.getDouble(3);
					Cursor paymentCursor = writableDatabase.rawQuery("select paidValue, paidDate, paymentMethod, chequeNo, status, bank from tbl_payment where salesOrderId=?", new String[]{Long.toString(salesOrderId)});
					for (paymentCursor.moveToFirst(); !paymentCursor.isAfterLast(); paymentCursor.moveToNext()) {
						double paidValue = paymentCursor.getDouble(0);
						String paidDate = paymentCursor.getString(1);
						String paymentMethod = paymentCursor.getString(2);
						String chequeNo = paymentCursor.getString(3);
						boolean status = paymentCursor.getInt(4) == 1;
						String bank = paymentCursor.getString(5);
						if (paymentMethod.equalsIgnoreCase(Payment.CASH_PAYMENT)) {
							payments.add(new Payment(salesOrderId, paidValue, paidDate, status));
						} else {
							payments.add(new Payment(salesOrderId, paidValue, paidDate, bank, chequeNo, status));
						}
					}
					paymentCursor.close();
					invoices.add(new Invoice(date, distributorCode, pendingAmount, salesOrderId, payments));
				}
				invoiceCursor.close();
				outlets.add(new Outlet(outletId, outletName, invoices));
			} else {
				outlets.add(new Outlet(outletId, outletName));
			}
		}
		cursor.close();
		databaseInstance.close();
		return outlets;
	}

	public static void downloadOutletsOfUser(final Context context) {
		new AsyncTask<User, Void, JSONArray>() {

			@Override
			protected void onPreExecute() {
				if (UserController.progressDialog == null) {
					UserController.progressDialog = new ProgressDialog(context);
					UserController.progressDialog.setMessage("Downloading Data");
					UserController.progressDialog.setCanceledOnTouchOutside(false);
				}
				if (!UserController.progressDialog.isShowing()) {
					UserController.progressDialog.show();
				}
			}

			protected JSONArray doInBackground(User... users) {
				try {
					HashMap<String, Object> parameters = new HashMap<String, Object>();
					parameters.put("userId", users[0].getUserId());
					return getJsonArray(getOutletsOfUser, parameters, context);
				} catch (IOException ex) {
					Logger.getLogger(OutletController.class.getName()).log(Level.SEVERE, null, ex);
				} catch (JSONException ex) {
					Logger.getLogger(OutletController.class.getName()).log(Level.SEVERE, null, ex);
				}
				return null;
			}

			@Override
			protected void onPostExecute(JSONArray result) {
				if (UserController.atomicInteger.decrementAndGet() == 0 && UserController.progressDialog != null && UserController.progressDialog.isShowing()) {
					UserController.progressDialog.dismiss();
					UserController.progressDialog = null;
				}
				if (result != null) {
					SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
					SQLiteDatabase writableDatabase = databaseInstance.getWritableDatabase();
					try {
						writableDatabase.beginTransaction();
						SQLiteStatement compiledStatement = writableDatabase.compileStatement("replace into tbl_outlet(outletId, outletName) values(?,?)");
						writableDatabase.compileStatement("delete from tbl_payment").executeUpdateDelete();
						writableDatabase.compileStatement("delete from tbl_invoice").executeUpdateDelete();
						SQLiteStatement invoiceStatement = writableDatabase.compileStatement("replace into tbl_invoice(salesOrderId, outletId, date, distributorCode, pendingAmount) values(?,?,?,?,?)");
						SQLiteStatement paymentStatement = writableDatabase.compileStatement("replace into tbl_payment(salesOrderId, paidValue, paidDate, paymentMethod, chequeNo, status, bank) values(?,?,?,?,?,?,?)");
						for (int i = 0; i < result.length(); i++) {
							JSONObject outlet = result.getJSONObject(i);
							compiledStatement.bindAllArgsAsStrings(new String[]{
								outlet.getString("outletId"),
								outlet.getString("outletName")
							});
							compiledStatement.executeInsert();
							JSONArray invoices = outlet.getJSONArray("invoices");
							for (int j = 0; j < invoices.length(); j++) {
								JSONObject invoiceJson = invoices.getJSONObject(j);
								invoiceStatement.bindAllArgsAsStrings(new String[]{
									String.valueOf(invoiceJson.getLong("salesOrderId")),
									outlet.getString("outletId"),
									invoiceJson.getString("date"),
									invoiceJson.getString("distributorCode"),
									invoiceJson.getString("pendingAmount")
								});
								invoiceStatement.executeInsert();
								JSONArray payments = invoiceJson.getJSONArray("payments");
								for (int k = 0; k < payments.length(); k++) {
									JSONObject payment = payments.getJSONObject(k);
									paymentStatement.bindAllArgsAsStrings(new String[]{
										invoiceJson.getString("salesOrderId"),
										payment.getString("paidValue"),
										payment.getString("paidDate"),
										payment.getString("paymentMethod"),
										payment.getString("chequeNo"),
										Integer.toString(1),
										payment.getString("bank")
									});
									paymentStatement.executeInsert();
								}
							}
						}
						writableDatabase.setTransactionSuccessful();
					} catch (JSONException ex) {
						Logger.getLogger(OutletController.class.getName()).log(Level.SEVERE, null, ex);
					} finally {
						writableDatabase.endTransaction();
						databaseInstance.close();
						Toast.makeText(context, "Outlets downloaded successfully", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(context, "No outlets Found", Toast.LENGTH_SHORT).show();
				}
			}
		}.execute(UserController.getAuthorizedUser(context));
	}

	public static boolean saveInvoicePayments(ArrayList<Outlet> outlets, Context context) {
		SQLiteDatabaseHelper databaseHelper = SQLiteDatabaseHelper.getDatabaseInstance(context);
		SQLiteDatabase database = databaseHelper.getWritableDatabase();
		String sql = "insert into tbl_payment(salesOrderId, paidValue, bank, paidDate, paymentMethod, chequeNo, status) values(?,?,?,?,?,?,?)";
		SQLiteStatement paymentsInsertQuery = database.compileStatement(sql);
		try {
			database.beginTransaction();
			for (Outlet outlet : outlets) {
				for (Invoice invoice : outlet.getPendingInvoices()) {
					if (invoice.getNewPayments() != null) {
						for (Payment payment : invoice.getNewPayments()) {
							paymentsInsertQuery.bindAllArgsAsStrings(new String[]{
								Long.toString(invoice.getSalesOrderId()),
								Double.toString(payment.getPaidValue()),
								(payment.getBank() == null) ? "" : payment.getBank(),
								payment.getPaidDate(),
								payment.getPaymentMethod(),
								(payment.getChequeNo() == null) ? "" : payment.getChequeNo(),
								Integer.toString(0)
							});
							paymentsInsertQuery.executeInsert();
						}
					}
				}
			}
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
			databaseHelper.close();
		}
		return true;
	}

	public static void syncPayments(final Context context) {
		new AsyncTask<Void, Void, Boolean>() {
			ProgressDialog progressDialog;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog = new ProgressDialog(context);
				progressDialog.setMessage("Syncing Payments");
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.show();
			}

			@Override
			protected Boolean doInBackground(Void... voids) {
				try {
					SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
					SQLiteDatabase database = databaseInstance.getWritableDatabase();
					Cursor paymentCursor = database.rawQuery("select paidValue, paidDate, paymentMethod, chequeNo, status, bank, salesOrderId from tbl_payment where status=0", null);
					JSONArray paymentsJson = new JSONArray();

					for (paymentCursor.moveToFirst(); !paymentCursor.isAfterLast(); paymentCursor.moveToNext()) {
						double paidValue = paymentCursor.getDouble(0);
						String paidDate = paymentCursor.getString(1);
						String paymentMethod = paymentCursor.getString(2);
						String chequeNo = paymentCursor.getString(3);
						boolean status = paymentCursor.getInt(4) == 1;
						String bank = paymentCursor.getString(5);
						long salesOrderId = paymentCursor.getLong(6);
						if (paymentMethod.equalsIgnoreCase(Payment.CASH_PAYMENT)) {
							paymentsJson.put(new Payment(salesOrderId, paidValue, paidDate, status).getPaymentAsJson());
						} else {
							paymentsJson.put(new Payment(salesOrderId, paidValue, paidDate, bank, chequeNo, status).getPaymentAsJson());
						}
					}
					paymentCursor.close();
					databaseInstance.close();
					HashMap<String, Object> httpParams = new HashMap<String, Object>();
					httpParams.put("payments", paymentsJson);
					httpParams.put("userId", UserController.getAuthorizedUser(context).getUserId());
					JSONObject response = getJsonObject(syncPayments, httpParams, context);
					return response != null && response.getBoolean("response");
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				} catch (JSONException e) {
					e.printStackTrace();
					return false;
				}
			}

			@Override
			protected void onPostExecute(Boolean response) {
				super.onPostExecute(response);
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				if (response) {
					Toast.makeText(context, "Payments Synced Successfully", Toast.LENGTH_LONG).show();
					SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
					SQLiteDatabase database = databaseInstance.getWritableDatabase();
					database.compileStatement("update tbl_payment set status=1").executeUpdateDelete();
					databaseInstance.close();
				} else {
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
					alertDialog.setTitle(R.string.message_title);
					alertDialog.setMessage("Unable to Sync Payments");
					alertDialog.setPositiveButton("Ok", null);
					alertDialog.show();
				}

			}
		}.execute();
	}
}
