<%@ page import="com.fbdblog.qtype.util.AppTemplateProcessor" %>
<%@ page import="com.fbdblog.qtype.util.AppPostParser" %>
<%@ page import="com.fbdblog.qtype.util.SavePost" %>
<%@ page import="com.fbdblog.qtype.def.ComponentException" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.fbdblog.util.Str" %>
<%@ page import="com.fbdblog.util.Num" %>
<%@ page import="com.fbdblog.systemprops.BaseUrl" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="com.fbdblog.chart.DataTypeDecimal" %>
<%@ page import="com.fbdblog.chart.DataTypeInteger" %>
<%@ page import="com.fbdblog.calc.Calctimeperiod" %>
<%@ page import="com.fbdblog.calc.CalculationFactory" %>
<%@ page import="com.fbdblog.calc.CalctimeperiodFactory" %>
<%@ page import="com.fbdblog.dao.*" %>
<%@ include file="header.jsp" %>

<%
String topOfPageMsg = "";
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        if (userSession.getUserappsettings()!=null) {
            if (request.getParameter("isprivate")!=null && request.getParameter("isprivate").equals("1")){
                userSession.getUserappsettings().setIsprivate(true);
            } else {
                userSession.getUserappsettings().setIsprivate(false);
            }
            try{userSession.getUserappsettings().save();}catch(Exception ex){logger.error("", ex);}
            StringBuffer tmp = new StringBuffer();
            tmp.append("<fb:success>\n" +
            "     <fb:message>Your Wish is My Command</fb:message>\n" +
            "     We've saved your settings and will operate according to your wishes.\n" +
            "</fb:success>");
            topOfPageMsg = tmp.toString();
        }
    }
%>


<%String selectedTab="settings";%>
<%@ include file="tabs.jsp" %>

<%
if (!topOfPageMsg.equals("")){
    %><%=topOfPageMsg%><%
}
%>


<div style="padding: 10px;">

    <form action="settings.jsp" method="post">
        <input type="hidden" name="action" value="save">
        <input type="hidden" name="nav" value="settings">
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td valign="top" width="30%">
                    <font style="font-size: 12px; font-weight: bold;">Keep Stuff Private?</font>
                    <br/>
                    <font style="font-size: 9px;">We won't publish to your news feeds or profile.  And we won't share your data with your friends.</font>
                </td>
                <td valign="top">
                    <%
                    String isprivateChecked = "";
                    if (userSession.getUserappsettings()!=null && userSession.getUserappsettings().getIsprivate()){
                        isprivateChecked = " checked";
                    }
                    %>
                    <input type="checkbox" name="isprivate" value="1" <%=isprivateChecked%>> <font style="font-size: 12px;">Yes, Keep My Stuff Private</font>
                </td>
            </tr>
            <tr>
                <td valign="top">

                </td>
                <td valign="top">
                    <input type="submit" value="Save Settings">
                </td>
            </tr>
        </table>
    </form>

</div>



<br/><br/>

<%@ include file="footer.jsp" %>