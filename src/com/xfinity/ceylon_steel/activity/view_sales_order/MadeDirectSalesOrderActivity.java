/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xfinity.ceylon_steel.activity.view_sales_order;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.xfinity.ceylon_steel.R;
import com.xfinity.ceylon_steel.controller.OrderController;
import com.xfinity.ceylon_steel.model.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Supun Lakshan
 */
public class MadeDirectSalesOrderActivity extends Activity {

	private ListView directOrderListView;
	private ArrayList<Order> directOrders;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.made_direct_page);
		initialize();

		directOrders = OrderController.getDirectOrders(this);
		directOrderListView.setAdapter(new BaseAdapter() {
			private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd MMMM, yyyy hh:mm:ss aa");

			public int getCount() {
				return directOrders.size();
			}

			public Object getItem(int position) {
				return directOrders.get(position);
			}

			public long getItemId(int position) {
				return position;
			}

			public View getView(int position, View convertView, ViewGroup parent) {
				OrderItemViewHolder orderItemViewHolder;
				if (convertView == null) {
					LayoutInflater inflater = (LayoutInflater) MadeDirectSalesOrderActivity.this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
					convertView = inflater.inflate(R.layout.order_list_item_view, null);
					orderItemViewHolder = new OrderItemViewHolder();
					orderItemViewHolder.txtOrderOwner = (TextView) convertView.findViewById(R.id.txtOrderOwner);
					orderItemViewHolder.txtDateTime = (TextView) convertView.findViewById(R.id.txtDateTime);
					convertView.setTag(orderItemViewHolder);
				} else {
					orderItemViewHolder = (OrderItemViewHolder) convertView.getTag();
				}
				Order order = directOrders.get(position);
				if (order.isSynced()) {
					convertView.setBackgroundColor(Color.RED);
				}
				orderItemViewHolder.txtOrderOwner.setText(order.toString());
				orderItemViewHolder.txtDateTime.setText(simpleDateFormat.format(new Date(order.getOrderMadeTimeStamp())));
				return convertView;
			}
		});
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

	private void onItemClicked(AdapterView<?> adapterView, View view, int position, long id) {
		Order order = directOrders.get(position);
		Intent viewConsignmentSalesOrderActivity = new Intent(this, ViewDirectSalesOrderActivity.class);
		viewConsignmentSalesOrderActivity.putExtra("order", order);
		startActivity(viewConsignmentSalesOrderActivity);
		finish();
	}
	// </editor-fold>

	private static class OrderItemViewHolder {

		TextView txtOrderOwner;
		TextView txtDateTime;
	}
}
