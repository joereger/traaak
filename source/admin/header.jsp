<%@ page import="com.fbdblog.session.UserSession" %>
<%@ page import="org.apache.log4j.Logger" %>
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

    //Make sure user's logged in
    if (!userSession.getIsloggedin() || !userSession.getIssysadmin()) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<table cellpadding="10" cellspacing="0" border="0">
    <tr>
        <td valign="top" colspan="2">
            Logged in as: <%=userSession.getUser().getFirstname()%> <%=userSession.getUser().getLastname()%>    
        </td>
    </tr>
    <tr>
        <td valign="top" width="150">
            <a href='apps.jsp'>Apps</a><br/>
            <a href='users.jsp'>Users</a><br/>
            <a href='sysprops.jsp'>SysProps</a><br/>
            <a href='instanceprops.jsp'>InstanceProps</a><br/>
            <a href='financialmodel.jsp'>Financial Model</a><br/>
            <a href='iaocache.jsp'>Iao Cache</a><br/>
            <a href='impressions.jsp'>Impressions</a><br/>
        </td>
        <td valign="top">

