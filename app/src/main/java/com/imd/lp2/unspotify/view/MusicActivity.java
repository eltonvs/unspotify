package com.imd.lp2.unspotify.view;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
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

public class MusicActivity extends AppCompatActivity {
    private String file;
    private ListView listViewMusics;
    private MediaPlayer player;
    private SeekBar seekBar;
    private Handler mHandler = new Handler();
    private ImageButton btPlayPause;

    private int currentSong;
    private ImageButton btNext;
    private ImageButton btPrevious;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        file = getIntent().getExtras().getString("playlistName");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        View includedLayout = findViewById( R.id.footer); // root View id from that link
        btPlayPause = (ImageButton) includedLayout.findViewById(R.id.btPlayPause);
        btNext = (ImageButton) includedLayout.findViewById(R.id.btNextSong);
        btPrevious = (ImageButton) includedLayout.findViewById(R.id.btPrevious);
        seekBar = (SeekBar) includedLayout.findViewById(R.id.seekBar);

        btPlayPause.setOnClickListener(btPlayStopListener);
        btNext.setOnClickListener(btNextSongListener);
        btPrevious.setOnClickListener(btPreviousSongListener);

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
            listViewMusics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    currentSong = i;
                    btPlayPause.setImageResource(android.R.drawable.ic_media_pause);
                    playMusic();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playMusic() {
        Music music = (Music) listViewMusics.getItemAtPosition(currentSong);
        File file = new File(music.getPath());
        Uri uri = Uri.fromFile(file);
        MusicaTask musicaTask = new MusicaTask();
        musicaTask.execute(uri);
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
                Music music = new Music(data[0], data[1]);
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


    private class MusicaTask extends AsyncTask<Uri, Void, Void> {
        @Override
        protected Void doInBackground(Uri... files) {
            if(player != null){
                player.stop();
                player.release();
                player = null;
            }
            player = MediaPlayer.create(MusicActivity.this, files[0]);
            player.setLooping(false); // Set looping
            player.setVolume(1.0f, 1.0f);
            player.start();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(player != null && player.isPlaying()){
                        seekBar.setMax(player.getDuration());
                        seekBar.setProgress(player.getCurrentPosition());
                        if(player.getCurrentPosition() >= player.getDuration()){
                            currentSong++;
                            playMusic();
                        } else if(currentSong > listViewMusics.getCount()){
                            currentSong = 0;
                            playMusic();
                        }
                    }
                    mHandler.postDelayed(this, 1000);
                }
            });
            return null;
        }

    }

    private View.OnClickListener btPlayStopListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(player != null){
                if(player.isPlaying()){
                    player.pause();
                    btPlayPause.setImageResource(android.R.drawable.ic_media_play);
                }else{
                    player.seekTo(player.getCurrentPosition());
                    seekBar.setProgress(player.getCurrentPosition());
                    player.start();
                    btPlayPause.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        }
    };

    private View.OnClickListener btNextSongListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            currentSong++;
            if(currentSong >= listViewMusics.getCount()){
                currentSong = 0;
            }
            playMusic();
        }
    };

    private View.OnClickListener btPreviousSongListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            currentSong--;
            if(currentSong < 0){
                currentSong = listViewMusics.getCount()-1;
            }
            playMusic();
        }
    };

}
