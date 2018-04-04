package com.example.lev.chatdemo;

import android.os.AsyncTask;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by LEV on 01/04/2018.
 */

public class ConnectToServer extends AsyncTask<Void,Void,Void> {

    private Socket socket;

    public ConnectToServer() {
        this.socket = null;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            socket = new Socket("192.168.1.12", 10000);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Socket getSocket() {
        return socket;
    }
}