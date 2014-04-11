/*
 * RequestManager.java
 * 
 * Class that does all of the server to client and client to server
 * communication for the application.
 */

package com.csc440.group5.nkuparking;

import java.io.InputStream;
import com.google.gson.*;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.*;
import retrofit.mime.TypedInput;

public class RequestManager 
{
	private final static String URL = "http://107.170.2.129/";
	private static RequestManager manager = null;
	private RequestManager() { }
	
	public static RequestManager getSharedInstance()
	{
		//Returns a shared instance of this controller to try and prevent race conditions
		if(manager == null)
			manager = new RequestManager();
		
		return manager;
	}
	
	/*
	 * To correctly use the login function of API:
	 * 
	 * 1) request a shared instance of this class via the getSharedInstance() method
	 * 2) call that instance name with the isCorrectLogin(username, password) method below
	 *    ex) req.isCorrectLogin("test", "test2");
	 * 3) the above should be called in an async task to make sure that you don't block the UI thread
	 * 3a) if done on the UI thread it will throw a network exception
	 * 4) the response from the isCorrectLogin() method is a boolean that is either true or false
	 * 	  true means that the username & password are correct and false means they are incorrect
	 */
	interface Login 
	{
		//Hook for the login checking.
		@GET("/hooks/hooks.php?id=login")
		Response loginUser(@Query("username") String user, @Query("password") String pass);
	}
	
	public boolean isCorrectLogin(String user, String pass)
	{
		//This method contacts the server and sees if the login information is correct.
		RestAdapter adapter = new RestAdapter.Builder()
			.setEndpoint(URL)
			.build();
		Login loginService = adapter.create(Login.class);
		
		//Grab the HTML response from the login.php output
		Response res = loginService.loginUser(user, pass);
		TypedInput inp = res.getBody();
		byte[] bytes = new byte[32];
		
		//Read in the bytes into a 32 index array
		try 
		{
			inp.in().read(bytes);
		} 
		catch (Exception e) 
		{
			throw new RuntimeException("error parsing the login from server");
		}
		
		//If there is a "1" in the output then it was successful, if not it is not a valid login.
		String val = new String(bytes);
		if(val.contains("1"))
			return true;
		else
			return false;
	}
}
