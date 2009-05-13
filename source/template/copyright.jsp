<%@ page import="com.fbdblog.htmlui.Pagez" %>
<%@ page import="com.fbdblog.systemprops.InstanceProperties" %>
<table width="100%" cellspacing="0" border="0" cellpadding="0">
<tr>
    <tr>
        <td valign="top" align="right">
            <center><font class="tinyfont">Copyright 2009. All rights reserved.</font> <font class="tinyfont" style="color: #cccccc; padding-right: 10px;">At Your Service is a Server Called: <%=InstanceProperties.getInstancename()%> which built this page in: <%=Pagez.getElapsedTime()%> milliseconds</font></center>
        </td>
    </tr>
</table>