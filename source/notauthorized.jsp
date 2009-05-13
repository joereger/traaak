<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.fbdblog.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Not Authorized";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>


    <font class="normalfont">You are not authorized to view that resource.</font>


<%@ include file="/template/footer.jsp" %>