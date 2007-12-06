package com.fbdblog.facebook;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.jdom.input.DOMBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.Element;
import com.facebook.api.FacebookRestClient;
import com.fbdblog.systemprops.SystemProperty;
import com.fbdblog.systemprops.BaseUrl;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Aug 14, 2007
 * Time: 11:31:22 PM
 */
public class FacebookUser implements Serializable {
    private String first_name;
    private String last_name;
    private String uid;
    private String sex;
    private String pic_square;
    private boolean has_added_app;

    public static String sqlListOfCols = "first_name, last_name, birthday, sex, uid, pic_square, has_added_app";

    public FacebookUser(Long facebookuserid, String facebookSessionKey, String api_key, String api_secret){
        refreshFromFacebookApi(facebookuserid, facebookSessionKey, api_key, api_secret);
    }

    public FacebookUser(Element userDom){
        populateFromUserDom(userDom);
    }

    public void refreshFromFacebookApi(Long facebookuserid, String facebookSessionKey, String api_key, String api_secret){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            FacebookRestClient facebookRestClient = new FacebookRestClient(api_key, api_secret, facebookSessionKey);
            String fql = "SELECT "+sqlListOfCols+" FROM user WHERE uid="+facebookuserid;
            Document w3cDoc = facebookRestClient.fql_query(fql.subSequence(0,fql.length()));
            DOMBuilder builder = new DOMBuilder();
            org.jdom.Document jdomDoc = builder.build(w3cDoc);
            logger.debug("Start Facebook FQL Response: "+fql);
            XMLOutputter outp = new XMLOutputter();
            outp.output(jdomDoc, System.out);
            logger.debug(":End Facebook FQL Response");
            Element root = jdomDoc.getRootElement();
            //outputChildrenToLogger(root, 0);
            Element user = FacebookApiWrapper.getChild(root, "user");
            populateFromUserDom(user);
        } catch (Exception ex){
            ex.printStackTrace();
            logger.error("",ex);
        }
    }

    private void populateFromUserDom(Element userDom){
        Element first_name = FacebookApiWrapper.getChild(userDom, "first_name");
        this.first_name = first_name.getTextTrim();
        Element last_name = FacebookApiWrapper.getChild(userDom, "last_name");
        this.last_name = last_name.getTextTrim();
        Element uid = FacebookApiWrapper.getChild(userDom, "uid");
        this.uid = uid.getTextTrim();
        Element sex = FacebookApiWrapper.getChild(userDom, "sex");
        this.sex = sex.getTextTrim();
        Element pic_square = FacebookApiWrapper.getChild(userDom, "pic_square");
        if (!pic_square.getTextTrim().equals("")){
            this.pic_square = pic_square.getTextTrim();
        } else {
            this.pic_square = BaseUrl.get(true)+"images/facebook-50x50-placeholder.gif";
        }
        Element has_added_app = FacebookApiWrapper.getChild(userDom, "has_added_app");
        if (has_added_app.getTextTrim().equals("1")){
            this.has_added_app = true;
        } else {
            this.has_added_app = false;
        }
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPic_square() {
        return pic_square;
    }

    public void setPic_square(String pic_square) {
        this.pic_square = pic_square;
    }

    public boolean getHas_added_app() {
        return has_added_app;
    }

    public void setHas_added_app(boolean has_added_app) {
        this.has_added_app = has_added_app;
    }
}
