<%@ page import="java.util.List" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="com.fbdblog.util.Str" %>
<%@ page import="com.fbdblog.util.Num" %>
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


<font class="pagetitle">Apps</font>
<br/><br/>

<table cellpadding="3" cellspacing="0" border="0">
<tr>
<td valign="top"></td>
<td valign="top">UniqueUsers</td>
<td valign="top">AvgImpPerUser</td>
<td valign="top">TotalImp</td>
<td valign="top">Crosspromote?</td>
</tr>
<%
    double totaluniqueusers = 0;
    double totaltotalimpressions = 0;

    List<App> apps=HibernateUtil.getSession().createQuery("from App").list();
    for (Iterator<App> iterator=apps.iterator(); iterator.hasNext();) {
        App app=iterator.next();

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
        totaltotalimpressions = totaltotalimpressions + totalimpressions;

        Double uniqueusers=0.0;
        Object obj3=HibernateUtil.getSession().createQuery("select count(*) from Impression where appid='" + app.getAppid() + "' and year='" + currentyear + "' and month='" + currentmonth + "'").uniqueResult();
        if (obj3 != null) {
            uniqueusers=((Long) obj3).doubleValue();
        }
        totaluniqueusers = totaluniqueusers + uniqueusers;



        %>
        <tr>
            <td valign="top"><a href="appdetail.jsp?appid=<%=app.getAppid()%>"><%=app.getTitle()%></a></td>
            <td valign="top"><%=Str.formatWithXDecimalPlaces(uniqueusers, 0)%></td>
            <td valign="top"><%=Str.formatWithXDecimalPlaces(avgimpressionsperuser, 2)%></td>
            <td valign="top"><a href='impressions.jsp?appid=<%=app.getAppid()%>'><%=Str.formatWithXDecimalPlaces(totalimpressions, 0)%></a></td>
            <td valign="top">
            <%
            if (app.getCrosspromote()){
                %><a href='apps.jsp?action=togglecrosspromote&appid=<%=app.getAppid()%>'>On</a><%
            } else {
                %><a href='apps.jsp?action=togglecrosspromote&appid=<%=app.getAppid()%>'>Off</a><%   
            }
            %>

            </td>
        </tr>
        <%
    }
%>
<%
double totalavgimpressionsperuser = 0;
if (totaluniqueusers>0){
    totalavgimpressionsperuser = totaltotalimpressions/totaluniqueusers;
}
%>
<tr>
    <td valign="top"><b>Total</b></td>
    <td valign="top"><b><%=Str.formatWithXDecimalPlaces(totaluniqueusers, 0)%></b></td>
    <td valign="top"><b><%=Str.formatWithXDecimalPlaces(totalavgimpressionsperuser, 2)%></b></td>
    <td valign="top"><b><%=Str.formatWithXDecimalPlaces(totaltotalimpressions, 0)%></b></td>
    <td valign="top"></td>
</tr>
</table>
<br/><br/>
<a href='appdetail.jsp?action=newapp'>+ Add App</a>
<br/>


<%@ include file="footer.jsp" %>