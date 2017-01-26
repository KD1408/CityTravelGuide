package com.example.ctg_final;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Subscribption extends Activity {
	
	TextView tv1;
	EditText et1,et2;
	Button b1;
	
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
		setContentView(R.layout.subscription);
		
		String fontPath= "Fonts/ALGER.TTF";
		
		tv1 = (TextView)findViewById(R.id.tvFont);
		et1 = (EditText)findViewById(R.id.etMob);
		et2 = (EditText)findViewById(R.id.etEmail);
		b1 = (Button)findViewById(R.id.btnSbscrb);
		
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
			Toast.makeText(Subscribption.this, "No Sensor Service is available...", Toast.LENGTH_LONG).show();
		}
		
		
		b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String m = et1.getText().toString();
				String e = et2.getText().toString().trim();
				
				boolean valid= true;
				
				if(m.equals(e)){
					valid = false;
					Toast.makeText(Subscribption.this,
							"You have entered invalid email-id",
							Toast.LENGTH_LONG).show();
				}
				if(!android.util.Patterns.EMAIL_ADDRESS.matcher(e).matches()){
					valid = false;
					et2.setError("Enter Valid Email address");
				}
				if(m.length()<10){
					valid= false;
					et1.setError("Enter Valid Mobile number");
				}
				if(m.equals("")){
					valid = false;
					et1.setError("Cannot be blank");
				}
				if(e.equals("")){
					valid = false;
					et2.setError("Cannot be blank");
				}
				if(valid){
				SendDetails sd = new SendDetails();
				sd.execute(""+et1.getText().toString(),""+et2.getText().toString());
				et1.setText("");
				et2.setText("");
				}
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent next = new Intent(Subscribption.this,City_Selection.class);
		startActivity(next);
		finish();
	}
	
	class SendDetails extends AsyncTask<String, String, String> {
		String output = "";
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			try
			{
				String mob_num = params[0];
				String emailId = params[1];
				String url = "http://ctg.krutarthdoshi.com/subscriber.php";
				String params1 = URLEncoder.encode("mob", "UTF-8")+"="+URLEncoder.encode(mob_num, "UTF-8")
						+"&"+URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(emailId, "UTF-8");
				URL server_location = new URL(url);
				URLConnection con = server_location.openConnection();
				con.setDoOutput(true);
				
				BufferedReader reader = null;
				OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
				writer.write(params1);
				writer.flush();
				
				reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
				
			}
			catch (Exception e)
			{
				
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			Toast.makeText(Subscribption.this, "Thank you for Subscription", Toast.LENGTH_LONG)
					.show();

		}
	}

}
