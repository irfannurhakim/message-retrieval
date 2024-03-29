/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.message_retrieval;

/**
 *
 * @author irfannurhakim
 */
public class Parser {

   

    /**
     * author: Irfan
     * @param collection
     * @param keyword
     * @return
     */
    public static int findIdx(String collection, String keyword) {
        return collection.indexOf(keyword);
    }

    /**
     * author: Elisafina
     * method untuk menghapus apostrope:
     * jika 's maka akan dihapus (misalkan mary's ==> mary)
     * jika ' saja maka hanya dihapus apostropenya saja
     * 
     * @param s
     * @return s
     */
    public static String removeApostrope(String s) {
        //s =Pattern.compile("'s").matcher(s).replaceAll("");
        //s =Pattern.compile("'").matcher(s).replaceAll("");
        return s.replaceAll("'s", "").replaceAll("'", "");
    }

    /**
     * author Elisafina
     * untuk menggabungkan kata-kata yang memiliki tanda hubung -
     * misalkan state-of-the-art menjadi stateoftheart
     * @param s
     * @return
     */
    public static String removeHypenate(String s) {
        if (s.matches(".*([a-zA-Z]+-)*[a-zA-Z]+.*")) {
            s = s.replaceAll("-", "");
        }
        return s;
    }


    /**
     * author: Irfan
     * @param s
     * @return
     */
    public static String removePuncuation(String s) {
        //return (Pattern.compile("\\p{Punct}").matcher(s).replaceAll(" "));
        //return s.replaceAll("\\p{Punct}", "");
         return s.replaceAll("[^A-Za-z0-9\":\\s]", "");
    }
    
    /**
     * author: Elisafina
     * menghapus semua html tag beserta semua atributnya
     * @param allString
     * @return
     */
    public static String removeHTMLTag(String allString){
        //return (Pattern.compile("<(\"[^\"]*\"|'[^']*'|[^'\">])*>").matcher(allString).replaceAll(""));
        //return allString.replaceAll("<(\"[^\"]*\"|'[^']*'|[^'\">])*>", "");
        return allString.replaceAll("\\<.*?\\>", "");
    }
    
    public static String parseQuery (String query)
    {
        query=removeApostrope(query);
        query= removeHypenate(query);
        query= removePuncuation(query);
        return query;
    }
    
    public static void main(String[] args) {
        
        System.out.println(parseQuery("saya, adalah. makan-makan budi's mc'd seorang \"gembala\""));
    }
}
