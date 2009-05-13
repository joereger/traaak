package com.fbdblog.htmlui;

import com.fbdblog.util.Str;

/**
 * User: Joe Reger Jr
 * Date: Oct 28, 2007
 * Time: 2:01:39 AM
 */
public class Textarea {

    public static String getHtml(String name, String value, int rows, int cols, String styleclass, String style){
        StringBuffer out = new StringBuffer();
        if (styleclass!=null && !styleclass.equals("")){
            styleclass = "class=\""+styleclass+"\"";
        }
        if (style!=null && !style.equals("")){
            style = "style=\""+style+"\"";
        }
        if (value==null){
            value="";
        }
        out.append("<textarea name=\""+Str.cleanForHtml(name)+"\" "+styleclass+" "+style+" rows=\""+rows+"\" cols=\""+cols+"\" wrap=\"soft\">"+ value+"</textarea>");

        return out.toString();
    }

    public static String getValueFromRequest(String name, String prettyName, boolean isrequired) throws ValidationException {
        if (Pagez.getRequest().getParameter(name)!=null && !Pagez.getRequest().getParameter(name).equals("")){
            return Pagez.getRequest().getParameter(name);
        }
        if (isrequired){
            throw new ValidationException(prettyName+" is required.");
        }
        return "";
    }



}
