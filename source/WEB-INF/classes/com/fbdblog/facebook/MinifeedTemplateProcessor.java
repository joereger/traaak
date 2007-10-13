package com.fbdblog.facebook;

import com.fbdblog.dao.User;
import com.fbdblog.dao.Question;
import com.fbdblog.dao.Post;
import com.fbdblog.dao.App;
import com.fbdblog.email.EmailTemplateProcessor;
import com.fbdblog.email.EmailSendThread;
import com.fbdblog.email.EmailSend;
import com.fbdblog.util.Io;
import com.fbdblog.util.Str;
import com.fbdblog.util.Num;
import com.fbdblog.systemprops.WebAppRootDir;
import com.fbdblog.systemprops.BaseUrl;
import com.fbdblog.qtype.def.ComponentTypes;
import com.fbdblog.qtype.def.Component;
import org.apache.log4j.Logger;
import org.apache.commons.mail.HtmlEmail;
import org.hibernate.type.ComponentType;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * User: Joe Reger Jr
 * Date: Sep 10, 2006
 * Time: 11:31:16 AM
 */
public class MinifeedTemplateProcessor {


    public static String processTemplate(String template, User user, Post post){
        Logger logger = Logger.getLogger(MinifeedTemplateProcessor.class);
        StringBuffer out = new StringBuffer();
        if (template!=null && !template.equals("")){
            Pattern p = Pattern.compile("\\<\\$(.|\\n)*?\\$\\>");
            Matcher m = p.matcher(template);
            while(m.find()) {
                String tag = m.group();
                m.appendReplacement(out, Str.cleanForAppendreplacement(findWhatToAppend(tag, user, post)));
            }
            try{ m.appendTail(out); } catch (Exception e){}
        }
        return out.toString();
    }

    private static String findWhatToAppend(String tag, User user, Post post){
        Logger logger = Logger.getLogger(MinifeedTemplateProcessor.class);
        String out = "";

        App app = App.get(post.getAppid());

        if (tag.equals("<$url$>")){
            if (app!=null){
                return "http://apps.facebook.com/"+app.getFacebookappname()+"/?comparetouserid="+user.getUserid();
            } else {
                return "";
            }
        }

        if (tag.equals("<$appname$>")){
            if (app!=null){
                return app.getTitle();
            } else {
                return "";
            }
        }


        //<$questionid.1$> <$questionid.2$> <$questionid.3$>
        logger.debug("didn't find a normal tag");
        if (tag.indexOf("questionid")>-1){
            logger.debug("found a questionid tag");
            String tagStripped = tag.substring(2, tag.length()-2);
            logger.debug("tagStripped="+tagStripped);
            String[] tagSplit = tagStripped.split("\\.");
            if (tagSplit.length>1){
                logger.debug("tagSplit.length>1");
                if (tagSplit[1]!=null && Num.isinteger(tagSplit[1])){
                    int questionid = Integer.parseInt(tagSplit[1]);
                    Question question = Question.get(questionid);
                    Component ct = ComponentTypes.getComponentByID(question.getComponenttype(), question, post, user);
                    return ct.getValueForDisplay();
                }
            }
        }
        logger.debug("didn't find any tag to apply... just returning blank");
        return out;
    }






}
