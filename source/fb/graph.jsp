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
page import="com.fbdblog.session.UserSession" %><%
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


    //try {

        //Determine whether or not this is a preview of a chart.
        boolean ispreview = false;
        if (request.getParameter("ispreview") != null && request.getParameter("ispreview").equals("1")) {
            ispreview = true;
        }

        //Get the chart and load its data
        MegaChart megaChart = new MegaChart(request);
        if (ispreview || userSession.getApp()==null || userSession.getUser()==null) {
            megaChart.setMegaChartSeriesAsPreview();
        } else {
            megaChart.loadMegaChartSeriesData(userSession);
        }

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

        //Chart size
        int chartwidth = 600;
        int chartheight = 300;
        if (request.getParameter("size") != null && request.getParameter("size").equals("tiny")) {
            chartwidth = 200;
            chartheight = 125;
        } else if (request.getParameter("size") != null && request.getParameter("size").equals("small")) {
            chartwidth = 400;
            chartheight = 250;
        } else if (request.getParameter("size") != null && request.getParameter("size").equals("medium")) {
            chartwidth = 600;
            chartheight = 300;
        } else if (request.getParameter("size") != null && request.getParameter("size").equals("large")) {
            chartwidth = 900;
            chartheight = 600;
        } else if (request.getParameter("size") != null && request.getParameter("size").equals("extralarge")) {
            chartwidth = 1200;
            chartheight = 1000;
        }

        //Create a bufferedimage from the chart
        BufferedImage bufImg = chart.createBufferedImage(chartwidth, chartheight);
        //Encode as a PNG
        byte[] imgBytes = ChartUtilities.encodeAsPNG(bufImg);
        //Write to the browser
        outStream.write(imgBytes);

        //Record the bytesOutput to the database
        int bytesOutput = imgBytes.length;
        logger.debug("Graph bytesOutput=" + bytesOutput);

        //Chart End
//    } catch (Exception e) {
//        logger.error(e);
//    }

    //Close the output stream
    outStream.close();
%>