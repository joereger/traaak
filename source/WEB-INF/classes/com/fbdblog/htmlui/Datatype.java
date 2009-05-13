package com.fbdblog.htmlui;


/**
 * Created by IntelliJ IDEA.
 * User: Joereger
 * Date: Nov 1, 2007
 * Time: 3:15:02 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Datatype {

    public int getId();
    public String getName();
    public void validate(String in) throws ValidationException;

}
