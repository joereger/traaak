<%@ include file="header.jsp" %>
<p>Giddyup Facebook!  Name: <%=userSession.getFacebookUser().getFirst_name()%> <%=userSession.getFacebookUser().getLast_name()%> </p>
<br/>
<form action="">
    <input type="hidden" name="action" value="submitpost" />
    <input type="text" id="firstname" name="firstname" />
    <input id="sendbutton" type="submit" value="Send It" />
</form>
<br/><br/>