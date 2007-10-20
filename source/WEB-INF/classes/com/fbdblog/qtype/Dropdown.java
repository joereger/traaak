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

/**
 * User: Joe Reger Jr
 * Date: Jul 6, 2006
 * Time: 1:01:00 PM
 */
public class Dropdown implements Component, ChartField {

    public static int ID = 3;
    public static String NAME = "Dropdown (Choose One)";
    private Question question;
    private User user;
    private Post post;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public Dropdown(){
        
    }

    public Dropdown(User user, Post post, Question question){
        this.question = question;
        this.user = user;
        this.post = post;
    }

    public String getName() {
        return Dropdown.NAME;
    }

    public int getID(){
        return Dropdown.ID;
    }

    public String getHtmlForInput() {
        StringBuffer out = new StringBuffer();
        out.append("<font class=\"questionfont\">"+question.getQuestion()+"</font>");
        if (question.getIsrequired()){
            out.append(" ");
            out.append("<font class=\"questionfont\" style=\"color: #ff0000;\">*</font>");
        }
        out.append("<br/>");

        //Select values
        out.append("<select name=\""+ AppPostParser.FBDBLOG_REQUEST_PARAM_IDENTIFIER +"questionid_"+question.getQuestionid()+"_\">");
        out.append("<option value=\"\"></option>");
        //Main values
        String values = "";
        String displayoverrides = "";
        String valuelabel = "Description";
        String displayoverridelabel = "Value";
        boolean usedisplayoverride = false;
        for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
            Questionconfig questionconfig = iterator.next();
            if (questionconfig.getName().equals("values")){
                values = questionconfig.getValue();
            }
            if (questionconfig.getName().equals("displayoverrides")){
                displayoverrides = questionconfig.getValue();
            }
            if (questionconfig.getName().equals("valuelabel")){
                valuelabel = questionconfig.getValue();
            }
            if (questionconfig.getName().equals("displayoverridelabel")){
                displayoverridelabel = questionconfig.getValue();
            }
            if (questionconfig.getName().equals("usedisplayoverride")){
                usedisplayoverride = true;
            }
        }
        String[] valuesSplit = values.split("\\n");
        String[] displayoverridesSplit = displayoverrides.split("\\n");
        for (int i = 0; i < valuesSplit.length; i++) {
            String s = valuesSplit[i];
            String selected = "";
            if (isThisvalueSelected(s)){
                selected = " selected='true'";
            }
            String optiondisplaytext = s.trim();
            if (usedisplayoverride){
                optiondisplaytext = getValueDisplayText(valuesSplit, displayoverridesSplit, i).trim();
                if (!optiondisplaytext.equals(s.trim())){
                    optiondisplaytext = optiondisplaytext + "("+s.trim()+")";
                }
            }
            out.append("<option value=\""+Str.cleanForHtml(s.trim())+"\" "+selected+">" + optiondisplaytext + "</option>");
        }
        //User values
        List<Questionuserconfig> questionuserconfigs = HibernateUtil.getSession().createCriteria(Questionuserconfig.class)
                                           .add(Restrictions.eq("questionid", question.getQuestionid()))
                                           .add(Restrictions.eq("userid", user.getUserid()))
                                           .setCacheable(true)
                                           .list();
        String uservalues = "";
        String userdisplayoverrides = "";
        for (Iterator<Questionuserconfig> iterator = questionuserconfigs.iterator(); iterator.hasNext();) {
            Questionuserconfig questionuserconfig = iterator.next();
            if (questionuserconfig.getName().equals("values")){
                uservalues = questionuserconfig.getValue();
            }
            if (questionuserconfig.getName().equals("displayoverrides")){
                userdisplayoverrides = questionuserconfig.getValue();
            }
        }
        String[] uservaluesSplit = uservalues.split("\\n");
        String[] userDisplayoverridesSplit = userdisplayoverrides.split("\\n");
        for (int i = 0; i < uservaluesSplit.length; i++) {
            String s = uservaluesSplit[i];
            if (s.trim().length()>0){
                String selected = "";
                if (isThisvalueSelected(s)){
                    selected = " selected='true'";
                }
                String optiondisplaytext = s.trim();
                if (usedisplayoverride){
                    optiondisplaytext = getValueDisplayText(uservaluesSplit, userDisplayoverridesSplit, i).trim();
                    if (!optiondisplaytext.equals(s.trim())){
                        optiondisplaytext = optiondisplaytext + "("+s.trim()+" "+valuelabel+")";
                    }
                }
                out.append("<option value=\""+Str.cleanForHtml(s.trim())+"\" "+selected+">" + optiondisplaytext + "</option>");
            }
        }
        //Close select tag
        out.append("</select>");

