package com.fbdblog.helpers;

import com.fbdblog.util.Str;

/**
 * User: Joe Reger Jr
 * Date: Jun 4, 2007
 * Time: 9:06:39 AM
 */
public class UserInputSafe {

    public static String clean(String in){
        if (in==null){
            return null;
        }
        String out = in;
        //out = cleanHtml(out);
        out = cleanJavascript(out);
        out = Str.cleanForSQL(out);
        return out;
    }

    public static String cleanJavascript(String in){
        if (in==null){
            return null;
        }
        //String out = in.replaceAll("<script", "&lt;script");


//        String out = in;
//        out = out.replaceAll("javascript:", "java script:");
//        out = out.replaceAll("<script", "&ltscr ipt");
//        out = out.replaceAll("< script", "&ltscr ipt");
//        out = out.replaceAll("<  script", "&ltscr ipt");
//        out = out.replaceAll("<   script", "&ltscr ipt");
//        out = out.replaceAll("eval((.*))", "");


        String out = in;
        out = out.replaceAll("eval\\((.*)\\)", "");
        out = out.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        out = out.replaceAll("script", "");
        
        return out;
    }

    public static String cleanHtml(String in){
        if (in==null){
            return null;
        }
        String out = in;
        out = out.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        return out;
    }




}
