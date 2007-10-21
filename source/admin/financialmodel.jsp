<%@ page import="com.fbdblog.util.Num" %>
<%@ page import="com.fbdblog.util.Str" %>
<%@ include file="header.jsp" %>


<font class="pagetitle">Financial Model</font>
<br/><br/>

<%

double monthlyinvestment = 1000;
double costtoattractanewuser = .25;
double monthlyuninstallpercentage = 30;
double monthlyearningsperuser = .1;
double percentofprofittoreinvesteachmonth = 90;
double avgimpressionssperuser = 30;
double cpmtoestimatewith = .45;

%>

<%
if (request.getParameter("action")!=null && request.getParameter("action").equals("calculate")) {
    if (request.getParameter("monthlyinvestment")!=null && Num.isdouble(request.getParameter("monthlyinvestment"))) {
        monthlyinvestment = Double.parseDouble(request.getParameter("monthlyinvestment"));
    }
    if (request.getParameter("costtoattractanewuser")!=null && Num.isdouble(request.getParameter("costtoattractanewuser"))) {
        costtoattractanewuser = Double.parseDouble(request.getParameter("costtoattractanewuser"));
    }
    if (request.getParameter("monthlyuninstallpercentage")!=null && Num.isdouble(request.getParameter("monthlyuninstallpercentage"))) {
        monthlyuninstallpercentage = Double.parseDouble(request.getParameter("monthlyuninstallpercentage"));
    }
    if (request.getParameter("monthlyearningsperuser")!=null && Num.isdouble(request.getParameter("monthlyearningsperuser"))) {
        monthlyearningsperuser = Double.parseDouble(request.getParameter("monthlyearningsperuser"));
    }
    if (request.getParameter("percentofprofittoreinvesteachmonth")!=null && Num.isdouble(request.getParameter("percentofprofittoreinvesteachmonth"))) {
        percentofprofittoreinvesteachmonth = Double.parseDouble(request.getParameter("percentofprofittoreinvesteachmonth"));
    }
    if (request.getParameter("avgimpressionssperuser")!=null && Num.isdouble(request.getParameter("avgimpressionssperuser"))) {
        avgimpressionssperuser = Double.parseDouble(request.getParameter("avgimpressionssperuser"));
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
    <td valign="top"><font style="font-family: arial; font-size: 12px;">monthlyearningsperuser</font></td>
    <td valign="top"><input type="text" name="monthlyearningsperuser" value="<%=monthlyearningsperuser%>"></td>
</tr>
<tr>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">percentofprofittoreinvesteachmonth</font></td>
    <td valign="top"><input type="text" name="percentofprofittoreinvesteachmonth" value="<%=percentofprofittoreinvesteachmonth%>"></td>
</tr>
<tr>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">avgimpressionssperuser</font></td>
    <td valign="top"><input type="text" name="avgimpressionssperuser" value="<%=avgimpressionssperuser%>"></td>
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
    <td valign="top"><font style="font-family: arial; font-size: 12px;">Est Imp Rev</font></td>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">This Mo New Users</font></td>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">This Mo Investment</font></td>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">This Mo Cash</font></td>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">Mo Revenue</font></td>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">Mo Profit</font></td>

    <td valign="top"><font style="font-family: arial; font-size: 12px;">Tot Users</font></td>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">Tot Investment</font></td>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">Tot Cash</font></td>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">Tot Revenue</font></td>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">Tot Profit</font></td>
    <td valign="top"><font style="font-family: arial; font-size: 12px;">Balance</font></td>
</tr>
<%
double totalusers = 0;
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
    double newusersthismonth = newusersboughtthismonth;
    double userslostthismonth = totalusers*(monthlyuninstallpercentage/100);
    totalusers = (totalusers-userslostthismonth) + newusersthismonth;

    //Impressions
    double estimpressions = totalusers * avgimpressionssperuser;
    double estimpressionrevenue = (estimpressions/1000) * cpmtoestimatewith;

    //Revenue/profit
    double thismonthrevenue = (totalusers*monthlyearningsperuser);
    previousmonthrevenue = thismonthrevenue;
    double thismonthprofit = (totalusers*monthlyearningsperuser) - monthlyinvestment;
    previousmonthprofit = thismonthprofit;
    totalrevenue = totalrevenue + thismonthrevenue;
    totalprofit = totalrevenue - totalinvestment;

    //Balance
    balance = balance + totalprofit;


    %>
        <tr>
            <td valign="top"><font style="font-family: arial; font-size: 12px;"><%=i+1%></font></td>
            <td valign="top"><font style="font-family: arial; font-size: 12px;"><%=Str.formatForMoney(estimpressions)%></font></td>
            <td valign="top"><font style="font-family: arial; font-size: 12px;">$<%=Str.formatForMoney(estimpressionrevenue)%></font></td>
            <td valign="top"><font style="font-family: arial; font-size: 12px;"><%=Str.formatForMoney(newusersthismonth)%></font></td>
            <td valign="top"><font style="font-family: arial; font-size: 12px;">$<%=Str.formatForMoney(thismonthinvestment)%></font></td>
            <td valign="top"><font style="font-family: arial; font-size: 12px;">$<%=Str.formatForMoney(thismonthcashoutofpocket)%></font></td>
            <td valign="top"><font style="font-family: arial; font-size: 12px;">$<%=Str.formatForMoney(thismonthrevenue)%></font></td>
            <td valign="top"><font style="font-family: arial; font-size: 12px;"><b>$<%=Str.formatForMoney(thismonthprofit)%></b></font></td>

            <td valign="top"><font style="font-family: arial; font-size: 12px;"><%=Str.formatForMoney(totalusers)%></font></td>
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