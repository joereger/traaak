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

import java.util.ArrayList;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class ImpressionCache implements Job {


    public static ArrayList<ImpressionActivityObject> iaos;

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
//        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() ImpressionCache called");
            try{
                if (iaos!=null){
                    synchronized(iaos){
                        for (Iterator it = iaos.iterator(); it.hasNext(); ) {
                            ImpressionActivityObject iao = (ImpressionActivityObject)it.next();
                            try{
                                ImpressionActivityObjectStorage.store(iao);
                                synchronized(it){
                                    it.remove();
                                }
                            } catch (Exception ex){
                                logger.error(ex);
                            }
                        }
                    }
                }
            } catch (Exception ex){
                logger.debug("Error in top block.");
                logger.error(ex);
            }




//        } else {
//            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
//        }
    }



    public static void addIao(ImpressionActivityObject iao){
        if (iaos==null){
            iaos = new ArrayList<ImpressionActivityObject>();
        }
        synchronized(iaos){
            iaos.add(iao);
        }
    }

    public static ArrayList<ImpressionActivityObject> getIaos() {
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
            for (Iterator it = iaos.iterator(); it.hasNext(); ) {
                ImpressionActivityObject iao = (ImpressionActivityObject)it.next();
               

                User user = User.get(iao.getUserid());
                App app = App.get(iao.getAppid());

                out.append("<tr>");
                out.append("<td valign='top'>");
                out.append("userid:"+user.getUserid());
                out.append("</td>");
                out.append("<td valign='top'>");
                out.append(user.getFirstname()+" "+user.getLastname());
                out.append("</td>");
                out.append("<td valign='top'>");
                out.append("appid:"+app.getAppid());
                out.append("</td>");
                out.append("<td valign='top'>");
                out.append(app.getTitle());
                out.append("</td>");
                out.append("<td valign='top'>");
                out.append(iao.getYear());
                out.append("</td>");
                out.append("<td valign='top'>");
                out.append(iao.getMonth());
                out.append("</td>");
                out.append("</tr>");

            }
            out.append("</table>");
        }
        return out.toString();
    }


}
