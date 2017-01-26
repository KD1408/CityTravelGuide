package com.example.ctg_final;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.ctg_final.Online_Result.onlineResult;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Online_Result_Time extends Activity {
	TextView tvbsno, tvtime, tvsrc, tvdest, tvdist;
	ListView lst1;
	String src1,des1,lin1;
	ArrayList<String> detailList1;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.online_result_time);

		String rno = getIntent().getExtras().getString("rtno");
		String bno = getIntent().getExtras().getString("bsno");
		String time = getIntent().getExtras().getString("Time");
		src1 = getIntent().getExtras().getString("source1");
		des1 = getIntent().getExtras().getString("destination1");
		lin1 = getIntent().getExtras().getString("line1");
		
		tvbsno = (TextView) findViewById(R.id.tvBusNumber);
		tvtime = (TextView) findViewById(R.id.tvBusTime);
		tvsrc = (TextView) findViewById(R.id.tvMainSource);
		tvdest = (TextView) findViewById(R.id.tvMainDest);
		tvdist = (TextView) findViewById(R.id.tvMainDistance);
		lst1 = (ListView) findViewById(R.id.lstOnlineBusResult);

		tvbsno.setText(bno);
		tvtime.setText(time);

		detailList1 = new ArrayList<String>();
		new sendLine().execute(rno);

	}

	@Override
	public void onBackPressed() {
		Intent n = new Intent(Online_Result_Time.this, Online_Result.class);
		n.putExtra("srcDetailFromSearch", src1);
		n.putExtra("destDetailFromSearch", des1);
		n.putExtra("line", lin1);
		startActivity(n);
		finish();
	}

	class sendLine extends AsyncTask<String, String, String> {
		ProgressDialog pDlg = new ProgressDialog(Online_Result_Time.this);
		String output = "";

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pDlg.setCancelable(false);
			pDlg.setMessage("Please wait for a moment...");
			pDlg.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				String line_no = params[0];
				String url = "http://ctg.krutarthdoshi.com/JSONbus_route.php";

				String params1 = URLEncoder.encode("line", "UTF-8") + "="
						+ URLEncoder.encode(line_no, "UTF-8");
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
			try {
				JSONObject data = new JSONObject(output);
				JSONArray detailArra = data.getJSONArray("result4");
				JSONArray detailArra1 = data.getJSONArray("result5");

				for (int i = 0; i < detailArra1.length(); i++) {
					
					JSONObject curentDetail1 = detailArra1.getJSONObject(i);
					String stp = curentDetail1.getString("Stops");

					
					detailList1.add(stp);

					ArrayAdapter<String> adap = new ArrayAdapter<String>(
							Online_Result_Time.this,
							android.R.layout.simple_list_item_1, detailList1);
					lst1.setAdapter(adap);
				}

				for (int i = 0; i < detailArra.length(); i++) {
					HashMap<String, String> detail = new HashMap<String, String>();
					JSONObject curentDetail = detailArra.getJSONObject(i);
					String src = curentDetail.getString("Source");
					String dest = curentDetail.getString("Destination");
					String dist = curentDetail.getString("Distance");

					tvsrc.setText(src);
					tvdest.setText(dest);
					tvdist.setText(dist + " Km");
					// Toast.makeText(adv_search_result.this, ""+lat+ " " +lan,
					// Toast.LENGTH_LONG).show();

				}

				// JSONObject data1 = new JSONObject(output);

			} catch (Exception e) {

			}
			pDlg.setCancelable(true);
			pDlg.cancel();
		}

	}

}
