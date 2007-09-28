package com.fbdblog.chart.chartcache;

import com.fbdblog.systemprops.InstanceProperties;
import com.fbdblog.chart.MegaChart;
import com.fbdblog.chart.MegaChartFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * User: Joe Reger Jr
 * Date: Sep 26, 2007
 * Time: 1:49:06 PM
 */
public class GetChart {

    public static void getChart(OutputStream outStream, HttpServletResponse response, int chartid, int userid, int comparetouserid, String size){
        Logger logger = Logger.getLogger(GetChart.class);

        //Get the chart and load its data
        MegaChart megaChart=new MegaChart(chartid);

        //Build the filename
        String sep="/";
        String compareto = "";
        if (comparetouserid>0){
            compareto = "comparetouserid-"+comparetouserid+sep;
        }
        String filename= InstanceProperties.getAbsolutepathtochartfiles() + sep + "appid-" + megaChart.getChart().getAppid() + sep + "userid-" + userid + sep + "chartid-" + chartid + sep + compareto + "chart-" + size + ".png";
        filename = FilenameUtils.separatorsToSystem(filename);
        logger.debug("filename="+filename);

        //Does the file exist?
        File img = new File(filename);
        if (img.exists()){
            //Pull img from disk, write to browser
            logger.debug("file exists, returning from disk");
            writeFileToBrowser(img, outStream, response);
        } else {
            //Generate the img, store it and return it to the browser
            logger.debug("file does not exist, generating chart");
            byte[] chartBytes = generateChart(chartid, userid, comparetouserid, size);
            writeBytesToFileCache(img, chartBytes);
            writeBytesToBrowser(chartBytes, "image/png", outStream, response);
        }
    }

    private static byte[] generateChart(int chartid, int userid, int comparetouserid, String size){
        Logger logger = Logger.getLogger(GetChart.class);
        //Get the chart and load its data
        MegaChart megaChart=new MegaChart(chartid);
//        if (ispreview || megaChart.getChart().getChartid() == 0 || userid == 0) {
//            megaChart.setMegaChartSeriesAsPreview();
//        } else {
            megaChart.loadMegaChartSeriesData(megaChart.getChart().getAppid(), userid, comparetouserid);
//        }

        //Chart size
        int chartwidth=600;
        int chartheight=300;
        if (size.equals("tiny")) {
            chartwidth=200;
            chartheight=125;
        } else if (size.equals("profilenarrow")) {
            chartwidth=180;
            chartheight=100;
        } else if (size.equals("profilewide")) {
            chartwidth=380;
            chartheight=200;
        } else if (size.equals("small")) {
            chartwidth=400;
            chartheight=250;
        } else if (size.equals("medium")) {
            chartwidth=600;
            chartheight=300;
        } else if (size.equals("large")) {
            chartwidth=900;
            chartheight=600;
        } else if (size.equals("xlarge")) {
            chartwidth=1200;
            chartheight=1000;
        }

        //Get the chart
        JFreeChart chart=MegaChartFactory.get(megaChart);

        //Chart bg color
        Color col1=new Color(255, 255, 255);
        chart.setBackgroundPaint(col1);
        //Plot bg color
        Plot plot=chart.getPlot();
        Color col2=new Color(250, 250, 250, 255);
        plot.setBackgroundPaint(col2);
    
        //Create a bufferedimage from the chart
        BufferedImage bufImg=chart.createBufferedImage(chartwidth, chartheight);
        //Encode as a PNG
        try{
            byte[] imgBytes=ChartUtilities.encodeAsPNG(bufImg);
            return imgBytes;
        } catch (Exception ex){
            logger.error(ex);
        }
        return new byte[0];
    }

    private static void writeFileToBrowser(File file, OutputStream outStream, HttpServletResponse response){
        Logger logger = Logger.getLogger(GetChart.class);
        try {
            response.setContentType("image/jpeg");
            FileInputStream fis=new FileInputStream(file);
            if (fis != null) {
                byte[] data=new byte[4096];
                try {
                    int bytesRead;
                    while ((bytesRead=fis.read(data)) != -1) {
                        outStream.write(data, 0, bytesRead);
                    }
                    fis.close();
                } catch (IOException e) {
                    //Do nothing unless debugging
                    logger.debug("IO Exception attempting to read file.  Error occurred on mediaout.log: '" + file.getAbsolutePath() + "' - " + e.toString());
                    logger.debug(e);
                } catch (Throwable e) {
                    logger.error(e);
                }
            }
        } catch (IOException e) {
            //This is a specific error that happens when users abort their connection.
            logger.debug(e);
        } catch (java.lang.IllegalStateException e) {
            //This is a specific error that happens when users abort their connection.
            logger.debug(e);
        } catch (Exception e) {
            if (!e.toString().substring(0, 21).equals("ClientAbortException:")) {
                logger.error(e);
            } else {
                logger.debug(e);
            }
        }
    }

    private static void writeBytesToBrowser(byte[] bytes, String contenttype, OutputStream outStream, HttpServletResponse response){
        Logger logger = Logger.getLogger(GetChart.class);
        try{
            //Output to browser
            response.setContentType(contenttype);
            //Write to the browser
            outStream.write(bytes);
            //Close the output stream
            outStream.close();
        } catch (IOException e) {
            //This is a specific error that happens when users abort their connection.
            logger.debug(e);
        } catch (java.lang.IllegalStateException e) {
            //This is a specific error that happens when users abort their connection.
            logger.debug(e);
        } catch (Exception e) {
            if (!e.toString().substring(0, 21).equals("ClientAbortException:")) {
                logger.error(e);
            } else {
                logger.debug(e);
            }
        }
    }

    private static void writeBytesToFileCache(File file, byte[] bytes){
        Logger logger = Logger.getLogger(GetChart.class);
        try{
            if (!file.exists()){
                logger.debug("file does not exist so creating path");
                String dirStr = FilenameUtils.getFullPath(file.getAbsolutePath());
                File dir = new File(dirStr);
                FileUtils.forceMkdir(dir);
            } else {
                logger.debug("file exists");
            }
            FileOutputStream fileOut = new FileOutputStream(file);
            fileOut.write(bytes);
            fileOut.close();
        } catch (Exception ex){
            ex.printStackTrace();
            logger.error(ex);
        }
    }


}
