/*
 * RegistrationActivity.java
 * 
 * The page to register a new user for the app.
 * 
 * -Jordan Bossman
 * 
 * Copyright 2014 CSCGroup5
 */

package com.csc440.group5.nkuparking;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class RegistrationActivity extends Activity implements OnItemSelectedListener 
{
	private EditText userText, passText, firstText, lastText;
	private Spinner spinner;
	//Determines if the user is student or faculty/staff.
	boolean isStudent, isFacultyStaff;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		//Method grabs all of the needed items and add listeners to them.
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_page);
		
		//Grab all of the textfields.
		userText = (EditText)findViewById(R.id.reguserfield);
		passText = (EditText)findViewById(R.id.regpassfield);
		firstText = (EditText)findViewById(R.id.firstnamefield);
		lastText = (EditText)findViewById(R.id.lastnamefield);
		
		//Add listeners to each of the textfields to close the keyboard.
        OnFocusChangeListener listener = new KeyboardFocusChangeListener();		
		userText.setOnFocusChangeListener(listener);
		passText.setOnFocusChangeListener(listener);
		firstText.setOnFocusChangeListener(listener);
		lastText.setOnFocusChangeListener(listener);
		
		//Add the options to the spinner (dropdown box for student & faculty/staff).
		spinner = (Spinner)findViewById(R.id.class_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.class_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
	}
	
    private class KeyboardFocusChangeListener implements OnFocusChangeListener
    {
    	//This private class closes the keyboard of the username/password textviews when they lose focus.
		@Override
		public void onFocusChange(View v, boolean hasFocus) 
		{
			if(hasFocus)
			{
				//Should close the keyboard if the user clicks away.
				InputMethodManager manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
		}
    }
	
	public void register(View view)
	{
		//Build up the error so the user gets all of the errors that happened
		StringBuffer error = new StringBuffer();
		
		//Check for first name.
		if(firstText.getText().toString().equals(""))
			error.append("Please enter your first name\n");
		
		//Check for last name.
		if(lastText.getText().toString().equals(""))
			error.append("Please enter your last name\n");
		
		//Check for username.
		if(userText.getText().toString().equals(""))
			error.append("Please enter a username\n");
		
		//Check for password.
		if(passText.getText().toString().equals(""))
			error.append("Please enter a password\n");
		
		//If both of these are false, then the user chose the "-----" option, which is invalid.
		if(!isStudent && !isFacultyStaff)
			error.append("Please make sure you have set your classification\n");
			
		//If the error string has been built, then there was an error, if not, there was not an error 
		//and the app should continue normal execution.
		if(!error.toString().equals(""))
		{
    		new AlertDialog.Builder(this)
			.setTitle("Error")
			.setMessage(error)
			.setPositiveButton(android.R.string.ok, null)
			.show();
		}
		else
		{
			//Load up the normal map activity page.
    		Log.v(null, "Successfully registered, logging in....");
    		Intent intent = new Intent(this, MapPage.class);
    		startActivity(intent);
		}
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
	{
		//Determines which option for the dropdown list has been clicked on.
		switch(pos)
		{
			case 0: //The '-------' option.
				isStudent = false;
				isFacultyStaff = false;
				break;
			case 1: //The 'Student' option.
				isStudent = true;
				isFacultyStaff = false;
				break;
			case 2: //The 'Faculty/Staff' option.
				isFacultyStaff = true;
				isStudent = false;
				break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) 
	{
		// TODO Auto-generated method stub
		
	}
}
