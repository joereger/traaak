package com.fbdblog.chart;

import org.jdom.Element;

/**
 *
 */

public interface FieldInterface {

    public Object clone();

    public void populateFromAnotherField(Field field);


    public void populateFromMegafieldid(int questionid);

    

    

    /**
     * Creates a new field in the database based on the data stored in this field at the moment of calling.
     */
    public void saveField();

    //public void delete();


    public String fieldNamePre(int logidOrEtid);

    public int getMegafieldid();

    public int getFieldtype();

    public int getEventtypeid();

    public String getFieldname();

    public String getFieldnameForApis();

    public String getFielddescription();

    public int getMegadatatypeid();

    public int getIsrequired();

    public void setIsrequired(int isrequired);

    public void setFielddescription(String fielddescription);

    public void setFieldname(String fieldname);

    public void setFieldtype(int fieldtypeid);

    public void setEventtypeid(int eventtypeid);

    public void setMegadatatypeid(int megadatatypeid);

    public void setIsrequired(boolean isrequired);



    /**
     * Returns a jdom element describing the XML SCHEMA of this field type.
     */
     public Element getXmlSchemaRepresentationOfField(Element restrictions);

}
