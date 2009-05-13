package com.fbdblog.dbgrid;

/**
 * User: Joe Reger Jr
 * Date: Nov 7, 2007
 * Time: 12:16:27 PM
 */
public class GridColRendererString implements GridColRenderer {

    //Format of content is <$propertyname$>
    // or
    // <$propertyname|"+Grid.GRIDCOLRENDERER_DATETIMECOMPACT+"$>

    public String render(Object in) {
        if (in!=null){
            return in.toString();
        }
        return "";
    }

}
