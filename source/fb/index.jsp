<%@ page import="com.fbdblog.qtype.util.AppTemplateProcessor" %>
<%@ page import="com.fbdblog.qtype.util.AppPostParser" %>
<%@ page import="com.fbdblog.qtype.util.SavePost" %>
<%@ page import="com.fbdblog.qtype.def.ComponentException" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="com.fbdblog.dao.Chart" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.fbdblog.systemprops.BaseUrl" %>
<%@ page import="com.fbdblog.dao.Post" %>
<%@ page import="com.fbdblog.util.Num" %>
<%@ page import="com.fbdblog.util.Time" %>
<%@ page import="com.fbdblog.chart.chartcache.ClearCache" %>
<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="com.fbdblog.dao.hibernate.NumFromUniqueResult" %>
<%@ page import="com.fbdblog.calc.DoCalculationsAfterPost" %>
<%@ include file="header.jsp" %>

<%
    //Load the post requested
    Post post=null;
    if (request.getParameter("postid") != null && Num.isinteger(request.getParameter("postid"))) {
        post=Post.get(Integer.parseInt(request.getParameter("postid")));
    }
%>

<%
//For holding top of page messages
String topOfPageMsg = "";
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("trackit")) {
        AppPostParser appPostParser = new AppPostParser(request);
        try {
            SavePost.save(userSession.getApp(), userSession.getUser(), post, appPostParser, userSession);
            StringBuffer tmp = new StringBuffer();
            tmp.append("<fb:success>\n" +
            "     <fb:message>Good trackin'.  Now track s'more.</fb:message>\n" +
            "     We've updated your profile so that others can check out your stuff.  Now's a good time to check out <a href='http://apps.facebook.com/"+userSession.getApp().getFacebookappname()+"/?nav=friends'>your friends' stuff</a>.\n" +
            "</fb:success>");
            if (!userSession.getApp().getAdpostsave().equals("")){
                tmp.append("<br/>");
                tmp.append(userSession.getApp().getAdpostsave());
                tmp.append("<br/>");
            }
            topOfPageMsg = tmp.toString();
        } catch (ComponentException cex) {
            StringBuffer tmp = new StringBuffer();
            tmp.append(" <fb:error>\n" +
            "      <fb:message>Houston, we have a problem.</fb:message>\n" +
            "      "+cex.getErrorsAsSingleString()+"\n" +
            " </fb:error>");
            topOfPageMsg = tmp.toString();
        }
    }
%>

<%
if (userSession.getIsnewappforthisuser()){
    StringBuffer tmp = new StringBuffer();
    tmp.append("<fb:success>\n" +
    "     <fb:message>Howdy!</fb:message>\n" +
    "     Glad you've added "+userSession.getApp().getTitle()+".  Track some stuff and get some charts.  Just realize that everything you track is public.\n" +
    "</fb:success>");
    topOfPageMsg = tmp.toString();
}
%>


<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("deletepost")) {
        if (request.getParameter("postid") != null && Num.isinteger(request.getParameter("postid"))) {
            Post postToDel=Post.get(Integer.parseInt(request.getParameter("postid")));
            if (postToDel.getUserid() == userSession.getUser().getUserid()) {
                try {
                    postToDel.delete();
                } catch (Exception ex) {
                    logger.error("", ex);
                }
                post=null;
                StringBuffer tmp=new StringBuffer();
                tmp.append("<fb:success>\n" +
                        "     <fb:message>As ordered, thy data hath been deleted.</fb:message>\n" +
                        "     Now add some more so this app doesn't get an inferiority complex.  It's lonely.  It needs data.\n" +
                        "</fb:success>");
                topOfPageMsg=tmp.toString();
                //Clear the chart image cache
                ClearCache.clearCacheForUser(userSession.getUser().getUserid(), userSession.getApp().getAppid());
                //Do Calculations
                DoCalculationsAfterPost.doCalculations(userSession.getUser(), userSession.getApp());
            } else {
                StringBuffer tmp=new StringBuffer();
                tmp.append(" <fb:error>\n" +
                        "      <fb:message>Seriously? You don't even own that post.</fb:message>\n" +
                        "      " + "Am. Uh. Toor." + "\n" +
                        " </fb:error>");
                topOfPageMsg=tmp.toString();
            }
        }
    }
%>

<%String selectedTab="index";%>
<%@ include file="tabs.jsp" %>

