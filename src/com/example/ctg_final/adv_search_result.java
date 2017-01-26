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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class adv_search_result extends Activity {

	ListView lst1;
	String route, src, des;
	ArrayList<String> arr = new ArrayList<String>();
	DatabaseHandler db = new DatabaseHandler(this);
	ArrayAdapter<String> ad1 = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adv_search_result);

		lst1 = (ListView) findViewById(R.id.lvRtNum);

		src = getIntent().getExtras().getString("srcDetailsFromADV");
		des = getIntent().getExtras().getString("dstDetailsFromADV");

		ArrayList<String> cursor = db.resultRoutesNo(src.trim(), des);

		if (cursor.size() > 0) {

			String txt = "";

			for (int i = 0; i < cursor.size(); i++) {

				txt = cursor.get(i);
				arr.add(txt);
				ad1 = new ArrayAdapter<String>(adv_search_result.this,
						android.R.layout.simple_list_item_1, arr);
				// ad1.add(cursor.get(i));
				lst1.setAdapter(ad1);
			}

		}
		lst1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				route = arr.get(position);

				sendLine o = new sendLine();
				o.execute(route);
			}

		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent next = new Intent(adv_search_result.this, Tabbed_Layout.class);
		startActivity(next);
		finish();
	}

	class sendLine extends AsyncTask<String, String, String> {
		ProgressDialog pDlg = new ProgressDialog(adv_search_result.this);
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
				String url = "http://ctg.krutarthdoshi.com/JSONbus_track1.php";

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
				JSONArray detailArra = data.getJSONArray("result3");

				for (int i = 0; i < detailArra.length(); i++) {
					HashMap<String, String> detail = new HashMap<String, String>();
					JSONObject curentDetail = detailArra.getJSONObject(i);
					Double lat = null;
					lat = curentDetail.getDouble("latitude");
					Double lan = null;
					lan = curentDetail.getDouble("longitude");

					if (!lat.equals(null) && !lan.equals(null)) {
						Intent n = new Intent(adv_search_result.this,
								Maps.class);
						n.putExtra("lat1", lat);
						n.putExtra("lan1", lan);
						n.putExtra("rtNo1", route);
						n.putExtra("Source1", src);
						n.putExtra("Destination1", des);
						startActivity(n);
						finish();
					} else {
						Toast.makeText(adv_search_result.this,
								"No data Found..!!", Toast.LENGTH_LONG).show();
					}
				}
			} catch (Exception e) {

			}
			pDlg.setCancelable(true);
			pDlg.cancel();
		}

	}
}
