/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.message_retrieval;

/**
 *
 * @author user
 */
public class DocMappingModel {
    
    String messID;
    long docLength [];

    public DocMappingModel() {
    }

    public DocMappingModel(String messID, long[] docLength) {
        this.messID = messID;
        this.docLength = docLength;
    }

    public String getMessID() {
        return messID;
    }

    public void setMessID(String messID) {
        this.messID = messID;
    }

    public long[] getDocLength() {
        return docLength;
    }

    public void setDocLength(long[] docLength) {
        this.docLength = docLength;
    }
    
    
    
}
