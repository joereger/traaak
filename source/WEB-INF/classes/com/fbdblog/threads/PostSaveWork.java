package com.fbdblog.threads;

import com.fbdblog.threadpool.ThreadPool;
import com.fbdblog.calc.DoCalculationsAfterPost;
import com.fbdblog.facebook.FacebookApiWrapper;
import com.fbdblog.dao.Post;
import com.fbdblog.session.UserSession;

import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Dec 7, 2007
 * Time: 2:57:02 AM
 */
public class PostSaveWork implements Runnable, Serializable {

    private static ThreadPool tp;
    private Post post;
    private UserSession userSession;

    public PostSaveWork(Post post, UserSession userSession){
        this.post = post;
        this.userSession = userSession;
    }

     public void run() {
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            //Do Calculations
            DoCalculationsAfterPost.doCalculations(post);
            //Update Facebook
            if (!userSession.getUserappsettings().getIsprivate()){ 
                FacebookApiWrapper facebookApiWrapper = new FacebookApiWrapper(userSession);
                facebookApiWrapper.postToFeed(post);
                facebookApiWrapper.updateProfile(userSession.getUser());
            }
        } catch (Exception ex){
            logger.error("", ex);
        }
     }

     public void startThread(){
        if (tp==null){
            tp = new ThreadPool(10);
        }
        tp.assign(this);
    }

}
