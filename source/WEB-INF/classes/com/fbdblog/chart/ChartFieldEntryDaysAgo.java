package com.fbdblog.chart;


import java.util.Calendar;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.ArrayList;

import com.fbdblog.util.DateDiff;
import com.fbdblog.util.Time;
import com.fbdblog.dao.Post;
import org.apache.log4j.Logger;

/**
 * A dropdown field
 */
public class ChartFieldEntryDaysAgo implements ChartField{


    public static int ID = -6;
    String value = "";
    String timezoneid = "EST";

    int megadatatypeid = DataTypeInteger.DATATYPEID;


    public int getID() {
        return ChartFieldEntryDaysAgo.ID;
    }

    public String getName() {
        return "Days Ago";
    }

    /**
     * Description of this field type
     */
    public String getDescription() {
        return "The number of days ago for this entry.";
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

            //Now in user timezone
            Calendar nowinusertimezone = Time.nowInUserTimezone(getTimezoneid());

           //End of this week is what we base the whole thing on
           Calendar endOfDayInUserTimezone = Time.xDaysAgoEnd(nowinusertimezone, 0);

            for (Iterator it = posts.iterator(); it.hasNext(); ) {
                Post post = (Post)it.next();
                Calendar cal = Time.getCalFromDate(post.getPostdate());
                cal = Time.gmttousertime(cal, getTimezoneid());
                //Get the days ago
                int daysago = DateDiff.dateDiff("day", endOfDayInUserTimezone, cal);
                data.put(post.getPostid(), String.valueOf(daysago));
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
        Logger logger = Logger.getLogger(this.getClass().getName());
        //Util.logTreeMapToDb("data into fillEmptyXAxis", data);
        try {
            //Need to get the max days ago
            int max = Integer.parseInt(data.lastKey().toString());

            //Need to get the min days ago
            int min = Integer.parseInt(data.firstKey().toString());


            //From min to max
            for(int i=min; i<=max; i++){
                //The value we want to make sure exists
                //Careful!!! I must make sure it's of the correct type.
                Integer val = new Integer(i);
                //If it doesn't exist
                if(data.get(val)==null){
                    //Add it to the TreeMap
                    data.put(val, new Integer(0));
                }
            }
        } catch (java.util.NoSuchElementException e){
            //Do nothing
        } catch (Exception e){
            logger.error(e);
        }
        //Util.logTreeMapToDb("data out of fillEmptyXAxis", data);
        //Return the data
        return data;
    }


}
