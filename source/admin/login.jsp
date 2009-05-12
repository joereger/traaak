<%@ page import="com.fbdblog.session.UserSession" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="com.fbdblog.dao.User" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.fbdblog.systemprops.BaseUrl" %>
<%@ page import="com.fbdblog.session.Pagez" %>
<%@ page import="com.fbdblog.session.PersistentLogin" %>
<%
    //Logger
    Logger logger = Logger.getLogger(this.getClass());
%>

<style type="text/css">
    body, td {font-family: sans-serif;}
</style>

<body>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("login")) {
        List<User> users = HibernateUtil.getSession().createCriteria(User.class)
                .add(Restrictions.eq("email", request.getParameter("email")))
                .setCacheable(false)
                .list();
        for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
            User user =  iterator.next();
            if (user.getPassword().equals(request.getParameter("password"))){
                Pagez.getUserSession().setUser(user);
                Pagez.getUserSession().setIsloggedin(true);
                //Set persistent login cookie, if necessary
                boolean keepmeloggedin = true;
                if (keepmeloggedin){
                    logger.debug("keepmeloggedin=true");
                    //Get all possible cookies to set
                    Cookie[] cookies = PersistentLogin.getPersistentCookies(user.getUserid(), Pagez.getRequest());
                    logger.debug("cookies.length="+cookies.length);
                    //Add a cookies to the response
                    for (int j = 0; j < cookies.length; j++) {
                        logger.debug("Setting persistent login cookie name="+cookies[j].getName()+" value="+cookies[j].getValue()+" cookies[j].getDomain()="+cookies[j].getDomain()+" cookies[j].getPath()="+cookies[j].getPath());
                        Pagez.getResponse().addCookie(cookies[j]);
                    }
                }
                //Redir sysadmins
                if (Pagez.getUserSession().getIssysadmin()){
                    response.sendRedirect("apps.jsp");
                    return;
                }
            }
        }
        out.print("Sorry.  Fail.");
    }
%>



<center>
<form action="login.jsp" method="post">
    <input type="hidden" name="action" value="login">
    <table cellpadding="2" cellspacing="0" border="0">
        <tr>
            <td valign="top">
                <img src="/images/cake.jpg" alt="Mmmm, cake"> 
            </td>
            <td valign="top">
                <img src="/images/clear.gif" alt="" width="1" height="240">
                <br/>
                <img src="<%=BaseUrl.get(false)%>images/logo-128.png" alt="" width="128" height="128"/>
            </td>
            <td valign="top">
                <img src="/images/clear.gif" alt="" width="1" height="240">
                <table cellpadding="3" cellspacing="0" border="0">
                    <tr>
                        <td valign="top" colspan="2">
                            <font style="font-family: impact; font-size: 42px; color: #cccccc;">Log In for Cake</font>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font style="font-size: 12px; color: #666666;"><b>Email:</b></font>
                        </td>
                        <td valign="top">
                            <input type="text" name="email" value="" size="25" maxlength="255">
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <font style="font-size: 12px; color: #666666;"><b>Password:</b></font>
                        </td>
                        <td valign="top">
                            <input type="password" name="password" value="" size="25" maxlength="255">
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">

                        </td>
                        <td valign="top">
                            <input type="submit" value="Log In">
                        </td>
                    </tr>
                 </table>
             </td>
        </tr>
    </table>  
</form>
</center>


</body>