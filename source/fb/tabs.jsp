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