/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2014, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Jun 19, 2014, 4:23:10 PM
 */
package com.xfinity.ceylon_steel.activity.payment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.xfinity.ceylon_steel.R;
import com.xfinity.ceylon_steel.activity.HomeActivity;
import com.xfinity.ceylon_steel.controller.OutletController;
import com.xfinity.ceylon_steel.model.Invoice;
import com.xfinity.ceylon_steel.model.Outlet;
import com.xfinity.ceylon_steel.model.Payment;

import java.util.ArrayList;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class PaymentActivity extends Activity {
	private AutoCompleteTextView outletAuto;
	private ExpandableListView invoiceList;
	private LinearLayout paymentList;
	private Button btnAddCashPayment;
	private Button btnAddChequePayment;

	private ArrayList<Outlet> outlets;

	private Outlet selectedOutlet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pending_invoices_page);
		initialize();

		outlets = OutletController.getOutletsWithInvoices(PaymentActivity.this);
		outletAuto.setAdapter(new ArrayAdapter<Outlet>(this, android.R.layout.simple_dropdown_item_1line, outlets));

		outletAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				selectedOutlet = (Outlet) adapterView.getAdapter().getItem(i);
				invoiceList.setAdapter(new BaseExpandableListAdapter() {
					@Override
					public int getGroupCount() {
						return selectedOutlet.getPendingInvoices().size();
					}

					@Override
					public int getChildrenCount(int groupPosition) {
						return 1;
					}

					@Override
					public Invoice getGroup(int groupPosition) {
						return selectedOutlet.getPendingInvoices().get(groupPosition);
					}

					@Override
					public Object getChild(int groupPosition, int childPosition) {
						return null;
					}

					@Override
					public long getGroupId(int groupPosition) {
						return groupPosition;
					}

					@Override
					public long getChildId(int groupPosition, int childPosition) {
						return childPosition;
					}

					@Override
					public boolean hasStableIds() {
						return false;
					}

					@Override
					public View getGroupView(int groupPosition, boolean b, View convertView, ViewGroup viewGroup) {
						CategoryViewHolder categoryViewHolder;
						if (convertView == null) {
							LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
							convertView = inflater.inflate(R.layout.invoice_item, viewGroup, false);
							categoryViewHolder = new CategoryViewHolder();
							categoryViewHolder.txtInvoiceNo = (TextView) convertView.findViewById(R.id.txtInvoiceNo);
							categoryViewHolder.txtTotal = (TextView) convertView.findViewById(R.id.txtTotal);
							categoryViewHolder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
							convertView.setTag(categoryViewHolder);
						} else {
							categoryViewHolder = (CategoryViewHolder) convertView.getTag();
						}
						Invoice invoice = getGroup(groupPosition);
						categoryViewHolder.txtInvoiceNo.setText(invoice.getDistributorCode());
						categoryViewHolder.txtTotal.setText("10000.00");
						categoryViewHolder.txtDate.setText(invoice.getDate());
						return convertView;
					}

					@Override
					public View getChildView(int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
						ChildViewHolder childViewHolder;
						LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
						if (convertView == null) {
							convertView = inflater.inflate(R.layout.invoice_payment_details_page, viewGroup, false);
							childViewHolder = new ChildViewHolder();
							childViewHolder.donePaymentList = (LinearLayout) convertView.findViewById(R.id.donePaymentList);
							childViewHolder.txtPendingAmount = (TextView) convertView.findViewById(R.id.txtPendingAmount);
							childViewHolder.inputNowPaying = (EditText) convertView.findViewById(R.id.inputNowPaying);
							convertView.setTag(childViewHolder);
						} else {
							childViewHolder = (ChildViewHolder) convertView.getTag();
						}
						Invoice invoice = getGroup(groupPosition);
						for (Payment payment : invoice.getPayments()) {

							if (payment.getPaymentMethod().equalsIgnoreCase(Payment.CASH_PAYMENT)) {
								View cashPaymentDetail = inflater.inflate(R.layout.cash_payment_details_page, viewGroup, false);
								TextView txtPaidValue = (TextView) cashPaymentDetail.findViewById(R.id.txtPaidValue);
								TextView txtPaidDate = (TextView) cashPaymentDetail.findViewById(R.id.txtPaidDate);
								txtPaidDate.setText(payment.getPaidDate());
								txtPaidValue.setText(Double.toString(payment.getPaidValue()));
								childViewHolder.donePaymentList.addView(cashPaymentDetail);
							} else {
								View chequePaymentDetail = inflater.inflate(R.layout.cheque_payment_detail_page, viewGroup, false);
								TextView txtPaidValue = (TextView) chequePaymentDetail.findViewById(R.id.txtPaidValue);
								TextView txtPaidDate = (TextView) chequePaymentDetail.findViewById(R.id.txtPaidDate);
								TextView txtChequeNo = (TextView) chequePaymentDetail.findViewById(R.id.txtChequeNo);
								txtPaidDate.setText(payment.getPaidDate());
								txtChequeNo.setText(payment.getChequeNo());
								txtPaidValue.setText(Double.toString(payment.getPaidValue()));
								childViewHolder.donePaymentList.addView(chequePaymentDetail);
							}
						}
						childViewHolder.txtPendingAmount.setText(Double.toString(invoice.getPendingAmount()));
						return convertView;
					}

					@Override
					public boolean isChildSelectable(int groupPosition, int childPosition) {
						return false;
					}
				});
			}
		});
	}

	@Override
	public void onBackPressed() {
		Intent homeActivity = new Intent(this, HomeActivity.class);
		startActivity(homeActivity);
		finish();
	}

	// <editor-fold defaultstate="collapsed" desc="Initialize">
	private void initialize() {
		outletAuto = (AutoCompleteTextView) findViewById(R.id.outletAuto);
		invoiceList = (ExpandableListView) findViewById(R.id.invoiceList);

		outletAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				outletAutoItemClicked(adapterView, view, position, id);
			}
		});
/*
		btnAddCashPayment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				btnAddCashPaymentClicked(view);
			}
		});
		btnAddCashPayment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				btnAddChequePaymentClicked(view);
			}
		});*/
	}

	private void outletAutoItemClicked(AdapterView<?> adapterView, View view, int position, long id) {
		Outlet outlet = (Outlet) outletAuto.getAdapter().getItem(position);
	}

	private void btnAddChequePaymentClicked(View view) {

	}
	// </editor-fold>

	private void btnAddCashPaymentClicked(View view) {

	}

	private static class CategoryViewHolder {
		TextView txtInvoiceNo;
		TextView txtTotal;
		TextView txtDate;
	}

	private static class ChildViewHolder {
		LinearLayout donePaymentList;
		TextView txtPendingAmount;
		EditText inputNowPaying;
	}
}
