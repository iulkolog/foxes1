package ru.yull.loginovayuliya.foxhunting;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.analytics.tracking.android.EasyTracker;

import java.util.Random;


//Это самый первый экран, который будет выводиться при загрузке игры.
public class StartScreen extends FragmentActivity implements OnClickListener
{
	
	int clr = 0;
	
	//@Override
    public void onCreate(Bundle savedInstanceState)//событие на создание
    {
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.startscreen);


        if (savedInstanceState == null )
        {
	        int red, green, blue;
	        Random randomGenerator = new Random();
			int diapasoneForGeneration = 7 ; //от нуля до этого числа (не включительно)
			red = randomGenerator.nextInt(diapasoneForGeneration)*36;
			green = randomGenerator.nextInt(diapasoneForGeneration)*36;
			blue = randomGenerator.nextInt(diapasoneForGeneration)*36;
	        clr = Color.rgb(red,green,blue);

	        
	       
        }
        else
        {
        	clr = savedInstanceState.getInt("colorBkgr");
        }
        
        if (getResources().getBoolean(R.bool.admobON))
	    {
	        LinearLayout parent = (LinearLayout) findViewById(R.id.adfragment);
	        AdView adView = new AdView(this, AdSize.SMART_BANNER, getString(R.string.admob_publisher_id));
	        parent.addView(adView);
	        AdRequest adRequest = new AdRequest();
	        //adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
	        adView.loadAd(adRequest);
	    }
        
        View startScreen = (View)findViewById(R.id.start);
        startScreen.setBackgroundColor(clr); 
        startScreen.setBackgroundResource(R.drawable.background3);
        

        View startButton1 = (View)findViewById(R.id.PlayButton);//кнопки старта
        TextView tx = (TextView) findViewById(R.id.textAppName);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/RussoOne-Regular.ttf");
        if (tx!=null)
        	tx.setTypeface(face);
        
        startButton1.setOnClickListener(this);//повесили на них слушатель кликов
        
    } 
    
    
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    	outState.putInt("colorBkgr", clr);
      }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    	clr = savedInstanceState.getInt("colorBkgr");
      }
    
    @Override
    public void onDestroy() {
      //adView.destroy();
      super.onDestroy();
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    	if (getResources().getBoolean(R.bool.ga_autoActivityTracking))
    			EasyTracker.getInstance().activityStart(this);
      
      
    }
    
    @Override
    public void onStop() {
      super.onStop();
      if (getResources().getBoolean(R.bool.ga_autoActivityTracking))
    	  			EasyTracker.getInstance().activityStop(this);
    }
    
    
    /** Обработка нажатия кнопок */
    public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.PlayButton :
			{
			    Intent intent = new Intent();
			    intent.setClass(this, GameScreen.class);
			    startActivity(intent);
				break;
			}
			    
			default:
				break;
		}
	}
}