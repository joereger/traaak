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



<br/><br/><br/>
<table cellpadding="0" cellspacing="0" border="0" width="900">
    <tr>
        <td valign="top">
            <img src="/images/clear.gif" alt="" width="25" height="1" border="0">
        </td>
        <td valign="top" width="500">
            <img src="/images/v2-choosesomething.gif" alt="" width="344" height="85"><br/>
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
        </td>
        <td valign="top">
            <img src="/images/v2-embedyourblog.gif" alt="" width="369" height="444" border="0">
        </td>
    </tr>
</table>

<br/><br/>

<%@ include file="/template/footer.jsp" %>
