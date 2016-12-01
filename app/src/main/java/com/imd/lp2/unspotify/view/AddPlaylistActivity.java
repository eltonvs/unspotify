package com.imd.lp2.unspotify.view;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.imd.lp2.unspotify.R;
import com.imd.lp2.unspotify.adapter.MusicAdapter;
import com.imd.lp2.unspotify.model.Music;
import com.imd.lp2.unspotify.tools.Constants;
import com.imd.lp2.unspotify.tools.FileTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddPlaylistActivity extends AppCompatActivity {
    private String file;
    private ListView listViewMusics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_playlist);
        file = getIntent().getExtras().getString("playlistName");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent, "Select song"), 0);
                }catch (ActivityNotFoundException e){
                    Toast.makeText(getApplicationContext(), "None file manager found", Toast.LENGTH_LONG).show();
                }
            }
        });
        try {
            listViewMusics = (ListView) findViewById(R.id.listMusics);
            readMusic(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMusic(String playlist) throws IOException {
        // Construct BufferedReader from FileReader
        FileReader fw = new FileReader(Constants.EXTERNAL_UNSPOTIFY_FOLDER+"playlists/"+playlist+".txt");
        BufferedReader in = new BufferedReader(fw);
        String line ;
        List<Music> listMusic = new ArrayList<>();
        int i = 0;
        while((line = in.readLine()) != null) {
            if(i > 0){
                String[] data = line.split(";");
                Music music = new Music(data[0], data[1], 1000, "Tool");
                listMusic.add(music);
            }
            i++;
        }
        listViewMusics.setAdapter(null);
        MusicAdapter adapter = new MusicAdapter(listMusic, getApplicationContext());
        listViewMusics.setAdapter(adapter);
        in.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String filePath = getRealPath(uri);
                    String fileName = new File(filePath).getName();
                    FileTools.writeToFile(filePath+";"+fileName, Constants.EXTERNAL_UNSPOTIFY_FOLDER+"playlists",file+".txt", getApplicationContext());
                    try {
                        readMusic(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getRealPath(Uri uri) {
        String wholeID = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            wholeID = DocumentsContract.getDocumentId(uri);
        }

        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = getContentResolver().
                query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{ id }, null);

        String filePath = "";

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }

        cursor.close();
        return filePath;
    }

}
