package com.fbdblog.qtype;



import java.util.*;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.fbdblog.dao.*;
import com.fbdblog.dao.hibernate.HibernateUtil;
import com.fbdblog.qtype.def.Component;
import com.fbdblog.qtype.def.ComponentException;
import com.fbdblog.qtype.util.AppPostParser;
import com.fbdblog.util.Str;
import com.fbdblog.chart.ChartField;
import com.fbdblog.chart.DataType;
import com.fbdblog.chart.DataTypeFactory;
import com.fbdblog.chart.DataTypeString;

/**
 * User: Joe Reger Jr
 * Date: Jul 6, 2006
 * Time: 1:01:00 PM
 */
public class Checkboxes implements Component, ChartField {

    public static int ID = 4;
    public static String NAME = "Checkboxes (Choose Multiple)";
    private Question question;
    private User user;
    private Post post;
    
    Logger logger = Logger.getLogger(this.getClass().getName());

    public Checkboxes(){
        
    }

    public Checkboxes(User user, Post post, Question question){
        this.question = question;
        this.user = user;
        this.post = post;
    }

    public String getName() {
        return Checkboxes.NAME;
    }

    public int getID(){
        return Checkboxes.ID;
    }


    public int getMegadatatypeid() {
        if (question!=null){
            return question.getDatatypeid();
        }
        return DataTypeString.DATATYPEID;
    }

    public String getHtmlForInput() {
        StringBuffer out = new StringBuffer();
        out.append("<font class=\"questionfont\">"+question.getQuestion()+"</font>");
        if (question.getIsrequired()){
            out.append(" ");
            out.append("<font class=\"questionfont\" style=\"color: #ff0000;\">*</font>");
        }
        out.append("<br/>");

        //Main options
        String options = "";
        for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
            Questionconfig questionconfig = iterator.next();
            if (questionconfig.getName().equals("options")){
                options = questionconfig.getValue();
            }
        }
        String[] optionsSplit = options.split("\\n");
        //@todo Test checkbox because i don't think that the hashmap holding the values properly handles multiple values for the same name
        for (int i = 0; i < optionsSplit.length; i++) {
            String s = optionsSplit[i];
            String selected = "";
            if (isThisOptionSelected(s)){
                selected = " checked='true'";
            }
            out.append("<input type=\"checkbox\" name=\""+ AppPostParser.FBDBLOG_REQUEST_PARAM_IDENTIFIER +"questionid_"+question.getQuestionid()+"_\" value=\""+Str.cleanForHtml(s.trim())+"\" "+selected+">" + s.trim());
            out.append("<br/>");
        }
        //User options
        List<Questionuserconfig> questionuserconfigs = HibernateUtil.getSession().createCriteria(Questionuserconfig.class)
                                           .add(Restrictions.eq("questionid", question.getQuestionid()))
                                           .add(Restrictions.eq("userid", user.getUserid()))
                                           .setCacheable(true)
                                           .list();
        String useroptions = "";
        for (Iterator<Questionuserconfig> iterator = questionuserconfigs.iterator(); iterator.hasNext();) {
            Questionuserconfig questionuserconfig = iterator.next();
            if (questionuserconfig.getName().equals("options")){
                useroptions = questionuserconfig.getValue();
            }
        }
        String[] userOptionsSplit = useroptions.split("\\n");
        for (int i = 0; i < userOptionsSplit.length; i++) {
            String s = userOptionsSplit[i];
            if (s.trim().length()>0){
                String selected = "";
                if (isThisOptionSelected(s)){
                    selected = " checked='true'";
                }
                out.append("<input type=\"checkbox\" name=\""+ AppPostParser.FBDBLOG_REQUEST_PARAM_IDENTIFIER +"questionid_"+question.getQuestionid()+"_\" value=\""+Str.cleanForHtml(s.trim())+"\" "+selected+">" + s.trim());
                out.append("<br/>");
            }
        }

        //User inputs own option
        out.append("<font size=-2>Or, enter your own:</font>");
        out.append("<br/>");
        out.append("<input type='text' name=\""+AppPostParser.FBDBLOG_REQUEST_PARAM_IDENTIFIER +"questionid_"+question.getQuestionid()+"_-newoption\" value=\""+""+"\" size=\"20\" maxlength=\"255\" style=\"font-size: 7px;\">");


