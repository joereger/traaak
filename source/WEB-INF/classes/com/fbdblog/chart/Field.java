package com.fbdblog.chart;

import com.fbdblog.util.Num;
import org.jdom.Element;
import org.apache.log4j.Logger;

/**
 *
 */
@Cacheable
public class Field implements Cloneable, FieldInterface {

    public static final int FIELDTYPEDROPDOWN = 1;
    public static final int FIELDTYPEHORIZONTALRADIOS = 2;
    public static final int FIELDTYPEVERTICALRADIOS = 3;
    public static final int FIELDTYPETEXTBOX = 5;
    public static final int FIELDTYPENUMERICRANGE = 7;
    public static final int FIELDTYPETIMEPERIOD = 8;

    public static final int DIRECTIONUP = 1;
    public static final int DIRECTIONDOWN = 2;
    public static final int DIRECTIONLEFT = 3;
    public static final int DIRECTIONRIGHT = 4;

    public static final int EDITFIELDASUSER = 1;
    public static final int EDITFIELDASSYSTEM = 2;

    //Database values for field
    public int questionid = 0;
    public int fieldtype = 0;
    public int eventtypeid = 0;
    public String fieldname = "";
    public String fielddescription = "";
    public int megadatatypeid = 0;
    public int isrequired = 0;


    public Field(){
        //Do nothing, default values for Field
    }

    public Field(int questionid){
        populateFromMegafieldid(questionid);
    }

    public Field(Field field){
        populateFromAnotherField(field);
    }

