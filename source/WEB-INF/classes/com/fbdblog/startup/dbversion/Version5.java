package com.fbdblog.startup.dbversion;

import com.fbdblog.startup.UpgradeDatabaseOneVersion;
import com.fbdblog.db.Db;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Nov 26, 2006
 * Time: 11:57:46 AM
 */
public class Version5 implements UpgradeDatabaseOneVersion {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void doPreHibernateUpgrade(){
        logger.debug("doPreHibernateUpgrade() start");
        logger.debug("Not really doing anything... Version0 is a placeholder class.");
        logger.debug("doPreHibernateUpgrade() finish");
    }

    public void doPostHibernateUpgrade(){
        logger.debug("doPostHibernateUpgrade() start");

        //-----------------------------------
        //-----------------------------------
        String[][] rstAppactivity= Db.RunSQL("SELECT userid, appid FROM userappactivity WHERE isinstall=true");
        //-----------------------------------
        //-----------------------------------
        if (rstAppactivity!=null && rstAppactivity.length>0){
            for(int i=0; i<rstAppactivity.length; i++){
                int userid = Integer.parseInt(rstAppactivity[i][0]);
                int appid = Integer.parseInt(rstAppactivity[i][1]);
                //-----------------------------------
                //-----------------------------------
                String[][] rstAppstatus= Db.RunSQL("SELECT count(*) FROM userappstatus WHERE userid='"+userid+"' AND appid='"+appid+"'");
                //-----------------------------------
                //-----------------------------------
                int num = 0;
                if (rstAppstatus!=null && rstAppstatus.length>0){
                    num = Integer.parseInt(rstAppstatus[0][0]);
                }
                if (num==0){
                    //-----------------------------------
                    //-----------------------------------
                    int identity = Db.RunSQLInsert("INSERT INTO userappstatus(userid, appid, isinstalled) VALUES('"+userid+"', '"+appid+"', true)");
                    //-----------------------------------
                    //-----------------------------------
                }
            }
        }

        


        logger.debug("doPostHibernateUpgrade() finish");
    }


    //Sample sql statements

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("CREATE TABLE `pltemplate` (`pltemplateid` int(11) NOT NULL auto_increment, logid int(11), plid int(11), type int(11), templateid int(11), PRIMARY KEY  (`pltemplateid`)) ENGINE=MyISAM DEFAULT CHARSET=latin1;");
    //-----------------------------------
    //-----------------------------------

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("ALTER TABLE megachart CHANGE daterangesavedsearchid daterangesavedsearchid int(11) NOT NULL default '0'");
    //-----------------------------------
    //-----------------------------------

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("ALTER TABLE account DROP gps");
    //-----------------------------------
    //-----------------------------------

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("ALTER TABLE megalogtype ADD isprivate int(11) NOT NULL default '0'");
    //-----------------------------------
    //-----------------------------------

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("DROP TABLE megafielduser");
    //-----------------------------------
    //-----------------------------------

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("CREATE INDEX name_of_index ON table (field1, field2)");
    //-----------------------------------
    //-----------------------------------


}
