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
public class ViewDirectSalesOrderActivity extends Activity {

	private Order receivedOrder;

	private TextView txtViewDirectOutletName;
	private TextView txtViewDirectVehicleNo;
	private TextView txtViewDirectDriverName;
	private TextView txtViewDirectDriverNIC;
	private TextView txtViewDirectDeliveryDate;
	private TextView txtViewDirectRemarks;
	private TextView txtViewTotal;
	private ListView directOrderDetailListView;
	private Button btnDirectSync;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_direct_order_page);
		initialize();

		receivedOrder = (Order) getIntent().getExtras().get("order");
		txtViewDirectOutletName.setText(receivedOrder.getOutletName());
		txtViewDirectVehicleNo.setText(receivedOrder.getVehicle());
		txtViewDirectDriverName.setText(receivedOrder.getDriver());
		txtViewDirectDriverNIC.setText(receivedOrder.getDriverNIC());
		txtViewDirectDeliveryDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(receivedOrder.getDeliveryDate())));
		txtViewDirectRemarks.setText(receivedOrder.getRemarks());
		txtViewTotal.setText(String.valueOf(receivedOrder.getTotal()));

		ArrayAdapter<OrderDetail> orderDetails = new ArrayAdapter<OrderDetail>(this, android.R.layout.simple_list_item_1, receivedOrder.getOrderDetails()) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				OrderedItemViewHolder orderedItemViewHolder;
				if (convertView == null) {
					LayoutInflater inflater = (LayoutInflater) ViewDirectSalesOrderActivity.this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
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
		directOrderDetailListView.setAdapter(orderDetails);
	}

	@Override
	public void onBackPressed() {
		Intent madeDirectSalesOrderActivity = new Intent(this, MadeDirectSalesOrderActivity.class);
		startActivity(madeDirectSalesOrderActivity);
		finish();
	}

	// <editor-fold defaultstate="collapsed" desc="Initialize">
	private void initialize() {
		txtViewDirectOutletName = (TextView) findViewById(R.id.txtViewDirectOutletName);
		txtViewDirectVehicleNo = (TextView) findViewById(R.id.txtViewDirectVehicleNo);
		txtViewDirectDriverName = (TextView) findViewById(R.id.txtViewDirectDriverName);
		txtViewDirectDriverNIC = (TextView) findViewById(R.id.txtViewDirectDriverNIC);
		txtViewDirectDeliveryDate = (TextView) findViewById(R.id.txtViewDirectDeliveryDate);
		txtViewDirectRemarks = (TextView) findViewById(R.id.txtViewDirectRemarks);
		txtViewTotal = (TextView) findViewById(R.id.txtViewTotal);
		directOrderDetailListView = (ListView) findViewById(R.id.directOrderDetailListView);
		btnDirectSync = (Button) findViewById(R.id.btnDirectSync);

		directOrderDetailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				orderDetailClicked(adapterView, view, position, id);
			}
		});

		btnDirectSync.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnDirectSyncClicked(view);
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

	private void btnDirectSyncClicked(View view) {
		if (receivedOrder.isSynced()) {
			Toast.makeText(ViewDirectSalesOrderActivity.this, "Already Synced", Toast.LENGTH_LONG).show();
		} else {
			OrderController.syncOrder(this, receivedOrder);
		}
	}

	private static class OrderedItemViewHolder {

		TextView txtItemDescription;
		TextView txtQuantity;
		TextView txtEachDiscount;
	}
}
