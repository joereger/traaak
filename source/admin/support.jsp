<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="com.fbdblog.dao.Supportissue" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="com.fbdblog.util.Time" %>
<%@ page import="org.hibernate.criterion.Order" %>
<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="com.fbdblog.util.Str" %>
<%@ page import="com.fbdblog.dao.User" %>
<%@ page import="com.fbdblog.util.Num" %>
<%@ page import="com.fbdblog.dao.Supportissuecomm" %>
<%@ page import="org.hibernate.Criteria" %>
<%@ include file="header.jsp" %>

<%
    //Load the supportissue requested
    Supportissue supportissue=null;
    if (request.getParameter("supportissueid") != null && Num.isinteger(request.getParameter("supportissueid"))) {
        supportissue=Supportissue.get(Integer.parseInt(request.getParameter("supportissueid")));
    }
%>

<%
    if (request.getParameter("action") != null && request.getParameter("action").equals("save")) {
        if (supportissue!=null){
            if (request.getParameter("notes") != null && !request.getParameter("notes").trim().equals("")) {
                Supportissuecomm supportissuecomm=new Supportissuecomm();
                supportissuecomm.setNotes(request.getParameter("notes"));
                supportissuecomm.setSupportissueid(supportissue.getSupportissueid());
                supportissuecomm.setUserid(Pagez.getUserSession().getUser().getUserid());
                supportissuecomm.setDatetime(Time.nowInGmtDate());
                try {
                    supportissuecomm.save();
                } catch (Exception ex) {
                    logger.error("", ex);
                }
            }
            supportissue.setMostrecentupdateat(Time.nowInGmtDate());
            int status = Supportissue.STATUS_REPLYWAITING;
            if (Num.isinteger(request.getParameter("status"))){
                status = Integer.parseInt(request.getParameter("status"));
            }
            supportissue.setStatus(status);
            int type = Supportissue.TYPE_UNDEFINED;
            if (Num.isinteger(request.getParameter("type"))){
                type = Integer.parseInt(request.getParameter("type"));
            }
            supportissue.setType(type);
            try {
                supportissue.save();
            } catch (Exception ex) {
                logger.error("", ex);
            }
        }
    }
%>

