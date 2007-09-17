package com.fbdblog.dao.hibernate;

import com.fbdblog.dao.User;

/**
 * User: Joe Reger Jr
 * Date: Sep 17, 2007
 * Time: 9:14:55 AM
 */
public interface AuthControlled {

    public boolean canRead(User user);
    public boolean canEdit(User user);

}
