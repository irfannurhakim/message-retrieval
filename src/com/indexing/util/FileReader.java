/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.indexing.util;

import com.indexing.controller.FromTokenizer;
import com.indexing.controller.dateTokenizer;
import com.indexing.controller.subject_bodyTokenizer;
import com.indexing.controller.toTokenizer;
import com.indexing.model.BigConcurentHashMap;
import indexing.Indexing;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.concurrent.Callable;

/**
 *
 * @author irfannurhakim
 */
public class FileReader implements Callable {

    private FileWalker fileWalker;
    private Path path;
    private int count;

    public FileReader() {
    }

    public FileReader(Path path, int count) {
        this.path = path;
        this.count = count;
    }

    public void setCaller(FileWalker fileWalker) {
        this.fileWalker = fileWalker;
    }

    public FileWalker getCaller() {
        return fileWalker;
    }

    /**
     * This is callable method, so while the FileReader object instantiated this
     * method will performed. Generally, this method slice the structure of each
     * email document to five parts: Date, From, To, Subject and Body. After
     * document has success sliced, then send result to HashMap and invoke
     * callback to acknowledge the main thread that job was done.
     *
     * @return Boolean
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public Boolean call() {

//        synchronized(Indexing.test)
//        {
//            Indexing.test.add(this.path.toString());
//        }
        String line = "";
        try {
            line = Files.readAllLines(this.path, StandardCharsets.ISO_8859_1).toString().toLowerCase().replaceAll("x-to|x-from", "");
            //System.out.println(line);
        } catch (IOException ex) {
            System.out.println(ex.toString() + " ---" + this.path.toString());
//            synchronized(Indexing.test)
//        {
//            Indexing.test.remove(this.path.toString());
//        }
        }

        /*
         * raw -> array 0 head, array 1 tail
         */
        try {

            if (!line.equals("")) {
                String[] raw = line.split("mime-version: ", 2);

                String[] rawh = raw[0].split("date: ", 2);
                String idEmail = rawh[0].replace("[message-id: <", "").replace(">,", "");

                long docNumber = 0;
//                String temp;
//               
//                synchronized (Indexing.docMapping) {
//                    Indexing.docID++;
//                    docNumber = Indexing.docID;
//                    temp= docNumber + "=" + idEmail +"|";
//                    
//                    //Tokenizer.docMapping.close();
//                    //System.out.println("aaaaa");
//                }
                
                long[] doclength = {0,0,0,0,0,0}; //0. date, 1.from, 2.to, 3.subject, 4.body, 5.all

                String[] date = rawh[1].split("from: ", 2);
                HashMap<String, String> dateMap = dateTokenizer.getListDate(date[0], doclength);

                


                if (date.length == 1) {
                    date[1] = "";
                }

                String[] from;
                if (date[1].contains("to: ")) {
                    from = date[1].split("to: ", 2);
                } else {
                    from = date[1].split("subject: ", 2);
                }

                HashMap<String, String> fromMap = FromTokenizer.getListFrom(from[0].replaceAll(", ", " "), doclength);
               
                //System.out.println("from=" + from[0]);

                if (from.length == 1) {
                    from[1] = "";
                }

                String[] to = new String[2];
                if (date[1].contains("to: ")) {
                    if (from[1].contains("subject: ")) {
                        to = from[1].split("subject: ", 2);
                    } else {
                        to = from;
                    }
                } else {
                    to[0] = "";
                }

                HashMap<String, String> toMap = toTokenizer.getListTo(to[0], doclength);
                //System.out.println(toMap);
               

                //System.out.println("to" + to[0]);

                if (to.length == 1) {
                    to[1] = "";
                }


                if (to[1] == null) {
                    to[1] = "";
                } else {
                    if (to[1].contains("cc: ")) {
                        to[1] = to[1].split("cc: ", 2)[0];
                    }
                }


                HashMap<String, String> subjectMap = subject_bodyTokenizer.getListTerm(to[1], doclength, true);
                //System.out.println(path.toString()+"===="+subjectMap);
                
                //System.out.println("subjet" + to[1]);


                String[] body = raw[1].split("(\\.pst)|(\\.nsf)", 2);
                if (body.length == 1) {
                    body = new String[2];
                    body[0] = "";
                    body[1] = "";
                }

                HashMap<String, String> bodyMap = subject_bodyTokenizer.getListTerm(body[1], doclength, false);
               
                doclength[5]=doclength[0]+doclength[1]+doclength[2]+doclength[3]+doclength[4];
                //System.out.println(doclength[5]);
                synchronized (Indexing.docMapping) {
                    Indexing.docID++;
                    docNumber = Indexing.docID;
                    String temp= docNumber + "=" + idEmail +"|"
                            +doclength[0]+"," 
                            +doclength[1]+","
                            +doclength[2]+","
                            +doclength[3]+","
                            +doclength[4]+","
                            +doclength[5]+"\r\n";
                    //System.out.println(temp);
                    Indexing.docMapping.write(temp);
                }
                synchronized (Indexing.treeIndexDate) {
                    BigConcurentHashMap.mergeBigHashMap(BigConcurentHashMap.dateConcurentMap, Indexing.treeIndexDate, dateMap, docNumber);
                }
                 synchronized (Indexing.treeIndexFrom) {
                    BigConcurentHashMap.mergeBigHashMap(BigConcurentHashMap.fromConcurentMap, Indexing.treeIndexFrom, fromMap, docNumber);
                }
                  synchronized (Indexing.treeIndexTo) {
                    BigConcurentHashMap.mergeBigHashMap(BigConcurentHashMap.toConcurentMap, Indexing.treeIndexTo, toMap, docNumber);
                }
                  synchronized (Indexing.treeIndexSubject) {
                    BigConcurentHashMap.mergeBigHashMap(BigConcurentHashMap.subjectConcurentMap, Indexing.treeIndexSubject, subjectMap, docNumber);
                }
                   synchronized (Indexing.treeIndexBody) {
                    BigConcurentHashMap.mergeBigHashMap(BigConcurentHashMap.bodyConcurentMap, Indexing.treeIndexBody, bodyMap, docNumber);
                }
            }
            fileWalker.callback(new HashMap<String, Integer>(), new HashMap<String, Integer>(), new HashMap<String, Integer>(), new HashMap<String, Integer>(), new HashMap<String, Integer>(), new HashMap<String, Integer>(), count);

        } catch (Exception e) {

            System.out.println(e.toString() + " ---" + this.path.toString());

            e.printStackTrace();
            System.out.println(this.path);
            try {
                fileWalker.callback(new HashMap<String, Integer>(), new HashMap<String, Integer>(), new HashMap<String, Integer>(), new HashMap<String, Integer>(), new HashMap<String, Integer>(), new HashMap<String, Integer>(), count);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

        }
        return true;
    }
}
