package com.fbdblog.chart;


import com.fbdblog.util.Util;
import com.fbdblog.util.Num;
import com.fbdblog.session.UserSession;
import com.fbdblog.dao.Chart;

import java.util.Calendar;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * Represents a chart.  Note that MegaChartSeries does the heavy lifting of data retrieval for a data series.
 */
public class MegaChart {


    private Chart chart;
    private ArrayList<MegaChartSeries> megaChartSeries = null;
    private MegaChartEntryChooser entryChooser = null;
    private String xAxisTitle = "";
    private String yAxisTitle = "";
    private int[] yquestionid = new int[0];





    public MegaChart(int chartid){
        chart = Chart.get(chartid);
    }

    public MegaChart(javax.servlet.http.HttpServletRequest request){
        populateFromRequest(request);
    }




    public void populateFromRequest(javax.servlet.http.HttpServletRequest request){
        if (chart==null){
            chart = new Chart();
        }
        if (request.getParameter("chartname")!=null && !request.getParameter("chartname").equals("")){
            chart.setName(request.getParameter("chartname"));
        }

        chart.setAppid(0);

        chart.setXquestionid(ChartFieldEntrydatetime.ID);
        if (request.getParameter("xQuestionid")!=null && Num.isinteger(request.getParameter("xQuestionid"))){
            chart.setXquestionid(Integer.parseInt(request.getParameter("xQuestionid")));
        }

        yquestionid = new int[0];
        if (request.getParameterValues("yquestionid")!=null){
            String[] yQuestionParams = request.getParameterValues("yquestionid");
            for (int i = 0; i < yQuestionParams.length; i++) {
                if (Num.isinteger(yQuestionParams[i])){
                    yquestionid = Util.addToIntArray(yquestionid, Integer.parseInt(yQuestionParams[i]));
                }
            }
        } else {
            yquestionid = new int[1];
            yquestionid[0] = ChartFieldEntrycount.ID;
        }

        chart.setDaterange(MegaConstants.DATERANGEALLTIME);
        if (request.getParameter("daterange")!=null && Num.isinteger(request.getParameter("daterange"))){
            chart.setDaterange(Integer.parseInt(request.getParameter("daterange")));
        }

        chart.setYaxiswhattodo(MegaConstants.YAXISWHATTODOSUM);
        if (request.getParameter("yaxiswhattodo")!=null && Num.isinteger(request.getParameter("yaxiswhattodo"))){
            chart.setYaxiswhattodo(Integer.parseInt(request.getParameter("yaxiswhattodo")));
        }

        chart.setCharttype(MegaConstants.CHARTTYPELINE);
        if (request.getParameter("charttype")!=null && Num.isinteger(request.getParameter("charttype"))){
            chart.setCharttype(Integer.parseInt(request.getParameter("charttype")));
        }
        chart.setLastxdays(1);
        if (request.getParameter("lastxdays")!=null && Num.isinteger(request.getParameter("lastxdays"))){
            chart.setLastxdays(Integer.parseInt(request.getParameter("lastxdays")));
        }
        chart.setLastxweeks(1);
        if (request.getParameter("lastxweeks")!=null && Num.isinteger(request.getParameter("lastxweeks"))){
            chart.setLastxweeks(Integer.parseInt(request.getParameter("lastxweeks")));
        }
        chart.setLastxmonths(1);
        if (request.getParameter("lastxmonths")!=null && Num.isinteger(request.getParameter("lastxmonths"))){
            chart.setLastxmonths(Integer.parseInt(request.getParameter("lastxmonths")));
        }
        chart.setLastxyears(1);
        if (request.getParameter("lastxyears")!=null && Num.isinteger(request.getParameter("lastxyears"))){
            chart.setLastxyears(Integer.parseInt(request.getParameter("lastxyears")));
        }
        chart.setDaterangefromyyyy(Calendar.getInstance().get(Calendar.YEAR)-1);
        if (request.getParameter("daterangefromyyyy")!=null && Num.isinteger(request.getParameter("daterangefromyyyy"))){
            chart.setDaterangefromyyyy(Integer.parseInt(request.getParameter("daterangefromyyyy")));
        }
        chart.setDaterangefrommm(Calendar.getInstance().get(Calendar.MONTH)+1);
        if (request.getParameter("daterangefrommm")!=null && Num.isinteger(request.getParameter("daterangefrommm"))){
            chart.setDaterangefrommm(Integer.parseInt(request.getParameter("daterangefrommm")));
        }
        chart.setDaterangefromdd(Calendar.getInstance().get(Calendar.DATE));
        if (request.getParameter("daterangefromdd")!=null && Num.isinteger(request.getParameter("daterangefromdd"))){
            chart.setDaterangefromdd(Integer.parseInt(request.getParameter("daterangefromdd")));
        }
        chart.setDaterangetoyyyy(Calendar.getInstance().get(Calendar.YEAR));
        if (request.getParameter("daterangetoyyyy")!=null && Num.isinteger(request.getParameter("daterangetoyyyy"))){
            chart.setDaterangetoyyyy(Integer.parseInt(request.getParameter("daterangetoyyyy")));
        }
        chart.setDaterangetomm(Calendar.getInstance().get(Calendar.MONTH)+1);
        if (request.getParameter("daterangetomm")!=null && Num.isinteger(request.getParameter("daterangetomm"))){
            chart.setDaterangetomm(Integer.parseInt(request.getParameter("daterangetomm")));
        }
        chart.setDaterangetodd(Calendar.getInstance().get(Calendar.DATE));
        if (request.getParameter("daterangetodd")!=null && Num.isinteger(request.getParameter("daterangetodd"))){
            chart.setDaterangetodd(Integer.parseInt(request.getParameter("daterangetodd")));
        }
    }

