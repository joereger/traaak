<%@ page import="com.fbdblog.qtype.util.AppTemplateProcessor" %>
<%@ page import="com.fbdblog.qtype.util.AppPostParser" %>
<%@ page import="com.fbdblog.qtype.util.SavePost" %>
<%@ page import="com.fbdblog.qtype.def.ComponentException" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="com.fbdblog.dao.Chart" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.fbdblog.util.Str" %>
<%@ page import="com.fbdblog.util.Num" %>
<%@ page import="com.fbdblog.systemprops.BaseUrl" %>
<%@ page import="com.fbdblog.dao.Question" %>
<%@ page import="com.fbdblog.dao.Questioncalc" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="com.fbdblog.dao.Calculation" %>
<%@ page import="com.fbdblog.chart.DataTypeDecimal" %>
<%@ page import="com.fbdblog.chart.DataTypeInteger" %>
<%@ page import="com.fbdblog.util.Time" %>
<%@ include file="header.jsp" %>

<%
    //Load the questioncalc requested
    Questioncalc questioncalc=null;
    if (request.getParameter("questioncalcid") != null && Num.isinteger(request.getParameter("questioncalcid"))) {
        questioncalc=Questioncalc.get(Integer.parseInt(request.getParameter("questioncalcid")));
    }
    if (questioncalc==null || questioncalc.getQuestionid()<=0){
        %><fb:redirect url="http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=reports" /><%
        return;
    }
%>


<br/>
<fb:tabs>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=main' title='Track Stuff'/>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=charts' title='Da Charts'/>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=reports' title='Da Reports' selected='true'/>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=history' title='Yo History'/>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=friends' title='Le Friends' align='right'/>
</fb:tabs>
<br/>


<div style="padding: 10px;">
<%
    //Get the question associated with this calculation
    Question question = Question.get(questioncalc.getQuestionid());
    %>
    <font style="font-size: 14px; font-weight: bold;"><%=question.getQuestion()%>: <%=questioncalc.getName()%></font>
    <br/><br/>
    <%
    //Get the user's most recent value for this
    List<Calculation> calculations=HibernateUtil.getSession().createCriteria(Calculation.class)
            .add(Restrictions.eq("questionid", question.getQuestionid()))
            .add(Restrictions.eq("userid", userSession.getUser().getUserid()))
            .add(Restrictions.eq("calculationtype", questioncalc.getCalculationtype()))
            .add(Restrictions.eq("calctimeperiodid", questioncalc.getCalctimeperiodid()))
            .addOrder(Order.desc("recordeddate"))
            .setCacheable(true)
            .list();
    %>
    <table cellpadding="5" cellspacing="1" border="0">
    <tr>
        <td bgcolor="#cccccc">
            <font style="font-size: 12px; font-weight: bold;">Calculated On</font>
        </td>
        <td bgcolor="#cccccc">
            <font style="font-size: 12px; font-weight: bold;">Value</font>
        </td>
    </tr>
    <%
    for (Iterator<Calculation> iterator2=calculations.iterator(); iterator2.hasNext();) {
        Calculation calculation=iterator2.next();
        %>
        <tr>
            <td bgcolor="#e6e6e6">
                <font style="font-size: 10px;"><%=Time.dateformatcompactwithtime(Time.getCalFromDate(calculation.getRecordeddate()))%></font>
            </td>
            <td>
                <font style="font-size: 12px; font-weight: bold;"><%=calculation.getValue()%></font>
            </td>
        </tr>
        <%
    }
    %>
    </table>
    <%


%>
</div>



<br/><br/>

<%@ include file="footer.jsp" %>