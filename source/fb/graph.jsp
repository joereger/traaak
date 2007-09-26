<%@
page import="java.io.OutputStream" %><%@
page import="com.fbdblog.chart.MegaChart" %><%@
page import="org.jfree.chart.JFreeChart" %><%@
page import="com.fbdblog.chart.MegaChartFactory" %><%@
page import="java.awt.*" %><%@
page import="org.jfree.chart.plot.Plot" %><%@
page import="java.awt.image.BufferedImage" %><%@
page import="org.jfree.chart.ChartUtilities" %><%@
page import="org.apache.log4j.Logger" %><%@
page import="com.fbdblog.session.UserSession" %><%@
page import="com.fbdblog.util.Num" %><%@
page import="java.io.IOException" %><%@
page import="java.io.FileInputStream" %><%@
page import="org.apache.commons.io.FilenameUtils" %><%@
page import="com.fbdblog.chart.chartcache.GetChart" %><%
    //Expected URL format
    //graph.jsp?chartid=34&userid=12&size=small&comparetouserid=45

    //Logger
    Logger logger=Logger.getLogger(this.getClass().getName());

    //Make sure we have a userSession to work with
    UserSession userSession=null;
    Object ustmp=request.getSession().getAttribute("userSession");
    if (ustmp != null) {
        userSession=(UserSession) ustmp;
        logger.debug("Found an existing userSession");
    } else {
        userSession=new UserSession();
        request.getSession().setAttribute("userSession", userSession);
        logger.debug("Had to create a new userSession");
    }

    //Determine whether or not this is a preview of a chart.
    boolean ispreview=false;
    if (request.getParameter("ispreview") != null && request.getParameter("ispreview").equals("1")) {
        ispreview=true;
    }

    //Get the chartid
    int chartid=0;
    if (request.getParameter("chartid") != null && Num.isinteger(request.getParameter("chartid"))) {
        chartid=Integer.parseInt(request.getParameter("chartid"));
    }

    //Get the userid
    int userid=0;
    if (request.getParameter("userid") != null && Num.isinteger(request.getParameter("userid"))) {
        userid=Integer.parseInt(request.getParameter("userid"));
    }

    //Get the comparetouserid
    int comparetouserid=0;
    if (request.getParameter("comparetouserid") != null && Num.isinteger(request.getParameter("comparetouserid"))) {
        comparetouserid=Integer.parseInt(request.getParameter("comparetouserid"));
    }

    //Size
    String size="medium";
    if (request.getParameter("size") != null && request.getParameter("size").equals("tiny")) {
        size="tiny";
    } else if (request.getParameter("size") != null && request.getParameter("size").equals("small")) {
        size="small";
    } else if (request.getParameter("size") != null && request.getParameter("size").equals("medium")) {
        size="medium";
    } else if (request.getParameter("size") != null && request.getParameter("size").equals("large")) {
        size="large";
    } else if (request.getParameter("size") != null && request.getParameter("size").equals("xlarge")) {
        size="xlarge";
    }

    //Create our output stream to the browser
    OutputStream outStream=response.getOutputStream();
    //Output the actual chart
    GetChart.getChart(outStream, response, chartid, userid, comparetouserid, size);
    
%>