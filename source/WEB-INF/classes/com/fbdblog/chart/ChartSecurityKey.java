package com.fbdblog.chart;

import org.jasypt.util.text.BasicTextEncryptor;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.apache.log4j.Logger;

import java.net.URLEncoder;
import java.net.URLDecoder;

/**
 * User: Joe Reger Jr
 * Date: Dec 10, 2007
 * Time: 2:16:15 PM
 */
public class ChartSecurityKey {

    private static String ENC_PASS = "pupper";

    public static boolean isValidChartKey(String key, int userid, int chartid){
        Logger logger = Logger.getLogger(ChartSecurityKey.class);
        if (key==null){
            return false;
        }
        try{
            //String urldecodedKey = URLDecoder.decode(key, "UTF-8");
            BasicTextEncryptor textEncryptor=new BasicTextEncryptor();
            textEncryptor.setPassword(ENC_PASS);
            String plainTextKey=textEncryptor.decrypt(key);
            if (plainTextKey==null || !plainTextKey.equals(userid+"-"+chartid)){
                return false;
            }
        } catch (EncryptionOperationNotPossibleException enpe){
            logger.error("", enpe);
            return false;
        } catch (Exception ex){
            logger.error("", ex);
            return false;
        }
        return true;
    }

    public static String getChartKey(int userid, int chartid){
        Logger logger = Logger.getLogger(ChartSecurityKey.class);
        String plainTextKey = userid+"-"+chartid;
        BasicTextEncryptor textEncryptor=new BasicTextEncryptor();
        textEncryptor.setPassword(ENC_PASS);
        String encKey = textEncryptor.encrypt(plainTextKey);
        try{
            String urlencodedKey = URLEncoder.encode(encKey, "UTF-8");
            return urlencodedKey;
        } catch (Exception ex){
            logger.error("", ex);
        }
        return "";
    }




}
