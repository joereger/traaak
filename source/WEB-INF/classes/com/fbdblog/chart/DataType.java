package com.fbdblog.chart;

import org.jdom.Element;
import com.fbdblog.util.ValidationException;

/**
 * Defines a DataType, not a field type.
 * Data types are things like String, Int, etc.
 */
public interface DataType {

    public String getName();

    public int getDataTypeId();

    public boolean validataData(String in) throws ValidationException;

    public Element getXmlSchemaRepresentationOfType();

}
