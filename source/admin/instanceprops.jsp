<%@ page import="java.util.List" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.fbdblog.util.Str" %>
<%@ page import="com.fbdblog.systemprops.InstanceProperties" %>
<%@ include file="header.jsp" %>


<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        InstanceProperties.setDbConnectionUrl(request.getParameter("dbConnectionUrl"));
        InstanceProperties.setDbUsername(request.getParameter("dbUsername"));
        InstanceProperties.setDbPassword(request.getParameter("dbPassword"));
        InstanceProperties.setDbMaxActive(request.getParameter("dbMaxActive"));
        InstanceProperties.setDbMaxIdle(request.getParameter("dbMaxIdle"));
        InstanceProperties.setDbMinIdle(request.getParameter("dbMinIdle"));
        InstanceProperties.setDbMaxWait(request.getParameter("dbMaxWait"));
        InstanceProperties.setDbDriverName(request.getParameter("dbDriverName"));
        InstanceProperties.setRunScheduledTasksOnThisInstance(Str.booleanFromSQLText(request.getParameter("runScheduledTasksOnThisInstance")));
        InstanceProperties.setInstancename(request.getParameter("instancename"));
        InstanceProperties.setAbsolutepathtochartfiles(request.getParameter("absolutepathtochartfiles"));

        //Save them
        InstanceProperties.save();
    }

%>

InstanceProps
<br/><br/>

<form action="instanceprops.jsp" method="post">
    <input type="hidden" name="action" value="save">
        <table cellpadding="5" cellspacing="0" border="0">
            <tr>
                <td valign="top">
                    dbConnectionUrl
                    <br/><font style="font-size: 8px;">jdbc:mysql://localhost:3306/fbdblog?autoReconnect=true</font>
                </td>
                <td valign="top">
                    <input type="textbox" name="dbConnectionUrl" value="<%=Str.cleanForHtml(InstanceProperties.getDbConnectionUrl())%>">
                </td>
            </tr>
            <tr>
                <td valign="top">
                    dbUsername
                </td>
                <td valign="top">
                    <input type="textbox" name="dbUsername" value="<%=Str.cleanForHtml(InstanceProperties.getDbUsername())%>">
                </td>
            </tr>
            <tr>
                <td valign="top">
                    dbPassword
                </td>
                <td valign="top">
                    <input type="password" name="dbPassword" value="<%=Str.cleanForHtml(InstanceProperties.getDbPassword())%>">
                </td>
            </tr>
            <tr>
                <td valign="top">
                    dbMaxActive
                </td>
                <td valign="top">
                    <input type="textbox" name="dbMaxActive" value="<%=Str.cleanForHtml(String.valueOf(InstanceProperties.getDbMaxActive()))%>">
                </td>
            </tr>
            <tr>
                <td valign="top">
                    dbMaxIdle
                </td>
                <td valign="top">
                    <input type="textbox" name="dbMaxIdle" value="<%=Str.cleanForHtml(String.valueOf(InstanceProperties.getDbMaxIdle()))%>">
                </td>
            </tr>
            <tr>
                <td valign="top">
                    dbMinIdle
                </td>
                <td valign="top">
                    <input type="textbox" name="dbMinIdle" value="<%=Str.cleanForHtml(String.valueOf(InstanceProperties.getDbMinIdle()))%>">
                </td>
            </tr>
            <tr>
                <td valign="top">
                    dbMaxWait
                </td>
                <td valign="top">
                    <input type="textbox" name="dbMaxWait" value="<%=Str.cleanForHtml(String.valueOf(InstanceProperties.getDbMaxWait()))%>">
                </td>
            </tr>
            <tr>
                <td valign="top">
                    dbDriverName
                    <br/><font style="font-size: 8px;">com.mysql.jdbc.Driver</font>
                </td>
                <td valign="top">
                    <input type="textbox" name="dbDriverName" value="<%=Str.cleanForHtml(InstanceProperties.getDbDriverName())%>">
                </td>
            </tr>
            <tr>
                <td valign="top">
                    runScheduledTasksOnThisInstance
                    <br/><font style="font-size: 8px;">0 or 1</font>
                </td>
                <td valign="top">
                    <input type="textbox" name="dbDriverName" value="<%=Str.cleanForHtml(Str.booleanAsSQLText(InstanceProperties.getRunScheduledTasksOnThisInstance()))%>">
                </td>
            </tr>
            <tr>
                <td valign="top">
                    instancename
                </td>
                <td valign="top">
                    <input type="textbox" name="instancename" value="<%=Str.cleanForHtml(InstanceProperties.getInstancename())%>">
                </td>
            </tr>
            <tr>
                <td valign="top">
                    absolutepathtochartfiles
                    <br/><font style="font-size: 8px;">c:\SuperFly\fdblog-chartfiles</font>
                </td>
                <td valign="top">
                    <input type="textbox" name="absolutepathtochartfiles" value="<%=Str.cleanForHtml(InstanceProperties.getAbsolutepathtochartfiles())%>">
                    <br/><font style="font-size: 8px;">Make sure service that Tomcat is running as has access</font>
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