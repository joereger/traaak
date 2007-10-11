<%@ page import="com.fbdblog.systemprops.InstanceProperties" %>
<fb:google-analytics uacct="UA-208946-6" page="<%=userSession.getApp().getTitle()%>(<%=userSession.getApp().getAppid()%>)" />
<br/><br/>
<font style="font-size: 9px; color: #cccccc;">Brought to you by a server called <%=InstanceProperties.getInstancename()%></font>