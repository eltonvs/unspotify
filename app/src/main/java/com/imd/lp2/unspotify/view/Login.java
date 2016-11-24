package com.imd.lp2.unspotify.view;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.imd.lp2.unspotify.R;
import com.imd.lp2.unspotify.model.User;
import com.imd.lp2.unspotify.model.UserCommon;
import com.imd.lp2.unspotify.model.UserVip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Login extends AppCompatActivity {
    private List<User> listUsers = new ArrayList<>();
    private EditText edUser;
    private EditText edPass;
    private Button btLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setup();
    }

    private void setup() {
        try {
            readFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        edUser = (EditText) findViewById(R.id.edUser);
        edPass = (EditText) findViewById(R.id.edPass);
        btLogin = (Button) findViewById(R.id.btLogin);

        btLogin.setOnClickListener(btLoginListener);
    }

    private void readFile() throws IOException {
        // Construct BufferedReader from FileReader
        BufferedReader in = new BufferedReader(new InputStreamReader(getAssets().open("users.txt")));
        String line ;

        while((line = in.readLine()) != null) {
            User user;
            String[] data = line.split(";");
                if(data[3].contains("true"))
                user = new UserVip(data[0], data[1], data[2]);
            else
                user = new UserCommon(data[0], data[1], data[2]);
            listUsers.add(user);
        }
        in.close();
    }

    private View.OnClickListener btLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            for (User user: listUsers) {
                if(edUser.getText().toString().trim().matches(user.getUserName()) && edPass.getText().toString().trim().matches(user.getPass())){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    Log.d("USER", user.getId()+"");
                    startActivity(intent);
                    return;
                }
            }
            Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_LONG).show();
        }
    };


}
