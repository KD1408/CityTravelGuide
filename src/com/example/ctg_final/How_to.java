package com.example.ctg_final;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class How_to extends Activity {
	ListView lst1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.how_to);
		
		lst1 = (ListView) findViewById(R.id.lstHow);
		
		String[] arr =new String[] {"1>  Synchronize data from Sliding Window When you use the app for the first time.",
						"2>  Use the Subscription option for getting latest updates",
						"3>  -d indicates the station name for destination",
						"4>  There are 2 routes which contains some station names for two times so we have separated them by -d",
						"5>  If you want to go to ranip to rto circle use ranip-d to rto circle-d,etc."};
		
		ArrayAdapter<String> adap = new ArrayAdapter<String>(How_to.this,
										android.R.layout.simple_list_item_activated_1 ,arr);
		lst1.setAdapter(adap);
		
		
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent next = new Intent(How_to.this,City_Selection.class);
		startActivity(next);
		finish();
		super.onBackPressed();
	}
}
