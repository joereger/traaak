package com.fbdblog.cache.providers.ehcache;

import com.fbdblog.cache.providers.CacheProvider;
import com.fbdblog.cache.providers.ehcache.KeyGroupRelationship;
import com.fbdblog.systemprops.InstanceProperties;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.TerracottaClientConfiguration;
import net.sf.ehcache.config.TerracottaConfiguration;
import net.sf.ehcache.event.NotificationScope;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * OsCache cacheManager implementation
 */
public class EhcacheProvider implements CacheProvider {

    private static CacheManager cacheManager;
    private static ArrayList<KeyGroupRelationship> groupKeyRelationships = new ArrayList<KeyGroupRelationship>();
    Logger logger = Logger.getLogger(this.getClass().getName());
    private static String CACHENAME = "TRAAAKCACHE";

    public EhcacheProvider(){

    }

    private static void setupCache(){
        Logger logger = Logger.getLogger(EhcacheProvider.class);
        //Initialize the groupKeyRelationships
        groupKeyRelationships = new ArrayList<KeyGroupRelationship>();

        //Load props file, create cacheManager instance
        //URL url = EhcacheProvider.class.getResource("/ehcache-objects.xml");
        //cacheManager = new CacheManager(url);
        //logger.debug("creating new Ehcache CacheManager by loading ehcache-objects.xml");

        //Configure the cache
        CacheConfiguration cacheConfig = new CacheConfiguration(CACHENAME, 30000);
        cacheConfig.timeToIdleSeconds(0);
        cacheConfig.timeToLiveSeconds(0);
        cacheConfig.eternal(true);
        cacheConfig.memoryStoreEvictionPolicy("LRU");
        if (isTerracottaClustered()){
            cacheConfig.terracotta(new TerracottaConfiguration());
        }

        //Use the cache configuration to create the cacheManager
        Configuration managerConfig = new Configuration();
        if (isTerracottaClustered()){
            managerConfig.terracotta(new TerracottaClientConfiguration().url(InstanceProperties.getTerracottahost01()+":9510"));
        }
        CacheConfiguration defaultCacheConfig = new CacheConfiguration("default", 30000);
        defaultCacheConfig.timeToIdleSeconds(0);
        defaultCacheConfig.timeToLiveSeconds(0);
        defaultCacheConfig.eternal(true);
        defaultCacheConfig.memoryStoreEvictionPolicy("LRU");
        if (isTerracottaClustered()){
            defaultCacheConfig.terracotta(new TerracottaConfiguration());
        }

        //Add default and main caches to manager
        managerConfig.defaultCache(defaultCacheConfig);
        managerConfig.cache(cacheConfig);
        cacheManager = new CacheManager(managerConfig);

        //Add the event listener
        Cache cache = cacheManager.getCache(CACHENAME);
        if (cache!=null){
            cache.getCacheEventNotificationService().registerListener(new EhcacheCacheEventListener(), NotificationScope.ALL);
        }

        //List available caches to log
        String[] cachesAvailable = cacheManager.getCacheNames();
        if (cachesAvailable!=null && cachesAvailable.length>0){
            for (int i = 0; i < cachesAvailable.length; i++) {
                String cacheName = cachesAvailable[i];
                logger.debug("Ehcache cache available: "+cacheName);
            }
        } else {
            logger.debug("No caches available... cachesAvailable==null or cachesAvailable.length=0");
        }
    }

    private static Cache getCache(){
        if (cacheManager == null){
            setupCache();
        }
        Cache cache = cacheManager.getCache(CACHENAME);
        return cache;
    }

    public String getProviderName(){
        return "EhcacheProvider";
    }

    public Object get(String key, String group) {
        try {
            return getCache().get("/"+group+"/"+key);
        }  catch (Exception e){
            logger.error("Error getting from cacheManager", e);
        }
        return null;
    }

