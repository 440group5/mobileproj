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

public class ParkingLot 
{
	private double latitude, longitude;
	private String description, name;
	private ParkingSpace[] spaces;
	private int status;
	public final static int STUDENT_LOT = 0, FACULTY_STAFF_LOT = 1, VISITOR_LOT = 2, OPEN_PARKING = 3, CLOSED_PARKING = 4;
	private double percentFilled = 0.0;
	
	public ParkingLot(String name, String desc, double latitude, double longitude, int numSpaces, int status)
	{
		//Constructor that creates a ParkingLot object with a name, description,
		//latitude and longitude.
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
		this.description = desc;
		spaces = new ParkingSpace[numSpaces];
		this.status = status;
	}
	
	public ParkingSpace[] getSpaces()
	{
		//Returns the list of spaces in this parking lot.
		return spaces;
	}
	
	public String getStatus()
	{
		//Returns the lot status of this lot.
		switch(status)
		{
			case STUDENT_LOT:
				return "Student Lot";
			case FACULTY_STAFF_LOT:
				return "Faculty/Staff Lot";
			case VISITOR_LOT:
				return "Visitor Lot";
			case OPEN_PARKING:
				return "Open Lot";
			case CLOSED_PARKING:
				return "Closed Lot";
			default:
				return null;
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
	
	public String getName()
	{
		//Returns this lot's name.
		return name;
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
