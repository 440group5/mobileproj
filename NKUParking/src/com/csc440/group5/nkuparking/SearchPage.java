/*
 * SearchPage.java
 * 
 * Used to search for a specific lot by name or availability. 
 * 
 * -Ryan Lietzenmayer
 * Copyright (c) 2014 CSCGroup5
 */

package com.csc440.group5.nkuparking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;

public class SearchPage extends Activity implements OnItemSelectedListener
{
	private Spinner spinner;
	Map<String, ParkingLot> lotInformation;
	ArrayList<String> listOfLotNames;
	String selectedLotName;
	ParkingLot currentLot;
	boolean randomLot=true;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_page);

		populateSpinner();
	}

	/**
	 * View Lot button for Search Page.
	 * Opens a Lot Status page so user can select a space to reserve.
	 * @param view
	 */
	public void loadLotStatus(View view)
	{
		// Starting Status window
		Intent status = new Intent(this, StatusPage.class);
		if( randomLot )
		{
			// Find space in same type of lot as the user.
			SharedPreferences prefs = getSharedPreferences("NKUParkingPrefs", 0);
			String userType = prefs.getString("Status", "Visitor");

			boolean foundspace=false;
			int i=0, j=0;
			while( foundspace==false )
			{
				// Get list of spaces for each lot
				ArrayList<ParkingSpace> tmpLot =lotInformation.get(listOfLotNames.get(i)).getSpaces();
				if( lotInformation.get(i).getStatus().equals(userType) )
				{
					// Go through list of spaces
					while( j<tmpLot.size() && foundspace==false )
					{
						// if one of them is open, set that lot to open as status, then exit loop
						if( tmpLot.get(i).isAvailable() )
						{
							status.putExtra( "LotName",listOfLotNames.get(i) );
							foundspace=true;
						}
						j++;
					}
				}
				j=0;	//reset j
				i++;
			}
		}
		else
			status.putExtra("LotName", selectedLotName);
		startActivity(status);	//Start Status Window
	}

	/**
	 * Directions Button for Search Page.
	 * @param view
	 */
	public void loadDirs(View view)
	{
		Intent intent = new Intent(this, WebViewActivity.class);
		String lat = String.format("%f", currentLot.getCoordinate().latitude);
		String lg = String.format("%f", currentLot.getCoordinate().longitude);
		intent.putExtra("Lat", lat);
		intent.putExtra("Long", lg);
		startActivity(intent);
	}

	public void onRadioButtonClicked(View view) 
	{
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();

		// Check which radio button was clicked
		switch(view.getId()) 
		{
		case R.id.radio_any_spot:
			if (checked)
			{
				// any spot checked
				randomLot=true;
			}
			break;
		case R.id.radio_lot_spot:
			if (checked)
			{
				randomLot=false;
			}
			break;
		}
	}

	/**
	 * Spinner Stuff
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
	{
		//Determines which option for the dropdown list has been clicked on.
		selectedLotName = listOfLotNames.get(pos);
		currentLot = lotInformation.get(selectedLotName);
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) 
	{
		// Generic default
		selectedLotName = listOfLotNames.get(0);	//default as first lot
	}

	/**
	 * Populates Spinner, the dropdown box where
	 * the user selects from a list of lots.
	 */
	public void populateSpinner()
	{
		//Add the options to the spinner (dropdown box for student & faculty/staff).
		spinner = (Spinner)findViewById(R.id.spinner_lots);

		//Async Call for Lot List
		RequestAsync asyncLotList = new RequestAsync();

		try {
			listOfLotNames = asyncLotList.execute().get();
			Collections.sort(listOfLotNames);
		}
		catch (Exception e)
		{
			new AlertDialog.Builder(this)
			.setTitle("Error")
			.setMessage("There was an error contacting the server for lot information.")
			.setPositiveButton(android.R.string.yes, null)
			.show();
			return;
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listOfLotNames);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
	}

	/**
	 * Asynchronously goes and gets shared parking lot map, lotInformation
	 * @author ryanlietzenmayer
	 *
	 */
	private class RequestAsync extends AsyncTask<Void, Void, ArrayList<String>>
	{
		@Override
		protected ArrayList<String> doInBackground(Void... params)
		{			
			lotInformation = RequestManager.getSharedInstance().getParkingLotMap(false);

			ArrayList<String> lotNameList = new ArrayList<String>(lotInformation.size());

			for(String key: lotInformation.keySet() )
			{
				lotNameList.add( lotInformation.get(key).getName() );
			}

			return lotNameList;
		}
	}


	// Options Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
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
