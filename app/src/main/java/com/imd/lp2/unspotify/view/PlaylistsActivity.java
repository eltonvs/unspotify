package com.imd.lp2.unspotify.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.imd.lp2.unspotify.R;
import com.imd.lp2.unspotify.adapter.PlaylistsAdapter;
import com.imd.lp2.unspotify.model.Playlist;
import com.imd.lp2.unspotify.tools.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistsActivity extends AppCompatActivity {
    private List<Playlist> listPlaylist = new ArrayList<>();
    private ListView listViewPlaylist;
    private PlaylistsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists);

        setup();
    }

    private void setup() {
        File f = new File(Constants.EXTERNAL_UNSPOTIFY_FOLDER+"playlists/");
        listFilesForFolder(f);
        listViewPlaylist = (ListView) findViewById(R.id.listPlaylists);
        adapter = new PlaylistsAdapter(listPlaylist, getApplicationContext());
        listViewPlaylist.setAdapter(adapter);
        listViewPlaylist.setOnItemClickListener(listPlaylistListener);
    }

    private AdapterView.OnItemClickListener listPlaylistListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getApplicationContext(), AddPlaylistActivity.class);
            Playlist pl = (Playlist) listViewPlaylist.getItemAtPosition(i);
            intent.putExtra("playlistName", pl.getName());
            startActivity(intent);
        }
    };

    public void listFilesForFolder(final File folder) {
        try {
            for (final File fileEntry : folder.listFiles()) {
                if (fileEntry.isFile()) {
                    readPlaylist(fileEntry.getName());
                } else {
                    System.out.println(fileEntry.getName());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void readPlaylist(String file) throws IOException {
        // Construct BufferedReader from FileReader
        FileReader fw = new FileReader(Constants.EXTERNAL_UNSPOTIFY_FOLDER+"playlists/"+file);
        BufferedReader in = new BufferedReader(fw);
        String[] data = in.readLine().split(";");
        Playlist playlist = new Playlist(data[0], data[1], null);
        listPlaylist.add(playlist);
        in.close();
    }
}
