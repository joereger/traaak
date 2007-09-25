package com.fbdblog.chart;

import org.jdom.Element;
import com.fbdblog.util.ValidationException;
import com.fbdblog.qtype.def.ComponentException;

/**
 * Defines a DataType, not a field type.
 * Data types are things like String, Int, etc.
 */
public interface DataType {

    public String getName();

    public int getDataTypeId();

    public boolean validataData(String in) throws ComponentException;

    public Element getXmlSchemaRepresentationOfType();

}
