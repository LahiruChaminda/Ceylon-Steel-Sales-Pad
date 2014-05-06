/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 14, 2014, 8:41:37 PM
 */
package com.xfinity.ceylon_steel.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

	private AtomicInteger atomicInteger;
	private static final String DATABASE_NAME = "ceylon_steel";
	private static final int VERSION = 23;
	private static volatile SQLiteDatabaseHelper database;
	private final AssetManager assets;

	private SQLiteDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
		assets = context.getAssets();
	}

	public static synchronized SQLiteDatabaseHelper getDatabaseInstance(Context context) {
		if (database == null) {
			database = new SQLiteDatabaseHelper(context);
		}
		return database;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			InputStream databaseStream = assets.open("database.sql");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(databaseStream));
			String databaseDeclaration = "";
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				databaseDeclaration = databaseDeclaration + line.replace("\t", "");
			}
			for (String sql : databaseDeclaration.split(";")) {
				String trimmedQuery;
				if (!(trimmedQuery = sql.trim()).isEmpty()) {
					db.execSQL(trimmedQuery);
				}
			}
			db.execSQL("PRAGMA foreign_keys = ON;");
		} catch (IOException ex) {
			Logger.getLogger(SQLiteDatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
	}

	public static boolean dropDatabase(Context context) {
		return context.deleteDatabase(DATABASE_NAME);
	}

	@Override
	public SQLiteDatabase getWritableDatabase() {
		if (atomicInteger == null) {
			atomicInteger = new AtomicInteger();
		}
		atomicInteger.incrementAndGet();
		SQLiteDatabase writableDatabase = super.getWritableDatabase();
		writableDatabase.execSQL("PRAGMA foreign_keys = ON;");
		return writableDatabase;
	}

	@Override
	public synchronized void close() {
		if (atomicInteger != null && atomicInteger.decrementAndGet() == 0) {
			super.close();
		}
	}
}
