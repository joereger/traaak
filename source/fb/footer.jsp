<%@ page import="com.fbdblog.systemprops.InstanceProperties" %>
<%@ page import="com.fbdblog.htmlui.Pagez" %>

<%
    if (Pagez.getUserSession().getIsfacebook()){
        %>
        <%@ include file="footer-fb.jsp" %>
        <%
    } else {
        %>
        <%@ include file="footer-web.jsp" %>
        <%
    }
%>