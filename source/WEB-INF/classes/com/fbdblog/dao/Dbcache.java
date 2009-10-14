package com.fbdblog.dao;

import com.fbdblog.dao.hibernate.BasePersistentClass;
import com.fbdblog.dao.hibernate.HibernateUtil;
import com.fbdblog.dao.hibernate.AuthControlled;
import com.fbdblog.dao.hibernate.RegerEntity;
import com.fbdblog.util.GeneralException;

import org.apache.log4j.Logger;

import java.util.Date;


public class Dbcache extends BasePersistentClass implements java.io.Serializable, RegerEntity, AuthControlled {


     // Fields
     private int dbcacheid;
     private Date date;
     private Date datelastaccessed;
     private int accesscount;
     private String grp;
     private String keyname;
     private Object val;

    public static Dbcache get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Dbcache");
        try {
            logger.debug("Dbcache.get(" + id + ") called.");
            Dbcache obj = (Dbcache) HibernateUtil.getSession().get(Dbcache.class, id);
            if (obj == null) {
                logger.debug("Dbcache.get(" + id + ") returning new instance because hibernate returned null.");
                return new Dbcache();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Dbcache", ex);
            return new Dbcache();
        }
    }

    //Loader
    public void load(){

    }

    //Validator
    public void validateRegerEntity() throws GeneralException {

    }


    // Constructors

    /** default constructor */
    public Dbcache() {
    }

    public boolean canRead(User user){
        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }




    // Property accessors

    public int getDbcacheid() {
        return dbcacheid;
    }

    public void setDbcacheid(int dbcacheid) {
        this.dbcacheid=dbcacheid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date=date;
    }

    public String getGrp() {
        return grp;
    }

    public void setGrp(String grp) {
        this.grp=grp;
    }

    public String getKeyname() {
        return keyname;
    }

    public void setKeyname(String keyname) {
        this.keyname=keyname;
    }

    public Object getVal() {
        return val;
    }

    public void setVal(Object val) {
        this.val=val;
    }

    public Date getDatelastaccessed() {
        return datelastaccessed;
    }

    public void setDatelastaccessed(Date datelastaccessed) {
        this.datelastaccessed=datelastaccessed;
    }

    public int getAccesscount() {
        return accesscount;
    }

    public void setAccesscount(int accesscount) {
        this.accesscount=accesscount;
    }
}