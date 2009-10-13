package com.fbdblog.helpers;

import com.fbdblog.dao.App;
import com.fbdblog.dao.User;
import com.fbdblog.dao.hibernate.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Oct 13, 2009
 * Time: 6:42:54 PM
 */
public class FindUserFromNickname {


    public static User find(String nickname){
        Logger logger = Logger.getLogger(FindUserFromNickname.class);
        logger.debug("looking for user with nickname="+ nickname);
        nickname = nickname.replaceAll("/", "");
        nickname = nickname.replaceAll("\\\\", "");
        List<User> users = HibernateUtil.getSession().createCriteria(User.class)
                                           .add(Restrictions.eq("nickname", nickname.trim()))
                                           .setCacheable(true)
                                           .list();
        if (users!=null && users.size()>1){
            logger.error("More than one user for nickname="+ nickname);
        }
        for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
            User user = iterator.next();
            logger.debug("returning userid="+user.getUserid()+" for nickname="+ nickname);
            return user;
        }
        logger.debug("returning null user for nickname="+ nickname);
        return null;
    }


}