    public Object clone() {
        Logger logger = Logger.getLogger(this.getClass().getName());
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            logger.error(e);
        }
        return null;
    }

    public Field(int fieldtype, int eventtypeid, String fieldname, String fielddescription, int megadatatypeid, int isrequired){
        this.fieldtype = fieldtype;
        this.eventtypeid = eventtypeid;
        this.fieldname = fieldname;
        this.fielddescription = fielddescription;
        this.megadatatypeid = megadatatypeid;
        this.isrequired = isrequired;
    }

    public void populateFromAnotherField(Field field){
        this.questionid = field.questionid;
        this.fieldtype = field.fieldtype;
        this.eventtypeid = field.eventtypeid;
        this.fieldname = field.fieldname;
        this.fielddescription = field.fielddescription;
        this.megadatatypeid = field.megadatatypeid;
        this.isrequired = field.isrequired;
    }


    public void populateFromMegafieldid(int questionid){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("Field.java - questionid=" + questionid);
        //-----------------------------------
        //-----------------------------------
        String[][] rstField= Db.RunSQL("SELECT questionid, fieldtype, eventtypeid, fieldname, fielddescription, megadatatypeid, isrequired FROM megafield WHERE questionid='"+questionid+"'");
        //-----------------------------------
        //-----------------------------------
        if (rstField!=null && rstField.length>0){
        	for(int i=0; i<rstField.length; i++){
        	    if (Num.isinteger(rstField[i][0])){
        	        this.questionid = Integer.parseInt(rstField[i][0]);
                }
        	    if (Num.isinteger(rstField[i][1])){
                    fieldtype = Integer.parseInt(rstField[i][1]);
                }
                if (Num.isinteger(rstField[i][2])){
                    eventtypeid = Integer.parseInt(rstField[i][2]);
                }

                fieldname = rstField[i][3];
                fielddescription = rstField[i][4];
                if (Num.isinteger(rstField[i][5])){
                    megadatatypeid = Integer.parseInt(rstField[i][5]);
                }
                if (Num.isinteger(rstField[i][6])){
                    isrequired = Integer.parseInt(rstField[i][6]);
                }

                logger.debug("Field.java - found and populated field.<br>this.questionid=" + this.questionid + "<br>fieldname=" + fieldname);
        	}
        }
    }






    /**
     * Creates a new field in the database based on the data stored in this field at the moment of calling.
     */
    public void saveField(){
        Logger logger = Logger.getLogger(this.getClass().getName());

        StringBuffer debug = new StringBuffer();
        debug.append("Field.java - saveField() called on: " + fieldname + "<br>");
        debug.append("this.eventtypeid=" + this.eventtypeid + "<br>");
        debug.append("eventtypeid=" + eventtypeid + "<br>");

        if (fieldname==null || fieldname.equals("")) {
            fieldname="New Field";
        }

        if (megadatatypeid<=0) {
            megadatatypeid=DataTypeString.DATATYPEID;
        }

        saveMegafieldid();

        //Record debug
        logger.debug(debug.toString());


    }



    private void saveMegafieldid(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        //Try to update.
        StringBuffer debug = new StringBuffer();
        debug.append("Field.java - saveMegafieldid() trying to update megafield " + fieldname + "<br>this.eventtypeid=" + this.eventtypeid + "<br>");


        //-----------------------------------
        //-----------------------------------
        String[][] rstChk= Db.RunSQL("SELECT count(*) FROM megafield WHERE questionid='"+ questionid +"' AND eventtypeid='"+eventtypeid+"'");
        //-----------------------------------
        //-----------------------------------
        if (rstChk!=null && rstChk.length>0 && Integer.parseInt(rstChk[0][0])>0){
            //-----------------------------------
            //-----------------------------------
            int count = Db.RunSQLUpdate("UPDATE megafield SET fieldname='"+ Util.cleanForSQL(fieldname) +"', fielddescription='"+ Util.cleanForSQL(fielddescription) +"', fieldtype='"+ fieldtype +"', eventtypeid='"+ this.eventtypeid +"',  isrequired='"+ isrequired +"', megadatatypeid='"+ megadatatypeid +"' WHERE questionid='"+ questionid +"' AND eventtypeid='"+eventtypeid+"'");
            //-----------------------------------
            //-----------------------------------
        } else {
            debug.append("Field.java - saveField() creating new record " + fieldname+ "<br>");
            //Create the new field in the database
            //-----------------------------------
            //-----------------------------------
            int identity = Db.RunSQLInsert("INSERT INTO megafield(fieldtype, fieldname, fielddescription, isrequired, megadatatypeid, eventtypeid) VALUES('"+ fieldtype +"', '"+ Util.cleanForSQL(fieldname) +"', '"+ Util.cleanForSQL(fielddescription) +"', '"+ isrequired +"', '"+ megadatatypeid +"', '"+ this.eventtypeid +"')");
            //-----------------------------------
            //-----------------------------------

            this.questionid=identity;
        }




        debug.append("this.questionid=" + this.questionid);

        logger.debug(debug.toString());
    }







    


    public void delete(){
        //-----------------------------------
        //-----------------------------------
        int count = Db.RunSQLUpdate("DELETE FROM megafield WHERE questionid='"+questionid+"'");
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int count3 = Db.RunSQLUpdate("DELETE FROM megafieldparam WHERE questionid='"+questionid+"'");
        //-----------------------------------
        //-----------------------------------

        //Refresh any logs in the system that are using this field
        //For performance only refresh those affected
        //The order of the refresh is important too
        AllFieldsInSystem.refresh(questionid);
        AllMegaLogTypesInSystem.refresh(eventtypeid);
        LogCache.flushByMegafieldid(questionid);
    }




    public String fieldNamePre(int logidOrEtid){
        String out = this.questionid + "-logid-" + logidOrEtid + "-";
        return out;
    }

    public static int parseFieldidFromFieldName(String fieldName){
        int fieldid=-1;

        String[] split = fieldName.split("-");
        if (split.length>=3){
            if (Num.isinteger(split[2])){
                fieldid = Integer.parseInt(split[2]);
            }
        }

        return fieldid;
    }

    public static int parseLogidFromFieldName(String fieldName){
        int logid=-1;

        String[] split = fieldName.split("-");
        if (split.length>=5){
            if (Num.isinteger(split[4])){
                logid = Integer.parseInt(split[4]);
            }
        }

        return logid;
    }


    public int getMegafieldid() {
        return questionid;
    }

    public int getFieldtype() {
        return fieldtype;
    }

    public int getEventtypeid() {
        return eventtypeid;
    }


    public String getFieldname() {
        return fieldname;
    }

    /**
     * Strips spaces and any non alphanumeric chars to create a nice
     * <fieldname></fieldname> friendly name
     */
    public String getFieldnameForApis() {
        if (fieldname!=null){
            return Util.xmlFieldNameClean(fieldname);
        }
        return "";
    }

    public String getFielddescription() {
        return fielddescription;
    }

    public int getMegadatatypeid() {
        return megadatatypeid;
    }

    public int getIsrequired() {
        return isrequired;
    }



    public void setIsrequired(int isrequired) {
        this.isrequired = isrequired;
    }

    public void setFielddescription(String fielddescription) {
        this.fielddescription = fielddescription;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    public void setIsrequired(boolean isrequired) {
        if (isrequired){
            this.isrequired = 1;
        } else {
            this.isrequired = 0;
        }
    }

    public void setMegadatatypeid(int megadatatypeid) {
        this.megadatatypeid = megadatatypeid;
    }

    public void setEventtypeid(int eventtypeid) {
        this.eventtypeid = eventtypeid;
    }

    public void setFieldtype(int fieldtype) {
        this.fieldtype = fieldtype;
    }

    public void setMegafieldid(int questionid) {
        this.questionid = questionid;
    }

    public Element getXmlSchemaRepresentationOfField(Element fieldTypeSpecific) {
        Element elField = new Element("element", reger.MegaLogTypeXmlSchemaRenderer.xsNs);
        elField.setAttribute("name", getFieldnameForApis());
        //Fieldtype/ui-display-type
        //@todo Create a text list of field types (i.e. dropdown, checkboxes, radios, etc.)
        Element attr = new Element("attribute", reger.MegaLogTypeXmlSchemaRenderer.xsNs);
        attr.setAttribute("name","ui-display-type");
        attr.setAttribute("use","optional");
        elField.addContent(attr);
            Element st = new Element("simpleType", reger.MegaLogTypeXmlSchemaRenderer.xsNs);
            attr.addContent(st);
                Element rest = new Element("restriction", reger.MegaLogTypeXmlSchemaRenderer.xsNs);
                rest.setAttribute("base","xs:string");
                st.addContent(rest);
                    Element enum1 = new Element("enumeration", reger.MegaLogTypeXmlSchemaRenderer.xsNs);
                    enum1.setAttribute("name", "dropdown");
                    rest.addContent(enum1);
                    Element enum2 = new Element("enumeration", reger.MegaLogTypeXmlSchemaRenderer.xsNs);
                    enum2.setAttribute("name", "textbox");
                    rest.addContent(enum2);
                    Element enum3 = new Element("enumeration", reger.MegaLogTypeXmlSchemaRenderer.xsNs);
                    enum3.setAttribute("name", "radiobuttons");
                    rest.addContent(enum3);

        //Requiredness
        Element attr2 = new Element("attribute", reger.MegaLogTypeXmlSchemaRenderer.xsNs);
        attr2.setAttribute("name","required");
        attr2.setAttribute("use","optional");
        elField.addContent(attr2);
            Element st2 = new Element("simpleType", reger.MegaLogTypeXmlSchemaRenderer.xsNs);
            attr2.addContent(st2);
                Element rest2 = new Element("restriction", reger.MegaLogTypeXmlSchemaRenderer.xsNs);
                rest2.setAttribute("base","xs:boolean");
                st2.addContent(rest2);

        //Restrictions/datatype, created in FieldType
        elField.addContent(fieldTypeSpecific);


        return elField;
    }

}
