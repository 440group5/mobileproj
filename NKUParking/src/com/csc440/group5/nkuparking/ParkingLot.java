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
	String description, name;
	
	ParkingLot(String name, String desc, double latitude, double longitude)
	{
		//Constructor that creates a ParkingLot object with a name, description,
		//latitude and longitude.
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
		this.description = desc;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public LatLng getCoordinate()
	{
		return new LatLng(latitude, longitude);
	}
}
