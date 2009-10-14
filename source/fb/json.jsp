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
<%@ page import="com.fbdblog.htmlui.Pagez" %>
<%@ page import="com.fbdblog.cache.providers.CacheFactory" %><%
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
    boolean cache = true;

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
        //To Cache or Not to Cache, that is the Question
        String jsonOut= null;
        String nameInCache = "jsondata-c"+chartid+"-u"+userid+"-ct"+comparetouserid+"-ispreview"+ispreview;
        String cacheGroup =  "embeddedjsoncache"+"/"+"appid"+chart.getAppid()+"-userid"+userid; //Flushed in ClearCache.java
        Object fromCache = CacheFactory.getCacheProvider("DbcacheProvider").get(nameInCache, cacheGroup);
        if (!cache){logger.debug("cache off");} else {logger.debug("cache on");}
        logger.debug("fromCache="+String.valueOf((String)fromCache));
        if (fromCache!=null && cache){
            logger.debug("returning jsonOut from cache");
            jsonOut = (String)fromCache;
            logger.debug("jsonOut="+ jsonOut);
        } else {
            logger.debug("rebuilding jsonOut and putting into cache");
            try{
                MegaChart megaChart = new MegaChart(chart.getChartid());
                megaChart.loadMegaChartSeriesData(app.getAppid(), user.getUserid(), comparetouserid);
                logger.debug("megaChart.getChart().getCharttype()="+megaChart.getChart().getCharttype());
                jsonOut = MegaChartFactory.getType(megaChart).chartDataAsJSON(megaChart);
                logger.debug("jsonOut="+ jsonOut);
                //Consider: jsonOut = URLEncoder.encode(jsonOut.toString(), "UTF-8");
                //Cache put
                if (1==1){
                    try{
                        //Put bytes into cache
                        logger.debug("putting to cache jsonOut="+ jsonOut);
                        CacheFactory.getCacheProvider("DbcacheProvider").put(nameInCache, cacheGroup, jsonOut);
                    } catch (Exception ex){
                        logger.error("Error with transform in bottom section",ex);
                    }
                }
            } catch (Exception ex){
                logger.error("Error getting survey from cache: ex.getMessage()="+ex.getMessage(), ex);
            }
        }

        //Print it out
        out.print(jsonOut);
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
        out.print("Error.");
        out.flush();
    }

%>