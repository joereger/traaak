package com.fbdblog.chart;

import org.jdom.Element;
import com.fbdblog.util.Num;
import com.fbdblog.util.ValidationException;

/**
 * A Data Type.
 */
public class DataTypeInteger implements DataType{

    public static int DATATYPEID = 2;

    public String getName() {
        return "Integer";
    }

    public int getDataTypeId() {
        return DATATYPEID;
    }



    public boolean validataData(String in) throws ValidationException {
        if (Num.isinteger(in)){
            return true;
        } else {
            ValidationException ex = new ValidationException();
            ex.addValidationError("Not an integer.");
            throw ex;
        }
    }

    public Element getXmlSchemaRepresentationOfType() {
        Element stAuth = new Element("simpleType", AppXmlSchemaRenderer.xsNs);

            Element restAuth = new Element("restriction", AppXmlSchemaRenderer.xsNs);
            restAuth.setAttribute("base","xs:integer");
            stAuth.addContent(restAuth);

        return stAuth;
    }

 


}
