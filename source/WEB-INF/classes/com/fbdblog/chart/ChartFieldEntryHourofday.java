package com.fbdblog.chart;

import java.util.*;

import com.fbdblog.util.Time;
import com.fbdblog.dao.Post;

/**
 * A dropdown field
 */
public class ChartFieldEntryHourofday implements ChartField{


    public static int ID = -7;
    String value = "";
    String timezoneid = "EST";


    public int getID() {
        return ChartFieldEntryHourofday.ID;
    }

    public String getName() {
        return "Entry Hour of Day";
    }

    /**
     * Description of this field type
     */
    public String getDescription() {
        return "The hour of day of the entry.";
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
    public TreeMap getChartData(ArrayList<Post> posts, int questionid) {
       if (posts!=null && posts.size()>0){
            TreeMap data = new TreeMap();
            for (Iterator it = posts.iterator(); it.hasNext(); ) {
                Post post = (Post)it.next();
                Calendar cal = Time.getCalFromDate(post.getPostdate());
                cal = Time.gmttousertime(cal, getTimezoneid());
                String dateString = Time.dateformatfordb(cal);
                //Get the hour of day by parsing the dateString
                String tmp = String.valueOf((Integer.parseInt(dateString.substring(11,13))+1));
                data.put(post.getPostid(), tmp);
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
        //Hours of day are 1-24
        for(int i=1; i<=24; i++){
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
