package com.fbdblog.impressions;

/**
 * User: Joe Reger Jr
 * Date: Sep 29, 2007
 * Time: 11:09:14 AM
 */
public class ImpressionActivityObject {

    private int appid=0;
    private int year=0;
    private int month=0;
    private int day=0;
    private String page="";



    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid=appid;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year=year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month=month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day=day;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page=page;
    }
}
