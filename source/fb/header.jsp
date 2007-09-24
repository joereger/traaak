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


<table cellpadding="2" cellspacing="0" border="0" width="100%">
    <tr>
        <td valign="top">
            <font style="font-size: 24px; font-weight: bold;"><%=userSession.getApp().getTitle()%></font>
        </td>
        <td valign="top">
            <div align="right">
            <font style="font-size: 18px;"><%=userSession.getFacebookUser().getFirst_name()%> <%=userSession.getFacebookUser().getLast_name()%></font>
            </div>
        </td>
        <td valign="top" width="50">
            <img src="<%=userSession.getFacebookUser().getPic_square()%>" alt="<%=userSession.getFacebookUser().getFirst_name()%>" align="right">
        </td>
    </tr>
    <tr>
        <td valign="top" colspan="3" align="right">
            <div align="right">
            <form action="index.jsp"><input type="hidden" name="action" value="compare"><fb:friend-selector uid="<%=userSession.getFacebookUser().getUid()%>" name="uid" idname="friend_sel" /><input type="submit" value="Compare" style="font-size: 10px;"></form>
            </div>
        </td>
    </tr>
</table>