<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="java.util.List" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.fbdblog.systemprops.BaseUrl" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="com.fbdblog.dao.User" %>
<%@ page import="com.fbdblog.helpers.FindUserFromNickname" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.fbdblog.helpers.FindAppsUserHasUsed" %>
<%@ page import="com.fbdblog.session.FindUserappsettings" %>
<%@ page import="com.fbdblog.dao.Userappsettings" %>
<%@ page import="com.fbdblog.dao.Chart" %>
<%@ page import="com.fbdblog.chart.ChartSecurityKey" %>
<%@ page import="com.fbdblog.chart.FlashChartWidget" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Traaak";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>

<%
//See if this user exists
User userToDisplay = FindUserFromNickname.find(request.getParameter("nickname"));
if(userToDisplay==null || !userToDisplay.getIsenabled()){
    Pagez.getUserSession().setMessage("User not found.");
    Pagez.sendRedirect("/index.jsp");
    return;
}
%>


<%@ include file="/template/header.jsp" %>

<font class="largefont"><%=userToDisplay.getNickname()%>'s Profile</font>
<br/><br/>
<%
ArrayList<App> appsUserUses = FindAppsUserHasUsed.find(userToDisplay);
if (appsUserUses!=null){
    for (Iterator<App> appIterator=appsUserUses.iterator(); appIterator.hasNext();) {
        App app=appIterator.next();
        Userappsettings userappsettings = FindUserappsettings.get(userToDisplay, app);
        if (userappsettings==null || !userappsettings.getIsprivate()){
            Chart chart = Chart.get(app.getPrimarychartid());
            %>
            <br/><font class="normalfont" style="font-weight: bold;"><%=userToDisplay.getNickname()%> has used: <%=app.getFacebookappname()%></font>
            <br/>
            <%if (Pagez.getUserSession().getIsfacebook()){%>
                <%
                String key=ChartSecurityKey.getChartKey(userToDisplay.getUserid(), chart.getChartid());
                StringBuffer embedHtml = new StringBuffer();
                embedHtml.append("<a href=\""+BaseUrl.get(false)+"user/"+userToDisplay.getNickname()+"/\">");
                embedHtml.append("<img src=\""+BaseUrl.get(false)+"fb/graph.jsp?chartid="+chart.getChartid()+"&userid="+userToDisplay.getUserid()+"&size=small&key="+key+"\"  alt=\"\" width=\"400\" height=\"250\" style=\"border: 3px solid #e6e6e6;\">");
                embedHtml.append("</a>");
                %>
                <img src="<%=BaseUrl.get(false)%>fb/graph.jsp?chartid=<%=chart.getChartid()%>&userid=<%=userToDisplay.getUserid()%>&size=medium&key=<%=key%>" alt="" width="600" height="300" style="border: 3px solid #e6e6e6;"/>
            <%}%>
            <%if (!Pagez.getUserSession().getIsfacebook()) { %>
                    <%=FlashChartWidget.getEmbedCode(chart, userToDisplay, 600, 200)%>
            <%}%>
            <br/><br/>
            <%
        } else {
            //This is a private app    
        }
    }
}
%>



<%@ include file="/template/footer.jsp" %>
