package com.csc440.group5.nkuparking;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.RadioButton;
import android.widget.Toast;

public class SearchPage extends Activity implements OnItemSelectedListener
{
	private Spinner spinner;
	ArrayList<String> listOfLots;
	String selectedLot;
	Boolean randomLot;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_page);

		populateSpinner();
	}

	/**
	 * Reservation Button for Search Page.
	 * Opens a Lot Status page so user can select a space to reserve.
	 * @param view
	 */
	public void loadLotStatus(View view)
	{
		// Toast message for button.
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, "Reservation Button Pressed", duration);
		toast.show();

		// Starts Status window
		Intent status = new Intent(this, StatusPage.class);
		startActivity(status);
	}

	/**
	 * Directions Button for Search Page.
	 * @param view
	 */
	public void loadDirs(View view)
	{
		//39.032356,-84.4654'/'39.03364,-84.466995
		String begin = String.format(Locale.ENGLISH, "%f,%f", 39.032356, -84.4654);
		String end = String.format(Locale.ENGLISH, "%f,%f", 39.027865, -84.462392);

		//        String uristr = begin + "?q=" + end + "&z=18";
		//        String uristr = "saddr=" + begin + "&daddr=" + end + "&z=18";
		//        Uri uri = Uri.parse(uristr);
		Intent intent = new Intent(this, WebViewActivity.class);
		startActivity(intent);

		//        String uri = String.format(Locale.ENGLISH, "geo:%f,%f?q=%f,%f", 39.032356, -84.4654, 39.032356, -84.4654);
		//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		//        this.startActivity(intent);
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
				// specific spot checked
				Context context = getApplicationContext();
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, selectedLot, duration);
				toast.show();
				
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
		selectedLot = listOfLots.get(pos);
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) 
	{
		// Generic default
		selectedLot = listOfLots.get(0);	//default as first lot
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
			listOfLots = asyncLotList.execute().get();
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


		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listOfLots);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
	}

	private class RequestAsync extends AsyncTask<Void, Void, ArrayList<String>>
	{
		//Asynchronously goes and sees if the login information is correct
		@Override
		protected ArrayList<String> doInBackground(Void... params)
		{			
			Map<String, ParkingLot> lots = RequestManager.getSharedInstance().getLotInformation();
			ArrayList<String> lotNameList = new ArrayList<String>(lots.size());
			for(String key: lots.keySet() )
			{
				lotNameList.add( lots.get(key).getName() );
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
