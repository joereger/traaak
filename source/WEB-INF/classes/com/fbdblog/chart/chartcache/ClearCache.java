package com.fbdblog.chart.chartcache;

import com.fbdblog.systemprops.InstanceProperties;
import com.fbdblog.util.Util;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.util.*;
import java.io.File;

/**
 * User: Joe Reger Jr
 * Date: Sep 26, 2007
 * Time: 2:54:59 PM
 */
public class ClearCache {

//    String sep="/";
//    String filename= InstanceProperties.getAbsolutepathtochartfiles() + sep + "appid-" + megaChart.getChart().getAppid() + sep + "userid-" + userid + sep + "chartid-" + chartid + sep + "chart-" + size + ".png";
//    filename = FilenameUtils.separatorsToSystem(filename);

    public static void clearCacheForUser(int userid, int appid){
        Logger logger = Logger.getLogger(ClearCache.class);
        String sep="/";
        String filename= InstanceProperties.getAbsolutepathtochartfiles() + sep + "appid-" + appid + sep + "userid-" + userid + sep;
        filename = FilenameUtils.separatorsToSystem(filename);
        deleteDirAndSubs(new File(filename));
        //Now delete comparetouserid- directories
        lookForDirectoryWithThisNameAndDeleteIt(new File(filename), "comparetouserid-"+userid);
    }

    public static void clearCacheForApp(int appid){
        Logger logger = Logger.getLogger(ClearCache.class);
        String sep="/";
        String filename= InstanceProperties.getAbsolutepathtochartfiles() + sep + "appid-" + appid + sep;
        filename = FilenameUtils.separatorsToSystem(filename);
        deleteDirAndSubs(new File(filename));
    }

    public static void clearCacheForChart(int appid, int chartid){
        Logger logger = Logger.getLogger(ClearCache.class);
        String sep="/";
        String filename= InstanceProperties.getAbsolutepathtochartfiles() + sep + "appid-" + appid + sep;
        filename = FilenameUtils.separatorsToSystem(filename);
        logger.debug("clearCacheForApp() filename="+filename);
        //Look for and delete
        lookForDirectoryWithThisNameAndDeleteIt(new File(filename), "chartid-"+chartid);
    }


    private static void lookForDirectoryWithThisNameAndDeleteIt(File startingpath, String dirname){
        Logger logger = Logger.getLogger(ClearCache.class);
        ArrayList<File> subdirs = getAllSubdirectories(startingpath);
        for (Iterator<File> iterator=subdirs.iterator(); iterator.hasNext();) {
            File file=iterator.next();
            boolean shouldwedelete = false;
            String fname = FilenameUtils.normalize(file.getAbsolutePath())+File.separator;
            logger.debug("checking '"+fname+"' for '"+dirname+"'");
            if (fname.indexOf(File.separator+dirname+File.separator)>-1){
                shouldwedelete = true;
                //logger.debug("indexof='"+File.separator+dirname+"' equals dirname="+dirname);
            } else {
                //logger.debug("indexof='"+File.separator+dirname+"' does not equal dirname="+dirname);
            }
//            String[] fnameSpl = fname.split(File.separator);
//            logger.debug("fnameSpl.length="+fnameSpl.length);
//            for (int i=0; i<fnameSpl.length; i++) {
//                String dir =fnameSpl[i];
//                logger.debug("got a dir="+dir);
//                if (dir.equals(dirname)){
//                    logger.debug("dirname="+dirname+" equals dir="+dir);
//                    shouldwedelete = true;
//                } else {
//                    logger.debug("dirname="+dirname+" does not equal dir="+dir);
//                }
//            }
            if (shouldwedelete){
                deleteDirAndSubs(file);
            }
        }
    }

    private static void deleteDirAndSubs(File file){
        Logger logger = Logger.getLogger(ClearCache.class);
        logger.debug("would have DELETED: "+file.getAbsolutePath());
        try{
            FileUtils.forceDelete(file);
        } catch (Exception ex){
            logger.error(ex);
            try{
                FileUtils.forceDeleteOnExit(file);
            } catch (Exception ex2){
                logger.error(ex2);
            }
        }
    }

    private static ArrayList<File> getAllSubdirectories(File in) {
        Logger logger = Logger.getLogger(ClearCache.class);
        logger.debug("getAllFiles() called: "+ in.getAbsolutePath());
        ArrayList<File> out = new ArrayList<File>();
        if (in.isDirectory()) {
            out.add(in);
            //Go looking for children
            String[] children = in.list();
            for (int i=0; i<children.length; i++) {
                out.addAll(getAllSubdirectories(new File(in, children[i])));
            }
        } else {
            //It's a file, not a dir
        }
        return out;
    }



}
