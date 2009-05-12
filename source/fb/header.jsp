<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.facebook.api.FacebookRestClient" %>
<%@ page import="com.fbdblog.xmpp.SendXMPPMessage" %>
<%@ page import="com.fbdblog.facebook.FacebookUser" %>
<%@ page import="com.fbdblog.session.UserSession" %>
<%@ page import="com.facebook.api.FacebookException" %>
<%@ page import="com.fbdblog.facebook.FindUserFromFacebookUid" %>
<%@ page import="com.fbdblog.facebook.FindApp" %>
<%@ page import="com.fbdblog.session.UrlSplitter" %>
<%@ page import="com.fbdblog.impressions.ImpressionActivityObject" %>
<%@ page import="com.fbdblog.scheduledjobs.ImpressionCache" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="com.fbdblog.systemprops.BaseUrl" %>
<%
    //Logger
    Logger logger=Logger.getLogger(this.getClass());

    //If user hasn't added app yet redir to the app add page
    if (Pagez.getUserSession().getFacebookUser() == null || !Pagez.getUserSession().getFacebookUser().getHas_added_app()) {
        logger.debug("Redirecting this user to facebook add app page");
        //If we have a known app
        if (Pagez.getUserSession().getApp() != null) {
            if (Pagez.getUserSession().getFacebookUser() != null && Pagez.getUserSession().getFacebookUser().getUid().length()>0) {
                //Notify via XMPP
                SendXMPPMessage xmpp=new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "Redirecting " + Pagez.getUserSession().getFacebookUser().getFirst_name() + " " + Pagez.getUserSession().getFacebookUser().getLast_name() + " to add app page. " + "(facebook.uid=" + Pagez.getUserSession().getFacebookUser().getUid() + ")");
                xmpp.send();
            } else {
                SendXMPPMessage xmpp=new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "Redirecting an unknown facebook user to add app page.");
                xmpp.send();
            }
            //Get the current url
            UrlSplitter urlSplitter=new UrlSplitter(request);
            String next="http://apps.facebook.com/" + Pagez.getUserSession().getApp().getFacebookappname() + "/?" + urlSplitter.getQuerystring();
            next=URLEncoder.encode(next, "UTF-8");
            //Redirect to app add page with fbml
            logger.debug("<fb:redirect url=\"http://www.facebook.com/add.php?api_key=" + Pagez.getUserSession().getApp().getFacebookapikey() + "&next=" + next + "\" />");
            out.print("<fb:redirect url=\"http://www.facebook.com/add.php?api_key=" + Pagez.getUserSession().getApp().getFacebookapikey() + "&next=" + next + "\" />");
            return;
        } else {
            //@todo what to do with unknown app?
            logger.debug("unknown app");
        }
    }
    logger.debug("still executing so assuming we have an facebook user who's added the app");

    //Record Impression
    if (Pagez.getUserSession() != null && Pagez.getUserSession().getUser() != null && Pagez.getUserSession().getApp() != null) {
        if (Pagez.getUserSession().getUser().getUserid()>0 && Pagez.getUserSession().getApp().getAppid()>0) {
            Calendar cal=Calendar.getInstance();
            ImpressionActivityObject iao=new ImpressionActivityObject();
            iao.setAppid(Pagez.getUserSession().getApp().getAppid());
            iao.setYear(cal.get(Calendar.YEAR));
            iao.setMonth(cal.get(Calendar.MONTH) + 1);
            iao.setDay(cal.get(Calendar.DAY_OF_MONTH));
            iao.setPage(request.getServletPath());
            ImpressionCache.addIao(iao);
        }
    }

%>

<style type="text/css">
.questionfont{font-weight: bold; background: #ffffcc;}
</style>

<%
if (!Pagez.getUserSession().getApp().getAdglobalheader().equals("")){
    %><%=Pagez.getUserSession().getApp().getAdglobalheader()%><%
    %><br/><%
}
%>
<table cellpadding="2" cellspacing="0" border="0" width="100%">
    <tr>
        <td valign="top" width="64">
            <img src="<%=com.fbdblog.systemprops.BaseUrl.get(false)%>images/logo-64.png" alt="" width="64" height="64"/>
        </td>
        <td valign="top">
            <font style="font-size: 24px; font-weight: bold;"><%=Pagez.getUserSession().getApp().getTitle()%></font>
        </td>
        <td valign="top">
            <div align="right">
            <font style="font-size: 18px; font-weight: bold;"><%=Pagez.getUserSession().getFacebookUser().getFirst_name()%> <%=Pagez.getUserSession().getFacebookUser().getLast_name()%></font>
            </div>
        </td>
        <td valign="top" width="50">
            <img src="<%=Pagez.getUserSession().getFacebookUser().getPic_square()%>" alt="<%=Pagez.getUserSession().getFacebookUser().getFirst_name()%>" align="right">
        </td>
    </tr>
    <!--
    <tr>
        <td valign="top" colspan="3" align="right">
            <div align="right">
            <form action="index.jsp"><input type="hidden" name="action" value="compare"><fb:friend-selector uid="<%=Pagez.getUserSession().getFacebookUser().getUid()%>" name="uid" idname="friend_sel" /><input type="submit" value="Compare" style="font-size: 10px;"></form>
            </div>
        </td>
    </tr>
    -->
</table>