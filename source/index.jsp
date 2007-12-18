<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="java.util.List" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.fbdblog.systemprops.BaseUrl" %>
<%@ page import="org.hibernate.criterion.Order" %>

<br/><br/><br/><br/>
<center>
<table cellpadding="0" cellspacing="0" border="0" width="400">
    <tr>
        <td valign="top">
            <img src="<%=BaseUrl.get(false)%>images/logo-128.png" alt="" width="128" height="128"/>
        </td>
        <td valign="top">
            <font style="font-size: 55px; color: #666666; font-family: arial;"><b>traaak.com</b></font>
            <br/>
            <font style="font-size: 18px; color: #cccccc; font-family: arial;"><b>yes, that's three a's</b></font>
            <br/>
            <font style="font-size: 14px; color: #cccccc; font-family: arial;"><b>by Joe Reger, Jr.</b></font>
        </td>
    </tr>
    <tr>
        <td valign="top">

        </td>
        <td valign="top">
            <font style="font-size: 12px; color: #000000; font-family: arial;">
            <b>Applications that allow Facebookers to track stuff.</b>  Body weight, biorythms, sleep time, study time, body fat percentage, water consumed per day, miles run, hours of exercise, etc.  After tracking stuff they get charts and graphs which they can then compare to their friends.  Throwdowns allow people to have mano-a-mano to-the-death data duels to see who can achieve a challenge metric on a given date (most weight lost, most miles run, fewest pimples, etc.)
            <br/><br/>
            <b>I'm adding applications all the time</b> to create the largest network of its kind... bwah haha... bwwwwwaaaaaawwwww haaaaaahaaaaa.... bwwwwwwwwwwwwwwwaaaaaaaawwwwwwww haaaaaaaaaaaaaaahaaaaaaaaaaa!
            <br/><br/>
            <b>Shoot me an email for advertising/sponsorship opportunities.</b>  You can advertise on an app that's relevant to your business.  Or I can create a custom app just for you... and then you can advertise on it. We have a number of different ad units ranging in size from banners to half pages.
            <br/>
            <a href="mailto:joe.reger@dneero.com">joe.reger@dneero.com</a>
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
                        .add(Restrictions.eq("crosspromote", true))
                        .addOrder(Order.asc("title"))
                        .setCacheable(true)
                        .list();
                for (Iterator<App> iterator=apps.iterator(); iterator.hasNext();) {
                    App app=iterator.next();
                    %>
                        <br/>&nbsp;&nbsp;<a href="http://apps.facebook.com/<%=app.getFacebookappname()%>/"><%=app.getTitle()%></a>
                    <%
                }
            %>
            </font>
        </td>
    </tr>
</table>
</center>
<br/><br/>
