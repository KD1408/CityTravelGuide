package com.example.ctg_final;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class About_us extends Activity implements OnClickListener {
	
	Button b1,b2,b3,b4,b5,b6;
	Intent next;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us);
		
		b1 = (Button)findViewById(R.id.btnBRTS);
		b2 = (Button)findViewById(R.id.btnRules);
		b3 = (Button)findViewById(R.id.btnCard);
		b4 = (Button)findViewById(R.id.btnPolicy);
		b5 = (Button)findViewById(R.id.btnToken);
		b6 = (Button)findViewById(R.id.btnFB);
		
		b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				next = new Intent(android.content.Intent.ACTION_VIEW);
				next.setData(Uri.parse("http://www.ahmedabadbrts.com/web/index.html"));
				startActivity(next);
			}
		});
		
		b2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				next = new Intent(android.content.Intent.ACTION_VIEW);
				next.setData(Uri.parse("http://www.ahmedabadbrts.com/web/documents/Smartcard_Leaflet_english.pdf"));
				startActivity(next);
			}
		});
		
		b3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				next = new Intent(android.content.Intent.ACTION_VIEW);
				next.setData(Uri.parse("http://www.ahmedabadbrts.com/others/Download.aspx?File=concession_pass_form_free_webcopy.pdf"));
				startActivity(next);
			}
		});
		b4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				next = new Intent(android.content.Intent.ACTION_VIEW);
				next.setData(Uri.parse("http://www.ahmedabadbrts.com/web/index.html"));
				startActivity(next);
			}
		});

		b5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				next = new Intent(android.content.Intent.ACTION_VIEW);
				next.setData(Uri.parse("http://www.ahmedabadbrts.com/web/pdf%20bus%20schedule/Janmarg_Token%20Details.pdf"));
				startActivity(next);
			}
		});
		
		b6.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				next = new Intent(android.content.Intent.ACTION_VIEW);
				next.setData(Uri.parse("http://www.facebook.com"));
				startActivity(next);
			}
		});

		
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent next = new Intent(About_us.this,City_Selection.class);
		startActivity(next);
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
