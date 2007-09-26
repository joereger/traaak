<%@ page import="java.util.List" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.fbdblog.systemprops.SystemProperty" %>
<%@ page import="com.fbdblog.util.Str" %>
<%@ include file="header.jsp" %>


<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        SystemProperty.setProp(SystemProperty.PROP_BASEURL, request.getParameter("baseurl"));
        SystemProperty.setProp(SystemProperty.PROP_ISSSLON, request.getParameter("issslon"));
        SystemProperty.setProp(SystemProperty.PROP_SENDXMPP, request.getParameter("sendxmpp"));
        SystemProperty.setProp(SystemProperty.PROP_SMTPOUTBOUNDSERVER, request.getParameter("smtpoutboundserver"));
    }

%>

SysProps
<br/><br/>

<form action="sysprops.jsp" method="post">
    <input type="hidden" name="action" value="save">
        <table cellpadding="5" cellspacing="0" border="0">
            <tr>
                <td valign="top">
                    Base Url
                    <br/><font style="font-size: 8px;">ex: www.fdblog.com</font>
                </td>
                <td valign="top">
                    <input type="textbox" name="baseurl" value="<%=Str.cleanForHtml(SystemProperty.getProp(SystemProperty.PROP_BASEURL))%>">
                </td>
            </tr>
            <tr>
                <td valign="top">
                    Is SSL On
                    <br/><font style="font-size: 8px;">0 or 1</font>
                </td>
                <td valign="top">
                    <input type="textbox" name="issslon" value="<%=Str.cleanForHtml(SystemProperty.getProp(SystemProperty.PROP_ISSSLON))%>">
                </td>
            </tr>
            <tr>
                <td valign="top">
                    Send XMPP
                    <br/><font style="font-size: 8px;">0 or 1</font>
                </td>
                <td valign="top">
                    <input type="textbox" name="sendxmpp" value="<%=Str.cleanForHtml(SystemProperty.getProp(SystemProperty.PROP_SENDXMPP))%>">
                </td>
            </tr>
            <tr>
                <td valign="top">
                    SMTP Outbound
                    <br/><font style="font-size: 8px;">ex: localhost</font>
                </td>
                <td valign="top">
                    <input type="textbox" name="smtpoutboundserver" value="<%=Str.cleanForHtml(SystemProperty.getProp(SystemProperty.PROP_SMTPOUTBOUNDSERVER))%>">
                </td>
            </tr>
            <tr>
                <td valign="top">

                </td>
                <td valign="top">
                    <input type="submit" value="Save Props">
                </td>
            </tr>
        </table>
</form>

<%@ include file="footer.jsp" %>