package com.example.lev.chatdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by LEV on 26/03/2018.
 */


public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> messages = new ArrayList<String>() ;


    public MyRecyclerViewAdapter(ArrayList<String> list, Context context){
        this.messages = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_list, parent, false);
        MyViewHolder myViewHolder= new MyViewHolder(v);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.message.setText(messages.get(position));

    }



    @Override
    public int getItemCount() {
        return messages.size();
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView message;

        public MyViewHolder(View itemView) {
            super(itemView);

            message =(TextView)itemView.findViewById(R.id._message);

        }
    }
}