package com.fbdblog.chart;

import java.util.*;

import org.apache.log4j.Logger;
import com.fbdblog.util.Num;
import com.fbdblog.util.Time;
import com.fbdblog.util.Str;
import com.fbdblog.util.Util;
import com.fbdblog.dao.Post;
import com.fbdblog.dao.Question;
import com.fbdblog.dao.Chart;
import com.fbdblog.qtype.Textbox;

/**
 * Retrieves, formats and prepares data from the megavalue table for charting operations.
 */
public class MegaChartSeries {

    //public ArrayList<Post> posts;
    public MegaChartEntryChooser entryChooser;
    public String[][] rawChartData;
    public String[][] cleanData;
    public TreeMap avgCount = new TreeMap();
    //public String dateSql = "";
    //public reger.UserSession userSession;

    public int appid=-1;

    //xAxis
    public int xQuestionid =-1;
    public String xAxisTitle="";
    public int xFieldtype=-1;
    public int xMegadatatype=-1;

    //yAxis
    public int yQuestionid =-1;
    public String yAxisTitle="";
    public int yFieldtype=-1;
    public int yMegadatatype=-1;
    public int yaxiswhattodo = -1;





    /**
     * Constructor
     * Go grab data from the database and put it into the built-in raw data property.
     */
    public MegaChartSeries(){

    }



    public MegaChartSeries(int yQuestionid, int appid, MegaChartEntryChooser entryChooser, Chart chart){
        Logger logger = Logger.getLogger(this.getClass().getName());
        //Store the incoming data in the correct place
        this.xQuestionid =chart.getXquestionid();
        this.yQuestionid =yQuestionid;
        this.appid=appid;
        this.yaxiswhattodo=chart.getYaxiswhattodo();

        //Break out the fieldtype from the questionid.
        //If the MegaFieldid < 0 then it's actually a fieldtype
        //xField
        if (xQuestionid < 0){
            //Less than zero are interpolated types
            xFieldtype = xQuestionid;
            ChartField f = ChartFieldFactory.getHandlerByFieldtype(xFieldtype);
            xAxisTitle = f.getName();
            xMegadatatype = f.getMegadatatypeid();
        } else {
            if (xQuestionid>1000000){
                //Over a million use displayoverride, but here we're just getting field names and can use the question
                Question question = Question.get(xQuestionid-1000000);
                xFieldtype = question.getComponenttype();
                xAxisTitle = Str.truncateString(question.getQuestion(), 25);
                xMegadatatype = question.getDatatypeid();
            } else {
                //Under a million are plain old fields
                Question question = Question.get(xQuestionid);
                xFieldtype = question.getComponenttype();
                xAxisTitle = Str.truncateString(question.getQuestion(), 25);
                xMegadatatype = question.getDatatypeid();
            }
        }
        logger.debug("in costructor() point A xMegadatatype="+xMegadatatype);

        //yField
        if (yQuestionid < 0){
            //Less than zero are interpolated types
            yFieldtype = yQuestionid;
            ChartField f = ChartFieldFactory.getHandlerByFieldtype(yFieldtype);
            if(f!=null){
                yAxisTitle = f.getName();
                yMegadatatype = f.getID();
            }
        } else {
            if (yQuestionid>1000000){
                //Over a million use displayoverride, but here we're just getting field names and can use the question
                Question question = Question.get(yQuestionid-1000000);
                yFieldtype = question.getComponenttype();
                yAxisTitle = Str.truncateString(question.getQuestion(),25);
                yMegadatatype = question.getDatatypeid();
            } else {
                //Under a million are plain old fields
                Question question = Question.get(yQuestionid);
                yFieldtype = question.getComponenttype();
                yAxisTitle = Str.truncateString(question.getQuestion(),25);
                yMegadatatype = question.getDatatypeid();
            }
        }

        //Get the data for x Axis
        TreeMap xAxisData = getFieldData(xQuestionid, xFieldtype, entryChooser.getPosts());
        logger.debug("xAxisData.size()="+xAxisData.size());

        //Get the data for y Axis
        TreeMap yAxisData = getFieldData(yQuestionid, yFieldtype, entryChooser.getPosts());
        logger.debug("yAxisData.size()="+yAxisData.size());

        //Now, combine xAxisData and yAxisData into String[][]
        this.rawChartData = combineTwoTreeMapsIntoDataArray(xAxisData, yAxisData, entryChooser.getPosts());
        logger.debug("this.rawChartData.length="+this.rawChartData.length);

        //Util.logDoubleStringArrayToDb("Combined Data", this.rawChartData);

        //Sort, order, cleanup and otherwise massage the data
        massageData();
        logger.debug("this.cleanData.length="+this.cleanData.length);

    }

