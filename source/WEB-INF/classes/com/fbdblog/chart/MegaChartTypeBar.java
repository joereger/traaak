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
import jofc2.model.elements.BarChart;

import java.util.ArrayList;
import java.util.Iterator;

import com.fbdblog.util.Num;
import com.fbdblog.util.Util;

/**
 * Chart Type
 */
public class MegaChartTypeBar implements MegaChartType{

    public JFreeChart getJFreeChart(MegaChart megaChart) {
        //Dataset to hold data
        DefaultCategoryDataset dataset = MegaChartConvertToJFreeDataType.defaultCategoryDataset(megaChart);

        //Create the chart
        return ChartFactory.createBarChart(megaChart.getChart().getName(), megaChart.getxAxisTitle(), megaChart.getyAxisTitle(), dataset, PlotOrientation.VERTICAL, true, false, false);
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
        double minYVal = 0;
        double maxYVal = 0;
        //Loop on the series of the megaChart
        if (megaChart!=null && megaChart.getMegaChartSeries()!=null){
            for (Iterator it = megaChart.getMegaChartSeries().iterator(); it.hasNext(); ) {
                MegaChartSeries megaChartSeries = (MegaChartSeries)it.next();
                if (megaChartSeries.cleanData!=null && megaChartSeries.cleanData.length>0){
                    //Create the Series
                    BarChart bc = new BarChart(BarChart.Style.GLASS);
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
                                bc.addBars(bar);
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


}
