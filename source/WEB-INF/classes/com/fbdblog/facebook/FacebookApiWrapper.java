package com.fbdblog.facebook;

import com.facebook.api.FacebookRestClient;
import com.facebook.api.TemplatizedAction;
import com.fbdblog.session.UserSession;
import com.fbdblog.systemprops.SystemProperty;
import com.fbdblog.systemprops.BaseUrl;
import com.fbdblog.dao.Post;
import com.fbdblog.dao.User;
import com.fbdblog.dao.App;
import com.fbdblog.dao.Throwdown;
import com.fbdblog.dao.hibernate.HibernateUtil;
import com.fbdblog.util.Num;
import com.fbdblog.util.Str;
import com.fbdblog.util.Time;
import com.fbdblog.throwdown.ThrowdownStatus;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.jdom.input.DOMBuilder;
import org.w3c.dom.Document;
import org.hibernate.criterion.Restrictions;

import java.util.*;
import java.net.URL;

/**
 * User: Joe Reger Jr
 * Date: Jul 16, 2007
 * Time: 10:41:57 AM
 */
public class FacebookApiWrapper {


    private String sessionkey;
    private App app;
    private User user;



    public FacebookApiWrapper(UserSession userSession){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (userSession.getFacebooksessionkey()!=null && !userSession.getFacebooksessionkey().trim().equals("")){
            sessionkey = userSession.getFacebooksessionkey();
        }
        if (userSession.getUser()!=null){
            user = userSession.getUser();
        }
        if (userSession.getApp()!=null){
            app = userSession.getApp();
        }
    }

    public FacebookApiWrapper(App app, User user, String sessionkey) {
        this.app = app;
        this.user = user;
        this.sessionkey = sessionkey;
    }

    public FacebookApiWrapper(App app, User user) {
        this.app = app;
        this.user = user;
        this.sessionkey = app.getFacebookinfinitesessionkey();
    }

//    public FacebookApiWrapper(UserSession userSession){
//        Logger logger = Logger.getLogger(this.getClass().getName());
//        this.userSession = userSession;
//        if (userSession.getFacebooksessionkey()!=null && !userSession.getFacebooksessionkey().trim().equals("")){
//            try{
//                FacebookRestClient facebookRestClient = new FacebookRestClient(userSession.getApp().getFacebookapikey(), userSession.getApp().getFacebookapisecret(), userSession.getFacebooksessionkey());
//                if (userSession.getUser()!=null && userSession.getUser().getUserid()>0){
//                    if (userSession.getUser().getFacebookuid()!=null){
//                        logger.debug("userSession.getUser().getFacebookuid()="+userSession.getUser().getFacebookuid()+" facebookRestClient.users_getLoggedInUser()="+facebookRestClient.users_getLoggedInUser());
//                        if (userSession.getUser().getFacebookuid().trim().equals(String.valueOf(facebookRestClient.users_getLoggedInUser()))){
//                            issessionok = true;
//                        } else {
//                            logger.debug("userSession.getUser().getFacebookuserid()!=facebookRestClient.users_getLoggedInUser()");
//                        }
//                    } else {
//                        logger.debug("userSession.getUser() (userid="+userSession.getUser().getUserid()+") passed to FacebookApiWrapper does not have a saved facebookuserid");
//                    }
//                } else {
//                    if (userSession.getFacebookUser()!=null && userSession.getFacebookUser().getUid().length()>0){
//                        issessionok = true;
//                    } else {
//                        logger.debug("don't have a facebookuserid to work with");
//                    }
//                }
//            } catch (Exception ex){
//                logger.error("",ex);
//            }
//        }
//    }

