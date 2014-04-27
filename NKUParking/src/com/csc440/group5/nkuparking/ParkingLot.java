/*
 * ParkingLot.java
 * 
 * ParkingLot object for storage of the initial coordinates
 * for the map markers that will be displayed for information purposes.
 * 
 * -Jordan Bossman
 * 
 * Copyright (c) 2014 CSCGroup5
 */

package com.csc440.group5.nkuparking;

import com.google.android.gms.maps.model.LatLng;
import java.util.*;

public class ParkingLot 
{
	private double latitude, longitude;
	private String description;
	private String name;
	private ArrayList<ParkingSpace> spaces;
	private int max;
	public final static int STUDENT_LOT = 0, FACULTY_STAFF_LOT = 1, VISITOR_LOT = 2, OPEN_PARKING = 3, CLOSED_PARKING = 4;
	private double percentFilled = 0.0;
	private String status;
	
	public ParkingLot(String name, String desc, double latitude, double longitude, int numSpaces, int status)
	{
		//Constructor that creates a ParkingLot object with a name, description,
		//latitude and longitude.
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
		this.description = desc;
		spaces = new ArrayList<ParkingSpace>(numSpaces);
//		this.status = status;
		this.max = numSpaces;
	}
	
	public ParkingLot(String name, String desc, double latitude, double longitude, int numSpaces, String status)
	{
		//This constructor performs the same as the previous except it takes in a string status
		//from the server and symbolicates it to the proper integer value.
		this.latitude = latitude;
		this.longitude = longitude;
//		this.name = name.charAt(0);
		this.description = desc;
		spaces = new ArrayList<ParkingSpace>(numSpaces);
		
//		if(status.equals("Open"))
//			this.status = OPEN_PARKING;
//		else if(status.equals("Student"))
//			this.status = STUDENT_LOT;
//		else if(status.equals("Faculty_Staff"))
//			this.status = FACULTY_STAFF_LOT;
//		else if(status.equals("Visitor"))
//			this.status = VISITOR_LOT;
//		else if(status.equals("Closed"))
//			this.status = CLOSED_PARKING;
		
		this.max = numSpaces;
	}
	
	public char getCharName()
	{
		return (char)Integer.parseInt(name);
	}
	
	public ArrayList<ParkingSpace> getSpaces()
	{
		//Returns the list of spaces in this parking lot.
		return spaces;
	}
	
	public boolean checkIfSpotExpired(int spot_id)
	{
		RequestManager req = RequestManager.getSharedInstance();
		return req.checkIfSpotIsExpired(name, spot_id);
	}
	
	public boolean spotExpired(int spot_id, int user_id)
	{
		for(ParkingSpace space : spaces)
		{
			if(space.getSpotID() == spot_id)
			{
				RequestManager req = RequestManager.getSharedInstance();
				boolean status = req.spotAtLotExpired(name, spot_id, user_id);
				if(status)
					space.setExpire(false);
				
				return status;
			}
		}
		
		return false;
	}
	
	public int getNumSpaces()
	{
		return spaces.size() - 1;
	}
	
	public void setSpaces(ArrayList<ParkingSpace> spaces)
	{
		//Set the spaces of this lot object with the respective array
		if(this.spaces == null)
		{
			this.spaces = new ArrayList<ParkingSpace>();
			this.spaces.addAll(spaces);
		}
		else
		{
			this.spaces.clear();
			this.spaces.addAll(spaces);
		}
	}
	
	public double getPercentFilled()
	{
		//Returns the percentage filled of this lot.
		return percentFilled;
	}
	
	public void setPercentFilled(double value)
	{
		//Sets the percentage filled for this lot.
		percentFilled = value;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public ParkingSpace getSpaceAtId(int spot_id)
	{
		//Returns a ParkingSpace object for the respective spot id in this lot.
		for(int i = 0; i < spaces.size(); i++)
		{
			if(spaces.get(i).getSpotID() == spot_id)
				return spaces.get(i);
		}
		
		return null;
	}
	
	public String getStatus()
	{
		return status;
	}
	
	public String getDescription()
	{
		//Returns the description of this lot.
		return description;
	}
	
	public LatLng getCoordinate()
	{
		//Returns the coordinates of this lot.
		return new LatLng(latitude, longitude);
	}
}
