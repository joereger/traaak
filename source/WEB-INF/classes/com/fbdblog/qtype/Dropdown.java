package com.fbdblog.qtype;


import java.util.*;

import org.apache.log4j.Logger;
import com.fbdblog.dao.*;
import com.fbdblog.qtype.def.Component;
import com.fbdblog.qtype.def.ComponentException;
import com.fbdblog.qtype.util.AppPostParser;
import com.fbdblog.util.Str;

/**
 * User: Joe Reger Jr
 * Date: Jul 6, 2006
 * Time: 1:01:00 PM
 */
public class Dropdown implements Component {

    public static int ID = 3;
    public static String NAME = "Dropdown (Choose One)";
    private Question question;
    private User user;
    private Post post;

    Logger logger = Logger.getLogger(this.getClass().getName());

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
        out.append("<font class=\"formfieldnamefont\">"+question.getQuestion()+"</font>");
        if (question.getIsrequired()){
            out.append(" ");
            out.append("<font class=\"formfieldnamefont\" style=\"color: #ff0000;\">(Required)</font>");
        }
        out.append("<br/>");

        String options = "";
        for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
            Questionconfig questionconfig = iterator.next();
            if (questionconfig.getName().equals("options")){
                options = questionconfig.getValue();
            }
        }
        String[] optionsSplit = options.split("\\n");

        out.append("<select name=\""+ AppPostParser.FBDBLOG_REQUEST_PARAM_IDENTIFIER +"questionid_"+question.getQuestionid()+"\">");
        out.append("<option value=\"\"></option>");
        for (int i = 0; i < optionsSplit.length; i++) {
            String s = optionsSplit[i];
            out.append("<option value=\""+Str.cleanForHtml(s)+"\">" + s + "</option>");
        }
        out.append("</select>");


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



}
