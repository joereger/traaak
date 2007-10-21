package com.fbdblog.email;

import com.fbdblog.dao.User;
import com.fbdblog.util.*;
import com.fbdblog.systemprops.SystemProperty;
import com.fbdblog.systemprops.WebAppRootDir;
import com.fbdblog.systemprops.BaseUrl;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Sep 10, 2006
 * Time: 11:31:16 AM
 */
public class EmailTemplateProcessor {

    public static void sendMail(String subject, String emailTemplateFilenameWithoutExtension, User userTo){
        sendMail(subject, emailTemplateFilenameWithoutExtension, userTo, null, null, null);
    }

    public static void sendMail(String subject, String emailTemplateFilenameWithoutExtension, User userTo, String[] args){
        sendMail(subject, emailTemplateFilenameWithoutExtension, userTo, args, null, null);
    }

    public static void sendMail(String subject, String emailTemplateFilenameWithoutExtension, User userTo, String[] args, String toaddress, String fromaddress){
        Logger logger = Logger.getLogger(EmailTemplateProcessor.class);
        String htmlTemplate = Io.textFileRead(WebAppRootDir.getWebAppRootPath() + "emailtemplates" + java.io.File.separator + emailTemplateFilenameWithoutExtension + ".html").toString();
        String txtTemplate = Io.textFileRead(WebAppRootDir.getWebAppRootPath() + "emailtemplates" + java.io.File.separator + emailTemplateFilenameWithoutExtension + ".txt").toString();
        sendMail(subject, htmlTemplate, txtTemplate, userTo, args, toaddress, fromaddress);
    }

    public static void sendMail(String subject, String htmlTemplate, String txtTemplate, User userTo, String[] args, String toaddress, String fromaddress){
        Logger logger = Logger.getLogger(EmailTemplateProcessor.class);
        String htmlEmailHeader = Io.textFileRead(WebAppRootDir.getWebAppRootPath() + "emailtemplates" + java.io.File.separator + "emailheader.html").toString();
        String htmlEmailFooter = Io.textFileRead(WebAppRootDir.getWebAppRootPath() + "emailtemplates" + java.io.File.separator + "emailfooter.html").toString();
        String htmlMessage = processTemplate(htmlTemplate, userTo, args);
        String txtMessage = processTemplate(txtTemplate, userTo, args);
        htmlMessage = translateImageLinks(htmlMessage);
        txtMessage = translateImageLinks(txtMessage);
        try{
            HtmlEmail email = new HtmlEmail();
            boolean havetoaddress=false;
            if (toaddress!=null && !toaddress.equals("")){
                email.addTo(toaddress);
                havetoaddress = true;
            }
            if (fromaddress!=null && !fromaddress.equals("")){
                email.setFrom(fromaddress, fromaddress);
            } else {
                email.setFrom(EmailSendThread.DEFAULTFROM, "Fbdblog Social Surveys");
            }
            email.setSubject(subject);
            email.setHtmlMsg(htmlEmailHeader + htmlMessage + htmlEmailFooter);
            email.setTextMsg(txtMessage);
            if (havetoaddress){
                EmailSend.sendMail(email);
            }
        } catch (Exception e){
            logger.error("",e);
        }
    }

    public static String processTemplate(String template, User user, String[] args){
        Logger logger = Logger.getLogger(EmailTemplateProcessor.class);
        StringBuffer out = new StringBuffer();
        if (template!=null && !template.equals("")){
            Pattern p = Pattern.compile("\\<\\$(.|\\n)*?\\$\\>");
            Matcher m = p.matcher(template);
            while(m.find()) {
                String tag = m.group();
                m.appendReplacement(out, Str.cleanForAppendreplacement(findWhatToAppend(tag, user, args)));
            }
            try{ m.appendTail(out); } catch (Exception e){}
        }
        return out.toString();
    }

    private static String findWhatToAppend(String tag, User user, String[] args){
        Logger logger = Logger.getLogger(EmailTemplateProcessor.class);
        String out = "";

        if (tag.equals("<$user.firstname$>")){
            if (user!=null){
                return user.getFirstname();
            } else {
                return "";
            }
        } else if (tag.equals("<$user.lastname$>")){
            if (user!=null){
                return user.getLastname();
            } else {
                return "";
            }
        } else if (tag.equals("<$user.userid$>")){
            if (user!=null){
                return String.valueOf(user.getUserid());
            } else {
                return "";
            }
        } else if (tag.equals("<$baseUrl.includinghttp$>")){
            return BaseUrl.get(false);
        }


        //<$args.1$> <$args.2$> <$args.3$>
        logger.debug("didn't find a normal tag");
        if (tag.indexOf("args")>-1){
            logger.debug("found an args tag");
            String tagStripped = tag.substring(2, tag.length()-2);
            logger.debug("tagStripped="+tagStripped);
            String[] tagSplit = tagStripped.split("\\.");
            if (tagSplit.length>1){
                logger.debug("tagSplit.length>1");
                if (tagSplit[1]!=null && Num.isinteger(tagSplit[1])){
                    int index = Integer.parseInt(tagSplit[1]);
                    if (args!=null && args[index]!=null){
                        logger.debug("returning: args["+index+"]="+args[index]);
                        return args[index];
                    }
                }
            }
        }
        logger.debug("didn't find any tag to apply... just returning blank");
        return out;
    }

    public static String translateImageLinks(String template){
        Logger logger = Logger.getLogger(EmailTemplateProcessor.class);
        try{
            StringBuffer out = new StringBuffer();
            Pattern p = Pattern.compile("img src=(['\"])?images", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(template);
            while(m.find()) {
                String tag = m.group();
                logger.debug("found tag="+tag);
                String openquote = "";
                if (m.group(1)!=null){
                    openquote = m.group(1);
                }
                String replacement = "img src="+openquote+BaseUrl.get(false)+"emailtemplates/images";
                logger.debug("replacement ="+replacement);
                m.appendReplacement(out, Str.cleanForAppendreplacement(replacement));
            }
            try{ m.appendTail(out); } catch (Exception e){}
            return out.toString();
        } catch (Exception ex){
            logger.error("",ex);
            return template;
        }


    }




}
