/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xfinity.ceylon_steel.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.widget.Toast;
import static com.xfinity.ceylon_steel.controller.WebServiceURL.CustomerURL.getCustomersOfUser;
import com.xfinity.ceylon_steel.db.SQLiteDatabaseHelper;
import com.xfinity.ceylon_steel.model.Customer;
import com.xfinity.ceylon_steel.model.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Supun Lakshan
 */
public class CustomerController extends AbstractController {

	public static ArrayList<Customer> getCustomers(Context context) {
		ArrayList<Customer> customers = new ArrayList<Customer>();
		SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
		SQLiteDatabase database = databaseInstance.getWritableDatabase();
		Cursor customerCursor = database.rawQuery("select customerId, customerName from tbl_customer", null);
		for (customerCursor.moveToFirst(); !customerCursor.isAfterLast(); customerCursor.moveToFirst()) {
			Customer driver = new Customer(
					customerCursor.getInt(0),
					customerCursor.getString(1)
			);
			customers.add(driver);
		}
		customerCursor.close();
		database.close();
		return customers;
	}

	public static void downloadCustomers(final Context context) {
		new AsyncTask<User, Void, JSONArray>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute(); //To change body of generated methods, choose Tools | Templates.
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
				if (result != null) {
					SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
					SQLiteDatabase database = databaseInstance.getWritableDatabase();
					SQLiteStatement compiledStatement = database.compileStatement("insert ignore into tbl_customer(customerId,customerName) values(?,?)");
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
						database.close();
					}
				} else {
					Toast.makeText(context, "Unable to download customers", Toast.LENGTH_SHORT).show();
				}
			}

		}.execute(UserController.getAuthorizedUser(context));
	}
}
