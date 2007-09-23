<%@ page import="java.util.List" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.fbdblog.util.Num" %>
<%@ page import="com.fbdblog.dao.Question" %>
<%@ page import="com.fbdblog.qtype.def.Component" %>
<%@ page import="com.fbdblog.qtype.Textbox" %>
<%@ page import="com.fbdblog.qtype.Timeperiod" %>
<%@ page import="com.fbdblog.chart.DataTypeString" %>
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
    Question question = null;
    if (request.getParameter("questionid") != null && Num.isinteger(request.getParameter("questionid"))) {
        question = Question.get(Integer.parseInt(request.getParameter("questionid")));
    } else {
        if (request.getParameter("action") == null) {
            response.sendRedirect("appdetail.jsp?appid="+app.getAppid());
            return;
        }
        question = new Question();
    }
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        question.setComponenttype(Timeperiod.ID);
        question.setQuestion(request.getParameter("question"));
        question.setAppid(app.getAppid());
        boolean isrequired = false;
        if (request.getParameter("isrequired") != null && request.getParameter("isrequired").equals("1")) {
            isrequired = true;
        }
        question.setIsrequired(isrequired);
        question.setDatatypeid(DataTypeString.DATATYPEID);
        try {
            question.save();
        } catch (Exception ex) {
            logger.error(ex);
        }
        response.sendRedirect("appdetail.jsp?appid=" + app.getAppid());
        return;
    }
%>
App: <a href='appdetail.jsp?appid=<%=app.getAppid()%>'><%=app.getTitle()%></a><br/>
Question Detail: <%=question.getQuestion()%>
<br/><br/>
<form action="appdetail-question-timeperiod.jsp" method="post">
    <input type="hidden" name="appid" value="<%=app.getAppid()%>">
    <input type="hidden" name="questionid" value="<%=question.getQuestionid()%>">
    <input type="hidden" name="action" value="save">
    <table cellpadding="0" cellspacing="0" border="0">
        <tr>
            <td valign="top">
                Question
            </td>
            <td valign="top">
                <input type="text" name="question" value="<%=question.getQuestion()%>" size="25" maxlength="255">
            </td>
        </tr>
        <tr>
            <td valign="top">
                Required?
            </td>
            <td valign="top">
                <%
                String selectedIsrequired = "";
                if (question.getIsrequired()){
                    selectedIsrequired = " checked";
                }
                %>
                <input type="checkbox" name="isrequired" value="1" <%=selectedIsrequired%>>
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