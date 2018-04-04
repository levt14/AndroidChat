package com.example.lev.chatdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Socket;


public class MainActivity extends AppCompatActivity {

    EditText nickname;
    TextView status;
    ConnectToServer connectToServer;
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static Socket socket;
    public static boolean connectedToServer = true;



    private void openChatActivity() {
        Intent intent = new Intent(this,ChatActivity.class);
        intent.putExtra(EXTRA_MESSAGE, nickname.getText().toString());
        startActivity(intent);
     }

    public  void checkIfNicknameAvailable(String result) {
        if(result == null) {
            status.setText("There is no connection to the server");
            return;
        }
        if(result.equals("0")) {
            status.setText("This nickname is already taken");
            return;
        }
        else {
            socket = connectToServer.getSocket();
            openChatActivity();

        }

    }


    public boolean checkNickname() {
        if(nickname.getText().toString().length() == 0) {
            Toast.makeText(this, "Enter a nickname.", Toast.LENGTH_SHORT).show();
            return true;
        }
        if(nickname.getText().toString().contains(" ")) {
            Toast.makeText(this, "Enter a nickname without spaces.", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public void checkIfNicknameAvailable(View view) {
        if(checkNickname()) {
            return;
        }

        if(connectToServer.getSocket() == null) {
            openChatActivity();

        }
        else {
            new MessageSender(this, connectToServer.getSocket()).execute(nickname.getText().toString());

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nickname = findViewById(R.id.nickname);
        status = findViewById(R.id.status);

        connectToServer = (ConnectToServer) new ConnectToServer().execute();

    }
}
