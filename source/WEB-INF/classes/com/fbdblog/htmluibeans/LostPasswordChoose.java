package com.fbdblog.htmluibeans;

import org.apache.log4j.Logger;
import com.fbdblog.util.jcaptcha.CaptchaServiceSingleton;

import com.fbdblog.util.GeneralException;
import com.fbdblog.util.Num;
import com.fbdblog.dao.User;
import com.fbdblog.email.LostPasswordSend;
import com.fbdblog.htmlui.Pagez;
import com.fbdblog.htmlui.ValidationException;
import com.octo.captcha.service.CaptchaServiceException;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class LostPasswordChoose implements Serializable {

    //Form props
    private String password;
    private String passwordverify;
    private String j_captcha_response;
    private String u;
    private String k;
    private String captchaId;

    public LostPasswordChoose(){

    }

    public void initBean(){
        checkKey();
    }

    public void checkKey() {
        Logger logger = Logger.getLogger(this.getClass().getName());

        User user=null;
        if (Pagez.getRequest().getParameter("u")!=null && Num.isinteger(Pagez.getRequest().getParameter("u"))){
            user = User.get(Integer.parseInt(Pagez.getRequest().getParameter("u")));
        }

        String k = "";
        if (Pagez.getRequest().getParameter("k")!=null && !Pagez.getRequest().getParameter("k").equals("")){
            k = Pagez.getRequest().getParameter("k");
        }

        logger.debug("user.getUserid()="+user.getUserid()+" k="+k);

        if (user!=null && user.getEmailactivationkey()!=null && k!=null && user.getEmailactivationkey().trim().equals(k.trim())){
            user.setIsactivatedbyemail(true);
            try{
                user.save();
            } catch (GeneralException gex){
                logger.error("registerAction failed: " + gex.getErrorsAsSingleString());
            }

            //Set the flag in the session that'll allow this user to reset their password
            if (Pagez.getUserSession()!=null){
                Pagez.getUserSession().setAllowedToResetPasswordBecauseHasValidatedByEmail(true);
                Pagez.getUserSession().setUser(user);
                Pagez.getUserSession().setIsloggedin(false);
                return;
            } else {
                //Pagez.sendRedirect("/lostpassword.jsp");
                return;
            }
        } else {
            //Pagez.sendRedirect("/lostpassword.jsp");
            return;
        }
    }

    public void choosePassword() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());

        boolean haveErrors = false;

        if (password==null || password.equals("") || password.length()<6){
            vex.addValidationError("Password must be at least six characters long.");
            haveErrors = true;
        }

        if (!password.equals(passwordverify)){
            vex.addValidationError("Password and Verify Password must match.");
            haveErrors = true;
        }

        boolean isCaptchaCorrect = false;
        try {
            isCaptchaCorrect = CaptchaServiceSingleton.getInstance().validateResponseForID(captchaId, j_captcha_response);
        } catch (CaptchaServiceException e) {
             //should not happen, may be thrown if the id is not valid
             logger.error("", e);
        }
        if (!isCaptchaCorrect){
            Pagez.getUserSession().setMessage("You failed to correctly type the letters into the box.");
            haveErrors = true;
        }

        
        if (haveErrors){
            throw vex;
        }

        //Checks the key and userid again
        checkKey();


        if (Pagez.getUserSession().getIsAllowedToResetPasswordBecauseHasValidatedByEmail()){

            User user = Pagez.getUserSession().getUser();
            user.setPassword(password);

            try{
                user.save();
            } catch (GeneralException gex){
                logger.error("registerAction failed: " + gex.getErrorsAsSingleString());
            }

            Pagez.getUserSession().setIsloggedin(true);
        } else {
            vex.addValidationError("Sorry, it doesn't appear that you came to this page from a valid email link.");
            throw vex;
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


    public String getJ_captcha_response() {
        return j_captcha_response;
    }

    public void setJ_captcha_response(String j_captcha_response) {
        this.j_captcha_response = j_captcha_response;
    }

    public String getU() {
        return u;
    }

    public void setU(String u) {
        this.u=u;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k=k;
    }

    public String getCaptchaId() {
        return captchaId;
    }

    public void setCaptchaId(String captchaId) {
        this.captchaId=captchaId;
    }
}
