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
<%@ include file="header.jsp" %>




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
    for (Iterator<Question> iterator=userSession.getApp().getQuestions().iterator(); iterator.hasNext();) {
        Question question=iterator.next();
        //If it's a numeric question
        if (question.getDatatypeid()==DataTypeDecimal.DATATYPEID || question.getDatatypeid()==DataTypeInteger.DATATYPEID) {

            //Get the sysadmin-configured questioncalcs
            List<Questioncalc> questioncalcs=HibernateUtil.getSession().createCriteria(Questioncalc.class)
                    .add(Restrictions.eq("questionid", question.getQuestionid()))
                    .addOrder(Order.asc("questioncalcid"))
                    .setCacheable(true)
                    .list();
            //If there are questioncalcs to display
            if (questioncalcs!=null && questioncalcs.size()>0){
                %>
                <font style="font-size: 14px; font-weight: bold;"><%=question.getQuestion()%></font><br/>
                <%
            }
            for (Iterator<Questioncalc> iterator1=questioncalcs.iterator(); iterator1.hasNext();) {
                Questioncalc questioncalc=iterator1.next();

                //Get the user's most recent value for this
                List<Calculation> calculations=HibernateUtil.getSession().createCriteria(Calculation.class)
                        .add(Restrictions.eq("questionid", question.getQuestionid()))
                        .add(Restrictions.eq("userid", userSession.getUser().getUserid()))
                        .add(Restrictions.eq("calculationtype", questioncalc.getCalculationtype()))
                        .add(Restrictions.eq("calctimeperiodid", questioncalc.getCalctimeperiodid()))
                        .addOrder(Order.desc("recordeddate"))
                        .setMaxResults(1)
                        .setCacheable(true)
                        .list();

                for (Iterator<Calculation> iterator2=calculations.iterator(); iterator2.hasNext();) {
                    Calculation calculation=iterator2.next();
                    %>
                    <img src="<%=BaseUrl.get(false)%>images/clear.gif" alt="" width="15" height="1"/><font style="font-size: 10px;"><a href="http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=reportsdetail&questioncalcid=<%=questioncalc.getQuestioncalcid()%>"><%=questioncalc.getName()%></a>: <%=calculation.getValue()%></font><br/>
                    <%
                }
            }
        }
    }

%>
</div>



<br/><br/>

<%@ include file="footer.jsp" %>