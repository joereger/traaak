package com.fbdblog.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.fbdblog.systemprops.InstanceProperties;
import com.fbdblog.dao.hibernate.HibernateUtil;
import com.fbdblog.dao.Throwdown;
import com.fbdblog.dao.App;
import com.fbdblog.throwdown.ThrowdownEnd;

import java.util.List;
import java.util.Date;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class ThrowdownStatusScheduledTask implements Job {

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() ThrowdownStatusScheduledTask called");
            List<Throwdown> throwdowns = HibernateUtil.getSession().createCriteria(Throwdown.class)
                                               .add(Restrictions.lt("enddate", new Date()))
                                               .add(Restrictions.eq("iscomplete", false))
                                               .setCacheable(true)
                                               .list();
            for (Iterator<Throwdown> iterator=throwdowns.iterator(); iterator.hasNext();) {
                Throwdown throwdown=iterator.next();
                try{
                    App app = App.get(throwdown.getAppid());
                    if (app.getFacebookinfinitesessionkey()!=null && !app.getFacebookinfinitesessionkey().equals("")){
                        ThrowdownEnd.checkEnd(throwdown, app.getFacebookinfinitesessionkey());
                    }
                } catch (Exception ex){
                    logger.error("", ex);
                }
            }
        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }
    }


}
