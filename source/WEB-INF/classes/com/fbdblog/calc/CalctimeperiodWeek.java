package com.fbdblog.calc;

import com.fbdblog.dao.Post;
import com.fbdblog.dao.User;
import com.fbdblog.dao.App;
import com.fbdblog.dao.hibernate.HibernateUtil;
import com.fbdblog.util.Time;

import java.util.List;
import java.util.Calendar;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;

/**
 * User: Joe Reger Jr
 * Date: Dec 2, 2007
 * Time: 11:02:25 AM
 */
public class CalctimeperiodWeek implements Calctimeperiod {

    public static int ID = 5;
    private List<Post> posts;

    public CalctimeperiodWeek(){

    }

    public CalctimeperiodWeek(User user, App app){
        Logger logger = Logger.getLogger(this.getClass().getName());
        Calendar startTime = Time.xWeeksAgoStart(Calendar.getInstance(), 0);
        //logger.debug("startTime="+Time.dateformatcompactwithtime(startTime));
        List<Post> posts = HibernateUtil.getSession().createCriteria(Post.class)
                                           .add(Restrictions.eq("userid", user.getUserid()))
                                           .add(Restrictions.eq("appid", app.getAppid()))
                                           .add(Restrictions.ge("postdate", startTime.getTime()))
                                           .addOrder(Order.asc("postdate"))
                                           .setCacheable(true)
                                           .list();
        this.posts = posts;
//        for (Iterator<Post> iterator=posts.iterator(); iterator.hasNext();) {
//            Post post=iterator.next();
//            logger.debug(Time.dateformatcompactwithtime(Time.getCalFromDate(post.getPostdate()))+" - postid="+post.getPostid());
//        }
    }

    public int getId() {
        return ID;
    }

    public String getName(){ //"All Time", "Monthly", "Daily"
        return "Week";
    }

    public String getKey(){ // "alltime", "2007-08", "2007-08-07"
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;
        int weekofmonth = cal.get(Calendar.WEEK_OF_MONTH);
        return year+"-"+month+" wk:"+weekofmonth;
    }

    public List<Post> getPosts() {
        return posts;
    }
}
