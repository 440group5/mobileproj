/*
 * ParkingSpace.java
 * 
 * Object for a specific parking space.
 */

package com.csc440.group5.nkuparking;

public class ParkingSpace 
{
	private boolean isAvailable = false;
	private boolean isHandicapped = false;
	//TODO: expiration time
	private String expirationTime;
	
	public ParkingSpace(boolean avail, boolean handicapped)//, String exirationtime)
	{
		this.isAvailable = avail;
		this.isHandicapped = handicapped;
		//self.expirationTime = expirationTime;
	}
	
	public boolean isExpired()
	{
		//Returns if the space time has expired or not.
		return (expirationTime == null);
	}
	
	public boolean isHandicapped()
	{
		//Returns if the space is handicapped or not.
		return isHandicapped;
	}
	
	public boolean isAvailable()
	{
		//Returns if the space is available or not.
		return isAvailable;
	}
}
