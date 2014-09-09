/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2014, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Sep 09, 2014, 11:32 AM
 */
package com.xfinity.ceylon_steel.activity.report;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.xfinity.ceylon_steel.R;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class ChequeRealizationReport extends Activity {

	private Button btnReturnToHome;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cheque_realization_report);
		initialize();
	}

	private void initialize() {
		btnReturnToHome = (Button) findViewById(R.id.btnReturnToHome);
		btnReturnToHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
}
