package com.fbdblog.htmluibeans;

import org.apache.log4j.Logger;

import com.fbdblog.util.GeneralException;
import com.fbdblog.util.Str;
import com.fbdblog.dao.User;
import com.fbdblog.dao.hibernate.NumFromUniqueResult;
import com.fbdblog.htmlui.UserSession;
import com.fbdblog.htmlui.Pagez;
import com.fbdblog.htmlui.ValidationException;
import com.fbdblog.email.EmailActivationSend;
import com.fbdblog.helpers.UserInputSafe;


import java.io.Serializable;
import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class AccountSettings implements Serializable {

    //Form props
    private String email;
    private String firstname;
    private String lastname;
    private String nickname;

    

    //Other props
    private int userid;

    public AccountSettings(){

    }

    public void initBean(){
        UserSession userSession = Pagez.getUserSession();
        if (userSession.getUser()!=null){
            User user = userSession.getUser();
            email = user.getEmail();
            firstname = user.getFirstname();
            lastname = user.getLastname();
            nickname = user.getNickname();
        }
    }

    public void saveAction() throws ValidationException {
        ValidationException vex = new ValidationException();
        UserSession userSession = Pagez.getUserSession();
        if (userSession.getUser()!=null){
            boolean emailNeedsToBeReactivated = false;
            User user = userSession.getUser();
            if (!user.getEmail().equals(email)){
                int cnt = NumFromUniqueResult.getInt("select count(*) from User where email='"+Str.cleanForSQL(email)+"' and userid<>'"+userSession.getUser().getUserid()+"'");
                if (cnt>0){
                    vex.addValidationError("The email address ("+email+") is already in use and was not added to your account.");
                    email = user.getEmail();
                } else {
                    emailNeedsToBeReactivated = true;
                    user.setEmail(email);
                }
            }

            if (!user.getNickname().equals(nickname)){
                int cnt = NumFromUniqueResult.getInt("select count(*) from User where nickname='"+Str.cleanForSQL(nickname)+"' and userid<>'"+userSession.getUser().getUserid()+"'");
                if (cnt>0){
                    vex.addValidationError("The nickname ("+nickname+") is already in use and was not added to your account.");
                    nickname = user.getNickname();
                } else {
                    user.setNickname(nickname);
                }
            }

            user.setFirstname(firstname);
            user.setLastname(lastname);
            try{
                user.save();
                userid = user.getUserid();
                if (vex.getErrors()!=null && vex.getErrors().length>0){
                    throw vex;
                }
            } catch (GeneralException gex){
                Logger logger = Logger.getLogger(this.getClass().getName());
                logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                return;
            }

            if (emailNeedsToBeReactivated){
                EmailActivationSend.sendActivationEmail(user);
            }
        }
    }

    public TreeMap<String, String> getNotificationFrequencies(){
        TreeMap<String, String> out  = new TreeMap<String, String>();
        out.put("1", "Every Day");
        out.put("7", "Every 7 Days");
        out.put("31", "Every 31 Days");
        out.put("0", "Never");
        return out;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname=nickname;
    }
}
