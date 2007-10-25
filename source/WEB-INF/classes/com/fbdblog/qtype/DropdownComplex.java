package com.fbdblog.qtype;

import com.fbdblog.qtype.def.Component;
import com.fbdblog.qtype.def.ComponentException;
import com.fbdblog.qtype.util.AppPostParser;
import com.fbdblog.qtype.util.QuestionuserconfigUtil;
import com.fbdblog.chart.ChartField;
import com.fbdblog.chart.DataType;
import com.fbdblog.chart.DataTypeFactory;
import com.fbdblog.dao.*;
import com.fbdblog.dao.hibernate.HibernateUtil;
import com.fbdblog.util.Util;
import com.fbdblog.util.Str;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.ArrayList;

/**
 * User: Joe Reger Jr
 * Date: Jul 6, 2006
 * Time: 1:01:00 PM
 */
public class DropdownComplex implements Component, ChartField {

    public static int ID = 8;
    public static String NAME = "DropdownComplex (Choose One With Titles)";
    private Question question;
    private User user;
    private Post post;

    //Questionconfig values
    private String values = "";
    private String displayoverrides = "";
    private String valuelabel = "Description";
    private String displayoverridelabel = "Value";


    private static String DELIMITER = "---...---";

    Logger logger = Logger.getLogger(this.getClass().getName());

    public DropdownComplex(){

    }

    public DropdownComplex(User user, Post post, Question question){
        this.question = question;
        this.user = user;
        this.post = post;
        loadQuestionConfigsIntoEasyToUseVars();
    }

    private void loadQuestionConfigsIntoEasyToUseVars(){
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
        }
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

        //System values
        out.append("<select name=\""+ AppPostParser.FBDBLOG_REQUEST_PARAM_IDENTIFIER +"questionid_"+question.getQuestionid()+"_\">");
        out.append("<option value=\"\"></option>");
        //Get option html
        out.append(getOption(values.split("\\n"), displayoverrides.split("\\n")));

        //User values
        String uservalues = QuestionuserconfigUtil.getValue(question.getQuestionid(), user.getUserid(), "values");
        String userdisplayoverrides = QuestionuserconfigUtil.getValue(question.getQuestionid(), user.getUserid(), "displayoverrides");
        //Get option html
        out.append(getOption(uservalues.split("\\n"), userdisplayoverrides.split("\\n")));

        //Close select tag
        out.append("</select>");

