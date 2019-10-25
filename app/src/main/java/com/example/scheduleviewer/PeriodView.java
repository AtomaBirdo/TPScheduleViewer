package com.example.scheduleviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;

public class PeriodView extends AppCompatActivity {

    public static class MyView extends Drawable {

        public MyView(){
            super();
        }

        @Override
        public void setAlpha(int alpha){

        }

        @Override
        public void setColorFilter(ColorFilter colorFilter){

        }

        @Override
        public int getOpacity(){
            return PixelFormat.OPAQUE;
        }

        @Override
        public void draw(Canvas canvas){

            for (int i = 0; i < Period.periods.size(); i++){
                Period temp = Period.periods.get(i);
                try {
                    temp.setSubject(Period.subjects.get(Integer.parseInt(temp.getPeriod()) - 1));
                }
                catch(Exception e){}
                Period.periods.set(i, temp);
            }

            //super.onDraw(canvas);
            //setBackgroundColor(Color.CYAN);
            int x = getBounds().width();
            int y = getBounds().height();
            int radius = 100;
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);

            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.STROKE);
            //paint.setStrokeWidth(2);
            //canvas.drawCircle(x / 2, y / 2, radius, paint);

            drawAll(canvas, paint, Period.periods);
/*
            canvas.drawRect(x / 2 - 200, y / 2 - 300, x / 2 + 200, y / 2 + 300, paint);

            paint.setTextSize(50);
            canvas.drawText("(๑•̀ㅂ•́)و✧", x / 2 - 100, y / 2, paint);*/
        }

        public void drawAll(Canvas canvas, Paint paint, ArrayList<Period> periods) {
            //Period previousPeriod = periods.get(0);
            Calendar now = Calendar.getInstance();
            int day = now.get(Calendar.DAY_OF_WEEK) - 1;
            WTime time = new WTime();
            Log.i("SHOWTIME", now.get(Calendar.HOUR_OF_DAY) + "");
            for (Period period : periods) {
                if (period.start.getDay() == day){
                    if (period.start.isBefore(time) && period.end.isAfter(time))
                    Period.drawCurrentPeriod(canvas, paint, period, Period.findNextPeriod(period.end));
                    //else previousPeriod = period;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);
        //setContentView(new ScrollView(this));
        MyView wv = new MyView();
        ImageView image = findViewById(R.id.image);
        image.setImageDrawable(wv);
    }
}
