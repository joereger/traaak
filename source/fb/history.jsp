<%@ page import="com.fbdblog.qtype.util.AppTemplateProcessor" %>
<%@ page import="com.fbdblog.qtype.util.AppPostParser" %>
<%@ page import="com.fbdblog.qtype.util.SavePost" %>
<%@ page import="com.fbdblog.qtype.def.ComponentException" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="com.fbdblog.dao.Post" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.fbdblog.util.Time" %>

<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "History";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="header.jsp" %>

<%String selectedTab="history";%>
<%@ include file="tabs.jsp" %>

<fb:success>
<fb:message>A List of Your Old Stuff</fb:message>
This is a list of all of the stuff you've tracked, organized by date.  You can edit and delete stuff using this screen.
</fb:success>
<br/>

<br/>
<div style="padding: 10px;">
<table cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td valign="top" width="390">
            <table cellpadding="3" cellspacing="0" border="0">
            <%
                List<Post> posts=HibernateUtil.getSession().createCriteria(Post.class)
                        .add(Restrictions.eq("appid", Pagez.getUserSession().getApp().getAppid()))
                        .add(Restrictions.eq("userid", Pagez.getUserSession().getUser().getUserid()))
                        .addOrder(Order.desc("postdate"))
                        .setCacheable(true)
                        .list();
                for (Iterator<Post> iterator=posts.iterator(); iterator.hasNext();) {
                    Post post= iterator.next();
                    %>
                    <tr>
                        <td valign="top">
                            <font size="-1">
                            <a href='<%=linkToUrl%><%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=main&postid=<%=post.getPostid()%>'><%=Time.dateformatcompactwithtime(Time.gmttousertime(post.getPostdate(), Pagez.getUserSession().getUser().getTimezoneid()))%></a>
                            </font>
                        </td>
                        <td valign="top">

                        </td>
                    </tr>
                    <%
                }
            %>
            </table>
        </td>
        <td valign="top" width="250">
            <%=Pagez.getUserSession().getApp().getAdhistoryright()%>
        </td>
    </tr>
</table>
</div>
<br/><br/>
<%@ include file="footer.jsp" %>