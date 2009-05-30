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
String pagetitle = "Charts";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="header.jsp" %>


<%
    //Load the chart requested
    Chart chart=null;
    if (request.getParameter("chartid") != null && Num.isinteger(request.getParameter("chartid"))) {
        chart=Chart.get(Integer.parseInt(request.getParameter("chartid")));
    }

    //If no chart requested, choose the primary for this app
    if (chart==null || chart.getChartid()<=0){
        chart=Chart.get(Pagez.getUserSession().getApp().getPrimarychartid());
    }
%>

<%String selectedTab="charts";%>
<%@ include file="tabs.jsp" %>



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
                            .add(Restrictions.eq("appid", Pagez.getUserSession().getApp().getAppid()))
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
                <input type="submit" value="Show">
            </form>
            <br/>
        </td>
    </tr>
    <tr>
        <td valign="top" width="10">

        </td>
        <td valign="top" width="600">
            <%
            String key=ChartSecurityKey.getChartKey(Pagez.getUserSession().getUser().getUserid(), chart.getChartid());
            StringBuffer embedHtml = new StringBuffer();
            embedHtml.append("<a href=\""+BaseUrl.get(false)+"user/"+Pagez.getUserSession().getUser().getNickname()+"/\">");
            embedHtml.append("<img src=\""+BaseUrl.get(false)+"fb/graph.jsp?chartid="+chart.getChartid()+"&userid="+Pagez.getUserSession().getUser().getUserid()+"&size=small&key="+key+"\"  alt=\"\" width=\"400\" height=\"250\" style=\"border: 3px solid #e6e6e6;\">");
            embedHtml.append("</a>");

            %>
            <img src="<%=BaseUrl.get(false)%>fb/graph.jsp?chartid=<%=chart.getChartid()%>&userid=<%=Pagez.getUserSession().getUser().getUserid()%>&size=medium&key=<%=key%>" alt="" width="600" height="300" style="border: 3px solid #e6e6e6;"/>
            <br/><br/>
            <font class="smallfont" style="font-weight:bold;">Embed this chart by copying the code below and pasting it into your blog or website:</font>
            <br/>
            <textarea rows="0" cols="45" readonly="readonly" onClick="javascript:this.select();"><%=embedHtml.toString()%></textarea>
        </td>
    </tr>
</table>
<br/><br/>
<center>
<%=Pagez.getUserSession().getApp().getAdunderchart()%>
</center>
<br/><br/> 

<br/><br/>

<%@ include file="footer.jsp" %>