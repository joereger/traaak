<%@ page import="com.fbdblog.qtype.util.AppTemplateProcessor" %>
<%@ page import="com.fbdblog.qtype.util.AppPostParser" %>
<%@ page import="com.fbdblog.qtype.util.SavePost" %>
<%@ page import="com.fbdblog.qtype.def.ComponentException" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="com.fbdblog.util.Time" %>
<%@ page import="com.fbdblog.facebook.FacebookApiWrapper" %>
<%@ page import="java.util.*" %>
<%@ page import="com.fbdblog.dao.*" %>
<%@ page import="com.fbdblog.util.Num" %>
<%@ page import="com.fbdblog.util.Str" %>
<%@ include file="header.jsp" %>

<%
//Will need this throughout the page
FacebookApiWrapper faw=new FacebookApiWrapper(userSession);
TreeSet<FacebookUser> friends = faw.getFriends();
//Create comma-separated list of friends who have app installed
StringBuffer commaSepFriendsAlreadyUsingApp = new StringBuffer();
ArrayList<FacebookUser> friendsUsingApp = new ArrayList<FacebookUser>();
for (Iterator it=friends.iterator(); it.hasNext();) {
    FacebookUser facebookUser = (FacebookUser) it.next();
    if (facebookUser.getHas_added_app()){
        friendsUsingApp.add(facebookUser);
        if (commaSepFriendsAlreadyUsingApp.length()>0){
            commaSepFriendsAlreadyUsingApp.append(",");
        }
        commaSepFriendsAlreadyUsingApp.append(facebookUser.getUid());
    }
}
%>

<%
    Chart chart=null;
    if (request.getParameter("facebookuid") != null) {
        //Load the chart requested
        if (request.getParameter("chartid") != null && Num.isinteger(request.getParameter("chartid"))) {
            chart=Chart.get(Integer.parseInt(request.getParameter("chartid")));
        }
        //If no chart requested, choose the primary for this app
        if (chart==null || chart.getChartid()<=0) {
            chart=Chart.get(userSession.getApp().getPrimarychartid());
        }
    }
%>

<%String selectedTab="friends";%>
<%@ include file="tabs.jsp" %>

<br/>
<%
if (request.getParameter("facebookuid")==null) {
    %>
    <fb:success>
    <fb:message>Check Out Your Friends' Stuff</fb:message>
    Click a friend to see their <%=userSession.getApp().getTitle()%> charts or invite them.  Friends with an asterisk* already have the app installed.
    </fb:success>
    <br/>
    <%
}
%>

<table cellpadding="3" cellspacing="0" border="0">
    <tr>
        <td valign="top" width="20">
        </td>
        <td valign="top" width="200">
            <!-- Begin Friends List -->
            <table cellpadding="2" cellspacing="0" border="0">
            <%
                for (Iterator it=friends.iterator(); it.hasNext();) {
                    FacebookUser facebookUser = (FacebookUser) it.next();
                    %>
                    <tr>
                        <td valign="top">
                            <font size="-1">
                            <a href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=friends&facebookuid=<%=facebookUser.getUid()%>'><%=facebookUser.getFirst_name()%> <%=facebookUser.getLast_name()%></a>
                            <%
                            if (facebookUser.getHas_added_app()){
                                %> *<%
                            }
                            %>
                            </font>
                        </td>
                    </tr>
                    <%
                }
            %>
            </table>
            <!-- End Friends List -->
        </td>
        <%
        if (request.getParameter("facebookuid")!=null){
            FacebookUser facebookUser = faw.getFacebookUserByUid(request.getParameter("facebookuid"));
            if (facebookUser!=null){
                %>
                <td valign="top" width="410">
                    <%
                    if (facebookUser.getHas_added_app()){
                        if (chart!=null){
                            //Need to find the userid of the person selected
                            //@todo Optimize this by only making one db call higher up in the code
                            User selectedUser = (User)HibernateUtil.getSession().createCriteria(User.class)
                                       .add(Restrictions.eq("facebookuid", facebookUser.getUid()))
                                       .setCacheable(true)
                                       .uniqueResult();
                            if (selectedUser!=null){
                                %>

                                <!-- Start Chart -->
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td valign="top" width="10">
                                        </td>
                                        <td valign="top" width="400">
                                            <form action="">
                                                <input type="hidden" name="nav" value="friends">
                                                <input type="hidden" name="facebookuid" value="<%=facebookUser.getUid()%>">
                                                <table cellpadding="0" cellspacing="2" border="0">
                                                    <tr>
                                                        <td valign="top" width="50">
                                                            <img src="<%=facebookUser.getPic_square()%>" alt="" width="50" height="50" align="top"/>
                                                        </td>
                                                        <td valign="top">
                                                            <font style="font-size: 16px; font-weight: bold;"><%=facebookUser.getFirst_name()%> <%=facebookUser.getLast_name()%>'s Stuff</font>
                                                            <br/>
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
                                                        </td>
                                                    </tr>
                                                </table>
                                            </form>
                                            <br/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td valign="top" width="10">

                                        </td>
                                        <td valign="top" width="400">
                                            <img src="<%=BaseUrl.get(false)%>fb/graph.jsp?chartid=<%=chart.getChartid()%>&userid=<%=selectedUser.getUserid()%>&size=small" alt="" width="400" height="250" style="border: 3px solid #e6e6e6;"/>
                                        </td>
                                    </tr>
                                </table>
                                <!-- End Chart -->
                                <%
                            } else {
                                //@todo what if selectedUser is null (i.e. Facebook thinks they have the app installed but we don't have their facebookuid in the database?)
                            }
                        }
                    //Put up the invitations screen
                    } else {
                        %>
                        <fb:success>
                        <fb:message><%=facebookUser.getFirst_name()%> <%=facebookUser.getLast_name()%> hasn't added <%=userSession.getApp().getTitle()%>... yet.</fb:message>
                        Invite <%=facebookUser.getFirst_name()%> and some other friends so you can compare stuff!
                        </fb:success>
                        <br/>
                        <fb:request-form
                            action="http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=friends&invitecomplete=1"
                            method="POST"
                            invite="true"
                            type="<%=userSession.getApp().getTitle()%>"
                            content="<%=userSession.getApp().getDescription()%><fb:req-choice url='http://www.facebook.com/add.php?api_key=<%=userSession.getApp().getFacebookapikey()%>' label='Check out <%=userSession.getApp().getTitle()%>!' />
                        ">
                            <fb:multi-friend-selector
                                showborder="false"
                                actiontext="Invite friends to <%=userSession.getApp().getTitle()%>."
                                exclude_ids="<%=commaSepFriendsAlreadyUsingApp.toString()%>"
                                rows="3"
                                max="20"
                                bypass="cancel" />
                        </fb:request-form>
                        <%
                    }
                    %>
                </td>
                <%
            } else {
                //@todo what if facebook doesn't return user?
            }
        }
        %>
    </tr>
</table>

<br/><br/>
<%@ include file="footer.jsp" %>