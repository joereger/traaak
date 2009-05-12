package com.fbdblog.session;

import com.fbdblog.dao.User;
import com.fbdblog.dao.Userrole;
import com.fbdblog.dao.App;
import com.fbdblog.dao.Userappsettings;
import com.fbdblog.dao.hibernate.HibernateUtil;
import com.fbdblog.facebook.FacebookUser;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: May 31, 2006
 * Time: 12:51:06 PM
 */
public class UserSession implements Serializable {

    //Note: Only use primitives to simplify clustering.
    //Example: userid as int and then getUser() calls on the clustered/cached hibernate layer.

    private int userid;
    private int appid;
    private int userappsettingsid;
    private boolean isloggedin = false;
    private boolean issysadmin = false;
    private String facebooksessionkey = "";
    private FacebookUser facebookUser = null;
    private boolean isnewappforthisuser = false;
    private boolean isweb = false;
    private boolean isfacebook = false;
  
    public UserSession(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("New UserSession created.");
    }

    public User getUser() {
        if (userid>0){
            return User.get(userid);
        } else {
            return null;
        }
    }

    public void setUser(User user) {
        if (user!=null){
            userid = user.getUserid();
            issysadmin = false;
            for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
                Userrole userrole = iterator.next();
                if (userrole.getRoleid()== Userrole.SYSADMIN){
                    issysadmin = true;
                }
            }
        } else {
            userid = 0;
        }
    }

    public App getApp() {
        if (appid>0){
            return App.get(appid);
        } else {
            return null;
        }
    }

    public void setApp(App app) {
        if (app!=null){
            appid = app.getAppid();
        } else {
            appid = 0;
        }
    }

    public Userappsettings getUserappsettings() {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (userappsettingsid>0){
            return Userappsettings.get(userappsettingsid);
        } else {
            Userappsettings userappsettings = FindUserappsettings.get(User.get(userid), App.get(appid));
            if (userappsettings!=null){
                userappsettingsid = userappsettings.getUserappsettingsid();
            }
            return userappsettings;
        }
    }

    public void setUserappsettings(Userappsettings userappsettings) {
        if (userappsettings!=null){
            userappsettingsid = userappsettings.getUserappsettingsid();
        } else {
            userappsettingsid = 0;
        }
    }

    public boolean getIsloggedin() {
        return isloggedin;
    }

    public void setIsloggedin(boolean isloggedin) {
        this.isloggedin = isloggedin;
    }




    public boolean getIssysadmin() {
        return issysadmin;
    }

    public void setIssysadmin(boolean issysadmin) {
        this.issysadmin = issysadmin;
    }

    public String getFacebooksessionkey() {
        return facebooksessionkey;
    }

    public void setFacebooksessionkey(String facebooksessionkey) {
        this.facebooksessionkey = facebooksessionkey;
    }

    public FacebookUser getFacebookUser() {
        return facebookUser;
    }

    public void setFacebookUser(FacebookUser facebookUser) {
        this.facebookUser = facebookUser;
    }

    public boolean getIsnewappforthisuser() {
        return isnewappforthisuser;
    }

    public void setIsnewappforthisuser(boolean isnewappforthisuser) {
        this.isnewappforthisuser=isnewappforthisuser;
    }

    public boolean getIsfacebook() {
        return isfacebook;
    }

    public void setIsfacebook(boolean isfacebook) {
        this.isfacebook=isfacebook;
    }

    public boolean getIsweb() {
        return isweb;
    }

    public void setIsweb(boolean isweb) {
        this.isweb=isweb;
    }
}
