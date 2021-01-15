
package com.example.whatsappclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.R;
import com.example.whatsappclone.model.chat.chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class chatsadapter extends RecyclerView.Adapter<chatsadapter.viewholder> {

    private List<chat> list = new ArrayList<>();
    private Context context;
    public static final int msg_type_left = 0;
    public static final int msg_type_right = 1;
    private FirebaseUser firebaseUser;

    public chatsadapter(List<chat> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == msg_type_left) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_chat_left, parent, false);
            return new viewholder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.layout_chat_right, parent, false);
            return new viewholder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.bind(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        private TextView textmessage;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            textmessage = itemView.findViewById(R.id.text_message);
        }
        void bind(chat chat) {
            textmessage.setText(chat.getTextMessage());
        }
    }


    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(list.get(position).getSender().equals(firebaseUser.getUid())) {

            return  msg_type_right;
        }
        else{
            return msg_type_left;
        }
       
    }
}
