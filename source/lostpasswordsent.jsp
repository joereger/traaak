<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.fbdblog.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Password Sent";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>


    <font class="normalfont">Please check your email... we have sent a password reset link.</font>


<%@ include file="/template/footer.jsp" %>
