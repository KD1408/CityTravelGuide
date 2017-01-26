package com.example.ctg_final;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder.DeathRecipient;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Online_Result extends Activity {
	ListView lv1;
	TextView tv1,tv2;
	String src, des, line_number;
	ArrayList<HashMap<String, String>> detailList;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.online_result);
		
		lv1 = (ListView)findViewById(R.id.lstOResult);
		tv1 = (TextView)findViewById(R.id.tvOSrc);
		tv2 = (TextView)findViewById(R.id.tvODest);
		
		src = getIntent().getExtras().getString("srcDetailFromSearch");
		des = getIntent().getExtras().getString("destDetailFromSearch");
		line_number = getIntent().getExtras().getString("line");
		
		detailList = new ArrayList<HashMap<String, String>>();
		
		tv1.setText(src);
		tv2.setText(des);
		
		onlineResult o = new onlineResult();
		o.execute(line_number);
		
		lv1.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) 
			{
				Intent next = new Intent(Online_Result.this, Online_Result_Time.class);
				HashMap<String , String> detaillistview = detailList.get(position);
				next.putExtra("rtno", detaillistview.get("Rt_No"));
				next.putExtra("bsno", detaillistview.get("Bs_No"));
				next.putExtra("Time", detaillistview.get("d_time"));
				next.putExtra("source1", src);
				next.putExtra("destination1", des);
				next.putExtra("line1", line_number);
				startActivity(next);
				finish();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		Intent next = new Intent(Online_Result.this,Tabbed_Layout.class);
		startActivity(next);
		finish();
	}
	
	class onlineResult extends AsyncTask<String, String, String>
	{
		final String url = "http://ctg.krutarthdoshi.com/JSONdaily1.php";
		String output = "";
		ProgressDialog pDlg = new ProgressDialog(Online_Result.this);

		@Override
		protected void onPreExecute() {
			pDlg.setCancelable(false);
			pDlg.setMessage("Please wait for a moment...");
			pDlg.show();
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				String line = params[0];
				
				String params1 = URLEncoder.encode("line", "UTF-8") + "="
						+ URLEncoder.encode(line, "UTF-8");
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
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.i("RESPONSE", output);
			if(output.length() > 0)
			{
				try
				{
					JSONObject data = new JSONObject(output);
					JSONArray detailArra = data.getJSONArray("result");
					
					for(int i = 0;i <= detailArra.length();i++)
					{
						HashMap<String , String> detail = new HashMap<String , String>();
						JSONObject curentDetail = detailArra.getJSONObject(i);
						String rt_no = curentDetail.getString("Route_No");
						String bs_no = curentDetail.getString("Bus_NO");
						String time = curentDetail.getString("Departure_Time");
						
						detail.put("Rt_No", rt_no);
						detail.put("Bs_No", bs_no);
						detail.put("d_time", time);
						detailList.add(detail);
				
						SimpleAdapter adap1 = new SimpleAdapter(Online_Result.this,
								detailList,
								R.layout.listview_online,
								new String[]{"Rt_No","Bs_No"}, 
								new int[] {R.id.tvRtno, R.id.tvBsno});
						lv1.setAdapter(adap1);
					
					}
				}
				catch(JSONException e)
				{
					
				}
				pDlg.setCancelable(true);
				pDlg.cancel();
			}	
		}
	}

}
