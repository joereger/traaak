<%@ page import="com.fbdblog.qtype.util.AppTemplateProcessor" %>
<%@ page import="com.fbdblog.qtype.util.AppPostParser" %>
<%@ page import="com.fbdblog.qtype.util.SavePost" %>
<%@ page import="com.fbdblog.qtype.def.ComponentException" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="com.fbdblog.util.Str" %>
<%@ page import="com.fbdblog.util.Num" %>
<%@ page import="com.fbdblog.systemprops.BaseUrl" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="com.fbdblog.chart.DataTypeDecimal" %>
<%@ page import="com.fbdblog.chart.DataTypeInteger" %>
<%@ page import="com.fbdblog.calc.Calctimeperiod" %>
<%@ page import="com.fbdblog.calc.CalculationFactory" %>
<%@ page import="com.fbdblog.calc.CalctimeperiodFactory" %>
<%@ page import="com.fbdblog.facebook.FacebookApiWrapper" %>
<%@ page import="java.util.*" %>
<%@ page import="com.fbdblog.ui.DateTimeHtmlInput" %>
<%@ page import="com.fbdblog.dao.*" %>
<%@ page import="com.fbdblog.util.ValidationException" %>
<%@ page import="com.fbdblog.util.Time"%>
<%@ include file="header.jsp" %>

<%
    String topOfPageMsg = "";
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("add")) {
        //Validation
        boolean haveerror=false;
        StringBuffer errMsg=new StringBuffer();
        if (request.getParameter("name") == null || request.getParameter("name").equals("")) {
            haveerror=true;
            errMsg.append("You must give your Throwdown a name.");
        }
        Calendar enddateCal = Calendar.getInstance();
        try {
            enddateCal=DateTimeHtmlInput.getValueFromRequest("enddate", "End Date", false, request);
        } catch (ValidationException vex) {
            haveerror = true;
            errMsg.append(vex.getErrorsAsSingleString());
        }
        //If it passes validation
        if (!haveerror){
            Throwdown throwdown=new Throwdown();
            throwdown.setAppid(userSession.getApp().getAppid());
            throwdown.setCreatedate(new Date());
            throwdown.setEnddate(enddateCal.getTime());
            throwdown.setFromuserid(userSession.getUser().getUserid());
            throwdown.setIsaccepted(false);
            throwdown.setIsdeclined(false);
            throwdown.setIscomplete(false);
            throwdown.setIsfromwinner(false);
            throwdown.setFromvalue(0);
            throwdown.setTovalue(0);
            if (request.getParameter("isgreaterthan") != null && request.getParameter("isgreaterthan").equals("1")) {
                throwdown.setIsgreaterthan(true);
            } else {
                throwdown.setIsgreaterthan(false);
            }
            throwdown.setName(request.getParameter("name"));
            throwdown.setTofacebookuid(request.getParameter("facebookuid"));
            //See if this user has a userid
            throwdown.setTouserid(0);
            List<User> existingusers=HibernateUtil.getSession().createCriteria(User.class)
                    .add(Restrictions.eq("facebookuid", request.getParameter("facebookuid")))
                    .setCacheable(true)
                    .list();
            for (Iterator<User> iterator=existingusers.iterator(); iterator.hasNext();) {
                User existinguser=iterator.next();
                throwdown.setTouserid(existinguser.getUserid());
            }
            //It's a questionid
            if (request.getParameter("calc") != null && request.getParameter("calc").indexOf("questionid")>-1) {
                String[] split=request.getParameter("calc").split("-");
                if (split.length == 2) {
                    if (Num.isinteger(split[1])) {
                        throwdown.setQuestionid(Integer.parseInt(split[1]));
                        throwdown.setQuestioncalcid(0);
                    }
                }
            }
            //It's a questioncalcid
            if (request.getParameter("calc") != null && request.getParameter("calc").indexOf("questioncalcid")>-1) {
                String[] split=request.getParameter("calc").split("-");
                if (split.length == 2) {
                    if (Num.isinteger(split[1])) {
                        throwdown.setQuestionid(0);
                        throwdown.setQuestioncalcid(Integer.parseInt(split[1]));
                    }
                }
            }
            //Save
            try {
                throwdown.save();
            } catch (Exception ex) {
                logger.error("", ex);
            }

            //Notify challengee
            FacebookApiWrapper faw = new FacebookApiWrapper(userSession);
            faw.postThrowdownChallengeToFeed(throwdown);
            ArrayList<Long> recipientIds = new ArrayList<Long>();
            recipientIds.add(Long.parseLong(throwdown.getTofacebookuid()));
            StringBuffer message = new StringBuffer();
            message.append(" has challenged you to a throwdown called <a href='http://apps.facebook.com/"+userSession.getApp().getFacebookappname()+"/?nav=throwdown&throwdownid="+throwdown.getThrowdownid()+"'>"+throwdown.getName()+"</a>.  You must now choose whether to accept or decline this challenge.  Be strong.");
            String url = faw.sendNotification(recipientIds, message.toString(), message.toString());
            if (url.equals("")){
                url = "http://apps.facebook.com/"+userSession.getApp().getFacebookappname()+"/?nav=throwdowns&action=addcomplete";
            }
            //Redirect to either the facebook-defined page or to the complete page
            %>
            <fb:redirect url="<%=url%>" />
            <%
            return;
        } else {
            StringBuffer tmp=new StringBuffer();
            tmp.append("<fb:error>\n" +
                    "     <fb:message>Oops!  You've got an error with your Throwdown.</fb:message>\n" +
                    "     "+errMsg.toString()+"\n" +
                    "</fb:error>");
            topOfPageMsg=tmp.toString();
        }
    }

