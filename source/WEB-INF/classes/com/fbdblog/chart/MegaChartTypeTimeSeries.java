package com.fbdblog.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.TimeSeriesCollection;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Calendar;
import java.util.Date;

import jofc2.model.Chart;
import jofc2.model.Text;
import jofc2.model.axis.XAxis;
import jofc2.model.axis.YAxis;
import jofc2.model.elements.ScatterChart;
import com.fbdblog.util.Num;
import com.fbdblog.util.Util;
import com.fbdblog.util.Time;


/**
 * Chart Type
 */
public class MegaChartTypeTimeSeries implements MegaChartType{

    public JFreeChart getJFreeChart(MegaChart megaChart) {
        //Dataset to hold data
        TimeSeriesCollection timedataseries = MegaChartConvertToJFreeDataType.timeSeriesCollection(megaChart);

        //Create the chart
        return ChartFactory.createTimeSeriesChart(megaChart.getChart().getName(), megaChart.getxAxisTitle(), megaChart.getyAxisTitle(), timedataseries, true, false, false);
    }

    public JFreeChart formatChart(JFreeChart chart) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("attempting to format time series");
        XYPlot plot = chart.getXYPlot();
        XYItemRenderer renderer = plot.getRenderer();
        renderer.setBaseStroke(new BasicStroke(2));
        return chart;
    }

    public String chartDataAsJSON(MegaChart megaChart){
        return ChartDataAsJSON.timeSeriesChart(megaChart);
    }

}
