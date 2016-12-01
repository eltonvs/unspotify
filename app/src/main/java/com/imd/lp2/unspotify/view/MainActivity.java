package com.imd.lp2.unspotify.view;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.imd.lp2.unspotify.R;
import com.imd.lp2.unspotify.model.UserVip;
import com.imd.lp2.unspotify.tools.Constants;
import com.imd.lp2.unspotify.tools.FileTools;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "LOG";
    public static MediaPlayer player;
    private SeekBar seekBar;
    private Handler mHandler = new Handler();
    private Button btPlayStop;
    private MusicaTask musicaTask;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setup();
    }

    private void setup(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("USUARIO",  LoginActivity.currentUser.getName());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                    intent.setType("*/*");
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                    try {
//                        startActivityForResult(Intent.createChooser(intent, "Select your playlist"), 0);
//                    }catch (ActivityNotFoundException e){
//                        Toast.makeText(getApplicationContext(), "None file manager found", Toast.LENGTH_LONG).show();
//                    }

                }
            });
        }
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        btPlayStop = (Button) findViewById(R.id.btPlayStop);

        btPlayStop.setOnClickListener(btPlayStopListener);

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    Log.d(TAG, "File Uri: " + uri.toString());
                    String path = uri.getPath();
                    Log.d(TAG, "File Path: " + path);
                    musicaTask = new MusicaTask();
                    musicaTask.execute(uri);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

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

    private void menuCommon(int itemId) {
        switch (itemId) {
            case R.id.nav_logout:
                break;
            case R.id.nav_select_playlist:
                break;
        }
    }

    private void menuVip(int posicao) {
        switch (posicao) {
            case R.id.nav_add_folder:
                break;
            case R.id.nav_add_playlist:
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
                        FileTools.writeToFile(sbPlaylistName.toString(), Constants.EXTERNAL_UNSPOTIFY_FOLDER+"playlists/",edPlaylistName.getText().toString()+".txt",  getApplicationContext());
                        Intent intentPlaylist = new Intent(getApplicationContext(), MusicActivity.class);
                        intentPlaylist.putExtra("playlistName", edPlaylistName.getText().toString());
                        startActivity(intentPlaylist);
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
                startActivity(intentPlaylists);
                break;
        }
    }

    private class MusicaTask extends AsyncTask<Uri, Void, Void>{
        @Override
        protected Void doInBackground(Uri... files) {
            player = MediaPlayer.create(MainActivity.this, files[0]);
            player.setLooping(false); // Set looping
            player.setVolume(1.0f, 1.0f);
            player.start();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(player != null && player.isPlaying()){
                        seekBar.setMax(player.getDuration());
                        seekBar.setProgress(player.getCurrentPosition());
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
                }else{
                    player.seekTo(player.getCurrentPosition());
                    seekBar.setProgress(player.getCurrentPosition());
                    player.start();
                }
            }
        }
    };



}
