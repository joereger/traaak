package com.fbdblog.htmlui;

import com.fbdblog.util.Str;
import com.fbdblog.util.Num;


/**
 * User: Joe Reger Jr
 * Date: Oct 28, 2007
 * Time: 2:01:39 AM
 */
public class Textbox {

    public static String getHtml(String name, String value, int maxlength, int size, String styleclass, String style){
        StringBuffer out = new StringBuffer();
        if (styleclass!=null && !styleclass.equals("")){
            styleclass = "class=\""+styleclass+"\"";
        }
        if (style!=null && !style.equals("")){
            style = "style=\""+style+"\"";
        }
        out.append("<input type=\"text\" name=\""+Str.cleanForHtml(name)+"\" value=\""+ Str.cleanForHtml(value)+"\" size=\""+size+"\" maxlength=\""+maxlength+"\" "+styleclass+" "+style+" />");
        return out.toString();
    }

    public static String getValueFromRequest(String name, String prettyName, boolean isrequired, int datatypeid) throws ValidationException {
        if (Pagez.getRequest().getParameter(name)!=null && !Pagez.getRequest().getParameter(name).equals("")){
            Datatype dt = DatatypeFactory.getById(datatypeid);
            if (dt!=null){
                dt.validate(Pagez.getRequest().getParameter(name));
            } else {
                throw new ValidationException("Application error... invalid datatypeid.");
            }
            //If it passes, send it on along
            return Pagez.getRequest().getParameter(name);
        }
        if (isrequired){
            throw new ValidationException(prettyName+" is required.");
        }
        return "";
    }

    public static int getIntFromRequest(String name, String prettyName, boolean isrequired, int datatypeid) throws ValidationException {
        String str = getValueFromRequest(name, prettyName, isrequired, datatypeid);
        if (Num.isinteger(str)){
            return Integer.parseInt(str);
        } else {
            if (Num.isdouble(str)){
                Double dbl = Double.parseDouble(str);
                return dbl.intValue();       
            }
        }
        return 0;
    }
    
    public static double getDblFromRequest(String name, String prettyName, boolean isrequired, int datatypeid) throws ValidationException {
        String str = getValueFromRequest(name, prettyName, isrequired, datatypeid);
        if (Num.isdouble(str)){
            Double dbl = Double.parseDouble(str);
            return dbl;
        }
        return 0;
    }



}
