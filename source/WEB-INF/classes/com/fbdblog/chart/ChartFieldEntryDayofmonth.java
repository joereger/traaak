package com.fbdblog.chart;


import java.util.Calendar;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.ArrayList;

import com.fbdblog.util.Time;
import com.fbdblog.dao.Post;

/**
 * A dropdown field
 */
public class ChartFieldEntryDayofmonth extends Field implements ChartField{


    //Field data - These properties define the data type for this field
    String value = "";
    String timezoneid = "GMT";


    /**
     * Friendly name
     */
    public String getFieldname() {
        return "Entry Day of Month";
    }

    /**
     * Description of this field type
     */
    public String getFielddescription() {
        return "The day of the month of the entry.";
    }

    /**
     * Get the megadatatypeid
     */
    public int getMegadatatypeid() {
        return DataTypeInteger.DATATYPEID;
    }



    /**
     * Accepts an array of eventid's and returns a set of values for this field
     * corresponding to those eventid's.
     */
    public TreeMap getChartDataForField(ArrayList<Post> posts) {
        if (posts!=null && posts.size()>0){
            TreeMap data = new TreeMap();
            for (Iterator it = posts.iterator(); it.hasNext(); ) {
                Post post = (Post)it.next();
                Calendar cal = Time.getCalFromDate(post.getPostdate());
                cal = Time.gmttousertime(cal, getTimezoneid());
                //Get the day of the month
                String dayofmonth = "NA";
                dayofmonth = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
                data.put(post.getPostid(), dayofmonth);
            }
            return data;


       } else {
           return new TreeMap();
       }
    }





    /**
     * Set timezoneid of display
     */
    public void setTimezoneid(String timezoneid) {
        this.timezoneid = timezoneid;
    }

    /**
     * Get timezoneid of display
     */
    public String getTimezoneid() {
        return this.timezoneid;
    }

    /**
     * Returns the Field object that this field is based on.
     * The Field object is the core requirement of a fieldtype.
     * Generally FieldType implementations extend Field.
     */
//    public Field getField() {
//        return this;
//    }

    /**
     * Add empty xAxis values to fill out set.
     * In the case of something like DaysOfTheWeek, often
     * the data will only have data on Mon, Fri.  But
     * the final data set should represent explicitly that
     * Sun, Tue, Wed, Thu and Sat have a value of 0.
     * Incoming is a treemap with
     * (xAxis, yAxis)
     * (xAxis, yAxis)
     * (xAxis, yAxis)
     * If FieldType doesn't need to do this, simply return data unchanged.
     */
    public TreeMap fillEmptyXAxis(TreeMap data) {
        //Day of month is 1-31
        for(int i=1; i<=31; i++){
            //The value we want to make sure exists
            //Careful!!! I must make sure it's of the correct type.
            Integer val = new Integer(i);
            //If it doesn't exist
            if(data.get(val)==null){
                //Add it to the TreeMap
                data.put(val, new Integer(0));
            }
        }
        //Return the data
        return data;
    }





}
