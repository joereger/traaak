

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
    <font class="largefont" style="color: #c6c6c6;"><%=Pagez.getUserSession().getApp().getTitle()%></font>
    <br/>
    <%if (!Pagez.getUserSession().getIsloggedin()){%>
        <font class="smallfont" style="color:#cccccc;"><%=Pagez.getUserSession().getApp().getDescription()%></font>
        <br/><br/>  
    <%}%>

    <%if (Pagez.getUserSession().getIsloggedin()){%>
        <%
            boolean displayTabs = true;
            if (selectedTab.equals("home")){displayTabs=false;}
        %>
        <%if (displayTabs){%>
            <div class="navbarmain">
            <div id="navbar">
            <span class="inbar">
                <ul>
                    <%String navbarclass="";%>
                    <%if (selectedTab.equals("index")){navbarclass=" class=\"navhome\"";}else{navbarclass="";}%>
                    <li <%=navbarclass%>><a href="/app/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=main"><span><%=Pagez.getUserSession().getApp().getTitle()%> Main</span></a></li>
                    <%if (selectedTab.equals("charts")){navbarclass=" class=\"navhome\"";}else{navbarclass="";}%>
                    <li <%=navbarclass%>><a href="/app/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=charts"><span>Charts</span></a></li>
                    <%if (selectedTab.equals("reports")){navbarclass=" class=\"navhome\"";}else{navbarclass="";}%>
                    <li <%=navbarclass%>><a href="/app/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=reports"><span>Reports</span></a></li>
                    <%if (selectedTab.equals("history")){navbarclass=" class=\"navhome\"";}else{navbarclass="";}%>
                    <li <%=navbarclass%>><a href="/app/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=history"><span>History</span></a></li>
                    <%if (selectedTab.equals("settings")){navbarclass=" class=\"navhome\"";}else{navbarclass="";}%>
                    <li <%=navbarclass%>><a href="/app/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=settings"><span>Prefs</span></a></li>
                    <%if (selectedTab.equals("help")){navbarclass=" class=\"navhome\"";}else{navbarclass="";}%>
                    <li <%=navbarclass%>><a href="/app/<%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=help"><span>Help</span></a></li>
                </ul>
            </span>
            </div>
            </div>
            <br/><br/>
        <%}%>
    <%}%>
<%}%>