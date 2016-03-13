package ru.yull.loginovayuliya.foxhunting;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import ru.yull.loginovayuliya.foxhunting.objects.Field;
import ru.yull.loginovayuliya.foxhunting.objects.FoundTable;
import ru.yull.loginovayuliya.foxhunting.objects.LooseGameButton;
import ru.yull.loginovayuliya.foxhunting.objects.NewGameButton;
import ru.yull.loginovayuliya.foxhunting.objects.ScoreTable;


public class GameManager extends Thread {
    //Интерфейсы и переменные
    //интерфейс для рисования.
    private interface DrawHelper {
        void helpToDraw(Canvas canvas);
    }

    /////////////////////////////////////////////////
    private boolean isGameContinues = true;
    //Состояние потока 
    private boolean isRunning = false;
    //Были ли инициализированы координаты игровых объектов
    private boolean isInitialized = false;
    //Стоит ли приложение на паузе
    //private boolean isPaused = false;
    private boolean wasRunned = false;

    //Область, на которой будем рисовать
    private SurfaceHolder drawingArea;
    private Resources resources; //ссылка на захватываемые ресурсы
    private int screenHeight;
    private int screenWidth;
    //Игровые объекты:
    private Field gameField = null; //поле
    private FoundTable foundTable = null;
    private NewGameButton newGameButton = null;
    private ScoreTable scoreTable = null;
    private LooseGameButton looseButton = null;
    private Paint backgroundPaintForAllScreen; //цвет заливки для пространства вокруг поля
    private Rect background; //квадрат для отрисовки бэка
    private Bitmap bitmapBackground = null;


    //Стили рисования
    private Paint textPaint;
    /**
     * Хелпер для перерисовки экрана
     */
    private DrawHelper DrawScreenHelper;
    private Vibrator vibro;
    private Context context;


    ///////////////////////////////////////////////////////
    //конструктор. инициализируем всё, что будем в дальнейшем использовать.
    public GameManager(Context context) {
        //Log.d("GMInfo", "GM.Constructor");
        this.context = context; //чтобы иметь в дальнейшем доступ к контексту.
        vibro = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE); //получили доступ к вибре

        // игровые объекты
        backgroundPaintForAllScreen = new Paint(); //заливка экрана
        backgroundPaintForAllScreen.setColor(Color.GREEN);
        backgroundPaintForAllScreen.setStyle(Style.FILL);

        background = new Rect();

        gameField = new Field(8, 9, 9); //игровое поле
        foundTable = new FoundTable(); //Таблица с найденными
        scoreTable = new ScoreTable(); //Таблица очков
        newGameButton = new NewGameButton(); //кнопка новой игры
        looseButton = new LooseGameButton(); //кнопка сдаться
        looseButton.setAvalibleToClickStyle(); //делаем кнопку кликабельной
        //это поток, так что при повороте экрана будет вызван метод инит, а конструктор создаётся один раз.


        scoreTable.setScore(0);
        scoreTable.setCount(0);


        textPaint = new Paint();
        textPaint.setColor(Color.DKGRAY);
        textPaint.setStrokeWidth(1);

        DrawScreenHelper = new DrawHelper() {
            public void helpToDraw(Canvas canvas) {
                refreshCanvas(canvas);
            }
        };
    }


    ////////////////////////////////////////////////////////

    /**
     * Рисование на экране
     *
     * @param helper Класс для рисовальной функции
     */
    private void draw(DrawHelper helper) {
        Canvas canvas = null;
        try {
            // подготовка Canvas-а
            canvas = drawingArea.lockCanvas();
            synchronized (drawingArea) {
                if (isInitialized) {
                    helper.helpToDraw(canvas);
                }
            }
        } catch (Exception e) {
        } finally {
            if (canvas != null) {
                drawingArea.unlockCanvasAndPost(canvas);
            }

        }
    }


    ///////////////////////////////////////////////

    /**
     * Задание состояния потока
     *
     * @param running
     */
    public void setRunning(boolean running) {
        isRunning = running;
        wasRunned = true;
    }

    //проверка состояния потока
    public boolean isRunning() {
        return isRunning;
    }

    public boolean wasRunned() {
        return wasRunned;
    }


    /////////////////////////////////////////////
    @Override
    /**Запуск отрисовки игры.  (Действия, выполняемые в потоке) */

    public void run() {
        //Log.d("GMInfo", "GM.run");
        draw(DrawScreenHelper);

    }

    //////////////////////////////////////////

    /**
     * Обновление объектов на экране
     */
    private void refreshCanvas(Canvas canvas) {
        // рисуем игровые объекты

        canvas.drawBitmap(bitmapBackground, null, background, backgroundPaintForAllScreen);
        gameField.draw(canvas);
        foundTable.draw(canvas, resources);
        newGameButton.draw(canvas, resources);
        scoreTable.draw(canvas, resources);
        looseButton.draw(canvas, resources);
    }

