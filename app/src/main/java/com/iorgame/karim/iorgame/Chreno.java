package com.iorgame.karim.iorgame;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by karim on 29/05/2017.
 */

public class Chreno {


    private int year;
    private int month;
    private int day;
    private int hour_start;
    private int hour_end;
    private int ireserveIt;


    public int getIreserveIt() {
        return ireserveIt;
    }

    public void setIreserveIt(int ireserveIt) {
        this.ireserveIt = ireserveIt;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour_start() {
        return hour_start;
    }

    public void setHour_start(int hour_start) {
        this.hour_start = hour_start;
    }

    public int getHour_end() {
        return hour_end;
    }

    public void setHour_end(int hour_end) {
        this.hour_end = hour_end;
    }
    private Date toDate(){
        Date date=null;
        date=new Date();
        date.setHours(hour_start);
        //date.setMinutes(0);
        date.setYear(year);
        date.setMonth(month);
        date.setDate(day);
        return date;
    }
    public boolean comparDate(Calendar date){
         if(date.get(Calendar.YEAR)==year) {
             if (month == date.get(Calendar.MONTH)+1) {
                 if (day == date.get(Calendar.DAY_OF_MONTH)) {
                     if (hour_start == date.get(Calendar.HOUR_OF_DAY)) {
                         return true;
                     }
                 }
             }
         }
        return false;
    }
}
