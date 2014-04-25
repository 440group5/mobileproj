package com.csc440.group5.nkuparking;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status_page);

		final Button bttnStat = (Button) findViewById(R.id.directionsbuttonstatus);
		
		bttnStat.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				// Perform action on click
				Context context = getApplicationContext();
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, "Directions Button Pressed", duration);
				toast.show();
			}
		});
		final Button bttnDir = (Button) findViewById(R.id.reservebuttonstatus);
		bttnDir.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				// Perform action on click
				Context context = getApplicationContext();
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, "Reservation Button Pressed", duration);
				toast.show();
			}
		});



		TableLayout table = (TableLayout) findViewById(R.id.mytablelayout);
		final ArrayList<Button> bttnlist = new ArrayList<Button>();
		// rows
		for(int y=0; y<20; y++) 
		{
			TableRow row = new TableRow(this);
			// columns
			for(int x=0; x<9; x++)
			{
				// create a new button         
				final Button b = new Button(this);
				//b.setText(x+","+y); // text on button

				if( x!=1 && x!=4 && x!=7 )
				{
					if( y>2 )	// if available space
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
										tmp.setBackgroundColor( getResources().getColor(R.color.reserved) );
									else
										tmp.setBackgroundColor( getResources().getColor(R.color.available) );
							}
						});
					}
					else
						b.setBackgroundColor( getResources().getColor(R.color.unavailable) );
				}
				else
					b.setBackgroundColor( getResources().getColor(R.color.generic) );
				//handicapped
				if(x==2 && y==0)
					b.setBackgroundColor( getResources().getColor(R.color.handicapped) );

				row.addView(b); //Attach TextView to its parent (row)
				row.setWeightSum(9);
				
				//Warning: do not call t.setLayoutParams(params)
				//before attaching the view to the parent, 
				//else null reference will result
				//Use the following lines only if special formatting
				//as shown below is needed. If not, skip.
				//
				
				TableRow.LayoutParams params = 
						(TableRow.LayoutParams)b.getLayoutParams();
				params.column= x; //place at xth columns.
				//Skip above line if being placed side by side

				
				//params.span = 1; //span these many columns, 
				//i.e merge these many cells. Skip if not needed

				params.setMargins(1,1,1,1); //To "draw" margins
				//around (outside) the ButtonView, skip if not needed

				params.weight=1;
				
				//params.width=20;
				//Set width as needed (Important: this and the
				//.height below is for layout of "text" inside 
				//the TextView, not for layout of TextView' by its
				//parent)

				params.height = TableRow.LayoutParams.WRAP_CONTENT;
				
				b.setPadding(2, 2, 2, 2); 
				//Skip padding (space around text) above if not needed

				b.setLayoutParams(params); // causes layout update. 
				//Skip above if no special setting is needed

			}
			//Next add the new row to the table
			table.addView( row, 
					new TableLayout.LayoutParams
					(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		} //...Here our row is all complete with 10 TextViews


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
