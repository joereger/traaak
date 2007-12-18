<%@ page import="com.fbdblog.scheduledjobs.ImpressionCache" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="com.fbdblog.util.Num" %>
<%@ page import="com.fbdblog.util.Str" %>
<%@ include file="header.jsp" %>


<%
    App app=null;
    if (request.getParameter("appid") != null && Num.isinteger(request.getParameter("appid"))) {
        app=App.get(Integer.parseInt(request.getParameter("appid")));
    }
%>

<font class="pagetitle">Impressions</font>
<br/><br/>

<form action="impressions.jsp" method="get">
<select name="appid">
<%
List apps=HibernateUtil.getSession().createQuery("from App").list();
for (Iterator iterator=apps.iterator(); iterator.hasNext();) {
    App appTmp =(App) iterator.next();
    String selected = "";
    if (app!=null && appTmp.getAppid()==app.getAppid()){
        selected = " selected";
    }
    %>
        <option value="<%=appTmp.getAppid()%>" <%=selected%>><%=appTmp.getTitle()%></option>
    <%
}
%>
</select>
<input type="submit" value="Go">
</form>


<table cellpadding="3" cellspacing="0" border="0">
<tr>
<td valign="top">Year</td>
<td valign="top">Month</td>
<td valign="top">UniqueUsers</td>
<td valign="top">AvgImpPerUser</td>
<td valign="top">TotalImp</td>
</tr>
<%
    if (app!=null){
        Calendar now = Calendar.getInstance();
        int nowyear = now.get(Calendar.YEAR);
        int nowmonth = now.get(Calendar.MONTH)+1;

        int currentmonth = nowmonth;
        int currentyear = nowyear;

        boolean keepgoing = true;
        while(keepgoing){
            //Go get some numbers
            Double avgimpressionsperuser = 0.0;
            Object obj1 = HibernateUtil.getSession().createQuery("select avg(impressions) from Impression where appid='"+app.getAppid()+"' and year='"+currentyear+"' and month='"+currentmonth+"'").uniqueResult();
            if (obj1!=null){
                avgimpressionsperuser = ((Double)obj1);
            }

            Double totalimpressions = 0.0;
            Object obj2 = HibernateUtil.getSession().createQuery("select sum(impressions) from Impression where appid='"+app.getAppid()+"' and year='"+currentyear+"' and month='"+currentmonth+"'").uniqueResult();
            if (obj2!=null){
                totalimpressions = ((Long)obj2).doubleValue();
            }

            Double uniqueusers = 0.0;
            Object obj3 = HibernateUtil.getSession().createQuery("select count(*) from Impression where appid='"+app.getAppid()+"' and year='"+currentyear+"' and month='"+currentmonth+"'").uniqueResult();
            if (obj3!=null){
                uniqueusers = ((Long)obj3).doubleValue();
            }

            %>
            <tr>
            <td valign="top"><%=currentyear%></td>
            <td valign="top"><%=currentmonth%></td>
            <td valign="top"><%=Str.formatForMoney(uniqueusers)%></td>
            <td valign="top"><%=Str.formatForMoney(avgimpressionsperuser)%></td>
            <td valign="top"><%=Str.formatForMoney(totalimpressions)%></td>
            </tr>


            <%

            //Decrement year/month
            if (currentmonth>=2){
                currentmonth=currentmonth-1;
            } else if (currentmonth==1) {
                currentmonth = 12;
                currentyear = currentyear-1;
            }
            //Break when we reach 2006
            if (currentyear<=2006){
                keepgoing=false;
            }
        }
    }

%>
</table>




<%@ include file="footer.jsp" %>