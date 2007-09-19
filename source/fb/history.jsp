<%@ page import="com.fbdblog.qtype.util.AppTemplateProcessor" %>
<%@ page import="com.fbdblog.qtype.util.AppPostParser" %>
<%@ page import="com.fbdblog.qtype.util.SavePost" %>
<%@ page import="com.fbdblog.qtype.def.ComponentException" %>
<%@ include file="header.jsp" %>

<br/>
<fb:tabs>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=main' title='Track Stuff'/>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=charts' title='Cool Charts' />
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=history' title='Yo History' selected='true'/>
</fb:tabs>
<br/>

History

<br/><br/>