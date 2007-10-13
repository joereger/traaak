package com.fbdblog.facebook;

import com.fbdblog.dao.App;
import com.fbdblog.dao.hibernate.HibernateUtil;
import com.fbdblog.session.UrlSplitter;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Sep 17, 2007
 * Time: 12:33:56 PM
 */
public class FindApp {

    public static App findFromRequest(HttpServletRequest request){
        Logger logger = Logger.getLogger(FindApp.class);
        //Parse /app/appname/ to find app name
        String requestUrl = request.getRequestURL().toString();
        logger.debug("requestUrl="+requestUrl);
        if (requestUrl!=null && requestUrl.indexOf("/app/")>-1){
            String[] split = requestUrl.split("/");
            if (split!=null && split.length>=5){
                String appname = split[4];
                logger.debug("parsed appname="+appname);
                App app = findFromAppName(appname.trim());
                if (app!=null && app.getAppid()>0){
                    logger.debug("app found via request.getRequestURL()");
                    return app;
                }
            }
        }
        //Look to the fb_sig_api_key (which doesn't appear on first app install)
        logger.debug("no app found via request.getRequestURL() so looking to request.getParameter(\"fb_sig_api_key\")="+request.getParameter("fb_sig_api_key"));
        if (request.getParameter("fb_sig_api_key") != null && !request.getParameter("fb_sig_api_key").equals("")) {
            App app = findFromApiKey(request.getParameter("fb_sig_api_key").trim());
            if (app!=null && app.getAppid()>0){
                logger.debug("app found via fb_sig_api_key");
                return app;
            }
        }
        //Last resort, check to see if the postaddappname is being passed
        logger.debug("no api_key found so looking to request.getParameter(\"postaddappname\")="+request.getParameter("postaddappname"));
        if (request.getParameter("postaddappname") != null && !request.getParameter("postaddappname").equals("")) {
            App app = findFromAppName(request.getParameter("postaddappname").trim());
            if (app!=null && app.getAppid()>0){
                logger.debug("app found via postaddappname");
                return app;
            }
        }
        //If all fails, return null
        logger.debug("app not found, returning null");
        return null;
    }

    public static App findFromAppName(String facebookappname){
        Logger logger = Logger.getLogger(FindApp.class);
        logger.debug("looking for app with facebookappname="+facebookappname);
        List<App> apps = HibernateUtil.getSession().createCriteria(App.class)
                                           .add(Restrictions.eq("facebookappname", facebookappname.trim()))
                                           .setCacheable(true)
                                           .list();
        if (apps!=null && apps.size()>1){
            logger.error("More than one app for facebookappname="+facebookappname);
        }
        for (Iterator<App> iterator = apps.iterator(); iterator.hasNext();) {
            App app = iterator.next();
            logger.debug("returning appid="+app.getAppid()+" for facebookappname="+facebookappname);
            return app;
        }
        logger.debug("returning null app for facebookappname="+facebookappname);
        return null;
    }



    private static App findFromApiKey(String api_key){
        Logger logger = Logger.getLogger(FindApp.class);
        logger.debug("looking for app with api_key="+api_key);
        List<App> apps = HibernateUtil.getSession().createCriteria(App.class)
                                           .add(Restrictions.eq("facebookapikey", api_key))
                                           .setCacheable(true)
                                           .list();
        if (apps!=null && apps.size()>1){
            logger.error("More than one app for facebookapikey="+api_key);
        }
        for (Iterator<App> iterator = apps.iterator(); iterator.hasNext();) {
            App app = iterator.next();
            logger.debug("returning appid="+app.getAppid()+" for facebookapikey="+api_key);
            return app;
        }
        logger.debug("returning null app for facebookapikey="+api_key);
        return null;
    }




}
