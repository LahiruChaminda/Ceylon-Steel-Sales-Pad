/*
 * Intellectual properties of Supun Lakshan Wanigarathna Dissanayake
 * Copyright (c) 2014, Supun Lakshan Wanigarathna Dissanayake. All rights reserved.
 * Created on : Aug 26, 2014, 5:27:52 PM
 */
package com.xfinity.ceylon_steel.activity.attendance;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.xfinity.ceylon_steel.R;
import com.xfinity.ceylon_steel.activity.HomeActivity;
import com.xfinity.ceylon_steel.controller.UserController;
import com.xfinity.ceylon_steel.model.AttendanceRecord;

import java.util.ArrayList;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class CheckInCheckOutHistory extends Activity {
	private ListView listView;
	private Button btnBack;
	private ArrayList<AttendanceRecord> attendanceRecords;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attendance_history_page);
		attendanceRecords = UserController.getAttendanceHistory(CheckInCheckOutHistory.this);
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
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		listView.setAdapter(new BaseAdapter() {
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
		});
	}

	private class ViewHolder {
		TextView txtDate;
		TextView txtCheckInTime;
		TextView txtCheckOutTime;
	}
}
