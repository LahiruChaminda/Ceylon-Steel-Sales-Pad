/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2014, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Sep 13, 2014, 12:57 PM
 */
package com.xfinity.ceylon_steel.widget;

import android.widget.BaseExpandableListAdapter;
import android.widget.Filter;
import android.widget.Filterable;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public abstract class FilterableBaseExpandableListAdapter extends BaseExpandableListAdapter implements Filterable {
	@Override
	public Filter getFilter() {
		return null;
	}
}
