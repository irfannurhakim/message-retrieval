/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.message_retrieval;

import com.query.controller.QueryProcessor;
import indexing.Indexing;
import java.io.*;
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
        //
        String path = "/Users/hadipratama/NetbeansProjects/SimpleIndexing/";
        String termMappingFileName = path + QueryProcessor.DOC_MAPPING;
        HashMap<String, DocMappingModel> temp = new HashMap<>();
        
        try {
            FileInputStream fstream = new FileInputStream(termMappingFileName);
            try (DataInputStream in = new DataInputStream(fstream)) {
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;
                String[] a;
                while ((strLine = br.readLine()) != null) {
                    a = strLine.replaceAll("=", "\\|").split("\\|"); 
                    String[] b = a[2].split(",");
                    int[] c = new int[b.length];
                    for (int i = 0; i < b.length; i++) {
                        c[i] = Integer.valueOf(b[i]);
                    }
                    temp.put(a[0], new DocMappingModel(a[1], c));
                }
            }
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
        
        return temp;
    }

    public static int getQueryLength(String query) {
        //ambil panjang query, co: Budi "bermain bola" -->3
        return 0;
    }

    public static ArrayList<String> queryNormalization(String query) {
        // agus dan "Budi bermain" "bola Kaki" --> budi bermain bola|kaki
        // si | buat tanda klo pke kutip
        ArrayList<String> res = new ArrayList<>();
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
        }
        return res;
    }
    
    public static HashMap<String, ArrayList<Integer>> getPostingList(String term, int field)
    {
        // dari 1 term ambil hashmap <docID, arrayList of position>
        // klo fieldnya all, klo ada doc yg sama posisinya disatuin terus di sort
        // field 1=date, 2=to, 3=from, 4=subject, 5=body, 6=all
        String field;
        HashMap<String, ArrayList<Integer>> temp = new HashMap<>();
        switch (fieldCode) {
            case 1:
                field = "date";
                break;
            case 2:
                field = "to";
                break;
            case 3:
                field = "from";
                break;
            case 4:
                field = "subject";
                break;
            case 5:
                field = "body";
                break;
            default:
                field = "body";
                break;
        }

        String path = "/Users/hadipratama/NetbeansProjects/SimpleIndexing/";
        String indexFileName = path + QueryProcessor.PREFIX_INDEX_FILENAME + field + ".txt";
        String termMappingFileName = path + QueryProcessor.PREFIX_TERM_MAPPING_FILENAME + field + ".txt";

        // muali mencari dengan binary search algo
        RandomAccessFile file = new RandomAccessFile(termMappingFileName, "r");
        ArrayList<Object> position = new ArrayList<>();
        file.seek(0);
        String line = file.readLine().split("=")[0];
        if (line == null || line.compareTo(term) >= 0) {
            return null;
        }

        long beg = 0;
        long end = file.length();
        boolean found = false;
        while (beg <= end) {

            long mid = beg + (end - beg) / 2;
            file.seek(mid);
            file.readLine();
            line = file.readLine().split("=")[0];
            if (line == null || line.compareTo(term) >= 0) {
                if (line.matches(term)) {
                    found = true;
                }
                end = mid - 1;
            } else {
                beg = mid + 1;
            }
        }

        if (found) {
            file.seek(beg);
            file.readLine();
            String target = file.readLine();
            position.add(Long.parseLong(target.split("\\|")[1]));
            position.add(Integer.valueOf(target.split("\\|")[2]));
            position.add(target.split("=")[0]);
        } else {
            return null;
        }

        RandomAccessFile indexFile = new RandomAccessFile(indexFileName, "r");
        indexFile.seek((Long) position.get(0));
        byte[] buffer = new byte[(int) position.get(1) - Indexing.NEWLINE.getBytes().length];
        indexFile.read(buffer);
        String str = new String(buffer);
        String content = str.split("=")[1];
        String[] msgs = content.split(";");
        ArrayList<Integer> tempPos = new ArrayList<>();
        for (String docs : msgs) {
            String[] pos = docs.split(":");
            for (String posTerm : pos) {
                tempPos.add(Integer.valueOf(posTerm));
            }
        }
        temp.put(str.split("=")[0], tempPos);
        return temp;
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
