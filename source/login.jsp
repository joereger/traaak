<%@ page import="com.fbdblog.htmluibeans.Login" %>
<%@ page import="com.fbdblog.htmlui.*" %>
<%@ page import="com.fbdblog.systemprops.SystemProperty" %>
<%@ page import="com.fbdblog.session.PersistentLogin" %>
<%@ page import="com.fbdblog.session.UrlSplitter" %>
<%@ page import="com.fbdblog.systemprops.BaseUrl" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Log In";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>

<%
Login login = (Login) Pagez.getBeanMgr().get("Login");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("login")) {
        try {
            login.setEmail(com.fbdblog.htmlui.Textbox.getValueFromRequest("email", "Email", true, DatatypeString.DATATYPEID));
            login.setPassword(com.fbdblog.htmlui.Textbox.getValueFromRequest("password", "Password", true, DatatypeString.DATATYPEID));
            login.setKeepmeloggedin(CheckboxBoolean.getValueFromRequest("keepmeloggedin"));
            login.login();
            //Redir if https is on
            String keepmeloggedinStr="";
            if (login.getKeepmeloggedin()) {
                keepmeloggedinStr="?keepmeloggedin=1";
                //Get all possible cookies to set
                Cookie[] cookies=PersistentLogin.getPersistentCookies(Pagez.getUserSession().getUser().getUserid(), Pagez.getRequest());
                //Add a cookies to the response
                for (int j=0; j<cookies.length; j++) {
                    Pagez.getResponse().addCookie(cookies[j]);
                }
            }
            //For login from embed widget
            if (request.getParameter("redirtocaller") != null && request.getParameter("redirtocaller").equals("1")) {
                String referer = request.getHeader("referer");
                if (referer!=null){
                    Pagez.sendRedirect(referer);
                    return;
                }
            }
            //Redir somewhere
            if (SystemProperty.getProp(SystemProperty.PROP_ISSSLON).equals("1")) {
                try {
                    logger.debug("redirecting to https - " + BaseUrl.get(true) + "account/index.jsp" + keepmeloggedinStr);
                    Pagez.sendRedirect(BaseUrl.get(true) + "account/index.jsp" + keepmeloggedinStr);
                    return;
                } catch (Exception ex) {
                    logger.error("", ex);
                    Pagez.sendRedirect("/account/index.jsp" + keepmeloggedinStr);
                    return;
                }
            } else {
                Pagez.sendRedirect("/account/index.jsp" + keepmeloggedinStr);
                return;
            }
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("logout")) {
        try {
            login.logout();
            Pagez.getUserSession().setMessage("You have been logged out.");
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%@ include file="/template/header.jsp" %>

    <br/><br/>
    <form action="/login.jsp" method="post">
        <input type="hidden" name="dpage" value="/login.jsp">
        <input type="hidden" name="action" value="login">

            <table cellpadding="5" cellspacing="0" border="0">

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Email</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("email", login.getEmail(), 255, 20, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Password</font>
                    </td>
                    <td valign="top">
                        <%=TextboxSecret.getHtml("password", login.getPassword(), 255, 20, "", "")%>
                        <br/>
                        <a href="/lostpassword.jsp"><font class="tinyfont" style="color: #000000;">Lost your password?</font></a>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <%=CheckboxBoolean.getHtml("keepmeloggedin", login.getKeepmeloggedin(), "", "")%>
                        <font class="formfieldnamefont">Stay Logged In?</font>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <input type="submit" class="formsubmitbutton" value="Log In">
                    </td>
                </tr>

            </table>

    </form>

<%@ include file="/template/footer.jsp" %>
