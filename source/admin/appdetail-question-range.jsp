<%@ page import="java.util.List" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.fbdblog.util.Num" %>
<%@ page import="com.fbdblog.dao.Question" %>
<%@ page import="com.fbdblog.qtype.def.Component" %>
<%@ page import="com.fbdblog.dao.Questionconfig" %>
<%@ page import="com.fbdblog.util.UserInputSafe" %>
<%@ page import="com.fbdblog.qtype.*" %>
<%@ page import="com.fbdblog.chart.DataTypeString" %>
<%@ page import="com.fbdblog.chart.DataTypeDecimal" %>
<%@ page import="com.fbdblog.chart.DataTypeDatetime" %>
<%@ page import="com.fbdblog.chart.DataTypeInteger" %>
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
        question.setComponenttype(Range.ID);
        question.setQuestion(request.getParameter("question"));
        question.setAppid(app.getAppid());
        boolean isrequired = false;
        if (request.getParameter("isrequired") != null && request.getParameter("isrequired").equals("1")) {
            isrequired = true;
        }
        question.setIsrequired(isrequired);
        int datatypeid = DataTypeString.DATATYPEID;
        if (request.getParameter("datatypeid") != null && Num.isinteger(request.getParameter("datatypeid"))) {
            datatypeid = Integer.parseInt(request.getParameter("datatypeid"));
        }
        question.setDatatypeid(datatypeid);
        try {
            question.save();
        } catch (Exception ex) {
            logger.error(ex);
        }
        for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
            Questionconfig questionconfig = iterator.next();
            iterator.remove();
        }

        Questionconfig qc1 = new Questionconfig();
        qc1.setQuestionid(question.getQuestionid());
        qc1.setName("mintitle");
        qc1.setValue(UserInputSafe.clean(request.getParameter("mintitle")));
        question.getQuestionconfigs().add(qc1);

        Questionconfig qc2 = new Questionconfig();
        qc2.setQuestionid(question.getQuestionid());
        qc2.setName("min");
        qc2.setValue(UserInputSafe.clean(request.getParameter("min")));
        question.getQuestionconfigs().add(qc2);

        Questionconfig qc3 = new Questionconfig();
        qc3.setQuestionid(question.getQuestionid());
        qc3.setName("step");
        qc3.setValue(UserInputSafe.clean(request.getParameter("step")));
        question.getQuestionconfigs().add(qc3);

        Questionconfig qc4 = new Questionconfig();
        qc4.setQuestionid(question.getQuestionid());
        qc4.setName("max");
        qc4.setValue(UserInputSafe.clean(request.getParameter("max")));
        question.getQuestionconfigs().add(qc4);

        Questionconfig qc5 = new Questionconfig();
        qc5.setQuestionid(question.getQuestionid());
        qc5.setName("maxtitle");
        qc5.setValue(UserInputSafe.clean(request.getParameter("maxtitle")));
        question.getQuestionconfigs().add(qc5);

        try {
            question.save();
            app.save();
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
<form action="appdetail-question-range.jsp" method="post">
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
                Data Type
            </td>
            <td valign="top">
                <select name="datatypeid">
                    <%String numericSelected = "";
                    if (question.getDatatypeid()==DataTypeDecimal.DATATYPEID){numericSelected=" selected";}%>
                    <option value="<%=DataTypeDecimal.DATATYPEID%>" <%=numericSelected%>>Numeric/Decimal</option>
                </select>
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
                Min Title
            </td>
            <td valign="top">
                <%
                String mintitle = "";
                for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
                    Questionconfig questionconfig = iterator.next();
                    if (questionconfig.getName().equals("mintitle")){
                        mintitle = questionconfig.getValue();
                    }
                }
                %>
                <input type="text" name="mintitle" value="<%=mintitle%>" size="25" maxlength="255">
            </td>
        </tr>
        <tr>
            <td valign="top">
                Min
            </td>
            <td valign="top">
                <%
                String min = "";
                for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
                    Questionconfig questionconfig = iterator.next();
                    if (questionconfig.getName().equals("min")){
                        min = questionconfig.getValue();
                    }
                }
                %>
                <input type="text" name="min" value="<%=min%>" size="25" maxlength="255">
            </td>
        </tr>
        <tr>
            <td valign="top">
                Step
            </td>
            <td valign="top">
                <%
                String step = "";
                for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
                    Questionconfig questionconfig = iterator.next();
                    if (questionconfig.getName().equals("step")){
                        step = questionconfig.getValue();
                    }
                }
                %>
                <input type="text" name="step" value="<%=step%>" size="25" maxlength="255">
            </td>
        </tr>
        <tr>
            <td valign="top">
                Max
            </td>
            <td valign="top">
                <%
                String max = "";
                for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
                    Questionconfig questionconfig = iterator.next();
                    if (questionconfig.getName().equals("max")){
                        max = questionconfig.getValue();
                    }
                }
                %>
                <input type="text" name="max" value="<%=max%>" size="25" maxlength="255">
            </td>
        </tr>
        <tr>
            <td valign="top">
                Max Title
            </td>
            <td valign="top">
                <%
                String maxtitle = "";
                for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
                    Questionconfig questionconfig = iterator.next();
                    if (questionconfig.getName().equals("maxtitle")){
                        maxtitle = questionconfig.getValue();
                    }
                }
                %>
                <input type="text" name="maxtitle" value="<%=maxtitle%>" size="25" maxlength="255">
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