package com.imd.lp2.unspotify.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.imd.lp2.unspotify.tools.FileTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private List<User> listUsers = new ArrayList<>();
    private EditText edUser;
    private EditText edPass;
    private Button btLogin;
    private Button btSignUp;
    public static User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setup();
    }

    private void setup() {
        File folder = new File("sdcard/unspotify/users.txt");
        if(!folder.exists())
            FileTools.writeToFile("Administrador;admin;admin;true", "users.txt",getApplicationContext());

        edUser = (EditText) findViewById(R.id.edName);
        edPass = (EditText) findViewById(R.id.edPass);
        btLogin = (Button) findViewById(R.id.btLogin);
        btSignUp = (Button) findViewById(R.id.btSignUp);

        btLogin.setOnClickListener(btLoginListener);
        btSignUp.setOnClickListener(btSignUpListener);
    }

    private void readUsers() throws IOException {
        // Construct BufferedReader from FileReader
        File folder = new File("sdcard/unspotify");
        FileReader fw = new FileReader(folder.getPath()+"/users.txt");
        BufferedReader in = new BufferedReader(fw);
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
            try {
                readUsers();
                for (User user: listUsers) {
                    if(edUser.getText().toString().trim().matches(user.getUserName()) && edPass.getText().toString().trim().matches(user.getPass())){
                        currentUser = user;
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        Log.d("USER", user.getId()+"");
                        startActivity(intent);
                        return;
                    }
                }
                Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private View.OnClickListener btSignUpListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
        }
    };

}
