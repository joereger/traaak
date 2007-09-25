package com.fbdblog.qtype;

import com.fbdblog.qtype.def.Component;
import com.fbdblog.qtype.def.ComponentException;
import com.fbdblog.qtype.util.AppPostParser;
import com.fbdblog.chart.ChartField;
import com.fbdblog.chart.DataType;
import com.fbdblog.chart.DataTypeFactory;
import com.fbdblog.dao.Question;
import com.fbdblog.dao.User;
import com.fbdblog.dao.Post;
import com.fbdblog.dao.Postanswer;
import com.fbdblog.util.Num;
import org.apache.log4j.Logger;

import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jul 6, 2006
 * Time: 1:01:00 PM
 */
public class Timeperiod implements Component, ChartField {

    public static int ID = 7;
    public static String NAME = "Time Period";
    private Question question;
    private User user;
    private Post post;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public Timeperiod(User user, Post post, Question question){
        this.question = question;
        this.user = user;
        this.post = post;
    }

    public String getName() {
        return NAME;
    }

    public int getID(){
        return ID;
    }

    public String getHtmlForInput() {
        StringBuffer out = new StringBuffer();
        out.append("<font class=\"questionfont\">"+question.getQuestion()+"</font>");
        if (question.getIsrequired()){
            out.append(" ");
            out.append("<font class=\"questionfont\" style=\"color: #ff0000;\">*</font>");
        }
        out.append("<br/>");

        String value = "";
        if (post!=null && post.getPostanswers()!=null){
            for (Iterator<Postanswer> iterator=post.getPostanswers().iterator(); iterator.hasNext();) {
                Postanswer postanswer=iterator.next();
                if (postanswer.getQuestionid()==question.getQuestionid()){
                    if (postanswer.getName().equals("response")){
                        value=postanswer.getValue();
                        break;
                    }
                }
            }
        }

        //@todo convert seconds to h:m:s... I think I have some utility code to do this already

        out.append("<table cellpadding='0' cellspacing='0' border='0'>");
        out.append("<tr>");
        out.append("<td valign='top'>");
            out.append("<input type=\"text\" size=\"2\" maxlength=\"5\" name=\""+ AppPostParser.FBDBLOG_REQUEST_PARAM_IDENTIFIER +"questionid_"+question.getQuestionid()+"-hours\"> : ");
        out.append("</td>");
        out.append("<td valign='top'>");
            out.append("<input type=\"text\" size=\"2\" maxlength=\"5\" name=\""+ AppPostParser.FBDBLOG_REQUEST_PARAM_IDENTIFIER +"questionid_"+question.getQuestionid()+"-minutes\"> : ");
        out.append("</td>");
        out.append("<td valign='top'>");
            out.append("<input type=\"text\" size=\"2\" maxlength=\"5\" name=\""+ AppPostParser.FBDBLOG_REQUEST_PARAM_IDENTIFIER +"questionid_"+question.getQuestionid()+"-seconds\">");
        out.append("</td>");
        out.append("</tr>");
        out.append("<tr>");
        out.append("<td valign='top'>");
            out.append("<font size=-2>hrs</font>");
        out.append("</td>");
        out.append("<td valign='top'>");
            out.append("<font size=-2>min</font>");
        out.append("</td>");
        out.append("<td valign='top'>");
            out.append("<font size=-2>sec</font>");
        out.append("</td>");
        out.append("</tr>");
        out.append("</table>");
        return out.toString();
    }



    public void validateAnswer(AppPostParser srp) throws ComponentException {
        ComponentException allCex = new ComponentException();
        //Requiredness validation
        if (question.getIsrequired()){
            String[] requestParams = srp.getParamsForQuestion(question.getQuestionid());
            if (requestParams==null || requestParams.length<1 || requestParams[0]==null || requestParams[0].trim().equals("")){
                allCex.addErrorsFromAnotherGeneralException(new ComponentException("'"+question.getQuestion()+"' is required."), "");
            }
        }
        //Datatype validation
        DataType dt = DataTypeFactory.get(question.getDatatypeid());
        try{
            String[] requestParams = srp.getParamsForQuestion(question.getQuestionid());
            if (requestParams!=null && requestParams.length>0){
                for (int i = 0; i < requestParams.length; i++) {
                    String requestParam = requestParams[i];
                    if (requestParam!=null && requestParam.trim().length()>0){
                        dt.validataData(requestParam);
                    }
                }
            }
        } catch (ComponentException cex){
            allCex.addErrorsFromAnotherGeneralException(cex, "'"+question.getQuestion()+"' ");
        } catch (Exception ex){
            ex.printStackTrace();
            logger.error(ex);
            allCex.addValidationError(ex.getMessage());
        }
        //Throw if necessary
        if(allCex.getErrors().length>0){
            throw allCex;
        }
    }

    public void processAnswer(AppPostParser srp, Post post) throws ComponentException {
        String[] requestParamsHours = srp.getParamsWithCertainStringForQuestion(question.getQuestionid(), "-hours");
        String[] requestParamsMinutes = srp.getParamsWithCertainStringForQuestion(question.getQuestionid(), "-minutes");
        String[] requestParamsSeconds = srp.getParamsWithCertainStringForQuestion(question.getQuestionid(), "-seconds");

        int hours = 0;
        if (requestParamsHours!=null && Num.isinteger(requestParamsHours[0])){
            hours = Integer.parseInt(requestParamsHours[0]);
        }
        int minutes = 0;
        if (requestParamsMinutes!=null && Num.isinteger(requestParamsMinutes[0])){
            minutes = Integer.parseInt(requestParamsMinutes[0]);
        }
        int seconds = 0;
        if (requestParamsSeconds!=null && Num.isinteger(requestParamsSeconds[0])){
            seconds = Integer.parseInt(requestParamsSeconds[0]);   
        }
        int totalseconds = (hours*3600)+(minutes*60)+seconds;

        if (totalseconds>0){
            Postanswer postanswer = new Postanswer();
            postanswer.setQuestionid(question.getQuestionid());
            postanswer.setUserid(user.getUserid());
            postanswer.setName("response");
            postanswer.setValue(String.valueOf(totalseconds));
            postanswer.setPostid(post.getPostid());
            try{postanswer.save();}catch(Exception ex){logger.error(ex);}
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
