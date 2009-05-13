package com.fbdblog.dbgrid;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.fbdblog.util.Str;
import com.fbdblog.util.Num;
import com.fbdblog.htmlui.Pagez;

/**
 * User: Joe Reger Jr
 * Date: Nov 7, 2007
 * Time: 12:12:36 PM
 */
public class Grid {

    public static int GRIDCOLRENDERER_STRING= 1;
    public static int GRIDCOLRENDERER_DATETIMECOMPACT= 2;
    public static int GRIDCOLRENDERER_DOUBLEASMONEY= 3;
    public static int GRIDCOLRENDERER_DATETIMEAGOTEXT= 4;
    public static int GRIDCOLRENDERER_DOUBLETWODECIMALPLACES= 5;

    public static String render(List rows, ArrayList<GridCol> cols, int rowsperpage, String pagename, String currentpageVar){
        Logger logger = Logger.getLogger(Grid.class);
        StringBuffer out = new StringBuffer();
        if (rows!=null && cols!=null){
            out.append("<table callpadding=\"2\" cellspacing=\"1\" border=\"0\" width=\"100%\">");
            //To do "proper" paging I'll accept totalrows in the method and then limit/do paging in the bean
            int totalrows = rows.size();
            int totalpages = totalrows/rowsperpage;
            if (totalrows>(totalpages*rowsperpage)){
                totalpages = totalpages + 1;
            }

            //Paging
            int currentpage = 1;
            if (Num.isinteger(Pagez.getRequest().getParameter(currentpageVar))){
                currentpage = Integer.parseInt(Pagez.getRequest().getParameter(currentpageVar));
            }
            int startrow = ((currentpage * rowsperpage) - rowsperpage) + 1;
            int endrow = (currentpage * rowsperpage);



            //Put up headers row
            out.append("<tr>");
            for (Iterator itCol = cols.iterator(); itCol.hasNext(); ) {
                GridCol gridCol = (GridCol)itCol.next();
                String headerStyle = "";
                if (gridCol.getHeaderStyle()!=null && !gridCol.getHeaderStyle().equals("")){
                    headerStyle = " style=\""+gridCol.getHeaderStyle()+"\" ";
                }
                String headerStyleClass = " class=\"gridheader\" ";
                if (gridCol.getHeaderStyleClass()!=null && !gridCol.getHeaderStyleClass().equals("")){
                    headerStyleClass = " class=\""+gridCol.getHeaderStyleClass()+"\" ";
                }
                out.append("<td valign=\"top\" "+headerStyleClass+" "+headerStyle+">");
                out.append(gridCol.getHeader());
                out.append("</td>");
            }
            out.append("</tr>");

            //Iterate rows
            int currentrow = 0;
            for (Iterator it = rows.iterator(); it.hasNext(); ) {
                Object rowObj = (Object)it.next();
                currentrow = currentrow + 1;
                if (currentrow>=startrow && currentrow<=endrow){
                    out.append("<tr>");
                    for (Iterator itCol = cols.iterator(); itCol.hasNext(); ) {
                        GridCol gridCol = (GridCol)itCol.next();
                        String contentStyle = "";
                        if (gridCol.getContentStyle()!=null && !gridCol.getContentStyle().equals("")){
                            contentStyle = " style=\""+gridCol.getContentStyle()+"\" ";
                        }
                        String contentStyleClass = " class=\"gridcontent\" ";
                        if (gridCol.getContentStyleClass()!=null && !gridCol.getContentStyleClass().equals("")){
                            contentStyleClass = " class=\""+gridCol.getContentStyleClass()+"\" ";
                        }
                        String nowrap = "";
                        if (gridCol.getIsnowrap()){
                            nowrap = " nowrap ";
                        }
                        out.append("<td valign=\"top\" "+contentStyleClass+" "+contentStyle+" "+nowrap+">");
                        out.append(processTemplate(gridCol.getContent(), rowObj));
                        out.append("</td>");
                    }
                    out.append("</tr>");
                }
            }

            //Paging display
            if (totalrows>rowsperpage){
                String questionmark = "?";
                if (pagename.indexOf("?")>-1){
                    questionmark = "&";
                }
                out.append("<tr>");
                out.append("<td align=\"right\" colspan=\""+cols.size()+"\" style=\"background: #e6e6e6\">");
                if (currentpage>1){
                    out.append("<a href=\""+pagename+questionmark+currentpageVar+"="+(currentpage-1)+"\">");
                    out.append("Previous");
                    out.append("</a>");
                    out.append(" ");
                }
                out.append("Page "+currentpage+" of "+totalpages);
                if (currentpage<totalpages){
                    out.append(" ");
                    out.append("<a href=\""+pagename+questionmark+currentpageVar+"="+(currentpage+1)+"\">");
                    out.append("Next");
                    out.append("</a>");
                }
                out.append("</td>");
                out.append("</tr>");
            }

            out.append("</table>");
        }
        return out.toString();
    }

    public static String processTemplate(String template, Object rowObj){
        Logger logger = Logger.getLogger(Grid.class);
        StringBuffer out = new StringBuffer();
        if (template!=null && !template.equals("")){
            Pattern p = Pattern.compile("\\<\\$(.|\\n)*?\\$\\>");
            Matcher m = p.matcher(template);
            while(m.find()) {
                String tag = m.group();
                m.appendReplacement(out, Str.cleanForAppendreplacement(findWhatToAppend(tag, rowObj)));
            }
            try{ m.appendTail(out); } catch (Exception e){}
        }
        return out.toString();
    }

    public static String findWhatToAppend(String tag, Object rowObj){
        Logger logger = Logger.getLogger(Grid.class);
        StringBuffer out = new StringBuffer();
        int overrideGridColType = 0;
        String beanprop = tag.substring(2, tag.length()-2);
        //logger.debug("beanprop(pre split)="+beanprop);
        if (beanprop.indexOf("|")>-1){
            String[] split = beanprop.split("\\|");
            if (split.length>=1){
                beanprop = split[0];
            }
            if (split.length>=2){
                if (Num.isinteger(split[1])){
                    overrideGridColType = Integer.parseInt(split[1]);
                }
            }
        }
        //logger.debug("beanprop(after split)="+beanprop);

        try{
            PropertyUtilsBean pub = new PropertyUtilsBean();
            Object colObj = pub.getNestedProperty(rowObj, beanprop);
            GridColRenderer gridColRenderer;
            if (overrideGridColType>0){
                gridColRenderer = GridColRendererFactory.getByID(overrideGridColType);
            } else {
                gridColRenderer = GridColRendererFactory.getByObjectType(colObj);
            }
            String colStr = gridColRenderer.render(colObj);
            out.append(colStr);
        } catch (Exception ex) {logger.error("", ex);}

        return out.toString();
    }



}
