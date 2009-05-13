package com.fbdblog.htmlui;

/**
 * Created by IntelliJ IDEA.
 * User: Joereger
 * Date: Nov 1, 2007
 * Time: 3:23:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class DatatypeFactory {

    public static Datatype getById(int datatypeid){
        if (datatypeid==1){
            return new DatatypeString();
        } else if (datatypeid==2){
            return new DatatypeDouble();
        } else if (datatypeid==3){
            return new DatatypeInteger();
        }
        return null;
    }

}
