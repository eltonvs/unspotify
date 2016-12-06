package com.imd.lp2.unspotify.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
        File f = new File(Constants.EXTERNAL_UNSPOTIFY_FOLDER + "playlists/");
        if(listPlaylist != null){
            listPlaylist = new ArrayList<>();
        }
        listViewPlaylist = (ListView) findViewById(R.id.listPlaylists);
        listFilesForFolder(f);
        registerForContextMenu(listViewPlaylist);
        adapter = new PlaylistsAdapter(listPlaylist, getApplicationContext());
        listViewPlaylist.setAdapter(adapter);
        listViewPlaylist.setOnItemClickListener(listPlaylistListener);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Delete Playlist");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Playlist playlist = (Playlist) adapter.getItem(info.position);
        switch (item.getItemId()){
            case 0:
//                Toast.makeText(getApplicationContext(), playlist.getName(), Toast.LENGTH_LONG).show();
                if(LoginActivity.currentUser.getUserName().contains(playlist.getUserName())){
                    File file = new File(Constants.EXTERNAL_UNSPOTIFY_FOLDER + "playlists/"+playlist.getName()+".txt");
                    file.delete();
                    setup();
                } else{
                    Toast.makeText(getApplicationContext(), "You're not the owner of the list", Toast.LENGTH_LONG).show();
                }

        }
        return super.onContextItemSelected(item);
    }

    public void listFilesForFolder(final File folder) {
        try {
            for (final File fileEntry : folder.listFiles()) {
                if (fileEntry.isFile()) {
                    readPlaylist(fileEntry.getName());
                } else {
                    System.out.println(fileEntry.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readPlaylist(String file) throws IOException {
        // Construct BufferedReader from FileReader
        FileReader fw = new FileReader(Constants.EXTERNAL_UNSPOTIFY_FOLDER + "playlists/" + file);
        BufferedReader in = new BufferedReader(fw);
        String[] data = in.readLine().split(";");
        Playlist playlist = new Playlist(data[0], data[1], null);
        listPlaylist.add(playlist);
        in.close();
    }

    private AdapterView.OnItemClickListener listPlaylistListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Playlist pl = (Playlist) listViewPlaylist.getItemAtPosition(i);
            Intent data = new Intent();
            data.setData(Uri.parse(pl.getName()));
            setResult(RESULT_OK, data);
            finish();
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