////////////////////////////////////////////

    /**
     * Инициализация объектов, в соответствии с размерами экрана
     *
     * @param screenHeight Высота экрана
     * @param screenWidth  Ширина экрана
     */
    public void initPositions(SurfaceHolder surfaceHolder,
                              int screenHeight, int screenWidth, Resources res) {
        //Log.d("GMInfo", "GM.init");
        drawingArea = surfaceHolder;
        this.resources = res;
        gameField.toInitField(screenWidth, screenHeight, 10, res);
        isInitialized = true;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        background.set(0, 0, screenWidth, screenHeight);
        foundTable.calculateGabarites(gameField, screenHeight, screenWidth);
        foundTable.setNumberOfAllFoxes(gameField.getNumberOfAllFoxes());
        foundTable.setNumberOfOpenFoxes(gameField.getNumberOfOpenFoxes());

        scoreTable.calculateGabarites(gameField, screenHeight, screenWidth);


        newGameButton.calculateGabarites(gameField, screenHeight, screenWidth);
        looseButton.calculateGabarites(gameField, screenHeight, screenWidth);


        bitmapBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.background3);
        if (res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bitmapBackground = Bitmap.createBitmap(bitmapBackground, 0, 0,
                    bitmapBackground.getWidth(), bitmapBackground.getHeight(),
                    matrix, false);
        }


        draw(DrawScreenHelper);
        //Log.d("GMInfo", "GM.initEND");
    }

    private void createNewGame() {

        gameField = new Field(8, 9, 9);
        scoreTable.setScore(0);
        scoreTable.setCount(0);
        looseButton.setAvalibleToClickStyle();
        initPositions(drawingArea, screenHeight, screenWidth, resources);
        isGameContinues = true;
    }


    ///////////////////////////////////////////////
    //обработка события нажатия на экран в захваченной области
    public boolean toClick(MotionEvent event) {

        //Log.d("GMInfo", "GM.click");
        Point coords = new Point();
        coords.x = (int) event.getX();
        coords.y = (int) event.getY();

        int whatToDo;
        if (newGameButton.toClick(coords)) {
            //Log.d("Clicks", "NewGamebutton is clicked");
            //нужно создать новую игру
            looseButton.setAvalibleToClickStyle();
            if (vibro != null) {
                vibro.vibrate(100);
            }
            createNewGame();


        } else {
            //Log.d("GMInfo", "GM.click.else");
            if (isGameContinues) //если игра продолжается
            {
                //Log.d("GMInfo", "GM.click.else.Game continues");
                if (looseButton.toClick(coords)) //и нажата кнопка проиграть
                {
                    //Log.d("GMInfo", "GM.click.else.Game continues.loosebutton is clicked");
                    //нужно открыть все ячейки
                    looseButton.setUnableToClickStyle();
                    if (!gameField.isGameLoosed()) //если не проиграна. Как она может быть проиграна, если продолжается?
                    {
                        //Log.d("GMInfo", "GM.click.else.Game continues.if game is not loosed");
                        if (vibro != null) {
                            vibro.vibrate(500);
                        }
                        gameField.toOpenAllCells();
                        isGameContinues = !gameField.isGameLoosed();
                        draw(DrawScreenHelper);
                    }

                } else //нажаты ли ячейки
                {
                    whatToDo = gameField.toOpenCell(coords);
                    if (whatToDo >= 0)//если ячейка открывается
                    {
                        //Log.d("Clicks", "game field is clicked");
                        scoreTable.increaseCountOfMoves();

                        if (whatToDo > 0)//если это лиска
                        {
                            scoreTable.toCalculateCount(gameField);
                            if (vibro != null) {
                                vibro.vibrate(250);
                            }
                            foundTable.setNumberOfOpenFoxes(gameField.getNumberOfOpenFoxes());

                        }
                        isGameContinues = !gameField.wereAllFoxesFound() && !gameField.isGameLoosed();
                        if (!isGameContinues)
                            looseButton.setUnableToClickStyle();
                        draw(DrawScreenHelper);
                    }

                }
            }


        }


        return true;
    }


}
