package com.fbdblog.qtype.def;

import com.fbdblog.util.Str;

/**
 * A standard validation exception.
 */

public class ComponentException extends Exception {

    private String[] validationErrors = new String[0];

    public ComponentException(){

    }

    public ComponentException(String validationError){
        addValidationError(validationError);
    }

    public String getErrorsAsSingleString(){
        StringBuffer mb = new StringBuffer();
        for (int i = 0; i < validationErrors.length; i++) {
            String validationError = validationErrors[i];
            mb.append(validationError + " ");
        }
        return mb.toString();
    }

    public void addErrorsFromAnotherGeneralException(ComponentException errors){
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
        validationErrors = Str.addToStringArray(validationErrors, validationError);
    }

}
