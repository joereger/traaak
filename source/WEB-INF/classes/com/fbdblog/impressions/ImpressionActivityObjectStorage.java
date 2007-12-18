package com.fbdblog.impressions;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

import com.fbdblog.dao.hibernate.HibernateUtil;
import com.fbdblog.dao.Impression;

/**
 * User: Joe Reger Jr
 * Date: Sep 29, 2007
 * Time: 11:12:22 AM
 */
public class ImpressionActivityObjectStorage {

    public static String DELIM = "---";


    public static void store(Map<String, Integer> queue){
        Logger logger = Logger.getLogger(ImpressionActivityObjectStorage.class);
        //Run through queue and add to database
        Iterator keyValuePairs = queue.entrySet().iterator();
        for (int i = 0; i < queue.size(); i++){
            try{
                Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
                String key = (String)mapentry.getKey();
                Integer impressioncount = (Integer)mapentry.getValue();
                String[] keySplit = key.split(DELIM);
                if (keySplit.length>=5){
                    boolean updatedone = false;
                    //See if there's already an entry in the db for this userid, appid, year and month
                    List<Impression> impressions = HibernateUtil.getSession().createCriteria(Impression.class)
                                                       .add(Restrictions.eq("appid", Integer.parseInt(keySplit[0])))
                                                       .add(Restrictions.eq("year", Integer.parseInt(keySplit[1])))
                                                       .add(Restrictions.eq("month", Integer.parseInt(keySplit[2])))
                                                       .add(Restrictions.eq("day", Integer.parseInt(keySplit[3])))
                                                       .add(Restrictions.eq("page", keySplit[4]))
                                                       .setCacheable(false)
                                                       .list();
                    for (Iterator<Impression> iterator=impressions.iterator(); iterator.hasNext();) {
                        Impression impression=iterator.next();
                        impression.setImpressions(impression.getImpressions()+impressioncount);
                        try{impression.save();}catch(Exception ex){logger.error("",ex);}
                        updatedone = true;
                    }
                    //If nothing was found, create a new record
                    if (!updatedone){
                        Impression impression = new Impression();
                        impression.setAppid(Integer.parseInt(keySplit[0]));
                        impression.setYear(Integer.parseInt(keySplit[1]));
                        impression.setMonth(Integer.parseInt(keySplit[2]));
                        impression.setDay(Integer.parseInt(keySplit[3]));
                        impression.setPage(keySplit[4]);
                        impression.setImpressions(impressioncount);
                        try{impression.save();}catch(Exception ex){logger.error("",ex);}
                    }
                }
            } catch(Exception ex){
                logger.error("",ex);
            }
        }

    }


}
