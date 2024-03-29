<%@ page import="java.util.List" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.fbdblog.dao.User" %>
<%@ page import="com.fbdblog.util.Time" %>
<%@ include file="header.jsp" %>


<%
int totalusers = ((Long)HibernateUtil.getSession().createQuery("select count(*) from User").uniqueResult()).intValue();
%>

<font class="pagetitle">Users (<%=totalusers%>)</font>
<br/><br/>


<%
    List<User> users=HibernateUtil.getSession().createQuery("from User order by userid desc").setMaxResults(100).list();
    for (Iterator<User> iterator=users.iterator(); iterator.hasNext();) {
        User user=iterator.next();
        %>
        (<%=user.getUserid()%>) <%=user.getFirstname()%> <%=user.getLastname()%> - <%=user.getFacebookuid()%> - <%=Time.dateformatcompactwithtime(Time.gmttousertime(user.getCreatedate(), Pagez.getUserSession().getUser().getTimezoneid()))%><br/>
        <%
    }
%>
<br/><br/>


<%@ include file="footer.jsp" %>