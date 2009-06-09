package com.fbdblog.chart;

import jofc2.OFC;
import jofc2.OFCException;
import jofc2.model.Chart;
import jofc2.model.Text;
import jofc2.model.axis.XAxis;
import jofc2.model.axis.YAxis;
import jofc2.model.axis.Label;
import jofc2.model.axis.XAxisLabels;
import jofc2.model.elements.*;
import jofc2.model.elements.BarChart.Bar;
import jofc2.model.elements.LineChart.Dot;

import java.util.Iterator;
import java.util.ArrayList;

import com.fbdblog.util.Util;
import com.fbdblog.util.Str;
import com.fbdblog.util.Num;
import org.apache.log4j.Logger;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.Millisecond;

/**
 * User: Joe Reger Jr
 * Date: May 30, 2009
 * Time: 9:51:30 AM
 */
public class ChartDataAsJSON {




    public static String scatterLineChart(MegaChart megaChart){
        Logger logger = Logger.getLogger(ChartDataAsJSON.class);
        logger.debug("scatterLineChart() called");
        Chart chart = new Chart();
        chart.setTitle(new Text(megaChart.getChart().getName()));
        chart.setBackgroundColour("#FFFFFF");
        ArrayList<String> xAxisVals = new ArrayList<String>();
        double minXVal = 0;
        double maxXVal = 0;
        double minYVal = 0;
        double maxYVal = 0;
        //Loop on the series of the megaChart
        if (megaChart!=null && megaChart.getMegaChartSeries()!=null){
            for (Iterator it = megaChart.getMegaChartSeries().iterator(); it.hasNext(); ) {
                MegaChartSeries megaChartSeries = (MegaChartSeries)it.next();
                if (megaChartSeries.cleanData!=null && megaChartSeries.cleanData.length>0){
                    //Create the Series
                    ScatterChart lc = new ScatterChart(ScatterChart.Style.LINE);
                    lc.setText(megaChartSeries.getyAxisTitle());
                    lc.setWidth(2);
                    lc.setGradientFill(true);
                    lc.setDotSize(1);
                    //Dot Style
                    ScatterChart.DotStyle dotStyle = new ScatterChart.DotStyle();
                    //dotStyle.setType("hollow-dot");
                    //dotStyle.setDotSize("2");
                    //dotStyle.setWidth("1");
                    dotStyle.setTip(megaChart.getXAxisTitle()+" #x#<br>#y#");
                    lc.setDotStyle(dotStyle);
                    //Iterate the data and create an array for xaxis and an array for yaxis
                    ArrayList<String> yAxisVals =  new ArrayList<String>();
                    for (int i=0; i<megaChartSeries.cleanData.length; i++) {
                        String[] row=megaChartSeries.cleanData[i];
                        //logRow(row);
                        String xVal = row[1];
                        String yVal = row[2];
                        if (Num.isdouble(xVal) && Num.isdouble(yVal)){
                            double dblX = Double.parseDouble(xVal);
                            dblX = Util.doubleRound(dblX, 3);
                            double dblY = Double.parseDouble(yVal);
                            dblY = Util.doubleRound(dblY, 3);
                            logger.debug("++++++ dblX="+dblX);
                            if (dblY!=0){
                                Util.logXY(String.valueOf(dblX), String.valueOf(dblY));
                                lc.addPoints(new ScatterChart.Point(dblX, dblY));
                                xAxisVals.add(row[1]);
                                yAxisVals.add(row[2]);
                                if (dblX==0 || dblX<minXVal){minXVal=dblX;}
                                if (dblX==0 || dblX>maxXVal){maxXVal=dblX;}
                                if (dblY==0 || dblY<minYVal){minYVal=dblY;}
                                if (dblY==0 || dblY>maxYVal){maxYVal=dblY;}
                            }
                        }
                    }
                    //Add the series to the chart
                    chart.addElements(lc);
                }
            }
        }
        //Set step
        int desiredSteps = 5;
        Number step = desiredSteps;
        if (maxXVal>10){
            step = (maxXVal/desiredSteps);
        }
        //Set the xAxis on the chart
        XAxis xAxis = new XAxis();
        //xAxis.setLabels(xAxisVals);
        xAxis.setSteps(step.intValue());
        xAxis.setRange(minXVal, maxXVal, step.intValue());
        //xAxis.setRange(0, 1000);
        xAxis.setGridColour("#DDDEE1");
        xAxis.setColour("#96A9C5");
        chart.setXAxis(xAxis);
        //YAxis
        YAxis ya = new YAxis();
        ya.setGridColour("#DDDEE1");
        ya.setColour("#96A9C5");
        ya.setMin(minYVal);
        ya.setMax(maxYVal);
        chart.setYAxis(ya);
        //Final schtuff
        chart.setFixedNumDecimalsForced(false);
        chart.setDecimalSeparatorIsComma(false);
        return chart.toDebugString();
    }

