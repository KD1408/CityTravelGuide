package com.example.ctg_final;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class Maps extends FragmentActivity implements LocationListener{
	
	MapFragment mapFrg;
	GoogleMap gMap;
	String rt_number, tag,src1,des1;
	LocationManager mgr;
	Double lt,ln;
	Marker tm;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.map);
		
		lt =getIntent().getExtras().getDouble("lat1");
		ln =getIntent().getExtras().getDouble("lan1");
		rt_number = getIntent().getExtras().getString("rtNo1");
		src1 = getIntent().getExtras().getString("Source1");
		des1 = getIntent().getExtras().getString("Destination1");
		
		
		//Toast.makeText(Maps.this, ""+lt+ " " +ln +"" +rt_number, Toast.LENGTH_LONG).show();
		
		mapFrg = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
		
		String locationService = Context.LOCATION_SERVICE;
		String networkService = LocationManager.NETWORK_PROVIDER;
		
		mgr = (LocationManager)getSystemService(locationService);
		
		mgr.requestLocationUpdates(mgr.GPS_PROVIDER, 5000, 1000, this);

		Location lastLocation = mgr.getLastKnownLocation(mgr.GPS_PROVIDER);

		
		gMap = mapFrg.getMap();
		if(gMap !=null)
		{
			gMap.setMyLocationEnabled(true);
			gMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			LatLng temp = new LatLng(lt, ln);
			tm = gMap.addMarker(new MarkerOptions().position(temp).title("Tag:"+tag).snippet(lt+" "+ln));
			
			gMap.setBuildingsEnabled(true);
			gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(temp, 21.0f));	
		}
		
		Thread timer = new Thread()
		{
			public void run()
			{
				try 
				{	
					while(true)
					{
						sendLine o = new sendLine();
						o.execute(rt_number);
						sleep(7000);
					}
				} 
				catch (InterruptedException e) 
				{
					e.getStackTrace();
				}
				finally
				{	
				}
			}
		};
		
		
		timer.start();
		
	}
	class sendLine extends AsyncTask<String, String, String>{

		String output = "";
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try{
			String line_no = params[0];
			String url = "http://ctg.comeze.com/JSONbus_track1.php";
			
			String params1 = URLEncoder.encode("line", "UTF-8")+"="+URLEncoder.encode(line_no, "UTF-8");
			URL server_location = new URL(url);
			URLConnection con = server_location.openConnection();
			con.setDoOutput(true);
			
			BufferedReader reader = null;
			OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
			writer.write(params1);
			writer.flush();
			
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String text =  "";
			while ((text = reader.readLine())!= null)
			{
				output += text + "\n";
			}
			
			}
			catch(Exception e){
				output = "error : " + e.getMessage();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
		
			
			try
			{
			JSONObject data = new JSONObject(output);
			JSONArray detailArra = data.getJSONArray("result3");
			
			for(int i = 0;i < detailArra.length();i++)
			{
				HashMap<String , String> detail = new HashMap<String , String>();
				JSONObject curentDetail = detailArra.getJSONObject(i);
				Double lat = curentDetail.getDouble("latitude");
				Double lan = curentDetail.getDouble("longitude");
				tag = curentDetail.getString("GPS_Tag");
				
				lt = lat;
				ln = lan;

				tm.setTitle("Tag:"+tag);
				tm.setSnippet("\n"+lt+" "+ln);
//				Toast.makeText(Maps.this, ""+lat+ " " +lan, 1000).show();
				
			}
			}
			catch(Exception e)
			{
				
			}
		}
		
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

	@Override
	public void onBackPressed() {
		Intent n = new Intent(Maps.this,adv_search_result.class);
		n.putExtra("srcDetailsFromADV", src1);
		n.putExtra("dstDetailsFromADV", des1);
		startActivity(n);
		finish();
	}
}
