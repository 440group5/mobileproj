package com.csc440.group5.nkuparking;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;

public class RequestManager 
{
	private final static String URL = "http://107.170.2.129/";
	
	interface Login 
	{
		@GET("/hooks/hooks.php?id=login&username={user}&password={pass}")
		void loginUser(@Path("user") String user, @Path("pass") String pass, Callback<Response> cb);
	}
	
	public static void main(String[] args)
	{
		RestAdapter adapter = new RestAdapter.Builder()
			.setEndpoint(URL)
			.build();
		
		Login loginService = adapter.create(Login.class);
		
		Callback callback = new Callback()
		{
			@Override
			public void success(Object o, Response response)
			{
				
			}
			
			@Override
			public void failure(RetrofitError error)
			{
				
			}
		};
		
		loginService.loginUser("", "", callback);
	}
}
