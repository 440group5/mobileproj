/*
 * MainActivity.java
 * 
 * Implements the main activity of the mobile application.
 * 
 * -Jordan Bossman
 * 
 * Copyright 2014 CSCGroup5
 */

package com.csc440.group5.nkuparking;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.*;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;

public class MainActivity extends Activity implements OnTouchListener 
{
	private EditText userText, passText;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
    	//Method sets up the textfields and add listeners for input used later on.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        userText = (EditText)findViewById(R.id.userField);
        passText = (EditText)findViewById(R.id.passField);
        
        //Grab the top layout and add a touch event to close the android soft keyboard
        //when the view's background is selected.
        ScrollView layout = (ScrollView)findViewById(R.id.mainView);
        layout.setOnTouchListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void registerUser(View view)
    {
    	//Registers the user.
    	Log.v(null, "Going to registration page....");
    	Intent intent = new Intent(this, RegistrationActivity.class);
    	startActivity(intent);
    }
    
    public void loginUser(View view)
    {
    	//Checks to make sure that a valid user is attempting to login.
    	String username = userText.getText().toString();
    	String password = passText.getText().toString();
    	String concatUserPass = username + password;
    	
    	if(concatUserPass.equals("fakefake"))
    	{
    		//load the map view because the user is a valid user
    		//TODO: connect to server instead of mock data
    		Log.v(null, "Successfully logged the user in.....");
    		Intent intent = new Intent(this, MapPage.class);
    		startActivity(intent);
    	}
    	else
    	{
    		//Popup an alert view because the information was wrong
    		new AlertDialog.Builder(this)
    			.setTitle("Error")
    			.setMessage("The information is incorrect or there is not a valid username.")
    			.setPositiveButton(android.R.string.yes, null)
    			.show();
    	}
    }

	@Override
	public boolean onTouch(View view, MotionEvent event) 
	{
		//Close the keyboard when the background of the view is touched.
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			InputMethodManager manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
			return true;
		}
		return false;
	}
    
}