        //User inputs own value
        out.append("<br/>");
        out.append("<font size=-2 style=\"color: #666666;\"><b>Or, enter your own:</b></font>");
        out.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"margin: 0px; padding: 0px;\">");
        out.append("<tr>");
        if (usedisplayoverride){
            out.append("<td valign='top'>");
                out.append("<input type='text' name=\""+AppPostParser.FBDBLOG_REQUEST_PARAM_IDENTIFIER +"questionid_"+question.getQuestionid()+"_-newdisplayoverride\" value=\""+""+"\" size=\"25\" maxlength=\"255\" style=\"font-size: 7px;\">");
            out.append("</td>");
        }
        out.append("<td valign='top'>");
            out.append("<input type='text' name=\""+AppPostParser.FBDBLOG_REQUEST_PARAM_IDENTIFIER +"questionid_"+question.getQuestionid()+"_-newvalue\" value=\""+""+"\" size=\"10\" maxlength=\"255\" style=\"font-size: 7px;\">");
        out.append("</td>");
        out.append("</tr>");
        out.append("<tr>");
        if (usedisplayoverride){
            out.append("<td valign='top'>");
                out.append("<font size=-2 style=\"color: #666666;\">"+displayoverridelabel+"</font>");
            out.append("</td>");
        }
        out.append("<td valign='top'>");
            out.append("<font size=-2 style=\"color: #666666;\">"+valuelabel+"</font>");
        out.append("</td>");
        out.append("</tr>");
        out.append("</table>");


        return out.toString();
    }

    private String getValueDisplayText(String[] valuesSplit, String[] displayoverridesSplit, int indextoget){
        Logger logger = Logger.getLogger(this.getClass().getName());
        String displaytext = "";
        try{
            if (valuesSplit!=null && valuesSplit.length>=(indextoget+1)){
                logger.debug("found displaytext with valuesSplit["+indextoget+"]="+valuesSplit[indextoget]);
                displaytext = valuesSplit[indextoget];
            }
            if (displayoverridesSplit!=null && displayoverridesSplit.length>=(indextoget+1)){
                if (displayoverridesSplit[indextoget]!=null && displayoverridesSplit[indextoget].length()>0){
                    logger.debug("found displaytext with displayoverridesSplit["+indextoget+"]="+displayoverridesSplit[indextoget]);
                    displaytext = displayoverridesSplit[indextoget];
                }
            }
        } catch (Exception ex){
            logger.error(ex);
        }
        logger.debug("returning displaytext="+displaytext);
        return displaytext;
    }

    public String getValueForDisplay() {
        if (post!=null && post.getPostanswers()!=null){
            for (Iterator<Postanswer> iterator=post.getPostanswers().iterator(); iterator.hasNext();) {
                Postanswer postanswer=iterator.next();
                if (postanswer.getQuestionid()==question.getQuestionid()){
                    if (postanswer.getName().equals("response")){
                        return postanswer.getValue();
                    }
                }
            }
        }
        return "";
    }

    private boolean isThisvalueSelected(String value){
        if (post!=null && post.getPostanswers()!=null){
            for (Iterator<Postanswer> iterator=post.getPostanswers().iterator(); iterator.hasNext();) {
                Postanswer postanswer=iterator.next();
                if (postanswer.getQuestionid()==question.getQuestionid()){
                    if (postanswer.getName().equals("response")){
                        if (postanswer.getValue().trim().equals(value.trim())){
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
        //Validate newvalue
        //@todo need to have both, or one, or something... bleh... need some sort of validation
        //Datatype validation
        DataType dt = DataTypeFactory.get(question.getDatatypeid());
        try{
            String[] requestParams = srp.getParamsExcludingThoseWithCertainStringForQuestion(question.getQuestionid(), "newdisplayoverride");
            if (requestParams!=null && requestParams.length>0){
                for (int i = 0; i < requestParams.length; i++) {
                    String requestParam = requestParams[i];
                    if (requestParam!=null && requestParam.trim().length()>0){
                        //logger.debug("about to validate requestParams["+i+"]="+requestParams[i]);
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
                    try{postanswer.save();}catch(Exception ex){logger.error(ex);}
                }
            }
        }
        //Handle new user values
        String[] newvalueRequestParams = srp.getParamsWithCertainStringForQuestion(question.getQuestionid(), "-newvalue");
        if (newvalueRequestParams!=null && newvalueRequestParams.length>0){
            for (int i = 0; i < newvalueRequestParams.length; i++) {
                String requestParam = newvalueRequestParams[i];
                if (requestParam!=null && requestParam.trim().length()>0){
                    //Handle new uservalues
                    logger.debug("handling requestParam["+i+"]="+requestParam+" as a new value");
                    boolean uservaluealreadyexisted = false;
                    List<Questionuserconfig> questionuserconfigs = HibernateUtil.getSession().createCriteria(Questionuserconfig.class)
                                           .add(Restrictions.eq("questionid", question.getQuestionid()))
                                           .add(Restrictions.eq("userid", user.getUserid()))
                                           .setCacheable(false)
                                           .list();
                    for (Iterator<Questionuserconfig> iterator = questionuserconfigs.iterator(); iterator.hasNext();) {
                        Questionuserconfig questionuserconfig = iterator.next();
                        if (questionuserconfig.getName().equals("values")){
                            //We already have a uservalues entry for this question... need to append to it
                            logger.debug("found a pre-existing questionuserconfig entry: questionuserconfig.getQuestionuserconfigid()="+questionuserconfig.getQuestionuserconfigid()+" (requestParam="+requestParam+")");
                            uservaluealreadyexisted = true;
                            String uservalues = questionuserconfig.getValue();
                            String[] uservaluesSplit = uservalues.split("\\n");
                            boolean exactvaluealreadyexisted = false;
                            for (int j = 0; j < uservaluesSplit.length; j++) {
                                String s = uservaluesSplit[j];
                                //Determine whether this exact value exists
                                logger.debug("s.trim()="+s.trim());
                                logger.debug("s.trim().indexOf(requestParam.trim())="+s.trim().indexOf(requestParam.trim()));
                                if (s.trim().indexOf(requestParam.trim())>-1){
                                    logger.debug("an exact value ("+requestParam.trim()+") already exists in a Questionuserconfig");
                                    exactvaluealreadyexisted = true;
                                }
                            }
                            if (!exactvaluealreadyexisted){
                                //Append the new value to the existing Questionuserconfig
                                logger.debug("appending the new value ("+requestParam.trim()+")");
                                questionuserconfig.setValue(questionuserconfig.getValue()+"\n"+requestParam);
                                try{questionuserconfig.save();}catch(Exception ex){logger.error(ex);}
                            }
                        }
                    }
                    //Create a new Questionuserconfig
                    if (!uservaluealreadyexisted){
                        logger.debug("!uservaluealreadyexisted so creating new one");
                        Questionuserconfig questionuserconfig = new Questionuserconfig();
                        questionuserconfig.setName("values");
                        questionuserconfig.setQuestionid(question.getQuestionid());
                        questionuserconfig.setUserid(user.getUserid());
                        questionuserconfig.setValue(requestParam);
                        try{questionuserconfig.save();}catch(Exception ex){logger.error(ex);}
                    }
                }
            }
        }
        //Handle new user displayoverrides
        String[] newdisplayoverrideRequestParams = srp.getParamsWithCertainStringForQuestion(question.getQuestionid(), "-newdisplayoverride");
        if (newdisplayoverrideRequestParams!=null && newdisplayoverrideRequestParams.length>0){
            for (int i = 0; i < newdisplayoverrideRequestParams.length; i++) {
                String requestParam = newdisplayoverrideRequestParams[i];
                if (requestParam!=null && requestParam.trim().length()>0){
                    //Handle new displayoverrides
                    logger.debug("handling requestParam["+i+"]="+requestParam+" as a new displayoverride");
                    boolean uservaluealreadyexisted = false;
                    List<Questionuserconfig> questionuserconfigs = HibernateUtil.getSession().createCriteria(Questionuserconfig.class)
                                           .add(Restrictions.eq("questionid", question.getQuestionid()))
                                           .add(Restrictions.eq("userid", user.getUserid()))
                                           .setCacheable(false)
                                           .list();
                    for (Iterator<Questionuserconfig> iterator = questionuserconfigs.iterator(); iterator.hasNext();) {
                        Questionuserconfig questionuserconfig = iterator.next();
                        if (questionuserconfig.getName().equals("displayoverrides")){
                            //We already have a uservalues entry for this question... need to append to it
                            logger.debug("found a pre-existing questionuserconfig entry: questionuserconfig.getQuestionuserconfigid()="+questionuserconfig.getQuestionuserconfigid()+" (requestParam="+requestParam+")");
                            uservaluealreadyexisted = true;
                            String uservalues = questionuserconfig.getValue();
                            String[] uservaluesSplit = uservalues.split("\\n");
                            boolean exactvaluealreadyexisted = false;
                            for (int j = 0; j < uservaluesSplit.length; j++) {
                                String s = uservaluesSplit[j];
                                //Determine whether this exact value exists
                                logger.debug("s.trim()="+s.trim());
                                logger.debug("s.trim().indexOf(requestParam.trim())="+s.trim().indexOf(requestParam.trim()));
                                if (s.trim().indexOf(requestParam.trim())>-1){
                                    logger.debug("an exact value ("+requestParam.trim()+") already exists in a Questionuserconfig");
                                    exactvaluealreadyexisted = true;
                                }
                            }
                            if (!exactvaluealreadyexisted){
                                //Append the new value to the existing Questionuserconfig
                                logger.debug("appending the new value ("+requestParam.trim()+")");
                                questionuserconfig.setValue(questionuserconfig.getValue()+"\n"+requestParam);
                                try{questionuserconfig.save();}catch(Exception ex){logger.error(ex);}
                            }
                        }
                    }
                    //Create a new Questionuserconfig
                    if (!uservaluealreadyexisted){
                        logger.debug("!uservaluealreadyexisted so creating new one");
                        Questionuserconfig questionuserconfig = new Questionuserconfig();
                        questionuserconfig.setName("displayoverrides");
                        questionuserconfig.setQuestionid(question.getQuestionid());
                        questionuserconfig.setUserid(user.getUserid());
                        questionuserconfig.setValue(requestParam);
                        try{questionuserconfig.save();}catch(Exception ex){logger.error(ex);}
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
    public TreeMap getChartData(ArrayList<Post> posts, int questionid) {
        if (posts!=null && posts.size()>0){
            TreeMap data = new TreeMap();
            for (Iterator it = posts.iterator(); it.hasNext(); ) {
                Post post = (Post)it.next();
                for (Iterator<Postanswer> iterator = post.getPostanswers().iterator(); iterator.hasNext();) {
                    Postanswer postanswer =  (Postanswer)iterator.next();
                    if (postanswer.getQuestionid()==questionid){
                        if(postanswer.getName().equals("response")){
                            data.put(post.getPostid(), postanswer.getValue());
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
