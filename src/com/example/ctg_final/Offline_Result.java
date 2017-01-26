package com.example.ctg_final;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Offline_Result extends Activity {

	TextView tv2, tv4, tv6;
	ListView lst1;
	String src, des;

	String routes_list = "";
	DatabaseHandler db = new DatabaseHandler(Offline_Result.this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offline_result);

		tv2 = (TextView) findViewById(R.id.tvSrc);
		tv4 = (TextView) findViewById(R.id.tvDest);
		tv6 = (TextView) findViewById(R.id.tvRoutes);
		lst1 = (ListView) findViewById(R.id.lstStp);

		src = getIntent().getExtras().getString("srcDetailFromSearch");
		des = getIntent().getExtras().getString("destDetailFromSearch");

		tv2.setText(src);
		tv4.setText(des);

		ArrayList<String> cursor = db.resultRoutesNo(src, des);

		if (cursor.size() > 0) {

			String txt = "";
			for (int i = 0; i < cursor.size(); i++) {
				txt += cursor.get(i) + "\n";
			}

			tv6.setText(txt);

			String route = cursor.get(0);
			Cursor stopsCursor = db.stopLists(route, src, des);

			ArrayAdapter<String> ad1 = new ArrayAdapter<String>(
					Offline_Result.this, android.R.layout.simple_list_item_1);

			stopsCursor.moveToFirst();
			if (stopsCursor.getCount() > 0) {
				do {
					ad1.add(stopsCursor.getString(0));
					ad1.notifyDataSetChanged();
				} while (stopsCursor.moveToNext());
			}
			else
			{
				ad1.add("Route is available for " + des + " to " + src);
			}
			lst1.setAdapter(ad1);
		} else {
			Toast.makeText(Offline_Result.this, "Nothing found", 5000).show();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent next = new Intent(Offline_Result.this, Tabbed_Layout.class);
		startActivity(next);
		finish();
	}
}
