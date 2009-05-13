package com.fbdblog.htmluibeans;

import org.apache.log4j.Logger;

import com.fbdblog.util.GeneralException;
import com.fbdblog.dao.User;
import com.fbdblog.htmlui.UserSession;
import com.fbdblog.htmlui.Pagez;
import com.fbdblog.htmlui.ValidationException;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class ChangePassword implements Serializable {

    //Form props
    private String password;
    private String passwordverify;

    public ChangePassword(){

    }

    public void initBean(){

    }

    public void saveAction() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (!password.equals(passwordverify)){
            vex.addValidationError("Password and Verify Password must match.");
            throw vex;
        }

        if (password.length()<6){
            vex.addValidationError("Password must be at least six characters long.");
            throw vex;
        }


        UserSession userSession = Pagez.getUserSession();
        if (userSession.getUser()!=null){
            User user = userSession.getUser();
            user.setPassword(password);
            try{
                user.save();
            } catch (GeneralException gex){
                logger.debug("registerAction failed: " + gex.getErrorsAsSingleString());
                vex.addValidationError("Sorry, there was an error.");
                throw vex;
            }
        }
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordverify() {
        return passwordverify;
    }

    public void setPasswordverify(String passwordverify) {
        this.passwordverify = passwordverify;
    }




}
