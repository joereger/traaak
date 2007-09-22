package com.fbdblog.util;

/**
 * User: Joe Reger Jr
 * Date: Apr 17, 2006
 * Time: 10:55:41 AM
 */
public class Num {

    public static boolean isinteger(String str){
        if (str==null){
            return false;
        }
        try{
            Integer.parseInt(str);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public static boolean isdouble(String str){
        if (str==null){
            return false;
        }
        try{
            Double.parseDouble(str);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public static long absoluteValue(long num){
        if (num < 0) {
           return -1*num;
       } else {
           return num;
       }
   }

   public static int randomInt(int max){
        return (int)(Math.random()*(max+1));
    }
    



}