        return out.toString();
    }

    public String getValue() {
        return getValueForDisplay();
    }

    public String getValueForDisplay() {
        StringBuffer out = new StringBuffer();
        if (post!=null && post.getPostanswers()!=null){
            for (Iterator<Postanswer> iterator=post.getPostanswers().iterator(); iterator.hasNext();) {
                Postanswer postanswer=iterator.next();
                if (postanswer.getQuestionid()==question.getQuestionid()){
                    if (postanswer.getName().equals("response")){
                        if (out.length()>0){
                            out.append(", ");   
                        }
                        out.append(postanswer.getValue());
                    }
                }
            }
        }
        return out.toString();
    }

    private boolean isThisOptionSelected(String option){
        if (post!=null && post.getPostanswers()!=null){
            for (Iterator<Postanswer> iterator=post.getPostanswers().iterator(); iterator.hasNext();) {
                Postanswer postanswer=iterator.next();
                if (postanswer.getQuestionid()==question.getQuestionid()){
                    if (postanswer.getName().equals("response")){
                        if (postanswer.getValue().trim().equals(option.trim())){
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
            logger.error("",ex);
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
                    try{iterator.remove();}catch(Exception ex){logger.error("",ex);}
                }
            }
        }
        //Now save the latest stuff
        logger.debug("start processanswer");
        //Save the answers
        String[] requestParams = srp.getParamsForQuestion(question.getQuestionid());
        if (requestParams!=null && requestParams.length>0){
            for (int i = 0; i < requestParams.length; i++) {
                String requestParam = requestParams[i];
                logger.debug("handling requestParam["+i+"]="+requestParam+" as an answer");
                //Create a new Postanswer
                logger.debug("creating new Postanswer()");
                if (requestParam!=null && requestParam.trim().length()>0){
                    Postanswer postanswer = new Postanswer();
                    postanswer.setQuestionid(question.getQuestionid());
                    postanswer.setUserid(user.getUserid());
                    postanswer.setName("response");
                    postanswer.setValue(requestParam.trim());
                    postanswer.setPostid(post.getPostid());
                    try{postanswer.save();}catch(Exception ex){logger.error("",ex);}
                }
            }
        }
        //Handle new user options
        String[] newOptionRequestParams = srp.getParamsWithCertainStringForQuestion(question.getQuestionid(), "-newoption");
        if (newOptionRequestParams!=null && newOptionRequestParams.length>0){
            for (int i = 0; i < newOptionRequestParams.length; i++) {
                String requestParam = newOptionRequestParams[i];
                if (requestParam!=null && requestParam.trim().length()>0){
                    //Handle new useroptions
                    logger.debug("handling requestParam["+i+"]="+requestParam+" as a new option");
                    boolean useroptionalreadyexisted = false;
                    List<Questionuserconfig> questionuserconfigs = HibernateUtil.getSession().createCriteria(Questionuserconfig.class)
                                           .add(Restrictions.eq("questionid", question.getQuestionid()))
                                           .add(Restrictions.eq("userid", user.getUserid()))
                                           .setCacheable(false)
                                           .list();
                    for (Iterator<Questionuserconfig> iterator = questionuserconfigs.iterator(); iterator.hasNext();) {
                        Questionuserconfig questionuserconfig = iterator.next();
                        if (questionuserconfig.getName().equals("options")){
                            //We already have a useroptions entry for this question... need to append to it
                            logger.debug("found a pre-existing questionuserconfig entry: questionuserconfig.getQuestionuserconfigid()="+questionuserconfig.getQuestionuserconfigid()+" (requestParam="+requestParam+")");
                            useroptionalreadyexisted = true;
                            String useroptions = questionuserconfig.getValue();
                            String[] userOptionsSplit = useroptions.split("\\n");
                            boolean exactoptionalreadyexisted = false;
                            for (int j = 0; j < userOptionsSplit.length; j++) {
                                String s = userOptionsSplit[j];
                                //Determine whether this exact option exists
                                logger.debug("s.trim()="+s.trim());
                                logger.debug("s.trim().indexOf(requestParam.trim())="+s.trim().indexOf(requestParam.trim()));
                                if (s.trim().indexOf(requestParam.trim())>-1){
                                    logger.debug("an exact option ("+requestParam.trim()+") already exists in a Questionuserconfig");
                                    exactoptionalreadyexisted = true;
                                }
                            }
                            if (!exactoptionalreadyexisted){
                                //Append the new option to the existing Questionuserconfig
                                logger.debug("appending the new option ("+requestParam.trim()+")");
                                questionuserconfig.setValue(questionuserconfig.getValue()+"\n"+requestParam);
                                try{questionuserconfig.save();}catch(Exception ex){logger.error("",ex);}
                            }
                        }
                    }
                    //Create a new Questionuserconfig
                    if (!useroptionalreadyexisted){
                        logger.debug("!useroptionalreadyexisted so creating new one");
                        Questionuserconfig questionuserconfig = new Questionuserconfig();
                        questionuserconfig.setName("options");
                        questionuserconfig.setQuestionid(question.getQuestionid());
                        questionuserconfig.setUserid(user.getUserid());
                        questionuserconfig.setValue(requestParam);
                        try{questionuserconfig.save();}catch(Exception ex){logger.error("",ex);}
                    }
                }
            }
        }
        logger.debug("end processanswer");
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
    public TreeMap getChartData(ArrayList<Post> posts, int questionid, boolean usedisplayoverrideifpossible) {
        if (posts!=null && posts.size()>0){
            TreeMap data = new TreeMap();
            for (Iterator it = posts.iterator(); it.hasNext(); ) {
                Post post = (Post)it.next();
//                List<Postanswer> postanswers = HibernateUtil.getSession().createCriteria(Postanswer.class)
//                                                   .add(Restrictions.eq("postid", post.getPostid()))
//                                                   .setCacheable(false)
//                                                   .list();
//
//                for (Iterator<Postanswer> iterator=postanswers.iterator(); iterator.hasNext();) {
//                    Postanswer postanswer=iterator.next();
//                    if (postanswer.getQuestionid()==questionid){
//                        if(postanswer.getName().equals("response")){
//                            data.put(post.getPostid(), postanswer.getValue().trim());
//                        }
//                    }
//                }
                for (Iterator<Postanswer> iterator = post.getPostanswers().iterator(); iterator.hasNext();) {
                    Postanswer postanswer =  (Postanswer)iterator.next();
                    if (postanswer.getQuestionid()==questionid){
                        if(postanswer.getName().equals("response")){
                            data.put(post.getPostid(), postanswer.getValue().trim());
                        }
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
