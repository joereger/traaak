package com.fbdblog.throwdown;

import com.fbdblog.dao.Throwdown;
import com.fbdblog.dao.User;
import com.fbdblog.dao.App;
import com.fbdblog.dao.Userappsettings;
import com.fbdblog.session.FindUserappsettings;

/**
 * User: Joe Reger Jr
 * Date: Dec 10, 2007
 * Time: 9:56:06 AM
 */
public class ThrowdownPrivacy {

    public static boolean isok(Throwdown throwdown){
        //This method checks the participants of the throwdown and makes sure that they both have public apps
        ThrowdownStatus ts = new ThrowdownStatus(throwdown);
        App app = App.get(throwdown.getAppid());
        Userappsettings fromUserappsettings = FindUserappsettings.get(ts.getFromUser(), app);
        if (fromUserappsettings.getIsprivate()){
            return false;
        }
        if (ts.getToUser()!=null){
            Userappsettings toUserappsettings = FindUserappsettings.get(ts.getToUser(), app);
            if (toUserappsettings.getIsprivate()){
                return false;
            }
        }
        return true;
    }

}
