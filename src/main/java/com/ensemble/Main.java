package com.ensemble;


import com.ensemble.models.TOC;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import sun.plugin.javascript.navig4.Link;

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
        HashMap<String, String> TOC = new HashMap<String,String>();

        Gson gson = new GsonBuilder().create();
        LinkedTreeMap TOCMap = (LinkedTreeMap) gson.fromJson(tocStr, Object.class);

        if ( TOCMap.containsKey("files")) {

            LinkedHashMap lhm = new LinkedHashMap();

            ArrayList files = (ArrayList) TOCMap.get("files");

            lhm = createDDXMap(files,lhm);
        }

    }


    private static LinkedHashMap createDDXMap(ArrayList files, LinkedHashMap lhm) {

        for (int i = 0; i < files.size();i++){
            LinkedTreeMap fileTreeMap = (LinkedTreeMap) files.get(i);
            lhm.put(fileTreeMap.get("title"),processFile(fileTreeMap ,lhm));
        }

        return lhm;
    }

    private static LinkedHashMap processFile(LinkedTreeMap file, LinkedHashMap lhm) {
        System.out.println("title = " + file.get("title"));

        if(file.containsKey("details")){
            LinkedTreeMap details = (LinkedTreeMap) file.get("details");

            if(details.containsKey("files")){
                ArrayList nextFile = (ArrayList) details.get("files");

                createDDXMap(nextFile,lhm);
            }
            else {
                return lhm;
            }
        }

        return new LinkedHashMap();
    }


}
