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
<%@ page import="com.fbdblog.util.Time" %>
<%@ include file="header.jsp" %>

<%
String topOfPageMsg = "";
%>

<%
    //Load the supportissue requested
    Supportissue supportissue=null;
    if (request.getParameter("supportissueid") != null && Num.isinteger(request.getParameter("supportissueid"))) {
        supportissue=Supportissue.get(Integer.parseInt(request.getParameter("supportissueid")));
        //Make sure it's this user's
        if (supportissue.getUserid()!=userSession.getUser().getUserid()){
            supportissue = null;
        }
    }
    if (supportissue==null || supportissue.getSupportissueid()<=0){
        %><fb:redirect url="http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=help" /><%
        return;
    }
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        if (request.getParameter("notes") != null && !request.getParameter("notes").equals("")) {
            Supportissuecomm supportissuecomm=new Supportissuecomm();
            supportissuecomm.setNotes(request.getParameter("notes"));
            supportissuecomm.setSupportissueid(supportissue.getSupportissueid());
            supportissuecomm.setUserid(userSession.getUser().getUserid());
            supportissuecomm.setDatetime(Time.nowInGmtDate());
            try {
                supportissuecomm.save();
                supportissue.refresh();
            } catch (Exception ex) {
                logger.error("", ex);
            }
            StringBuffer tmp=new StringBuffer();
            tmp.append("<fb:success>\n" +
                    "     <fb:message>Note Added</fb:message>\n" +
                    "     Your note has been added.\n" +
                    "</fb:success>");
            topOfPageMsg=topOfPageMsg + tmp.toString();
        } else {
            StringBuffer tmp=new StringBuffer();
            tmp.append("<fb:error>\n" +
                    "     <fb:message>Oops!</fb:message>\n" +
                    "     Well, we need you to enter a note.\n" +
                    "</fb:error>");
            topOfPageMsg=topOfPageMsg + tmp.toString();
        }
    }
%>


<%String selectedTab="help";%>
<%@ include file="tabs.jsp" %>

<%
if (!topOfPageMsg.equals("")){
    %><%=topOfPageMsg%><%
}
%>


<div style="padding: 10px;">

    <form action="help-issuedetail.jsp" method="post">
        <input type="hidden" name="action" value="save">
        <input type="hidden" name="nav" value="helpissuedetail">
        <input type="hidden" name="supportissueid" value="<%=supportissue.getSupportissueid()%>">
        <table cellpadding="5" cellspacing="0" border="0">
            <tr>
                <td valign="top" colspan="2">
                    <font style="font-size: 20px; font-weight: bold; color: #666666;"><%=supportissue.getSubject()%></font>
                    <br/><br/>
                </td>
            </tr>

            <%
                for (Iterator<Supportissuecomm> iterator=supportissue.getSupportissuecomms().iterator(); iterator.hasNext();){
                    Supportissuecomm supportissuecomm = iterator.next();
                    User userwhosent = User.get(supportissuecomm.getUserid());
                    %>
                    <tr>
                        <td valign="top" width="20%" style="text-align: right;">
                            <font style="font-size: 12px; font-weight: bold;"><%=userwhosent.getFirstname()%> <%=userwhosent.getLastname()%></font>
                            <br/>
                            <font style="font-size: 9px;"><%=Time.dateformatcompactwithtime(Time.gmttousertime(supportissuecomm.getDatetime(), userSession.getUser().getTimezoneid()))%></font>
                        </td>
                        <td valign="top">
                            <font style="font-size: 10px;"><%=supportissuecomm.getNotes()%></font>
                            <br/><br/>
                        </td>
                    </tr>
                    <%
                }
            %>
            <tr>
                <td valign="top" width="20%" style="text-align: right;">
                    <font style="font-size: 12px; font-weight: bold;"><%=userSession.getUser().getFirstname()%> <%=userSession.getUser().getLastname()%></font>
                    <br/>
                    <font style="font-size: 9px;">Anything you'd like to add?</font>
                </td>
                <td valign="top">
                    <textarea rows="5" cols="45" name="notes"></textarea>
                </td>
            </tr>
            <tr>
                <td valign="top">

                </td>
                <td valign="top">
                    <input type="submit" value="Add Note">
                </td>
            </tr>
        </table>
    </form>
    <br/><br/>


</div>



<br/><br/>

<%@ include file="footer.jsp" %>