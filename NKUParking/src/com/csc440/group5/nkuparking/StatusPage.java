package com.csc440.group5.nkuparking;

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

	/**	final Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				// Perform action on click
				Context context = getApplicationContext();
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, "Button pressed", duration);
				toast.show();
			}
		});**/

		TableLayout table = (TableLayout) findViewById(R.id.mytablelayout);

		//Rows
		for(int y=0; y<10; y++) 
		{
			TableRow row = new TableRow(this);
			//Columns
			for(int x=0; x<9; x++)
			{
				// create a new TextView         
				TextView t = new TextView(this);         
				t.setText(x+"."+y); //Set to any meaningful text

				if( x!=1 && x!=4 && x!=7 )
				{
					if(y!=0)
						t.setBackgroundColor( getResources().getColor(R.color.available) );
					else
						t.setBackgroundColor( getResources().getColor(R.color.unavailable) );
				}
				else
					t.setBackgroundColor( getResources().getColor(R.color.generic) );
				//handicapped
				if(x==2 && y==0)
					t.setBackgroundColor( getResources().getColor(R.color.handicapped) );

				row.addView(t); //Attach TextView to its parent (row)

				//Warning: do not call t.setLayoutParams(params)
				//before attaching the view to the parent, 
				//else null reference will result
				//Use the following lines only if special formatting
				//as shown below is needed. If not, skip.
				//
				//TableRow.LayoutParams params = 
					//	(TableRow.LayoutParams)t.getLayoutParams();
				//params.column= x; //place at xth columns.

				//Skip above line if being placed side by side
				//params.span = 1; //span these many columns, 

				//i.e merge these many cells. Skip if not needed
				//params.setMargins(2,2,2,2); //To "draw" margins

				//around (outside) the TextView, skip if not needed
				//params.width = TableRow.LayoutParams.FILL_PARENT;

				//Set width as needed (Important: this and the
				//.height below is for layout of "text" inside 
				//the TextView, not for layout of TextView' by its
				//parent)
				//params.height = TableRow.LayoutParams.WRAP_CONTENT;
				//t.setPadding(2, 2, 2, 2); 
				//Skip padding (space around text) above if not
				//needed
				//t.setLayoutParams(params); // causes layout update. 
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
