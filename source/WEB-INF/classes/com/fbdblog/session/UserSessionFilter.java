package com.fbdblog.session;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import com.fbdblog.dao.hibernate.HibernateUtil;

/**
 * User: Joe Reger Jr
 * Date: Jul 18, 2006
 * Time: 9:50:38 AM
 */
public class UserSessionFilter implements Filter {

    private FilterConfig filterConfig = null;
    Logger logger = Logger.getLogger(this.getClass().getName());
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void destroy() {
        this.filterConfig = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {


        try{
            HttpServletRequest httpServletRequest = (HttpServletRequest)request;
            if (httpServletRequest.getRequestURL().indexOf("jpg")==-1 && httpServletRequest.getRequestURL().indexOf("css")==-1 && httpServletRequest.getRequestURL().indexOf("gif")==-1 && httpServletRequest.getRequestURL().indexOf("png")==-1){
                logger.debug("Start UserSessionCreator");
                UserSessionSetup.setup((HttpServletRequest)request, response);
                logger.debug("End UserSessionCreator");
            }
        } catch (Exception ex){
            logger.debug("Error setting up UserSession");
            logger.error("",ex);
        }

        if (!response.isCommitted()){
            chain.doFilter(request, response);
        }

    }

}
