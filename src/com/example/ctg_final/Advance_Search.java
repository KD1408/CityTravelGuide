package com.example.ctg_final;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Advance_Search extends Activity implements LocationListener {

	Location lastLocation;
	TextView tv1;
	AutoCompleteTextView actv1 ,tv2;
	Button b1;
	Spinner spn1;
	Intent next;
	int REQUEST_CODE = 1234;
	ImageButton bv1;
	ArrayAdapter<String> adapStation,adapStation1,adapStation2;
	ListView lv1;
	DatabaseHandler db = new DatabaseHandler(Advance_Search.this);
	LocationManager mgr;

	
	SensorManager sMgr = null;
	List<Sensor> sensorList;
	
	SensorEventListener sensEvent = new SensorEventListener() {
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub

			float[] values = event.values;
			
			int x_color = (int)(Math.abs(values[0]) * 25);
			int y_color = (int)(Math.abs(values[1]) * 25);
			int z_color = (int)(Math.abs(values[2]) * 25);
			
			tv1.setTextColor(Color.rgb(x_color, y_color, z_color));
			
			
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.advance_search);
		String fontPath= "Fonts/ALGER.TTF";
		
		
		lv1 = (ListView)findViewById(R.id.lvVcAResult);
		bv1 = (ImageButton)findViewById(R.id.imgbtnADest);
		tv1 = (TextView)findViewById(R.id.tvFont);
		tv2 = (AutoCompleteTextView)findViewById(R.id.tvASrc);
		actv1 = (AutoCompleteTextView)findViewById(R.id.actvADest);
		spn1 = (Spinner)findViewById(R.id.spnADest);
		b1 = (Button)findViewById(R.id.btnASrch);
		tv2.setEnabled(false);
		
		String locationService = Context.LOCATION_SERVICE;
		String networkService = LocationManager.NETWORK_PROVIDER;
		
		mgr = (LocationManager)getSystemService(locationService);
		
		mgr.requestLocationUpdates(mgr.GPS_PROVIDER, 5, 2, this);

		lastLocation = mgr.getLastKnownLocation(mgr.GPS_PROVIDER);
		if(lastLocation != null)
		{
			Toast.makeText(Advance_Search.this, lastLocation.getLatitude() + " "+lastLocation.getLongitude(), 5000).show();
			SendUserData o = new SendUserData();
			o.execute();
		}
		
		PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0)
        {
            bv1.setEnabled(false);
        }
    
		
		Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
		
		tv1.setTypeface(tf);
		
		sMgr = (SensorManager)getSystemService(SENSOR_SERVICE);
		if (sMgr != null)
		{
			// we have sensor hardware 
			//Toast.makeText(SensorOperations.this, "Sensor Service is available...", Toast.LENGTH_LONG).show();
			sensorList = sMgr.getSensorList(Sensor.TYPE_ACCELEROMETER);
			String items = "";
			/*for (int i = 0; i < sensorList.size(); i++)
			{
				Sensor curr = sensorList.get(i);
				items += curr.getClass().toString() +"\n";
			}
			tv1.setText(items);
			*/
			if (sensorList.size() > 0)
			{
				// make sensor activate and perform task
				sMgr.registerListener(sensEvent, sensorList.get(0), SensorManager.SENSOR_DELAY_NORMAL);
			}
		}
		else
		{
			Toast.makeText(Advance_Search.this, "No Sensor Service is available...", Toast.LENGTH_LONG).show();
		}
		
		String[] station = db.getStation();
		adapStation = new ArrayAdapter<String>(Advance_Search.this, android.R.layout.simple_list_item_1, station);
		actv1.setAdapter(adapStation);
		
		String[] station2 = db.getStation();
		adapStation2 = new ArrayAdapter<String>(Advance_Search.this, android.R.layout.simple_list_item_1, station2);
		tv2.setAdapter(adapStation2);
		
		String[] station1 = db.getStation();
		adapStation1 = new ArrayAdapter<String>(Advance_Search.this, android.R.layout.simple_list_item_1, station1);
		spn1.setAdapter(adapStation1);
		
		b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent next1 = new Intent("android.location.GPS_ENABLED_CHANGE");
				next1.putExtra("enabled", false);
				sendBroadcast(next1);

				next = new Intent(Advance_Search.this,adv_search_result.class);
				next.putExtra("srcDetailsFromADV", tv2.getText().toString());
				next.putExtra("dstDetailsFromADV", actv1.getText().toString());
				startActivity(next);
				finish();
			}
		});
		
		bv1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startVoiceRecognitionActivity();
			}
		});
		
		lv1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String stn = (String) lv1.getItemAtPosition(position);
				actv1.setText(stn);
			}
			
		});
		
		spn1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String srcStn = adapStation1.getItem(position);
				actv1.setText(srcStn);				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				actv1.setText("");
				
				
			}
		});

	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent next = new Intent(Advance_Search.this,City_Selection.class);
		startActivity(next);
		finish();
	}
	private void startVoiceRecognitionActivity()
	{
	    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
	            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Your Station...");
	    startActivityForResult(intent, REQUEST_CODE);
	}
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            // Populate the wordsList with the String values the recognition engine thought it heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            lv1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                    matches));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
		@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	
	class SendUserData extends AsyncTask<String, String, String>
	{
		Double lat = lastLocation.getLatitude();
		Double lan = lastLocation.getLongitude();
		String answer = "";
		private String url = "http://ctg.comeze.com/getStationName.php?lat="+lat+"&long="+lan;
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			//String value = params[0];
			//url += value;
			try
			{
				DefaultHttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost(url);
				HttpResponse res = client.execute(request);
				HttpEntity ent = res.getEntity();
				answer = EntityUtils.toString(ent);
			}
			catch (Exception e)
			{
				answer = "";
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			try{
				JSONObject data = new JSONObject(answer);
				JSONArray detailArra = data.getJSONArray("result");
				
				if(detailArra.length()>0){
					int i = 0;
					JSONObject curentDetail = detailArra.getJSONObject(i);
					String station_name = curentDetail.getString("station_name");
					tv2.setText(station_name);
					tv2.setEnabled(false);
				}
				else{
					tv2.setHint("Can't find your Lacation..");
					tv2.setEnabled(true);
				}
								
			}catch(Exception e){
				
			}
		}
	}
}
