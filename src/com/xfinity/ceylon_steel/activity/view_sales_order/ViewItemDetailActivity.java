/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 18, 2014, 12:23:28 AM
 */
package com.xfinity.ceylon_steel.activity.view_sales_order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.xfinity.ceylon_steel.R;
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
	private TextView viewTxtQuantity;
	private TextView viewTxtEachDiscount;
	private TextView viewItemAmount;
	private Button btnViewItemBack;

	private OrderDetail receivedOrderDetail;
	private Order order;
	private Class referer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_item_detail_page);
		initialize();
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
		this.receivedOrderDetail = (OrderDetail) getIntent().getExtras().get("orderDetail");
		this.order = (Order) getIntent().getExtras().get("order");
		this.referer = (Class) getIntent().getExtras().get("referer");

		viewItem = (TextView) findViewById(R.id.viewItem);
		viewItemUnitPrice = (TextView) findViewById(R.id.viewItemUnitPrice);
		viewTxtQuantity = (TextView) findViewById(R.id.viewTxtQuantity);
		viewTxtEachDiscount = (TextView) findViewById(R.id.viewTxtEachDiscount);
		viewItemAmount = (TextView) findViewById(R.id.viewItemAmount);
		btnViewItemBack = (Button) findViewById(R.id.btnViewItemBack);

		NumberFormat currencyFormat = NumberFormat.getInstance();
		currencyFormat.setMinimumFractionDigits(2);
		currencyFormat.setMaximumFractionDigits(2);
		currencyFormat.setGroupingUsed(true);

		viewItem.setText(receivedOrderDetail.getItemDescription());
		viewItemUnitPrice.setText("Rs " + currencyFormat.format(receivedOrderDetail.getUnitPrice()));
		viewTxtQuantity.setText(Double.toString(receivedOrderDetail.getQuantity()));
		viewTxtEachDiscount.setText("Rs " + currencyFormat.format(receivedOrderDetail.getEachDiscount()));
		viewItemAmount.setText("Rs " + currencyFormat.format((receivedOrderDetail.getUnitPrice() - receivedOrderDetail.getEachDiscount()) * receivedOrderDetail.getQuantity()));

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
}
