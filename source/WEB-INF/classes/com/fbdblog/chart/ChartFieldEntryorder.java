package com.fbdblog.chart;

import com.fbdblog.dao.Post;
import com.fbdblog.util.Time;

import java.util.TreeMap;
import java.util.Iterator;
import java.util.Calendar;
import java.util.ArrayList;

/**
 * A dropdown field
 */
public class ChartFieldEntryorder extends Field implements ChartField{


    //Field data - These properties define the data type for this field
    String value = "";
    String timezoneid = "GMT";


    /**
     * Friendly name
     */
    public String getFieldname() {
        return "Individual Entry Order";
    }

    /**
     * Description of this field type
     */
    public String getFielddescription() {
        return "The order of individual entries.";
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
            int count = 0;
            for (Iterator it = posts.iterator(); it.hasNext(); ) {
                Post post = (Post)it.next();
                data.put(post.getPostid(), String.valueOf(count+1));
                count = count + 1;
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
        //Return the data
        return data;
    }





}
