<%@ page import="com.fbdblog.qtype.util.AppTemplateProcessor" %>
<%@ page import="com.fbdblog.qtype.util.AppPostParser" %>
<%@ page import="com.fbdblog.qtype.util.SavePost" %>
<%@ page import="com.fbdblog.qtype.def.ComponentException" %>
<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="com.fbdblog.dao.Chart" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.fbdblog.util.Str" %>
<%@ page import="com.fbdblog.util.Num" %>
<%@ page import="com.fbdblog.systemprops.BaseUrl" %>
<%@ page import="com.fbdblog.chart.ChartSecurityKey" %>
<%@ page import="com.fbdblog.chart.FlashChartWidget" %>

<%
Logger logger = Logger.getLogger(this.getClass().getName());
String pagetitle = "Charts";
String navtab = "youraccount";
String acl = "public";
%>
<%@ include file="header.jsp" %>


<%
    //Load the chart requested
    Chart chart=null;
    if (request.getParameter("chartid") != null && Num.isinteger(request.getParameter("chartid"))) {
        chart=Chart.get(Integer.parseInt(request.getParameter("chartid")));
    }

    //If no chart requested, choose the primary for this app
    if (chart==null || chart.getChartid()<=0){
        chart=Chart.get(Pagez.getUserSession().getApp().getPrimarychartid());
    }
%>

<%String selectedTab="charts";%>
<%@ include file="tabs.jsp" %>



