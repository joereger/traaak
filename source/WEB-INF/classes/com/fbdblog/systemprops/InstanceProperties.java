package com.fbdblog.systemprops;

import com.fbdblog.systemprops.WebAppRootDir;
import com.fbdblog.util.Num;
import com.fbdblog.util.GeneralException;
import com.fbdblog.db.Db;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Holds database configuration parameters
 */
public class InstanceProperties {

    private static String dbConnectionUrl;
    private static String dbUsername;
    private static String dbPassword;
    private static String dbMaxActive;
    private static String dbMaxIdle;
    private static String dbMinIdle;
    private static String dbMaxWait;
    private static String dbDriverName;
    private static String runScheduledTasksOnThisInstance;
    private static String instancename;
    private static String absolutepathtochartfiles;
    private static String terracottahost01;
    private static String terracottahost02;
    private static String terracottahost03;
    private static String hibernateshowsql;
    private static String infinispanconfigfileobjectcache;
    private static String infinispanconfigfilehibernate;
    private static String jgroupstcppinginitialhosts;
    private static String jgroupstcpport;


    private static String passPhrase = "pupper";
    private static boolean haveValidConfig = false;
    private static boolean haveNewConfigToTest = false;
    private static boolean haveAttemptedToLoadDefaultPropsFile = false;
    private static String dbPropsInternalFilename = WebAppRootDir.getWebAppRootPath() + "conf"+File.separatorChar+"instance.props";
    private static String dbPropsExternalFilename = "fbdblog-"+WebAppRootDir.getUniqueContextId()+"-dbconfig.txt";

