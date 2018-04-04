package com.example.lev.chatdemo;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by LEV on 30/03/2018.
 */


public class MessageSender extends AsyncTask<String,Void,String> {

    private Socket socket;
    MainActivity mainActivity;

    public MessageSender(MainActivity mainActivity, Socket socket) {
        this.socket = socket;
        this.mainActivity = mainActivity;
    }

    @Override
    protected String doInBackground(String... voids) {

        String dataToServer = voids[0];
        String dataFromServer = null;

        try {

            DataOutputStream outToServer =
                    new DataOutputStream(socket.getOutputStream());

            BufferedReader inFromServer =
                    new BufferedReader(new
                            InputStreamReader(socket.getInputStream()));

            outToServer.writeBytes(dataToServer + '\n');

            dataFromServer = inFromServer.readLine();



        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataFromServer;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        mainActivity.checkIfNicknameAvailable(s);

    }
}