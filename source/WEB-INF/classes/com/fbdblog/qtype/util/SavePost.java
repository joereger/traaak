package com.fbdblog.qtype.util;

import com.fbdblog.dao.App;
import com.fbdblog.dao.User;
import com.fbdblog.dao.Post;
import com.fbdblog.dao.Question;
import com.fbdblog.qtype.def.ComponentException;
import com.fbdblog.qtype.def.ComponentTypes;
import com.fbdblog.qtype.def.Component;
import com.fbdblog.facebook.FacebookApiWrapper;
import com.fbdblog.xmpp.SendXMPPMessage;
import com.fbdblog.chart.chartcache.ClearCache;
import com.fbdblog.htmlui.UserSession;
import com.fbdblog.calc.DoCalculationsAfterPost;
import com.fbdblog.threads.PostSaveWork;
import com.fbdblog.util.Time;


import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Sep 17, 2007
 * Time: 10:22:14 PM
 */
public class SavePost {

    public static void save(App app, User user, Post post, AppPostParser appPostParser, UserSession userSession)  throws ComponentException {
        Logger logger = Logger.getLogger(SavePost.class.getName());
        ComponentException allCex = new ComponentException();

        //Validate each question
        try{
            if (allCex.getErrors().length<=0){
                for (Iterator<Question> iterator = app.getQuestions().iterator(); iterator.hasNext();) {
                    Question question = iterator.next();
                    Component component = ComponentTypes.getComponentByID(question.getComponenttype(), question, null, user);
                    try{
                        component.validateAnswer(appPostParser);
                    } catch (ComponentException cex){
                        allCex.addErrorsFromAnotherGeneralException(cex, "");
                    }
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
            logger.error("",ex);
            allCex.addValidationError(ex.getMessage());
        }

        //If we have no validation errors, save that sucker!
        if (allCex.getErrors().length<=0){
            try{
                //Create the post
                if (post==null){
                    post = new Post();
                    post.setPostdate(Time.nowInGmtDate());
                }
                post.setUserid(user.getUserid());
                post.setAppid(app.getAppid());
                String notes = "";
                String[] notesParams = appPostParser.getParamsWithCertainString("notes");
                if (notesParams!=null && notesParams.length>0){
                    if (notesParams[0]!=null){
                        notes = notesParams[0];
                    }
                }
                post.setNotes(notes);
                try{
                    post.save();
                } catch (Exception ex){
                    logger.error("",ex);
                    allCex.addValidationError(ex.getMessage());
                }
                //Process each question
                if (allCex.getErrors().length<=0){
                    for (Iterator<Question> iterator = app.getQuestions().iterator(); iterator.hasNext();) {
                        Question question = iterator.next();
                        Component component = ComponentTypes.getComponentByID(question.getComponenttype(), question, post, user);
                        try{component.processAnswer(appPostParser, post);} catch (ComponentException cex){allCex.addErrorsFromAnotherGeneralException(cex, "");}
                    }
                    //Refresh user
                    try{user.save();} catch (Exception ex){logger.error("",ex);}
                    //Refresh the post
                    try{post.save();} catch (Exception ex){logger.error("",ex);}
                    //Clear the chart image cache
                    ClearCache.clearCacheForUser(user.getUserid(), app.getAppid());
                    //Do the heavier work in a thread
                    try{
                        PostSaveWork psw = new PostSaveWork(post, userSession);
                        psw.startThread();
                    } catch(Exception ex){
                        logger.error("", ex);
                    }   
                }
            } catch (Exception ex){
                ex.printStackTrace();
                logger.error("",ex);
                allCex.addValidationError(ex.getMessage());
            }
            //Notify debug group
            SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_CUSTOMERSUPPORT, app.getTitle()+" Post by "+user.getFirstname()+" "+user.getLastname()+"");
            xmpp.send();
        } else {
            //Throw the errors back at the caller... bam baby!
            throw allCex;
        }

    }




}
