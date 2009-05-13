package com.fbdblog.htmlui;

import com.fbdblog.util.Num;

/**
 * Created by IntelliJ IDEA.
 * User: Joereger
 * Date: Nov 1, 2007
 * Time: 3:16:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class DatatypeInteger implements Datatype {

    public static int DATATYPEID = 3;

    public int getId() {
        return DATATYPEID;
    }

    public String getName() {
        return "Integer";
    }

    public void validate(String in) throws ValidationException {
        if (!Num.isinteger(in)){
           throw new ValidationException("Not an Integer.");
        }
    }
}