    public void put(String key, String group, Object obj) {
        try{
            groupKeyRelationships.add(new KeyGroupRelationship("/"+group+"/"+key, group));
            Element el = new Element("/"+group+"/"+key, obj);
            getCache().put(el);
        } catch (Exception ex){
            logger.error("Error putting to cacheManager", ex);
        }
    }

    public void flush() {
        try{
            getCache().flush();
            groupKeyRelationships = new ArrayList<KeyGroupRelationship>();
        } catch (Exception e){
            logger.error("Error flushing", e);
        }
    }

    public void flush(String group) {
        try{

            String[] keys = getKeys(group);
            if (keys!=null && keys.length>0){
                for (int i = 0; i < keys.length; i++) {
                    String key = keys[i];
                    boolean wasRemoved = getCache().remove(key);
                    logger.debug("removing '"+key+"' from cache... wasRemoved="+wasRemoved);
                    removeKeyFromKeyGroupMap(key);
                }
            }
        } catch (Exception e){
            logger.error("Error flushing", e);
        }
    }

    public void flush(String key, String group) {
        try{
            boolean wasRemoved = getCache().remove("/"+group+"/"+key);
            logger.debug("removing '"+key+"' from cache... wasRemoved="+wasRemoved);
            removeKeyFromKeyGroupMap("/"+group+"/"+key);
        } catch (Exception e){
            logger.error("Error flushing", e);
        }
    }

    public String[] getKeys() {
        if (cacheManager !=null){
            ArrayList<String> outList = new ArrayList<String>();
            for (Iterator it = groupKeyRelationships.iterator(); it.hasNext(); ) {
                KeyGroupRelationship kgr = (KeyGroupRelationship)it.next();
                outList.add(kgr.key);
            }
            String[] out = new String[outList.size()];
            int i = 0;
            for (Iterator it = outList.iterator(); it.hasNext(); ) {
                String key = (String)it.next();
                out[i]=key;
                i=i+1;
            }
            return out;
        }
        return new String[0];
    }

    public String[] getKeys(String group) {
        if (cacheManager !=null){
            ArrayList<String> outList = new ArrayList<String>();
            for (Iterator it = groupKeyRelationships.iterator(); it.hasNext(); ) {
                KeyGroupRelationship kgr = (KeyGroupRelationship)it.next();
                if (kgr.group.equals(group)){
                    outList.add(kgr.key);
                }
            }
            String[] out = new String[outList.size()];
            int i = 0;
            for (Iterator it = outList.iterator(); it.hasNext(); ) {
                String key = (String)it.next();
                out[i]=key;
                i=i+1;
            }
            return out;
        }
        return new String[0];
    }

    public static void cleanKeyGroupRelationships(){
        //In clustered mode other nodes will evict elements from the cache.
        //But this instance won't know to remove them from
    }

    public String getCacheStatsAsHtml() {
        StringBuffer mb = new StringBuffer();
        mb.append("EhCacheProvider<br>");
        if (cacheManager !=null){
            String[] keys = getKeys();
            mb.append(keys.length + " keys in cacheManager<br>");
            for (int i = 0; i < keys.length; i++) {
                String key = keys[i];
                mb.append("<br>"+key);
            }
        } else {
            mb.append("Cache is null.");
        }
        return mb.toString();
    }

    protected static void removeKeyFromKeyGroupMap(String key){
        ArrayList<String> outList = new ArrayList<String>();
        for (Iterator it = groupKeyRelationships.iterator(); it.hasNext(); ) {
            KeyGroupRelationship kgr = (KeyGroupRelationship)it.next();
            if (kgr.key.equals(key)){
                it.remove();
            }
        }
    }


    public static boolean isTerracottaClustered(){
        if (InstanceProperties.getTerracottahost01()!=null && !InstanceProperties.getTerracottahost01().equals("")){
            return true;
        }
//        if (InstanceProperties.getTerracottahost02()!=null && !InstanceProperties.getTerracottahost02().equals("")){
//            return true;
//        }
//        if (InstanceProperties.getTerracottahost03()!=null && !InstanceProperties.getTerracottahost03().equals("")){
//            return true;
//        }
        return false;
    }


}