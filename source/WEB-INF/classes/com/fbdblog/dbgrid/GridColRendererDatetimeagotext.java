package com.fbdblog.dbgrid;

import com.fbdblog.util.Time;

import java.util.Calendar;
import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Nov 7, 2007
 * Time: 12:16:27 PM
 */
public class GridColRendererDatetimeagotext implements GridColRenderer {

    //Format of content is <$propertyname$>
    // or
    // <$propertyname|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>

    public String render(Object in) {
        if (in!=null){
            if (in instanceof Calendar){
                return Time.agoText((Calendar)in);
            } else if (in instanceof Date){
                return Time.agoText(Time.getCalFromDate((Date)in));
            }
        }
        return "";
    }

}
