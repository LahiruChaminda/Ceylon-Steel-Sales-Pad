/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 9, 2014, 3:58:24 PM
 */
package com.xfinity.ceylon_steel.activity.make_sales_order;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
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
		if (UserController.getAuthorizedUser(this).getUserType().equalsIgnoreCase("REP_COMPANY")) {
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
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(com.xfinity.ceylon_steel.R.layout.order_type_seletion_page);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setTitle(com.xfinity.ceylon_steel.R.string.message_title);
		RadioButton defaultProjectOption = (RadioButton) dialog.findViewById(com.xfinity.ceylon_steel.R.id.defaultProjectOption);
		defaultProjectOption.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				Intent makeProjectSalesOrderActivity = new Intent(MakeSalesOrderActivity.this, MakeProjectSalesOrderActivity.class);
				startActivity(makeProjectSalesOrderActivity);
				finish();
			}
		});
		RadioButton directProjectOption = (RadioButton) dialog.findViewById(com.xfinity.ceylon_steel.R.id.directProjectOption);
		directProjectOption.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				Intent makeProjectSalesOrderActivity = new Intent(MakeSalesOrderActivity.this, MakeDirectProjectSalesOrderActivity.class);
				startActivity(makeProjectSalesOrderActivity);
				finish();
			}
		});
		dialog.show();
	}
}
