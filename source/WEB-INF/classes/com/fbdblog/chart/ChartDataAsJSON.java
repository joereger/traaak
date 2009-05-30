package com.fbdblog.chart;

import jofc2.OFC;
import jofc2.OFCException;
import jofc2.model.Chart;
import jofc2.model.Text;
import jofc2.model.axis.XAxis;
import jofc2.model.axis.YAxis;
import jofc2.model.elements.BarChart;
import jofc2.model.elements.LineChart;
import jofc2.model.elements.BarChart.Bar;
import jofc2.model.elements.LineChart.Dot;

import java.util.Iterator;

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

        LineChart lc = new LineChart();
        lc.setText("Stuff");
        LineChart lc2 = new LineChart();
        lc2.setColour("#FF0000");
        lc2.setText("Werkzeugbranch");

        BarChart bc3 = new BarChart(BarChart.Style.NORMAL);
        bc3.setAlpha(0.3f);

        for (int i = 1; i <= 3; i++) {
            double valore = Math.random() * 25000 + 10000;
            lc.addDots(new Dot(valore, "#0000FF", 3, 1));
            double value2 = Math.random() * 25000 + 10000;
            lc2.addDots(new Dot(value2, "#FF0000", 3, 1));
            Bar b2 = new Bar(Math.random() * 25000 + 25000);
            bc3.addBars(b2);
        }

        lc.addValues(5,6,7,8);
        lc2.addValues(null,null,null,null);

        chart.addElements(bc3, lc, lc2);

        YAxis ya = new YAxis();
        ya.setGridColour("#DDDEE1");
        ya.setColour("#96A9C5");
        chart.setYAxis(ya);

        XAxis xa = new XAxis();
        xa.setGridColour("#DDDEE1");
        xa.setColour("#96A9C5");

        chart.setXAxis(xa);
        chart.setFixedNumDecimalsForced(true);
        chart.setDecimalSeparatorIsComma(true);
        chart.computeYAxisRange(15);


        for (Iterator<MegaChartSeries> iterator=megaChart.getMegaChartSeries().iterator(); iterator.hasNext();) {
            MegaChartSeries megaChartSeries=iterator.next();
            String[][] cleanData = megaChartSeries.cleanData;
            for (int i=0; i<cleanData.length; i++) {
                String[] row=cleanData[i];
                for (int j=0; j<row.length; j++) {
                    String col=row[j];

                }
            }
        }

        return chart.toDebugString();
    }


}
