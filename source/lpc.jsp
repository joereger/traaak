<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.fbdblog.htmluibeans.LostPasswordChoose" %>
<%@ page import="com.fbdblog.htmlui.*" %>
<%@ page import="com.fbdblog.util.RandomString" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Reset Password";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
LostPasswordChoose lostPasswordChoose=(LostPasswordChoose) Pagez.getBeanMgr().get("LostPasswordChoose");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("choose")) {
        try {
            lostPasswordChoose.setPassword(TextboxSecret.getValueFromRequest("password", "Password", true, DatatypeString.DATATYPEID));
            lostPasswordChoose.setPasswordverify(TextboxSecret.getValueFromRequest("passwordverify", "Password Verify", true, DatatypeString.DATATYPEID));
            lostPasswordChoose.setJ_captcha_response(com.fbdblog.htmlui.Textbox.getValueFromRequest("j_captcha_response", "Squiggly Letters", true, DatatypeString.DATATYPEID));
            lostPasswordChoose.setU(request.getParameter("u"));
            lostPasswordChoose.setK(request.getParameter("k"));
            lostPasswordChoose.setCaptchaId(request.getParameter("captchaId"));
            lostPasswordChoose.choosePassword();
            Pagez.getUserSession().setMessage("Your password has been set.  Store it in a safe place.");
            Pagez.sendRedirect("/account/index.jsp");
            return;
        } catch (ValidationException vex) {
            Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
        }
    }
%>
<%
    String captchaId=RandomString.randomAlphanumeric(10);
%>
<%@ include file="/template/header.jsp" %>

<form action="/lpc.jsp" method="post">
    <input type="hidden" name="dpage" value="/lpc.jsp">
    <input type="hidden" name="action" value="choose">
    <input type="hidden" name="u" value="<%=request.getParameter("u")%>">
    <input type="hidden" name="k" value="<%=request.getParameter("k")%>">
    <input type="hidden" name="captchaId" value="<%=captchaId%>">
    <table cellpadding="0" cellspacing="0" border="0">

        <tr>
            <td valign="top">
                <font class="formfieldnamefont">Password</font>
            </td>
            <td valign="top">
                <%=TextboxSecret.getHtml("password", lostPasswordChoose.getPassword(), 255, 35, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Password Verify</font>
                </td>
                <td valign="top">
                    <%=TextboxSecret.getHtml("passwordverify", lostPasswordChoose.getPasswordverify(), 255, 35, "", "")%>
                </td>
            </tr>

            <tr>
                <td valign="top">
                    <font class="formfieldnamefont">Prove You're a Human</font>
                </td>
                <td valign="top">
                    <div style="border: 1px solid #ccc; padding: 3px;">
                    <%=Textbox.getHtml("j_captcha_response", lostPasswordChoose.getJ_captcha_response(), 255, 35, "", "")%>
                    <br/>
                    <font class="tinyfont">(type the squiggly letters that appear below)</font>
                    <br/>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td><img src="/images/clear.gif" alt="" width="1" height="100"></img></td>
                            <td style="background: url(/images/loading-captcha.gif);">
                                <img src="/images/clear.gif" alt="" width="200" height="1"></img><br/>
                                <img src="/jcaptcha?captchaId=<%=captchaId%>"/>
                            </td>
                        </tr>
                    </table>
                    </div>
                </td>
            </tr>

            <tr>
                <td valign="top">
                </td>
                <td valign="top">
                    <input type="submit" class="formsubmitbutton" value="Reset Password">
                </td>
            <tr>

        </table>
    </form>


<%@ include file="/template/footer.jsp" %>
