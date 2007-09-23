package com.fbdblog.qtype;



import org.apache.log4j.Logger;
import com.fbdblog.dao.User;
import com.fbdblog.dao.Post;
import com.fbdblog.dao.Question;
import com.fbdblog.dao.Postanswer;
import com.fbdblog.qtype.def.Component;
import com.fbdblog.qtype.def.ComponentException;
import com.fbdblog.qtype.util.AppPostParser;
import com.fbdblog.chart.ChartField;

import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jul 6, 2006
 * Time: 1:01:00 PM
 */
public class Essay implements Component, ChartField {

    public static int ID = 2;
    public static String NAME = "Essay (Long Text)";
    private Question question;
    private User user;
    private Post post;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public Essay(User user, Post post, Question question){
        this.question = question;
        this.user = user;
        this.post = post;
    }

    public String getName() {
        return Essay.NAME;
    }

    public int getID(){
        return Essay.ID;
    }

    public String getHtmlForInput() {
        StringBuffer out = new StringBuffer();
        out.append("<font class=\"formfieldnamefont\">"+question.getQuestion()+"</font>");
        if (question.getIsrequired()){
            out.append(" ");
            out.append("<font class=\"formfieldnamefont\" style=\"color: #ff0000;\">(Required)</font>");
        }
        out.append("<br/>");

        out.append("<textarea cols=\"30\" rows=\"3\" name=\""+ AppPostParser.FBDBLOG_REQUEST_PARAM_IDENTIFIER +"questionid_"+question.getQuestionid()+"\"></textarea>");

        return out.toString();
    }



    public void validateAnswer(AppPostParser srp) throws ComponentException {
        if (question.getIsrequired()){
            String[] requestParams = srp.getParamsForQuestion(question.getQuestionid());
            if (requestParams==null || requestParams.length<1){
                throw new ComponentException(question.getQuestion()+" is required.");
            }
            if (requestParams[0]==null || requestParams[0].equals("")){
                throw new ComponentException(question.getQuestion()+" is required.");
            }
        }
    }

    public void processAnswer(AppPostParser srp, Post post) throws ComponentException {
        String[] requestParams = srp.getParamsForQuestion(question.getQuestionid());
        if (requestParams!=null && requestParams.length>0){
            for (int i = 0; i < requestParams.length; i++) {
                String requestParam = requestParams[i];
                Postanswer postanswer = new Postanswer();
                postanswer.setQuestionid(question.getQuestionid());
                postanswer.setUserid(user.getUserid());
                postanswer.setName("response");
                postanswer.setValue(requestParam.trim());
                postanswer.setPostid(post.getPostid());
                try{postanswer.save();}catch(Exception ex){logger.error(ex);}
            }
        }
    }

    /**
     * Description of this Field.
     * For example: Running distance is the .
     * This is dynamic from the database and generally uses this.fielddescription from the extended Field class.
     */
    public String getDescription() {
        return NAME;
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
        return data;
    }

    /**
     * Accepts an array of eventid's and returns a set of values for this field
     * corresponding to those eventid's.
     * Result[eventid][value]
     */
    public TreeMap getChartData(ArrayList<Post> posts) {
        if (posts!=null && posts.size()>0){
            TreeMap data = new TreeMap();
            for (Iterator it = posts.iterator(); it.hasNext(); ) {
                Post post = (Post)it.next();
                for (Iterator<Postanswer> iterator = post.getPostanswers().iterator(); iterator.hasNext();) {
                    Postanswer postanswer =  (Postanswer)iterator.next();
                    if(postanswer.getName().equals("response")){
                        data.put(post.getPostid(), postanswer.getValue());
                    }
                }
            }
            return data;
       } else {
           return new TreeMap();
       }
    }

    /**
     * Set timezoneid of display.   If this fieldtype doesn't use timezoneid then the body of this method can be empty.
    */
    public void setTimezoneid(String timezoneid) {

    }

    /**
     * Get timezoneid of display. If this fieldtype doesn't use timezoneid then this can return "".
     */
    public String getTimezoneid() {
        return "EST";
    }




}
