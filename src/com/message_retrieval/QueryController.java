/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.message_retrieval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author user
 */
public class QueryController {

    public static HashMap<String, DocMappingModel> getDocMapping(double[] avgDocLength) {
        //hashmap <docID, DocMappingModel (messID, docLength)
        //avgDocLength itu rata2 panjang document buat semua field+all.. array by reference
        return null;
    }

    public static int getQueryLength(String query) {
        //ambil panjang query, co: Budi "bermain bola" -->3
        return 0;
    }

    public static ArrayList<String> queryNormalization(String query) {
        // agus dan "Budi bermain" "bola Kaki" --> budi bermain bola|kaki
        // si | buat tanda klo pke kutip
        ArrayList<String> res = new ArrayList<>();
        int idx = 0;
        String[] yy = query.split("\\s");
        for (int i = 0; i < yy.length; i++) {
            String temp = "";
            if (yy[i].startsWith("\"")) {
                for (int j = i; j < yy.length; j++) {
                    temp += yy[j] + "|";

                    if (!yy[j].endsWith("\"")) {
                        i++;
                    } else {
                        break;
                    }
                }
                res.add(temp.replaceAll("\"", ""));
            } else {
                res.add(yy[i]);
            }
            idx++;
        }
        return res;
    }

    public static HashMap<String, ArrayList<Integer>> getPostingList(String term, int field) {
        // dari 1 term ambil hashmap <docID, arrayList of position>
        // klo fieldnya all, klo ada doc yg sama posisinya disatuin terus di sort
        return null;
    }

    public static void getPostingListBig(String term, ArrayList<HashMap<String, Integer>> allPostList,
            HashSet<String> allDocID, String field) {
        //untuk term standar misalkan budi doank
        //nanti panggil c get posting list
        //terus hasilnya dimasukin ke all posting list, tp isinya docID, sama termfreq nya lngs
        // terus semua docID yg ada dimasukin ke allDocID.. krn hashSet tar dia lngs nimpa..
    }

    public static void getPostingListBigSequence(String term, ArrayList<HashMap<String, Integer>> allPostList,
            HashSet<String> allDocID, String field) {
        //untuk term sequence bola|kaki
        //nanti panggil c get posting list
        //dicek dulu docID mana yg posisinya ada yg sebelahan, klo yg ga ada buang c docID nya
        //terus hasilnya dimasukin ke all posting list, tp isinya docID, sama termfreq nya lngs
        // terus semua docID yg ada dimasukin ke allDocID.. krn hashSet tar dia lngs nimpa..
    }

    public HashMap<String, Double> calculateRank(ArrayList<HashMap<String, Integer>> allPostList,
            HashSet<String> allDocID, HashMap<String, DocMappingModel> docMapping,
            double[] avgDocLength, String field) {

        //ngitung pke allpostList sama alldoc, pke algo BM25
        return null;
    }
}
