package com.fbdblog.chart;

import java.util.Calendar;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.ArrayList;

import com.fbdblog.util.Time;
import com.fbdblog.dao.Post;
import com.fbdblog.dao.User;

/**
 * A dropdown field
 */
public class ChartFieldEntryDayofweek implements ChartField{


    public static int ID = -5;
    String value = "";
    String timezoneid = "EST";


    public int getID() {
        return ChartFieldEntryDayofweek.ID;
    }

    public String getName() {
        return "Entry Day of Week";
    }

    /**
     * Description of this field type
     */
    public String getDescription() {
        return "The day of the week of the entry.";
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
    public TreeMap getChartData(ArrayList<Post> posts, int questionid, boolean usedisplayoverrideifpossible) {
       if (posts!=null && posts.size()>0){
            TreeMap data = new TreeMap();
            for (Iterator it = posts.iterator(); it.hasNext(); ) {
                Post post = (Post)it.next();
                User user = User.get(post.getUserid());
                Calendar cal = Time.getCalFromDate(Time.gmttousertime(post.getPostdate(), user.getTimezoneid()));
                cal = Time.gmttousertime(cal, getTimezoneid());
                //Get the day of the week
                String dayofweek = "NA";
                dayofweek = cal.get(Calendar.DAY_OF_WEEK) + "-" + Time.getDayOfWeekAbbreviated(cal.get(Calendar.DAY_OF_WEEK));
                data.put(post.getPostid(), dayofweek);
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
        //Days are numbered 1-7 so iterate through these
        for(int i=1; i<=7; i++){
            //The value we want to make sure exists
            String val = i + "-" + Time.getDayOfWeekAbbreviated(i);
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
