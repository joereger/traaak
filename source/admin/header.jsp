<%@ page import="com.fbdblog.session.UserSession" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.fbdblog.systemprops.BaseUrl" %>
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

<style type="text/css">
    body, td {font-family: sans-serif;}
    .pagetitle {font-family: impact; font-size: 28px; color: #666666;}
</style>

<body>

<table cellpadding="3" cellspacing="0" border="0" width="100%">
    <tr>
        <td valign="top" align="left" width="64">
            <img src="<%=BaseUrl.get(false)%>images/logo-64.png" alt="" width="64" height="64"/>
        </td>
        <td valign="top" align="left">
            <font style="font-family: impact; font-size: 42px; color: #cccccc;">SysAdmin</font>       
        </td>
        <td valign="top" align="right">
            <font style="font-family: impact; font-size: 18px; color: #cccccc;">Logged in as: <%=userSession.getUser().getFirstname()%> <%=userSession.getUser().getLastname()%></font>
        </td>
    </tr>
</table>

<table cellpadding="10" cellspacing="0" border="0" width="100%">
    <tr>
        <td valign="top" width="150">
            <!--<a href='index.jsp'>Index</a><br/>-->
            <a href='apps.jsp'>Apps</a><br/>
            <a href='users.jsp'>Users</a><br/>
            <a href='sysprops.jsp'>SysProps</a><br/>
            <a href='instanceprops.jsp'>InstanceProps</a><br/>
            <a href='financialmodel.jsp'>Financial Model</a><br/>
            <a href='iaocache.jsp'>Iao Cache</a><br/>
            <a href='impressions.jsp'>Impressions</a><br/>
        </td>
        <td valign="top">

