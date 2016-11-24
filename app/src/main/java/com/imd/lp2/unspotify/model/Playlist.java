package com.imd.lp2.unspotify.model;

import java.util.List;

/**
 * Created by johnnylee on 24/11/16.
 */

public class Playlist {
    private Integer id;
    private String name;
    private Integer idUser;
    private List<Music> listMusics;

    public Playlist() {

    }

    public Playlist(Integer id, String name, Integer idUsuario, List<Music> listMusics) {
        this.id = id;
        this.name = name;
        this.idUser = idUsuario;
        this.listMusics = listMusics;
    }

    public void addSong(Music music){
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

    public Integer getIdUsuario() {
        return idUser;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUser = idUsuario;
    }

    public List<Music> getListMusicas() {
        return listMusics;
    }

    public void setListMusicas(List<Music> listMusics) {
        this.listMusics = listMusics;
    }
}
