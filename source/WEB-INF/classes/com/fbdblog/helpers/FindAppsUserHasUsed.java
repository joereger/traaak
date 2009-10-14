package com.fbdblog.helpers;

import com.fbdblog.dao.App;
import com.fbdblog.dao.User;
import com.fbdblog.dao.hibernate.HibernateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Oct 14, 2009
 * Time: 9:27:37 AM
 */
public class FindAppsUserHasUsed {

    public static ArrayList<App> find(int userid){
        return find(User.get(userid));
    }
    public static ArrayList<App> find(User user){
        Logger logger = Logger.getLogger(FindAppsUserHasUsed.class);
        ArrayList<App> out = new ArrayList<App>();
        List<Integer> appids = HibernateUtil.getSession().createQuery("select distinct p.appid from Post p where userid='"+user.getUserid()+"'").list();
        if (appids!=null){
            for (Iterator<Integer> integerIterator=appids.iterator(); integerIterator.hasNext();) {
                Integer appid=integerIterator.next();
                out.add(App.get(appid));
                logger.debug("added appid="+appid+" to out");
            }
        }
        return out;
    }


}
