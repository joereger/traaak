package com.fbdblog.htmlui;

import com.fbdblog.util.Str;


import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Nov 9, 2007
 * Time: 6:10:50 PM
 */
public class PercentCompleteBar {

    public static String get(String currentvalue, String maximumvalue, String mintitle, String maxtitle, String widthinpixels){
        return PercentCompleteBar.get(currentvalue, maximumvalue, mintitle, maxtitle, widthinpixels, 10, true);
    }

    public static String get(String currentvalue, String maximumvalue, String mintitle, String maxtitle, String widthinpixels, int height, boolean shownumbersbelow){
        Logger logger = Logger.getLogger(PercentCompleteBar.class);
        StringBuffer mb = new StringBuffer();

        if (currentvalue==null || currentvalue.equals("null")){
            currentvalue = "0";
        }
        if (maximumvalue==null || maximumvalue.equals("null")){
            maximumvalue = "100";
        }
        if (mintitle==null || mintitle.equals("null")){
            mintitle = "";
        }
        if (maxtitle==null || maxtitle.equals("null")){
            maxtitle = "";
        }
        if (widthinpixels==null || widthinpixels.equals("null")){
            widthinpixels = "300";
        }

        double currentvalueNum = .0000001;
        try{ currentvalueNum = Double.parseDouble(currentvalue);} catch (Exception ex){logger.error("",ex);}

        double maxvalueNum = .00000001;
        try{ maxvalueNum = Double.parseDouble(maximumvalue);} catch (Exception ex){logger.error("",ex);}

        int widthinpixelsNum = 0;
        try{ widthinpixelsNum = Integer.parseInt(widthinpixels);} catch (Exception ex){logger.error("",ex);}

        double percentcomplete = (currentvalueNum/maxvalueNum)*100;
        String percentcompleteStr = Str.formatNoDecimals(percentcomplete);

        double leftwidthinpixels = (currentvalueNum/maxvalueNum)*Double.parseDouble(String.valueOf(widthinpixelsNum));
        double rightwidthinpixels = widthinpixelsNum - leftwidthinpixels;

        if (leftwidthinpixels>widthinpixelsNum){
            leftwidthinpixels = widthinpixelsNum;
            rightwidthinpixels = 0;
        } else if (rightwidthinpixels>widthinpixelsNum){
            leftwidthinpixels = 0;
            rightwidthinpixels = widthinpixelsNum;
        }


        mb.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\""+widthinpixels+"\">\n" +
                "<tr>\n" +
                "<td colspan=\"3\" nowrap>\n" +
                "<img src=\"/images/bar_green-blend.gif\" width=\""+leftwidthinpixels+"\" height=\""+height+"\" border=\"0\"><img src=\"/images/bar_ltgrey-blend.gif\" width=\""+rightwidthinpixels+"\" height=\""+height+"\" border=\"0\">\n" +
                "</td>\n" +
                "</tr>\n");
        if (shownumbersbelow){
            mb.append("<tr>\n" +
                "<td valign=\"top\" align=\"left\">\n" +
                "<font class=\"tinyfont\">"+mintitle+"</font>\n" +
                "</td>\n" +
                "<td valign=\"top\" align=\"center\">\n" +
                "<font class=\"tinyfont\">"+currentvalue+" of "+maximumvalue+" ("+percentcompleteStr+"%)</font>\n" +
                "</td>\n" +
                "<td valign=\"top\" align=\"right\">\n" +
                "<font class=\"tinyfont\">"+maxtitle+"</font>\n" +
                "</td>\n" +
                "</tr>\n");
        }
        mb.append("</table>");

         return mb.toString();
    }

}
