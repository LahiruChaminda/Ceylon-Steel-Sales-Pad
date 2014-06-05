/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 8, 2014, 8:08:30 PM
 */
package com.xfinity.ceylon_steel.controller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.location.Location;
import android.os.AsyncTask;
import android.provider.Settings;
import android.widget.Toast;
import com.xfinity.ceylon_steel.activity.HomeActivity;
import com.xfinity.ceylon_steel.db.SQLiteDatabaseHelper;
import com.xfinity.ceylon_steel.model.User;
import com.xfinity.ceylon_steel.model.UserLocation;
import com.xfinity.ceylon_steel.service.BatteryService;
import com.xfinity.ceylon_steel.service.GpsReceiver;
import com.xfinity.ceylon_steel.service.Tracker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.xfinity.ceylon_steel.controller.WebServiceURL.UserURL.*;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class UserController extends AbstractController {

	public static volatile ProgressDialog progressDialog;
	public static volatile AtomicInteger atomicInteger;

	private UserController() {
	}

	public static void downloadDistributors(final Context context) {
		new AsyncTask<User, Object, JSONArray>() {

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
				User user = users[0];
				try {
					HashMap<String, Object> parameters = new HashMap<String, Object>();
					parameters.put("userId", user.getUserId());
					return getJsonArray(getDistributorsOfUser, parameters, context);
				} catch (IOException ex) {
					Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
				} catch (JSONException ex) {
					Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
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
					String userInsertQuery = "insert or ignore into tbl_distributor(distributorId, distributorName) values (?,?)";
					SQLiteStatement compiledStatement = database.compileStatement(userInsertQuery);
					try {
						database.beginTransaction();
						for (int i = 0; i < result.length(); i++) {
							try {
								JSONObject distributor = result.getJSONObject(i);
								int userId = distributor.getInt("userId");
								String name = distributor.getString("name");
								compiledStatement.bindAllArgsAsStrings(new String[]{Integer.toString(userId), name});
								compiledStatement.executeInsert();
							} catch (JSONException ex) {
								Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
							}
						}
						database.setTransactionSuccessful();
					} finally {
						database.endTransaction();
						databaseInstance.close();
					}
					Toast.makeText(context, "Distributors downloaded successfully", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, "Unable to download distributors", Toast.LENGTH_SHORT).show();
				}
			}

		}.execute(UserController.getAuthorizedUser(context));
	}

	public static ArrayList<User> getDistributors(Context context) {
		SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
		SQLiteDatabase database = databaseInstance.getWritableDatabase();
		Cursor cursor = database.rawQuery("select distributorId, distributorName from tbl_distributor", null);
		ArrayList<User> users = new ArrayList<User>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			User user = new User(cursor.getInt(0), cursor.getString(1));
			users.add(user);
		}
		cursor.close();
		databaseInstance.close();
		return users;
	}

	public static User getAuthorizedUser(Context context) {
		SharedPreferences userData = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
		Integer userId;
		String userName;
		String userType;
		long loginTime;
		if ((loginTime = userData.getLong("loginTime", -1)) == -1) {
			return null;
		}
		Date lastLoginDate = new Date(loginTime);
		Date currentDate = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("y-M-d");
		if (!simpleDateFormat.format(lastLoginDate).equalsIgnoreCase(simpleDateFormat.format(currentDate))) {
			return null;
		}
		if ((userId = userData.getInt("userId", -1)) == -1) {
			return null;
		}
		if ((userName = userData.getString("userName", "")).isEmpty()) {
			return null;
		}
		if ((userType = userData.getString("type", "")).isEmpty()) {
			return null;
		}
		return new User(userId, userName, userType);
	}

	public static boolean setAuthorizedUser(Context context, int userId, String name, String type, long loginTime) {
		SharedPreferences userData = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = userData.edit();
		editor.putInt("userId", userId);
		editor.putString("userName", name);
		editor.putString("type", type);
		editor.putLong("loginTime", loginTime);
		return editor.commit();
	}

	public static void authenticate(String userName, String password, final Context context) {
		new AsyncTask<String, Void, JSONObject>() {
			private ProgressDialog authenticatingProgressDialog;

			@Override
			protected void onPreExecute() {
				authenticatingProgressDialog = new ProgressDialog(context);
				authenticatingProgressDialog.setMessage("Waiting for Authenticating...");
				authenticatingProgressDialog.setCanceledOnTouchOutside(false);
				authenticatingProgressDialog.show();
			}

			@Override
			protected JSONObject doInBackground(String... userData) {
				try {
					String userName = userData[0];
					String password = userData[1];
					HashMap<String, Object> parameters = new HashMap<String, Object>();
					parameters.put("userName", userName);
					parameters.put("password", getMD5HashVal(password));
					return getJsonObject(getUserDetails, parameters, context);
				} catch (IOException ex) {
					Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
				} catch (JSONException ex) {
					Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
				} catch (NoSuchAlgorithmException ex) {
					Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
				}
				return null;
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				if (authenticatingProgressDialog != null && authenticatingProgressDialog.isShowing()) {
					authenticatingProgressDialog.dismiss();
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle(com.xfinity.ceylon_steel.R.string.message_title);
				try {
					builder.setPositiveButton("OK", null);
					if (result != null) {
						if (result.getBoolean("response")) {
							setAuthorizedUser(context, result.getInt("userId"), result.getString("name"), result.getString("type"), new Date().getTime());
							loadDataFromServer(context);
							new Thread() {
								@Override
								public void run() {
									while (atomicInteger.get() != 0) {
										try {
											Thread.sleep(200);
										} catch (InterruptedException ex) {
											Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
										}
									}
									Intent homeActivity = new Intent(context, HomeActivity.class);
									context.startActivity(homeActivity);
								}
							}.start();
						} else {
							builder.setMessage("Incorrect Username Password combination");
							builder.show();
						}
					} else {
						builder.setMessage("No Active Internet Connection Found");
						builder.show();
					}
				} catch (JSONException ex) {
					Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
					builder.setMessage("Json Parse Error.\nPlease Contact Ceylon Linux Developer Team!");
					builder.show();
				}
			}

		}.execute(userName, password);
	}

	private static String getMD5HashVal(String strToBeEncrypted) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		Formatter formatter = new Formatter();
		try {
			String encryptedString;
			byte[] bytesToBeEncrypted;
			bytesToBeEncrypted = strToBeEncrypted.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] theDigest = md.digest(bytesToBeEncrypted);
			for (byte b : theDigest) {
				formatter.format("%02x", b);
			}
			encryptedString = formatter.toString().toLowerCase();
			return encryptedString;
		} finally {
			formatter.close();
		}
	}

	public static void checkIn(final Context context) {
		final GpsReceiver gpsReceiver = GpsReceiver.getGpsReceiver(context);
		new AsyncTask<User, Void, JSONObject>() {
			private ProgressDialog progressDialog;
			private String checkinDateTime;

			@Override
			protected void onPreExecute() {
				progressDialog = new ProgressDialog(context);
				progressDialog.setMessage("Waiting for GPS Location...");
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.show();
			}

			@Override
			protected JSONObject doInBackground(User... users) {
				try {
					Date date = new Date();
					HashMap<String, Object> checkIn = new HashMap<String, Object>();
					Location lastKnownLocation;
					do {
						lastKnownLocation = gpsReceiver.getHighAccurateLocation();
					} while (lastKnownLocation == null);
					checkIn.put("time", new SimpleDateFormat("HH:mm:ss").format(date));
					checkIn.put("userId", UserController.getAuthorizedUser(context).getUserId());
					checkIn.put("type", "CHECKIN");
					checkIn.put("longitude", lastKnownLocation.getLongitude());
					checkIn.put("latitude", lastKnownLocation.getLatitude());
					checkIn.put("batteryLevel", BatteryService.getBatteryLevel(context));
					checkIn.put("date", new SimpleDateFormat("yyyy-MM-dd").format(date));
					HashMap<String, Object> parameters = new HashMap<String, Object>();
					parameters.put("data", new JSONObject(checkIn));
					checkinDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
					return getJsonObject(checkInCheckOut, parameters, context);
				} catch (JSONException ex) {
					Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IOException ex) {
					Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
				}
				return null;
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				try {
					int checkIn = result.getInt("response");
					AlertDialog.Builder responseDialog = new AlertDialog.Builder(context);
					responseDialog.setTitle(com.xfinity.ceylon_steel.R.string.message_title);
					switch (checkIn) {
						case -1:
							responseDialog.setMessage("Please check your system time");
							responseDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0, int arg1) {
									Intent settingsActivity = new Intent(Settings.ACTION_DATE_SETTINGS);
									context.startActivity(settingsActivity);
								}
							});
							break;
						case 0:
							responseDialog.setMessage("Unable to check in");
							responseDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0, int arg1) {
									Intent homeActivity = new Intent(context, HomeActivity.class);
									context.startActivity(homeActivity);
								}
							});
							break;
						case 1:
							responseDialog.setMessage("Checked in successful\n" + checkinDateTime);
							responseDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0, int arg1) {
									Intent homeActivity = new Intent(context, HomeActivity.class);
									context.startActivity(homeActivity);
								}
							});
							break;
						case 2:
							responseDialog.setMessage("Already checked in");
							responseDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0, int arg1) {
									Intent homeActivity = new Intent(context, HomeActivity.class);
									context.startActivity(homeActivity);
								}
							});
							break;
					}
					responseDialog.show();
				} catch (JSONException ex) {
					Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}.execute(new User());
	}

	public static void checkOut(final Context context) {
		final GpsReceiver gpsReceiver = GpsReceiver.getGpsReceiver(context);
		new AsyncTask<User, Void, JSONObject>() {
			private ProgressDialog progressDialog;
			private String checkoutDateTime;

			@Override
			protected void onPreExecute() {
				progressDialog = new ProgressDialog(context);
				progressDialog.setMessage("Waiting for GPS Location...");
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.show();
			}

			@Override
			protected JSONObject doInBackground(User... users) {
				try {
					Date date = new Date();
					HashMap<String, Object> checkOut = new HashMap<String, Object>();
					Location lastKnownLocation;
					do {
						lastKnownLocation = gpsReceiver.getHighAccurateLocation();
					} while (lastKnownLocation == null);
					checkOut.put("time", new SimpleDateFormat("HH:mm:ss").format(date));
					checkOut.put("userId", UserController.getAuthorizedUser(context).getUserId());
					checkOut.put("type", "CHECKOUT");
					checkOut.put("longitude", lastKnownLocation.getLongitude());
					checkOut.put("latitude", lastKnownLocation.getLatitude());
					checkOut.put("batteryLevel", BatteryService.getBatteryLevel(context));
					checkOut.put("date", new SimpleDateFormat("yyyy-MM-dd").format(date));
					checkoutDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
					HashMap<String, Object> parameters = new HashMap<String, Object>();
					parameters.put("data", new JSONObject(checkOut));
					return getJsonObject(checkInCheckOut, parameters, context);
				} catch (JSONException ex) {
					Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IOException ex) {
					Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
				}
				return null;
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				try {
					int checkIn = result.getInt("response");
					AlertDialog.Builder responseDialog = new AlertDialog.Builder(context);
					responseDialog.setTitle(com.xfinity.ceylon_steel.R.string.message_title);
					switch (checkIn) {
						case -1:
							responseDialog.setMessage("Please check your system time");
							responseDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0, int arg1) {
									Intent settingsActivity = new Intent(Settings.ACTION_DATE_SETTINGS);
									context.startActivity(settingsActivity);
								}
							});
							break;
						case 0:
							responseDialog.setMessage("Unable to checkout");
							responseDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0, int arg1) {
									Intent homeActivity = new Intent(context, HomeActivity.class);
									context.startActivity(homeActivity);
								}
							});
							break;
						case 1:
							responseDialog.setMessage("Checkout successful\n" + checkoutDateTime);
							responseDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0, int arg1) {
									Intent homeActivity = new Intent(context, HomeActivity.class);
									context.startActivity(homeActivity);
								}
							});
							Intent tracker = new Intent(context, Tracker.class);
							context.stopService(tracker);
							break;
						case 2:
							responseDialog.setMessage("You've been already checked out");
							responseDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0, int arg1) {
									Intent homeActivity = new Intent(context, HomeActivity.class);
									context.startActivity(homeActivity);
								}
							});
							break;
					}
					responseDialog.show();
				} catch (JSONException ex) {
					Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
				}
			}

		}.execute(new User());
	}

	public static void loadDataFromServer(Context context) {
		if (UserController.atomicInteger == null) {
			UserController.atomicInteger = new AtomicInteger();
		}

		UserController.atomicInteger.incrementAndGet();
		CategoryController.downLoadItemsAndCategories(context);

		UserController.atomicInteger.incrementAndGet();
		OutletController.downloadOutletsOfUser(context);

		UserController.atomicInteger.incrementAndGet();
		UserController.downloadDistributors(context);

		UserController.atomicInteger.incrementAndGet();
		CustomerController.downloadCustomers(context);
	}

	public static void markRepLocation(Context context, UserLocation userLocation) {
		User user;
		if ((user = UserController.getAuthorizedUser(context)) == null) {
			Tracker.stopTracking();
			return;
		}
		SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
		SQLiteDatabase database = databaseInstance.getWritableDatabase();
		String sql = "insert into tbl_rep_location(repId,longitude,latitude,gpsTime,batteryLevel) values(?,?,?,?,?)";
		SQLiteStatement compiledStatement = database.compileStatement(sql);
		compiledStatement.bindAllArgsAsStrings(new String[]{
			Integer.toString(user.getUserId()),
			Double.toString(userLocation.getLongitude()),
			Double.toString(userLocation.getLatitude()),
			Long.toString(userLocation.getTimestamp()),
			Integer.toString(userLocation.getBatteryLevel())
		});
		compiledStatement.executeInsert();
		databaseInstance.close();
	}

	public static void syncRepLocations(final Context context) {
		final ArrayList<UserLocation> userLocations = new ArrayList<UserLocation>();
		SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
		SQLiteDatabase database = databaseInstance.getWritableDatabase();
		SQLiteStatement compiledStatement = database.compileStatement("delete from tbl_rep_location where repLocationId=?");
		Cursor cursor = database.rawQuery("select repId, longitude, latitude, gpsTime, repLocationId, batteryLevel from tbl_rep_location", null);
		int repIdIndex = cursor.getColumnIndex("repId");
		int longitudeIndex = cursor.getColumnIndex("longitude");
		int latitudeIndex = cursor.getColumnIndex("latitude");
		int gpsTimeIndex = cursor.getColumnIndex("gpsTime");
		int repLocationIdIndex = cursor.getColumnIndex("repLocationId");
		int batteryLevelIndex = cursor.getColumnIndex("batteryLevel");
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			UserLocation userLocation = new UserLocation(
				cursor.getDouble(longitudeIndex),
				cursor.getDouble(latitudeIndex),
				cursor.getLong(gpsTimeIndex),
				cursor.getInt(repIdIndex),
				cursor.getInt(repLocationIdIndex),
				cursor.getInt(batteryLevelIndex)
			);
			userLocations.add(userLocation);
		}
		cursor.close();
		List<Integer> synckedRepLocations = new ArrayList<Integer>();
		for (UserLocation userLocation : userLocations) {
			try {
				HashMap<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("data", userLocation.getUserLocationJson());
				JSONObject responseJson = getJsonObject(markRepLocations, parameters, context);
				if (responseJson != null && responseJson.getBoolean("response")) {
					synckedRepLocations.add(userLocation.getRepLocationId());
				}
			} catch (IOException ex) {
				Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
			} catch (JSONException ex) {
				Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		for (Object repLocationId : synckedRepLocations) {
			compiledStatement.bindAllArgsAsStrings(new String[]{
				Integer.toString((Integer) repLocationId)
			});
			compiledStatement.executeUpdateDelete();
		}
		databaseInstance.close();
	}

	public static boolean clearAuthentication(Context context) {
		SharedPreferences userData = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = userData.edit();
		editor.putInt("userId", -1);
		editor.putString("userName", "");
		editor.putString("type", "");
		editor.putLong("loginTime", -1);
		return editor.commit();
	}
}
