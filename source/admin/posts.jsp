<%@ page import="com.fbdblog.scheduledjobs.ImpressionCache" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="com.fbdblog.util.Num" %>
<%@ page import="com.fbdblog.util.Str" %>
<%@ page import="com.fbdblog.dao.Post" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="java.util.*" %>
<%@ page import="com.fbdblog.dao.User" %>
<%@ page import="com.fbdblog.util.Time" %>
<%@ include file="header.jsp" %>


<%
    App app=null;
    if (request.getParameter("appid") != null && Num.isinteger(request.getParameter("appid"))) {
        app=App.get(Integer.parseInt(request.getParameter("appid")));
    }
%>

<font class="pagetitle">Posts</font>
<br/><br/>

<form action="posts.jsp" method="get">
<select name="appid">
<option value="0">All Apps</option>
<%
List apps=HibernateUtil.getSession().createQuery("from App").list();
for (Iterator iterator=apps.iterator(); iterator.hasNext();) {
    App appTmp =(App) iterator.next();
    String selected = "";
    if (app!=null && appTmp.getAppid()==app.getAppid()){
        selected = " selected";
    }
    %>
        <option value="<%=appTmp.getAppid()%>" <%=selected%>><%=appTmp.getTitle()%></option>
    <%
}
%>
</select>
<input type="submit" value="Go">
</form>


<table cellpadding="3" cellspacing="0" border="0">
<tr>
<td valign="top">Postid</td>
<td valign="top">App</td>
<td valign="top">User</td>
<td valign="top">Date</td>
</tr>
<%
    List<Post> posts=new java.util.ArrayList<Post>();

    if (app != null && app.getAppid()>0) {
        posts=HibernateUtil.getSession().createCriteria(Post.class)
                .add(Restrictions.eq("appid", app.getAppid()))
                .addOrder(Order.desc("postid"))
                .setMaxResults(250)
                .setCacheable(true)
                .list();
    } else {
        posts=HibernateUtil.getSession().createCriteria(Post.class)
                .addOrder(Order.desc("postid"))
                .setMaxResults(250)
                .setCacheable(true)
                .list();
    }
    for (Iterator<Post> iterator=posts.iterator(); iterator.hasNext();) {
        Post post=iterator.next();
        User user=User.get(post.getUserid());
        app = App.get(post.getAppid());

%>
            <tr>
            <td valign="top"><%=post.getPostid()%></td>
            <td valign="top"><a href="appdetail.jsp?appid=<%=app.getAppid()%>"><%=app.getTitle()%></a></td>
            <td valign="top"><%=user.getFirstname()%> <%=user.getLastname()%></td>
            <td valign="top"><%=Time.dateformatcompactwithtime(post.getPostdate())%></td>
            </tr>
            <%
    }

%>
</table>




<%@ include file="footer.jsp" %>