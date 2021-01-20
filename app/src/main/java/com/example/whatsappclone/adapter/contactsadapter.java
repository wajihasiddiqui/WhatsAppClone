package com.example.whatsappclone.adapter;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.R;
import com.example.whatsappclone.chats.ChatsActivity;
import com.example.whatsappclone.model.users;
import java.util.List;

public class contactsadapter extends RecyclerView.Adapter<contactsadapter.Viewholder>{

    private List<users> list;
    private Context context;

    public contactsadapter(List<users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_contact_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        final users user = list.get(position);

        holder.username.setText(user.getUserName());
        holder.description.setText(user.getBio());
        Glide.with(context).load(user.getImageProfile()).into(holder.imgprofile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ChatsActivity.class)
                        .putExtra("userId", user.getUserId())
                        .putExtra("userName", user.getUserName())
                        .putExtra("imageProfile", user.getImageProfile()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView username,description;
        private ImageView imgprofile;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.tvusername);
            description = itemView.findViewById(R.id.tvdesc);
            imgprofile = itemView.findViewById(R.id.imgprofile);

        }
    }
}
