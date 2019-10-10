package com.example.scheduleviewer;

//package com.example.jay.myapplication;

//package com.example.admin.myapplication;
/**
 * Created by Jay on 1/29/18.
 * 1.1 added teacher room and Subject  VM
 * 1.2 changed findNext to always return a period and added findPrevious VM
 * 1.3 (to do)  added drawStringCentered and drawStringRight
 * 1.4 (to do) add concept of meeting and so you can meet a class 2 times a week IE meeting 2nd and 3rd
 *
 * Note the arrayList needs to be kept sorted
 * To do :
 *      javaDocs
 *      sort the list.
 */

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Calendar;


public class Period {
    WTime start;
    WTime end;
    String period;
    String teacher, room, subject;

    public static ArrayList<Period> periods = new ArrayList<Period>();

    public Period(WTime start, int periodLength, String period){
        this.start = start;
        this.end = new WTime(start, periodLength);
        this.period = period;
        subject = "";
        room = "";
        teacher = "";
    }
    public Period(Period a){
        start = a.start;
        end = a.end;
        period = a.period;
        teacher = a.teacher;
        room = a.room;
        subject = a.subject;
    }
    public Period (WTime start, WTime end, String period){
        this.start = start;
        this.end = end;
        this.period = period;
        subject = "";
        room = "";
        teacher = "";
    }
    public void setSubject(String g){
        subject = g;
    }
    public String getSubject(){
        return subject;
    }
    public void setRoom(String g){
        room = g;
    }
    public String getRoom(){
        return room;
    }
    public void setTeacher(String g){
        teacher= g;
    }
    public String getTeacher(){
        return teacher;
    }
    public String getPeriod(){
        return period;
    }
    public static int loadPeriods(InputStream PeriodData)throws IOException{
        Period p;
        WTime start;
        int num = 0;
        String line = "";
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(PeriodData, Charset.forName("UTF-8")));
        try{
            reader.readLine();
            while ((line = reader.readLine())!= null){
                String[] tokens = line.split(",");
                start = new WTime(Integer.parseInt(tokens[0]),
                        Integer.parseInt(tokens[2]),Integer.parseInt(tokens[3]),0);
                p = new Period(start, Integer.parseInt(tokens[4]),(tokens[5]));
                periods.add(p);
                num++;
            }
        }
        catch (IOException e) {
            System.out.println("Error reading data file on line " + line);
            e.printStackTrace();
            throw e;
        }
        return num;
    }
    public static void setAllPeriodInfo(String num,String meets, String className, String room, String teacher){
        System.out.println("num "+num+" meets "+meets+" className "+className+" Room "+room+" Teacher "+teacher);
        int meetsSession = 1;
        int meetsNum = 0;
        for (Period p:periods){
            if (p.period.equals(num)){
                if (meets.length() < 1){
                    p.setSubject(className);
                    p.setRoom(room);
                    p.setTeacher(teacher);
                }
                else if (meets.length() > meetsNum && meets.substring(meetsNum,meetsNum+1).equals(""+meetsSession)){
                    meetsNum++;
                    meetsSession++;
                    p.setSubject(className);
                    p.setRoom(room);
                    p.setTeacher(teacher);
                }
            }
        }
    }
    public static void setAllPeriodInfo(String num, String className){
        setAllPeriodInfo(num,"",className,"","");
    }
    public static Period findContainingPeriod(WTime target){
        for (Period p:periods){
            if (p.contains(target)){
                return p;
            }
        }
        return null;
    }
    //Returns the period before the current passing block or the previous period to the current period
    // if between last period of the week and the first period of the week return the last period of the week
    public static Period findPreviousPeriod(WTime target){
        Period previous = null;
        Period current = null;
        current = findContainingPeriod(target);
        if (current != null){  //return the previous period
            int index = periods.indexOf(current);
            if (index > 0)
                return periods.get(index-1);
            else
                return periods.get(periods.size()-1);
        }
        else{    //not a current period ie a passing block
            current  = findNextPeriod(target);
            if (periods.indexOf(current) == 0)
                return periods.get(periods.size()-1);
            else
                return periods.get(periods.indexOf(current)-1);
        }
        // return null;
    }
    //Returns the period after the current passing block or the period after current period
    // if between last period of the week and the first period of the week return the first period of the week
    public static Period findNextPeriod(WTime target){
        Period previous = null;
        for (Period p:periods){
            if (!p.startsAfter(target))
                return p;
        }
        return periods.get(0);
    }

    public boolean startsAfter(WTime target){
        return start.isBefore(target);
    }

    public boolean contains(WTime target){
        if (start.isBefore(target) && end.isAfter(target))
            return true;
        return false;
    }

    public void setStart(WTime a){
        start = a;
    }

    public void setPeriodNumber(String g){
        period = g;
    }

    /*public void draw(Graphics g ){
        WTime eight = new WTime(8,0);
        int gs =(start.ticks - eight.ticks)/400;
        int ge =(end.ticks- eight.ticks)/400;
        g.drawRect(20,gs,120,ge-gs);
        // g.drawString(""+start,30,gs+20);
        g.drawString(start.getHourAMPM()+":"+start.getMinuteS(),30,gs+20);
        g.drawString(end.getHourAMPM()+":"+end.getMinuteS(),105,ge-10);
        g.drawString(subject,40,(ge+gs)/2+10);
        g.drawString(getPeriod(),75,(ge+gs)/2);

    }*/

    public static void drawDayPeriod(Canvas canvas, Paint paint, Period period){
        WTime eight = new WTime(period.start.getDay(),8,0);
        int gs = (period.start.ticks - eight.ticks) / 175 + 20;
        int ge = (period.end.ticks - eight.ticks) / 175 + 20;
        int left = 100;
        int right = 400;
        int length;
        canvas.drawRect(left, gs, right, ge, paint);
        paint.setTextSize(36);

        length = (period.end.getHourAMPM()+":"+period.start.getMinuteS()).length();
        if (length > 4){
            canvas.drawText(period.end.getHourAMPM()+":"+period.end.getMinuteS(),right - 100,ge-20,paint);
        }else{
            canvas.drawText(period.end.getHourAMPM()+":"+period.end.getMinuteS(),right - 90,ge-20,paint);
        }
        canvas.drawText(period.start.getHourAMPM()+":"+period.start.getMinuteS(),left + 10,gs+40,paint);

        length = period.subject.length();
        try {
            canvas.drawText(Integer.parseInt(period.getPeriod()) + "", (left + right) / 2 - 20, (ge + gs) / 2 - 15, paint);
        }
        catch(Exception e){
            canvas.drawText(period.getPeriod(), (left+right)/2 - (period.period.length() * 10), (ge + gs) / 2, paint);
        }
        if (length > 15){
            paint.setTextSize(29);
            canvas.drawText(period.subject,(left+right)/2 - (length * 7),(ge+gs)/2 + 15,paint);
        }else{
            canvas.drawText(period.subject,(left+right)/2 - (length * 10),(ge+gs)/2 + 15,paint);
        }
    }

    public static void drawPeriod(Canvas canvas, Paint paint, Period period){
        WTime eight = new WTime(period.start.getDay(),8,0);
        int gs = (period.start.ticks - eight.ticks) / 175 + 20;
        int ge = (period.end.ticks - eight.ticks) / 175 + 20;
        int left = 100 + 350 * (period.start.getDay() - 1);
        int right = 400 + 350 * (period.start.getDay() - 1);
        int length;
        canvas.drawRect(left, gs, right, ge, paint);
        paint.setTextSize(36);

        length = (period.end.getHourAMPM()+":"+period.start.getMinuteS()).length();
        if (length > 4){
            canvas.drawText(period.end.getHourAMPM()+":"+period.end.getMinuteS(),right - 100,ge-20,paint);
        }else{
            canvas.drawText(period.end.getHourAMPM()+":"+period.end.getMinuteS(),right - 90,ge-20,paint);
        }
        canvas.drawText(period.start.getHourAMPM()+":"+period.start.getMinuteS(),left + 10,gs+40,paint);

        length = period.subject.length();
        try {
            canvas.drawText(Integer.parseInt(period.getPeriod()) + "", (left + right) / 2 - 20, (ge + gs) / 2 - 15, paint);
        }
        catch(Exception e){
            canvas.drawText(period.getPeriod(), (left+right)/2 - (period.period.length() * 10), (ge + gs) / 2, paint);
        }
        if (length > 15){
            paint.setTextSize(29);
            canvas.drawText(period.subject,(left+right)/2 - (length * 7),(ge+gs)/2 + 15,paint);
        }else{
            canvas.drawText(period.subject,(left+right)/2 - (length * 10),(ge+gs)/2 + 15,paint);
        }

    }

    public static void loadAllPeriods(){
        periods.clear();

        periods.add(new Period(new WTime(1,8,20), 70, "1"));
        periods.add(new Period(new WTime(1,9,35), 45, "2"));
        periods.add(new Period(new WTime(1,10,25), 70, "3"));
        periods.add(new Period(new WTime(1,11,45), 25, "Lunch"));
        periods.add(new Period(new WTime(1,12,30), 45, "4"));
        periods.add(new Period(new WTime(1,13,20), 45, "5"));
        periods.add(new Period(new WTime(1,14,10), 45, "6"));

        periods.add(new Period(new WTime(2,8,20), 70, "7"));
        periods.add(new Period(new WTime(2,9,35), 45, "4"));
        periods.add(new Period(new WTime(2,10,25), 70, "2"));
        periods.add(new Period(new WTime(2,11,45), 25, "Lunch"));
        periods.add(new Period(new WTime(2,12,30), 45, "5"));
        periods.add(new Period(new WTime(2,13,20), 45, "1"));
        periods.add(new Period(new WTime(2,14,10), 45, "3"));

        periods.add(new Period(new WTime(3,8,20), 45, "5"));
        periods.add(new Period(new WTime(3,9,10), 45, "6"));
        periods.add(new Period(new WTime(3,10,00), 45, "1"));
        periods.add(new Period(new WTime(3,10,50), 45, "7"));

        periods.add(new Period(new WTime(4,8,20), 70, "4"));
        periods.add(new Period(new WTime(4,9,35), 45, "7"));
        periods.add(new Period(new WTime(4,10,25), 70, "6"));
        periods.add(new Period(new WTime(4,11,45), 25, "Lunch"));
        periods.add(new Period(new WTime(4,12,30), 45, "2"));
        periods.add(new Period(new WTime(4,13,20), 45, "3"));
        periods.add(new Period(new WTime(4,14,10), 45, "1"));

        periods.add(new Period(new WTime(5,8,00), 45, "3"));
        periods.add(new Period(new WTime(5,8,50), 45, "2"));
        periods.add(new Period(new WTime(5,9,40), 70, "5"));
        periods.add(new Period(new WTime(5,11,00), 40, "Chapel"));
        periods.add(new Period(new WTime(5,11,45), 25, "Lunch"));
        periods.add(new Period(new WTime(5,12,30), 45, "6"));
        periods.add(new Period(new WTime(5,13,20), 45, "7"));
        periods.add(new Period(new WTime(5,14,10), 45, "4"));
    }

    public static ArrayList<Period> getTodaysPeriods(int dDay){
        ArrayList <Period> dayPeriods = new ArrayList<Period>();
        for(Period p:periods){
            if(p.start.getDay() == dDay)
                dayPeriods.add(p);
        }
        return dayPeriods;
    }
    public String toString(){
        return "Period :"+period+" starts "+start+" ends "+end;
    }
    // for testing
    public static void main(String[] args) {
        Period test1 = new Period(new WTime(1, 8, 40), new WTime(1, 9, 30), "1");
        System.out.println(test1);
        InputStream f = null;
        try {
            f = new FileInputStream("PeriodTimes.csv");
            loadPeriods(f);
        } catch (IOException e) {
            System.out.println("file not found " + e);
        }
        setAllPeriodInfo("4", "Lunch");
        setAllPeriodInfo("5", "App Development");
        WTime a = new WTime();
        // WTime a = new WTime(6,14,30,0);  //saturday after classes
        //  WTime a = new WTime(0,14,30,0);   // Sunday Morning
        //WTime a = new WTime(1,8,30,0);   // Monday Morning
        //WTime a = new WTime(1, 9, 32, 0);   // Monday Morning passing block
        System.out.println(a);
        System.out.println(Period.findContainingPeriod(a));
        System.out.println(Period.findNextPeriod(a));
        System.out.println(Period.findPreviousPeriod(a));
        //  for (Period a: periods){
        //    System.out.println(a);
        //  }
    }}