    public static String barChart(MegaChart megaChart){
        Logger logger = Logger.getLogger(ChartDataAsJSON.class);
        logger.debug("barChart() called");
        Chart chart = new Chart();
        chart.setTitle(new Text(megaChart.getChart().getName()));
        chart.setBackgroundColour("#FFFFFF");
        ArrayList<String> xAxisVals = new ArrayList<String>();
        double minYVal = 0;
        double maxYVal = 0;
        //Loop on the series of the megaChart
        if (megaChart!=null && megaChart.getMegaChartSeries()!=null){
            for (Iterator it = megaChart.getMegaChartSeries().iterator(); it.hasNext(); ) {
                MegaChartSeries megaChartSeries = (MegaChartSeries)it.next();
                if (megaChartSeries.cleanData!=null && megaChartSeries.cleanData.length>0){
                    //Create the Series
                    BarChart bc = new BarChart(BarChart.Style.GLASS);
                    //bc.setTooltip(megaChartSeries.getyAxisTitle()+" #x#<br>#y#");
                    //bc.setAlpha(0.3f);
                    //Iterate the data and create an array for xaxis and an array for yaxis
                    ArrayList<String> yAxisVals =  new ArrayList<String>();
                    for (int i=0; i<megaChartSeries.cleanData.length; i++) {
                        String[] row=megaChartSeries.cleanData[i];
                        Util.logRow(row);
                        String xVal = row[1];
                        String yVal = row[2];
                        if (Num.isdouble(yVal)){
                            double dblY = Double.parseDouble(yVal);
                            dblY = Util.doubleRound(dblY, 3);
                            //if (dblY!=0){
                                BarChart.Bar bar = new BarChart.Bar(dblY);
                                bar.setTooltip(megaChart.getXAxisTitle()+"="+xVal+"<br>#val#");
                                bc.addBars(bar);
                                xAxisVals.add(row[1]);
                                yAxisVals.add(row[2]);
                                if (dblY==0 || dblY<minYVal){minYVal=dblY;}
                                if (dblY==0 || dblY>maxYVal){maxYVal=dblY;}
                            //}
                        }
                    }
                    //Add the series to the chart
                    chart.addElements(bc);
                }
            }
        }
        //Set the xAxis on the chart
        //XAxis xAxis = new XAxis();
        //xAxis.setLabels(xAxisVals);
        //xAxis.setGridColour("#DDDEE1");
        //xAxis.setColour("#96A9C5");
        //chart.setXAxis(xAxis);
        //YAxis
        YAxis ya = new YAxis();
        ya.setGridColour("#DDDEE1");
        ya.setColour("#96A9C5");
        ya.setMin(minYVal);
        ya.setMax(maxYVal);
        chart.setYAxis(ya);
        //Final schtuff
        chart.setFixedNumDecimalsForced(false);
        chart.setDecimalSeparatorIsComma(false);
        return chart.toDebugString();
    }

