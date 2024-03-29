package com.fbdblog.dao;

import com.fbdblog.util.GeneralException;
import com.fbdblog.dao.hibernate.BasePersistentClass;
import com.fbdblog.dao.hibernate.RegerEntity;
import com.fbdblog.dao.hibernate.HibernateUtil;
import com.fbdblog.dao.hibernate.AuthControlled;
import org.apache.log4j.Logger;

import java.util.Set;
import java.util.HashSet;
import java.util.Date;
// Generated Apr 17, 2006 3:45:25 PM by Hibernate Tools 3.1.0.beta4



/**
 * User generated by hbm2java
 */

public class User extends BasePersistentClass implements java.io.Serializable, RegerEntity, AuthControlled {


     private int userid;
     private String facebookuid;
     private boolean isenabled;
     private String firstname;
     private String lastname;
     private String nickname;
     private String email;
     private String password;
     private Date createdate;
     private String timezoneid;
     private boolean isactivatedbyemail;
     private String emailactivationkey;
     private Date emailactivationlastsent;



     //Association
    private Set<Userrole> userroles = new HashSet<Userrole>();

    //Validator
    public void validateRegerEntity() throws GeneralException {

    }

    //Loader
    public void load(){

    }

    public static User get(int id) {
        Logger logger = Logger.getLogger("com.fbdblog.dao.User");
        try {
            logger.debug("User.get(" + id + ") called.");
            User obj = (User) HibernateUtil.getSession().get(User.class, id);
            if (obj == null) {
                logger.debug("User.get(" + id + ") returning new instance because hibernate returned null.");
                return new User();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.fbdblog.dao.User", ex);
            return new User();
        }
    }

    // Constructors

    /** default constructor */
    public User() {
    }

    public boolean canRead(User user){
        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }


    

   
    // Property accessors


    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public boolean getIsenabled() {
        return isenabled;
    }

    public void setIsenabled(boolean isenabled) {
        this.isenabled = isenabled;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }


    public String getFacebookuid() {
        return facebookuid;
    }

    public void setFacebookuid(String facebookuid) {
        this.facebookuid = facebookuid;
    }


    public Set<Userrole> getUserroles() {
        return userroles;
    }

    public void setUserroles(Set<Userrole> userroles) {
        this.userroles = userroles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getTimezoneid() {
        return timezoneid;
    }

    public void setTimezoneid(String timezoneid) {
        this.timezoneid=timezoneid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname=nickname;
    }

    public boolean getIsactivatedbyemail() {
        return isactivatedbyemail;
    }

    public void setIsactivatedbyemail(boolean isactivatedbyemail) {
        this.isactivatedbyemail=isactivatedbyemail;
    }

    public String getEmailactivationkey() {
        return emailactivationkey;
    }

    public void setEmailactivationkey(String emailactivationkey) {
        this.emailactivationkey=emailactivationkey;
    }

    public Date getEmailactivationlastsent() {
        return emailactivationlastsent;
    }

    public void setEmailactivationlastsent(Date emailactivationlastsent) {
        this.emailactivationlastsent=emailactivationlastsent;
    }
}
