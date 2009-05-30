<%@ page import="com.fbdblog.qtype.util.AppTemplateProcessor" %>
<%@ page import="com.fbdblog.qtype.util.AppPostParser" %>
<%@ page import="com.fbdblog.qtype.util.SavePost" %>
<%@ page import="com.fbdblog.qtype.def.ComponentException" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="com.fbdblog.dao.Chart" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.fbdblog.util.Str" %>
<%@ page import="com.fbdblog.util.Num" %>
<%@ page import="com.fbdblog.systemprops.BaseUrl" %>
<%@ page import="com.fbdblog.chart.ChartSecurityKey" %>

<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Embed";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="header.jsp" %>



<%String selectedTab="embed";%>
<%@ include file="tabs.jsp" %>

Embedding your charts into a blog or website is simple:

<ol>
    <li>Go to your <a href="/app/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=charts">Charts Page</a></li>
    <li>Choose a chart</li>
    <li>Copy and paste the embed code beneath the chart into your blog or website... you're done!</li>
</ol>

<br/><br/>

<%@ include file="footer.jsp" %>