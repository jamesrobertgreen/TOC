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



    public static void main(String[] args) {

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

            doc = createDDXMap(files,doc);
            String test = "test";
        }

    }


    private static Document createDDXMap(ArrayList files, Document doc) {

        for (int i = 0; i < files.size();i++){
            LinkedTreeMap fileTreeMap = (LinkedTreeMap) files.get(i);
            Document doc1 = processFile(fileTreeMap,doc);
            doc.appendChild(doc1);
//            lhm.put(fileTreeMap.get("title"),processFile(fileTreeMap ,lhm));
        }

        return doc;
    }

    private static Document processFile(LinkedTreeMap file, Document doc) {
        System.out.println("title = " + file.get("title"));
        if (file.containsKey("title")) {
            String title = (String) file.get("title");

            if (file.containsKey("details")) {
                Element ele = doc.createElement("PDF");
                ele.setAttribute("source", title);

                LinkedTreeMap details = (LinkedTreeMap) file.get("details");

                if (details.containsKey("files")) {
                    ArrayList nextFile = (ArrayList) details.get("files");

                    createDDXMap(nextFile, doc);
                }
            }
        }

        return doc;
    }


}
