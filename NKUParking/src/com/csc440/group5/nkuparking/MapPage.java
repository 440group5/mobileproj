/*
 * MapPage.java
 * 
 * Implements the actual google maps page of the app.
 * 
 * -Jordan Bossman
 * 
 * Copyright 2014 CSCGroup5
 */

package com.csc440.group5.nkuparking;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebView;

public class MapPage extends Activity
{
	private WebView webView;
	private final String MAP_URL = "https://www.google.com/maps?ie=UTF8&t=m&ll=39.031819,-84.462848&spn=0.011668,0.031543&z=15&source=embed";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_page);
        
        webView = (WebView)findViewById(R.id.webView);
      
        Log.v(null, "Loading the map.....");
        
        //Ignore this warning about XSS because it is going to an already good URL.
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.loadUrl(MAP_URL);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
