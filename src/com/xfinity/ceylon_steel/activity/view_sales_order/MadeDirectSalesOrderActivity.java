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
public class MadeDirectSalesOrderActivity extends Activity {

	private ListView directOrderListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.made_direct_page);
		initialize();

		ArrayList<Order> directOrders = OrderController.getDirectOrders(this);
		ArrayAdapter<Order> orderAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, directOrders);
		directOrderListView.setAdapter(orderAdapter);
	}

	@Override
	public void onBackPressed() {
		Intent viewSalesOrderActivity = new Intent(this, ViewSalesOrderActivity.class);
		startActivity(viewSalesOrderActivity);
		finish();
	}

	// <editor-fold defaultstate="collapsed" desc="Initialize">
	private void initialize() {
		directOrderListView = (ListView) findViewById(R.id.directOrderListView);
		directOrderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				onItemClicked(adapterView, view, position, id);
			}
		});
	}
	// </editor-fold>

	private void onItemClicked(AdapterView<?> adapterView, View view, int position, long id) {
		Order order = (Order) adapterView.getAdapter().getItem(position);
		Intent viewConsignmentSalesOrderActivity = new Intent(this, ViewDirectSalesOrderActivity.class);
		viewConsignmentSalesOrderActivity.putExtra("order", order);
		startActivity(viewConsignmentSalesOrderActivity);
		finish();
	}
}
