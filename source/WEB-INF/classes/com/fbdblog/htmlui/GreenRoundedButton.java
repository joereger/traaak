package com.fbdblog.htmlui;

/**
 * User: Joe Reger Jr
 * Date: Nov 9, 2007
 * Time: 6:08:04 PM
 */
public class GreenRoundedButton {

    public static String get(String text){
        StringBuffer mb = new StringBuffer();
        mb.append("<table cellpadding=0 cellspacing=0 width=100% border=0>");
        mb.append("<tr>");
        mb.append("<td valign=\"center\" background='/images/accordion/greenbar-leftcap.gif' align=left width=13>");
        mb.append("<img src='/images/clear.gif' height=41 width=1 border=0>");
        mb.append("</td>");
        mb.append("<td valign=\"center\" background='/images/accordion/greenbar-center.gif' align=center>");
        mb.append("<div style=\"line-height:41px;\">");
        mb.append(text);
        mb.append("</div>");
        mb.append("</td>");
        mb.append("<td valign=center background='/images/accordion/greenbar-rightcap.gif' align=right width=13>");
        mb.append("<img src='/images/clear.gif' height=1 width=1 border=0>");
        mb.append("</td>");
        mb.append("</tr>");
        mb.append("</table>");
        return mb.toString();
    }

}
