<%@ page import="java.util.List" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="com.fbdblog.util.Str" %>
<%@ page import="com.fbdblog.util.Num" %>
<%@ include file="header.jsp" %>


<%
String unit = "";
if (request.getParameter("unit")!=null && !request.getParameter("unit").equals("")){
    if (request.getParameter("unit").equals("adglobalheader")){
        unit = "adglobalheader";
    } else if (request.getParameter("unit").equals("adpostsave")){
        unit = "adpostsave";
    } else if (request.getParameter("unit").equals("adhistoryright")){
        unit = "adhistoryright";
    } else if (request.getParameter("unit").equals("adunderchart")){
        unit = "adunderchart";
    }
}
if (unit.equals("")){
    response.sendRedirect("ads.jsp");
    return;
}
%>

<%
if (request.getParameter("action")!=null && request.getParameter("action").equals("save")){
    List<App> apps=HibernateUtil.getSession().createQuery("from App").list();
    for (Iterator<App> iterator=apps.iterator(); iterator.hasNext();) {
        App app=iterator.next();
        String ad = "";
        if (request.getParameter(unit+"-ad-appid-"+app.getAppid())!=null){
            ad = request.getParameter(unit+"-ad-appid-"+app.getAppid());
        }
        if (unit.equals("adglobalheader")){
            app.setAdglobalheader(ad);
        } else if (unit.equals("adpostsave")){
            app.setAdpostsave(ad);
        } else if (unit.equals("adhistoryright")){
            app.setAdhistoryright(ad);
        } else if (unit.equals("adunderchart")){
            app.setAdunderchart(ad);
        }
        try{app.save();}catch(Exception ex){logger.error("",ex);}
    }
    out.print("Saved.<br/>");
}
%>


<font class="pagetitle">Ads: <%=unit%></font>
<br/><br/>
<form action="ads-byunit.jsp" method="post">
<input type="hidden" name="unit" value="<%=unit%>">
<input type="hidden" name="action" value="save">

<table cellpadding="3" cellspacing="0" border="0" width="100%">
    <tr>
        <td valign="top"></td>
        <td valign="top"></td>
    </tr>
    <%
    List<App> apps=HibernateUtil.getSession().createQuery("from App").list();
    for (Iterator<App> iterator=apps.iterator(); iterator.hasNext();) {
        App app=iterator.next();
        %>
        <tr>
            <td valign="top" style="text-align: right;"><a href="appdetail.jsp?appid=<%=app.getAppid()%>"><b><%=app.getTitle()%></b></a></td>
            <td valign="top" align="center">
                <%
                String ad = "";
                if (unit.equals("adglobalheader")){
                    ad = app.getAdglobalheader();
                } else if (unit.equals("adpostsave")){
                    ad = app.getAdpostsave();
                } else if (unit.equals("adhistoryright")){
                    ad = app.getAdhistoryright();
                } else if (unit.equals("adunderchart")){
                    ad = app.getAdunderchart();
                }
                %>
                <textarea name="<%=unit%>-ad-appid-<%=app.getAppid()%>" cols="65" rows="3" style="width: 100%;"><%=ad%></textarea>
            </td>
        </tr>
        <%
    }
    %>
    <tr>
        <td valign="top"></td>
        <td valign="top"><input type="submit" value="Save"></td>
    </tr>
</table>
</form>
<br/><br/>



<%@ include file="footer.jsp" %>