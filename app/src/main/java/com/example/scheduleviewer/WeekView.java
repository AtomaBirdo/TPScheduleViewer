package com.example.scheduleviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class WeekView extends AppCompatActivity {

    public class MyView extends View {

        String[] subjects = {"AP English Language", "AP American History", "Adv App Dev", "AP Calc BC", "AP Microeconomics", "French 3", "AP Physics"};

        public MyView(Context context){
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas){

            for (int i = 0; i < Period.periods.size(); i++){
                Period temp = Period.periods.get(i);
                try {
                    temp.setSubject(subjects[Integer.parseInt(temp.getPeriod()) - 1]);
                }
                catch(Exception e){}
                Period.periods.set(i, temp);
            }

            super.onDraw(canvas);
            //setBackgroundColor(Color.CYAN);
            int x = getWidth();
            int y = getHeight();
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

        public void drawAll(Canvas canvas, Paint paint, ArrayList<Period> periods){
            for (Period period: periods) {
                Period.drawPeriod(canvas, paint, period);
            }
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(new ScrollView(this));
        setContentView(new MyView(this));
    }
}
