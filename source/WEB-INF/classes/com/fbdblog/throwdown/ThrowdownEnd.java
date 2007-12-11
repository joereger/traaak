package com.fbdblog.throwdown;

import com.fbdblog.dao.Throwdown;
import com.fbdblog.dao.App;
import com.fbdblog.dao.User;
import com.fbdblog.facebook.FacebookApiWrapper;
import com.fbdblog.util.Time;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Dec 7, 2007
 * Time: 5:06:41 PM
 */
public class ThrowdownEnd {

    public static void checkEnd(Throwdown throwdown, String sessionkey){
        Logger logger = Logger.getLogger(ThrowdownEnd.class);
        if (!throwdown.getIscomplete()){
            if (throwdown.getEnddate().before(Time.nowInGmtDate())){
                //Let's end this Throwdown
                throwdown.setIscomplete(true);
                //Let's figure out who won and what the score was
                ThrowdownStatus ts = new ThrowdownStatus(throwdown, sessionkey);
                throwdown.setIsfromwinner(ts.getIsFromWinning());
                throwdown.setFromvalue(ts.getFromValue());
                throwdown.setTovalue(ts.getToValue());
                //Save it
                try{throwdown.save();}catch(Exception ex){logger.error("", ex);}
                //Update profile
                App app = App.get(throwdown.getAppid());
                if (app.getFacebookinfinitesessionkey()!=null && !app.getFacebookinfinitesessionkey().equals("")){
                    FacebookApiWrapper faw = new FacebookApiWrapper(app, ts.getFromUser(), app.getFacebookinfinitesessionkey());
                    faw.updateProfile(ts.getFromUser());
                    FacebookApiWrapper faw2 = new FacebookApiWrapper(app, ts.getToUser(), app.getFacebookinfinitesessionkey()); 
                    faw2.updateProfile(ts.getToUser());
                }
                //Send messages
                FacebookApiWrapper faw = new FacebookApiWrapper(app, ts.getFromUser(), app.getFacebookinfinitesessionkey());
                faw.postThrowdownCompleteToFeeds(throwdown);
                

            }
        }
    }

}
