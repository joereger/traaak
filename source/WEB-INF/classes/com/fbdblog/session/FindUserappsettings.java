package com.fbdblog.session;

import com.fbdblog.dao.Userappsettings;
import com.fbdblog.dao.User;
import com.fbdblog.dao.App;
import com.fbdblog.dao.hibernate.HibernateUtil;

import java.util.List;
import java.util.Iterator;

import org.hibernate.criterion.Restrictions;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Dec 10, 2007
 * Time: 9:27:50 AM
 */
public class FindUserappsettings {

    public static Userappsettings get(User user, App app){
        Logger logger = Logger.getLogger(FindUserappsettings.class);
        if (user!=null && user.getUserid()>0 && app!=null && app.getAppid()>0){
            //Search for this user's settings
            List<Userappsettings> userappsettingss = HibernateUtil.getSession().createCriteria(Userappsettings.class)
                                           .add(Restrictions.eq("userid", user.getUserid()))
                                           .add(Restrictions.eq("appid", app.getAppid()))
                                           .setCacheable(true)
                                           .list();
            if (userappsettingss!=null && userappsettingss.size()>0){
                for (Iterator<Userappsettings> iterator=userappsettingss.iterator(); iterator.hasNext();) {
                    Userappsettings uas=iterator.next();
                    return uas;
                }
            }
            //User doesn't have any settings
            Userappsettings userappsettings = new Userappsettings();
            userappsettings.setAppid(app.getAppid());
            userappsettings.setUserid(user.getUserid());
            userappsettings.setIsprivate(false);
            try{userappsettings.save();}catch(Exception ex){logger.error("", ex);}
            return userappsettings;
        }
        return null;
    }



}
