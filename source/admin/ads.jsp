<%@ page import="java.util.List" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="com.fbdblog.util.Str" %>
<%@ page import="com.fbdblog.util.Num" %>
<%@ include file="header.jsp" %>




<font class="pagetitle">Ads</font>
<br/><br/>

<table cellpadding="3" cellspacing="0" border="0">
<tr>
<td valign="top"></td>
<td valign="top"><a href='ads-byunit.jsp?unit=adglobalheader'>Adglobalheader</a></td>
<td valign="top"><a href='ads-byunit.jsp?unit=adpostsave'>Adpostsave</a></td>
<td valign="top"><a href='ads-byunit.jsp?unit=adhistoryright'>Adhistoryright</a></td>
<td valign="top"><a href='ads-byunit.jsp?unit=adunderchart'>Adunderchart</a></td>
</tr>
<%


    List<App> apps=HibernateUtil.getSession().createQuery("from App").list();
    for (Iterator<App> iterator=apps.iterator(); iterator.hasNext();) {
        App app=iterator.next();

        %>
        <tr>
            <td valign="top"><a href="appdetail.jsp?appid=<%=app.getAppid()%>"><%=app.getTitle()%></a></td>
            <td valign="top" align="center">
                <%
                if (!app.getAdglobalheader().equals("")){
                    %><img src="/images/misc-green-16.png" alt="" width="16" height="16"/><%
                } else {
                    %><img src="/images/misc-red-16.png" alt="" width="16" height="16"/><%
                }
                %>
            </td>
            <td valign="top" align="center">
                <%
                if (!app.getAdpostsave().equals("")){
                    %><img src="/images/misc-green-16.png" alt="" width="16" height="16"/><%
                } else {
                    %><img src="/images/misc-red-16.png" alt="" width="16" height="16"/><%
                }
                %>
            </td>
            <td valign="top" align="center">
                <%
                if (!app.getAdhistoryright().equals("")){
                    %><img src="/images/misc-green-16.png" alt="" width="16" height="16"/><%
                } else {
                    %><img src="/images/misc-red-16.png" alt="" width="16" height="16"/><%
                }
                %>
            </td>
            <td valign="top" align="center">
                <%
                if (!app.getAdunderchart().equals("")){
                    %><img src="/images/misc-green-16.png" alt="" width="16" height="16"/><%
                } else {
                    %><img src="/images/misc-red-16.png" alt="" width="16" height="16"/><%
                }
                %>
            </td>
        </tr>
        <%
    }
%>
</table>
<br/><br/>



<%@ include file="footer.jsp" %>