package com.fbdblog.chart;

import java.util.Calendar;
import java.util.ArrayList;

import com.fbdblog.util.Time;
import com.fbdblog.dao.Post;
import com.fbdblog.dao.hibernate.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;

/**
 * So that I don't have to call the database for each series, this object
 * creates and stores those entries that may be related to the chart at hand.
 */
public class MegaChartEntryChooser {

    private ArrayList<Post> posts = new ArrayList<Post>();

    //These are the vars required to execute the search
    private int appid;
    private int userid;
    private MegaChart megaChart;

    //Derived
    //private String dateSql = "";


    public MegaChartEntryChooser(MegaChart megaChart, int appid, int userid){
        this.appid = appid;
        this.userid = userid;
        this.megaChart = megaChart;
    }


    public ArrayList<Post> getPosts() {
        return posts;
    }

    public void populate(){
        Logger logger = Logger.getLogger(this.getClass().getName());

        //I need to get a list of events that make up the data for this chart.
        //I can do this one of two ways.
        //The first way is via a saved search.
        //The second way is via sql string building
        posts = new ArrayList<Post>();
        //Start Build the date-limiting SQL - The goal of the next chunk is to build a SQL statement
        //that returns a three column resultset (eventid, xaxis, yaxis)
        String dateSql="";
        Calendar dateLow=null;
        Calendar dateHigh=null;
        String dateStrLow="";
        String dateStrHigh="";
        if (megaChart.getChart().getDaterange()==MegaConstants.DATERANGETHISWEEK){
            dateLow=Time.xWeeksAgoStart(Calendar.getInstance(), 0);
            dateHigh=Time.xWeeksAgoEnd(Calendar.getInstance(), 0);
            dateStrLow = Time.dateformatfordb(dateLow);
            dateStrHigh = Time.dateformatfordb(dateHigh);
            dateSql = " AND event.date>'" + dateStrLow + "' AND event.date<'" + dateStrHigh + "'";
        } else if (megaChart.getChart().getDaterange()==MegaConstants.DATERANGETHISMONTH) {
            dateLow=Time.xMonthsAgoStart(Calendar.getInstance(), 0);
            dateHigh=Time.xMonthsAgoEnd(Calendar.getInstance(), 0);
            dateStrLow = Time.dateformatfordb(dateLow);
            dateStrHigh = Time.dateformatfordb(dateHigh);
            dateSql = " AND event.date>'" + dateStrLow + "' AND event.date<'" + dateStrHigh + "'";
        } else if (megaChart.getChart().getDaterange()==MegaConstants.DATERANGETHISYEAR) {
            dateLow=Time.xYearsAgoStart(Calendar.getInstance(), 0);
            dateHigh=Time.xYearsAgoEnd(Calendar.getInstance(), 0);
            dateStrLow = Time.dateformatfordb(dateLow);
            dateStrHigh = Time.dateformatfordb(dateHigh);
            dateSql = " AND event.date>'" + dateStrLow + "' AND event.date<'" + dateStrHigh + "'";
        } else if (megaChart.getChart().getDaterange()==MegaConstants.DATERANGELASTWEEK){
            dateLow=Time.xWeeksAgoStart(Calendar.getInstance(), 1);
            dateHigh=Time.xWeeksAgoEnd(Calendar.getInstance(), 1);
            dateStrLow = Time.dateformatfordb(dateLow);
            dateStrHigh = Time.dateformatfordb(dateHigh);
            dateSql = " AND event.date>'" + dateStrLow + "' AND event.date<'" + dateStrHigh + "'";
        } else if (megaChart.getChart().getDaterange()==MegaConstants.DATERANGELASTMONTH) {
            dateLow=Time.xMonthsAgoStart(Calendar.getInstance(), 1);
            dateHigh=Time.xMonthsAgoEnd(Calendar.getInstance(), 1);
            dateStrLow = Time.dateformatfordb(dateLow);
            dateStrHigh = Time.dateformatfordb(dateHigh);
            dateSql = " AND event.date>'" + dateStrLow + "' AND event.date<'" + dateStrHigh + "'";
        } else if (megaChart.getChart().getDaterange()==MegaConstants.DATERANGELASTYEAR) {
            dateLow=Time.xYearsAgoStart(Calendar.getInstance(), 1);
            dateHigh=Time.xMonthsAgoEnd(Calendar.getInstance(), 1);
            dateStrLow = Time.dateformatfordb(dateLow);
            dateStrHigh = Time.dateformatfordb(dateHigh);
            dateSql = " AND event.date>'" + dateStrLow + "' AND event.date<'" + dateStrHigh + "'";
        } else if (megaChart.getChart().getDaterange()==MegaConstants.DATERANGELASTXDAYS) {
            dateLow=Time.xDaysAgoStart(Calendar.getInstance(), megaChart.getChart().getLastxdays());
            dateHigh=Time.maxTime(Calendar.getInstance());
            dateStrLow = Time.dateformatfordb(dateLow);
            dateStrHigh = Time.dateformatfordb(dateHigh);
            dateSql = " AND event.date>'" + dateStrLow + "' AND event.date<'" + dateStrHigh + "'";
        } else if (megaChart.getChart().getDaterange()==MegaConstants.DATERANGELASTXWEEKS) {
            dateLow=Time.xWeeksAgoStart(Calendar.getInstance(), megaChart.getChart().getLastxweeks());
            dateHigh=Time.maxTime(Calendar.getInstance());
            dateStrLow = Time.dateformatfordb(dateLow);
            dateStrHigh = Time.dateformatfordb(dateHigh);
            dateSql = " AND event.date>'" + dateStrLow + "' AND event.date<'" + dateStrHigh + "'";
        } else if (megaChart.getChart().getDaterange()==MegaConstants.DATERANGELASTXMONTHS) {
            dateLow=Time.xMonthsAgoStart(Calendar.getInstance(), megaChart.getChart().getLastxmonths());
            dateHigh=Time.maxTime(Calendar.getInstance());
            dateStrLow = Time.dateformatfordb(dateLow);
            dateStrHigh = Time.dateformatfordb(dateHigh);
            dateSql = " AND event.date>'" + dateStrLow + "' AND event.date<'" + dateStrHigh + "'";
        } else if (megaChart.getChart().getDaterange()==MegaConstants.DATERANGELASTXYEARS) {
            dateLow=Time.xYearsAgoStart(Calendar.getInstance(), megaChart.getChart().getLastxyears());
            dateHigh=Time.maxTime(Calendar.getInstance());
            dateStrLow = Time.dateformatfordb(dateLow);
            dateStrHigh = Time.dateformatfordb(dateHigh);
            dateSql = " AND event.date>'" + dateStrLow + "' AND event.date<'" + dateStrHigh + "'";
        } else if (megaChart.getChart().getDaterange()==MegaConstants.DATERANGESPECIFIED) {
            dateLow=Time.dbstringtocalendar(megaChart.getChart().getDaterangefromyyyy()+"-"+megaChart.getChart().getDaterangefrommm() +"-"+megaChart.getChart().getDaterangefromdd()+" 00:00:00");
            dateHigh=Time.dbstringtocalendar(megaChart.getChart().getDaterangetoyyyy()+"-"+megaChart.getChart().getDaterangetomm() +"-"+megaChart.getChart().getDaterangetodd()+" 23:59:59");
            dateStrLow = Time.dateformatfordb(dateLow);
            dateStrHigh = Time.dateformatfordb(dateHigh);
            dateSql = " AND event.date>'"+dateStrLow+"' AND event.date<'"+dateStrHigh+"'";
        }

        //This query generates a list of eventids that are relevant to this chart.  That's it.
        posts = (ArrayList<Post>)HibernateUtil.getSession().createCriteria(Post.class)
                                           .add(Restrictions.ge("postdate", dateLow.getTime()))
                                           .add(Restrictions.le("postdate", dateHigh.getTime()))
                                           .add(Restrictions.eq("userid", userid))
                                           .add(Restrictions.eq("appid", appid))
                                           .addOrder( Order.desc("postdate") )
                                           .setCacheable(true)
                                           .list();

    }





}
