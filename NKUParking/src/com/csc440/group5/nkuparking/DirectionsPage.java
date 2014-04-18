package com.csc440.group5.nkuparking;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import com.google.android.gms.maps.model.MarkerOptions;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class DirectionsPage extends Activity 
{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_directions_page);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.context_menu, menu);
		EditText txt = (EditText)findViewById(R.id.testText);
		
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
