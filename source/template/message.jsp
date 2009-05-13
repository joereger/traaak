<%@ page import="com.fbdblog.htmlui.Pagez" %>
<% if (Pagez.getUserSession().getMessage()!=null && !Pagez.getUserSession().getMessage().equals("")){
    %>
    <br/>
    <center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="formfieldnamefont"><%=Pagez.getUserSession().getMessage()%></font></div></center>
    <br/><br/>
    <%
    //Clear the message since it's been displayed
    Pagez.getUserSession().setMessage("");
    %>
<%}%>