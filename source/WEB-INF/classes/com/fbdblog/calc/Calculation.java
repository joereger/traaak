package com.fbdblog.calc;

import com.fbdblog.dao.User;
import com.fbdblog.dao.Question;
import com.fbdblog.dao.Post;

import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Dec 2, 2007
 * Time: 10:36:57 AM
 */
public interface Calculation {

    public int getId();
    public String getName();
    public double calculate(User user, Question question, List<Post> posts);


}
