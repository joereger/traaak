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
<%@ page import="com.fbdblog.dao.Questioncalc" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="com.fbdblog.chart.SysadminAutoCalcCreator" %>
<%@ page import="com.fbdblog.chart.DataTypeDecimal" %>
<%@ page import="com.fbdblog.chart.DataTypeInteger" %>
<%@ page import="com.fbdblog.qtype.util.AppTemplateProcessor" %>
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
        app.setTitle("Track");
        app.setFacebookapikey("");
        app.setFacebookapisecret("");
        app.setFacebookappname("track");
        app.setDescription("");
        app.setPrimarychartid(0);
        app.setMinifeedtemplate("tracked some stuff with <a href='<$url$>'><$appname$></a>");
        app.setCrosspromote(false);
        app.setAdglobalheader("");
        app.setAdhistoryright("");
        app.setAdpostsave("");
        app.setAdunderchart("");
        app.setFacebookinfinitesessionkey("");
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
    app.setFacebookinfinitesessionkey(request.getParameter("facebookinfinitesessionkey"));
    app.setAdglobalheader(request.getParameter("adglobalheader"));
    app.setAdhistoryright(request.getParameter("adhistoryright"));
    app.setAdpostsave(request.getParameter("adpostsave"));
    app.setAdunderchart(request.getParameter("adunderchart"));
    try{app.save();}catch(Exception ex){logger.error("",ex);}
    out.print("Saved.<br/>");
}
%>

<%
if (request.getParameter("action")!=null && request.getParameter("action").equals("deletequestion")){
    if (request.getParameter("questionid") != null && Num.isinteger(request.getParameter("questionid"))) {
        Question question = Question.get(Integer.parseInt(request.getParameter("questionid")));
        HibernateUtil.getSession().createQuery("delete Calculation c where c.questionid='"+question.getQuestionid()+"'").executeUpdate();
        HibernateUtil.getSession().createQuery("delete Questioncalc c where c.questionid='"+question.getQuestionid()+"'").executeUpdate();
        HibernateUtil.getSession().createQuery("delete Postanswer c where c.questionid='"+question.getQuestionid()+"'").executeUpdate();
        HibernateUtil.getSession().createQuery("delete Questionconfig c where c.questionid='"+question.getQuestionid()+"'").executeUpdate();
        HibernateUtil.getSession().createQuery("delete Questionuserconfig c where c.questionid='"+question.getQuestionid()+"'").executeUpdate();
        HibernateUtil.getSession().createQuery("delete Throwdown c where c.questionid='"+question.getQuestionid()+"'").executeUpdate();
        HibernateUtil.getSession().createQuery("delete Chartyaxis c where c.yquestionid='"+question.getQuestionid()+"'").executeUpdate();
        HibernateUtil.getSession().createQuery("delete Chart c where c.xquestionid='"+question.getQuestionid()+"'").executeUpdate();
        try{question.delete();}catch(Exception ex){logger.error("",ex);}
        out.print("Deleted question.<br/>");
    }
}
%>

