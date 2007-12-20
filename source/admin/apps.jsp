<%@ page import="java.util.List" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="com.fbdblog.util.Str" %>
<%@ page import="com.fbdblog.util.Num" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ include file="header.jsp" %>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("togglecrosspromote")) {
        if (request.getParameter("appid")!=null && Num.isinteger(request.getParameter("appid"))) {
            App app = App.get(Integer.parseInt(request.getParameter("appid")));
            if (app!=null && app.getAppid()>0){
                app.setCrosspromote(!app.getCrosspromote());
                try {app.save();} catch (Exception ex) {logger.error("", ex);}
            }
        }
    }
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("toggleisdefaultprivate")) {
        if (request.getParameter("appid")!=null && Num.isinteger(request.getParameter("appid"))) {
            App app = App.get(Integer.parseInt(request.getParameter("appid")));
            if (app!=null && app.getAppid()>0){
                app.setIsdefaultprivate(!app.getIsdefaultprivate());
                try {app.save();} catch (Exception ex) {logger.error("", ex);}
            }
        }
    }
%>


<font class="pagetitle">Apps</font>
<br/><br/>

<table cellpadding="3" cellspacing="0" border="0">
<tr>
<td valign="top"></td>
<td valign="top">Installs</td>
<td valign="top">ImpPerUser</td>
<td valign="top">TotalImp</td>
<td valign="top">Promote</td>
<td valign="top">Private</td>
<td valign="top">InfSessKey</td>
</tr>
<%
    double totalinstalls=0;
    double totaltotalimpressions=0;
    double totalapps=0;
    int totalusers=((Long) HibernateUtil.getSession().createQuery("select count(*) from User").uniqueResult()).intValue();
    List<App> apps = HibernateUtil.getSession().createCriteria(App.class)
            .addOrder(Order.asc("title"))
            .setCacheable(true)
            .list();
    for (Iterator<App> iterator=apps.iterator(); iterator.hasNext();) {
        App app=iterator.next();
        totalapps=totalapps + 1;

        Calendar now=Calendar.getInstance();
        int currentyear=now.get(Calendar.YEAR);
        int currentmonth=now.get(Calendar.MONTH) + 1;

        Double avgimpressionsperuser=0.0;
        Object obj1=HibernateUtil.getSession().createQuery("select avg(impressions) from Impression where appid='" + app.getAppid() + "' and year='" + currentyear + "' and month='" + currentmonth + "'").uniqueResult();
        if (obj1 != null) {
            avgimpressionsperuser=((Double) obj1);
        }

        Double totalimpressions=0.0;
        Object obj2=HibernateUtil.getSession().createQuery("select sum(impressions) from Impression where appid='" + app.getAppid() + "' and year='" + currentyear + "' and month='" + currentmonth + "'").uniqueResult();
        if (obj2 != null) {
            totalimpressions=((Long) obj2).doubleValue();
        }
        totaltotalimpressions=totaltotalimpressions + totalimpressions;

        Double installs=0.0;
        Object obj3=HibernateUtil.getSession().createQuery("select count(*) from Userappstatus where appid='" + app.getAppid() + "' and isinstalled=true").uniqueResult();
        if (obj3 != null) {
            installs=((Long) obj3).doubleValue();
        }
        totalinstalls=totalinstalls + installs;

%>
        <tr>
            <td valign="top"><a href="appdetail.jsp?appid=<%=app.getAppid()%>"><%=app.getTitle()%></a></td>
            <td valign="top"><%=Str.formatWithXDecimalPlaces(installs, 0)%></td>
            <td valign="top"><%=Str.formatWithXDecimalPlaces(avgimpressionsperuser, 2)%></td>
            <td valign="top"><a href='impressions.jsp?appid=<%=app.getAppid()%>'><%=Str.formatWithXDecimalPlaces(totalimpressions, 0)%></a></td>
            <td valign="top" align="center">
            <%
            if (app.getCrosspromote()){
                %><a href='apps.jsp?action=togglecrosspromote&appid=<%=app.getAppid()%>'><img src="/images/wireless-green-16.png" alt="Promote It" width="16" height="16" border="0"/></a><%
            } else {
                %><a href='apps.jsp?action=togglecrosspromote&appid=<%=app.getAppid()%>'><img src="/images/misc-16.png" alt="Don't Promote It" width="16" height="16" border="0"/></a><%
            }
            %>
            </td>
            <td valign="top" align="center">
            <%
            if (app.getIsdefaultprivate()){
                %><a href='apps.jsp?action=toggleisdefaultprivate&appid=<%=app.getAppid()%>'><img src="/images/lock-16.png" alt="Private" width="16" height="16" border="0"/></a><%
            } else {
                %><a href='apps.jsp?action=toggleisdefaultprivate&appid=<%=app.getAppid()%>'><img src="/images/misc-16.png" alt="Public" width="16" height="16" border="0"/></a><%
            }
            %>
            <td valign="top" align="center">
            <%
            if (app.getFacebookinfinitesessionkey()==null || app.getFacebookinfinitesessionkey().equals("")){
                %><a href='appdetail.jsp?appid=<%=app.getAppid()%>'><img src="/images/alert-16.png" alt="No Key" width="16" height="16" border="0"/></a><%
            } else {
                %><a href='appdetail.jsp?appid=<%=app.getAppid()%>'><img src="/images/misc-16.png" alt="Key OK" width="16" height="16" border="0"/></a><%
            }
            %>
            </td>
        </tr>
        <%
    }
%>
<%
double totalavgimpressionsperuser = 0;
if (totalinstalls>0){
    totalavgimpressionsperuser = totaltotalimpressions/totalinstalls;
}
%>
<%
double appsperuser = 0;
if (totalusers>0){
    appsperuser = totalinstalls/totalusers;
}
%>
<tr>
    <td valign="top"><b>Total</b></td>
    <td valign="top"><b><%=Str.formatWithXDecimalPlaces(totalinstalls, 0)%></b><br/><font style="font-size: 10px;"><%=Str.formatWithXDecimalPlaces(appsperuser, 2)%><br/>apps/user</font></td>
    <td valign="top"><b><%=Str.formatWithXDecimalPlaces(totalavgimpressionsperuser, 2)%></b></td>
    <td valign="top"><b><%=Str.formatWithXDecimalPlaces(totaltotalimpressions, 0)%></b></td>
    <td valign="top"></td>
</tr>
</table>
<br/><br/>
<a href='appdetail.jsp?action=newapp'>+ Add App</a>
<br/>


<%@ include file="footer.jsp" %>