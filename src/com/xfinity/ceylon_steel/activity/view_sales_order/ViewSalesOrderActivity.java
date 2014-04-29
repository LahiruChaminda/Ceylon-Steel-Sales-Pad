/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 9, 2014, 4:10:08 PM
 */
package com.xfinity.ceylon_steel.activity.view_sales_order;

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
public class ViewSalesOrderActivity extends Activity {

	private Button btnViewConsignmentSalesOrder;
	private Button btnViewDirectSalesOrder;
	private Button btnViewProjectSalesOrder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_order_page);
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
		btnViewConsignmentSalesOrder = (Button) findViewById(R.id.btnViewConsignmentSalesOrder);
		btnViewDirectSalesOrder = (Button) findViewById(R.id.btnViewDirectSalesOrder);
		btnViewProjectSalesOrder = (Button) findViewById(R.id.btnViewProjectSalesOrder);

		btnViewConsignmentSalesOrder.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnViewConsignmentSalesOrderClicked(view);
			}
		});
		btnViewDirectSalesOrder.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnViewDirectSalesOrderClicked(view);
			}
		});
		btnViewProjectSalesOrder.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnViewProjectSalesOrderClicked(view);
			}
		});

		if (UserController.getAuthorizedUser(this).getUserType().equals("REP_COMPANY")) {
			btnViewConsignmentSalesOrder.setEnabled(false);
			btnViewProjectSalesOrder.setEnabled(false);
		}
	}
	// </editor-fold>

	private void btnViewConsignmentSalesOrderClicked(View view) {
		Intent madeConsignmentSalesOrderActivity = new Intent(this, MadeConsignmentSalesOrderActivity.class);
		startActivity(madeConsignmentSalesOrderActivity);
	}

	private void btnViewDirectSalesOrderClicked(View view) {
		Intent madeDirectSalesOrderActivity = new Intent(this, MadeDirectSalesOrderActivity.class);
		startActivity(madeDirectSalesOrderActivity);
	}

	private void btnViewProjectSalesOrderClicked(View view) {
		Intent madeProjectSalesOrderActivity = new Intent(this, MadeProjectSalesOrderActivity.class);
		startActivity(madeProjectSalesOrderActivity);
	}
}
