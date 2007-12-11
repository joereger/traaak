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
<%@ page import="com.fbdblog.session.FindUserappsettings" %>
<%@ page import="com.fbdblog.chart.ChartSecurityKey" %>
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
    if (request.getParameter("frienduserid")!=null && Num.isinteger(request.getParameter("frienduserid"))){
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
    <fb:message>These Friends Track the Same Stuff You Do</fb:message>
    Click a friend to see their <%=userSession.getApp().getTitle()%> charts or invite them.  Listed are those who have the app installed.
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
                boolean haveaddedafriend = false;
                for (Iterator it=friends.iterator(); it.hasNext();) {
                    FacebookUser facebookUser=(FacebookUser) it.next();
                    if (facebookUser.getHas_added_app()) {
                        User user=null;
                        List<User> users=HibernateUtil.getSession().createCriteria(User.class)
                                .add(Restrictions.eq("facebookuid", facebookUser.getUid()))
                                .setCacheable(true)
                                .list();
                        for (Iterator<User> iterator=users.iterator(); iterator.hasNext();) {
                            user=iterator.next();
                        }
                        Userappsettings userappsettings=FindUserappsettings.get(user, userSession.getApp());
                        if (user!=null && !userappsettings.getIsprivate()) {
                            haveaddedafriend = true;
                            %>
                            <tr>
                                <td valign="top">
                                    <font style="font-size: 14px; font-weight: bold;">
                                        <a href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=friends&frienduserid=<%=user.getUserid()%>'><%=facebookUser.getFirst_name()%> <%=facebookUser.getLast_name()%></a>
                                    </font>
                                </td>
                                <%//@todo calculations columns?%>
                            </tr>
                            <%
                        }
                    }
                }
                if (!haveaddedafriend){
                    %>
                    <tr>
                        <td valign="top">
                            <font size="-1">
                                None.
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
        if (request.getParameter("frienduserid")!=null && Num.isinteger(request.getParameter("frienduserid"))){
            User friend = User.get(Integer.parseInt(request.getParameter("frienduserid")));
            if (friend!=null){
                Userappsettings friendUserappsettings=FindUserappsettings.get(friend, userSession.getApp());
                if (!friendUserappsettings.getIsprivate()){
                    FacebookUser friendFacebookuser = new FacebookUser(Long.parseLong(String.valueOf(friend.getFacebookuid())), userSession.getFacebooksessionkey(), userSession.getApp().getFacebookapikey(), userSession.getApp().getFacebookapisecret());
                    %>
                    <td valign="top" width="410">
                    <%
                    if (chart!=null){
                        %>
                        <!-- Start Chart -->
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td valign="top" width="10">
                                </td>
                                <td valign="top" width="400">
                                    <form action="">
                                        <input type="hidden" name="nav" value="friends">
                                        <input type="hidden" name="frienduserid" value="<%=friend.getUserid()%>">
                                        <table cellpadding="0" cellspacing="2" border="0">
                                            <tr>
                                                <td valign="top" width="50">
                                                    <img src="<%=friendFacebookuser.getPic_square()%>" alt="" width="50" height="50" align="top"/>
                                                </td>
                                                <td valign="top">
                                                    <font style="font-size: 16px; font-weight: bold;"><%=friendFacebookuser.getFirst_name()%> <%=friendFacebookuser.getLast_name()%>'s Stuff</font>
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
                                    <%
                                    String key=ChartSecurityKey.getChartKey(userSession.getUser().getUserid(), chart.getChartid());
                                    %>
                                    <img src="<%=BaseUrl.get(false)%>fb/graph.jsp?chartid=<%=chart.getChartid()%>&userid=<%=friend.getUserid()%>&size=small&key=<%=key%>" alt="" width="400" height="250" style="border: 3px solid #e6e6e6;"/>
                                </td>
                            </tr>
                        </table>
                        <!-- End Chart -->
                        <%
                    }
                    %>
                    </td>
                    <%
                } else {
                    %>Sorry, that friend is not found.<%
                }
            } else {
                %>
                Sorry, that friend is not found.
                <%
            }
        }
        %>
    </tr>
</table>
<br/><br/><br/>
<fb:request-form
    action="http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=friends&invitecomplete=1"
    method="POST"
    invite="true"
    type="<%=userSession.getApp().getTitle()%>"
    content="<%=userSession.getApp().getDescription()%> <fb:req-choice url='http://www.facebook.com/add.php?api_key=<%=userSession.getApp().getFacebookapikey()%>' label='Check out <%=userSession.getApp().getTitle()%>!' />
">
    <fb:multi-friend-selector
        showborder="true"
        actiontext="Invite friends to <%=userSession.getApp().getTitle()%>."
        exclude_ids="<%=commaSepFriendsAlreadyUsingApp.toString()%>"
        rows="3"
        max="20"
        bypass="cancel" />
</fb:request-form>

<br/><br/>
<%@ include file="footer.jsp" %>