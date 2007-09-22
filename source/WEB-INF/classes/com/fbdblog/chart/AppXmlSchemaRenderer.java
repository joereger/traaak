package com.fbdblog.chart;

import org.jdom.Element;
import org.jdom.Document;
import org.jdom.Namespace;
import org.jdom.output.XMLOutputter;
import org.apache.log4j.Logger;

import java.util.Iterator;

import com.fbdblog.dao.App;
import com.fbdblog.dao.Question;


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
                            extendedData.addContent(question.getXmlSchemaRepresentationOfFieldType());
                        }

            XMLOutputter outp = new XMLOutputter();
            return outp.outputString(doc);

        } catch (Exception e){
            logger.error(e);
        }
        return "General Failure.  Many apologies.";
    }


}
