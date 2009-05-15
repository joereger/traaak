<%@ page import="com.fbdblog.htmlui.Pagez" %>
<%--<table width="786" cellspacing="0" border="0" cellpadding="0">--%>
<%--<tr>--%>
    <%--<td rowspan="2"><font class="largefont"><a href="/">CelebTwit</a></font><br/><br/><br/><br/></td>--%>
    <%--<td  valign="center" style="text-align: right;">--%>
        <%--<img src="/images/clear.gif" width="1" height="6" alt=""/><br/> --%>
		<%--<%if (navtab.equals("home")){%><font class="navtabfontlevel1" style="background: #ffffff;"><a href="/index.jsp"><b>Home</b></a></font><%}%>--%>
        <%--<%if (!navtab.equals("home")){%><font class="navtabfontlevel1"><a href="/index.jsp"><b>Home</b></a></font><%}%>--%>
        <%--<%if (!navtab.equals("celebtwit") && Pagez.getUserSession().getIsloggedin()){%><font class="navtabfontlevel1"><a href="/account/index.jsp"><b>Publish</b></a></font><%}%>--%>
        <%--<%if (navtab.equals("celebtwit") && Pagez.getUserSession().getIsloggedin()){%><font class="navtabfontlevel1" style="background: #ffffff;"><a href="/account/index.jsp"><b>Publish</b></a></font><%}%>--%>
        <%--<%if (!navtab.equals("youraccount") && Pagez.getUserSession().getIsloggedin()){%><font class="navtabfontlevel1"><a href="/account/accountsettings.jsp"><b>Settings</b></a></font><%}%>--%>
        <%--<%if (navtab.equals("youraccount") && Pagez.getUserSession().getIsloggedin()){%><font class="navtabfontlevel1" style="background: #ffffff;"><a href="/account/accountsettings.jsp"><b>Settings</b></a></font><%}%>--%>
        <%--<%if (1==2 && !navtab.equals("youraccount") && !Pagez.getUserSession().getIsloggedin()){%><font class="navtabfontlevel1"><a href="/registration.jsp"><b>Sign Up</b></a></font><%}%>--%>
        <%--<%if (1==2 && navtab.equals("youraccount") && !Pagez.getUserSession().getIsloggedin()){%><font class="navtabfontlevel1" style="background: #ffffff;"><a href="/registration.jsp"><b>Sign Up</b></a></font><%}%>--%>
        <%--<%if (Pagez.getUserSession().getIsSysadmin() && !navtab.equals("sysadmin")){%><font class="navtabfontlevel1"><a href="/sysadmin/index.jsp"><b>SysAdmin</b></a></font><%}%>--%>
        <%--<%if (Pagez.getUserSession().getIsSysadmin() && navtab.equals("sysadmin")){%><font class="navtabfontlevel1" style="background: #ffffff;"><a href="/sysadmin/index.jsp"><b>SysAdmin</b></a></font><%}%>--%>
	<%--</td>--%>
