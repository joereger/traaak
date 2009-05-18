<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="java.util.List" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.fbdblog.systemprops.BaseUrl" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Log In";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>




<center>
<table cellpadding="0" cellspacing="0" border="0" width="400">


    <tr>
        <td valign="top">
            <center>
            <font style="font-size: 22px; color: #cccccc; font-family: arial;"><b>what do you want to traaak?</b></font>
            </center>
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
    </tr>
</table>
</center>
<br/><br/>

<%@ include file="/template/footer.jsp" %>
