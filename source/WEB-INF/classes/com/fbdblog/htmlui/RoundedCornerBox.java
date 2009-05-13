package com.fbdblog.htmlui;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Nov 9, 2007
 * Time: 6:12:39 PM
 */
public class RoundedCornerBox {

    public static String get(String body, String uniqueboxname, String widthinpercentString, String widthinpixelsString, String titlecolor, String subtitlecolor, String bodycolor, String bordercolor, String title, String subtitle){
        Logger logger = Logger.getLogger(RoundedCornerBox.class);
        StringBuffer mb = new StringBuffer();

        if (uniqueboxname==null){
            uniqueboxname = "thunder";
        }
        if (widthinpercentString==null){
            widthinpercentString = "100";
        }
        if (widthinpixelsString==null){
            widthinpercentString = "200";
        }
        if (titlecolor==null){
            titlecolor = "000000";
        }
        if (subtitlecolor==null){
            subtitlecolor = "000000";
        }
        if (bodycolor==null){
            bodycolor = "cccccc";
        }
        if (bordercolor==null){
            bordercolor = "999999";
        }
        if (title==null){
            title = "";
        }
        if (subtitle==null){
            subtitle = "";
        }


        String x = uniqueboxname;
        String width = "width:100%";
        if (com.fbdblog.util.Num.isinteger(widthinpercentString) && Integer.parseInt(widthinpercentString)>0){
             width = "width:"+Integer.parseInt(widthinpercentString)+"%";
        }
        if (com.fbdblog.util.Num.isinteger(widthinpixelsString) && Integer.parseInt(widthinpixelsString)>0){
             width = "width:"+Integer.parseInt(widthinpixelsString)+"px";
        }

        logger.debug("width="+width+" widthinpercentString="+widthinpercentString+ " widthinpixelsString="+widthinpixelsString);

        mb.append("<!-- Start Rounded Corner Box -->\n" +
                "<style type=\"text/css\">\n" +
                "#xsnazzy"+x+" h1, #xsnazzy"+x+" h2, #xsnazzy"+x+" p {margin:0 10px; letter-spacing:1px;}\n" +
                "#xsnazzy"+x+" h1 {font-size:2.5em; color:#"+titlecolor+";}\n" +
                "#xsnazzy"+x+" h2 {font-size:2em;color:#"+subtitlecolor+"; border:0;}\n" +
                "#xsnazzy"+x+" p {padding-bottom:0.5em;}\n" +
                "#xsnazzy"+x+" h2 {padding-top:0.5em;}\n" +
                "#xsnazzy"+x+" {background: transparent; margin:1em; "+width+"}\n" +
                ".xtop"+x+", .xbottom"+x+" {display:block; background:transparent; font-size:1px;}\n" +
                ".xb1"+x+", .xb2"+x+", .xb3"+x+", .xb4"+x+" {display:block; overflow:hidden;}\n" +
                ".xb1"+x+", .xb2"+x+", .xb3"+x+" {height:1px;}\n" +
                ".xb2"+x+", .xb3"+x+", .xb4"+x+" {background:#"+bodycolor+"; border-left:1px solid #"+bordercolor+"; border-right:1px solid #"+bordercolor+";}\n" +
                ".xb1"+x+" {margin:0 5px; background:#"+bordercolor+";}\n" +
                ".xb2"+x+" {margin:0 3px; border-width:0 2px;}\n" +
                ".xb3"+x+" {margin:0 2px;}\n" +
                ".xb4"+x+" {height:2px; margin:0 1px;}\n" +
                ".xboxcontent"+x+" {display:block; background:#"+bodycolor+"; border:0 solid #"+bordercolor+"; border-width:0 1px;}\n" +
                "</style>\n" +
                "<div id=\"xsnazzy"+x+"\" >\n" +
                "<b class=\"xtop"+x+"\"><b class=\"xb1"+x+"\"></b><b class=\"xb2"+x+"\"></b><b class=\"xb3"+x+"\"></b><b class=\"xb4"+x+"\"></b></b>\n" +
                "<div class=\"xboxcontent"+x+"\">\n");
         if(title!=null && !title.equals("")){
            mb.append("<h1>"+title+"</h1>\n");
         }
         if(subtitle!=null && !subtitle.equals("")){
            mb.append("<h2>"+subtitle+"</h2>\n");
         }
         mb.append("<p>");
         mb.append(body);
         mb.append("</p>\n" +
                "</div>\n" +
                "<b class=\"xbottom\"><b class=\"xb4"+x+"\"></b><b class=\"xb3"+x+"\"></b><b class=\"xb2"+x+"\"></b><b class=\"xb1"+x+"\"></b></b>\n" +
                "</div>\n" +
                "<!-- End Rounded Corner Box -->");

          return mb.toString();
    }

}
