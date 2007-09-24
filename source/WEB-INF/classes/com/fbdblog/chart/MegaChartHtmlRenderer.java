package com.fbdblog.chart;

import java.util.Iterator;


import com.fbdblog.util.Str;
import com.fbdblog.dao.App;
import com.fbdblog.dao.Question;
import org.apache.log4j.Logger;

/**
 * Renders a chart into html screen with options to set, etc.
 */
public class MegaChartHtmlRenderer {


    public static String getHtml(MegaChart megaChart, int appid){
        Logger logger = Logger.getLogger(MegaChartHtmlRenderer.class);
        StringBuffer mb = new StringBuffer();
        App app = App.get(appid);


        //Table-creation html joy.

        mb.append("<table cellpadding=0 cellspacing=0 border=0 width=100% bgcolor=#666666><tr><td>");
        mb.append("<table cellpadding=5 cellspacing=1 width=100% border=0>");

        //Output the img tag that calls the chart.
        //The url line is my only interface to the graph object.
        //I create the url line delicately.
//        StringBuffer urlP=new StringBuffer();
//        urlP.append("xQuestionid="+ megaChart.getChart().getXquestionid()+"&");
//        for (int i = 0; i < megaChart.getYquestionid().length; i++) {
//            urlP.append("yQuestionid="+ megaChart.getYquestionid()[i] +"&");
//        }
//        urlP.append("yaxiswhattodo="+ megaChart.getChart().getYaxiswhattodo() +"&");
//        urlP.append("charttype="+ megaChart.getChart().getCharttype() +"&");
//        urlP.append("daterange="+ megaChart.getChart().getDaterange() +"&");
//        urlP.append("lastxdays="+ megaChart.getChart().getLastxdays() +"&");
//        urlP.append("lastxweeks="+ megaChart.getChart().getLastxweeks() +"&");
//        urlP.append("lastxmonths="+ megaChart.getChart().getLastxmonths() +"&");
//        urlP.append("lastxyears="+ megaChart.getChart().getLastxyears() +"&");
//        urlP.append("daterangefromyyyy="+ megaChart.getChart().getDaterangefromyyyy() +"&");
//        urlP.append("daterangefrommm="+ megaChart.getChart().getDaterangefrommm() +"&");
//        urlP.append("daterangefromdd="+ megaChart.getChart().getDaterangefromdd() +"&");
//        urlP.append("daterangetoyyyy="+ megaChart.getChart().getDaterangetoyyyy() +"&");
//        urlP.append("daterangetomm="+ megaChart.getChart().getDaterangetomm() +"&");
//        urlP.append("daterangetodd="+ megaChart.getChart().getDaterangetodd() +"&");



        mb.append("<tr><td bgcolor=#ffffff align=center valign=top>");
        //Chart title
        mb.append("<font face=arial size=+1 class=bigfont>");
        if (!megaChart.getChart().getName().equals("")){
            mb.append(megaChart.getChart().getName());
        } else {
            mb.append("Custom Graph");
        }
        mb.append("</font><br>");

        //Chart image only if we have a chartid to look at
        if (megaChart.getChart()!=null && megaChart.getChart().getChartid()>0){
            mb.append("<img src='/fb/graph.jsp??chartid="+megaChart.getChart().getChartid()+"&userid=0&size=medium&comparetouserid=0' border=0>");
        }

        mb.append("</td></tr>");


        //The goal here is to display any situation where I'm diverging from what the user has selected on the form.
        //For example, when you choose multiple Y-Axis checkboxes and a pie chart type I'll only use the last y-Axis selected in the list.
        //So I should display a message to that effect.

        //charttype=pie or 3dpie and multiple Y-Axis values are chosen.  I only use last Y-Axis.
        if (megaChart.getYquestionid().length==1 && megaChart.getYquestionid()[0]==0){
            mb.append("<tr><td bgcolor=#ffffff>");
            mb.append("No Y-Axis is selected.  Please choose at least one below.");
            mb.append("</td></tr>");
        }

        //xQuestionid=Date/Time and anything other than charttype=Line is selected.  I override with Line chart type.
        if (megaChart.getChart().getXquestionid()==ChartFieldEntrydatetime.ID && megaChart.getChart().getCharttype()!=MegaConstants.CHARTTYPELINE){
            mb.append("<tr><td bgcolor=#ffffff>");
            mb.append("Quick note: When you choose Date/Time for the X-Axis you can only choose a Chart Type of Line.  We've automatically adjusted for you.");
            mb.append("</td></tr>");
        }

        //xQuestionid=Day of the Week and anything other than charttype=bar, 3dbar, stacked bar, horizontal bar, horizontal3d bar
        if (megaChart.getChart().getXquestionid()==ChartFieldEntryDayofweek.ID && !(megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPE3DBAR || megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPEHORIZONTALBAR || megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPEHORIZONTAL3DBAR || megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPEBAR  || megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHART  || megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHART3D || megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHARTHORIZONTAL || megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHART3DHORIZONTAL)) {
            mb.append("<tr><td bgcolor=#ffffff>");
            mb.append("Quick note: When you choose Day of the Week for the X-Axis you can only choose one of the Bar chart types.  We've automatically adjusted for you.");
            mb.append("</td></tr>");
        }

        //megaChart.getChart().getCharttype()=pie or 3dpie and multiple Y-Axis values are chosen.  I only use last Y-Axis.
        if ((megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPE3DPIE || megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPEPIE) && (megaChart.getYquestionid().length>1)){
            mb.append("<tr><td bgcolor=#ffffff>");
            mb.append("Quick note: When you choose multiple Y-Axis series and a Chart Type of Pie or 3D Pie only the last Y-Axis selected will be charted.");
            mb.append("</td></tr>");
        }


        //A dropdown formatting string
        String dropdownstyle="style=\"font-family: Arial, Helvetica, sans-serif; font-size: 10px; color: #000000; background-color: #ffffff; border: #000000; border-style: solid; border-top-width: 1px; border-right-width: 1px; border-bottom-width: 1px; border-left-width: 1px\"";

        //Create the chart-customizing form
        mb.append("<tr><td bgcolor=#ffffff>");




            mb.append("<table cellpadding=8 width=100% cellspacing=1 border=0>");


            //Save chart?
            mb.append("<tr>");
            mb.append("<td valign=top bgcolor=#ffffff colspan=3>");
            mb.append("<center>");
            mb.append("<font face=arial size=-1>");
            mb.append("Chart Name: ");
            mb.append("<input type=textbox name=chartname value=\""+ Str.cleanForHtml(megaChart.getChart().getName())+"\" size=25 maxlength=100>");
            mb.append("<input type=submit value='Save'>");
            mb.append("</font>");
            mb.append("</center>");
            mb.append("</td>");
            mb.append("</tr>");



            //Title row
            mb.append("<tr>");
            mb.append("<td valign=top bgcolor=#e6e6e6>");
            mb.append("<font face=arial size=+1 color=#666666>");
            mb.append("X-Axis (Choose One)");
            mb.append("</font>");
            mb.append("</td>");
            mb.append("<td valign=top bgcolor=#e6e6e6>");
            mb.append("<font face=arial size=+1 color=#666666>");
            mb.append("Y-Axis (Choose Any)");
            mb.append("</font>");
            mb.append("</td>");
            mb.append("<td valign=top bgcolor=#e6e6e6>");
            mb.append("<font face=arial size=+1 color=#666666>");
            mb.append("Graph These Entries");
            mb.append("</font>");
            mb.append("</td>");
            mb.append("</tr>");

            //New row
            mb.append("<tr>");

            mb.append("<td valign=top>");

            logger.debug("MegaChartHtmlRenderer.java: xquestionid="+megaChart.getChart().getXquestionid());

            mb.append("<font face=arial size=-1 class=smallfont>");
            mb.append("<b>");
            mb.append("System Fields");
            mb.append("</b>");
            mb.append("</font>");
            mb.append("</br>");

            mb.append("<font face=arial size=-1>");
            //Derived xAxis types
            mb.append("<input name=xquestionid type=radio value='"+ChartFieldEntrydatetime.ID+"'");
            if (megaChart.getChart().getXquestionid()==ChartFieldEntrydatetime.ID){
                mb.append(" checked");
            }
            mb.append("> Exact Date/Time<br>");
            mb.append("<input name=xquestionid type=radio value='"+ChartFieldEntryDaysAgo.ID+"'");
            if (megaChart.getChart().getXquestionid()==ChartFieldEntryDaysAgo.ID){
                mb.append(" checked");
            }
            mb.append("> Days Ago<br>");
            mb.append("<input name=xquestionid type=radio value='"+ChartFieldEntryWeeksAgo.ID+"'");
            if (megaChart.getChart().getXquestionid()==ChartFieldEntryWeeksAgo.ID){
                mb.append(" checked");
            }
            mb.append("> Weeks Ago<br>");
            mb.append("<input name=xquestionid type=radio value='"+ChartFieldEntryMonthsAgo.ID+"'");
            if (megaChart.getChart().getXquestionid()==ChartFieldEntryMonthsAgo.ID){
                mb.append(" checked");
            }
            mb.append("> Months Ago<br>");
            mb.append("<input name=xquestionid type=radio value='"+ChartFieldEntryHourofday.ID+"'");
            if (megaChart.getChart().getXquestionid()==ChartFieldEntryHourofday.ID){
                mb.append(" checked");
            }
            mb.append("> Hour of the Day<br>");
            mb.append("<input name=xquestionid type=radio value='"+ChartFieldEntryDayofweek.ID+"'");
            if (megaChart.getChart().getXquestionid()==ChartFieldEntryDayofweek.ID){
                mb.append(" checked");
            }
            mb.append("> Day of the Week<br>");
            mb.append("<input name=xquestionid type=radio value='"+ChartFieldEntryDayofmonth.ID+"'");
            if (megaChart.getChart().getXquestionid()==ChartFieldEntryDayofmonth.ID){
                mb.append(" checked");
            }
            mb.append("> Day of the Month<br>");
            mb.append("<input name=xquestionid type=radio value='"+ChartFieldEntryorder.ID+"'");
            if (megaChart.getChart().getXquestionid()==ChartFieldEntryorder.ID){
                mb.append(" checked");
            }
            mb.append("> Entry Order<br>");

            //Title and check box
            mb.append("<font face=arial size=-1 class=smallfont>");
            mb.append("<b>");
            mb.append(app.getTitle());
            mb.append("</b>");
            mb.append("</font>");
            mb.append("</br>");

            //Output xAxis fields
            boolean foundAtLeastOneField = false;
            for (Iterator iterator = app.getQuestions().iterator(); iterator.hasNext();) {
                Question question =  (Question)iterator.next();
                foundAtLeastOneField = true;
                mb.append("<font face=arial size=-1 class=smallfont>");
                mb.append("<input name=xquestionid type=radio value='"+question.getQuestionid()+"'");
                if (megaChart.getChart().getXquestionid()==question.getQuestionid()){
                    mb.append(" checked");
                }
                mb.append("> "+question.getQuestion()+"<br>");
                mb.append("</font>");
            }
            if (!foundAtLeastOneField) {
                mb.append("<font face=arial size=-2 class=tinyfont>");
                mb.append("No graphable fields in this log.<br>");
                mb.append("</font>");
            }

            mb.append("</font>");
            mb.append("</td>");

            mb.append("<td valign=top>");
            mb.append("<font face=arial size=-1>");
            mb.append("<input name=yquestionid type=checkbox value='"+ChartFieldEntrycount.ID+"'");
            for(int j=0; j<megaChart.getYquestionid().length; j++){
                if (megaChart.getYquestionid()[j]==ChartFieldEntrycount.ID){
                    mb.append(" checked");
                }
            }
            mb.append("> Number of Entries<br>");


            //Title and check box
            mb.append("<font face=arial size=-1 class=smallfont>");
            mb.append("<b>");
            mb.append(app.getTitle());
            mb.append("</b>");
            mb.append("</font>");
            mb.append("</br>");


            //Output y axis fields... only numerics
            foundAtLeastOneField = false;
            for (Iterator iterator = app.getQuestions().iterator(); iterator.hasNext();) {
                Question question =  (Question)iterator.next();
                if (question.getDatatypeid()!=DataTypeString.DATATYPEID){
                    foundAtLeastOneField=true;
                    mb.append("<font face=arial size=-1 class=smallfont>");
                    mb.append("<input name=yquestionid type=checkbox value='"+question.getQuestionid()+"'");
                    for(int l=0; l<megaChart.getYquestionid().length; l++){
                        if (megaChart.getYquestionid()[l]==question.getQuestionid()){
                            mb.append(" checked");
                        }
                    }
                    mb.append("> "+question.getQuestion());
                    mb.append("</font><br>");
                }
            }
            if (!foundAtLeastOneField) {
                mb.append("<font face=arial size=-2 class=tinyfont>");
                mb.append("No graphable fields in this log.<br>");
                mb.append("</font>");
            }

            mb.append("</font>");
            mb.append("</td>");


            //Saved search/date range - which entries to graph?
            mb.append("<td valign=top nowrap>");
            mb.append("<font face=arial size=-1>");
            mb.append("<input name=daterange type=radio value="+MegaConstants.DATERANGEALLTIME);
            if (megaChart.getChart().getDaterange()==MegaConstants.DATERANGEALLTIME){
                mb.append(" checked");
            }
            mb.append("> All Time");
            //
            mb.append("<br><input name=daterange type=radio value="+MegaConstants.DATERANGETHISWEEK);
            if (megaChart.getChart().getDaterange()==MegaConstants.DATERANGETHISWEEK){
                mb.append(" checked");
            }
            mb.append("> This Week");
            //
            mb.append("<br><input name=daterange type=radio value="+MegaConstants.DATERANGETHISMONTH);
            if (megaChart.getChart().getDaterange()==MegaConstants.DATERANGETHISMONTH){
                mb.append(" checked");
            }
            mb.append("> This Month");
            //
            mb.append("<br><input name=daterange type=radio value="+MegaConstants.DATERANGETHISYEAR);
            if (megaChart.getChart().getDaterange()==MegaConstants.DATERANGETHISYEAR){
                mb.append(" checked");
            }
            mb.append("> This Year");
            //
            mb.append("<br><input name=daterange type=radio value="+MegaConstants.DATERANGELASTWEEK);
            if (megaChart.getChart().getDaterange()==MegaConstants.DATERANGELASTWEEK){
                mb.append(" checked");
            }
            mb.append("> Last Week");
            //
            mb.append("<br><input name=daterange type=radio value="+MegaConstants.DATERANGELASTMONTH);
            if (megaChart.getChart().getDaterange()==MegaConstants.DATERANGELASTMONTH){
                mb.append(" checked");
            }
            mb.append("> Last Month");
            //
            mb.append("<br><input name=daterange type=radio value="+MegaConstants.DATERANGELASTXDAYS);
            if (megaChart.getChart().getDaterange()==MegaConstants.DATERANGELASTXDAYS){
                mb.append(" checked");
            }
            mb.append("> Last ");
            mb.append("<select name=lastxdays "+dropdownstyle+">");
            for(int i=0; i<60; i++){
               mb.append("<option value="+i);
               if (megaChart.getChart().getLastxdays()==i){
                   mb.append(" selected");
               }
               mb.append(">"+i+"</option>");
            }
            mb.append("</select>");
            mb.append(" Day(s)");
            //
            mb.append("<br><input name=daterange type=radio value="+MegaConstants.DATERANGELASTXWEEKS);
            if (megaChart.getChart().getDaterange()==MegaConstants.DATERANGELASTXWEEKS){
                mb.append(" checked");
            }
            mb.append("> Last ");
            mb.append("<select name=lastxweeks "+dropdownstyle+">");
            for(int i=0; i<16; i++){
               mb.append("<option value="+i);
               if (megaChart.getChart().getLastxweeks()==i){
                   mb.append(" selected");
               }
               mb.append(">"+i+"</option>");
            }
            mb.append("</select>");
            mb.append(" Week(s)");
            //
            mb.append("<br><input name=daterange type=radio value="+MegaConstants.DATERANGELASTXMONTHS);
            if (megaChart.getChart().getDaterange()==MegaConstants.DATERANGELASTXMONTHS){
                mb.append(" checked");
            }
            mb.append("> Last ");
            mb.append("<select name=lastxmonths "+dropdownstyle+">");
            for(int i=0; i<24; i++){
               mb.append("<option value="+i);
               if (megaChart.getChart().getLastxmonths()==i){
                   mb.append(" selected");
               }
               mb.append(">"+i+"</option>");
            }
            mb.append("</select>");
            mb.append(" Month(s)");
            //
            mb.append("<br><input name=daterange type=radio value="+MegaConstants.DATERANGELASTXYEARS);
            if (megaChart.getChart().getDaterange()==MegaConstants.DATERANGELASTXYEARS){
                mb.append(" checked");
            }
            mb.append("> Last ");
            mb.append("<select name=lastxyears "+dropdownstyle+">");
            for(int i=0; i<100; i++){
               mb.append("<option value="+i);
               if (megaChart.getChart().getLastxyears()==i){
                   mb.append(" selected");
               }
               mb.append(">"+i+"</option>");
            }
            mb.append("</select>");
            mb.append(" Year(s)");
            //
            mb.append("<br><input name=daterange type=radio value="+MegaConstants.DATERANGESPECIFIED);
            if (megaChart.getChart().getDaterange()==MegaConstants.DATERANGESPECIFIED){
                mb.append(" checked");
            }
            mb.append("> Date Range:");
            mb.append("<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;From:<br>");
            mb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            mb.append("<select name=daterangefromyyyy "+dropdownstyle+">");
            for(int i=1900; i<=2010; i++){
               mb.append("<option value="+i);
               if (megaChart.getChart().getDaterangefromyyyy()==i){
                   mb.append(" selected");
               }
               mb.append(">"+i+"</option>");
            }
            mb.append("</select>");
            mb.append(" / ");
            mb.append("<select name=daterangefrommm "+dropdownstyle+">");
            for(int i=1; i<=12; i++){
               mb.append("<option value="+i);
               if (megaChart.getChart().getDaterangefrommm()==i){
                   mb.append(" selected");
               }
               mb.append(">"+i+"</option>");
            }
            mb.append("</select>");
            mb.append(" / ");
            mb.append("<select name=daterangefromdd "+dropdownstyle+">");
            for(int i=0; i<=31; i++){
               mb.append("<option value="+i);
               if (megaChart.getChart().getDaterangefromdd()==i){
                   mb.append(" selected");
               }
               mb.append(">"+i+"</option>");
            }
            mb.append("</select>");
            mb.append("<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;To:<br>");
            mb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            mb.append("<select name=daterangetoyyyy "+dropdownstyle+">");
            for(int i=1900; i<=2010; i++){
               mb.append("<option value="+i);
               if (megaChart.getChart().getDaterangetoyyyy()==i){
                   mb.append(" selected");
               }
               mb.append(">"+i+"</option>");
            }
            mb.append("</select>");
            mb.append(" / ");
            mb.append("<select name=daterangetomm "+dropdownstyle+">");
            for(int i=1; i<=12; i++){
               mb.append("<option value="+i);
               if (megaChart.getChart().getDaterangetomm()==i){
                   mb.append(" selected");
               }
               mb.append(">"+i+"</option>");
            }
            mb.append("</select>");
            mb.append(" / ");
            mb.append("<select name=daterangetodd "+dropdownstyle+">");
            for(int i=1; i<=31; i++){
               mb.append("<option value="+i);
               if (megaChart.getChart().getDaterangetodd()==i){
                   mb.append(" selected");
               }
               mb.append(">"+i+"</option>");
            }
            mb.append("</select>");

            mb.append("</font>");
            mb.append("</td>");

            mb.append("</tr>");


            //Title row
            mb.append("<tr>");
            mb.append("<td valign=top bgcolor=#e6e6e6>");
            mb.append("<font face=arial size=+1 color=#666666>");
            mb.append("Chart Type");
            mb.append("</font>");
            mb.append("</td>");
            mb.append("<td valign=top bgcolor=#e6e6e6>");
            mb.append("<font face=arial size=+1 color=#666666>");
            mb.append("Size of Chart");
            mb.append("</font>");
            mb.append("</td>");
            mb.append("<td valign=top bgcolor=#e6e6e6>");
            mb.append("<font face=arial size=+1 color=#666666>");
            mb.append("Handle Multiple Y Values For Same X Value By");
            mb.append("</font>");
            mb.append("</td>");
            mb.append("</tr>");


            //New row
            mb.append("<tr>");


            mb.append("<td valign=top>");
            mb.append("<font face=arial size=-1>");
            mb.append("<input name=charttype type=radio value="+MegaConstants.CHARTTYPELINE);
            if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPELINE){
                mb.append(" checked");
            }
            mb.append("> Line");
            mb.append("<br><input name=charttype type=radio value="+MegaConstants.CHARTTYPEBAR);
            if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPEBAR){
                mb.append(" checked");
            }
            mb.append("> Bar");
            mb.append("<br><input name=charttype type=radio value="+MegaConstants.CHARTTYPE3DBAR);
            if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPE3DBAR){
                mb.append(" checked");
            }
            mb.append("> 3D Bar");
            mb.append("<br><input name=charttype type=radio value="+MegaConstants.CHARTTYPESTACKEDBARCHART);
            if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHART){
                mb.append(" checked");
            }
            mb.append("> Stacked Bar");
            mb.append("<br><input name=charttype type=radio value="+MegaConstants.CHARTTYPESTACKEDBARCHART3D);
            if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHART3D){
                mb.append(" checked");
            }
            mb.append("> Stacked 3D Bar");
            mb.append("<br><input name=charttype type=radio value="+MegaConstants.CHARTTYPEHORIZONTALBAR);
            if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPEHORIZONTALBAR){
                mb.append(" checked");
            }
            mb.append("> Horizontal Bar");
            mb.append("<br><input name=charttype type=radio value="+MegaConstants.CHARTTYPEHORIZONTAL3DBAR);
            if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPEHORIZONTAL3DBAR){
                mb.append(" checked");
            }
            mb.append("> Horizontal 3D Bar");
            mb.append("<br><input name=charttype type=radio value="+MegaConstants.CHARTTYPESTACKEDBARCHARTHORIZONTAL);
            if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHARTHORIZONTAL){
                mb.append(" checked");
            }
            mb.append("> Stacked Horiz Bar");
            mb.append("<br><input name=charttype type=radio value="+MegaConstants.CHARTTYPESTACKEDBARCHART3DHORIZONTAL);
            if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHART3DHORIZONTAL){
                mb.append(" checked");
            }
            mb.append("> Stacked Horiz 3D Bar");
            mb.append("<br><input name=charttype type=radio value="+MegaConstants.CHARTTYPEPIE);
            if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPEPIE){
                mb.append(" checked");
            }
            mb.append("> Pie Chart");
            mb.append("<br><input name=charttype type=radio value="+MegaConstants.CHARTTYPE3DPIE);
            if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPE3DPIE){
                mb.append(" checked");
            }
            mb.append("> 3D Pie Chart");
            mb.append("<br><input name=charttype type=radio value="+MegaConstants.CHARTTYPESCATTERPLOT);
            if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPESCATTERPLOT){
                mb.append(" checked");
            }
            mb.append("> Scatter Plot");
            mb.append("<br><input name=charttype type=radio value="+MegaConstants.CHARTTYPESTEPCHART);
            if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPESTEPCHART){
                mb.append(" checked");
            }
            mb.append("> Step Chart");
            mb.append("<br><input name=charttype type=radio value="+MegaConstants.CHARTTYPEAREACHART);
            if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPEAREACHART){
                mb.append(" checked");
            }
            mb.append("> Area Chart");
            mb.append("<br><input name=charttype type=radio value="+MegaConstants.CHARTTYPESTACKEDAREA);
            if (megaChart.getChart().getCharttype()==MegaConstants.CHARTTYPESTACKEDAREA){
                mb.append(" checked");
            }
            mb.append("> Stacked Area Chart");
            mb.append("</font>");
            mb.append("</td>");


            mb.append("<td valign=top>");
                mb.append("<font face=arial size=-1>Chart size not configurable</font>");
            mb.append("</td>");

            mb.append("<td valign=top>");
            mb.append("<font face=arial size=-1>");
            mb.append("<input name=yaxiswhattodo type=radio value="+MegaConstants.YAXISWHATTODOAVG);
            if (megaChart.getChart().getYaxiswhattodo()==MegaConstants.YAXISWHATTODOAVG){
                mb.append(" checked");
            }
            mb.append("> Averaging Them");
            mb.append("<br><input name=yaxiswhattodo type=radio value="+MegaConstants.YAXISWHATTODOSUM);
            if (megaChart.getChart().getYaxiswhattodo()==MegaConstants.YAXISWHATTODOSUM){
                mb.append(" checked");
            }
            mb.append("> Adding Them Together");
            mb.append("</font>");
            mb.append("</td>");


            mb.append("</tr>");




            mb.append("</table>");


        mb.append("</td></tr>");





        //Raw Data Output
        //int megaChart.getChart().getXquestionid()=-1;
        //if (request.getParameter("megaChart.getChart().getXquestionid()")!=null && Num.isinteger(request.getParameter("megaChart.getChart().getXquestionid()"))){
        //    megaChart.getChart().getXquestionid()=Integer.parseInt(request.getParameter("megaChart.getChart().getXquestionid()"));
        //}
        //int[] megaChart.getYquestionid() = new int[1];
        //if (request.getParameter("megaChart.getYquestionid()")!=null && Num.isinteger(request.getParameter("megaChart.getYquestionid()"))){
        //    megaChart.getYquestionid()[0]=Integer.parseInt(request.getParameter("megaChart.getYquestionid()"));
        //}
        //int yaxiswhattodo=-1;
        //if (request.getParameter("yaxiswhattodo")!=null && Num.isinteger(request.getParameter("yaxiswhattodo"))){
        //    yaxiswhattodo=Integer.parseInt(request.getParameter("yaxiswhattodo"));
        //}
        //int megaChart.getChart().getChartsize()=-1;
        //if (request.getParameter("megaChart.getChart().getChartsize()")!=null && Num.isinteger(request.getParameter("megaChart.getChart().getChartsize()"))){
        //    megaChart.getChart().getChartsize()=Integer.parseInt(request.getParameter("megaChart.getChart().getChartsize()"));
        //}
        //Create a reger chart object
