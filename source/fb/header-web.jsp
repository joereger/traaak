<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.google.code.facebookapi.FacebookXmlRestClient" %>
<%@ page import="com.google.code.facebookapi.FacebookException" %>
<%@ page import="com.fbdblog.xmpp.SendXMPPMessage" %>
<%@ page import="com.fbdblog.facebook.FacebookUser" %>
<%@ page import="com.fbdblog.htmlui.UserSession" %>
<%@ page import="com.fbdblog.facebook.FindUserFromFacebookUid" %>
<%@ page import="com.fbdblog.facebook.FindApp" %>
<%@ page import="com.fbdblog.session.UrlSplitter" %>
<%@ page import="com.fbdblog.impressions.ImpressionActivityObject" %>
<%@ page import="com.fbdblog.scheduledjobs.ImpressionCache" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="com.fbdblog.systemprops.BaseUrl" %>
<%@ page import="com.fbdblog.htmlui.Pagez" %>



<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>

