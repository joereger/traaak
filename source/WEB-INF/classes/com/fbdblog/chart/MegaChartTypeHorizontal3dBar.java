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

/**
 * 3DBar Type
 */
public class MegaChartTypeHorizontal3dBar implements MegaChartType{

    public JFreeChart getJFreeChart(MegaChart megaChart) {
        //Dataset to hold data
        DefaultCategoryDataset dataset = MegaChartConvertToJFreeDataType.defaultCategoryDataset(megaChart);

        //Create the chart
        return ChartFactory.createBarChart3D(megaChart.getChartname(), megaChart.getxAxisTitle(), megaChart.getyAxisTitle(), dataset, PlotOrientation.HORIZONTAL, true, false, false);
    }

    public JFreeChart formatChart(JFreeChart chart) {
        return chart;
    }

}
