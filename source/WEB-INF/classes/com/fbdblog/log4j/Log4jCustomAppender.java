package com.fbdblog.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ErrorCode;

import java.util.Calendar;

import com.fbdblog.db.Db;
import com.fbdblog.util.Str;
import com.fbdblog.util.Time;
import com.fbdblog.startup.ApplicationStartup;
import com.fbdblog.xmpp.SendXMPPMessage;

/**
 * User: Joe Reger Jr
 * Date: Oct 17, 2006
 * Time: 11:34:38 AM
 */
public class Log4jCustomAppender extends AppenderSkeleton {

    public boolean requiresLayout(){
        return true;
    }

    public synchronized void append( LoggingEvent event ){
        StringBuffer errorMessage = new StringBuffer();
        StringBuffer errorMessageAsHtml = new StringBuffer();
        if( this.layout == null ){
            errorHandler.error("No layout for appender " + name , null, ErrorCode.MISSING_LAYOUT );
            return;
        }
        //Get main message
        errorMessage.append(this.layout.format(event));
        CustomHtmlLayout htmlLayout = new CustomHtmlLayout();
        errorMessageAsHtml.append(htmlLayout.format(event));
        //If layout doesn't handle throwables
        if( layout.ignoresThrowable() ){
            String[] messages = event.getThrowableStrRep();
            if( messages != null ){
                for( int j = 0; j < messages.length; ++j ){
                    errorMessage.append(messages[j]);
                    errorMessage.append( '\n' );
                }
            }
        }

        if (shouldRecordThis(errorMessageAsHtml.toString())){
            //Write to database
            if (ApplicationStartup.getIsappstarted()){
                if (event.getLevel()==Level.ERROR || event.getLevel()==Level.FATAL){
                    try{
                        //-----------------------------------
                        //-----------------------------------
                        int identity = Db.RunSQLInsert("INSERT INTO error(error, level, status, date) VALUES('"+Str.cleanForSQL(errorMessageAsHtml.toString())+"', '"+event.getLevel().toInt()+"', '"+com.fbdblog.dao.Error.STATUS_NEW+"', '"+ Time.dateformatfordb(Calendar.getInstance())+"')", false);
                        //-----------------------------------
                        //-----------------------------------
                    } catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
            //XMPP (Instant Messages)
            if (event.getLevel()==Level.ERROR || event.getLevel()==Level.FATAL){
                SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SYSADMINS, Str.truncateString(errorMessage.toString(), 300));
                xmpp.send();
            }
        }

    }
    
    public synchronized void close(){

    }

    //Allows me to filter out annoying framework-based errors that can't be fixed otherwise
    public boolean shouldRecordThis(String err){
        if (err.indexOf("org.apache.myfaces.shared_tomahawk.renderkit.html.HtmlTableRendererBase")>-1){
            return false;
        }
        if (err.indexOf("org.apache.myfaces.renderkit.html.util.MyFacesResourceLoader")>-1 && err.indexOf("Unparsable")>-1){
            return false;
        }
        return true;
    }



}
