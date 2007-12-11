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
<%@ page import="com.fbdblog.throwdown.ThrowdownStatus" %>
<%@ page import="com.fbdblog.throwdown.ThrowdownPrivacy" %>
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
    if (!ThrowdownPrivacy.isok(throwdown)) {
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
            throwdown.setIscomplete(true);
            throwdown.setTouserid(userSession.getUser().getUserid());
            try{throwdown.save();}catch(Exception ex){logger.error("", ex);}
        }
    }
%>

<%
//Calculate who's winning, etc
%>


<%String selectedTab="throwdown";%>
<%@ include file="tabs.jsp" %>


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

<%
if (throwdown.getIscomplete() && throwdown.getIsaccepted()){
    %>
    <fb:success>
    <fb:message>This Throwdown is complete!</fb:message>
    There was a Winner.  There was a Loser.  It is the way of life.  Throwdown again soon!
    </fb:success>
    <%
}
%>

<%
if (throwdown.getIscomplete() && !throwdown.getIsaccepted()){
    %>
    <fb:success>
    <fb:message>This Throwdown is complete!</fb:message>
    The challengee never accepted the throwdown.
    </fb:success>
    <%
}
%>

<center>
    <table cellspacing="0" cellpadding="0" border="0">






        <tr>
            <td height="50" valign="top" width="75"><center><font style="font-size: 10px; font-weight: bold;"><%=fromFacebookUser.getFirst_name()%><br/><%=fromFacebookUser.getLast_name()%></font><br/><img src="<%=userSession.getFacebookUser().getPic_square()%>" alt="" width="50" height="50"/><br/><img src="<%=BaseUrl.get(false)%>images/throwdown-body.gif" alt="" width="85" height="189"/></center></td>
            <td rowspan="3" valign="top"><img src="<%=BaseUrl.get(false)%>images/clear.gif" alt="" width="10" height="1"/></td>
            <td rowspan="3" valign="top">
                    <%
                        String hisher = "his";
                        if (!fromFacebookUser.getSex().equals("male")){
                            hisher = "her";
                        }
                    %>
                    <center>
                    <img src="<%=BaseUrl.get(false)%>images/throwdown-vs-big.gif" alt="" width="128" height="149"/>
                    <br/>
                    <font style="font-size: 14px; font-weight: bold;"><%=fromFacebookUser.getFirst_name()%> <%=fromFacebookUser.getLast_name()%></font>
                    <br/>
                    <font style="font-size: 10px; font-weight: bold;">hereby challenges</font>
                    <br/>
                    <font style="font-size: 14px; font-weight: bold;"><%=toFacebookUser.getFirst_name()%> <%=toFacebookUser.getLast_name()%></font>
                    <br/>
                    <font style="font-size: 10px; font-weight: bold;">to a Throwdown called</font>
                    <br/>
                    <font style="font-size: 25px; font-weight: bold; color: #ff0000;"><%=throwdown.getName()%></font>
                    <br/>
                    <br/>
                    <font style="font-size: 10px; font-weight: bold;"><%=fromFacebookUser.getFirst_name()%> says that on</font>
                    <br/>
                    <font style="font-size: 10px; font-weight: bold;"><%=Time.dateformatcompactwithtime(Time.gmttousertime(throwdown.getEnddate(), userSession.getUser().getTimezoneid()))%></font>
                    <br/>
                    <font style="font-size: 10px; font-weight: bold;"><%=hisher%> value of</font>
                    <br/>
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
                                <font style="font-size: 12px; font-weight: bold;"><%=question.getQuestion()%></font>
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
                                <font style="font-size: 12px; font-weight: bold;"><%=questioncalc.getName()%></font>
                                <%
                            }
                        }
                    }
                    %>
                    <br/>
                    <font style="font-size: 10px; font-weight: bold;">will be</font>
                    <br/>
                    <%
                    if (throwdown.getIsgreaterthan()){
                        %>
                        <font style="font-size: 12px; font-weight: bold;">greater than</font>
                        <%
                    } else {
                        %>
                        <font style="font-size: 10px; font-weight: bold;">less than</font>
                        <%
                    }
                    %>
                    <br/>
                    <font style="font-size: 10px; font-weight: bold;"><%=toFacebookUser.getFirst_name()%> <%=toFacebookUser.getLast_name()%>'s.</font>
                    </center>

            </td>
            <td rowspan="3" valign="top"><img src="<%=BaseUrl.get(false)%>images/clear.gif" alt="" width="10" height="1"/></td>
            <td height="50" valign="top" width="75"><center><font style="font-size: 10px; font-weight: bold;"><%=toFacebookUser.getFirst_name()%><br/><%=toFacebookUser.getLast_name()%></font><br/><img src="<%=toFacebookUser.getPic_square()%>" alt="" width="50" height="50"/><br/><img src="<%=BaseUrl.get(false)%>images/throwdown-body.gif" alt="" width="85" height="189"/></center></td>
        </tr>


        <%
        //If the challenge has been accepted
        if (throwdown.getIsaccepted()){
            ThrowdownStatus throwdownStatus=new ThrowdownStatus(throwdown, userSession, fromFacebookUser, toFacebookUser, fromUser, toUser);
            %>
            <tr>
                <%
                String fromColor = "#ff6666";
                if (throwdownStatus.getIsFromWinning()){
                    fromColor = "#cccc99";
                }
                %>
                <td valign="top">
                    <div style="background: <%=fromColor%>; border: 1px solid #000000;">
                    <center>
                    <font style="font-size: 13px;"><%=throwdownStatus.getFromStatus()%></font>
                    <br/>
                    <font style="font-size: 18px;"><%=Str.formatWithXDecimalPlaces(throwdownStatus.getFromValue(), 2)%></font>
                    </center>
                    </div>
                </td>
                <%
                String toColor = "#ff6666";
                if (throwdownStatus.getIsToWinning()){
                    toColor = "#cccc99";
                }
                %>
                <td valign="top">
                    <div style="background: <%=toColor%>; border: 1px solid #000000;">
                    <center>
                    <font style="font-size: 13px;"><%=throwdownStatus.getToStatus()%></font>
                    <br/>
                    <font style="font-size: 18px;"><%=Str.formatWithXDecimalPlaces(throwdownStatus.getToValue(), 2)%></font>
                    </center>
                    </div>
                </td>
            </tr>
        <%}%>

    </table>
</center>

</div>



<br/><br/>

<%@ include file="footer.jsp" %>