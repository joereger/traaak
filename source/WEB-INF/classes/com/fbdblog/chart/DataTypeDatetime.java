package com.fbdblog.chart;

import org.jdom.Element;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.fbdblog.util.ValidationException;
import com.fbdblog.qtype.def.ComponentException;

/**
 * A Data Type.
 */
public class DataTypeDatetime implements DataType{

    public static int DATATYPEID = 5;

    public String getName() {
        return "Datetime";
    }

    public int getDataTypeId() {
        return DATATYPEID;
    }

    public boolean validataData(String in) throws ComponentException {
        try{
			DateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			cal.setTime(myDateFormat.parse(in));
			return true;
		} catch(Exception e) {
            ComponentException ex = new ComponentException();
            ex.addValidationError("should be a date of the form yyyy-MM-dd HH:mm:ss.");
            throw ex;
		}

    }

    public Element getXmlSchemaRepresentationOfType() {
        Element stAuth = new Element("simpleType", AppXmlSchemaRenderer.xsNs);

            Element restAuth = new Element("restriction", AppXmlSchemaRenderer.xsNs);
            restAuth.setAttribute("base","xs:string");
            stAuth.addContent(restAuth);

        return stAuth;
    }

 


}
