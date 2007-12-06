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
<%@ page import="com.fbdblog.dao.*" %>
<%@ page import="com.fbdblog.facebook.FacebookApiWrapper" %>
<%@ page import="com.fbdblog.util.Time" %>
<%@ page import="com.fbdblog.calc.CalcUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ include file="header.jsp" %>



<%
    //Load the post requested
    Throwdown throwdown=null;
    if (request.getParameter("throwdownid") != null && Num.isinteger(request.getParameter("throwdownid"))) {
        throwdown=Throwdown.get(Integer.parseInt(request.getParameter("throwdownid")));
    }
    if (throwdown==null || throwdown.getThrowdownid()==0){
        %>
        <fb:redirect url="http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=throwdowns" />
        <%
        return;
    }
%>

<%
    FacebookApiWrapper faw=new FacebookApiWrapper(userSession);
    User fromUser = User.get(throwdown.getFromuserid());
    FacebookUser fromFacebookUser = faw.getFacebookUserByUid(fromUser.getFacebookuid());
    FacebookUser toFacebookUser = faw.getFacebookUserByUid(throwdown.getTofacebookuid());
    User toUser = null;
    if (throwdown.getTouserid()>0){
        toUser = User.get(throwdown.getTouserid());
    }
%>

<%
//Capture a userid if I can
if (userSession.getFacebookUser().getUid().equals(throwdown.getTofacebookuid())){
    if (throwdown.getTouserid()!=userSession.getUser().getUserid()){
        logger.debug("Updating throwdown.touserid to use the logged-in user's value");
        throwdown.setTouserid(userSession.getUser().getUserid());
        try{throwdown.save();}catch(Exception ex){logger.error("", ex);}
    }
}
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("accept")) {
        if (userSession.getFacebookUser().getUid().equals(throwdown.getTofacebookuid())) {
            throwdown.setIsaccepted(true);
            throwdown.setIsdeclined(false);
            throwdown.setTouserid(userSession.getUser().getUserid());
            try {
                throwdown.save();
            } catch (Exception ex) {
                logger.error("", ex);
            }
            faw.postThrowdownAcceptToFeed(throwdown);
            ArrayList<Long> recipientIds=new ArrayList<Long>();
            recipientIds.add(Long.parseLong(fromFacebookUser.getUid()));
            StringBuffer message=new StringBuffer();
            message.append(" has accepted your throwdown challenge called <a href='http://apps.facebook.com/" + userSession.getApp().getFacebookappname() + "/?nav=throwdown&throwdownid=" + throwdown.getThrowdownid() + "'>" + throwdown.getName() + "</a>.  Now it is on!");
            String url=faw.sendNotification(recipientIds, message.toString(), message.toString());
            //@todo message may not work because not redirecting to url
        }
    }
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("decline")) {
        if (userSession.getFacebookUser().getUid().equals(throwdown.getTofacebookuid())){
            throwdown.setIsaccepted(false);
            throwdown.setIsdeclined(true);
            throwdown.setTouserid(userSession.getUser().getUserid());
            try{throwdown.save();}catch(Exception ex){logger.error("", ex);}
        }
    }
%>

<%
//Calculate who's winning, etc
%>


<br/>
<fb:tabs>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=main' title='Track Stuff'/>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=charts' title='Da Charts'/>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=reports' title='Da Reports'/>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=history' title='Yo History'/>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=friends' title='Le Friends' align='right'/>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=throwdowns' title='Throwdown!!!' align='right' selected='true'/>
</fb:tabs>
<br/>


<div style="padding: 10px;">

<%
if (!throwdown.getIsaccepted() && !throwdown.getIsdeclined()){
    if (!userSession.getFacebookUser().getUid().equals(throwdown.getTofacebookuid())){
        %>
        <fb:success>
        <fb:message><%=toFacebookUser.getFirst_name()%> <%=toFacebookUser.getLast_name()%> has not yet accepted this throwdown challenge</fb:message>
        Check back soon to see how things develop.
        </fb:success>
        <%
    } else {
        %>
        <fb:success>
        <fb:message><%=toFacebookUser.getFirst_name()%> <%=toFacebookUser.getLast_name()%>, do you accept this throwdown challenge?</fb:message>
        <a href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=throwdown&throwdownid=<%=throwdown.getThrowdownid()%>&action=accept'>Yes, I Accept</a>
        <br/>
        <a href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=throwdown&throwdownid=<%=throwdown.getThrowdownid()%>&action=decline'>No, I Decline</a>
        </fb:success>
        <%
    }
}
%>