    public static String timeSeriesChart(MegaChart megaChart){
        Logger logger = Logger.getLogger(ChartDataAsJSON.class);
        logger.debug("timeSeriesChart() called");
        Chart chart = new Chart();
        chart.setTitle(new Text(megaChart.getChart().getName()));
        chart.setBackgroundColour("#FFFFFF");
        ArrayList<String> xAxisVals = new ArrayList<String>();
        double minXVal = 0;
        double maxXVal = 0;
        double minYVal = 0;
        double maxYVal = 0;
        //Loop on the series of the megaChart
        if (megaChart!=null && megaChart.getMegaChartSeries()!=null){
            for (Iterator it = megaChart.getMegaChartSeries().iterator(); it.hasNext(); ) {
                MegaChartSeries megaChartSeries = (MegaChartSeries)it.next();
                if (megaChartSeries.cleanData!=null && megaChartSeries.cleanData.length>0){
                    //Create the Series
                    ScatterChart lc = new ScatterChart(ScatterChart.Style.LINE);
                    lc.setText(megaChartSeries.getyAxisTitle());
                    lc.setWidth(2);
                    lc.setGradientFill(true);
                    lc.setDotSize(1);
                    //Dot Style
                    ScatterChart.DotStyle dotStyle = new ScatterChart.DotStyle();
                    //dotStyle.setType("hollow-dot");
                    //dotStyle.setDotSize("2");
                    //dotStyle.setWidth("1");
                    dotStyle.setTip("#date#<br>#y#");
                    lc.setDotStyle(dotStyle);
                    //Iterate the data and create an array for xaxis and an array for yaxis
                    ArrayList<String> yAxisVals =  new ArrayList<String>();
                    for (int i=0; i<megaChartSeries.cleanData.length; i++) {
                        String[] row=megaChartSeries.cleanData[i];
                        //logRow(row);
                        String xVal = row[1];
                        String yVal = row[2];
                        if (Num.isdouble(xVal) && Num.isdouble(yVal)){
                            double dblX = Double.parseDouble(xVal);
                            //dblX = Util.doubleRound(dblX, 3);
                            double dblY = Double.parseDouble(yVal);
                            dblY = Util.doubleRound(dblY, 3);
                            logger.debug("++++++ dblX="+dblX);
                            if (dblY!=0){
                                Util.logXY(String.valueOf(dblX/1000), String.valueOf(dblY));
                                lc.addPoints(new ScatterChart.Point(dblX/1000, dblY)); //Unix timestamp used in patched OFC is 1000th of java time in millis
                                xAxisVals.add(row[1]);
                                yAxisVals.add(row[2]);
                                if (dblX==0 || dblX<minXVal){minXVal=dblX;}
                                if (dblX==0 || dblX>maxXVal){maxXVal=dblX;}
                                if (dblY==0 || dblY<minYVal){minYVal=dblY;}
                                if (dblY==0 || dblY>maxYVal){maxYVal=dblY;}
                            }
                        }
                    }
                    //Add the series to the chart
                    chart.addElements(lc);
                }                                         
            }
        }
        //Set step
        int desiredSteps = 5;
        Number step = desiredSteps;
        if (maxXVal>10){
            step = (maxXVal/desiredSteps);
        }
        //Set the xAxis on the chart
        XAxis xAxis = new XAxis();
        xAxis.setGridColour("#DDDEE1");
        xAxis.setColour("#96A9C5");
        XAxisLabels xLabel = new XAxisLabels();
        xLabel.setText(".");
        xLabel.setVisible(false);
        xAxis.addLabels(xLabel);
        chart.setXAxis(xAxis);
//        xAxis.setMin(new Double(minXVal).intValue());
//        xAxis.setMax(new Double(maxXVal).intValue());
//        xAxis.setGridColour("#DDDEE1");
//        xAxis.setColour("#96A9C5");
//        //Xaxis labels
//        xAxis.setSteps(86400); //seconds in a day
//        //ArrayList<Label> xaxislabels = new ArrayList<Label>();
//        XAxisLabels xLabel = new XAxisLabels();
//        xLabel.setText("#date#");
//        xLabel.setRotation(Label.Rotation.VERTICAL);
//        //xaxislabels.add(xLabel);
//        xAxis.addLabels(xLabel);
//        //Add xaxis to chart
//        chart.setXAxis(xAxis);
        //YAxis
        YAxis ya = new YAxis();
        ya.setGridColour("#DDDEE1");
        ya.setColour("#96A9C5");
        ya.setMin(minYVal);
        ya.setMax(maxYVal);
        chart.setYAxis(ya);
        //Final schtuff
        chart.setFixedNumDecimalsForced(false);
        chart.setDecimalSeparatorIsComma(false);
        return chart.toDebugString();
    }

