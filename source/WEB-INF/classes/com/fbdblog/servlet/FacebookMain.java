package com.fbdblog.servlet;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import java.io.IOException;


/**
 * User: Joe Reger Jr
 * Date: Sep 18, 2007
 * Time: 9:51:36 AM
 */
public class FacebookMain extends HttpServlet {


    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("doPost called");

        if (request.getParameter("nav")!=null && request.getParameter("nav").equals("main")){
            request.getRequestDispatcher("/fb/index.jsp").forward(request, response);
            return;
        }

        if (request.getParameter("nav")!=null && request.getParameter("nav").equals("history")){
            request.getRequestDispatcher("/fb/history.jsp").forward(request, response);
            return;
        }

        if (request.getParameter("nav")!=null && request.getParameter("nav").equals("charts")){
            request.getRequestDispatcher("/fb/charts.jsp").forward(request, response);
            return;
        }

        if (request.getParameter("nav")!=null && request.getParameter("nav").equals("friends")){
            request.getRequestDispatcher("/fb/friends.jsp").forward(request, response);
            return;
        }

        if (request.getParameter("fb_sig_uninstall")!=null && request.getParameter("fb_sig_uninstall").equals("1")){
            request.getRequestDispatcher("/fb/uninstall.jsp").forward(request, response);
            return;
        }
        
        request.getRequestDispatcher("/fb/index.jsp").forward(request, response);
    }



}
