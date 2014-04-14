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

import java.security.MessageDigest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;

public class RegistrationActivity extends Activity implements OnItemSelectedListener, OnTouchListener 
{
	private EditText userText, passText, retypePassText, emailText;
	private Spinner spinner;
	//Determines if the user is student or faculty/staff.
	boolean isStudent, isFacultyStaff, isVisitor, agreementChecked;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		//Method grabs all of the needed items and add listeners to them.
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_page);
		
		//Grab all of the textfields.
		userText = (EditText)findViewById(R.id.reguserfield);
		passText = (EditText)findViewById(R.id.regpassfield);
		retypePassText = (EditText)findViewById(R.id.retypepassfield);
		emailText = (EditText)findViewById(R.id.emailField);
//		firstText = (EditText)findViewById(R.id.firstnamefield);
//		lastText = (EditText)findViewById(R.id.lastnamefield);
		
		//Grab the view and register a touch listener to close the android soft
		//keyboard when it is touched.
		ScrollView layout = (ScrollView)findViewById(R.id.registrationLayout);
		layout.setOnTouchListener(this);
		
		//Add the options to the spinner (dropdown box for student & faculty/staff).
		spinner = (Spinner)findViewById(R.id.class_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.class_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
	}
	
	public void register(View view)
	{
		//Build up the error so the user gets all of the errors that happened
		StringBuffer error = new StringBuffer();
		
		//Check for username.
		if(userText.getText().toString().equals(""))
			error.append("Please enter a username\n");
		
		//Check for password.
		if(passText.getText().toString().equals(""))
			error.append("Please enter a password\n");
		
		//Check to make sure that the agreement checkbox is checked.
		if(!agreementChecked)
			error.append("Please make sure you have agreed to the agreement\n");
		
		if(!emailText.equals(""))
			error.append("Please make sure your email is properly filled out");
		
		//Check to make sure the retype password field has text
		if(retypePassText.getText().toString().equals(""))
			error.append("Please retype your password\n");
		else
		{
			//Make sure both passwords entered by the user are the same
			if(!passText.getText().toString().equals(retypePassText.getText().toString()))
				error.append("The two password fields do not match");
		}
			
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
			//Hash the username and password then send it to the server
			//TODO: uncomment this code when connected to server
			/*
    		MessageDigest digester;
    		try
    		{
    			digester = MessageDigest.getInstance("SHA256");
    			byte[] bytes = (userText.getText().toString() + passText.getText().toString()).getBytes("UTF8");
    			digester.update(bytes, 0, bytes.length);
    			String hashedUserPass = new String(digester.digest());
    			
    			//TODO: send hashedUserPass to server and get confirmation of registration
    		}
    		catch(Exception e)
    		{
        		new AlertDialog.Builder(this)
    			.setTitle("Error")
    			.setMessage("There was an error while creating your account, please try again later.")
    			.setPositiveButton(android.R.string.ok, null)
    			.show();
    		}
    		*/
    		
    		Log.v(null, "Successfully registered, logging in....");
    		Intent intent = new Intent(this, MapPage.class);
    		startActivity(intent);
		}
		
	}
	
    public boolean onPrepareOptionsMenu(Menu menu)
    {
    	//Do not show the menu on the login page 
    	return false;
    }
	
	public void agreementClicked(View view)
	{
		agreementChecked = ((CheckBox) view).isChecked();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
	{
		//Determines which option for the dropdown list has been clicked on.
		switch(pos)
		{
			case 0: //The 'Visitor' option.
				isStudent = false;
				isFacultyStaff = false;
				isVisitor = true;
				break;
			case 1: //The 'Student' option.
				isStudent = true;
				isFacultyStaff = false;
				isVisitor = false;
				break;
			case 2: //The 'Faculty/Staff' option.
				isFacultyStaff = true;
				isStudent = false;
				isVisitor = false;
				break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) 
	{
		//Close the keyboard when the background view has been selected.
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			InputMethodManager manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
			return true;
		}
		return false;
	}
}
