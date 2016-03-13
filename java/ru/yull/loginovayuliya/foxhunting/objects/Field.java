package ru.yull.loginovayuliya.foxhunting.objects;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.TypedValue;

import java.util.Random;

//import android.util.Log;

public class Field
{
	//пространственные данные о поле
	private Point controlPoint; //координаты опорной точки - самая верхняя левая по традиции
	private int cellCountOnHeight = 0; //количество ячеек по длине
	private int cellCountOnWidth = 0; //и ширине
	private int numberOfAllFoxes; //число всех лис на поле
	private int numberOfOpenFoxes = 0; //число открытых лис на поле
	private int fieldWidth; //ширина поля
	private int fieldHeight; //высота поля
	private int sideOfCell; //сторона квадратной ячейки
    private float coff = 0.5f;
	
	//массив ячеек
	private Cell arrayOfCells[][];
	
	//данные, связанные со стилем отрисовки и вывода текста
	private Paint linePaint; //цвет линии
	private Paint textPaint;
	private Paint backgroundPaintForDigits; //цвет заливки для ячеек без лис
	private Paint backgroundPaintForFoxes; //цвет заливки для ячеек с лисами
	private Paint backgroundPaintForClosed; //цвет заливки для закрытых ячеек
	

	private Rect contourLineOfGameField; //квадрат для отрисовки линии вокруг поля
	
	private int cellsHadSeenAllThatTheyCouldSee = 0; //
	private int numberOfOpenCells = 0;
	
	private boolean isGameLoosed = false;

	//конструктор, в котором мы задаём количество ячеек
	public Field(int cellCountOnWidth, int cellCountOnHeight, int numberOfAllFoxes )
	{
		//Log.d("GMInfo", "Field.Constructor");
		this.cellCountOnHeight = cellCountOnHeight;
		this.cellCountOnWidth = cellCountOnWidth;
		this.numberOfAllFoxes = numberOfAllFoxes; 
		
        linePaint = new Paint();
        backgroundPaintForDigits = new Paint();
        backgroundPaintForFoxes = new Paint();
        backgroundPaintForClosed = new Paint();
        textPaint = new Paint();

		contourLineOfGameField = new Rect();
		
		controlPoint = new Point();
        
		arrayOfCells = new Cell[cellCountOnHeight][cellCountOnWidth];		
		for(int n=0; n < cellCountOnHeight ; n++)
    	{
    		for(int m=0; m < cellCountOnWidth ; m++) //из-за особенностей реализации рандома, сначала все без лис
    		{
    				arrayOfCells[n][m] = new Cell(0, linePaint, backgroundPaintForClosed, textPaint);
    			
    		}
    	}
		
		randomOneToOne();
	}
	

	//инициализируем массив ячеек данными о их координатах и цветах отрисовки
	//для этого нам нужно знать координаты опорной точки
	public void toInitField(int screenWidth, int screenHeight, int indentation, Resources res)
	{
		//Высчитываем контрольную точку с учётом отступов в процентнах 
		//Log.d("GMInfo", "field.init");
		
		if (screenWidth<screenHeight)
		{
			controlPoint.x = screenWidth*indentation/200;
			fieldWidth = screenWidth*(100-indentation)/100;
			sideOfCell = fieldWidth/cellCountOnWidth; 
			controlPoint.y = (screenHeight - sideOfCell*cellCountOnHeight)/2;
			
		}
		else
		{
			controlPoint.y = screenHeight*indentation/200;
			fieldHeight = screenHeight*(100-indentation)/100;
			sideOfCell = fieldHeight/cellCountOnHeight; 
			controlPoint.x = (screenWidth - sideOfCell*cellCountOnWidth)/2;
		}
		//вычисляем высоту и ширину поля
		fieldWidth = sideOfCell * cellCountOnWidth; 
		fieldHeight = sideOfCell  * cellCountOnHeight;
		if (screenWidth<screenHeight) //portret
		{
			if (fieldHeight > (0.6 * screenHeight))
				{
				fieldHeight = (int) (0.6 * screenHeight);
				controlPoint.y = (screenHeight - fieldHeight)/2;
				sideOfCell = fieldHeight/cellCountOnHeight; 
				controlPoint.x = (screenWidth - sideOfCell*cellCountOnWidth)/2;
				
				fieldWidth = sideOfCell * cellCountOnWidth; 
				fieldHeight = sideOfCell  * cellCountOnHeight;
				}
		}
		else //land
			if (fieldWidth > (0.6 * screenWidth))
				{
				
				fieldWidth = (int) (0.6 * screenWidth);
				controlPoint.x = (screenWidth - fieldWidth)/2;
				sideOfCell = fieldWidth/cellCountOnWidth; 
				controlPoint.y = (screenHeight - sideOfCell*cellCountOnHeight)/2;

				fieldWidth = sideOfCell * cellCountOnWidth; 
				fieldHeight = sideOfCell  * cellCountOnHeight;

				}
		//выставляем значения для отрисовочных материалов
		contourLineOfGameField.set(controlPoint.x, controlPoint.y, controlPoint.x+ fieldWidth, controlPoint.y + fieldHeight);
		
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(1);
        linePaint.setStyle(Style.STROKE);
        
		backgroundPaintForDigits.setColor(Color.rgb(0, 204, 204));
		backgroundPaintForDigits.setStyle(Style.FILL);
		backgroundPaintForDigits.setAlpha(150);
        

		backgroundPaintForFoxes.setColor(Color.rgb(204, 119, 34));
		backgroundPaintForFoxes.setStyle(Style.FILL);
		backgroundPaintForFoxes.setAlpha(150);

		backgroundPaintForClosed.setColor(Color.rgb(52,201,36));
		backgroundPaintForClosed.setStyle(Style.FILL);
		backgroundPaintForClosed.setAlpha(150);
		
		
		textPaint.setColor(Color.DKGRAY);
		textPaint.setStrokeWidth(1);
		textPaint.setAntiAlias(true);


		int currentLeft = controlPoint.x;
		int currentTop = controlPoint.y;


    	int ht = (int) (coff * sideOfCell);
    	//Log.d("GMInfo", "ht = " + ht);
		float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, ht, res.getDisplayMetrics());

