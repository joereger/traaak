<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.facebook.api.FacebookRestClient" %>
<%@ page import="com.fbdblog.xmpp.SendXMPPMessage" %>
<%@ page import="com.fbdblog.facebook.FacebookUser" %>
<%@ page import="com.fbdblog.session.UserSession" %>
<%@ page import="com.facebook.api.FacebookException" %>
<%@ page import="com.fbdblog.dao.User" %>
<%@ page import="com.fbdblog.facebook.FindUserFromFacebookUid" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="com.fbdblog.facebook.FindAppFromApiKey" %>
<%@ page import="com.fbdblog.session.UrlSplitter" %>
<%
    Logger logger = Logger.getLogger(this.getClass());
    UserSession userSession = null;
    try {
        //Get UserSession from app server session
        Object ustmp = request.getSession().getAttribute("userSession");
        if (ustmp != null) {
            userSession = (UserSession) ustmp;
        } else {
            userSession = new UserSession();
        }

        //Debug
        UrlSplitter urlSplitter = new UrlSplitter(request);

        //Determine whether this is a facebook request
        //Facebook sends this any time it puts the app inside an iFrame
        if (request.getParameter("fb_sig_api_key") != null && !request.getParameter("fb_sig_api_key").equals("")) {

            //Figure out which app we're dealing with here
            userSession.setApp(FindAppFromApiKey.find(request.getParameter("fb_sig_api_key")));
            if (userSession.getApp() != null && userSession.getApp().getAppid() > 0) {

                //I only want to run the auth stuff when I see a new Facebook session key...
                //i.e. one that's not null, not empty and is different than the one that's in userSession.
                //Facebook only sends the fb_sig_session_key after the user has added the app
                if (request.getParameter("fb_sig_session_key") != null && !request.getParameter("fb_sig_session_key").trim().equals("") && !request.getParameter("fb_sig_session_key").trim().equals(userSession.getFacebooksessionkey().trim())) {

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

                    //Make sure we have a facebookuser
                    if (userSession.getFacebookUser()!=null && !userSession.getFacebookUser().getUid().trim().equals("")){
                        //If user hasn't added app yet redir to the app add page
                        if (!userSession.getFacebookUser().getHas_added_app()) {
                            logger.debug("redirecting this user to facebook add app page");
                            //Notify via XMPP
                            SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "Redirecting " + userSession.getFacebookUser().getFirst_name() + " " + userSession.getFacebookUser().getLast_name() + " to add app page. " + "(facebook.uid=" + userSession.getFacebookUser().getUid() + ")");
                            xmpp.send();
                            //@todo how to redirect to app add page with fbml?
                        }
                        logger.debug("User has added app... we have facebookSessionKey=" + userSession.getFacebooksessionkey());

                        //See if we have this facebook user as a dNeero user
                        User user = FindUserFromFacebookUid.find(userSession.getFacebookUser().getUid());
                        if (user != null && user.getUserid() > 0) {
                            //Is already a dNeero user
                            userSession.setUser(user);
                            userSession.setIsloggedin(true);
                            logger.debug("Fbdblog Facebook Login: " + user.getFirstname() + " " + user.getLastname() + " (Facebookuid=" + user.getFacebookuid() + ")");

                            //Notify via XMPP
                            SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "Facebook Login: " + user.getFirstname() + " " + user.getLastname());
                            xmpp.send();
                        } else {
                            //Create a new User for this facebookuid
                            user = new User();
                            user.setCreatedate(new Date());
                            user.setFacebookuid(userSession.getFacebookUser().getUid());
                            user.setFirstname(userSession.getFacebookUser().getFirst_name());
                            user.setLastname(userSession.getFacebookUser().getLast_name());
                            user.setIsenabled(true);
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
                        logger.debug("userSession.getFacebookUser() is null");
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
        logger.error("Facebook Error ex", ex);
    }

//    //If is coming from facebook but hasn't added app, make them add it
//    if (userSession.getIsfacebookappadded()) {
//        //UrlSplitter urlSplitter = new UrlSplitter(Jsf.getHttpServletRequest());
//        //If the showsurvey var isn't set in the incoming request, make them add it... this is currently the only exception
//        if (request.getParameter("stoplooping") == null || !request.getParameter("stoplooping").equals("1")) {
//
//            //Need to record impressions if we're gonna send them away
//            if (request.getParameter("action") != null && request.getParameter("action").indexOf("showsurvey") > -1) {
//                RecordImpression.record(Jsf.getHttpServletRequest());
//            }
//            logger.debug("redirecting to facebook add app page");
//            try {
//                response.sendRedirect("/facebooklandingpage.jsf?stoplooping=1&action=" + request.getParameter("action"));
//                return;
//            } catch (Exception ex) {
//                logger.error(ex);
//            }
//            //PublicFacebookLandingPage pfblp = new PublicFacebookLandingPage();
//            //try{Jsf.redirectResponse(pfblp.getAddurl());return;}catch(Exception ex){logger.error(ex);}
//        }
//
//    }

%>