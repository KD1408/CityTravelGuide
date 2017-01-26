package com.example.ctg_final;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class City_Selection extends Activity {
	Button b1,b2;
	TextView tv1;
	Intent next;
	ListView lst1;
	String[] drawerListItems;
	
	DatabaseHandler db = new DatabaseHandler(City_Selection.this);
	
	//sensor code starts
	SensorManager sMgr = null;
	List<Sensor> sensorList;
	
	SensorEventListener sensEvent = new SensorEventListener() {
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			float[] values = event.values;
			
			int x_color = (int)(Math.abs(values[0]) * 125);
			int y_color = (int)(Math.abs(values[1]) * 125);
			int z_color = (int)(Math.abs(values[2]) * 125);
			
			tv1.setTextColor(Color.rgb(x_color, y_color, z_color));
			}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};// sensor code ends
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_selection);
		
		String fontPath= "Fonts/ALGER.TTF";
		
		b1 = (Button)findViewById(R.id.btnAhm);
		b2 = (Button)findViewById(R.id.btnVdr);
		tv1 = (TextView)findViewById(R.id.tvFont);
		lst1 = (ListView)findViewById(R.id.lvDrawer);
		
		//Defining the drawer list items and getting the items from the res>values>string.xml
		drawerListItems = getResources().getStringArray(R.array.slider_items);
		lst1.setAdapter(new ArrayAdapter<String>(City_Selection.this,
						R.layout.drawer_listview_items,drawerListItems));//setting adapter in listview of sliding menu
		
		lst1.setOnItemClickListener(new drawerItemClickListener());
		
		 getActionBar().setDisplayHomeAsUpEnabled(true);
		 getActionBar().setHomeButtonEnabled(true);
		 
		 
		//CTG background code starts
		Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
		
		tv1.setTypeface(tf);
		
		sMgr = (SensorManager)getSystemService(SENSOR_SERVICE);
		if (sMgr != null)
		{
			// we have sensor hardware 
			sensorList = sMgr.getSensorList(Sensor.TYPE_ACCELEROMETER);
			String items = "";
			
			if (sensorList.size() > 0)
			{
				// make sensor activate and perform task
				sMgr.registerListener(sensEvent, sensorList.get(0), SensorManager.SENSOR_DELAY_NORMAL);
			}
		}
		else
		{
			Toast.makeText(City_Selection.this, "No Sensor Service is available...", Toast.LENGTH_LONG).show();
		}
		// CTG background code ends
		
		
		b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				next = new Intent(City_Selection.this,Tabbed_Layout.class);
				startActivity(next);
				finish();
			}
		});
		
		b2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(City_Selection.this, "No data available for VADODARA...We are back soon..!!", 3000).show();
			}
		});
	
	}
	

	//slidingmenu items click listener
	private class drawerItemClickListener implements ListView.OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			displayView(position);
		}
		
	}
	
	private void displayView(int position){
		Intent next;
		switch(position){
		case 0:
			MediaPlayer mp = new MediaPlayer();
			mp = MediaPlayer.create(getBaseContext(), R.raw.sound1);
			mp.start();
			
			break;
		case 1:
			//for grabbing data from server
			JsonLoader jsnLdr = new JsonLoader();
			jsnLdr.execute();
			
			break;
			
		case 2:
			
			next= new Intent(City_Selection.this,Subscribption.class);
			startActivity(next);
			finish();
			
			break;
			
		case 3:
			next = new Intent(City_Selection.this,About_us.class);
			startActivity(next);
			finish();
			
			break;
			
		case 4:
			
			next = new Intent(City_Selection.this,How_to.class);
			startActivity(next);
			finish();
			
		default:
			
			break;
		}
		
	}
	
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this).setTitle("CONFIRM EXIT")
									.setMessage("Do you Want to exit City Travel Guide?")
									.setNegativeButton("Cancel", null)
									.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											finish();
										}
									})
									.show();
	}
	class JsonLoader extends AsyncTask<String, String, String>
	{
		final String url = "http://ctg.comeze.com/JSONstation.php";
		final String url1 = "http://ctg.comeze.com/JSONstop.php";
		
		ProgressDialog pDlg = new ProgressDialog(City_Selection.this);
		
		String responseText = "", responseText1 = "";
		
		
		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			pDlg.setCancelable(false);
			pDlg.setMessage("Synchronizing Your Data");
			pDlg.show();
		}
		
		@Override
		protected String doInBackground(String... params) {
			try
			{
				//JSONstation
				DefaultHttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost(url);
				HttpResponse response = client.execute(request);
				HttpEntity entity = response.getEntity();
				responseText = EntityUtils.toString(entity);
				
				//JSONstop
				DefaultHttpClient client1 = new DefaultHttpClient();
				HttpPost request1 = new HttpPost(url1);
				HttpResponse response1 = client1.execute(request1);
				HttpEntity entity1 = response1.getEntity();
				responseText1 = EntityUtils.toString(entity1);
			}
			catch (ClientProtocolException e)
			{}
			catch (IOException e)
			{}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) 
		{
			if (responseText.length() > 0)
			{
				try
				{
					
					// Convert our received string data into JSON Object first
					
					//for JSONstation
					JSONObject myData = new JSONObject(responseText);
					JSONArray stationArr = myData.getJSONArray("result1");
					
					//for JSONstop
					JSONObject myData1 = new JSONObject(responseText1);
					JSONArray stopArr = myData1.getJSONArray("result");
					
					db.truncateTables();
					
					// access each individual element from JSON Array
					for(int i = 0; i < stopArr.length(); i++)
					{
						
						JSONObject currentStop = stopArr.getJSONObject(i);
						String rout_no = currentStop.getString("Route_No");
						String stp = currentStop.getString("Stops");
						String stp_no = currentStop.getString("stop_number");
						
						db.insertStops(rout_no, stp, stp_no);
					}
					
					for(int i = 0; i < stationArr.length(); i++)
					{
						
						JSONObject currentStation = stationArr.getJSONObject(i);
						String sta_name = currentStation.getString("station_name");
						
						db.insertStation(sta_name);
					}
					
					
				}
				catch (JSONException e)
				{
					Log.e("JSON Error", e.getMessage());
				}
			}
			else
			{
					Toast.makeText(City_Selection.this, "No Internet Connection Found...", 3000).show();
			}
			pDlg.setCancelable(true);
			pDlg.cancel();
		}
	}
}