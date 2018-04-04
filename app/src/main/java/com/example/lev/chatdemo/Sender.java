package com.example.lev.chatdemo;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by LEV on 01/04/2018.
 */

public class Sender extends AsyncTask<String,Void,Void> {


    @Override
    protected Void doInBackground(String... voids) {

        String dataToServer = voids[0];


        try {

            DataOutputStream outToServer =
                    new DataOutputStream(MainActivity.socket.getOutputStream());


            outToServer.writeBytes(dataToServer + '\n');


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}