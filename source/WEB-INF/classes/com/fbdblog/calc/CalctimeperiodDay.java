package com.fbdblog.calc;

import com.fbdblog.dao.Post;
import com.fbdblog.dao.User;
import com.fbdblog.dao.App;
import com.fbdblog.dao.hibernate.HibernateUtil;
import com.fbdblog.util.Time;

import java.util.List;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;

/**
 * User: Joe Reger Jr
 * Date: Dec 2, 2007
 * Time: 11:02:25 AM
 */
public class CalctimeperiodDay implements Calctimeperiod {

    public static int ID = 4;
    private List<Post> posts;
    private User user;
    private App app;

    public CalctimeperiodDay(User user, App app){
        this.user = user;
        this.app = app;
    }

    public void loadPosts(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        Calendar startTime = Time.xDaysAgoStart(Time.nowInUserTimezone(user.getTimezoneid()), 0);
        List<Post> posts = HibernateUtil.getSession().createCriteria(Post.class)
                                           .add(Restrictions.eq("userid", user.getUserid()))
                                           .add(Restrictions.eq("appid", app.getAppid()))
                                           .add(Restrictions.ge("postdate", startTime.getTime()))
                                           .addOrder(Order.asc("postdate"))
                                           .setCacheable(true)
                                           .list();
        this.posts = posts;
    }

    public int getId() {
        return ID;
    }

    public String getName(){ //"All Time", "Monthly", "Daily"
        return "Day";
    }

    public String getKey(){ // "alltime", "2007-08", "2007-08-07"
        Calendar cal = Time.nowInUserTimezone(user.getTimezoneid());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return year+"-"+month+"-"+day;
    }


    public List<Post> getPosts() {
        return posts;
    }
}