    public void loadMegaChartSeriesData(UserSession userSession){
        Logger logger = Logger.getLogger(this.getClass().getName());
        //Get the list of entries that this chart covers
        entryChooser = new MegaChartEntryChooser(this, userSession.getApp().getAppid(), userSession.getUser().getUserid());
        entryChooser.populate();

        int debugCount = 0;
        megaChartSeries = new ArrayList<MegaChartSeries>();
        //Iterate yAxis and create a series for each
        for (int i = 0; i < yquestionid.length; i++) {
            int yMegaFieldidTmp = yquestionid[i];
            MegaChartSeries seriesTmp = new MegaChartSeries(yMegaFieldidTmp, userSession.getApp().getAppid(), this, entryChooser);
            xAxisTitle = seriesTmp.getxAxisTitle();
            yAxisTitle = seriesTmp.getyAxisTitle();
            megaChartSeries.add(seriesTmp);
            logger.debug("MegaChart.java - seriesTmp.cleanData.length="+seriesTmp.cleanData.length);
            debugCount = debugCount + seriesTmp.cleanData.length;
        }
        logger.debug("MegaChart.java - items graphed="+debugCount);
    }


    public ArrayList<MegaChartSeries> getMegaChartSeries() {
        return megaChartSeries;
    }

    public void setMegaChartSeriesAsPreview(){
        megaChartSeries = new ArrayList<MegaChartSeries>();
        //Iterate yAxis and create a series for each
        for (int i = 0; i < yquestionid.length; i++) {
            MegaChartSeries seriesTmp = new MegaChartPreviewSeries(chart.getXquestionid(), yquestionid[i]);
            megaChartSeries.add(seriesTmp);
        }
    }

    public MegaChartEntryChooser getEntryChooser() {
        return entryChooser;
    }


    public int getxMegadatatype(){
        if (megaChartSeries!=null && megaChartSeries.size()>0){
            return megaChartSeries.get(0).getxMegadatatype();
        } else {
            return DataTypeString.DATATYPEID;
        }
    }

    public void truncateDataToCertainNumberOfPoints(int maxNumberOfPoints){
        //For each series in the chart
        for (Iterator it = megaChartSeries.iterator(); it.hasNext(); ) {
            MegaChartSeries series = (MegaChartSeries)it.next();
            //If the data is bigger than the maxNumberOfPoints
            if (series.cleanData!=null && series.cleanData.length>maxNumberOfPoints){
                String[][] tmpData = new String[maxNumberOfPoints][];
                //Dump the first maxNumberOfPoints data points into tmpData
                for(int i=0; i<maxNumberOfPoints; i++){
                    tmpData[i] = series.cleanData[i];
                }
                //Set the series data to the tmpData
                series.cleanData = tmpData;
            }
        }
    }





    public String getxAxisTitle() {
        return xAxisTitle;
    }

    public String getyAxisTitle() {
        return yAxisTitle;
    }

    public Chart getChart() {
        return chart;
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }

    public String getXAxisTitle() {
        return xAxisTitle;
    }

    public void setXAxisTitle(String xAxisTitle) {
        this.xAxisTitle = xAxisTitle;
    }

    public String getYAxisTitle() {
        return yAxisTitle;
    }

    public void setYAxisTitle(String yAxisTitle) {
        this.yAxisTitle = yAxisTitle;
    }

    public int[] getYquestionid() {
        return yquestionid;
    }

}
