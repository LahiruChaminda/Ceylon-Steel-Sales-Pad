/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 10, 2014, 12:26:28 AM
 */
package com.xfinity.ceylon_steel.activity.unproductive_call;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.xfinity.ceylon_steel.R;
import com.xfinity.ceylon_steel.controller.UnProductiveCallController;
import com.xfinity.ceylon_steel.model.UnProductiveCall;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class ViewUnProductiveCallActivity extends Activity {

	private TextView unProductiveCallOutlet;
	private EditText txtViewUnProductiveCallReason;
	private Button btnUnProductiveCallSync;

	private UnProductiveCall receivedUnProductiveCall;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_unproductive_call_page);
		initialize();
	}

	@Override
	public void onBackPressed() {
		Intent unProductiveCall = new Intent(this, UnProductiveCallActivity.class);
		startActivity(unProductiveCall);
		finish();
	}

	// <editor-fold defaultstate="collapsed" desc="Initialize">
	private void initialize() {
		unProductiveCallOutlet = (TextView) findViewById(R.id.unProductiveCallOutlet);
		txtViewUnProductiveCallReason = (EditText) findViewById(R.id.txtViewUnProductiveCallReason);
		btnUnProductiveCallSync = (Button) findViewById(R.id.btnUnProductiveCallSync);

		receivedUnProductiveCall = (UnProductiveCall) getIntent().getExtras().getSerializable("unProductiveCall");
		unProductiveCallOutlet.setText(receivedUnProductiveCall.getOutletName());
		txtViewUnProductiveCallReason.setText(receivedUnProductiveCall.getReason());

		btnUnProductiveCallSync.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnUnProductiveCallSyncClicked(view);
			}
		});
	}
	// </editor-fold>

	private void btnUnProductiveCallSyncClicked(View view) {
		UnProductiveCallController.syncUnproductiveCall(receivedUnProductiveCall, this);
	}
}
