/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2014, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Aug 26, 2014, 5:27:52 PM
 */
package com.xfinity.ceylon_steel.activity.attendance;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.xfinity.ceylon_steel.R;
import com.xfinity.ceylon_steel.activity.HomeActivity;
import com.xfinity.ceylon_steel.controller.UserController;
import com.xfinity.ceylon_steel.model.AttendanceRecord;
import com.xfinity.ceylon_steel.widget.FilterableBaseAdapter;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class CheckInCheckOutHistory extends Activity {
	private final ArrayList<AttendanceRecord> attendanceRecords = new ArrayList<AttendanceRecord>();
	private final Calendar calendar = Calendar.getInstance();
	private ListView listView;
	private Button btnBack;
	private EditText inputFromDate;
	private EditText inputToDate;
	private FilterableBaseAdapter adapter;
	private String fromDate = "";
	private String toDate = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attendance_history_page);
		attendanceRecords.clear();
		attendanceRecords.addAll(UserController.getAttendanceHistory(CheckInCheckOutHistory.this, fromDate.trim(), toDate.trim()));
		initialize();
	}

	@Override
	public void onBackPressed() {
		Intent homeActivity = new Intent(CheckInCheckOutHistory.this, HomeActivity.class);
		startActivity(homeActivity);
		finish();
	}

	private void initialize() {
		listView = (ListView) findViewById(R.id.listView);
		btnBack = (Button) findViewById(R.id.btnBack);
		inputFromDate = (EditText) findViewById(R.id.inputFromDate);
		inputToDate = (EditText) findViewById(R.id.inputToDate);
		inputFromDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				inputFromDateClicked(v);
			}
		});
		inputToDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				inputToDateClicked(v);
			}
		});
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		listView.setAdapter(adapter = new FilterableBaseAdapter() {
			@Override
			public int getCount() {
				return attendanceRecords.size();
			}

			@Override
			public AttendanceRecord getItem(int position) {
				return attendanceRecords.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder viewHolder;
				if (convertView == null) {
					LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
					viewHolder = new ViewHolder();
					convertView = inflater.inflate(R.layout.attendance_record, null);
					viewHolder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
					viewHolder.txtCheckInTime = (TextView) convertView.findViewById(R.id.txtCheckInTime);
					viewHolder.txtCheckOutTime = (TextView) convertView.findViewById(R.id.txtCheckOutTime);
					convertView.setTag(viewHolder);
				} else {
					viewHolder = (ViewHolder) convertView.getTag();
				}
				AttendanceRecord attendanceRecord = getItem(position);
				viewHolder.txtDate.setText(attendanceRecord.getDate());
				viewHolder.txtCheckInTime.setText(attendanceRecord.getCheckInTime());
				viewHolder.txtCheckOutTime.setText(attendanceRecord.getCheckOutTime());
				convertView.setBackgroundColor((position % 2 == 0) ? Color.parseColor("#E6E6E6") : Color.parseColor("#FFFFFF"));
				return convertView;
			}

			@Override
			public Filter getFilter() {
				return null;
			}
		});
	}

	private void btnSearchClicked(View view) {
		attendanceRecords.clear();
		attendanceRecords.addAll(UserController.getAttendanceHistory(CheckInCheckOutHistory.this, fromDate.trim(), toDate.trim()));
		adapter.notifyDataSetChanged();
	}

	private void inputToDateClicked(View view) {
		new DatePickerDialog(CheckInCheckOutHistory.this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				inputToDate.setText(toDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
	}

	private void inputFromDateClicked(View view) {
		new DatePickerDialog(CheckInCheckOutHistory.this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				inputFromDate.setText(fromDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
	}

	private class ViewHolder {
		TextView txtDate;
		TextView txtCheckInTime;
		TextView txtCheckOutTime;
	}
}
