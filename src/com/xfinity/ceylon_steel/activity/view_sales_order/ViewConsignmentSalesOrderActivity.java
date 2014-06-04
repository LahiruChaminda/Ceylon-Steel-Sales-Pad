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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
public class ViewConsignmentSalesOrderActivity extends Activity {

	private Order receivedOrder;

	private TextView txtViewConsignmentOutletName;
	private TextView txtViewConsignmentDistributorName;
	private TextView txtViewConsignmentVehicleNo;
	private TextView txtViewConsignmentDriverName;
	private TextView txtViewConsignmentDriverNIC;
	private TextView txtViewConsignmentDeliveryDate;
	private TextView txtViewConsignmentRemarks;
	private ListView consignmentOrderDetailListView;
	private Button btnConsignmentSync;

	private static class OrderedItemViewHolder {

		TextView txtItemDescription;
		TextView txtQuantity;
		TextView txtEachDiscount;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_consignment_order_page);
		initialize();

		receivedOrder = (Order) getIntent().getExtras().get("order");
		txtViewConsignmentOutletName.setText(receivedOrder.getOutletName());
		txtViewConsignmentDistributorName.setText(receivedOrder.getDistributorName());
		txtViewConsignmentVehicleNo.setText(receivedOrder.getVehicle());
		txtViewConsignmentDriverName.setText(receivedOrder.getDriver());
		txtViewConsignmentDriverNIC.setText(receivedOrder.getDriverNIC());
		txtViewConsignmentDeliveryDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(receivedOrder.getDeliveryDate())));
		txtViewConsignmentRemarks.setText(receivedOrder.getRemarks());

		ArrayAdapter<OrderDetail> orderDetails = new ArrayAdapter<OrderDetail>(this, android.R.layout.simple_list_item_1, receivedOrder.getOrderDetails()) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				OrderedItemViewHolder orderedItemViewHolder;
				if (convertView == null) {
					LayoutInflater inflater = (LayoutInflater) ViewConsignmentSalesOrderActivity.this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
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
		consignmentOrderDetailListView.setAdapter(orderDetails);
	}

	@Override
	public void onBackPressed() {
		Intent madeConsignmentSalesOrderActivity = new Intent(this, MadeConsignmentSalesOrderActivity.class);
		startActivity(madeConsignmentSalesOrderActivity);
		finish();
	}

	// <editor-fold defaultstate="collapsed" desc="Initialize">
	private void initialize() {
		txtViewConsignmentOutletName = (TextView) findViewById(R.id.txtViewConsignmentOutletName);
		txtViewConsignmentDistributorName = (TextView) findViewById(R.id.txtViewConsignmentDistributorName);
		txtViewConsignmentVehicleNo = (TextView) findViewById(R.id.txtViewConsignmentVehicleNo);
		txtViewConsignmentDriverName = (TextView) findViewById(R.id.txtViewConsignmentDriverName);
		txtViewConsignmentDriverNIC = (TextView) findViewById(R.id.txtViewConsignmentDriverNIC);
		txtViewConsignmentDeliveryDate = (TextView) findViewById(R.id.txtViewConsignmentDeliveryDate);
		txtViewConsignmentRemarks = (TextView) findViewById(R.id.txtViewConsignmentRemarks);
		consignmentOrderDetailListView = (ListView) findViewById(R.id.consignmentOrderDetailListView);
		btnConsignmentSync = (Button) findViewById(R.id.btnConsignmentSync);
		consignmentOrderDetailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				orderDetailClicked(adapterView, view, position, id);
			}
		});
		btnConsignmentSync.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnConsignmentSyncClicked(view);
			}
		});
	}
	// </editor-fold>

	private void orderDetailClicked(AdapterView<?> adapterView, View view, int position, long id) {
		Intent viewItemDetailActivity = new Intent(this, ViewItemDetailActivity.class);
		OrderDetail orderDetail = receivedOrder.getOrderDetails().get(position);
		viewItemDetailActivity.putExtra("orderDetail", orderDetail);
		viewItemDetailActivity.putExtra("order", receivedOrder);
		viewItemDetailActivity.putExtra("referer", getClass());
		startActivity(viewItemDetailActivity);
		finish();
	}

	private void btnConsignmentSyncClicked(View view) {
		OrderController.syncOrder(this, receivedOrder);
	}
}