<font class="pagetitle">Support</font>
<br/><br/>
<table cellpadding="10" cellspacing="0" border="0" width="100%">
    <tr>
        <td valign="top" bgcolor="#f6f6f6" width="33%">
            <!-- Left Col -->
            <form action="support.jsp" method="post">
                <select name="showstatus" style="font-size: 10px;">
                    <%int status=Supportissue.STATUS_NEW;if(Num.isinteger(request.getParameter("showstatus"))){status=Integer.parseInt(request.getParameter("showstatus"));}%>
                    <%String anystatusSelect=""; if (status==-1){anystatusSelect=" showstatus";}%>
                    <option value="-1" <%=anystatusSelect%>>Any Status</option>
                    <%String newSelect=""; if (status==Supportissue.STATUS_NEW){newSelect=" selected";}%>
                    <option value="<%=Supportissue.STATUS_NEW%>" <%=newSelect%>>New</option>
                    <%String rwSelect=""; if (status==Supportissue.STATUS_REPLYWAITING){rwSelect=" selected";}%>
                    <option value="<%=Supportissue.STATUS_REPLYWAITING%>" <%=rwSelect%>>Reply Waiting for User</option>
                    <%String clSelect=""; if (status==Supportissue.STATUS_CLOSED){clSelect=" selected";}%>
                    <option value="<%=Supportissue.STATUS_CLOSED%>" <%=clSelect%>>Closed</option>
                </select>
                <select name="showtype" style="font-size: 10px;">
                    <%int type=-1;if(Num.isinteger(request.getParameter("showtype"))){type=Integer.parseInt(request.getParameter("showtype"));}%>
                    <%String anytypeSelect=""; if (type==-1){anytypeSelect=" selected";}%>
                    <option value="-1" <%=anytypeSelect%>>Any Type</option>
                    <%String undefSelect=""; if (type==Supportissue.TYPE_UNDEFINED){undefSelect=" selected";}%>
                    <option value="<%=Supportissue.TYPE_UNDEFINED%>" <%=undefSelect%>>Undefined</option>
                    <%String bugSelect=""; if (type==Supportissue.TYPE_BUG){bugSelect=" selected";}%>
                    <option value="<%=Supportissue.TYPE_BUG%>" <%=bugSelect%>>Bug</option>
                    <%String frSelect=""; if (type==Supportissue.TYPE_FEATUREREQUEST){frSelect=" selected";}%>
                    <option value="<%=Supportissue.TYPE_FEATUREREQUEST%>" <%=frSelect%>>Feature Request</option>
                    <%String naaSelect=""; if (type==Supportissue.TYPE_NEWAPPIDEA){naaSelect=" selected";}%>
                    <option value="<%=Supportissue.TYPE_NEWAPPIDEA%>" <%=naaSelect%>>New App Idea</option>
                </select>
                <input type="submit" value="Go" style="font-size: 10px;">
            </form>
            <%
                Criteria criteria=HibernateUtil.getSession().createCriteria(Supportissue.class);
                criteria.addOrder(Order.desc("mostrecentupdateat"));
                if (request.getParameter("showstatus")!=null && Num.isinteger(request.getParameter("showstatus"))){
                    if (Integer.parseInt(request.getParameter("showstatus"))>-1){
                        criteria.add(Restrictions.eq("status", Integer.parseInt(request.getParameter("showstatus"))));
                    } else {
                        //status = 0 so no criteria needed
                    }
                } else {
                    criteria.add(Restrictions.eq("status", Supportissue.STATUS_NEW));
                }
                if (request.getParameter("showtype")!=null && Num.isinteger(request.getParameter("showtype"))){
                    if (Integer.parseInt(request.getParameter("showtype"))>-1){
                        criteria.add(Restrictions.eq("type", Integer.parseInt(request.getParameter("showtype"))));
                    }
                }
                List<Supportissue> supportissues=criteria.setCacheable(true).setMaxResults(250).list();
                if (supportissues != null && supportissues.size()>0) {
                    %>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <%
                        for (Iterator<Supportissue> iterator=supportissues.iterator(); iterator.hasNext();) {
                            Supportissue si=iterator.next();
                            App app=App.get(si.getAppid());
                            User userwhosent=User.get(si.getUserid());
                            String statusStr="New";
                            if (si.getStatus() == Supportissue.STATUS_NEW) {
                                statusStr="New";
                            } else if (si.getStatus() == Supportissue.STATUS_REPLYWAITING) {
                                statusStr="Reply Waiting";
                            } else if (si.getStatus() == Supportissue.STATUS_CLOSED) {
                                statusStr="Closed";
                            }
                        %>
                        <tr>
                            <td>
                                <font style="font-size: 8px; font-weight: bold; color: #666666;"><%=Time.agoText(Time.getCalFromDate(si.getDatetime()))%>: <%=statusStr%></font>
                            </td>
                            <td style="text-align: right;">
                                <font style="font-size: 8px; font-weight: bold; color: #666666;"><%=app.getTitle()%></font>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <a href="support.jsp?supportissueid=<%=si.getSupportissueid()%>&showstatus=<%=request.getParameter("showstatus")%>&showtype=<%=request.getParameter("showtype")%>"><font style="font-size: 8px; font-weight: bold;"><%=Str.truncateString(si.getSubject(), 80)%></font></a>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <font style="font-size: 8px; font-weight: bold; color: #999999;"><%=userwhosent.getFirstname()%> <%=userwhosent.getLastname()%></font><br/><br/>
                            </td>
                        </tr>
                        <%
                    }
                    %>
                    </table>
                    <%
                }
            %>
        </td>
        <td valign="top">
            <!-- Right Col -->
            <%
            if (supportissue!=null){
                App app = App.get(supportissue.getAppid());
             %>
             <a href="appdetail.jsp?appid=<%=app.getAppid()%>" style="text-decoration: none;"><font style="font-size: 18px; font-weight: bold; color: #cccccc;"><%=app.getTitle()%></font></a>
             <br/>
             <font style="font-size: 20px; font-weight: bold; color: #666666;"><%=supportissue.getSubject()%></font>
             <br/><br/>
             <form action="support.jsp" method="post">
                <input type="hidden" name="action" value="save">
                <input type="hidden" name="supportissueid" value="<%=supportissue.getSupportissueid()%>">
                <input type="hidden" name="showstatus" value="<%=request.getParameter("showstatus")%>">
                <input type="hidden" name="showtype" value="<%=request.getParameter("showtype")%>">
                <table cellpadding="5" cellspacing="0" border="0">

                    <%
                        for (Iterator<Supportissuecomm> iterator=supportissue.getSupportissuecomms().iterator(); iterator.hasNext();) {
                            Supportissuecomm supportissuecomm=iterator.next();
                            User userwhosent=User.get(supportissuecomm.getUserid());
                    %>
                            <tr>
                                <td valign="top" width="20%" style="text-align: right;">
                                    <font style="font-size: 12px; font-weight: bold;"><%=userwhosent.getFirstname()%> <%=userwhosent.getLastname()%></font>
                                    <br/>
                                    <font style="font-size: 9px;"><%=Time.agoText(Time.getCalFromDate(supportissuecomm.getDatetime()))%></font>
                                </td>
                                <td valign="top">
                                    <font style="font-size: 10px;"><%=supportissuecomm.getNotes()%></font>
                                    <br/><br/>
                                </td>
                            </tr>
                            <%
                        }
                    %>
                    <tr>
                        <td valign="top" width="20%" style="text-align: right;">
                            <font style="font-size: 12px; font-weight: bold;"><%=Pagez.getUserSession().getUser().getFirstname()%> <%=Pagez.getUserSession().getUser().getLastname()%></font>
                            <br/>
                            <font style="font-size: 9px;">Anything you'd like to add?</font>
                        </td>
                        <td valign="top">
                            <textarea rows="5" cols="35" name="notes"></textarea>
                        </td>
                    </tr>




                    <tr>
                        <td valign="top" width="20%" style="text-align: right;">
                            <font style="font-size: 12px; font-weight: bold;">Current Status</font>
                        </td>
                        <td valign="top">
                            <%
                            String statusStr="New";
                            if (supportissue.getStatus() == Supportissue.STATUS_NEW) {
                                statusStr="New";
                            } else if (supportissue.getStatus() == Supportissue.STATUS_REPLYWAITING) {
                                statusStr="Reply Waiting";
                            } else if (supportissue.getStatus() == Supportissue.STATUS_CLOSED) {
                                statusStr="Closed";
                            }
                            %>
                            <font style="font-size: 12px;"><%=statusStr%></font>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top" width="20%" style="text-align: right;">
                            <font style="font-size: 12px; font-weight: bold;">Set Status To</font>
                        </td>
                        <td valign="top">
                            <select name="status">
                                <%
                                if (supportissue.getStatus()==Supportissue.STATUS_NEW){
                                    supportissue.setStatus(Supportissue.STATUS_REPLYWAITING);
                                }
                                %>
                                <%String newSel=""; if (supportissue.getStatus()==Supportissue.STATUS_NEW){newSel=" selected";}%>
                                <option value="<%=Supportissue.STATUS_NEW%>" <%=newSel%>>New</option>
                                <%String rwSel=""; if (supportissue.getStatus()==Supportissue.STATUS_REPLYWAITING){rwSel=" selected";}%>
                                <option value="<%=Supportissue.STATUS_REPLYWAITING%>" <%=rwSel%>>Reply Waiting for User</option>
                                <%String clSel=""; if (supportissue.getStatus()==Supportissue.STATUS_CLOSED){clSel=" selected";}%>
                                <option value="<%=Supportissue.STATUS_CLOSED%>" <%=clSel%>>Closed</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top" width="20%" style="text-align: right;">
                            <font style="font-size: 12px; font-weight: bold;">Type</font>
                        </td>
                        <td valign="top">
                            <select name="type">
                                <%String undefSel=""; if (supportissue.getType()==Supportissue.TYPE_UNDEFINED){undefSel=" selected";}%>
                                <option value="<%=Supportissue.TYPE_UNDEFINED%>" <%=undefSel%>>Undefined</option>
                                <%String bugSel=""; if (supportissue.getType()==Supportissue.TYPE_BUG){bugSel=" selected";}%>
                                <option value="<%=Supportissue.TYPE_BUG%>" <%=bugSel%>>Bug</option>
                                <%String frSel=""; if (supportissue.getType()==Supportissue.TYPE_FEATUREREQUEST){frSel=" selected";}%>
                                <option value="<%=Supportissue.TYPE_FEATUREREQUEST%>" <%=frSel%>>Feature Request</option>
                                <%String naaSel=""; if (supportissue.getType()==Supportissue.TYPE_NEWAPPIDEA){naaSel=" selected";}%>
                                <option value="<%=Supportissue.TYPE_NEWAPPIDEA%>" <%=naaSel%>>New App Idea</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">

                        </td>
                        <td valign="top">
                            <input type="submit" value="Exert Some Authoritayyy!!!">
                        </td>
                    </tr>
                </table>
            </form>


             <%


            }
            %>
        </td>
    </tr>
</table>





<%@ include file="footer.jsp" %>