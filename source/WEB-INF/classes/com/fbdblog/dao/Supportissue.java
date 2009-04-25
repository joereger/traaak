package com.fbdblog.dao;

import com.fbdblog.dao.hibernate.BasePersistentClass;
import com.fbdblog.dao.hibernate.RegerEntity;
import com.fbdblog.dao.hibernate.AuthControlled;
import com.fbdblog.dao.hibernate.HibernateUtil;
import com.fbdblog.util.GeneralException;

import java.util.Set;
import java.util.HashSet;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * Survey generated by hbm2java
 */

public class Supportissue extends BasePersistentClass implements java.io.Serializable, RegerEntity, AuthControlled {

     public static int STATUS_NEW = 0;
     public static int STATUS_REPLYWAITING = 1;
     public static int STATUS_CLOSED = 2;

     public static int TYPE_UNDEFINED = 0;
     public static int TYPE_BUG = 1;
     public static int TYPE_NEWAPPIDEA = 2;
     public static int TYPE_FEATUREREQUEST = 3;

     // Fields
     private int supportissueid;
     private int userid;
     private int appid;
     private Date datetime;
     private Date mostrecentupdateat;
     private int status;
     private int type;
     private String subject;
     private Set<Supportissuecomm> supportissuecomms = new HashSet<Supportissuecomm>();



    //Validator
    public void validateRegerEntity() throws GeneralException {

    }

    //Loader
    public void load(){

    }

    public static Supportissue get(int id) {
        Logger logger = Logger.getLogger("com.fbdblog.dao.Supportissue");
        try {
            logger.debug("Supportissue.get(" + id + ") called.");
            Supportissue obj = (Supportissue) HibernateUtil.getSession().get(Supportissue.class, id);
            if (obj == null) {
                logger.debug("Supportissue.get(" + id + ") returning new instance because hibernate returned null.");
                return new Supportissue();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.fbdblog.dao.Supportissue", ex);
            return new Supportissue();
        }
    }

    // Constructors

    /** default constructor */
    public Supportissue() {
    }




    public boolean canRead(User user){
        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }







    // Property accessors


    public int getSupportissueid() {
        return supportissueid;
    }

    public void setSupportissueid(int supportissueid) {
        this.supportissueid=supportissueid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid=userid;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime=datetime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status=status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject=subject;
    }

    public Set<Supportissuecomm> getSupportissuecomms() {
        return supportissuecomms;
    }

    public void setSupportissuecomms(Set<Supportissuecomm> supportissuecomms) {
        this.supportissuecomms=supportissuecomms;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type=type;
    }

    public Date getMostrecentupdateat() {
        return mostrecentupdateat;
    }

    public void setMostrecentupdateat(Date mostrecentupdateat) {
        this.mostrecentupdateat=mostrecentupdateat;
    }

    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid=appid;
    }
}