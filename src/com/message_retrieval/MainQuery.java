/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.message_retrieval;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class MainQuery {

    public static HashMap<String, DocMappingModel> docMapping = new HashMap<>();
    public static ArrayList<HashMap<String, String>> termMapping = new ArrayList<>();
    public static HashMap<String, Integer> terms = new HashMap<>();
    public static double[] avgDocLength = new double[6];
    public static String path = ".", pathQueryFile = ".";
    public static HashMap<String, HashMap<String, Integer>> allPostList = new HashMap<>();
    public static HashSet<String> allDocID = new HashSet<>();
    public static final String codeName = "irfan_elisafina_pandapotan";
    public final static String NEWLINE = "\r\n";
    public static long start = 0;
    public static long end = 0;
    public static String com = "";
    public static boolean isCompress = false;

    public static void main(String[] args) {

        //String query;
        int field = 6;

        if (args.length <= 3) {
            System.out.println("Usage: < path_to_index_file > < path_to_query_file > < -u | -c > [<field_code>]\n");
            System.out.println("Choose index file : ");
            System.out.println("    -u      uncompressed index\n");
            System.out.println("    -c      compressed index\n");
            System.out.println("Field code :\n");
            System.out.println("    1       Data Field\n");
            System.out.println("    2       To Field\n");
            System.out.println("    3       From Field\n");
            System.out.println("    4       Subject Field\n");
            System.out.println("    5       Body Field\n");
            System.out.println("If you want search to all fields, just omit the field_code parameter.\n");
            System.out.println("Example search \"myquery\" on field body : MainQuery /path/to/index \"myquery\" -u 5");
            System.exit(0);
        }

        path = args[0];
        pathQueryFile = args[1];

        if (args[2].equalsIgnoreCase("-c")) {
            com = "com_";
            isCompress = true;
        }
        if (args.length == 4) {
            field = Integer.valueOf(args[3]);
        }

        docMapping = QueryController.getDocMapping();
        termMapping = QueryController.getTermMapping();

        HashMap<String, String> queryList = new HashMap<>();

        try {
            queryList = QueryController.dumpTermMapping(pathQueryFile);
        } catch (IOException ex) {
            Logger.getLogger(MainQuery.class.getName()).log(Level.SEVERE, null, ex);
        }

        //query = "\"meeting tomorrow\" urgent";
        for (Map.Entry<String, String> entry : queryList.entrySet()) {
            String query = entry.getValue();
            String fileName = codeName + "-" + query + ".txt";

            start = System.currentTimeMillis();
            query = Parser.parseQuery(query);
            terms = QueryController.queryNormalization(query);
            System.out.println("Processing query : " + terms);
            LinkedHashMap<String, Double> weight = QueryController.getWeight(terms, docMapping, avgDocLength, field);
            try {
                QueryController.printWeight(query, weight, com + fileName.replaceAll("\"", ""));
            } catch (IOException ex) {
                Logger.getLogger(MainQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
