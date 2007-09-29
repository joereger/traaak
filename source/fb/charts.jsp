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
<%@ include file="header.jsp" %>


<%
    //Load the chart requested
    Chart chart=null;
    if (request.getParameter("chartid") != null && Num.isinteger(request.getParameter("chartid"))) {
        chart=Chart.get(Integer.parseInt(request.getParameter("chartid")));
    }

    //If no chart requested, choose the primary for this app
    if (chart==null || chart.getChartid()<=0){
        chart=Chart.get(userSession.getApp().getPrimarychartid());
    }
%>


<br/>
<fb:tabs>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=main' title='Track Stuff'/>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=charts' title='Da Charts' selected='true'/>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=history' title='Yo History'/>
</fb:tabs>
<br/>



<table>
    <tr>
        <td valign="top" width="10">

        </td>
        <td valign="top" width="600">
            <form action="">
                <input type="hidden" name="nav" value="charts">
                <select name="chartid">
                    <%
                    List<Chart> charts=HibernateUtil.getSession().createCriteria(Chart.class)
                            .add(Restrictions.eq("appid", userSession.getApp().getAppid()))
                            .setCacheable(true)
                            .list();
                    for (Iterator<Chart> iterator=charts.iterator(); iterator.hasNext();) {
                        Chart chartTmp = iterator.next();
                        String selected = "";
                        if (chart!=null && chartTmp.getChartid()==chart.getChartid()){
                            selected = " selected";
                        }
                        %>
                            <option value="<%=chartTmp.getChartid()%>" <%=selected%>><%=Str.cleanForHtml(chartTmp.getName())%></option>
                        <%
                    }
                    %>
                </select>
                <input type="submit" value="Gimme">
            </form>
            <br/>
        </td>
    </tr>
    <tr>
        <td valign="top" width="10">

        </td>
        <td valign="top" width="600">
            <img src="<%=BaseUrl.get(false)%>fb/graph.jsp?chartid=<%=chart.getChartid()%>&userid=<%=userSession.getUser().getUserid()%>&size=medium&comparetouserid=0" alt="" width="600" height="300" style="border: 3px solid #e6e6e6;"/>
        </td>
    </tr>
</table>

<br/><br/> 

<br/><br/>

<%@ include file="footer.jsp" %>