    /**
     * Get data for a particular questionid.
     * What's happening here is this:
     * We're looking at a single field and we want to create an
     * array with array[eventid][questionid.value].
     * The first step is to find the correct field handler
     */
     public TreeMap getFieldData(int questionid, int fieldtype, ArrayList<Post> posts){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("getFieldData() - questionid=" + questionid + " fieldtype=" + fieldtype);
        //Adjust for displayoverride... those over 1000000 use it
        boolean usedisplayoverrideifpossible = false;
        if (questionid>1000000){
            usedisplayoverrideifpossible = true;
            questionid=questionid-1000000;
        }
        //Now we pass this to the fieldtype handler
        //Figure out which type of field this is
        ChartField f = ChartFieldFactory.getHandlerByFieldtype(fieldtype);
        //Populate the field with title, description and all that good stuff in the database
        //f.populateFromQuestionid(questionid);
        //Call the function that gets a set of chart data
        //Result[eventid][value]
        TreeMap axisRawData = f.getChartData(posts, questionid, usedisplayoverrideifpossible);
        if(axisRawData!=null){
            logger.debug("axisRawData.size()="+axisRawData.size());
        } else {
            logger.debug("axisRawData is null");   
        }
        //Util.logTreeMapToDb("axisRawData questionid=" + questionid + " fieldtype=" + fieldtype, axisRawData);
        return axisRawData;
     }

     public String[][] combineTwoTreeMapsIntoDataArray(TreeMap xAxis, TreeMap yAxis, ArrayList<Post> posts){
        Logger logger = Logger.getLogger(this.getClass().getName());
        //The final format is an array of [eventid][xAxisData][yAxisData]
        String[][] outData = new String[0][3];
        if (posts!=null){
            outData = new String[posts.size()][3];
        }
        //Iterate the list of eventids
        Object xData;
        Object yData;

        int count = 0;
        for (Iterator it = posts.iterator(); it.hasNext(); ) {
            Post post = (Post)it.next();

            //Get xData
            xData = xAxis.get(post.getPostid());

            //Get yData
            yData = yAxis.get(post.getPostid());

            logger.debug("postid="+post.getPostid()+" xData="+xData+" yData="+yData);

            //Only add if both x and y have something to contribute
            if (xData!=null && !xData.equals("null") && !xData.equals("")){
                if (yData!=null && !yData.equals("null") && !yData.equals("")){
                    logger.debug("adding to outData["+count+"]: post.getPostid()="+String.valueOf(post.getPostid())+" xData.toString()="+xData.toString()+" yData.toString()="+yData.toString());
                    //Create triple array
                    String[] tmpArr = new String[3];
                    tmpArr[0] = String.valueOf(post.getPostid());
                    tmpArr[1] = xData.toString();
                    tmpArr[2] = yData.toString();
                    //Add this tmpArray to the main data array
                    outData[count] = tmpArr;
                    count = count + 1;
                }
            }
        }
        //Return the data
        return outData;
     }

