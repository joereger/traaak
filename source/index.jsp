<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="java.util.List" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.fbdblog.systemprops.BaseUrl" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Traaak: Social App to track things that're important to you.";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>



<br/>
<table cellpadding="0" cellspacing="0" border="0" width="900">
    <tr>
        <td valign="top" width="400">
            <font style="font-size: 22px; color: #cccccc; font-family: arial;"><b>what do you want to traaak?</b></font>
            <table cellpadding="0" cellspacing="10" border="0" width="100%">
            <%
                List<App> apps=HibernateUtil.getSession().createCriteria(App.class)
                        .add(Restrictions.eq("crosspromote", true))
                        .addOrder(Order.asc("title"))
                        .setCacheable(true)
                        .list();
                int numOfCols = 4;
                int col = 1;
                for (Iterator<App> iterator=apps.iterator(); iterator.hasNext();) {
                    App app=iterator.next();
                    %>
                        <%if (col==1){%><tr><%}%>
                            <td valign="top">
                                <font class="normalfont" style="font-weight: bold;"><a href="/app/<%=app.getFacebookappname()%>/"><%=app.getTitle()%></a></font>
                            </td>
                        <%if (col==numOfCols){%></tr><%}%>
                    <%
                    col = col + 1;
                    if (col>numOfCols){ col = 1;}
                }
            %>
            </table>
        </td>
        <td valign="top">
            <%if (!Pagez.getUserSession().getIsloggedin()){%>
                <a href="/login.jsp">Log In</a> or <a href="/registration.jsp">Sign Up</a> 
            <%} else { %>
                <a href="/login.jsp?action=logout">Log Out</a>
            <%}%>
        </td>
    </tr>
</table>

<br/><br/>

<%@ include file="/template/footer.jsp" %>