        //User inputs own value
        out.append("<br/>");
        out.append("<font size=-2 style=\"color: #666666;\"><b>Or, enter your own:</b></font>");
        out.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"margin: 0px; padding: 0px;\">");
        out.append("<tr>");
        out.append("<td valign='top'>");
            out.append("<input type='text' name=\""+AppPostParser.FBDBLOG_REQUEST_PARAM_IDENTIFIER +"questionid_"+question.getQuestionid()+"_-newdisplayoverride\" value=\""+""+"\" size=\"25\" maxlength=\"255\" style=\"font-size: 7px;\">");
        out.append("</td>");
        out.append("<td valign='top'>");
            out.append("<input type='text' name=\""+AppPostParser.FBDBLOG_REQUEST_PARAM_IDENTIFIER +"questionid_"+question.getQuestionid()+"_-newvalue\" value=\""+""+"\" size=\"10\" maxlength=\"255\" style=\"font-size: 7px;\">");
        out.append("</td>");
        out.append("</tr>");
        out.append("<tr>");
        out.append("<td valign='top'>");
            out.append("<font size=-2 style=\"color: #666666;\">"+displayoverridelabel+"</font>");
        out.append("</td>");
        out.append("<td valign='top'>");
            out.append("<font size=-2 style=\"color: #666666;\">"+valuelabel+"</font>");
        out.append("</td>");
        out.append("</tr>");
        out.append("</table>");
        return out.toString();
    }

    private String getOption(String[] values, String[] displayoverrides){
        StringBuffer out = new StringBuffer();
        if (values!=null){
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                String optiondisplaytext = value.trim();
                String displayoverride = Util.getFromStringArraySafely(displayoverrides, i);
                if (displayoverrides!=null && displayoverrides.length>=(i+1)){
                    if (displayoverrides[i]!=null && displayoverrides[i].length()>0){
                        //logger.debug("found displaytext with displayoverrides["+i+"]="+displayoverrides[i]);
                        optiondisplaytext = displayoverrides[i] + "("+value.trim()+" "+valuelabel+")";
                    }
                }
                String selected = "";
                if (isThisvalueSelected(value, displayoverride)){
                    selected = " selected='true'";
                }
                out.append("<option value=\""+ Str.cleanForHtml(value.trim()+DELIMITER+displayoverride.trim())+"\" "+selected+">" + Str.truncateString(optiondisplaytext, 30) + "</option>");
            }
        }
        return out.toString();
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

    private boolean isThisvalueSelected(String value, String displayoverride){
        if (post!=null && post.getPostanswers()!=null){
            boolean isresponsesame = false;
            boolean isdisplayoverridesame = false;
            for (Iterator<Postanswer> iterator=post.getPostanswers().iterator(); iterator.hasNext();) {
                Postanswer postanswer=iterator.next();
                if (postanswer.getQuestionid()==question.getQuestionid()){
                    //Look for the same response
                    if (postanswer.getName().equals("response")){
                        if (postanswer.getValue().trim().equals(value.trim())){
                            isresponsesame = true;
                        }
                    }
                    //Look for the same displayoverride
                    if (postanswer.getName().equals("displayoverride")){
                        if (postanswer.getValue().trim().equals(displayoverride.trim())){
                            isdisplayoverridesame = true;
                        }
                    }
                }
            }
            if (isresponsesame && isdisplayoverridesame){
                return true;
            }
        }
        return false;
    }



    public void validateAnswer(AppPostParser srp) throws ComponentException {
        ComponentException allCex = new ComponentException();
        //Determine whether there is an inbound newvalue and, if so, what it is
        String[] newvalueRequestParams = srp.getParamsWithCertainStringForQuestion(question.getQuestionid(), "-newvalue");
        String newvalue = "";
        if (newvalueRequestParams!=null && newvalueRequestParams.length>0){
            for (int i = 0; i < newvalueRequestParams.length; i++) {
                String requestParam = newvalueRequestParams[i];
                if (requestParam!=null && requestParam.length()>0){
                    newvalue = requestParam;
                }
            }
        }
        logger.debug("newvalue = "+newvalue);
        //Determine whether there is an inbound displayoverride value and, if so, what it is
        String[] newdisplayoverrideRequestParams = srp.getParamsWithCertainStringForQuestion(question.getQuestionid(), "-newdisplayoverride");
        String newdisplayoverride = "";
        if (newdisplayoverrideRequestParams!=null && newdisplayoverrideRequestParams.length>0){
            for (int i = 0; i < newdisplayoverrideRequestParams.length; i++) {
                String requestParam = newdisplayoverrideRequestParams[i];
                if (requestParam!=null && requestParam.length()>0){
                    newdisplayoverride = requestParam;
                }
            }
        }
        if (newdisplayoverride==null || newdisplayoverride.equals("")){
            newdisplayoverride = newvalue;
        }
        logger.debug("newdisplayoverride = "+newdisplayoverride);
        //Requiredness validation
        if (question.getIsrequired()){
            //If we've got a new value to work with, ignore the dropdown
            if (!newvalue.equals("") && !newdisplayoverride.equals("")){
                //Nothing to do... we have a new value
            } else {
                //Don't have new values to look at so use dropdown
                String[] exclude = new String[2];
                exclude[0] = "newvalue";
                exclude[1] = "newdisplayoverride";
                String[] requestParams = srp.getParamsExcludingThoseWithCertainStringForQuestion(question.getQuestionid(), exclude);
                if (requestParams==null){
                    logger.debug("requestParams==null");
                } else {
                    if (requestParams.length<1){
                        logger.debug("requestParams.length<1");
                    } else {
                        if (requestParams[0]==null){
                            logger.debug("requestParams[0]==null");
                        } else {
                            logger.debug("requestParams[0].trim().equals(\"\")="+requestParams[0].trim().equals(""));
                            logger.debug("requestParams[0]="+requestParams[0]);
                        }
                    }
                }
                if (requestParams==null || requestParams.length<1 || requestParams[0]==null || requestParams[0].trim().equals("")){
                    allCex.addErrorsFromAnotherGeneralException(new ComponentException("'"+question.getQuestion()+"' is required."), "");
                }
            }
        }
        //Datatype validation
        DataType dt = DataTypeFactory.get(question.getDatatypeid());
        try{
            //If we've got a new value to work with, ignore the dropdown
            if (!newvalue.equals("") && !newdisplayoverride.equals("")){
                 dt.validataData(newvalue);
            } else {
                //Don't have new values to look at so use dropdown
                String[] requestParams = srp.getParamsExcludingThoseWithCertainStringForQuestion(question.getQuestionid(), "newdisplayoverride");
                if (requestParams!=null && requestParams.length>0){
                    for (int i = 0; i < requestParams.length; i++) {
                        String requestParam = requestParams[i];
                        if (requestParam!=null && requestParam.trim().length()>0){
                            logger.debug("about to validate requestParams["+i+"]="+requestParams[i]);
                            //Split it
                            String[] split = requestParam.split(DELIMITER);
                            if (split!=null && split.length>=2){
                                for (int j=0; j<split.length; j++) {
                                    String s=split[j];
                                    logger.debug("split["+j+"]="+split[j]);
                                }
                                logger.debug("split[0].trim()="+split[0].trim());
                                dt.validataData(split[0].trim());
                            } else {
                                logger.debug("split null or not >2 in length");
                            }

                        }
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
        logger.debug("start processanswer");
        //Determine whether there is an inbound newvalue and, if so, what it is
        String[] newvalueRequestParams = srp.getParamsWithCertainStringForQuestion(question.getQuestionid(), "-newvalue");
        String newvalue = "";
        if (newvalueRequestParams!=null && newvalueRequestParams.length>0){
            for (int i = 0; i < newvalueRequestParams.length; i++) {
                String requestParam = newvalueRequestParams[i];
                if (requestParam!=null && requestParam.length()>0){
                    newvalue = requestParam;
                }
            }
        }
        logger.debug("newvalue = "+newvalue);
        //Determine whether there is an inbound displayoverride value and, if so, what it is
        String[] newdisplayoverrideRequestParams = srp.getParamsWithCertainStringForQuestion(question.getQuestionid(), "-newdisplayoverride");
        String newdisplayoverride = "";
        if (newdisplayoverrideRequestParams!=null && newdisplayoverrideRequestParams.length>0){
            for (int i = 0; i < newdisplayoverrideRequestParams.length; i++) {
                String requestParam = newdisplayoverrideRequestParams[i];
                if (requestParam!=null && requestParam.length()>0){
                    newdisplayoverride = requestParam;
                }
            }
        }
        if (newdisplayoverride==null || newdisplayoverride.equals("")){
            newdisplayoverride = newvalue;
        }
        logger.debug("newdisplayoverride = "+newdisplayoverride);
        //Figure out what the incoming values are... sometimes use the dropdown... sometimes use the user input fields
        String value = "";
        String displayoverride = "";
        if (!newvalue.equals("") && !newdisplayoverride.equals("")){
            //Use the new value typed by the user
            value = newvalue;
            displayoverride = newdisplayoverride;
        } else {
            //Use the dropdown value, but have to split it
            String[] exclude = new String[2];
            exclude[0] = "-newvalue";
            exclude[1] = "-newdisplayoverride";
            String[] requestParams = srp.getParamsExcludingThoseWithCertainStringForQuestion(question.getQuestionid(), exclude);
            logger.debug("requestParams.length="+requestParams.length);
            if (requestParams!=null && requestParams.length>0){
                for (int i = 0; i < requestParams.length; i++) {
                    String requestParam = requestParams[i];
                    logger.debug("requestParams["+i+"]="+requestParams[i]);
                    if (requestParam!=null && requestParam.trim().length()>0){
                        //Split it
                        String[] split = requestParam.split(DELIMITER);
                        if (split!=null && split.length>=2){
                            value = split[0].trim();
                            displayoverride = split[1].trim();
                        } else {
                            logger.debug("split null or not >2 in length");
                        }
                    }
                }
            }
        }
        //Save the answers
        if (!value.equals("") && !displayoverride.equals("")){
            logger.debug("saving answers value="+value+" displayoverride="+displayoverride);
            if (!value.equals("")){
                Postanswer postanswer = new Postanswer();
                postanswer.setQuestionid(question.getQuestionid());
                postanswer.setUserid(user.getUserid());
                postanswer.setName("response");
                postanswer.setValue(value.trim());
                postanswer.setPostid(post.getPostid());
                try{postanswer.save();}catch(Exception ex){logger.error("",ex);}
            }
            if (!displayoverride.equals("")){
                Postanswer postanswer = new Postanswer();
                postanswer.setQuestionid(question.getQuestionid());
                postanswer.setUserid(user.getUserid());
                postanswer.setName("displayoverride");
                postanswer.setValue(displayoverride.trim());
                postanswer.setPostid(post.getPostid());
                try{postanswer.save();}catch(Exception ex){logger.error("",ex);}
            }
        } else {
            logger.debug("not saving answers... nothing to save");   
        }


        //Handle newvalues
        if (newvalue!=null && newvalue.trim().length()>0){
            //Iterate questionconfigs
            logger.debug("handling newvalue="+newvalue+" as a new value");
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
                    logger.debug("found a pre-existing questionuserconfig entry: questionuserconfig.getQuestionuserconfigid()="+questionuserconfig.getQuestionuserconfigid()+" (newvalue="+newvalue+")");
                    uservaluealreadyexisted = true;
                    String uservalues = questionuserconfig.getValue();
                    String[] uservaluesSplit = uservalues.split("\\n");
                    boolean exactvaluealreadyexisted = false;
                    for (int j = 0; j < uservaluesSplit.length; j++) {
                        String s = uservaluesSplit[j];
                        //Determine whether this exact value exists
                        logger.debug("s.trim()="+s.trim());
                        logger.debug("s.trim().indexOf(newvalue.trim())="+s.trim().indexOf(newvalue.trim()));
                        if (s.trim().indexOf(newvalue.trim())>-1){
                            logger.debug("an exact value ("+newvalue.trim()+") already exists in a Questionuserconfig... but do we have a displayoverride too?");
                            //We now have a value match but we may need more
                            //Displayoverride is in use to we need to look to it
                            for (Iterator<Questionuserconfig> iterator2 = questionuserconfigs.iterator(); iterator2.hasNext();) {
                                Questionuserconfig questionuserconfig2 = iterator2.next();
                                if (questionuserconfig2.getName().equals("displayoverride")){
                                    String userdisplayoverrides = questionuserconfig.getValue();
                                    String[] userdisplayoverridesSplit = userdisplayoverrides.split("\\n");
                                    for (int k = 0; k < userdisplayoverridesSplit.length; k++) {
                                        String doov = userdisplayoverridesSplit[k];
                                        if (doov.trim().indexOf(newdisplayoverride.trim())>-1){
                                            //Both the value and displayoverride match
                                            exactvaluealreadyexisted = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //If we didn't find the exact value, append to the existing Questionuserconfig
                    if (!exactvaluealreadyexisted){
                        logger.debug("appending the new value ("+newvalue.trim()+")");
                        questionuserconfig.setValue(questionuserconfig.getValue()+"\n"+newvalue);
                        try{questionuserconfig.save();}catch(Exception ex){logger.error("",ex);}
                        //Now append to displayoverride
                        List<Questionuserconfig> questionuserconfigs2 = HibernateUtil.getSession().createCriteria(Questionuserconfig.class)
                               .add(Restrictions.eq("questionid", question.getQuestionid()))
                               .add(Restrictions.eq("userid", user.getUserid()))
                               .setCacheable(false)
                               .list();
                        for (Iterator<Questionuserconfig> iterator2 = questionuserconfigs2.iterator(); iterator2.hasNext();) {
                            Questionuserconfig questionuserconfig2 = iterator2.next();
                            if (questionuserconfig2.getName().equals("displayoverrides")){
                                String userdisplayoverrides = questionuserconfig2.getValue();
                                questionuserconfig2.setValue(questionuserconfig2.getValue()+"\n"+newdisplayoverride);
                                try{questionuserconfig2.save();}catch(Exception ex){logger.error("",ex);}
                            }
                        }
                    }
                }
            }
            //Create a new Questionuserconfig for the value
            if (!uservaluealreadyexisted){
                logger.debug("!uservaluealreadyexisted so creating new one");
                Questionuserconfig questionuserconfig = new Questionuserconfig();
                questionuserconfig.setName("values");
                questionuserconfig.setQuestionid(question.getQuestionid());
                questionuserconfig.setUserid(user.getUserid());
                questionuserconfig.setValue(newvalue);
                try{questionuserconfig.save();}catch(Exception ex){logger.error("",ex);}
            }
            //Create a new Questionuserconfig for the displayoverride
            if (!uservaluealreadyexisted){
                logger.debug("!uservaluealreadyexisted so creating new one");
                Questionuserconfig questionuserconfig = new Questionuserconfig();
                questionuserconfig.setName("displayoverrides");
                questionuserconfig.setQuestionid(question.getQuestionid());
                questionuserconfig.setUserid(user.getUserid());
                questionuserconfig.setValue(newdisplayoverride);
                try{questionuserconfig.save();}catch(Exception ex){logger.error("",ex);}
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
                List<Postanswer> postanswers = HibernateUtil.getSession().createCriteria(Postanswer.class)
                                                   .add(Restrictions.eq("postid", post.getPostid()))
                                                   .add(Restrictions.eq("questionid", questionid))
                                                   .setCacheable(true)
                                                   .list();
                for (Iterator<Postanswer> iterator=postanswers.iterator(); iterator.hasNext();) {
                    Postanswer postanswer=iterator.next();
                    logger.debug("found postanswerid="+postanswer.getPostanswerid()+" postanswer.getName()="+postanswer.getName()+" postanswer.getValue()="+postanswer.getValue());
                    if(postanswer.getName().equals("response")){
                        logger.debug("+ Adding postid="+post.getPostid()+" postanswer.getValue()="+postanswer.getValue());
                        data.put(post.getPostid(), postanswer.getValue());
                    }
                }
//                for (Iterator<Postanswer> iterator = post.getPostanswers().iterator(); iterator.hasNext();) {
//                    Postanswer postanswer =  (Postanswer)iterator.next();
//                    if (postanswer.getQuestionid()==questionid){
//                        if(postanswer.getName().equals("response")){
//                            data.put(post.getPostid(), postanswer.getValue());
//                        }
//                    }
//                }
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
