package com.fbdblog.chart;

import org.jdom.Element;
import org.apache.log4j.Logger;
import com.fbdblog.util.Num;
import com.fbdblog.util.ValidationException;
import com.fbdblog.qtype.def.ComponentException;

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



    public boolean validataData(String in) throws ComponentException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("datatype validation in="+in);
        if (Num.isinteger(in.trim())){
            return true;
        } else {
            ComponentException ex = new ComponentException();
            ex.addValidationError("should be an integer.");
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
