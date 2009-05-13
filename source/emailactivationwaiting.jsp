<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.fbdblog.htmlui.Pagez" %>
<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Account Awaiting Email Activation";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="/template/auth.jsp" %>
<%@ include file="/template/header.jsp" %>


            Your account has not yet been activated by email.  It must be activated by email within 3 days after signup.  Please check your email inbox and click the link that we sent you to begin the activation process. (Also check your Junk Mail and/or Spam folders... sometimes email clients put the message there.)</h:outputText>
            <br/><br/>
            <a href="/emailactivationresend.jsp">Click here to re-send your email activation message.</a>


<%@ include file="/template/footer.jsp" %>
