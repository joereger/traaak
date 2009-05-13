<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.fbdblog.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Email Activation Sent";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>

            Your email activation message has been sent.  Please check your email inbox.
            <br/><br/>
            Also, please note that all previous activation emails are now invalid... you must use the most recent one that we've sent.  This is for your security.  Thanks!


<%@ include file="/template/footer.jsp" %>
