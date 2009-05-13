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
public class DropdownMultiselect {

    //In options<String, String> the first is the id value... second is displayed in the dropdown
    public static String getHtml(String name, ArrayList<String> values, TreeMap<String, String> options, int size, String styleclass, String style){
        StringBuffer out = new StringBuffer();

        if (styleclass!=null && !styleclass.equals("")){
            styleclass = "class=\""+styleclass+"\"";
        }
        if (style!=null && !style.equals("")){
            style = "style=\""+style+"\"";
        }
        if (size==0){
            size=1;    
        }

        out.append("<select name=\""+ Str.cleanForHtml(name)+"\" multiple size=\""+size+"\">");
        out.append("<option value=\"\"></option>");
        Iterator keyValuePairs = options.entrySet().iterator();
        for (int i = 0; i < options.size(); i++){
            Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
            String key = (String)mapentry.getKey();
            String val = (String)mapentry.getValue();
            String selected = "";
            if (isSelected(key, values)){
                selected = " selected";
            }
            out.append("<option value=\""+Str.cleanForHtml(key.trim())+"\" "+selected+">" + Str.truncateString(val.trim(), 30) + "</option>");
        }
        out.append("</select>");

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
