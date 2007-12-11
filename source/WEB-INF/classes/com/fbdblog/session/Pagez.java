package com.fbdblog.session;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;
import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Dec 10, 2007
 * Time: 5:06:16 PM
 */
public class Pagez {

    private static ThreadLocal<Long> startTime = new ThreadLocal<Long>();
    private static ThreadLocal<String> timezoneidTl = new ThreadLocal<String>();


    public static void setStartTime(Long time){
        startTime.set(time);
    }

    public static Long getStartTime(){
        if (startTime.get()==null){
            startTime.set((new Date()).getTime());
        }
        return startTime.get();
    }

    public static long getElapsedTime(){
        long timeend = new java.util.Date().getTime();
        long elapsedtime = timeend - startTime.get();
        return elapsedtime;
    }

    public static void setTz(String timezoneid){
        timezoneidTl.set(timezoneid);
    }

    public static String getTz(){
        if (timezoneidTl.get()==null || timezoneidTl.get().equals("")){
            timezoneidTl.set("EST");
        }
        return timezoneidTl.get();
    }



}
