package com.fbdblog.qtype;



import java.util.Iterator;
import java.util.TreeMap;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import com.fbdblog.dao.*;
import com.fbdblog.qtype.def.Component;
import com.fbdblog.qtype.def.ComponentException;
import com.fbdblog.qtype.util.AppPostParser;
import com.fbdblog.chart.ChartField;
import com.fbdblog.chart.DataType;
import com.fbdblog.chart.DataTypeFactory;

/**
 * User: Joe Reger Jr
 * Date: Jul 6, 2006
 * Time: 1:01:00 PM
 */
public class Range implements Component, ChartField {

    public static int ID = 5;
    public static String NAME = "Range (i.e. 1-10)";
    private Question question;
    private User user;
    private Post post;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public Range(User user, Post post, Question question){
        this.question = question;
        this.user = user;
        this.post = post;
    }

    public String getName() {
        return Range.NAME;
    }

    public int getID(){
        return Range.ID;
    }

    public String getHtmlForInput() {
        StringBuffer out = new StringBuffer();
        out.append("<font class=\"questionfont\">"+question.getQuestion()+"</font>");
        if (question.getIsrequired()){
            out.append(" ");
            out.append("<font class=\"questionfont\" style=\"color: #ff0000;\">*</font>");
        }
        out.append("<br/>");

        //@todo implement range edit display
        if (post!=null && post.getPostanswers()!=null){
            for (Iterator<Postanswer> iterator=post.getPostanswers().iterator(); iterator.hasNext();) {
                Postanswer postanswer=iterator.next();
                if (postanswer.getQuestionid()==question.getQuestionid()){
                    if (postanswer.getName().equals("response")){
                        //Do something
                    }
                }
            }
        }

        String mintitle = "Low";
        double min = 1;
        double step = 1;
        double max = 5;
        String maxtitle = "High";
        for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
            Questionconfig questionconfig = iterator.next();
            if (questionconfig.getName().equals("mintitle")){
                mintitle = questionconfig.getValue();
            } else if (questionconfig.getName().equals("min")){
                min = Double.parseDouble(questionconfig.getValue());
            } else if (questionconfig.getName().equals("step")){
                step = Double.parseDouble(questionconfig.getValue());
            } else if (questionconfig.getName().equals("max")){
                max = Double.parseDouble(questionconfig.getValue());
            } else if (questionconfig.getName().equals("maxtitle")){
                maxtitle = questionconfig.getValue();
            }
        }

        out.append("<table cellpadding=\"3\" cellspacing=\"0\" border=\"0\">");
        out.append("<tr>");
        out.append("<td align=\"center\" valign=\"top\">");
        out.append(mintitle);
        out.append("</td>");
        boolean createdExactlyMaxRadio = false;
        for (double i = min; i<=max; i=i+step) {
            if (i==max){
                createdExactlyMaxRadio = true;
            }
            String checked = "";
            if (isThisOptionSelected(String.valueOf(i))){
                checked = " checked";
            }
            out.append("<td align=\"center\" valign=\"top\">");
            out.append("<input type=\"radio\" name=\""+ AppPostParser.FBDBLOG_REQUEST_PARAM_IDENTIFIER +"questionid_"+question.getQuestionid()+"_\" value=\""+i+"\" "+checked+">");
            out.append("<br>");
            out.append(i);
            out.append("</td>");
        }
        if (!createdExactlyMaxRadio){
            String checked = "";
            if (isThisOptionSelected(String.valueOf(max))){
                checked = " checked";
            }
            out.append("<td align=\"center\" valign=\"top\">");
            out.append("<input type=\"radio\" name=\""+ AppPostParser.FBDBLOG_REQUEST_PARAM_IDENTIFIER +"questionid_"+question.getQuestionid()+"_\" value=\""+max+"\" "+checked+">");
            out.append("<br>");
            out.append(max);
            out.append("</td>");
        }
        out.append("<td align=\"center\" valign=\"top\">");
        out.append(maxtitle);
        out.append("</td>");
        out.append("</tr>");
        out.append("</table>");
        

        return out.toString();
    }

    private boolean isThisOptionSelected(String option){
        if (post!=null && post.getPostanswers()!=null){
            for (Iterator<Postanswer> iterator=post.getPostanswers().iterator(); iterator.hasNext();) {
                Postanswer postanswer=iterator.next();
                if (postanswer.getQuestionid()==question.getQuestionid()){
                    if (postanswer.getName().equals("response")){
                        if (postanswer.getValue().trim().equals(option)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
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
        //Delete any existing postanswers for this questionid
        if (post!=null && post.getPostanswers()!=null){
            for (Iterator<Postanswer> iterator=post.getPostanswers().iterator(); iterator.hasNext();) {
                Postanswer postanswer=iterator.next();
                if (postanswer.getQuestionid()==question.getQuestionid()){
                    try{iterator.remove();}catch(Exception ex){logger.error(ex);}
                }
            }
        }
        //Now save the latest stuff
        String[] requestParams = srp.getParamsForQuestion(question.getQuestionid());
        if (requestParams!=null && requestParams.length>0){
            for (int i = 0; i < requestParams.length; i++) {
                String requestParam = requestParams[i];
                if (requestParam!=null && requestParam.trim().length()>0){
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
