<%@ page import="java.util.List" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="java.util.Iterator" %>
<%@ include file="header.jsp" %>


Apps
<br/><br/>


<%
    List<App> apps = HibernateUtil.getSession().createQuery("from App").list();
    for (Iterator<App> iterator = apps.iterator(); iterator.hasNext();) {
        App app =  iterator.next();
        %>
        <a href="appdetail.jsp?appid=<%=app.getAppid()%>"><%=app.getTitle()%></a><br/>
        <%
    }
%>
<br/><br/>
<a href='appdetail.jsp?action=newapp'>+ Add App</a>
<br/>


<%@ include file="footer.jsp" %>