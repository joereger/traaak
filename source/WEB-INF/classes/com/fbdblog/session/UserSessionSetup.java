package com.fbdblog.session;

import org.apache.log4j.Logger;

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
import com.fbdblog.util.Num;
import com.facebook.api.FacebookRestClient;
import com.facebook.api.FacebookException;

import java.util.Date;

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

        //Facebook
        try {
            //Need an api key to determine which app this is
            if (request.getParameter("fb_sig_api_key") != null && !request.getParameter("fb_sig_api_key").equals("")) {
                userSession.setApp(FindAppFromApiKey.find(request.getParameter("fb_sig_api_key")));
            }
            if (userSession.getApp()==null || userSession.getApp().getAppid()<=0) {
                logger.debug("no api_key found so looking to request.getParameter(\"postaddappid\")="+request.getParameter("postaddappid"));
                if (request.getParameter("postaddappid")!=null && Num.isinteger(request.getParameter("postaddappid"))) {
                    App app = App.get(Integer.parseInt(request.getParameter("postaddappid")));
                    userSession.setApp(app);
                }
            }
            if (userSession.getApp()==null || userSession.getApp().getAppid()<=0) {
                logger.debug("no valid app found");
                return;
                //@todo how to handle unknown api_key?  list all apps with links to an add page?
            }


            //Need a session key
            String initialFacebooksessionkey = userSession.getFacebooksessionkey();
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

            //In general try not to handle request vars below this line
            //I only want to run this stuff when I see a new Facebook session key...
            if (!userSession.getFacebooksessionkey().equals(initialFacebooksessionkey)) {
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

        //Save in session
        request.getSession().setAttribute("userSession", userSession);
    }

}
