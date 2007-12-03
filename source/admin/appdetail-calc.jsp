<%@ page import="java.util.List" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.fbdblog.util.Num" %>
<%@ page import="com.fbdblog.dao.Question" %>
<%@ page import="com.fbdblog.qtype.def.Component" %>
<%@ page import="com.fbdblog.qtype.Textbox" %>
<%@ page import="com.fbdblog.chart.DataTypeString" %>
<%@ page import="com.fbdblog.chart.DataTypeDatetime" %>
<%@ page import="com.fbdblog.chart.DataTypeInteger" %>
<%@ page import="com.fbdblog.chart.DataTypeDecimal" %>
<%@ page import="com.fbdblog.dao.Questioncalc" %>
<%@ page import="com.fbdblog.calc.*" %>
<%@ include file="header.jsp" %>

<%
    App app = null;
    if (request.getParameter("appid") != null && Num.isinteger(request.getParameter("appid"))) {
        app = App.get(Integer.parseInt(request.getParameter("appid")));
    } else {
        response.sendRedirect("apps.jsp");
        return;
    }
%>

<%
    Question question = null;
    if (request.getParameter("questionid")!=null && Num.isinteger(request.getParameter("questionid"))) {
        question = Question.get(Integer.parseInt(request.getParameter("questionid")));
    } else {
        response.sendRedirect("appdetail.jsp?appid="+app.getAppid());
        return;
    }
%>

<%
    Questioncalc questioncalc=null;
    if (request.getParameter("questioncalcid") != null && Num.isinteger(request.getParameter("questioncalcid"))) {
        questioncalc=Questioncalc.get(Integer.parseInt(request.getParameter("questioncalcid")));
    } else {
        if (request.getParameter("action").equals("newcalc")){
            questioncalc = new Questioncalc();
        } else {
            response.sendRedirect("appdetail.jsp?appid=" + app.getAppid());
            return;
        }
    }
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        if (request.getParameter("calctimeperiodid")!=null && Num.isinteger(request.getParameter("calctimeperiodid"))){
            questioncalc.setCalctimeperiodid(Integer.parseInt(request.getParameter("calctimeperiodid")));
        }
        if (request.getParameter("calculationtype")!=null && Num.isinteger(request.getParameter("calculationtype"))){
            questioncalc.setCalculationtype(Integer.parseInt(request.getParameter("calculationtype")));
        }
        questioncalc.setName(request.getParameter("name"));
        questioncalc.setQuestionid(question.getQuestionid());
        try{questioncalc.save();}catch(Exception ex){logger.error("", ex);}
        response.sendRedirect("appdetail.jsp?appid=" + app.getAppid());
        return;
    }
%>
<font class="pagetitle">App: <a href='appdetail.jsp?appid=<%=app.getAppid()%>'><%=app.getTitle()%></a></font>
<br/>Question Detail: <%=question.getQuestion()%>
<br/><br/>
<form action="appdetail-calc.jsp" method="post">
    <input type="hidden" name="appid" value="<%=app.getAppid()%>">
    <input type="hidden" name="questioncalcid" value="<%=questioncalc.getQuestioncalcid()%>">
    <input type="hidden" name="questionid" value="<%=question.getQuestionid()%>">
    <input type="hidden" name="action" value="save">

    <table cellpadding="5" cellspacing="2" border="0">
        <tr>
            <td valign="top">
                <select name="calculationtype">
                    <%String sel = "";%>
                    <%
                    sel = "";
                    if (questioncalc.getCalculationtype()==CalculationSum.ID){
                        sel = " selected";
                    }
                    %>
                    <option value="<%=CalculationSum.ID%>" <%=sel%>>Sum</option>
                    <%
                        sel = "";
                        if (questioncalc.getCalculationtype()==CalculationAvg.ID){
                            sel = " selected";
                        }
                    %>
                    <option value="<%=CalculationAvg.ID%>" <%=sel%>>Average</option>
                    <%
                        sel="";
                        if (questioncalc.getCalculationtype()==CalculationDeltaAbs.ID) {
                            sel=" selected";
                        }
                    %>
                    <option value="<%=CalculationDeltaAbs.ID%>" <%=sel%>>Increase/Decrease</option>
                    <%
                        sel="";
                        if (questioncalc.getCalculationtype() == CalculationDeltaPercent.ID) {
                            sel=" selected";
                        }
                    %>
                    <option value="<%=CalculationDeltaPercent.ID%>" <%=sel%>>Increase/Decrease Percent</option>
                </select>
            </td>
            <td valign="top">
                <select name="calctimeperiodid">
                    <%
                    sel = "";
                    if (questioncalc.getCalctimeperiodid()==CalctimeperiodAlltime.ID){
                        sel = " selected";
                    }
                    %>
                    <option value="<%=CalctimeperiodAlltime.ID%>" <%=sel%>>All Time</option>
                    <%
                    sel = "";
                    if (questioncalc.getCalctimeperiodid()==CalctimeperiodYear.ID){
                        sel = " selected";
                    }
                    %>
                    <option value="<%=CalctimeperiodYear.ID%>" <%=sel%>>Year</option>
                    <%
                    sel = "";
                    if (questioncalc.getCalctimeperiodid()==CalctimeperiodMonth.ID){
                        sel = " selected";
                    }
                    %>
                    <option value="<%=CalctimeperiodMonth.ID%>" <%=sel%>>Month</option>
                    <%
                    sel = "";
                    if (questioncalc.getCalctimeperiodid()==CalctimeperiodWeek.ID){
                        sel = " selected";
                    }
                    %>
                    <option value="<%=CalctimeperiodWeek.ID%>" <%=sel%>>Week</option>
                    <%
                    sel = "";
                    if (questioncalc.getCalctimeperiodid()==CalctimeperiodDay.ID){
                        sel = " selected";
                    }
                    %>
                    <option value="<%=CalctimeperiodDay.ID%>" <%=sel%>>Day</option>
                </select>
            </td>
        </tr>

        <tr>
            <td valign="top">
                <input type="text" name="name" value="<%=questioncalc.getName()%>" size="25" maxlength="255"/>
            </td>
            <td valign="top">
                <input type="submit" value="Save">
            </td>
        </tr>

    </table>
</form>




<%@ include file="footer.jsp" %>