package com.fbdblog.calc;

import com.fbdblog.dao.Post;
import com.fbdblog.dao.Question;
import com.fbdblog.dao.User;
import com.fbdblog.dao.App;

import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Dec 2, 2007
 * Time: 10:38:25 AM
 */
public interface Calctimeperiod {

    public int getId();
    public String getName(); //"All Time", "Monthly", "Daily"
    public String getKey(); // "alltime", "2007-08", "2007-08-07"
    public List<Post> getPosts();
    
}
