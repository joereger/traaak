<%@ page import="com.fbdblog.qtype.util.AppTemplateProcessor" %>
<%@ page import="com.fbdblog.qtype.util.AppPostParser" %>
<%@ page import="com.fbdblog.qtype.util.SavePost" %>
<%@ page import="com.fbdblog.qtype.def.ComponentException" %>
<%@ include file="header.jsp" %>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("trackit")) {
        AppPostParser appPostParser = new AppPostParser(request);
        try {
            SavePost.save(userSession.getApp(), userSession.getUser(), appPostParser);
            out.print("<fb:success>\n" +
            "     <fb:message>Success!  Your info has been tracked!</fb:message>\n" +
            "     We've also updated your profile so that others can check you out.\n" +
            "</fb:success>");
        } catch (ComponentException cex) {
            out.print(" <fb:error>\n" +
            "      <fb:message>Oops, there was an error:</fb:message>\n" +
            "      "+cex.getErrorsAsSingleString()+"\n" +
            " </fb:error>");
        }
    }
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("compare")) {
        //@todo implement compare
        logger.debug("compare called");
    }
%>



<br/>
<fb:tabs>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=main' title='Track Stuff' selected='true'/>
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=charts' title='Cool Charts' />
  <fb:tab-item href='http://apps.facebook.com/<%=userSession.getApp().getFacebookappname()%>/?nav=history' title='Yo History' />
</fb:tabs>
<br/>

<table>
    <tr>
        <td valign="top" width="50%">
            <form action="">
                <input type="hidden" name="action" value="trackit" />

                <%
                AppTemplateProcessor atp = new AppTemplateProcessor(userSession.getApp(), userSession.getUser(), null);
                out.print(atp.getHtmlForInput(false));
                %>

                <input id="sendbutton" type="submit" value="Track It" />
            </form>
        </td>
        <td valign="top">
            <img src="http://joereger.yi.org/images/clear.gif" alt="" width="200" height="150" style="border: 3px solid #e6e6e6;"/>
            <br/>
            Zoom
            <br/><br/>

        </td>
    </tr>
</table>

<br/><br/>