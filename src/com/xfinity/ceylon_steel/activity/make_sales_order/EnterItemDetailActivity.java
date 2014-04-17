/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 18, 2014, 12:23:28 AM
 */
package com.xfinity.ceylon_steel.activity.make_sales_order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.xfinity.ceylon_steel.R;
import com.xfinity.ceylon_steel.model.Item;
import com.xfinity.ceylon_steel.model.OrderDetail;
import java.text.NumberFormat;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class EnterItemDetailActivity extends Activity {

	private TextView enterItem;
	private TextView enterItemUnitPrice;
	private EditText enterTxtQuantity;
	private EditText enterTxtEachDiscount;
	private TextView enterItemAmount;
	private Button btnEnterItem;
	private Button btnCancelItem;
	private final NumberFormat currencyFormat = NumberFormat.getInstance();
	private Item item;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enter_item_detail_page);

		currencyFormat.setMinimumFractionDigits(2);
		currencyFormat.setMaximumFractionDigits(2);
		currencyFormat.setGroupingUsed(true);

		initialize();
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		setResult(RESULT_CANCELED, intent);
		finish();
	}

	// <editor-fold defaultstate="collapsed" desc="Initialize">
	private void initialize() {
		this.item = (Item) getIntent().getExtras().get("item");

		enterItem = (TextView) findViewById(R.id.enterItem);
		enterItemUnitPrice = (TextView) findViewById(R.id.enterItemUnitPrice);
		enterTxtQuantity = (EditText) findViewById(R.id.enterTxtQuantity);
		enterTxtEachDiscount = (EditText) findViewById(R.id.enterTxtEachDiscount);
		enterItemAmount = (TextView) findViewById(R.id.enterItemAmount);
		btnEnterItem = (Button) findViewById(R.id.btnEnterItem);
		btnCancelItem = (Button) findViewById(R.id.btnCancelItem);
		enterItem.setText(item.getDescription());
		enterItemUnitPrice.setText("Rs " + currencyFormat.format(item.getPrice()));
		enterTxtQuantity.addTextChangedListener(new TextWatcher() {

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void afterTextChanged(Editable editable) {
				enterTxtQuantityAfterTextChanged(editable);
			}
		});

		enterTxtEachDiscount.addTextChangedListener(new TextWatcher() {

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void afterTextChanged(Editable editable) {
				enterTxtEachDiscountAfterTextChanged(editable);
			}
		});

		btnEnterItem.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnEnterItemClicked(view);
			}
		});

		btnCancelItem.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnCancelItemClicked(view);
			}
		});
	}
	// </editor-fold>

	private void btnEnterItemClicked(View view) {
		double unitPrice = item.getPrice();
		double eachDiscount = Double.parseDouble(enterTxtEachDiscount.getText().toString().isEmpty() ? "0" : enterTxtEachDiscount.getText().toString());
		double quantity = Double.parseDouble(enterTxtQuantity.getText().toString().isEmpty() ? "0" : enterTxtQuantity.getText().toString());
		OrderDetail orderDetail = new OrderDetail(item.getItemId(), quantity, unitPrice, eachDiscount);
		Intent intent = new Intent();
		intent.putExtra("orderDetail", orderDetail);
		setResult(RESULT_OK, intent);
		finish();
	}

	private void btnCancelItemClicked(View view) {
		Intent intent = new Intent();
		setResult(RESULT_CANCELED, intent);
		finish();
	}

	private void enterTxtQuantityAfterTextChanged(Editable editable) {
		double unitPrice = item.getPrice();
		double eachDiscount = Double.parseDouble(enterTxtEachDiscount.getText().toString().isEmpty() ? "0" : enterTxtEachDiscount.getText().toString());
		double quantity = Double.parseDouble(enterTxtQuantity.getText().toString().isEmpty() ? "0" : enterTxtQuantity.getText().toString());
		enterItemAmount.setText("Rs " + currencyFormat.format((unitPrice - eachDiscount) * quantity));
	}

	private void enterTxtEachDiscountAfterTextChanged(Editable editable) {
		double unitPrice = item.getPrice();
		double eachDiscount = Double.parseDouble(enterTxtEachDiscount.getText().toString().isEmpty() ? "0" : enterTxtEachDiscount.getText().toString());
		double quantity = Double.parseDouble(enterTxtQuantity.getText().toString().isEmpty() ? "0" : enterTxtQuantity.getText().toString());
		enterItemAmount.setText("Rs " + currencyFormat.format((unitPrice - eachDiscount) * quantity));
	}
}
