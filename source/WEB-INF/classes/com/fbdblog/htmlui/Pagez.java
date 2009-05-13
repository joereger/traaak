package com.fbdblog.htmlui;


import com.fbdblog.cache.providers.CacheFactory;
import com.fbdblog.systemprops.SystemProperty;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.ServletOutputStream;

import org.apache.log4j.Logger;

import java.net.URLEncoder;
import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Oct 28, 2007
 * Time: 11:05:30 AM
 */
public class Pagez {

    private static ThreadLocal<HttpServletRequest> requestLocal = new ThreadLocal<HttpServletRequest>();
    private static ThreadLocal<HttpServletResponse> responseLocal = new ThreadLocal<HttpServletResponse>();
    private static ThreadLocal<UserSession> userSessionLocal = new ThreadLocal<UserSession>();
    private static ThreadLocal<BeanMgr> beanMgrLocal = new ThreadLocal<BeanMgr>();
    private static ThreadLocal<Long> startTime = new ThreadLocal<Long>();
    private static ThreadLocal<String> timezoneidTl = new ThreadLocal<String>();


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

    public static void setUserSessionAndUpdateCache(UserSession userSession){
        CacheFactory.getCacheProvider().put(getRequest().getSession().getId(), "userSession", userSession);
        userSessionLocal.set(userSession);
    }

    public static void setUserSession(UserSession userSession){
        userSessionLocal.set(userSession);
    }

    public static void setBeanMgr(BeanMgr beanMgr){
        beanMgrLocal.set(beanMgr);
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

    public static BeanMgr getBeanMgr(){
        return beanMgrLocal.get();
    }

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
