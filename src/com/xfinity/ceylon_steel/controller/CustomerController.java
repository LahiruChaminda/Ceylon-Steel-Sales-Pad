/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xfinity.ceylon_steel.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.widget.Toast;
import com.xfinity.ceylon_steel.db.SQLiteDatabaseHelper;
import com.xfinity.ceylon_steel.model.Customer;
import com.xfinity.ceylon_steel.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.xfinity.ceylon_steel.controller.WebServiceURL.CustomerURL.getCustomersOfUser;

/**
 * @author Supun Lakshan
 */
public class CustomerController extends AbstractController {

	public static ArrayList<Customer> getCustomers(Context context) {
		ArrayList<Customer> customers = new ArrayList<Customer>();
		SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
		SQLiteDatabase database = databaseInstance.getWritableDatabase();
		Cursor customerCursor = database.rawQuery("select customerId, customerName from tbl_customer", null);
		int customerIdIndex = customerCursor.getColumnIndex("customerId");
		int customerNameIndex = customerCursor.getColumnIndex("customerName");
		for (customerCursor.moveToFirst(); !customerCursor.isAfterLast(); customerCursor.moveToNext()) {
			Customer customer = new Customer(
				customerCursor.getInt(customerIdIndex),
				customerCursor.getString(customerNameIndex)
			);
			customers.add(customer);
		}
		customerCursor.close();
		databaseInstance.close();
		return customers;
	}

	public static void downloadCustomers(final Context context) {
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

			@Override
			protected JSONArray doInBackground(User... users) {
				try {
					User user = users[0];
					HashMap<String, Object> parameters = new HashMap<String, Object>();
					parameters.put("userId", user.getUserId());
					return getJsonArray(getCustomersOfUser, parameters, context);
				} catch (IOException ex) {
					Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
				} catch (JSONException ex) {
					Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
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
					SQLiteDatabase database = databaseInstance.getWritableDatabase();
					SQLiteStatement compiledStatement = database.compileStatement("insert or ignore into tbl_customer(customerId,customerName) values(?,?)");
					try {
						database.beginTransaction();
						for (int i = 0; i < result.length(); i++) {
							JSONObject customer = result.getJSONObject(i);
							compiledStatement.bindAllArgsAsStrings(new String[]{
								Integer.toString(customer.getInt("customerId")),
								customer.getString("customerName")
							});
							long response = compiledStatement.executeInsert();
							if (response == -1) {
								return;
							}
						}
						database.setTransactionSuccessful();
						Toast.makeText(context, "Customers downloaded successfully", Toast.LENGTH_SHORT).show();
					} catch (JSONException ex) {
						Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
						Toast.makeText(context, "Unable parse customers", Toast.LENGTH_SHORT).show();
					} finally {
						database.endTransaction();
						databaseInstance.close();
					}
				} else {
					Toast.makeText(context, "Unable to download customers", Toast.LENGTH_SHORT).show();
				}
			}

		}.execute(UserController.getAuthorizedUser(context));
	}
}
