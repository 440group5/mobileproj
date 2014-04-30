/*
 * StatusPage.java
 * 
 * Status page of an individual lot. 
 * 
 * -Ryan Lietzenmayer
 * Copyright (c) 2014 CSCGroup5
 */
package com.csc440.group5.nkuparking;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class StatusPage extends Activity 
{
	public ParkingLot currentLot = new ParkingLot("#", "Filler Text Lorem Ipsum", -39.031495, -84.4640840, 100, 0);
	private String selectedLotName;
	int reserveIndex=-1, userClickedIndex=-1;
	ParkingSpace userReservedSpace = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status_page);

		if( getIntent().hasExtra("LotName") )
			selectedLotName = getIntent().getExtras().getString("LotName");
		else
			selectedLotName="A";


		Map<String, ParkingLot> parkingLots = RequestManager.getSharedInstance().getParkingLotMap(false);

		currentLot = parkingLots.get(selectedLotName);

		final TextView textViewToChange = (TextView) findViewById(R.id.lot_name_status);
		textViewToChange.setText( currentLot.getName() );

		MakeReserveButton(); 
		MakeTable(currentLot.getSpaces() );
	}


	/**
	 *  Makes our Reserve button.
	 */
	public void MakeReserveButton()
	{
		// Reserve Button
		final Button bttnDir = (Button) findViewById(R.id.reservebuttonstatus);
		bttnDir.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				// User Restrictions
				SharedPreferences prefs = getSharedPreferences("NKUParkingPrefs", 0);
				String userType = prefs.getString("Status", "Visitor");

				if( reserveIndex > -1 )
				{
					if( currentLot.getStatus().equals(userType) || currentLot.getStatus().equals("Open"))
					{
						boolean spaceWasReserved = false;
						try {
							spaceWasReserved = new ReserveAsync().execute(userType).get();
						} catch (Exception e) {//error contacting server
						}


						if(!spaceWasReserved)
						{
							//Not reserved.
							Context context = getApplicationContext();
							int duration = Toast.LENGTH_SHORT;
							Toast toast = Toast.makeText(context, "Space was not reserved. Error.", duration);
							toast.show();
							//errorDialog();
						}
						else
						{
							//Successfully reserved.
							Context context = getApplicationContext();
							int duration = Toast.LENGTH_SHORT;
							Toast toast = Toast.makeText(context, selectedLotName + " successfully reserved.", duration);
							toast.show();

							userReservedSpace=currentLot.getSpaceAtId(reserveIndex);
						}
					}
					else
					{
						// Tell user they can't park here.
						String restricted = "Reservations in Lot "+selectedLotName+" are restricted to "+currentLot.getStatus()+" users.";
						Context context = getApplicationContext();
						int duration = Toast.LENGTH_SHORT;
						Toast toast = Toast.makeText(context, restricted, duration);
						toast.show();
					}
				}
				else
				{
					Context context = getApplicationContext();
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, "Please select a space.", duration);
					toast.show();
				}					
			}
		});
	}

	public void markOccupied(View view)
	{
		if(reserveIndex > -1)
		{
			ParkingSpace space = currentLot.getSpaceAtIndex(reserveIndex);
			if(space != null)
			{
				if(space.setOccupied())
				{
					Context context = getApplicationContext();
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, String.format("index: %d is now occupied", reserveIndex), duration);
					toast.show();
					
					//TODO: add color to button now or reload data
				}
			}
		}
		else
		{
			Context context = getApplicationContext();
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, "Please select a space", duration);
			toast.show();
		}
	}

	public void errorDialog()
	{
		new AlertDialog.Builder(this)
		.setTitle("Error")
		.setMessage("There was an error reserving this lot.")
		.setPositiveButton(android.R.string.yes, null)
		.show();
	}

	private class ReserveAsync extends AsyncTask<String, Void, Boolean>
	{
		@Override
		protected Boolean doInBackground(String... params)
		{
			SharedPreferences prefs = getSharedPreferences("NKUParkingPrefs", 0);
			int user_id = prefs.getInt("User_id", 9);
			String status = params[0];
			return RequestManager.getSharedInstance().reserveLot(reserveIndex, user_id, selectedLotName, status);
		}
	}

	//	private class CheckReserveAsync extends AsyncTask<String, Void, ParkingSpace>
	//	{
	//		@Override
	//		protected ParkingSpace doInBackground(String... params)
	//		{
	//			SharedPreferences prefs = getSharedPreferences("NKUParkingPrefs", 0);
	//			String user = prefs.getString("Username", "");
	//			return RequestManager.getSharedInstance().getUserSpotInfo(user);
	//		}
	//	}

	/**
	 * Directions Button for Status Page.
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


	/**
	 * Makes the table of buttons which represent spaces and spacing.
	 * @param spaceList
	 */
	public void MakeTable(ArrayList<ParkingSpace> spaceList)
	{
		TableLayout table = (TableLayout) findViewById(R.id.mytablelayout);
		final ArrayList<Button> bttnlist = new ArrayList<Button>();		//list of all available spaces

		//		try {
		//			userReservedSpace = new CheckReserveAsync().execute().get();
		//		} catch (InterruptedException e) {} catch (ExecutionException e) {}

		int spaceIndex=0;
		// rows
		while( spaceIndex < spaceList.size() )
		{
			TableRow row = new TableRow(this);
			// columns
			for(int x=0; x<9; x++)
			{
				// create a new button         
				final Button b = new Button(this);

				if( x!=1 && x!=4 && x!=7 && spaceIndex<spaceList.size() )	// not spacing columns
				{
					// Available Space
					if( spaceList.get(spaceIndex).isAvailable() )	
					{
						bttnlist.add(b);	//add to list of clickable spaces
						b.setBackgroundColor( getResources().getColor(R.color.available) );

						b.setOnClickListener(new View.OnClickListener() 
						{
							public void onClick(View v) 
							{
								// Perform action on click
								for( Button tmp : bttnlist )
								{
									if ( tmp==b )
									{
										// spot clicked
										tmp.setBackgroundColor( getResources().getColor(R.color.reserved) );
										userClickedIndex=bttnlist.indexOf(tmp); 
										setReserveIndex();
									}
									else if( isUserReservedSpace( bttnlist.indexOf(tmp) ) )
										tmp.setBackgroundColor( getResources().getColor(R.color.reserved) );
									else
										tmp.setBackgroundColor( getResources().getColor(R.color.available) );
									// if this user has reserved this space

								}
							}
						});
					}
					// Unavailable Space
					else
					{
						b.setBackgroundColor( getResources().getColor(R.color.unavailable) );
						// Handicapped Space
						if( spaceList.get(spaceIndex).isHandicapped() )
							b.setBackgroundColor( getResources().getColor(R.color.handicapped) );
					}
					spaceIndex++;

				}
				// Generic Filler Space
				else
					b.setBackgroundColor( getResources().getColor(R.color.generic) );

				setRowOptions(row, b, x);

			}
			// Add the new row to the table
			table.addView( row, 
					new TableLayout.LayoutParams
					(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		} //...Here our table is all complete
	}

	/**
	 * Sets various options for the row of buttons.
	 * @param row
	 * @param b
	 * @param x
	 */
	public void setRowOptions(TableRow row, Button b, int x)
	{
		// Row UI Specifications
		row.addView(b); //Attach TextView to its parent (row)

		//Warning: do not call t.setLayoutParams(params)
		//before attaching the view to the parent, 
		//else null reference will result

		//Use the following lines only if special formatting
		//as shown below is needed. If not, skip.
		//

		TableRow.LayoutParams params = 
				(TableRow.LayoutParams)b.getLayoutParams();
		params.column= x; //place at xth columns.

		params.setMargins(1,1,1,1); //To "draw" margins
		//around (outside) the ButtonView, skip if not needed

		params.weight= 1;	// Giving weight means they will fill and share evenly


		//Set width and height as needed
		//params.width=20;
		params.height = TableRow.LayoutParams.WRAP_CONTENT;

		b.setPadding(2, 2, 2, 2); 
		//Skip padding (space around text) above if not needed

		b.setLayoutParams(params); // causes layout update. 
		//Skip above if no special setting is needed
	}

	/**
	 * Sets the reserve index as a place in the list of spaces,
	 * regardless of availability.
	 */
	public void setReserveIndex()
	{
		//userClickedSpace=4,  so reserveIndex could be 30 or something. 0123
		ArrayList<ParkingSpace> list = currentLot.getSpaces();

		int i=0, j=0;
		boolean found =false;
		while( i<list.size() && found==false )
		{
			if( list.get(i).isAvailable() )
			{
				if(j==userClickedIndex)
				{
					reserveIndex=i;
					found=true;
				}
				j++;
			}
			i++;
		}
	}

	/**
	 * Returns true if given index 
	 * matches the User's reserved space.
	 * @param i
	 * @return
	 */
	public boolean isUserReservedSpace(int i)
	{
		if( userReservedSpace != null )
			if( userReservedSpace.equals( currentLot.getSpaces().get(i) ) ) 
				return true;
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.status_page_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Handle presses on the action bar items
		switch (item.getItemId()) {

		//case R.id.action_refresh:
			//TODO PULL LOT DATA
		//	return true;

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