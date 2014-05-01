/*
 * MapPage.java
 * 
 * Implements the actual google maps page of the app.
 * 
 * -Jordan Bossman
 * 
 * Copyright (c) 2014 CSCGroup5
 */

package com.csc440.group5.nkuparking;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapPage extends Activity implements OnMyLocationChangeListener
{
	private GoogleMap map;
	private LatLng startingCoord;
	private float zoom = 16.0f;
	private final double START_LAT = 39.032356, START_LONG = -84.465406;
	private Map<String, ParkingLot> lotMap;
	private Location currentLocation;
	private boolean gpsLoaded = false, firstLoad = true;
	private ProgressDialog spinner;
	private ParkingLot userSelectedLot;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		//Method loads the webview and sets the URL to the MAP_URL constant.
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_page);

		//Set starting coordinate to NKU & load the map fragment
		startingCoord = new LatLng(START_LAT, START_LONG);
		map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setMyLocationEnabled(true);
		map.setOnMyLocationChangeListener(this);
		map.getUiSettings().setAllGesturesEnabled(true);
		map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

		//Move the camera to NKU
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(startingCoord, zoom));

		//Add the map marker click listener
		map.setOnMarkerClickListener(new OnMarkerClickListener() 
		{
			@Override
			public boolean onMarkerClick(Marker marker)
			{
				//Grab the parking lot from the marker clicked			
				userSelectedLot = lotMap.get(marker.getTitle());			

				return false;
			}
		});

		buildLots();
	}

	private void buildLots()
	{
		try 
		{
			//Throw up a progress indicator while the app fetches the lot information
//			spinner = new ProgressDialog(this);
//			spinner.setMessage("Loading Lot Information...");
//			spinner.setCancelable(false);
//			spinner.show();

			//Asynchronously load the lot data & add map markers
			new LoadLotsAsync(this).execute();
		}
		catch (Exception e) 
		{
			//Dismiss the spinner so it doesn't stay up if there was an error.
			spinner.dismiss();

			new AlertDialog.Builder(this)
			.setTitle("Error")
			.setMessage("Error contacting the server for the lot information.")
			.setPositiveButton(android.R.string.yes, null)
			.show();

			return;
		}
	}

	/**
	 * View Lot Button for Map Page.
	 * Opens a Lot Status page so user can select a space to reserve.
	 * @param view
	 */
	public void loadLotStatus(View view)
	{
		if(userSelectedLot == null)
		{
			Toast.makeText(getBaseContext(), 
					"Please select a lot.",
					Toast.LENGTH_SHORT).show();	
		}
		else if(userSelectedLot.getName().equals("Welcome Center Garage") || userSelectedLot.getName().equals("University Drive Garage") || userSelectedLot.getName().equals("Kenton Drive Garage"))
		{
			Toast.makeText(getBaseContext(), 
					"Parking Garage Status Restricted",
					Toast.LENGTH_SHORT).show();	
		}
		else
		{
			// Starts Status window
			Intent status = new Intent(this, StatusPage.class);
			status.putExtra( "LotName", userSelectedLot.getName() );
			startActivity(status);
		}
	}

	/**
	 * Directions Button for Map Page.
	 * @param view
	 */
	public void loadDirections(View view)
	{
		if(userSelectedLot == null)
		{
			Toast.makeText(getBaseContext(), 
					"Please select a lot.",
					Toast.LENGTH_SHORT).show();	
		}
		else
		{
			Intent intent = new Intent(this, WebViewActivity.class);
			String lat = String.format("%f", userSelectedLot.getCoordinate().latitude);
			String lg = String.format("%f", userSelectedLot.getCoordinate().longitude);
			intent.putExtra("Lat", lat);
			intent.putExtra("Long", lg);
			startActivity(intent);
		}
	}

	private class LoadLotsAsync extends AsyncTask<Void, Void, Map<String, ParkingLot>>
	{
		private Context context;
		
		public LoadLotsAsync(Context context)
		{
			this.context = context;
		}
		
		@Override 
		public void onPreExecute()
		{
			int currentOrientation = getResources().getConfiguration().orientation; 
					  
			if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE)
		        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
			else
		        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
			  
			spinner = new ProgressDialog(context);
			spinner.setMessage("Loading Lot Information...");
			spinner.setCancelable(false);
			spinner.show();
				
		}
		
		@Override
		protected Map<String, ParkingLot> doInBackground(Void... params)
		{
			try
			{
				//Only pull the data unless its the first time loading
				if(firstLoad)
				{
					firstLoad = false;
					return RequestManager.getSharedInstance().getParkingLotMap(true);
				}

				return RequestManager.getSharedInstance().getParkingLotMap(firstLoad);
			}
			catch(Exception e)
			{
				return null;
			}
		}

		@Override
		protected void onPostExecute(Map<String, ParkingLot> lots)
		{
			if(lotMap == null)
				lotMap = new HashMap<String, ParkingLot>();
			else
				lotMap.clear();


			lotMap.putAll(lots);
			lotMap.put("Welcome Center Garage", new ParkingLot("Welcome Center Garage", "Parking Garage", 39.032303, -84.460835,0, ParkingLot.OPEN_PARKING));
			lotMap.put("University Drive Garage", new ParkingLot("University Drive Garage", "Parking Garage", 39.03018, -84.461148,0, ParkingLot.OPEN_PARKING));
			lotMap.put("Kenton Drive Garage", new ParkingLot("Kenton Drive Garage", "Parking Garage", 39.030286, -84.467913,0, ParkingLot.OPEN_PARKING));

			//Load the map markers for each of the lots
			for(String key : lotMap.keySet())
			{
				ParkingLot lot = lotMap.get(key);
				String desc = "";
				if(!(key.equals("Welcome Center Garage") || key.equals("University Drive Garage") || key.equals("Kenton Drive Garage")))
				{
					desc = String.format("%s Lot", lot.getStatus());

					// Color Map Marker based on type of lot
					if( lot.getStatus().equals("Open") )
					{
						map.addMarker(
								new MarkerOptions()
								.position(lot.getCoordinate())
								.title( lot.getName() )
								.snippet(desc)
								.icon( BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN) )
								);
					}
					else if( lot.getStatus().equals("Student") )
					{
						map.addMarker(
								new MarkerOptions()
								.position(lot.getCoordinate())
								.title( lot.getName() )
								.snippet(desc)
								.icon( BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE) )
								);
					}

					else if( lot.getStatus().equals("Faculty/Staff") )
					{
						map.addMarker(
								new MarkerOptions()
								.position(lot.getCoordinate())
								.title( lot.getName() )
								.snippet(desc)
								.icon( BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET) )
								);
					}
					else if( lot.getStatus().equals("Closed") )
					{
						map.addMarker(
								new MarkerOptions()
								.position(lot.getCoordinate())
								.title( lot.getName() )
								.snippet(desc)
								.icon( BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE) )
								);
					}
					else
					{
						map.addMarker(
								new MarkerOptions()
								.position(lot.getCoordinate())
								.title( lot.getName() )
								.snippet(desc)
								.icon( BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED) )
								);
					}
				}
				else	//colors garages
				{
					desc = lot.getDescription();

					map.addMarker(
							new MarkerOptions()
							.position(lot.getCoordinate())
							.title( lot.getName() )
							.snippet(desc)
							.icon( BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA) )
							);
				}


			}

			//Dismiss the progress indicator
			spinner.dismiss();
			
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		}
	}

	@Override
	public void onMyLocationChange(Location location)
	{
		if(!gpsLoaded)
			gpsLoaded = true;

		currentLocation = location;

		double lat = currentLocation.getLatitude();
		double lg = currentLocation.getLongitude();

		SharedPreferences prefs = getSharedPreferences("NKUParkingPrefs", 0);
		Editor edit = prefs.edit();
		edit.putString("CurrentLat", String.format("%f",lat));
		edit.putString("CurrentLong", String.format("%f", lg));
		edit.commit();
	}

	// Options Menu items
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
		if(!gpsLoaded)
		{
			new AlertDialog.Builder(this)
			.setTitle("Error")
			.setMessage("Please wait for the GPS to load before selecting any menu options. To check, please press the my location button in the upper right.")
			.setPositiveButton(android.R.string.yes, null)
			.show();
			return false;
		}

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
//    		OLD PARKING LOT INFORMATION
//			ParkingLot Constructor: ParkingLot(name, description, latitude, longitude, number of spaces)
//			lotList.add(new ParkingLot("A", "Lot A - Open Lot", 39.030569, -84.468997, 0, ParkingLot.OPEN_PARKING));
//			lotList.add(new ParkingLot("C", "Lot C - Faculty/Staff Lot", 39.031650, -84.466539, 0, ParkingLot.FACULTY_STAFF_LOT));
//			lotList.add(new ParkingLot("D", "Lot D - Faculty/Staff Lot", 39.032240, -84.461732, 0, ParkingLot.FACULTY_STAFF_LOT));
//			lotList.add(new ParkingLot("E", "Lot E - Faculty/Staff Lot", 39.033813, -84.465048, 0, ParkingLot.FACULTY_STAFF_LOT));
//			lotList.add(new ParkingLot("F", "Lot F - Student Lot", 39.034353, -84.464137, 0, ParkingLot.STUDENT_LOT));
//			lotList.add(new ParkingLot("G", "Lot G - Open Lot", 39.029586, -84.469147, 0, ParkingLot.OPEN_PARKING));
//			lotList.add(new ParkingLot("I", "Lot I - Student Lot", 39.033275, -84.463403, 0, ParkingLot.STUDENT_LOT));
//			lotList.add(new ParkingLot("J", "Lot J - Faculty/Staff Lot", 39.030242, -84.461554, 0, ParkingLot.FACULTY_STAFF_LOT));
//			lotList.add(new ParkingLot("K", "Lot K - Open Lot", 39.031553, -84.468237, 0, ParkingLot.OPEN_PARKING));
//			lotList.add(new ParkingLot("L", "Lot L - Open Lot", 39.032869, -84.468344, 0, ParkingLot.OPEN_PARKING));
//			lotList.add(new ParkingLot("M", "Lot M - Student Lot", 39.033643, -84.466995, 0, ParkingLot.STUDENT_LOT));
//			lotList.add(new ParkingLot("N", "Lot N - Reserved Lot", 39.02963, -84.462902, 0, ParkingLot.CLOSED_PARKING));
//			lotList.add(new ParkingLot("O", "Lot O - Open/VIP Lot", 39.031161, -84.45933, 0, ParkingLot.OPEN_PARKING));
//			lotList.add(new ParkingLot("P", "Lot P - Student Lot", 39.034545, -84.467398, 0, ParkingLot.STUDENT_LOT));
//			lotList.add(new ParkingLot("Q", "Lot Q - Student Lot", 39.036795, -84.46669, 0, ParkingLot.STUDENT_LOT));
//			lotList.add(new ParkingLot("R", "Lot R - Student Lot (Ceramics Permit)", 39.036931, -84.465037, 0, ParkingLot.CLOSED_PARKING));
//			lotList.add(new ParkingLot("S", "Lot S - Open Lot", 39.028469, -84.466405, 0, ParkingLot.OPEN_PARKING));
//			lotList.add(new ParkingLot("T", "Lot T - Open Lot", 39.028633, -84.462243, 0, ParkingLot.OPEN_PARKING));
//			lotList.add(new ParkingLot("U", "Lot U - Student Lot", 39.036169, -84.465173, 0, ParkingLot.STUDENT_LOT));
//			lotList.add(new ParkingLot("V", "Lot V - Faculty/Staff Lot", 39.028757, -84.4638, 0, ParkingLot.FACULTY_STAFF_LOT));
//			lotList.add(new ParkingLot("W", "Lot W - Student Lot", 39.032522, -84.463036, 0, ParkingLot.STUDENT_LOT));
//			lotList.add(new ParkingLot("X", "Lot X - Open Lot", 39.027865, -84.462392, 0, ParkingLot.OPEN_PARKING));
//			lotList.add(new ParkingLot("Y", "Lot Y - Student Lot", 39.033911, -84.468868, 0, ParkingLot.STUDENT_LOT));
//			lotList.add(new ParkingLot("Welcome Center Garage", "", 39.032303, -84.460835,0, ParkingLot.OPEN_PARKING));
//			lotList.add(new ParkingLot("University Drive Garage", "", 39.03018, -84.461148,0, ParkingLot.OPEN_PARKING));
//			lotList.add(new ParkingLot("Kenton Drive Garage", "", 39.030286, -84.467913,0, ParkingLot.OPEN_PARKING));