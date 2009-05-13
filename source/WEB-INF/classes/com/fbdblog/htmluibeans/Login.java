package com.fbdblog.htmluibeans;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.Iterator;
import java.io.Serializable;

import com.fbdblog.dao.User;
import com.fbdblog.dao.hibernate.HibernateUtil;

import com.fbdblog.util.Str;
import com.fbdblog.htmlui.UserSession;
import com.fbdblog.htmlui.Pagez;
import com.fbdblog.htmlui.ValidationException;
import com.fbdblog.session.PersistentLogin;
import com.fbdblog.xmpp.SendXMPPMessage;



import javax.servlet.http.Cookie;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class Login implements Serializable {

    private String email;
    private String password;
    private boolean keepmeloggedin = true;

    public Login(){

    }

    public void initBean(){

    }


    public void login() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("login() called.");
        logger.debug("keepmeloggedin="+keepmeloggedin);
        List users = HibernateUtil.getSession().createQuery("FROM User as user WHERE user.email='"+ Str.cleanForSQL(email)+"' AND user.password='"+Str.cleanForSQL(password)+"'").setMaxResults(1).list();
        if (users.size()==0){
            vex.addValidationError("Email/password incorrect.");
            throw vex;
        }
        for (Iterator it = users.iterator(); it.hasNext(); ) {
            User user = (User)it.next();
            if (user.getIsenabled()){
                //Create a new session so that I can manually move stuff over and guarantee it's clean
                UserSession userSession = new UserSession();
                userSession.setUser(user);
                userSession.setIsloggedin(true);



                //Set persistent login cookie, if necessary
                if (keepmeloggedin){
                    logger.debug("keepmeloggedin=true");
                    //Get all possible cookies to set
                    Cookie[] cookies = PersistentLogin.getPersistentCookies(user.getUserid(), Pagez.getRequest());
                    logger.debug("cookies.length="+cookies.length);
                    //Add a cookies to the response
                    for (int j = 0; j < cookies.length; j++) {
                        logger.debug("Setting persistent login cookie name="+cookies[j].getName()+" value="+cookies[j].getValue()+" cookies[j].getDomain()="+cookies[j].getDomain()+" cookies[j].getPath()="+cookies[j].getPath());
                        Pagez.getResponse().addCookie(cookies[j]);
                    }
                }


                //Notify via XMPP
                SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SALES, "dNeero User Login: "+ user.getFirstname() + " " + user.getLastname() + " ("+user.getEmail()+")");
                xmpp.send();

                //This is where the new UserSession is actually bound to Pagez.getUserSession()
                Pagez.setUserSessionAndUpdateCache(userSession);



            } else {
                vex.addValidationError("This account is not active.  Please contact the system administrator if you feel this is an error.");
                throw vex;
            }
        }
    }

    public void logout() throws ValidationException{
        Pagez.setUserSession(new UserSession());
        Pagez.setUserSessionAndUpdateCache(new UserSession());
        //Persistent Logout
        Pagez.getResponse().addCookie(PersistentLogin.createCookieToClearPersistentLogin(Pagez.getRequest()));
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public boolean getKeepmeloggedin() {
        return keepmeloggedin;
    }

    public void setKeepmeloggedin(boolean keepmeloggedin) {
        this.keepmeloggedin = keepmeloggedin;
    }
}
