package com.imd.lp2.unspotify.model;

import java.util.List;

/**
 * Created by johnnylee on 24/11/16.
 */

public class Playlist {
    private Integer id;
    private String name;
    private String userName;
    private List<Music> listMusics;

    public Playlist() {

    }

    public Playlist(String name, String idUsuario, List<Music> listMusics) {
        this.name = name;
        this.userName = idUsuario;
        this.listMusics = listMusics;
    }

    public void addSong(Music music) {
        listMusics.add(music);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public List<Music> getListMusicas() {
        return listMusics;
    }

    public void setListMusicas(List<Music> listMusics) {
        this.listMusics = listMusics;
    }
}
