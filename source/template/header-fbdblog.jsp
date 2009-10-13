<%@ page import="com.fbdblog.htmlui.Pagez" %>
<%@ page import="com.fbdblog.facebook.FindApp" %>
<%@ page import="com.fbdblog.systemprops.SystemProperty" %>
<%@ page import="com.google.code.facebookapi.FacebookXmlRestClient" %>
<%@ page import="com.google.code.facebookapi.FacebookException" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" xmlns:fb="http://www.facebook.com/2008/fbml">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;CHARSET=iso-8859-1"/>
    <%
        String finalTitle = "Traaak Stuff";
        if (pagetitle!=null && !pagetitle.equals("")){
            finalTitle = pagetitle;
        }
    %>
    <title><%=finalTitle%></title>
    <link rel="stylesheet" type="text/css" href="/css/basic.css"/>
    <link rel="stylesheet" type="text/css" href="/css/Fbdblog.css"/>
    <link rel="stylesheet" type="text/css" href="/css/topbarGettyone/topbarGettyone.css"/>
    <link rel="stylesheet" type="text/css" href="/css/digg_navbar/style.css"/>
    <meta name="description" content="<%=finalTitle%>"/>
    <meta name="keywords" content="<%=finalTitle%>"/>
    <script type="text/JavaScript" src="/js/curvycorners/curvycorners.js"></script>

</head>
<body LEFTMARGIN="0" TOPMARGIN="0" MARGINWIDTH="0" MARGINHEIGHT="0">
<script src="http://static.ak.connect.facebook.com/js/api_lib/v0.4/FeatureLoader.js.php/en_US" type="text/javascript"></script>
<script type="text/javascript">FB.init("<%=SystemProperty.getProp(SystemProperty.PROP_DEFAULTAPIKEY)%>");</script>
<center>

    <%@ include file="message.jsp" %>
    <%@ include file="nav.jsp" %>

<table width="900" cellspacing="0" border="0" cellpadding="5">
<tr>
    <td valign="top">
        <a href="/">
            <img src="/images/clear.gif" alt="" width="10" height="100" border="0">
        </a>
    </td>
    <td valign="top" width="195">
        <img src="/images/clear.gif" alt="" width="1" height="30"><br/>
        <a href="/">
            <img src="/images/v2-logo.gif" alt="" width="195" height="67" border="0">
        </a>
    </td>
    <td valign="top" nowrap>
        <div style="float:right;">
        <img src="/images/clear.gif" alt="" width="1" height="30"><br/>
        <table cellspacing="0" border="0" cellpadding="0"><tr>
        <td valign="top"><img src="/images/v2-nav-leftcap.gif" alt=""></td>
        <%if (!Pagez.getUserSession().getIsloggedin()){%>
            <td valign="top"><a href="/"><img src="/images/v2-nav-home.gif" alt="" border="0"></a></td>
            <td valign="top"><img src="/images/v2-nav-divider.gif" alt="" border="0"></td>
            <td valign="top"><a href="/registration.jsp"><img src="/images/v2-nav-signup.gif" alt="" border="0"></a></td>
            <td valign="top"><img src="/images/v2-nav-divider.gif" alt="" border="0"></td>
            <td valign="top"><a href="/login.jsp"><img src="/images/v2-nav-login.gif" alt="" border="0"></a></td>
        <%} else {%>
            <td valign="top"><a href="/"><img src="/images/v2-nav-home.gif" alt="" border="0"></a></td>
            <td valign="top"><img src="/images/v2-nav-divider.gif" alt="" border="0"></td>
            <td valign="top"><a href="/account/accountsettings.jsp"><img src="/images/v2-nav-accountsettings.gif" alt="" border="0"></a></td>
            <td valign="top"><img src="/images/v2-nav-divider.gif" alt="" border="0"></td>
            <td valign="top"><a href="/login.jsp?action=logout"><img src="/images/v2-nav-logout.gif" alt="" border="0"></a></td>
        <%}%>
        <td valign="top"><img src="/images/v2-nav-rightcap.gif" alt=""></td>
        </tr></table>
        </div>
    </td>
</tr>
<%--<tr>--%>
    <%--<td valign="top">--%>
        <%--<a href="/">--%>
            <%--<img src="/images/clear.gif" alt="" width="10" height="30" border="0">--%>
        <%--</a>--%>
    <%--</td>--%>
    <%--<td valign="top" colspan="2">--%>

    <%--</td>--%>
<%--</tr>--%>
</table>

<table width="900" cellspacing="0" border="0" cellpadding="5">
<tr>
    <td valign="top">

        <div style="text-align: left;">

