package com.fbdblog.calc;

import com.fbdblog.dao.Post;
import com.fbdblog.dao.User;
import com.fbdblog.dao.Question;
import com.fbdblog.dao.App;
import com.fbdblog.dao.hibernate.HibernateUtil;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;

/**
 * User: Joe Reger Jr
 * Date: Dec 2, 2007
 * Time: 11:02:25 AM
 */
public class CalctimeperiodAlltime implements Calctimeperiod {

    public static int ID = 1;
    private List<Post> posts;

    public CalctimeperiodAlltime(){

    }

    public CalctimeperiodAlltime(User user, App app){
        List<Post> posts = HibernateUtil.getSession().createCriteria(Post.class)
                                           .add(Restrictions.eq("userid", user.getUserid()))
                                           .add(Restrictions.eq("appid", app.getAppid()))
                                           .addOrder(Order.asc("postdate"))
                                           .setCacheable(true)
                                           .list();
        this.posts = posts;
    }

    public int getId() {
        return ID;
    }

    public String getName(){ //"All Time", "Monthly", "Daily"
        return "All Time";
    }

    public String getKey(){ // "alltime", "2007-08", "2007-08-07"
        return "Forever";
    }

    public List<Post> getPosts() {
        return posts;
    }
}
