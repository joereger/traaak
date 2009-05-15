<%@ page import="com.fbdblog.systemprops.InstanceProperties" %>
<%@ page import="com.fbdblog.htmlui.Pagez" %>

<br/>
<div style="text-align: right;"><font style="font-size: 9px; color: #cccccc;">Brought to you by a server called <%=InstanceProperties.getInstancename()%> in <%=Pagez.getElapsedTime()%> milliseconds</font></div>

