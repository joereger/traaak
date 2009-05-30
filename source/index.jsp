<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="java.util.List" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.fbdblog.systemprops.BaseUrl" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="com.fbdblog.dao.User" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Traaak: Social App to track things that're important to you.";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>



<br/><br/><br/>
<table cellpadding="0" cellspacing="0" border="0" width="900">
    <tr>
        <td valign="top">
            <img src="/images/clear.gif" alt="" width="25" height="1" border="0">
        </td>
        <td valign="top" width="500">

        
            <img src="/images/v2-choosesomething_sm.gif" alt="" width="344" height="46"><br/>
            <table cellpadding="0" cellspacing="10" border="0" width="100%">
            <%
                List<App> apps=HibernateUtil.getSession().createCriteria(App.class)
                        .add(Restrictions.eq("crosspromote", true))
                        .addOrder(Order.asc("title"))
                        .setCacheable(true)
                        .list();
                int numOfCols = 3;
                int col = 1;
                for (Iterator<App> iterator=apps.iterator(); iterator.hasNext();) {
                    App app=iterator.next();
                    %>
                        <%if (col==1){%><tr><%}%>
                            <td valign="top">
                                <font class="normalfont" style="font-weight: bold; font-size: 14px;"><a href="/app/<%=app.getFacebookappname()%>/"><%=app.getTitle()%></a></font>
                            </td>
                        <%if (col==numOfCols){%></tr><%}%>
                    <%
                    col = col + 1;
                    if (col>numOfCols){ col = 1;}
                }
            %>
            </table>

            <br/><br/>
            <img src="/images/v2-whostraaaking.gif" alt="" width="344" height="45"><br/>
            <table cellpadding="0" cellspacing="10" border="0" width="100%">
            <%
                List<User> users=HibernateUtil.getSession().createCriteria(User.class)
                        .add(Restrictions.eq("isenabled", true))
                        .addOrder(Order.desc("userid"))
                        .setCacheable(true)
                        .setMaxResults(55)
                        .list();
                int numOfColsUsers = 4;
                int colUsers = 1;
                for (Iterator<User> iterator=users.iterator(); iterator.hasNext();) {
                    User user = iterator.next();
                    %>
                        <%if (colUsers==1){%><tr><%}%>
                            <td valign="top">
                                <font class="normalfont" style="font-weight: bold; font-size: 14px;"><a href="/user/<%=user.getNickname()%>/"><%=user.getNickname()%></a></font>
                            </td>
                        <%if (colUsers==numOfColsUsers){%></tr><%}%>
                    <%
                    colUsers = colUsers + 1;
                    if (colUsers>numOfColsUsers){ colUsers = 1;}
                }
            %>
            </table>


            <br/><br/>
            <blockquote>
            <%--<img src="/images/v2-asseeninself.gif" alt="" width="344" height="45"><br/>--%>
            <img src="/images/selfmag.jpg" alt="" width="92" height="124" align="left">
            <font class="smallfont">We couldn't be happier that SELF Magazine included <a href="/app/trackcaffeine/">TrackCaffeine</a> in its April 2009 issue.</a></font>
            </blockquote>


        </td>
        <td valign="top">
            <img src="/images/v2-embedyourblog.gif" alt="" width="369" height="444" border="0">
        </td>
    </tr>
</table>

<br/><br/>

<%@ include file="/template/footer.jsp" %>
