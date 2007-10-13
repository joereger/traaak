package com.fbdblog.session;

import org.apache.log4j.Logger;

import java.util.*;

public class UrlSplitter {

    private String rawIncomingServername = "";
    private int port = 80;
    private String scheme = "http://";
    private String requestUrl = "";
    private String querystring = "";
    private String method = "";
    private String servletPath = "";
    private ArrayList<String> servernameAllPossibleDomains=new ArrayList<String>();
    private javax.servlet.http.HttpServletRequest request;



    public UrlSplitter(javax.servlet.http.HttpServletRequest request){
        Logger logger = Logger.getLogger(this.getClass().getName());
        this.request = request;
        logger.debug(">>UrlSplitter Start<<");

        //Get the host
        rawIncomingServername = request.getServerName();
        logger.debug("rawIncomingServername=" + rawIncomingServername);

        //Sequentially rip off subdomains from the servername
        String tmpServername = rawIncomingServername;
        servernameAllPossibleDomains.add(rawIncomingServername);

        //See if we have any subdomains
        while (tmpServername.indexOf(".")>-1 && tmpServername.split("\\.").length>=3){
            //Grab what's to the right of the dot
            tmpServername = tmpServername.substring(tmpServername.indexOf(".")+1, tmpServername.length());
            //Add it to the array
            servernameAllPossibleDomains.add(tmpServername);
        }

        //Url
        requestUrl = request.getRequestURL().toString();
        logger.debug("requestUrl=" + requestUrl);

        //Qs
        querystring = request.getQueryString();
        logger.debug("querystring=" + querystring);

        //Debug
        querystring = getParametersAsQueryStringNoQuestionMark();
        logger.debug("querystring=" + querystring);

        //Set the port
        port = request.getServerPort();

        //Set the protocol
        scheme = request.getScheme();
        logger.debug("scheme=" + scheme);

        //Method
        method = request.getMethod();
        logger.debug("method=" + method);

        //Pathinfo
        servletPath = request.getServletPath();
        logger.debug("servletPath=" + servletPath);
       

        logger.debug(">>UrlSplitter End<<");
    }

    public String getReconstructedGetVersionOfRequest(){
        return request.getRequestURL().toString() + getParametersAsQueryStringQuestionMarkIfRequired();
    }

    public String getParametersAsQueryStringQuestionMarkIfRequired(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("getParametersAsQueryStringQuestionMarkIfRequired() start");
        String getqs = getParametersAsQueryStringNoQuestionMark();
        String qs = "";
        if (!getqs.equals("")){
            qs = "?" + getqs;
        }
        logger.debug("getParametersAsQueryStringQuestionMarkIfRequired() end");
        return qs;
    }

    public String getParametersAsQueryStringNoQuestionMark(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("getParametersAsQueryStringNoQuestionMark() start");
        if (request.getMethod().equals("GET")){
            String qs = "";
            if (querystring!=null && !querystring.equals("")){
                qs = querystring;
            }
            logger.debug("getParametersAsQueryStringNoQuestionMark() end");
            return qs;
        } else if (request.getMethod().equals("POST")){
            String qs = "";

//            while (request.getParameterNames().hasMoreElements()) {
//                String name = (String) request.getParameterNames().nextElement();
//                logger.debug("name="+name);
//                String[] values = request.getParameterValues(name);
//                for (int i = 0; i < values.length; i++) {
//                    String value = values[i];
//                    logger.debug("value="+value);
//                    String namevalue = name + "=" + value;
//                    qs = qs + namevalue;
//                }
//            }


            for(Enumeration e=request.getParameterNames(); e.hasMoreElements(); ){
               String name = (String)e.nextElement();
               logger.debug("name="+name);
               String[] values = request.getParameterValues(name);
               for (int i = 0; i < values.length; i++) {
                    String value = values[i];
                    logger.debug("value="+value);
                    String namevalue = name + "=" + value;
                    qs = qs + "&" + namevalue;
                }
            }


            logger.debug("getParametersAsQueryStringNoQuestionMark() end");
            return qs;
        }
        logger.debug("getParametersAsQueryStringNoQuestionMark() end");
        return "";
    }

    public String getUrlSplitterAsString(){
        return rawIncomingServername;
    }


    public String getRequestUrl() {
        return requestUrl;
    }

    public int getPort() {
        return port;
    }

    public String getSiterooturlPlusPortNum(){
        String portStr="";
        if (port!=80 && port!=443){
            portStr = ":"+port;
        }
        return rawIncomingServername+portStr;

    }


    public String getQuerystring() {
        return querystring;
    }

    public String getMethod() {
        return method;
    }

    public String getServletPath() {
        return servletPath;
    }

    public String getRawIncomingServername() {
        return rawIncomingServername;
    }

    public ArrayList<String> getServernameAllPossibleDomains() {
        return servernameAllPossibleDomains;
    }

    public String getScheme(){
        return scheme;
    }

}

