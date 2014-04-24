/*
 * Settings.java
 * 
 * Settings page for the app.
 * 
 * Copyright (c) 2014 CSCGroup5
 */

package com.csc440.group5.nkuparking;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

public class SettingsPage extends Activity 
{
	private SharedPreferences settings;
	private CheckBox autoLogin;
	private boolean shouldAutoLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_page);
		
		autoLogin = ((CheckBox)findViewById(R.id.autoLoginBox));
		
		//Auto check the autologin checkbox based on the value from shared prefs.
		settings = getSharedPreferences("NKUParkingPrefs", 0);
		shouldAutoLogin = settings.getBoolean("AutoLogin", false);
		autoLogin.setChecked(shouldAutoLogin);
	}
	
	public void changedAutoLogin(View view)
	{
		//Changes the value of whether the user wants their user & pass
		//stored on the device or not
		shouldAutoLogin = !shouldAutoLogin;
		settings = getSharedPreferences("NKUParkingPrefs", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("AutoLogin", shouldAutoLogin);
		editor.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.context_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Handle presses on the action bar items
		switch (item.getItemId()) {

		case R.id.action_map:
			Intent map = new Intent(this, MapPage.class);
			startActivity(map);
			return true;

		case R.id.action_directions:
			Intent directions = new Intent(this, DirectionsPage.class);
			startActivity(directions);
			return true;

		case R.id.action_search:
			Intent search = new Intent(this, SearchPage.class);
			startActivity(search);
			return true; 

		case R.id.action_status:
			Intent status = new Intent(this, StatusPage.class);
			startActivity(status);
			return true;

		case R.id.action_settings:
			Intent settings = new Intent(this, SettingsPage.class);
			startActivity(settings);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
