package ru.yull.loginovayuliya.foxhunting.objects;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;


//В этом классе мы рисуем поле, расчитываем габариты, храним инфу о стилях

public class FoundTable {
	private String strFound;
	private String strOf;
	private Rect gabarites;
	private Paint textPaint;
	private int numberOfAllFoxes = 0;
	private int numberOfOpenFoxes = 0 ;
	private Paint linePaint;
	private Paint fillPaint;
	
	private  int x, y1, y2;
	public FoundTable(){

		strFound = "Found ";
		strOf = " of ";	
		gabarites = new Rect();
    	linePaint = new Paint();
    	linePaint.setColor(Color.BLACK);
    	linePaint.setStrokeWidth(2);
    	linePaint.setStyle(Style.STROKE);
    	
        textPaint = new Paint();
		textPaint.setColor(Color.DKGRAY);
		textPaint.setStrokeWidth(1);
		textPaint.setAntiAlias(true);
		
    	fillPaint = new Paint();
    	fillPaint.setColor(Color.rgb(239, 175, 140));
    	fillPaint.setStyle(Style.FILL);
    	fillPaint.setAlpha(150);
	}

	public void setNumberOfOpenFoxes(int numberOfOpenFoxes){this.numberOfOpenFoxes = numberOfOpenFoxes;}
	public void setNumberOfAllFoxes(int numberOfAllFoxes){this.numberOfAllFoxes = numberOfAllFoxes;}
	
	//чтобы нарисовать это табло нам надо знать, где мы его можем разместить
	public void calculateGabarites(Field f/*, Resources res*/, int screenHeight, int screenWidth ){
		//нужны знания о расположении поля, канвасе, ориентации экрана
		int intendant; //отступы считаем как 10%
		if (Resources.getSystem().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
		//	Log.d("GMInfo", "Fgabarites: " + f.getGabarites().left +"-"+ f.getGabarites().top + "-"+ 
		//			f.getGabarites().right + "-"+ f.getGabarites().bottom);
			intendant = (int) (0.1* f.getGabarites().left);
			//Log.d("GMInfo", "intendant = " + intendant);
			int bottom = f.getGabarites().top + (int)(0.2 * f.getGabarites().height());
			//считаем от лева до левого края поля
			gabarites.set(intendant, f.getGabarites().top, f.getGabarites().left - intendant, bottom);
		//	Log.d("GMInfo", "OR = land, gabarites: " + gabarites.left +"-"+ gabarites.top + "-"+ 
		//			gabarites.right + "-"+ gabarites.bottom);
		}
		else
		{
			//считаем от верха до поля
			//Log.d("GMInfo", "Fgabarites: " + f.getGabarites().left +"-"+ f.getGabarites().top + "-"+ 
			//		f.getGabarites().right + "-"+ f.getGabarites().bottom);
			intendant = (int) (0.1* f.getGabarites().top);
			//Log.d("GMInfo", "intendant = " + intendant);
			int height = f.getGabarites().top - intendant - intendant;
			int right = f.getGabarites().left + (int) (0.4 * f.getGabarites().width());
			int width = right - f.getGabarites().left;
			if (height > (0.7 * width))
			{
				height = (int) (0.7 * width);
				intendant = (int) ((screenHeight - f.getGabarites().bottom - height) / 2);
			}	
			
			gabarites.set(f.getGabarites().left, intendant, right, f.getGabarites().top - intendant);
			//Log.d("GMInfo", "OR = port, gabarites: " + gabarites.left +"-"+ gabarites.top + "-"+ 
			//		gabarites.right + "-"+ gabarites.bottom);
		}
    	x = gabarites.centerX();
    	int difference = (int)(gabarites.height()/4);
    	int ht = (int) (0.25 * gabarites.height());
    	textPaint.setTextSize(ht);
    	y1 = gabarites.centerY() - difference + (int)(textPaint.getTextSize()/2);
    	y2 = gabarites.centerY() + difference + (int)(textPaint.getTextSize()/2);
    	//Log.d("GMInfo", "y1 = " + y1 + " y2 = " + y2 + " G_top = " + gabarites.top + " G_bottom = " + gabarites.bottom);
		
	}
	
    public void draw(Canvas canvas, Resources res)
    {

    	canvas.drawRect(gabarites, fillPaint);
    	String str = Integer.toString(numberOfOpenFoxes)  + strOf+ Integer.toString(numberOfAllFoxes);
    	textPaint.setTextAlign(Paint.Align.CENTER);
    	canvas.drawText(strFound, x, y1, textPaint);
    	canvas.drawText(str, x, y2, textPaint);
    }

}
