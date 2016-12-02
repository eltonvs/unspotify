package com.imd.lp2.unspotify.view;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.imd.lp2.unspotify.R;
import com.imd.lp2.unspotify.adapter.MusicAdapter;
import com.imd.lp2.unspotify.model.Music;
import com.imd.lp2.unspotify.model.UserVip;
import com.imd.lp2.unspotify.tools.Constants;
import com.imd.lp2.unspotify.tools.FileTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "LOG";
    private Dialog dialog;
    private String file;
    private ListView listViewMusics;
    private MediaPlayer player;
    private SeekBar seekBar;
    private Handler mHandler = new Handler();
    private ImageButton btPlayPause;
    private List<Music> listMusic = new ArrayList<>();
    private int currentSong;
    private ImageButton btNext;
    private ImageButton btPrevious;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            file = getIntent().getExtras().getString("playlistName");
        fab = (FloatingActionButton) findViewById(R.id.fab);
        View includedLayout = findViewById(R.id.footer); // root View id from that link
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btPlayPause = (ImageButton) includedLayout.findViewById(R.id.btPlayPause);
        btNext = (ImageButton) includedLayout.findViewById(R.id.btNextSong);
        btPrevious = (ImageButton) includedLayout.findViewById(R.id.btPrevious);
        seekBar = (SeekBar) includedLayout.findViewById(R.id.seekBar);

        btPlayPause.setOnClickListener(btPlayStopListener);
        btNext.setOnClickListener(btNextSongListener);
        btPrevious.setOnClickListener(btPreviousSongListener);

        Log.d("USUARIO", LoginActivity.currentUser.getName());
        btPlayPause.setOnClickListener(btPlayStopListener);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (LoginActivity.currentUser instanceof UserVip)
            navigationView.inflateMenu(R.menu.menu_vip);
        else
            navigationView.inflateMenu(R.menu.menu_common);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent, "Select song"), 0);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "No file manager found", Toast.LENGTH_LONG).show();
                }
            }
        });
        listViewMusics = (ListView) findViewById(R.id.listMusics);
        if (file == null){
            readMusic();
            fab.setVisibility(View.GONE);
        }
        listViewMusics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentSong = i;
                btPlayPause.setImageResource(android.R.drawable.ic_media_pause);
                playMusic();
            }
        });
    }

    private void playMusic() {
        Music music = (Music) listViewMusics.getItemAtPosition(currentSong);
        File file = new File(music.getPath());
        Uri uri = Uri.fromFile(file);
        MusicaTask musicaTask = new MusicaTask();
        musicaTask.execute(uri);
    }

    private void readMusic() {
        File f = new File(Constants.EXTERNAL_UNSPOTIFY_FOLDER + "playlists/");
        try {
            for (final File fileEntry : f.listFiles()) {
                if (fileEntry.isFile()) {
                    readMusic(fileEntry.getName().replace(".txt", ""), false);
                } else {
                    System.out.println(fileEntry.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readMusic(String playlist, boolean clear) throws IOException {
        // Construct BufferedReader from FileReader
        FileReader fw = new FileReader(Constants.EXTERNAL_UNSPOTIFY_FOLDER + "playlists/" + playlist + ".txt");
        BufferedReader in = new BufferedReader(fw);
        String line;
        if (clear) {
            listMusic = new ArrayList<>();
            listViewMusics.setAdapter(null);
        }
        int i = 0;
        while ((line = in.readLine()) != null) {
            if (i > 0) {
                String[] data = line.split(";");
                Music music = new Music(data[0], data[1]);
                listMusic.add(music);
            }
            i++;
        }
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
                    String filePath = FileTools.getRealPath(uri, getApplicationContext());
                    String fileName = new File(filePath).getName();
                    for (Music m : listMusic) {
                        if (m.getPath().contains(filePath)) {
                            Toast.makeText(getApplicationContext(), "Music already on this playlist", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    try {
                        FileTools.writeToFile(filePath + ";" + fileName, Constants.EXTERNAL_UNSPOTIFY_FOLDER + "playlists", file + ".txt", getApplicationContext());
                        readMusic(file, true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 1:
                try {
                    if (data != null && data.getDataString() != null) {
                        file = data.getDataString();
                        readMusic(file, true);
                        fab.setVisibility(View.VISIBLE);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void menuCommon(int itemId) {
        switch (itemId) {
            case R.id.nav_logout:
                break;
            case R.id.nav_select_playlist:
                break;
        }
    }

    List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file));
            }
        }
        return inFiles;
    }

    private void menuVip(int posicao) {
        switch (posicao) {
            case R.id.nav_add_folder:
//                List<File> files = getListFiles(new File("YOUR ROOT"));

                break;
            case R.id.nav_add_playlist:
                if (player != null) {
                    player.stop();
                    player.release();
                    player = null;
                }
                dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_create_playlist);
                Button btCreate = (Button) dialog.findViewById(R.id.btCreatePlaylist);
                final EditText edPlaylistName = (EditText) dialog.findViewById(R.id.edDialogPlaylistName);
                btCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StringBuilder sbPlaylistName = new StringBuilder();
                        sbPlaylistName.append(edPlaylistName.getText().toString()).append(";");
                        sbPlaylistName.append(LoginActivity.currentUser.getName());
                        FileTools.writeToFile(sbPlaylistName.toString(), Constants.EXTERNAL_UNSPOTIFY_FOLDER + "playlists", edPlaylistName.getText().toString() + ".txt", getApplicationContext());
                        try {
                            file = edPlaylistName.getText().toString();
                            readMusic(file, true);
                            fab.setVisibility(View.VISIBLE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.nav_logout:
                break;
            case R.id.nav_register:
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_select_playlist:
                Intent intentPlaylists = new Intent(getApplicationContext(), PlaylistsActivity.class);
                startActivityForResult(intentPlaylists, 1);
                break;
        }
    }



    private class MusicaTask extends AsyncTask<Uri, Void, Void> {

        @Override
        protected Void doInBackground(Uri... files) {
            if (player != null) {
                player.stop();
                player.release();
                player = null;
            }
            player = MediaPlayer.create(MainActivity.this, files[0]);
            player.setLooping(false); // Set looping
            player.setVolume(1.0f, 1.0f);
            player.start();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (player != null && player.isPlaying()) {
                        seekBar.setMax(player.getDuration());
                        seekBar.setProgress(player.getCurrentPosition());
                        if (player.getCurrentPosition() >= player.getDuration()) {
                            currentSong++;
                            playMusic();
                        } else if (currentSong > listViewMusics.getCount()) {
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
            if (player != null) {
                if (player.isPlaying()) {
                    player.pause();
                    btPlayPause.setImageResource(android.R.drawable.ic_media_play);
                } else {
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
            if (currentSong >= listViewMusics.getCount()) {
                currentSong = 0;
            }
            playMusic();
        }
    };

    private View.OnClickListener btPreviousSongListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            currentSong--;
            if (currentSong < 0) {
                currentSong = listViewMusics.getCount() - 1;
            }
            playMusic();
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        if (LoginActivity.currentUser instanceof UserVip) {
            menuVip(item.getItemId());
        } else {
            menuCommon(item.getItemId());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
