<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="java.util.List" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.fbdblog.systemprops.BaseUrl" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="com.fbdblog.dao.User" %>
<%@ page import="com.fbdblog.helpers.FindUserFromNickname" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Traaak";
String navtab = "home";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>

<%
//See if this user exists
User userToDisplay = FindUserFromNickname.find(request.getParameter("nickname"));
if(userToDisplay==null || !userToDisplay.getIsenabled()){
    Pagez.getUserSession().setMessage("User not found.");
    Pagez.sendRedirect("/index.jsp");
    return;
}
%>


<%@ include file="/template/header.jsp" %>

<font class="largefont"><%=userToDisplay.getNickname()%>'s Profile</font>
<br/><br/>




<%@ include file="/template/footer.jsp" %>
