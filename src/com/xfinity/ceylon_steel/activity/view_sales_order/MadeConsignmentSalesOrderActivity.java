/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xfinity.ceylon_steel.activity.view_sales_order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.xfinity.ceylon_steel.R;
import com.xfinity.ceylon_steel.controller.OrderController;
import com.xfinity.ceylon_steel.model.Order;
import java.util.ArrayList;

/**
 *
 * @author Supun Lakshan
 */
public class MadeConsignmentSalesOrderActivity extends Activity {

	private ListView consignmentOrderListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.made_consignment_page);
		initialize();

		ArrayList<Order> consignmentOrders = OrderController.getConsignmentOrders(this);
		ArrayAdapter<Order> orderAdapter = new ArrayAdapter<Order>(this, android.R.layout.simple_list_item_1, consignmentOrders);
		consignmentOrderListView.setAdapter(orderAdapter);
	}

	@Override
	public void onBackPressed() {
		Intent viewSalesOrderActivity = new Intent(this, ViewSalesOrderActivity.class);
		startActivity(viewSalesOrderActivity);
		finish();
	}

	// <editor-fold defaultstate="collapsed" desc="Initialize">
	private void initialize() {
		consignmentOrderListView = (ListView) findViewById(R.id.consignmentOrderListView);

		consignmentOrderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				onItemClicked(adapterView, view, position, id);
			}
		});
	}
	// </editor-fold>

	private void onItemClicked(AdapterView<?> adapterView, View view, int position, long id) {
		Order order = (Order) adapterView.getAdapter().getItem(position);
		Intent viewConsignmentSalesOrderActivity = new Intent(this, ViewConsignmentSalesOrderActivity.class);
		viewConsignmentSalesOrderActivity.putExtra("order", order);
		startActivity(viewConsignmentSalesOrderActivity);
		finish();
	}
}
