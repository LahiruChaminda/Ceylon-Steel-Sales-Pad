package com.xfinity.ceylon_steel.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.xfinity.ceylon_steel.R;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Supun Lakshan Wanigarathna Dissanayake
 * @mobile +94711290392
 * @email supunlakshan.xfinity@gmail.com
 */
public class StartingActivity extends Activity {

	/**
	 * Called when the activity is first created.
	 *
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException ex) {
					Logger.getLogger(StartingActivity.class.getName()).log(Level.SEVERE, null, ex);
				} finally {
					Intent loginActivity = new Intent(StartingActivity.this, LoginActivity.class);
					startActivity(loginActivity);
					finish();
				}
			}
		};
		thread.start();
	}
}
