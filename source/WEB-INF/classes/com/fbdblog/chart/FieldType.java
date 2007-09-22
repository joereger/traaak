package com.fbdblog.chart;

import org.jdom.Element;

import java.util.ArrayList;

/**
 * The iterface represents field types like textbox, dropdown, time, etc.
 * FieldTypes are generally created by extending the Field class and implementing the FieldType class.
 * FieldTypes do not generally talk directly to the database... they rely on StorageMethods (essentially DAOs)
 * to get data.
 */
public interface FieldType extends FieldInterface {

    //Megafieldtype. The name is the field type.  The value is the megafieldtypeid in the megafieldtype table in the database.
    public static final int FIELDTYPEDROPDOWN = 1;
    public static final int FIELDTYPEHORIZONTALRADIOS = 2;
    public static final int FIELDTYPEVERTICALRADIOS = 3;
    public static final int FIELDTYPETEXTBOX = 5;
    public static final int FIELDTYPENUMERICRANGE = 7;
    public static final int FIELDTYPETIME = 8;
    public static final int FIELDTYPECONTAINER = 9;
    //Charting x Axis derived xQuestionid values.  These must be negative
    public static final int XAXISDATETIME = -2;
    public static final int XAXISTIMEOFDAY = -3;
    public static final int XAXISENTRYORDER = -4;
    public static final int XAXISDAYOFWEEK = -5;
    public static final int XAXISDAYOFMONTH = -6;
    public static final int XAXISCALENDARWEEKS = -7;
    public static final int XAXISCALENDARMONTHS = -8;
    public static final int XAXISCALENDARDAYS = -9;
    //yAxis Default to all
    public static final int YAXISCOUNT = -10;



    /**
     * Friendly name of this FieldType.
     * For example: Dropdown List
     */
    String getBackEndFriendlyName();

    /**
     * Description of this FieldType.
     * For example: This is a dropdown field.
     */
    String getBackEndDescription();


    /**
     * Called to load possible values and do any other startup work.
     */
    void initialize();


    /**
     * An array of acceptable datatypes for this field
     */
    int[] getAcceptableDataTypes();

    /**
     * The default datatype for this field
     */
    int getDefaultDataType();

    /**
     * Returns an array of FieldData objects which represent the data held in this field.
     */
    ArrayList<FieldData> getDataForField();

    /**
     * Returns an array of empty FieldData objects that demonstrate the name/value pairs that this field generates/accepts/works with
     */
    ArrayList<FieldData> getEmptyDataFields();


    /**
     * Returns html to display this field in the admin.
     * This includes whatever is required to get the user to enter data.
     */
    String getHtmlAdmin(int logid, boolean isFormActive);

    /**
     * Returns html to display this field to the public.
     */
    String getHtmlPublic(int logid);



    /**
     * Sample add/edit html for this type of field.
     */
     String getHtmlAdminSample();

     /**
     * Sample public html for this type of field.
     */
     String getHtmlPublicSample();


    /**
     * Accepts an http request and populates the incoming data of this object from that request.
     */
     void populateFromRequest(javax.servlet.http.HttpServletRequest request);

    /**
     * Populate the data for this field for a particular eventid.
     * Use a FieldDAO object to do this.
     */
     void loadDataForEventid(int eventid, int logid);

     /**
     * Populate the default data for this field for a particular eventid.
     * Use a FieldDAO object to do this.
     */
     void loadDefaultData(int logid);

     /**
     * Validate the currently stored value in the field.
     */
     String validateCurrentData();

     /**
     * Save the current field value to the database
     */
     void saveToDb(int eventid, int logid);

     /**
     * Save the current field value as a default to the database
     */
     void saveDefaultToDb(int logid);



    /**
     * Delete the data for this field from the database.
     * Typically a FieldDAO object is used so that actual database
     * code is not needed here.
     */
     void deleteData(int eventid);



    /**
     * Whether or not any data exists in the database for this questionid.
     * This is a precursor to either deletion or hiding
     */
    boolean isThereDataForFieldInDB();

    /**
     * Move field data to another logid.
     * With some fieldtypes there is nothing to do as
     * the data is simply tied to eventid.
     * In other cases, like megaoption, the logid is part of the options and must be moved.
     */
     void moveDataToAnotherLog(int eventid, int oldlogid, int newlogid);

    /**
     * Move to new eventid.  All data will be moved.  Sourceeventid will no longer have the data.
     */
    public void moveDataToNewEventid(int sourceeventid, int desteventid);

    /**
     * Copy to new eventid.  All data will be copied.  Sourceeventid will still have the data.
     */
    public void copyDataToNewEventid(int sourceeventid, int desteventid);


    /**
     * Deletes a field, including all data.
     * First, call the DAO's deleteAllDataForAFieldAcrossAllLogsAndEntries method.
     * Next, call the Field object's delete() object.
     * There isn't much logic in this method for FieldType... it's just a caller back
     * to the core and ahead to the DAO.
     */
    void deleteField();

    /**
     * Fields can have default system values and options (data) that stay with
     * them for all users.  This data is setup with the logtype (not log) on
     * logs-field.log.  The type of data that can be stored is dependent
     * on the FieldDAO that is used for the FieldType.
     * So, this method in FieldType is little more than a way to pass the request to FieldDAO
     * which will send back a string of HTML to pass to the screen.  All
     * configurations must be done with a simple request form back and forth.
     * Form elements will ne named "systemconfig-*" to avoid collission. Only
     * one FieldType's similar method will be called and displayed on a form at
     * any given time.
     */
    public String processSystemDefaultDataConfig(javax.servlet.http.HttpServletRequest request);

    /**
     * Return the data of this field in the form of a string for the
     * purposes of offensive content validation.
     */
    public String getValuesAsStringForOffensiveContentValidation();

    


    /**
     * Returns a jdom element describing the XML SCHEMA of this field type.
     */
     public Element getXmlSchemaRepresentationOfFieldType();

     /**
     * Returns a jdom element with the actual data of the field represented.
     */
     public Element getXmlForFieldData();


}

