package com.fbdblog.cache.providers.dbcache;

import java.util.*;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.fbdblog.cache.providers.CacheProvider;
import com.fbdblog.dao.Dbcache;
import com.fbdblog.dao.hibernate.HibernateUtil;


/**
 * Dbcache cache implementation
 * This stores cached items in a separate database as defined in HibernateUtilDbcache.java
 */
public class DbcacheProvider implements CacheProvider {

    public DbcacheProvider(){

    }


    public String getProviderName(){
        return "DbcacheProvider";
    }

    public Object get(String key, String group) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            List<Dbcache> dbcaches = HibernateUtil.getSession().createCriteria(Dbcache.class)
                                               .add(Restrictions.eq("grp", group))
                                               .add(Restrictions.eq("keyname", key))
                                               .setCacheable(true)
                                               .list();
            if (dbcaches!=null && dbcaches.size()>0){
                Dbcache dbcache = dbcaches.get(0);
                dbcache.setDatelastaccessed(new Date());
                dbcache.setAccesscount(dbcache.getAccesscount()+1);
                //@todo performance: implement something like IAOC to cache these updates so that I don't have to call .save() on every request
                dbcache.save();
                return dbcache.getVal();
            }
        } catch (Exception ex){
            logger.error("", ex);
        }
        return null;
    }

    public void put(String key, String group, Object obj) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
//            String val = "";
//            if (obj instanceof String){
//                val = (String)obj;
//            } else {
//                throw new Exception("Dbcache can only store objects of type String");
//            }

            //Delete any existing entries from the same group/key
            List<Dbcache> dbcaches = HibernateUtil.getSession().createCriteria(Dbcache.class)
                                               .add(Restrictions.eq("grp", group))
                                               .add(Restrictions.eq("keyname", key))
                                               .setCacheable(true)
                                               .list();
            if (dbcaches!=null && dbcaches.size()>0){
                for (Iterator<Dbcache> dbcacheIterator=dbcaches.iterator(); dbcacheIterator.hasNext();) {
                    Dbcache dbcache=dbcacheIterator.next();
                    dbcache.delete();
                }
            }

            //Save the value
            Dbcache dbcacheNew = new Dbcache();
            dbcacheNew.setDate(new Date());
            dbcacheNew.setDatelastaccessed(new Date());
            dbcacheNew.setAccesscount(1);
            dbcacheNew.setGrp(group);
            dbcacheNew.setKeyname(key);
            dbcacheNew.setVal(obj);
            dbcacheNew.save();

        } catch (Exception ex){
            logger.error("", ex);
        }
    }

    public void flush() {
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            String emptystring = "";
            HibernateUtil.getSession().createQuery("delete Dbcache d where d.dbcacheid>0"+emptystring).executeUpdate();
        } catch (Exception ex){
            logger.error("", ex);
        }
    }

    public void flush(String group) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            HibernateUtil.getSession().createQuery("delete Dbcache d where d.dbcacheid>0 and d.grp='"+group+"'").executeUpdate();
        } catch (Exception ex){
            logger.error("", ex);
        }
    }

    public void flush(String key, String group) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            HibernateUtil.getSession().createQuery("delete Dbcache d where d.dbcacheid>0 and d.grp='"+group+"' and d.keyname='"+key+"'").executeUpdate();
        } catch (Exception ex){
            logger.error("", ex);
        }
    }

    public String[] getKeys() {
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            throw new Exception("getKeys() not supported yet for Dbcache");
        } catch (Exception ex){
            logger.error("", ex);
        }
        return new String[0];
    }

    public String[] getKeys(String group) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            throw new Exception("getKeys(String group) not supported yet for Dbcache");
        } catch (Exception ex){
            logger.error("", ex);
        }
        return new String[0];
    }

    public String getCacheStatsAsHtml() {
        StringBuffer mb = new StringBuffer();
        mb.append("DbcacheProvider... nothin' to report.<br>");
        return mb.toString();
    }


}