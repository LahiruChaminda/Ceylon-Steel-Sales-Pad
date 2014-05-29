/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Xfinity and/or its affiliates. All rights reserved.
 * Created on : Mar 10, 2014, 2:59:35 PM
 */
package com.xfinity.ceylon_steel.activity.make_sales_order;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import com.xfinity.ceylon_steel.R;
import com.xfinity.ceylon_steel.activity.HomeActivity;
import com.xfinity.ceylon_steel.controller.CategoryController;
import com.xfinity.ceylon_steel.controller.OrderController;
import com.xfinity.ceylon_steel.controller.UserController;
import com.xfinity.ceylon_steel.model.Category;
import com.xfinity.ceylon_steel.model.Item;
import com.xfinity.ceylon_steel.model.Order;
import com.xfinity.ceylon_steel.model.OrderDetail;
import java.util.ArrayList;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class SelectCategoryActivity extends Activity {

	private Button btnFinishMakeBill;
	private Button btnQuickSync;
	private Button btnReturnToHome;
	private ExpandableListView expandableListView;
	private ArrayList<Category> categories;
	private Order order;

	private final ArrayList<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
	private final ArrayList<Integer> itemIds = new ArrayList<Integer>();
	private int categoryPosition;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_category_page);
		order = (Order) getIntent().getExtras().get("order");
		categories = CategoryController.getCategories(this);
		itemIds.clear();
		initialize();
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.message_title);
		if (orderDetails.size() > 0) {
			builder.setMessage("You are about to cancel this order\nAre you sure you want to continue?");
			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
					SelectCategoryActivity.super.onBackPressed();
				}
			});
			builder.setNegativeButton("No", null);
			builder.show();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			OrderDetail orderDetail = (OrderDetail) data.getSerializableExtra("orderDetail");
			if (orderDetail.getQuantity() > 0) {
				orderDetails.add(orderDetail);
				if (order.getOrderType().equalsIgnoreCase(Order.DIRECT)) {
					order.setTotal(order.getTotal() + (orderDetail.getUnitPrice() * orderDetail.getQuantity()));
				} else {
					order.setTotal(order.getTotal() + ((orderDetail.getUnitPrice() - orderDetail.getEachDiscount()) * orderDetail.getQuantity()));
				}
			}
		}
		itemIds.clear();
		for (OrderDetail orderDetail : orderDetails) {
			itemIds.add(orderDetail.getItemId());
		}
		expandableListView.collapseGroup(categoryPosition);
		expandableListView.expandGroup(categoryPosition);
	}

	private class ChildViewHolder {

		TextView childViewHolder;
	}

	private class GroupViewHolder {

		TextView categoryTextView;
	}

	// <editor-fold defaultstate="collapsed" desc="Initialize">
	private void initialize() {
		expandableListView = (ExpandableListView) findViewById(R.id.categoryExpandableListView);
		ExpandableListAdapter expandableListAdapter = new BaseExpandableListAdapter() {

			public int getGroupCount() {
				return categories.size();
			}

			public int getChildrenCount(int categoryPosition) {
				return categories.get(categoryPosition).getItems().size();
			}

			public Object getGroup(int categoryPosition) {
				return categories.get(categoryPosition);
			}

			public Object getChild(int categoryPosition, int itemPosition) {
				return categories.get(categoryPosition).getItems().get(itemPosition);
			}

			public long getGroupId(int categoryPosition) {
				return categoryPosition;
			}

			public long getChildId(int categoryPosition, int itemPosition) {
				return itemPosition;
			}

			public boolean hasStableIds() {
				return false;
			}

			public View getGroupView(int categoryPosition, boolean isExpanded, View convertView, ViewGroup parent) {
				GroupViewHolder groupViewHolder;
				TextView categoryTextView;
				if (convertView == null) {
					LayoutInflater mInflater = (LayoutInflater) SelectCategoryActivity.this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
					convertView = mInflater.inflate(R.layout.category_list, null);
					categoryTextView = (TextView) convertView.findViewById(R.id.category);
					groupViewHolder = new GroupViewHolder();
					groupViewHolder.categoryTextView = categoryTextView;
					convertView.setTag(groupViewHolder);
				} else {
					groupViewHolder = (GroupViewHolder) convertView.getTag();
					categoryTextView = groupViewHolder.categoryTextView;
				}
				categoryTextView.setText(categories.get(categoryPosition).toString());
				return convertView;
			}

			public View getChildView(int categoryPosition, int itemPosition, boolean isLastChild, View convertView, ViewGroup parent) {
				ChildViewHolder childViewHolder;
				TextView itemTextView;
				if (convertView == null) {
					LayoutInflater mInflater = (LayoutInflater) SelectCategoryActivity.this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
					convertView = mInflater.inflate(R.layout.item_list, null);
					itemTextView = (TextView) convertView.findViewById(R.id.item);
					childViewHolder = new ChildViewHolder();
					childViewHolder.childViewHolder = itemTextView;
					convertView.setTag(childViewHolder);
				} else {
					childViewHolder = (ChildViewHolder) convertView.getTag();
					itemTextView = childViewHolder.childViewHolder;
				}
				itemTextView.setText(categories.get(categoryPosition).getItems().get(itemPosition).toString());
				if (itemIds.contains(categories.get(categoryPosition).getItems().get(itemPosition).getItemId())) {
					itemTextView.setBackgroundColor(Color.rgb(79, 130, 180));
					itemTextView.setTextColor(Color.WHITE);
				} else {
					itemTextView.setBackgroundColor(Color.TRANSPARENT);
					itemTextView.setTextColor(Color.BLACK);
				}
				return convertView;
			}

			public boolean isChildSelectable(int arg0, int arg1) {
				return true;
			}
		};
		expandableListView.setAdapter(expandableListAdapter);
		expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {
				return onChildClicked(parent, view, groupPosition, childPosition, id);
			}
		});
		btnFinishMakeBill = (Button) findViewById(R.id.btnFinishMakeBill);
		btnFinishMakeBill.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnFinishMakeBillClicked(view);
			}
		});
		btnQuickSync = (Button) findViewById(R.id.btnQuickSync);
		btnQuickSync.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnQuickSyncClicked(view);
			}
		});
		btnReturnToHome = (Button) findViewById(R.id.btnReturnToHome);
		btnReturnToHome.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnReturnToHomeClicked(view);
			}
		});
	}
	// </editor-fold>

	private boolean onChildClicked(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {
		Item item = categories.get(groupPosition).getItems().get(childPosition);
		Intent enterItemDetailActivity = new Intent(this, EnterItemDetailActivity.class);
		categoryPosition = groupPosition;
		enterItemDetailActivity.putExtra("item", item);
		startActivityForResult(enterItemDetailActivity, 0);
		return true;
	}

	private void btnFinishMakeBillClicked(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.message_title);
		if (orderDetails.size() > 0) {
			order.setOrderDetails(orderDetails);
			long placeOrder = -1;
			if (order.getOrderType().equalsIgnoreCase(Order.CONSIGNMENT)) {
				placeOrder = OrderController.placeConsignmentOrder(this, order);
			} else if (order.getOrderType().equalsIgnoreCase(Order.DIRECT)) {
				placeOrder = OrderController.placeDirectOrder(this, order);
			} else if (order.getOrderType().equalsIgnoreCase(Order.PROJECT)) {
				placeOrder = OrderController.placeProjectOrder(this, order);
			} else if (order.getOrderType().equalsIgnoreCase(Order.DIRECT_PROJECT)) {
				placeOrder = OrderController.placeProjectOrder(this, order);
			}

			if (placeOrder != -1) {
				orderDetails.clear();
				builder.setMessage("Order Placed Successfully");
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						Intent makeSalesOrderActivity = new Intent(SelectCategoryActivity.this, MakeSalesOrderActivity.class);
						startActivity(makeSalesOrderActivity);
						finish();
					}
				});
			} else {
				builder.setMessage("Unable to make order");
				builder.setPositiveButton("Ok", null);
			}
		} else {
			builder.setMessage("In order to proceed, You need to select at least one item");
			builder.setPositiveButton("Ok", null);
		}
		builder.show();
	}

	private void btnQuickSyncClicked(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.message_title);
		if (orderDetails.size() > 0) {
			order.setOrderDetails((ArrayList<OrderDetail>) orderDetails.clone());
			long placeOrder = -1;
			if (order.getOrderType().equalsIgnoreCase(Order.CONSIGNMENT)) {
				placeOrder = OrderController.placeConsignmentOrder(this, order);
			} else if (order.getOrderType().equalsIgnoreCase(Order.DIRECT)) {
				placeOrder = OrderController.placeDirectOrder(this, order);
			} else if (order.getOrderType().equalsIgnoreCase(Order.PROJECT)) {
				placeOrder = OrderController.placeProjectOrder(this, order);
			} else if (order.getOrderType().equalsIgnoreCase(Order.DIRECT_PROJECT)) {
				placeOrder = OrderController.placeProjectOrder(this, order);
			}
			order.setOrderId(placeOrder);
			order.setRepId(UserController.getAuthorizedUser(this).getUserId());
			OrderController.syncOrder(this, order);
			if (placeOrder != -1) {
				orderDetails.clear();
			} else {
				builder.setMessage("Unable to make order");
				builder.setPositiveButton("Ok", null);
				builder.show();
			}
		} else {
			builder.setMessage("In order to proceed, You need to select at least one item");
			builder.setPositiveButton("Ok", null);
			builder.show();
		}
	}

	private void btnReturnToHomeClicked(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.message_title);
		if (orderDetails.size() > 0) {
			builder.setMessage("You are about to cancel this order\nAre you sure you want to continue?");
			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
					Intent homeActivity = new Intent(SelectCategoryActivity.this, HomeActivity.class);
					startActivity(homeActivity);
					finish();
				}
			});
			builder.setNegativeButton("No", null);
			builder.show();
		} else {
			Intent homeActivity = new Intent(SelectCategoryActivity.this, HomeActivity.class);
			startActivity(homeActivity);
			finish();
		}
	}
}
