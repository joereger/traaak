package com.fbdblog.dao;

import com.fbdblog.dao.hibernate.BasePersistentClass;
import com.fbdblog.dao.hibernate.RegerEntity;
import com.fbdblog.dao.hibernate.AuthControlled;
import com.fbdblog.dao.hibernate.HibernateUtil;
import com.fbdblog.util.GeneralException;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;

import org.apache.log4j.Logger;

/**
 * User generated by hbm2java
 */

public class Impression extends BasePersistentClass implements java.io.Serializable, RegerEntity, AuthControlled {


     private int impressionid;
     private int appid;
     private int year;
     private int month;
     private int day;
     private int impressions;
     private String page;

    //Validator
    public void validateRegerEntity() throws GeneralException {

    }

    //Loader
    public void load(){

    }

    public static Impression get(int id) {
        Logger logger = Logger.getLogger("com.fbdblog.dao.Impression");
        try {
            logger.debug("Impression.get(" + id + ") called.");
            Impression obj = (Impression) HibernateUtil.getSession().get(Impression.class, id);
            if (obj == null) {
                logger.debug("Impression.get(" + id + ") returning new instance because hibernate returned null.");
                return new Impression();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.fbdblog.dao.Impression", ex);
            return new Impression();
        }
    }

    // Constructors

    /** default constructor */
    public Impression() {
    }

    public boolean canRead(User user){
        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }





    // Property accessors


    public int getImpressionid() {
        return impressionid;
    }

    public void setImpressionid(int impressionid) {
        this.impressionid=impressionid;
    }

    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid=appid;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year=year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month=month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day=day;
    }

    public int getImpressions() {
        return impressions;
    }

    public void setImpressions(int impressions) {
        this.impressions=impressions;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page=page;
    }
}
