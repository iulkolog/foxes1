package ru.yull.loginovayuliya.foxhunting;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.analytics.tracking.android.EasyTracker;

//import android.util.Log;
//import android.view.animation.*;
//import com.google.ads.*;


public class GameScreen extends Activity {
	
	GameManager gm;
	
	//методы "оживления" =)
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        //Log.d("GMInfo", "GameScreen.onCreate");
        if (getLastNonConfigurationInstance() != null)
        	//gm = savedInstanceState.getClass("gm");
        {
        	gm = (GameManager) getLastNonConfigurationInstance();
        	//Log.d("GMInfo", "GameScreen.onCreate.получили ссылку на ранее созданный поток");
        }
        else
        {
        	gm = new GameManager(this);
        	//Log.d("GMInfo", "GameScreen.onCreate.создали поток");
        	
        }
        
        
        
        View addView2 = new GameView(this, null, gm);
        RelativeLayout.LayoutParams ld = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        addView2.setLayoutParams(ld);
        
        LinearLayout layout = new LinearLayout(this);
        //layout.setId(1);

        layout.addView(addView2);
        setContentView(layout);
        
    }
    
    @Override
    protected void onStart() {
      super.onStart();
      //Log.d("GMInfo", "GameScreen.onStart");
      if (getResources().getBoolean(R.bool.ga_autoActivityTracking))
    	  EasyTracker.getInstance().activityStart(this);
    }

    @Override
    protected void onResume() {
      super.onResume();
      //Log.d("GMInfo", "onResume");
    }


    @Override
    protected void onPause() {
      super.onPause();
      //Log.d("GMInfo", "onPause");
    }

    @Override
    protected void onStop() {
      super.onStop();
      //Log.d("GMInfo", "onStop");
      if (getResources().getBoolean(R.bool.ga_autoActivityTracking))
    	  EasyTracker.getInstance().activityStop(this);
    }
     
    @Override
    protected void onDestroy() {
      super.onDestroy();
    }

    //сохраняем ссылку на объект
    
    @Override
    public Object onRetainNonConfigurationInstance() {
    	//Log.d("GMInfo", "onRetainNonConfigurationInstance");
        return gm;
    }

}

    