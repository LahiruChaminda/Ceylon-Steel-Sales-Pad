/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 9, 2014, 6:04:35 PM
 */
package com.xfinity.ceylon_steel.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.widget.Toast;
import static com.xfinity.ceylon_steel.controller.AbstractController.CategoryURL.getItemsAndCategories;
import com.xfinity.ceylon_steel.db.SQLiteDatabaseHelper;
import com.xfinity.ceylon_steel.model.Category;
import com.xfinity.ceylon_steel.model.Item;
import java.io.IOException;
import java.util.ArrayList;
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
public class CategoryController extends AbstractController {

	public static ArrayList<Category> getCategories(Context context) {
		ArrayList<Category> categories = new ArrayList<Category>();
		SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
		SQLiteDatabase database = databaseInstance.getWritableDatabase();
		Cursor categoryCursor = database.rawQuery("select categoryId,categoryDescription from tbl_category", null);
		for (categoryCursor.moveToFirst(); !categoryCursor.isAfterLast(); categoryCursor.moveToNext()) {
			int categoryId = categoryCursor.getInt(0);
			String categoryDescription = categoryCursor.getString(1);
			Cursor itemCursor = database.rawQuery("select itemId,itemCode,itemDescription,price from tbl_item where categoryId=?", new String[]{String.valueOf(categoryId)});
			ArrayList<Item> items = new ArrayList<Item>();
			for (itemCursor.moveToFirst(); !itemCursor.isAfterLast(); itemCursor.moveToNext()) {
				Item item = new Item(
						itemCursor.getInt(0),
						itemCursor.getString(1),
						itemCursor.getString(2),
						itemCursor.getDouble(3)
				);
				items.add(item);
			}
			itemCursor.close();
			Category category = new Category(categoryId, categoryDescription, items);
			categories.add(category);
		}
		categoryCursor.close();
		database.close();
		return categories;
	}

	public static void downLoadItemsAndCategories(final Context context) {
		new AsyncTask<Void, Void, JSONArray>() {

			@Override
			protected void onPreExecute() {
				Toast.makeText(context, "Downloading categories and items from remote server", Toast.LENGTH_SHORT).show();
			}

			@Override
			protected JSONArray doInBackground(Void... arg0) {
				try {
					return getJsonArray(getItemsAndCategories, null, context);
				} catch (IOException ex) {
					Logger.getLogger(CategoryController.class.getName()).log(Level.SEVERE, null, ex);
				} catch (JSONException ex) {
					Logger.getLogger(CategoryController.class.getName()).log(Level.SEVERE, null, ex);
				}
				return null;
			}

			@Override
			protected void onPostExecute(JSONArray result) {
				SQLiteDatabaseHelper databaseInstance = SQLiteDatabaseHelper.getDatabaseInstance(context);
				SQLiteDatabase database = databaseInstance.getWritableDatabase();
				try {
					if (result != null) {
						database.beginTransaction();
						SQLiteStatement categoryStatement = database.compileStatement("insert or ignore into tbl_category(categoryId,categoryDescription) values(?,?)");
						SQLiteStatement itemStatement = database.compileStatement("insert or ignore into tbl_item(itemId,categoryId,itemCode,itemDescription,price) values(?,?,?,?,?)");
						for (int categoryCount = 0; categoryCount < result.length(); categoryCount++) {
							JSONObject categoryJSON = result.getJSONObject(categoryCount);
							JSONArray items = categoryJSON.getJSONArray("items");
							String[] categoryParamaters = new String[]{
								categoryJSON.getString("categoryId"),
								categoryJSON.getString("categoryDescription")
							};
							categoryStatement.bindAllArgsAsStrings(categoryParamaters);
							categoryStatement.executeInsert();
							for (int itemCount = 0; itemCount < items.length(); itemCount++) {
								JSONObject item = items.getJSONObject(itemCount);
								String[] itemParamaters = new String[]{
									item.getString("itemId"),
									item.getString("categoryId"),
									item.getString("itemCode"),
									item.getString("itemDescription"),
									item.getString("price")
								};
								itemStatement.bindAllArgsAsStrings(itemParamaters);
								itemStatement.executeInsert();
							}
						}
						database.setTransactionSuccessful();
						Toast.makeText(context, "Categories and items downloaded successfully", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(context, "No categories and items found", Toast.LENGTH_SHORT).show();
					}
				} catch (SQLException ex) {
					Logger.getLogger(CategoryController.class.getName()).log(Level.SEVERE, null, ex);
				} catch (JSONException ex) {
					Logger.getLogger(CategoryController.class.getName()).log(Level.SEVERE, null, ex);
				} finally {
					database.endTransaction();
					database.close();
				}
			}
		}.execute();
	}
}
