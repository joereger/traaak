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
<%@ page import="com.fbdblog.chart.ChartSecurityKey" %>
<%@ page import="org.hibernate.criterion.Order" %>

<%
Logger logger = Logger.getLogger(this.getClass().getName());
logger.error("top of index.jsp");
String pagetitle = "Traaak";
String navtab = "youraccount";
String acl = "public";
%>
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
String adPostSave = "";
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("trackit")) {
        AppPostParser appPostParser = new AppPostParser(request);
        try {
            SavePost.save(Pagez.getUserSession().getApp(), Pagez.getUserSession().getUser(), post, appPostParser, Pagez.getUserSession());
            StringBuffer tmp = new StringBuffer();
            if (Pagez.getUserSession().getIsfacebook()){
                tmp.append("<fb:success>\n" +
                "     <fb:message>Nice trackin'.  Now track s'more.</fb:message>\n" +
                "     We've updated your profile so that others can check out your stuff.  Now's a good time to check out <a href='http://apps.facebook.com/"+Pagez.getUserSession().getApp().getFacebookappname()+"/?nav=friends'>your friends' stuff</a>.\n" +
                "</fb:success>");
            } else {
                tmp.append("<div class=\"traaakbox\">\n" +
                "   <div class=\"traaakboxtitle\">Nice traaaking!</div>\n" +
                " We've updated your charts.  Have you <a href=\"/app/"+Pagez.getUserSession().getApp().getFacebookappname()+"/?nav=charts\">embedded charts</a> into your blog/website yet?  Why not traaak some <a href=\"/\">other stuff</a>?"+
                "</div>");
            }
            topOfPageMsg = tmp.toString();
            StringBuffer tmpAps = new StringBuffer();
            if (!Pagez.getUserSession().getApp().getAdpostsave().equals("")){
                tmpAps.append("<br/>");
                tmpAps.append("<center>");
                tmpAps.append(Pagez.getUserSession().getApp().getAdpostsave());
                tmpAps.append("</center>");
                tmpAps.append("<br/>");
            }
            adPostSave = tmpAps.toString();
        } catch (ComponentException cex) {
            StringBuffer tmp = new StringBuffer();
            if (Pagez.getUserSession().getIsfacebook()){
                tmp.append(" <fb:error>\n" +
                "      <fb:message>Houston, we have a problem.</fb:message>\n" +
                "      "+cex.getErrorsAsSingleString()+"\n" +
                " </fb:error>");
            } else {
                tmp.append("<div class=\"traaakbox\">\n" +
                "   <div class=\"traaakboxtitle\">Houston, we have a problem.</div>\n" +
                cex.getErrorsAsSingleString() +
                "</div>");
            }
            topOfPageMsg = tmp.toString();
        }
    }
%>

<%
if (Pagez.getUserSession().getIsnewappforthisuser() && Pagez.getUserSession().getIsfacebook()){
    StringBuffer tmp = new StringBuffer();
    if (!Pagez.getUserSession().getApp().getIsdefaultprivate()){
        tmp.append("<fb:success>\n" +
        "     <fb:message>Howdy!</fb:message>\n" +
        "     Glad you've added "+Pagez.getUserSession().getApp().getTitle()+".  Track some stuff and get some charts.  Just realize that everything you track is public unless you change your <a href='http://apps.facebook.com/"+Pagez.getUserSession().getApp().getFacebookappname()+"/?nav=settings'>settings</a>.\n" +
        "</fb:success>");
    } else {
        tmp.append("<fb:success>\n" +
        "     <fb:message>Howdy!</fb:message>\n" +
        "     Glad you've added "+Pagez.getUserSession().getApp().getTitle()+".  Track some stuff and get some charts.  Everything you track is private unless you change your <a href='http://apps.facebook.com/"+Pagez.getUserSession().getApp().getFacebookappname()+"/?nav=settings'>settings</a>.\n" +
        "</fb:success>");
    }
    topOfPageMsg = tmp.toString();
}
%>

<%
    if (!Pagez.getUserSession().getIsloggedin() && !Pagez.getUserSession().getIsfacebook()){
        StringBuffer tmp=new StringBuffer();
        tmp.append("<div class=\"traaakbox\">\n" +
                    "   <div class=\"traaakboxtitle\">"+Pagez.getUserSession().getApp().getTitle()+"</div>\n" +
                    "" +  Pagez.getUserSession().getApp().getDescription() +
                    "</div>");
    }
%>