<%
if (request.getParameter("action")!=null && request.getParameter("action").equals("deletechart")){
    if (request.getParameter("chartid") != null && Num.isinteger(request.getParameter("chartid"))) {
        Chart chart = Chart.get(Integer.parseInt(request.getParameter("chartid")));
        try{chart.delete();}catch(Exception ex){logger.error("",ex);}
        out.print("Deleted chart.<br/>");
    }
}
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("clearchartcache")) {
        try {
            ClearCache.clearCacheForApp(app.getAppid());
        } catch (Exception ex) {
            logger.error("",ex);
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
                logger.error("",ex);
            }
        }
        out.print("Primary chart has been set.<br/>");
    }
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("allpossiblecalcs")) {
        if (request.getParameter("questionid")!=null && Num.isinteger(request.getParameter("questionid"))) {
            Question question=Question.get(Integer.parseInt(request.getParameter("questionid")));
            if (question!=null && question.getQuestionid()>0) {
                SysadminAutoCalcCreator.createAllPossibleCalcs(question);
                out.print("Calcs created.<br/>");
            }
        }
    }
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("commoncalcs")) {
        if (request.getParameter("questionid")!=null && Num.isinteger(request.getParameter("questionid"))) {
            Question question=Question.get(Integer.parseInt(request.getParameter("questionid")));
            if (question!=null && question.getQuestionid()>0) {
                SysadminAutoCalcCreator.createCommonCalcs(question);
                out.print("Calcs created.<br/>");
            }
        }
    }
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("alltotalcalcs")) {
        if (request.getParameter("questionid")!=null && Num.isinteger(request.getParameter("questionid"))) {
            Question question=Question.get(Integer.parseInt(request.getParameter("questionid")));
            if (question!=null && question.getQuestionid()>0) {
                SysadminAutoCalcCreator.createAllTotalCalcs(question);
                out.print("Calcs created.<br/>");
            }
        }
    }
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("allaveragecalcs")) {
        if (request.getParameter("questionid")!=null && Num.isinteger(request.getParameter("questionid"))) {
            Question question=Question.get(Integer.parseInt(request.getParameter("questionid")));
            if (question!=null && question.getQuestionid()>0) {
                SysadminAutoCalcCreator.createAllAverageCalcs(question);
                out.print("Calcs created.<br/>");
            }
        }
    }
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("alldeltaabscalcs")) {
        if (request.getParameter("questionid")!=null && Num.isinteger(request.getParameter("questionid"))) {
            Question question=Question.get(Integer.parseInt(request.getParameter("questionid")));
            if (question!=null && question.getQuestionid()>0) {
                SysadminAutoCalcCreator.createAllDeltaAbsCalcs(question);
                out.print("Calcs created.<br/>");
            }
        }
    }
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("alldeltapctcalcs")) {
        if (request.getParameter("questionid")!=null && Num.isinteger(request.getParameter("questionid"))) {
            Question question=Question.get(Integer.parseInt(request.getParameter("questionid")));
            if (question!=null && question.getQuestionid()>0) {
                SysadminAutoCalcCreator.createAllDeltaPctCalcs(question);
                out.print("Calcs created.<br/>");
            }
        }
    }
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("deletequestioncalc")) {
        if (request.getParameter("questionid")!=null && Num.isinteger(request.getParameter("questionid"))) {
            Question question=Question.get(Integer.parseInt(request.getParameter("questionid")));
            if (question!=null && question.getQuestionid()>0) {
                if (request.getParameter("questioncalcid")!=null && Num.isinteger(request.getParameter("questioncalcid"))) {
                    Questioncalc questioncalc = Questioncalc.get(Integer.parseInt(request.getParameter("questioncalcid")));
                    if (questioncalc!=null && questioncalc.getQuestioncalcid()>0){
                        HibernateUtil.getSession().createQuery("delete Calculation c where c.questionid='"+questioncalc.getQuestionid()+"' and c.calculationtype='"+questioncalc.getCalculationtype()+"' and c.calctimeperiodid='"+questioncalc.getCalctimeperiodid()+"'").executeUpdate();
                        HibernateUtil.getSession().createQuery("delete Throwdown c where c.questioncalcid='"+questioncalc.getQuestioncalcid()+"'").executeUpdate();
                        try{questioncalc.delete();}catch(Exception ex){logger.error("", ex);}
                        out.print("Calc deleted.<br/>");
                    }
                }
            }
        }
    }
%>

