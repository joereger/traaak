package com.fbdblog.facebook;

import com.facebook.api.FacebookRestClient;
import com.fbdblog.session.UserSession;
import com.fbdblog.systemprops.SystemProperty;
import com.fbdblog.systemprops.BaseUrl;
import com.fbdblog.dao.Post;
import com.fbdblog.dao.User;
import com.fbdblog.dao.App;
import com.fbdblog.util.Str;
import com.fbdblog.util.Num;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.jdom.input.DOMBuilder;
import org.w3c.dom.Document;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.TreeMap;
import java.net.URL;

/**
 * User: Joe Reger Jr
 * Date: Jul 16, 2007
 * Time: 10:41:57 AM
 */
public class FacebookApiWrapper {

    private UserSession userSession = null;
    private boolean issessionok = false;

    public FacebookApiWrapper(UserSession userSession){
        Logger logger = Logger.getLogger(this.getClass().getName());
        this.userSession = userSession;
        if (userSession.getFacebooksessionkey()!=null && !userSession.getFacebooksessionkey().trim().equals("")){
            try{
                FacebookRestClient facebookRestClient = new FacebookRestClient(userSession.getApp().getFacebookapikey(), userSession.getApp().getFacebookapisecret(), userSession.getFacebooksessionkey());
                if (userSession.getUser()!=null && userSession.getUser().getUserid()>0){
                    if (userSession.getUser().getFacebookuid()!=null){
                        logger.debug("userSession.getUser().getFacebookuid()="+userSession.getUser().getFacebookuid()+" facebookRestClient.users_getLoggedInUser()="+facebookRestClient.users_getLoggedInUser());
                        if (userSession.getUser().getFacebookuid().trim().equals(String.valueOf(facebookRestClient.users_getLoggedInUser()))){
                            issessionok = true;
                        } else {
                            logger.debug("userSession.getUser().getFacebookuserid()!=facebookRestClient.users_getLoggedInUser()");
                        }
                    } else {
                        logger.debug("userSession.getUser() (userid="+userSession.getUser().getUserid()+") passed to FacebookApiWrapper does not have a saved facebookuserid");
                    }
                } else {
                    if (userSession.getFacebookUser()!=null && userSession.getFacebookUser().getUid().length()>0){
                        issessionok = true;
                    } else {
                        logger.debug("don't have a facebookuserid to work with");
                    }
                }
            } catch (Exception ex){
                logger.error(ex);
            }
        }
    }

    public void postSurveyToFacebookMiniFeed(Post post){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (issessionok){
            try{
                if (userSession!=null && userSession.getApp()!=null && !userSession.getApp().getMinifeedtemplate().equals("")){
                    //@todo need to limit the length to 60 chars... not counting tags... just the displayed chars
                    StringBuffer mf = new StringBuffer();
                    mf.append(MinifeedTemplateProcessor.processTemplate(userSession.getApp().getMinifeedtemplate(), userSession.getUser(), post));
                    FacebookRestClient facebookRestClient = new FacebookRestClient(userSession.getApp().getFacebookapikey(), userSession.getApp().getFacebookapisecret(), userSession.getFacebooksessionkey());
                    facebookRestClient.feed_publishActionOfUser(mf.toString(), "");
                }
            } catch (Exception ex){logger.error(ex);}
        } else {logger.debug("Can't execute because issessionok = false");}
    }

    public void updateFacebookProfile(User user){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("Starting to create FBML for profile");
        if (issessionok){
            try{
                String imgUrl = BaseUrl.get(false)+"fb/graph.jsp?chartid="+userSession.getApp().getPrimarychartid()+"&userid="+user.getUserid()+"&size=profilewide&comparetouserid=0";

                StringBuffer fbml = new StringBuffer();
                fbml.append("<center>");
                fbml.append("<a href='http://apps.facebook.com/"+userSession.getApp().getFacebookappname()+"/'>");
                fbml.append("<img src=\""+imgUrl+"\" alt=\"\" width=\"380\" height=\"200\"/>");
                fbml.append("<br/>");
                fbml.append("<font size='-2'>Chart by "+userSession.getApp().getTitle()+"</font>");
                fbml.append("</a>");
                fbml.append("</center>");

                CharSequence cs = fbml.subSequence(0, fbml.length());
                FacebookRestClient facebookRestClient = new FacebookRestClient(userSession.getApp().getFacebookapikey(), userSession.getApp().getFacebookapisecret(), userSession.getFacebooksessionkey());
                boolean success = facebookRestClient.profile_setFBML(cs, Integer.parseInt(user.getFacebookuid()));
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
            } catch (Exception ex){logger.error(ex);}
        } else {logger.debug("Can't execute because issessionok = false");}
    }

