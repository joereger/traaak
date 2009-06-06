<%@
page import="org.apache.log4j.Logger" %><%@
page import="com.fbdblog.util.Num" %><%@
page import="com.fbdblog.dao.User" %><%@
page import="com.fbdblog.dao.App" %><%@
page import="com.fbdblog.dao.Chart" %><%@
page import="com.fbdblog.session.FindUserappsettings" %><%@
page import="com.fbdblog.dao.Userappsettings" %><%@
page import="com.fbdblog.chart.*" %>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="com.fbdblog.htmlui.Pagez" %><%
    //Expected URL format
    //graph.jsp?chartid=34&userid=12&size=small&comparetouserid=45&key=KJHGKU

    //Logger
    Logger logger=Logger.getLogger(this.getClass().getName());


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

    boolean safetodisplay=true;

    User user=User.get(userid);
    Chart chart=Chart.get(chartid);
    App app=App.get(chart.getAppid());
    Userappsettings userappsettings=FindUserappsettings.get(user, app);
    if (userappsettings!=null && userappsettings.getIsprivate()) {
        logger.debug("checking key="+request.getParameter("key"));
        //Now need to look for the key
        //String urldecodedKey = URLDecoder.decode(request.getParameter("key"), "UTF-8");
        if (!ChartSecurityKey.isValidChartKey(request.getParameter("key"), userid, chartid)){
            safetodisplay = false;
        }
    }

    //override for sysadmin
    if (Pagez.getUserSession().getIssysadmin()){
        safetodisplay = true;   
    }

    if (safetodisplay) {

        MegaChart megaChart = new MegaChart(chart.getChartid());
        megaChart.loadMegaChartSeriesData(app.getAppid(), user.getUserid(), comparetouserid);
        logger.debug("megaChart.getChart().getCharttype()="+megaChart.getChart().getCharttype());
        String json = MegaChartFactory.getType(megaChart).chartDataAsJSON(megaChart);
        out.print(json);
        out.flush();

//        JSONObject jsonOut = new JSONObject();
//        for (Iterator<MegaChartSeries> iterator=megaChart.getMegaChartSeries().iterator(); iterator.hasNext();) {
//            MegaChartSeries megaChartSeries=iterator.next();
//            JSONObject jsonSeries = new JSONObject();
//            String[][] cleanData = megaChartSeries.cleanData;
//            for (int i=0; i<cleanData.length; i++) {
//                String[] row=cleanData[i];
//                JSONObject jsonRow = new JSONObject();
//                for (int j=0; j<row.length; j++) {
//                    String col=row[j];
//                    jsonRow.put("value", col);
//                }
//                jsonSeries.put("row", jsonRow);
//            }
//            jsonOut.put("series", jsonOut);
//        }
//        out.print(jsonOut);
//        out.flush();



    } else {
        //@todo display an error image... for now it'll be a broken image
    }

%>