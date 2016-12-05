package com.imd.lp2.unspotify.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.imd.lp2.unspotify.R;
import com.imd.lp2.unspotify.adt.bst.BinarySearchTree;
import com.imd.lp2.unspotify.model.User;
import com.imd.lp2.unspotify.model.UserCommon;
import com.imd.lp2.unspotify.model.UserVip;
import com.imd.lp2.unspotify.tools.Constants;
import com.imd.lp2.unspotify.tools.FileTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    public static User currentUser;
    private BinarySearchTree<User> bstUsers = new BinarySearchTree<>();
    private EditText edUser;
    private EditText edPass;
    private Button btLogin;
    private Button btSignUp;
    private View.OnClickListener btLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                readUsers();
                for (User user : bstUsers.getArrayList()) {
                    if (edUser.getText().toString().trim().matches(user.getUserName()) && edPass.getText().toString().trim().matches(user.getPass())) {
                        currentUser = user;
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        Log.d("USER", user.getId() + "");
                        return;
                    }
                }
                Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setup();
    }

    private void setup() {
        grantStoragePermission();

        File folder = new File(Constants.EXTERNAL_UNSPOTIFY_FOLDER);
        if (!folder.exists()) {
            FileTools.writeToFile("Administrador;admin;admin;true", folder.getPath(), "users.txt", getApplicationContext());
        }

        edUser = (EditText) findViewById(R.id.edName);
        edPass = (EditText) findViewById(R.id.edPass);
        btLogin = (Button) findViewById(R.id.btLogin);
        btSignUp = (Button) findViewById(R.id.btSignUp);

        btLogin.setOnClickListener(btLoginListener);

        // TODO: Remove this before submit!!!
        edUser.setText("admin");
        edPass.setText("admin");
        // btLogin.performClick();
    }

    private void readUsers() throws IOException {
        // Construct BufferedReader from FileReader
        FileReader fw = new FileReader(Constants.EXTERNAL_UNSPOTIFY_FOLDER + "users.txt");
        BufferedReader in = new BufferedReader(fw);
        String line;
        User user;

        while ((line = in.readLine()) != null) {
            String[] data = line.split(";");
            if (data.length < 4)
                continue;
            user = data[3].contains("true") ? new UserVip(data[0], data[1], data[2]) : new UserCommon(data[0], data[1], data[2]);
            bstUsers.insert(user);
        }
        in.close();
    }

    public boolean grantStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission is revoked");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;
        }

        // permission is automatically granted on sdk < 23 upon installation
        Log.v(TAG, "Permission is granted");
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + " was " + grantResults[0]);
        }
    }

}
