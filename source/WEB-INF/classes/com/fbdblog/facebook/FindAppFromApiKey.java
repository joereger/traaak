package com.fbdblog.facebook;

import com.fbdblog.dao.User;
import com.fbdblog.dao.App;
import com.fbdblog.dao.hibernate.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Sep 17, 2007
 * Time: 12:33:56 PM
 */
public class FindAppFromApiKey {

    public static App find(String api_key){
        Logger logger = Logger.getLogger(FindAppFromApiKey.class);
        logger.debug("looking for app with api_key="+api_key);
        List<App> apps = HibernateUtil.getSession().createCriteria(App.class)
                                           .add(Restrictions.eq("facebookapikey", api_key))
                                           .setCacheable(true)
                                           .list();
        if (apps!=null && apps.size()>1){
            logger.error("More than one app for facebookapikey="+api_key);
        }
        for (Iterator<App> iterator = apps.iterator(); iterator.hasNext();) {
            App app = iterator.next();
            logger.debug("returning appid="+app.getAppid()+" for facebookapikey="+api_key);
            return app;
        }
        logger.debug("returning null app for facebookapikey="+api_key);
        return null;
    }


}
