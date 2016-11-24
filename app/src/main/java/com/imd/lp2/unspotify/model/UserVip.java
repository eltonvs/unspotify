package com.imd.lp2.unspotify.model;

import java.util.List;

/**
 * Created by johnnylee on 24/11/16.
 */

public class UserVip extends User {

    public UserVip(String name, String userName, String pass) {
        super(name, userName, pass);
    }

    public void createPlaylist(Integer id, String name, Integer idUsuario, List<Music> listMusics) {
        Playlist playlist = new Playlist(id, name, idUsuario, listMusics);
    }
}
