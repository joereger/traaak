package com.fbdblog.email;

import com.fbdblog.dao.User;
import com.fbdblog.util.RandomString;
import com.fbdblog.util.GeneralException;
import org.apache.log4j.Logger;
import org.apache.commons.mail.HtmlEmail;

import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Jul 12, 2006
 * Time: 2:25:52 PM
 */
public class LostPasswordSend {

    public static void sendLostPasswordEmail(User user){
        Logger logger = Logger.getLogger(LostPasswordSend.class);

        user.setEmailactivationkey(RandomString.randomAlphanumeric(5));
        user.setEmailactivationlastsent(new Date());

        try{
            user.save();
        } catch (GeneralException gex){
            logger.error("registerAction failed: " + gex.getErrorsAsSingleString());
        }

        EmailTemplateProcessor.sendMail("dNeero Password Recovery", "lostpassword", user);

    }

}