    /**
     * Clean up the data and get it ready to be thrown at a chart object.
     */
    public void massageData(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        //Sort
        //Create TreeMap
        TreeMap tmap = new TreeMap();

        //Create a working array
        String [][] tmpChartData = rawChartData;


        //Remove Dupes AND Add to TreeMap in correct object type (double or String)
        //Iterate rawChartData
        Object tmpobj;
        //Create treemaps to detect dupes
        TreeMap xDupes = new TreeMap();
        TreeMap yDupes = new TreeMap();
        boolean xdupeFlag=false;
        boolean ydupeFlag=false;
        if (tmpChartData!=null && tmpChartData.length>0){
        	for(int i=0; i<tmpChartData.length; i++){
                //logger.debug("tmpChartData["+i+"][0]="+tmpChartData[i][0]+" tmpChartData.length="+tmpChartData.length);
        	    if (tmpChartData[i][0]==null){
                    //logger.debug("it's really null... tmpChartData[i][0]==null, tmpChartData[i][1]="+tmpChartData[i][1]+", tmpChartData[i][2]="+tmpChartData[i][2]);
                }
                if (tmpChartData[i][0]!=null){
                    logger.debug("tmpChartData[i][0]="+tmpChartData[i][0]+", tmpChartData[i][1]="+tmpChartData[i][1]+", tmpChartData[i][2]="+tmpChartData[i][2]);
                    //Create a properly typed eventid
                    Object eventid = new Integer(tmpChartData[i][0]);

                    //Create a properly typed xAxisValue
                    Object xAxisValue = null;
                    logger.debug("in massageData() point B xMegadatatype="+xMegadatatype);
                    logger.debug("in massageData() point B xFieldtype="+xFieldtype);
                    if (xMegadatatype==DataTypeInteger.DATATYPEID){
                        if (Num.isinteger(tmpChartData[i][1])){
                            logger.debug("xMegadatatype==DataTypeInteger.DATATYPEID for tmpChartData[i][1]:"+tmpChartData[i][1]);
                            xAxisValue = new Integer(tmpChartData[i][1]);
                        } else {
                            logger.debug("setting to zero because not Integer for tmpChartData[i][1]:"+tmpChartData[i][1]);
                            xAxisValue = new Integer(0);
                        }
                    } else if (xMegadatatype==DataTypeDecimal.DATATYPEID) {
                        if (Num.isdouble(tmpChartData[i][1])){
                            logger.debug("xMegadatatype==DataTypeDecimal.DATATYPEID for tmpChartData[i][1]:"+tmpChartData[i][1]);
                            xAxisValue = new Double(tmpChartData[i][1]);
                        } else {
                            logger.debug("setting to zero because not Double for tmpChartData[i][1]:"+tmpChartData[i][1]);
                            xAxisValue = new Double(0);
                        }
                    } else {
                        logger.debug("setting to String because not numeric:"+tmpChartData[i][1]);
                        xAxisValue = tmpChartData[i][1];
                    }
                    //Override when using displayoverride because strings are allowed
                    if (xQuestionid>1000000){
                        xAxisValue = tmpChartData[i][1];
                    }

                    //Create a properly typed yAxisValue
                    Object yAxisValue = null;
                    if (Num.isdouble(tmpChartData[i][2])){
                        yAxisValue = new Double(tmpChartData[i][2]);
                    } else {
                        yAxisValue = new Double(0);
                    }

                    //Reset the dupe finder flag
                    xdupeFlag=false;
                    ydupeFlag=false;

                    //I create two treemaps, each keyed off of the eventid (rawChartData[i][0]).
                    //I then check for x dupes and y dupes.  You have to have both duped to find an actual row dupe.
                    //Detect x dupes
                    if (xDupes.containsKey(eventid) && xDupes.get(eventid).equals(xAxisValue)) {
                        xdupeFlag=true;
                    } else {
                        //Add it to the dupe cache
                        xDupes.put(eventid, xAxisValue);
                    }
                    //Detect y dupes
                    if (yDupes.containsKey(eventid) && yDupes.get(eventid).equals(yAxisValue)) {
                        ydupeFlag=true;
                    } else {
                        //Add it to the dupe cache
                        yDupes.put(eventid, yAxisValue);
                    }

                    //If no dupes have yet been found for this x,y combination
                    //In other words, each x,y combo will pass through here at least once
                    if (!xdupeFlag && !ydupeFlag) {
                        //If key already exists, sum/avg the values...
                        if (tmap.containsKey(xAxisValue)) {

                            //Get the current value
                            tmpobj=tmap.get(xAxisValue);
                            //Clear the current treemap entry
                            tmap.remove(xAxisValue);

                            //Add the entry back with the new avg/sum value

                            //This handles Sum and Average cases because it's doing the Sum
                            //portion.  Later, after all have been processed, the divide
                            //part of the average operation is done.
                            //Note that we're adding to the TreeMap as a typed object so
                            //that sorting works properly.
                            tmap.put(xAxisValue, new Double(Double.parseDouble(yAxisValue.toString()) + Double.parseDouble(tmpobj.toString())));
                            //Be sure to update the avgCount treemap
                            incrementAvgCount(xAxisValue);

                        //The key doesn't yet exist so we have to add it... simple.
                        } else {
                            //Add the new entry to the treemap
                            tmap.put(xAxisValue, yAxisValue);

                            //Be sure to update the avgCount treemap
                            incrementAvgCount(xAxisValue);
                        }
                    }
                }
        	}
        }


        //Now, fill in some empty xAxis values
        ChartField f = ChartFieldFactory.getHandlerByFieldtype(xFieldtype);
        tmap = f.fillEmptyXAxis(tmap);


        //If the user wants to average, do it. Jeeze.  What are you standing around for?
        //Can only do at this level because only now do we see both x and y axes.
        if (yaxiswhattodo==MegaConstants.YAXISWHATTODOAVG) {
            //@todo Why does doAverage() have any effect when dealing with XAXISENTRYORDER?  Shouldn't we have one value for each entry and that's it?  I need to print out and compare rawData with cleanData.
            tmap = doAverage(tmap);
        }


        //Dump from treemap to cleanData
        //Util.logTreeMapToDb("TreeMap just before being converted to cleanData", tmap);
        cleanData=null;
        cleanData= new String[tmap.size()][3];
        int j=0;
        for (Iterator i=tmap.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry e = (Map.Entry) i.next();
            //Move from treemap to cleandata
            if (xQuestionid !=ChartFieldEntrydatetime.ID){
                //As long as it's not a date this will be used
                cleanData[j][1] = String.valueOf(e.getKey());
            } else {
                //Do the milli conversion
                Calendar tmp = Time.dbstringtocalendar(String.valueOf(e.getKey()));
                cleanData[j][1]=String.valueOf(tmp.getTimeInMillis());
            }

            cleanData[j][2] = String.valueOf(e.getValue());


            //Increment the counter
            j=j+1;
        }
        //Util.logDoubleStringArrayToDb("Final cleanData from MegaChartNew", cleanData);

    }



