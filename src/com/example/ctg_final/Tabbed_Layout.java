package com.example.ctg_final;

import android.R;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class Tabbed_Layout extends TabActivity {

TabHost host;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabbed_layout);
		host = getTabHost();
		
		//intent to the tab search activity
		Intent i1 = new Intent(Tabbed_Layout.this, Search.class);
		TabSpec ts1 = host.newTabSpec("TAB-1").setIndicator("Search").setContent(i1);
		
		//intent to the tab Advance search activity
		Intent i2 = new Intent(Tabbed_Layout.this, Advance_Search.class);
		TabSpec ts2 = host.newTabSpec("TAB-2").setIndicator("Advance Search").setContent(i2);
		
		host.addTab(ts1);
		host.addTab(ts2);
		
		//for GPS enable disable when tab changed
		host.setOnTabChangedListener(new OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
				LocationManager mgr = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
				if(tabId.equals("TAB-2")){
					Intent next = new Intent("android.location.GPS_ENABLED_CHANGE");
					next.putExtra("enabled", true);
					sendBroadcast(next);
				}
				else
				{
					Intent next = new Intent("android.location.GPS_ENABLED_CHANGE");
					next.putExtra("enabled", false);
					sendBroadcast(next);
				}

			}
		});
	}
}
