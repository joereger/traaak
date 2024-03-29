package com.fbdblog.dao;

import com.fbdblog.dao.hibernate.BasePersistentClass;
import com.fbdblog.dao.hibernate.RegerEntity;
import com.fbdblog.dao.hibernate.AuthControlled;
import com.fbdblog.dao.hibernate.HibernateUtil;
import com.fbdblog.util.GeneralException;

import java.util.Set;
import java.util.HashSet;

import org.apache.log4j.Logger;

/**
 * Survey generated by hbm2java
 */

public class Chartyaxis extends BasePersistentClass implements java.io.Serializable, RegerEntity, AuthControlled {



     // Fields
     private int chartyaxisid;
     private int chartid;
     private int yquestionid;




    //Validator
    public void validateRegerEntity() throws GeneralException {

    }

    //Loader
    public void load(){

    }

    public static Chartyaxis get(int id) {
        Logger logger = Logger.getLogger("com.fbdblog.dao.Chartyaxis");
        try {
            logger.debug("Chartyaxis.get(" + id + ") called.");
            Chartyaxis obj = (Chartyaxis) HibernateUtil.getSession().get(Chartyaxis.class, id);
            if (obj == null) {
                logger.debug("Chartyaxis.get(" + id + ") returning new instance because hibernate returned null.");
                return new Chartyaxis();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.fbdblog.dao.Chartyaxis", ex);
            return new Chartyaxis();
        }
    }

    // Constructors

    /** default constructor */
    public Chartyaxis() {
    }




    public boolean canRead(User user){
        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }


    public int getChartyaxisid() {
        return chartyaxisid;
    }

    public void setChartyaxisid(int chartyaxisid) {
        this.chartyaxisid = chartyaxisid;
    }

    public int getChartid() {
        return chartid;
    }

    public void setChartid(int chartid) {
        this.chartid = chartid;
    }

    public int getYquestionid() {
        return yquestionid;
    }

    public void setYquestionid(int yquestionid) {
        this.yquestionid = yquestionid;
    }
}
