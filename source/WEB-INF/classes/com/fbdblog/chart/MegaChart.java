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
    private String xQuestionChoice = "";
    private String [] yQuestionChoice = new String[0];





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
        xQuestionChoice = "";
        chart.setXquestionid(MegaConstants.XAXISDATETIME);
        chart.setAppid(0);
        if (request.getParameter("xQuestionChoice")!=null){
            xQuestionChoice =request.getParameter("xQuestionChoice");
        }
        if(!xQuestionChoice.equals("")){
            //Break apart the xQuestionChoice which is of the format <fieldid>_<logid>_<eventtypeid>
            if (Num.isinteger(xQuestionChoice.split("_")[0])){
                chart.setXquestionid(Integer.parseInt(xQuestionChoice.split("_")[0]));
            }
            if (Num.isinteger(xQuestionChoice.split("_")[1])){
                chart.setAppid(Integer.parseInt(xQuestionChoice.split("_")[1]));
            }
        }


        yQuestionChoice = new String[0];
        yQuestionid = new int[0];
        if (request.getParameter("yQuestionChoice")!=null){
            yQuestionChoice =request.getParameterValues("yQuestionChoice");
        }
        if (yQuestionChoice.length>0){
            //Break apart the yQuestionChoice which is of the format <fieldid>_<logid>_<eventtypeid>
            //reger.core.Debug.logtodb("Starting to dissect yQuestionChoice", "MegaChart.java");
            for (int i = 0; i < yQuestionChoice.length; i++) {
                if (Num.isinteger(yQuestionChoice[i].split("_")[0]) && !Util.arrayContainsValue(yQuestionid, new Integer(yQuestionChoice[i].split("_")[0]).intValue())){
                    if (Num.isinteger(yQuestionChoice[i].split("_")[0])){
                        yQuestionid = Util.addToIntArray(yQuestionid, Integer.parseInt(yQuestionChoice[i].split("_")[0]));
                    }
                }
                //reger.core.Debug.logIntArrayToDb("MegaChart.java - inside loop on yQuestionChoice i="+i+" now tracking yQuestionid", yQuestionid);

            }
            //reger.core.Debug.logIntArrayToDb("MegaChart.java - FINAL yQuestionid", yQuestionid);
            //reger.core.Debug.logtodb("Done dissecting yQuestionChoice", "MegaChart.java");
        } else {
            yQuestionid = new int[1];
            yQuestionid[0] = MegaConstants.YAXISCOUNT;
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
        chart.setDaterangetomm(Calendar.getInstance().get(Calendar.MONTH)+1));
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
        for (int i = 0; i < yQuestionid.length; i++) {
            int yMegaFieldidTmp = yQuestionid[i];
            MegaChartSeries seriesTmp = new MegaChartSeries(yMegaFieldidTmp, appid, this, entryChooser);
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
        for (int i = 0; i < yQuestionid.length; i++) {
            MegaChartSeries seriesTmp = new MegaChartPreviewSeries(xQuestionid, yQuestionid[i]);
            megaChartSeries.add(seriesTmp);
        }
    }

    public MegaChartEntryChooser getEntryChooser() {
        return entryChooser;
    }

    public void save(){


        //Try to update the chart
        boolean updateSuccessful = false;
        if (chartid>0){
            //-----------------------------------
            //-----------------------------------
            int count = Db.RunSQLUpdate("UPDATE megachart SET chartname='"+Util.cleanForSQL(chartname)+"', xeventtypeid='"+xeventtypeid+"', xlogid='"+ appid +"', xquestionid='"+ xQuestionid +"', yaxiswhattodo='"+yaxiswhattodo+"', chartsize='"+chartsize+"', charttype='"+charttype+"', daterange='"+daterange+"', lastxdays='"+lastxdays+"', lastxweeks='"+lastxweeks+"', lastxmonths='"+lastxmonths+"', lastxyears='"+lastxyears+"', daterangefromyyyy='"+daterangefromyyyy+"', daterangefrommm='"+daterangefrommm+"', daterangefromdd='"+daterangefromdd+"', daterangetoyyyy='"+daterangetoyyyy+"', daterangetomm='"+daterangetomm+"', daterangetodd='"+daterangetodd+"', daterangesavedsearchid='"+daterangesavedsearchid+"', userid='"+ userid +"' WHERE chartid='"+chartid+"'");
            //-----------------------------------
            //-----------------------------------
            if (count>0){
                updateSuccessful=true;
            }
        }

        //Insert a new record, if necessary
        if (!updateSuccessful){
            //Store the main chart
            //-----------------------------------
            //-----------------------------------
            chartid = Db.RunSQLInsert("INSERT INTO megachart(chartname, xeventtypeid, xlogid, xquestionid, yaxiswhattodo, chartsize, charttype, daterange, lastxdays, lastxweeks, lastxmonths, lastxyears, daterangefromyyyy, daterangefrommm, daterangefromdd, daterangetoyyyy, daterangetomm, daterangetodd, daterangesavedsearchid, userid) VALUES('"+Util.cleanForSQL(chartname)+"', '"+xeventtypeid+"', '"+ appid +"', '"+ xQuestionid +"', '"+yaxiswhattodo+"', '"+chartsize+"', '"+charttype+"', '"+daterange+"', '"+lastxdays+"', '"+lastxweeks+"', '"+lastxmonths+"', '"+lastxyears+"', '"+daterangefromyyyy+"', '"+daterangefrommm+"', '"+daterangefromdd+"', '"+daterangetoyyyy+"', '"+daterangetomm+"', '"+daterangetodd+"', '"+daterangesavedsearchid+"', '"+ userid +"')");
            //-----------------------------------
            //-----------------------------------

            //Update the AccountCounts cache
            reger.cache.AccountCountCache.flushByAccountid(userid);
        }

        //Clean out any existing yaxis values
        //-----------------------------------
        //-----------------------------------
        int count = Db.RunSQLUpdate("DELETE FROM megachartyaxis WHERE chartid='"+chartid+"'");
        //-----------------------------------
        //-----------------------------------


        //Store the yaxis values
        for(int i=0; i< yQuestionid.length; i++){
            //-----------------------------------
            //-----------------------------------
            int identity = Db.RunSQLInsert("INSERT INTO megachartyaxis(chartid, yquestionid, ylogid, yeventtypeid) VALUES('"+chartid+"', '"+ yQuestionid[i]+"', '"+yLogid[i]+"', '"+yEventtypeid[i]+"')");
            //-----------------------------------
            //-----------------------------------
        }


    }

    public void delete(){
        //Delete the megachart
        //-----------------------------------
        //-----------------------------------
        int count = Db.RunSQLUpdate("DELETE FROM megachart WHERE chartid='"+chartid+"'");
        //-----------------------------------
        //-----------------------------------

        //Delete the megachart yaxis values
        //-----------------------------------
        //-----------------------------------
        int count2 = Db.RunSQLUpdate("DELETE FROM megachartyaxis WHERE chartid='"+chartid+"'");
        //-----------------------------------
        //-----------------------------------


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
                    tmpData[i] = getMegaChartSeries()[j].cleanData[i];
                }
                //Set the series data to the tmpData
                series.cleanData = tmpData;
            }
        }



    }


    public String getXQuestionChoice() {
        return xQuestionChoice;
    }

    public void setxMegafieldChoice(String xMegafieldChoice) {
        this.xQuestionChoice = xMegafieldChoice;
    }

    public String[] getYQuestionChoice() {
        return yQuestionChoice;
    }

    public void setyMegafieldChoice(String[] yMegafieldChoice) {
        this.yQuestionChoice = yMegafieldChoice;
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
}
