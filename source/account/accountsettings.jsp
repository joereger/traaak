<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.fbdblog.htmlui.*" %>
<%@ page import="com.fbdblog.htmluibeans.AccountSettings" %>
<%@ page import="com.fbdblog.util.Str" %>
<%@ page import="java.util.*" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Account Settings";
String navtab = "account";
String acl = "account";
%>
<%@ include file="/template/auth.jsp" %>
<%
AccountSettings accountSettings = (AccountSettings) Pagez.getBeanMgr().get("AccountSettings");
%>
<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
    try {
        accountSettings.setFirstname(Textbox.getValueFromRequest("firstname", "First Name", true, DatatypeString.DATATYPEID));
        accountSettings.setLastname(Textbox.getValueFromRequest("lastname", "Last Name", true, DatatypeString.DATATYPEID));
        accountSettings.setNickname(Textbox.getValueFromRequest("nickname", "Nickname", true, DatatypeString.DATATYPEID));
        accountSettings.setEmail(Textbox.getValueFromRequest("email", "Email", true, DatatypeString.DATATYPEID));
        accountSettings.setTimezoneid(request.getParameter("timezoneid"));
        accountSettings.saveAction();
        Pagez.getUserSession().setMessage("Settings saved.");
    } catch (com.fbdblog.htmlui.ValidationException vex) {
        Pagez.getUserSession().setMessage(vex.getErrorsAsSingleString());
    }
}
%>
<%@ include file="/template/header.jsp" %>



    <br/><br/>
    <form action="/account/accountsettings.jsp" method="post">
        <input type="hidden" name="dpage" value="/account/accountsettings.jsp">
        <input type="hidden" name="action" value="save">

            <table cellpadding="3" cellspacing="0" border="0">

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">First Name</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("firstname", accountSettings.getFirstname(), 255, 20, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Last Name</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("lastname", accountSettings.getLastname(), 255, 20, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Nickname</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("nickname", accountSettings.getNickname(), 255, 20, "", "")%>
                    </td>
                </tr>

                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Email</font>
                        <br/>
                        <font class="tinyfont">This is used to log in.</font>
                    </td>
                    <td valign="top">
                        <%=Textbox.getHtml("email", accountSettings.getEmail(), 255, 20, "", "")%>
                        <br/>
                        <font class="tinyfont">Changing email will require re-activation</font>
                    </td>
                </tr>


                <tr>
                    <td valign="top">
                        <font class="formfieldnamefont">Password</font>
                        <br/>
                        <font class="tinyfont">Password changes are handled on a separate screen.</font>
                    </td>
                    <td valign="top">
                        <a href="/account/changepassword.jsp"><font class="smallfont">Change Password</font></a>
                    </td>
                </tr>


                <tr>
                    <td valign="top" width="30%">
                        <font style="font-size: 12px; font-weight: bold;">Timezone</font>
                        <br/>
                        <font style="font-size: 9px;">Note that this will affect everything you track.</font>
                    </td>
                    <td valign="top">
                        <select name="timezoneid">
                        <%
                        String[] timezone=TimeZone.getAvailableIDs();
                        //Treeset for alphabetizing
                        TreeSet timezoneids=new TreeSet();
                        for (int i=0; i<timezone.length; i++) {
                            timezoneids.add(timezone[i]);
                        }
                        for (Iterator iterator=timezoneids.iterator(); iterator.hasNext();) {
                            String javatimezoneid=(String) iterator.next();
                            String selected = "";
                            if (accountSettings.getTimezoneid().equalsIgnoreCase(javatimezoneid)){
                                selected = " selected";
                            }
                            %>
                            <option value="<%=Str.cleanForHtml(javatimezoneid)%>" <%=selected%>><%=javatimezoneid%></option>
                            <%
                        }
                        %>
                        </select>
                    </td>
                </tr>


                <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                        <br/><br/>
                        <input type="submit" class="formsubmitbutton" value="Save Settings">
                    </td>
                </tr>

            </table>

    </form>


<%@ include file="/template/footer.jsp" %>


