package com.fbdblog.session;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import javax.servlet.ServletResponse;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fbdblog.facebook.FindAppFromApiKey;
import com.fbdblog.facebook.FacebookUser;
import com.fbdblog.facebook.FindUserFromFacebookUid;
import com.fbdblog.xmpp.SendXMPPMessage;
import com.fbdblog.dao.User;
import com.fbdblog.dao.App;
import com.fbdblog.dao.Userappactivity;
import com.fbdblog.dao.hibernate.HibernateUtil;
import com.fbdblog.util.Num;
import com.fbdblog.cache.providers.CacheProvider;
import com.fbdblog.cache.providers.CacheFactory;
import com.facebook.api.FacebookRestClient;
import com.facebook.api.FacebookException;

import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Sep 18, 2007
 * Time: 8:17:51 AM
 */
public class UserSessionSetup {

    public static void setup(HttpServletRequest request, ServletResponse response){
        //Logger
        Logger logger = Logger.getLogger(UserSessionSetup.class);

        //Debug
        UrlSplitter urlSplitter = new UrlSplitter(request);

        //Get UserSession from app server session
        UserSession userSession = null;
        Object ustmp = request.getSession().getAttribute("userSession");
        if (ustmp != null) {
            userSession = (UserSession) ustmp;
        } else {
            userSession = new UserSession();
        }


        //Track app uninstalls
        if (request.getParameter("fb_sig_uninstall")!=null && request.getParameter("fb_sig_uninstall").equals("1")){
            //try{request.getRequestDispatcher("/fb/uninstall.jsp").forward(request, response);}catch(Exception ex){logger.error(ex);}
            return;
        }

        //Reset the app add flag
        userSession.setIsnewappforthisuser(false);

        //Facebook
        try {
            //Need an api key to determine which app this is
            if (request.getParameter("fb_sig_api_key") != null && !request.getParameter("fb_sig_api_key").equals("")) {
                userSession.setApp(FindAppFromApiKey.find(request.getParameter("fb_sig_api_key")));
            }
            if (userSession.getApp()==null || userSession.getApp().getAppid()<=0) {
                logger.debug("no api_key found so looking to request.getParameter(\"postaddappname\")="+request.getParameter("postaddappname"));
                if (request.getParameter("postaddappname")!=null) {
                    List<App> apps = HibernateUtil.getSession().createCriteria(App.class)
                                                       .add(Restrictions.eq("facebookappname", request.getParameter("postaddappname")))
                                                       .setCacheable(true)
                                                       .list();
                    for (Iterator<App> iterator=apps.iterator(); iterator.hasNext();) {
                        App app=iterator.next();
                        userSession.setApp(app);
                    }
                }
            }
            if (userSession.getApp()==null || userSession.getApp().getAppid()<=0) {
                logger.debug("no valid app found");
                return;
                //@todo how to handle unknown api_key?  list all apps with links to an add page?
            }

            //Need a session key
            //auth_token should immediately be traded in for a valid fb_sig_session_key
            if ((request.getParameter("auth_token")!=null && !request.getParameter("auth_token").trim().equals(""))){
                logger.debug("auth_token found in request... will try to convert to session_key");
                FacebookRestClient facebookRestClient = new FacebookRestClient(userSession.getApp().getFacebookapikey(), userSession.getApp().getFacebookapisecret(), userSession.getFacebooksessionkey());
                String facebooksessionkey = facebookRestClient.auth_getSession(request.getParameter("auth_token").trim());
                userSession.setFacebooksessionkey(facebooksessionkey);
            } else {
                //No auth_token was sent (it's only sent for new apps and new logins, etc) so look to session_key
                logger.debug("no auth_token found in request, looking for fb_sig_session_key");
                if ((request.getParameter("fb_sig_session_key")!=null && !request.getParameter("fb_sig_session_key").trim().equals(""))){
                    logger.debug("found a fb_sig_session_key in request");
                    userSession.setFacebooksessionkey(request.getParameter("fb_sig_session_key").trim());
                } else {
                    logger.debug("no fb_sig_session_key found in request... redirecting to main app page");
                    HttpServletResponse httpresponse = (HttpServletResponse)response;
                    httpresponse.sendRedirect("http://apps.facebook.com/"+userSession.getApp().getFacebookappname()+"/");
                    return;
                }
            }



            //Pull userSession from cache
            boolean foundSessionInCache = false;
            Object obj = CacheFactory.getCacheProvider().get(userSession.getFacebooksessionkey(), "userSession");
            if (obj!=null && (obj instanceof UserSession)){
                logger.debug("found a userSession in the cache");
                userSession = (UserSession)obj;
                foundSessionInCache = true;
            } else {
                logger.debug("no userSession in cache");
            }

            //Reset the app add flag
            userSession.setIsnewappforthisuser(false);

            //In general try not to handle request vars below this line
            //I only want to run this stuff when I see a new Facebook session key...
            if (!foundSessionInCache) {
                logger.debug("running heavy Facebook user setup with api calls due to new facebooksessionkey");

                //Go get some details on this facebookuser
                FacebookRestClient facebookRestClient = null;
                try {
                    facebookRestClient = new FacebookRestClient(userSession.getApp().getFacebookapikey(), userSession.getApp().getFacebookapisecret(), userSession.getFacebooksessionkey());
                    userSession.setFacebookUser(new FacebookUser(facebookRestClient.users_getLoggedInUser(), userSession.getFacebooksessionkey(), userSession.getApp().getFacebookapikey(), userSession.getApp().getFacebookapisecret()));
                } catch (FacebookException fex) {
                    logger.error("Facebook Error fex", fex);
                }

                //Make sure we have a user
                if (userSession.getFacebookUser()!=null && !userSession.getFacebookUser().getUid().trim().equals("")){
                    //See if we have this facebook user as a dNeero user
                    User user = FindUserFromFacebookUid.find(userSession.getFacebookUser().getUid());
                    if (user!=null && user.getUserid()>0) {
                        //Is already a user
                        logger.debug("Found local user account");
                        userSession.setUser(user);
                        logger.debug("Fbdblog Facebook Login: " + user.getFirstname() + " " + user.getLastname() + " (Facebookuid=" + user.getFacebookuid() + ")");
                        //Notify via XMPP
                        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "Facebook Login: " + user.getFirstname() + " " + user.getLastname());
                        xmpp.send();
                    } else {
                        //Create a new User for this facebookuid
                        logger.debug("Creating a new user account for this facebookuid");
                        user = new User();
                        user.setCreatedate(new Date());
                        user.setFacebookuid(userSession.getFacebookUser().getUid());
                        user.setFirstname(userSession.getFacebookUser().getFirst_name());
                        user.setLastname(userSession.getFacebookUser().getLast_name());
                        user.setIsenabled(true);
                        user.setEmail("");
                        user.setPassword("");
                        try {user.save();} catch (Exception ex) {logger.error(ex);}
                        //Store in session
                        userSession.setUser(user);
                        //Notify via XMPP
                        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "New Facebook user '" + userSession.getFacebookUser().getFirst_name() + " " + userSession.getFacebookUser().getLast_name() + "'");
                        xmpp.send();
                    }
                } else {
                    logger.debug("userSession.getFacebookUser() is empty after calling facebook api");
                    //@todo how to handle facebook call to populate user is empty?
                }
            } else {
                logger.debug("didn't find a new facebooksessionkey so didn't make api call to load facebook user");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex);
        }

        //Track app adds
        if (request.getParameter("postaddappname")!=null) {
            //Set var in session so that I can call a banner add thing
            if (userSession.getUser()!=null && userSession.getUser().getUserid()>0 && userSession.getApp()!=null && userSession.getApp().getAppid()>0) {
                //Is this a new app for this user?
                List<Userappactivity> uaas = HibernateUtil.getSession().createCriteria(Userappactivity.class)
                                               .add(Restrictions.eq("userid", userSession.getUser().getUserid()))
                                               .add(Restrictions.eq("appid", userSession.getApp().getAppid()))
                                               .setCacheable(false)
                                               .list();
                if (uaas!=null && uaas.size()>0){
                    userSession.setIsnewappforthisuser(false);
                } else {
                    userSession.setIsnewappforthisuser(true);
                }
                //Now record the app add
                Calendar cal = Calendar.getInstance();
                Userappactivity userappactivity=new Userappactivity();
                userappactivity.setAppid(userSession.getApp().getAppid());
                userappactivity.setUserid(userSession.getUser().getUserid());
                userappactivity.setDate(new Date());
                userappactivity.setYear(cal.get(Calendar.YEAR));
                userappactivity.setMonth(cal.get(Calendar.MONTH)+1);
                userappactivity.setIsinstall(true);
                userappactivity.setIsuninstall(false);
                try {userappactivity.save();} catch (Exception ex) {logger.error(ex);}
            }


        }

        //Save UserSession in Cache
        CacheFactory.getCacheProvider().put(userSession.getFacebooksessionkey(), "userSession", userSession);

        //Save in session
        request.getSession().setAttribute("userSession", userSession);

    }

}
