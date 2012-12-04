/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.message_retrieval;

import com.query.controller.QueryProcessor;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author user
 */
public class MainQuery {

    public static HashMap<String, DocMappingModel> docMapping;
    public static HashMap<String, Integer> terms = new HashMap<>();
    public static double[] avgDocLength = new double[6];
    public static String path;
    public static HashMap<String, HashMap<String, Integer>> allPostList = new HashMap<>();
    public static HashSet<String> allDocID = new HashSet<>();

    public static void main(String[] args) throws IOException {
        
        String term = "";
        int field = 6;
        
        if(args.length <= 2){
            System.out.println("Usage: <path_to_index_file> <query_term> [<field_code>]\n");
            System.out.println("Field code :\n");
            System.out.println("1 Data Field\n");
            System.out.println("2 To Field\n");
            System.out.println("3 From Field\n");
            System.out.println("4 Subject Field\n");
            System.out.println("5 Body Field\n");
            System.out.println("If you want search to all fields, just omit the field_code parameter.\n");
            System.out.println("\n");
            System.out.println("Example search \"myquery\" on field body : MainQuery /path/to/index \"myquery\" 5");


            System.exit(0);
        }
        
        path = args[0];
        term = args[1];
        
        if(args.length == 3){
            field = Integer.valueOf(args[2]);
        }
        
        //path = "C:\\Users\\user\\Documents\\NetBeansProjects\\message-retrieval\\";
        //path = "/Users/hadipratama/Documents/Indexing/";
        
        docMapping = QueryController.getDocMapping();
        long start = System.currentTimeMillis();
        terms = QueryController.queryNormalization(term);
        System.out.println(terms);
        LinkedHashMap<String, Double> weight = QueryController.getWeight(terms, docMapping, avgDocLength, field);
        long end = System.currentTimeMillis();
        double timeElapsed = (end - start) * 1.00 / 1000;
        String temp = "";
        int rank = 1;
        //write to file
        for (Map.Entry<String, Double> entry : weight.entrySet()) {
            temp += term + " MSG ID: " + entry.getKey() + " Rank: " + rank++ + " Weight: " + entry.getValue() + "\n";
        }

        temp += "Execution time" + timeElapsed;
        QueryProcessor.writeFile(temp, "All", term);
        
//        System.out.println(docMapping.size());
//        for (int i = 0; i < avgDocLength.length; i++) {
//            System.out.println(avgDocLength[i]);
//            
//        }
        //        HashMap<String, Integer> terms = QueryController.queryNormalization("saya \"adalah seorang\" anak gembala saya \"selalu riang\" serta gembira");
        //        System.out.println(terms);
        //        
        //        HashMap<String, ArrayList<Integer>> test = new HashMap<>();
        //        try {
        //            test = QueryController.getPostingList("goes", 5);
        //        } catch (FileNotFoundException ex) {
        //            Logger.getLogger(MainQuery.class.getName()).log(Level.SEVERE, null, ex);
        //        } catch (IOException ex) {
        //            Logger.getLogger(MainQuery.class.getName()).log(Level.SEVERE, null, ex);
        //        }
        //        System.out.println(test);



//                    QueryController.getPostingListBig("goes", allPostList, allDocID, 5);
//                    QueryController.getPostingListBig("golden", allPostList, allDocID, 5);
//                    QueryController.getPostingListBig("golly", allPostList, allDocID, 5);

//                    QueryController.getPostingListBigSequence("let|me|know", allPostList, allDocID, 5);
//                    //System.out.println(allDocID);
//                    System.out.println(allPostList);

//        try {
//            //        for (int i = 1; i < 6; i++) {
//            //            System.out.println(docMapping.get("2").docLength[i-1]);
//            //            System.out.println(QueryController.fieldLengthAcc("2", i));
//            //            System.out.println("================");
//            //        }
//                    
//                   // System.out.println(QueryController.getPostingListAll("editor"));
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(MainQuery.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(MainQuery.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
