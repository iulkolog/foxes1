package ru.yull.loginovayuliya.foxhunting.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;

//таблица для вывода данных о сыгранной игре
public class WinTable {

	private Rect table = null;
	
	//это поле будет по размеру игрового поля
	public WinTable(Rect field){
		table = field;
	}
	
    public void draw(Canvas canvas)
    {
    	
    	
    	Paint linePaint2Test = new Paint();
    	linePaint2Test.setColor(Color.BLACK);
    	linePaint2Test.setStrokeWidth(2);
    	linePaint2Test.setStyle(Style.STROKE);

    	canvas.drawRect(table, linePaint2Test);
    }
    
    
    
	
}
