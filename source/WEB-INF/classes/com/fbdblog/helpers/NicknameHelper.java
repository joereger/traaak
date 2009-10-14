package com.fbdblog.helpers;

import org.apache.log4j.Logger;
import com.fbdblog.dao.User;
import com.fbdblog.dao.hibernate.HibernateUtil;
import com.fbdblog.util.Str;

import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Oct 14, 2009
 * Time: 12:34:14 PM
 */
public class NicknameHelper {

    public static String recommend(String nickname){
        Logger logger = Logger.getLogger(NicknameHelper.class);
        String appendStr = "";
        int appendCnt = 1;
        while(isNicknameInUse(nickname+appendStr) && appendCnt<100){
            appendCnt++;
            appendStr = String.valueOf(appendCnt);
        }
        return nickname+appendStr;
    }

    public static boolean isNicknameInUse(String nickname){
        Logger logger = Logger.getLogger(NicknameHelper.class);
        List<User> users2 = HibernateUtil.getSession().createQuery("from User where nickname='"+ Str.cleanForSQL(nickname)+"'").list();
        if (users2.size()>0){
            return true;
        }
        return false;
    }


}
