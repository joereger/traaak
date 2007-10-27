package com.fbdblog;

import com.fbdblog.dao.User;
import com.fbdblog.dao.Userappstatus;
import com.fbdblog.dao.App;
import com.fbdblog.dao.hibernate.HibernateUtil;
import org.hibernate.criterion.Restrictions;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Oct 27, 2007
 * Time: 4:31:21 PM
 */
public class UserappstatusUtil {

    public static void userInstalledApp(User user, App app){
        setStatus(user, app, true);
    }

    public static void userUninstalledApp(User user, App app){
        setStatus(user, app, false);
    }

    private static void setStatus(User user, App app, boolean isinstalled){
        Logger logger = Logger.getLogger(UserappstatusUtil.class);
        boolean saved = false;
        int numberseen = 0;
        List<Userappstatus> objects = HibernateUtil.getSession().createCriteria(Userappstatus.class)
                                           .add(Restrictions.eq("userid", user.getUserid()))
                                           .add(Restrictions.eq("appid", app.getAppid()))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Userappstatus> iterator=objects.iterator(); iterator.hasNext();) {
            Userappstatus userappstatus=iterator.next();
            numberseen = numberseen + 1;
            if (numberseen==1){
                userappstatus.setIsinstalled(isinstalled);
                try{userappstatus.save();}catch(Exception ex){logger.error("", ex);}
                saved = true;
            } else {
                //There are multiple records, delete them
                logger.debug("deleting userappstatusid="+userappstatus.getUserappstatusid());
                try{iterator.remove(); userappstatus.delete();}catch(Exception ex){logger.error("", ex);}
            }
        }
        if (!saved){
            Userappstatus userappstatus = new Userappstatus();
            userappstatus.setAppid(app.getAppid());
            userappstatus.setIsinstalled(isinstalled);
            userappstatus.setUserid(user.getUserid());
            try{userappstatus.save();}catch(Exception ex){logger.error("", ex);}
            saved = true;
        }
    }

}
