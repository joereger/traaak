<%@ page import="java.util.List" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.fbdblog.util.Num" %>
<%@ page import="com.fbdblog.dao.Question" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="com.fbdblog.qtype.def.ComponentTypes" %>
<%@ page import="com.fbdblog.qtype.def.Component" %>
<%@ page import="com.fbdblog.qtype.*" %>
<%@ page import="com.fbdblog.dao.Chart" %>
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
<form action="appdetail.jsp" method="post">
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

<br/><br/>
Questions:
<br/>
<%
    List<Question> questions = HibernateUtil.getSession().createCriteria(Question.class)
            .add(Restrictions.eq("appid", app.getAppid()))
            .setCacheable(false)
            .list();
    for (Iterator<Question> iterator = questions.iterator(); iterator.hasNext();) {
        Question question = iterator.next();
        String comptypefilename = "";
        if (question.getComponenttype() == Textbox.ID) {
            comptypefilename = "textbox";
        } else if (question.getComponenttype() == Checkboxes.ID) {
            comptypefilename = "checkboxes";
        } else if (question.getComponenttype() == Dropdown.ID) {
            comptypefilename = "dropdown";
        } else if (question.getComponenttype() == Essay.ID) {
            comptypefilename = "essay";
        } else if (question.getComponenttype() == Matrix.ID) {
            comptypefilename = "matrix";
        } else if (question.getComponenttype() == Range.ID) {
            comptypefilename = "range";
        } else if (question.getComponenttype() == Timeperiod.ID) {
            comptypefilename = "timeperiod";
        }
        %>
        <a href="appdetail-question-<%=comptypefilename%>.jsp?appid=<%=app.getAppid()%>&questionid=<%=question.getQuestionid()%>"><%=question.getQuestion()%></a><br/>
        <%
    }
%>
<br/>
<br/><a href='appdetail-question-textbox.jsp?action=newquestion&appid=<%=app.getAppid()%>'>+ Add Textbox</a>
<br/><a href='appdetail-question-checkboxes.jsp?action=newquestion&appid=<%=app.getAppid()%>'>+ Add Checkboxes</a>
<br/><a href='appdetail-question-dropdown.jsp?action=newquestion&appid=<%=app.getAppid()%>'>+ Add Dropdown</a>
<br/><a href='appdetail-question-essay.jsp?action=newquestion&appid=<%=app.getAppid()%>'>+ Add Essay</a>
<br/><a href='appdetail-question-matrix.jsp?action=newquestion&appid=<%=app.getAppid()%>'>+ Add Matrix</a>
<br/><a href='appdetail-question-range.jsp?action=newquestion&appid=<%=app.getAppid()%>'>+ Add Range</a>
<br/><a href='appdetail-question-timeperiod.jsp?action=newquestion&appid=<%=app.getAppid()%>'>+ Add Timeperiod</a>
<br/><br/>


<br/><br/>
Charts:
<br/>
<%
    List<Chart> charts = HibernateUtil.getSession().createCriteria(Chart.class)
            .add(Restrictions.eq("appid", app.getAppid()))
            .setCacheable(false)
            .list();
    for (Iterator<Chart> iterator = charts.iterator(); iterator.hasNext();) {
        Chart chart = (Chart)iterator.next();
        %>
        <a href="appdetail-chart.jsp?appid=<%=app.getAppid()%>&chartid=<%=chart.getChartid()%>"><%=chart.getChartid()%>: <%=chart.getName()%></a><br/>
        <%
    }
%>
<br/>
<br/><a href="appdetail-chart.jsp?action=newchart&appid=<%=app.getAppid()%>">+ Add Chart</a><br/>


<%@ include file="footer.jsp" %>