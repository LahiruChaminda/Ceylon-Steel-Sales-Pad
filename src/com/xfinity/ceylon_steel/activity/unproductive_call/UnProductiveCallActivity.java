/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 9, 2014, 4:26:05 PM
 */
package com.xfinity.ceylon_steel.activity.unproductive_call;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.xfinity.ceylon_steel.R;
import com.xfinity.ceylon_steel.activity.HomeActivity;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class UnProductiveCallActivity extends Activity {

	Button btnMakeUnProductiveCall;
	Button btnViewUnProductiveCall;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unproductive_call_page);
		initialize();
	}

	@Override
	public void onBackPressed() {
		Intent homeActivity = new Intent(this, HomeActivity.class);
		startActivity(homeActivity);
		finish();
	}

	// <editor-fold defaultstate="collapsed" desc="Initialize">
	private void initialize() {
		btnMakeUnProductiveCall = (Button) findViewById(R.id.btnMakeUnProductiveCall);
		btnViewUnProductiveCall = (Button) findViewById(R.id.btnViewUnProductiveCall);

		btnMakeUnProductiveCall.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnMakeUnProductiveCallClicked(view);
			}
		});
		btnViewUnProductiveCall.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnViewUnProductiveCallClicked(view);
			}
		});
	}
	// </editor-fold>

	private void btnMakeUnProductiveCallClicked(View view) {
		Intent makeUnProductiveCallActivity = new Intent(this, MakeUnProductiveCallActivity.class);
		startActivity(makeUnProductiveCallActivity);
	}

	private void btnViewUnProductiveCallClicked(View view) {
		Intent madeUnProductiveCallActivity = new Intent(this, MadeUnProductiveCallActivity.class);
		startActivity(madeUnProductiveCallActivity);
		finish();
	}
}
