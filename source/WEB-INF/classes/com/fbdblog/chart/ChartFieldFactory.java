package com.fbdblog.chart;

import org.apache.log4j.Logger;


import com.fbdblog.qtype.*;


public class ChartFieldFactory {
    /**
     * Accepts a fieldtype and returns a question type handler object.
     */
    public static ChartField getHandlerByFieldtype(int fieldtype){
        Logger logger = Logger.getLogger(ChartFieldFactory.class);
        if (fieldtype==ChartFieldEntryorder.ID){
            return new ChartFieldEntryorder();
        } else if (fieldtype==ChartFieldEntryHourofday.ID) {
            return new ChartFieldEntryHourofday();
        } else if (fieldtype==ChartFieldEntryDayofweek.ID) {
            return new ChartFieldEntryDayofweek();
        } else if (fieldtype==ChartFieldEntryDayofmonth.ID) {
            return new ChartFieldEntryDayofmonth();
        } else if (fieldtype==ChartFieldEntryDaysAgo.ID) {
            return new ChartFieldEntryDaysAgo();
        } else if (fieldtype==ChartFieldEntryWeeksAgo.ID) {
            return new ChartFieldEntryWeeksAgo();
        } else if (fieldtype==ChartFieldEntryMonthsAgo.ID) {
            return new ChartFieldEntryMonthsAgo();
        } else if (fieldtype==ChartFieldEntrydatetime.ID) {
            return new ChartFieldEntrydatetime();
        } else if (fieldtype==ChartFieldEntrycount.ID) {
            return new ChartFieldEntrycount();
        } else if (fieldtype== Checkboxes.ID) {
            return new Checkboxes();
        } else if (fieldtype== Dropdown.ID) {
            return new Dropdown();
        } else if (fieldtype== DropdownComplex.ID) {
            return new DropdownComplex();
        } else if (fieldtype== Essay.ID) {
            return new Essay();
        } else if (fieldtype== Matrix.ID) {
            return new Matrix();
        } else if (fieldtype== Range.ID) {
            return new Range();
        } else if (fieldtype== Textbox.ID) {
            return new Textbox();
        } else if (fieldtype== Timeperiod.ID) {
            return new Timeperiod();
        } else {
            logger.debug("No handler found: ChartFieldFactory.getHandlerByFieldtype - incoming fieldtype=" + fieldtype);
            return null;
        }
    }
}


