/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2013, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Mar 8, 2014, 7:25:03 PM
 */
package com.xfinity.ceylon_steel.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.xfinity.ceylon_steel.R;
import com.xfinity.ceylon_steel.controller.UserController;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class LoginActivity extends Activity {

	private EditText txtUserName;
	private EditText txtPassword;
	private Button btnSignIn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_page);
		initialize();
	}

	@Override
	public void onBackPressed() {
		System.exit(0);
	}

	// <editor-fold defaultstate="collapsed" desc="Initialize">
	private void initialize() {
		txtUserName = (EditText) findViewById(R.id.txtUserName);
		txtPassword = (EditText) findViewById(R.id.txtPassword);
		btnSignIn = (Button) findViewById(R.id.btnSignIn);
		btnSignIn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				btnSignInClicked(view);
			}
		});
	}
	// </editor-fold>

	private void btnSignInClicked(View view) {
		String userName = txtUserName.getText().toString();
		if (userName.trim().isEmpty()) {
			txtUserName.requestFocus();
			return;
		}
		String password = txtPassword.getText().toString();
		if (password.trim().isEmpty()) {
			txtPassword.requestFocus();
			return;
		}
		UserController.authenticate(userName, password, this);
	}
}
