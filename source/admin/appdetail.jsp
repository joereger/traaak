<%@ page import="java.util.List" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.fbdblog.util.Num" %>
<%@ include file="header.jsp" %>

<%
    App app = null;
    if (request.getParameter("appid") != null && Num.isinteger(request.getParameter("appid"))) {
        app = App.get(Integer.parseInt(request.getParameter("appid")));
    } else {
        if (request.getParameter("action")==null){
            response.sendRedirect("apps.jsp");
            return;
        }
        app = new App();
    }
%>

<%
if (request.getParameter("action")!=null && request.getParameter("action").equals("save")){
    app.setTitle(request.getParameter("title"));
    app.setDescription(request.getParameter("description"));
    app.setFacebookappname(request.getParameter("facebookappname"));
    app.setFacebookapikey(request.getParameter("facebookapikey"));
    app.setFacebookapisecret(request.getParameter("facebookapisecret"));
    try{app.save();}catch(Exception ex){logger.error(ex);}
    out.print("Saved.<br/>");
}
%>

App Detail: <%=app.getTitle()%>
<br/><br/>
<form action="" method="post">
    <input type="hidden" name="appid" value="<%=app.getAppid()%>">
    <input type="hidden" name="action" value="save">
    <table cellpadding="0" cellspacing="0" border="0">
        <tr>
            <td valign="top">
                App Title
            </td>
            <td valign="top">
                <input type="text" name="title" value="<%=app.getTitle()%>" size="25" maxlength="255">
            </td>
        </tr>
        <tr>
            <td valign="top">
                Description
            </td>
            <td valign="top">
                <textarea name="description"><%=app.getDescription()%></textarea>
            </td>
        </tr>
        <tr>
            <td valign="top">
                Facebook App Name
            </td>
            <td valign="top">
                <input type="text" name="facebookappname" value="<%=app.getFacebookappname()%>" size="25" maxlength="255">
            </td>
        </tr>
        <tr>
            <td valign="top">
                Facebook Api Key
            </td>
            <td valign="top">
                <input type="text" name="facebookapikey" value="<%=app.getFacebookapikey()%>" size="25" maxlength="255">
            </td>
        </tr>
        <tr>
            <td valign="top">
                Facebook Api Secret
            </td>
            <td valign="top">
                <input type="text" name="facebookapisecret" value="<%=app.getFacebookapisecret()%>" size="25" maxlength="255">
            </td>
        </tr>
        <tr>
            <td valign="top">

            </td>
            <td valign="top">
                <input type="submit" value="Save">
            </td>
        </tr>
    </table>
</form>




<%@ include file="footer.jsp" %>