    public String sendNotification(ArrayList<Long> recipientIds, String notification, String email){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            if (app!=null && !app.getMinifeedtemplate().equals("")){
                FacebookRestClient facebookRestClient = new FacebookRestClient(app.getFacebookapikey(), app.getFacebookapisecret(), sessionkey);
                URL url = facebookRestClient.notifications_send(recipientIds, Str.getCharSequence(notification), Str.getCharSequence(email));
                if (url!=null){
                    return url.toString();
                }
            }
        } catch (Exception ex){logger.error("",ex);}
        return "";
    }

    public void postToFeed(Post post){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            if (app!=null && !app.getMinifeedtemplate().equals("")){
                StringBuffer mf = new StringBuffer();
                mf.append(MinifeedTemplateProcessor.processTemplate(app.getMinifeedtemplate(), user, post));

                StringBuffer titleTemplate = new StringBuffer();
                titleTemplate.append("{actor} "+mf.toString());

                TemplatizedAction action = new TemplatizedAction(titleTemplate.toString());

                FacebookRestClient facebookRestClient = new FacebookRestClient(app.getFacebookapikey(), app.getFacebookapisecret(), sessionkey);
                facebookRestClient.feed_PublishTemplatizedAction(action);
            }
        } catch (Exception ex){logger.error("",ex);}
    }

    public void postThrowdownChallengeToFeed(Throwdown throwdown){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            if (app!=null && !app.getMinifeedtemplate().equals("")){

                FacebookUser toFacebookUser = getFacebookUserByUid(throwdown.getTofacebookuid());
                String toname=toFacebookUser.getFirst_name()+" "+toFacebookUser.getLast_name();


                StringBuffer titleTemplate = new StringBuffer();
                titleTemplate.append("{actor} challenged {toname} to a throwdown!");

                StringBuffer bodyTemplate = new StringBuffer();
                bodyTemplate.append("{toname} must now choose whether to accept this throwdown challenge called {throwdownname}.");

                TemplatizedAction action = new TemplatizedAction(titleTemplate.toString(), bodyTemplate.toString());
                action.addTargetIds(throwdown.getTofacebookuid());
                action.addTitleParam("toname", toname);
                action.addBodyParam("toname", toname);
                action.addBodyParam("throwdownname", "<a href='http://apps.facebook.com/"+app.getFacebookappname()+"/?nav=throwdown&throwdownid="+throwdown.getThrowdownid()+"'>"+throwdown.getName()+"</a>");

                FacebookRestClient facebookRestClient = new FacebookRestClient(app.getFacebookapikey(), app.getFacebookapisecret(), sessionkey);
                facebookRestClient.feed_PublishTemplatizedAction(action);
            }
        } catch (Exception ex){logger.error("",ex);}
    }

    public void postThrowdownAcceptToFeed(Throwdown throwdown){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            if (app!=null && !app.getMinifeedtemplate().equals("")){

                FacebookUser toFacebookUser = getFacebookUserByUid(throwdown.getTofacebookuid());
                User fromUser = User.get(throwdown.getFromuserid());
                FacebookUser fromFacebookUser = getFacebookUserByUid(fromUser.getFacebookuid());
                String toname=toFacebookUser.getFirst_name()+" "+toFacebookUser.getLast_name();
                String fromname=fromFacebookUser.getFirst_name()+" "+fromFacebookUser.getLast_name();

                StringBuffer titleTemplate = new StringBuffer();
                titleTemplate.append("{actor} accepted {fromname}'s challenge to a throwdown!");

                StringBuffer bodyTemplate = new StringBuffer();
                bodyTemplate.append("{throwdownname} They will not battle it out in this epic throwdown!");

                TemplatizedAction action = new TemplatizedAction(titleTemplate.toString(), bodyTemplate.toString());
                action.addTargetIds(throwdown.getTofacebookuid());
                action.addTitleParam("fromname", fromname);
                action.addBodyParam("throwdownname", "<a href='http://apps.facebook.com/"+app.getFacebookappname()+"/?nav=throwdown&throwdownid="+throwdown.getThrowdownid()+"'>"+throwdown.getName()+"</a>");

                FacebookRestClient facebookRestClient = new FacebookRestClient(app.getFacebookapikey(), app.getFacebookapisecret(), sessionkey);
                facebookRestClient.feed_PublishTemplatizedAction(action);
            }
        } catch (Exception ex){logger.error("",ex);}
    }

    public void postThrowdownCompleteToFeeds(Throwdown throwdown){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            if (app!=null && !app.getMinifeedtemplate().equals("")){

                User fromUser = User.get(throwdown.getFromuserid());
                FacebookUser toFacebookUser = getFacebookUserByUid(throwdown.getTofacebookuid());

                StringBuffer titleTemplate = new StringBuffer();
                titleTemplate.append("The Throwdown is officially complete!");

                StringBuffer bodyTemplate = new StringBuffer();
                bodyTemplate.append("{throwdownname} has completed.  Now go see who won!");

                TemplatizedAction action = new TemplatizedAction(titleTemplate.toString(), bodyTemplate.toString());

                action.addTargetIds(throwdown.getTofacebookuid()+","+fromUser.getFacebookuid());
                action.addBodyParam("throwdownname", "<a href='http://apps.facebook.com/"+app.getFacebookappname()+"/?nav=throwdown&throwdownid="+throwdown.getThrowdownid()+"'>"+throwdown.getName()+"</a>");

                FacebookRestClient facebookRestClient = new FacebookRestClient(app.getFacebookapikey(), app.getFacebookapisecret(), sessionkey);
                facebookRestClient.feed_PublishTemplatizedAction(action);
            }
        } catch (Exception ex){logger.error("",ex);}
    }

    public void updateProfile(User user){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("Starting to create FBML for profile");
        try{
            String imgUrl = BaseUrl.get(false)+"fb/graph.jsp?chartid="+app.getPrimarychartid()+"&userid="+user.getUserid()+"&size=profilewide&comparetouserid=0";

            StringBuffer fbml = new StringBuffer();
            fbml.append("<center>");
            fbml.append("<a href='http://apps.facebook.com/"+app.getFacebookappname()+"/'>");
            fbml.append("<img src=\""+imgUrl+"\" alt=\"\" width=\"380\" height=\"200\"/>");
            fbml.append("<br/>");
            fbml.append("<font size='-2'>Chart by "+app.getTitle()+"</font>");
            fbml.append("</a>");
            fbml.append("</center>");

            //Add throwdowns update to profile here
            List<Throwdown> throwdowns=HibernateUtil.getSession().createCriteria(Throwdown.class)
                    .add(Restrictions.or(
                                        Restrictions.eq("fromuserid", user.getUserid()),
                                        Restrictions.eq("tofacebookuid", user.getFacebookuid()))
                                        )
                    .add(Restrictions.eq("isaccepted", true))
                    .add(Restrictions.ge("enddate", Time.xDaysAgoStart(Calendar.getInstance(), 7).getTime()))
                    .setCacheable(true)
                    .list();
            if (throwdowns!=null && throwdowns.size()>0){
                fbml.append("<center>");
                fbml.append("<table cellpadding='2' cellspacing='1' border='0' width='100%'>");
                for (Iterator<Throwdown> iterator=throwdowns.iterator(); iterator.hasNext();) {
                    Throwdown throwdown=iterator.next();
                    ThrowdownStatus ts = new ThrowdownStatus(throwdown);
                    fbml.append("<tr>");
                        fbml.append("<td valign=\"top\" colspan=\"3\" bgcolor=\"#e6e6e6\"><center><a href=\"http://apps.facebook.com/"+app.getFacebookappname()+"/?nav=throwdown&throwdownid="+throwdown.getThrowdownid()+"\"><font style=\"color: #0000ff; font-weight: bold;\">"+throwdown.getName()+"</font></a></center></td>");
                    fbml.append("</tr>");
                    fbml.append("<tr>");
                        fbml.append("<td valign=\"top\" colspan=\"3\"><center>Ends: "+Time.dateformatcompactwithtime(Time.getCalFromDate(throwdown.getEnddate()))+"</center></td>");
                    fbml.append("</tr>");
                    fbml.append("<tr>");
                         fbml.append("<td valign=\"top\"><center>"+ts.getFromStatus()+"</center></td>");
                         fbml.append("<td valign=\"center\"></td>");
                         fbml.append("<td valign=\"top\"><center>"+ts.getToStatus()+"</center></td>");
                    fbml.append("</tr>");
                    fbml.append("<tr>");
                         fbml.append("<td valign=\"top\"><center><fb:profile-pic uid=\""+ts.getFromFacebookUser().getUid()+"\" size=\"square\" linked=\"false\"/><br/>"+ts.getFromFacebookUser().getFirst_name()+" "+ts.getFromFacebookUser().getLast_name()+"</center></td>");
                         fbml.append("<td valign=\"center\"><center>vs.</center></td>");
                         fbml.append("<td valign=\"top\"><center><fb:profile-pic uid=\""+ts.getToFacebookUser().getUid()+"\" size=\"square\" linked=\"false\"/><br/>"+ts.getToFacebookUser().getFirst_name()+" "+ts.getToFacebookUser().getLast_name()+"</center></td>");
                    fbml.append("</tr>");
                }
                fbml.append("</table>");
                fbml.append("</center>");
            }

            CharSequence cs = fbml.subSequence(0, fbml.length());
            FacebookRestClient facebookRestClient = new FacebookRestClient(app.getFacebookapikey(), app.getFacebookapisecret(), sessionkey);
            boolean success = facebookRestClient.profile_setFBML(cs, Long.parseLong(user.getFacebookuid()));
            if (success){
                logger.debug("Apparently the setFBML was successful.");
            } else {
                logger.debug("Apparently the setFBML was not successful.");
            }
            boolean successrefreshimage = facebookRestClient.fbml_refreshImgSrc(imgUrl);
            if (successrefreshimage){
                logger.debug("Apparently refresh fb image was successful.");
            } else {
                logger.debug("Apparently refresh fb image was not successful.");
            }
        } catch (Exception ex){logger.error("",ex);}
    }

    public ArrayList<Integer> getFriendUids(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        ArrayList<Integer> friends = new ArrayList<Integer>();
        try{
            //Set up the facebook rest client
            FacebookRestClient facebookRestClient = new FacebookRestClient(app.getFacebookapikey(), app.getFacebookapisecret(), sessionkey);
            //Get a list of uids
            Document w3cDoc = facebookRestClient.friends_get();
            DOMBuilder builder = new DOMBuilder();
            org.jdom.Document jdomDoc = builder.build(w3cDoc);
            logger.debug("Start Facebook API Friends Response:");
            XMLOutputter outp = new XMLOutputter();
            outp.output(jdomDoc, System.out);
            logger.debug(":End Facebook API Friends Response");
            Element root = jdomDoc.getRootElement();
            outputChildrenToLogger(root, 0);
            //Extract the uids
            List allChildren = root.getChildren();
            for (Iterator iterator = allChildren.iterator(); iterator.hasNext();) {
                Element element = (Element) iterator.next();
                if (element.getName().equals("uid")){
                    if(Num.isinteger(element.getTextTrim())){
                        friends.add(Integer.parseInt(element.getTextTrim()));
                    }
                }
            }
        } catch (Exception ex){logger.error("",ex);}
        return friends;
    }

