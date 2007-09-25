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
            SavePost.save(userSession.getApp(), userSession.getUser(), post, appPostParser);
            out.print("<fb:success>\n" +
            "     <fb:message>Success!  Data tracked!</fb:message>\n" +
            "     We've also updated your profile so that others can check you out.\n" +
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
    //Load a chart
    //@todo Define a chart as the primary chart for the app
    int chartid = 0;
    List<Chart> charts=HibernateUtil.getSession().createCriteria(Chart.class)
            .add(Restrictions.eq("appid", userSession.getApp().getAppid()))
            .setCacheable(true)
            .list();
    for (Iterator<Chart> iterator=charts.iterator(); iterator.hasNext();) {
        Chart chart= iterator.next();
        chartid = chart.getChartid();
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
                    <textarea cols="30" rows="3" name="<%=AppPostParser.FBDBLOG_REQUEST_PARAM_IDENTIFIER%>notes"></textarea>
                    <br/>
                    <input id="sendbutton" type="submit" value="Track It" />
                </div>
            </form>
        </td>
        <td valign="top" width="400">
            <img src="<%=BaseUrl.get(false)%>fb/graph.jsp?chartid=<%=chartid%>&userid=<%=userSession.getUser().getUserid()%>&size=small&comparetouserid=0" alt="" width="400" height="250" style="border: 3px solid #e6e6e6;"/>
            <br/>
            <a href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=charts&chartid=<%=chartid%>'>Zoom</a>
            <br/>
        </td>
    </tr>
</table>

<br/><br/>