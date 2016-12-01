package com.imd.lp2.unspotify.model;

/**
 * Created by johnnylee on 24/11/16.
 */

public class Music {
    private String nome;
    private String path;

    public Music(String path, String nome) {
        this.nome = nome;
        this.path = path;
    }

    public String getName() {
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

}
