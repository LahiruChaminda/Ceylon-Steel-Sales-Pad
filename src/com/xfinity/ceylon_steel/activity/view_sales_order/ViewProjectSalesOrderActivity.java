/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2014, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 31, 2014, 9:18:44 AM
 */
package com.xfinity.ceylon_steel.activity.view_sales_order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.xfinity.ceylon_steel.R;
import com.xfinity.ceylon_steel.controller.OrderController;
import com.xfinity.ceylon_steel.model.Order;
import com.xfinity.ceylon_steel.model.OrderDetail;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class ViewProjectSalesOrderActivity extends Activity {

	private Order receivedOrder;

	private TextView txtViewProjectCustomerName;
	private TextView txtViewProjectDistributorName;
	private TextView txtViewProjectVehicleNo;
	private TextView txtViewProjectDriverName;
	private TextView txtViewProjectDriverNIC;
	private TextView txtViewProjectDeliveryDate;
	private TextView txtViewProjectRemarks;
	private ListView projectOrderDetailListView;
	private Button btnProjectSync;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_project_order_page);
		initialize();

		receivedOrder = (Order) getIntent().getExtras().get("order");
		txtViewProjectCustomerName.setText(receivedOrder.getCustomerName());
		txtViewProjectDistributorName.setText(receivedOrder.getDistributorName());
		txtViewProjectVehicleNo.setText(receivedOrder.getVehicle());
		txtViewProjectDriverName.setText(receivedOrder.getDriver());
		txtViewProjectDriverNIC.setText(receivedOrder.getDriverNIC());
		txtViewProjectDeliveryDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(receivedOrder.getDeliveryDate())));
		txtViewProjectRemarks.setText(receivedOrder.getRemarks());

		ArrayAdapter<OrderDetail> orderDetails = new ArrayAdapter<OrderDetail>(this, android.R.layout.simple_list_item_1, receivedOrder.getOrderDetails()) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				OrderedItemViewHolder orderedItemViewHolder;
				if (convertView == null) {
					LayoutInflater inflater = (LayoutInflater) ViewProjectSalesOrderActivity.this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
					convertView = inflater.inflate(R.layout.ordered_item, null);
					orderedItemViewHolder = new OrderedItemViewHolder();
					orderedItemViewHolder.txtItemDescription = (TextView) convertView.findViewById(R.id.txtItemDescription);
					orderedItemViewHolder.txtQuantity = (TextView) convertView.findViewById(R.id.txtQuantity);
					orderedItemViewHolder.txtEachDiscount = (TextView) convertView.findViewById(R.id.txtEachDiscount);
					convertView.setTag(orderedItemViewHolder);
				} else {
					orderedItemViewHolder = (OrderedItemViewHolder) convertView.getTag();
				}
				OrderDetail orderDetail = receivedOrder.getOrderDetails().get(position);
				orderedItemViewHolder.txtItemDescription.setText(orderDetail.getItemDescription());
				orderedItemViewHolder.txtQuantity.setText("Quantity " + orderDetail.getQuantity());
				orderedItemViewHolder.txtEachDiscount.setText("Discount " + orderDetail.getEachDiscount());
				return convertView;
			}
		};
		projectOrderDetailListView.setAdapter(orderDetails);
	}

	@Override
	public void onBackPressed() {
		Intent madeProjectSalesOrderActivity = new Intent(this, MadeProjectSalesOrderActivity.class);
		startActivity(madeProjectSalesOrderActivity);
		finish();
	}

	// <editor-fold defaultstate="collapsed" desc="Initialize">
	private void initialize() {
		txtViewProjectCustomerName = (TextView) findViewById(R.id.txtViewProjectCustomerName);
		txtViewProjectDistributorName = (TextView) findViewById(R.id.txtViewProjectDistributorName);
		txtViewProjectVehicleNo = (TextView) findViewById(R.id.txtViewProjectVehicleNo);
		txtViewProjectDriverName = (TextView) findViewById(R.id.txtViewProjectDriverName);
		txtViewProjectDriverNIC = (TextView) findViewById(R.id.txtViewProjectDriverNIC);
		txtViewProjectDeliveryDate = (TextView) findViewById(R.id.txtViewProjectDeliveryDate);
		txtViewProjectRemarks = (TextView) findViewById(R.id.txtViewProjectRemarks);
		projectOrderDetailListView = (ListView) findViewById(R.id.projectOrderDetailListView);
		btnProjectSync = (Button) findViewById(R.id.btnProjectSync);

		projectOrderDetailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				orderDetailClicked(adapterView, view, position, id);
			}
		});

		btnProjectSync.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnProjectSyncClicked(view);
			}
		});
	}

	private void orderDetailClicked(AdapterView<?> adapterView, View view, int position, long id) {
		Intent viewItemDetailActivity = new Intent(this, ViewItemDetailActivity.class);
		OrderDetail orderDetail = receivedOrder.getOrderDetails().get(position);
		viewItemDetailActivity.putExtra("orderDetail", orderDetail);
		viewItemDetailActivity.putExtra("order", receivedOrder);
		viewItemDetailActivity.putExtra("referer", getClass());
		startActivity(viewItemDetailActivity);
		finish();
	}
	// </editor-fold>

	private void btnProjectSyncClicked(View view) {
		OrderController.syncOrder(this, receivedOrder);
	}

	private static class OrderedItemViewHolder {

		TextView txtItemDescription;
		TextView txtQuantity;
		TextView txtEachDiscount;
	}
}
