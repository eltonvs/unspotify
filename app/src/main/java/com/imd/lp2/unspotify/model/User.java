package com.imd.lp2.unspotify.model;

/**
 * Created by johnnylee on 24/11/16.
 */

public abstract  class User {
    private static Integer id = 0;
    private String name;
    private String userName;
    private String pass;

    static {
        id++;
    }

    public User(String name, String userName, String pass) {
        this.name = name;
        this.userName = userName;
        this.pass = pass;
    }

    public static Integer getId() {
        return id;
    }

    public static void setId(Integer id) {
        User.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
