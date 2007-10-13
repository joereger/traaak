<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="java.util.List" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="java.util.Iterator" %>
<font style="font-size: 35px; color: #666666; font-family: arial;"><b>Track Apps</b></font>
<br/>
<font style="font-size: 18px; color: #666666; font-family: arial;"><b>Powered by Reger.com and dNeero.com</b></font>
<br/><br/>
<font style="font-size: 12px; color: #000000; font-family: arial;">
We run numerous Facebook applications that allow Facebookers to track things.  Body weight, movies watched, biorythms, miles run, etc.  This concept is an extension of reger.com's datablogging.
<br/><br/>
Please contact us for advertising/sponsorship opportunities.  You can take over an app that's relevant to your business.  Or we can create a custom app just for you... and then you can take it over.
<br/><br/>
joe.reger@dneero.com
</font>

<br/><br/>
<font style="font-size: 18px; color: #666666; font-family: arial;"><b>The Apps</b></font>
<font style="font-size: 12px; color: #000000; font-family: arial;">
<%
    List<App> apps=HibernateUtil.getSession().createCriteria(App.class)
            .setCacheable(true)
            .list();
    for (Iterator<App> iterator=apps.iterator(); iterator.hasNext();) {
        App app= iterator.next();
        %>
            <br/><a href="http://www.facebook.com/add.php?api_key=<%=app.getFacebookapikey()%>"><%=app.getTitle()%></a>
        <%
    }
%>
</font>