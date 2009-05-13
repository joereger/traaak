package com.fbdblog.htmluibeans;

import com.fbdblog.dao.hibernate.HibernateUtil;
import com.fbdblog.htmlui.Pagez;
import com.fbdblog.util.Num;
import com.fbdblog.util.Str;
import com.fbdblog.session.PersistentLogin;

import java.util.List;
import java.io.Serializable;

import org.hibernate.criterion.Restrictions;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;

/**
 * User: Joe Reger Jr
 * Date: Nov 9, 2006
 * Time: 11:18:03 AM
 */
public class AccountIndex implements Serializable {

    private String msg = "";
    private boolean isfirsttimelogin = false;

    public AccountIndex(){

    }

    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if(Pagez.getUserSession().getUser()!=null && Num.isinteger(String.valueOf(Pagez.getUserSession().getUser().getUserid()))){
            //Set persistent login cookie, if necessary
            if (Pagez.getRequest().getParameter("keepmeloggedin")!=null && Pagez.getRequest().getParameter("keepmeloggedin").equals("1")){
                //Get all possible cookies to set
                Cookie[] cookies = PersistentLogin.getPersistentCookies(Pagez.getUserSession().getUser().getUserid(), Pagez.getRequest());
                //Add a cookies to the response
                for (int j = 0; j < cookies.length; j++) {
                    Pagez.getResponse().addCookie(cookies[j]);
                }
            }

        }

    }







    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean getIsfirsttimelogin() {
        return isfirsttimelogin;
    }

    public void setIsfirsttimelogin(boolean isfirsttimelogin) {
        this.isfirsttimelogin = isfirsttimelogin;
    }

 
}
