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
        StringBuffer urlP=new StringBuffer();

        //Working
        //http://bob.reger.com/graph.log?xMegafieldChoice=-7_0_0&yMegafieldChoice=108_2_108&yMegafieldChoice=113_2_113&yMegafieldChoice=101_2_101&yaxiswhattodo=2&chartsize=3&charttype=12&daterange=1&lastxdays=1&lastxweeks=1&lastxmonths=16&lastxyears=1&daterangefromyyyy=2003&daterangefrommm=4&daterangefromdd=8&daterangetoyyyy=2004&daterangetomm=4&daterangetodd=8&daterangesavedsearchid=1&
        //Broken
        //http://bob.reger.com/graph.log?xMegafieldChoice=-7_0_16&yMegafieldChoice=113_0_16&yMegafieldChoice=101_0_16&yMegafieldChoice=108_0_16&yaxiswhattodo=2&chartsize=3&charttype=12&daterange=1&lastxdays=1&lastxweeks=1&lastxmonths=16&lastxyears=1&daterangefromyyyy=2003&daterangefrommm=4&daterangefromdd=8&daterangetoyyyy=2004&daterangetomm=4&daterangetodd=8&daterangesavedsearchid=0&
        urlP.append("xMegafieldChoice="+ megaChart.getXQuestionid()+"_"+appid+"_"+appid +"&");
        for (int i = 0; i < megaChart.getYQuestionid().length; i++) {
            int tmp = megaChart.getYQuestionid()[i];
            urlP.append("yMegafieldChoice="+ tmp+"_"+appid+"_"+appid +"&");
        }
        urlP.append("yaxiswhattodo="+ megaChart.getYaxiswhattodo() +"&");
        urlP.append("chartsize="+ megaChart.getChartsize() +"&");
        urlP.append("charttype="+ megaChart.getCharttype() +"&");
        urlP.append("daterange="+ megaChart.getDaterange() +"&");
        urlP.append("lastxdays="+ megaChart.getLastxdays() +"&");
        urlP.append("lastxweeks="+ megaChart.getLastxweeks() +"&");
        urlP.append("lastxmonths="+ megaChart.getLastxmonths() +"&");
        urlP.append("lastxyears="+ megaChart.getLastxyears() +"&");
        urlP.append("daterangefromyyyy="+ megaChart.getDaterangefromyyyy() +"&");
        urlP.append("daterangefrommm="+ megaChart.getDaterangefrommm() +"&");
        urlP.append("daterangefromdd="+ megaChart.getDaterangefromdd() +"&");
        urlP.append("daterangetoyyyy="+ megaChart.getDaterangetoyyyy() +"&");
        urlP.append("daterangetomm="+ megaChart.getDaterangetomm() +"&");
        urlP.append("daterangetodd="+ megaChart.getDaterangetodd() +"&");
        urlP.append("daterangesavedsearchid="+ megaChart.getDaterangesavedsearchid() +"&");



        mb.append("<tr><td bgcolor=#ffffff align=center valign=top>");
        //Chart title
        mb.append("<font face=arial size=+1 class=bigfont>");
        if (!megaChart.getChartname().equals("")){
            mb.append(megaChart.getChartname());
        } else {
            mb.append("Custom Graph");
        }
        mb.append("</font><br>");

        //Chart image
        mb.append("<img src='/graph.log?"+urlP+"' border=0>");
        mb.append("</td></tr>");


        //The goal here is to display any situation where I'm diverging from what the user has selected on the form.
        //For example, when you choose multiple Y-Axis checkboxes and a pie chart type I'll only use the last y-Axis selected in the list.
        //So I should display a message to that effect.

        //charttype=pie or 3dpie and multiple Y-Axis values are chosen.  I only use last Y-Axis.
        if (megaChart.getYQuestionid().length==1 && megaChart.getYQuestionid()[0]==0){
            mb.append("<tr><td bgcolor=#ffffff>");
            mb.append("No Y-Axis is selected.  Please choose at least one below.");
            mb.append("</td></tr>");
        }

        //xQuestionid=Date/Time and anything other than charttype=Line is selected.  I override with Line chart type.
        if (megaChart.getXQuestionid()==MegaConstants.XAXISDATETIME && megaChart.getCharttype()!=MegaConstants.CHARTTYPELINE){
            mb.append("<tr><td bgcolor=#ffffff>");
            mb.append("Quick note: When you choose Date/Time for the X-Axis you can only choose a Chart Type of Line.  We've automatically adjusted for you.");
            mb.append("</td></tr>");
        }

        //xQuestionid=Day of the Week and anything other than charttype=bar, 3dbar, stacked bar, horizontal bar, horizontal3d bar
        if (megaChart.getXQuestionid()==MegaConstants.XAXISDAYOFWEEK && !(megaChart.getCharttype()==MegaConstants.CHARTTYPE3DBAR || megaChart.getCharttype()==MegaConstants.CHARTTYPEHORIZONTALBAR || megaChart.getCharttype()==MegaConstants.CHARTTYPEHORIZONTAL3DBAR || megaChart.getCharttype()==MegaConstants.CHARTTYPEBAR  || megaChart.getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHART  || megaChart.getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHART3D || megaChart.getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHARTHORIZONTAL || megaChart.getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHART3DHORIZONTAL)) {
            mb.append("<tr><td bgcolor=#ffffff>");
            mb.append("Quick note: When you choose Day of the Week for the X-Axis you can only choose one of the Bar chart types.  We've automatically adjusted for you.");
            mb.append("</td></tr>");
        }

        //megaChart.getCharttype()=pie or 3dpie and multiple Y-Axis values are chosen.  I only use last Y-Axis.
        if ((megaChart.getCharttype()==MegaConstants.CHARTTYPE3DPIE || megaChart.getCharttype()==MegaConstants.CHARTTYPEPIE) && (megaChart.getYQuestionid().length>1)){
            mb.append("<tr><td bgcolor=#ffffff>");
            mb.append("Quick note: When you choose multiple Y-Axis series and a Chart Type of Pie or 3D Pie only the last Y-Axis selected will be charted.");
            mb.append("</td></tr>");
        }


        //A dropdown formatting string
        String dropdownstyle="style=\"font-family: Arial, Helvetica, sans-serif; font-size: 10px; color: #000000; background-color: #ffffff; border: #000000; border-style: solid; border-top-width: 1px; border-right-width: 1px; border-bottom-width: 1px; border-left-width: 1px\"";

        //Create the chart-customizing form
        mb.append("<tr><td bgcolor=#ffffff>");

            mb.append("<form action=graphs-detail.log method=get>");
            mb.append("<center><input type=submit value='Redraw Graph'></center>");


            mb.append("<table cellpadding=8 width=100% cellspacing=1 border=0>");




            //Save chart?
            mb.append("<tr>");
            mb.append("<td valign=top bgcolor=#ffffff colspan=3>");
            mb.append("<center>");
            mb.append("<input type=checkbox name=savechart value=1>");
            mb.append("<font face=arial size=-1>");
            mb.append("Save this graph and name it:<br>");
            mb.append("<input type=textbox name=chartname value=\""+ Str.cleanForHtml(megaChart.getChartname())+"\" size=25 maxlength=100>");
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

            logger.debug("MegaChartHtmlRenderer.java: xQuestionid="+megaChart.getXQuestionid());

            mb.append("<font face=arial size=-1 class=smallfont>");
            mb.append("<b>");
            mb.append("System Fields");
            mb.append("</b>");
            mb.append("</font>");
            mb.append("</br>");

            mb.append("<font face=arial size=-1>");
            //Derived xAxis types
            mb.append("<input name=xMegafieldChoice type=radio value='"+MegaConstants.XAXISDATETIME+"_0_0'");
            if (megaChart.getXQuestionid()==MegaConstants.XAXISDATETIME){
                mb.append(" checked");
            }
            mb.append("> Exact Date/Time<br>");
            mb.append("<input name=xMegafieldChoice type=radio value='"+MegaConstants.XAXISCALENDARDAYS+"_0_0'");
            if (megaChart.getXQuestionid()==MegaConstants.XAXISCALENDARDAYS){
                mb.append(" checked");
            }
            mb.append("> Days Ago<br>");
            mb.append("<input name=xMegafieldChoice type=radio value='"+MegaConstants.XAXISCALENDARWEEKS+"_0_0'");
            if (megaChart.getXQuestionid()==MegaConstants.XAXISCALENDARWEEKS){
                mb.append(" checked");
            }
            mb.append("> Weeks Ago<br>");
            mb.append("<input name=xMegafieldChoice type=radio value='"+MegaConstants.XAXISCALENDARMONTHS+"_0_0'");
            if (megaChart.getXQuestionid()==MegaConstants.XAXISCALENDARMONTHS){
                mb.append(" checked");
            }
            mb.append("> Months Ago<br>");
            mb.append("<input name=xMegafieldChoice type=radio value='"+MegaConstants.XAXISTIMEOFDAY+"_0_0'");
            if (megaChart.getXQuestionid()==MegaConstants.XAXISTIMEOFDAY){
                mb.append(" checked");
            }
            mb.append("> Hour of the Day<br>");
            mb.append("<input name=xMegafieldChoice type=radio value='"+MegaConstants.XAXISDAYOFWEEK+"_0_0'");
            if (megaChart.getXQuestionid()==MegaConstants.XAXISDAYOFWEEK){
                mb.append(" checked");
            }
            mb.append("> Day of the Week<br>");
            mb.append("<input name=xMegafieldChoice type=radio value='"+MegaConstants.XAXISDAYOFMONTH+"_0_0'");
            if (megaChart.getXQuestionid()==MegaConstants.XAXISDAYOFMONTH){
                mb.append(" checked");
            }
            mb.append("> Day of the Month<br>");
            mb.append("<input name=xMegafieldChoice type=radio value='"+MegaConstants.XAXISENTRYORDER+"_0_0'");
            if (megaChart.getXQuestionid()==MegaConstants.XAXISENTRYORDER){
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
                String xMegaFieldChoiceString = question.getQuestionid()+"_"+0+"_"+appid;
                mb.append("<font face=arial size=-1 class=smallfont>");
                mb.append("<input name=xMegafieldChoice type=radio value='"+xMegaFieldChoiceString+"'");
                if (megaChart.getXQuestionid()==question.getQuestionid()){
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
            mb.append("<input name=yMegafieldChoice type=checkbox value='"+MegaConstants.YAXISCOUNT+"_0_0'");
            for(int j=0; j<megaChart.getYQuestionid().length; j++){
                if (megaChart.getYQuestionid()[j]==MegaConstants.YAXISCOUNT){
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
                    String yMegaFieldChoiceString = question.getQuestionid()+"_"+0+"_"+appid;
                    mb.append("<font face=arial size=-1 class=smallfont>");
                    mb.append("<input name=yMegafieldChoice type=checkbox value='"+yMegaFieldChoiceString+"'");
                    for(int l=0; l<megaChart.getYQuestionid().length; l++){
                        if (megaChart.getYQuestionid()[l]==question.getQuestionid()){
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
            if (megaChart.getDaterange()==MegaConstants.DATERANGEALLTIME){
                mb.append(" checked");
            }
            mb.append("> All Time");
            //
            mb.append("<br><input name=daterange type=radio value="+MegaConstants.DATERANGETHISWEEK);
            if (megaChart.getDaterange()==MegaConstants.DATERANGETHISWEEK){
                mb.append(" checked");
            }
            mb.append("> This Week");
            //
            mb.append("<br><input name=daterange type=radio value="+MegaConstants.DATERANGETHISMONTH);
            if (megaChart.getDaterange()==MegaConstants.DATERANGETHISMONTH){
                mb.append(" checked");
            }
            mb.append("> This Month");
            //
            mb.append("<br><input name=daterange type=radio value="+MegaConstants.DATERANGETHISYEAR);
            if (megaChart.getDaterange()==MegaConstants.DATERANGETHISYEAR){
                mb.append(" checked");
            }
            mb.append("> This Year");
            //
            mb.append("<br><input name=daterange type=radio value="+MegaConstants.DATERANGELASTWEEK);
            if (megaChart.getDaterange()==MegaConstants.DATERANGELASTWEEK){
                mb.append(" checked");
            }
            mb.append("> Last Week");
            //
            mb.append("<br><input name=daterange type=radio value="+MegaConstants.DATERANGELASTMONTH);
            if (megaChart.getDaterange()==MegaConstants.DATERANGELASTMONTH){
                mb.append(" checked");
            }
            mb.append("> Last Month");
            //
            mb.append("<br><input name=daterange type=radio value="+MegaConstants.DATERANGELASTXDAYS);
            if (megaChart.getDaterange()==MegaConstants.DATERANGELASTXDAYS){
                mb.append(" checked");
            }
            mb.append("> Last ");
            mb.append("<select name=lastxdays "+dropdownstyle+">");
            for(int i=0; i<60; i++){
               mb.append("<option value="+i);
               if (megaChart.getLastxdays()==i){
                   mb.append(" selected");
               }
               mb.append(">"+i+"</option>");
            }
            mb.append("</select>");
            mb.append(" Day(s)");
            //
            mb.append("<br><input name=daterange type=radio value="+MegaConstants.DATERANGELASTXWEEKS);
            if (megaChart.getDaterange()==MegaConstants.DATERANGELASTXWEEKS){
                mb.append(" checked");
            }
            mb.append("> Last ");
            mb.append("<select name=lastxweeks "+dropdownstyle+">");
            for(int i=0; i<16; i++){
               mb.append("<option value="+i);
               if (megaChart.getLastxweeks()==i){
                   mb.append(" selected");
               }
               mb.append(">"+i+"</option>");
            }
            mb.append("</select>");
            mb.append(" Week(s)");
            //
            mb.append("<br><input name=daterange type=radio value="+MegaConstants.DATERANGELASTXMONTHS);
            if (megaChart.getDaterange()==MegaConstants.DATERANGELASTXMONTHS){
                mb.append(" checked");
            }
            mb.append("> Last ");
            mb.append("<select name=lastxmonths "+dropdownstyle+">");
            for(int i=0; i<24; i++){
               mb.append("<option value="+i);
               if (megaChart.getLastxmonths()==i){
                   mb.append(" selected");
               }
               mb.append(">"+i+"</option>");
            }
            mb.append("</select>");
            mb.append(" Month(s)");
            //
            mb.append("<br><input name=daterange type=radio value="+MegaConstants.DATERANGELASTXYEARS);
            if (megaChart.getDaterange()==MegaConstants.DATERANGELASTXYEARS){
                mb.append(" checked");
            }
            mb.append("> Last ");
            mb.append("<select name=lastxyears "+dropdownstyle+">");
            for(int i=0; i<100; i++){
               mb.append("<option value="+i);
               if (megaChart.getLastxyears()==i){
                   mb.append(" selected");
               }
               mb.append(">"+i+"</option>");
            }
            mb.append("</select>");
            mb.append(" Year(s)");
            //
            mb.append("<br><input name=daterange type=radio value="+MegaConstants.DATERANGESPECIFIED);
            if (megaChart.getDaterange()==MegaConstants.DATERANGESPECIFIED){
                mb.append(" checked");
            }
            mb.append("> Date Range:");
            mb.append("<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;From:<br>");
            mb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            mb.append("<select name=daterangefromyyyy "+dropdownstyle+">");
            for(int i=1900; i<=2010; i++){
               mb.append("<option value="+i);
               if (megaChart.getDaterangefromyyyy()==i){
                   mb.append(" selected");
               }
               mb.append(">"+i+"</option>");
            }
            mb.append("</select>");
            mb.append(" / ");
            mb.append("<select name=daterangefrommm "+dropdownstyle+">");
            for(int i=1; i<=12; i++){
               mb.append("<option value="+i);
               if (megaChart.getDaterangefrommm()==i){
                   mb.append(" selected");
               }
               mb.append(">"+i+"</option>");
            }
            mb.append("</select>");
            mb.append(" / ");
            mb.append("<select name=daterangefromdd "+dropdownstyle+">");
            for(int i=0; i<=31; i++){
               mb.append("<option value="+i);
               if (megaChart.getDaterangefromdd()==i){
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
               if (megaChart.getDaterangetoyyyy()==i){
                   mb.append(" selected");
               }
               mb.append(">"+i+"</option>");
            }
            mb.append("</select>");
            mb.append(" / ");
            mb.append("<select name=daterangetomm "+dropdownstyle+">");
            for(int i=1; i<=12; i++){
               mb.append("<option value="+i);
               if (megaChart.getDaterangetomm()==i){
                   mb.append(" selected");
               }
               mb.append(">"+i+"</option>");
            }
            mb.append("</select>");
            mb.append(" / ");
            mb.append("<select name=daterangetodd "+dropdownstyle+">");
            for(int i=1; i<=31; i++){
               mb.append("<option value="+i);
               if (megaChart.getDaterangetodd()==i){
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
            if (megaChart.getCharttype()==MegaConstants.CHARTTYPELINE){
                mb.append(" checked");
            }
            mb.append("> Line");
            mb.append("<br><input name=charttype type=radio value="+MegaConstants.CHARTTYPEBAR);
            if (megaChart.getCharttype()==MegaConstants.CHARTTYPEBAR){
                mb.append(" checked");
            }
            mb.append("> Bar");
            mb.append("<br><input name=charttype type=radio value="+MegaConstants.CHARTTYPE3DBAR);
            if (megaChart.getCharttype()==MegaConstants.CHARTTYPE3DBAR){
                mb.append(" checked");
            }
            mb.append("> 3D Bar");
            mb.append("<br><input name=charttype type=radio value="+MegaConstants.CHARTTYPESTACKEDBARCHART);
            if (megaChart.getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHART){
                mb.append(" checked");
            }
            mb.append("> Stacked Bar");
            mb.append("<br><input name=charttype type=radio value="+MegaConstants.CHARTTYPESTACKEDBARCHART3D);
            if (megaChart.getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHART3D){
                mb.append(" checked");
            }
            mb.append("> Stacked 3D Bar");
            mb.append("<br><input name=charttype type=radio value="+MegaConstants.CHARTTYPEHORIZONTALBAR);
            if (megaChart.getCharttype()==MegaConstants.CHARTTYPEHORIZONTALBAR){
                mb.append(" checked");
            }
            mb.append("> Horizontal Bar");
            mb.append("<br><input name=charttype type=radio value="+MegaConstants.CHARTTYPEHORIZONTAL3DBAR);
            if (megaChart.getCharttype()==MegaConstants.CHARTTYPEHORIZONTAL3DBAR){
                mb.append(" checked");
            }
            mb.append("> Horizontal 3D Bar");
            mb.append("<br><input name=charttype type=radio value="+MegaConstants.CHARTTYPESTACKEDBARCHARTHORIZONTAL);
            if (megaChart.getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHARTHORIZONTAL){
                mb.append(" checked");
            }
            mb.append("> Stacked Horiz Bar");
            mb.append("<br><input name=charttype type=radio value="+MegaConstants.CHARTTYPESTACKEDBARCHART3DHORIZONTAL);
            if (megaChart.getCharttype()==MegaConstants.CHARTTYPESTACKEDBARCHART3DHORIZONTAL){
                mb.append(" checked");
            }
            mb.append("> Stacked Horiz 3D Bar");
            mb.append("<br><input name=charttype type=radio value="+MegaConstants.CHARTTYPEPIE);
            if (megaChart.getCharttype()==MegaConstants.CHARTTYPEPIE){
                mb.append(" checked");
            }
            mb.append("> Pie Chart");
            mb.append("<br><input name=charttype type=radio value="+MegaConstants.CHARTTYPE3DPIE);
            if (megaChart.getCharttype()==MegaConstants.CHARTTYPE3DPIE){
                mb.append(" checked");
            }
            mb.append("> 3D Pie Chart");
            mb.append("<br><input name=charttype type=radio value="+MegaConstants.CHARTTYPESCATTERPLOT);
            if (megaChart.getCharttype()==MegaConstants.CHARTTYPESCATTERPLOT){
                mb.append(" checked");
            }
            mb.append("> Scatter Plot");
            mb.append("<br><input name=charttype type=radio value="+MegaConstants.CHARTTYPESTEPCHART);
            if (megaChart.getCharttype()==MegaConstants.CHARTTYPESTEPCHART){
                mb.append(" checked");
            }
            mb.append("> Step Chart");
            mb.append("<br><input name=charttype type=radio value="+MegaConstants.CHARTTYPEAREACHART);
            if (megaChart.getCharttype()==MegaConstants.CHARTTYPEAREACHART){
                mb.append(" checked");
            }
            mb.append("> Area Chart");
            mb.append("<br><input name=charttype type=radio value="+MegaConstants.CHARTTYPESTACKEDAREA);
            if (megaChart.getCharttype()==MegaConstants.CHARTTYPESTACKEDAREA){
                mb.append(" checked");
            }
            mb.append("> Stacked Area Chart");
            mb.append("</font>");
            mb.append("</td>");


            mb.append("<td valign=top>");
            mb.append("<font face=arial size=-1>");
            mb.append("<input name=chartsize type=radio value="+MegaConstants.CHARTSIZEMINISCULE);
            if (megaChart.getChartsize()==MegaConstants.CHARTSIZEMINISCULE){
                mb.append(" checked");
            }
            mb.append("> Miniscule");
            mb.append("<br><input name=chartsize type=radio value="+MegaConstants.CHARTSIZESMALL);
            if (megaChart.getChartsize()==MegaConstants.CHARTSIZESMALL){
                mb.append(" checked");
            }
            mb.append("> Small");
            mb.append("<br><input name=chartsize type=radio value="+MegaConstants.CHARTSIZEMEDIUM);
            if (megaChart.getChartsize()==MegaConstants.CHARTSIZEMEDIUM){
                mb.append(" checked");
            }
            mb.append("> Medium");
            mb.append("<br><input name=chartsize type=radio value="+MegaConstants.CHARTSIZELARGE);
            if (megaChart.getChartsize()==MegaConstants.CHARTSIZELARGE){
                mb.append(" checked");
            }
            mb.append("> Large");
            mb.append("<br><input name=chartsize type=radio value="+MegaConstants.CHARTSIZEMASSIVE);
            if (megaChart.getChartsize()==MegaConstants.CHARTSIZEMASSIVE){
                mb.append(" checked");
            }
            mb.append("> Massive");
            mb.append("</font>");
            mb.append("</td>");

            mb.append("<td valign=top>");
            mb.append("<font face=arial size=-1>");
            mb.append("<input name=yaxiswhattodo type=radio value="+MegaConstants.YAXISWHATTODOAVG);
            if (megaChart.getYaxiswhattodo()==MegaConstants.YAXISWHATTODOAVG){
                mb.append(" checked");
            }
            mb.append("> Averaging Them");
            mb.append("<br><input name=yaxiswhattodo type=radio value="+MegaConstants.YAXISWHATTODOSUM);
            if (megaChart.getYaxiswhattodo()==MegaConstants.YAXISWHATTODOSUM){
                mb.append(" checked");
            }
            mb.append("> Adding Them Together");
            //@todo Consider allowing the user to return percentages of total.
            mb.append("</font>");
            mb.append("</td>");


            mb.append("</tr>");




            mb.append("</table>");

            mb.append("</form>");

        mb.append("</td></tr>");





        //Raw Data Output
        //int megaChart.getXQuestionid()=-1;
        //if (request.getParameter("megaChart.getXQuestionid()")!=null && Num.isinteger(request.getParameter("megaChart.getXQuestionid()"))){
        //    megaChart.getXQuestionid()=Integer.parseInt(request.getParameter("megaChart.getXQuestionid()"));
        //}
        //int[] megaChart.getYQuestionid() = new int[1];
        //if (request.getParameter("megaChart.getYQuestionid()")!=null && Num.isinteger(request.getParameter("megaChart.getYQuestionid()"))){
        //    megaChart.getYQuestionid()[0]=Integer.parseInt(request.getParameter("megaChart.getYQuestionid()"));
        //}
        //int yaxiswhattodo=-1;
        //if (request.getParameter("yaxiswhattodo")!=null && Num.isinteger(request.getParameter("yaxiswhattodo"))){
        //    yaxiswhattodo=Integer.parseInt(request.getParameter("yaxiswhattodo"));
        //}
        //int megaChart.getChartsize()=-1;
        //if (request.getParameter("megaChart.getChartsize()")!=null && Num.isinteger(request.getParameter("megaChart.getChartsize()"))){
        //    megaChart.getChartsize()=Integer.parseInt(request.getParameter("megaChart.getChartsize()"));
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
//                for(int j=0; j<megaChart.getYQuestionid().length; j++){
//                    //Create the chart object
//                    MegaChartSeries megaChartSeries=new MegaChartSeries(userSession, megaChart.getXQuestionid(), megaChart.getAppid(), megaChart.getYQuestionid()[j], megaChart.getyLogid()[j], megaChart.getYaxiswhattodo(), megaChart.getChartsize(), megaChart.getDaterange(), megaChart.getLastxdays(), megaChart.getLastxweeks(), megaChart.getLastxmonths(), megaChart.getLastxyears(), megaChart.getDaterangefromyyyy(), megaChart.getDaterangefrommm(), megaChart.getDaterangefromdd(), megaChart.getDaterangetoyyyy(), megaChart.getDaterangetomm(), megaChart.getDaterangetodd(), megaChart.getDaterangesavedsearchid());
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
