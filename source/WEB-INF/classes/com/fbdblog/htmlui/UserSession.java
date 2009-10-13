package com.fbdblog.htmlui;

import com.fbdblog.dao.User;
import com.fbdblog.dao.Userrole;
import com.fbdblog.dao.App;
import com.fbdblog.dao.Userappsettings;
import com.fbdblog.facebook.FacebookUser;
import com.fbdblog.session.FindUserappsettings;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Calendar;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: May 31, 2006
 * Time: 12:51:06 PM
 */
public class UserSession implements Serializable {

    //Note: Only use primitives to simplify clustering.
    //Example: userid as int and then getUser() calls on the clustered/cached hibernate layer.

    private int userid;
    private boolean isloggedin = false;
    private boolean isAllowedToResetPasswordBecauseHasValidatedByEmail = false;
    private int referredbyOnlyUsedForSignup = 0;
    private String message = "";
    private Calendar createdate = Calendar.getInstance();
    private int appid;
    private int userappsettingsid;
    private boolean issysadmin = false;
    private String facebookapikey = "";
    private String facebookapisecret = "";
    private String facebooksessionkey = "";
    private FacebookUser facebookUser = null;
    private boolean isnewappforthisuser = false;
    private boolean isweb = false;
    private boolean isfacebook = false;

    public UserSession(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("New UserSession created.");
        try{
            createdate = Calendar.getInstance();

        } catch (Exception ex){
            logger.error("", ex);
        }
    }

    public User getUser() {
        if (userid>0){
            return User.get(userid);
        } else {
            return null;
        }
    }




    public void setUser(User user) {
        Logger logger = Logger.getLogger(this.getClass().getName());
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

    public boolean getIsloggedin() {
        return isloggedin;
    }

    public void setIsloggedin(boolean isloggedin) {
        this.isloggedin = isloggedin;
    }

    public boolean getIsAllowedToResetPasswordBecauseHasValidatedByEmail() {
        return isAllowedToResetPasswordBecauseHasValidatedByEmail;
    }

    public void setAllowedToResetPasswordBecauseHasValidatedByEmail(boolean allowedToResetPasswordBecauseHasValidatedByEmail) {
        isAllowedToResetPasswordBecauseHasValidatedByEmail = allowedToResetPasswordBecauseHasValidatedByEmail;
    }

    public int getReferredbyOnlyUsedForSignup() {
        return referredbyOnlyUsedForSignup;
    }

    public void setReferredbyOnlyUsedForSignup(int referredbyOnlyUsedForSignup) {
        this.referredbyOnlyUsedForSignup = referredbyOnlyUsedForSignup;
    }








    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if (this.message==null || this.message.equals("")){
            this.message = message;
        } else {
            this.message = this.message + "<br/>" + message;
        }
        if (message==null || message.equals("")){
            this.message = "";
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


    public Calendar getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Calendar createdate) {
        this.createdate=createdate;
    }


  

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

    public int getUserappsettingsid() {
        return userappsettingsid;
    }

    public void setUserappsettingsid(int userappsettingsid) {
        this.userappsettingsid=userappsettingsid;
    }

    public boolean getIssysadmin() {
        return issysadmin;
    }

    public void setIssysadmin(boolean issysadmin) {
        this.issysadmin=issysadmin;
    }

    public String getFacebooksessionkey() {
        return facebooksessionkey;
    }

    public void setFacebooksessionkey(String facebooksessionkey) {
        this.facebooksessionkey=facebooksessionkey;
    }

    public FacebookUser getFacebookUser() {
        return facebookUser;
    }

    public void setFacebookUser(FacebookUser facebookUser) {
        this.facebookUser=facebookUser;
    }

    public boolean getIsnewappforthisuser() {
        return isnewappforthisuser;
    }

    public void setIsnewappforthisuser(boolean isnewappforthisuser) {
        this.isnewappforthisuser=isnewappforthisuser;
    }

    public boolean getIsweb() {
        return isweb;
    }

    public void setIsweb(boolean isweb) {
        this.isweb=isweb;
    }

    public boolean getIsfacebook() {
        return isfacebook;
    }

    public void setIsfacebook(boolean isfacebook) {
        this.isfacebook=isfacebook;
    }

    public String getFacebookapikey() {
        return facebookapikey;
    }

    public void setFacebookapikey(String facebookapikey) {
        this.facebookapikey=facebookapikey;
    }

    public String getFacebookapisecret() {
        return facebookapisecret;
    }

    public void setFacebookapisecret(String facebookapisecret) {
        this.facebookapisecret=facebookapisecret;
    }
}
