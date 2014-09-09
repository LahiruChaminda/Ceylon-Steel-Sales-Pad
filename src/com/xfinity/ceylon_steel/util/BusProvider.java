/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2014, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Sep 07, 2014, 11:28:45 AM
 */
package com.xfinity.ceylon_steel.util;

import com.squareup.otto.Bus;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public final class BusProvider {
	private static final Bus BUS = new Bus();

	private BusProvider() {
	}

	public static final Bus getInstance() {
		return BUS;
	}
}
