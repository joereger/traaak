package com.fbdblog.chart;

import org.apache.log4j.Logger;


public class ChartFieldFactory {
    /**
     * Accepts a fieldtype and returns a question type handler object.
     */
    public static ChartField getHandlerByFieldtype(int fieldtype){
        Logger logger = Logger.getLogger(ChartFieldFactory.class);
        if (fieldtype==MegaConstants.FIELDTYPEDROPDOWN){
            return new FieldTypeDropdown();
        } else if (fieldtype==MegaConstants.FIELDTYPEHORIZONTALRADIOS){
            return new FieldTypeHorizontalradios();
        } else if (fieldtype==MegaConstants.FIELDTYPENUMERICRANGE){
            return new FieldTypeNumericrange();
        } else if (fieldtype==MegaConstants.FIELDTYPETEXTBOX){
            return new FieldTypeTextbox();
        } else if (fieldtype==MegaConstants.FIELDTYPETIME){
            return new FieldTypeTimeperiod();
        } else if (fieldtype==MegaConstants.FIELDTYPEVERTICALRADIOS){
            return new FieldTypeVerticalradios();
        } else if (fieldtype==MegaConstants.XAXISENTRYORDER){
            return new ChartFieldEntryorder();
        } else if (fieldtype==MegaConstants.XAXISTIMEOFDAY) {
            return new ChartFieldEntryHourofday();
        } else if (fieldtype==MegaConstants.XAXISDAYOFWEEK) {
            return new ChartFieldEntryDayofweek();
        } else if (fieldtype==MegaConstants.XAXISDAYOFMONTH) {
            return new ChartFieldEntryDayofmonth();
        } else if (fieldtype==MegaConstants.XAXISCALENDARDAYS) {
            return new ChartFieldEntryDaysAgo();
        } else if (fieldtype==MegaConstants.XAXISCALENDARWEEKS) {
            return new ChartFieldEntryWeeksAgo();
        } else if (fieldtype==MegaConstants.XAXISCALENDARMONTHS) {
            return new ChartFieldEntryMonthsAgo();
        } else if (fieldtype==MegaConstants.XAXISDATETIME) {
            return new ChartFieldEntrydatetime();
        } else if (fieldtype==MegaConstants.YAXISCOUNT) {
            return new ChartFieldEntrycount();
        } else {
            logger.debug("No handler found: ChartFieldFactory.getHandlerByFieldtype - incoming fieldtype=" + fieldtype);
            return null;
        }
    }
}


