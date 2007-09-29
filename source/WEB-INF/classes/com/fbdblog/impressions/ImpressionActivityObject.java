package com.fbdblog.impressions;

/**
 * User: Joe Reger Jr
 * Date: Sep 29, 2007
 * Time: 11:09:14 AM
 */
public class ImpressionActivityObject {

    private int userid=0;
    private int appid=0;
    private int year=0;
    private int month=0;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid=userid;
    }

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

}
