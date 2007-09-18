package com.fbdblog.qtype;



import java.util.Iterator;

import org.apache.log4j.Logger;
import com.fbdblog.dao.*;
import com.fbdblog.qtype.def.Component;
import com.fbdblog.qtype.def.ComponentException;
import com.fbdblog.qtype.util.AppPostParser;

/**
 * User: Joe Reger Jr
 * Date: Jul 6, 2006
 * Time: 1:01:00 PM
 */
public class Range implements Component {

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
        out.append("<font class=\"formfieldnamefont\">"+question.getQuestion()+"</font>");
        if (question.getIsrequired()){
            out.append(" ");
            out.append("<font class=\"formfieldnamefont\" style=\"color: #ff0000;\">(Required)</font>");
        }
        out.append("<br/>");


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
            out.append("<td align=\"center\" valign=\"top\">");
            out.append("<input type=\"radio\" name=\""+ AppPostParser.FBDBLOG_REQUEST_PARAM_IDENTIFIER +"questionid_"+question.getQuestionid()+"\" value=\""+i+"\">");
            out.append("<br>");
            out.append(i);
            out.append("</td>");
        }
        if (!createdExactlyMaxRadio){
            out.append("<td align=\"center\" valign=\"top\">");
            out.append("<input type=\"radio\" name=\""+ AppPostParser.FBDBLOG_REQUEST_PARAM_IDENTIFIER +"questionid_"+question.getQuestionid()+"\" value=\""+max+"\">");
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