<font class="pagetitle"><a href='appdetail.jsp?appid=<%=app.getAppid()%>'><%=app.getTitle()%></a></font>
<br/><br/>
<table cellpadding="0" cellspacing="10" border="0">
    <tr>
        <td valign="top">
            <!-- Top Left -->
            <form action="appdetail.jsp" method="post">
                <input type="hidden" name="appid" value="<%=app.getAppid()%>">
                <input type="hidden" name="action" value="save">
                <table cellpadding="0" cellspacing="1" border="0">
                    <tr>
                        <td valign="top">
                            <b>App Title</b>
                        </td>
                        <td valign="top">
                            <input type="text" name="title" value="<%=app.getTitle()%>" size="25" maxlength="255">
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <b>FB App Name</b>
                        </td>
                        <td valign="top">
                            <input type="text" name="facebookappname" value="<%=app.getFacebookappname()%>" size="25" maxlength="255">
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <b>FB Api Key</b>
                        </td>
                        <td valign="top">
                            <input type="text" name="facebookapikey" value="<%=app.getFacebookapikey()%>" size="36" maxlength="255">
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <b>FB Api Secret</b>
                        </td>
                        <td valign="top">
                            <input type="text" name="facebookapisecret" value="<%=app.getFacebookapisecret()%>" size="36" maxlength="255">
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <b>FB Inf Sess Key</b>
                        </td>
                        <td valign="top">
                            <input type="text" name="facebookinfinitesessionkey" value="<%=app.getFacebookinfinitesessionkey()%>" size="36" maxlength="255">
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <b>Description</b>
                        </td>
                        <td valign="top">
                            <textarea name="description" cols="36" rows="5" style="font-size: 10px;"><%=app.getDescription()%></textarea>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <b>MiniFeed<br/>Template</b>
                        </td>
                        <td valign="top">
                            <textarea name="minifeedtemplate" cols="36" rows="5" style="font-size: 10px;"><%=app.getMinifeedtemplate()%></textarea><br/>
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
                            <b>Adglobalheader</b>
                        </td>
                        <td valign="top">
                            <input type="text" name="adglobalheader" value="<%=Str.cleanForHtml(app.getAdglobalheader())%>" size="36">
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <b>Adpostsave</b>
                        </td>
                        <td valign="top">
                            <input type="text" name="adpostsave" value="<%=Str.cleanForHtml(app.getAdpostsave())%>" size="36">
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <b>Adhistoryright</b>
                        </td>
                        <td valign="top">
                            <input type="text" name="adhistoryright" value="<%=Str.cleanForHtml(app.getAdhistoryright())%>" size="36">
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <b>Adunderchart</b>
                        </td>
                        <td valign="top">
                            <input type="text" name="adunderchart" value="<%=Str.cleanForHtml(app.getAdunderchart())%>" size="36">
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
        </td>
        <td valign="top">
            <!-- Top Right -->
            <b>Charts:</b>
            <br/>
            <%
                List<Chart> charts = HibernateUtil.getSession().createCriteria(Chart.class)
                        .add(Restrictions.eq("appid", app.getAppid()))
                        .setCacheable(false)
                        .list();
                for (Iterator<Chart> iterator = charts.iterator(); iterator.hasNext();) {
                    Chart chart = (Chart)iterator.next();
                    %>
                    <a href="appdetail-chart.jsp?appid=<%=app.getAppid()%>&chartid=<%=chart.getChartid()%>"><%=chart.getName()%></a>
                    <%
                    if (chart.getChartid()==app.getPrimarychartid()){
                        %>
                        (primary)
                        <%
                    } else {
                        %>
                        (<a href='appdetail.jsp?action=setprimarychartid&appid=<%=app.getAppid()%>&primarychartid=<%=chart.getChartid()%>'>make primary</a>) (<a href='appdetail.jsp?action=deletechart&appid=<%=app.getAppid()%>&chartid=<%=chart.getChartid()%>'>del</a>)
                        <%
                    }
                    %>
                    <br/>
                    <%
                }
            %>
            <br/><a href="appdetail-chart.jsp?action=newchart&appid=<%=app.getAppid()%>">+ Add Chart</a>
            <br/><a href='appdetail.jsp?action=clearchartcache&appid=<%=app.getAppid()%>'>- Clear Chart Cache For App</a>
        </td>
    </tr>
    <tr>
        <td valign="top">
            <!-- Bottom Left -->
            <b>Questions:</b>
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
                    } else if (question.getComponenttype() == DropdownComplex.ID) {
                        comptypefilename = "dropdowncomplex";
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
                    List<Questioncalc> questioncalcs=HibernateUtil.getSession().createCriteria(Questioncalc.class)
                            .add(Restrictions.eq("questionid", question.getQuestionid()))
                            .addOrder(Order.asc("questioncalcid"))
                            .setCacheable(true)
                            .list();
                    for (Iterator<Questioncalc> iterator1=questioncalcs.iterator(); iterator1.hasNext();) {
                        Questioncalc questioncalc = iterator1.next();
                        %>
                        <img src="/images/clear.gif" alt="" width="15" height="1"/><font style="font-size: 9px;"><a href="appdetail.jsp?appid=<%=app.getAppid()%>&questionid=<%=question.getQuestionid()%>&questioncalcid=<%=questioncalc.getQuestioncalcid()%>"><%=questioncalc.getName()%></a> (<a href="appdetail.jsp?appid=<%=app.getAppid()%>&questionid=<%=question.getQuestionid()%>&questioncalcid=<%=questioncalc.getQuestioncalcid()%>&action=deletequestioncalc">del</a>)</font><br/>
                        <%
                    }
                    if (question.getDatatypeid() == DataTypeDecimal.DATATYPEID || question.getDatatypeid() == DataTypeInteger.DATATYPEID) {
                        %>
                        <img src="/images/clear.gif" alt="" width="15" height="1"/><font style="font-size: 9px;"><a href="appdetail-calc.jsp?appid=<%=app.getAppid()%>&questionid=<%=question.getQuestionid()%>&action=newcalc">+ Add Calc</a></font><br/>
                        <img src="/images/clear.gif" alt="" width="15" height="1"/><font style="font-size: 9px;"><a href="appdetail.jsp?appid=<%=app.getAppid()%>&questionid=<%=question.getQuestionid()%>&action=commoncalcs">+ Add Common Calcs</a></font><br/>
                        <img src="/images/clear.gif" alt="" width="15" height="1"/><font style="font-size: 9px;"><a href="appdetail.jsp?appid=<%=app.getAppid()%>&questionid=<%=question.getQuestionid()%>&action=allpossiblecalcs">+ Add All Possible Calcs</a></font><br/>
                        <img src="/images/clear.gif" alt="" width="15" height="1"/><font style="font-size: 9px;"><a href="appdetail.jsp?appid=<%=app.getAppid()%>&questionid=<%=question.getQuestionid()%>&action=alltotalcalcs">+ Add All Total Calcs</a></font><br/>
                        <img src="/images/clear.gif" alt="" width="15" height="1"/><font style="font-size: 9px;"><a href="appdetail.jsp?appid=<%=app.getAppid()%>&questionid=<%=question.getQuestionid()%>&action=allaveragecalcs">+ Add All Average Calcs</a></font><br/>
                        <img src="/images/clear.gif" alt="" width="15" height="1"/><font style="font-size: 9px;"><a href="appdetail.jsp?appid=<%=app.getAppid()%>&questionid=<%=question.getQuestionid()%>&action=alldeltaabscalcs">+ Add All DeltaAbs Calcs</a></font><br/>
                        <img src="/images/clear.gif" alt="" width="15" height="1"/><font style="font-size: 9px;"><a href="appdetail.jsp?appid=<%=app.getAppid()%>&questionid=<%=question.getQuestionid()%>&action=alldeltapctcalcs">+ Add All DeltaPct Calcs</a></font><br/>
                        <%
                    }
                }
            %>
            <br/><a href='appdetail-question-textbox.jsp?action=newquestion&appid=<%=app.getAppid()%>'>+ Add Textbox</a>
            <br/><a href='appdetail-question-checkboxes.jsp?action=newquestion&appid=<%=app.getAppid()%>'>+ Add Checkboxes</a>
            <br/><a href='appdetail-question-dropdown.jsp?action=newquestion&appid=<%=app.getAppid()%>'>+ Add Dropdown</a>
            <br/><a href='appdetail-question-dropdowncomplex.jsp?action=newquestion&appid=<%=app.getAppid()%>'>+ Add Dropdown Complex</a>
            <br/><a href='appdetail-question-essay.jsp?action=newquestion&appid=<%=app.getAppid()%>'>+ Add Essay</a>
            <br/><a href='appdetail-question-matrix.jsp?action=newquestion&appid=<%=app.getAppid()%>'>+ Add Matrix</a>
            <br/><a href='appdetail-question-range.jsp?action=newquestion&appid=<%=app.getAppid()%>'>+ Add Range</a>
            <br/><a href='appdetail-question-timeperiod.jsp?action=newquestion&appid=<%=app.getAppid()%>'>+ Add Timeperiod</a>
            <br/><br/>
        </td>
        <td valign="top">
            <!-- Bottom Right -->
            <b>Sample Input Form:</b>
            <br/>
            <%
                AppTemplateProcessor atp=new AppTemplateProcessor(app, userSession.getUser(), null);
                out.print(atp.getHtmlForInput(false));
            %>
        </td>
    </tr>
</table>
<br/>



<%@ include file="footer.jsp" %>