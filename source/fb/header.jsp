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
    //Logger
    Logger logger = Logger.getLogger(this.getClass());

    //Make sure we have a userSession to work with
    UserSession userSession = null;
    Object ustmp = request.getSession().getAttribute("userSession");
    if (ustmp != null) {
        userSession = (UserSession) ustmp;
    } else {
        userSession = new UserSession();
        request.getSession().setAttribute("userSession", userSession);
    }

    //If user hasn't added app yet redir to the app add page
    if (userSession.getFacebookUser()==null || !userSession.getFacebookUser().getHas_added_app()) {
        logger.debug("Redirecting this user to facebook add app page");
        //If we have a known app
        if (userSession.getApp()!=null){
            if (userSession.getFacebookUser()!=null && userSession.getFacebookUser().getUid().length()>0){
                //Notify via XMPP
                SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "Redirecting " + userSession.getFacebookUser().getFirst_name() + " " + userSession.getFacebookUser().getLast_name() + " to add app page. " + "(facebook.uid=" + userSession.getFacebookUser().getUid() + ")");
                xmpp.send();
            } else {
                SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "Redirecting an unknown facebook user to add app page.");
                xmpp.send();
            }
            //Redirect to app add page with fbml
            out.print("<fb:redirect url=\"http://www.facebook.com/add.php?api_key="+userSession.getApp().getFacebookapikey()+"\" />");
            return;
        } else {
            //@todo what to do with unknown app?
        }
    }

%>