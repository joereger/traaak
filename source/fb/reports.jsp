<%@ page import="com.fbdblog.qtype.util.AppTemplateProcessor" %>
<%@ page import="com.fbdblog.qtype.util.AppPostParser" %>
<%@ page import="com.fbdblog.qtype.util.SavePost" %>
<%@ page import="com.fbdblog.qtype.def.ComponentException" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.fbdblog.util.Str" %>
<%@ page import="com.fbdblog.util.Num" %>
<%@ page import="com.fbdblog.systemprops.BaseUrl" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="com.fbdblog.chart.DataTypeDecimal" %>
<%@ page import="com.fbdblog.chart.DataTypeInteger" %>
<%@ page import="com.fbdblog.calc.Calctimeperiod" %>
<%@ page import="com.fbdblog.calc.CalculationFactory" %>
<%@ page import="com.fbdblog.calc.CalctimeperiodFactory" %>
<%@ page import="com.fbdblog.calc.DoCalculationsAfterPost" %>
<%@ page import="com.fbdblog.dao.*" %>

<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Reports";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="header.jsp" %>

<%

    //Just a test... remove before production
//    Post post = null;
//    List<Post> posts=HibernateUtil.getSession().createCriteria(Post.class)
//            .add(Restrictions.eq("userid", Pagez.getUserSession().getUser().getUserid()))
//            .add(Restrictions.eq("appid", Pagez.getUserSession().getApp().getAppid()))
//            .addOrder(Order.desc("postid"))
//            .setMaxResults(1)
//            .setCacheable(true)
//            .list();
//    for (Iterator<Post> iterator=posts.iterator(); iterator.hasNext();) {
//         Post p=iterator.next();
//         post = p;
//    }
//    if (post!=null){
//        logger.debug("Starting calculations");
//        long startTime = Pagez.getElapsedTime();
//        DoCalculationsAfterPost.doCalculations(post);
//        long endTime = Pagez.getElapsedTime();
//        logger.debug("Done with calculations... "+(endTime-startTime)+" millis elapsed");
//    }

%>



<%String selectedTab="reports";%>
<%@ include file="tabs.jsp" %>


<div style="padding: 10px;">
<table cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td valign="top" width="390">
            <%
                for (Iterator<Question> iterator=Pagez.getUserSession().getApp().getQuestions().iterator(); iterator.hasNext();) {
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
                                    CalctimeperiodFactory ctpFactory=new CalctimeperiodFactory(Pagez.getUserSession().getUser(), Pagez.getUserSession().getApp());
                                    Calctimeperiod calctimeperiod=ctpFactory.getCalctimeperiodUnpopulated(questioncalc.getCalctimeperiodid());
                                    com.fbdblog.calc.Calculation calculation=CalculationFactory.getCalculationByType(questioncalc.getCalculationtype());
                                    if (calctimeperiod != null && calculation != null) {
                                        //See if something's already recorded for this key
                                        boolean foundValue=false;
                                        double value=0;
                                        List<com.fbdblog.dao.Calculation> calcs=HibernateUtil.getSession().createCriteria(com.fbdblog.dao.Calculation.class)
                                                .add(Restrictions.eq("calctimeperiodid", calctimeperiod.getId()))
                                                .add(Restrictions.eq("calctimeperiodkey", calctimeperiod.getKey()))
                                                .add(Restrictions.eq("calculationtype", calculation.getId()))
                                                .add(Restrictions.eq("questionid", question.getQuestionid()))
                                                .add(Restrictions.eq("userid", Pagez.getUserSession().getUser().getUserid()))
                                                .setCacheable(true)
                                                .list();
                                        for (Iterator<com.fbdblog.dao.Calculation> iterator2=calcs.iterator(); iterator2.hasNext();)
                                        {
                                            com.fbdblog.dao.Calculation calcTmp=iterator2.next();
                                            value=calcTmp.getValue();
                                            foundValue=true;
                                        }
                                        if (foundValue) {
                                            %>
                                            <img src="<%=BaseUrl.get(false)%>images/clear.gif" alt="" width="15" height="1"/><font style="font-size: 10px;"><a href="<%=linkToUrl%><%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=reportsdetail&questioncalcid=<%=questioncalc.getQuestioncalcid()%>"><%=questioncalc.getName()%></a>: <%=Str.formatWithXDecimalPlaces(value, 2)%></font><br/>
                                            <%
                                        } else {
                                            %>
                                            <img src="<%=BaseUrl.get(false)%>images/clear.gif" alt="" width="15" height="1"/><font style="font-size: 10px;"><a href="<%=linkToUrl%><%=Pagez.getUserSession().getApp().getFacebookappname()%>/?nav=reportsdetail&questioncalcid=<%=questioncalc.getQuestioncalcid()%>"><%=questioncalc.getName()%></a></font><br/>
                                            <%
                                        }
                                }
                            }
                        }
                    }
                }

            %>
        </td>
        <td valign="top" width="250">
            <%=Pagez.getUserSession().getApp().getAdhistoryright()%>
        </td>
    </tr>
</table>
</div>



<br/><br/>

<%@ include file="footer.jsp" %>