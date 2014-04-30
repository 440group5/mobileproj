/*
 * RequestManager.java
 * 
 * Class that does all of the server to client and client to server
 * communication for the application.
 */

package com.csc440.group5.nkuparking;

import android.util.Log;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.http.*;
import retrofit.mime.TypedInput;
import java.util.*;

public class RequestManager 
{
	private final static RequestManager INSTANCE = new RequestManager();
	private final static String URL = "http://107.170.2.129/";
	private RequestManager() { }
	private Map<String, ParkingLot> lotInfo = new HashMap<String, ParkingLot>();
	private ArrayList<ParkingSpace> parkingSpaceList = new ArrayList<ParkingSpace>();
//	private ArrayList<ParkingLot> lotInfo = new ArrayList<ParkingLot>();
	
	public static RequestManager getSharedInstance()
	{
		//Returns a shared instance of this controller to try and prevent race conditions
		return INSTANCE;
	}
	
	public Map<String, ParkingLot> getParkingLotMap(boolean sync)
	{
		if(sync || lotInfo == null)
			return pullParkingLotInformation();
		else
			return lotInfo;
	}
	
	/**
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
	 * 	  int correctLogin = request.isCorrectLogin("test", "test2");
	 *    if(correctLogin > 0)
	 *    	successful login && username/pass are correct
	 *    	and correctLogin = the userID
	 *    else
	 *    	not successful, one or both of the username and pass are incorrect
	 */
	
	interface Login 
	{
		//Hook for the login checking.
		@GET("/hooks/hooks.php?id=login")
		Response loginUser(@Query("username") String user, @Query("password") String pass);
	}
	
	public Map<String, Integer> isCorrectLogin(String user, String pass)
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

//		List<String> loginValue = 
//		loginService.loginUser(user, pass);
//		int id = Integer.parseInt(loginValue.get(0));
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
		String temp = new String(bytes);
//		String[] val = temp.split("<body>");
//		val = val[1].split("</body>");
		String[] val = temp.split("\"");
		Log.w("JSON", val[1]);
		
