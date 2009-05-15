<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.facebook.api.FacebookRestClient" %>
<%@ page import="com.fbdblog.xmpp.SendXMPPMessage" %>
<%@ page import="com.fbdblog.facebook.FacebookUser" %>
<%@ page import="com.fbdblog.htmlui.UserSession" %>
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

    //Include proper header
    if (Pagez.getUserSession().getIsfacebook()){
        %>
        <%@ include file="header-fb.jsp" %>
        <%
    } else {
        %>
        <%@ include file="header-web.jsp" %>
        <%
    }
%>
