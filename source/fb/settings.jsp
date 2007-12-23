<%@ page import="com.fbdblog.qtype.util.AppTemplateProcessor" %>
<%@ page import="com.fbdblog.qtype.util.AppPostParser" %>
<%@ page import="com.fbdblog.qtype.util.SavePost" %>
<%@ page import="com.fbdblog.qtype.def.ComponentException" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
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
<%@ page import="java.util.*" %>
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

            if (request.getParameter("timezoneid")!=null && !request.getParameter("timezoneid").equals("")){
                boolean isvalidtimezone = false;
                String[] timezone=TimeZone.getAvailableIDs();
                for (int i=0; i<timezone.length; i++) {
                    if (request.getParameter("timezoneid").equalsIgnoreCase(timezone[i])){
                        isvalidtimezone = true;
                    }
                }
                if (isvalidtimezone){
                    userSession.getUser().setTimezoneid(request.getParameter("timezoneid"));
                    try{userSession.getUser().save();}catch(Exception ex){logger.error("", ex);}
                    Pagez.setTz(userSession.getUser().getTimezoneid());
                } else {
                    StringBuffer tmp = new StringBuffer();
                    tmp.append("<fb:error>\n" +
                    "     <fb:message>What's Up With the Timezone?</fb:message>\n" +
                    "     Your timezone is invalid... please choose another. Kthxbye.\n" +
                    "</fb:error>");
                    topOfPageMsg = topOfPageMsg + tmp.toString();
                }
            }

            StringBuffer tmp = new StringBuffer();
            tmp.append("<fb:success>\n" +
            "     <fb:message>Your Wish is My Command</fb:message>\n" +
            "     We've saved your settings and will operate according to your wishes.\n" +
            "</fb:success>");
            topOfPageMsg = topOfPageMsg + tmp.toString();
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

<table cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td valign="top" width="390">

            <form action="settings.jsp" method="post">
                <input type="hidden" name="action" value="save">
                <input type="hidden" name="nav" value="settings">
                <table cellpadding="5" cellspacing="0" border="0">
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
                            <input type="checkbox" name="isprivate" value="1" <%=isprivateChecked%>> <font style="font-size: 10px;">Yes, Keep My Stuff Private</font>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top" width="30%">
                            <font style="font-size: 12px; font-weight: bold;">Timezone</font>
                            <br/>
                            <font style="font-size: 9px;">Note that this will affect every Track app you have installed on your Facebook account.</font>
                        </td>
                        <td valign="top">
                            <select name="timezoneid">
                            <%
                            String[] timezone=TimeZone.getAvailableIDs();
                            //Treeset for alphabetizing
                            TreeSet timezoneids=new TreeSet();
                            for (int i=0; i<timezone.length; i++) {
                                timezoneids.add(timezone[i]);
                            }
                            for (Iterator iterator=timezoneids.iterator(); iterator.hasNext();) {
                                String javatimezoneid=(String) iterator.next();
                                String selected = "";
                                if (userSession.getUser().getTimezoneid().equalsIgnoreCase(javatimezoneid)){
                                    selected = " selected";
                                }
                                %>
                                <option value="<%=Str.cleanForHtml(javatimezoneid)%>" <%=selected%>><%=javatimezoneid%></option>
                                <%
                            }
                            %>
                            </select>
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
        </td>
        <td valign="top" width="250">
            <%=userSession.getApp().getAdhistoryright()%>
        </td>
    </tr>
</table>
</div>



<br/><br/>

<%@ include file="footer.jsp" %>