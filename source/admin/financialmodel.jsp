<%@ page import="com.fbdblog.util.Num" %>
<%@ page import="com.fbdblog.util.Str" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Calendar" %>
<%@ include file="header.jsp" %>


<font class="pagetitle">Financial Model</font>
<br/><br/>

<%
    //Set date to last month so we get a whole month
    Calendar now=Calendar.getInstance();
    int currentyear=now.get(Calendar.YEAR - 1);
    int currentmonth=(now.get(Calendar.MONTH) + 1) - 1;
    if (currentmonth==0){
        currentmonth = 12;
    }
    //Total
    double totalusersinsystemnow=((Long) HibernateUtil.getSession().createQuery("select count(*) from User").uniqueResult()).doubleValue();
    //Avgimp/install
    Double avgimpressionsperinstalllastmonth=0.0;
    Object obj1=HibernateUtil.getSession().createQuery("select avg(impressions) from Impression where year='" + currentyear + "' and month='" + currentmonth + "'").uniqueResult();
    if (obj1 != null) {
        avgimpressionsperinstalllastmonth=((Double) obj1);
    }
    //totalimpressions last month
    Double totalimpressionslastmonth=0.0;
    Object obj2=HibernateUtil.getSession().createQuery("select sum(impressions) from Impression where year='" + currentyear + "' and month='" + currentmonth + "'").uniqueResult();
    if (obj2 != null) {
        totalimpressionslastmonth=((Long) obj2).doubleValue();
    }
    //total installs
    Double totalinstallsinsystemnow=0.0;
    Object obj3=HibernateUtil.getSession().createQuery("select count(*) from Userappstatus where isinstalled=true").uniqueResult();
    if (obj3 != null) {
        totalinstallsinsystemnow=((Long) obj3).doubleValue();
    }
    //Installs per user
    double appsperuser = 0;
    if (totalusersinsystemnow>0){
        appsperuser = totalinstallsinsystemnow/totalusersinsystemnow;
    }


    //Defaults for others
    double initialinstalls = totalinstallsinsystemnow;
    double monthlyinvestment=1000;
    double costtoattractanewuser=.33;
    //@todo calculate this from userappactivity table... heavy computation though... will need to add App.monthlyuninstallpercentage and a scheduled job
    double monthlyuninstallpercentage=30;
    double percentofprofittoreinvesteachmonth=90;
    double avgimpressionssperinstall=avgimpressionsperinstalllastmonth;
    if (avgimpressionssperinstall<=0){
        avgimpressionssperinstall = 60;   
    }
    double cpmtoestimatewith=1;

%>

<%
if (request.getParameter("action")!=null && request.getParameter("action").equals("calculate")) {
    if (request.getParameter("initialinstalls")!=null && Num.isdouble(request.getParameter("initialinstalls"))) {
        initialinstalls = Double.parseDouble(request.getParameter("initialinstalls"));
    }
    if (request.getParameter("appsperuser")!=null && Num.isdouble(request.getParameter("appsperuser"))) {
        appsperuser = Double.parseDouble(request.getParameter("appsperuser"));
    }
    if (request.getParameter("monthlyinvestment")!=null && Num.isdouble(request.getParameter("monthlyinvestment"))) {
        monthlyinvestment = Double.parseDouble(request.getParameter("monthlyinvestment"));
    }
    if (request.getParameter("costtoattractanewuser")!=null && Num.isdouble(request.getParameter("costtoattractanewuser"))) {
        costtoattractanewuser = Double.parseDouble(request.getParameter("costtoattractanewuser"));
    }
    if (request.getParameter("monthlyuninstallpercentage")!=null && Num.isdouble(request.getParameter("monthlyuninstallpercentage"))) {
        monthlyuninstallpercentage = Double.parseDouble(request.getParameter("monthlyuninstallpercentage"));
    }
    if (request.getParameter("percentofprofittoreinvesteachmonth")!=null && Num.isdouble(request.getParameter("percentofprofittoreinvesteachmonth"))) {
        percentofprofittoreinvesteachmonth = Double.parseDouble(request.getParameter("percentofprofittoreinvesteachmonth"));
    }
    if (request.getParameter("avgimpressionssperinstall")!=null && Num.isdouble(request.getParameter("avgimpressionssperinstall"))) {
        avgimpressionssperinstall = Double.parseDouble(request.getParameter("avgimpressionssperinstall"));
    }
    if (request.getParameter("cpmtoestimatewith")!=null && Num.isdouble(request.getParameter("cpmtoestimatewith"))) {
        cpmtoestimatewith = Double.parseDouble(request.getParameter("cpmtoestimatewith"));
    }
}
%>

<form action="financialmodel.jsp" method="get">
<input type="hidden" name="action" value="calculate">
<table cellpadding="3" cellspacing="0" border="0">
<tr>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">initialinstalls</font></td>
    <td valign="top"><input type="text" name="initialinstalls" value="<%=initialinstalls%>"></td>
</tr>
<tr>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">appsperuser</font></td>
    <td valign="top"><input type="text" name="appsperuser" value="<%=appsperuser%>"></td>
</tr>
<tr>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">monthlyinvestment</font></td>
    <td valign="top"><input type="text" name="monthlyinvestment" value="<%=monthlyinvestment%>"></td>
</tr>
<tr>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">costtoattractanewuser</font></td>
    <td valign="top"><input type="text" name="costtoattractanewuser" value="<%=costtoattractanewuser%>"></td>
</tr>
<tr>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">monthlyuninstallpercentage</font></td>
    <td valign="top"><input type="text" name="monthlyuninstallpercentage" value="<%=monthlyuninstallpercentage%>"></td>
