package com.fbdblog.scheduledjobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.log4j.Logger;
import com.fbdblog.systemprops.InstanceProperties;


/**
 * User: Joe Reger Jr
 * Date: Jul 19, 2006
 * Time: 2:22:28 PM
 */
public class SystemStats implements Job {



    //BE SURE TO SYNC CODE HERE WITH MAIN SystemStats in formbeans (which jsf uses)


    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            logger.debug("execute() SystemStats called");


     

        } else {
            logger.debug("InstanceProperties.getRunScheduledTasksOnThisInstance() is FALSE for this instance so this task is not being executed.");
        }
    }


}
