package com.team18.studybuddy.studybuddy;

/**
 * Created by Alpaca on 10/16/2015.
 */
public class SuggestGetSet {

    String id,name;
    public SuggestGetSet(String id, String name){
        this.setId(id);
        this.setName(name);
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}