package ru.yull.loginovayuliya.foxhunting;

import android.content.Context;
import android.util.AttributeSet;
//import android.util.Log;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, OnGestureListener

{

    private GameManager gameManager;//Поток, рисующий в области
    private GestureDetector gestureScanner;

/////////////////////////////////  
    public GameView(Context context, AttributeSet attrs, GameManager gm)
    {
        super(context, attrs);
        // подписываемся на события Surface
        getHolder().addCallback(this);

        gestureScanner = new GestureDetector(this); //сканер нажатий
        gameManager = gm;
        // Создание менеджера игровых объектов 

    }

/////////////////////////////////
    @Override
    // Изменение области рисования
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        gameManager.initPositions(holder, height, width, getResources());
       
    }
    
    @Override
    // Создание области рисования
    public void surfaceCreated(SurfaceHolder holder)
    {
        if (!gameManager.wasRunned())
        { 	
        	//Log.d("GMInfo", "SurfaceCreated, gm wasn't runed yet");
        	gameManager.start();
        	gameManager.setRunning(true);
        }
        /*else
        	Log.d("GMInfo", "SurfaceCreated, gm was runed yet");*/
    }
//////////////////////////////////
    
    @Override
    //Уничтожение области рисования
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        gameManager.setRunning(false); 

    }
    /////////////////////////////////////////////////////////////////////////

    public boolean onTouchEvent(MotionEvent event)
    {
        
        if (gestureScanner.onTouchEvent(event))
        {
            return true;
        }
        else
            return false;


    }
    
    @Override 
    public boolean onSingleTapUp(MotionEvent event) {
        	gameManager.toClick(event);
            
        return true;
    }
    
    
    ////////////////////////////////////////
    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }
    
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
            float velocityY) {
        return false;
    }
    
    @Override
    public void onLongPress(MotionEvent e) { }
    
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
            float distanceY) 
    {
        return false;
    }
    
    @Override
    public void onShowPress(MotionEvent e) { }

    
}