<table>
    <tr>
        <td valign="top" width="10">
        </td>
        <td valign="top" width="600">
            <form action="">
                <input type="hidden" name="nav" value="charts">
                <select name="chartid">
                    <%
                    List<Chart> charts=HibernateUtil.getSession().createCriteria(Chart.class)
                            .add(Restrictions.eq("appid", Pagez.getUserSession().getApp().getAppid()))
                            .setCacheable(true)
                            .list();
                    for (Iterator<Chart> iterator=charts.iterator(); iterator.hasNext();) {
                        Chart chartTmp = iterator.next();
                        String selected = "";
                        if (chart!=null && chartTmp.getChartid()==chart.getChartid()){
                            selected = " selected";
                        }
                        %>
                            <option value="<%=chartTmp.getChartid()%>" <%=selected%>><%=Str.cleanForHtml(chartTmp.getName())%></option>
                        <%
                    }
                    %>
                </select>
                <input type="submit" value="Show">
            </form>
            <br/>
        </td>
        <%if (!Pagez.getUserSession().getIsfacebook()){%>
            <td valign="top" width="315" rowspan="2">
                <font class="mediumfont">Embed into Your Blog/Website</font>
                <br/>
                <script language="javascript">
                    function toggleSmall() {
                        var eleSmall = document.getElementById("toggleTextSmall");
                        var eleMedium = document.getElementById("toggleTextMedium");
                        var eleLarge = document.getElementById("toggleTextLarge");
                        if(eleSmall.style.display == "block") {
                            eleSmall.style.display = "none";
                            eleMedium.style.display = "none";
                            eleLarge.style.display = "none";
                        } else {
                            eleSmall.style.display = "block";
                            eleMedium.style.display = "none";
                            eleLarge.style.display = "none";
                        }
                    }
                    function toggleMedium() {
                        var eleSmall = document.getElementById("toggleTextSmall");
                        var eleMedium = document.getElementById("toggleTextMedium");
                        var eleLarge = document.getElementById("toggleTextLarge");
                        if(eleMedium.style.display == "block") {
                            eleSmall.style.display = "none";
                            eleMedium.style.display = "none";
                            eleLarge.style.display = "none";
                        } else {
                            eleSmall.style.display = "none";
                            eleMedium.style.display = "block";
                            eleLarge.style.display = "none";
                        }
                    }
                    function toggleLarge() {
                        var eleSmall = document.getElementById("toggleTextSmall");
                        var eleMedium = document.getElementById("toggleTextMedium");
                        var eleLarge = document.getElementById("toggleTextLarge");
                        if(eleLarge.style.display == "block") {
                            eleSmall.style.display = "none";
                            eleMedium.style.display = "none";
                            eleLarge.style.display = "none";
                        } else {
                            eleSmall.style.display = "none";
                            eleMedium.style.display = "none";
                            eleLarge.style.display = "block";
                        }
                    }
                </script>

                <table>
                    <tr>
                        <td valign="top">
                            <div style="border: 2px #cccccc solid;">
                                <a href="javascript:toggleSmall();">
                                    <font class="smallfont">Small<br/>200x175</font>
                                </a>
                            </div>
                        </td>
                        <td valign="top">
                            <div style="border: 2px #cccccc solid;">
                                <a href="javascript:toggleMedium();">
                                    <font class="smallfont">Medium<br/>350x250</font>
                                </a>
                            </div>
                        </td>
                        <td valign="top">
                            <div style="border: 2px #cccccc solid;">
                                <a href="javascript:toggleLarge();">
                                    <font class="smallfont">Large<br/>640x300</font>
                                </a>
                            </div>
                        </td>
                    </tr>
                </table>

                <script src="http://cdn.gigya.com/wildfire/js/wfapiv2.js"></script>

                <div id="toggleTextSmall" style="display: block;">
                            <font class="normalfont"><b>Small Embed</b></font><br/>
                            <textarea rows="1" cols="1" id="TEXTAREA_ID_SMALL" style="display: none">
                            <%=FlashChartWidget.getEmbedCode(chart, Pagez.getUserSession().getUser(), 225, 225)%>
                            </textarea>
                            <div id="divWildfirePostSmall"></div>
                            <script>
                            var pconfsmall={
                              widgetTitle: 'Traaak Chart',
                              defaultContent: 'TEXTAREA_ID_SMALL',
                              UIConfig: '<config><display showDesktop="false" showEmail="false" useTransitions="true" showBookmark="false" codeBoxHeight="auto"></display><body><background frame-color="#BFBFBF" background-color="#FFFFFF" gradient-color-begin="#ffffff" gradient-color-end="#F4F4F4" corner-roundness="4;4;4;4"></background><controls color="#202020" corner-roundness="4;4;4;4" gradient-color-begin="#EAEAEA" gradient-color-end="#F4F4F4" bold="false"><snbuttons type="textUnder" frame-color="#D5D5D5" background-color="#fafafa" over-frame-color="#60BFFF" over-background-color="#ebebeb" color="#808080" gradient-color-begin="#FFFFFF" gradient-color-end="d4d6d7" size="10" bold="false" down-frame-color="#60BFFF" down-gradient-color-begin="#6DDADA" over-gradient-color-end="#6DDADA" down-gradient-color-end="#F4F4F4" over-color="#52A4DA" down-color="#52A4DA" over-bold="false"><more frame-color="#A4DBFF" over-frame-color="#A4DBFF" gradient-color-begin="#F4F4F4" gradient-color-end="#BBE4FF" over-gradient-color-begin="#A4DBFF" over-gradient-color-end="#F4F4F4"></more><previous frame-color="#BBE4FF" over-frame-color="#A4DBFF" gradient-color-begin="#FFFFFF" gradient-color-end="#A4DBFF" over-gradient-color-begin="#A4DBFF" over-gradient-color-end="#F4F4F4"></previous></snbuttons><textboxes frame-color="#CACACA" color="#757575" gradient-color-begin="#ffffff" bold="false"><codeboxes color="#757575" frame-color="#DFDFDF" background-color="#FFFFFF" gradient-color-begin="#ffffff" gradient-color-end="#FFFFFF" size="10"></codeboxes><inputs frame-color="#CACACA" color="#757575" gradient-color-begin="#F4F4F4" gradient-color-end="#ffffff"></inputs><dropdowns list-item-over-color="#52A4DA" frame-color="#CACACA"></dropdowns></textboxes><buttons frame-color="#CACACA" gradient-color-begin="#F4F4F4" gradient-color-end="#CACACA" color="#000000" bold="false" over-frame-color="#60BFFF" over-gradient-color-begin="#BBE4FF" down-gradient-color-begin="#BBE4FF" over-gradient-color-end="#FFFFFF" down-gradient-color-end="#ffffff"><post-buttons frame-color="#CACACA" gradient-color-end="#CACACA"></post-buttons></buttons><listboxes frame-color="#CACACA" corner-roundness="4;4;4;4" gradient-color-begin="#F4F4F4" gradient-color-end="#FFFFFF"></listboxes><checkboxes checkmark-color="#00B600" frame-color="#D5D5D5" corner-roundness="3;3;3;3" gradient-color-begin="#F4F4F4" gradient-color-end="#FFFFFF"></checkboxes><servicemarker gradient-color-begin="#ffffff" gradient-color-end="#D5D5D5"></servicemarker><tooltips color="#6D5128" gradient-color-begin="#FFFFFF" gradient-color-end="#FFE4BB" size="10" frame-color="#FFDBA4"></tooltips></controls><texts color="#202020"><headers color="#202020"></headers><messages color="#202020"></messages><links color="#52A4DA" underline="false" over-color="#353535" down-color="#353535" down-bold="false"></links></texts></body></config>'
                            };
                            Wildfire.initPost('551572', 'divWildfirePostSmall', 250, 350, pconfsmall);
                            </script>
                </div>

                <div id="toggleTextMedium" style="display: none">
                            <font class="normalfont"><b>Medium Embed</b></font><br/>
                            <textarea rows="1" cols="1" id="TEXTAREA_ID_MEDIUM" style="display: none">
                            <%=FlashChartWidget.getEmbedCode(chart, Pagez.getUserSession().getUser(), 350, 250)%>
                            </textarea>
                            <div id="divWildfirePostMedium"></div>
                            <script>
                            var pconfmedium={
                              widgetTitle: 'Traaak Chart',
                              defaultContent: 'TEXTAREA_ID_MEDIUM',
                              UIConfig: '<config><display showDesktop="false" showEmail="false" useTransitions="true" showBookmark="false" codeBoxHeight="auto"></display><body><background frame-color="#BFBFBF" background-color="#FFFFFF" gradient-color-begin="#ffffff" gradient-color-end="#F4F4F4" corner-roundness="4;4;4;4"></background><controls color="#202020" corner-roundness="4;4;4;4" gradient-color-begin="#EAEAEA" gradient-color-end="#F4F4F4" bold="false"><snbuttons type="textUnder" frame-color="#D5D5D5" background-color="#fafafa" over-frame-color="#60BFFF" over-background-color="#ebebeb" color="#808080" gradient-color-begin="#FFFFFF" gradient-color-end="d4d6d7" size="10" bold="false" down-frame-color="#60BFFF" down-gradient-color-begin="#6DDADA" over-gradient-color-end="#6DDADA" down-gradient-color-end="#F4F4F4" over-color="#52A4DA" down-color="#52A4DA" over-bold="false"><more frame-color="#A4DBFF" over-frame-color="#A4DBFF" gradient-color-begin="#F4F4F4" gradient-color-end="#BBE4FF" over-gradient-color-begin="#A4DBFF" over-gradient-color-end="#F4F4F4"></more><previous frame-color="#BBE4FF" over-frame-color="#A4DBFF" gradient-color-begin="#FFFFFF" gradient-color-end="#A4DBFF" over-gradient-color-begin="#A4DBFF" over-gradient-color-end="#F4F4F4"></previous></snbuttons><textboxes frame-color="#CACACA" color="#757575" gradient-color-begin="#ffffff" bold="false"><codeboxes color="#757575" frame-color="#DFDFDF" background-color="#FFFFFF" gradient-color-begin="#ffffff" gradient-color-end="#FFFFFF" size="10"></codeboxes><inputs frame-color="#CACACA" color="#757575" gradient-color-begin="#F4F4F4" gradient-color-end="#ffffff"></inputs><dropdowns list-item-over-color="#52A4DA" frame-color="#CACACA"></dropdowns></textboxes><buttons frame-color="#CACACA" gradient-color-begin="#F4F4F4" gradient-color-end="#CACACA" color="#000000" bold="false" over-frame-color="#60BFFF" over-gradient-color-begin="#BBE4FF" down-gradient-color-begin="#BBE4FF" over-gradient-color-end="#FFFFFF" down-gradient-color-end="#ffffff"><post-buttons frame-color="#CACACA" gradient-color-end="#CACACA"></post-buttons></buttons><listboxes frame-color="#CACACA" corner-roundness="4;4;4;4" gradient-color-begin="#F4F4F4" gradient-color-end="#FFFFFF"></listboxes><checkboxes checkmark-color="#00B600" frame-color="#D5D5D5" corner-roundness="3;3;3;3" gradient-color-begin="#F4F4F4" gradient-color-end="#FFFFFF"></checkboxes><servicemarker gradient-color-begin="#ffffff" gradient-color-end="#D5D5D5"></servicemarker><tooltips color="#6D5128" gradient-color-begin="#FFFFFF" gradient-color-end="#FFE4BB" size="10" frame-color="#FFDBA4"></tooltips></controls><texts color="#202020"><headers color="#202020"></headers><messages color="#202020"></messages><links color="#52A4DA" underline="false" over-color="#353535" down-color="#353535" down-bold="false"></links></texts></body></config>'
                            };
                            Wildfire.initPost('551572', 'divWildfirePostMedium', 250, 350, pconfmedium);
                            </script>
                </div>

                <div id="toggleTextLarge" style="display: none">
                            <font class="normalfont"><b>Large Embed</b></font><br/>
                            <textarea rows="1" cols="1" id="TEXTAREA_ID_LARGE" style="display: none">
                            <%=FlashChartWidget.getEmbedCode(chart, Pagez.getUserSession().getUser(), 640, 300)%>
                            </textarea>
                            <div id="divWildfirePostLarge"></div>
                            <script>
                            var pconflarge={
                              widgetTitle: 'Traaak Chart',
                              defaultContent: 'TEXTAREA_ID_lARGE',
                              UIConfig: '<config><display showDesktop="false" showEmail="false" useTransitions="true" showBookmark="false" codeBoxHeight="auto"></display><body><background frame-color="#BFBFBF" background-color="#FFFFFF" gradient-color-begin="#ffffff" gradient-color-end="#F4F4F4" corner-roundness="4;4;4;4"></background><controls color="#202020" corner-roundness="4;4;4;4" gradient-color-begin="#EAEAEA" gradient-color-end="#F4F4F4" bold="false"><snbuttons type="textUnder" frame-color="#D5D5D5" background-color="#fafafa" over-frame-color="#60BFFF" over-background-color="#ebebeb" color="#808080" gradient-color-begin="#FFFFFF" gradient-color-end="d4d6d7" size="10" bold="false" down-frame-color="#60BFFF" down-gradient-color-begin="#6DDADA" over-gradient-color-end="#6DDADA" down-gradient-color-end="#F4F4F4" over-color="#52A4DA" down-color="#52A4DA" over-bold="false"><more frame-color="#A4DBFF" over-frame-color="#A4DBFF" gradient-color-begin="#F4F4F4" gradient-color-end="#BBE4FF" over-gradient-color-begin="#A4DBFF" over-gradient-color-end="#F4F4F4"></more><previous frame-color="#BBE4FF" over-frame-color="#A4DBFF" gradient-color-begin="#FFFFFF" gradient-color-end="#A4DBFF" over-gradient-color-begin="#A4DBFF" over-gradient-color-end="#F4F4F4"></previous></snbuttons><textboxes frame-color="#CACACA" color="#757575" gradient-color-begin="#ffffff" bold="false"><codeboxes color="#757575" frame-color="#DFDFDF" background-color="#FFFFFF" gradient-color-begin="#ffffff" gradient-color-end="#FFFFFF" size="10"></codeboxes><inputs frame-color="#CACACA" color="#757575" gradient-color-begin="#F4F4F4" gradient-color-end="#ffffff"></inputs><dropdowns list-item-over-color="#52A4DA" frame-color="#CACACA"></dropdowns></textboxes><buttons frame-color="#CACACA" gradient-color-begin="#F4F4F4" gradient-color-end="#CACACA" color="#000000" bold="false" over-frame-color="#60BFFF" over-gradient-color-begin="#BBE4FF" down-gradient-color-begin="#BBE4FF" over-gradient-color-end="#FFFFFF" down-gradient-color-end="#ffffff"><post-buttons frame-color="#CACACA" gradient-color-end="#CACACA"></post-buttons></buttons><listboxes frame-color="#CACACA" corner-roundness="4;4;4;4" gradient-color-begin="#F4F4F4" gradient-color-end="#FFFFFF"></listboxes><checkboxes checkmark-color="#00B600" frame-color="#D5D5D5" corner-roundness="3;3;3;3" gradient-color-begin="#F4F4F4" gradient-color-end="#FFFFFF"></checkboxes><servicemarker gradient-color-begin="#ffffff" gradient-color-end="#D5D5D5"></servicemarker><tooltips color="#6D5128" gradient-color-begin="#FFFFFF" gradient-color-end="#FFE4BB" size="10" frame-color="#FFDBA4"></tooltips></controls><texts color="#202020"><headers color="#202020"></headers><messages color="#202020"></messages><links color="#52A4DA" underline="false" over-color="#353535" down-color="#353535" down-bold="false"></links></texts></body></config>'
                            };
                            Wildfire.initPost('551572', 'divWildfirePostLarge', 250, 350, pconflarge);
                            </script>
                </div>



            </td>
        <%}%>
    </tr>
    <tr>
        <td valign="top" width="10">

        </td>
        <td valign="top" width="600">
            <%if (Pagez.getUserSession().getIsfacebook()){%>
                <%
                String key=ChartSecurityKey.getChartKey(Pagez.getUserSession().getUser().getUserid(), chart.getChartid());
                StringBuffer embedHtml = new StringBuffer();
                embedHtml.append("<a href=\""+BaseUrl.get(false)+"user/"+Pagez.getUserSession().getUser().getNickname()+"/\">");
                embedHtml.append("<img src=\""+BaseUrl.get(false)+"fb/graph.jsp?chartid="+chart.getChartid()+"&userid="+Pagez.getUserSession().getUser().getUserid()+"&size=small&key="+key+"\"  alt=\"\" width=\"400\" height=\"250\" style=\"border: 3px solid #e6e6e6;\">");
                embedHtml.append("</a>");
                %>
                <img src="<%=BaseUrl.get(false)%>fb/graph.jsp?chartid=<%=chart.getChartid()%>&userid=<%=Pagez.getUserSession().getUser().getUserid()%>&size=medium&key=<%=key%>" alt="" width="600" height="300" style="border: 3px solid #e6e6e6;"/>
            <%}%>
            <%if (!Pagez.getUserSession().getIsfacebook()) { %>
                    <%=FlashChartWidget.getEmbedCode(chart, Pagez.getUserSession().getUser(), 640, 300)%>
            <%}%>
        </td>
    </tr>
</table>



<br/><br/>
<center>
<%=Pagez.getUserSession().getApp().getAdunderchart()%>
</center>
<br/><br/> 

<br/><br/>


<%@ include file="footer.jsp" %>