<%--</tr>--%>
<%--<tr>--%>
    <%--<td valign="center" style="text-align: right;">--%>
		<%--<%if (navtab.equals("home")){%>--%>
            <%--<img src="/images/clear.gif" alt="" width="10" height="1"/>--%>
            <%--<%if (1==2 && !Pagez.getUserSession().getIsloggedin()){%><a href="/registration.jsp"><font class="subnavfont" style="color: #000000;">Sign Up</font></a><%}%>--%>
            <%--<img src="/images/clear.gif" alt="" width="10" height="1"/>--%>
            <%--<%if (1==2 && !Pagez.getUserSession().getIsloggedin()){%><a href="/login.jsp"><font class="subnavfont" style="color: #000000;">Log In</font></a><%}%>--%>
        <%--<%}%>--%>

        <%if (navtab.equals("account")){%>
            <%if (Pagez.getUserSession().getIsloggedin()){%>
                <img src="/images/clear.gif" alt="" width="10" height="1"/>
                <a href="/account/accountsettings.jsp"><font class="subnavfont" style="color: #000000;">Account Settings</font></a>
                <img src="/images/clear.gif" alt="" width="10" height="1"/>
                <a href="/account/changepassword.jsp"><font class="subnavfont" style="color: #000000;">Change Password</font></a>
                <%if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getIssysadmin()){%>
                    <img src="/images/clear.gif" alt="" width="10" height="1"/>
                    <a href="/sysadmin/index.jsp"><font class="subnavfont" style="color: #000000;">Sysadmin</font></a>
                <%}%>
            <%}%>
        <%}%>

        <%--<%if (navtab.equals("celebtwit")){%>--%>
            <%--<%if (Pagez.getUserSession().getIsloggedin()){%>--%>
                <%--<img src="/images/clear.gif" alt="" width="10" height="1"/>--%>
                <%--<a href="/account/index.jsp"><font class="subnavfont" style="color: #000000;">Main</font></a>--%>
            <%--<%}%>--%>
        <%--<%}%>--%>

        <%if (navtab.equals("sysadmin")){%>
            <%if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getIssysadmin()){%>
                <a href="/sysadmin/errorlist.jsp"><font class="subnavfont" style=" color: #000000;">Log</font></a>
                <a href="/sysadmin/userlist.jsp"><font class="subnavfont" style=" color: #000000;">Users</font></a>
                <a href="/sysadmin/manuallyrunscheduledtask.jsp"><font class="subnavfont" style=" color: #000000;">Scheds</font></a>
                <a href="/sysadmin/systemprops.jsp"><font class="subnavfont" style=" color: #000000;">SysProps</font></a>
                <a href="/sysadmin/instanceprops.jsp"><font class="subnavfont" style=" color: #000000;">InsProps</font></a>
                <a href="/sysadmin/hibernatecache.jsp"><font class="subnavfont" style=" color: #000000;">Cache</font></a>
            <%}%>
        <%}%>
	<%--</td>--%>
<%--</tr>--%>
<%--</table><table width="786" cellspacing="0" border="0" cellpadding="0">--%>
<%--<tr>--%>
    <%--<td valign="top">--%>
        <%--<%if (pagetitle!=null && !pagetitle.equals("")){%>--%>
            <%--<font class="pagetitlefont"><%=pagetitle%></font>--%>
            <%--<br/><br/>--%>
        <%--<%}%>--%>
        <%--<%--%>
        <%--logger.debug("Pagez.getUserSession().getMessage()="+Pagez.getUserSession().getMessage());--%>
        <%--if (Pagez.getUserSession().getMessage()!=null && !Pagez.getUserSession().getMessage().equals("")){--%>
            <%--%>--%>
            <%--<br/>--%>
            <%--<center><div class="rounded" style="background: #F2FFBF; text-align: left; padding: 20px;"><font class="formfieldnamefont"><%=Pagez.getUserSession().getMessage()%></font></div></center>--%>
            <%--<br/><br/>--%>
            <%--<%--%>
            <%--//Clear the message since it's been displayed--%>
            <%--Pagez.getUserSession().setMessage("");--%>
        <%--}--%>
        <%--%>--%>
    <%--</td>--%>
    <%--<td valign="top" style="text-align: right;">--%>
        <%--<%if (!Pagez.getUserSession().getIsloggedin()){%>--%>
                <%--<div style="text-align: right;">--%>
                    <%--<font class="subnavfont">Already have an account?<img src="/images/clear.gif" width="20" height="1"/><a href="/login.jsp">Log In</a></font>--%>
                    <%--<br/>--%>
                    <%--<font class="subnavfont">Want to get one?<img src="/images/clear.gif" width="20" height="1"/><a href="/registration.jsp">Sign Up</a></font>--%>
                <%--</div>--%>
            <%--<%}%>--%>
            <%--<%if (Pagez.getUserSession().getIsloggedin()){%>--%>
                <%--<div style="text-align: right;">--%>
                    <%--<font class="subnavfont">Hi, <%=Pagez.getUserSession().getUser().getFirstname()%> <%=Pagez.getUserSession().getUser().getLastname()%>! <a href="/login.jsp?action=logout">Log Out</a></font>--%>
                <%--</div>--%>
            <%--<%}%>--%>
        <%--</td>--%>
    <%--</tr>--%>
<%--</table>--%>