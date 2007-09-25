package com.fbdblog.chart;

import org.jdom.Element;
import com.fbdblog.util.ValidationException;
import com.fbdblog.util.Num;
import com.fbdblog.qtype.def.ComponentException;

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



    public boolean validataData(String in) throws ComponentException {
        if (Num.isdouble(in.trim())){
            return true;
        } else {
            ComponentException ex = new ComponentException();
            ex.addValidationError("should be a number.");
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
