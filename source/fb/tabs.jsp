
<%if (Pagez.getUserSession().getIsfacebook()){%>
    <br/>
    <fb:tabs>
      <fb:tab-item href='http://apps.facebook.com/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=main' title='Track Stuff' <%if (selectedTab.equals("index")){out.print("selected='true'");}%>/>
      <fb:tab-item href='http://apps.facebook.com/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=charts' title='Charts' <%if (selectedTab.equals("charts")){out.print("selected='true'");}%>/>
      <fb:tab-item href='http://apps.facebook.com/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=reports' title='Reports' <%if (selectedTab.equals("reports")){out.print("selected='true'");}%>/>
      <fb:tab-item href='http://apps.facebook.com/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=history' title='History' <%if (selectedTab.equals("history")){out.print("selected='true'");}%>/>
      <fb:tab-item href='http://apps.facebook.com/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=settings' title='Settings' <%if (selectedTab.equals("settings")){out.print("selected='true'");}%>/>
      <fb:tab-item href='http://apps.facebook.com/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=help' title='Help' <%if (selectedTab.equals("help")){out.print("selected='true'");}%>/>
      <fb:tab-item href='http://apps.facebook.com/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=friends' title='Friends' align='right' <%if (selectedTab.equals("friends")){out.print("selected='true'");}%>/>
      <fb:tab-item href='http://apps.facebook.com/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=throwdowns' title='Throwdown!!!' align='right' <%if (selectedTab.equals("throwdown")){out.print("selected='true'");}%>/>
    </fb:tabs>
    <br/>
<%} else {%>
    <%--<br/>--%>
      <%--<a href='/app/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=main'>Track Stuff</a>--%>
      <%--<a href='/app/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=charts'>Charts</a>--%>
      <%--<a href='/app/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=reports'>Reports</a>--%>
      <%--<a href='/app/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=history'>History</a>--%>
      <%--<a href='/app/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=settings'>Settings</a>--%>
      <%--<a href='/app/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=help'>Help</a>--%>
    <%--<br/>--%>

<div style="float:right;">All Traaaks | Traaaks You've Used | <a href="/app/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=settings">Settings</a> | <a href="/app/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=help">Help</a> | Log Out</div>
<div class="navbarmain">
<div id="navbar">
<span class="inbar">
    <ul>
        <li class="navhome"><a href="/app/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=main"><span><%=Pagez.getUserSession().getApp().getTitle()%></span></a></li>
        <li><a href="/app/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=charts"><span>Charts</span></a></li>
        <li><a href="/app/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=reports"><span>Reports</span></a></li>
        <li><a href="/app/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=history"><span>History</span></a></li>
    </ul>
</span>
</div>
</div>
<br/><br/>
<%}%>