    public ArrayList<Integer> getFriendUids(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        ArrayList<Integer> friends = new ArrayList<Integer>();
        if (issessionok){
            try{
                //Set up the facebook rest client
                FacebookRestClient facebookRestClient = new FacebookRestClient(userSession.getApp().getFacebookapikey(), userSession.getApp().getFacebookapisecret(), userSession.getFacebooksessionkey());
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
            } catch (Exception ex){logger.error(ex);}
        } else {logger.debug("Can't execute because issessionok = false");}
        return friends;
    }


    public ArrayList<FacebookUser> getFriends(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("begin getFriends() userSession.getFacebooksessionkey()="+userSession.getFacebooksessionkey());
        ArrayList<FacebookUser> friends = new ArrayList<FacebookUser>();
        if (issessionok){
            try{

                //Set up the facebook rest client
                FacebookRestClient facebookRestClient = new FacebookRestClient(userSession.getApp().getFacebookapikey(), userSession.getApp().getFacebookapisecret(), userSession.getFacebooksessionkey());
                //Get the list of uids
                ArrayList<Integer> uids = getFriendUids();

                if (uids!=null && uids.size()>0){
                    logger.debug("getFriends() uids not null userSession.getFacebooksessionkey()="+userSession.getFacebooksessionkey());
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
                                friends.add(facebookUser);
                            }
                        }
                    }
                }
            } catch (Exception ex){logger.error(ex); ex.printStackTrace();}
        } else {logger.debug("Can't execute because issessionok = false");}
        logger.debug("end getFriends() userSession.getFacebooksessionkey()="+userSession.getFacebooksessionkey());
        return friends;
    }



    private FacebookUser getFacebookUserByUid(ArrayList<FacebookUser> facebookUsers, String uid){
        for (Iterator<FacebookUser> iterator = facebookUsers.iterator(); iterator.hasNext();) {
            FacebookUser facebookUser = iterator.next();
            if (facebookUser.getUid().equals(uid)){
                return facebookUser;
            }
        }
        return null;
    }




    public void inviteFriendsToApp(ArrayList<Integer> uids, App app){
        Logger logger = Logger.getLogger(this.getClass().getName());
        FacebookRestClient facebookRestClient = new FacebookRestClient(userSession.getApp().getFacebookapikey(), userSession.getApp().getFacebookapisecret(), userSession.getFacebooksessionkey());

        String type = "social survey";
        CharSequence typeChars = type.subSequence(0, type.length());
        StringBuffer content = new StringBuffer();
        content.append("You've been invited to "+app.getTitle()+". "+app.getDescription());

        content.append("<fb:req-choice url=\"http://apps.facebook.com/"+userSession.getApp().getFacebookappname()+"/?action=invited\" label=\"Check it Out\" />");
        CharSequence contentChars = content.subSequence(0, content.length());
        URL imgUrl = null;
        try{
            //@todo have icon for inviting friends
            imgUrl = new URL("http", SystemProperty.getProp(SystemProperty.PROP_BASEURL), "/images/bleh.png");
        } catch (Exception ex){
            logger.error(ex);
        }
        try{
            URL url = facebookRestClient.notifications_sendRequest(uids, typeChars, contentChars, imgUrl, true);
            if (url!=null){
                logger.debug("FacebookAPI returned: " + url.toString());
                //String redirUrl = "/redirectoutofframe.jsp?url="+ URLEncoder.encode(url.toString(), "UTF-8");
                //Jsf.redirectResponse(url.toString());
                return;
            }

        } catch (Exception ex){
            logger.error(ex);
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
//
//
//
//    public boolean getIssessionok() {
//        return issessionok;
//    }
//
//    public String getFacebooksessionkey() {
//        return facebookSessionKey;
//    }


}
