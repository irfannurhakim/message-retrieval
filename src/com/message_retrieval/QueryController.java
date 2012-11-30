/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.message_retrieval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author user
 */
public class QueryController {
    
    final static double k1= 1.2;
    final static double b = 0.75;
    final static double k2=100;
        
    public static HashMap<String, DocMappingModel> getDocMapping (double[]avgDocLength)
    {
        //hashmap <docID, DocMappingModel (messID, docLength)
        //avgDocLength itu rata2 panjang document buat semua field+all.. array by reference
        return null;
    }
    public static int getQueryLength (String query)
    {
        //ambil panjang query, co: Budi "bermain bola" -->3
        return 0;
    }
    
    public static String queryNormalization (String query)
    {
        // Budi, bermain "bola Kaki" --> budi bermain bola|kaki
        // si | buat tanda klo pke kutip
        return "";
    }
    
    public static HashMap<String, ArrayList<Integer>> getPostingList(String term, int field)
    {
        // dari 1 term ambil hashmap <docID, arrayList of position>
        // klo fieldnya all, klo ada doc yg sama posisinya disatuin terus di sort
        return null;
    }
    
    public static void  getPostingListBig (String term, ArrayList<HashMap<String, Integer>> allPostList, 
            HashSet<String> allDocID, int field)
    {
        //untuk term standar misalkan budi doank
        //nanti panggil c get posting list
        //terus hasilnya dimasukin ke all posting list, tp isinya docID, sama termfreq nya lngs
        // terus semua docID yg ada dimasukin ke allDocID.. krn hashSet tar dia lngs nimpa..
        
        HashMap<String, Integer> postListFreq = new HashMap<>();
        HashMap<String, ArrayList<Integer>> postList = getPostingList(term, field);
         Iterator<Entry<String, ArrayList<Integer>>> itr = postList.entrySet().iterator();
        while (itr.hasNext()) {
            Entry<String, ArrayList<Integer>> entry = itr.next();
            String docID = entry.getKey();
            ArrayList<Integer> docPos = entry.getValue();
            allDocID.add(docID);
            postListFreq.put(docID, docPos.size());
        }
        
    }
    
    public static void  getPostingListBigSequence (String term, ArrayList<HashMap<String, Integer>> allPostList, 
            HashSet<String> allDocID, int field)
    {
        //untuk term sequence bola|kaki
        //nanti panggil c get posting list
        //dicek dulu docID mana yg posisinya ada yg sebelahan, klo yg ga ada buang c docID nya
        //terus hasilnya dimasukin ke all posting list, tp isinya docID, sama termfreq nya lngs
        // terus semua docID yg ada dimasukin ke allDocID.. krn hashSet tar dia lngs nimpa..
    }
    
    public HashMap<String, Double> calculateRank (ArrayList<HashMap<String, Integer>> allPostList, 
            HashSet<String> allDocID, HashMap<String, DocMappingModel> docMapping, 
            double[]avgDocLength, int field)
    {
        HashMap<String, Double> hasil = new HashMap<>();
        
        int totalDoc = docMapping.size();
        Iterator it = allDocID.iterator();
 
        while(it.hasNext())
        {
            double BM25Score=0;
            String docID= (String) it.next();
            DocMappingModel docMod = docMapping.get(docID);
            int docLength = (int) docMod.getDocLength()[field];
            double avgDoc = avgDocLength[field];
            for (int i = 0; i < allPostList.size(); i++) {
                HashMap<String, Integer> postList = allPostList.get(i); //postList untuk 1 term
                int docFreq = postList.size();
                int termFreq = 0;
                if(postList.get(docID)!=null)
                {
                    termFreq = postList.get(docID);
                }
                BM25Score+= BM25Calculator(docLength, avgDoc, docFreq, termFreq, totalDoc);
            }
            hasil.put(docMod.messID, BM25Score);
        }

        

        //ngitung pke allpostList sama alldoc, pke algo BM25
        return null;
    }
    
    public static double BM25Calculator (int docLength, double avgDocLength, int docFreq, int termFreq, int totalDoc)
    {
        double hasil=0.0;
        double persen =docLength*1.0/avgDocLength;
        System.out.println(persen);
        double K = k1 * ((1-b)+(b*persen));
        
        double a1 = Math.log((0.5/0.5)/((docFreq+0.5)/(totalDoc-docFreq+0.5)));
        System.out.println(a1);
        double a2=((k1+1)*termFreq)/(K+termFreq);
        System.out.println(a2);
        double a3 =((k2+1)*1)/(k2+1);
        System.out.println(a3);
        hasil = a1*a2*a3;
     
        return hasil;
    }
    
    public static void main(String[] args) {
        
        double k=BM25Calculator(90, 100, 300, 25, 500000);
        System.out.println(k);
    }
}
