package by.sergeantbulkin.drawingtool.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class DrawingView extends View
{
    public static final int LINE = 1;
    public static final int RECTANGLE = 2;
    public static final int SQUARE = 3;
    public static final int CIRCLE = 4;
    public static final int TRIANGLE = 5;

    private int currentShape;
    //----------------------------------------------------------------------------------------------
    // путь для рисования
    private Path drawPath;
    // Paint для рисования и для холста
    private Paint drawPaint, canvasPaint;
    // холст
    private Canvas drawCanvas;
    // битмап холста
    private Bitmap canvasBitmap;
    //----------------------------------------------------------------------------------------------
    public DrawingView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        setUpDrawing();
    }
    //----------------------------------------------------------------------------------------------
    //Установить цвет рисования
    public void setDrawPaintColor(int paintColor)
    {
        drawPaint.setColor(paintColor);
    }
    //----------------------------------------------------------------------------------------------
    private void setUpDrawing()
    {
        currentShape = 1;
        drawPath = new Path();
        drawPaint = new Paint();
        // начальный цвет
        drawPaint.setColor(0xFF000000);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(25);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }
    //----------------------------------------------------------------------------------------------
    //Задание размера View
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }
    //----------------------------------------------------------------------------------------------
    //Отображения рисунка, который нарисован пользователем
    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawBitmap(canvasBitmap, 0,0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float touchX = event.getX();
        float touchY = event.getY();
        //Log.d("TAG", "X: " + touchX + "|Y: " + touchY);
        // реакция на события "палец на экране", "движение по экрану" и "палец поднят"
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawPath.lineTo(touchX, touchY);
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }
        // перерисовать
        invalidate();
        return true;
    }
    //----------------------------------------------------------------------------------------------
}
