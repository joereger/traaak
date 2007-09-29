package com.fbdblog.facebook;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Iterator;

import com.fbdblog.dao.User;
import com.fbdblog.dao.hibernate.HibernateUtil;
import com.fbdblog.util.UserInputSafe;

/**
 * User: Joe Reger Jr
 * Date: Sep 17, 2007
 * Time: 11:52:39 AM
 */
public class FindUserFromFacebookUid {

    public static User find(String facebookuid){
        Logger logger = Logger.getLogger(FindUserFromFacebookUid.class);
        logger.debug("looking for user with facebookid="+facebookuid);
        List<User> users = HibernateUtil.getSession().createCriteria(User.class)
                                           .add(Restrictions.eq("facebookuid", facebookuid))
                                           .setCacheable(true)
                                           .list();
        if (users!=null && users.size()>1){
            logger.error("More than one user for facebookuid="+facebookuid);
        }
        for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
            User user = iterator.next();
            logger.debug("returning userid="+user.getUserid()+" for facebookuid="+facebookuid);
            return user;
        }
        logger.debug("returning null user for facebookuid="+facebookuid);
        return null;
    }


}
