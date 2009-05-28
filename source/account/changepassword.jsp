<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.fbdblog.htmluibeans.ChangePassword" %>
<%@ page import="com.fbdblog.htmlui.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Change Password";
String navtab = "account";
String acl = "account";
%>
<%@ include file="/template/auth.jsp" %>
<%
ChangePassword changePassword = (ChangePassword) Pagez.getBeanMgr().get("ChangePassword");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
    try {
        changePassword.setPassword(TextboxSecret.getValueFromRequest("password", "New Password", true, DatatypeString.DATATYPEID));
        changePassword.setPasswordverify(TextboxSecret.getValueFromRequest("passwordverify", "Verify New Password", true, DatatypeString.DATATYPEID));
        changePassword.saveAction();
        Pagez.getUserSession().setMessage("Your password has been changed.");
        Pagez.sendRedirect("/account/accountsettings.jsp");
        return;
    } catch (ValidationException vex) {
        Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
    }
}
%>
<%@ include file="/template/header.jsp" %>


    <br/><br/>
    <form action="/account/changepassword.jsp" method="post">
        <input type="hidden" name="dpage" value="/account/changepassword.jsp">
        <input type="hidden" name="action" value="save">
            <table cellpadding="0" cellspacing="0" border="0">

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">New Password</font>
                    </td>
                    <td valign="top">
                        <%=TextboxSecret.getHtml("password", changePassword.getPassword(), 255, 20, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Verify New Password</font>
                    </td>
                    <td valign="top">
                        <%=TextboxSecret.getHtml("passwordverify", changePassword.getPasswordverify(), 255, 20, "", "")%>
                    </td>
                </tr>



                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <br/><br/>
                        <input type="submit" class="formsubmitbutton" value="Save New Password">
                    </td>
                </tr>

            </table>
       </form>

<%@ include file="/template/footer.jsp" %>

