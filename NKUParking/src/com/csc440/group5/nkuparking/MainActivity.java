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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class MainActivity extends Activity 
{
	private EditText userText, passText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        userText = (EditText)findViewById(R.id.userField);
        passText = (EditText)findViewById(R.id.passField);
        
        OnFocusChangeListener listener = new KeyboardFocusChangeListener();
        userText.setOnFocusChangeListener(listener);
        passText.setOnFocusChangeListener(listener);
    }
    
    private class KeyboardFocusChangeListener implements OnFocusChangeListener
    {
    	//This private class closes the keyboard of the username/password textviews when they lose focus.
		@Override
		public void onFocusChange(View v, boolean hasFocus) 
		{
			if(hasFocus)
			{
				InputMethodManager manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
		}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void registerUser(View view)
    {
    	//Registers the user as a new user
    	//TODO: add this
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
    		//popup an alert view because the information was wrong
    		new AlertDialog.Builder(this)
    			.setTitle("Error")
    			.setMessage("The information is incorrect or there is not a valid username.")
    			.setPositiveButton(android.R.string.yes, null)
    			.show();
    	}
    }
    
}
