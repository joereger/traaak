package com.fbdblog.chart;

import com.fbdblog.systemprops.BaseUrl;
import com.fbdblog.htmlui.Pagez;
import com.fbdblog.dao.User;
import com.fbdblog.dao.Chart;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2009
 * Time: 5:51:46 PM
 */
public class FlashChartWidget {

    public static String getEmbedCode(Chart chart, User user, int width, int height){
        StringBuffer out = new StringBuffer();
        String key = ChartSecurityKey.getChartKey(user.getUserid(), chart.getChartid());
        out.append(
                "<div style=\"width:"+width+"px;\">\n"+
                "<object classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000\"\n" +
                "                    codebase=\"http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0\"\n" +
                "                    width=\""+width+"\"\n" +
                "                    height=\""+height+"\" id=\"graph-2\" align=\"middle\">\n" +
                "                <param name=\"allowScriptAccess\" value=\"sameDomain\" />\n" +
                "                <param name=\"movie\" value=\"/ofc/traaak.swf\" />\n" +
                "                <param name=\"quality\" value=\"high\" />\n" +
                "                <param name=\"FlashVars\" value=\"data-file="+ BaseUrl.get(false)+"jsondata/"+chart.getChartid()+"/"+ user.getUserid()+"/"+key+"\" />\n" +
                "                <embed src=\"/ofc/traaak.swf\"\n" +
                "                       quality=\"high\"\n" +
                "                       bgcolor=\"#FFFFFF\"\n" +
                "                       width=\""+width+"\"\n" +
                "                       height=\""+height+"\"\n" +
                "                       name=\"open-flash-chart\"\n" +
                "                       align=\"middle\"\n" +
                "                       allowScriptAccess=\"sameDomain\"\n" +
                "                       type=\"application/x-shockwave-flash\"\n" +
                "                       flashvars = \"data-file="+ BaseUrl.get(false)+"jsondata/"+chart.getChartid()+"/"+ user.getUserid()+"/"+key+"\"\n" +
                "                       pluginspage=\"http://www.macromedia.com/go/getflashplayer\" />\n" +
                "            </object>\n"+
                "<br/><center><a href=\""+BaseUrl.get(false)+"\"><font style=\"font-size: 9px;\">powered by traaak</font></center></a>" +
                "</div>\n"
                );

        return out.toString();
    }


}
