/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2014, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Jun 19, 2014, 4:23:10 PM
 */
package com.xfinity.ceylon_steel.activity.payment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.xfinity.ceylon_steel.R;
import com.xfinity.ceylon_steel.activity.HomeActivity;
import com.xfinity.ceylon_steel.controller.BankController;
import com.xfinity.ceylon_steel.controller.OutletController;
import com.xfinity.ceylon_steel.model.Bank;
import com.xfinity.ceylon_steel.model.Invoice;
import com.xfinity.ceylon_steel.model.Outlet;
import com.xfinity.ceylon_steel.model.Payment;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class PaymentActivity extends Activity {
	private AutoCompleteTextView outletAuto;
	private ExpandableListView invoiceList;
	private ArrayList<Outlet> outlets;
	private Outlet selectedOutlet;
	private Button btnOk;
	private Button btnCancel;
	private BaseExpandableListAdapter adapter;
	private NumberFormat currencyFormat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pending_invoices_page);
		initialize();

		currencyFormat = NumberFormat.getNumberInstance();
		currencyFormat.setMinimumFractionDigits(2);
		currencyFormat.setMaximumFractionDigits(2);
		currencyFormat.setGroupingUsed(true);

		outlets = OutletController.getOutlets(PaymentActivity.this);
		outletAuto.setAdapter(new ArrayAdapter<Outlet>(this, android.R.layout.simple_dropdown_item_1line, outlets));

		outletAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				outletAutoOnItemClicked(adapterView, view, i, l);
			}
		});

		adapter = new MyExpandableListAdapter();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.payments_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		OutletController.syncPayments(PaymentActivity.this);
		return true;
	}

	private void outletAutoOnItemClicked(AdapterView<?> adapterView, View view, int position, long id) {
		selectedOutlet = (Outlet) adapterView.getAdapter().getItem(position);
		adapter.notifyDataSetChanged();
		invoiceList.setAdapter(adapter);
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
		btnOk = (Button) findViewById(R.id.btnOk);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				btnOkClicked(view);
			}
		});
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				btnCancelClicked(view);
			}
		});
	}
	// </editor-fold>

	private void btnCancelClicked(View view) {
		Intent homeActivity = new Intent(PaymentActivity.this, HomeActivity.class);
		startActivity(homeActivity);
		finish();
	}

	private void btnOkClicked(View view) {
		boolean response = OutletController.saveInvoicePayments(outlets, PaymentActivity.this);
		if (response) {
			Intent homeActivity = new Intent(PaymentActivity.this, HomeActivity.class);
			startActivity(homeActivity);
			finish();
			Toast.makeText(this, "Payments Saved Successfully", Toast.LENGTH_LONG).show();
		} else {
			AlertDialog.Builder dialog = new AlertDialog.Builder(PaymentActivity.this);
			dialog.setTitle(R.string.message_title);
			dialog.setMessage("Unable to place Payments");
			dialog.setPositiveButton("Ok", null);
			dialog.show();
		}
	}

	private long calculateAging(String bankingDateString) {

		Date today = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date bankingDate = null;
		try {
			if (!bankingDateString.isEmpty()) {
				bankingDate = simpleDateFormat.parse(bankingDateString);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (bankingDate == null) {
			return 0;
		}
		long timeDifference = (bankingDate.getTime() - today.getTime()) / AlarmManager.INTERVAL_DAY;
		return (timeDifference > 0) ? timeDifference : 0;

	}

	private static class CategoryViewHolder {
		TextView txtInvoiceNo;
		TextView txtTotal;
		TextView txtDate;
	}

	private class MyExpandableListAdapter extends BaseExpandableListAdapter {
		@Override
		public int getGroupCount() {
			return selectedOutlet.getPendingInvoices(PaymentActivity.this).size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return 1;
		}

		@Override
		public Invoice getGroup(int groupPosition) {
			return selectedOutlet.getPendingInvoices(PaymentActivity.this).get(groupPosition);
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
			categoryViewHolder.txtTotal.setText("Rs " + currencyFormat.format(invoice.getInvoiceAmount()));
			categoryViewHolder.txtDate.setText(invoice.getDate());
			return convertView;
		}

		@Override
		public View getChildView(final int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
			final Invoice invoice = getGroup(groupPosition);
			LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = convertView == null ? inflater.inflate(R.layout.invoice_payment_details_page, viewGroup, false) : convertView;
			LinearLayout donePaymentList = (LinearLayout) convertView.findViewById(R.id.donePaymentList);
			LinearLayout unSyncedPaymentList = (LinearLayout) convertView.findViewById(R.id.madePayments);
			TextView txtPendingAmount = (TextView) convertView.findViewById(R.id.txtPendingAmount);
			donePaymentList.removeAllViews();
			unSyncedPaymentList.removeAllViews();

			for (Payment payment : invoice.getPayments()) {
				if (payment.getPaymentMethod().equalsIgnoreCase(Payment.CASH_PAYMENT)) {
					View cashPaymentDetail = inflater.inflate(R.layout.cash_payment_details_page, viewGroup, false);
					TextView txtPaidValue = (TextView) cashPaymentDetail.findViewById(R.id.txtPaidValue);
					TextView txtPaidDate = (TextView) cashPaymentDetail.findViewById(R.id.txtPaidDate);
					txtPaidDate.setText(payment.getPaidDate());
					txtPaidValue.setText("Rs " + currencyFormat.format(payment.getPaidValue()));
					cashPaymentDetail.setBackgroundColor((payment.getStatus() == Payment.REJECTED_PAYMENT) ? Color.parseColor("#ffcccc") : Color.parseColor("#ccffcc"));
					donePaymentList.addView(cashPaymentDetail);
				} else {
					View chequePaymentDetail = inflater.inflate(R.layout.cheque_payment_detail_page, viewGroup, false);
					TextView txtPaidValue = (TextView) chequePaymentDetail.findViewById(R.id.txtPaidValue);
					TextView txtPaidDate = (TextView) chequePaymentDetail.findViewById(R.id.txtPaidDate);
					TextView txtChequeNo = (TextView) chequePaymentDetail.findViewById(R.id.txtChequeNo);
					TextView txtAgingAnalysis = (TextView) chequePaymentDetail.findViewById(R.id.txtAgingAnalysis);
					txtPaidDate.setText(payment.getPaidDate());
					txtChequeNo.setText(payment.getChequeNo());
					txtPaidValue.setText("Rs " + currencyFormat.format(payment.getPaidValue()));
					txtAgingAnalysis.setText(calculateAging(payment.getRealizationDate()) + " day(s)");
					chequePaymentDetail.setBackgroundColor((payment.getStatus() == Payment.REJECTED_PAYMENT) ? Color.parseColor("#ffcccc") : Color.parseColor("#ccffcc"));
					donePaymentList.addView(chequePaymentDetail);
				}
			}

			if (invoice.getPendingPayments() != null) {
				for (Payment payment : invoice.getPendingPayments()) {
					if (payment.getPaymentMethod().equalsIgnoreCase(Payment.CASH_PAYMENT)) {
						View cashPaymentDetail = inflater.inflate(R.layout.cash_payment_details_page, viewGroup, false);
						TextView txtPaidValue = (TextView) cashPaymentDetail.findViewById(R.id.txtPaidValue);
						TextView txtPaidDate = (TextView) cashPaymentDetail.findViewById(R.id.txtPaidDate);
						txtPaidDate.setText(payment.getPaidDate());
						txtPaidValue.setText("Rs " + currencyFormat.format(payment.getPaidValue()));
						cashPaymentDetail.setBackgroundColor(Color.parseColor("#ffffcc"));
						donePaymentList.addView(cashPaymentDetail);
					} else {
						View chequePaymentDetail = inflater.inflate(R.layout.cheque_payment_detail_page, viewGroup, false);
						TextView txtPaidValue = (TextView) chequePaymentDetail.findViewById(R.id.txtPaidValue);
						TextView txtPaidDate = (TextView) chequePaymentDetail.findViewById(R.id.txtPaidDate);
						TextView txtChequeNo = (TextView) chequePaymentDetail.findViewById(R.id.txtChequeNo);
						TextView txtAgingAnalysis = (TextView) chequePaymentDetail.findViewById(R.id.txtAgingAnalysis);
						txtPaidDate.setText(payment.getPaidDate());
						txtChequeNo.setText(payment.getChequeNo());
						txtPaidValue.setText("Rs " + currencyFormat.format(payment.getPaidValue()));
						txtAgingAnalysis.setText(calculateAging(payment.getRealizationDate()) + " day(s)");
						chequePaymentDetail.setBackgroundColor(Color.parseColor("#ffffcc"));
						donePaymentList.addView(chequePaymentDetail);
					}
				}
			}

			if (invoice.getUnSyncedPayments() != null) {
				for (Payment payment : invoice.getUnSyncedPayments()) {
					if (payment.getPaymentMethod().equalsIgnoreCase(Payment.CASH_PAYMENT)) {
						View cashPaymentDetail = inflater.inflate(R.layout.cash_payment_details_page, viewGroup, false);
						TextView txtPaidValue = (TextView) cashPaymentDetail.findViewById(R.id.txtPaidValue);
						TextView txtPaidDate = (TextView) cashPaymentDetail.findViewById(R.id.txtPaidDate);
						txtPaidDate.setText(payment.getPaidDate());
						txtPaidValue.setText("Rs " + currencyFormat.format(payment.getPaidValue()));
						cashPaymentDetail.setBackgroundColor(Color.parseColor("#ccccff"));
						unSyncedPaymentList.addView(cashPaymentDetail);
					} else {
						View chequePaymentDetail = inflater.inflate(R.layout.cheque_payment_detail_page, viewGroup, false);
						TextView txtPaidValue = (TextView) chequePaymentDetail.findViewById(R.id.txtPaidValue);
						TextView txtPaidDate = (TextView) chequePaymentDetail.findViewById(R.id.txtPaidDate);
						TextView txtChequeNo = (TextView) chequePaymentDetail.findViewById(R.id.txtChequeNo);
						TextView txtAgingAnalysis = (TextView) chequePaymentDetail.findViewById(R.id.txtAgingAnalysis);
						txtPaidDate.setText(payment.getPaidDate());
						txtChequeNo.setText(payment.getChequeNo());
						txtPaidValue.setText("Rs " + currencyFormat.format(payment.getPaidValue()));
						txtAgingAnalysis.setText(calculateAging(payment.getRealizationDate()) + " day(s)");
						chequePaymentDetail.setBackgroundColor(Color.parseColor("#ccccff"));
						unSyncedPaymentList.addView(chequePaymentDetail);
					}
				}
			}

			Button btnCash = (Button) convertView.findViewById(R.id.btnCash);
			btnCash.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					final Dialog dialog = new Dialog(PaymentActivity.this);
					dialog.setTitle("Cash Payment");
					dialog.setContentView(R.layout.cash_data_input_dialog_page);
					Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
					Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
					final EditText inputAmount = (EditText) dialog.findViewById(R.id.inputAmount);
					btnOk.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							String amountString = inputAmount.getText().toString();
							Payment payment = new Payment(invoice.getSalesOrderId(), amountString.isEmpty() ? 0 : Double.parseDouble(amountString), new SimpleDateFormat("yyyy-MM-dd").format(new Date()), Payment.FRESH_PAYMENT);
							ArrayList<Payment> unSyncedPayments;
							if ((unSyncedPayments = invoice.getUnSyncedPayments()) == null) {
								invoice.setUnSyncedPayments(unSyncedPayments = new ArrayList<Payment>());
							}
							unSyncedPayments.add(payment);
							adapter.notifyDataSetChanged();
							dialog.dismiss();
						}
					});
					btnCancel.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							dialog.dismiss();
						}
					});
					dialog.show();
				}
			});
			Button btnCheque = (Button) convertView.findViewById(R.id.btnCheque);
			btnCheque.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					final Dialog dialog = new Dialog(PaymentActivity.this);
					dialog.setTitle("Cheque Payment");
					dialog.setContentView(R.layout.cheque_data_input_dialog_page);
					final Spinner bankCombo = (Spinner) dialog.findViewById(R.id.bankCombo);
					ArrayAdapter<Bank> bankAdapter = new ArrayAdapter<Bank>(PaymentActivity.this, android.R.layout.simple_spinner_item, BankController.getBanks());
					bankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					bankCombo.setAdapter(bankAdapter);
					Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
					Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
					final EditText inputAmount = (EditText) dialog.findViewById(R.id.inputAmount);
					final EditText inputChequeNo = (EditText) dialog.findViewById(R.id.inputChequeNo);
					final EditText inputYear = (EditText) dialog.findViewById(R.id.inputYear);
					final EditText inputMonth = (EditText) dialog.findViewById(R.id.inputMonth);
					final EditText inputDate = (EditText) dialog.findViewById(R.id.inputDate);
					btnOk.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							String amountString = inputAmount.getText().toString();
							String realizationDate = inputYear.getText().toString() + "-" + inputMonth.getText().toString() + "-" + inputDate.getText().toString();
							Payment payment = new Payment(invoice.getSalesOrderId(), amountString.isEmpty() ? 0 : Double.parseDouble(amountString), new SimpleDateFormat("yyyy-MM-dd").format(new Date()), bankCombo.getSelectedItem().toString(), inputChequeNo.getText().toString(), realizationDate, Payment.FRESH_PAYMENT);
							ArrayList<Payment> unSyncedPayments;
							if ((unSyncedPayments = invoice.getUnSyncedPayments()) == null) {
								invoice.setUnSyncedPayments(unSyncedPayments = new ArrayList<Payment>());
							}
							unSyncedPayments.add(payment);
							adapter.notifyDataSetChanged();
							dialog.dismiss();
						}
					});
					btnCancel.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							dialog.dismiss();
						}
					});
					dialog.show();
				}
			});
			txtPendingAmount.setText(Double.toString(invoice.getPendingAmount()));
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}
	}
}
