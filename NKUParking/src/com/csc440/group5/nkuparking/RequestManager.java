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
	private ArrayList<ParkingLot> lotInfo;
	
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
		//print out those error statements.
		//Otherwise, return null for successful registration.
		if(val.contains("0"))
		{
			//Just print out what is in the <body> tags
			String[] contents = val.split("(?=<body>)</body>");
			return contents[1];
		}
		else
			return null;
	}
	
	/*
	 * reserve spot stuff
	 */
	
	interface Reservation
	{
		//Hook for reserving a spot
		@GET("/hooks/hooks.php?id=space_reserve")
		ArrayList<String> reserveLot(@Query("space_id") int id);
	}
	
	public boolean reserveLot(int spaceId, int userId)
	{
		RestAdapter adapter = new RestAdapter.Builder()
			.setEndpoint(URL)
			.build();
		
		Reservation resService = adapter.create(Reservation.class);
		ArrayList<String> list = resService.reserveLot(spaceId);
		
		if(!list.get(0).contains("-1"))
		{
			//successfully reserved said spot
			return true;
		}
		else
			return false;
	}
	
	/*
	 * lot stuff
	 */
	
	interface Lot
	{
		//Hook for grabbing the lot data from the server
		@GET("/hooks/hooks.php?id=lots")
		ArrayList<ArrayList<String>> getLots();
	}
	
	public ArrayList<ParkingLot> getLotInformation()
	{
		//This method contacts the server and grabs all of the Lot information.
		RestAdapter adapter = new RestAdapter.Builder()
			.setEndpoint(URL)
			.build();
		Lot lotService = adapter.create(Lot.class);
		ArrayList<ArrayList<String>> list = lotService.getLots();
		
		//Refresh the list by clearing it and then assigning it to a new instance
		if(lotInfo == null)
			lotInfo = new ArrayList<ParkingLot>();
		else
			lotInfo.clear();
		
		//Create ParkingLot objects from the provided JSON data.
		for(int i = 0; i < list.size(); i++)
		{
			String name = (String)list.get(i).get(0);
			String desc = (String)list.get(i).get(0);
			double longitude = Double.valueOf((String)list.get(i).get(1));
			double latitude = Double.valueOf((String)list.get(i).get(2));
			String status = (String)list.get(i).get(3);
			int max = Integer.valueOf((String)list.get(i).get(4));
			lotInfo.add(new ParkingLot(name, desc, longitude, latitude, max, status));
		}
		
		return lotInfo;
	}
	
	/*
	 * spots stuff
	 */
	
	interface Spots
	{
		@GET("/hooks/hooks.php?id=spots")
		ArrayList<ArrayList<ArrayList<String>>> pullParkingLotInfo();
	}
	
	public ArrayList<ParkingLot> pullParkingLotInfo()
	{
		//This method contacts the server and grabs all of the space information.
		RestAdapter adapter = new RestAdapter.Builder()
			.setEndpoint(URL)
			.build();
		
		Spots spotService = adapter.create(Spots.class);
		ArrayList<ArrayList<ArrayList<String>>> list = spotService.pullParkingLotInfo();
		
		//Clear or create a new instance of the array
		if(lotInfo == null)
			lotInfo = new ArrayList<ParkingLot>();
		else
			lotInfo.clear();
		
		//Generate a lot object & subsequent space array for each lot object
		for(int i = 0; i < list.size(); i++)
		{
			ArrayList<ParkingSpace> spaces = new ArrayList<ParkingSpace>();
			
			String name = ((list.get(i)).get(0)).get(0);
			String lat = ((list.get(i)).get(0)).get(1);
			String longitude = ((list.get(i)).get(0)).get(2);
			String desc = ((list.get(i)).get(0)).get(3);
			String status = ((list.get(i)).get(0)).get(4);
			String max = ((list.get(i)).get(0)).get(5);
			ParkingLot lotObj = new ParkingLot(name, desc, Double.parseDouble(lat), Double.parseDouble(longitude), Integer.parseInt(max), status);
			
			for(int k = 1; k < list.get(i).get(k).size(); k++)
			{
				String id = ((list.get(i)).get(k)).get(0);
				//TODO: Update these values with the correct values from JSON
				spaces.add(new ParkingSpace(false, false, 0));
			}
			
			lotObj.setSpaces(spaces);
			lotInfo.add(lotObj);
		}
		
		return lotInfo;
	}
}
