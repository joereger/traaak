package com.fbdblog.chart;


import com.fbdblog.util.Util;
import com.fbdblog.util.Num;
import com.fbdblog.dao.Chart;
import com.fbdblog.dao.Chartyaxis;
import com.fbdblog.dao.App;

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
    private MegaChartEntryChooser entryChooserCompareto = null;
    private String xAxisTitle = "";
    private String yAxisTitle = "";
    private int[] yquestionid = new int[0];

    public MegaChart(int chartid){
        chart = Chart.get(chartid);
        if (chart.getChartid()>0){
            //Load yaxis
            yquestionid = new int[0];
            if (chart.getChartyaxes()!=null){
                for (Iterator<Chartyaxis> iterator=chart.getChartyaxes().iterator(); iterator.hasNext();) {
                    Chartyaxis chartyaxis=iterator.next();
                    yquestionid = Util.addToIntArray(yquestionid, chartyaxis.getYquestionid());
                }
            }
        } else {
            chart.setAppid(0);
            chart.setName("");
            chart.setChartid(0);
            chart.setCharttype(MegaConstants.CHARTTYPELINE);
            chart.setDaterange(MegaConstants.DATERANGEALLTIME);
            chart.setDaterangefromdd(0);
            chart.setDaterangefrommm(0);
            chart.setDaterangefromyyyy(0);
            chart.setDaterangetodd(0);
            chart.setDaterangetomm(0);
            chart.setDaterangetoyyyy(0);
            chart.setLastxdays(0);
            chart.setLastxmonths(0);
            chart.setLastxweeks(0);
            chart.setLastxyears(0);
            chart.setXquestionid(ChartFieldEntrydatetime.ID);
            chart.setYaxiswhattodo(MegaConstants.YAXISWHATTODOSUM);

        }
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
        if (request.getParameter("xquestionid")!=null && Num.isinteger(request.getParameter("xquestionid"))){
            chart.setXquestionid(Integer.parseInt(request.getParameter("xquestionid")));
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

    public void loadMegaChartSeriesData(int appid, int userid, int comparetouserid){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("appid="+appid+" userid="+userid);
        if (appid>0 && userid>0){
            //Get the list of entries that this chart covers
            entryChooser = new MegaChartEntryChooser(this, appid, userid);
            entryChooser.populate();
            if (entryChooser.getPosts()!=null){
                logger.debug("entryChooser.getPosts().size()="+entryChooser.getPosts().size());
            } else {
                logger.debug("entryChooser.getPosts() is null");
            }

            if (comparetouserid>0){
                entryChooserCompareto = new MegaChartEntryChooser(this, appid, userid);
                entryChooserCompareto.populate();
            }
        
            int debugCount = 0;
            megaChartSeries = new ArrayList<MegaChartSeries>();
            logger.debug("yquestionid.length="+yquestionid.length);
            //Iterate yAxis and create a series for each
            for (int i = 0; i < yquestionid.length; i++) {
                int tmp = yquestionid[i];
                MegaChartSeries seriesTmp = new MegaChartSeries(yquestionid[i], appid, entryChooser, chart);
                xAxisTitle = seriesTmp.getxAxisTitle();
                yAxisTitle = seriesTmp.getyAxisTitle();
                megaChartSeries.add(seriesTmp);
                //Compare to
                if (comparetouserid>0){
                    MegaChartSeries seriesCompare = new MegaChartSeries(yquestionid[i], appid, entryChooserCompareto, chart);
                    megaChartSeries.add(seriesCompare);
                }
                //Debug
                logger.debug("seriesTmp.cleanData.length="+seriesTmp.cleanData.length);
                debugCount = debugCount + seriesTmp.cleanData.length;
            }
            logger.debug("items graphed="+debugCount);
        }
    }

    public void save(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (chart!=null){
            try{chart.save();}catch(Exception ex){logger.error("",ex);}
        }
        //Save yaxis, first delete all existing entries
        if (chart.getChartyaxes()!=null){
            for (Iterator<Chartyaxis> iterator=chart.getChartyaxes().iterator(); iterator.hasNext();) {
                Chartyaxis chartyaxis=iterator.next();
                try{iterator.remove();}catch(Exception ex){logger.error("",ex);}
            }
        }
        if (yquestionid!=null && yquestionid.length>0){
            for (int i=0; i<yquestionid.length; i++) {
                int yqid=yquestionid[i];
                Chartyaxis chartyaxis = new Chartyaxis();
                chartyaxis.setChartid(chart.getChartid());
                chartyaxis.setYquestionid(yqid);
                try{chartyaxis.save();}catch(Exception ex){logger.error("",ex);}
            }
        }
        //If the app has no primarychartid, use this one
        App app = App.get(chart.getAppid());
        if (app!=null && app.getAppid()>0){
            if (app.getPrimarychartid()<=0){
                app.setPrimarychartid(chart.getChartid());
            }
        }
        //Refresh chart
        try{chart.save();}catch(Exception ex){logger.error("",ex);}
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



    public int getxMegadatatype(){
        if (megaChartSeries!=null && megaChartSeries.size()>0){
            return megaChartSeries.get(0).getxMegadatatype();
        } else {
            return DataTypeString.DATATYPEID;
        }
    }

    public void truncateDataToCertainNumberOfPoints(int maxNumberOfPoints){
        if (megaChartSeries!=null){
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
