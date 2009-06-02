package com.fbdblog.chart;

import jofc2.OFC;
import jofc2.OFCException;
import jofc2.model.Chart;
import jofc2.model.Text;
import jofc2.model.axis.XAxis;
import jofc2.model.axis.YAxis;
import jofc2.model.elements.BarChart;
import jofc2.model.elements.LineChart;
import jofc2.model.elements.ScatterChart;
import jofc2.model.elements.BarChart.Bar;
import jofc2.model.elements.LineChart.Dot;

import java.util.Iterator;
import java.util.ArrayList;

import com.fbdblog.util.Util;
import com.fbdblog.util.Str;
import com.fbdblog.util.Num;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: May 30, 2009
 * Time: 9:51:30 AM
 */
public class ChartDataAsJSON {


    public static String convertToJSON(MegaChart megaChart){
        Chart chart = new Chart();
        chart.setTitle(new Text("Boomps Chart of MegaStuffs Wow"));
        chart.setBackgroundColour("#FFFFFF");

//        BarChart bc3 = new BarChart(BarChart.Style.NORMAL);
//        bc3.setAlpha(0.3f);
//        for (int i = 1; i <= 30; i++) {
//            Bar b2 = new Bar(Math.random() * 25000 + 25000);
//            bc3.addBars(b2);
//        }

        ArrayList<String> xAxisVals = new ArrayList<String>();
        double minXVal = 0;
        double maxXVal = 0;
        double minYVal = 0;
        double maxYVal = 0;

        //Iterate series
        for (Iterator<MegaChartSeries> iterator=megaChart.getMegaChartSeries().iterator(); iterator.hasNext();) {
            MegaChartSeries megaChartSeries=iterator.next();

            //Create the Series
            ScatterChart lc = new ScatterChart(ScatterChart.Style.LINE);
            lc.setText(megaChartSeries.getyAxisTitle());
            lc.setWidth(2);
            lc.setGradientFill(true);
            lc.setDotSize(1);

            //Dot Style
            ScatterChart.DotStyle dotStyle = new ScatterChart.DotStyle();
            dotStyle.setType("hollow-dot");
            dotStyle.setDotSize("2");
            dotStyle.setWidth("1");
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
                    double dblY = Double.parseDouble(yVal);
                    if (dblY!=0){
                        logXY(String.valueOf(dblX), String.valueOf(dblY));
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


        YAxis ya = new YAxis();
        ya.setGridColour("#DDDEE1");
        ya.setColour("#96A9C5");
        ya.setMin(minYVal);
        ya.setMax(maxYVal);
        chart.setYAxis(ya);

        chart.setFixedNumDecimalsForced(false);
        chart.setDecimalSeparatorIsComma(false);
        //chart.computeYAxisRange(15);
        return chart.toDebugString();
    }

    private static void logRow(String[] row){
        Logger logger = Logger.getLogger(ChartDataAsJSON.class);
        StringBuffer out = new StringBuffer();
        for (int j=0; j<row.length; j++) {
            String col=row[j];
            out.append(col + "\t");
        }
        logger.debug(out.toString());
    }

    private static void logXY(String x, String y){
        Logger logger = Logger.getLogger(ChartDataAsJSON.class);
        StringBuffer out = new StringBuffer();
        out.append("x="+x+" y="+y);
        logger.debug(out.toString());
    }


}
