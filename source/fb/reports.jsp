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
<%@ page import="com.fbdblog.calc.Calctimeperiod" %>
<%@ page import="com.fbdblog.calc.CalculationFactory" %>
<%@ page import="com.fbdblog.calc.CalctimeperiodFactory" %>
<%@ include file="header.jsp" %>




<br/>
<fb:tabs>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=main' title='Track Stuff'/>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=charts' title='Da Charts'/>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=reports' title='Da Reports' selected='true'/>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=history' title='Yo History'/>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=friends' title='Le Friends' align='right'/>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=throwdowns' title='Throwdown!!!' align='right'/>
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
                for (Iterator<Questioncalc> iterator1=questioncalcs.iterator(); iterator1.hasNext();) {
                    Questioncalc questioncalc=iterator1.next();

                    Calctimeperiod calctimeperiod=CalctimeperiodFactory.getCalctimeperiodByIdStatic(questioncalc.getCalctimeperiodid());
                    com.fbdblog.calc.Calculation calculation=CalculationFactory.getCalculationByType(questioncalc.getCalculationtype());
                    if (calctimeperiod != null && calculation != null) {
                        //See if something's already recorded for this key
                        boolean foundValue=false;
                        double value = 0;
                        List<com.fbdblog.dao.Calculation> calcs=HibernateUtil.getSession().createCriteria(com.fbdblog.dao.Calculation.class)
                                .add(Restrictions.eq("calctimeperiodid", calctimeperiod.getId()))
                                .add(Restrictions.eq("calctimeperiodkey", calctimeperiod.getKey()))
                                .add(Restrictions.eq("calculationtype", calculation.getId()))
                                .add(Restrictions.eq("questionid", question.getQuestionid()))
                                .add(Restrictions.eq("userid", userSession.getUser().getUserid()))
                                .setCacheable(true)
                                .list();
                        for (Iterator<com.fbdblog.dao.Calculation> iterator2=calcs.iterator(); iterator2.hasNext();) {
                            com.fbdblog.dao.Calculation calcTmp = iterator2.next();
                            value = calcTmp.getValue();
                            foundValue = true;
                        }
                        if (foundValue){
                            %>
                            <img src="<%=BaseUrl.get(false)%>images/clear.gif" alt="" width="15" height="1"/><font style="font-size: 10px;"><a href="http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=reportsdetail&questioncalcid=<%=questioncalc.getQuestioncalcid()%>"><%=questioncalc.getName()%></a>: <%=Str.formatWithXDecimalPlaces(value, 2)%></font><br/>
                            <%
                        } else {
                            %>
                            <img src="<%=BaseUrl.get(false)%>images/clear.gif" alt="" width="15" height="1"/><font style="font-size: 10px;"><a href="http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=reportsdetail&questioncalcid=<%=questioncalc.getQuestioncalcid()%>"><%=questioncalc.getName()%></a>: na</font><br/>
                            <%
                        }
                    }
                }
            }
        }
    }

%>
</div>



<br/><br/>

<%@ include file="footer.jsp" %>