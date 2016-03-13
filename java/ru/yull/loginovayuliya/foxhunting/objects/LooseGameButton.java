package ru.yull.loginovayuliya.foxhunting.objects;

import android.graphics.Canvas;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Paint.Style;


public class LooseGameButton {
	private String str;
	private Rect gabarites;
	private Paint textPaint;
	private Paint linePaint;
	private Paint fillPaint;
	
	private  int x, y;
	public LooseGameButton(){
		str = "Give up";
		gabarites = new Rect();
    	linePaint = new Paint();
    	linePaint.setColor(Color.BLACK);
    	linePaint.setStrokeWidth(2);
    	linePaint.setStyle(Style.STROKE);
    	
    	fillPaint = new Paint();
    	fillPaint.setColor(Color.rgb(0, 204, 204));
    	fillPaint.setStyle(Style.FILL);
        textPaint = new Paint();
		textPaint.setColor(Color.DKGRAY);
		textPaint.setStrokeWidth(1);
		textPaint.setAntiAlias(true);
	}


	
	//чтобы нарисовать это табло нам надо знать, где мы его можем разместить
	public void calculateGabarites(Field f, int screenHeight, int screenWidth ){
		//нужны знания о расположении поля, канвасе, ориентации экрана
		int intendant; //отступы считаем как 10%
		if (Resources.getSystem().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
		//	Log.d("GMInfo", "Fgabarites: " + f.getGabarites().left +"-"+ f.getGabarites().top + "-"+ 
		//			f.getGabarites().right + "-"+ f.getGabarites().bottom);
			intendant = (int) (0.1* f.getGabarites().left);
			//Log.d("GMInfo", "intendant = " + intendant);
			int top = f.getGabarites().bottom - (int)(0.2 * f.getGabarites().height());

			gabarites.set(f.getGabarites().right + intendant, top,
					 screenWidth - intendant, f.getGabarites().bottom);
		//	Log.d("GMInfo", "OR = land, gabarites: " + gabarites.left +"-"+ gabarites.top + "-"+ 
		//			gabarites.right + "-"+ gabarites.bottom);
		}
		else //port
		{
			//считаем от верха до поля
			//Log.d("GMInfo", "Fgabarites: " + f.getGabarites().left +"-"+ f.getGabarites().top + "-"+ 
			//		f.getGabarites().right + "-"+ f.getGabarites().bottom);
			intendant = (int) (0.1* f.getGabarites().top);
			//Log.d("GMInfo", "intendant = " + intendant);
			//int left = f.getGabarites().left + (int) (f.getGabarites().width() / 4);
			int height = screenHeight - intendant - f.getGabarites().bottom - intendant;
			int left = f.getGabarites().right - (int) (0.4 * f.getGabarites().width());
			int width = f.getGabarites().right - left;
			if (height > (0.7 * width))
			{
				height = (int) (0.7 * width);
				intendant = (int) ((screenHeight - f.getGabarites().bottom - height) / 2);
			}
			gabarites.set(left, f.getGabarites().bottom + intendant, f.getGabarites().right, screenHeight - intendant);
			//Log.d("GMInfo", "OR = port, gabarites: " + gabarites.left +"-"+ gabarites.top + "-"+ 
			//		gabarites.right + "-"+ gabarites.bottom);
		}
    	x = gabarites.centerX();
    	int ht = (int) (0.25 * gabarites.height());
    	textPaint.setTextSize(ht);
    	y = gabarites.centerY() + (int)(textPaint.getTextSize()/2);
    	//Log.d("GMInfo", "y = " + y + " G_top = " + gabarites.top + " G_bottom = " + gabarites.bottom);
		
	}
	
    public void draw(Canvas canvas, Resources res)
    {

    	canvas.drawRect(gabarites, fillPaint);
    	canvas.drawRect(gabarites, linePaint);
    	textPaint.setTextAlign(Paint.Align.CENTER);
    	canvas.drawText(str, x, y, textPaint);
    }
    
    public boolean toClick(Point coordinate){
    	if ((coordinate.x >= gabarites.left) && (coordinate.x < gabarites.right) &&
				(coordinate.y >= gabarites.top) && (coordinate.y < gabarites.bottom))
    	{
    		return true;
    	}
    	return false;
    }
    
    public void setUnableToClickStyle()
    {
    	fillPaint.setAlpha(0);
    }
    
    public void setAvalibleToClickStyle()
    {
    	fillPaint.setAlpha(150);
    }
}


