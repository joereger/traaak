package com.fbdblog.startup;

import com.fbdblog.systemprops.WebAppRootDir;
import com.fbdblog.systemprops.InstanceProperties;
import com.fbdblog.systemprops.SystemProperty;
import com.fbdblog.dao.hibernate.HibernateUtil;
import com.fbdblog.dao.hibernate.HibernateSessionQuartzCloser;
import com.fbdblog.xmpp.SendXMPPMessage;
import com.fbdblog.scheduledjobs.SystemStats;

import javax.servlet.*;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

import org.apache.log4j.Logger;
import org.quartz.SchedulerFactory;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 * User: Joe Reger Jr
 * Date: Apr 17, 2006
 * Time: 10:50:54 AM
 */
public class ApplicationStartup implements ServletContextListener {

    private static boolean ishibernateinitialized = false;
    private static boolean iswabapprooddirdiscovered = false;
    private static boolean isdatabasereadyforapprun = false;
    private static boolean isappstarted = false;

    Logger logger = Logger.getLogger(this.getClass().getName());
    private static Scheduler scheduler = null;

    public void contextInitialized(ServletContextEvent cse) {
       System.out.println("Fbdblog: Application initialized");
       //Shut down mbeans, if they're running
       shutdownCacheMBean();
       //Configure some dir stuff
        WebAppRootDir ward = new WebAppRootDir(cse.getServletContext());
        iswabapprooddirdiscovered = true;
        //Connect to database
        if (InstanceProperties.haveValidConfig()){
            //Run pre-hibernate db upgrades
            DbVersionCheck dbvcPre = new DbVersionCheck();
            dbvcPre.doCheck(DbVersionCheck.EXECUTE_PREHIBERNATE);
            //Set up hibernate
            HibernateUtil.getSession();
            ishibernateinitialized = true;
            //Run post-hibernate db upgrades
            DbVersionCheck dbvcPost = new DbVersionCheck();
            dbvcPost.doCheck(DbVersionCheck.EXECUTE_POSTHIBERNATE);
            //Check to make sure we're good to go
            if (RequiredDatabaseVersion.getHavecorrectversion()){
                isdatabasereadyforapprun = true;
                isappstarted = true;
            }
            //Configure Log4j
            //Logger.getRootLogger().setLevel();
        } else {
            logger.info("InstanceProperties.haveValidConfig()=false");
        }
        //Load SystemProps
        SystemProperty.refreshAllProps();
        //Refresh SystemStats
        SystemStats ss = new SystemStats();
        try{ss.execute(null);}catch(Exception ex){logger.error(ex);}
        //Initialize Quartz
        initQuartz(cse.getServletContext());
        //Add Quartz listener
        try{
            SchedulerFactory schedFact = new StdSchedulerFactory();
            schedFact.getScheduler().addGlobalJobListener(new HibernateSessionQuartzCloser());
        } catch (Exception ex){logger.error(ex);}
        //Report to log and XMPP
        logger.info("WebAppRootDir = " + WebAppRootDir.getWebAppRootPath());
        logger.info("Fbdblog Application Started!  Let's make some dinero!");
        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SYSADMINS, "Fbdblog Application started! ("+WebAppRootDir.getUniqueContextId()+")");
        xmpp.send();
    }

    public void contextDestroyed(ServletContextEvent cse) {
        //Notify sysadmins
        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SYSADMINS, "Fbdblog Application shut down! ("+WebAppRootDir.getUniqueContextId()+")");
        xmpp.send();
        //Shut down Hibernate
        try{
            HibernateUtil.closeSession();
            HibernateUtil.killSessionFactory();
        } catch (Exception ex){logger.error(ex);}
        //Shut down MBeans
        shutdownCacheMBean();
        //Log it
        System.out.println("Fbdblog: Application shut down! ("+InstanceProperties.getInstancename()+")");
    }

    public static void initQuartz(ServletContext sc){
        //
        //If there are errors in this code, check the org.quartz.ee.servlet.QuartzInitializerServlet
        //I grabbed this code from there instead of having the app server call it from web.xml
        //Potential problem with web.xml is that I may not add my listeners quickly enough.
        //
        Logger logger = Logger.getLogger(ApplicationStartup.class);
        logger.debug("Quartz Initializing");
        String QUARTZ_FACTORY_KEY = "org.quartz.impl.StdSchedulerFactory.KEY";
		StdSchedulerFactory factory;
		try {

			String configFile = null;
			if (configFile != null) {
				factory = new StdSchedulerFactory(configFile);
			} else {
				factory = new StdSchedulerFactory();
			}

			// Should the Scheduler being started now or later
			String startOnLoad = null;
			if (startOnLoad == null || (Boolean.valueOf(startOnLoad).booleanValue())) {
				// Start now
				scheduler = factory.getScheduler();
				scheduler.start();
				logger.debug("Quartz Scheduler has been started");
			} else {
				logger.debug("Quartz Scheduler has not been started - Use scheduler.start()");
			}

			logger.debug("Quartz Scheduler Factory stored in servlet context at key: " + QUARTZ_FACTORY_KEY);
			sc.setAttribute(QUARTZ_FACTORY_KEY, factory);

		} catch (Exception e) {
			logger.error("Quartz failed to initialize", e);
		}
    }

    public static void shutdownCacheMBean(){
        Logger logger = Logger.getLogger(ApplicationStartup.class);
        try{
            ArrayList servers = MBeanServerFactory.findMBeanServer(null);
            for (Iterator it = servers.iterator(); it.hasNext(); ) {
                try{
                    MBeanServer mBeanServer = (MBeanServer)it.next();
                    //List of beans to log
                    Set mBeanNames = mBeanServer.queryNames(null, null);
                    for (Iterator iterator = mBeanNames.iterator(); iterator.hasNext();) {
                        ObjectName objectName = (ObjectName) iterator.next();
                        //logger.debug("MBean -> Name:"+objectName.getCanonicalName()+" Domain:"+objectName.getDomain());
                        if (objectName.getCanonicalName().indexOf("Fbdblog-TreeCache-Cluster")>-1){
                            try{
                                logger.info("Unregistering MBean: "+objectName.getCanonicalName());
                                mBeanServer.unregisterMBean(objectName);
                            } catch (Exception ex){
                                logger.error(ex);
                            }
                        }
                    }
                } catch (Exception ex){
                    logger.error(ex);
                }
            }
        } catch (Exception ex){
            logger.error(ex);
        }
    }

    public static void shutdownMBean(String name){
        Logger logger = Logger.getLogger(ApplicationStartup.class);
        try{
            ArrayList servers = MBeanServerFactory.findMBeanServer(null);
            for (Iterator it = servers.iterator(); it.hasNext(); ) {
                try{
                    MBeanServer mBeanServer = (MBeanServer)it.next();
                    //Do the remove
                    ObjectName tcObject = new ObjectName(name);
                    if (mBeanServer.isRegistered(tcObject)){
                        logger.info(tcObject.getCanonicalName()+" was already registered");
                        try{
                            logger.info("Unregistering MBean: "+tcObject.getCanonicalName());
                            mBeanServer.unregisterMBean(tcObject);
                        } catch (Exception ex){
                            logger.error(ex);
                        }
                    } else {
                        logger.info(tcObject.getCanonicalName()+" was *not* already registered");
                    }
                } catch (Exception ex){
                    logger.error(ex);
                }
            }
        } catch (Exception ex){
            logger.error(ex);
        }
    }


    public static boolean getIswabapprooddirdiscovered() {
        return iswabapprooddirdiscovered;
    }

    public static boolean getIshibernateinitialized() {
        return ishibernateinitialized;
    }

    public static boolean getIsdatabasereadyforapprun() {
        return isdatabasereadyforapprun;
    }

    public static boolean getIsappstarted() {
        return isappstarted;
    }


}
