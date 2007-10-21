<%@ page import="com.fbdblog.scheduledjobs.ImpressionCache" %>
<%@ include file="header.jsp" %>


<font class="pagetitle">IAO Cache</font>
<br/><br/>

<%=ImpressionCache.getIaoCacheAsString()%>



<%@ include file="footer.jsp" %>