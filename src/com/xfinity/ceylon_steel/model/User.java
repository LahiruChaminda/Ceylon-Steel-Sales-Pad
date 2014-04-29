/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 8, 2014, 8:15:28 PM
 */
package com.xfinity.ceylon_steel.model;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class User {

	private int userId;
	private String name;
	private String userType;

	public User() {
	}

	public User(int userId, String name) {
		this.userId = userId;
		this.name = name;
	}

	public User(int userId, String name, String userType) {
		this.userId = userId;
		this.name = name;
		this.userType = userType;
	}

	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

}
