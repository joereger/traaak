package com.fbdblog.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.TickUnitSource;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Chart Type
 */
public class MegaChartTypeStackedAreaChart implements MegaChartType{

    public JFreeChart getJFreeChart(MegaChart megaChart) {
        //Dataset to hold data
        DefaultTableXYDataset stackedareadata = MegaChartConvertToJFreeDataType.defaultTableXYDataset(megaChart);

        //Create the chart
        return ChartFactory.createStackedXYAreaChart(megaChart.getChartname(), megaChart.getxAxisTitle(), megaChart.getyAxisTitle(), stackedareadata,PlotOrientation.VERTICAL, true, false, false);
    }

    public JFreeChart formatChart(JFreeChart chart) {


        return chart;
    }

}
