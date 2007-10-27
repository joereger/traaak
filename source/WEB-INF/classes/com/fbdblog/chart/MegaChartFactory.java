package com.fbdblog.chart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.ContourPlot;
import org.jfree.chart.axis.DateAxis;
import com.fbdblog.qtype.Timeperiod;


/**
 * Gets you a jFreechart object
 */
public class MegaChartFactory {

    public static JFreeChart get(MegaChart megaChart){
        //Default Type
        MegaChartType ct = new MegaChartTypeLine();

        //Boolean if
//        boolean isdisplayoverride
//        if (megaChart.getChart().getXquestionid()>1000000){
//
//        }

        //Figure out which type the user wants
        if (megaChart.getxMegadatatype()==DataTypeString.DATATYPEID || megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPE3DBAR || megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPEHORIZONTALBAR || megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPEHORIZONTAL3DBAR || megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPEBAR  || megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHART  || megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHART3D || megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHARTHORIZONTAL || megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHART3DHORIZONTAL) {
            if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPE3DBAR) {
                ct = new MegaChartType3DBar();
            } else if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPEHORIZONTALBAR) {
                ct = new MegaChartTypeHorizontalBar();
            } else if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPEHORIZONTAL3DBAR) {
                ct = new MegaChartTypeHorizontal3dBar();
            } else if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHART) {
                ct = new MegaChartTypeStackedBar();
            } else if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHART3D) {
                ct = new MegaChartTypeStackedBar3d();
            } else if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHARTHORIZONTAL) {
                ct = new MegaChartTypeStackedBarHorizontal();
            } else if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHART3DHORIZONTAL) {
                ct = new MegaChartTypeStackedBar3dHorizontal();
            } else {
                ct = new MegaChartTypeBar();
            }
        }
        if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPEPIE){
           ct = new MegaChartTypePie();
        }
        if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPE3DPIE){
           ct = new MegaChartTypePie3d();
        }
        if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPESCATTERPLOT){
           ct = new MegaChartTypeScatterPlot();
        }
        if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPESTEPCHART){
           ct = new MegaChartTypeStepChart();
        }
        if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPEAREACHART){
           ct = new MegaChartTypeAreaChart();
        }
        if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPESTACKEDAREA){
           ct = new MegaChartTypeStackedAreaChart();
        }
        if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPELINE){
           ct = new MegaChartTypeLine();
        }
        if (megaChart.getChart().getXquestionid()==ChartFieldEntrydatetime.ID) {
            ct = new MegaChartTypeTimeSeries();
        }

        //Truncate the data for performance
        megaChart.truncateDataToCertainNumberOfPoints(2000);
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
            if (megaChart.getMegaChartSeries().get(0).yFieldtype== Timeperiod.ID){
                //First, create the rangeAxis
                DateAxis rangeAxis = new DateAxis("");
                java.text.DateFormat formatter = (java.text.DateFormat)new java.text.SimpleDateFormat("HH:mm:ss");
                formatter.setTimeZone(java.util.TimeZone.getTimeZone("EST"));
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
