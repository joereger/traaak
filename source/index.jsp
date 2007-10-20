<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="java.util.List" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.fbdblog.systemprops.BaseUrl" %>

<br/><br/><br/><br/>
<center>
<table cellpadding="0" cellspacing="0" border="0" width="400">
    <tr>
        <td valign="top">
            <img src="<%=BaseUrl.get(false)%>images/logo-128.png" alt="" width="128" height="128"/>
        </td>
        <td valign="top">
            <font style="font-size: 35px; color: #666666; font-family: arial;"><b>Track Apps</b></font>
            <br/>
            <font style="font-size: 12px; color: #cccccc; font-family: arial;"><b>Powered by</b></font>
            <br/>
            <font style="font-size: 18px; color: #cccccc; font-family: arial;"><b>Reger.com and dNeero.com</b></font>
        </td>
    </tr>
    <tr>
        <td valign="top">

        </td>
        <td valign="top">
            <font style="font-size: 12px; color: #000000; font-family: arial;">
            <b>We run numerous Facebook applications that allow Facebookers to track things.</b>  Body weight, movies watched, biorythms, miles run, etc.  This concept is an extension of reger.com's datablogging.
            <br/><br/>
            <b>Please contact us for advertising/sponsorship opportunities.</b>  You can take over an app that's relevant to your business.  Or we can create a custom app just for you... and then you can take it over.
            <br/>
            <a href="mailto:joe@joereger.com">joe.reger@dneero.com</a>
            <br/><br/>
            </font>
        </td>
    </tr>
    <tr>
        <td valign="top">

        </td>
        <td valign="top">
            <font style="font-size: 22px; color: #cccccc; font-family: arial;"><b>The Apps</b></font>
            <font style="font-size: 12px; color: #000000; font-family: arial;">
            <%
                List<App> apps=HibernateUtil.getSession().createCriteria(App.class)
                        .setCacheable(true)
                        .list();
                for (Iterator<App> iterator=apps.iterator(); iterator.hasNext();) {
                    App app= iterator.next();
                    %>
                        <br/>&nbsp;&nbsp;<a href="http://www.facebook.com/add.php?api_key=<%=app.getFacebookapikey()%>"><%=app.getTitle()%></a>
                    <%
                }
            %>
            </font>
        </td>
    </tr>
</table>
</center>
<br/><br/>
