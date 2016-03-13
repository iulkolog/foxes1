package ru.yull.loginovayuliya.foxhunting.objects;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
//import android.util.Log;

public class ScoreTable {
	private String strScore;
	private String strMoves;
	private Rect gabarites;
	private Paint textPaint;
	private int score = 0;
	private int countOfMoves = 0;
	private Paint linePaint;
	private  int x, y1, y2;
	private Paint fillPaint;
	
	public ScoreTable(){
		strScore = "Score: ";
		strMoves = "Moves: ";
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

	public void setScore(int score){this.score = score;}
	public void setCount(int count){this.countOfMoves = count;}
	
	public void increaseCountOfMoves(){
		countOfMoves++;
	}
	
	public void toCalculateCount(Field f){
		score = score + (f.getCountOfAllCells()-f.getCountOfOpenCells()-f.getCountOfHintCells())*10;
		/*Log.d("Score", "CountOfAllCells = " + f.getCountOfAllCells() + 
						" CountOfOpenCells = " + f.getCountOfOpenCells() +
						" CountOfHintCells = " + f.getCountOfHintCells());
		Log.d("Score", "Score = " + score);*/
	}

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
			int top = f.getGabarites().bottom - (int)(0.2 * f.getGabarites().height());
			gabarites.set(intendant, top, f.getGabarites().left - intendant, f.getGabarites().bottom);
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
			int left = f.getGabarites().right - (int) (0.4 * f.getGabarites().width());
			int width = f.getGabarites().right - left;
			if (height > (0.7 * width))
			{
				height = (int) (0.7 * width);
				intendant = (int) ((screenHeight - f.getGabarites().bottom - height) / 2);
			}	
			
			
			gabarites.set(left, intendant, f.getGabarites().right, f.getGabarites().top - intendant);
			//Log.d("GMInfo", "OR = port, gabarites: " + gabarites.left +"-"+ gabarites.top + "-"+ 
			//		gabarites.right + "-"+ gabarites.bottom);
		}
    	x = gabarites.centerX();
    	int difference = (int)(gabarites.height()/4);
    	int ht = (int) (0.25 * gabarites.height());
    	//float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, ht, Resources.getSystem().getDisplayMetrics());
    	textPaint.setTextSize(ht);
    	y1 = gabarites.centerY() - difference + (int)(textPaint.getTextSize()/2);
    	y2 = gabarites.centerY() + difference + (int)(textPaint.getTextSize()/2);
    	//Log.d("GMInfo", "y1 = " + y1 + " y2 = " + y2 + " G_top = " + gabarites.top + " G_bottom = " + gabarites.bottom);
		
	}
	
    public void draw(Canvas canvas, Resources res)
    {

    	canvas.drawRect(gabarites, fillPaint);
    	String str1 = strScore + Integer.toString(score);
    	String str2 = strMoves + Integer.toString(countOfMoves);
    	textPaint.setTextAlign(Paint.Align.CENTER);
    	canvas.drawText(str1, x, y1, textPaint);
    	canvas.drawText(str2, x, y2, textPaint);
    }
    

}
