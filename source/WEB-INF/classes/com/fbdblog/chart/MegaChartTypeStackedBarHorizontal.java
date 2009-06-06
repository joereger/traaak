package com.fbdblog.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.general.DefaultPieDataset;
import org.apache.log4j.Logger;
import jofc2.model.Chart;
import jofc2.model.Text;
import jofc2.model.axis.XAxis;
import jofc2.model.axis.YAxis;
import jofc2.model.elements.ScatterChart;

import java.util.ArrayList;
import java.util.Iterator;

import com.fbdblog.util.Num;
import com.fbdblog.util.Util;

/**
 * Chart Type
 */
public class MegaChartTypeStackedBarHorizontal implements MegaChartType{

    public JFreeChart getJFreeChart(MegaChart megaChart) {
        //Dataset to hold data
        DefaultCategoryDataset dataset = MegaChartConvertToJFreeDataType.defaultCategoryDataset(megaChart);

        //Create the chart
        return ChartFactory.createStackedBarChart(megaChart.getChart().getName(), megaChart.getxAxisTitle(), megaChart.getyAxisTitle(), dataset, PlotOrientation.HORIZONTAL, true, false, false);
    }

    public JFreeChart formatChart(JFreeChart chart) {
        return chart;
    }

    public String chartDataAsJSON(MegaChart megaChart){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("chartDataAsJSON() called");
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

}
