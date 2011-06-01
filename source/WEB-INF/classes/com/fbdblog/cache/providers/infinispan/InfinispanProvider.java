package com.fbdblog.cache.providers.infinispan;

import com.fbdblog.cache.providers.CacheProvider;
import com.fbdblog.systemprops.InstanceProperties;
import org.apache.log4j.Logger;
import org.infinispan.Cache;
import org.infinispan.CacheException;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.tree.Fqn;
import org.infinispan.tree.TreeCache;
import org.infinispan.tree.TreeCacheFactory;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * CacheProvider implementation
 */
public class InfinispanProvider implements CacheProvider {

    private static EmbeddedCacheManager manager;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public InfinispanProvider(){

    }

    private static void setupCache(){
        Logger logger = Logger.getLogger(InfinispanProvider.class);
        logger.debug("setupCache() called");
        try{
            //Note that InvocationBatchingEnabled must be set to TRUE to use TreeCache
            //TreeCache API is used because it allows entire nodes (groups in my scheme) to be evicted at once
            //Note that Infinispan's Tree module must be installed (just add the infinispan-tree.jar file to project)
            if (InstanceProperties.getInfinispanconfigfileobjectcache()!=null && !InstanceProperties.getInfinispanconfigfileobjectcache().equals("")){
                manager = new DefaultCacheManager(InstanceProperties.getInfinispanconfigfileobjectcache());
            } else {
                manager = new DefaultCacheManager("infinispan-cache4objects.xml");
            }

        } catch (Exception ex){
            logger.error("", ex);
        }
    }

    public static EmbeddedCacheManager getManager(){
        if (manager == null){
            setupCache();
        }
        return manager;
    }

    public static Cache getCacheSimple(){
        if (manager == null){
            setupCache();
        }
        return manager.getCache();
    }

    public static TreeCache getCache(){
        if (manager == null){
            setupCache();
        }
        TreeCacheFactory treeCacheFactory = new TreeCacheFactory();
        TreeCache treeCache = treeCacheFactory.createTreeCache(getCacheSimple());
        return treeCache;
    }

    public String getProviderName(){
        return "InfinispanProvider";
    }

     public Object get(String key, String group) {
        try {
            logger.debug("get(key="+key+", group="+group+")");

            if (key==null || key.equals("")){
                if (group==null || group.equals("")){
                    logger.debug("Empty key and group requested");
                    return null;
                }
            }

            TreeCache cache = getCache();
            if (cache!=null){
                Fqn fqn = Fqn.fromString("/"+group);
                Object element = cache.get(fqn, key);
                if (element!=null){
                    return element;
                } else {
                    logger.debug("Element is null");
                }
            } else {
                logger.error("CACHE IS NULL");
            }
        }  catch (Exception e){
            logger.error("Error getting from cacheManager", e);
        }
        return null;
    }

    public void put(String key, String group, Object obj) {
        try{
            Fqn fqn = Fqn.fromString("/" + group);
            getCache().getCache().startBatch();
            getCache().put(fqn, key, obj);
            getCache().getCache().endBatch(true);
        } catch (Exception ex){
            logger.error("Error putting to cacheManager", ex);
        }
    }

    public void flush() {
        try{
            Fqn fqn = Fqn.fromString("/");
            getCache().clearData(fqn);
        } catch (Exception e){
            logger.error("Error flushing", e);
        }
    }

    public void flush(String group) {
        try{
            Fqn fqn = Fqn.fromString("/"+group);
            getCache().clearData(fqn);
        } catch (Exception e){
            logger.error("Error flushing", e);
        }
    }

    public void flush(String key, String group) {
        try{
            Fqn fqn = Fqn.fromString("/"+group);
            getCache().remove(fqn, key);
        } catch (Exception e){
            logger.error("Error flushing", e);
        }
    }

    public String[] getKeys() {
        if (manager !=null){


            try{


//                Cache cache = manager.getCache();
//                Set keys  = cache.keySet();
//                int i = 0;
//                String[] out = new String[keys.size()];
//                for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
//                    Object key = iterator.next();
//                    NodeKey nKey = (NodeKey)key;
//                    String fqn = nKey.getFqn().toString();
//                    String kdsj = nKey.
//                    out[i] = String.valueOf(fqn);
//                    i=i+1;
//                }


                Cache cache = getCacheSimple();
                Set entries = cache.entrySet();
                int i = 0;
                String[] out = new String[entries.size()];
                for (Iterator iterator = entries.iterator(); iterator.hasNext();) {
                    Object entry = iterator.next();
                    out[i] = String.valueOf(entry);
                    i=i+1;
                }

//                Fqn fqn = Fqn.fromString("/");
//                HashSet hm = (HashSet)getCache().getKeys(fqn);
//                String[] out = new String[hm.size()];
//                int i = 0;
//                for (Iterator iterator = hm.iterator(); iterator.hasNext();) {
//                    String s = (String) iterator.next();
//                    out[i] = s;
//                    i=i+1;
//                }


                return out;
            } catch (CacheException ex){
                return new String[0];
            } catch (Exception ex){
                return new String[0];
            }
        }
        return new String[0];
    }

    public String[] getKeys(String group) {
        if (manager !=null){
            try{
                HashSet hm = (HashSet)getCache().getKeys("/"+group);
                String[] out = new String[hm.size()];
                int i = 0;
                for (Iterator iterator = hm.iterator(); iterator.hasNext();) {
                    String s = (String) iterator.next();
                    out[i] = s;
                    i=i+1;
                }
                return out;
            } catch (CacheException ex){
                return new String[0];
            } catch (Exception ex){
                return new String[0];
            }
        }
        return new String[0];
    }


    public String getCacheStatsAsHtml() {
        StringBuffer mb = new StringBuffer();
        mb.append("Infinispan<br>");
        if (manager !=null){
            mb.append("Cache is live.");
        } else {
            mb.append("Cache is null.");
        }
        return mb.toString();
    }







}