package com.fbdblog.chart;

import org.jdom.Element;
import org.jdom.Document;
import org.jdom.Namespace;
import org.jdom.output.XMLOutputter;
import org.apache.log4j.Logger;

import java.util.Iterator;

import com.fbdblog.dao.App;
import com.fbdblog.dao.Question;
import com.fbdblog.util.Util;


/**
 * Renders a log type as an XML Schema document
 */
public class AppXmlSchemaRenderer {


    public static Namespace xsNs = Namespace.getNamespace("xs","http://www.w3.org/2000/10/XMLSchema-instance");

    public static String getSchema(App app){
        Logger logger = Logger.getLogger(AppXmlSchemaRenderer.class);
        try{

            Document doc = new Document();

            //Whole schema doc
            Element schema = new Element("schema", xsNs);
            //schema.setAttribute("xs","http://www.w3.org/2001/XMLSchema", Namespace.getNamespace("XXXxmlns","http://www.w3.org/2001/XMLSchema"));
            doc.addContent(schema);

                //Entry
                Element entry = new Element("element", xsNs);
                entry.setAttribute("name","entry");
                schema.addContent(entry);


                    //Title
                    Element elTit = new Element("element", xsNs);
                    elTit.setAttribute("name","title");
                    entry.addContent(elTit);

                        Element stTit = new Element("simpleType", xsNs);
                        elTit.addContent(stTit);

                            Element restTit = new Element("restriction", xsNs);
                            restTit.setAttribute("base","xs:string");
                            stTit.addContent(restTit);

                    //Datetime
                    Element elDt = new Element("element", xsNs);
                    elDt.setAttribute("name","datetime");
                    entry.addContent(elDt);

                        Element stDt = new Element("simpleType", xsNs);
                        elDt.addContent(stDt);

                            Element restDt = new Element("restriction", xsNs);
                            restDt.setAttribute("base","xs:string");
                            stDt.addContent(restDt);


                    //Description
                    Element el = new Element("element", xsNs);
                    el.setAttribute("name","description");
                    entry.addContent(el);


                        Element ct = new Element("complexType", xsNs);
                        ct.setAttribute("mixed","true");
                        el.addContent(ct);

                            Element attr = new Element("attribute", xsNs);
                            attr.setAttribute("name","type");
                            attr.setAttribute("use","required");
                            ct.addContent(attr);

                                Element st = new Element("simpleType", xsNs);
                                attr.addContent(st);

                                    Element rest = new Element("restriction", xsNs);
                                    rest.setAttribute("base","xs:string");
                                    st.addContent(rest);

                            Element attr2 = new Element("attribute", xsNs);
                            attr2.setAttribute("name","escaped");
                            attr2.setAttribute("use","optional");
                            ct.addContent(attr2);

                                Element st2 = new Element("simpleType", xsNs);
                                attr2.addContent(st2);

                                    Element rest2 = new Element("restriction", xsNs);
                                    rest2.setAttribute("base","xs:boolean");
                                    st2.addContent(rest2);


                    //Location
                    Element elLoc = new Element("element", xsNs);
                    elLoc.setAttribute("name","location");
                    entry.addContent(elLoc);

                        Element stLoc = new Element("simpleType", xsNs);
                        elLoc.addContent(stLoc);

                            Element restLoc = new Element("restriction", xsNs);
                            restLoc.setAttribute("base","xs:string");
                            stLoc.addContent(restLoc);


                    //Role
                    Element elRole = new Element("element", xsNs);
                    elRole.setAttribute("name","role");
                    entry.addContent(elRole);

                        Element stRole = new Element("simpleType", xsNs);
                        elRole.addContent(stRole);

                            Element restRole = new Element("restriction", xsNs);
                            restRole.setAttribute("base","xs:string");
                            stRole.addContent(restRole);


                    //Author
                    Element elAuth = new Element("element", xsNs);
                    elAuth.setAttribute("name","author");
                    entry.addContent(elAuth);

                        Element stAuth = new Element("simpleType", xsNs);
                        elAuth.addContent(stAuth);

                            Element restAuth = new Element("restriction", xsNs);
                            restAuth.setAttribute("base","xs:string");
                            stAuth.addContent(restAuth);


                    //Extended data
                    Element extendedData = new Element("element", xsNs);
                    extendedData.setAttribute("name","structured-data");
                    entry.addContent(extendedData);

                        //Individual fields
                        for (Iterator<Question> iterator = app.getQuestions().iterator(); iterator.hasNext();) {
                            Question question = iterator.next();
                            //@todo fix AppXmlSchemaRenderer by implementing the line below
                            //extendedData.addContent(question.getXmlSchemaRepresentationOfFieldType());
                        }

            XMLOutputter outp = new XMLOutputter();
            return outp.outputString(doc);

        } catch (Exception e){
            logger.error("",e);
        }
        return "General Failure.  Many apologies.";
    }

    public Element getXmlSchemaRepresentationOfField(Element fieldTypeSpecific) {
        Element elField = new Element("element", AppXmlSchemaRenderer.xsNs);
        //@todo fix AppXmlSchemaRenderer by implementing the line below
        //elField.setAttribute("name", getFieldnameForApis());
        //Fieldtype/ui-display-type
        //@todo Create a text list of field types (i.e. dropdown, checkboxes, radios, etc.)
        Element attr = new Element("attribute", AppXmlSchemaRenderer.xsNs);
        attr.setAttribute("name","ui-display-type");
        attr.setAttribute("use","optional");
        elField.addContent(attr);
            Element st = new Element("simpleType", AppXmlSchemaRenderer.xsNs);
            attr.addContent(st);
                Element rest = new Element("restriction", AppXmlSchemaRenderer.xsNs);
                rest.setAttribute("base","xs:string");
                st.addContent(rest);
                    Element enum1 = new Element("enumeration", AppXmlSchemaRenderer.xsNs);
                    enum1.setAttribute("name", "dropdown");
                    rest.addContent(enum1);
                    Element enum2 = new Element("enumeration", AppXmlSchemaRenderer.xsNs);
                    enum2.setAttribute("name", "textbox");
                    rest.addContent(enum2);
                    Element enum3 = new Element("enumeration", AppXmlSchemaRenderer.xsNs);
                    enum3.setAttribute("name", "radiobuttons");
                    rest.addContent(enum3);

        //Requiredness
        Element attr2 = new Element("attribute", AppXmlSchemaRenderer.xsNs);
        attr2.setAttribute("name","required");
        attr2.setAttribute("use","optional");
        elField.addContent(attr2);
            Element st2 = new Element("simpleType", AppXmlSchemaRenderer.xsNs);
            attr2.addContent(st2);
                Element rest2 = new Element("restriction", AppXmlSchemaRenderer.xsNs);
                rest2.setAttribute("base","xs:boolean");
                st2.addContent(rest2);

        //Restrictions/datatype
        elField.addContent(fieldTypeSpecific);


        return elField;
    }

    public String getFieldnameForApis(String str) {
        if (str!=null){
            return Util.xmlFieldNameClean(str);
        }
        return "";
    }


}
