package com.fbdblog.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.TickUnitSource;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.general.DefaultPieDataset;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

import jofc2.model.Chart;
import jofc2.model.Text;
import jofc2.model.axis.XAxis;
import jofc2.model.axis.YAxis;
import jofc2.model.elements.ScatterChart;
import com.fbdblog.util.Num;
import com.fbdblog.util.Util;

/**
 * Chart Type
 */
public class MegaChartTypeLine implements MegaChartType{

    public JFreeChart getJFreeChart(MegaChart megaChart) {
        //Dataset to hold data
        XYSeriesCollection xyseries = MegaChartConvertToJFreeDataType.xySeriesCollection(megaChart);
        //Create the chart
        return ChartFactory.createXYLineChart(megaChart.getChart().getName(), megaChart.getxAxisTitle(), megaChart.getyAxisTitle(), xyseries,PlotOrientation.VERTICAL, true, false, false);
    }

    public JFreeChart formatChart(JFreeChart chart) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("attempting to format line chart");
        XYPlot plot = chart.getXYPlot();
        XYItemRenderer renderer = plot.getRenderer();
        logger.debug("renderer.getClass().getName()="+renderer.getClass().getName());
        if (renderer instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer rr = (XYLineAndShapeRenderer) renderer;
            rr.setShapesVisible(true);
            rr.setShapesFilled(true);
            rr.setBaseItemLabelsVisible(true);
        }
        renderer.setBaseStroke(new BasicStroke(2));
        return chart;
    }

    public String chartDataAsJSON(MegaChart megaChart){
        return ChartDataAsJSON.scatterLineChart(megaChart);
    }

}
