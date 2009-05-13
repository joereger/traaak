package com.fbdblog.throwdown;

import com.fbdblog.dao.*;
import com.fbdblog.calc.CalcUtil;
import com.fbdblog.facebook.FacebookUser;
import com.fbdblog.facebook.FacebookApiWrapper;
import com.fbdblog.htmlui.UserSession;

/**
 * User: Joe Reger Jr
 * Date: Dec 7, 2007
 * Time: 2:03:01 AM
 */
public class ThrowdownStatus {

    private Throwdown throwdown;
    private String sessionkey;
    private double fromValue = 0;
    private double toValue = 0;
    private FacebookUser fromFacebookUser;
    private FacebookUser toFacebookUser;
    private User fromUser;
    private User toUser;
    private String fromStatus = "";
    private String toStatus = "";
    private boolean isFromWinning = false;
    private boolean isToWinning = false;

    public ThrowdownStatus(Throwdown throwdown){
        this.throwdown = throwdown;
        initUsers();
        initBean();
    }

    public ThrowdownStatus(Throwdown throwdown, String sessionkey){
        this.throwdown = throwdown;
        this.sessionkey = sessionkey;
        initUsers();
        initBean();
    }

    public ThrowdownStatus(Throwdown throwdown, UserSession userSession, FacebookUser fromFacebookUser, FacebookUser toFacebookUser, User fromUser, User toUser){
        this.throwdown = throwdown;
        this.sessionkey = userSession.getFacebooksessionkey();
        this.fromFacebookUser = fromFacebookUser;
        this.toFacebookUser = toFacebookUser;
        this.fromUser = fromUser;
        this.toUser = toUser;
        //Note that there's no call to initUsers() in this method... performance baby!  Performance!  Yehaw!  I'm up coding way too late.
        initBean();
    }

    private void initUsers(){
        fromUser = User.get(throwdown.getFromuserid());
        FacebookApiWrapper faw = null;
        if (sessionkey!=null){
            faw=new FacebookApiWrapper(App.get(throwdown.getAppid()), fromUser, sessionkey);
        } else {
            faw=new FacebookApiWrapper(App.get(throwdown.getAppid()), fromUser);
        }
        fromFacebookUser = faw.getFacebookUserByUid(fromUser.getFacebookuid());
        toFacebookUser = faw.getFacebookUserByUid(throwdown.getTofacebookuid());
        if (throwdown.getTouserid()>0){
            toUser = User.get(throwdown.getTouserid());
        }
    }

    private void initBean(){
        if(!throwdown.getIscomplete()){
            App app = App.get(throwdown.getAppid());
            if (throwdown.getQuestionid()>0){
                fromValue = CalcUtil.getValueForQuestion(Question.get(throwdown.getQuestionid()), fromUser, app);
            } else if (throwdown.getQuestioncalcid()>0) {
                fromValue = CalcUtil.getValueForCalc(Questioncalc.get(throwdown.getQuestioncalcid()), fromUser, app);
            }
            if (toUser!=null){
                if (throwdown.getQuestionid()>0){
                    toValue = CalcUtil.getValueForQuestion(Question.get(throwdown.getQuestionid()), toUser, app);
                } else if (throwdown.getQuestioncalcid()>0) {
                    toValue = CalcUtil.getValueForCalc(Questioncalc.get(throwdown.getQuestioncalcid()), toUser, app);
                }
            }
            if (fromValue==toValue){
                fromStatus = "Tied.";
                toStatus = "Tied.";
            } else if (throwdown.getIsgreaterthan() && fromValue>toValue){
                fromStatus = "Winning!";
                isFromWinning = true;
                toStatus = "Losing.";
                isToWinning = false;
            } else if (!throwdown.getIsgreaterthan() && fromValue<toValue){
                fromStatus = "Winning!";
                isFromWinning = true;
                toStatus = "Losing.";
                isToWinning = false;
            } else {
                fromStatus = "Losing.";
                isFromWinning = false;
                toStatus = "Winning!";
                isToWinning = true;
            }
        } else {
            fromValue = throwdown.getFromvalue();
            toValue = throwdown.getTovalue();
            if (throwdown.getIsfromwinner()){
                fromStatus = "Winner!";
                isFromWinning = true;
                toStatus = "Loser.";
                isToWinning = false;
            } else if (throwdown.getIsfromwinner() && (fromValue!=toValue)) {
                fromStatus = "Loser.";
                isFromWinning = false;
                toStatus = "Winner!";
                isToWinning = true;
            } else {
                fromStatus = "Tie.";
                isFromWinning = false;
                toStatus = "Tie.";
                isToWinning = false;
            }
        }
    }


    public Throwdown getThrowdown() {
        return throwdown;
    }

    public void setThrowdown(Throwdown throwdown) {
        this.throwdown=throwdown;
    }

    public double getFromValue() {
        return fromValue;
    }

    public void setFromValue(double fromValue) {
        this.fromValue=fromValue;
    }

    public double getToValue() {
        return toValue;
    }

    public void setToValue(double toValue) {
        this.toValue=toValue;
    }

    public FacebookUser getFromFacebookUser() {
        return fromFacebookUser;
    }

    public void setFromFacebookUser(FacebookUser fromFacebookUser) {
        this.fromFacebookUser=fromFacebookUser;
    }

    public FacebookUser getToFacebookUser() {
        return toFacebookUser;
    }

    public void setToFacebookUser(FacebookUser toFacebookUser) {
        this.toFacebookUser=toFacebookUser;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser=fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser=toUser;
    }

    public String getFromStatus() {
        return fromStatus;
    }

    public void setFromStatus(String fromStatus) {
        this.fromStatus=fromStatus;
    }

    public String getToStatus() {
        return toStatus;
    }

    public void setToStatus(String toStatus) {
        this.toStatus=toStatus;
    }

    public boolean getIsFromWinning() {
        return isFromWinning;
    }

    public void setIsFromWinning(boolean isFromWinning) {
        this.isFromWinning=isFromWinning;
    }

    public boolean getIsToWinning() {
        return isToWinning;
    }

    public void setIsToWinning(boolean isToWinning) {
        this.isToWinning=isToWinning;
    }
}