    /**
     * This method adds 1 to the key value in the avgCount treemap.  The goal here
     * is to be able to tell how many entries have been consolidated in cleanData
     * so that an average can be computed.
     * @param key - The key to add a count to
     */
    public void incrementAvgCount(Object key){
        String tmpobj="";
        if (avgCount.containsKey(key)) {
            //Get the current value
            tmpobj=String.valueOf(avgCount.get(key));
            //Clear the current treemap entry
            avgCount.remove(key);
            //Increment and add
            avgCount.put(key, String.valueOf(Integer.parseInt(tmpobj)+1));
        } else {
            //Just add a new key
            avgCount.put(key, String.valueOf(1));
        }
    }

    /**
     * Compute the average for y axis.  Here's where we're at: the TreeMap holds summed values.
     * This.avgCount holds the key-by-key count of how many entries are in each.
     * So all we have to do is the division.
     */
    public TreeMap doAverage(TreeMap tmap){
        TreeMap newTmap = new TreeMap();
        String oldValue="";
        String newValue="";
        //Iterate the incoming treemap
        for (Iterator i=tmap.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry e = (Map.Entry) i.next();
            //out.println(e.getKey() + ": " + e.getValue());
            if (avgCount.containsKey(e.getKey())) {
                //Store the temp tmap value
                oldValue=String.valueOf(e.getValue());
                //Do the math
                if (Num.isdouble(oldValue)){
                    //Calculate the average
                    newValue=String.valueOf(Double.parseDouble(oldValue)/Double.parseDouble(String.valueOf(avgCount.get(e.getKey()))));
                } else {
                    newValue=oldValue;
                }
                //Dump the new value into the newTmap
                newTmap.put(e.getKey(), newValue);
            }
        }
        return newTmap;
    }


