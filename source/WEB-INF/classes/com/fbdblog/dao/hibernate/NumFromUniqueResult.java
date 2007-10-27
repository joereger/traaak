package com.fbdblog.dao.hibernate;

import org.apache.log4j.Logger;
import com.fbdblog.util.Num;

/**
 * User: Joe Reger Jr
 * Date: Oct 27, 2007
 * Time: 1:59:16 AM
 */
public class NumFromUniqueResult {

    public static int getInt(String query){
        Logger logger = Logger.getLogger(NumFromUniqueResult.class.getName());
        Object obj = HibernateUtil.getSession().createQuery(query).setCacheable(true).uniqueResult();
        if (obj!=null && obj instanceof Long){
            try{
                Long lng =  (Long)obj;
                if (Num.isinteger(String.valueOf(lng))){
                    int i = lng.intValue();
                    return i;
                }
            }catch(Exception ex){logger.error("",ex); return 0;}
        }
        return 0;
    }

    public static double getDouble(String query){
        Logger logger = Logger.getLogger(NumFromUniqueResult.class.getName());
        Object obj = HibernateUtil.getSession().createQuery(query).setCacheable(true).uniqueResult();
        if (obj!=null && obj instanceof Double){
            try{
                Double dbl =  (Double)obj;
                if (Num.isdouble(String.valueOf(dbl))){
                    return dbl;
                }
            }catch(Exception ex){logger.error("",ex); return 0;}
        }
        return new Double(0);
    }


}
