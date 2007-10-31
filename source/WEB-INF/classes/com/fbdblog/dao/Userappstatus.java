package com.fbdblog.dao;

import com.fbdblog.dao.hibernate.BasePersistentClass;
import com.fbdblog.dao.hibernate.RegerEntity;
import com.fbdblog.dao.hibernate.AuthControlled;
import com.fbdblog.dao.hibernate.HibernateUtil;
import com.fbdblog.util.GeneralException;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * User generated by hbm2java
 */

public class Userappstatus extends BasePersistentClass implements java.io.Serializable, RegerEntity, AuthControlled {

     private int userappstatusid;
     private int userid;
     private int appid;
     private boolean isinstalled;

    //Validator
    public void validateRegerEntity() throws GeneralException {

    }

    //Loader
    public void load(){

    }

    public static Userappstatus get(int id) {
        Logger logger = Logger.getLogger("com.fbdblog.dao.Userappstatus");
        try {
            logger.debug("Userappstatus.get(" + id + ") called.");
            Userappstatus obj = (Userappstatus) HibernateUtil.getSession().get(Userappstatus.class, id);
            if (obj == null) {
                logger.debug("Userappstatus.get(" + id + ") returning new instance because hibernate returned null.");
                return new Userappstatus();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.fbdblog.dao.Userappstatus", ex);
            return new Userappstatus();
        }
    }

    // Constructors

    /** default constructor */
    public Userappstatus() {
    }

    public boolean canRead(User user){
        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }





    // Property accessors


    public int getUserappstatusid() {
        return userappstatusid;
    }

    public void setUserappstatusid(int userappstatusid) {
        this.userappstatusid=userappstatusid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid=userid;
    }

    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid=appid;
    }

    public boolean getIsinstalled() {
        return isinstalled;
    }

    public void setIsinstalled(boolean isinstalled) {
        this.isinstalled=isinstalled;
    }
}