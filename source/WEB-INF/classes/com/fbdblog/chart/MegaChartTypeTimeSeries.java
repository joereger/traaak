package com.fbdblog.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.data.time.TimeSeriesCollection;


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
        return chart;
    }

}
