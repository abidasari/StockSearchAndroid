package com.adasari.stocksse;

/**
 * Created by adasari on 5/2/16.
 */
public class SuggestGetSet {

    String id,label;
    public SuggestGetSet(String id, String label){
        this.setId(id);
        this.setName(label);
    }
    @Override
    public String toString(){
        return id;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return label;
    }

    public void setName(String label) {
        this.label = label;
    }

}
