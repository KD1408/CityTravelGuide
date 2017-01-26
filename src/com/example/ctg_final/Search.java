package com.example.ctg_final;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Search extends Activity {
	static int recognizer = 0;
	Button b1;
	ImageButton bv1, bv2;
	AutoCompleteTextView actv1, actv2;
	Spinner spn1, spn2;
	TextView tv1;
	int REQUEST_CODE = 1234;
	ListView lv1;
	Intent next;
	ConnectionDetector co;
	DatabaseHandler db = new DatabaseHandler(Search.this);
	ArrayAdapter<String> adapStation, adapStation1, adapStation2, adapStation3;
	//sensor code starts
	SensorManager sMgr = null;
	List<Sensor> sensorList;

	SensorEventListener sensEvent = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			float[] values = event.values;

			int x_color = (int) (Math.abs(values[0]) * 100);
			int y_color = (int) (Math.abs(values[1]) * 100);
			int z_color = (int) (Math.abs(values[2]) * 100);

			tv1.setTextColor(Color.rgb(x_color, y_color, z_color));

		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub

		}
	};//sensor code ends

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		String fontPath = "Fonts/ALGER.TTF";

		b1 = (Button) findViewById(R.id.btnSrch);
		actv1 = (AutoCompleteTextView) findViewById(R.id.actvSrc);
		actv2 = (AutoCompleteTextView) findViewById(R.id.actvDest);
		spn1 = (Spinner) findViewById(R.id.spnSrc);
		spn2 = (Spinner) findViewById(R.id.spnDest);
		tv1 = (TextView) findViewById(R.id.tvFont);
		bv1 = (ImageButton) findViewById(R.id.imgbtnSrc);
		bv2 = (ImageButton) findViewById(R.id.imgbtnDest);
		lv1 = (ListView) findViewById(R.id.lvVcResult);

		co = new ConnectionDetector(Search.this);

		// Disable button if no recognition service is present
		PackageManager pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (activities.size() == 0) {
			bv1.setEnabled(false);
			bv2.setEnabled(false);
		}
		//CTG backgroung colour
		Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

		tv1.setTypeface(tf);

		sMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
		if (sMgr != null) {
			sensorList = sMgr.getSensorList(Sensor.TYPE_ACCELEROMETER);
			String items = "";

			if (sensorList.size() > 0) {
				// make sensor activate and perform task
				sMgr.registerListener(sensEvent, sensorList.get(0),
						SensorManager.SENSOR_DELAY_NORMAL);
			}
		} else {
			Toast.makeText(Search.this, "No Sensor Service is available...",
					Toast.LENGTH_LONG).show();
		}//CTG background code ends

		String[] station = db.getStation();
		adapStation = new ArrayAdapter<String>(Search.this,
				android.R.layout.simple_list_item_1, station);
		actv1.setAdapter(adapStation);

		String[] station1 = db.getStation();
		adapStation1 = new ArrayAdapter<String>(Search.this,
				android.R.layout.simple_list_item_1, station1);
		spn1.setAdapter(adapStation1);

		String[] station2 = db.getStation();
		adapStation2 = new ArrayAdapter<String>(Search.this,
				android.R.layout.simple_list_item_1, station2);
		spn2.setAdapter(adapStation2);

		String[] station3 = db.getStation();
		adapStation3 = new ArrayAdapter<String>(Search.this,
				android.R.layout.simple_list_item_1, station3);
		actv2.setAdapter(adapStation3);
		
		b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String src = actv1.getText().toString();
				String dest = actv2.getText().toString();

				boolean valid = true;
				if (src.equals(dest)) {
					valid = false;
					Toast.makeText(Search.this,
							"Source and Destination cannot be same",
							Toast.LENGTH_LONG).show();
				}
				if (dest.equals("")) {
					valid = false;
					actv2.setError("Cannot be Blank");
				}
				if (src.equals("")) {
					valid = false;
					actv1.setError("Cannot be Blank");
				}
				// to move to next activity carrying some value
				if (valid) {
					if (co.isConnectingToInternet()) {
						next = new Intent(Search.this, Online_Result.class);
						sendData o = new sendData();
						o.execute(src,dest);
						next.putExtra("srcDetailFromSearch", src);
						next.putExtra("destDetailFromSearch", dest);
						
					} else {
						next = new Intent(Search.this, Offline_Result.class);
						next.putExtra("srcDetailFromSearch", src);
						next.putExtra("destDetailFromSearch", dest);
						startActivity(next);
						finish();
					}
				}
			}
		});
		
		//voice input button
		bv1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				recognizer = 0;
				startVoiceRecognitionActivity();
			}
		});

		//voice input button
		bv2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				recognizer = 1;
				startVoiceRecognitionActivity();
			}
		});
		
		lv1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String stn = (String) lv1.getItemAtPosition(position);
				if (recognizer == 0) {
					actv1.setText(stn);
				} else {
					actv2.setText(stn);
				}
				lv1.setAdapter(null);
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

		spn2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String srcStn1 = adapStation2.getItem(position);
				actv2.setText(srcStn1);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				actv2.setText("");
			}
		});
	}

	/**
	 * Handle the action of the button being clicked
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent next = new Intent(Search.this, City_Selection.class);
		startActivity(next);
		finish();
	}

	private void startVoiceRecognitionActivity() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Your Station...");
		startActivityForResult(intent, REQUEST_CODE);
	}

	class sendData extends AsyncTask<String, String, String>{
		String output = "";
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				String src = params[0];
				String dst = params[1];
				String url = "http://ctg.comeze.com/JSONdaily.php";

				
				String params1 = URLEncoder.encode("source", "UTF-8") + "="
						+ URLEncoder.encode(src, "UTF-8")+"&"+URLEncoder.encode("destination", "UTF-8") + "="
						+ URLEncoder.encode(dst, "UTF-8");
				URL server_location = new URL(url);
				URLConnection con = server_location.openConnection();
				con.setDoOutput(true);

				BufferedReader reader = null;
				OutputStreamWriter writer = new OutputStreamWriter(
						con.getOutputStream());
				writer.write(params1);
				writer.flush();

				reader = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String text = "";
				while ((text = reader.readLine()) != null) {
					output += text + "\n";
				}

			} catch (Exception e) {
				output = "error : " + e.getMessage();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result)
		{
		 next.putExtra("line",output);
		 startActivity(next);
		 finish();
		}
	
	}
	/**
	 * Handle the results from the voice recognition activity.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			// Populate the wordsList with the String values the recognition
			// engine thought it heard
			ArrayList<String> matches = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			lv1.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, matches));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}


// checking for internet connection
class ConnectionDetector {
	private Context _context;

	public ConnectionDetector(Context context) {
		this._context = context;
	}

	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) _context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		return false;
	}
}
	

