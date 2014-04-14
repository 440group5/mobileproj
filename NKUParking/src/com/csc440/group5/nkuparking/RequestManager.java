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
import java.util.*;

public class RequestManager 
{
	private final static RequestManager INSTANCE = new RequestManager();
	private final static String URL = "http://107.170.2.129/";
	private RequestManager() { }
	
	public static RequestManager getSharedInstance()
	{
		//Returns a shared instance of this controller to try and prevent race conditions
		return INSTANCE;
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
	 * 
	 * Example:
	 * 	  boolean correctLogin = request.isCorrectLogin("test", "test2");
	 *    if(correctLogin)
	 *    	successful login && username/pass are correct
	 *    else
	 *    	not successful, one or both of the username and pass are incorrect
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
			throw new RuntimeException("Error parsing the login from server");
		}
		
		//If there is a "1" in the output then it was successful, if not it is not a valid login.
		String val = new String(bytes);
		if(val.contains("1"))
			return true;
		else
			return false;
	}
	
	/*
	 * To correctly use the registration function of API:
	 * 
	 * 1) request a shared instance of this class via the getSharedInstance() method
	 * 2) use the method register(username, password, email) to send a registration request
	 * 3) if the string returned is null, then it correctly registered the user
	 * 	  but if it returns any information, it is the error(s) that the server
	 *    encountered while trying to register the user.
	 *    
	 * Example call: 
	 *    String temp = request.regsiter("test", "test2", "test@test.com");
	 *    if(temp != null)
	 *    	print temp;
	 *    else
	 *    	successful;
	 */
	
	interface Register
	{
		//Hook for registration of users
		@GET("/hooks/hooks.php?id=register")
		Response registerUser(@Query("username") String user, @Query("password") String pass, @Query("email") String email);
	}
	
	public String register(String user, String pass, String email)
	{
		//Method to register a user for the NKUParking app.
		RestAdapter adapter = new RestAdapter.Builder()
			.setEndpoint(URL)
			.build();
		
		Register regService = adapter.create(Register.class);
		
		//Send the request to register a person and grab the HTML
		Response res = regService.registerUser(user, pass, email);
		TypedInput inp = res.getBody();
		byte[] bytes = new byte[1024];
		
		//Read the HTML
		try
		{
			inp.in().read(bytes);
		}
		catch(Exception e)
		{
			throw new RuntimeException("Error parsing server information");
		}
		
		String val = new String(bytes);
		
		//If the HTML contains a "0", then it was not successful and has some error statements
		//print out those error statements (parsing the html tags).
		//Otherwise, return null for successful registration.
		if(val.contains("0"))
		{
			val.replaceAll("<.*?>", "");
			return val;
		}
		else
			return null;
	}
	
	/*
	 * lot stuff
	 */
	
	interface Lot
	{
		//Hook for grabbing the lot data from the server
		@GET("/hooks/hooks.php?id=lots")
		ArrayList<String> getLots();
	}
	
	public ArrayList<ParkingLot> getLotInformation()
	{
		//This method contacts the server and sees if the login information is correct.
		RestAdapter adapter = new RestAdapter.Builder()
			.setEndpoint(URL)
			.build();
		Lot lotService = adapter.create(Lot.class);
		ArrayList<String> list = lotService.getLots();
		ArrayList<ParkingLot> lotInfo = new ArrayList<ParkingLot>();
		
		for(int i = 0; i < list.size() - 1; i+=5)
		{
			int k = i;
			char name = ((String)list.get(k)).charAt(0);
			String desc = (String)list.get(k);
			double longitude = Double.valueOf((String)list.get(k+1));
			double latitude = Double.valueOf((String)list.get(k+2));
			String status = (String)list.get(k+3);
			int max = Integer.valueOf((String)list.get(k+4));
			lotInfo.add(new ParkingLot(name, desc, longitude, latitude, max, status));
		}
		
		return lotInfo;
	}
	
	/*
	 * spots stuff
	 */
	
	interface Spots
	{
		
	}
}
