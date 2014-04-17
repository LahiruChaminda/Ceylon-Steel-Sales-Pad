/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 9, 2014, 6:04:58 PM
 */
package com.xfinity.ceylon_steel.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.widget.Toast;
import static com.xfinity.ceylon_steel.controller.AbstractController.OutletURL.getOutletsOfUser;
import com.xfinity.ceylon_steel.db.SQLiteDatabaseHelper;
import com.xfinity.ceylon_steel.model.Outlet;
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
public class OutletController extends AbstractController {

	public static ArrayList<Outlet> getOutlets(Context context) {
		SQLiteDatabaseHelper database = SQLiteDatabaseHelper.getDatabaseInstance(context);
		SQLiteDatabase writableDatabase = database.getWritableDatabase();
		Cursor cursor = writableDatabase.rawQuery("select * from tbl_outlet", null);
		int outletIdIndex = cursor.getColumnIndex("outletId");
		int outletNameIndex = cursor.getColumnIndex("outletName");
		ArrayList<Outlet> outlets = new ArrayList<Outlet>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			Outlet outlet = new Outlet(cursor.getInt(outletIdIndex), cursor.getString(outletNameIndex));
			outlets.add(outlet);
		}
		cursor.close();
		writableDatabase.close();
		return outlets;
	}

	public static void downloadOutletsOfUser(final Context context) {
		new AsyncTask<User, Void, JSONArray>() {

			@Override
			protected void onPreExecute() {
				Toast.makeText(context, "Downloading outlets from remote server", Toast.LENGTH_SHORT).show();
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
				if (result != null) {
					SQLiteDatabaseHelper database = SQLiteDatabaseHelper.getDatabaseInstance(context);
					SQLiteDatabase writableDatabase = database.getWritableDatabase();
					try {
						writableDatabase.beginTransaction();
						SQLiteStatement compiledStatement = writableDatabase.compileStatement("insert or ignore into tbl_outlet(outletId, outletName) values(?,?)");
						for (int i = 0; i < result.length(); i++) {
							JSONObject outlet = result.getJSONObject(i);
							compiledStatement.bindAllArgsAsStrings(new String[]{
								outlet.getString("outletId"),
								outlet.getString("outletName")
							});
							compiledStatement.executeInsert();
						}
						writableDatabase.setTransactionSuccessful();
					} catch (JSONException ex) {
						Logger.getLogger(OutletController.class.getName()).log(Level.SEVERE, null, ex);
					} finally {
						writableDatabase.endTransaction();
						writableDatabase.close();
						Toast.makeText(context, "Outlets downloaded succesfully", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(context, "No outlets Found", Toast.LENGTH_SHORT).show();
				}
			}
		}.execute(UserController.getAuthorizedUser(context));
	}
}