    public static void load(){
        Logger logger = Logger.getLogger(InstanceProperties.class);
        if (!haveValidConfig && !haveAttemptedToLoadDefaultPropsFile){
            try {
                boolean gotFile = false;
                //Look in the /conf directory
                try{
                    File internalFile = new File(dbPropsInternalFilename);
                    if (internalFile!=null && internalFile.exists() && internalFile.canRead() && internalFile.isFile()){
                        Properties properties = new Properties();
                        properties.load(new FileInputStream(internalFile));
                        loadPropsFile(properties);
                        gotFile = true;
                        logger.debug("Got instance props from internal file ("+dbPropsInternalFilename+")");
                    }
                } catch (IOException e) {
                    //e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
                //If we don't have one in the conf directory, look to the system default dir
                if (!gotFile){
                    try{
                        File externalFile = new File("", dbPropsExternalFilename);
                        if (externalFile!=null && externalFile.exists() && externalFile.canRead() && externalFile.isFile()){
                            Properties properties = new Properties();
                            properties.load(new FileInputStream(externalFile));
                            loadPropsFile(properties);
                            gotFile = true;
                            logger.debug("Got instance props from internal file (dbPropsExternalFilename)");
                        }
                    } catch (IOException e) {
                        //e.printStackTrace();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadPropsFile(Properties properties){
        try{
            dbConnectionUrl = properties.getProperty("dbConnectionUrl", "jdbc:mysql://localhost:3306/fbdblog?autoReconnect=true");
            dbUsername = properties.getProperty("dbUsername", "username");
            //DesEncrypter encrypter2 = new DesEncrypter(passPhrase);
            //dbPassword = encrypter2.decrypt(properties.getProperty("dbPassword", "password"));
            dbPassword = properties.getProperty("dbPassword", "password");
            dbMaxActive = properties.getProperty("dbMaxActive", "100");
            dbMaxIdle = properties.getProperty("dbMaxIdle", "15");
            dbMinIdle = properties.getProperty("dbMinIdle", "10");
            dbMaxWait = properties.getProperty("dbMaxWait", "10000");
            dbDriverName = properties.getProperty("dbDriverName", "com.mysql.jdbc.Driver");
            runScheduledTasksOnThisInstance = properties.getProperty("runScheduledTasksOnThisInstance", "0");
            instancename = properties.getProperty("instancename", "InstanceNotNamed");
            absolutepathtochartfiles = properties.getProperty("absolutepathtochartfiles", "c:\\SuperFly\\fdblog-chartcache\\");
            terracottahost01 = properties.getProperty("terracottahost01", "");
            terracottahost02 = properties.getProperty("terracottahost02", "");
            terracottahost03 = properties.getProperty("terracottahost03", "");
            hibernateshowsql = properties.getProperty("hibernateshowsql", "0");
            infinispanconfigfilehibernate = properties.getProperty("infinispanconfigfilehibernate", "");
            infinispanconfigfileobjectcache = properties.getProperty("infinispanconfigfileobjectcache", "");
            jgroupstcppinginitialhosts = properties.getProperty("jgroupstcppinginitialhosts", "localhost[7800]");
            jgroupstcpport = properties.getProperty("jgroupstcpport", "7800");

            haveAttemptedToLoadDefaultPropsFile = true;
            haveNewConfigToTest = true;
        } catch (Exception e){
            e.printStackTrace();
        }

        testConfig();
    }

    public static void save() throws GeneralException {
        Logger logger = Logger.getLogger(InstanceProperties.class);
        logger.debug("save() called");
        logger.debug("haveNewConfigToTest="+haveNewConfigToTest);
        if (!haveNewConfigToTest){
            Properties properties = new Properties();
            try {
                //Make sure we test the next time around
                haveNewConfigToTest = true;
                
                if (dbConnectionUrl!=null){
                    properties.setProperty("dbConnectionUrl", dbConnectionUrl);
                }
                if (dbUsername!=null){
                    properties.setProperty("dbUsername", dbUsername);
                }
                if (dbPassword!=null){
                    //DesEncrypter encrypter2 = new DesEncrypter(passPhrase);
                    //String encDbPassword = encrypter2.encrypt(dbPassword);
                    //properties.setProperty("dbPassword", encDbPassword);
                    properties.setProperty("dbPassword", dbPassword);
                }
                if (dbMaxActive!=null){
                    properties.setProperty("dbMaxActive", dbMaxActive);
                }
                if (dbMaxIdle!=null){
                    properties.setProperty("dbMaxIdle", dbMaxIdle);
                }
                if (dbMinIdle!=null){
                    properties.setProperty("dbMinIdle", dbMinIdle);
                }
                if (dbMaxWait!=null){
                    properties.setProperty("dbMaxWait", dbMaxWait);
                }
                if (dbDriverName!=null){
                    properties.setProperty("dbDriverName", dbDriverName);
                }
                if (runScheduledTasksOnThisInstance!=null){
                    properties.setProperty("runScheduledTasksOnThisInstance", runScheduledTasksOnThisInstance);
                }
                if (instancename!=null){
                    properties.setProperty("instancename", instancename);
                }
                if (absolutepathtochartfiles!=null){
                    properties.setProperty("absolutepathtochartfiles", absolutepathtochartfiles);
                }
                if (terracottahost01!=null){
                    properties.setProperty("terracottahost01", terracottahost01);
                }
                if (terracottahost02!=null){
                    properties.setProperty("terracottahost02", terracottahost02);
                }
                if (terracottahost03!=null){
                    properties.setProperty("terracottahost03", terracottahost03);
                }
                if (hibernateshowsql!=null){
                    properties.setProperty("hibernateshowsql", hibernateshowsql);
                }
                if (infinispanconfigfilehibernate!=null){
                    properties.setProperty("infinispanconfigfilehibernate", infinispanconfigfilehibernate);
                }
                if (infinispanconfigfileobjectcache!=null){
                    properties.setProperty("infinispanconfigfileobjectcache", infinispanconfigfileobjectcache);
                }
                if (jgroupstcppinginitialhosts!=null){
                    properties.setProperty("jgroupstcppinginitialhosts", jgroupstcppinginitialhosts);
                }
                if (jgroupstcpport!=null){
                    properties.setProperty("jgroupstcpport", jgroupstcpport);
                }

                if (testConfig()){
                    logger.debug("passed testConfig()");
                    //Save to the file system in the conf directory
                    File fil = new File(dbPropsInternalFilename);
                    fil.createNewFile();
                    FileOutputStream fos = new FileOutputStream(fil);
                    properties.store(fos, "InstanceProperties");
                    fos.close();
                    fil = null;
                    fos = null;

                    //Store to default system location
                    File fil2 = new File("", dbPropsExternalFilename);
                    fil2.createNewFile();
                    FileOutputStream fos2 = new FileOutputStream(fil2);
                    properties.store(fos2, "InstanceProperties for " + WebAppRootDir.getUniqueContextId());
                    fos2.close();
                    fil2 = null;
                    fos2 = null;
                } else {
                    haveAttemptedToLoadDefaultPropsFile = false;
                    load();
                    //Failed validation
                    GeneralException gex = new GeneralException("The new properties failed validation.");
                    throw gex;
                }

            } catch (IOException e) {
                e.printStackTrace();
                GeneralException gex = new GeneralException("The new properties failed validation: "+e.getMessage());
                throw gex;
            }
        }
    }

    public static boolean testConfig(){
        if (!haveValidConfig && haveNewConfigToTest){
            //-----------------------------------
            //-----------------------------------
            String[][] rstTest= Db.RunSQL("SELECT 1");
            //-----------------------------------
            //-----------------------------------
            if (rstTest!=null && rstTest.length>0){
                haveValidConfig = true;
            }
        }
        haveNewConfigToTest = false;
        return haveValidConfig;
    }

    public static String getDbConnectionUrl() {
        load();
        return dbConnectionUrl;
    }

    public static void setDbConnectionUrl(String dbConnectionUrl) {
        InstanceProperties.dbConnectionUrl = dbConnectionUrl;
    }

    public static String getDbUsername() {
        load();
        return dbUsername;
    }

    public static void setDbUsername(String dbUsername) {
        InstanceProperties.dbUsername = dbUsername;
    }

    public static String getDbPassword() {
        load();
        return dbPassword;
    }

    public static void setDbPassword(String dbPassword) {
        InstanceProperties.dbPassword = dbPassword;
    }

    public static int getDbMaxActive() {
        load();
        if (Num.isinteger(dbMaxActive)){
            return Integer.parseInt(dbMaxActive);
        } else {
            return 100;
        }
    }

    public static void setDbMaxActive(String dbMaxActive) {
        InstanceProperties.dbMaxActive = dbMaxActive;
    }

    public static int getDbMaxIdle() {
        load();
        if (Num.isinteger(dbMaxIdle)){
            return Integer.parseInt(dbMaxIdle);
        } else {
            return 15;
        }
    }

    public static void setDbMaxIdle(String dbMaxIdle) {
        InstanceProperties.dbMaxIdle = dbMaxIdle;
    }

    public static int getDbMinIdle() {
        load();
        if (Num.isinteger(dbMinIdle)){
            return Integer.parseInt(dbMinIdle);
        } else {
            return 10;
        }
    }

    public static void setDbMinIdle(String dbMinIdle) {
        InstanceProperties.dbMinIdle = dbMinIdle;
    }

    public static int getDbMaxWait() {
        load();
        if (Num.isinteger(dbMaxWait)){
            return Integer.parseInt(dbMaxWait);
        } else {
            return 10000;
        }
    }

    public static void setDbMaxWait(String dbMaxWait) {
        InstanceProperties.dbMaxWait = dbMaxWait;
    }

    public static boolean haveValidConfig() {
        load();
        return haveValidConfig;
    }

    public static void setHaveValidConfig(boolean haveValidConfig) {
        InstanceProperties.haveValidConfig = haveValidConfig;
    }

    public static String getDbPropsInternalFilename() {
        load();
        return dbPropsInternalFilename;
    }

    public static void setDbPropsInternalFilename(String dbPropsInternalFilename) {
        InstanceProperties.dbPropsInternalFilename = dbPropsInternalFilename;
    }

    public static String getDbDriverName(){
        load();
        return dbDriverName;
    }

    public static void setDbDriverName(String dbDriverName) {
        InstanceProperties.dbDriverName = dbDriverName;
    }

    public static boolean haveNewConfigToTest() {
        return haveNewConfigToTest;
    }

    public static void setHaveNewConfigToTest(boolean haveNewConfigToTest) {
        InstanceProperties.haveNewConfigToTest = haveNewConfigToTest;
    }

    public static boolean getRunScheduledTasksOnThisInstance(){
        load();
        if (runScheduledTasksOnThisInstance!=null && runScheduledTasksOnThisInstance.equals("1")){
            return true;
        }
        return false;
    }

    public static void setRunScheduledTasksOnThisInstance(boolean runScheduledTasksOnThisInstance){
        if (runScheduledTasksOnThisInstance){
            InstanceProperties.runScheduledTasksOnThisInstance = "1";
        } else {
            InstanceProperties.runScheduledTasksOnThisInstance = "0";
        }
    }


    public static String getInstancename() {
        load();
        return instancename;
    }

    public static void setInstancename(String instancename) {
        InstanceProperties.instancename = instancename;
    }


    public static String getAbsolutepathtochartfiles() {
        load();
        return absolutepathtochartfiles;
    }

    public static void setAbsolutepathtochartfiles(String absolutepathtochartfiles) {
        InstanceProperties.absolutepathtochartfiles=absolutepathtochartfiles;
    }

    public static String getTerracottahost01() {
        load();
        return terracottahost01;
    }

    public static void setTerracottahost01(String terracottahost01) {
        InstanceProperties.terracottahost01 = terracottahost01;
    }

    public static String getTerracottahost02() {
        load();
        return terracottahost02;
    }

    public static void setTerracottahost02(String terracottahost02) {
        InstanceProperties.terracottahost02 = terracottahost02;
    }

    public static String getTerracottahost03() {
        load();
        return terracottahost03;
    }

    public static void setTerracottahost03(String terracottahost03) {
        InstanceProperties.terracottahost03 = terracottahost03;
    }

    public static boolean getHibernateshowsql(){
        load();
        if (hibernateshowsql!=null && hibernateshowsql.equals("1")){
            return true;
        }
        return false;
    }

    public static void setHibernateshowsql(boolean hibernateshowsql){
        if (hibernateshowsql){
            InstanceProperties.hibernateshowsql = "1";
        } else {
            InstanceProperties.hibernateshowsql = "0";
        }
    }




    public static String getInfinispanconfigfilehibernate() {
        load();
        return infinispanconfigfilehibernate;
    }

    public static void setInfinispanconfigfilehibernate(String infinispanconfigfilehibernate) {
        InstanceProperties.infinispanconfigfilehibernate = infinispanconfigfilehibernate;
    }



    public static String getInfinispanconfigfileobjectcache() {
        load();
        return infinispanconfigfileobjectcache;
    }



    public static String getJgroupstcppinginitialhosts() {
        load();
        return jgroupstcppinginitialhosts;
    }

    public static void setJgroupstcppinginitialhosts(String jgroupstcppinginitialhosts) {
        InstanceProperties.jgroupstcppinginitialhosts = jgroupstcppinginitialhosts;
    }


    public static String getJgroupstcpport() {
        load();
        return jgroupstcpport;
    }

    public static void setJgroupstcpport(String jgroupstcpport) {
        InstanceProperties.jgroupstcpport = jgroupstcpport;
    }

}
