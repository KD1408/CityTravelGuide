package com.example.ctg_final;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;

public class Splash_Screen extends Activity implements AnimationListener {
	
	ImageView imgLogo,imgLogo1,imgLogo2,imgLogo3,imgLogo4,imgLogo5,imgLogo6,imgLogo7,imgLogo8;

	//Making object of Animation
	Animation animMove, animSideDown;
	
    @Override 
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
        
        //full Screen code starts
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //full screen code ends
        
        setContentView(R.layout.splash_screen); 
        
        imgLogo  = (ImageView) findViewById(R.id.ImageView01);
        imgLogo1 = (ImageView) findViewById(R.id.ImageView02);
        imgLogo2 = (ImageView) findViewById(R.id.ImageView03);
        imgLogo3 = (ImageView) findViewById(R.id.ImageView04);
        imgLogo4 = (ImageView) findViewById(R.id.ImageView05);
        imgLogo5 = (ImageView) findViewById(R.id.ImageView06);
        imgLogo6 = (ImageView) findViewById(R.id.ImageView07);
        imgLogo7 = (ImageView) findViewById(R.id.ImageView08);
        imgLogo8 = (ImageView) findViewById(R.id.ImageView09);

		// load the animation(calling or applying the files into the res>anim)
		animMove = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move);
		animSideDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_down);

		// set animation listener
		animMove.setAnimationListener(this);
		animSideDown.setAnimationListener(this);
		
		// start the animation
		imgLogo6.startAnimation(animMove);
		imgLogo8.setVisibility(View.VISIBLE);
		imgLogo8.startAnimation(animSideDown);
		
		//Sound play code
		MediaPlayer mp = new MediaPlayer();
		mp = MediaPlayer.create(getBaseContext(), R.raw.sound); /*Gets your soundfile from res/raw/sound*/
		
		//Starts your sound
		mp.start(); 
        
        Thread timer = new Thread()
		{
			public void run()
			{
				try 
				{
					sleep(7000);
				} 
				catch (InterruptedException e) 
				{
					e.getStackTrace();
				}
				finally
				{
					Intent next = new Intent(Splash_Screen.this, City_Selection.class);
					startActivity(next);
					finish();
				}
			}
		};
		timer.start();

    }      
	@Override
	public void onAnimationStart(Animation animation) {
	}
	@Override
	public void onAnimationEnd(Animation animation) {
	}
	@Override
	public void onAnimationRepeat(Animation animation) {
	}
 }
