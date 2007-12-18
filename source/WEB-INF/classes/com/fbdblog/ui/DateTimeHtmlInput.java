package com.fbdblog.ui;

import com.fbdblog.util.Time;
import com.fbdblog.util.ValidationException;
import com.fbdblog.util.Num;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Dec 5, 2007
 * Time: 1:53:41 PM
 */
public class DateTimeHtmlInput {



    public static String getHtml(String name, java.util.Date date, String styleclass, String style){
        return getHtml(name, Time.getCalFromDate(date), styleclass, style);
    }

    public static String getHtml(String name, Calendar cal, String styleclass, String style){
        StringBuffer out = new StringBuffer();

        if (styleclass!=null && !styleclass.equals("")){
            styleclass = "class=\""+styleclass+"\"";
        }
        if (style!=null && !style.equals("")){
            style = "style=\""+style+"\"";
        }

        //Break cal into components
        String date = Time.dateformatfordb(cal);
        int hh=Integer.parseInt(date.substring(11,13));
        int min=Integer.parseInt(date.substring(14,16));
        int mo = Integer.parseInt(date.substring(5,7));
        int day = Integer.parseInt(date.substring(8,10));
        int year = Integer.parseInt(date.substring(0,4));
        String ampm = "PM";
        if (hh>=13){
            ampm = "PM";
            hh=hh-12;
        } else if (hh==12){
            ampm = "PM";
            hh=12;
        } else if (hh==0){
            ampm = "AM";
            hh=12;
        } else {
            ampm = "AM";
            hh=hh;
        }


        out.append("<table cellpadding=2 cellspacing=1 border=0>");
        out.append("<tr>");



        //StartMonth
        out.append("<td align=left valign=top>");
        out.append("<select name='"+name+"mo' "+styleclass+" "+style+">");
        for(int i=1; i<=12; i++){
            out.append("<option value='" + i + "' ");
            if (i==mo) {
                out.append("selected");
            }
            out.append(">" + i + "</option>");
        }
        out.append("</select>");
        out.append("<br><font face=arial size=-2 class=smallfont>Month</font>");
        out.append("</td>");

        //StartDay
        out.append("<td align=left valign=top>");
        out.append("<select name='"+name+"day' "+styleclass+" "+style+">");
        for(int i=1; i<=31; i++){
            out.append("<option value='" + i + "' ");
            if (i==day) {
                out.append("selected");
            }
            out.append(">" + i + "</option>");
        }
        out.append("</select>");
        out.append("<br><font face=arial size=-2 class=smallfont>Day</font>");
        out.append("</td>");

        //StartYear
        out.append("<td align=left valign=top>");
        out.append("<select name='"+name+"year' "+styleclass+" "+style+">");
        for(int i=1900; i<=2020; i++){
            out.append("<option value='" + i + "' ");
            if (i==year) {
                out.append("selected");
            }
            out.append(">" + i + "</option>");
        }
        out.append("</select>");
        out.append("<br><font face=arial size=-2 class=smallfont>Year</font>");
        out.append("</td>");

        //StartHour
        out.append("<td align=left valign=top>");
        out.append("<select name='"+name+"hh' "+styleclass+" "+style+">");
        for(int i=1; i<=12; i++){
            out.append("<option value='" + i + "' ");
            if (i==hh) {
                out.append("selected");
            }
            out.append(">" + i + "</option>");
        }
        out.append("</select>");
        out.append("<br><font face=arial size=-2 class=smallfont>Hour</font>");
        out.append("</td>");

        //StartMin
        out.append("<td align=left valign=top>");
        out.append("<select name='"+name+"min' "+styleclass+" "+style+">");
        for(int i=1; i<=60; i++){
            out.append("<option value='" + i + "' ");
            if (i==min) {
                out.append("selected");
            }
            out.append(">" + i + "</option>");
        }
        out.append("</select>");
        out.append("<br><font face=arial size=-2 class=smallfont>Min</font>");
        out.append("</td>");

        //StartAmpm
        out.append("<td align=left valign=top>");
        out.append("<select name='"+name+"ampm' "+styleclass+" "+style+">");
        out.append("<option value='PM' ");
        if (ampm.equals("PM")) {
            out.append("selected");
        }
        out.append(">PM</option>");
        out.append("<option value='AM' ");
        if (ampm.equals("AM")) {
            out.append("selected");
        }
        out.append(">AM</option>");
        out.append("</select>");
        out.append("<br><font face=arial size=-2 class=smallfont>AM/PM</font>");
        out.append("</td>");

        out.append("</tr>");
        out.append("</table>");

        return out.toString();

    }




    public static Calendar getValueFromRequest(String name, String prettyName, boolean isrequired, HttpServletRequest request) throws ValidationException {
        Logger logger = Logger.getLogger(DateTimeHtmlInput.class);
        int hh=0;
        int min=0;
        int mo =0;
        int day =0;
        int year =0;
        String ampm = "PM";

        if (request.getParameter(name+"mo")!=null && Num.isinteger(request.getParameter(name+"mo"))) {
            mo=Integer.parseInt(request.getParameter(name+"mo"));
        }
        if (request.getParameter(name+"day")!=null && Num.isinteger(request.getParameter(name+"day"))) {
            day=Integer.parseInt(request.getParameter(name+"day"));
        }
        if (request.getParameter(name+"year")!=null && Num.isinteger(request.getParameter(name+"year"))) {
            year=Integer.parseInt(request.getParameter(name+"year"));
        }
        if (request.getParameter(name+"hh")!=null && Num.isinteger(request.getParameter(name+"hh"))) {
            hh=Integer.parseInt(request.getParameter(name+"hh"));
        }
        if (request.getParameter(name+"min")!=null && Num.isinteger(request.getParameter(name+"min"))) {
            min=Integer.parseInt(request.getParameter(name+"min"));
        }
        if (request.getParameter(name+"ampm")!=null) {
            ampm=request.getParameter(name+"ampm");
        }

        try{
            Calendar cal = Time.formtocalendar(year, mo, day, hh, min, 0, ampm);
            return cal;
        } catch (Exception ex){
            logger.error("", ex);
            if (isrequired){
                throw new ValidationException(prettyName+" is not a valid date.");
            } else {
                throw new ValidationException(prettyName+" is not a valid date.");
            }
        }
    }


}
