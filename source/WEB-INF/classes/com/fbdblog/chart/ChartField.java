package com.fbdblog.chart;

import com.fbdblog.dao.Post;

import java.util.TreeMap;
import java.util.ArrayList;

/**
 * This is the interface that is used in the MegaChart infrastructure to define field types.
 */
public interface ChartField {


    public String getName();
    public int getID();

    /**
     * Description of this Field.
     * For example: Running distance is the .
     * This is dynamic from the database and generally uses this.fielddescription from the extended Field class.
     */
    String getDescription();

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
     TreeMap fillEmptyXAxis(TreeMap data);

     /**
     * Accepts an array of eventid's and returns a set of values for this field
     * corresponding to those eventid's.
     * Result[eventid][value]
     */
     TreeMap getChartData(ArrayList<Post> posts);

     /**
     * Set timezoneid of display.   If this fieldtype doesn't use timezoneid then the body of this method can be empty.
     */
    void setTimezoneid(String timezoneid);

    /**
     * Get timezoneid of display. If this fieldtype doesn't use timezoneid then this can return "".
     */
    String getTimezoneid();



}