//    public ArrayList<Integer> getFriendUids(){
//        Logger logger = Logger.getLogger(this.getClass().getName());
//        ArrayList<Integer> friends = new ArrayList<Integer>();
//        try{
//            //Set up the facebook rest client
//            FacebookRestClient facebookRestClient = new FacebookRestClient(app.getFacebookapikey(), app.getFacebookapisecret(), sessionkey);
//            //Get a list of uids
//            //@todo Rewrite to work with infinite session key
//            Document w3cDoc = facebookRestClient.friends_get();
//            DOMBuilder builder = new DOMBuilder();
//            org.jdom.Document jdomDoc = builder.build(w3cDoc);
//            logger.debug("Start Facebook API Friends Response:");
//            XMLOutputter outp = new XMLOutputter();
//            outp.output(jdomDoc, System.out);
//            logger.debug(":End Facebook API Friends Response");
//            Element root = jdomDoc.getRootElement();
//            outputChildrenToLogger(root, 0);
//            //Extract the uids
//            List allChildren = root.getChildren();
//            for (Iterator iterator = allChildren.iterator(); iterator.hasNext();) {
//                Element element = (Element) iterator.next();
//                if (element.getName().equals("uid")){
//                    if(Num.isinteger(element.getTextTrim())){
//                        friends.add(Integer.parseInt(element.getTextTrim()));
//                    }
//                }
//            }
//        } catch (Exception ex){logger.error("",ex);}
//        return friends;
//    }

    public FacebookUser getFacebookUserByUid(String facebookuid){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            //Set up the facebook rest client
            FacebookRestClient facebookRestClient = new FacebookRestClient(app.getFacebookapikey(), app.getFacebookapisecret(), sessionkey);
                //Go back and get all the important info
                String fql = "SELECT "+FacebookUser.sqlListOfCols+" FROM user WHERE uid='"+facebookuid+"'";
                Document w3cDoc2 = facebookRestClient.fql_query(fql.subSequence(0,fql.length()));
                DOMBuilder builder2 = new DOMBuilder();
                org.jdom.Document jdomDoc2 = builder2.build(w3cDoc2);
                logger.debug("Start Facebook FQL Response: "+fql);
                XMLOutputter outp2 = new XMLOutputter();
                outp2.output(jdomDoc2, System.out);
                logger.debug(":End Facebook FQL Response");
                Element root2 = jdomDoc2.getRootElement();
                //Iterate each child
                List fbusers = root2.getChildren();
                for (Iterator iterator = fbusers.iterator(); iterator.hasNext();) {
                    Element fbuser = (Element) iterator.next();
                    if (fbuser.getName().equals("user")){
                        FacebookUser facebookUser = new FacebookUser(fbuser);
                        if (facebookUser.getUid().length()>0){
                            return facebookUser;
                        }
                    }
                }
        } catch (Exception ex){logger.error("",ex); ex.printStackTrace();}
        return null;
    }




    public TreeSet<FacebookUser> getFriends(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("begin getFriends() sessionkey="+sessionkey);
        //ArrayList<FacebookUser> friends = new ArrayList<FacebookUser>();
        TreeSet friendsSorted = new TreeSet<FacebookUser>(new Comparator() {
            public int compare(Object a, Object b) {
                FacebookUser facebookUserA = (FacebookUser) a;
                FacebookUser facebookUserB = (FacebookUser) b;
                String firstnameA = facebookUserA.getFirst_name();
                String firstnameB = facebookUserB.getFirst_name();
                return firstnameA.compareTo(firstnameB);
            }
        });
        try{
            //Set up the facebook rest client
            FacebookRestClient facebookRestClient = new FacebookRestClient(app.getFacebookapikey(), app.getFacebookapisecret(), sessionkey);
            //Get the list of uids
            //@todo can I prevent this call by using the fb_sig_friends that seems to be sent all the time?
            ArrayList<Integer> uids = getFriendUids();

            if (uids!=null && uids.size()>0){
                logger.debug("getFriends() uids not null sessionkey="+sessionkey);
                //Create fql based on the list of uids
                StringBuffer fqlWhere = new StringBuffer();
                fqlWhere.append("(uid IN (");
                for (Iterator iterator = uids.iterator(); iterator.hasNext();) {
                    Integer uid = (Integer) iterator.next();
                    fqlWhere.append(uid);
                    if (iterator.hasNext()){
                        fqlWhere.append(", ");
                    }
                }
                fqlWhere.append("))");
                //Go back and get all the important info
                String fql = "SELECT "+FacebookUser.sqlListOfCols+" FROM user WHERE "+fqlWhere;
                Document w3cDoc2 = facebookRestClient.fql_query(fql.subSequence(0,fql.length()));
                DOMBuilder builder2 = new DOMBuilder();
                org.jdom.Document jdomDoc2 = builder2.build(w3cDoc2);
                logger.debug("Start Facebook FQL Response: "+fql);
                XMLOutputter outp2 = new XMLOutputter();
                outp2.output(jdomDoc2, System.out);
                logger.debug(":End Facebook FQL Response");
                Element root2 = jdomDoc2.getRootElement();
                //Iterate each child
                List fbusers = root2.getChildren();
                for (Iterator iterator = fbusers.iterator(); iterator.hasNext();) {
                    Element fbuser = (Element) iterator.next();
                    if (fbuser.getName().equals("user")){
                        FacebookUser facebookUser = new FacebookUser(fbuser);
                        if (facebookUser.getUid().length()>0){
                            friendsSorted.add(facebookUser);
                        }
                    }
                }
            }
        } catch (Exception ex){logger.error("",ex); ex.printStackTrace();}
        logger.debug("end getFriends() sessionkey="+sessionkey);
        return friendsSorted;
    }



    public FacebookUser getFacebookUserByUid(ArrayList<FacebookUser> facebookUsers, String uid){
        for (Iterator<FacebookUser> iterator = facebookUsers.iterator(); iterator.hasNext();) {
            FacebookUser facebookUser = iterator.next();
            if (facebookUser.getUid().equals(uid)){
                return facebookUser;
            }
        }
        return null;
    }




    public void inviteFriendsToApp(ArrayList<Long> uids, App app){
        Logger logger = Logger.getLogger(this.getClass().getName());
        FacebookRestClient facebookRestClient = new FacebookRestClient(app.getFacebookapikey(), app.getFacebookapisecret(), sessionkey);

        String type = "social survey";
        CharSequence typeChars = type.subSequence(0, type.length());
        StringBuffer content = new StringBuffer();
        content.append("You've been invited to "+app.getTitle()+". "+app.getDescription());

        content.append("<fb:req-choice url=\"http://apps.facebook.com/"+app.getFacebookappname()+"/?action=invited\" label=\"Check it Out\" />");
        CharSequence contentChars = content.subSequence(0, content.length());
        try{
            URL url = facebookRestClient.notifications_send(uids, typeChars, contentChars);
            if (url!=null){
                logger.debug("FacebookAPI returned: " + url.toString());
                //String redirUrl = "/redirectoutofframe.jsp?url="+ URLEncoder.encode(url.toString(), "UTF-8");
                //Jsf.redirectResponse(url.toString());
                return;
            }

        } catch (Exception ex){
            logger.error("",ex);
        }
    }




    public static Element getChild(Element el, String name){
        List allChildren = el.getChildren();
        for (Iterator iterator = allChildren.iterator(); iterator.hasNext();) {
            Element element = (Element) iterator.next();
            if (element.getName().equals(name)){
                return element;
            }
        }
        return null;
    }

    public static void outputChildrenToLogger(Element el, int level){
        Logger logger = Logger.getLogger(FacebookApiWrapper.class);
        level = level + 1;
        String indent = "";
        for(int i=0; i<level; i++){
            indent = indent + "-";
        }
        List allChildren = el.getChildren();
        for (Iterator iterator = allChildren.iterator(); iterator.hasNext();) {
            Element element = (Element) iterator.next();
            //logger.debug(indent + " " + element.getName());
            outputChildrenToLogger(element, level);
        }
    }



}
