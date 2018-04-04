package com.example.lev.chatdemo;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.os.Handler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements TextWatcher  {

    RecyclerView recyclerView;
    MyRecyclerViewAdapter adapter;

    String name;
    EditText message;
    Button sendButtonEnable;
    Button sendButtonDisable;
    ArrayList<String> messages = new ArrayList<String>();

    public static String msg = "none";


    public  void receiveMessage(String m) {

            messages.add(m);

            adapter = new MyRecyclerViewAdapter(messages, this);
            recyclerView.setAdapter(adapter);

            recyclerView.scrollToPosition(messages.size()-1);

    }


    public void sendMessage(View view) {

        msg = message.getText().toString();

        messages.add(name + ": " + message.getText().toString());

        adapter = new MyRecyclerViewAdapter(messages, this);
        recyclerView.setAdapter(adapter);

        message.setText("");
        recyclerView.scrollToPosition(messages.size()-1);

        //send message to server
        if(MainActivity.socket != null) {
            new Sender().execute(msg);
        }

    }

    Thread t1 = new Thread(new Runnable() {
        Handler handler = new Handler();

        @Override
        public void run() {
            while (true) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChatActivity.this, "There is no connection to the server", Toast.LENGTH_SHORT).show();
                    }
                });
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);



        if(MainActivity.socket != null) {
            Thread myThread = new Thread(new MyServerThread());
            myThread.start();
        }
        else {
            t1.start();
        }



        //close the keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        recyclerView =  findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        Intent intent = getIntent();
        name = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        sendButtonEnable = findViewById(R.id.send_button_enable);
        sendButtonDisable = findViewById(R.id.send_button_disable);

        message = findViewById(R.id.message);
        message.addTextChangedListener(this);


        message.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                message.getWindowVisibleDisplayFrame(r);
                int screenHeight = message.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                Log.i("info", "keypadHeight = " + keypadHeight);

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT,
                            700 ); // or set height to any fixed value you want
                    recyclerView.setLayoutParams(lp);
                }
                else {
                    // keyboard is closed
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT,
                            1400 ); // or set height to any fixed value you want
                    recyclerView.setLayoutParams(lp);
                }
            }
        });

//       new MessageReceiver(this).execute();

    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


//        if(name.length() + message.length() == 34) {
//
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//
//                    Instrumentation inst = new Instrumentation();
//                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);
//                }
//            }).start();
//        }

        if(message.getText().toString().length() == 0 || !(message.getText().toString().trim().length() > 0)) {
            sendButtonEnable.setVisibility(View.GONE);
            sendButtonDisable.setVisibility(View.VISIBLE);
        }
        else{
            sendButtonDisable.setVisibility(View.GONE);
            sendButtonEnable.setVisibility(View.VISIBLE);
            sendButtonEnable.isFocusable();

        }
    }

    @Override
    public void afterTextChanged(Editable s) {


    }



    ////////////////////////////////////////////////


    class MyServerThread implements Runnable {

        Handler handler = new Handler();
        String message = "none";

        @Override
        public void run() {

            try {

                while (true) {

                    BufferedReader inFromServer =
                            new BufferedReader(new
                                    InputStreamReader(MainActivity.socket.getInputStream()));

                    message = inFromServer.readLine();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            receiveMessage(message);
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
