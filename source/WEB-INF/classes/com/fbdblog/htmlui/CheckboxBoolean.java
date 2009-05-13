package com.fbdblog.htmlui;

import com.fbdblog.util.Str;

/**
 * User: Joe Reger Jr
 * Date: Oct 28, 2007
 * Time: 2:01:39 AM
 */
public class CheckboxBoolean {

    public static String getHtml(String name, boolean value, String styleclass, String style){
        StringBuffer out = new StringBuffer();
        if (styleclass!=null && !styleclass.equals("")){
            styleclass = "class=\""+styleclass+"\"";
        }
        if (style!=null && !style.equals("")){
            style = "style=\""+style+"\"";
        }
        String checked = "";
        if (value){
            checked = " checked ";
        }
        out.append("<input type=\"checkbox\" name=\""+ Str.cleanForHtml(name)+"\" value=\"1\" "+checked+" "+styleclass+" "+style+" />");
        return out.toString();
    }

    public static boolean getValueFromRequest(String name) throws ValidationException {
        if (Pagez.getRequest().getParameter(name)!=null && Pagez.getRequest().getParameter(name).equals("1")){
            return true;    
        }
        return false;
    }



}
