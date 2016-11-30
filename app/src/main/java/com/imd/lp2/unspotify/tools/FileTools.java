package com.imd.lp2.unspotify.tools;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Mobile on 29/11/2016.
 */
public class FileTools {

    public static void writeToFile(String data,String path,String sFileName, Context context) {
        try {
            File folder = new File(path);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            FileWriter fw = new FileWriter(path+"/"+sFileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.println(data);
            out.flush();
            out.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
