/*
 * MainActivity.java
 * 
 * Implements the main activity of the mobile application.
 * 
 * -Jordan Bossman
 * 
 * 
 * Copyright 2014 CSCGroup5
 */

package com.csc440.group5.nkuparking;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.*;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnTouchListener 
{
	private EditText userText, passText;
	private MessageDigest digester;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
    	//Method sets up the textfields and add listeners for input used later on.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        userText = (EditText)findViewById(R.id.userField);
        passText = (EditText)findViewById(R.id.passField);

        SharedPreferences settings = getSharedPreferences("NKUParkingPrefs", 0);
        String userPassHash = settings.getString("UserPass", null);
        String username = settings.getString("Username", null);
        String password = settings.getString("Password", null);
        boolean shouldAutoLogin = settings.getBoolean("AutoLogin", false);
        if(shouldAutoLogin)
        {
        	//TODO: update to use hashed user & pass not plain pass
        	if(username != null && password != null)
        	{
                LoginAsync asyncLogin = new LoginAsync();
                Map<String,Integer> loginCreds;
                try 
                {
                	loginCreds = asyncLogin.execute(username, password).get();
                }
                catch (Exception e)
                {
            		new AlertDialog.Builder(this)
        			.setTitle("Error")
        			.setMessage("There was an error contacting the server for login.")
        			.setPositiveButton(android.R.string.yes, null)
        			.show();
                	return;
                }
                
        		//Successfully logged in, load the map page
        		Log.v(null, "Successfully logged the user in.....");
        		
        		int temp = loginCreds.get("status");
        		String status; 
        		if(temp == 2)
        			status = "Faculty/Staff";
        		else if(temp == 3)
        			status = "Student";
        		else
                {
            		new AlertDialog.Builder(this)
        			.setTitle("Error")
        			.setMessage("There was an error processing the input for the login.")
        			.setPositiveButton(android.R.string.yes, null)
        			.show();
                	return;
                }
        		
        		Editor editor = settings.edit();
        		editor.putInt("User_id", loginCreds.get("user_id"));
        		editor.putString("Status", status);
        		editor.commit();
        		
        		Intent intent = new Intent(this, MapPage.class);
        		startActivity(intent);
        	}
        }
        
        //Grab the top layout and add a touch event to close the android soft keyboard
        //when the view's background is selected.
        ScrollView layout = (ScrollView)findViewById(R.id.mainView);
        layout.setOnTouchListener(this);
    }
    
    private class LoginAsync extends AsyncTask<String, Void, Map<String,Integer>>
	{
    	//Asynchronously goes and sees if the login information is correct
		@Override
		protected Map<String,Integer> doInBackground(String... params)
		{
			RequestManager request = RequestManager.getSharedInstance();
			return request.isCorrectLogin(params[0], params[1]);
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.context_menu, menu);
        return true;
    }
    
    public boolean onPrepareOptionsMenu(Menu menu)
    {
    	//Do not show the menu on the login page 
    	return false;
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
    	
//		Intent intent = new Intent(this, MapPage.class);
//		startActivity(intent);
    	
    	//Checks to make sure that a valid user is attempting to login.
    	String username = userText.getText().toString();
    	String password = passText.getText().toString();
    	String concatUserPass = username + password;
    	
    	MessageDigest digester;
    	//Do the SHA256 hash, if there was an error pop up an alert dialog and exit.
    	try 
    	{
    		digester = MessageDigest.getInstance("SHA256");
    		byte[] bytes = concatUserPass.getBytes("UTF8");
    		digester.update(bytes, 0, bytes.length);
    		String hashedUserPass = new String(digester.digest());
    		
    		//Send the information to the server to see if it's correct
            LoginAsync asyncLogin = new LoginAsync();
            Map<String,Integer> loginCreds;
            try 
            {
            	loginCreds = asyncLogin.execute(username, password).get();
            }
            catch (Exception e)
            {
        		new AlertDialog.Builder(this)
    			.setTitle("Error")
    			.setMessage("There was an error contacting the server for login.")
    			.setPositiveButton(android.R.string.yes, null)
    			.show();
            	return;
            }
            
    		//Successfully logged in, load the map page
    		Log.v(null, "Successfully logged the user in.....");
    		
//    		int temp = loginCreds.get("status");
//    		String status; a
//    		if(temp == 1)
//    			status = "Admin";
//    		else if(temp == 2)
//    			status = "Faculty/Staff";
//    		else if(temp == 3)
//    			status = "Student";
//    		else
//            {
//        		new AlertDialog.Builder(this)
//    			.setTitle("Error")
//    			.setMessage("There was an error processing the input for the login.")
//    			.setPositiveButton(android.R.string.yes, null)
//    			.show();
//            	return;
//            }
    		
    		SharedPreferences settings = getSharedPreferences("NKUParkingPrefs", 0);
    		Editor editor = settings.edit();
    		editor.putInt("User_id", loginCreds.get("user_id"));
    		editor.putInt("Status", loginCreds.get("status"));
    		editor.commit();
    		
    		Intent intent = new Intent(this, MapPage.class);
    		startActivity(intent);
    	}
    	catch(Exception e)
    	{
    		new AlertDialog.Builder(this)
    			.setTitle("Error")
    			.setMessage("There was an internal error and login cannot be completed.")
    			.setPositiveButton(android.R.string.ok, null)
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