		try
		{
			int user_id = Integer.parseInt(val[3]);
			int status = Integer.parseInt(val[7]);
			Map<String, Integer> map = new HashMap<String, Integer>();
			
			if(user_id > 0 && status > 0)
			{
				map.put("user_id", user_id);
				map.put("status", status);
				
				return map;
			}
			else
				return null;
		}
		catch(Exception e)
		{
			String test = Log.getStackTraceString(e);
			Log.e("debugger", test);
			
			return null;
		}
//		
//		if(!val[3].contains("0"))
//			return -1;
//		else
//			return Integer.parseInt(val[1]);
//		return 0;
//		if(id == 0)
//			return -1;
//		else
//			return id;
	}
	
	/**
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
		Response registerUser(@Query("username") String user, @Query("password") String pass, @Query("selection") int status);
	}
	
	public int register(String user, String pass, int status)
	{
		//Method to register a user for the NKUParking app.
		RestAdapter adapter = new RestAdapter.Builder()
			.setEndpoint(URL)
			.build();
		
		Register regService = adapter.create(Register.class);
		
		//Send the request to register a person and grab the HTML
		Response res = regService.registerUser(user, pass, status);
		TypedInput inp = res.getBody();
		byte[] bytes = new byte[1024];
		
		//Read the HTML
		try
		{
			inp.in().read(bytes);
			String val = new String(bytes);
//			val = val.replace("\"", "");
//			val = val.replace(System.getProperty("line.separator"), "");
			val = val.replaceAll("[^0-9]", "");
			
			if(val.contains("-1"))
				return -1;
			else
				return (Integer)Integer.parseInt(val);
		}
		catch(Exception e)
		{
//			throw new RuntimeException("Error parsing server information");
			return -1;
		}
		
//		String val = new String(bytes);
		
		//If the HTML contains a "0", then it was not successful and has some error statements
		//print out those error statements.
		//Otherwise, return null for successful registration.
//		if(val.contains("-"))
//		{
//			//Just print out what is in the <body> tags
//			String[] contents = val.split("(?=<body>)</body>");
//			return contents[1];
//		}
//		else
//			return null;
	}
	
	/**
	 * This method reserves a given spot.
	 * To call this method, you need the spot id, user id, lot name and status of the user.
	 * To get the user_id and status from the user, you can easily just pull it from preferences.
	 */
	interface Reservation
	{
		//Hook for reserving a spot
		@GET("/hooks/hooks.php?id=reserve")
		Response reserveLot(@Query("spot") int spot_id, @Query("user_id") int user_id, @Query("lot") String lot, @Query("status") int status);
	}
	
	/**
	 * This method reserves a given spot.
	 * To call this method, you need the spot id, user id, lot name and status of the user.
	 * To get the user_id and status from the user, you can easily just pull it from preferences.
	 */
	public boolean reserveLot(int spot_id, int user_id, String lot, String userStatus)
	{
		RestAdapter adapter = new RestAdapter.Builder()
			.setEndpoint(URL)
			.build();
		
		//Associate the correct number to the string status
		int status = 0;
		try
		{
			if(userStatus.equals("Admin"))
				status = 1;
			else if(userStatus.equals("Faculty/Staff"))
				status = 2;
			else if(userStatus.equals("Student"))
				status = 3;
			else
				throw new Exception();
		}
		catch(Exception e)
		{
			throw new RuntimeException("User status is an incorrect string or not set.");
		}
		
		Reservation resService = adapter.create(Reservation.class);
		
		//Send the request to register a person and grab the HTML
		Response res = resService.reserveLot(spot_id, user_id, lot, status);
		TypedInput inp = res.getBody();
		byte[] bytes = new byte[1024];
		
		//Read the HTML
		try
		{
			inp.in().read(bytes);
			String val = new String(bytes);
			if(val.contains("-1"))
				return false;
			else
				return true;
		}
		catch(Exception e)
		{
//			throw new RuntimeException("Error parsing server information");
			return false;
		}
		
//		if(!list.get(0).contains("-1"))
//		{
//			//successfully reserved said spot
//			lotInfo.get(lot).getSpaceAtId(spot_id).setUnavailable();
//			return true;
//		}
//		else
//			return false;
	}
	
	/**
	 * Call the getParkingLotInformation() and it will return a map of parking lots
	 * Each parking lot can be accessed by its appropriate key which is its name
	 */
	
	interface Lot
	{
		//Hook for grabbing the lot data from the server
		@GET("/hooks/hooks.php?id=lots")
		ArrayList<ParkingLot> getLots();
	}
	
	public Map<String, ParkingLot> pullParkingLotInformation()
	{
		boolean result = syncSpacesWithLots();
		if(result)
			return lotInfo;
		else
			return null;
	}

	public Map<String, ParkingLot> getLotInformation()
	{
		//This method contacts the server and grabs all of the Lot information.
		RestAdapter adapter = new RestAdapter.Builder()
			.setEndpoint(URL)
			.build();
		Lot lotService = adapter.create(Lot.class);
		ArrayList<ParkingLot> list = lotService.getLots();
		lotInfo = new HashMap<String, ParkingLot>();
		
		for(int i = 0; i < list.size(); i++)
		{
			String temp = list.get(i).getName();
			list.get(i).setName(String.format("%c", (char)Integer.parseInt(temp)));
			lotInfo.put(list.get(i).getName(), list.get(i));
		}
		
		return lotInfo;
	}

	/*
	 * Call the pullSpotsForLot(Lot) function, which will also pull the whole dataset
	 * If you need the whole dataset, just call it in a loop on each lot
	 */
	
	interface Spots
	{
		@GET("/hooks/hooks.php?id=spots")
		ArrayList<ParkingSpace> pullParkingSpaceInfo();
	}
	
	public ArrayList<ParkingSpace> pullParkingSpaceInfo()
	{
		//This method contacts the server and grabs all of the space information.
		RestAdapter adapter = new RestAdapter.Builder()
			.setEndpoint(URL)
			.build();
		
		Spots spotService = adapter.create(Spots.class);
		ArrayList<ParkingSpace> list = spotService.pullParkingSpaceInfo();
		parkingSpaceList = new ArrayList<ParkingSpace>(list);
		//Clear or create a new instance of the array
//		if(lotInfo == null)
//			lotInfo = new ArrayList<ParkingLot>();
//		else
//			lotInfo.clear();
		
		//Generate a lot object & subsequent space array for each lot object
//		for(int i = 0; i < list.size(); i++)
//		{
//			ArrayList<ParkingSpace> spaces = new ArrayList<ParkingSpace>();
//			
//			String name = ((list.get(i)).get(0)).get(0);
//			String lat = ((list.get(i)).get(0)).get(1);
//			String longitude = ((list.get(i)).get(0)).get(2);
//			String desc = ((list.get(i)).get(0)).get(3);
//			String status = ((list.get(i)).get(0)).get(4);
//			String max = ((list.get(i)).get(0)).get(5);
//			ParkingLot lotObj = new ParkingLot(name, desc, Double.parseDouble(lat), Double.parseDouble(longitude), Integer.parseInt(max), status);
//			
//			for(int k = 1; k < list.get(i).get(k).size(); k++)
//			{
//				String id = ((list.get(i)).get(k)).get(0);
//				spaces.add(new ParkingSpace(false, false, 0));
//			}
//			
//			lotObj.setSpaces(spaces);
////			lotInfo.add(lotObj);
//		}
		return parkingSpaceList;
//		return lotInfo;
	}
	
	public boolean syncSpacesWithLots()
	{
		try
		{
			lotInfo = getLotInformation();
			parkingSpaceList = pullParkingSpaceInfo();
			ArrayList<ParkingSpace> temp = new ArrayList<ParkingSpace>(parkingSpaceList);
			ArrayList<ParkingSpace> spaces;
		
			Set<String> keys = lotInfo.keySet();
			for(String key : keys)
			{
				ParkingLot lot = lotInfo.get(key);
				spaces = new ArrayList<ParkingSpace>();
				
				for(int i = 0; i < temp.size(); i++)
				{
					ParkingSpace space = temp.get(i);
					space.setLotName(String.format("%c", (char)space.getLot()));
					if(space.getLotName().equals(lot.getName()))
					{
						spaces.add(space);
//						temp.remove(i);
					}
				}
				
				lot.setSpaces(spaces);
				spaces = new ArrayList<ParkingSpace>();
			}
		
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
//	public ArrayList<ParkingSpace> getSpacesForLot(ParkingLot lot)
//	{
////		ArrayList<ParkingSpace> newList = new ArrayList<ParkingSpace>();
////		if(lotInfo == null)
////			lotInfo = getLotInformation();
////		
////		parkingSpaceList = pullParkingSpaceInfo();
////		
////		for(ParkingSpace space : parkingSpaceList)
////		{
////			if(space.getLotName() == lot.getCharName())
////				newList.add(space);
////		}
//		lot.setSpaces(newList);
//		return newList;
//	}
	

	/**
	 * User hook
	 * This returns the spot that a user has registered
	 * or null if the user has nothing registered.
	 * It requires the username to be passed in.
	 */
	interface User
	{
		@GET("/hooks/hooks.php?id=user")
		Response getUserSpotInfo(@Query("username") String user);
	}
	
	/**
	 * User hook
	 * This returns the spot that a user has registered
	 * or null if the user has nothing registered.
	 * It requires the username to be passed in.
	 */
	public ParkingSpace getUserSpotInfo(String user)
	{
		//Method to check if a spot is expired or not
		RestAdapter adapter = new RestAdapter.Builder()
			.setEndpoint(URL)
			.build();
		
		User userService = adapter.create(User.class);
		
		//Send the request
		Response res = userService.getUserSpotInfo(user);
		TypedInput inp = res.getBody();
		byte[] bytes = new byte[32];
		
		try 
		{
			inp.in().read(bytes);
		} 
		catch (Exception e) 
		{
			throw new RuntimeException("Error parsing from the server");
		}
		
		String temp = new String(bytes);
		String[] val = temp.split("\"");
		Log.w("JSON", val[1]);
		
		//Grab the spot id & lot name from the json or throw return null
		try
		{
			int spot_id = Integer.parseInt(val[3]);
			String lot = val[7];
			
			return lotInfo.get(lot).getSpaceAtId(spot_id);
		}
		catch(Exception e)
		{
			String test = Log.getStackTraceString(e);
			Log.e("debugger", test);
			
			return null;
		}
	}
	

	
	interface CheckExpire
	{
		@GET("/hooks/hooks.php?id=checkexpire")
		Response checkIfSpotIsExpired(@Query("lot") String lotName, @Query("spot") int spot_id);
	}
	
	/**
	 * Check if space is expired
	 * This will not be called directly, but from the ParkingLot class
	 */
	public boolean checkIfSpotIsExpired(String lotName, int spot_id)
	{
		//Method to check if a spot is expired or not
		RestAdapter adapter = new RestAdapter.Builder()
			.setEndpoint(URL)
			.build();
		
		CheckExpire expireService = adapter.create(CheckExpire.class);
		
		//Send the request
		Response res = expireService.checkIfSpotIsExpired(lotName, spot_id);
		TypedInput inp = res.getBody();
		byte[] bytes = new byte[32];
		
		//Read the HTML
		try
		{
			inp.in().read(bytes);
			String val = new String(bytes);
			if(val.contains("-1"))
				return false;
			else
				return true;
		}
		catch(Exception e)
		{
//			throw new RuntimeException("Error parsing server information");
			return false;
		}
	}
	
	/**
	 * Will tell the server that the given spot in the given lot has expired
	 * Not for use to be called directly, will be used by the ParkingLot class
	 * To get the User ID for the account on this device = getSharedPreferences("NKUParkingPrefs,0);
	 * Then get the int from the key "User_id" and that is the id to pass in
	 */
	
	interface SpotExpired
	{
		@GET("/hooks/hooks.php?id=expire")
		Response spotAtLotExpired(@Query("lot") String lotName, @Query("spot") int spot_id, @Query("user_id") int user_id);
	}
	
	public boolean spotAtLotExpired(String lotName, int spot_id, int user_id)
	{
		//Method to tell the server that a spot expired, true means it was success
		//and false means that the server update failed
		RestAdapter adapter = new RestAdapter.Builder()
			.setEndpoint(URL)
			.build();
		
		SpotExpired expireService = adapter.create(SpotExpired.class);
		
		//Send the request
		Response res = expireService.spotAtLotExpired(lotName, spot_id, user_id);
		TypedInput inp = res.getBody();
		byte[] bytes = new byte[32];
		
		//Read the HTML
		try
		{
			inp.in().read(bytes);
			String val = new String(bytes);
			if(val.contains("-1"))
				return false;
			else
			{
				lotInfo.get(lotName).getSpaceAtId(spot_id).setAvailable();
				return true;
			}
		}
		catch(Exception e)
		{
//			throw new RuntimeException("Error parsing server information");
			return false;
		}
	}
	
	/** 
	 * Sets a specified spot id with string lot name as occupied.
	 */
	
	interface Occupied
	{
		@GET("/hooks/hooks.php?id=make_occupied")
		Response setSpotAsOccupied(@Query("lot") String lotName, @Query("spot") int spot_id);
	}
	
	public boolean setSpotAsOccupied(String lotName, int spot_id)
	{
		//Method to tell the server that a spot is occupied.
		RestAdapter adapter = new RestAdapter.Builder()
			.setEndpoint(URL)
			.build();
		
		Occupied occupiedService = adapter.create(Occupied.class);
		
		//Send the request
		Response res = occupiedService.setSpotAsOccupied(lotName, spot_id);
		TypedInput inp = res.getBody();
		byte[] bytes = new byte[32];
		
		//Read the HTML
		try
		{
			inp.in().read(bytes);
			String val = new String(bytes);
			if(val.contains("-1"))
				return false;
			else
				return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
}
