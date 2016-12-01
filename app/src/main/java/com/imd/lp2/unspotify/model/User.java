package com.imd.lp2.unspotify.model;

import java.util.UUID;

/**
 * Created by johnnylee on 24/11/16.
 */

public abstract  class User implements Comparable<User> {
    private static Long id;
    private String name;
    private String userName;
    private String pass;

    static {
        UUID uniqueID = UUID.randomUUID();
        id = Math.abs(uniqueID.getMostSignificantBits());
    }

    public User(String name, String userName, String pass) {
        this.name = name;
        this.userName = userName;
        this.pass = pass;
    }

    public static Long getId() {
        return id;
    }

    public static void setId(Long id) {
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

    @Override
    public int compareTo(User user) {
        return id.compareTo(user.getId());
    }
}
