/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 9, 2014, 5:41:28 PM
 */
package com.xfinity.ceylon_steel.model;

import android.content.Context;

import java.util.ArrayList;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class Category {

	private int categoryId;
	private String description;
	private ArrayList<Item> items;

	public Category(int categoryId, String description, ArrayList<Item> items) {
		this.categoryId = categoryId;
		this.description = description;
		this.items = items;
	}

	/**
	 * @return the categoryId
	 */
	public int getCategoryId() {
		return categoryId;
	}

	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return description;
	}

	public ArrayList<Item> getItems(Context context) {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}
}
