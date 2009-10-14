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
<%@ page import="com.fbdblog.htmlui.*" %>
<%@ page import="com.fbdblog.dao.hibernate.NumFromUniqueResult" %>

<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Settings";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="header.jsp" %>

<%
String topOfPageMsg = "";
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        if (Pagez.getUserSession().getUserappsettings()!=null) {
            if (request.getParameter("isprivate")!=null && request.getParameter("isprivate").equals("1")){
                Pagez.getUserSession().getUserappsettings().setIsprivate(true);
            } else {
                Pagez.getUserSession().getUserappsettings().setIsprivate(false);
            }
            try{Pagez.getUserSession().getUserappsettings().save();}catch(Exception ex){logger.error("", ex);}

            if (request.getParameter("timezoneid")!=null && !request.getParameter("timezoneid").equals("")){
                boolean isvalidtimezone = false;
                String[] timezone=TimeZone.getAvailableIDs();
                for (int i=0; i<timezone.length; i++) {
                    if (request.getParameter("timezoneid").equalsIgnoreCase(timezone[i])){
                        isvalidtimezone = true;
                    }
                }
                if (isvalidtimezone){
                    Pagez.getUserSession().getUser().setTimezoneid(request.getParameter("timezoneid"));
                    try{Pagez.getUserSession().getUser().save();}catch(Exception ex){logger.error("", ex);}
                    Pagez.setTz(Pagez.getUserSession().getUser().getTimezoneid());
                } else {
                    StringBuffer tmp = new StringBuffer();
                    tmp.append("<fb:error>\n" +
                    "     <fb:message>What's Up With the Timezone?</fb:message>\n" +
                    "     Your timezone is invalid... please choose another. Kthxbye.\n" +
                    "</fb:error>");
                    topOfPageMsg = topOfPageMsg + tmp.toString();
                }
            }

            if (request.getParameter("nickname")!=null && !request.getParameter("nickname").equals("") && !request.getParameter("nickname").trim().equals(Pagez.getUserSession().getUser().getNickname().trim())){
                int cnt = NumFromUniqueResult.getInt("select count(*) from User where nickname='"+Str.cleanForSQL(request.getParameter("nickname").trim())+"' and userid<>'"+Pagez.getUserSession().getUser().getUserid()+"'");
                if (cnt>0){
                    topOfPageMsg = topOfPageMsg + "The nickname ("+request.getParameter("nickname")+") is already in use and was not added to your account.";
                } else {
                    Pagez.getUserSession().getUser().setNickname(request.getParameter("nickname"));
                    try{Pagez.getUserSession().getUser().save();}catch(Exception ex){logger.error("", ex);}
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
                            <font style="font-size: 12px; font-weight: bold;">Keep <%=Pagez.getUserSession().getApp().getTitle()%> Data Private?</font>
                            <br/>
                            <font style="font-size: 9px;">Applies only to <%=Pagez.getUserSession().getApp().getTitle()%> data.</font>
                        </td>
                        <td valign="top">
                            <%
                            String isprivateChecked = "";
                            if (Pagez.getUserSession().getUserappsettings()!=null && Pagez.getUserSession().getUserappsettings().getIsprivate()){
                                isprivateChecked = " checked";
                            }
                            %>
                            <input type="checkbox" name="isprivate" value="1" <%=isprivateChecked%>> <font style="font-size: 10px;">Yes, Keep <%=Pagez.getUserSession().getApp().getTitle()%> Data Private</font>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font class="formfieldnamefont">Nickname</font>
                            <br/>
                            <font style="font-size: 9px;">Others will see this.  Determines where your profile is... i.e. traaak.com/user/nickname/.  Also applies to everything you traaak.</font>
                        </td>
                        <td valign="top">
                            <%=com.fbdblog.htmlui.Textbox.getHtml("nickname", Pagez.getUserSession().getUser().getNickname(), 255, 20, "", "")%>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top" width="30%">
                            <font style="font-size: 12px; font-weight: bold;">Timezone</font>
                            <br/>
                            <font style="font-size: 9px;">Note that this will affect everything you Traaak.</font>
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
                                if (Pagez.getUserSession().getUser().getTimezoneid().equalsIgnoreCase(javatimezoneid)){
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
            <%=Pagez.getUserSession().getApp().getAdhistoryright()%>
        </td>
    </tr>
</table>
</div>



<br/><br/>

<%@ include file="footer.jsp" %>