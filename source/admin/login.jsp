<%@ page import="com.fbdblog.session.UserSession" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="com.fbdblog.dao.User" %>
<%@ page import="java.util.Iterator" %>
<%
    //Logger
    Logger logger = Logger.getLogger(this.getClass());

    //Make sure we have a userSession to work with
    UserSession userSession = null;
    Object ustmp = request.getSession().getAttribute("userSession");
    if (ustmp != null) {
        userSession = (UserSession) ustmp;
    } else {
        userSession = new UserSession();
        request.getSession().setAttribute("userSession", userSession);
    }
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("login")) {
        List<User> users = HibernateUtil.getSession().createCriteria(User.class)
                .add(Restrictions.eq("email", request.getParameter("email")))
                .setCacheable(false)
                .list();
        for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
            User user =  iterator.next();
            if (user.getPassword().equals(request.getParameter("password"))){
                userSession.setUser(user);
                userSession.setIsloggedin(true);
                if (userSession.getIssysadmin()){
                    response.sendRedirect("index.jsp");
                    return;
                }
            }
        }
        out.print("Sorry.  Fail.");
    }
%>

<form action="login.jsp" method="post">
    <input type="hidden" name="action" value="login">
    Email:  <input type="text" name="email" value="" size="25" maxlength="255">
    <br/>
    Password: <input type="password" name="password" value="" size="25" maxlength="255">
    <br/>
    <input type="submit" value="Log In">  
</form>