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

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_page);

		//Add the options to the spinner (dropdown box for student & faculty/staff).
		spinner = (Spinner)findViewById(R.id.spinner_lots);

		//Async Call for Lot List
		RequestAsync asyncLogin = new RequestAsync();
		ArrayList<String> listOfLots;

		try {
			listOfLots = asyncLogin.execute().get();
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
	
	public void loadLotStatus(View view)
	{
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, "Reservation Button Pressed", duration);
		toast.show();
		
		Intent status = new Intent(this, StatusPage.class);
		startActivity(status);
	}
	
	public void loadDirs(View view)
	{//39.032356,-84.4654'/'39.03364,-84.466995
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

	public void onRadioButtonClicked(View view) {
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();

		// Check which radio button was clicked
		switch(view.getId()) 
		{
		case R.id.radio_any_spot:
			if (checked)
				// anyspot checked
				break;
		case R.id.radio_lot_spot:
			if (checked)
				// specific spot checked
				break;
		}
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
	{
		//Determines which option for the dropdown list has been clicked on.
		switch(pos)
		{
		case 0:
			break;
		}
	}

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
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
}
