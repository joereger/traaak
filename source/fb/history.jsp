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
<%@ include file="header.jsp" %>

<br/>
<fb:tabs>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=main' title='Track Stuff'/>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=charts' title='Da Charts' />
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=history' title='Yo History' selected='true'/>
</fb:tabs>
<br/>

<br/>
<table cellpadding="3" cellspacing="0" border="0">
<%
    List<Post> posts=HibernateUtil.getSession().createCriteria(Post.class)
            .add(Restrictions.eq("appid", userSession.getApp().getAppid()))
            .add(Restrictions.eq("userid", userSession.getUser().getUserid()))
            .addOrder(Order.desc("postdate"))
            .setCacheable(true)
            .list();
    for (Iterator<Post> iterator=posts.iterator(); iterator.hasNext();) {
        Post post= iterator.next();
        %>
        <tr>
            <td valign="top">
                <font size="-1">
                <a href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=main&postid=<%=post.getPostid()%>'><%=Time.dateformatcompactwithtime(Time.getCalFromDate(post.getPostdate()))%></a>
                </font>
                <%
                if (post.getNotes()!=null && post.getNotes().length()>0){
                    %>
                    <br/>
                    <font size=-2><%=post.getNotes()%></font>
                    <%
                }
                %>
            </td>
            <td valign="top">

            </td>
        </tr>
        <%
    }
%>
</table>

<br/><br/>