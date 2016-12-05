package com.imd.lp2.unspotify.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.imd.lp2.unspotify.R;
import com.imd.lp2.unspotify.tools.Constants;
import com.imd.lp2.unspotify.tools.FileTools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {
    private EditText edName;
    private EditText edUser;
    private EditText edPass;
    private EditText edPassConfirm;
    private Button btFinish;
    private CheckBox chkVip;
    private View.OnClickListener btFinishListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String name = edName.getText().toString().trim();
            String user = edUser.getText().toString().trim().toLowerCase();
            String pass = edPass.getText().toString().trim();
            String confPass = edPassConfirm.getText().toString().trim();

            if (!name.isEmpty() && !pass.isEmpty() && pass.matches(confPass)) {
                if (!userExists(user)) {
                    FileTools.writeToFile(name + ";" + user + ";" + pass + ";" + (chkVip.isChecked() ? "true" : "false"), Constants.EXTERNAL_UNSPOTIFY_FOLDER, "users.txt", getApplicationContext());
                    Toast.makeText(getApplicationContext(), "User: " + name + " was succesfully created", Toast.LENGTH_LONG).show();
                    clearFields();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "This user already exists", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Error: The user wasn't created", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setup();
    }

    private boolean userExists(String user) {
        try {
            FileReader fw = new FileReader(Constants.EXTERNAL_UNSPOTIFY_FOLDER + "users.txt");
            BufferedReader in = new BufferedReader(fw);
            String line;

            while ((line = in.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length < 4)
                    continue;
                if (data[1].equals(user)) {
                    return true;
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
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

    private void clearFields() {
        edName.setText("");
        edUser.setText("");
        edPass.setText("");
        edPassConfirm.setText("");
        chkVip.setChecked(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
