/*
 * MainActivity.java
 * 
 * Implements the main activity of the mobile application.
 * 
 * -Jordan Bossman
 * 
 * 
 * Copyright 2014 CSCGroup5
 */

package com.csc440.group5.nkuparking;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity 
{
	private WebView webview;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		//Method sets up the textfields and add listeners for input used later on.
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_web_view);

		SharedPreferences prefs = getSharedPreferences("NKUParkingPrefs", 0);
		//        Editor edit = prefs.edit();
		double lat = Double.parseDouble(prefs.getString("CurrentLat", "-1.0"));
		double lg = Double.parseDouble(prefs.getString("CurrentLong", "-1.0"));
		double toLat = Double.parseDouble(getIntent().getExtras().getString("Lat"));
		double toLong = Double.parseDouble(getIntent().getExtras().getString("Long"));
		//        edit.remove("CurrentLat");
		//        edit.remove("CurrentLong");
		//        edit.commit();

		//        String url = String.format("http://maps.google.com/maps?saddr=%f,%f&daddr=39.03364,-84.466995&f=d", lat, lg);

		String url = String.format("http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f&f=d", lat, lg, toLat, toLong);
		webview = (WebView)findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				view.loadUrl(url);
				return true;
			}
		});
		//        webview.loadUrl("https://maps.google.com/maps/dir/'39.032356,-84.4654'/'39.03364,-84.466995'/");
		//        webview.loadUrl("https://www.google.com/maps/dir/39.032356,-84.4654/39.03364,-84.466995/@39.0324539,-84.4686292,17z/");
		//        webview.loadUrl("https://www.google.com/maps/preview?saddr=39.032356,-84.4654&daddr=39.03364,-84.466995&z=18");
		//        webview.loadUrl("https://maps.google.com/?saddr=39.032356,-84.4654&daddr=39.03364,-84.466995");
		webview.loadUrl(url);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.context_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Handle presses on the action bar items
		switch (item.getItemId()) {

		case R.id.action_map:
			Intent map = new Intent(this, MapPage.class);
			startActivity(map);
			return true;

		case R.id.action_search:
			Intent search = new Intent(this, SearchPage.class);
			startActivity(search);
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