    public static String stackedBarChart(MegaChart megaChart){
        Logger logger = Logger.getLogger(ChartDataAsJSON.class);
        logger.debug("stackedBarChart() called");
        Chart chart = new Chart();
        chart.setTitle(new Text(megaChart.getChart().getName()));
        chart.setBackgroundColour("#FFFFFF");
        ArrayList<String> xAxisVals = new ArrayList<String>();
        double minYVal = 0;
        double maxYVal = 0;
        //Loop on the series of the megaChart
        if (megaChart!=null && megaChart.getMegaChartSeries()!=null){
            for (Iterator it = megaChart.getMegaChartSeries().iterator(); it.hasNext(); ) {
                MegaChartSeries megaChartSeries = (MegaChartSeries)it.next();
                if (megaChartSeries.cleanData!=null && megaChartSeries.cleanData.length>0){
                    //Create the Series
                    StackedBarChart bc = new StackedBarChart();
                    bc.setTooltip(megaChart.getXAxisTitle()+"=#x#<br>#val#");
                    //bc.setAlpha(0.3f);
                    //Iterate the data and create an array for xaxis and an array for yaxis
                    ArrayList<String> yAxisVals =  new ArrayList<String>();
                    for (int i=0; i<megaChartSeries.cleanData.length; i++) {
                        String[] row=megaChartSeries.cleanData[i];
                        Util.logRow(row);
                        String xVal = row[1];
                        String yVal = row[2];
                        if (Num.isdouble(yVal)){
                            double dblY = Double.parseDouble(yVal);
                            dblY = Util.doubleRound(dblY, 3);
                            //if (dblY!=0){
                                StackedBarChart.Stack bar = new StackedBarChart.Stack();
                                bar.addValues(dblY);
                                bc.addStack(bar);
                                xAxisVals.add(row[1]);
                                yAxisVals.add(row[2]);
                                if (dblY==0 || dblY<minYVal){minYVal=dblY;}
                                if (dblY==0 || dblY>maxYVal){maxYVal=dblY;}
                            //}
                        }
                    }
                    //Add the series to the chart
                    chart.addElements(bc);
                }
            }
        }
        //Set the xAxis on the chart
        XAxis xAxis = new XAxis();
        xAxis.setLabels(xAxisVals);
        xAxis.setGridColour("#DDDEE1");
        xAxis.setColour("#96A9C5");
        //chart.setXAxis(xAxis);
        //YAxis
        YAxis ya = new YAxis();
        ya.setGridColour("#DDDEE1");
        ya.setColour("#96A9C5");
        ya.setMin(minYVal);
        ya.setMax(maxYVal);
        chart.setYAxis(ya);
        //Final schtuff
        chart.setFixedNumDecimalsForced(false);
        chart.setDecimalSeparatorIsComma(false);
        return chart.toDebugString();
    }

