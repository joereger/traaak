package com.fbdblog.htmluibeans;

import org.apache.log4j.Logger;
import com.fbdblog.util.jcaptcha.CaptchaServiceSingleton;

import com.fbdblog.util.Str;
import com.fbdblog.dao.User;
import com.fbdblog.dao.hibernate.HibernateUtil;
import com.fbdblog.email.EmailActivationSend;
import com.fbdblog.email.LostPasswordSend;
import com.fbdblog.htmlui.Pagez;
import com.fbdblog.htmlui.ValidationException;
import com.octo.captcha.service.CaptchaServiceException;

import java.util.List;
import java.util.Iterator;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class LostPassword implements Serializable {

    //Form props
    private String email;
    private String j_captcha_response;
    private String captchaId;

    public LostPassword(){
        
    }

    public void initBean(){

    }

    public void recoverPassword() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
//        boolean isCaptchaCorrect = false;
//        try {
//            isCaptchaCorrect = CaptchaServiceSingleton.getInstance().validateResponseForID(captchaId, j_captcha_response);
//        } catch (CaptchaServiceException e) {
//             //should not happen, may be thrown if the id is not valid
//             logger.error("", e);
//        }
//        if (!isCaptchaCorrect){
//            vex.addValidationError("You failed to correctly type the letters into the box.");
//            throw vex;
//        }

        List<User> users = HibernateUtil.getSession().createQuery("from User where email='"+ Str.cleanForSQL(email)+"'").list();
        if (users.size()>0){
            for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
                User user = iterator.next();
                LostPasswordSend.sendLostPasswordEmail(user);
            }
        } else {
            vex.addValidationError("Email address not found.");
            throw vex;
        }

    }




    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