<center>
    <table cellspacing="0" cellpadding="0" border="0">
        <tr>
            <td height="50" valign="top"><center><img src="<%=userSession.getFacebookUser().getPic_square()%>" alt="" width="50" height="50"/><br/><%=fromFacebookUser.getFirst_name()%> <%=fromFacebookUser.getLast_name()%></center></td>
            <td rowspan="3" valign="top"><img src="<%=BaseUrl.get(false)%>images/clear.gif" alt="" width="10" height="1"/></td>
            <td rowspan="3" valign="top">



                    <%
                        String hisher = "his";
                        if (!fromFacebookUser.getSex().equals("male")){
                            hisher = "her";
                        }
                    %>

                    <center><font style="font-size: 35px; color: #cccccc;">vs.</font></center>
                    <br/><br/>
                    <%=fromFacebookUser.getFirst_name()%> <%=fromFacebookUser.getLast_name()%>
                    <br/>
                    hereby challenges
                    <br/>
                    <%=toFacebookUser.getFirst_name()%> <%=toFacebookUser.getLast_name()%>
                    <br/>
                    to a Throwdown called
                    <br/>
                    <%=throwdown.getName()%>.
                    <br/>
                    <br/>
                    <%=fromFacebookUser.getFirst_name()%> wages that on
                    <br/>
                    <%=Time.dateformatcompactwithtime(Time.getCalFromDate(throwdown.getEnddate()))%>
                    <br/>
                    <%=hisher%> value of
                    <%
                    //It's a question
                    if (throwdown.getQuestionid()>0){
                        //Get the sysadmin-configured question
                        List<Question> questions=HibernateUtil.getSession().createCriteria(Question.class)
                                .add(Restrictions.eq("questionid", throwdown.getQuestioncalcid()))
                                .addOrder(Order.asc("questionid"))
                                .setCacheable(true)
                                .list();
                        //If there are questioncalcs to display
                        if (questions!=null && questions.size()>0){
                            for (Iterator<Question> iterator1=questions.iterator(); iterator1.hasNext();) {
                                Question question=iterator1.next();
                                %>
                                <%=question.getQuestion()%>
                                <%
                            }
                        }
                    //It's a questioncalc
                    } else if (throwdown.getQuestioncalcid()>0) {
                        //Get the sysadmin-configured questioncalcs
                        List<Questioncalc> questioncalcs=HibernateUtil.getSession().createCriteria(Questioncalc.class)
                                .add(Restrictions.eq("questioncalcid", throwdown.getQuestioncalcid()))
                                .addOrder(Order.asc("questioncalcid"))
                                .setCacheable(true)
                                .list();
                        //If there are questioncalcs to display
                        if (questioncalcs!=null && questioncalcs.size()>0){
                            for (Iterator<Questioncalc> iterator1=questioncalcs.iterator(); iterator1.hasNext();) {
                                Questioncalc questioncalc=iterator1.next();
                                %>
                                <%=questioncalc.getName()%>
                                <%
                            }
                        }
                    }
                    %>
                    <br/>
                    will be
                    <br/>
                    <%
                    if (throwdown.getIsgreaterthan()){
                        %>
                        greater than
                        <%
                    } else {
                        %>
                        less than
                        <%
                    }
                    %>
                    <br/>
                    <%=toFacebookUser.getFirst_name()%> <%=toFacebookUser.getLast_name()%>'s.

            </td>
            <td rowspan="3" valign="top"><img src="<%=BaseUrl.get(false)%>images/clear.gif" alt="" width="10" height="1"/></td>
            <td height="50" valign="top"><center><img src="<%=BaseUrl.get(false)%>images/facebook-50x50-placeholder.gif" alt="" width="50" height="50"/><br/><%=toFacebookUser.getFirst_name()%> <%=toFacebookUser.getLast_name()%></center></td>
        </tr>
        <tr>
            <td valign="top"><!--Body Left--></td>
            <td valign="top"><!--Body Right--></td>
        </tr>

        <%
        //If the challenge has been accepted
        if (throwdown.getIsaccepted()){
        %>
            <%
                double fromValue = 0;
                if (throwdown.getQuestionid()>0){
                    fromValue = CalcUtil.getValueForQuestion(Question.get(throwdown.getQuestionid()), fromUser);
                } else if (throwdown.getQuestioncalcid()>0) {
                    fromValue = CalcUtil.getValueForCalc(Questioncalc.get(throwdown.getQuestioncalcid()), fromUser);
                }
            %>
            <%
                double toValue = 0;
                if (toUser!=null){
                    if (throwdown.getQuestionid()>0){
                        toValue = CalcUtil.getValueForQuestion(Question.get(throwdown.getQuestionid()), toUser);
                    } else if (throwdown.getQuestioncalcid()>0) {
                        toValue = CalcUtil.getValueForCalc(Questioncalc.get(throwdown.getQuestioncalcid()), toUser);
                    }
                }
            %>
            <%
            String fromStatus = "";
            String toStatus = "";
            if (fromValue==toValue){
                fromStatus = "Tied.";
                toStatus = "Tied.";
            } else if (throwdown.getIsgreaterthan() && fromValue>toValue){
                fromStatus = "Winning!";
                toStatus = "Losing.";
            } else if (!throwdown.getIsgreaterthan() && fromValue<toValue){
                fromStatus = "Winning!";
                toStatus = "Losing.";
            } else {
                fromStatus = "Losing.";
                toStatus = "Winning!";
            }
            %>
            <tr>
                <td valign="top">
                    <font style="font-size: 13px;"><%=fromStatus%></font>
                    <br/>
                    <font style="font-size: 18px;"><%=Str.formatWithXDecimalPlaces(fromValue, 2)%></font>
                </td>
                <td valign="top">
                    <font style="font-size: 13px;"><%=toStatus%></font>
                    <br/>
                    <font style="font-size: 18px;"><%=Str.formatWithXDecimalPlaces(toValue, 2)%></font>
                </td>
            </tr>
        <%}%>

    </table>
</center>

</div>



<br/><br/>

<%@ include file="footer.jsp" %>