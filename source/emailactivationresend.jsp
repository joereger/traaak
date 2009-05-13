<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.fbdblog.htmluibeans.EmailActivationResend" %>
<%@ page import="com.fbdblog.htmlui.*" %>
<%@ page import="com.fbdblog.util.RandomString" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Email Activation Re-Send";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%
EmailActivationResend emailActivationResend = (EmailActivationResend)Pagez.getBeanMgr().get("EmailActivationResend");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("send")) {
        try {
            emailActivationResend.setEmail(Textbox.getValueFromRequest("email", "Email", true, DatatypeString.DATATYPEID));
            emailActivationResend.setJ_captcha_response(Textbox.getValueFromRequest("j_captcha_response", "Squiggly Letters", true, DatatypeString.DATATYPEID));
            emailActivationResend.setCaptchaId(request.getParameter("captchaId"));
            emailActivationResend.reSendEmail();
            Pagez.sendRedirect("/emailactivationresendcomplete.jsp");
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

        <form action="/emailactivationresend.jsp" method="post">
            <input type="hidden" name="dpage" value="/emailactivationresend.jsp">
            <input type="hidden" name="action" value="send">
            <input type="hidden" name="captchaId" value="<%=captchaId%>">

            <table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Email</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("email", emailActivationResend.getEmail(), 255, 35, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Prove You're a Human</font>
                    </td>
                    <td valign="top">
                        <div style="border: 1px solid #ccc; padding: 3px;">
                        <%=Textbox.getHtml("j_captcha_response", emailActivationResend.getJ_captcha_response(), 255, 35, "", "")%>
                        <br/>
                        <font class="tinyfont">(type the squiggly letters that appear below)</font>
                        <br/>
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td><img src="/images/clear.gif" alt="" width="1" height="100"/></td>
                                <td style="background: url(/images/loading-captcha.gif);">
                                    <img src="/images/clear.gif" alt="" width="200" height="1"/><br/>
                                    <img src="/jcaptcha?captchaId=<%=captchaId%>" alt=""/>
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
                        <input type="submit" class="formsubmitbutton" value="Re-Send Activation Email">
                    </td>
                </tr>

            </table>
        </form>



<%@ include file="/template/footer.jsp" %>
