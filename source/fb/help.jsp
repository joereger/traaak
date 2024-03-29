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

<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Help";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="header.jsp" %>

<%
String topOfPageMsg = "";
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        if (request.getParameter("subject") != null && !request.getParameter("subject").equals("")) {
            if (request.getParameter("notes") != null && !request.getParameter("notes").equals("")) {
                Supportissue supportissue=new Supportissue();
                supportissue.setStatus(Supportissue.STATUS_NEW);
                supportissue.setAppid(Pagez.getUserSession().getApp().getAppid());
                supportissue.setSubject(request.getParameter("subject"));
                supportissue.setType(Supportissue.TYPE_UNDEFINED);
                supportissue.setUserid(Pagez.getUserSession().getUser().getUserid());
                supportissue.setDatetime(Time.nowInGmtDate());
                supportissue.setMostrecentupdateat(Time.nowInGmtDate());
                try {
                    supportissue.save();
                } catch (Exception ex) {
                    logger.error("", ex);
                }
                Supportissuecomm supportissuecomm=new Supportissuecomm();
                supportissuecomm.setNotes(request.getParameter("notes"));
                supportissuecomm.setSupportissueid(supportissue.getSupportissueid());
                supportissuecomm.setUserid(Pagez.getUserSession().getUser().getUserid());
                supportissuecomm.setDatetime(Time.nowInGmtDate());
                try {
                    supportissuecomm.save();
                    supportissue.refresh();
                } catch (Exception ex) {
                    logger.error("", ex);
                }
                StringBuffer tmp=new StringBuffer();
                tmp.append("<fb:success>\n" +
                        "     <fb:message>The Pigeon is Flying.  Repeat: the Pigeon is Flying.</fb:message>\n" +
                        "     We'll get your message shortly and reply to you.  It's not like we're some mega-corporation with thousands of people waiting for your message to come in.  But we do care and will try to get back to you in a reasonable period of time.  Yeah, we get to define reasonable.\n" +
                        "</fb:success>");
                topOfPageMsg=topOfPageMsg + tmp.toString();
            } else {
                StringBuffer tmp=new StringBuffer();
                tmp.append("<fb:error>\n" +
                        "     <fb:message>Oops!</fb:message>\n" +
                        "     Well, we need you to enter a message... so we have something to respond to... subjects rarely provide enough detail.\n" +
                        "</fb:error>");
                topOfPageMsg=topOfPageMsg + tmp.toString();
            }
        } else {
            StringBuffer tmp=new StringBuffer();
            tmp.append("<fb:error>\n" +
                    "     <fb:message>Oops!</fb:message>\n" +
                    "     We need you to enter a subject.\n" +
                    "</fb:error>");
            topOfPageMsg=topOfPageMsg + tmp.toString();
        }

    }
%>


<%String selectedTab="help";%>
<%@ include file="tabs.jsp" %>

<%
if (!topOfPageMsg.equals("")){
    %><div class="traaakbox"><%=topOfPageMsg%></div><%
}
%>


<div style="padding: 10px;">

<table cellpadding="5" cellspacing="0" border="0">
    <tr>
        <td valign="top" width="70%">

            <font style="font-size: 14px; font-weight: bold; color: #666666;">Got something to say?  Have a Question? Found a bug?  Want to track other stuff?</font>
            <br/>
            <font style="font-size: 9px; color: #666666;">We're geeks, stuck behind computer monitors 24/7... we crave the attention! So let us know what you think.  Recommend something new that you'd like to track.  Tell us about a bug.  Ask us a question about how something works.  We're game.</font>
            <br/><br/>
            <form action="help.jsp" method="post">
                <input type="hidden" name="action" value="save">
                <input type="hidden" name="nav" value="help">
                <table cellpadding="5" cellspacing="0" border="0">
                    <tr>
                        <td valign="top" width="20%">
                            <font style="font-size: 12px; font-weight: bold;">Subject</font>
                            <br/>
                            <font style="font-size: 9px;">The short and sweet of it.</font>
                        </td>
                        <td valign="top">
                            <input type="text" name="subject" value="" size="40" maxlength="100">
                        </td>
                    </tr>
                    <tr>
                        <td valign="top" width="20%">
                            <font style="font-size: 12px; font-weight: bold;">Message</font>
                            <br/>
                            <font style="font-size: 9px;">Let 'er rip.</font>
                        </td>
                        <td valign="top">
                            <textarea rows="5" cols="45" name="notes"></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">

                        </td>
                        <td valign="top">
                            <input type="submit" value="Release the Pigeon">
                        </td>
                    </tr>
                </table>
            </form>
            <br/><br/>
            <!-- FAQ will go here -->
        </td>
        <td valign="top" bgcolor="#f6f6f6">
            <font style="font-size: 14px; font-weight: bold; color: #666666;">Things You've Sent Us</font>
            <br/>
            <%
                List<Supportissue> supportissues=HibernateUtil.getSession().createCriteria(Supportissue.class)
                        .add(Restrictions.eq("userid", Pagez.getUserSession().getUser().getUserid()))
                        .add(Restrictions.eq("appid", Pagez.getUserSession().getApp().getAppid()))
                        .addOrder(Order.desc("mostrecentupdateat"))
                        .setCacheable(true)
                        .list();
                if (supportissues != null && supportissues.size()>0) {
                    %>
                    <%
                    for (Iterator<Supportissue> iterator=supportissues.iterator(); iterator.hasNext();) {
                         Supportissue supportissue=iterator.next();
                         String statusStr = "New";
                         if (supportissue.getStatus()==Supportissue.STATUS_NEW){
                            statusStr = "New";
                         } else if (supportissue.getStatus()==Supportissue.STATUS_REPLYWAITING){
                            statusStr = "Reply Waiting";
                         } else if (supportissue.getStatus()==Supportissue.STATUS_CLOSED){
                            statusStr = "Closed";    
                         }
                        %>
                        <font style="font-size: 8px; font-weight: bold;"><%=Time.agoText(Time.getCalFromDate(supportissue.getMostrecentupdateat()))%>: <%=statusStr%></font>
                        <br/>
                        <a href="<%=linkToUrl%><%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=helpissuedetail&supportissueid=<%=supportissue.getSupportissueid()%>"><font style="font-size: 10px; font-weight: bold;"><%=supportissue.getSubject()%></font></a>
                        <br/><br/>
                        <%
                    }
                } else {
                    %>
                    <font style="font-size: 9px; color: #666666;">You haven't sent us anything yet!</font>
                    <%
                }
                %>
        </td>

    </tr>
</table>

</div>



<br/><br/>

<%@ include file="footer.jsp" %>