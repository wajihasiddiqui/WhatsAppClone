package com.example.whatsappclone.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.R;
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

        users user = list.get(position);

        holder.username.setText(user.getUsername());
        holder.description.setText(user.getBio());
        Glide.with(context).load(user.getUrlprofile()).into(holder.imgprofile);

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
