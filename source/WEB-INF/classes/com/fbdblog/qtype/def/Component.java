package com.fbdblog.qtype.def;

import com.fbdblog.qtype.util.AppPostParser;
import com.fbdblog.dao.Post;


/**
 * User: Joe Reger Jr
 * Date: Jul 6, 2006
 * Time: 10:17:58 AM
 */
public interface Component {

    public String getName();
    public int getID();
    public String getHtmlForInput();
    public void validateAnswer(AppPostParser srp) throws com.fbdblog.qtype.def.ComponentException;
    public void processAnswer(AppPostParser srp, Post post) throws ComponentException;

}
