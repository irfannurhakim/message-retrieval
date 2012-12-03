/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.message_retrieval;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

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

    public static void main(String[] args) {

        //path = "C:\\Users\\user\\Documents\\NetBeansProjects\\message-retrieval\\";
        path = "/Users/hadipratama/NetBeansProjects/SimpleIndexing/";

        docMapping = QueryController.getDocMapping();
        long start = System.currentTimeMillis();
        terms = QueryController.queryNormalization("customer account \"your password\"");
        System.out.println(terms);
        LinkedHashMap<String, Double> weight = QueryController.getWeight(terms, docMapping, avgDocLength, 5);
        System.out.println(weight);
        long end = System.currentTimeMillis();
        System.out.println("time:" + (end - start) * 1.00 / 1000);
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
