package com.imd.lp2.unspotify.model;

/**
 * Created by johnnylee on 24/11/16.
 */

public class Music {
    private String nome;
    private String path;
    private Integer duration;
    private String artist;

    public Music(String nome, String path, Integer duration, String artist) {
        this.nome = nome;
        this.path = path;
        this.duration = duration;
        this.artist = artist;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
