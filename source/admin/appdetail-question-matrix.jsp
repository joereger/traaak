<%@ page import="java.util.List" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.fbdblog.util.Num" %>
<%@ page import="com.fbdblog.dao.Question" %>
<%@ page import="com.fbdblog.qtype.def.Component" %>
<%@ page import="com.fbdblog.qtype.Textbox" %>
<%@ page import="com.fbdblog.dao.Questionconfig" %>
<%@ page import="com.fbdblog.util.UserInputSafe" %>
<%@ page import="com.fbdblog.qtype.Checkboxes" %>
<%@ page import="com.fbdblog.qtype.Dropdown" %>
<%@ page import="com.fbdblog.qtype.Matrix" %>
<%@ page import="com.fbdblog.chart.DataTypeString" %>
<%@ page import="com.fbdblog.chart.DataTypeDatetime" %>
<%@ page import="com.fbdblog.chart.DataTypeDecimal" %>
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
        question.setComponenttype(Matrix.ID);
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
        qc1.setName("rows");
        qc1.setValue(UserInputSafe.clean(request.getParameter("rows")));
        question.getQuestionconfigs().add(qc1);

        Questionconfig qc2 = new Questionconfig();
        qc2.setQuestionid(question.getQuestionid());
        qc2.setName("cols");
        qc2.setValue(UserInputSafe.clean(request.getParameter("cols")));
        question.getQuestionconfigs().add(qc2);

        Questionconfig qc3 = new Questionconfig();
        qc3.setQuestionid(question.getQuestionid());
        qc3.setName("respondentcanselectmany");
        qc3.setValue(UserInputSafe.clean(request.getParameter("respondentcanselectmany")));
        question.getQuestionconfigs().add(qc3);

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
<form action="appdetail-question-matrix.jsp" method="post">
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
                    <%String stringSelected = "";
                    if (question.getDatatypeid()==DataTypeString.DATATYPEID){stringSelected=" selected";}%>
                    <option value="<%=DataTypeString.DATATYPEID%>" <%=stringSelected%>>String</option>

                    <%String datetimeSelected = "";
                    if (question.getDatatypeid()==DataTypeDatetime.DATATYPEID){datetimeSelected=" selected";}%>
                    <option value="<%=DataTypeDatetime.DATATYPEID%>" <%=datetimeSelected%>>Date/Time</option>

                    <%String numericSelected = "";
                    if (question.getDatatypeid()==DataTypeDecimal.DATATYPEID){numericSelected=" selected";}%>
                    <option value="<%=DataTypeDecimal.DATATYPEID%>" <%=numericSelected%>>Numeric/Decimal</option>

                    <%String integerSelected = "";
                    if (question.getDatatypeid()==DataTypeInteger.DATATYPEID){integerSelected=" selected";}%>
                    <option value="<%=DataTypeInteger.DATATYPEID%>" <%=integerSelected%>>Integer</option>
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
                Rows
            </td>
            <td valign="top">
                <%
                String rows = "";
                for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
                    Questionconfig questionconfig = iterator.next();
                    if (questionconfig.getName().equals("rows")){
                        rows = questionconfig.getValue();
                    }
                }
                %>
                <textarea name="rows"><%=rows%></textarea>
            </td>
        </tr>
        <tr>
            <td valign="top">
                Cols
            </td>
            <td valign="top">
                <%
                String cols = "";
                for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
                    Questionconfig questionconfig = iterator.next();
                    if (questionconfig.getName().equals("cols")){
                        cols = questionconfig.getValue();
                    }
                }
                %>
                <textarea name="cols"><%=cols%></textarea>
            </td>
        </tr>
        <tr>
            <td valign="top">
                Respondent Can Select Many?
            </td>
            <td valign="top">
                <%
                String respondentcanselectmanySelected = "";
                for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
                    Questionconfig questionconfig = iterator.next();
                    logger.debug("found questionconfig: "+questionconfig.getName());
                    if (questionconfig.getName().equals("respondentcanselectmany")){
                        logger.debug("found respondentcanselectmany: "+questionconfig.getValue());
                        if(questionconfig.getValue().equals("1")){
                            respondentcanselectmanySelected = " checked";
                            logger.debug("respondentcanselectmanySelected: "+respondentcanselectmanySelected);
                        }
                    }
                }
                %>
                <input type="checkbox" name="respondentcanselectmany" value="1" <%=respondentcanselectmanySelected%>>
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