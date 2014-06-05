/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2014, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Apr 2, 2014, 10:21:38 PM
 */
package com.xfinity.ceylon_steel.activity.unproductive_call;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.xfinity.ceylon_steel.R;
import com.xfinity.ceylon_steel.controller.UnProductiveCallController;
import com.xfinity.ceylon_steel.model.UnProductiveCall;

import java.util.ArrayList;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class MadeUnProductiveCallActivity extends Activity {

	private ListView unProductiveCallsListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.made_unproductive_call_page);
		initialize();
		ArrayList<UnProductiveCall> unProductiveCalls = UnProductiveCallController.getUnProductiveCalls(this);
		ArrayAdapter<UnProductiveCall> unProductiveCallAdapter = new ArrayAdapter<UnProductiveCall>(this, android.R.layout.simple_list_item_1, unProductiveCalls);
		unProductiveCallsListView.setAdapter(unProductiveCallAdapter);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent unProductiveCallActivity = new Intent(this, UnProductiveCallActivity.class);
		startActivity(unProductiveCallActivity);
		finish();
	}

	// <editor-fold defaultstate="collapsed" desc="Initialize">
	private void initialize() {
		unProductiveCallsListView = (ListView) findViewById(R.id.unProductiveCallsListView);
		unProductiveCallsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				onItemClicked(adapterView, view, position, id);
			}
		});
	}
	// </editor-fold>

	private void onItemClicked(AdapterView<?> adapterView, View view, int position, long id) {
		UnProductiveCall unProductiveCall = (UnProductiveCall) adapterView.getAdapter().getItem(position);
		Intent viewUnProductiveCallActivity = new Intent(this, ViewUnProductiveCallActivity.class);
		viewUnProductiveCallActivity.putExtra("unProductiveCall", unProductiveCall);
		startActivity(viewUnProductiveCallActivity);
		finish();
	}
}