<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("deletepost")) {
        if (request.getParameter("postid") != null && Num.isinteger(request.getParameter("postid"))) {
            Post postToDel=Post.get(Integer.parseInt(request.getParameter("postid")));
            if (postToDel.getUserid() == Pagez.getUserSession().getUser().getUserid()) {
                try {
                    postToDel.delete();
                } catch (Exception ex) {
                    logger.error("", ex);
                }
                post=null;
                StringBuffer tmp=new StringBuffer();
                if (Pagez.getUserSession().getIsfacebook()){
                    tmp.append("<fb:success>\n" +
                        "     <fb:message>As ordered, your data has been deleted.</fb:message>\n" +
                        "     Now add some more so this app doesn't get an inferiority complex.  It's lonely.  It needs data.\n" +
                        "</fb:success>");
                } else {
                    tmp.append("<div class=\"traaakbox\">\n" +
                    "   <div class=\"traaakboxtitle\">As ordered, your data has been deleted.</div>\n" +
                    "Now add some more so this app doesn't get an inferiority complex.  It's lonely.  It needs data." +
                    "</div>");
                }
                topOfPageMsg=tmp.toString();
                //Clear the chart image cache
                ClearCache.clearCacheForUser(Pagez.getUserSession().getUser().getUserid(), Pagez.getUserSession().getApp().getAppid());
                //Do Calculations
                DoCalculationsAfterPost.doCalculations(Pagez.getUserSession().getUser(), Pagez.getUserSession().getApp());
            } else {
                StringBuffer tmp=new StringBuffer();
                if (Pagez.getUserSession().getIsfacebook()){
                    tmp.append(" <fb:error>\n" +
                        "      <fb:message>Seriously? You don't even own that post.</fb:message>\n" +
                        "      " + "Am. Uh. Toor." + "\n" +
                        " </fb:error>");
                } else {
                    tmp.append("<div class=\"traaakbox\">\n" +
                    "   <div class=\"traaakboxtitle\">Seriously? You don't even own that post.</div>\n" +
                    "Am. Uh. Toor." +
                    "</div>");
                }
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
        <td valign="top" width="320">
            <%
            if (!adPostSave.equals("")){
                %><%=adPostSave%><%
            }
            %>
            <%
            if (post!=null && post.getPostid()>0){
                if (Pagez.getUserSession().getIsfacebook()){
                    %>
                    <fb:success>
                    <fb:message>Editing data from <%=Time.dateformatcompactwithtime(Time.gmttousertime(post.getPostdate(), Pagez.getUserSession().getUser().getTimezoneid()))%>.</fb:message>
                    <br/><a href='http://apps.facebook.com/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=main&postid=<%=post.getPostid()%>&action=deletepost'>Delete it?</a>
                    <br/><a href='http://apps.facebook.com/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=main'>Start new?</a>
                    </fb:success>
                    <br/>
                    <%
                } else {
                    %>
                    <div class="traaakbox">
                        <div class="traaakboxtitle">Editing data from <%=Time.dateformatcompactwithtime(Time.gmttousertime(post.getPostdate(), Pagez.getUserSession().getUser().getTimezoneid()))%>.</div>
                        <a href='/app/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=main&postid=<%=post.getPostid()%>&action=deletepost'>Delete it?</a>
                        <br/><a href='/app/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=main'>Start new?</a>
                    </div>
                    <%
                }
            }
            %>

            <%
            if (Pagez.getUserSession().getUser()!=null) {
                int totalposts = NumFromUniqueResult.getInt("select count(*) from Post where userid='"+Pagez.getUserSession().getUser().getUserid()+"' and appid='"+Pagez.getUserSession().getApp().getAppid()+"'");
                if (totalposts==0){
                    if (Pagez.getUserSession().getIsfacebook()){
                        %>
                        <fb:success>
                        <fb:message>Get Started Tracking!</fb:message>
                        Just fill out the form below and click Traaak It!  Fields with an asterisk* are required.
                        </fb:success>
                        <br/>
                        <%
                    } else {
                        %>
                        <div class="traaakbox">
                            <div class="traaakboxtitle">Get Started Tracking!</div>
                            Just fill out the form below and click Traaak It!  Fields with an asterisk* are required.
                        </div>
                        <%
                    }
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
                AppTemplateProcessor atp = new AppTemplateProcessor(Pagez.getUserSession().getApp(), Pagez.getUserSession().getUser(), post);
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
                    <%if (Pagez.getUserSession().getIsloggedin()){%>
                        <br/><br/>
                        <input id="sendbutton" type="submit" value="Traaak It!" />
                    <%}%>
                </div>
                <%
                if (Pagez.getUserSession().getIsloggedin()){
                    if (Pagez.getUserSession().getUserappsettings()!=null && Pagez.getUserSession().getUserappsettings().getIsprivate()){
                        if (Pagez.getUserSession().getIsfacebook()){
                            %>
                            <fb:success>
                            <fb:message>Private Mode</fb:message>
                            Your friends won't be able to see your stuff. You can make stuff public <a href='http://apps.facebook.com/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=settings'>over here</a>.
                            </fb:success>
                            <br/>
                            <%
                        } else {
                            %>
                            <div class="traaakbox">
                                <div class="traaakboxtitle">Private Mode</div>
                                Nobody'll be able to see your stuff. You can make it public <a href='/app/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=settings'>over here</a>.
                            </div>
                            <%
                        }
                    } else {
                        if (Pagez.getUserSession().getIsfacebook()){
                            %>
                            <fb:success>
                            <fb:message>Public Mode</fb:message>
                            Everything you post is visible to friends. Make stuff private <a href='http://apps.facebook.com/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=settings'>over here</a>.
                            </fb:success>
                            <br/>
                            <%
                        } else {
                            %>
                            <div class="traaakbox">
                                <div class="traaakboxtitle">Public Mode</div>
                                Everything you track is publicly visible. You can make it private <a href='/app/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=settings'>over here</a>.
                            </div>
                            <%
                        }
                    }
                } else {
                    if (Pagez.getUserSession().getIsfacebook()){

                    } else {
                        %>
                        <div class="traaakbox">
                            <div class="traaakboxtitle">Free Login/Signup</div>
                            Traaak requires a Facebook account.  Once you "Connect with Facebook" you'll be able to save this data and create your own charts/graphs.  You can keep your data private or share it with the world.


                        <fb:login-button length="long" size="large" onlogin="facebook_onlogin();"></fb:login-button>
                        <script type="text/javascript">
                           function facebook_onlogin(){
                              FB.Connect.ifUserConnected("/app/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/",null);
                            }
                        </script>
                        <br/><br/>
                        </div>
                        <%
                    }
                }
                %>
            </form>
        </td>
        <td valign="top" width="400">
            <%
            int useridForChartKey = 0;
            if (Pagez.getUserSession().getUser()!=null){useridForChartKey=Pagez.getUserSession().getUser().getUserid();}
            String key=ChartSecurityKey.getChartKey(useridForChartKey, Pagez.getUserSession().getApp().getPrimarychartid());
            %>
            <img src="<%=BaseUrl.get(false)%>fb/graph.jsp?chartid=<%=Pagez.getUserSession().getApp().getPrimarychartid()%>&userid=<%=useridForChartKey%>&size=small&key=<%=key%>" alt="" width="400" height="250" style="border: 3px solid #e6e6e6;"/>
            <br/>
            <%if (Pagez.getUserSession().getIsfacebook()){%>
                <a href='http://apps.facebook.com/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=charts&chartid=<%=Pagez.getUserSession().getApp().getPrimarychartid()%>'>+Zoom</a>
                |
                <a href='http://apps.facebook.com/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=charts'>All Charts</a>
            <%} else {%>
                <a href='/app/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=charts&chartid=<%=Pagez.getUserSession().getApp().getPrimarychartid()%>'>+Zoom</a>
                |
                <a href='/app/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=charts'>All Charts</a>
            <%}%>
            <br/><br/><br/><br/>

            <div style="border: 3px solid #e6e6e6;">
                <center><font style="font-size: 18px; color: #cccccc;">Track Other Stuff Too</font></center>
                <table cellpadding="3" cellspacing="1" border="0" width="100%" bgcolor="#efefef">
                <%
                    int col=0;
                    int numOfCols = 3;
                    List<App> apps=HibernateUtil.getSession().createCriteria(App.class)
                            .add(Restrictions.eq("crosspromote", true))
                            .addOrder(Order.asc("title"))
                            .setCacheable(true)
                            .list();
                    for (Iterator<App> iterator=apps.iterator(); iterator.hasNext();) {
                        App app=iterator.next();
                        if (app.getAppid() != Pagez.getUserSession().getApp().getAppid()) {
                            col=col + 1;
                            if (col == 1) {
                                %><tr><%
                            }
                                %><td valign="top"><%
                                %>
                                <a href="<%=linkToUrl%><%=app.getFacebookappname()%>/"><%=app.getTitle()%></a>
                                <%
                                %></td><%
                            if (col==numOfCols){
                                col = 0;
                                %></tr><%
                            }
                        }
                    }
                %>
                </table>
            </div>
            <div style="text-align: center;"><font style="font-size: 10px;">Don't see what you want?  <a href="<%=linkToUrl%><%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=help">Tell us what you'd like to track.</a></font></div>
        </td>
    </tr>
</table>

<br/><br/>
<%
if (Pagez.getUserSession().getIssysadmin()){
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