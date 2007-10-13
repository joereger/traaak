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
<%@ page import="com.fbdblog.chart.chartcache.ClearCache" %>
<%@ page import="com.fbdblog.util.Str" %>
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
        app.setPrimarychartid(0);
        app.setMinifeedtemplate("tracked some data with <a href='<$url$>'><$appname$></a>");
    }
%>

<%
if (request.getParameter("action")!=null && request.getParameter("action").equals("save")){
    app.setTitle(request.getParameter("title"));
    app.setDescription(request.getParameter("description"));
    app.setMinifeedtemplate(request.getParameter("minifeedtemplate"));
    app.setFacebookappname(request.getParameter("facebookappname"));
    app.setFacebookapikey(request.getParameter("facebookapikey"));
    app.setFacebookapisecret(request.getParameter("facebookapisecret"));
    try{app.save();}catch(Exception ex){logger.error(ex);}
    out.print("Saved.<br/>");
}
%>

<%
if (request.getParameter("action")!=null && request.getParameter("action").equals("deletequestion")){
    if (request.getParameter("questionid") != null && Num.isinteger(request.getParameter("questionid"))) {
        Question question = Question.get(Integer.parseInt(request.getParameter("questionid")));
        try{question.delete();}catch(Exception ex){logger.error(ex);}
        out.print("Deleted question.<br/>");
    }
}
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("clearchartcache")) {
        try {
            ClearCache.clearCacheForApp(app.getAppid());
        } catch (Exception ex) {
            logger.error(ex);
        }
        out.print("App chart cache cleared.<br/>");
    }
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("setprimarychartid")) {
        if (request.getParameter("primarychartid") != null && Num.isinteger(request.getParameter("primarychartid"))) {
            try {
                app.setPrimarychartid(Integer.parseInt(request.getParameter("primarychartid")));
                app.save();
            } catch (Exception ex) {
                logger.error(ex);
            }
        }
        out.print("Primary chart has been set.<br/>");
    }
%>

App Detail
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
                MiniFeed Template
            </td>
            <td valign="top">
                <textarea name="minifeedtemplate"><%=app.getMinifeedtemplate()%></textarea><br/>
                <font style="font-size: 9px; font-family: arial;">
                &lt;$url$><br/>
                &lt;$appname$><br/>
                <%
                    List<Question> minifeedquestions = HibernateUtil.getSession().createCriteria(Question.class)
                            .add(Restrictions.eq("appid", app.getAppid()))
                            .setCacheable(false)
                            .list();
                    for (Iterator<Question> iterator = minifeedquestions.iterator(); iterator.hasNext();) {
                        Question question = iterator.next();
                        %>
                        &lt;$questionid.<%=question.getQuestionid()%>$> <%=Str.truncateString(question.getQuestion(), 25)%> <br/>
                        <%
                    }
                %>
                </font>
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
<table cellpadding="5" cellspacing="0" border="0">
    <tr>
        <td valign="top" width="50%">


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
                    String req = "";
                    if (question.getIsrequired()){
                        req = "*";
                    }
                    %>
                    <a href="appdetail-question-<%=comptypefilename%>.jsp?appid=<%=app.getAppid()%>&questionid=<%=question.getQuestionid()%>"><%=question.getQuestion()%></a><%=req%> (<a href='appdetail.jsp?action=deletequestion&appid=<%=app.getAppid()%>&questionid=<%=question.getQuestionid()%>'>del</a>)<br/>
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

        </td>
        <td valign="top">

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
                    <a href="appdetail-chart.jsp?appid=<%=app.getAppid()%>&chartid=<%=chart.getChartid()%>"><%=chart.getChartid()%>: <%=chart.getName()%></a>
                    <%
                    if (chart.getChartid()==app.getPrimarychartid()){
                        %>
                        (primary)    
                        <%
                    } else {
                        %>
                        (<a href='appdetail.jsp?action=setprimarychartid&appid=<%=app.getAppid()%>&primarychartid=<%=chart.getChartid()%>'>make primary</a>)
                        <%
                    }
                    %>
                    <br/>
                    <%
                }
            %>
            <br/>
            <br/><a href="appdetail-chart.jsp?action=newchart&appid=<%=app.getAppid()%>">+ Add Chart</a>
            <br/><a href='appdetail.jsp?action=clearchartcache&appid=<%=app.getAppid()%>'>- Clear Chart Cache For App</a>
        </td>
    </tr>
</table>


<%@ include file="footer.jsp" %>