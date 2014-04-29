/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 9, 2014, 3:58:24 PM
 */
package com.xfinity.ceylon_steel.activity.make_sales_order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.xfinity.ceylon_steel.R;
import com.xfinity.ceylon_steel.activity.HomeActivity;
import com.xfinity.ceylon_steel.controller.UserController;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class MakeSalesOrderActivity extends Activity {

	private Button btnMakeConsignmentSalesOrder;
	private Button btnMakeDirectSalesOrder;
	private Button btnMakeProjectSalesOrder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.make_order_page);
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
		btnMakeConsignmentSalesOrder = (Button) findViewById(R.id.btnMakeConsignmentSalesOrder);
		btnMakeDirectSalesOrder = (Button) findViewById(R.id.btnMakeDirectSalesOrder);
		btnMakeProjectSalesOrder = (Button) findViewById(R.id.btnMakeProjectSalesOrder);

		btnMakeConsignmentSalesOrder.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnMakeConsignmentSalesOrderClicked(view);
			}
		});
		btnMakeDirectSalesOrder.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnMakeDirectSalesOrderClicked(view);
			}
		});
		btnMakeProjectSalesOrder.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnMakeProjectSalesOrderClicked(view);
			}
		});
		if (UserController.getAuthorizedUser(this).getUserType().equals("REP_COMPANY")) {
			btnMakeConsignmentSalesOrder.setEnabled(false);
			btnMakeProjectSalesOrder.setEnabled(false);
		}
	}
	// </editor-fold>

	private void btnMakeConsignmentSalesOrderClicked(View view) {
		Intent makeConsignmentSalesOrderActivity = new Intent(this, MakeConsignmentSalesOrderActivity.class);
		startActivity(makeConsignmentSalesOrderActivity);
		finish();
	}

	private void btnMakeDirectSalesOrderClicked(View view) {
		Intent makeDirectSalesOrderActivity = new Intent(this, MakeDirectSalesOrderActivity.class);
		startActivity(makeDirectSalesOrderActivity);
		finish();
	}

	private void btnMakeProjectSalesOrderClicked(View view) {
		Intent makeProjectSalesOrderActivity = new Intent(this, MakeProjectSalesOrderActivity.class);
		startActivity(makeProjectSalesOrderActivity);
		finish();
	}
}