//            if (1==2){
//                //Now output with the new
//                mb.append("<tr><td bgcolor=#ffffff align=left valign=top>");
//                mb.append("<font face=arial size=+3>New MegaChartNew</font>");
//                mb.append("</td></tr>");
//                reger.executionTime executionTimeNew = new reger.executionTime();
//                //Go get the entries that relate to this graph
//                MegaChartEntryChooser entryChooser = new MegaChartEntryChooser(userSession, xLogid, yLogid, daterange, daterangesavedsearchid, lastxdays, lastxweeks, lastxmonths, lastxyears, daterangetoyyyy, daterangetomm, daterangetodd, daterangefromyyyy, daterangefrommm, daterangefromdd);
//                entryChooser.populate();
//                for(int j=0; j<megaChart.getYquestionid().length; j++){
//                    //Create the chart object
//                    MegaChartSeries megaChartSeries=new MegaChartSeries(userSession, megaChart.getChart().getXquestionid(), megaChart.getChart().getAppid(), megaChart.getYquestionid()[j], megaChart.getChart().getyLogid()[j], megaChart.getChart().getYaxiswhattodo(), megaChart.getChart().getChartsize(), megaChart.getChart().getDaterange(), megaChart.getChart().getLastxdays(), megaChart.getChart().getLastxweeks(), megaChart.getChart().getLastxmonths(), megaChart.getChart().getLastxyears(), megaChart.getChart().getDaterangefromyyyy(), megaChart.getChart().getDaterangefrommm(), megaChart.getChart().getDaterangefromdd(), megaChart.getChart().getDaterangetoyyyy(), megaChart.getChart().getDaterangetomm(), megaChart.getChart().getDaterangetodd(), megaChart.getChart().getDaterangesavedsearchid());
//                    //Output the data to the screen
//                    mb.append("<tr><td bgcolor=#ffffff align=left valign=top>");
//                    //@todo Figure out how to display the chart series name here.  Probably create another array along with rawChartData that holds the names and is indexed by the same number.
//                    mb.append("<font face=arial size=+1>Chart Data ("+j+")</font><br>");
//                    mb.append("<table cellpadding=2 cellspacing=1 border=0>");
//                    if (megaChartSeries.cleanData!=null && megaChartSeries.cleanData.length>0){
//                        for(int i=0; i<megaChartSeries.cleanData.length; i++){
//                            //@todo On date types convert back to date for display to user.
//                            mb.append("<tr><td valign=top align=left bgcolor=#e6e6e6>" + megaChartSeries.cleanData[i][1] + "</td><td valign=top align=left colspan=2>" + megaChartSeries.cleanData[i][2] + "</td></tr>");
//                        }
//                    }
//                    mb.append("</table>");
//
//                    //Show raw data for debugging purposes. Set showrawdata to true to show.  False to hide. Doi.
//                    boolean showrawdata=false;
//                    if (showrawdata) {
//                        mb.append("<font face=arial size=+1><br><br>Chart Raw Data ("+j+")</font><br>");
//                        mb.append("<table cellpadding=2 cellspacing=1 border=0>");
//                        if (megaChartSeries.rawChartData!=null && megaChartSeries.rawChartData.length>0){
//                            for(int i=0; i<megaChartSeries.rawChartData.length; i++){
//                                mb.append("<tr><td valign=top align=left bgcolor=#e6e6e6>" + megaChartSeries.rawChartData[i][1] + "</td><td valign=top align=left>" + megaChartSeries.rawChartData[i][2] + "</td><td valign=top align=left> Eventid:" + megaChartSeries.rawChartData[i][0] + "</td></tr>");
//                            }
//                        }
//                        mb.append("</table>");
//                    }
//
//                }
//                mb.append("<tr><td bgcolor=#ffffff align=left valign=top>");
//                mb.append("<font face=arial size=+2>"+executionTimeNew.getElapsed()+"</font>");
//                mb.append("</td></tr>");
//
//
//            }



        mb.append("</td></tr>");
        //Debug End

        mb.append("</table>");
        mb.append("</td></tr></table>");



        return mb.toString();
    }


}
