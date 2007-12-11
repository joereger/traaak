<%@ page import="java.util.List" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="com.fbdblog.util.Str" %>
<%@ page import="com.fbdblog.util.Num" %>
<%@ page import="com.fbdblog.dao.User" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.fbdblog.util.RandomString" %>
<%@ page import="com.fbdblog.util.Time" %>
<%@ include file="header.jsp" %>




<font class="pagetitle">Timezone</font>
<br/><br/>

<%
    User user=new User();
    user.setCreatedate(Time.nowInGmtDate());
    user.setEmail(RandomString.randomAlphabetic(3) + "@" + RandomString.randomAlphabetic(3) + ".com");
    user.setPassword(RandomString.randomAlphabetic(3));
    user.setFacebookuid("");
    user.setFirstname(RandomString.randomAlphabetic(3));
    user.setIsenabled(false);
    user.setLastname(RandomString.randomAlphabetic(3));
    try {
        user.save();
    } catch (Exception ex) {
        logger.error("", ex);
    }
    user.refresh();
    out.print("<br/>new Date()="+Time.dateformatcompactwithtime(new Date()));
    out.print("<br/>Time.nowInGmtDate()="+Time.dateformatcompactwithtime(Time.nowInGmtDate()));
    out.print("<br/>Time.usertogmttime(new Date(), \"EST\")="+Time.dateformatcompactwithtime(Time.usertogmttime(new Date(), "EST")));
    out.print("<br/>user.getCreatedate()="+Time.dateformatcompactwithtime(user.getCreatedate()));
    out.print("<br/>Time.gmttousertime(user.getCreatedate(), \"EST\")="+Time.dateformatcompactwithtime(Time.gmttousertime(user.getCreatedate(), "EST")));
%>



<%@ include file="footer.jsp" %>