/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 9, 2014, 6:05:12 PM
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
import android.widget.Toast;
import com.xfinity.ceylon_steel.activity.HomeActivity;
import com.xfinity.ceylon_steel.activity.unproductive_call.MadeUnProductiveCallActivity;
import com.xfinity.ceylon_steel.db.SQLiteDatabaseHelper;
import com.xfinity.ceylon_steel.model.UnProductiveCall;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.xfinity.ceylon_steel.controller.WebServiceURL.UnProductiveCallURL.recordUnProductiveCall;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class UnProductiveCallController extends AbstractController {

	public static void makeUnProductiveCall(UnProductiveCall unProductiveCall, final Context context) {
		SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
		SQLiteDatabase database = databaseInstance.getWritableDatabase();
		SQLiteStatement compileStatement = database.compileStatement("insert into tbl_unproductive_call(outletId,reason,time,longitude,latitude,batteryLevel,repId) values(?,?,?,?,?,?,?)");
		compileStatement.bindAllArgsAsStrings(new String[]{
			Integer.toString(unProductiveCall.getOutletId()),
			unProductiveCall.getReason(),
			Long.toString(unProductiveCall.getTimestamp()),
			Double.toString(unProductiveCall.getLongitude()),
			Double.toString(unProductiveCall.getLatitude()),
			Integer.toString(unProductiveCall.getBatteryLevel()),
			Integer.toString(unProductiveCall.getRepId())
		});
		long executeInsert = compileStatement.executeInsert();
		databaseInstance.close();
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if (executeInsert != -1) {
			builder.setTitle(com.xfinity.ceylon_steel.R.string.message_title);
			builder.setMessage("Unproductive Call Recorded");
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
					Intent homeActivity = new Intent(context, HomeActivity.class);
					context.startActivity(homeActivity);
				}
			});
		} else {
			builder.setTitle(com.xfinity.ceylon_steel.R.string.message_title);
			builder.setMessage("Unable to place Unproductive Call");
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
					Intent homeActivity = new Intent(context, HomeActivity.class);
					context.startActivity(homeActivity);
				}
			});
		}
		builder.show();
	}

	public static ArrayList<UnProductiveCall> getUnProductiveCalls(Context context) {
		ArrayList<UnProductiveCall> unProductiveCalls = new ArrayList<UnProductiveCall>();
		SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
		SQLiteDatabase database = databaseInstance.getWritableDatabase();
		String sql = "select unProductiveCallId, tbl_unproductive_call.outletId, repId, reason, batteryLevel, time, longitude, latitude, tbl_outlet.outletName from tbl_unproductive_call, tbl_outlet where tbl_unproductive_call.outletId=tbl_outlet.outletId";
		Cursor cursor = database.rawQuery(sql, null);
		int unProductiveCallIdIndex = cursor.getColumnIndex("unProductiveCallId");
		int outletIdIndex = cursor.getColumnIndex("outletId");
		int reasonIndex = cursor.getColumnIndex("reason");
		int timeIndex = cursor.getColumnIndex("time");
		int outletNameIndex = cursor.getColumnIndex("outletName");
		int longitudeIndex = cursor.getColumnIndex("longitude");
		int latitudeIndex = cursor.getColumnIndex("latitude");
		int repIdIndex = cursor.getColumnIndex("repId");
		int batteryLevelIndex = cursor.getColumnIndex("batteryLevel");

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			UnProductiveCall unProductiveCall = new UnProductiveCall(
				cursor.getInt(unProductiveCallIdIndex),
				cursor.getInt(outletIdIndex),
				cursor.getString(outletNameIndex),
				cursor.getString(reasonIndex),
				cursor.getLong(timeIndex),
				cursor.getDouble(longitudeIndex),
				cursor.getDouble(latitudeIndex),
				cursor.getInt(batteryLevelIndex),
				cursor.getInt(repIdIndex)
			);
			unProductiveCalls.add(unProductiveCall);
		}
		cursor.close();
		databaseInstance.close();
		return unProductiveCalls;
	}

	public static void syncUnproductiveCall(final UnProductiveCall unProductiveCall, final Context context) {
		new AsyncTask<UnProductiveCall, Void, Boolean>() {
			private ProgressDialog progressDialog;

			@Override
			protected void onPreExecute() {
				progressDialog = new ProgressDialog(context);
				progressDialog.setMessage("Syncing Unproductive Call");
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.show();
			}

			@Override
			protected Boolean doInBackground(UnProductiveCall... unProductiveCalls) {
				UnProductiveCall unProductiveCall = unProductiveCalls[0];
				try {
					HashMap<String, Object> parameters = new HashMap<String, Object>();
					parameters.put("data", unProductiveCall.getUnProductiveCallAsJson());
					JSONObject responseJson = getJsonObject(recordUnProductiveCall, parameters, context);
					return responseJson.getBoolean("response");
				} catch (IOException ex) {
					Logger.getLogger(UnProductiveCallController.class.getName()).log(Level.SEVERE, null, ex);
				} catch (JSONException ex) {
					Logger.getLogger(UnProductiveCallController.class.getName()).log(Level.SEVERE, null, ex);
				}
				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				if (result) {
					SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
					SQLiteDatabase database = databaseInstance.getWritableDatabase();
					SQLiteStatement compiledStatement = database.compileStatement("delete from tbl_unproductive_call where unProductiveCallId=?");
					compiledStatement.bindAllArgsAsStrings(new String[]{Integer.toString(unProductiveCall.getUnProductiveCallId())});
					compiledStatement.executeUpdateDelete();
					databaseInstance.close();
					Intent madeUnProductiveCallActivity = new Intent(context, MadeUnProductiveCallActivity.class);
					context.startActivity(madeUnProductiveCallActivity);
					Toast.makeText(context, "Unproductive Call Successfully synced", Toast.LENGTH_SHORT).show();
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setMessage("Unable to sync UnProductive Call");
					builder.setTitle(com.xfinity.ceylon_steel.R.string.message_title);
					builder.setPositiveButton("Ok", null);
					builder.show();
				}
			}
		}.execute(unProductiveCall);
	}
}
