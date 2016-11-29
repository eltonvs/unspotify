package com.imd.lp2.unspotify.view;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.imd.lp2.unspotify.R;
import com.imd.lp2.unspotify.tools.FileTools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SignUpActivity extends AppCompatActivity {
    private EditText edName;
    private EditText edUser;
    private EditText edPass;
    private EditText edPassConfirm;
    private Button btFinish;
    private CheckBox chkVip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setup();
    }

    private void setup() {
        edName = (EditText) findViewById(R.id.edName);
        edUser = (EditText) findViewById(R.id.edUser);
        edPass = (EditText) findViewById(R.id.edPass);
        edPassConfirm = (EditText) findViewById(R.id.edPassConfirm);
        btFinish = (Button) findViewById(R.id.btSignUp);
        chkVip = (CheckBox) findViewById(R.id.chkVip);

        btFinish.setOnClickListener(btFinishListener);
    }

    private View.OnClickListener btFinishListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(edPass.getText().toString().trim().matches(edPassConfirm.getText().toString().trim())){
                FileTools.writeToFile(edName.getText().toString().trim()+";"+edUser.getText().toString().trim()
                        +";"+edPass.getText().toString().trim()
                        +";"+ (chkVip.isChecked() ? "true":"false"),"users.txt", getApplicationContext());
            }
            Toast.makeText(getApplicationContext(), "User: " + edName.getText().toString() + " was succesfully created", Toast.LENGTH_LONG).show();
            clearFields();
        }
    };

    private void clearFields(){
        edName.setText("");
        edUser.setText("");
        edPass.setText("");
        edPassConfirm.setText("");
        chkVip.setChecked(false);

    }
}
