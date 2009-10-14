package com.fbdblog.htmluibeans;

import org.apache.log4j.Logger;
import org.apache.commons.validator.EmailValidator;
import org.hibernate.criterion.Restrictions;
import com.fbdblog.dao.*;
import com.fbdblog.dao.hibernate.HibernateUtil;
import com.fbdblog.util.*;
import com.fbdblog.util.jcaptcha.CaptchaServiceSingleton;
import com.fbdblog.htmlui.UserSession;
import com.fbdblog.htmlui.Pagez;
import com.fbdblog.htmlui.ValidationException;
import com.fbdblog.session.PersistentLogin;
import com.fbdblog.email.EmailActivationSend;
import com.fbdblog.xmpp.SendXMPPMessage;
import com.fbdblog.helpers.NicknameHelper;

import javax.servlet.http.Cookie;
import java.util.*;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class Registration implements Serializable {

    //Form props
    private String email;
    private String password;
    private String passwordverify;
    private String firstname;
    private String lastname;
    private String nickname;
    private String j_captcha_response;
    private String captchaId;
    private boolean displaytempresponsesavedmessage;


    //private String temp;

    //Other props
    private int userid;

    public Registration(){

    }


    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        displaytempresponsesavedmessage = false;

    }

    public void registerAction() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("registerAction called:  email="+email+" password="+password+" firstname="+firstname+" lastname="+lastname);

        //Use nickname for first and last name
        firstname = nickname;
        lastname = nickname;

        //Auto-verify password
        passwordverify = password;

        //Validation
        boolean haveErrors = false;

        if (nickname!=null && !nickname.equals("")){
            nickname = Str.onlyKeepLettersAndDigits(nickname);
            nickname =  nickname.toLowerCase();
        } else {
            nickname = "";
        }

        if (nickname==null || nickname.equals("")){
            vex.addValidationError("Nickname can't be blank and can only use letters and numbers.");
            haveErrors = true;
        }

        if (password==null || password.equals("") || password.length()<6){
            vex.addValidationError("Password must be at least six characters long.");
            haveErrors = true;
        }

        if (!password.equals(passwordverify)){
            vex.addValidationError("Password and Verify Password must match.");
            haveErrors = true;
        }

        EmailValidator ev = EmailValidator.getInstance();
        if (!ev.isValid(email)){
            vex.addValidationError("Not a valid email address.");
            haveErrors = true;
        }

        if (firstname==null || firstname.equals("")){
            vex.addValidationError("First Name can't be blank.");
            haveErrors = true;
        }

        if (lastname==null || lastname.equals("")){
            vex.addValidationError("Last Name can't be blank.");
            haveErrors = true;
        }

        //@todo need to check for lcase(firstname), lcase(lastname), email in the database... people are changing caps on name and creating another account.



//        boolean isCaptchaCorrect = false;
//        try {
//            isCaptchaCorrect = CaptchaServiceSingleton.getInstance().validateResponseForID(captchaId, j_captcha_response);
//        } catch (CaptchaServiceException e) {
//             //should not happen, may be thrown if the id is not valid
//        }
//        if (!isCaptchaCorrect){
//            vex.addValidationError("You failed to correctly type the letters into the box.");
//            haveErrors = true;
//        }

        List<User> users = HibernateUtil.getSession().createQuery("from User where email='"+ Str.cleanForSQL(email)+"'").list();
        if (users.size()>0){
            vex.addValidationError("That email address is already in use.");
            haveErrors = true;
        }

        if (NicknameHelper.isNicknameInUse(nickname)){
            vex.addValidationError("That nickname is already in use.");
            haveErrors = true;
        }


        if (haveErrors){
            throw vex;
        }

        //Create the user
        //@todo Use http://www.jasypt.org/ to encrypt password
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setNickname(nickname);
        user.setIsactivatedbyemail(false);
        user.setEmailactivationkey(RandomString.randomAlphanumeric(5));
        user.setEmailactivationlastsent(new Date());
        user.setCreatedate(new Date());
        user.setIsenabled(true);
        user.setFacebookuid("");
        user.setTimezoneid("EST");

        try{
            user.save();
            userid = user.getUserid();
        } catch (GeneralException gex){
            logger.debug("registerAction failed: " + gex.getErrorsAsSingleString());
            throw new ValidationException("An internal server error occurred.  Apologies for the trouble.  Please try again.");
        }




        //Send the activation email
        EmailActivationSend.sendActivationEmail(user);

        //Notify customer care group
        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_CUSTOMERSUPPORT, "New Traaak User: "+ user.getFirstname() + " " + user.getLastname() + "("+user.getEmail()+")");
        xmpp.send();

        //Log user in
        UserSession userSession = new UserSession();
        userSession.setUser(user);
        userSession.setIsloggedin(true);
        //Set persistent login cookie
        Cookie[] cookies = PersistentLogin.getPersistentCookies(user.getUserid(), Pagez.getRequest());
        //Add a cookies to the response
        for (int j = 0; j < cookies.length; j++) {
            Pagez.getResponse().addCookie(cookies[j]);
        }
        //Put userSession object into cache
        Pagez.setUserSessionAndUpdateCache(userSession);

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

    public String getPasswordverify() {
        return passwordverify;
    }

    public void setPasswordverify(String passwordverify) {
        this.passwordverify = passwordverify;
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

    public String getJ_captcha_response() {
        return j_captcha_response;
    }

    public void setJ_captcha_response(String j_captcha_response) {
        this.j_captcha_response = j_captcha_response;
    }


    public String getCaptchaId() {
        return captchaId;
    }

    public void setCaptchaId(String captchaId) {
        this.captchaId=captchaId;
    }




    public boolean getDisplaytempresponsesavedmessage() {
        return displaytempresponsesavedmessage;
    }

    public void setDisplaytempresponsesavedmessage(boolean displaytempresponsesavedmessage) {
        this.displaytempresponsesavedmessage = displaytempresponsesavedmessage;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname=nickname;
    }
}
