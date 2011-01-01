package com.fbdblog.cache.providers.ehcache;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: joereger
 * Date: Dec 26, 2010
 * Time: 1:13:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class EhcacheCacheEventListener implements CacheEventListener {


    public void notifyElementRemoved(Ehcache ehcache, Element element) throws CacheException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("notifyElementRemoved("+ehcache.getName()+", "+element.getKey()+")");
        EhcacheProvider.removeKeyFromKeyGroupMap(String.valueOf(element.getKey()));
    }

    public void notifyElementPut(Ehcache ehcache, Element element) throws CacheException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("notifyElementPut("+ehcache.getName()+", "+element.getKey()+")");
    }

    public void notifyElementUpdated(Ehcache ehcache, Element element) throws CacheException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("notifyElementUpdated("+ehcache.getName()+", "+element.getKey()+")");
    }

    public void notifyElementExpired(Ehcache ehcache, Element element) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("notifyElementExpired("+ehcache.getName()+", "+element.getKey()+")");
        EhcacheProvider.removeKeyFromKeyGroupMap(String.valueOf(element.getKey()));
    }

    public void notifyElementEvicted(Ehcache ehcache, Element element) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("notifyElementEvicted("+ehcache.getName()+", "+element.getKey()+")");
        EhcacheProvider.removeKeyFromKeyGroupMap(String.valueOf(element.getKey()));
    }

    public void notifyRemoveAll(Ehcache ehcache) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("notifyRemoveAll("+ehcache.getName()+")");
    }

    public void dispose() {

    }

    public Object clone() throws CloneNotSupportedException {
        return null;  
    }
}
