package com.fbdblog.chart;

import com.fbdblog.util.Time;
import com.fbdblog.util.Util;

import java.util.Calendar;

/**
 * Generates preview data for mega charts.
 */
public class MegaChartPreviewSeries extends MegaChartSeries {


    /**
     * Constructor
     * Preview constructor
     */
    public MegaChartPreviewSeries(int xMegafieldid, int yMegafieldid){

        //Basic plan here is to create two string arrays with the correct type of dummy preview data
        int sizeofpreviewset = 10;
        String[] xPreviewdata = new String[sizeofpreviewset-1];
        String[] yPreviewdata = new String[sizeofpreviewset-1];

        //Tell this object to respect the fieldid's coming in from the constructor
        this.xQuestionid =xMegafieldid;
        this.yQuestionid =yMegafieldid;

        //Set title and other properties for X axis.
        setXTitleBasedOnFieldId();

        //Set title and other properties for X axis.
        setYTitleBasedOnFieldId();

        //Get our two datasets
        xPreviewdata = previewDataGet(xMegafieldid, xMegadatatype, sizeofpreviewset, false);
        yPreviewdata = previewDataGet(yMegafieldid, yMegadatatype, sizeofpreviewset, true);

        //Now combine them into raw data
        rawChartData = new String[sizeofpreviewset][3];
        for (int i = 0; i <sizeofpreviewset; i++) {
            rawChartData[i][0]=String.valueOf(i);
            rawChartData[i][1]=xPreviewdata[i];
            rawChartData[i][2]=yPreviewdata[i];
        }

        //Util.logDoubleStringArrayToDb("rawChartData[][] Preview Data", rawChartData);

        //Clean the data
       massageData();
    }

    private String[] previewDataGet(int questionid, int Megadatatype, int howmanydatapoints, boolean isYaxis){
        //Util.logtodb("previewDataGet(questionid="+questionid+", Megadatatype="+Megadatatype+", howmanydatapoints="+howmanydatapoints +", isYaxis="+isYaxis);

        if (questionid>0){
            if (Megadatatype==DataTypeString.DATATYPEID){
                return previewDataAlpha(howmanydatapoints);
            } else if (Megadatatype==DataTypeDatetime.DATATYPEID){
                return previewDataTime(howmanydatapoints);
            } else {
                return previewDataNumeric(howmanydatapoints);
            }
        } else {
            if (!isYaxis){
                //It's the X axis
                if (questionid==ChartFieldEntryDayofweek.ID) {
                    return previewDataTimeExt(howmanydatapoints, questionid);
                } else if (questionid==ChartFieldEntryDayofmonth.ID) {
                    return previewDataTimeExt(howmanydatapoints, questionid);
                } else if (questionid==ChartFieldEntryHourofday.ID) {
                    return previewDataTimeExt(howmanydatapoints, questionid);
                } else if (questionid==ChartFieldEntryorder.ID) {
                    //This one's different
                    return previewDataNumeric(howmanydatapoints);
                } else if (questionid==ChartFieldEntryWeeksAgo.ID) {
                    return previewDataTimeExt(howmanydatapoints, questionid);
                } else if (questionid==ChartFieldEntryMonthsAgo.ID) {
                    return previewDataTimeExt(howmanydatapoints, questionid);
                } else if (questionid==ChartFieldEntrydatetime.ID) {
                    return previewDataTimeExt(howmanydatapoints, questionid);
                }
            } else {
                //It's a Y Axis
                if (questionid==ChartFieldEntrycount.ID) {
                    return previewDataCount(howmanydatapoints);
                }
            }
        }

       return previewDataNumeric(howmanydatapoints);
    }

    private String[] previewDataNumeric(int howmanydatapoints){
        String[] out = new String[howmanydatapoints];
        for(int i=0; i<=(howmanydatapoints-1); i++){
            out[i]=String.valueOf((Math.random()*100+1));
        }
        return out;
    }

     private String[] previewDataCount(int howmanydatapoints){
        String[] out = new String[howmanydatapoints];
        for(int i=0; i<=(howmanydatapoints-1); i++){
            out[i]=String.valueOf(1);
        }
        return out;
    }



    private String[] previewDataAlpha(int howmanydatapoints){
        String[] out = new String[howmanydatapoints];
        for(int i=0; i<=(howmanydatapoints-1); i++){
            out[i]="Data"+i;
        }
        return out;
    }

    private String[] previewDataTime(int howmanydatapoints){
        String[] out = new String[howmanydatapoints];
        Calendar cal = Calendar.getInstance();
        for(int i=0; i<=(howmanydatapoints-1); i++){
            out[i]= Time.dateformatfordb(cal);
            cal.add(Calendar.DATE, -1);
        }
        return out;
    }

    private String[] previewDataTimeExt(int howmanydatapoints, int questionid){
        String[] out = new String[howmanydatapoints];

        //Need to figure out what units we're subtracting from
        int unit = Calendar.DATE;
        int max = 31;
        if (questionid==ChartFieldEntryDayofweek.ID) {
            unit = Calendar.DATE;
            max = 7;
        } else if (questionid==ChartFieldEntryDayofmonth.ID) {
            unit = Calendar.DATE;
            max = 31;
        } else if (questionid==ChartFieldEntryHourofday.ID) {
            unit = Calendar.HOUR_OF_DAY;
            max = 23;
        } else if (questionid==ChartFieldEntryWeeksAgo.ID) {
            unit = Calendar.DATE;
            max = 100;
        } else if (questionid==ChartFieldEntryMonthsAgo.ID) {
            unit = Calendar.DATE;
            max = 365;
        } else if (questionid==ChartFieldEntrydatetime.ID) {
            unit = Calendar.DATE;
            max = 60;
        }

        for(int i=0; i<=(howmanydatapoints-1); i++){
            //Get now
            Calendar cal = Calendar.getInstance();
            //Get a random number up to the max defined above
            int random = Util.randomInt(max);
            
            //Subtract that random number of units (units defined above) from the calendar
            cal.add(unit, (-1*random));
            //Convert to a string
            out[i]=Time.dateformatfordb(cal);
        }

        return out;
    }



}
