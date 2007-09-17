package com.fbdblog.dao.hibernate;

import com.fbdblog.util.GeneralException;
import org.apache.log4j.Logger;


/**
 * Does the work of validation... is called by wrapper objects
 * that are hibernate event listeners.
 */
public class ValidateWorker {

    public static void validate(Object entity) throws GeneralException {
        Logger logger = Logger.getLogger("com.fbdblog.dao.hibernate.ValidateWorker");
        logger.debug("validate() called: " + entity.getClass().getName());
        try{
            ((RegerEntity)entity).validateRegerEntity();
        } catch (GeneralException vex){
            throw vex;
        }

    }


}
