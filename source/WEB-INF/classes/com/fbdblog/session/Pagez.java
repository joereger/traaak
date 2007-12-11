package com.fbdblog.session;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;

/**
 * User: Joe Reger Jr
 * Date: Dec 10, 2007
 * Time: 5:06:16 PM
 */
public class Pagez {

    private static ThreadLocal<Long> startTime = new ThreadLocal<Long>();



    public static void setStartTime(Long time){
        startTime.set(time);
    }

    public static Long getStartTime(){
        return startTime.get();
    }

    public static long getElapsedTime(){
        long timeend = new java.util.Date().getTime();
        long elapsedtime = timeend - startTime.get();
        return elapsedtime;
    }



}
