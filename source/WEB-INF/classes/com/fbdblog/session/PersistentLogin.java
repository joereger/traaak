package com.fbdblog.session;

import com.fbdblog.util.Num;
import com.fbdblog.util.Util;
import com.fbdblog.util.Time;
import com.fbdblog.dao.hibernate.HibernateUtil;
import com.fbdblog.dao.Userpersistentlogin;
import com.fbdblog.systemprops.SystemProperty;

import javax.servlet.http.Cookie;
import java.util.*;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.apache.log4j.Logger;

/**
 * Handles functions regarding persistent login via a browser cookie.
 * The cookie should consist of the user’s username, followed by a
 * separator character (-), followed by some large random number.
 * The server keeps a table of number->username associations, which is
 * looked up to verify the validity of the cookie. If the cookie supplies
 * a random number and username that are mapped to each other in the table,
 * the login is accepted.
 * At any time, a username may be mapped to several such numbers,
 * due to login at multiple browsers.
 * Also, while incredibly unlikely, it does not matter if two usernames
 * are mapped to the same random number.  They will not interfere.
 *
 */
public class PersistentLogin {

    public static int daysToKeepPersistentRecordWithoutLogin = 365;
    public static String cookieName = "keepmeloggedintotraaak";


    public static int checkPersistentLogin(javax.servlet.http.Cookie cookie){
        Logger logger = Logger.getLogger(PersistentLogin.class);
        if (cookie!=null && cookie.getName().equals(cookieName)){
            //Split the value on the dash and get the userid
            String value = cookie.getValue();
            String[] split = value.split("-", 2);
            if (split!=null && split.length>1){
                String firstVal = split[0];
                String secondVal = split[1];
                if (Num.isinteger(firstVal)){
                    Criteria crit = HibernateUtil.getSession().createCriteria(Userpersistentlogin.class);
                    crit.add(Restrictions.eq("userid", Integer.parseInt(firstVal)));
                    crit.add(Restrictions.eq("randomstring", secondVal));
                    List<Userpersistentlogin> userpersistentlogins = crit.list();
                    for (Iterator<Userpersistentlogin> iterator = userpersistentlogins.iterator(); iterator.hasNext();){
                        Userpersistentlogin userpersistentlogin = iterator.next();
                        userpersistentlogin.setLastusedtologin(Time.nowInGmtDate());
                        try{userpersistentlogin.save();}catch(Exception ex){logger.error("",ex);};
                        return userpersistentlogin.getUserid();
                    }
                }
            }
        }
        return -1;
    }

    public static Cookie[] getPersistentCookies(int accountuserid, javax.servlet.http.HttpServletRequest request){
        //Create the domain
        UrlSplitter urlSplitter = new UrlSplitter(request);
        Cookie[] outCookies = new Cookie[1];
        //Create a cookie with no domain specified
        outCookies[0] = createPersistentCookie(accountuserid, request, "");
        //Iterate all possible domains
        ArrayList<String> domains =  urlSplitter.getServernameAllPossibleDomains();
        for (Iterator it = domains.iterator(); it.hasNext(); ) {
            String domain = (String)it.next();
            outCookies = Util.addToCookieArray(outCookies, createPersistentCookie(accountuserid, request, domain));
        }
        return outCookies;
    }

    public static Cookie createPersistentCookie(int userid, javax.servlet.http.HttpServletRequest request, String domainToSetCookieOn){
        Logger logger = Logger.getLogger(PersistentLogin.class);
        String randomString = Util.truncateString(request.getSession().getId(), 240);

        Cookie userCookie = new Cookie(cookieName, userid+"-"+randomString);
        userCookie.setMaxAge(31536000);  //One Year
        if (!domainToSetCookieOn.equals("")){
            userCookie.setDomain("."+domainToSetCookieOn);
        }
        //@todo implement sslison system property
        if (SystemProperty.getProp("PROP_SSLISON").equals("1")){
            userCookie.setSecure(true);
        }
        userCookie.setPath("/");
        logger.debug("Creating cookie.<br>value=" + randomString + "<br>domainToSetCookieOn=" + domainToSetCookieOn);

        //Store this persistent cookie to the database
        Userpersistentlogin userpersistentlogin = new Userpersistentlogin();
        userpersistentlogin.setLastusedtologin(Time.nowInGmtDate());
        userpersistentlogin.setRandomstring(randomString);
        userpersistentlogin.setUserid(userid);
        try{userpersistentlogin.save();}catch(Exception ex){logger.error("",ex);};

        return userCookie;
    }

    public static Cookie createCookieToClearPersistentLogin(javax.servlet.http.HttpServletRequest request){
        Logger logger = Logger.getLogger(PersistentLogin.class);
        Cookie userCookie = new Cookie(cookieName, " ");
        userCookie.setMaxAge(0);  //Delete Cookie
        userCookie.setPath("/");
        if (SystemProperty.getProp("PROP_SSLISON").equals("1")){
            userCookie.setSecure(true);
        }

        //Now, delete the database record
        Cookie[] cookies = request.getCookies();
        if (cookies!=null && cookies.length>0){
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i]!=null && cookies[i].getName().equals(cookieName)){
                    //Split the value on the dash and get the accountid
                    String value = cookies[i].getValue();
                    String[] split = value.split("-", 2);
                    if (split!=null && split.length>1){
                        String firstVal = split[0];
                        String secondVal = split[1];
                        if (Num.isinteger(firstVal)){
                            Criteria crit = HibernateUtil.getSession().createCriteria(Userpersistentlogin.class);
                            crit.add(Restrictions.eq("userid", Integer.parseInt(firstVal)));
                            crit.add(Restrictions.eq("randomstring", secondVal));
                            List<Userpersistentlogin> userpersistentlogins = crit.list();
                            for (Iterator<Userpersistentlogin> iterator = userpersistentlogins.iterator(); iterator.hasNext();){
                                Userpersistentlogin userpersistentlogin = iterator.next();
                                try{userpersistentlogin.delete();}catch(Exception ex){logger.error("",ex);};
                            }
                        }
                    }
                }
            }
        }
        return userCookie;
    }




}