</tr>
<tr>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">percentofprofittoreinvesteachmonth</font></td>
    <td valign="top"><input type="text" name="percentofprofittoreinvesteachmonth" value="<%=percentofprofittoreinvesteachmonth%>"></td>
</tr>
<tr>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">avgimpressionssperinstall</font></td>
    <td valign="top"><input type="text" name="avgimpressionssperinstall" value="<%=avgimpressionssperinstall%>"></td>
</tr>
<tr>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">cpmtoestimatewith</font></td>
    <td valign="top"><input type="text" name="cpmtoestimatewith" value="<%=cpmtoestimatewith%>"></td>
</tr>
<tr>
    <td valign="top"><font style="font-family: arial; font-size: 12px;"></font></td>
    <td valign="top"><input type="submit" value="Calculate"></td>
</tr>
</table>
</form>


<table cellpadding="3" cellspacing="0" border="0">
<tr>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">Month</font></td>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">Est Imp</font></td>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">This Mo New Install</font></td>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">This Mo Investment</font></td>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">This Mo Cash</font></td>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">Mo Revenue</font></td>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">Mo Profit</font></td>

    <td valign="top"><font style="font-family: arial; font-size: 12px;">Tot Installs</font></td>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">Tot Investment</font></td>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">Tot Cash</font></td>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">Tot Revenue</font></td>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">Tot Profit</font></td>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">Balance</font></td>
</tr>
<%
double totalinstalls = initialinstalls;
double totalinvestment = 0;
double totalrevenue = 0;
double totalprofit = 0;
double previousmonthrevenue = 0;
double previousmonthprofit = 0;
double totalcashoutofpocket = 0;
double balance = 0;
for(int i=0; i<12; i++){

    //Investment/reinvestment
    double thismonthinvestment = monthlyinvestment;
    double thismonthcashoutofpocket = monthlyinvestment;
    if ((previousmonthprofit*(percentofprofittoreinvesteachmonth/100)>monthlyinvestment)){
        //No cash out of pocket
        thismonthinvestment = (previousmonthprofit*(percentofprofittoreinvesteachmonth/100));
        thismonthcashoutofpocket = 0;
    } else if (previousmonthprofit>0){
        //Same cash out of pocket but more spent on advertising
        thismonthinvestment = monthlyinvestment + (previousmonthprofit*(percentofprofittoreinvesteachmonth/100));
    }
    totalcashoutofpocket = totalcashoutofpocket + thismonthcashoutofpocket;
    totalinvestment = totalinvestment + thismonthinvestment;

    //Users
    double newusersboughtthismonth = thismonthinvestment/costtoattractanewuser;
    double newinstallsthismonth = newusersboughtthismonth * appsperuser;
    double installslostthismonth = totalinstalls*(monthlyuninstallpercentage/100);
    totalinstalls = (totalinstalls-installslostthismonth) + newinstallsthismonth;

    //Impressions
    double estimpressions = totalinstalls * avgimpressionssperinstall;
    double estimpressionrevenue = (estimpressions/1000) * cpmtoestimatewith;

    //Revenue/profit
    double thismonthrevenue = estimpressionrevenue;
    previousmonthrevenue = thismonthrevenue;
    double thismonthprofit = thismonthrevenue - thismonthinvestment;
    previousmonthprofit = thismonthprofit;
    totalrevenue = totalrevenue + thismonthrevenue;
    totalprofit = totalrevenue - totalinvestment;

    //Balance
    balance = balance + thismonthprofit - thismonthcashoutofpocket;

    %>
        <tr>
            <td valign="top"><font style="font-family: arial; font-size: 12px;"><%=i+1%></font></td>
            <td valign="top"><font style="font-family: arial; font-size: 12px;"><%=Str.formatWithXDecimalPlaces(estimpressions, 0)%></font></td>
            <%--<td valign="top"><font style="font-family: arial; font-size: 12px;">$<%=Str.formatForMoney(estimpressionrevenue)%></font></td>--%>
            <td valign="top"><font style="font-family: arial; font-size: 12px;"><%=Str.formatForMoney(newinstallsthismonth)%></font></td>
            <td valign="top"><font style="font-family: arial; font-size: 12px;">$<%=Str.formatForMoney(thismonthinvestment)%></font></td>
            <td valign="top"><font style="font-family: arial; font-size: 12px;">$<%=Str.formatForMoney(thismonthcashoutofpocket)%></font></td>
            <td valign="top"><font style="font-family: arial; font-size: 12px;">$<%=Str.formatForMoney(thismonthrevenue)%></font></td>
            <td valign="top"><font style="font-family: arial; font-size: 12px;"><b>$<%=Str.formatForMoney(thismonthprofit)%></b></font></td>

            <td valign="top"><font style="font-family: arial; font-size: 12px;"><%=Str.formatForMoney(totalinstalls)%></font></td>
            <td valign="top"><font style="font-family: arial; font-size: 12px;">$<%=Str.formatForMoney(totalinvestment)%></font></td>
            <td valign="top"><font style="font-family: arial; font-size: 12px;">$<%=Str.formatForMoney(totalcashoutofpocket)%></font></td>
            <td valign="top"><font style="font-family: arial; font-size: 12px;">$<%=Str.formatForMoney(totalrevenue)%></font></td>
            <td valign="top"><font style="font-family: arial; font-size: 12px;">$<%=Str.formatForMoney(totalprofit)%></font></td>
            <td valign="top"><font style="font-family: arial; font-size: 12px;">$<%=Str.formatForMoney(balance)%></font></td>
        </tr>
    <%
}
%>
</table>



<%@ include file="footer.jsp" %>