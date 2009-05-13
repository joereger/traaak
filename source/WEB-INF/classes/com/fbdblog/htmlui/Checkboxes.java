package com.fbdblog.htmlui;

import com.fbdblog.util.Str;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map;

/**
 * User: Joe Reger Jr
 * Date: Oct 28, 2007
 * Time: 2:01:39 AM
 */
public class Checkboxes {

    public static String getHtml(String name, ArrayList<String> values, ArrayList<String> options, String styleclass, String style){
        //This just converts the arraylist into a treemap
        TreeMap<String, String> opts = new TreeMap<String, String>();
        for (Iterator<String> stringIterator=options.iterator(); stringIterator.hasNext();) {
            String s=stringIterator.next();
            opts.put(s, s);
        }
        return getHtml(name, values, opts, styleclass, style);
    }

    public static String getHtml(String name, ArrayList<String> values, TreeMap<String, String> options, String styleclass, String style){
        StringBuffer out = new StringBuffer();
        if (styleclass!=null && !styleclass.equals("")){
            styleclass = "class=\""+styleclass+"\"";
        }
        if (style!=null && !style.equals("")){
            style = "style=\""+style+"\"";
        }
        Iterator keyValuePairs = options.entrySet().iterator();
        for (int i = 0; i < options.size(); i++){
            Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
            String key = (String)mapentry.getKey();
            String val = (String)mapentry.getValue();
            String selected = "";
            if (isSelected(key, values)){
                selected = " checked='true'";
            }
            out.append("<input type=\"checkbox\" name=\""+ Str.cleanForHtml(name) +"\" value=\""+Str.cleanForHtml(key.trim())+"\" style "+selected+"><font "+styleclass+" "+style+">" + val.trim() +"</font>");
            out.append("<br/>");
        }
        return out.toString();

    }

    private static boolean isSelected(String option, ArrayList<String> values){
        for (Iterator<String> iterator = values.iterator(); iterator.hasNext();) {
            String value = iterator.next();
            if (value.equals(option)){
                return true;
            }
        }
        return false;
    }



    public static ArrayList<String> getValueFromRequest(String name, String prettyName, boolean isrequired) throws ValidationException {
        ArrayList<String> out = new ArrayList<String>();
        if (Pagez.getRequest().getParameterValues(name)!=null){
            String[] selectedOptions = Pagez.getRequest().getParameterValues(name);
            for (int i = 0; i < selectedOptions.length; i++) {
                String selectedOption = selectedOptions[i];
                out.add(selectedOption);
            }
        }
        if (isrequired && out.size()==0){
            throw new ValidationException(prettyName+" is required.");
        }
        return out;
    }



}
