package com.fbdblog.util;

import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.math.BigDecimal;

/**
 * User: Joe Reger Jr
 * Date: Jul 27, 2006
 * Time: 7:50:21 AM
 */
public class Util {

    public static List setToArrayList(Set set){
        List out = new ArrayList();
        for (Iterator iterator = set.iterator(); iterator.hasNext();) {
            Object o = iterator.next();
            out.add(o);
        }
        return out;
    }

    public static String[] addToStringArray(String[] src, String str){
        if (src==null){
            src=new String[0];
        }

        String[] outArr = new String[src.length+1];
        for(int i=0; i < src.length; i++) {
            outArr[i]=src[i];
        }
        outArr[src.length]=str;
        return outArr;
    }

    public static String getFromStringArraySafely(String[] array, int indextoget){
        Logger logger = Logger.getLogger(Util.class);
        String out = "";
        try{
            if (array!=null && array.length>=(indextoget+1)){
                out = array[indextoget];
            }

        } catch (Exception ex){
            logger.error("",ex);
        }
        return out;
    }



    public static boolean arrayContains(String[] array, String value){
        if (array!=null){
            for (int i = 0; i < array.length; i++) {
                String s = array[i];
                if (s.equals(value)){
                    return true;
                }
            }
        }
        return false;
    }

    public static String[] appendToEndOfStringArray(String[] in, String[] append){
        int outlength = 0;
        if (in!=null){
            outlength = outlength + in.length;
        }
        if (append!=null){
            outlength = outlength + append.length;
        }
        String[] out = new String[outlength];
        int outindex = 0;
        if (in!=null){
            for (int i = 0; i < in.length; i++) {
                out[outindex]=in[i];
                outindex = outindex + 1;
            }
        }
        if (append!=null){
            for (int i = 0; i < append.length; i++) {
                out[outindex]=append[i];
                outindex = outindex + 1;
            }
        }
        return out;
    }


    public static String truncateString(String instring, int maxlength){
        String outstring="";
        try{
            outstring = instring.substring(0,maxlength-1);
        } catch (Exception e) {
            outstring = instring;
        }
        return outstring;
    }

    public static Cookie[] addToCookieArray(Cookie[] src, Cookie str){
        if (src==null){
            src=new Cookie[0];
        }

        Cookie[] outArr = new Cookie[src.length+1];
        for(int i=0; i < src.length; i++) {
            outArr[i]=src[i];
        }
        outArr[src.length]=str;
        return outArr;
    }

    public static int randomInt(int max){
        return (int)(Math.random()*(max+1));
    }

    public static int[] addToIntArray(int[] src, int str){
        //If the source isn't null
        if (src!=null){
            int[] outArr = new int[src.length+1];
            for(int i=0; i < src.length; i++) {
                outArr[i]=src[i];
            }
            outArr[src.length]=str;
            return outArr;
        //If the source is null, create an array, append str and return
        } else {
            int[] outArr = new int[1];
            outArr[0]=str;
            return outArr;
        }
    }

    public static boolean arrayContainsValue(int[] array, int value){
       if (array!=null){
            for (int i = 0; i < array.length; i++) {
                if (array[i]==value){
                    return true;
                }
            }
       }
       return false;
    }

    /**
         * Cleans a string for proper xml presentation
         */
        public static String xmlclean(String instring){
            String xmlclean="";
            if (instring!=null) {
                if (!instring.equals("")) {

                    instring=instring.replaceAll("<", "&lt;");
                    instring=instring.replaceAll(">", "&gt;");
                    instring=instring.replaceAll("&nbsp;", " ");
                    instring=instring.replaceAll("&", "&amp;");
                    instring=instring.replaceAll("’", "'");



                    xmlclean=instring;
                } else {
                    xmlclean=" ";
                }
            } else {
                xmlclean=" ";
            }
            return xmlclean;
        }

        /**
         * Cleans a string to make it work as an xml <fieldname></fieldname>.
         * Removes spaces and any non-alphanumeric chars
         */
        public static String xmlFieldNameClean(String in) {
            String out="";

            Pattern p = Pattern.compile("\\W");
            Matcher m = p.matcher(in);
            out = m.replaceAll("");

            return out;
        }

        public static ArrayList<String> stringArrayToArrayList(String[] in){
            ArrayList<String> out = new ArrayList<String>();
            if (in!=null){
                for (int i=0; i<in.length; i++) {
                    String s=in[i];
                    out.add(s);
                }
            }
            return out;
        }


        public static void logRow(String[] row){
            Logger logger = Logger.getLogger(Util.class);
            StringBuffer out = new StringBuffer();
            for (int j=0; j<row.length; j++) {
                String col=row[j];
                out.append(col + "\t");
            }
            logger.debug(out.toString());
        }

        public static void logXY(String x, String y){
            Logger logger = Logger.getLogger(Util.class);
            StringBuffer out = new StringBuffer();
            out.append("x="+x+" y="+y);
            logger.debug(out.toString());
        }

        public static double doubleRound(double d, int decimalPlace){
            BigDecimal bd = new BigDecimal(Double.toString(d));
            bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
            return bd.doubleValue();
        }



}
