<%@ page import="java.util.List" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.fbdblog.util.Num" %>
<%@ page import="com.fbdblog.dao.Question" %>
<%@ page import="com.fbdblog.qtype.def.Component" %>
<%@ page import="com.fbdblog.qtype.Textbox" %>
<%@ page import="com.fbdblog.dao.Chart" %>
<%@ page import="com.fbdblog.chart.MegaChartHtmlRenderer" %>
<%@ page import="com.fbdblog.chart.MegaChart" %>
<%@ include file="header.jsp" %>

<%
    App app = null;
    if (request.getParameter("appid") != null && Num.isinteger(request.getParameter("appid"))) {
        app = App.get(Integer.parseInt(request.getParameter("appid")));
    } else {
        if (request.getParameter("action")==null){
            response.sendRedirect("apps.jsp");
            return;
        }
        app = new App();
    }
%>

<%
    MegaChart megaChart = null;
    if (request.getParameter("chartid") != null && Num.isinteger(request.getParameter("chartid"))) {
        megaChart = new MegaChart(Integer.parseInt(request.getParameter("chartid")));
    } else {
        if (request.getParameter("action") == null) {
            response.sendRedirect("appdetail.jsp?appid=" + app.getAppid());
            return;
        }
        megaChart = new MegaChart(0);
    }
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        megaChart.populateFromRequest(request);
        megaChart.getChart().setAppid(app.getAppid());
        try {
            megaChart.getChart().save();
        } catch (Exception ex) {
            logger.error(ex);
        }
        //response.sendRedirect("appdetail.jsp?appid="+app.getAppid());
        //return;
    }
%>
App: <a href='appdetail.jsp?appid=<%=app.getAppid()%>'><%=app.getTitle()%></a><br/>
Chartid:<%=megaChart.getChart().getChartid()%>: <%=megaChart.getChart().getName()%>
<br/><br/>
<form action="appdetail-chart.jsp" method="post">
    <input type="hidden" name="appid" value="<%=app.getAppid()%>">
    <input type="hidden" name="chartid" value="<%=megaChart.getChart().getChartid()%>">
    <input type="hidden" name="action" value="save">
    <table cellpadding="0" cellspacing="0" border="0">
        
        <tr>
            <td valign="top" colspan="2">
                <%
                out.print(MegaChartHtmlRenderer.getHtml(megaChart, app.getAppid()));
                %>
            </td>
        </tr>

    </table>
</form>




<%@ include file="footer.jsp" %>