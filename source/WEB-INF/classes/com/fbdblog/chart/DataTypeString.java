package com.fbdblog.chart;

import org.jdom.Element;
import com.fbdblog.util.ValidationException;

/**
 * A Data Type.
 */
public class DataTypeString implements DataType{

    public static int DATATYPEID = 3;

    public String getName() {
        return "Alphanumeric String";
    }


    public int getDataTypeId() {
        return DATATYPEID;
    }

    public boolean validataData (String in) throws ValidationException {
        return true;
    }

    public Element getXmlSchemaRepresentationOfType() {
        Element stAuth = new Element("simpleType", AppXmlSchemaRenderer.xsNs);

            Element restAuth = new Element("restriction", AppXmlSchemaRenderer.xsNs);
            restAuth.setAttribute("base","xs:string");
            stAuth.addContent(restAuth);

        return stAuth;
    }




}
