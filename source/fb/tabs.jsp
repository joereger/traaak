<br/>
<fb:tabs>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=main' title='Track Stuff' <%if (selectedTab.equals("index")){out.print("selected='true'");}%>/>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=charts' title='Da Charts' <%if (selectedTab.equals("charts")){out.print("selected='true'");}%>/>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=reports' title='Da Reports' <%if (selectedTab.equals("reports")){out.print("selected='true'");}%>/>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=history' title='Yo History' <%if (selectedTab.equals("history")){out.print("selected='true'");}%>/>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=settings' title='Settings' <%if (selectedTab.equals("settings")){out.print("selected='true'");}%>/>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=friends' title='Le Friends' align='right' <%if (selectedTab.equals("friends")){out.print("selected='true'");}%>/>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=throwdowns' title='Throwdown!!!' align='right' <%if (selectedTab.equals("throwdown")){out.print("selected='true'");}%>/>
</fb:tabs>
<br/>