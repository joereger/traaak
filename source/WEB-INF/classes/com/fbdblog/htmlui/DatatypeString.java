package com.fbdblog.htmlui;

/**
 * Created by IntelliJ IDEA.
 * User: Joereger
 * Date: Nov 1, 2007
 * Time: 3:16:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class DatatypeString implements Datatype {

    public static int DATATYPEID = 1;

    public int getId() {
        return DATATYPEID;
    }

    public String getName() {
        return "String";
    }

    public void validate(String in) throws ValidationException {
        if (in==null){
           throw new ValidationException("Null");
        }
    }
}
