package com.fbdblog.session;

import org.apache.log4j.Logger;

import javax.servlet.ServletResponse;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;

import com.fbdblog.facebook.FindAppFromApiKey;
import com.fbdblog.facebook.FacebookUser;
import com.fbdblog.facebook.FindUserFromFacebookUid;
import com.fbdblog.xmpp.SendXMPPMessage;
import com.fbdblog.dao.User;
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


        //Facebook login stuff
        try {
            //Determine whether this is a facebook request
            //Facebook sends this any time it puts the app inside an iFrame
            if (request.getParameter("fb_sig_api_key") != null && !request.getParameter("fb_sig_api_key").equals("")) {

                //Figure out which app we're dealing with here
                userSession.setApp(FindAppFromApiKey.find(request.getParameter("fb_sig_api_key")));
                if (userSession.getApp() != null && userSession.getApp().getAppid() > 0) {

                    //I only want to run this stuff when I see a new Facebook session key...
                    if ( (request.getParameter("fb_sig_session_key")!=null && !request.getParameter("fb_sig_session_key").trim().equals("") && !request.getParameter("fb_sig_session_key").trim().equals(userSession.getFacebooksessionkey().trim())) || (request.getParameter("fb_sig")!=null && !request.getParameter("fb_sig").trim().equals("") && !request.getParameter("fb_sig").trim().equals(userSession.getFacebooksessionkey().trim()))) {
                        logger.debug("Running heavy Facebook user setup stuff because I'm seeing a new fb_sig_session_key");

                        //Set the sessionkey to the local session
                        userSession.setFacebooksessionkey(request.getParameter("fb_sig_session_key").trim());

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
                            if (user != null && user.getUserid() > 0) {
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
                                try {
                                    user.save();
                                } catch (Exception ex) {
                                    logger.error(ex);
                                }
                                //Store in session
                                userSession.setUser(user);
                                //Notify via XMPP
                                SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "New Facebook user '" + userSession.getFacebookUser().getFirst_name() + " " + userSession.getFacebookUser().getLast_name() + "'");
                                xmpp.send();
                            }
                        } else {
                            logger.debug("userSession.getFacebookUser() is empty");
                        }
                    } else {
                        logger.debug("no request.getParameter(\"fb_sig_session_key\") found");
                    }
                } else {
                    logger.debug("app not found for api_key=" + request.getParameter("fb_sig_api_key"));
                }
            } else {
                logger.debug("no request.getParameter(\"fb_sig_api_key\") found");
            }
        } catch (Exception ex) {
            logger.error(ex);
        }

        //Save in session
        request.getSession().setAttribute("userSession", userSession);
    }

}
