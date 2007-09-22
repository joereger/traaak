package com.fbdblog.chart;

import org.apache.log4j.Logger;


public class FieldTypeFactory {
    /**
     * Accepts a fieldtype and returns a FieldType handler object.
     * @param fieldtype
     */
    public static FieldType getHandlerByFieldtype(int fieldtype){
        Logger logger = Logger.getLogger(FieldTypeFactory.class);
        if (fieldtype==FieldType.FIELDTYPEDROPDOWN){
            return new FieldTypeDropdown();
        } else if (fieldtype==FieldType.FIELDTYPEHORIZONTALRADIOS){
            return new FieldTypeHorizontalradios();
        } else if (fieldtype==FieldType.FIELDTYPENUMERICRANGE){
            return new FieldTypeNumericrange();
        } else if (fieldtype==FieldType.FIELDTYPETEXTBOX){
            return new FieldTypeTextbox();
        } else if (fieldtype==FieldType.FIELDTYPETIME){
            return new FieldTypeTimeperiod();
        } else if (fieldtype==FieldType.FIELDTYPEVERTICALRADIOS){
            return new FieldTypeVerticalradios();
        } else if (fieldtype==FieldType.FIELDTYPECONTAINER){
            return new FieldTypeContainer();
        } else {
            logger.debug("No handler found: FieldTypeFactory.getHandlerByFieldtype - incoming fieldtype=" + fieldtype);
            return null;
        }
    }


    public static FieldType getCopyOfField(FieldType field){
        Logger logger = Logger.getLogger(FieldTypeFactory.class);
        if (field.getFieldtype()==FieldType.FIELDTYPEDROPDOWN){
            return new FieldTypeDropdown((FieldTypeDropdown)field);
        } else if (field.getFieldtype()==FieldType.FIELDTYPEHORIZONTALRADIOS){
            return new FieldTypeHorizontalradios((FieldTypeHorizontalradios)field);
        } else if (field.getFieldtype()==FieldType.FIELDTYPENUMERICRANGE){
            return new FieldTypeNumericrange((FieldTypeNumericrange)field);
        } else if (field.getFieldtype()==FieldType.FIELDTYPETEXTBOX){
            return new FieldTypeTextbox((FieldTypeTextbox)field);
        } else if (field.getFieldtype()==FieldType.FIELDTYPETIME){
            return new FieldTypeTimeperiod((FieldTypeTimeperiod)field);
        } else if (field.getFieldtype()==FieldType.FIELDTYPEVERTICALRADIOS){
            return new FieldTypeVerticalradios((FieldTypeVerticalradios)field);
        } else if (field.getFieldtype()==FieldType.FIELDTYPECONTAINER){
            return new FieldTypeContainer((FieldTypeContainer)field);
        } else {
            logger.debug("No handler found: FieldTypeFactory.getHandlerByFieldtype - incoming field.getFieldname()=" + field.getFieldname());
            return null;
        }
    }


    public static int[] getAllFieldTypes(){
        int[] fts = new int[6];
        fts[0] = FieldType.FIELDTYPEDROPDOWN;
        fts[1] = FieldType.FIELDTYPEHORIZONTALRADIOS;
        fts[2] = FieldType.FIELDTYPENUMERICRANGE;
        fts[3] = FieldType.FIELDTYPETEXTBOX;
        fts[4] = FieldType.FIELDTYPETIME;
        fts[5] = FieldType.FIELDTYPEVERTICALRADIOS;
        //fts[6] = FieldType.FIELDTYPECONTAINER;
        return fts;
    }

}


