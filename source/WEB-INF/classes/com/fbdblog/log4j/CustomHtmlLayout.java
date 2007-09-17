package com.fbdblog.log4j;

import org.apache.log4j.HTMLLayout;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.helpers.Transform;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.LocationInfo;

/**
 * User: Joe Reger Jr
 * Date: Oct 17, 2006
 * Time: 3:33:18 PM
 */
public class CustomHtmlLayout extends HTMLLayout {


    public String format(LoggingEvent event) {
        StringBuffer sbuf = new StringBuffer();
        if(sbuf.capacity() > MAX_CAPACITY) {
          sbuf = new StringBuffer(BUF_SIZE);
        } else {
          sbuf.setLength(0);
        }
        sbuf.append("<table>");

        sbuf.append(Layout.LINE_SEP + "<tr>" + Layout.LINE_SEP);
        sbuf.append("<td class=\"smallfont\">");
        sbuf.append(Transform.escapeTags(event.getLoggerName()));
        sbuf.append("</td>" + Layout.LINE_SEP);
        sbuf.append(Layout.LINE_SEP + "</tr>" + Layout.LINE_SEP);

        if(true) {
          LocationInfo locInfo = event.getLocationInformation();
          sbuf.append(Layout.LINE_SEP + "<tr>" + Layout.LINE_SEP);
          sbuf.append("<td class=\"tinyfont\">");
          sbuf.append(Transform.escapeTags(locInfo.getFileName()));
          sbuf.append(':');
          sbuf.append(locInfo.getLineNumber());
          sbuf.append("</td>" + Layout.LINE_SEP);
          sbuf.append(Layout.LINE_SEP + "</tr>" + Layout.LINE_SEP);
        }
        sbuf.append(Layout.LINE_SEP + "<tr>" + Layout.LINE_SEP);
        sbuf.append("<td class=\"tinyfont\">");
        sbuf.append(Transform.escapeTags(event.getRenderedMessage()));
        sbuf.append("</td>" + Layout.LINE_SEP);
        sbuf.append("</tr>" + Layout.LINE_SEP);

        if (event.getNDC() != null) {
          sbuf.append("<tr><td class=\"tinyfont\">");
          sbuf.append("NDC: " + Transform.escapeTags(event.getNDC()));
          sbuf.append("</td></tr>" + Layout.LINE_SEP);
        }

        String[] s = event.getThrowableStrRep();
        if(s != null) {
          appendThrowableAsHTML(s, sbuf);
        }
        sbuf.append("</table>");
        return sbuf.toString();
      }

      void appendThrowableAsHTML(String[] s, StringBuffer sbuf) {
        if(s != null) {
          int len = s.length;
          if(len == 0){
            return;
          }
          sbuf.append(Transform.escapeTags(s[0]));
          sbuf.append(Layout.LINE_SEP);
          for(int i = 1; i < len; i++) {
            sbuf.append("<tr><td class=\"tinyfont\">");
            sbuf.append(Transform.escapeTags(s[i]));
            sbuf.append(Layout.LINE_SEP);
            sbuf.append("</td></tr>" + Layout.LINE_SEP);
          }
        }
      }


}
