<%@ page import="com.fbdblog.htmlui.Pagez" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
<body LEFTMARGIN="0" TOPMARGIN="0" MARGINWIDTH="0" MARGINHEIGHT="0"><center>

    <%@ include file="message.jsp" %>
    <%@ include file="nav.jsp" %>

<table width="925" cellspacing="0" border="0" cellpadding="5">
<tr>
    <td valign="top">
        <a href="/">
            <img src="/images/logo-64.png" alt="" width="64" height="64" border="0">
            <font style="font-family: impact; font-size: 42px; color: #cccccc;">Traaak</font>
        </a>
    </td>

    <td valign="top">
        
    </td>
</tr>
<tr>
    <td colspan="2">

        <div style="text-align: left;">

