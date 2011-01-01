package com.fbdblog.cache.providers;

import com.fbdblog.cache.providers.ehcache.EhcacheProvider;
import com.fbdblog.cache.providers.jboss.JbossTreeCacheAOPProvider;
import com.fbdblog.cache.providers.oscache.OsCacheProvider;
import com.fbdblog.cache.providers.oscache.OsCacheClusteredProvider;
import com.fbdblog.cache.providers.dbcache.DbcacheProvider;

/**
 * Factory class to get a cache provider
 */
public class CacheFactory {

    public static CacheProvider getCacheProvider(){
        return getCacheProvider("EhcacheProvider");
    }

    public static CacheProvider getCacheProvider(String providername){
        if (providername.equals("EhcacheProvider")){
            return new EhcacheProvider();
        } else if (providername.equals("JbossTreeCacheAOPProvider")){
            return new JbossTreeCacheAOPProvider();
        } else if (providername.equals("OsCacheProvider")){
            return new OsCacheProvider();
        } else if (providername.equals("OsCacheClusteredProvider")){
            return new OsCacheClusteredProvider();
        } else if (providername.equals("DbcacheProvider")){
            return new DbcacheProvider();
        } else {
            return getCacheProvider();
        }
    }

}
