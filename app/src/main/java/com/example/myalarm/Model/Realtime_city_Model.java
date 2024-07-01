package com.example.myalarm.Model;

public class Realtime_city_Model {
    String year;
    String month;
    String day;

    String hour;
    String minute;
    String seconds;
    String milliSeconds;
    String dateTime;
    String date;
    String time;
    String timeZone;
    String dayOfWeek;
    boolean dstActive;


    public Realtime_city_Model(String date, String dateTime, String day, String dayOfWeek, boolean dstActive, String hour, String milliSeconds, String minute, String month, String seconds, String time, String timeZone, String year) {
        this.date = date;
        this.dateTime = dateTime;
        this.day = day;
        this.dayOfWeek = dayOfWeek;
        this.dstActive = dstActive;
        this.hour = hour;
        this.milliSeconds = milliSeconds;
        this.minute = minute;
        this.month = month;
        this.seconds = seconds;
        this.time = time;
        this.timeZone = timeZone;
        this.year = year;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public boolean isDstActive() {
        return dstActive;
    }

    public void setDstActive(boolean dstActive) {
        this.dstActive = dstActive;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMilliSeconds() {
        return milliSeconds;
    }

    public void setMilliSeconds(String milliSeconds) {
        this.milliSeconds = milliSeconds;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getSeconds() {
        return seconds;
    }

    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}


// https://timeapi.io/api/Time/current/zone?timeZone=Asia/Ho_Chi_Minh

