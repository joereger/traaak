<%@ page import="com.fbdblog.htmlui.Authorization" %>
<%@ page import="org.apache.log4j.Logger" %>

<%
    boolean isauthorised=Authorization.check(acl);
    if (!isauthorised){
        if (Pagez.getUserSession()!=null && Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getIsloggedin()) {
            Pagez.sendRedirect("/notauthorized.jsp");
            return;
        } else {
            Pagez.sendRedirect("/login.jsp");
            return;
        }
    }
%>