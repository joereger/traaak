package com.fbdblog.dao.hibernate;

import com.fbdblog.util.GeneralException;

/**
 * A standard validation exception.
 */

public class HibValEx extends org.hibernate.classic.ValidationFailure {

    private String[] validationErrors = new String[0];

    public HibValEx(){
        super("");
    }

    public HibValEx(String validationError){
        super(validationError);
        addValidationError(validationError);
    }

    public String getErrorsAsSingleString(){
        StringBuffer mb = new StringBuffer();
        for (int i = 0; i < validationErrors.length; i++) {
            String validationError = validationErrors[i];
            mb.append(validationError + "<br>");
        }
        return mb.toString();
    }

    public void addErrorsFromAnotherGeneralException(HibValEx errors){
        for (int i = 0; i < errors.getErrors().length; i++) {
            addValidationError(errors.getErrors()[i]);
        }
    }

    public String[] getErrors(){
        return validationErrors;
    }

    public void addValidationError(String validationError){
        if (validationErrors==null){
            validationErrors = new String[0];
        }
        validationErrors = com.fbdblog.util.Str.addToStringArray(validationErrors, validationError);
    }

}
