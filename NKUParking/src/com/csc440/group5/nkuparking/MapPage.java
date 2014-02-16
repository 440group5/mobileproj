/*
 * MapPage.java
 * 
 * Implements the actual google maps page of the app.
 * 
 * -Jordan Bossman
 * 
 * Copyright 2014 CSCGroup5
 */

package com.csc440.group5.nkuparking;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class MapPage extends Activity
{
	private GoogleMap map;
	private LatLng startingCoord;
	private float zoom = 16.0f;
	private final double START_LAT = 39.032356, START_LONG = -84.465406;
	
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
        
        //Move the camera to NKU
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(startingCoord, zoom));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