%>


<%String selectedTab="throwdown";%>
<%@ include file="tabs.jsp" %>

<%
if (!topOfPageMsg.equals("")){
    %><%=topOfPageMsg%><%
}
%>


<div style="padding: 10px;">

<center>
    <table cellspacing="0" cellpadding="0" border="0">
        <tr>
            <td height="50" valign="top"><center><img src="<%=userSession.getFacebookUser().getPic_square()%>" alt="" width="50" height="50"/><br/><img src="<%=BaseUrl.get(false)%>images/throwdown-body.gif" alt="" width="85" height="189"/></center></td>
            <td rowspan="2" valign="top"><img src="<%=BaseUrl.get(false)%>images/clear.gif" alt="" width="10" height="1"/></td>
            <td rowspan="2" valign="top">
                <form action="">
                    <input type="hidden" name="nav" value="throwdownadd" />
                    <input type="hidden" name="action" value="add" />

                    <%
                        String hisher = "his";
                        if (!userSession.getFacebookUser().getSex().equals("male")){
                            hisher = "her";
                        }
                    %>

                    <center>
                    <img src="<%=BaseUrl.get(false)%>images/throwdown-vs-big.gif" alt="" width="128" height="149"/>
                    <br/>
                    <font style="font-size: 14px; font-weight: bold;"><%=userSession.getFacebookUser().getFirst_name()%> <%=userSession.getFacebookUser().getLast_name()%></font>
                    <br/>
                    <font style="font-size: 10px; font-weight: bold;">hereby challenges</font>
                    <br/>
                    <select name="facebookuid">
                    <%
                        //Will need this throughout the page
                        FacebookApiWrapper faw=new FacebookApiWrapper(userSession);
                        TreeSet<FacebookUser> friends=faw.getFriends();
                        //Create comma-separated list of friends who have app installed
                        for (Iterator it=friends.iterator(); it.hasNext();) {
                            FacebookUser facebookUser=(FacebookUser) it.next();
                            %>
                            <option value="<%=facebookUser.getUid()%>"><%=facebookUser.getFirst_name()%> <%=facebookUser.getLast_name()%></option>
                            <%
                        }
                    %>
                    </select>
                    <br/>
                    <font style="font-size: 10px; font-weight: bold;">to a Throwdown called</font>
                    <br/>
                    <input type="text" name="name" value="<%=userSession.getApp().getTitle()%> Throwdown Challenge">.
                    <br/>
                    <br/>
                    <font style="font-size: 10px; font-weight: bold;"><%=userSession.getFacebookUser().getFirst_name()%> says that on</font>
                    <br/>
                    <%=DateTimeHtmlInput.getHtml("enddate", Time.xDaysAgoStart(Calendar.getInstance(), -7), "", "")%>
                    <br/>
                    <font style="font-size: 10px; font-weight: bold;"><%=hisher%> value of</font>
                    <div style="text-align: left;">
                    <%
                    for (Iterator<Question> iterator=userSession.getApp().getQuestions().iterator(); iterator.hasNext();) {
                        Question question=iterator.next();
                        if (question.getDatatypeid()==DataTypeDecimal.DATATYPEID || question.getDatatypeid()==DataTypeInteger.DATATYPEID) {
                            %>
                            <br/><input type="radio" name="calc" value="questionid-<%=question.getQuestionid()%>"> <%=question.getQuestion()%>
                            <%
                            //Get the sysadmin-configured questioncalcs
                            List<Questioncalc> questioncalcs=HibernateUtil.getSession().createCriteria(Questioncalc.class)
                                    .add(Restrictions.eq("questionid", question.getQuestionid()))
                                    .addOrder(Order.asc("questioncalcid"))
                                    .setCacheable(true)
                                    .list();
                            //If there are questioncalcs to display
                            if (questioncalcs!=null && questioncalcs.size()>0){
                                for (Iterator<Questioncalc> iterator1=questioncalcs.iterator(); iterator1.hasNext();) {
                                    Questioncalc questioncalc=iterator1.next();
                                    %>
                                    <br/><img src="<%=BaseUrl.get(false)%>images/clear.gif" alt="" width="15" height="1"/><input type="radio" name="calc" value="questioncalcid-<%=questioncalc.getQuestioncalcid()%>"> <%=questioncalc.getName()%>
                                    <%
                                }
                            }
                        }
                    }
                    %>
                    </div>
                    <br/>
                    <font style="font-size: 10px; font-weight: bold;">will be</font>
                    <br/>
                    <select name="isgreaterthan">
                        <option value="1">Greater Than</option>
                        <option value="0">Less Than</option>
                    </select>
                    <br/>
                    <font style="font-size: 10px; font-weight: bold;"><%=hisher%> competitor's.</font>
                    <br/><br/>
                    <input id="sendbutton" type="submit" value="Throwdown!!!" />

                    </center>
                </form>
            </td>
            <td rowspan="2" valign="top"><img src="<%=BaseUrl.get(false)%>images/clear.gif" alt="" width="10" height="1"/></td>
            <td height="50" valign="top"><center><img src="<%=BaseUrl.get(false)%>images/facebook-50x50-placeholder.gif" alt="" width="50" height="50"/><br/><img src="<%=BaseUrl.get(false)%>images/throwdown-body.gif" alt="" width="85" height="189"/></center></td>
        </tr>
    </table>
</center>

</div>



<br/><br/>

<%@ include file="footer.jsp" %>