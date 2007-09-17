package com.fbdblog.cache.providers.jboss;

import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.Set;

/**
 * Dumps a class to html
 */
public class CacheDumper {

    public static String getHtml(String fqn, int levelsToDisplay){
        Logger logger = Logger.getLogger(CacheDumper.class);
        try{
            Set childrenNames = JbossTreeCacheAOPProvider.getTreeCache().getChildrenNames(fqn);
            return dumpMap(childrenNames, 0, fqn, levelsToDisplay).toString();
        } catch (Exception ex){
            logger.debug(ex);
            return "Error retrieving cache: " + ex.getMessage();
        }
    }


    private static StringBuffer dumpMap(Set childrenNames, int nestinglevel, String fqnPrepend, int levelsToDisplay){
        Logger logger = Logger.getLogger(CacheDumper.class);
        StringBuffer out = new StringBuffer();
        nestinglevel++;
        if(childrenNames!=null){
            for (Iterator chilIterator = childrenNames.iterator(); chilIterator.hasNext();) {
                Object childName = chilIterator.next();
                String fqnFull = fqnPrepend+"/"+childName;
                if (fqnPrepend.equals("/")){
                    fqnFull = fqnPrepend + childName;
                }
                logger.debug("childName.toString()="+childName.toString()+"<br>fqnFull="+fqnFull);


                out.append("<br>");


                for(int i=0; i<=nestinglevel; i++){
                    out.append("&nbsp;&nbsp;&nbsp;&nbsp;");
                }
                out.append(childName.toString());

                //UserSession Special Output
                try{
                    Object objInCache = (Object)JbossTreeCacheAOPProvider.getTreeCache().get(fqnFull);
                    if (objInCache!=null){
                        Class c = objInCache.getClass();
                        String s = c.getName();
                        logger.debug("c.getName()="+s);

                    } else {
                        logger.debug("No obj in cache with fqnFull="+fqnFull);
                    }
                } catch (Exception e){
                    //Nothing to do, it's not likely a UserSession
                    logger.debug(e);
                }

                try{
                    if (nestinglevel<=levelsToDisplay){
                        Set cNames = JbossTreeCacheAOPProvider.getTreeCache().getChildrenNames(fqnFull);
                        out.append(dumpMap(cNames, nestinglevel, fqnFull, levelsToDisplay));
                    }
                } catch (org.jboss.cache.CacheException cex){
                    logger.debug(cex);
                }

            }
        }
        return out;
    }






}
