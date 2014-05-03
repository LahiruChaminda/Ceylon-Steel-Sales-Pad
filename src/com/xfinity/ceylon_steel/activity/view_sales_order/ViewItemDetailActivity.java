/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 18, 2014, 12:23:28 AM
 */
package com.xfinity.ceylon_steel.activity.view_sales_order;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.xfinity.ceylon_steel.R;
import com.xfinity.ceylon_steel.controller.OrderController;
import com.xfinity.ceylon_steel.model.Order;
import com.xfinity.ceylon_steel.model.OrderDetail;
import java.text.NumberFormat;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class ViewItemDetailActivity extends Activity {

	private TextView viewItem;
	private TextView viewItemUnitPrice;
	private EditText viewTxtQuantity;
	private EditText viewTxtEachDiscount;
	private TextView viewItemAmount;
	private Button btnViewItemUpdate;
	private Button btnViewItemBack;

	private OrderDetail receivedOrderDetail;
	private Order order;
	private Class referer;

	NumberFormat currencyFormat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_item_detail_page);
		initialize();

		this.receivedOrderDetail = (OrderDetail) getIntent().getExtras().get("orderDetail");
		this.order = (Order) getIntent().getExtras().get("order");
		this.referer = (Class) getIntent().getExtras().get("referer");

		currencyFormat = NumberFormat.getInstance();
		currencyFormat.setMinimumFractionDigits(2);
		currencyFormat.setMaximumFractionDigits(2);
		currencyFormat.setGroupingUsed(true);

		viewItem.setText(receivedOrderDetail.getItemDescription());
		viewItemUnitPrice.setText("Rs " + currencyFormat.format(receivedOrderDetail.getUnitPrice()));
		viewTxtQuantity.setText(Double.toString(receivedOrderDetail.getQuantity()));
		viewTxtEachDiscount.setText(Double.toString(receivedOrderDetail.getEachDiscount()));
		viewItemAmount.setText("Rs " + currencyFormat.format((receivedOrderDetail.getUnitPrice() - receivedOrderDetail.getEachDiscount()) * receivedOrderDetail.getQuantity()));
	}

	@Override
	public void onBackPressed() {
		Intent referedActivity = new Intent(this, referer);
		referedActivity.putExtra("order", order);
		startActivity(referedActivity);
		finish();
	}

	// <editor-fold defaultstate="collapsed" desc="Initialize">
	private void initialize() {
		viewItem = (TextView) findViewById(R.id.viewItem);
		viewItemUnitPrice = (TextView) findViewById(R.id.viewItemUnitPrice);
		viewTxtQuantity = (EditText) findViewById(R.id.viewTxtQuantity);
		viewTxtEachDiscount = (EditText) findViewById(R.id.viewTxtEachDiscount);
		viewItemAmount = (TextView) findViewById(R.id.viewItemAmount);
		btnViewItemUpdate = (Button) findViewById(R.id.btnViewItemUpdate);
		btnViewItemBack = (Button) findViewById(R.id.btnViewItemBack);

		viewTxtQuantity.addTextChangedListener(new TextWatcher() {
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void afterTextChanged(Editable editable) {
				viewTxtQuantityAfterTextChanged(editable);
			}
		});

		viewTxtEachDiscount.addTextChangedListener(new TextWatcher() {
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void afterTextChanged(Editable editable) {
				viewTxtEachDiscountAfterTextChanged(editable);
			}
		});

		btnViewItemUpdate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnViewItemUpdateClicked(view);
			}
		});

		btnViewItemBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnViewItemBackClicked(view);
			}
		});
	}
	// </editor-fold>

	private void btnViewItemBackClicked(View view) {
		Intent referedActivity = new Intent(this, referer);
		referedActivity.putExtra("order", order);
		startActivity(referedActivity);
		finish();
	}

	private void btnViewItemUpdateClicked(View view) {
		order.getOrderDetails().remove(receivedOrderDetail);
		receivedOrderDetail.setEachDiscount(Double.parseDouble(viewTxtEachDiscount.getText().toString()));
		receivedOrderDetail.setQuantity(Double.parseDouble(viewTxtQuantity.getText().toString()));
		order.getOrderDetails().add(receivedOrderDetail);
		long response;
		if (order.getOrderType().equalsIgnoreCase(Order.CONSIGNMENT)) {
			response = OrderController.updateConsignmentOrder(this, order);
		} else if (order.getOrderType().equalsIgnoreCase(Order.DIRECT)) {
			response = OrderController.updateDirectOrder(this, order);
		} else {
			response = OrderController.updateProjectOrder(this, order);
		}
		if (response == -1) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle(R.string.message_title);
			alert.setMessage("Unable to Update Order");
			alert.setPositiveButton("Ok", null);
			alert.show();
		} else {
			Intent referedActivity = new Intent(this, referer);
			referedActivity.putExtra("order", order);
			startActivity(referedActivity);
			finish();
		}
	}

	private void viewTxtQuantityAfterTextChanged(Editable editable) {
		double unitPrice = receivedOrderDetail.getUnitPrice();
		double eachDiscount = Double.parseDouble(viewTxtEachDiscount.getText().toString().isEmpty() ? "0" : viewTxtEachDiscount.getText().toString());
		double quantity = Double.parseDouble(viewTxtQuantity.getText().toString().isEmpty() ? "0" : viewTxtQuantity.getText().toString());
		viewItemAmount.setText("Rs " + currencyFormat.format((unitPrice - eachDiscount) * quantity));
	}

	private void viewTxtEachDiscountAfterTextChanged(Editable editable) {
		double unitPrice = receivedOrderDetail.getUnitPrice();
		double eachDiscount = Double.parseDouble(viewTxtEachDiscount.getText().toString().isEmpty() ? "0" : viewTxtEachDiscount.getText().toString());
		double quantity = Double.parseDouble(viewTxtQuantity.getText().toString().isEmpty() ? "0" : viewTxtQuantity.getText().toString());
		viewItemAmount.setText("Rs " + currencyFormat.format((unitPrice - eachDiscount) * quantity));
	}
}
