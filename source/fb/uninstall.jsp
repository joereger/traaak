<%@ page import="org.hibernate.criterion.Restrictions" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fbdblog.dao.hibernate.HibernateUtil" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.fbdblog.util.Num" %>
<%@ page import="com.fbdblog.dao.App" %>
<%@ page import="com.fbdblog.facebook.FindApp" %>
<%@ page import="com.fbdblog.xmpp.SendXMPPMessage" %>
<%@ page import="com.fbdblog.dao.User" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.fbdblog.util.UserInputSafe" %>
<%@ page import="com.fbdblog.dao.Userappactivity" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Calendar" %>
<%
    //@todo somehow validate that this is a good request from the actual servers... don't want script kiddies uninstalling (not that it does any more than setting a flag)
    Logger logger=Logger.getLogger(this.getClass().getName());
    logger.debug("a facebook app was uninstalled");
    logger.debug("fb_sig_user=" + request.getParameter("fb_sig_user"));
    if (request.getParameter("fb_sig_user") != null && Num.isinteger(request.getParameter("fb_sig_user"))) {
        //Figure out which app is being uninstalled
        App app=null;
        app=FindApp.findFromRequest(request);
        //If we have an app
        if (app != null && app.getAppid()>0) {
            List users=HibernateUtil.getSession().createCriteria(User.class)
                    .add(Restrictions.eq("facebookuid", UserInputSafe.clean(request.getParameter("fb_sig_user"))))
                    .setCacheable(false)
                    .list();
            for (Iterator iterator=users.iterator(); iterator.hasNext();) {
                User user=(User) iterator.next();

                Calendar cal=Calendar.getInstance();

                Userappactivity userappactivity=new Userappactivity();
                userappactivity.setAppid(app.getAppid());
                userappactivity.setUserid(user.getUserid());
                userappactivity.setDate(new Date());
                userappactivity.setYear(cal.get(Calendar.YEAR));
                userappactivity.setMonth(cal.get(Calendar.MONTH) + 1);
                userappactivity.setIsinstall(false);
                userappactivity.setIsuninstall(true);

                try {
                    userappactivity.save();
                } catch (Exception ex) {
                    logger.error(ex);
                }
                SendXMPPMessage xmpp=new SendXMPPMessage(SendXMPPMessage.GROUP_CUSTOMERSUPPORT, "Uninstalled Facebook App by " + user.getFirstname() + " " + user.getLastname());
                xmpp.send();
                logger.debug("user noted as app removed userid=" + user.getUserid());
            }
        } else {
            SendXMPPMessage xmpp=new SendXMPPMessage(SendXMPPMessage.GROUP_CUSTOMERSUPPORT, "Uninstalled Facebook App but not sure which.");
            xmpp.send();
        }
    }
%>