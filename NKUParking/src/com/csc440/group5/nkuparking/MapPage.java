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

import java.util.ArrayList;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.csc440.group5.nkuparking.ParkingLot;

public class MapPage extends Activity
{
	private GoogleMap map;
	private LatLng startingCoord;
	private float zoom = 16.0f;
	private final double START_LAT = 39.032356, START_LONG = -84.465406;
	private ArrayList<ParkingLot> lotList = new ArrayList<ParkingLot>();
	
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
        map.getUiSettings().setAllGesturesEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        
        //Load the map markers async.
        new BuildLotsAsync().execute();
        
        //Move the camera to NKU
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(startingCoord, zoom));
    }
    
    private class BuildLotsAsync extends AsyncTask<Void, Void, ArrayList<ParkingLot>>
	{
		@Override
		protected ArrayList<ParkingLot> doInBackground(Void... params)
		{
			//Build the Lots asynchronously.
			lotList.add(new ParkingLot("A", "Lot A - Open Lot", 39.030569, -84.468997));
			lotList.add(new ParkingLot("C", "Lot C - Faculty/Staff Lot", 39.031650, -84.466539));
			lotList.add(new ParkingLot("D", "Lot D - Faculty/Staff Lot", 39.032240, -84.461732));
			lotList.add(new ParkingLot("E", "Lot E - Faculty/Staff Lot", 39.033813, -84.465048));
			lotList.add(new ParkingLot("F", "Lot F - Student Lot", 39.034353, -84.464137));
			lotList.add(new ParkingLot("G", "Lot G - Open Lot", 39.029586, -84.469147));
			lotList.add(new ParkingLot("I", "Lot I - Student Lot", 39.033275, -84.463403));
			lotList.add(new ParkingLot("J", "Lot J - Faculty/Staff Lot", 39.030242, -84.461554));
			lotList.add(new ParkingLot("K", "Lot K - Open Lot", 39.031553, -84.468237));
			lotList.add(new ParkingLot("L", "Lot L - Open Lot", 39.032869, -84.468344));
			lotList.add(new ParkingLot("M", "Lot M - Student Lot", 39.033643, -84.466995));
			lotList.add(new ParkingLot("N", "Lot N - Reserved Lot", 39.02963, -84.462902));
			lotList.add(new ParkingLot("O", "Lot O - Open/VIP Lot", 39.031161, -84.45933));
			lotList.add(new ParkingLot("P", "Lot P - Student Lot", 39.034545, -84.467398));
			lotList.add(new ParkingLot("Q", "Lot Q - Student Lot", 39.036795, -84.46669));
			lotList.add(new ParkingLot("R", "Lot R - Student Lot (Ceramics Permit)", 39.036931, -84.465037));
			lotList.add(new ParkingLot("S", "Lot S - Open Lot", 39.028469, -84.466405));
			lotList.add(new ParkingLot("T", "Lot T - Open Lot", 39.028633, -84.462243));
			lotList.add(new ParkingLot("U", "Lot U - Student Lot", 39.036169, -84.465173));
			lotList.add(new ParkingLot("V", "Lot V - Faculty/Staff Lot", 39.028757, -84.4638));
			lotList.add(new ParkingLot("W", "Lot W - Student Lot", 39.032522, -84.463036));
			lotList.add(new ParkingLot("X", "Lot X - Open Lot", 39.027865, -84.462392));
			lotList.add(new ParkingLot("Y", "Lot Y - Student Lot", 39.033911, -84.468868));
			lotList.add(new ParkingLot("Welcome Center Garage", "", 39.032303, -84.460835));
			lotList.add(new ParkingLot("University Drive Garage", "", 39.03018, -84.461148));
			lotList.add(new ParkingLot("Kenton Drive Garage", "", 39.030286, -84.467913));
			return lotList;
		}
		
		protected void onPostExecute(ArrayList<ParkingLot> result)
		{
			//Add the markers to the map.
			for(ParkingLot lot : result)
				map.addMarker(new MarkerOptions().position(lot.getCoordinate()).title(lot.getName()).snippet(lot.getDescription()));
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