    public static String horizontalBar(MegaChart megaChart){
        Logger logger = Logger.getLogger(ChartDataAsJSON.class);
        logger.debug("horizontalBar() called");
        Chart chart = new Chart();
        chart.setTitle(new Text(megaChart.getChart().getName()));
        chart.setBackgroundColour("#FFFFFF");
        ArrayList<String> xAxisVals = new ArrayList<String>();
        double minYVal = 0;
        double maxYVal = 0;
        //Loop on the series of the megaChart
        if (megaChart!=null && megaChart.getMegaChartSeries()!=null){
            for (Iterator it = megaChart.getMegaChartSeries().iterator(); it.hasNext(); ) {
                MegaChartSeries megaChartSeries = (MegaChartSeries)it.next();
                if (megaChartSeries.cleanData!=null && megaChartSeries.cleanData.length>0){
                    //Create the Series
                    HorizontalBarChart bc = new HorizontalBarChart();
                    bc.setTooltip(megaChart.getXAxisTitle()+"=#x#<br>#val#");
                    //bc.setAlpha(0.3f);
                    //Iterate the data and create an array for xaxis and an array for yaxis
                    ArrayList<String> yAxisVals =  new ArrayList<String>();
                    for (int i=0; i<megaChartSeries.cleanData.length; i++) {
                        String[] row=megaChartSeries.cleanData[i];
                        Util.logRow(row);
                        String xVal = row[1];
                        String yVal = row[2];
                        if (Num.isdouble(yVal)){
                            double dblY = Double.parseDouble(yVal);
                            dblY = Util.doubleRound(dblY, 3);
                            //if (dblY!=0){
                                HorizontalBarChart.Bar bar = new HorizontalBarChart.Bar(dblY);
                                bc.addBars(bar);
                                xAxisVals.add(row[1]);
                                yAxisVals.add(row[2]);
                                if (dblY==0 || dblY<minYVal){minYVal=dblY;}
                                if (dblY==0 || dblY>maxYVal){maxYVal=dblY;}
                            //}
                        }
                    }
                    //Add the series to the chart
                    chart.addElements(bc);
                }
            }
        }
        //Set the xAxis on the chart
        XAxis xAxis = new XAxis();
        xAxis.setLabels(xAxisVals);
        xAxis.setGridColour("#DDDEE1");
        xAxis.setColour("#96A9C5");
        //chart.setXAxis(xAxis);
        //YAxis
        YAxis ya = new YAxis();
        ya.setGridColour("#DDDEE1");
        ya.setColour("#96A9C5");
        ya.setMin(minYVal);
        ya.setMax(maxYVal);
        chart.setYAxis(ya);
        //Final schtuff
        chart.setFixedNumDecimalsForced(false);
        chart.setDecimalSeparatorIsComma(false);
        return chart.toDebugString();
    }

    public static String pieChart(MegaChart megaChart){
        Logger logger = Logger.getLogger(ChartDataAsJSON.class);
        logger.debug("pieChart() called");
        Chart chart = new Chart();
        chart.setTitle(new Text(megaChart.getChart().getName()));
        chart.setBackgroundColour("#FFFFFF");
        ArrayList<String> xAxisVals = new ArrayList<String>();
        double minYVal = 0;
        double maxYVal = 0;
        //Loop on the series of the megaChart
        if (megaChart!=null && megaChart.getMegaChartSeries()!=null){
            for (Iterator it = megaChart.getMegaChartSeries().iterator(); it.hasNext(); ) {
                MegaChartSeries megaChartSeries = (MegaChartSeries)it.next();
                if (megaChartSeries.cleanData!=null && megaChartSeries.cleanData.length>0){
                    //Create the Series
                    PieChart bc = new PieChart();
                    //bc.setAlpha(0.3f);
                    //Iterate the data and create an array for xaxis and an array for yaxis
                    ArrayList<String> yAxisVals =  new ArrayList<String>();
                    for (int i=0; i<megaChartSeries.cleanData.length; i++) {
                        String[] row=megaChartSeries.cleanData[i];
                        Util.logRow(row);
                        String xVal = row[1];
                        String yVal = row[2];
                        if (Num.isdouble(yVal)){
                            double dblY = Double.parseDouble(yVal);
                            dblY = Util.doubleRound(dblY, 3);
                            if (dblY!=0){
                                bc.addSlice(dblY, xVal);
                                //bc.setTooltip(megaChart.getXAxisTitle()+"="+xVal+"<br>"+yVal);
                                xAxisVals.add(row[1]);
                                yAxisVals.add(row[2]);
                                if (dblY==0 || dblY<minYVal){minYVal=dblY;}
                                if (dblY==0 || dblY>maxYVal){maxYVal=dblY;}
                            }
                        }
                    }
                    //Add the series to the chart
                    chart.addElements(bc);
                }
            }
        }
        //Set the xAxis on the chart
        XAxis xAxis = new XAxis();
        xAxis.setLabels(xAxisVals);
        xAxis.setGridColour("#DDDEE1");
        xAxis.setColour("#96A9C5");
        //chart.setXAxis(xAxis);
        //YAxis
        YAxis ya = new YAxis();
        ya.setGridColour("#DDDEE1");
        ya.setColour("#96A9C5");
        ya.setMin(minYVal);
        ya.setMax(maxYVal);
        chart.setYAxis(ya);
        //Final schtuff
        chart.setFixedNumDecimalsForced(false);
        chart.setDecimalSeparatorIsComma(false);
        return chart.toDebugString();
    }

