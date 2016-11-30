package com.imd.lp2.unspotify.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.imd.lp2.unspotify.R;

public class AddPlaylistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_playlist);
        String file = getIntent().getExtras().getString("playlistName");
    }
}
