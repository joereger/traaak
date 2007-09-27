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
<%@ include file="header.jsp" %>

<%
    //Load the post requested
    Post post=null;
    if (request.getParameter("postid") != null && Num.isinteger(request.getParameter("postid"))) {
        post=Post.get(Integer.parseInt(request.getParameter("postid")));
    }
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("trackit")) {
        AppPostParser appPostParser = new AppPostParser(request);
        try {
            SavePost.save(userSession.getApp(), userSession.getUser(), post, appPostParser, userSession);
            out.print("<fb:success>\n" +
            "     <fb:message>Good trackin'.  Now track some more.</fb:message>\n" +
            "     We've updated your profile so that others can check out your data.\n" +
            "</fb:success>");
        } catch (ComponentException cex) {
            out.print(" <fb:error>\n" +
            "      <fb:message>Houston, we have a problem.</fb:message>\n" +
            "      "+cex.getErrorsAsSingleString()+"\n" +
            " </fb:error>");
        }
    }
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("compare")) {
        //@todo implement compare
        logger.debug("compare called");
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
                    logger.error(ex);
                }
                post=null;
                out.print("<fb:success>\n" +
                        "     <fb:message>As ordered, thy data hath been deleted.</fb:message>\n" +
                        "     Now add some more so this app doesn't get an inferiority complex.  It's lonely.  It needs data.\n" +
                        "</fb:success>");
                //Clear the chart image cache
                ClearCache.clearCacheForUser(userSession.getUser().getUserid(), userSession.getApp().getAppid());
            } else {
                out.print(" <fb:error>\n" +
                        "      <fb:message>Seriously? You don't even own that post.</fb:message>\n" +
                        "      " + "Am. Uh. Toor." + "\n" +
                        " </fb:error>");
            }
        }
    }
%>


<br/>
<fb:tabs>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=main' title='Track Stuff' selected='true'/>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=charts' title='Da Charts' />
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=history' title='Yo History' />
</fb:tabs>
<br/>

<table>
    <tr>
        <td valign="top" width="220">
            <%
            if (post!=null && post.getPostid()>0){
                %>
                <font size=-2>
                Editing data from <%=Time.dateformatcompactwithtime(Time.getCalFromDate(post.getPostdate()))%>.
                <br/><a href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=main&postid=<%=post.getPostid()%>&action=deletepost'>Delete it?</a>
                <br/><a href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=main'>Start a new one?</a>
                </font>
                <br/>
                <%
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
            <img src="<%=BaseUrl.get(false)%>fb/graph.jsp?chartid=<%=userSession.getApp().getPrimarychartid()%>&userid=<%=userSession.getUser().getUserid()%>&size=small&comparetouserid=0" alt="" width="400" height="250" style="border: 3px solid #e6e6e6;"/>
            <br/>
            <a href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=charts&chartid=<%=userSession.getApp().getPrimarychartid()%>'>+Zoom</a>
            <br/>
        </td>
    </tr>
</table>

<br/><br/>