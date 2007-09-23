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
public class ChartFieldEntrydatetime extends Field implements ChartField{


    //Field data - These properties define the data type for this field
    String value = "";
    String timezoneid = "GMT";


    /**
     * Friendly name
     */
    public String getFieldname() {
        return "Entry Date";
    }

    /**
     * Description of this field type
     */
    public String getFielddescription() {
        return "The date of the entry.";
    }


    /**
     * Get the megadatatypeid
     */
    public int getMegadatatypeid() {
        return DataTypeString.DATATYPEID;
    }


    /**
     * Accepts an array of eventid's and returns a set of values for this field
     * corresponding to those eventid's.
     */
    public java.util.TreeMap getChartDataForField(ArrayList<Post> posts) {
       if (posts!=null && posts.size()>0){
            TreeMap data = new TreeMap();
            for (Iterator it = posts.iterator(); it.hasNext(); ) {
                Post post = (Post)it.next();
                Calendar cal = Time.getCalFromDate(post.getPostdate());
                cal = Time.gmttousertime(cal, getTimezoneid());
                data.put(post.getPostid(), Time.dateformatfordb(cal));
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
     * Generally question type implementations extend Field.
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
     * If question type doesn't need to do this, simply return data unchanged.
     */
    public TreeMap fillEmptyXAxis(TreeMap data) {
        return data;
    }





}
