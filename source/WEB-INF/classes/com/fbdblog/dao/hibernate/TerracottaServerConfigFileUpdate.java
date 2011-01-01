package com.fbdblog.dao.hibernate;

import com.fbdblog.systemprops.InstanceProperties;
import com.fbdblog.util.Io;
import org.apache.log4j.Logger;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;

/**
 * Created by IntelliJ IDEA.
 * User: joereger
 * Date: Dec 26, 2010
 * Time: 4:57:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class TerracottaServerConfigFileUpdate {

    public static void updateFile(String filename){
        Logger logger = Logger.getLogger(TerracottaServerConfigFileUpdate.class);
        try{
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filename);

            boolean isTerracottaClustered = isTerracottaClustered();

            //Grab the ehcache element
            NodeList ehcaches = doc.getElementsByTagName("ehcache");
            for (int i = 0; i < ehcaches.getLength(); i++) {
                Element ehcache = (Element) ehcaches.item(i);
                //Handle the <terracottaConfig> element
                NodeList tcConfigEls = ehcache.getElementsByTagName("terracottaConfig");
                //If there's already a terracottaConfig element, remove or replace it
                if (tcConfigEls.getLength()>0){
                    //Iterate all terracottaConfig elements (likely only one)
                    for (int j = 0; j < tcConfigEls.getLength(); j++) {
                        Element tcConfigEl = (Element) tcConfigEls.item(j);
                        if (isTerracottaClustered){
                            //Replace element with newly-generated one
                            Node  terracottaConfig = doc.importNode(generateTerracottaConfigElement(), true);
                            ehcache.replaceChild(terracottaConfig, tcConfigEl);
                        } else {
                            //Remove <terracottaConfig>
                            ehcache.removeChild(tcConfigEl);
                        }
                    }
                } else {
                    //There's not already a terracottaConfig element so create it if necessary
                    if (isTerracottaClustered){
                        //Replace element with newly-generated one
                        Node  terracottaConfig = doc.importNode(generateTerracottaConfigElement(), true);
                        ehcache.appendChild(terracottaConfig);
                    }
                }
            }


            //Grab the ehcache element
            NodeList defaultCaches = doc.getElementsByTagName("defaultCache");
            for (int i = 0; i < defaultCaches.getLength(); i++) {
                Element defaultCache = (Element) defaultCaches.item(i);
                //Handle the <terracottaConfig> element
                NodeList terracottas = defaultCache.getElementsByTagName("terracotta");
                //If there's already a terracottas element, remove or replace it
                if (terracottas.getLength()>0){
                    //Iterate all terracottaConfig elements (likely only one)
                    for (int j = 0; j < terracottas.getLength(); j++) {
                        Element terracotta = (Element) terracottas.item(j);
                        if (isTerracottaClustered){
                            //Set to true
                            terracotta.setAttribute("clustered", "true");
                        } else {
                            //Set to true
                            terracotta.setAttribute("clustered", "false");
                        }
                    }
                } else {
                    //There's not already a terracottaConfig element so create it if necessary
                    if (isTerracottaClustered){
                        //Replace element with newly-generated one
                        Element terracotta = doc.createElement("terracotta");
                        terracotta.setAttribute("clustered", "true");
                        defaultCache.appendChild(terracotta);
                    }
                }
            }




            //Output to file
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            //initialize StreamResult with File object to save to file
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);

            String xmlString = result.getWriter().toString();
            //System.out.println(xmlString);

            Io.writeTextToFile(new File(filename), xmlString);


        } catch (Exception ex){
            logger.error("", ex);
        }
    }



    public static void updateFileOLD(String filename){
//        Logger logger = Logger.getLogger(TerracottaServerConfigFileUpdate.class);
//        try{
//            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//            Document doc = docBuilder.parse(filename);
//
//            NodeList tcConfigEls = doc.getElementsByTagName("tc-config");
//            for (int i = 0; i < tcConfigEls.getLength(); i++) {
//                  Element tcConfigEl = (Element) tcConfigEls.item(i);
//                  NodeList serversEls = tcConfigEl.getElementsByTagName("servers");
//                  for (int j = 0; j < serversEls.getLength(); j++) {
//                        Element serversEl = (Element) serversEls.item(j);
//                        NodeList serverEls = doc.getElementsByTagName("server");
//                        int serverNumber = 0;
//                        for (int k = 0; k < serverEls.getLength(); k++) {
//                            Element serverEl = (Element) serverEls.item(k);
//                            serverNumber++;
//                            //@todo pull from instance conf file
//                            serverEl.setAttribute("host", "127.0.0.1");
//                            serverEl.setAttribute("name", "s"+serverNumber);
//                        }
//                  }
//            }
//
//            Transformer transformer = TransformerFactory.newInstance().newTransformer();
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//
//            //initialize StreamResult with File object to save to file
//            StreamResult result = new StreamResult(new StringWriter());
//            DOMSource source = new DOMSource(doc);
//            transformer.transform(source, result);
//
//            String xmlString = result.getWriter().toString();
//            //System.out.println(xmlString);
//
//            Io.writeTextToFile(new File(filename), xmlString);
//
//
//        } catch (Exception ex){
//            logger.error("", ex);
//        }
    }


    private static Element generateTerracottaConfigElement(){
        Logger logger = Logger.getLogger(TerracottaServerConfigFileUpdate.class);
        Element out = null;
        try{

//     <terracottaConfig>
//        <tc-config>
//            <servers>
//                <server host="XXXXXHOST1XXXXX" name="s1"/>
//            </servers>
//            <clients>
//                <logs>app/logs-%i</logs>
//            </clients>
//        </tc-config>
//    </terracottaConfig>

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element terracottaConfig = doc.createElement("terracottaConfig");
            Element tcconfig = doc.createElement("tc-config");
            terracottaConfig.appendChild(tcconfig);
            Element servers = doc.createElement("servers");
            tcconfig.appendChild(servers);
            Element clients = doc.createElement("clients");
            tcconfig.appendChild(clients);
            Element logs = doc.createElement("logs");
            clients.appendChild(logs);
            logs.setNodeValue("app/logs-%i");
            if (InstanceProperties.getTerracottahost01()!=null && !InstanceProperties.getTerracottahost01().equals("")){
                Element s1 = doc.createElement("server");
                s1.setAttribute("host",InstanceProperties.getTerracottahost01());
                s1.setAttribute("name","s1");
                servers.appendChild(s1);
            }
            if (InstanceProperties.getTerracottahost02()!=null && !InstanceProperties.getTerracottahost02().equals("")){
                Element s2 = doc.createElement("server");
                s2.setAttribute("host",InstanceProperties.getTerracottahost02());
                s2.setAttribute("name","s2");
                servers.appendChild(s2);
            }
            if (InstanceProperties.getTerracottahost03()!=null && !InstanceProperties.getTerracottahost03().equals("")){
                Element s3 = doc.createElement("server");
                s3.setAttribute("host",InstanceProperties.getTerracottahost03());
                s3.setAttribute("name","s3");
                servers.appendChild(s3);
            }


            out = terracottaConfig;

        } catch (Exception ex){
            logger.error("", ex);
        }

        return out;
    }

    private static String getHostValueFromInstanceProps(int serverNumber){
        Logger logger = Logger.getLogger(TerracottaServerConfigFileUpdate.class);
        String host = "";
        return host;
    }

    private static boolean isTerracottaClustered(){
        if (InstanceProperties.getTerracottahost01()!=null && !InstanceProperties.getTerracottahost01().equals("")){
            return true;
        }
        if (InstanceProperties.getTerracottahost02()!=null && !InstanceProperties.getTerracottahost02().equals("")){
            return true;
        }
        if (InstanceProperties.getTerracottahost03()!=null && !InstanceProperties.getTerracottahost03().equals("")){
            return true;
        }
        return false;
    }


}
