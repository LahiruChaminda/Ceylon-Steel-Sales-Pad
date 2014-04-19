/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 11, 2014, 10:02:23 PM
 */
package com.xfinity.ceylon_steel.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.widget.Toast;
import static com.xfinity.ceylon_steel.controller.AbstractController.getJsonArray;
import static com.xfinity.ceylon_steel.controller.WebServiceURL.VehicleURL.getVehiclesOfUser;
import com.xfinity.ceylon_steel.db.SQLiteDatabaseHelper;
import com.xfinity.ceylon_steel.model.User;
import com.xfinity.ceylon_steel.model.Vehicle;
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
public class VehicleController extends AbstractController {

	public static ArrayList<Vehicle> getVehicles(Context context) {
		ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
		SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
		SQLiteDatabase writableDatabase = databaseInstance.getWritableDatabase();
		Cursor cursor = writableDatabase.rawQuery("select vehicleNo from tbl_vehicle", null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			Vehicle vehicle = new Vehicle(cursor.getString(0));
			vehicles.add(vehicle);
		}
		cursor.close();
		writableDatabase.close();
		return vehicles;
	}

	public static void downloadVehicles(final Context context) {
		new AsyncTask<User, Void, JSONArray>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected JSONArray doInBackground(User... users) {
				try {
					User user = users[0];
					HashMap<String, Object> parameters = new HashMap<String, Object>();
					parameters.put("userId", user.getUserId());
					return getJsonArray(getVehiclesOfUser, parameters, context);
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
					SQLiteStatement compiledStatement = database.compileStatement("insert ignore into tbl_vehicle(vehicleNo) values(?)");
					try {
						database.beginTransaction();
						for (int i = 0; i < result.length(); i++) {
							JSONObject customer = result.getJSONObject(i);
							compiledStatement.bindAllArgsAsStrings(new String[]{
								customer.getString("vehicleNo")
							});
							long response = compiledStatement.executeInsert();
							if (response == -1) {
								return;
							}
						}
						database.setTransactionSuccessful();
						Toast.makeText(context, "Vehicles downloaded successfully", Toast.LENGTH_SHORT).show();
					} catch (JSONException ex) {
						Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
						Toast.makeText(context, "Unable parse vehicles", Toast.LENGTH_SHORT).show();
					} finally {
						database.endTransaction();
						database.close();
					}
				} else {
					Toast.makeText(context, "Unable to download vehicles", Toast.LENGTH_SHORT).show();
				}
			}

		}.execute(UserController.getAuthorizedUser(context));
	}
}
