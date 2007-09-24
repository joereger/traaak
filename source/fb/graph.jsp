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
page import="com.fbdblog.util.Num" %><%
    //Expected URL format
    //graph.jsp?chartid=34&userid=12&size=small&comparetouserid=45

    //Logger
    Logger logger = Logger.getLogger(this.getClass().getName());

    //Make sure we have a userSession to work with
    UserSession userSession = null;
    Object ustmp = request.getSession().getAttribute("userSession");
    if (ustmp != null) {
        userSession = (UserSession) ustmp;
        logger.debug("Found an existing userSession");
    } else {
        userSession = new UserSession();
        request.getSession().setAttribute("userSession", userSession);
        logger.debug("Had to create a new userSession");
    }

    //Create our output stream to the browser
    OutputStream outStream = response.getOutputStream();

    //Determine whether or not this is a preview of a chart.
    boolean ispreview = false;
    if (request.getParameter("ispreview") != null && request.getParameter("ispreview").equals("1")) {
        ispreview = true;
    }

    //Get the chartid
    int chartid = 0;
    if (request.getParameter("chartid")!=null && Num.isinteger(request.getParameter("chartid"))) {
        chartid = Integer.parseInt(request.getParameter("chartid"));
    }

    //Get the userid
    int userid = 0;
    if (request.getParameter("userid")!=null && Num.isinteger(request.getParameter("userid"))) {
        userid = Integer.parseInt(request.getParameter("userid"));
    }

    //Get the comparetouserid
    int comparetouserid = 0;
    if (request.getParameter("comparetouserid")!=null && Num.isinteger(request.getParameter("comparetouserid"))) {
        comparetouserid = Integer.parseInt(request.getParameter("comparetouserid"));
    }

    //Chart size
    int chartwidth = 600;
    int chartheight = 300;
    String size = "medium";
    if (request.getParameter("size") != null && request.getParameter("size").equals("tiny")) {
        chartwidth = 200;
        chartheight = 125;
        size = "tiny";
    } else if (request.getParameter("size") != null && request.getParameter("size").equals("small")) {
        chartwidth = 400;
        chartheight = 250;
        size = "small";
    } else if (request.getParameter("size") != null && request.getParameter("size").equals("medium")) {
        chartwidth = 600;
        chartheight = 300;
        size = "medium";
    } else if (request.getParameter("size") != null && request.getParameter("size").equals("large")) {
        chartwidth = 900;
        chartheight = 600;
        size = "large";
    } else if (request.getParameter("size") != null && request.getParameter("size").equals("xlarge")) {
        chartwidth = 1200;
        chartheight = 1000;
        size = "xlarge";
    }

    //Get the chart and load its data
    MegaChart megaChart = new MegaChart(chartid);
    if (ispreview || megaChart.getChart().getChartid()==0 || userid==0) {
        megaChart.setMegaChartSeriesAsPreview();
    } else {
        megaChart.loadMegaChartSeriesData(megaChart.getChart().getAppid(), userid, comparetouserid);
    }

    //Build the filename
    String sep = "/";
    String filename = sep+"chartcache"+sep+"appid-"+megaChart.getChart().getAppid()+sep+"userid-"+userid+sep+"chartid-"+megaChart.getChart().getChartid()+sep+"chart-"+size+".png";



    //Get the chart
    JFreeChart chart = MegaChartFactory.get(megaChart);

    //Chart bg color
    Color col1 = new Color(255, 255, 255);
    chart.setBackgroundPaint(col1);
    //Plot bg color
    Plot plot = chart.getPlot();
    Color col2 = new Color(250, 250, 250, 255);
    plot.setBackgroundPaint(col2);

    //Output to browser
    response.setContentType("image/png");

    //Create a bufferedimage from the chart
    BufferedImage bufImg = chart.createBufferedImage(chartwidth, chartheight);
    //Encode as a PNG
    byte[] imgBytes = ChartUtilities.encodeAsPNG(bufImg);
    //Write to the browser
    outStream.write(imgBytes);

    //Record the bytesOutput to the database
    int bytesOutput = imgBytes.length;
    logger.debug("Graph bytesOutput=" + bytesOutput);

    //Close the output stream
    outStream.close();
%>