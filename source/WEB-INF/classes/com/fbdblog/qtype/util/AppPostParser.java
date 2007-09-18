package com.fbdblog.qtype.util;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Enumeration;
import java.util.Iterator;

import com.fbdblog.util.Util;


/**
 * User: Joe Reger Jr
 * Date: Nov 22, 2006
 * Time: 12:38:15 AM
 */
public class AppPostParser {


    Logger logger = Logger.getLogger(this.getClass().getName());
    private HashMap nameValuePairs = null;
    public static String FBDBLOG_REQUEST_PARAM_IDENTIFIER = "dblog-rp-";

    public AppPostParser(javax.servlet.http.HttpServletRequest request){
        Logger logger = Logger.getLogger(this.getClass().getName());
        nameValuePairs = new HashMap();
        Enumeration enumer = request.getParameterNames();
        while (enumer.hasMoreElements()) {
            String name = (String)enumer.nextElement();
            String[] values = request.getParameterValues(name);
            //If this is a dneero param
            if (name.indexOf(FBDBLOG_REQUEST_PARAM_IDENTIFIER)>-1){
                //Trim all values and hold in valuesTmp
                String[] valuesTmp = new String[values.length];
                for (int i = 0; i < values.length; i++) {
                    String s = values[i];
                    valuesTmp[i] = s.trim();
                }
                //Put valuesTmp into the nameValuePairs
                nameValuePairs.put(name, valuesTmp);
            }
        }
    }

    public AppPostParser(String allValuesAsQueryString){
        nameValuePairs = new HashMap();
        String[] split = allValuesAsQueryString.split("&");
        for (int i = 0; i < split.length; i++) {
            try{
                if(split[i].split("=").length>=2){
                    //Note the trim because I believe some browsers add different chars after input fields
                    String name = java.net.URLDecoder.decode(split[i].split("=")[0], "UTF-8").trim();
                    String value = java.net.URLDecoder.decode(split[i].split("=")[1], "UTF-8").trim();
                    if (nameValuePairs.containsKey(name)){
                        String[] currentvalue = (String[])nameValuePairs.get(name);
                        nameValuePairs.put(name, Util.addToStringArray(currentvalue, value.trim()));
                    } else {
                        nameValuePairs.put(name, new String[]{value});
                    }
                }
            } catch (Exception ex){
                logger.error(ex);
            }
        }
    }

    public String getAsString(){
        StringBuffer qs = new StringBuffer();
        if (nameValuePairs!=null){
            for (Iterator i=nameValuePairs.entrySet().iterator(); i.hasNext(); ) {
                Map.Entry e = (Map.Entry) i.next();
                logger.debug(e.getKey() + ": " + e.getValue());
                String name = (String)e.getKey();
                String[] values = (String[])e.getValue();
                logger.debug("name="+name+"+++++++++++");
                for (int j = 0; j < values.length; j++) {
                    logger.debug("j="+j);
                    logger.debug("values["+j+"]="+values[j]);
                    try{
                        qs.append(java.net.URLEncoder.encode(name, "UTF-8")+"="+java.net.URLEncoder.encode(values[j], "UTF-8"));
                        qs.append("&");
                    } catch (Exception ex){
                        logger.error(ex);
                    }
                }
            }
        }
        String out = qs.toString();
        if (out.length()>=2){
            if (out.substring(out.length()-1, out.length()).equals("&")){
                out = out.substring(0, out.length()-1);
            }
        }
        logger.debug("out="+out);
        return out;
    }


    public HashMap getNameValuePairs() {
        return nameValuePairs;
    }


    public String[] getParamsForQuestion(int questionid){
        String[] out = new String[0];
        Iterator keyValuePairs = nameValuePairs.entrySet().iterator();
        for (int i = 0; i < nameValuePairs.size(); i++){
            Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
            String name = (String)mapentry.getKey();
            String[] values = (String[])mapentry.getValue();
            if (name.indexOf("questionid_"+questionid)>-1){
                //Trim all values and hold in valuesTmp
                String[] valuesTmp = new String[values.length];
                for (int j = 0; j < values.length; j++) {
                    String s = values[j];
                    valuesTmp[j] = s.trim();
                }
                //Add valuesTmp
                out = Util.appendToEndOfStringArray(out, valuesTmp);
            }
        }
        return out;
    }



}
