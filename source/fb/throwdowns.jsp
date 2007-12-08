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
<%@ page import="com.fbdblog.facebook.FacebookApiWrapper" %>
<%@ page import="com.fbdblog.util.Time" %>
<%@ page import="java.util.ArrayList" %>
<%@ include file="header.jsp" %>



<%
    String topOfPageMsg = "";
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("addcomplete")) {
        //Set msg
        StringBuffer tmp=new StringBuffer();
        tmp.append("<fb:success>\n" +
                "     <fb:message>It's on like Donkey Kong!</fb:message>\n" +
                "     We've sent your friend a message so that they can accept your Throwdown challenge.\n" +
                "</fb:success>");
        topOfPageMsg=tmp.toString();
    }
%>


<%String selectedTab="throwdown";%>
<%@ include file="tabs.jsp" %>

<%
if (!topOfPageMsg.equals("")){
    %><%=topOfPageMsg%><%
}
%>

<%
FacebookApiWrapper faw=new FacebookApiWrapper(userSession);
%>


<div style="padding: 10px;">




<table cellpadding="3" cellspacing="1" border="0" width="100%">
<tr>
    <td valign="top">
        <fb:success>
            <fb:message>What're you waiting for?</fb:message>
            <a href="http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=throwdownadd">It's always a good time for a Throwdown!!!</a>
        </fb:success>

        <br/><br/>

        <table cellpadding="3" cellspacing="1" border="0" width="100%">

            <tr>
                <td valign="top" colspan="3" bgcolor="#e6e6e6"><b>Throwdowns You've Instigated</b></td>
            </tr>
            <%
                if (1==1){
                    List<Throwdown> throwdowns=HibernateUtil.getSession().createCriteria(Throwdown.class)
                            .add(Restrictions.eq("fromuserid", userSession.getUser().getUserid()))
                            .setCacheable(true)
                            .list();
                    if (throwdowns!=null && throwdowns.size()>0){
                        for (Iterator<Throwdown> iterator=throwdowns.iterator(); iterator.hasNext();) {
                            Throwdown throwdown=iterator.next();
                            FacebookUser toFbUser = faw.getFacebookUserByUid(throwdown.getTofacebookuid());
                            %>
                             <tr>
                                 <td valign="top" colspan="3"><a href="http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=throwdown&throwdownid=<%=throwdown.getThrowdownid()%>"><font style="font-size: 15px; font-weight: bold;"><%=throwdown.getName()%></font></a><br/><font style="font-size: 8px; font-weight: bold;">Ends: <%=Time.dateformatcompactwithtime(Time.getCalFromDate(throwdown.getEnddate()))%></font></td>
                             </tr>
                             <tr>
                                 <td valign="top" width="40%"><div style="text-align: right;"><img src="<%=userSession.getFacebookUser().getPic_square()%>" alt="" width="50" height="50"/><br/><%=userSession.getFacebookUser().getFirst_name()%><br/><%=userSession.getFacebookUser().getLast_name()%></div></td>
                                 <td valign="top" width="20%"><center><font style="font-size: 14px; font-weight: bold; color: #666666;">vs.</font></center></td>
                                 <td valign="top" width="40%"><img src="<%=toFbUser.getPic_square()%>" alt="" width="50" height="50"/><br/><%=toFbUser.getFirst_name()%><br/><%=toFbUser.getLast_name()%></td>
                             </tr>
                             <%
                        }
                    } else {
                        %>
                        <tr>
                            <td valign="top" colspan="3">None... the competitive spirit is weak in you... <a href="http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=throwdownadd">why not take it up a notch?</a></td>
                        </tr>
                        <%
                    }
                }
            %>

            <tr>
                <td valign="top" colspan="3"><br/><br/></td>
            </tr>
            <tr>
                <td valign="top" colspan="3" bgcolor="#e6e6e6"><b>Throwdowns Where You've Been Called Out</b></td>
            </tr>
            <%
                if (1==1){
                    List<Throwdown> throwdowns=HibernateUtil.getSession().createCriteria(Throwdown.class)
                            .add(Restrictions.eq("tofacebookuid", userSession.getFacebookUser().getUid()))
                            .setCacheable(true)
                            .list();
                    if (throwdowns!=null && throwdowns.size()>0){
                        for (Iterator<Throwdown> iterator=throwdowns.iterator(); iterator.hasNext();) {
                            Throwdown throwdown=iterator.next();
                            User fromUser = User.get(throwdown.getFromuserid());
                            FacebookUser fromFbUser = faw.getFacebookUserByUid(fromUser.getFacebookuid());
                            %>
                             <tr>
                                 <td valign="top" colspan="3"><a href="http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=throwdown&throwdownid=<%=throwdown.getThrowdownid()%>"><font style="font-size: 15px; font-weight: bold;"><%=throwdown.getName()%></font></a><br/><font style="font-size: 8px; font-weight: bold;">Ends: <%=Time.dateformatcompactwithtime(Time.getCalFromDate(throwdown.getEnddate()))%></font></td>
                             </tr>
                             <tr>
                                 <td valign="top" width="40%"><div style="text-align: right;"><img src="<%=fromFbUser.getPic_square()%>" alt="" width="50" height="50"/><br/><%=fromFbUser.getFirst_name()%><br/><%=fromFbUser.getLast_name()%></div></td>
                                 <td valign="top" width="20%"><center><font style="font-size: 14px; font-weight: bold; color: #666666;">vs.</font></center></td>
                                 <td valign="top" width="40%"><img src="<%=userSession.getFacebookUser().getPic_square()%>" alt="" width="50" height="50"/><br/><%=userSession.getFacebookUser().getFirst_name()%><br/><%=userSession.getFacebookUser().getLast_name()%></td>
                             </tr>
                             <%
                        }
                    } else {
                        %>
                        <tr>
                            <td valign="top" colspan="3">None... not being a target is a worthy skill in life.</td>
                        </tr>
                        <%
                    }
                }
            %>

            <tr>
                <td valign="top" colspan="3"><br/><br/></td>
            </tr>
            <tr>
                <td valign="top" colspan="3" bgcolor="#e6e6e6"><b>Your Friends' Throwdowns</b></td>
            </tr>
            <%
                if (1 == 1) {
                    ArrayList<Integer> frienduids=faw.getFriendUids();
                    if (frienduids!=null && frienduids.size()>0) {
                        //frienduidsFql will be used in the main query on the throwdowns tabls
                        //userFql is used in a pre query to get the userids of those people who are this user's friends
                        StringBuffer frienduidsFql=new StringBuffer();
                        StringBuffer userFql=new StringBuffer();
                        frienduidsFql.append(" ( ");
                        userFql.append(" ( ");
                        for (Iterator<Integer> iterator=frienduids.iterator(); iterator.hasNext();) {
                            Integer frienduid=iterator.next();
                            frienduidsFql.append(" tofacebookuid='" + frienduid + "'");
                            userFql.append(" facebookuid='" + frienduid + "'");
                            if (iterator.hasNext()) {
                                frienduidsFql.append(" OR ");
                                userFql.append(" OR ");
                            }
                        }
                        frienduidsFql.append(" ) ");
                        userFql.append(" ) ");
                        //Get some fql for the throwdown table's fromuserid col
                        StringBuffer tduserFql = new StringBuffer();
                        List<Integer> userids=HibernateUtil.getSession().createQuery("select userid from User where " + userFql).list();
                        if (userids!=null && userids.size()>0){
                            tduserFql.append(" ( ");
                            for (Iterator<Integer> iterator=userids.iterator(); iterator.hasNext();) {
                                Integer userid = iterator.next();
                                tduserFql.append(" fromuserid='" + userid + "'");
                                if (iterator.hasNext()) {
                                    tduserFql.append(" OR ");
                                }
                            }
                            tduserFql.append(" ) ");
                        }
                        if (tduserFql.length()==0){
                            tduserFql.append(" (fromuserid='-1') "); //Something that won't trigger a match
                        }
                        //Query the throwdown table using all this convoluted fql... hope it works
                        List<Throwdown> throwdowns=HibernateUtil.getSession().createQuery("from Throwdown where ( "+frienduidsFql+" OR "+tduserFql+" ) AND (fromuserid<>'"+userSession.getUser().getUserid()+"') AND (tofacebookuid<>'"+userSession.getUser().getFacebookuid()+"')").list();
                        if (throwdowns != null && throwdowns.size()>0) {
                            for (Iterator<Throwdown> iterator=throwdowns.iterator(); iterator.hasNext();) {
                                Throwdown throwdown=iterator.next();
                                User fromUser=User.get(throwdown.getFromuserid());
                                FacebookUser fromFbUser=faw.getFacebookUserByUid(fromUser.getFacebookuid());
                                FacebookUser toFbUser = faw.getFacebookUserByUid(throwdown.getTofacebookuid());
                                %>
                                 <tr>
                                     <td valign="top" colspan="3"><a href="http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=throwdown&throwdownid=<%=throwdown.getThrowdownid()%>"><font style="font-size: 15px; font-weight: bold;"><%=throwdown.getName()%></font></a><br/><font style="font-size: 8px; font-weight: bold;">Ends: <%=Time.dateformatcompactwithtime(Time.getCalFromDate(throwdown.getEnddate()))%></font></td>
                                 </tr>
                                 <tr>
                                     <td valign="top" width="40%"><div style="text-align: right;"><img src="<%=fromFbUser.getPic_square()%>" alt="" width="50" height="50"/><br/><%=fromFbUser.getFirst_name()%><br/><%=fromFbUser.getLast_name()%></div></td>
                                     <td valign="top" width="20%"><center><font style="font-size: 14px; font-weight: bold; color: #666666;">vs.</font></center></td>
                                     <td valign="top" width="40%"><img src="<%=toFbUser.getPic_square()%>" alt="" width="50" height="50"/><br/><%=toFbUser.getFirst_name()%><br/><%=toFbUser.getLast_name()%></td>
                                 </tr>
                                 <%
                            }
                        } else {
                            %>
                            <tr>
                                <td valign="top" colspan="3">None... no offense, but your friends are less than exciting... why not <a href="http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=throwdownadd">spice things up a bit?</a></td>
                            </tr>
                            <%
                        }
                    } else {
                        %>
                        <tr>
                            <td valign="top" colspan="3">None... no offense, your friends are less than exciting... why not <a href="http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=throwdownadd">spice things up a bit?</a></td>
                        </tr>
                        <%
                    }
                }
            %>

        </table>

    </td>
    <td valign="top" width="300">
        <img src="<%=BaseUrl.get(false)%>images/throwdown-title.gif" alt="" width="300" height="170" align="right"/>
    </td>
</tr>
</table>



</div>



<br/><br/>

<%@ include file="footer.jsp" %>