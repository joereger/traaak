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

    public static void store(ImpressionActivityObject iao){
        Logger logger = Logger.getLogger(ImpressionActivityObjectStorage.class);

        //Collapse impressions into queue with key=1-45-2007-06
        //Goal here is to cut down on inserts into the database
        HashMap<String, Integer> queue = new HashMap<String, Integer>();
        if (iao.getUserid()>0 && iao.getAppid()>0 && iao.getYear()>0 && iao.getMonth()>0){
            String iaoID = iao.getUserid()+"-"+iao.getAppid()+"-"+iao.getYear()+"-"+iao.getMonth();
            if (queue.containsKey(iaoID)){
                queue.put(iaoID, ((Integer)queue.get(iaoID))+1);
            } else {
                queue.put(iaoID, 1);
            }
        }

        //Run through queue and add to database
        Iterator keyValuePairs = queue.entrySet().iterator();
        for (int i = 0; i < queue.size(); i++){
            try{
                Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
                String key = (String)mapentry.getKey();
                Integer impressioncount = (Integer)mapentry.getValue();
                String[] keySplit = key.split("-");
                if (keySplit.length>=4){
                    boolean updatedone = false;
                    //See if there's already an entry in the db for this userid, appid, year and month
                    List<Impression> impressions = HibernateUtil.getSession().createCriteria(Impression.class)
                                                       .add(Restrictions.eq("userid", Integer.parseInt(keySplit[0])))
                                                       .add(Restrictions.eq("appid", Integer.parseInt(keySplit[1])))
                                                       .add(Restrictions.eq("year", Integer.parseInt(keySplit[2])))
                                                       .add(Restrictions.eq("month", Integer.parseInt(keySplit[3])))
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
                        impression.setUserid(Integer.parseInt(keySplit[0]));
                        impression.setAppid(Integer.parseInt(keySplit[1]));
                        impression.setYear(Integer.parseInt(keySplit[2]));
                        impression.setMonth(Integer.parseInt(keySplit[3]));
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
