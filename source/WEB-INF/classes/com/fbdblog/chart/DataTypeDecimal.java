package com.fbdblog.chart;

import org.jdom.Element;
import com.fbdblog.util.ValidationException;
import com.fbdblog.util.Num;

/**
 * A Data Type.
 */
public class DataTypeDecimal implements DataType{

    public static int DATATYPEID = 1;

    public String getName() {
        return "Decimal";
    }

    public int getDataTypeId() {
        return DATATYPEID;
    }



    public boolean validataData(String in) throws ValidationException {
        if (Num.isdouble(in)){
            return true;
        } else {
            ValidationException ex = new ValidationException();
            ex.addValidationError("Not a number.");
            throw ex;
        }
    }

    public Element getXmlSchemaRepresentationOfType() {
        Element stAuth = new Element("simpleType", AppXmlSchemaRenderer.xsNs);

            Element restAuth = new Element("restriction", AppXmlSchemaRenderer.xsNs);
            restAuth.setAttribute("base","xs:decimal");
            stAuth.addContent(restAuth);

        return stAuth;
    }

 


}
