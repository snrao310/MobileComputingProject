package com.mobilecomputing.group3.mcproject;

/**
 * Created by sureshgururajan on 4/19/16.
 */
public class SearchClass {
    String username;
    String name;

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setName(String name){this.name=name;}

    public SearchClass(String name,String username) {
        super();
        setName(name);
        setUsername(username);
    }
}
