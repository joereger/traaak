package com.fbdblog.session;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.List;
import java.util.Calendar;

import com.fbdblog.dao.hibernate.HibernateUtil;
import com.fbdblog.dao.User;
import com.fbdblog.dao.Userappactivity;
import com.fbdblog.facebook.FindApp;
import com.fbdblog.facebook.FacebookUser;
import com.fbdblog.facebook.FindUserFromFacebookUid;
import com.fbdblog.cache.providers.CacheFactory;
import com.fbdblog.xmpp.SendXMPPMessage;
import com.fbdblog.util.Time;
import com.fbdblog.UserappstatusUtil;
import com.facebook.api.FacebookRestClient;
import com.facebook.api.FacebookException;

/**
 * User: Joe Reger Jr
 * Date: Jul 18, 2006
 * Time: 9:50:38 AM
 */
public class FilterMain implements Filter {

    private FilterConfig filterConfig = null;
    Logger logger = Logger.getLogger(this.getClass().getName());
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void destroy() {
        this.filterConfig = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {


        try{
            HttpServletRequest httpServletRequest = (HttpServletRequest)request;
            HttpServletResponse httpServletResponse = (HttpServletResponse)response;
            if (httpServletRequest.getRequestURL().indexOf("jpg")==-1 && httpServletRequest.getRequestURL().indexOf("css")==-1 && httpServletRequest.getRequestURL().indexOf("gif")==-1 && httpServletRequest.getRequestURL().indexOf("png")==-1){
                logger.debug("Start UserSessionCreator");

                //Set up Pagez
                Pagez.setRequest(httpServletRequest);
                Pagez.setResponse(httpServletResponse);

                //Debug
                UrlSplitter urlSplitter = new UrlSplitter(httpServletRequest);

                //Find the userSession
                Object obj = CacheFactory.getCacheProvider().get(httpServletRequest.getSession().getId(), "userSession");
                if (obj!=null && (obj instanceof UserSession)){
                    logger.debug("found a userSession in the cache");
                    Pagez.setUserSession((UserSession)obj);
                } else {
                    logger.debug("no userSession in cache");
                    UserSession userSession = new UserSession();
                    Pagez.setUserSessionAndUpdateCache(userSession);
                }

                //Persistent login start
                boolean wasAutoLoggedIn = false;
                if (1==1){
                    if (!Pagez.getUserSession().getIsloggedin()){
                        //See if the incoming request has a persistent login cookie
                        Cookie[] cookies = httpServletRequest.getCookies();
                        logger.debug("looking for cookies");
                        if (cookies!=null && cookies.length>0){
                            logger.debug("cookies found.");
                            for (int i = 0; i < cookies.length; i++) {
                                logger.debug("cookies[i].getName()="+cookies[i].getName());
                                logger.debug("cookies[i].getValue()="+cookies[i].getValue());
                                if (cookies[i].getName().equals(PersistentLogin.cookieName)){
                                    logger.debug("persistent cookie found.");
                                    int useridFromCookie = PersistentLogin.checkPersistentLogin(cookies[i]);
                                    if (useridFromCookie>-1){
                                        logger.debug("setting userid="+useridFromCookie);
                                        User user = User.get(useridFromCookie);
                                        if (user!=null && user.getUserid()>0 && user.getIsenabled()){
                                            UserSession newUserSession = new UserSession();
                                            newUserSession.setUser(user);
                                            newUserSession.setIsloggedin(true);
                                            Pagez.setUserSessionAndUpdateCache(newUserSession);
                                            wasAutoLoggedIn = true;
                                            //Notify via XMPP
                                            SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SALES, "dNeero User Auto-Login: "+ user.getFirstname() + " " + user.getLastname() + " ("+user.getEmail()+")");
                                            xmpp.send();
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                logger.debug("after persistent login and wasAutoLoggedIn="+wasAutoLoggedIn);
                logger.debug("after persistent login and Pagez.getUserSession().getIsloggedin()="+Pagez.getUserSession().getIsloggedin());
                //Persistent login end

                //Track app uninstalls
                if (request.getParameter("fb_sig_uninstall")!=null && request.getParameter("fb_sig_uninstall").equals("1")){
                    //try{request.getRequestDispatcher("/fb/uninstall.jsp").forward(request, response);}catch(Exception ex){logger.error("",ex);}
                    if (!response.isCommitted()){ chain.doFilter(request, response); }
                    return;
                }

                //Reset the app add flag
                Pagez.getUserSession().setIsnewappforthisuser(false);

                //Need to identify which app this is
                Pagez.getUserSession().setApp(FindApp.findFromRequest(httpServletRequest));
                //If there's no app
                if (Pagez.getUserSession().getApp()==null || Pagez.getUserSession().getApp().getAppid()<=0) {
                    logger.debug("no valid app found so aborting UserSessionSetup");
                    if (!response.isCommitted()){ chain.doFilter(request, response); }
                    return;
                    //@todo how to handle unknown app?  list all apps with links to an add page?
                }

                //Facebook
                try {
                    //Need a session key
                    //auth_token should immediately be traded in for a valid fb_sig_session_key
                    //Can only convert auth_token to session key when I know which app this is
                    if (request.getParameter("auth_token")!=null && !request.getParameter("auth_token").trim().equals("") && Pagez.getUserSession().getApp()!=null && Pagez.getUserSession().getApp().getAppid()>0){
                        logger.debug("auth_token found in request... will try to convert to session_key");
                        FacebookRestClient facebookRestClient = new FacebookRestClient(Pagez.getUserSession().getApp().getFacebookapikey(), Pagez.getUserSession().getApp().getFacebookapisecret());
                        String facebooksessionkey = facebookRestClient.auth_getSession(request.getParameter("auth_token").trim());
                        Pagez.getUserSession().setFacebooksessionkey(facebooksessionkey);
                    } else {
                        //No auth_token was sent (it's only sent for new apps and new logins, etc) so look to session_key
                        logger.debug("no auth_token found in request, looking for fb_sig_session_key");
                        if ((request.getParameter("fb_sig_session_key")!=null && !request.getParameter("fb_sig_session_key").trim().equals(""))){
                            logger.debug("found a fb_sig_session_key in request");
                            Pagez.getUserSession().setFacebooksessionkey(request.getParameter("fb_sig_session_key").trim());
                        } else {
                            logger.debug("no fb_sig_session_key found in request... aborting UserSessionSetup");
                            if (!response.isCommitted()){ chain.doFilter(request, response); }
                            return;
                        }
                    }

                    //Pull userSession from cache
                    boolean foundSessionInCache = false;
                    Object obj2 = CacheFactory.getCacheProvider().get(Pagez.getUserSession().getFacebooksessionkey(), "userSessionKeyedOnFb");
                    if (obj2!=null && (obj2 instanceof UserSession)){
                        logger.debug("found a userSession in the cache");
                        UserSession userSession = (UserSession)obj2;
                        Pagez.setUserSessionAndUpdateCache(userSession);
                        foundSessionInCache = true;
                    } else {
                        logger.debug("no userSession in cache");
                    }

                    //Reset the app add flag
                    Pagez.getUserSession().setIsnewappforthisuser(false);

                    //In general try not to handle request vars below this line
                    //I only want to run this stuff when I see a new Facebook session key...
                    if (!foundSessionInCache) {
                        logger.debug("running heavy Facebook user setup with api calls due to new facebooksessionkey");
                        //Go get some details on this facebookuser
                        FacebookRestClient facebookRestClient = null;
                        try {
                            facebookRestClient = new FacebookRestClient(Pagez.getUserSession().getApp().getFacebookapikey(), Pagez.getUserSession().getApp().getFacebookapisecret(), Pagez.getUserSession().getFacebooksessionkey());
                            Pagez.getUserSession().setFacebookUser(new FacebookUser(facebookRestClient.users_getLoggedInUser(), Pagez.getUserSession().getFacebooksessionkey(), Pagez.getUserSession().getApp().getFacebookapikey(), Pagez.getUserSession().getApp().getFacebookapisecret()));
                        } catch (FacebookException fex) {
                            logger.error("Facebook Error fex", fex);
                        }

                        //Make sure we have a user
                        if (Pagez.getUserSession().getFacebookUser()!=null && !Pagez.getUserSession().getFacebookUser().getUid().trim().equals("")){
                            //See if we have this facebook user as a dNeero user
                            User user = FindUserFromFacebookUid.find(Pagez.getUserSession().getFacebookUser().getUid());
                            if (user!=null && user.getUserid()>0) {
                                //Is already a user
                                logger.debug("Found local user account");
                                Pagez.getUserSession().setUser(user);
                                logger.debug("Login to "+Pagez.getUserSession().getApp().getTitle()+": " + user.getFirstname() + " " + user.getLastname() + " (Facebookuid=" + user.getFacebookuid() + ")");
                                //Notify via XMPP
                                SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, Pagez.getUserSession().getApp().getTitle()+" Login: " + user.getFirstname() + " " + user.getLastname());
                                xmpp.send();
                            } else {
                                //Create a new User for this facebookuid
                                logger.debug("Creating a new user account for this facebookuid");
                                user = new User();
                                user.setCreatedate(Time.nowInGmtDate());
                                user.setFacebookuid(Pagez.getUserSession().getFacebookUser().getUid());
                                user.setFirstname(Pagez.getUserSession().getFacebookUser().getFirst_name());
                                user.setLastname(Pagez.getUserSession().getFacebookUser().getLast_name());
                                user.setIsenabled(true);
                                user.setEmail("");
                                user.setPassword("");
                                user.setTimezoneid(Time.getTimezoneidFromFacebookOffset(Pagez.getUserSession().getFacebookUser().getTimezoneoffset()));
                                try {user.save();} catch (Exception ex) {logger.error("",ex);}
                                //Store in session
                                Pagez.getUserSession().setUser(user);
                                //Notify via XMPP
                                SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "New "+Pagez.getUserSession().getApp().getTitle()+" User '" + Pagez.getUserSession().getFacebookUser().getFirst_name() + " " + Pagez.getUserSession().getFacebookUser().getLast_name() + "'");
                                xmpp.send();
                            }
                            //Set the timezoneid
                            Pagez.setTz(user.getTimezoneid());
                        } else {
                            logger.debug("userSession.getFacebookUser() is empty after calling facebook api");
                            //@todo how to handle facebook call to populate user is empty?
                        }
                    } else {
                        logger.debug("didn't find a new facebooksessionkey so didn't make api call to load facebook user");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    logger.error("",ex);
                }

                //Track app adds
                if (request.getParameter("postaddappname")!=null) {
                    //Set var in session so that I can call a banner add thing
                    if (Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getUser().getUserid()>0 && Pagez.getUserSession().getApp()!=null && Pagez.getUserSession().getApp().getAppid()>0) {
                        //Is this a new app for this user?
                        List<Userappactivity> uaas = HibernateUtil.getSession().createCriteria(Userappactivity.class)
                                                       .add(Restrictions.eq("userid", Pagez.getUserSession().getUser().getUserid()))
                                                       .add(Restrictions.eq("appid", Pagez.getUserSession().getApp().getAppid()))
                                                       .setCacheable(false)
                                                       .list();
                        if (uaas!=null && uaas.size()>0){
                            Pagez.getUserSession().setIsnewappforthisuser(false);
                        } else {
                            Pagez.getUserSession().setIsnewappforthisuser(true);
                        }
                        //Record the app activity
                        Calendar cal = Calendar.getInstance();
                        Userappactivity userappactivity=new Userappactivity();
                        userappactivity.setAppid(Pagez.getUserSession().getApp().getAppid());
                        userappactivity.setUserid(Pagez.getUserSession().getUser().getUserid());
                        userappactivity.setDate(Time.nowInGmtDate());
                        userappactivity.setYear(cal.get(Calendar.YEAR));
                        userappactivity.setMonth(cal.get(Calendar.MONTH)+1);
                        userappactivity.setIsinstall(true);
                        userappactivity.setIsuninstall(false);
                        try {userappactivity.save();} catch (Exception ex) {logger.error("",ex);}
                        //Record the app status
                        UserappstatusUtil.userInstalledApp(Pagez.getUserSession().getUser(), Pagez.getUserSession().getApp());
                        //Default to private
                        if (Pagez.getUserSession().getApp().getIsdefaultprivate()){
                            Pagez.getUserSession().getUserappsettings().setIsprivate(true);
                            try{Pagez.getUserSession().getUserappsettings().save();}catch(Exception ex){logger.error("", ex);}
                        }
                        //Send xmpp
                        SendXMPPMessage xmpp=new SendXMPPMessage(SendXMPPMessage.GROUP_CUSTOMERSUPPORT, Pagez.getUserSession().getApp().getTitle()+" installed by " + Pagez.getUserSession().getUser().getFirstname() + " " + Pagez.getUserSession().getUser().getLastname());
                        xmpp.send();
                    }
                }

                //Save UserSession in Cache
                CacheFactory.getCacheProvider().put(Pagez.getUserSession().getFacebooksessionkey(), "userSessionKeyedOnFb", Pagez.getUserSession());

            }
        } catch (Exception ex){
            logger.debug("Error setting up UserSession");
            logger.error("",ex);
        }

        //Continue processing stuff
        if (!response.isCommitted()){ chain.doFilter(request, response); }

    }

}
