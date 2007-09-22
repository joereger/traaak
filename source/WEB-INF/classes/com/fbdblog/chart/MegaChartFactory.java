package com.fbdblog.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.ContourPlot;
import org.jfree.chart.axis.DateAxis;


/**
 * Gets you a jFreechart object
 */
public class MegaChartFactory {

    public static JFreeChart get(MegaChart megaChart){
        //Default Type
        MegaChartType ct = new MegaChartTypeLine();

        //Figure out which type the user wants
        if (megaChart.getxMegadatatype()==DataTypeString.DATATYPEID || megaChart.getCharttype()==MegaConstants.CHARTTYPE3DBAR || megaChart.getCharttype()==MegaConstants.CHARTTYPEHORIZONTALBAR || megaChart.getCharttype()==MegaConstants.CHARTTYPEHORIZONTAL3DBAR || megaChart.getCharttype()==MegaConstants.CHARTTYPEBAR  || megaChart.getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHART  || megaChart.getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHART3D || megaChart.getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHARTHORIZONTAL || megaChart.getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHART3DHORIZONTAL) {
            if (megaChart.getCharttype()==MegaConstants.CHARTTYPE3DBAR) {
                ct = new MegaChartType3DBar();
            } else if (megaChart.getCharttype()==MegaConstants.CHARTTYPEHORIZONTALBAR) {
                ct = new MegaChartTypeHorizontalBar();
            } else if (megaChart.getCharttype()==MegaConstants.CHARTTYPEHORIZONTAL3DBAR) {
                ct = new MegaChartTypeHorizontal3dBar();
            } else if (megaChart.getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHART) {
                ct = new MegaChartTypeStackedBar();
            } else if (megaChart.getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHART3D) {
                ct = new MegaChartTypeStackedBar3d();
            } else if (megaChart.getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHARTHORIZONTAL) {
                ct = new MegaChartTypeStackedBarHorizontal();
            } else if (megaChart.getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHART3DHORIZONTAL) {
                ct = new MegaChartTypeStackedBar3dHorizontal();
            } else {
                ct = new MegaChartTypeBar();
            }
        }
        if (megaChart.getCharttype()==MegaConstants.CHARTTYPEPIE){
           ct = new MegaChartTypePie();
        }
        if (megaChart.getCharttype()==MegaConstants.CHARTTYPE3DPIE){
           ct = new MegaChartTypePie3d();
        }
        if (megaChart.getCharttype()==MegaConstants.CHARTTYPESCATTERPLOT){
           ct = new MegaChartTypeScatterPlot();
        }
        if (megaChart.getCharttype()==MegaConstants.CHARTTYPESTEPCHART){
           ct = new MegaChartTypeStepChart();
        }
        if (megaChart.getCharttype()==MegaConstants.CHARTTYPEAREACHART){
           ct = new MegaChartTypeAreaChart();
        }
        if (megaChart.getCharttype()==MegaConstants.CHARTTYPESTACKEDAREA){
           ct = new MegaChartTypeStackedAreaChart();
        }
        if (megaChart.getCharttype()==MegaConstants.CHARTTYPELINE){
           ct = new MegaChartTypeLine();
        }
        if (megaChart.getXQuestionid()==FieldType.XAXISDATETIME) {
            ct = new MegaChartTypeTimeSeries();
        }

        //Truncate the data for performance
        megaChart.truncateDataToCertainNumberOfPoints(1000);
        //Create the chart
        JFreeChart chart = ct.getJFreeChart(megaChart);
        //Format the chart
        chart = ct.formatChart(chart);
        //Format yAxis date
        formatYAxisAsDate(chart, megaChart);
        //Return
        return chart;
    }

    public static JFreeChart formatYAxisAsDate(JFreeChart chart, MegaChart megaChart){
         if (megaChart.getMegaChartSeries()!=null && megaChart.getMegaChartSeries().size()>0){
            if (megaChart.getMegaChartSeries().get(0).yFieldtype==FieldType.FIELDTYPETIME){
                //First, create the rangeAxis
                DateAxis rangeAxis = new DateAxis("");
                java.text.DateFormat formatter = (java.text.DateFormat)new java.text.SimpleDateFormat("HH:mm:ss");
                formatter.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
                rangeAxis.setDateFormatOverride(formatter);

                //Next, apply that rangeAxis to as many types of plots as possible

                //XYPlot
                if (chart.getPlot() instanceof XYPlot){
                    XYPlot plot = chart.getXYPlot();
                    plot.setRangeAxis(rangeAxis);
                }
                //CategoryPlot
                if (chart.getPlot() instanceof CategoryPlot){
                    CategoryPlot plot = chart.getCategoryPlot();
                    plot.setRangeAxis(rangeAxis);
                }
                //Contour Plot
                if (chart.getPlot() instanceof ContourPlot){
                    ContourPlot plot = (ContourPlot)chart.getPlot();
                    plot.setRangeAxis(rangeAxis);
                }
            }
         }
         return chart;
    }



}
