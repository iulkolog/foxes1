package ru.yull.loginovayuliya.foxhunting.objects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;


//класс Ячейки
public class Cell
{
private boolean isOpened = false; //Открыта ли она
private int numberOfFoxes; //Количество лис в ячейке
private int numberOfVisibleFoxes; //Количество всех лис, видимых из этой клетки
private int numberOfVisibleOpenFoxes; //Количество открытых лис, видимых из этой клетки
private Paint linePaint;
private Paint backgroundPaint; //открыта, закрыта, лиса.
private Paint textPaintNumbersOpenFoxes;
private Rect viewCell;
private boolean hintCell = false;

	//Конструктор
	public Cell(int numberOfFoxes,
			Paint linePaint, Paint backgrPaint, Paint textPaintNumbersOpenFoxes)
	{
		this.numberOfFoxes = numberOfFoxes;
		numberOfVisibleFoxes = 0; 
		viewCell = new Rect();

        this.linePaint = linePaint;
        backgroundPaint = backgrPaint;
        this.textPaintNumbersOpenFoxes = textPaintNumbersOpenFoxes;
        numberOfVisibleOpenFoxes = 0;

	}
	
	public void setCoords(int left, int top, int width, int height)
	{
		viewCell.set(left, top, left + width, top + height);
	}
	
	public void setTextSize(float textSize){
		textPaintNumbersOpenFoxes.setTextSize(textSize);
		}
	
	public Rect getGabarites(){return viewCell;}
	
	//Устанавливаем количество лис для этой ячейки
	public void setNumberOfFoxes(int number)
	{
		numberOfFoxes = number;
	}
	
	//Открыть ячейку
	public int toOpenCell(Paint openDigit, Paint openFoxes)
	{
		if (!isOpened)
		{
				hintCell = false;
				isOpened = true;
				if (numberOfFoxes != 0)
				{
			        backgroundPaint = openFoxes;
			        
				}
				
				else
				{
					backgroundPaint = openDigit;
				}
				return numberOfFoxes;
		}
		return 0;
		
		
	}
	
	//проверка на открытость
	public boolean isOpened(){ return isOpened;}
	
	//ставим количество видимых лис
	public void setNumberOfVisibleFoxes(int numberOfVisibleFoxes)
	{
		this.numberOfVisibleFoxes = numberOfVisibleFoxes;
	}
	
	///установка другого цвета 
	public void setBackgroundOfCell(Paint bckg)
	{
		backgroundPaint = bckg;
	}
	
	public void setHintCell(){
		hintCell = true;
	}
	
	public boolean isThatCellHinting(){
		return hintCell;
	}
	
	//установка количества открытых видимых лис
	public void setNumberOfVisibleOpenFoxes(int numberOfVisibleOpenFoxes)
	{
		this.numberOfVisibleOpenFoxes = numberOfVisibleOpenFoxes;
	}
	
	//количество всех лис, видимых из клетки
	public int getNumberOfVisibleFoxes(){ return numberOfVisibleFoxes;}

	//выдаём количество видимых открытых лис
	public int getNumberOfVisibleOpenFoxes(){ return numberOfVisibleOpenFoxes;}
	
	public void incNumberOfVisibleOpenFoxes(){numberOfVisibleOpenFoxes++;}
	
	//если ли хоть одна лиса в ячейке
	public boolean IsFoxed(){
		return (numberOfFoxes!=0);
		}

    public void draw(Canvas canvas)
    {
        //отрисовывает квадрат ячейки
    	canvas.drawRect(viewCell, backgroundPaint);
    	canvas.drawRect(viewCell, linePaint);
    	//если ячейка открытая и при этом не установлен маркер проигрыша, то будет отрисован текст
    	if ((isOpened))
    	{
            String text = Integer.toString(numberOfVisibleFoxes)+ "/" + Integer.toString(numberOfVisibleOpenFoxes) ;
        	
        	textPaintNumbersOpenFoxes.setTextAlign(Paint.Align.CENTER);
        	int x = viewCell.centerX();
    		canvas.drawText(text, x, 
    				viewCell.centerY()+textPaintNumbersOpenFoxes.getTextSize()/3,
    				textPaintNumbersOpenFoxes);
    	}

    }
	

}