    public static String areaChart(MegaChart megaChart){
        Logger logger = Logger.getLogger(ChartDataAsJSON.class);
        logger.debug("areaChart() called");
        Chart chart = new Chart();
        chart.setTitle(new Text(megaChart.getChart().getName()));
        chart.setBackgroundColour("#FFFFFF");
        ArrayList<String> xAxisVals = new ArrayList<String>();
        double minYVal = 0;
        double maxYVal = 0;
        //Loop on the series of the megaChart
        if (megaChart!=null && megaChart.getMegaChartSeries()!=null){
            for (Iterator it = megaChart.getMegaChartSeries().iterator(); it.hasNext(); ) {
                MegaChartSeries megaChartSeries = (MegaChartSeries)it.next();
                if (megaChartSeries.cleanData!=null && megaChartSeries.cleanData.length>0){
                    //Create the Series
                    AreaHollowChart bc = new AreaHollowChart();
                    //bc.setAlpha(0.3f);
                    //Iterate the data and create an array for xaxis and an array for yaxis
                    ArrayList<String> yAxisVals =  new ArrayList<String>();
                    for (int i=0; i<megaChartSeries.cleanData.length; i++) {
                        String[] row=megaChartSeries.cleanData[i];
                        Util.logRow(row);
                        String xVal = row[1];
                        String yVal = row[2];
                        if (Num.isdouble(yVal)){
                            double dblY = Double.parseDouble(yVal);
                            dblY = Util.doubleRound(dblY, 3);
                            if (dblY!=0){
                                BarChart.Bar bar = new BarChart.Bar(dblY);
                                bar.setTooltip(megaChart.getXAxisTitle()+"="+xVal+"<br>#val#");
                                bc.addValues(dblY);
                                xAxisVals.add(row[1]);
                                yAxisVals.add(row[2]);
                                if (dblY==0 || dblY<minYVal){minYVal=dblY;}
                                if (dblY==0 || dblY>maxYVal){maxYVal=dblY;}
                            }
                        }
                    }
                    //Add the series to the chart
                    chart.addElements(bc);
                }
            }
        }
        //Set the xAxis on the chart
        //XAxis xAxis = new XAxis();
        //xAxis.setLabels(xAxisVals);
        //xAxis.setGridColour("#DDDEE1");
        //xAxis.setColour("#96A9C5");
        //chart.setXAxis(xAxis);
        //YAxis
        YAxis ya = new YAxis();
        ya.setGridColour("#DDDEE1");
        ya.setColour("#96A9C5");
        ya.setMin(minYVal);
        ya.setMax(maxYVal);
        chart.setYAxis(ya);
        //Final schtuff
        chart.setFixedNumDecimalsForced(false);
        chart.setDecimalSeparatorIsComma(false);
        return chart.toDebugString();
    }


}
