/*
 * ParkingSpace.java
 * 
 * Object for a specific parking space.
 */

package com.csc440.group5.nkuparking;

import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;

public class ParkingSpace 
{
	private boolean isAvailable = false;
	private boolean isHandicapped = false;
	private String expirationTime;
	private int spot_id;
	private int id;
	private int lot;
	private String lotName;
	private String status;
	private String reserve;
	private String expire;
	private int user_id;
	private String username;
	
	public ParkingSpace(boolean avail, boolean handicapped, int id)//, String expirationtime)
	{
		this.isAvailable = avail;
		this.isHandicapped = handicapped;
		this.spot_id = id;
		//self.expirationTime = expirationTime;
	}
	
	public void setExpire(boolean val)
	{
		if(!val)
			this.expire = "Open";
		else
			this.expire = "Closed";
	}
	
	public String getLotName()
	{
		return lotName;
	}
	
	public int getLot()
	{
		return lot;
//		return (char)lot;
	}
	
	public void setLotName(String name)
	{
		this.lotName = name;
	}
	
	public int getSpotID()
	{
		return spot_id;
	}
	
	public boolean isExpired()
	{
		//Returns if the space time has expired or not.
		return (expirationTime == null);
	}
	
	public boolean isHandicapped()
	{
		//Returns if the space is handicapped or not.
		return status.equals("Handicap");
	}
	
	public void setAvailable()
	{
		status = "Open";
	}
	
	public boolean setOccupied()
	{
//		return RequestManager.getSharedInstance().setSpotAsOccupied(lotName, spot_id);
//		return false;
		status = "Occupied";
		try 
		{
			return new SetOccupiedAsync().execute().get();
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public void setClosed()
	{
		status = "Closed";
	}
//	
//	public void setUnavailable()
//	{
//		//TODO: occupied
//		status = "Reserved";
//	}
	
	public boolean isAvailable()
	{
		//Returns if the space is available or not.
		if(status.equals("Open"))
			return true;
		else
			return false;
	}
	
	private class SetOccupiedAsync extends AsyncTask<Void, Void, Boolean>
	{

		@Override
		protected Boolean doInBackground(Void... params) 
		{
			return RequestManager.getSharedInstance().setSpotAsOccupied(lotName, spot_id);
		}
		
	}
}
