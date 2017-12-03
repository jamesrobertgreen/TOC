package com.ensemble;


import com.ensemble.models.TOC;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import sun.plugin.javascript.navig4.Link;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;

public class Main {

    private static String tocStr = "{\n" +
            "    'files': [\n" +
            "        {\n" +
            "            'title':'Test Spain Two County Forms',\n" +
            "            'details':{\n" +
            "                'source':'relationship_acceptance.pdf',\n" +
            "                'files':[\n" +
            "                    {\n" +
            "                        'title':'Spanish',\n" +
            "                        'details':{\n" +
            "                            'source':'relationship_acceptance.pdf',\n" +
            "                            'files':[\n" +
            "                                {\n" +
            "                                    'title':'Relationship Acceptance Form',\n" +
            "                                    'details':{\n" +
            "                                        'source':'relationship_acceptance.pdf',\n" +
            "                                        'files':[]\n" +
            "                                    },\n" +
            "                                    'title':'Global Application Forms - Spain',\n" +
            "                                    'details':{\n" +
            "                                        'source':'account_opening.pdf',\n" +
            "                                        'files':[\n" +
            "                                            {\n" +
            "                                                'title':'Account Opening Forms',\n" +
            "                                                'details':{\n" +
            "                                                    'source':'account_opneing.pdf',\n" +
            "                                                    'files':[]\n" +
            "                                                } \n" +
            "                                            }\n" +
            "                                        ]\n" +
            "                                    }\n" +
            "                                }\n" +
            "                            ]\n" +
            "                        }\n" +
            "                    }\n" +
            "                ]\n" +
            "            }       \n" +
            "        },\n" +
            "        {\n" +
            "            'title':'Signature Book',\n" +
            "            'details':{\n" +
            "                'source':'signature_book.pdf',\n" +
            "                'files':[\n" +
            "                    {\n" +
            "                        'title':'Signature Book',\n" +
            "                        'details':{\n" +
            "                            'source':'signature_book.pdf',\n" +
            "                            'files':[]\n" +
            "                        }\n" +
            "                    }\n" +
            "                ]\n" +
            "            }\n" +
            "        }\n" +
            "    ]\n" +
            "}";



    public static void main(String[] args) throws IOException, TransformerException {

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        // root elements
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("DDX");
        doc.appendChild(rootElement);

        Gson gson = new GsonBuilder().create();
        LinkedTreeMap TOCMap = (LinkedTreeMap) gson.fromJson(tocStr, Object.class);

        if ( TOCMap.containsKey("files")) {

            LinkedHashMap lhm = new LinkedHashMap();

            ArrayList files = (ArrayList) TOCMap.get("files");

            Element root = createDDXMap("root",files,doc,rootElement);
            String test = "test";
           // doc.appendChild(root);

            printDocument(rootElement);
        }

    }

    public static void printDocument(Element doc) throws IOException, TransformerException {
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = transFactory.newTransformer();
        StringWriter buffer = new StringWriter();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.transform(new DOMSource(doc),
                new StreamResult(buffer));
        String str = buffer.toString();
        System.out.println(str);
    }

    private static Element createDDXMap(String name, ArrayList files, Document doc, Element rootElement) {
        Element ele = doc.createElement("PDF");
        ele.setAttribute("result",name);

        for (int i = 0; i < files.size();i++){
            LinkedTreeMap fileTreeMap = (LinkedTreeMap) files.get(i);
            Element el1 = processFile(fileTreeMap,doc,rootElement);
            rootElement.appendChild(el1);

//            lhm.put(fileTreeMap.get("title"),processFile(fileTreeMap ,lhm));
        }

        return ele;
    }

    private static Element processFile(LinkedTreeMap file, Document doc, Element rootElement) {
        Element ele = doc.createElement("PDF");
        System.out.println("title = " + file.get("title"));
        if (file.containsKey("title")) {
            String title = (String) file.get("title");

            if (file.containsKey("details")) {

                ele.setAttribute("source", title);

                LinkedTreeMap details = (LinkedTreeMap) file.get("details");

                if (details.containsKey("files")) {
                    ArrayList nextFile = (ArrayList) details.get("files");
                    Element el2 = createDDXMap(title,nextFile, doc, rootElement);

                    ele.appendChild(el2);
                }
            }
        }

        return ele;
    }


}
