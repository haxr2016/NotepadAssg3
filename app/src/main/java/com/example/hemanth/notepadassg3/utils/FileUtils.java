package com.example.hemanth.notepadassg3.utils;

import android.util.JsonReader;
import android.util.Log;

import com.example.hemanth.notepadassg3.models.Note;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by sandeepcmsm on 24/07/17.
 */

public class FileUtils {

    public static String readFromFile(File file) {

        String ret = "";

        try {
            InputStream inputStream = new FileInputStream(file);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);


                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("LoadFiles task", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("LoadFiles task", "Can not read file: " + e.toString());
        } catch (Exception e) {
            Log.e("LoadFiles task", "General Exception: " + e.toString());
        }
        return ret;
    }

    public static boolean saveFile(String data,String filePath){

        try {
            File file = new File(filePath);
            FileOutputStream outputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            return false;
        }
        return true;
    }
}
