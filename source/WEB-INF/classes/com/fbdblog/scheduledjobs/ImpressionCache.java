package com.fbdblog.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import com.fbdblog.systemprops.InstanceProperties;
import com.fbdblog.impressions.ImpressionActivityObject;
import com.fbdblog.impressions.ImpressionActivityObjectStorage;
import com.fbdblog.dao.User;
import com.fbdblog.dao.App;

import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class ImpressionCache implements Job {


    //private static List<ImpressionActivityObject> iaos;
    private static Map<String, Integer> iaos;

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
//        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() ImpressionCache called");
            try{
                if (iaos!=null){
                    synchronized(iaos){
                        ImpressionActivityObjectStorage.store(iaos);
                        iaos = Collections.synchronizedMap(new HashMap<String, Integer>());
                    }
                }
            } catch (Exception ex){
                logger.debug("Error in top block.");
                logger.error("",ex);
            }

//        } else {
//            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
//        }
    }



    public static synchronized void addIao(ImpressionActivityObject iao){
        if (iaos==null){
            iaos = Collections.synchronizedMap(new HashMap<String, Integer>());
        }
        synchronized(iaos){
            if (iao.getAppid()>0 && iao.getYear()>0 && iao.getMonth()>0 && iao.getDay()>0){
                String iaoID = iao.getAppid()+ImpressionActivityObjectStorage.DELIM+iao.getYear()+ImpressionActivityObjectStorage.DELIM+iao.getMonth()+ImpressionActivityObjectStorage.DELIM+iao.getDay()+ImpressionActivityObjectStorage.DELIM+iao.getPage();
                if (iaos.containsKey(iaoID)){
                    iaos.put(iaoID, ((Integer)iaos.get(iaoID))+1);
                } else {
                    iaos.put(iaoID, 1);
                }
            }
        }
    }

    public static Map<String, Integer> getIaos() {
        if(iaos!=null){
            synchronized(iaos){
                return iaos;
            }
        }
        return null;
    }

    public static String getIaoCacheAsString(){
        StringBuffer out = new StringBuffer();
        if (iaos!=null){
            out.append("<table cellpadding='3' cellspacing='0' border='0'>");

            Iterator keyValuePairs = iaos.entrySet().iterator();
            for (int i = 0; i < iaos.size(); i++){
                Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
                String key = (String)mapentry.getKey();
                Integer impressioncount = (Integer)mapentry.getValue();

                out.append("<tr>");
                out.append("<td valign='top'>");
                out.append(key);
                out.append("</td>");
                out.append("<td valign='top'>");
                out.append(impressioncount);
                out.append("</td>");
                out.append("</tr>");

            }
            out.append("</table>");
        }
        return out.toString();
    }


}