    	//Log.d("GMInfo", "textSize = " + textSize);
    	for(int n=0; n < cellCountOnHeight ; n++)
    	{
    		
    		for(int m = 0; m < cellCountOnWidth ; m++)
    		{
    				arrayOfCells[n][m].setCoords(currentLeft, currentTop, sideOfCell, sideOfCell);
    				
    			currentLeft += sideOfCell;
    		}
    	
    	currentTop += sideOfCell;
    	currentLeft = controlPoint.x;
    	}

    	arrayOfCells[2][2].setTextSize(textSize);
	}
	/////////////////////////////////
	
	private void randomOneToOne()//в одну ячейку не больше одной лисы.
	{
		
		//свяжем массив с ячейками и этот новый, в котором мы производим рандом.
		int numberOfAllCells = cellCountOnWidth*cellCountOnHeight;
		Cell[] arrayOfNumericCells = new Cell[numberOfAllCells];
		Cell dummy;
		
		int k = 0;
		for(int n=0; n < cellCountOnHeight; n++)
    	{
    		for(int m=0; m < cellCountOnWidth; m++)
    		{

    			arrayOfNumericCells[k] = arrayOfCells[n][m]; 
    			k++;
    		}
    	}
		
		//а теперь устроим рандом!
		Random randomGenerator = new Random();
		int diapasoneForGeneration = numberOfAllCells-1 ; //от нуля до этого числа (не включительно)
		int randomNumberOfCell;
		
		for(int numberOfGeneratedFoxes=0; numberOfGeneratedFoxes < numberOfAllFoxes; numberOfGeneratedFoxes++)
    	{
			randomNumberOfCell = randomGenerator.nextInt(diapasoneForGeneration);//номер рандомной ячейки
			arrayOfNumericCells[randomNumberOfCell].setNumberOfFoxes(1);
			dummy = arrayOfNumericCells[randomNumberOfCell];
			arrayOfNumericCells[randomNumberOfCell] = arrayOfNumericCells[diapasoneForGeneration];
			arrayOfNumericCells[diapasoneForGeneration] = dummy;
			diapasoneForGeneration--;
    	}

	}
	///=======================================================================================
	//Функции, связанные с открытием ячейки
	//открываем ячейку
	//мы можем обрабатывать только координаты, а не номер. 
	private Point calculateCoordsOfCell(Point coordinate)
	{
		Point p = new Point((coordinate.y - controlPoint.y)/sideOfCell, (coordinate.x - controlPoint.x)/sideOfCell);
		return p;
	}
	
	
	public int toOpenCell(Point coordinate)//возвращает, если клетка не была открыта ранее, кол-во лис, иначе -1
	{
		//если вообще входит в поле
		if ((coordinate.x >= controlPoint.x) && (coordinate.x < controlPoint.x + fieldWidth) &&
				(coordinate.y >= controlPoint.y) && (coordinate.y < controlPoint.y + fieldHeight))
		{		
			//надо высчитать в таком случае к какой ячейке обращаться
			Point p = calculateCoordsOfCell(coordinate);
			int n = p.x;
			int m = p.y;
			//если клетка ещё не открыта
			if (!arrayOfCells[n][m].isOpened())
			{
				if (arrayOfCells[n][m].isThatCellHinting())
					cellsHadSeenAllThatTheyCouldSee--;
				numberOfOpenCells++;
				int numberOfFoxesInCell = 0;
				arrayOfCells[n][m].toOpenCell(backgroundPaintForDigits, backgroundPaintForFoxes);
				toCountUpAllVisibleFoxesFromThisCell(n,m);
				//если клетка содержит лис
				if (arrayOfCells[n][m].IsFoxed())
				{
					toCountUpAllVisibleFoxesInOutCells(n,m);
					numberOfOpenFoxes++;
					numberOfFoxesInCell = 1;
				}
				//если количество открытых видимых из клетки лис совпадает с количеством видимых из клетки лис
				if (arrayOfCells[n][m].getNumberOfVisibleOpenFoxes()==arrayOfCells[n][m].getNumberOfVisibleFoxes())
					//функция расставления во всех таких клетках другого фонового цвета
					setBckgrToZeroVisibleCells(n,m);
				//Log.d("Click", "to open cell (number of foxes = " + numberOfFoxesInCell);
				return numberOfFoxesInCell;
			}
			else return -1;
			
		}
		
		return -1;
	}
	////открытие всех ранее не открытых ячеек
	public void toOpenAllCells()
	{
		isGameLoosed = true;
		for(int n=0; n < cellCountOnHeight; n++)
    	{
    		for(int m=0; m < cellCountOnWidth; m++)
    		{
    			if (!arrayOfCells[n][m].isOpened())
    			{
    				if (arrayOfCells[n][m].IsFoxed())
    					arrayOfCells[n][m].setBackgroundOfCell(backgroundPaintForFoxes);
    				else
    					arrayOfCells[n][m].setBackgroundOfCell(backgroundPaintForDigits);
    			}
    		}
    	}
	}
	//функция подсчёта видимых из этой клетки лис
	private void toCountUpAllVisibleFoxesFromThisCell(int n, int m) 
	{
		int numberOfVisibleFoxes = 0;
		int nn, mm;
		
		//нужно пройтись по горизонтали
		nn = n;
		for(mm=0; mm < m; mm++ )
		{
			if (arrayOfCells[nn][mm].IsFoxed())
				numberOfVisibleFoxes++;
		}
		for(mm=m+1; mm < cellCountOnWidth; mm++ )
		{
			if (arrayOfCells[nn][mm].IsFoxed())
				numberOfVisibleFoxes++;
		}
		//нужно пройтись по вертикали
		mm = m;
		
		for(nn=0; nn < n; nn++ )
		{
			if (arrayOfCells[nn][mm].IsFoxed())
				numberOfVisibleFoxes++;
		}
		for(nn=n+1; nn < cellCountOnHeight ; nn++ )
		{
			if (arrayOfCells[nn][mm].IsFoxed())
				numberOfVisibleFoxes++;
		}
		//по диагоналям:
		//лв-пн:

		
		if (m<=n)
		{
			 nn = n - m;
			 mm = 0;
		}
		else
		{
			nn = 0;
			mm = m - n;
		}
			
		if (n-m > cellCountOnHeight - cellCountOnWidth)// то до n = высота
		{
			for(; nn < n; nn++ )
			{
				if (arrayOfCells[nn][mm].IsFoxed())
					numberOfVisibleFoxes++;
				mm++;
			}
			nn = n + 1;
			mm = m + 1;
			for(; nn < cellCountOnHeight; nn++ )
			{
				if (arrayOfCells[nn][mm].IsFoxed())
					numberOfVisibleFoxes++;
				mm++;
			}
		}
		else //иначе до m = ширине
		{
			for(; mm < m; mm++ )
			{
				if (arrayOfCells[nn][mm].IsFoxed())
					numberOfVisibleFoxes++;
				nn++;
			}
			nn = n + 1;
			mm = m + 1;
			for(; mm < cellCountOnWidth; mm++ )
			{
				
				if (arrayOfCells[nn][mm].IsFoxed())
					numberOfVisibleFoxes++;
				nn++;
			}
		}

		//============================
		//пв-лн: /
		
		if (m+n < cellCountOnWidth)
		{
			nn = 0;
			mm = n + m;
		}
		else //вариант б
		{
			mm = cellCountOnWidth - 1;
			nn = n + m + 1 - cellCountOnWidth;
		}
			
		if (n + m >= cellCountOnHeight)// то до n = высота
		{
			for(; nn < n; nn++ )
			{
				if (arrayOfCells[nn][mm].IsFoxed())
					numberOfVisibleFoxes++;
				mm--;
			}
			nn = n + 1;
			mm = m - 1;
			for(; nn < cellCountOnHeight; nn++ )
			{
				if (arrayOfCells[nn][mm].IsFoxed())
					numberOfVisibleFoxes++;
				mm--;
			}
		}
		else //иначе до m = 0
		{
			for(; mm > m; mm-- )
			{
				if (arrayOfCells[nn][mm].IsFoxed())
					numberOfVisibleFoxes++;
				nn++;
			}
			nn = n + 1;
			mm = m - 1; 
			for(; mm >= 0; mm-- )
			{
				if (arrayOfCells[nn][mm].IsFoxed())
					numberOfVisibleFoxes++;
				nn++;
			}
		}
		arrayOfCells[n][m].setNumberOfVisibleFoxes(numberOfVisibleFoxes);
	}
	///////////////////////////
	//функция пересчёта в клетках, видящих эту лису
	private void toCountUpAllVisibleFoxesInOutCells(int n, int m) 
	{

		int nn, mm;
			
		//нужно пройтись по горизонтали
		nn = n;
		for(mm=0; mm < m; mm++ )
			{
				arrayOfCells[nn][mm].incNumberOfVisibleOpenFoxes();
				if (arrayOfCells[nn][mm].isOpened()
						&& arrayOfCells[nn][mm].getNumberOfVisibleOpenFoxes()==arrayOfCells[nn][mm].getNumberOfVisibleFoxes())
					setBckgrToZeroVisibleCells(nn, mm); 
			}
		
		for(mm=m+1; mm < cellCountOnWidth; mm++ )
			{
				arrayOfCells[nn][mm].incNumberOfVisibleOpenFoxes();
				if (arrayOfCells[nn][mm].isOpened()
						&& arrayOfCells[nn][mm].getNumberOfVisibleOpenFoxes()==arrayOfCells[nn][mm].getNumberOfVisibleFoxes())
					setBckgrToZeroVisibleCells(nn, mm); 
			}
			//нужно пройтись по вертикали
		mm = m;
			
		for(nn=0; nn < n; nn++ )
			{
				arrayOfCells[nn][mm].incNumberOfVisibleOpenFoxes();
				if (arrayOfCells[nn][mm].isOpened()
						&& arrayOfCells[nn][mm].getNumberOfVisibleOpenFoxes()==arrayOfCells[nn][mm].getNumberOfVisibleFoxes())
					setBckgrToZeroVisibleCells(nn, mm); 
			}
		
		for(nn=n+1; nn < cellCountOnHeight ; nn++ )
			{
				arrayOfCells[nn][mm].incNumberOfVisibleOpenFoxes();
				if (arrayOfCells[nn][mm].isOpened()
						&& arrayOfCells[nn][mm].getNumberOfVisibleOpenFoxes()==arrayOfCells[nn][mm].getNumberOfVisibleFoxes())
					setBckgrToZeroVisibleCells(nn, mm); 
			}
		//по диагоналям:
		//лв-пн:
		//вариант а

			
		if (m<=n)
			{
				 nn = n - m;
				 mm = 0;
			}
		else //вариант б
			{
				nn = 0;
				mm = m - n;
			}
				
		if (n-m > cellCountOnHeight - cellCountOnWidth)// то до n = высота
			{
			for(; nn < n; nn++ )
			{
				arrayOfCells[nn][mm].incNumberOfVisibleOpenFoxes();
				if (arrayOfCells[nn][mm].isOpened()
						&& arrayOfCells[nn][mm].getNumberOfVisibleOpenFoxes()==arrayOfCells[nn][mm].getNumberOfVisibleFoxes())
					setBckgrToZeroVisibleCells(nn, mm); 
				mm++;
			}
			
			nn = n + 1;
			mm = m + 1;

			for(; nn < cellCountOnHeight; nn++ )
				{
					arrayOfCells[nn][mm].incNumberOfVisibleOpenFoxes();
					if (arrayOfCells[nn][mm].isOpened()
							&& arrayOfCells[nn][mm].getNumberOfVisibleOpenFoxes()==arrayOfCells[nn][mm].getNumberOfVisibleFoxes())
						setBckgrToZeroVisibleCells(nn, mm); 
					mm++;
				}
			}
		else //иначе до m = ширине
			{
			for(; mm < m; mm++ )
			{
				arrayOfCells[nn][mm].incNumberOfVisibleOpenFoxes();
				if (arrayOfCells[nn][mm].isOpened()
						&& arrayOfCells[nn][mm].getNumberOfVisibleOpenFoxes()==arrayOfCells[nn][mm].getNumberOfVisibleFoxes())
					setBckgrToZeroVisibleCells(nn, mm); 
				nn++;
			}
			
			nn = n + 1;
			mm = m + 1;
			
			for(; mm < cellCountOnWidth; mm++ )
			{
				arrayOfCells[nn][mm].incNumberOfVisibleOpenFoxes();
				if (arrayOfCells[nn][mm].isOpened()
						&& arrayOfCells[nn][mm].getNumberOfVisibleOpenFoxes()==arrayOfCells[nn][mm].getNumberOfVisibleFoxes())
					setBckgrToZeroVisibleCells(nn, mm); 
				nn++;
			}
			}
			/*
			//============================
			//пв-лн: /
			//вариант а*/
			
			if (m+n < cellCountOnWidth)
			{
				nn = 0;
				mm = n + m;
			}
			else //вариант б
			{
				mm = cellCountOnWidth - 1;
				nn = n + m + 1 - cellCountOnWidth;
			}
				
			if (n + m >= cellCountOnHeight)// то до n = высота
			{
				for(; nn < n; nn++ )
				{
					arrayOfCells[nn][mm].incNumberOfVisibleOpenFoxes();
					if (arrayOfCells[nn][mm].isOpened()
							&& arrayOfCells[nn][mm].getNumberOfVisibleOpenFoxes()==arrayOfCells[nn][mm].getNumberOfVisibleFoxes())
						setBckgrToZeroVisibleCells(nn, mm); 
					mm--;
				}
				
				nn = n + 1;
				mm = m - 1;
				
				for(; nn < cellCountOnHeight; nn++ )
				{

					arrayOfCells[nn][mm].incNumberOfVisibleOpenFoxes();
					if (arrayOfCells[nn][mm].isOpened()
							&& arrayOfCells[nn][mm].getNumberOfVisibleOpenFoxes()==arrayOfCells[nn][mm].getNumberOfVisibleFoxes())
						setBckgrToZeroVisibleCells(nn, mm); 
					mm--;
				}
			}
			else //иначе до m = 0
			{

				for(; mm > m; mm-- )
				{

					arrayOfCells[nn][mm].incNumberOfVisibleOpenFoxes();
					if (arrayOfCells[nn][mm].isOpened()
							&& arrayOfCells[nn][mm].getNumberOfVisibleOpenFoxes()==arrayOfCells[nn][mm].getNumberOfVisibleFoxes())
						setBckgrToZeroVisibleCells(nn, mm); 
					nn++;
				}
				nn = n + 1;
				mm = m - 1; 
				for(; mm >= 0; mm-- )
				{
					arrayOfCells[nn][mm].incNumberOfVisibleOpenFoxes();
					if (arrayOfCells[nn][mm].isOpened()
							&& arrayOfCells[nn][mm].getNumberOfVisibleOpenFoxes()==arrayOfCells[nn][mm].getNumberOfVisibleFoxes())
						setBckgrToZeroVisibleCells(nn, mm); 
					nn++;
				}
			}
		
		
	}
	///////////////////
	private void producingCell(int nn, int mm){
		arrayOfCells[nn][mm].setBackgroundOfCell(backgroundPaintForDigits);
		if (!arrayOfCells[nn][mm].isThatCellHinting())
		{
			arrayOfCells[nn][mm].setHintCell();
			cellsHadSeenAllThatTheyCouldSee++;
		}
	}
	//если из клетки не видно неоткрытых лис - устанавливаем 
	private void setBckgrToZeroVisibleCells(int n, int m) 
	{
		

		int nn, mm;
			
		//нужно пройтись по горизонтали
		nn = n;
		for(mm=0; mm < m; mm++ )
			{
				if (!arrayOfCells[nn][mm].isOpened())
					producingCell(nn, mm);
			}
		
		for(mm=m+1; mm < cellCountOnWidth; mm++ )
			{
				if (!arrayOfCells[nn][mm].isOpened())
					producingCell(nn, mm);
			}
	    //нужно пройтись по вертикали
		mm = m;
			
		for(nn=0; nn < n; nn++ )
			{
				if (!arrayOfCells[nn][mm].isOpened())
					producingCell(nn, mm);
			}
		
		for(nn=n+1; nn < cellCountOnHeight ; nn++ )
			{
				if (!arrayOfCells[nn][mm].isOpened())
					producingCell(nn, mm);
			}
		//по диагоналям:
		//лв-пн:
		//вариант а
			
			
		if (m<=n)
			{
				 nn = n - m;
				 mm = 0;
			}
		else //вариант б
			{
				nn = 0;
				mm = m - n;
			}
				
		if (n-m > cellCountOnHeight - cellCountOnWidth)// то до n = высота
			{
			for(; nn < n; nn++ )
			{
				if (!arrayOfCells[nn][mm].isOpened())
					producingCell(nn, mm);
				mm++;
			}
			
			nn = n + 1;
			mm = m + 1;
			for(; nn < cellCountOnHeight; nn++ )
				{
					if (!arrayOfCells[nn][mm].isOpened())
						producingCell(nn, mm);
					mm++;
				}
			}
		else //иначе до m = ширине
			{

			for(; mm < m; mm++ )
			{
				if (!arrayOfCells[nn][mm].isOpened())
					producingCell(nn, mm);
				nn++;
			}
			
			nn = n + 1;
			mm = m + 1; 
			
			for(; mm < cellCountOnWidth; mm++ )
			{
				if (!arrayOfCells[nn][mm].isOpened())
					producingCell(nn, mm);
				nn++;
			}
			}
			/*
			//============================
			//пв-лн: /
			//вариант а*/
			
			if (m+n < cellCountOnWidth)
			{
				nn = 0;
				mm = n + m;
			}
			else //вариант б
			{
				mm = cellCountOnWidth - 1;
				nn = n + m + 1 - cellCountOnWidth;
			}
				
			if (n + m >= cellCountOnHeight)// то до n = высота
			{

				for(; nn < n; nn++ )
				{
					if (!arrayOfCells[nn][mm].isOpened())
						producingCell(nn, mm);
					mm--;
				}
				
				nn = n + 1;
				mm = m - 1;
				
				for(; nn < cellCountOnHeight; nn++ )
				{
					if (!arrayOfCells[nn][mm].isOpened())
						producingCell(nn, mm);
					mm--;
				}
			}
			else //иначе до m = 0
			{
				for(; mm > m; mm-- )
				{
					if (!arrayOfCells[nn][mm].isOpened())
						producingCell(nn, mm);
					nn++;
				}
				nn = n + 1;
				mm = m - 1; 
				for(; mm >= 0; mm-- )
				{
					if (!arrayOfCells[nn][mm].isOpened())
						producingCell(nn, mm);
					nn++;
				}
			}
		
		
	}
	//==============================================================================================
    // Отрисовка поля
    public void draw(Canvas canvas)
    {
        //делегируем это занятие каждой ячейке
    	
    	//говорим каждой клетке отрисоваться
    	for(int n=0; n < cellCountOnHeight; n++)
    		for(int m=0; m < cellCountOnWidth ; m++)
    			arrayOfCells[n][m].draw(canvas);
    	
    	Paint linePaint2Test = new Paint();
    	linePaint2Test.setColor(Color.BLACK);
    	linePaint2Test.setStrokeWidth(2);
    	linePaint2Test.setStyle(Style.STROKE);

    	canvas.drawRect(contourLineOfGameField, linePaint2Test);

    }
    //были ли все лисы найдены?
    public boolean wereAllFoxesFound(){
    	
    	return (numberOfOpenFoxes==numberOfAllFoxes);
    }
    
    public Point getControlPoint(){return controlPoint;}
    public Rect getGabarites(){return contourLineOfGameField;}
    public int getNumberOfOpenFoxes(){return numberOfOpenFoxes;}
    public int getNumberOfAllFoxes(){return numberOfAllFoxes;}
    
	public int getCountOfHintCells(){return cellsHadSeenAllThatTheyCouldSee;}
	public int getCountOfOpenCells(){return numberOfOpenCells;}
	public int getCountOfAllCells() {return cellCountOnHeight*cellCountOnWidth;}
	public boolean isGameLoosed(){return isGameLoosed;}
    public void setTextSize(float coff) {this.coff = coff;}
	
}