    /**
     * Sets the x title and some other properties
     */
    public void setXTitleBasedOnFieldId(){
        //Get details of the x axis
        if (xQuestionid >0) {
            Question question = Question.get(xQuestionid);
            xAxisTitle= Str.truncateString(question.getQuestion(), 25);
            xMegadatatype=question.getDatatypeid();
            xFieldtype=question.getComponenttype();
        } else {
            //It's a derived type
            //See Vars class for these values.
            if (xQuestionid ==ChartFieldEntryorder.ID){
                xAxisTitle="Order";
                xMegadatatype=DataTypeInteger.DATATYPEID;
                xFieldtype=ChartFieldEntryorder.ID;
            } else if (xQuestionid ==ChartFieldEntryHourofday.ID) {
                xAxisTitle="Hour of the Day";
                xMegadatatype=DataTypeInteger.DATATYPEID;
                xFieldtype=ChartFieldEntryHourofday.ID;
            } else if (xQuestionid ==ChartFieldEntryDayofweek.ID) {
                xAxisTitle="Day of the Week";
                xMegadatatype=DataTypeString.DATATYPEID;
                xFieldtype=ChartFieldEntryDayofweek.ID;
            } else if (xQuestionid ==ChartFieldEntryDayofmonth.ID) {
                xAxisTitle="Day of the Month";
                xMegadatatype=DataTypeInteger.DATATYPEID;
                xFieldtype=ChartFieldEntryDayofmonth.ID;
            } else if (xQuestionid ==ChartFieldEntryDaysAgo.ID) {
                xAxisTitle="Days Ago";
                xMegadatatype=DataTypeInteger.DATATYPEID;
                xFieldtype=ChartFieldEntryDaysAgo.ID;
            } else if (xQuestionid ==ChartFieldEntryWeeksAgo.ID) {
                xAxisTitle="Weeks Ago";
                xMegadatatype=DataTypeInteger.DATATYPEID;
                xFieldtype=ChartFieldEntryWeeksAgo.ID;
            } else if (xQuestionid ==ChartFieldEntryMonthsAgo.ID) {
                xAxisTitle="Months Ago";
                xMegadatatype=DataTypeInteger.DATATYPEID;
                xFieldtype=ChartFieldEntryMonthsAgo.ID;
            } else {
                xAxisTitle="Date";
                xMegadatatype=DataTypeDatetime.DATATYPEID;
                xFieldtype=ChartFieldEntrydatetime.ID;
            }
        }
    }



    /**
     * Set y axis properties based on
     *
     */
     public void setYTitleBasedOnFieldId(){
        Question question = Question.get(yQuestionid);
        yAxisTitle= Str.truncateString(question.getQuestion(), 25);
        yMegadatatype=question.getDatatypeid();
        yFieldtype=question.getComponenttype();
        if (yQuestionid ==ChartFieldEntrycount.ID) {
            yAxisTitle="Number of Posts";
            yMegadatatype=DataTypeInteger.DATATYPEID;
            yFieldtype= Textbox.ID;
        }
    }

    public String getyAxisTitle() {
        return yAxisTitle;
    }

    public String getxAxisTitle() {
        return xAxisTitle;
    }

    public int getxMegadatatype() {
        return xMegadatatype;
    }

    public int getyMegadatatype() {
        return yMegadatatype;
    }
}
