package com.fbdblog.qtype;


import org.apache.log4j.Logger;

import java.util.*;

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
public class Matrix implements Component {

    public static int ID = 6;
    public static String NAME = "Matrix";
    private Question question;
    private User user;
    private Post post;

    private String DELIM = "_X_X_----_X_X_";

    Logger logger = Logger.getLogger(this.getClass().getName());

    public Matrix(User user, Post post, Question question){
        this.question = question;
        this.user = user;
        this.post = post;
    }

    public String getName() {
        return Matrix.NAME;
    }

    public int getID(){
        return Matrix.ID;
    }

    public String getHtmlForInput() {
        StringBuffer out = new StringBuffer();
        out.append("<font class=\"formfieldnamefont\">"+question.getQuestion()+"</font>");
        if (question.getIsrequired()){
            out.append(" ");
            out.append("<font class=\"formfieldnamefont\" style=\"color: #ff0000;\">(Required)</font>");
        }
        out.append("<br/>");

        //Get config params
        String rowsStr = "";
        String colsStr = "";
        String respondentcanselectmanyStr = "0";
        for (Iterator<Questionconfig> iterator = question.getQuestionconfigs().iterator(); iterator.hasNext();) {
            Questionconfig questionconfig = iterator.next();
            if (questionconfig.getName().equals("rows")){
                rowsStr = questionconfig.getValue();
            }
            if (questionconfig.getName().equals("cols")){
                colsStr = questionconfig.getValue();
            }
            if (questionconfig.getName().equals("respondentcanselectmany")){
                respondentcanselectmanyStr = questionconfig.getValue();
            }
        }
        String[] rows = rowsStr.split("\\n");
        String[] cols = colsStr.split("\\n");
        boolean respondentcanselectmany = false;
        if (respondentcanselectmanyStr.equals("1")){
            respondentcanselectmany = true;
        }

        //Display
        out.append("<table cellpadding=\"3\" cellspacing=\"1\" border=\"0\">");

        //Header
        out.append("<tr>");
        out.append("<td valign=\"top\">");
        out.append("");
        out.append("</td>");
        for (int j = 0; j < cols.length; j++) {
            String col = cols[j].trim();
            out.append("<td align=\"center\" valign=\"top\">");
            out.append(col);
            out.append("</td>");
        }
        out.append("</tr>");

        //Input boxes
        for (int i = 0; i < rows.length; i++) {
            String row = rows[i].trim();
            out.append("<tr>");
            out.append("<td align=\"left\" valign=\"top\">");
            out.append(row);
            out.append("</td>");
            for (int j = 0; j < cols.length; j++) {
                String col = cols[j].trim();
                out.append("<td align=\"center\" valign=\"top\">");
                if (respondentcanselectmany){
                    out.append("<input type=\"checkbox\" name=\""+ AppPostParser.FBDBLOG_REQUEST_PARAM_IDENTIFIER +"questionid_"+question.getQuestionid()+"_row_"+Str.cleanForHtml(row)+"\" value=\""+Str.cleanForHtml(row+DELIM+col)+"\">");
                } else {
                    out.append("<input type=\"radio\" name=\""+ AppPostParser.FBDBLOG_REQUEST_PARAM_IDENTIFIER +"questionid_"+question.getQuestionid()+"_row_"+Str.cleanForHtml(row)+"\" value=\""+Str.cleanForHtml(row+DELIM+col)+"\">");
                }
                out.append("</td>");
            }
            out.append("</tr>");
        }

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
        logger.debug("made it to processAnswer");
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
