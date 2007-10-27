package com.fbdblog.chart;

import com.fbdblog.dao.Post;

import java.util.TreeMap;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * A dropdown field
 */
public class ChartFieldEntryorder implements ChartField{


    public static int ID = -9;
    String value = "";
    String timezoneid = "EST";


    public int getID() {
        return ChartFieldEntryorder.ID;
    }

    public String getName() {
        return "Individual Entry Order";
    }

    /**
     * Description of this field type
     */
    public String getDescription() {
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
    public TreeMap getChartData(ArrayList<Post> posts, int questionid, boolean usedisplayoverrideifpossible) {
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
        //Return the data
        return data;
    }





}
