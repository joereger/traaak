package com.fbdblog.session;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.net.URLEncoder;
import java.util.Date;

import com.fbdblog.cache.providers.CacheFactory;

/**
 * User: Joe Reger Jr
 * Date: Dec 10, 2007
 * Time: 5:06:16 PM
 */
public class Pagez {

    private static ThreadLocal<Long> startTime = new ThreadLocal<Long>();
    private static ThreadLocal<String> timezoneidTl = new ThreadLocal<String>();
    private static ThreadLocal<UserSession> userSessionLocal = new ThreadLocal<UserSession>();
    private static ThreadLocal<HttpServletRequest> requestLocal = new ThreadLocal<HttpServletRequest>();
    private static ThreadLocal<HttpServletResponse> responseLocal = new ThreadLocal<HttpServletResponse>();


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

    public static void setUserSessionAndUpdateCache(UserSession userSession){
        CacheFactory.getCacheProvider().put(getRequest().getSession().getId(), "userSession", userSession);
        userSessionLocal.set(userSession);
    }

    public static void setUserSession(UserSession userSession){
        userSessionLocal.set(userSession);
    }

    public static void setRequest(HttpServletRequest request){
        requestLocal.set(request);
    }

    public static void setResponse(HttpServletResponse response){
        responseLocal.set(response);
    }

    public static void sendRedirect(String url){
        sendRedirect(url, true);
    }

    public static void sendRedirect(String url, boolean doFancyDpageStuff){
        Logger logger = Logger.getLogger(Pagez.class);
        //Web ui
        url = responseLocal.get().encodeRedirectURL(url);
        if (!responseLocal.get().isCommitted()){
            responseLocal.get().resetBuffer();
            try{responseLocal.get().sendRedirect(url);}catch(Exception ex){logger.error("", ex);}
        }
    }

    public static HttpServletRequest getRequest(){
        return requestLocal.get();
    }

    public static HttpServletResponse getResponse(){
        return responseLocal.get();
    }

    public static UserSession getUserSession(){
        return userSessionLocal.get();
    }



}