<%
if (!topOfPageMsg.equals("")){
    %><%=topOfPageMsg%><%
}
%>

<table>
    <tr>
        <td valign="top" width="220">
            <%
            if (post!=null && post.getPostid()>0){
                %>
                <fb:success>
                <fb:message>Editing data from <%=Time.dateformatcompactwithtime(Time.getCalFromDate(post.getPostdate()))%>.</fb:message>
                <a href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=main&postid=<%=post.getPostid()%>&action=deletepost'>Delete it?</a>
                <br/><a href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=main'>Start new?</a>
                </fb:success>
                <br/>
                <%
            }
            %>
            <%
            if (userSession.getUser() != null) {
                int totalposts = NumFromUniqueResult.getInt("select count(*) from Post where userid='"+userSession.getUser().getUserid()+"' and appid='"+userSession.getApp().getAppid()+"'");
                if (totalposts==0){
                    %>
                    <fb:success>
                    <fb:message>Get Started Tracking!</fb:message>
                    Just fill out the form below and click Track It.  Fields with an asterisk* are required.
                    </fb:success>
                    <br/>
                    <%
                }
            }
            %>
            <form action="">
                <input type="hidden" name="action" value="trackit" />

                <%
                if (post!=null && post.getPostid()>0){
                    %>
                    <input type="hidden" name="postid" value="<%=post.getPostid()%>"/>
                    <%
                }
                %>

                <%
                AppTemplateProcessor atp = new AppTemplateProcessor(userSession.getApp(), userSession.getUser(), post);
                out.print(atp.getHtmlForInput(false));
                %>

                <div style="background : #ffffff; border: 0px solid #ffffff; padding : 5px; width : 220px; overflow : auto;">
                    <font class="questionfont">Notes:</font>
                    <br/>
                    <%
                    String notes = "";
                    if (post!=null && post.getPostid()>0){
                        notes = post.getNotes();
                    }
                    %>
                    <textarea cols="30" rows="3" name="<%=AppPostParser.FBDBLOG_REQUEST_PARAM_IDENTIFIER%>notes"><%=notes%></textarea>
                    <br/>
                    <input id="sendbutton" type="submit" value="Track It" />
                </div>
            </form>
        </td>
        <td valign="top" width="400">
            <img src="<%=BaseUrl.get(false)%>fb/graph.jsp?chartid=<%=userSession.getApp().getPrimarychartid()%>&userid=<%=userSession.getUser().getUserid()%>&size=small" alt="" width="400" height="250" style="border: 3px solid #e6e6e6;"/>
            <br/>
            <a href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=charts&chartid=<%=userSession.getApp().getPrimarychartid()%>'>+Zoom</a>
            |
            <a href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=charts'>All Charts</a>
            <br/><br/><br/><br/>

            <div style="border: 3px solid #e6e6e6;">
                <center><font style="font-size: 18px; color: #cccccc;">Track Other Stuff Too</font></center>
                <table cellpadding="3" cellspacing="1" border="0" width="100%" bgcolor="#efefef">
                <%


                    int col=0;
                    List<App> apps=HibernateUtil.getSession().createCriteria(App.class)
                            .add(Restrictions.eq("crosspromote", true))
                            .setCacheable(true)
                            .list();
                    for (Iterator<App> iterator=apps.iterator(); iterator.hasNext();) {
                        App app=iterator.next();
                        if (app.getAppid() != userSession.getApp().getAppid()) {
                            col=col + 1;
                            if (col == 1) {
                            %><tr><%
                            }
                                %><td valign="top"><%
                                %>
                                <a href="http://apps.facebook.com/<%=app.getFacebookappname()%>/"><%=app.getTitle()%></a>
                                <%
                                %></td><%
                            if (col==3){
                                col = 0;
                                %></tr><%
                            }
                        }
                    }
                %>
                </table>
            </div>
        </td>
    </tr>
</table>

<br/><br/>
<%
if (userSession.getIssysadmin()){
    if (request.getParameter("fb_sig_session_key")!=null){
        if (request.getParameter("fb_sig_expires")!=null && request.getParameter("fb_sig_expires").equals("0")){
            %>
            <div style="text-align: right;"><font style="font-size: 9px; color: #cccccc;">fb_sig_session_key=<%=request.getParameter("fb_sig_session_key")%></font></div>
            <%
        }
    }
}
%>
<%@ include file="footer.jsp" %>