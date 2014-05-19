/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 11, 2014, 10:03:18 PM
 */
package com.xfinity.ceylon_steel.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.widget.Toast;
import static com.xfinity.ceylon_steel.controller.WebServiceURL.DriverURL.getDriversOfUser;
import com.xfinity.ceylon_steel.db.SQLiteDatabaseHelper;
import com.xfinity.ceylon_steel.model.Driver;
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
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class DriverController extends AbstractController {

	public static ArrayList<Driver> getDrivers(Context context) {
		ArrayList<Driver> drivers = new ArrayList<Driver>();
		SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
		SQLiteDatabase database = databaseInstance.getWritableDatabase();
		Cursor driversCursor = database.rawQuery("select driverName, driverNIC from tbl_driver", null);
		for (driversCursor.moveToFirst(); !driversCursor.isAfterLast(); driversCursor.moveToNext()) {
			Driver driver = new Driver(
					driversCursor.getString(0),
					driversCursor.getString(1)
			);
			drivers.add(driver);
		}
		driversCursor.close();
		databaseInstance.close();
		return drivers;
	}

	public static void downloadDrivers(final Context context) {
		new AsyncTask<User, Void, JSONArray>() {

			@Override
			protected void onPreExecute() {
				Toast.makeText(context, "Downloading Drivers from server", Toast.LENGTH_SHORT).show();
			}

			@Override
			protected JSONArray doInBackground(User... users) {
				try {
					HashMap<String, Object> parameters = new HashMap<String, Object>();
					parameters.put("userId", users[0].getUserId());
					return getJsonArray(getDriversOfUser, parameters, context);
				} catch (IOException ex) {
					Logger.getLogger(CategoryController.class.getName()).log(Level.SEVERE, null, ex);
				} catch (JSONException ex) {
					Logger.getLogger(CategoryController.class.getName()).log(Level.SEVERE, null, ex);
				}
				return null;
			}

			@Override
			protected void onPostExecute(JSONArray result) {
				if (result != null) {
					SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
					SQLiteDatabase database = databaseInstance.getWritableDatabase();
					SQLiteStatement compiledStatement = database.compileStatement("insert or ignore into tbl_driver(driverName,driverNIC) values(?,?)");
					try {
						database.beginTransaction();
						for (int i = 0; i < result.length(); i++) {
							JSONObject driver = result.getJSONObject(i);
							compiledStatement.bindAllArgsAsStrings(new String[]{
								driver.getString("driverName"),
								driver.getString("driverNIC")
							});
							long response = compiledStatement.executeInsert();
							if (response == -1) {
								return;
							}
						}
						database.setTransactionSuccessful();
						Toast.makeText(context, "Drivers downloaded successfully", Toast.LENGTH_SHORT).show();
					} catch (JSONException ex) {
						Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
						Toast.makeText(context, "Unable parse drivers", Toast.LENGTH_SHORT).show();
					} finally {
						database.endTransaction();
						databaseInstance.close();
					}
				} else {
					Toast.makeText(context, "Unable to retrieve drivers", Toast.LENGTH_SHORT).show();
				}
			}

		}.execute(UserController.getAuthorizedUser(context));
	}
}
