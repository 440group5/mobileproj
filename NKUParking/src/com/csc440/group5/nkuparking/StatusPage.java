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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
	int reserveIndex=-1;

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

				if( currentLot.getStatus().equals(userType) )
				{	
					// Perform action on click
					Context context = getApplicationContext();
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, selectedLotName, duration);
					toast.show();
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
		});
	}

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

	// This is where the rows are added to the TableLayout
	public void MakeTable(ArrayList<ParkingSpace> spaceList)
	{
		TableLayout table = (TableLayout) findViewById(R.id.mytablelayout);
		final ArrayList<Button> bttnlist = new ArrayList<Button>();		//list of all available spaces

		int spaceIndex=0;
		// rows
		//for(int y=0; y<20; y++)
		while( spaceIndex < spaceList.size() )
		{
			TableRow row = new TableRow(this);
			// columns
			for(int x=0; x<9; x++)
			{
				// create a new button         
				final Button b = new Button(this);
				//b.setText(x+","+y); // text on button

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
									if (tmp==b )
									{
										// spot clicked
										tmp.setBackgroundColor( getResources().getColor(R.color.reserved) );
										reserveIndex=bttnlist.indexOf(tmp);
									}
									else
										tmp.setBackgroundColor( getResources().getColor(R.color.available) );
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


				row.addView(b); //Attach TextView to its parent (row)
				//row.setWeightSum(9);

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

				params.weight=1;	// Giving weight means they will fill and share evenly


				//Set width and height as needed
				//params.width=20;
				params.height = TableRow.LayoutParams.WRAP_CONTENT;

				b.setPadding(2, 2, 2, 2); 
				//Skip padding (space around text) above if not needed

				b.setLayoutParams(params); // causes layout update. 
				//Skip above if no special setting is needed

			}
			// Add the new row to the table
			table.addView( row, 
					new TableLayout.LayoutParams
					(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		} //...Here our table is all complete
	}

	// Set which lot this should load. Default is gibberish.
	public void setLot(ParkingLot tempLot)
	{
		currentLot = tempLot;
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
